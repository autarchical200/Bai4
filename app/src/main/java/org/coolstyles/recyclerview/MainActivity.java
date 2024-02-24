package org.coolstyles.recyclerview;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword, editTextRole;
    private Button buttonAdd;
    private RecyclerView recyclerViewUsers;
    private DatabaseHandler dbHandler;
    private UserListAdapter userListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHandler = new DatabaseHandler(this);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextRole = findViewById(R.id.editTextRole);
        buttonAdd = findViewById(R.id.buttonAdd);
        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);

        // Set up RecyclerView
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));
        userListAdapter = new UserListAdapter(this, dbHandler.getAllUsers());
        recyclerViewUsers.setAdapter(userListAdapter);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUser();
            }
        });

        userListAdapter.setUpdateClickListener(new UserListAdapter.UpdateClickListener() {
            @Override
            public void onUpdateClick(User user) {
                showUpdateDialog(user);
            }
        });
        userListAdapter.setDeleteClickListener(new UserListAdapter.DeleteClickListener() {
            @Override
            public void onDeleteClick(User user) {
                deleteUser(user.getId());
            }
        });
    }

    private void showUpdateDialog(final User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update User");

        View view = getLayoutInflater().inflate(R.layout.dialog_view_user, null);
        final EditText updateEmail = view.findViewById(R.id.editTextUpdateEmail);
        final EditText updatePassword = view.findViewById(R.id.editTextUpdatePassword);
        final EditText updateRole = view.findViewById(R.id.editTextUpdateRole);

        updateEmail.setText(user.getEmail());
        updatePassword.setText(user.getPassword());
        updateRole.setText(user.getRole());

        builder.setView(view);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String updatedEmail = updateEmail.getText().toString();
                String updatedPassword = updatePassword.getText().toString();
                String updatedRole = updateRole.getText().toString();

                User updatedUser = new User(user.getId(), updatedEmail, updatedPassword, updatedRole);
                updateUser(updatedUser);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Do nothing, just close the dialog
            }
        });

        builder.show();
    }

    private void addUser() {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        String role = editTextRole.getText().toString();

        User newUser = new User(email, password, role);
        dbHandler.addUser(newUser);

        refreshUserList();
        clearEditTexts();
    }

    private void updateUser(User user) {
        dbHandler.updateUser(user);
        refreshUserList();
        Toast.makeText(MainActivity.this, "User updated successfully", Toast.LENGTH_SHORT).show();
    }

    private void refreshUserList() {
        userListAdapter.setData(dbHandler.getAllUsers());
        userListAdapter.notifyDataSetChanged();
    }

    private void deleteUser(long userId) {
        dbHandler.deleteUser(userId);
        refreshUserList();
    }

    private void clearEditTexts() {
        editTextEmail.setText("");
        editTextPassword.setText("");
        editTextRole.setText("");
    }
}
