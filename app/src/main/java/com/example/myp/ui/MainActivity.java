package com.example.myp.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

// Login
public class MainActivity extends AppCompatActivity {
    // EditTexts Declaration
    EditText editTextEmail;
    EditText editTextPassword;

    // TextInputLayouts Declaration
    TextInputLayout textInputLayoutEmail;
    TextInputLayout textInputLayoutPassword;

    // Button Declaration
    Button buttonLogin;

    // Authentication Variable Declaration
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initCreateAccountTextView();
        initViews();

        // Hide Keyboard
        hideSoftKeyboard();

        // ActionListener to accept Enter input on keyboard
        // (from an editText XML element) as a press on the login button
        editTextPassword.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    buttonLogin.performClick();
                    return true;
                }
                return false;
            }
        });

        // Set click event of login button
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Disable the login button to avoid extra taps
                buttonLogin.setClickable(false);

                // Check if user input is correct or not
                if (validate()) {
                    hideSoftKeyboard();
                    String Email = editTextEmail.getText().toString();
                    String Password = editTextPassword.getText().toString();

                    // Sign in and navigate to the home screen
                    signIn(Email, Password);
                }
                else {
                    // Enable login button to tap again once the login process has ended.
                    buttonLogin.setClickable(true);
                }
            }
        });
    }

    // Function to sign in (validated through firebase)
    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    // Sign is was successful; print congrats message
                    showMessage("Has iniciado sesión exitosamente.");

                    // Change to home screen
                    Intent intent = new Intent(MainActivity.this, ClassroomsListActivity.class);
                    startActivity(intent);

                    // Terminate screen to avoid returning without first logging out.
                    finish();
                }
                else{
                    // Sign in failed. Print failure message.
                    showMessage("No existe la cuenta ingresada.");

                    // Enable the login button to try signing in again.
                    buttonLogin.setClickable(true);
                }
            }
        });
    }

    // Function to set Toast messages easily in order to print success/failure alerts.
    private void showMessage(String text) {
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();
    }

    // This method is used to set Create account TextView text and click event (multiple
    // colors for TextView aren't supported in XML yet so it's been done programmatically)
    private void initCreateAccountTextView() {
        TextView textViewCreateAccount = findViewById(R.id.textViewCreateAccount);
        textViewCreateAccount.setText(fromHtml("<font color='#000'>¿No tienes cuenta? </font><font color='#6dbaf8'>Registrate</font>"));
        textViewCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to registration screen
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
                // The screen isn't closed with finish() so you can return
            }
        });
    }

    // This method is used to connect XML views to its Objects
    private void initViews() {
        editTextEmail =             findViewById(R.id.email);
        editTextPassword =          findViewById(R.id.password);
        textInputLayoutEmail =      findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword =   findViewById(R.id.textInputLayoutPassword);
        buttonLogin =               findViewById(R.id.buttonLogin);
    }

    // This method is used to handle Html method deprecation
    public static Spanned fromHtml(String html) {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    // This method is used to validate input given by user (through front-end)
    public boolean validate() {
        // Variable to validate/cancel this function
        boolean validation = false;

        // Store values at the time of the registration attempt
        String Email = editTextEmail.getText().toString();
        String Password = editTextPassword.getText().toString();

        // Variable to validate every field (if cancel is true, it means a field is empty)
        boolean cancel = false;

        // This sends your view to the first empty field from the top.
        View focusView = null;

        // Link textInputLayouts to their XML objects
        textInputLayoutEmail    = findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = findViewById(R.id.textInputLayoutPassword);

        // Handle validation for Password field
        if (TextUtils.isEmpty(Password)) {
            editTextPassword.setError(getString(R.string.error_field_required));
            focusView = editTextPassword;
            cancel = true;
        } else if (!isPasswordValid(Password)) {
            editTextPassword.setError(getString(R.string.error_invalid_password));
            focusView = editTextPassword;
            cancel = true;
        } else editTextPassword.setError(null);

        // Handle validation for Email field
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
            // There was an error; login won't be attempted and you'll be redirected
            // to the first form incorrectly filled.
            focusView.requestFocus();
        } else {
            // Every form was correctly filled and the front-end authentication is finished.
            validation = true;
        }
        return validation;
    }

    // Front end authentication for 8 character passwords
    private boolean isPasswordValid(String password) {
        return (password.length() > 7);
    }

    // Front end authentication if email has an @ and a .
    private boolean isEmailValid(String email) {
        return (email.contains("@") && email.contains("."));
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