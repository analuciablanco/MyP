package com.example.myp;

import android.media.SoundPool;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.text.Html;
import android.text.Spanned;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    //Declaration EditTexts
    EditText editTextEmail;
    EditText editTextPassword;

    // Declaration TextInputLayout
    TextInputLayout textInputLayoutEmail;
    TextInputLayout textInputLayoutPassword;

    // Declaration Button
    Button buttonLogin;

    //Declaracion  variable autenticacion
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initCreateAccountTextView();
        initViews();

        // Set click event of login button
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if user input is correct or not
                if (validate()) {
                    String Email = editTextEmail.getText().toString();
                    String Password = editTextPassword.getText().toString();

                    signIn(Email, Password);
                }
            }
        });
    }

    private void signIn(String email, String password) {

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    updateUI();
                    Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                }
                else{
                    showMessage("No existe la cuenta ingresada.");
                }
            }
        });
    }


    // this method is used to set Create account TextView text and click event( multiple colors
    private void updateUI() {
        showMessage("Has iniciado sesion exitosamente.");
    }

    private void showMessage(String text) {

        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();
    }

    //this method used to set Create account TextView text and click event( maltipal colors
    // for TextView yet not supported in Xml so i have done it programmatically)
    private void initCreateAccountTextView() {

        TextView textViewCreateAccount = (TextView) findViewById(R.id.textViewCreateAccount);
        // El que hizo esto es un Dios
        textViewCreateAccount.setText(fromHtml("<font color='#000'>¿No tienes cuenta? </font><font color='#6dbaf8'>Registrate</font>"));
        textViewCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    // This method is used to connect XML views to its Objects
    private void initViews() {
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
    }

    // This method is for handling fromHtml method deprecation
    public static Spanned fromHtml(String html) {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    // This method is used to validate input given by user
    public boolean validate() {
        // Validación de variables
        boolean validation = false;

        // Store values at the time of the registration attempt
        String Email = editTextEmail.getText().toString();
        String Password = editTextPassword.getText().toString();

        // Validación de los campos requeridos (si cancel es true es porque un campo está vacío)
        boolean cancel = false;

        // Redirige al campo donde focusView sea diferente de null
        View focusView = null;

        // TextInputLayout
        textInputLayoutEmail    = findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = findViewById(R.id.textInputLayoutPassword);

        //Handling validation for Password field
        if (TextUtils.isEmpty(Password)) {
            editTextPassword.setError(getString(R.string.error_field_required));
            focusView = editTextPassword;
            cancel = true;
        } else if (!isPasswordValid(Password)) {
            editTextPassword.setError(getString(R.string.error_invalid_password));
            focusView = editTextPassword;
            cancel = true;
        } else editTextPassword.setError(null);

        // Handling validation for Email field
        if (Email.isEmpty()) {
            cancel = true;
            focusView = editTextEmail;
            editTextEmail.setError(getString(R.string.error_field_required));
        } else if (!isEmailValid(Email)) {
            cancel = true;
            focusView = editTextEmail;
            editTextEmail.setError(getString(R.string.error_invalid_email));
        } else {
            editTextEmail.setError(null);
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            validation = true;
        }
        return validation;
    }

    private boolean isPasswordValid(String password) {
        return (password.length() > 7);
    }

    private boolean isEmailValid(String email) {
        return (email.contains("@") && email.contains("."));
    }
}