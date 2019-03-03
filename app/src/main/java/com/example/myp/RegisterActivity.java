package com.example.myp;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    //Declaracion KEYS
    private static final String EMAIL_KEY       = "usuario_Correo",
                                PASSWORD_KEY    = "usuario_Contrasena",
                                FULLNAME_KEY    = "usuario_NombreCompleto",
                                PHONE_KEY       = "usuario_Telefono",
                                GENDER_KEY      = "usuario_Genero",
                                UID_KEY         = "usuario_UID";

    //Declaration EditTexts
    EditText    editTextUserName,
                editTextEmail,
                editTextPassword,
                editTextConfirmPassword,
                editTextFirstLastName,
                editTextSecondLastName,
                editTextPhone;

    //Declaration TextInputLayout
    TextInputLayout textInputLayoutUserName,
                    textInputLayoutEmail,
                    textInputLayoutPassword,
                    textInputLayoutPasswordConfirmation,
                    textInputLayoutFirstLastName,
                    textInputLayoutSecondLastName,
                    textInputLayoutPhone;

    //Declaracion de arrays (strings)
    ArrayAdapter adapterGender;

    //Declaracion Spinner
    Spinner spinnerGenero;

    //Declaration Button
    Button buttonRegister;

    //Declaracion variable autenticacion
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    //Declaracion variable base de datos
    private FirebaseFirestore usuarioDB = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initTextViewLogin();
        initViews();

        // Set click event of Back to Login Link
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check if user input is correct or not
                if (attemptRegistration()) {
                    String Email            = editTextEmail.getText().toString();
                    String Password         = editTextPassword.getText().toString();
                    String UserName         = editTextUserName.getText().toString();
                    String FirstLastName    = editTextFirstLastName.getText().toString();
                    String SecondLastName   = editTextSecondLastName.getText().toString();
                    String Phone            = editTextPhone.getText().toString();
                    String Genero           = spinnerGenero.getSelectedItem().toString();

                    String FullName         = UserName + " " + FirstLastName + " " + SecondLastName;

                    crearAutenticacion(Email, Password);
                    insertarUsuario(Email, Password, FullName, Phone, Genero);
                    finish();
                }
            }
        });
    }

    public void crearAutenticacion(final String Email, final String Password){
            mAuth.createUserWithEmailAndPassword(Email, Password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                              showMessage("Registro de usuario exitoso.");
                              updateUserInfo(mAuth.getCurrentUser());

                            }
                }
            })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                                showMessage("Fallo en el registro de usuario.");
                        }
                    });
    }

    public void insertarUsuario(final String Email, final String Password, final String UserFullName, final String Phone, final String Genero) {
        Map<String, Object> user = new HashMap<>();
        user.put(EMAIL_KEY, Email);
        user.put(PASSWORD_KEY, Password);
        user.put(FULLNAME_KEY, UserFullName);
        user.put(PHONE_KEY, Phone);
        user.put(GENDER_KEY, Genero);

        usuarioDB.collection("usuario").document().set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        showMessage("Registro de usuario exitoso.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showMessage("RFallo en el registro de usuario.");
                    }
                });

    }

    private void updateUserInfo(FirebaseUser currentUser) {
        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().build();
        currentUser.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    showMessage("Registro completado.");
                    updateUI();
                }
            }
        });
    }

    private void updateUI() {

        Intent loginActivity = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(loginActivity);
        finish();


    }
    private void showMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
    public boolean attemptRegistration() {
        // Validación de variables
        boolean autenticacion = false;

        // Store values at the time of the registration attempt.
        String email            = editTextEmail.getText().toString();
        String password         = editTextPassword.getText().toString();
        String passwordconfirm  = editTextConfirmPassword.getText().toString();
        String name             = editTextUserName.getText().toString();
        String firstlastname    = editTextFirstLastName.getText().toString();
        String phone            = editTextPhone.getText().toString();

        // Validación de los campos requeridos (si cancel es true es porque un campo está vacío)
        boolean cancel = false;

        // Redirige al campo donde focusView sea diferente de null
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

    // This method used to set Login TextView click event
    private void initTextViewLogin() {
        TextView textViewLogin = findViewById(R.id.textViewLogin);
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    // This method is used to connect XML views to its Objects
    private void initViews() {
        //editText
        editTextEmail               = (EditText) findViewById(R.id.email);
        editTextPassword            = (EditText) findViewById(R.id.password);
        editTextUserName            = (EditText) findViewById(R.id.editTextUserName);
        editTextConfirmPassword     = (EditText) findViewById(R.id.confirmpassword);
        editTextFirstLastName       = (EditText) findViewById(R.id.lastname1);
        editTextSecondLastName      = (EditText) findViewById(R.id.lastname2);
        editTextPhone               = (EditText) findViewById(R.id.phone);

        spinnerGenero = (Spinner) findViewById(R.id.GenderSpinner);

        //textInputLayout
        textInputLayoutEmail                    = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword                 = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        textInputLayoutUserName                 = (TextInputLayout) findViewById(R.id.textInputLayoutUserName);
        textInputLayoutPasswordConfirmation     = (TextInputLayout) findViewById(R.id.textInputLayoutPasswordConfirmation);
        textInputLayoutFirstLastName            = (TextInputLayout) findViewById(R.id.textInputLayoutFirstLastName);
        textInputLayoutSecondLastName           = (TextInputLayout) findViewById(R.id.textInputLayoutSecondLastName);
        textInputLayoutPhone                    = (TextInputLayout) findViewById(R.id.textInputLayoutPhone);
      
        //button
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
    }
}
