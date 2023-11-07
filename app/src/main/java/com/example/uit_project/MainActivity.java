package com.example.uit_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.AppCompatButton;
public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView signup;

    private Button login_main;

    private TextView dialog_lang;
    private LinearLayout show_dialog_lang;
    public int lang_selected;

    Context context;
    Resources resources;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       login_main = findViewById(R.id.login_main_btn);

       dialog_lang = findViewById(R.id.dialog_language);
       show_dialog_lang = (LinearLayout)findViewById(R.id.showlangdialog);
       GlobalVar.manager = new LanguageManager(this);

       login_main.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent i = new Intent();
               i.setClass(MainActivity.this, Login.class);
               startActivity(i);
               recreate();
           }
       });

        signup = findViewById(R.id.signup_suggest_main);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(MainActivity.this, SignUp.class);
                startActivity(i);
            }
        });
//
//        if(LocaleHelper.getLanguage(MainActivity.this).equalsIgnoreCase("en"))
//        {
//            context = LocaleHelper.setLocale(MainActivity.this,"en");
//            resources = context.getResources();
//            dialog_lang.setText("EN");
//            setTitle(resources.getString(R.string.app_name));
//            lang_selected = 0;
//        }else if(LocaleHelper.getLanguage(MainActivity.this).equalsIgnoreCase("vi")){
//            context = LocaleHelper.setLocale(MainActivity.this,"vi");
//            resources = context.getResources();
//            dialog_lang.setText("VI");
//            setTitle(resources.getString(R.string.app_name));
//            lang_selected = 1;
//        }

        show_dialog_lang.setOnClickListener(new View.OnClickListener() {
            int checklang;

            @Override
            public void onClick(View view) {
                lang_selected = checklang;
                final String[] Language = {"English", "Vietnamese"};
                Log.d("Clicked","Clicked");
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                dialogBuilder.setTitle("Select a Language")
                        .setSingleChoiceItems(Language, lang_selected, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                if(Language[i].equals("English")){
                                    lang_selected = 0;
                                    checklang = lang_selected;
                                    GlobalVar.manager.updateResources("en");


                                }
                                if(Language[i].equals("Vietnamese")) {
                                    lang_selected = 1;
                                    checklang = lang_selected;
                                    GlobalVar.manager.updateResources("vi");

                                }
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                recreate();
                            }
                        });
                dialogBuilder.create().show();
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

        return super.onOptionsItemSelected(item);
    }
}