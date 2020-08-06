package com.example.jagratiapp;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;

public class signup_page extends AppCompatActivity {
    //ToDO: Commented wale abhi use nahi kar rahe
    private ImageView img;
    private EditText email;
    private EditText password;
    private EditText cpassword;
//    private EditText Eotp;
//    private EditText phoneno;
//    private Button getotp;
    private Button signup;
//    private TextView errortext;
//    private TextView countertext;

    private ProgressBar progressBar;

    //Firebase authorisation
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);
        img = findViewById(R.id.signup_background);
        img.setImageAlpha(50);
//        countertext = findViewById(R.id.countertext);
//        getotp = findViewById(R.id.sign_get_otp);

        View decorView = getWindow().getDecorView();

        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        decorView.setSystemUiVisibility(uiOptions);
//        Eotp = findViewById(R.id.signup_otp);
//        Eotp.setVisibility(View.INVISIBLE);

        //starting from here
        email = findViewById(R.id.sign_up_email);
        password = findViewById(R.id.signup_pass);
        cpassword = findViewById(R.id.signup_pass_again);













//        getotp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new CountDownTimer(30000, 1000) {
//
//                    public void onTick(long millisUntilFinished) {
//                        countertext.setText("Create OTP again in " + millisUntilFinished / 1000 + " seconds");
//                        getotp.setEnabled(false);
//                        Eotp.setVisibility(View.VISIBLE);
//                    }
//
//
//                    public void onFinish() {
//                        countertext.setText("Get OTP again");
//                        getotp.setEnabled(true);
//                    }
//                }.start();

            }
        });


    }
}