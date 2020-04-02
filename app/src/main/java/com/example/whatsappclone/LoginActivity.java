package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jpardogo.android.googleprogressbar.library.FoldingCirclesDrawable;
import com.jpardogo.android.googleprogressbar.library.GoogleProgressBar;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

//import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtUsername, edtPassword;
    private Button btnLogin, btnSwitchToSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //ButterKnife.bind(this);

        edtUsername = findViewById(R.id.edtUsernameLogin);
        edtPassword = findViewById(R.id.edtPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        btnSwitchToSignup = findViewById(R.id.btnSwitchToSignup);

        edtPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
                    btnLogin.callOnClick();
                }
                return false;
            }
        });

        btnLogin.setOnClickListener(this);

        btnSwitchToSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToSignup();
            }
        });

        if (ParseUser.getCurrentUser() != null){
            switchToIndex();
        }
    }

    //Switch User To Signup Page
    private void switchToSignup(){
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
    }

    private void switchToIndex(){
        Intent intent = new Intent(LoginActivity.this, IndexActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finishAffinity();
        finish();
    }

    public void hideKeyboard(View view){
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    // This method is called whenever the login button is clicked
    @Override
    public void onClick(View v) {

        if (edtUsername.getText().toString().equals("") || edtPassword.getText().toString().equals("")){
            FancyToast.makeText(getApplicationContext(), "Username and Password are required", Toast.LENGTH_SHORT, FancyToast.INFO, true).show();;
        } else {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            ParseUser.logInInBackground(edtUsername.getText().toString(), edtPassword.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (e == null && user != null) {
                        FancyToast.makeText(getApplicationContext(), "Login Successful!!! \n Welcome " + edtUsername.getText().toString(), Toast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                        switchToIndex();
                    } else {
                        FancyToast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG, FancyToast.ERROR, true).show();
                    }
                }
            });
        }
    }
}