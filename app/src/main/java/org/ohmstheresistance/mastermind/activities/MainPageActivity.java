package org.ohmstheresistance.mastermind.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.ohmstheresistance.mastermind.R;

import java.util.Calendar;

public class MainPageActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    private TextView greetingTextView;
    private Button playNowButton;
    private Button instructionsButton;
    private Intent navigationIntent;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        setViews();
        setGreeting();

        playNowButton.setOnClickListener(this);
        instructionsButton.setOnClickListener(this);

        playNowButton.setOnTouchListener(this);
        instructionsButton.setOnTouchListener(this);
    }

    private void setViews() {

        greetingTextView = findViewById(R.id.greeting_textview);
        playNowButton = findViewById(R.id.play_now_button);
        instructionsButton = findViewById(R.id.instructions_button);
    }

    private void setGreeting() {

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 12) {
            greetingTextView.setText(getString(R.string.good_morning) + " Omar.");

        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            greetingTextView.setText(getString(R.string.good_afternoon) + " Omar.");

        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            greetingTextView.setText(getString(R.string.good_evening) + " Omar.");

        } else if (timeOfDay >= 21 && timeOfDay < 24) {
            greetingTextView.setText(getString(R.string.good_night) + " Omar.");
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                v.setBackground(getResources().getDrawable(R.drawable.pressed_rounded_button));

                break;

            case MotionEvent.ACTION_UP:

                v.setBackground(getResources().getDrawable(R.drawable.rounded_button_corners));

                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {

            case R.id.play_now_button:

                navigationIntent = new Intent(MainPageActivity.this, MainActivity.class);
                startActivity(navigationIntent);
                overridePendingTransition(0, 0);
                break;

            case R.id.instructions_button:

                MastermindInstructions dialog = new MastermindInstructions();
                dialog.show(getSupportFragmentManager(), "MyCustomDialog");
                break;
        }
    }
}

