package com.example.myp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

// Actividad para registrarse como usuario
// NOTA: Falta agregar un gif para la espera del proceso de autenticación
public class RegisterActivity extends AppCompatActivity {

    // FireBase KEYS' Initialization
    private static final String EMAIL_KEY       = "usuario_Correo",
                                PASSWORD_KEY    = "usuario_Contrasena",
                                FULLNAME_KEY    = "usuario_NombreCompleto",
                                PHONE_KEY       = "usuario_Telefono",
                                GENDER_KEY      = "usuario_Genero",
                                UID_KEY         = "usuario_UID";

    // EditTexts Declaration
    EditText    editTextUserName,
                editTextEmail,
                editTextPassword,
                editTextConfirmPassword,
                editTextFirstLastName,
                editTextSecondLastName,
                editTextPhone;

    // TextInputLayout Declaration
    TextInputLayout textInputLayoutUserName,
                    textInputLayoutEmail,
                    textInputLayoutPassword,
                    textInputLayoutPasswordConfirmation,
                    textInputLayoutFirstLastName,
                    textInputLayoutSecondLastName,
                    textInputLayoutPhone;

    // Spinner Declaration
    Spinner spinnerGenero;

    //Declaration Button
    Button buttonRegister;

    // FireBase Authentication variable initialized
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    // FireBase Instance Variable Declaration
    private FirebaseFirestore usuarioDB = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Link variables to their XML elements
        initTextViewLogin();
        initViews();

        // Set click event of Back to Login Link
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Disable the button to avoid multiple registration attempts
                buttonRegister.setClickable(false);

                // Check if user input is correct or not
                if (attemptRegistration()) {
                    String Email = editTextEmail.getText().toString();
                    String Password = editTextPassword.getText().toString();
                    String UserName = editTextUserName.getText().toString();
                    String FirstLastName = editTextFirstLastName.getText().toString();
                    String SecondLastName = editTextSecondLastName.getText().toString();
                    String Phone = editTextPhone.getText().toString();
                    String Gender = spinnerGenero.getSelectedItem().toString();

                    String FullName = UserName + " " + FirstLastName + " " + SecondLastName;

                    // Function to create the user in FireBase FireStore and exit layout
                    createUser(Email, Password, FullName, Phone, Gender);
                }
                else {
                    // Enable the button to try registration again
                    buttonRegister.setClickable(true);
                }
            }
        });
    }

    // Function to create user account in FireStore FireBase
    public void createUser(final String Email, final String Password, final String UserFullName, final String Phone, final String Genero) {
        mAuth.createUserWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User();
                            user.setEmail(Email);
                            user.setPassword(Password);
                            user.setUserFullName(UserFullName);
                            user.setPhone(Phone);
                            user.setGender(Genero);
                            user.setUserID(mAuth.getUid());

                            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                                    .setTimestampsInSnapshotsEnabled(true)
                                    .build();
                            usuarioDB.setFirestoreSettings(settings);

                            DocumentReference newUserRef = usuarioDB
                                    .collection(getString(R.string.collection_users))
                                    .document(FirebaseAuth.getInstance().getUid());

                            newUserRef.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            showMessage("Registro de usuario exitoso.");
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            showMessage("Fallo en el registro de usuario.");

                                            // The button is enabled to try registration again
                                            buttonRegister.setClickable(true);
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showMessage("Fallo en el registro de usuario.");

                        // The button is enabled to try registration again
                        buttonRegister.setClickable(true);
                    }
                });
    }

    // Function to set Toast messages more easily
    private void showMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    // Function to validate (through front end) the registration's fields
    public boolean attemptRegistration() {
        // Variable to validate/cancel this function
        boolean authentication = false;

        // Store values at the time of the registration attempt.
        String email            = editTextEmail.getText().toString();
        String password         = editTextPassword.getText().toString();
        String password_confirm  = editTextConfirmPassword.getText().toString();
        String name             = editTextUserName.getText().toString();
        String first_last_name    = editTextFirstLastName.getText().toString();
        String phone            = editTextPhone.getText().toString();

        // Variable to validate every field (if cancel is true, it means a field is empty)
        boolean cancel = false;

        // This sends your screen to the first empty field from the top.
        View focusView = null;

        // Require phone.
        if (TextUtils.isEmpty(phone)){
            editTextPhone.setError(getString(R.string.error_field_required));
            focusView = editTextPhone;
            cancel = true;
        } else editTextPhone.setError(null);

        // Require First Last Name.
        if (TextUtils.isEmpty(first_last_name)){
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
        if(!PasswordConfirmation(password, password_confirm)){
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

        // Check if every field was correctly filled
        if (cancel) {
            // There was an error; login won't be attempted and you'll be redirected
            // to the first form incorrectly filled.
            focusView.requestFocus();
        } else {
            // Since focusView = null redirects to wherever your view is currently
            // focused, we make focusView = editTextPhone since it's close to the button.
            focusView = editTextPhone;
            focusView.requestFocus();

            // Hide KeyBoard
            hideSoftKeyboard();

            // Every form was correctly filled and the front-end authentication is finished.
            authentication = true;
        }
        return authentication;
    }

    // Front end authentication if email has an @ and a .
    private boolean isEmailValid(String email) {
        return (email.contains("@") && email.contains("."));
    }

    // Front end authentication for 8 character passwords
    private boolean isPasswordValid(String password) {
        return (password.length() > 7);
    }

    // Front end validation for correct password confirmation
    private boolean PasswordConfirmation(String password, String passwordconfirm) {
        return (password.equals(passwordconfirm));
    }

    // This method used to set the Login TextView click event
    private void initTextViewLogin() {
        TextView textViewLogin = findViewById(R.id.textViewLogin);
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    // This method is used to connect XML views to their Objects
    private void initViews() {
        // EditText
        editTextEmail               = findViewById(R.id.email);
        editTextPassword            = findViewById(R.id.password);
        editTextUserName            = findViewById(R.id.editTextUserName);
        editTextConfirmPassword     = findViewById(R.id.confirmpassword);
        editTextFirstLastName       = findViewById(R.id.lastname1);
        editTextSecondLastName      = findViewById(R.id.lastname2);
        editTextPhone               = findViewById(R.id.phone);

        spinnerGenero = findViewById(R.id.GenderSpinner);

        // TextInputLayout
        textInputLayoutEmail                    = findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword                 = findViewById(R.id.textInputLayoutPassword);
        textInputLayoutUserName                 = findViewById(R.id.textInputLayoutUserName);
        textInputLayoutPasswordConfirmation     = findViewById(R.id.textInputLayoutPasswordConfirmation);
        textInputLayoutFirstLastName            = findViewById(R.id.textInputLayoutFirstLastName);
        textInputLayoutSecondLastName           = findViewById(R.id.textInputLayoutSecondLastName);
        textInputLayoutPhone                    = findViewById(R.id.textInputLayoutPhone);
      
        // Button
        buttonRegister = findViewById(R.id.buttonRegister);
    }

    // Function to (supposedly) hide the keyboard upon use.
    private void hideSoftKeyboard(){
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }
}
