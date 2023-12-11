package com.example.uit_project;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SignUp extends AppCompatActivity {
    private EditText input_username;
    private EditText input_email;
    private EditText input_pass;
    private EditText input_rePass;
    private ImageButton btn_back;
    private Button btn_signUp;

    private LoadingAlert loadingAlert;
    private TextView login;
    private String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,32}$";
    @SuppressLint({"SourceLockedOrientationActivity", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //Lock orientation
        GlobalVar.view = (RelativeLayout) findViewById(R.id.signup_src);
        GlobalVar.view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(v);
                return false;
            }
        });

        InitView();
        InitEvent();
    }
    boolean isEmail(EditText text) {        // Check email format
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    private void InitView() {
        // Initial all views
        btn_back = findViewById(R.id.ic_back_signup);
        login = findViewById(R.id.login_suggest);
        btn_signUp = findViewById(R.id.btn_signup);
        input_username = findViewById(R.id.input_username_signup);
        input_email = findViewById(R.id.input_email_signup);
        input_pass = findViewById(R.id.input_pass_signup);
        input_rePass = findViewById(R.id.input_retypepass_signup);

        loadingAlert = new LoadingAlert(SignUp.this);
    }

    private void InitEvent() {
        // Initial all event
        btn_back.setOnClickListener(view -> { // Back button
            finish();
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(SignUp.this, Login.class);
                startActivity(i);
            }
        });


        btn_signUp.setOnClickListener(view -> { // Sign up button
            // Validate user form, open sign up method
            String username = input_username.getText().toString(); // Extract username
            String email = input_email.getText().toString(); // Extract email
            String password = input_pass.getText().toString(); // Extract password
            String rePassword = input_rePass.getText().toString(); // Extract rePassword
            boolean isValidInformation = validateForm(username, email, password, rePassword);

            if (isValidInformation) { // If information is valid
                loadingAlert.startAlertDialog();
                getSignUp(username, email, password, rePassword);
            }
        });
    }

    private boolean validateForm(String username, String email, String password, String rePassword) {
        // Validate user input
        boolean isValid = true;

        // Validate username
        if (username.isEmpty()) {
            isValid = false;
            input_username.setError(getString(R.string.form_warning));
        }else {
            input_username.setError(null);
        }

        // Validate email
        if (email.isEmpty() || !isEmail(input_email)) {
            isValid = false;
            input_email.setError(getString(R.string.email_warning));
        }else {
            input_email.setError(null);
        }

        // Validate password
        if (password.isEmpty() || !(password.matches(passwordRegex))) {
            isValid = false;
            input_pass.setError(getString(R.string.invalidPass));
        } else {
            input_pass.setError(null);
        }

        // Validate password confirm
        if (rePassword.isEmpty() || !password.equals(rePassword)) {
            isValid = false;
            input_rePass.setError(getString(R.string.password_warning));
        }

        return isValid;
    }

    private void getSignUp(String username, String email, String password, String rePassword) {
        CookieManager.getInstance().removeAllCookies(null);

        WebView webView = new WebView(SignUp.this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {

                if (url.contains("openid-connect/auth")) { // Url is now in sign in page
                    String redirect = "document.getElementsByTagName('a')[0].click();"; // Click on sign up button
                    view.evaluateJavascript(redirect, null);
                }
                else if (url.contains("login-actions/registration")) { // Url is now in sign up page
                    Log.d(GlobalVar.LOG_TAG, "Enter registration");
                    String dataError = "document.getElementsByClassName('helper-text')[0].getAttribute('data-error');"; // Appear when email is exist
                    String redText = "document.getElementsByClassName('red-text')[1].textContent;"; // Appear when username already exist.

                    view.evaluateJavascript(dataError, dErr-> {
                        if (dErr.equals("null")) { // No error in form
                            view.evaluateJavascript(redText, red -> {
                                if (red.equals("null")) {
                                    String usrScript = "document.getElementById('username').value='" + username + "';";
                                    String emailScript = "document.getElementById('email').value='" + email + "';";
                                    String pwdScript = "document.getElementById('password').value='" + password + "';";
                                    String rePwdScript = "document.getElementById('password-confirm').value='" + rePassword + "';";

                                    view.evaluateJavascript(usrScript, null);
                                    view.evaluateJavascript(emailScript, null);
                                    view.evaluateJavascript(pwdScript, null);
                                    view.evaluateJavascript(rePwdScript, null);
                                    view.evaluateJavascript("document.getElementsByTagName('form')[0].submit();", null); // Submit form
                                }
                                else {
                                    signUpLog(red);
                                }
                            });
                        }
                        else { //
                            Log.d(GlobalVar.LOG_TAG, "error: " + dErr);
                            signUpLog(dErr);
                        }
                    });
                }
                else if (url.contains("manager/#state=")) {
                    Toast.makeText(SignUp.this, getString(R.string.signup_success), Toast.LENGTH_SHORT).show();
                    Log.d(GlobalVar.LOG_TAG, getString(R.string.signup_success));
                    Intent intent = new Intent();
                    intent.setClass(SignUp.this, Login.class);
                    startActivity(intent);
                }
                super.onPageFinished(view,url);
            }
        });
        webView.loadUrl("https://uiot.ixxc.dev/manager/");
    }
    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void openLogin() {
        Intent i = new Intent();
        i.setClass(SignUp.this, Login.class);
        startActivity(i);
    }
    private void signUpLog(String msg) {
        Toast.makeText(SignUp.this, msg, Toast.LENGTH_SHORT).show();
    }
}
