package org.ohmstheresistance.mastermind.dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.ohmstheresistance.mastermind.R;
import org.ohmstheresistance.mastermind.activities.MainActivity;

import java.util.Locale;

import static org.ohmstheresistance.mastermind.activities.MainActivity.NEW_HIGH_SCORE_KEY;

public class NewHighScoreDialog extends DialogFragment implements View.OnClickListener, View.OnTouchListener {

    private Button newHighScoreConfirmButton, newHighScorePlayAgainButton;
    private TextView dialogWinningCombinationTextView, highScoreTimeTextView;
    private Bundle getCombinationBundle;
    private long newHighScore;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getActivity(), R.style.WideDialog);
    }

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_high_score, container, false);

        newHighScoreConfirmButton = view.findViewById(R.id.new_high_score_confirm_button);
        newHighScorePlayAgainButton = view.findViewById(R.id.new_high_score_play_again_button);
        dialogWinningCombinationTextView = view.findViewById(R.id.new_high_score_dialog_combination_textview);
        highScoreTimeTextView = view.findViewById(R.id.new_high_score_time_textview);

        getCombinationBundle = getArguments();
        String winningCombo = getCombinationBundle.getString("combination");
        newHighScore = getCombinationBundle.getLong(NEW_HIGH_SCORE_KEY);

        int minutes = (int) (newHighScore / 1000 / 60);
        int seconds = (int) (newHighScore / 1000) % 60;

        String newHighScoreInMinsAndSeconds = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        dialogWinningCombinationTextView.setText("Winning Combo: " + winningCombo);
        highScoreTimeTextView.setText("Record time: "+ newHighScoreInMinsAndSeconds);

        newHighScoreConfirmButton.setOnClickListener(this);
        newHighScorePlayAgainButton.setOnClickListener(this);
        newHighScoreConfirmButton.setOnTouchListener(this);
        newHighScorePlayAgainButton.setOnTouchListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {

            case R.id.new_high_score_confirm_button:

                getDialog().dismiss();
                break;

            case R.id.new_high_score_play_again_button:

                playAgain();
                break;
        }

    }

    private void playAgain() {

        getDialog().dismiss();
        ((MainActivity)getActivity()).resetGame();
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
