package org.ohmstheresistance.mastermind.activities;

import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.ohmstheresistance.mastermind.R;
import org.ohmstheresistance.mastermind.rv.PrevGuessesAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final long COUNTDOWN_TIMER_IN_MILLIS = 60000;

    private EditText userGuessEditText;
    private TextView guessesRemainingTextView;
    private TextView previousGuessesHeaderTextView;
    private TextView countDownTimerTextView;
    private TextView firstNumberTextView;
    private TextView secondNumberTextView;
    private TextView thirdNumberTextView;
    private TextView fourthNumberTextView;
    private TextView fifthNumberTextView;
    private TextView sixthNumberTextView;
    private TextView seventhNumberTextView;
    private TextView eighthNumberTextView;
    private TextView combinationTextView;

    private ImageView personImageView;
    private ImageView brickOne;
    private ImageView brickTwo;
    private ImageView brickThree;
    private ImageView brickFour;
    private ImageView brickFive;
    private ImageView brickSix;
    private ImageView brickSeven;
    private ImageView brickEight;
    private ImageView brickNine;
    private ImageView brickTen;

    private RecyclerView prevGuessesRecyclerView;
    private PrevGuessesAdapter prevGuessesAdapter;
    private List<String> prevGuessesEnteredList;

    private Button zeroButton;
    private Button oneButton;
    private Button twoButton;
    private Button threeButton;
    private Button fourButton;
    private Button fiveButton;
    private Button sixButton;
    private Button sevenButton;
    private Button deleteButton;
    private Button resetButton;
    private Button hintButton;
    private Button guessButton;

    private CountDownTimer countDownTimer;
    private int totalGuesses = 10;
    private long timeLeftInMillis;

    private String combination;
    private String randomNumbersResponse;

    LinearLayoutManager linearLayoutManager;

    private List<String> comboList;


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
        firstNumberTextView = findViewById(R.id.first_number_textview);
        secondNumberTextView = findViewById(R.id.second_number_textview);
        thirdNumberTextView = findViewById(R.id.third_number_textview);
        fourthNumberTextView = findViewById(R.id.fourth_number_textview);
        fifthNumberTextView = findViewById(R.id.fifth_number_textview);
        sixthNumberTextView = findViewById(R.id.sixth_number_textview);
        seventhNumberTextView = findViewById(R.id.seventh_number_textview);
        eighthNumberTextView = findViewById(R.id.eighth_number_textview);
        prevGuessesRecyclerView = findViewById(R.id.prev_guess_recycler);
        combinationTextView = findViewById(R.id.combination_textview);
        resetButton = findViewById(R.id.reset_button);

        personImageView = findViewById(R.id.person_imageview);

        brickOne = findViewById(R.id.brick_one_imageview);
        brickTwo = findViewById(R.id.brick_two_imageview);
        brickThree = findViewById(R.id.brick_three_imageview);
        brickFour = findViewById(R.id.brick_four_imageview);
        brickFive = findViewById(R.id.brick_five_imageview);
        brickSix = findViewById(R.id.brick_six_imageview);
        brickSeven = findViewById(R.id.brick_seven_imageview);
        brickEight = findViewById(R.id.brick_eight_imageview);
        brickNine = findViewById(R.id.brick_nine_imageview);
        brickTen = findViewById(R.id.brick_ten_imageview);


        zeroButton = findViewById(R.id.zero_button);
        oneButton = findViewById(R.id.one_button);
        twoButton = findViewById(R.id.two_button);
        threeButton = findViewById(R.id.three_button);
        fourButton = findViewById(R.id.four_button);
        fiveButton = findViewById(R.id.five_button);
        sixButton = findViewById(R.id.six_button);
        sevenButton = findViewById(R.id.seven_button);
        deleteButton = findViewById(R.id.delete_button);
        hintButton = findViewById(R.id.hint_button);
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
        hintButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);

        linearLayoutManager = new LinearLayoutManager(this);
        prevGuessesRecyclerView.setLayoutManager(linearLayoutManager);
        prevGuessesEnteredList = new ArrayList<>();
        prevGuessesAdapter = new PrevGuessesAdapter(prevGuessesEnteredList, comboList);
        prevGuessesRecyclerView.setAdapter(prevGuessesAdapter);

        comboList = new ArrayList<>();

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

            case R.id.hint_button:

                pickAHintToDisplay();
                break;

            case R.id.reset_button:

                Toast.makeText(this, "Reset has not been implemented yet.", Toast.LENGTH_SHORT).show();
                break;

            case R.id.guess_button:

                if (totalGuesses > 0) {

                    if (userGuessEditText.getText().toString().length() < 4 && userGuessEditText.getText().toString().length() >= 1) {
                        Toast.makeText(this, "Please enter a 4 digit combination.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (userGuessEditText.getText().toString().isEmpty()) {
                        Toast.makeText(this, "Please enter a valid entry.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    totalGuesses--;
                    guessesRemainingTextView.setText(totalGuesses + "");

                    animatePersonLinear();

                } else {

                    animatePersonLinear();
                    guessButton.setEnabled(false);
                    Toast.makeText(this, "You Lost!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void getRandomNumbers() {
        OkHttpClient client = new OkHttpClient();

        String baseOfNumbers = "10";
        String col = "1";
        final String num = "4";
        String minNum = "0";
        String maxNum = "7";
        String format = "plain";
        String rnd = "new";

        String url = "https://www.random.org/integers/?num=" + num + "&min=" + minNum + "&max=" + maxNum + "&col=" + col + "&base=" + baseOfNumbers + "&format=" + format + "&rnd=" + rnd;

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

                    Log.e("RESPONSE", randomNumbersResponse);

                    String[] separatedResponse = randomNumbersResponse.split("\\s+");

                    String firstNumber = separatedResponse[0];
                    String secondNumber = separatedResponse[1];
                    String thirdNumber = separatedResponse[2];
                    String fourthNumber = separatedResponse[3];

                    combination = firstNumber + secondNumber + thirdNumber + fourthNumber;

                    comboList.add(combination);
                    combinationTextView.setText(combination);

                    String[] numbers = {"0", "1", "2", "3", "4", "5", "6", "7"};
                    Collections.shuffle(Arrays.asList(numbers));

                    final String[] eightDisplayedNumbers = {numbers[0], numbers[2], numbers[4], numbers[6], firstNumber, secondNumber, thirdNumber, fourthNumber};
                    Collections.shuffle(Arrays.asList(eightDisplayedNumbers));


                    Log.e("Combination", combination);
                    Log.e("Combination", numbers[2]);


                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            firstNumberTextView.setText(eightDisplayedNumbers[5]);
                            secondNumberTextView.setText(eightDisplayedNumbers[1]);
                            thirdNumberTextView.setText(eightDisplayedNumbers[3]);
                            fourthNumberTextView.setText(eightDisplayedNumbers[0]);
                            fifthNumberTextView.setText(eightDisplayedNumbers[2]);
                            sixthNumberTextView.setText(eightDisplayedNumbers[7]);
                            seventhNumberTextView.setText(eightDisplayedNumbers[4]);
                            eighthNumberTextView.setText(eightDisplayedNumbers[6]);
                        }

                    });
                }
            }
        });
    }

    private void checkWhatToastToDisplay() {

        prevGuessesAdapter.setComboInfo(comboList);
        prevGuessesEnteredList.add(userGuessEditText.getText().toString());
        prevGuessesAdapter.setData(prevGuessesEnteredList);

        String secondAndThirdNumbers = userGuessEditText.getText().toString().substring(1);
        String secondNumber = String.valueOf(secondAndThirdNumbers.charAt(0));
        String thirdNumber = String.valueOf(secondAndThirdNumbers.charAt(1));


        if (userGuessEditText.getText().toString().equals(combination)) {
            countDownTimer.cancel();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                userGuessEditText.setBackgroundColor(getColor(R.color.userWonColor));
                userGuessEditText.setText(combination);

            }
            Toast.makeText(this, "You won!", Toast.LENGTH_SHORT).show();
            return;
        }

        if ((combination.startsWith(String.valueOf(userGuessEditText.getText().toString().charAt(0))) ||
                String.valueOf(combination.charAt(1)).matches(secondNumber)||
                String.valueOf(combination.charAt(2)).matches(thirdNumber) ||
                        combination.endsWith((userGuessEditText.getText().toString().substring(3))))) {

            Toast.makeText(this, "You guessed a correct number and its correct location!", Toast.LENGTH_SHORT).show();
            userGuessEditText.setText("");

            return;
        }

        if (!(combination.startsWith(String.valueOf(userGuessEditText.getText().toString().charAt(0))) ||
                String.valueOf(combination.charAt(1)).matches(secondNumber)||
                String.valueOf(combination.charAt(2)).matches(thirdNumber) ||
                combination.endsWith((userGuessEditText.getText().toString().substring(3))))) {

            Toast.makeText(this, "Incorrect!", Toast.LENGTH_SHORT).show();
            userGuessEditText.setText("");
        }

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

        if (timeLeftInMillis < 15000) {
            countDownTimerTextView.setTextColor(Color.RED);
        } else {
            countDownTimerTextView.setTextColor(countDownTimerTextView.getTextColors());
        }

        if (totalGuesses == 0) {

            countDownTimer.cancel();
        }

    }

    private void animatePersonLinear() {

        checkWhatToastToDisplay();

        if (totalGuesses == 9) {
            brickTen.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_out_right));

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    brickTen.setVisibility(View.INVISIBLE);
                }
            }, 200);
        }
        if (totalGuesses == 8) {
            brickNine.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_out_right));

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    brickNine.setVisibility(View.INVISIBLE);
                }
            }, 200);
        }
        if (totalGuesses == 7) {
            brickEight.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_out_right));

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    brickEight.setVisibility(View.INVISIBLE);
                }
            }, 200);
        }
        if (totalGuesses == 6) {
            brickSeven.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_out_right));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    brickSeven.setVisibility(View.INVISIBLE);
                }
            }, 200);
        }
        if (totalGuesses == 5) {
            personImageView.setImageDrawable(getDrawable(R.drawable.bartjumping));
            brickSix.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_out_right));

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    brickSix.setVisibility(View.INVISIBLE);
                }
            }, 200);
        }
        if (totalGuesses == 4) {
            brickFive.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_out_right));

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    brickFive.setVisibility(View.INVISIBLE);
                }
            }, 200);
        }
        if (totalGuesses == 3) {
            brickFour.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_out_right));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    brickFour.setVisibility(View.INVISIBLE);
                }
            }, 200);
        }
        if (totalGuesses == 2) {
            personImageView.setImageDrawable(getDrawable(R.drawable.bartscared));
            brickThree.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_out_right));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    brickThree.setVisibility(View.INVISIBLE);
                }
            }, 200);        }
        if (totalGuesses == 1) {
            brickTwo.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_out_right));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    brickTwo.setVisibility(View.INVISIBLE);
                }
            }, 200);
        }
        if (totalGuesses == 0) {
            personImageView.setImageDrawable(getDrawable(R.drawable.bartfalling));
            brickOne.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_out_right));
            personImageView.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.exit_bottom));
            brickOne.setVisibility(View.INVISIBLE);

            guessButton.setEnabled(false);
            deleteButton.setEnabled(false);
        }

    }

    private void pickAHintToDisplay(){

        String[] hints = {"There is no chance the number to guess is negative.", "The combination is 4 digit long.", "At least one of the numbers above is in the combo.",
                "You have " + totalGuesses + " remaining!", "You have " +countDownTimerTextView.getText().toString()+ " left!" };

        Collections.shuffle(Arrays.asList(hints));
        String displayHint = hints[2];

        Toast.makeText(this, displayHint, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(0, 0);
    }
}
