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

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

//import butterknife.BindView;
//import butterknife.ButterKnife;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtEmail, edtUsername, edtPassword;
    private Button btnSignup, btnSwitchToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

    //    ButterKnife.bind(this);

        edtUsername = findViewById(R.id.edtUsernameSignup);
        edtEmail = findViewById(R.id.edtEmailSignup);
        edtPassword = findViewById(R.id.edtPasswordSignup);
        btnSignup = findViewById(R.id.btnSignup);
        btnSwitchToLogin = findViewById(R.id.btnSwitchToLogin);

        btnSwitchToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToLogin();
            }
        });

        btnSignup.setOnClickListener(this);

        edtPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
                    btnSignup.callOnClick();
                }
                return false;
            }
        });

        if (ParseUser.getCurrentUser() != null){
            switchToIndex();
        }
    }

    private void switchToLogin(){
        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void switchToIndex(){
        Intent intent = new Intent(SignupActivity.this, IndexActivity.class);
        startActivity(intent);
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

    @Override
    public void onClick(View v) {
        if (edtEmail.getText().toString().equals("") || edtUsername.getText().toString().equals("") || edtPassword.getText().toString().equals("")){
            FancyToast.makeText(SignupActivity.this, "Email, Username and Password Required", FancyToast.LENGTH_SHORT, FancyToast.INFO, true).show();
        } else{
            ParseUser parseUser = new ParseUser();
            parseUser.setEmail(edtEmail.getText().toString());
            parseUser.setUsername(edtUsername.getText().toString());
            parseUser.setPassword(edtPassword.getText().toString());
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Registering " + edtUsername.getText().toString());
            progressDialog.show();
            parseUser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null){
                        FancyToast.makeText(SignupActivity.this, ParseUser.getCurrentUser().getUsername() + " Registered Successfully", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                        switchToIndex();
                    }else {
                        FancyToast.makeText(SignupActivity.this, e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                    }
                    progressDialog.dismiss();
                }
            });
        }
    }
}
