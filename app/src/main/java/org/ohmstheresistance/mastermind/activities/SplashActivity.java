package org.ohmstheresistance.mastermind.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.ohmstheresistance.mastermind.R;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN_TIMER = 2000;

    private Button splashButtonSeven, splashButtonThree, splashButtonFive, splashButtonOne;

    private EditText splashScreenEditText;
    private ImageView splashCheckMarkImageView;

    private Intent toMainScreenIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        setUpViews();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                splashButtonSeven.setBackgroundColor(getResources().getColor(R.color.splashButtonBackgroundColor));
                splashScreenEditText.append("7");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        splashButtonThree.setBackgroundColor(getResources().getColor(R.color.splashButtonBackgroundColor));
                        splashButtonSeven.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                        splashScreenEditText.append("3");

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                splashButtonFive.setBackgroundColor(getResources().getColor(R.color.splashButtonBackgroundColor));
                                splashButtonThree.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                                splashScreenEditText.append("5");


                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        splashButtonOne.setBackgroundColor(getResources().getColor(R.color.splashButtonBackgroundColor));
                                        splashButtonFive.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                        splashScreenEditText.append("1");


                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {

                                                splashButtonOne.setBackgroundColor(getResources().getColor(R.color.splashButtonBackgroundColor));
                                                splashCheckMarkImageView.setVisibility(View.VISIBLE);

                                            }
                                        }, 500);

                                    }
                                }, 200);

                            }
                        }, 200);

                    }
                }, 200);
            }
        }, 200);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                toMainScreenIntent = new Intent(SplashActivity.this, MainPageActivity.class);
                startActivity(toMainScreenIntent);
                SplashActivity.this.finish();
                overridePendingTransition(0,0);


            }
        }, SPLASH_SCREEN_TIMER);

    }

    private void setUpViews() {

        splashScreenEditText = findViewById(R.id.splash_edittext);
        splashCheckMarkImageView = findViewById(R.id.splash_checkmark);

        splashButtonSeven = findViewById(R.id.splash_seven_button);
        splashButtonFive = findViewById(R.id.splash_five_button);
        splashButtonThree = findViewById(R.id.splash_three_button);
        splashButtonOne = findViewById(R.id.splash_one_button);

    }
}
