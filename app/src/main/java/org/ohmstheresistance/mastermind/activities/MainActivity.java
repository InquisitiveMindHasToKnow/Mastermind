package org.ohmstheresistance.mastermind.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.ohmstheresistance.mastermind.R;
import org.ohmstheresistance.mastermind.database.UserInfoDatabaseHelper;
import org.ohmstheresistance.mastermind.dialogs.NewHighScoreDialog;
import org.ohmstheresistance.mastermind.dialogs.NoMoreGuesses;
import org.ohmstheresistance.mastermind.dialogs.TimerRanOutDialog;
import org.ohmstheresistance.mastermind.dialogs.UserRevealedComboDialog;
import org.ohmstheresistance.mastermind.dialogs.WinnerWinner;
import org.ohmstheresistance.mastermind.rv.PrevGuessesAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static org.ohmstheresistance.mastermind.activities.MainPageActivity.HIGH_SCORE_MAKER;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    private static final long COUNTDOWN_TIMER_IN_MILLIS = 300000;
    public static final String SHARED_PREFS = "highScoreSharedPrefs";
    public static final String HIGH_SCORE_KEY = "highScoreKey";
    public static final String NEW_HIGH_SCORE_KEY = "newHighScoreKey";

    private EditText userGuessEditText;

    private TextView guessesRemainingTextView, previousGuessesHeaderTextView, countDownTimerTextView, firstNumberTextView, secondNumberTextView,
            thirdNumberTextView, fourthNumberTextView, fifthNumberTextView, sixthNumberTextView, seventhNumberTextView, eighthNumberTextView,
            combinationTextView, displayHintsAndGameStatusTextview, feedBackTextView, timeHeaderTextView;

    private ImageView personImageView, brickOne, brickTwo, brickThree, brickFour, brickFive, brickSix, brickSeven, brickEight, brickNine, brickTen, mainHighScoreCelebrationImageView;

    private Button zeroButton, oneButton, twoButton, threeButton, fourButton, fiveButton, sixButton, sevenButton, deleteButton, resetButton, hintButton,
            guessButton, revealButton;

    private List<String> prevGuessesEnteredList, comboList;
    private List<Integer> rightsGuesses;

    private String combination, randomNumbersResponse;

    private RecyclerView prevGuessesRecyclerView;
    private PrevGuessesAdapter prevGuessesAdapter;

    private CountDownTimer countDownTimer;
    private int totalGuesses = 10;
    private long timeLeftInMillis, defaultHighScore, score, currentHighScore;

    private LinearLayoutManager linearLayoutManager;
    private LinearLayout combinationLinearLayout, countDownTimerLinearLayout;

    private Bundle winningCombinationBundle;

    private SharedPreferences highScoreSharedPrefs;
    SharedPreferences.Editor highScoreSharedPrefsEditor;

    private int matchCounter = 0;
    private UserInfoDatabaseHelper userInfoDatabaseHelper;
    private String highScorer;

    private boolean connectedToInternet, animationRunning;
    private Handler blinkingHandler, displayNewHighScoreDialogHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timeLeftInMillis = COUNTDOWN_TIMER_IN_MILLIS;

        highScoreSharedPrefs = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        highScoreSharedPrefsEditor = highScoreSharedPrefs.edit();

        setUpViews();
        checkInternetConnection();

        defaultHighScore = TimeUnit.MINUTES.toMillis(5);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setUpViews() {

        countDownTimerLinearLayout = findViewById(R.id.countdown_timer_linear);

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
        displayHintsAndGameStatusTextview = findViewById(R.id.dispay_hints_and_game_status_textview);
        feedBackTextView = findViewById(R.id.feedback_textview);
        timeHeaderTextView = findViewById(R.id.time_header_textview);

        personImageView = findViewById(R.id.person_imageview);
        userInfoDatabaseHelper = UserInfoDatabaseHelper.getInstance(this);

        mainHighScoreCelebrationImageView = findViewById(R.id.main_high_score_celebration_imageview);
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
        resetButton = findViewById(R.id.reset_button);
        revealButton = findViewById(R.id.reveal_button);

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
        revealButton.setOnClickListener(this);

        zeroButton.setOnTouchListener(this);
        oneButton.setOnTouchListener(this);
        twoButton.setOnTouchListener(this);
        threeButton.setOnTouchListener(this);
        fourButton.setOnTouchListener(this);
        fiveButton.setOnTouchListener(this);
        sixButton.setOnTouchListener(this);
        sevenButton.setOnTouchListener(this);
        deleteButton.setOnTouchListener(this);
        guessButton.setOnTouchListener(this);
        hintButton.setOnTouchListener(this);
        resetButton.setOnTouchListener(this);
        revealButton.setOnTouchListener(this);

        linearLayoutManager = new LinearLayoutManager(this);
        combinationLinearLayout = findViewById(R.id.combination_linear);
        prevGuessesRecyclerView.setLayoutManager(linearLayoutManager);
        prevGuessesEnteredList = new ArrayList<>();
        rightsGuesses = new ArrayList<>();
        prevGuessesAdapter = new PrevGuessesAdapter(prevGuessesEnteredList, comboList, rightsGuesses);
        prevGuessesRecyclerView.setAdapter(prevGuessesAdapter);

        comboList = new ArrayList<>();
        winningCombinationBundle = new Bundle();

        revealButton.setEnabled(false);
        guessButton.setEnabled(false);
        hintButton.setEnabled(false);
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

                deleteLastEntry();

                break;

            case R.id.hint_button:

                pickAHintToDisplay();
                break;

            case R.id.reset_button:

                resetGame();
                break;

            case R.id.reveal_button:
                revealCombination();

                break;

            case R.id.guess_button:

                if (totalGuesses > 0) {

                    if (userGuessEditText.getText().toString().length() < 4 && userGuessEditText.getText().toString().length() >= 1) {

                        feedBackTextView.setText(getResources().getText(R.string.enter_four_digits));
                        return;
                    }

                    if (userGuessEditText.getText().toString().isEmpty()) {
                        feedBackTextView.setText(getResources().getText(R.string.enter_valid_entry));
                        return;
                    }

                    totalGuesses--;
                    guessesRemainingTextView.setText(totalGuesses + "");

                    animatePersonLinear();
                }
                break;
        }
    }

    private void revealCombination() {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this, R.style.RevealDialog);

        alertDialog.setTitle("Reveal combination?");
        alertDialog.setMessage("Completing this action will result in a loss!");
        alertDialog.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        countDownTimer.cancel();
                        combinationLinearLayout.setVisibility(View.VISIBLE);

                        feedBackTextView.setText(getResources().getText(R.string.revealed_answer_feedback));
                        displayHintsAndGameStatusTextview.setText(R.string.you_lost_text);

                        UserRevealedComboDialog userRevealedComboDialog = new UserRevealedComboDialog();
                        userRevealedComboDialog.setArguments(winningCombinationBundle);
                        userRevealedComboDialog.show(getSupportFragmentManager(), "UserRevealedComboDialog");

                        disableButtons();

                        brickOne.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_out_right));
                        brickTwo.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_out_right));
                        brickThree.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_out_right));
                        brickFour.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_out_right));
                        brickFive.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_out_right));
                        brickSix.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_out_right));
                        brickSeven.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_out_right));
                        brickEight.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_out_right));
                        brickNine.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_out_right));
                        brickTen.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_out_right));

                        brickOne.setVisibility(View.INVISIBLE);
                        brickTwo.setVisibility(View.INVISIBLE);
                        brickThree.setVisibility(View.INVISIBLE);
                        brickFour.setVisibility(View.INVISIBLE);
                        brickFive.setVisibility(View.INVISIBLE);
                        brickSix.setVisibility(View.INVISIBLE);
                        brickSeven.setVisibility(View.INVISIBLE);
                        brickEight.setVisibility(View.INVISIBLE);
                        brickNine.setVisibility(View.INVISIBLE);
                        brickTen.setVisibility(View.INVISIBLE);

                        personImageView.setImageResource(R.drawable.bartfalling);
                        personImageView.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.exit_bottom));

                    }

                });
        alertDialog.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        alertDialog.show();
    }


    private void deleteLastEntry() {

        int length = userGuessEditText.getText().length();
        if (length > 0) {
            userGuessEditText.getText().delete(length - 1, length);
        }
    }

    public void resetGame() {

        Intent resetGameIntent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(resetGameIntent);
        finish();
        overridePendingTransition(0, 0);
    }

    private void checkWhatMessageToDisplay() {

        prevGuessesAdapter.setComboInfo(comboList);
        prevGuessesEnteredList.add(userGuessEditText.getText().toString());
        prevGuessesAdapter.setData(prevGuessesEnteredList);

        matchCounter(combination, userGuessEditText.getText().toString());

        rightsGuesses.add(matchCounter);
        prevGuessesAdapter.setCorrectItems(rightsGuesses);

        if (matchCounter == 1) {
            feedBackTextView.setText(getResources().getText(R.string.one_entry_correct));
            userGuessEditText.setText("");
            matchCounter = 0;

        } else if (matchCounter == 2) {
            feedBackTextView.setText(getResources().getText(R.string.two_entries_correct));
            userGuessEditText.setText("");
            matchCounter = 0;

        } else if (matchCounter == 3) {
            feedBackTextView.setText(getResources().getText(R.string.three_entries_correct));
            userGuessEditText.setText("");
            matchCounter = 0;
        } else if (matchCounter == 4) {
            userWon();
        } else {

            feedBackTextView.setText(getResources().getText(R.string.incorrect));
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

                userLostBecauseTimerRanOut();
            }
        }.start();
    }

    private int matchCounter(String combo, String entry) {

        for (int i = 0; i < combo.length(); i++) {
            if (combo.charAt(i) == entry.charAt(i)) {

                matchCounter++;
            }
        }
        return matchCounter;
    }

    private void updateCountDownText() {

        int minutes = (int) (timeLeftInMillis / 1000 / 60);
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        countDownTimerTextView.setText(formattedTime);

        if (timeLeftInMillis < 15000) {
            countDownTimerTextView.setTextColor(getResources().getColor(R.color.lose_and_timer_running_out_color));
        } else {
            countDownTimerTextView.setTextColor(countDownTimerTextView.getTextColors());
        }

        if (totalGuesses == 0) {

            countDownTimer.cancel();
        }
    }

    private void userLostBecauseTimerRanOut() {
        combinationLinearLayout.setVisibility(View.VISIBLE);

        brickOne.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_out_right));
        brickTwo.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_out_right));
        brickThree.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_out_right));
        brickFour.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_out_right));
        brickFive.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_out_right));
        brickSix.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_out_right));
        brickSeven.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_out_right));
        brickEight.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_out_right));
        brickNine.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_out_right));
        brickTen.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_out_right));

        brickOne.setVisibility(View.INVISIBLE);
        brickTwo.setVisibility(View.INVISIBLE);
        brickThree.setVisibility(View.INVISIBLE);
        brickFour.setVisibility(View.INVISIBLE);
        brickFive.setVisibility(View.INVISIBLE);
        brickSix.setVisibility(View.INVISIBLE);
        brickSeven.setVisibility(View.INVISIBLE);
        brickEight.setVisibility(View.INVISIBLE);
        brickNine.setVisibility(View.INVISIBLE);
        brickTen.setVisibility(View.INVISIBLE);

        personImageView.setImageResource(R.drawable.bartfalling);
        personImageView.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.exit_bottom));

        displayHintsAndGameStatusTextview.setText(R.string.you_lost_text);

        TimerRanOutDialog timerRanOutDialog = new TimerRanOutDialog();
        timerRanOutDialog.setArguments(winningCombinationBundle);
        getSupportFragmentManager().beginTransaction().add(timerRanOutDialog, "TimerRanOutDialog").commitAllowingStateLoss();

        disableButtons();
    }

    private void userWon() {

        disableButtons();
        countDownTimer.cancel();
        combinationLinearLayout.setVisibility(View.VISIBLE);
        displayHintsAndGameStatusTextview.setText(getResources().getText(R.string.you_won_text));
        feedBackTextView.setText(getResources().getText(R.string.correct));
        userGuessEditText.setBackgroundColor(getResources().getColor(R.color.userWonColor));
        userGuessEditText.setText(combination);

        highScorer = userInfoDatabaseHelper.getUserInfo().get(0).getUserName();
        currentHighScore = highScoreSharedPrefs.getLong(HIGH_SCORE_KEY, 300001);
        score = (defaultHighScore - timeLeftInMillis) + 1000;

        if (score < currentHighScore) {

            highScoreSharedPrefsEditor.putString(HIGH_SCORE_MAKER, highScorer);
            highScoreSharedPrefsEditor.putLong(HIGH_SCORE_KEY, score);
            highScoreSharedPrefsEditor.putLong(NEW_HIGH_SCORE_KEY, score);
            highScoreSharedPrefsEditor.commit();

            Log.e("SCORE", score + "");
            Log.e("SCORECURRENT", currentHighScore + "");

            Glide.with(MainActivity.this)
                    .load(R.drawable.high_score_celebration)
                    .into(mainHighScoreCelebrationImageView);

            mainHighScoreCelebrationImageView.setVisibility(View.VISIBLE);
            resetButton.setEnabled(false);

            blinkAnimation(countDownTimerLinearLayout, timeHeaderTextView, countDownTimerTextView);

            displayNewHighScoreDialogHandler = new Handler();
            displayNewHighScoreDialogHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mainHighScoreCelebrationImageView.setVisibility(View.GONE);
                    resetButton.setEnabled(true);

                    NewHighScoreDialog newHighScoreDialog = new NewHighScoreDialog();
                    newHighScoreDialog.setArguments(winningCombinationBundle);
                    winningCombinationBundle.putLong(NEW_HIGH_SCORE_KEY, score);
                    newHighScoreDialog.show(getSupportFragmentManager(), "NewHighScoreDialog");

                }
            }, 4000);
        } else {

            WinnerWinner winnerWinnerDialog = new WinnerWinner();
            winnerWinnerDialog.setArguments(winningCombinationBundle);
            winnerWinnerDialog.show(getSupportFragmentManager(), "WinnerWinnerDialog");
        }
    }

    private void animatePersonLinear() {

        checkWhatMessageToDisplay();

        if (totalGuesses == 9) {
            animateBrick(brickTen);
        }
        if (totalGuesses == 8) {
            personImageView.setImageDrawable(getDrawable(R.drawable.bartchilling));
            animateBrick(brickNine);
        }
        if (totalGuesses == 7) {
            animateBrick(brickEight);
        }
        if (totalGuesses == 6) {
            animateBrick(brickSeven);
        }
        if (totalGuesses == 5) {
            personImageView.setImageDrawable(getDrawable(R.drawable.bartjumping));
            animateBrick(brickSix);
        }
        if (totalGuesses == 4) {
            animateBrick(brickFive);
        }
        if (totalGuesses == 3) {
            personImageView.setImageDrawable(getDrawable(R.drawable.bartscared));
            animateBrick(brickFour);
        }
        if (totalGuesses == 2) {
            guessesRemainingTextView.setTextColor(getResources().getColor(R.color.low_guesses_color));
            animateBrick(brickThree);
        }
        if (totalGuesses == 1) {
            personImageView.setImageDrawable(getDrawable(R.drawable.bartnervous));
            animateBrick(brickTwo);
        }
        if (totalGuesses == 0 && matchCounter != 4) {
            personImageView.setImageDrawable(getDrawable(R.drawable.bartfalling));
            brickOne.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_out_right));
            personImageView.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.exit_bottom));
            brickOne.setVisibility(View.INVISIBLE);

            displayHintsAndGameStatusTextview.setText(R.string.you_lost_text);
            combinationLinearLayout.setVisibility(View.VISIBLE);

            NoMoreGuesses noMoreGuessesDialog = new NoMoreGuesses();
            noMoreGuessesDialog.setArguments(winningCombinationBundle);
            noMoreGuessesDialog.show(getSupportFragmentManager(), "NoMoreGuesses");

            disableButtons();
        }
    }

    private void disableButtons() {
        guessButton.setEnabled(false);
        deleteButton.setEnabled(false);
        hintButton.setEnabled(false);
        zeroButton.setEnabled(false);
        oneButton.setEnabled(false);
        twoButton.setEnabled(false);
        threeButton.setEnabled(false);
        fourButton.setEnabled(false);
        fiveButton.setEnabled(false);
        sixButton.setEnabled(false);
        sevenButton.setEnabled(false);
        revealButton.setEnabled(false);
    }

    private void blinkAnimation(final LinearLayout linearLayout, final TextView timeHeaderTV, final TextView timeTV) {

        animationRunning = true;

        linearLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.blink));

        timeHeaderTV.setTextColor(getResources().getColor(R.color.userWonColor));
        timeHeaderTV.setTextSize(20);

        int minutes = (int) (score / 1000 / 60);
        int seconds = (int) (score / 1000) % 60;

        String newHighScoreInMinsAndSeconds = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        timeTV.setTextColor(getResources().getColor(R.color.userWonColor));
        timeTV.setText(newHighScoreInMinsAndSeconds);
        timeTV.setTextSize(20);

        blinkingHandler = new Handler();
        blinkingHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                int minutes = (int) (timeLeftInMillis / 1000 / 60);
                int seconds = (int) (timeLeftInMillis / 1000) % 60;

                String time = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

                linearLayout.clearAnimation();
                timeHeaderTV.setTextColor(getResources().getColor(R.color.textColor));
                timeHeaderTV.setTextSize(16);

                timeTV.setTextColor(getResources().getColor(R.color.textColor));
                timeTV.setTextSize(16);
                timeTV.setText(time);

            }
        }, 4000);
    }

    private void animateBrick(final ImageView brick) {
        brick.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_out_right));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                brick.setVisibility(View.INVISIBLE);
            }
        }, 200);
    }

    private void pickAHintToDisplay() {

        String[] hints = {"There is no chance the number to guess is negative.", "C'mon! The combination is 4 digits long.", "At least one of the numbers above is in the combo.",
                "You have " + totalGuesses + " guesses remaining!", "You have " + countDownTimerTextView.getText().toString() + " left!", "Haha! Not happening!",
                "I could but where's the fun in that?", "It's only 4 digits. You got this!", "I would've solved it already.", "FINE! There's a " + combination.charAt(2) + " include somewhere.",
                "You're running out of time!", "Okay a " + combination.charAt(0) + " is included somewhere."};

        Collections.shuffle(Arrays.asList(hints));
        String displayHint = hints[2];

        displayHintsAndGameStatusTextview.setText(displayHint);
        displayHintsAndGameStatusTextview.setTextColor(getResources().getColor(R.color.hintColor));
    }

    private void checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        connectedToInternet = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

        if (connectedToInternet) {

            getRandomNumbers();
        } else {

            pickRandomNumbersLocally();
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

                    String[] separatedResponse = randomNumbersResponse.split("\\s+");

                    String firstNumber = separatedResponse[0];
                    String secondNumber = separatedResponse[1];
                    String thirdNumber = separatedResponse[2];
                    String fourthNumber = separatedResponse[3];

                    combination = firstNumber + secondNumber + thirdNumber + fourthNumber;
                    comboList.add(combination);
                    winningCombinationBundle.putString("combination", combination);

                    String[] numbers = {"0", "1", "2", "3", "4", "5", "6", "7"};
                    Collections.shuffle(Arrays.asList(numbers));

                    final String[] eightDisplayedNumbers = {numbers[0], numbers[2], numbers[4], numbers[6], firstNumber, secondNumber, thirdNumber, fourthNumber};
                    Collections.shuffle(Arrays.asList(eightDisplayedNumbers));

                    Log.d("EIGHT", eightDisplayedNumbers[0] + eightDisplayedNumbers[1] + eightDisplayedNumbers[2] + eightDisplayedNumbers[3] + eightDisplayedNumbers[4] + eightDisplayedNumbers[5] + eightDisplayedNumbers[6] + eightDisplayedNumbers[7]);
                    Log.d("EIGHTCOMBO", combination);

                    Log.e("Combination", combination);
                    Log.e("Combination", numbers[2]);

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            startCountDown();

                            combinationTextView.setText(combination);

                            firstNumberTextView.setText(eightDisplayedNumbers[5]);
                            secondNumberTextView.setText(eightDisplayedNumbers[1]);
                            thirdNumberTextView.setText(eightDisplayedNumbers[3]);
                            fourthNumberTextView.setText(eightDisplayedNumbers[0]);
                            fifthNumberTextView.setText(eightDisplayedNumbers[2]);
                            sixthNumberTextView.setText(eightDisplayedNumbers[7]);
                            seventhNumberTextView.setText(eightDisplayedNumbers[4]);
                            eighthNumberTextView.setText(eightDisplayedNumbers[6]);

                            revealButton.setEnabled(true);
                            guessButton.setEnabled(true);
                            hintButton.setEnabled(true);
                        }

                    });
                }
            }
        });
    }


    private void pickRandomNumbersLocally() {

        startCountDown();

        String[] localNumbers = {"0", "1", "2", "3", "4", "5", "6", "7", "0", "1", "2", "3", "4", "5", "6", "7", "0", "1", "2", "3", "4", "5", "6", "7", "0", "1", "2", "3", "4", "5", "6", "7"};
        Collections.shuffle(Arrays.asList(localNumbers));

        String firstLocalNumber = localNumbers[7];
        String secondLocalNumber = localNumbers[14];
        String thirdLocalNumber = localNumbers[21];
        String fourthLocalNumber = localNumbers[3];

        combination = fourthLocalNumber + thirdLocalNumber + secondLocalNumber + firstLocalNumber;
        comboList.add(combination);
        winningCombinationBundle.putString("combination", combination);

        final String[] eightLocalDisplayedNumbers = {firstLocalNumber, secondLocalNumber, thirdLocalNumber, fourthLocalNumber, localNumbers[16], localNumbers[20], localNumbers[22], localNumbers[26]};
        Collections.shuffle(Arrays.asList(eightLocalDisplayedNumbers));

        Log.d("LOCALEIGHT", eightLocalDisplayedNumbers[0] + eightLocalDisplayedNumbers[1] + eightLocalDisplayedNumbers[2] + eightLocalDisplayedNumbers[3] + eightLocalDisplayedNumbers[4] + eightLocalDisplayedNumbers[5] + eightLocalDisplayedNumbers[6] + eightLocalDisplayedNumbers[7]);
        Log.d("LOCALEIGHTCOMBO", combination);

        Log.e("LOCALCombination", combination);
        Log.e("LOCALCombination", localNumbers[2]);

        combinationTextView.setText(combination);

        firstNumberTextView.setText(eightLocalDisplayedNumbers[5]);
        secondNumberTextView.setText(eightLocalDisplayedNumbers[1]);
        thirdNumberTextView.setText(eightLocalDisplayedNumbers[3]);
        fourthNumberTextView.setText(eightLocalDisplayedNumbers[0]);
        fifthNumberTextView.setText(eightLocalDisplayedNumbers[2]);
        sixthNumberTextView.setText(eightLocalDisplayedNumbers[7]);
        seventhNumberTextView.setText(eightLocalDisplayedNumbers[4]);
        eighthNumberTextView.setText(eightLocalDisplayedNumbers[6]);

        revealButton.setEnabled(true);
        guessButton.setEnabled(true);
        hintButton.setEnabled(true);
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

        if (blinkingHandler != null) {
            blinkingHandler.removeCallbacksAndMessages(null);
        }

        if (displayNewHighScoreDialogHandler != null) {
            displayNewHighScoreDialogHandler.removeCallbacksAndMessages(null);
        }
        overridePendingTransition(0, 0);
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
}

