package org.ohmstheresistance.mastermind.activities;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.ohmstheresistance.mastermind.R;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final long COUNTDOWN_TIMER_IN_MILLIS = 30000;

    private EditText userGuessEditText;
    private TextView previousGuessesHeaderTextView;
    private TextView guessesRemainingTextView;
    private TextView countDownTimerTextView;
    private TextView randomNumbersTextView;
    private TextView randomNumbersTextViewTwo;

    private Button zeroButton;
    private Button oneButton;
    private Button twoButton;
    private Button threeButton;
    private Button fourButton;
    private Button fiveButton;
    private Button sixButton;
    private Button sevenButton;
    private Button deleteButton;
    private Button unassignedButton;
    private Button guessButton;

    private CountDownTimer countDownTimer;
    private int totalGuesses = 10;
    private long timeLeftInMillis;

    private String randomNumbersResponse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timeLeftInMillis = COUNTDOWN_TIMER_IN_MILLIS;

        setUpViews();
        getRandomNumbers();
        startCountDown();
    }

    private void setUpViews() {

        userGuessEditText = findViewById(R.id.user_guess_edittext);
        previousGuessesHeaderTextView = findViewById(R.id.previous_guesses_header_textview);
        guessesRemainingTextView = findViewById(R.id.remaining_guess_count_textview);
        countDownTimerTextView = findViewById(R.id.countdown_timer_textview);
        randomNumbersTextView = findViewById(R.id.random_numbers_textview);
        randomNumbersTextViewTwo = findViewById(R.id.random_numbers_textview_two);

        zeroButton = findViewById(R.id.zero_button);
        oneButton = findViewById(R.id.one_button);
        twoButton = findViewById(R.id.two_button);
        threeButton = findViewById(R.id.three_button);
        fourButton = findViewById(R.id.four_button);
        fiveButton = findViewById(R.id.five_button);
        sixButton = findViewById(R.id.six_button);
        sevenButton = findViewById(R.id.seven_button);
        deleteButton = findViewById(R.id.delete_button);
        unassignedButton = findViewById(R.id.unassigned_button);
        guessButton = findViewById(R.id.guess_button);

        zeroButton.setOnClickListener(this);
        oneButton.setOnClickListener(this);
        twoButton.setOnClickListener(this);
        threeButton.setOnClickListener(this);
        fourButton.setOnClickListener(this);
        fiveButton.setOnClickListener(this);
        sixButton.setOnClickListener(this);
        sevenButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        guessButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {

            case R.id.zero_button:
                userGuessEditText.append("0");
                break;

            case R.id.one_button:
                userGuessEditText.append("1");
                break;

            case R.id.two_button:
                userGuessEditText.append("2");
                break;

            case R.id.three_button:
                userGuessEditText.append("3");
                break;

            case R.id.four_button:
                userGuessEditText.append("4");
                break;

            case R.id.five_button:
                userGuessEditText.append("5");
                break;

            case R.id.six_button:

                userGuessEditText.append("6");
                break;

            case R.id.seven_button:
                userGuessEditText.append("7");
                break;

            case R.id.delete_button:

                int length = userGuessEditText.getText().length();
                if (length > 0) {
                    userGuessEditText.getText().delete(length - 1, length);
                }
                break;

            case R.id.unassigned_button:

                break;

            case R.id.guess_button:


                if (totalGuesses > 0) {

                    if(userGuessEditText.getText().toString().length() < 4 && userGuessEditText.getText().toString().length() >= 1 ){
                        Toast.makeText(this, "Please enter a 4 digit combination.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(userGuessEditText.getText().toString().isEmpty()){
                        Toast.makeText(this, "Please enter a valid entry.", Toast.LENGTH_SHORT).show();

                        return;
                    }

                    totalGuesses--;
                    guessesRemainingTextView.setText(totalGuesses + "");

                } else {

                    guessButton.setEnabled(false);
                    Toast.makeText(this, "You Lost!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void getRandomNumbers(){
        OkHttpClient client = new OkHttpClient();

        String url = "https://www.random.org/integers/?num=4&min=0&max=7&col=4&base=10&format=plain&rnd=new";

        String baseOfNumers = "10";
        String col = "1";
        String num = "4";
        String minNum = "0";
        String maxNum = "7";
        String format = "plain";
        String rnd = "new";


        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    randomNumbersResponse = response.body().string();

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            randomNumbersTextView.setText(randomNumbersResponse);
                            randomNumbersTextViewTwo.setText(randomNumbersResponse);
                        }
                    });
                }
            }
        });
    }

    private void startCountDown() {

        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {

                timeLeftInMillis = 0;
                updateCountDownText();
            }
        }.start();
    }

    private void updateCountDownText() {

        int minutes = (int) (timeLeftInMillis / 1000 / 60);
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        countDownTimerTextView.setText(formattedTime);

        if (timeLeftInMillis < 5000) {
            countDownTimerTextView.setTextColor(Color.RED);
        } else {
            countDownTimerTextView.setTextColor(countDownTimerTextView.getTextColors());
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

    }
}
