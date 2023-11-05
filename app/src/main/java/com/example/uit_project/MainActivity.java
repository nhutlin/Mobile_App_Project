package com.example.uit_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.AppCompatButton;
public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView signup;

    private Button login_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       login_main = findViewById(R.id.login_main_btn);
       login_main.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent i = new Intent();
               i.setClass(MainActivity.this, Login.class);
               startActivity(i);
           }
       });

        signup = findViewById(R.id.signup_main);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(MainActivity.this, SignUp.class);

                startActivity(i);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.ic_vnese) {

        }
        return super.onOptionsItemSelected(item);
    }
}
//package com.example.uit_project;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.webkit.CookieManager;
//import android.webkit.WebChromeClient;
//import android.webkit.WebResourceRequest;
//import android.webkit.WebSettings;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//import com.example.uit_project.LoadingAlert;
//
//public class SignUp extends AppCompatActivity {
//    private ImageButton back_btn;
//
//    private ProgressBar progressBar;
//    private WebView webView;
//
//    private TextView goToLogin;
//    private EditText et_username;
//    private EditText et_password;
//    private EditText et_rePassword;
//    private EditText et_email;
//
//    private Button signup;
//    private LoadingAlert loadingAlert;
//    @SuppressLint("SourceLockedOrientationActivity")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sign_up);
//
//
//        InitView();
//        InitEvent();
//    }
////    boolean isEmail(EditText text) {        // Check email format
////        CharSequence email = text.getText().toString();
////        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
////    }
//
//    private void InitView() {
//        // Initial all views
//        back_btn = findViewById(R.id.ic_back_signup);
//        signup = findViewById(R.id.btn_signup);
//        //btn_changeLanguage = findViewById(R.id.btn_changeLanguage);
//        et_username = findViewById(R.id.input_username_signup); // Get username Edit Text
//        et_email = findViewById(R.id.input_email_signup); // Get email Edit Text
//        et_password = findViewById(R.id.input_pass_signup); // Get password Edit Text
//        et_rePassword = findViewById(R.id.input_retypepass_signup); //Get rePassword Edit Text
//         webView = findViewById(R.id.wv_browser);
//        loadingAlert = new LoadingAlert(SignUp.this);
//    }
//
//    private void InitEvent() {
//        // Initial all event
//        back_btn.setOnClickListener(view -> { // Back button
//            // Open main activity
//            finish();
//        });
//
//        signup.setOnClickListener(view -> { // Sign up button
//            // Validate user form, open sign up method
//            String username = et_username.getText().toString(); // Extract username
//            String email = et_email.getText().toString(); // Extract email
//            String password = et_password.getText().toString(); // Extract password
//            String rePassword = et_rePassword.getText().toString(); // Extract rePassword
//            boolean isValidInformation = validateForm(username, email, password, rePassword);
//
//            if (isValidInformation) { // If information is valid
////                loadingAlert.startAlertDialog();
//                getToken(username, email, password, rePassword);
//            }
//        });

//        btn_changeLanguage.setOnClickListener(view -> { // Change language button
//            // Open change language method
//            loadingAlert.startAlertDialog();
//
//            new Handler().postDelayed(() -> {
//                loadingAlert.closeAlertDialog();
//                onLanguageChange();
//            },1000);
//        });
//   }

//    private boolean validateForm(String username, String email, String password, String rePassword) {
//        // Validate user input
//        boolean isValid = true;
//
//        // Validate username
//        if (username.isEmpty()) {
//            isValid = false;
//            et_username.setError(getString(R.string.form_warning));
//        }else {
//            et_username.setError(null);
//        }
//
//        // Validate email
//        if (email.isEmpty()) { // || !isEmail(et_email)
//            isValid = false;
//            et_email.setError(getString(R.string.email_invalid));
//        }else {
//            et_email.setError(null);
//        }
//
//        // Validate password
//        if (password.isEmpty()) {
//            isValid = false;
//            et_password.setError(getString(R.string.form_warning));
//        } else {
//            et_password.setError(null);
//        }
//
//        // Validate password confirm
//        if (rePassword.isEmpty() || !password.equals(rePassword)) {
//            isValid = false;
//            et_rePassword.setError(getString(R.string.password_warning));
//        }
//
//        return isValid; // User information is valid
//    }
//
//    @SuppressLint("SetJavaScriptEnabled")
//    private void getToken(String username, String email, String password, String rePassword) {
//        // Get sign up token
//        CookieManager.getInstance().removeAllCookies(null);
//
////        webView.setVisibility(View.VISIBLE);
//        webView = new WebView(getBaseContext());
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                Log.d(GlobalVar.LOG_TAG, "onPageFinished: ");
//
//                if (url.contains("openid-connect/auth")) { // Url is now in sign in page
//                    String redirect = "document.getElementsByTagName('a')[0].click();"; // Click on sign up button
//                    view.evaluateJavascript(redirect, null);
//                }
//                else if (url.contains("login-actions/registration")) { // Url is now in sign up page
//                    Log.d(GlobalVar.LOG_TAG, "Enter registration");
//                    String dataError = "document.getElementsByClassName('helper-text')[0].getAttribute('data-error');"; // Appear when email is exist
//
//                    view.evaluateJavascript(dataError, dErr-> {
//                        if (dErr.equals("null")) { // dErr = "Email already exist."
//                            String usrScript = "document.getElementById('username').value='" + username + "';";
//                            String emailScript = "document.getElementById('email').value='" + email + "';";
//                            String pwdScript = "document.getElementById('password').value='" + password + "';";
//                            String rePwdScript = "document.getElementById('password-confirm').value='" + rePassword + "';";
//
//                            view.evaluateJavascript(usrScript, null);
//                            view.evaluateJavascript(emailScript, null);
//                            view.evaluateJavascript(pwdScript, null);
//                            view.evaluateJavascript(rePwdScript, null);
//                            view.evaluateJavascript("document.getElementsByTagName('form')[0].submit();", null); // Submit form
//                            loadingAlert.closeAlertDialog();
//                        }
//                        else { //
//                            Log.d(GlobalVar.LOG_TAG, "error: " + dErr);
//                            signUpLog(dErr);
//                        }
//                    });
//                }
//                else if (url.contains("manager/#state=")) { // Sign up success, open log in
//                    Log.d(GlobalVar.LOG_TAG, getString(R.string.success_warning));
//                    signUpLog(getString(R.string.success_warning));
//                   // openLogInActivity(); // Open login
//                    finish();
//                }
//
//                String cookies = CookieManager.getInstance().getCookie(url);
//                Log.d(GlobalVar.LOG_TAG, "return cookie: " + cookies);
//                Log.d(GlobalVar.LOG_TAG, "url: " + url);
//                super.onPageFinished(view,url);
//            }
//        });
//
//        webView.loadUrl(GlobalVar.baseUrl);
//    }
//
//    private void signUpLog(String msg) {
//        Toast.makeText(SignUp.this, msg, Toast.LENGTH_SHORT).show();
//    }
//}