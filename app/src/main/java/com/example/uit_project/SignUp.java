package com.example.uit_project;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import android.widget.RelativeLayout;
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
            et_email.setError(getString(R.string.email_warning));
        }else {
            et_email.setError(null);
        }

        // Validate password
        if (password.isEmpty() || !(password.matches(passwordRegex))) {
            isValid = false;
            et_password.setError(getString(R.string.invalidPass));
        } else {
            et_password.setError(null);
        }

        // Validate password confirm
        if (rePassword.isEmpty() || !password.equals(rePassword)) {
            isValid = false;
            et_rePassword.setError(getString(R.string.password_warning));
        }

        return isValid;
    }

//    @SuppressLint("SetJavaScriptEnabled")
    private void getToken(String username, String email, String password, String rePassword) {
        // Get sign up token
        CookieManager.getInstance().removeAllCookies(null);

        webView = new WebView(getBaseContext());
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d(GlobalVar.LOG_TAG, "onPageFinished: ");
                if (url.contains("auth")) {
                    // Url is now in sign in page
                    Log.d(GlobalVar.LOG_TAG, "Enter login");
                    String redirect = "document.getElementsByTagName('a')[0].click();";
                    view.evaluateJavascript(redirect, null);
                }
                if (url.contains("regi")) { // Url is now in sign up page
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
                if (url.contains("manager/#")) { // Sign up success, open log in
                    Log.d(GlobalVar.LOG_TAG, "Success!");
                    signUpLog(getString(R.string.signup_success));
                    openLogin();
                }

                String cookies = CookieManager.getInstance().getCookie(url);
                Log.d(GlobalVar.LOG_TAG, "return cookie: " + cookies);
                Log.d(GlobalVar.LOG_TAG, "url: " + url);
                super.onPageFinished(view,url);
            }
        });
        webView.loadUrl(GlobalVar.baseUrl + "manager");
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
