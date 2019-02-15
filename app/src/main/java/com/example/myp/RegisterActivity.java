package com.example.myp;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    //Declaration EditTexts
    EditText editTextUserName;
    EditText editTextEmail;
    EditText editTextPassword;
    EditText editTextConfirmPassword;
    EditText editTextFirstLastName;
    EditText editTextPhone;

    //Declaration TextInputLayout
    TextInputLayout textInputLayoutUserName;
    TextInputLayout textInputLayoutEmail;
    TextInputLayout textInputLayoutPassword;
    TextInputLayout textInputLayoutPasswordConfirmation;
    TextInputLayout textInputLayoutFirstLastName;
    TextInputLayout textInputLayoutPhone;

    //Declaration Button
    Button buttonRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initTextViewLogin();
        initViews();
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (attemptRegistration()) {
                    String UserName = editTextUserName.getText().toString();
                    String Email = editTextEmail.getText().toString();
                    String Password = editTextPassword.getText().toString();
                }
            }
        });
    }

    public boolean attemptRegistration() {
        //Validación variables
        boolean autenticacion = false;

        // Store values at the time of the registration attempt.
        String email            = editTextEmail.getText().toString();
        String password         = editTextPassword.getText().toString();
        String passwordconfirm  = editTextConfirmPassword.getText().toString();
        String name             = editTextUserName.getText().toString();
        String firstlastname    = editTextFirstLastName.getText().toString();
        String phone            = editTextPhone.getText().toString();

        //Validación de los campos requeridos (si cancel es true es porque un campo está vacío)
        boolean cancel = false;

        //Redirige al campo donde focusView sea diferente de null
        View focusView = null;

        // Require phone.
        if (TextUtils.isEmpty(phone)){
            editTextPhone.setError(getString(R.string.error_field_required));
            focusView = editTextPhone;
            cancel = true;
        } else editTextPhone.setError(null);

        // Require First Last Name.
        if (TextUtils.isEmpty(firstlastname)){
            editTextFirstLastName.setError(getString(R.string.error_field_required));
            focusView = editTextFirstLastName;
            cancel = true;
        } else editTextFirstLastName.setError(null);

        // Require name.
        if (TextUtils.isEmpty(name)){
            editTextUserName.setError(getString(R.string.error_field_required));
            focusView = editTextUserName;
            cancel = true;
        } else editTextUserName.setError(null);

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError(getString(R.string.error_field_required));
            focusView = editTextPassword;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            editTextPassword.setError(getString(R.string.error_invalid_password));
            focusView = editTextPassword;
            cancel = true;
        } else editTextPassword.setError(null);

        // Confirm password match
        if(!PasswordConfirmation(password, passwordconfirm)){
            editTextConfirmPassword.setError(getString(R.string.error_invalid_password_confirmation));
            focusView = editTextConfirmPassword;
            cancel = true;
        } else editTextConfirmPassword.setError(null);

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError(getString(R.string.error_field_required));
            focusView = editTextEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            editTextEmail.setError(getString(R.string.error_invalid_email));
            focusView = editTextEmail;
            cancel = true;
        } else editTextEmail.setError(null);


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            autenticacion = true;
        }

        return autenticacion;
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return (password.length() > 7);
    }

    private boolean PasswordConfirmation(String password, String passwordconfirm) {
        return (password.equals(passwordconfirm));
    }

    //this method used to set Login TextView click event
    private void initTextViewLogin() {
        TextView textViewLogin = (TextView) findViewById(R.id.textViewLogin);
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    //this method is used to connect XML views to its Objects
    private void initViews() {
        //editText
        editTextEmail               = (EditText) findViewById(R.id.email);
        editTextPassword            = (EditText) findViewById(R.id.password);
        editTextUserName            = (EditText) findViewById(R.id.editTextUserName);
        editTextConfirmPassword     = (EditText) findViewById(R.id.confirmpassword);
        editTextFirstLastName       = (EditText) findViewById(R.id.lastname1);
        editTextPhone               = (EditText) findViewById(R.id.phone);

        //textInputLayout
        textInputLayoutEmail                    = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword                 = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        textInputLayoutUserName                 = (TextInputLayout) findViewById(R.id.textInputLayoutUserName);
        textInputLayoutPasswordConfirmation     = (TextInputLayout) findViewById(R.id.textInputLayoutPasswordConfirmation);
        textInputLayoutFirstLastName            = (TextInputLayout) findViewById(R.id.textInputLayoutFirstLastName);
        textInputLayoutPhone                    = (TextInputLayout) findViewById(R.id.textInputLayoutPhone);

        //button
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
    }
}
