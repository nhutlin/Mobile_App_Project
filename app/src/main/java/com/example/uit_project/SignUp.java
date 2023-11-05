package com.example.uit_project;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.uit_project.LoadingAlert;

import androidx.appcompat.app.AppCompatActivity;

public class SignUp extends AppCompatActivity {
    private EditText et_username;
    private EditText et_email;
    private EditText et_password;
    private EditText et_rePassword;
    private ImageButton btn_back;
    private Button btn_signUp;

    private WebView webView;
    private LoadingAlert loadingAlert;
    private TextView login;
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //Lock orientation

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
        login = findViewById(R.id.label_login);
        btn_signUp = findViewById(R.id.btn_signup);
        //btn_changeLanguage = findViewById(R.id.btn_changeLanguage);
        et_username = findViewById(R.id.input_username_signup); // Get username Edit Text
        et_email = findViewById(R.id.input_email_signup); // Get email Edit Text
        et_password = findViewById(R.id.input_pass_signup); // Get password Edit Text
        et_rePassword = findViewById(R.id.input_retypepass_signup); //Get rePassword Edit Text
        webView = findViewById(R.id.wv_browser);
        loadingAlert = new LoadingAlert(SignUp.this);
    }

    private void InitEvent() {
        // Initial all event
        btn_back.setOnClickListener(view -> { // Back button
            // Open main activity
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
            String username = et_username.getText().toString(); // Extract username
            String email = et_email.getText().toString(); // Extract email
            String password = et_password.getText().toString(); // Extract password
            String rePassword = et_rePassword.getText().toString(); // Extract rePassword
            boolean isValidInformation = validateForm(username, email, password, rePassword);

            if (isValidInformation) { // If information is valid
                loadingAlert.startAlertDialog();
                getToken(username, email, password, rePassword);
            }
        });

//        btn_changeLanguage.setOnClickListener(view -> { // Change language button
//            // Open change language method
//            loadingAlert.startAlertDialog();
//
//            new Handler().postDelayed(() -> {
//                loadingAlert.closeAlertDialog();
//                onLanguageChange();
//            },1000);
//        });
    }

    private boolean validateForm(String username, String email, String password, String rePassword) {
        // Validate user input
        boolean isValid = true;

        // Validate username
        if (username.isEmpty()) {
            isValid = false;
            et_username.setError(getString(R.string.form_warning));
        }else {
            et_username.setError(null);
        }

        // Validate email
        if (email.isEmpty() || !isEmail(et_email)) {
            isValid = false;
            et_email.setError(getString(R.string.email_invalid));
        }else {
            et_email.setError(null);
        }

        // Validate password
        if (password.isEmpty()) {
            isValid = false;
            et_password.setError(getString(R.string.form_warning));
        } else {
            et_password.setError(null);
        }

        // Validate password confirm
        if (rePassword.isEmpty() || !password.equals(rePassword)) {
            isValid = false;
            et_rePassword.setError(getString(R.string.password_warning));
        }

        return isValid; // User information is valid
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void getToken(String username, String email, String password, String rePassword) {
        // Get sign up token
        CookieManager.getInstance().removeAllCookies(null);

        //webView.setVisibility(View.VISIBLE);
        webView = new WebView(getBaseContext());
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d(GlobalVar.LOG_TAG, "onPageFinished: ");
                if (url.contains("auth")) {
                    // Url is now in sign in page
                    Log.d(GlobalVar.LOG_TAG, "Enter login");
                    String redirect = "document.getElementsByTagName('a')[0].click();"; // Click on sign up button
                    view.evaluateJavascript(redirect, null);
                }
                if (url.contains("login-actions/registration")) { // Url is now in sign up page
                    Log.d(GlobalVar.LOG_TAG, "Enter registration");
                    String dataError = "document.getElementsByClassName('helper-text')[0].getAttribute('data-error');"; // Appear when email is exist

                    view.evaluateJavascript(dataError, dErr-> {
                        if (dErr.equals("null")) { // dErr = "Email already exist."
                            String usrScript = "document.getElementById('username').value='" + username + "';";
                            String emailScript = "document.getElementById('email').value='" + email + "';";
                            String pwdScript = "document.getElementById('password').value='" + password + "';";
                            String rePwdScript = "document.getElementById('password-confirm').value='" + rePassword + "';";

                            view.evaluateJavascript(usrScript, null);
                            view.evaluateJavascript(emailScript, null);
                            view.evaluateJavascript(pwdScript, null);
                            view.evaluateJavascript(rePwdScript, null);
                            view.evaluateJavascript("document.getElementsByTagName('form')[0].submit();", null); // Submit form
                            loadingAlert.closeAlertDialog();
                        }
                        else { //
                            Log.d(GlobalVar.LOG_TAG, "error: " + dErr);
                            signUpLog(dErr);
                        }
                    });
                }
                if (url.contains("manager")) { // Sign up success, open log in
                    Log.d(GlobalVar.LOG_TAG, getString(R.string.success_warning));
                    signUpLog(getString(R.string.success_warning));
                   // openLogInActivity(); // Open login
                    finish();
                }

                String cookies = CookieManager.getInstance().getCookie(url);
                Log.d(GlobalVar.LOG_TAG, "return cookie: " + cookies);
                Log.d(GlobalVar.LOG_TAG, "url: " + url);
                super.onPageFinished(view,url);
            }
        });
        webView.loadUrl(GlobalVar.baseUrl + "manager");
    }

    private void signUpLog(String msg) {
        Toast.makeText(SignUp.this, msg, Toast.LENGTH_SHORT).show();
    }
}
