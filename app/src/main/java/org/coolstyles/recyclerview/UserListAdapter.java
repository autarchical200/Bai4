package org.coolstyles.recyclerview;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView; // Import RecyclerView

import org.coolstyles.recyclerview.R;
import org.coolstyles.recyclerview.User;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    private List<User> userList;
    private Context context;

    public UserListAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);

        holder.textViewEmail.setText("Email: " + user.getEmail());
        holder.textViewPassword.setText("Password: " + user.getPassword());
        holder.textViewRole.setText("Role: " + user.getRole());

        // Set onClickListeners for update and delete buttons
        holder.buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (updateClickListener != null) {
                    updateClickListener.onUpdateClick(user);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewEmail;
        TextView textViewPassword;
        TextView textViewRole;
        Button buttonView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewEmail = itemView.findViewById(R.id.textViewEmail);
            textViewPassword = itemView.findViewById(R.id.textViewPassword);
            textViewRole = itemView.findViewById(R.id.textViewRole);
            buttonView = itemView.findViewById(R.id.buttonView);
        }
    }

    // Interface for click events
    public interface UpdateClickListener {
        void onUpdateClick(User user);
    }

    public interface DeleteClickListener {
        void onDeleteClick(User user);
    }

    private UpdateClickListener updateClickListener;
    private DeleteClickListener deleteClickListener;

    // Setters for listeners
    public void setUpdateClickListener(UpdateClickListener listener) {
        this.updateClickListener = listener;
    }

    public void setDeleteClickListener(DeleteClickListener listener) {
        this.deleteClickListener = listener;
    }
    public void setData(List<User> newList) {
        userList.clear();
        userList.addAll(newList);
        notifyDataSetChanged();
    }
}
