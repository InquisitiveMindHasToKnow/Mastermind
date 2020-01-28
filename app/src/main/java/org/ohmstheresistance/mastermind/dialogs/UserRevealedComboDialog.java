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

public class UserRevealedComboDialog extends DialogFragment implements View.OnClickListener, View.OnTouchListener {

    private Button revealedCombinationConfirmButton, revealedCombinationPlayAgainButton;
    private TextView dialogWinningCombinationTextView;
    private Bundle getCombinationBundle;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getActivity(), R.style.WideDialog);
    }

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_revealed_combo, container, false);

        revealedCombinationConfirmButton = view.findViewById(R.id.revealed_combination_confirm_button);
        revealedCombinationPlayAgainButton = view.findViewById(R.id.revealed_combination_play_again_button);
        dialogWinningCombinationTextView = view.findViewById(R.id.revealed_combination_winning_combination_textview);

        getCombinationBundle = getArguments();
        String winningCombo = getCombinationBundle.getString("combination");


        dialogWinningCombinationTextView.setText("Winning Combo: " + winningCombo);

        revealedCombinationConfirmButton.setOnClickListener(this);
        revealedCombinationPlayAgainButton.setOnClickListener(this);

        revealedCombinationConfirmButton.setOnTouchListener(this);
        revealedCombinationPlayAgainButton.setOnTouchListener(this);

        return view;
    }

    private void playAgain() {

        getDialog().dismiss();
        ((MainActivity)getActivity()).resetGame();
    }


    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {

            case R.id.revealed_combination_confirm_button:

                getDialog().dismiss();
                break;

            case R.id.revealed_combination_play_again_button:
                playAgain();
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
