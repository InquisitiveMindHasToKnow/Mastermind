package org.ohmstheresistance.mastermind.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.ohmstheresistance.mastermind.R;
import org.ohmstheresistance.mastermind.database.UserInfoDatabaseHelper;
import org.ohmstheresistance.mastermind.dialogs.EditUserInfoDialog;
import org.ohmstheresistance.mastermind.dialogs.MastermindInstructions;

import java.util.Calendar;
import java.util.Locale;

public class MainPageActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    private TextView greetingTextView, userNameTextView, notUserTextView, highScoreHeaderTextView, highScoreTextView, highScorerTextView;
    private Button playNowButton, instructionsButton;
    private Intent navigationIntent;
    private UserInfoDatabaseHelper userInfoDatabaseHelper;

    private String userName, highScorer;
    private long highScore;

    private SharedPreferences highScoreSharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        setViews();
        setGreeting();
        displayHighScore();
    }

    private void displayHighScore() {

        highScoreSharedPrefs = getApplicationContext().getSharedPreferences("hsSharedPrefs", MODE_PRIVATE);

        boolean sharedPrefsNotEmpty = highScoreSharedPrefs.getBoolean("sharedPrefsNotEmpty", false);

        if(!sharedPrefsNotEmpty){

            highScoreTextView.setVisibility(View.INVISIBLE);

        }else{

            highScoreTextView.setVisibility(View.VISIBLE);

            highScorer = highScoreSharedPrefs.getString("highScorer", "");
            highScore = highScoreSharedPrefs.getLong("highScore", 0);

            int minutes = (int) (highScore / 1000 / 60);
            int seconds = (int) (highScore / 1000) % 60;


            String highScoreInMinsAndSeconds = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

           highScorerTextView.setText(highScorer);
           highScoreTextView.setText(highScoreInMinsAndSeconds);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setViews() {

        greetingTextView = findViewById(R.id.greeting_textview);
        userNameTextView = findViewById(R.id.user_name_textview);
        notUserTextView = findViewById(R.id.not_user_textview);
        highScorerTextView = findViewById(R.id.high_scorer_textview);
        highScoreHeaderTextView = findViewById(R.id.high_score_header_textview);
        highScoreTextView = findViewById(R.id.high_score_textview);

        playNowButton = findViewById(R.id.play_now_button);
        instructionsButton = findViewById(R.id.instructions_button);
        userInfoDatabaseHelper = UserInfoDatabaseHelper.getInstance(this);

        playNowButton.setOnClickListener(this);
        instructionsButton.setOnClickListener(this);
        notUserTextView.setOnClickListener(this);

        playNowButton.setOnTouchListener(this);
        instructionsButton.setOnTouchListener(this);

        userName = userInfoDatabaseHelper.getUserInfo().get(0).getUserName();
        userNameTextView.setText(userName + "!");


        notUserTextView.setPaintFlags(notUserTextView.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        notUserTextView.setText("Not " + userName + "?");

    }

    private void setGreeting() {

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 12) {
            greetingTextView.setText(getString(R.string.good_morning));

        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            greetingTextView.setText(getString(R.string.good_afternoon));


        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            greetingTextView.setText(getString(R.string.good_evening));

        } else if (timeOfDay >= 21 && timeOfDay < 24) {
            greetingTextView.setText(getString(R.string.good_night) );
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

            case R.id.not_user_textview:
                updateUserName();
                break;
        }
    }

    private void updateUserName(){

        EditUserInfoDialog editUserInfoDialog = new EditUserInfoDialog();
        editUserInfoDialog.show(getSupportFragmentManager(), "EditUserInfoDialog");
    }

}

