package org.ohmstheresistance.mastermind.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.ohmstheresistance.mastermind.R;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN_TIMER = 2000;
    public static final String KEY_PREFS_FIRST_LAUNCH = "first_launch";


    private Button splashButtonSeven, splashButtonThree, splashButtonFive, splashButtonZero;

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

                splashButtonSeven.setBackground(getDrawable(R.drawable.pressed_rounded_button));
                splashScreenEditText.append("7");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        splashButtonSeven.setBackground(getDrawable(R.drawable.rounded_button_corners));
                        splashButtonZero.setBackground(getDrawable(R.drawable.pressed_rounded_button));
                        splashScreenEditText.append("0");

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                splashButtonZero.setBackground(getDrawable(R.drawable.rounded_button_corners));
                                splashButtonFive.setBackground(getDrawable(R.drawable.pressed_rounded_button));
                                splashScreenEditText.append("5");

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        splashButtonFive.setBackground(getDrawable(R.drawable.rounded_button_corners));
                                        splashButtonThree.setBackground(getDrawable(R.drawable.pressed_rounded_button));
                                        splashScreenEditText.append("3");


                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {

                                                splashButtonThree.setBackground(getDrawable(R.drawable.rounded_button_corners));
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

        final SharedPreferences firstLaunchCheck = PreferenceManager.getDefaultSharedPreferences(this);

        if (firstLaunchCheck.getBoolean(KEY_PREFS_FIRST_LAUNCH, true)) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    toMainScreenIntent = new Intent(SplashActivity.this, UserInfoActivity.class);
                    startActivity(toMainScreenIntent);
                    SplashActivity.this.finish();
                    overridePendingTransition(0, 0);

                    firstLaunchCheck.edit().putBoolean(KEY_PREFS_FIRST_LAUNCH, false).apply();

                }
            }, SPLASH_SCREEN_TIMER);

        } else {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    toMainScreenIntent = new Intent(SplashActivity.this, MainPageActivity.class);
                    startActivity(toMainScreenIntent);
                    SplashActivity.this.finish();
                    overridePendingTransition(0, 0);

                }
            }, SPLASH_SCREEN_TIMER);
        }
    }

    private void setUpViews() {

        splashScreenEditText = findViewById(R.id.splash_edittext);
        splashCheckMarkImageView = findViewById(R.id.splash_checkmark);

        splashButtonSeven = findViewById(R.id.splash_seven_button);
        splashButtonFive = findViewById(R.id.splash_five_button);
        splashButtonThree = findViewById(R.id.splash_three_button);
        splashButtonZero = findViewById(R.id.splash_zero_button);

    }

}
