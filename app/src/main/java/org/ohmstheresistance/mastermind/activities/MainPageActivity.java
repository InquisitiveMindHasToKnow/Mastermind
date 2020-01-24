package org.ohmstheresistance.mastermind.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.ohmstheresistance.mastermind.R;

import java.util.Calendar;

public class MainPageActivity extends AppCompatActivity {

    private TextView greetingTextView;
    private Button playNowButton;
    private Button rulesButton;
    private Intent navigationIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        setViews();
        setGreeting();
    }

    private void setViews(){

        greetingTextView = findViewById(R.id.greeting_textview);
        playNowButton = findViewById(R.id.play_now_button);
        rulesButton = findViewById(R.id.rules_button);

        playNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navigationIntent = new Intent(MainPageActivity.this, MainActivity.class);
                startActivity(navigationIntent);
                overridePendingTransition(0, 0);

            }
        });

        rulesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                navigationIntent = new Intent(MainPageActivity.this, GameRules.class);
//                startActivity(navigationIntent);
                Toast.makeText(MainPageActivity.this, "Game Rules", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setGreeting(){

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 12) {
            greetingTextView.setText(getString(R.string.good_morning) + " Omar.");

        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            greetingTextView.setText(getString(R.string.good_afternoon)+ " Omar.");

        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            greetingTextView.setText(getString(R.string.good_evening)+ " Omar.");

        } else if (timeOfDay >= 21 && timeOfDay < 24) {
            greetingTextView.setText(getString(R.string.good_night)+ " Omar.");
        }
    }
}

