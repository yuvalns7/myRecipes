package com.example.myrecipes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myrecipes.model.user.User;
import com.example.myrecipes.model.user.UserModel;

public class RegisterActivity extends AppCompatActivity {

    TextView alreadyHaveAccount;
    EditText inputEmail, inputFullName, inputUserName, inputPassword, inputConfirmPassword;
    Button btnRegister;
    String emailPattern= "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        alreadyHaveAccount = findViewById(R.id.alreadyHaveAccount);
        inputEmail = findViewById(R.id.inputEmail);
        inputFullName = findViewById(R.id.inputFullName);
        inputPassword = findViewById(R.id.inputPassword);
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        progressDialog = new ProgressDialog(this);

        alreadyHaveAccount.setOnClickListener(view ->
                startActivity(new Intent(RegisterActivity.this, MainActivity.class)));

        btnRegister.setOnClickListener(view -> PerformAuth());
    }

    private void PerformAuth() {
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        String fullName = inputFullName.getText().toString();
        String confirmPassword = inputConfirmPassword.getText().toString();

        if(!email.matches(emailPattern)) {
            inputEmail.setError("Enter correct email");
            inputEmail.requestFocus();
        } else if (password.isEmpty() || password.length()<6) {
            inputPassword.setError("Enter proper password");
            inputPassword.requestFocus();
        } else if (fullName.isEmpty()) {
            inputFullName.setError("Enter full name");
            inputFullName.requestFocus();
        } else if (!password.equals(confirmPassword)) {
            inputConfirmPassword.setError("Password not match both fields");
            inputConfirmPassword.requestFocus();
        } else {
            progressDialog.setMessage("Please wait while registration...");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            registerUser(new User(email, password, fullName));
        }
    }

    private void registerUser(User user) {
        UserModel.instance().registerUser(user, (task) -> {
            if (task.isSuccessful()) {
                progressDialog.dismiss();
                sendUserToNextActivity();
                Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
            } else {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendUserToNextActivity() {
        Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}