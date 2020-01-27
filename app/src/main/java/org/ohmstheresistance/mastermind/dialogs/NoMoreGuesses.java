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

public class NoMoreGuesses extends DialogFragment implements View.OnClickListener, View.OnTouchListener {

    private TextView correctCombinationTextView;
    private Button noMoreGuessesConfirmButton;
    private Bundle getCombinationBundle;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getActivity(), R.style.WideDialog);
    }

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.no_guesses_left_loser, container, false);

        correctCombinationTextView = view.findViewById(R.id.no_more_guesses_winning_combination_textview);
        noMoreGuessesConfirmButton = view.findViewById(R.id.no_more_guesses_confirm_button);

        getCombinationBundle = getArguments();
        String winningCombo = getCombinationBundle.getString("combination");

        correctCombinationTextView.setText("Winning combination: " + winningCombo);

        noMoreGuessesConfirmButton.setOnClickListener(this);
        noMoreGuessesConfirmButton.setOnTouchListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {

            case R.id.no_more_guesses_confirm_button:

                getDialog().dismiss();
                break;
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
}
