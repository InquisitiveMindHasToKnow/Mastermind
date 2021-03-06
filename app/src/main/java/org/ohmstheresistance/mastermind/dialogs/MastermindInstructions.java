package org.ohmstheresistance.mastermind.dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.ohmstheresistance.mastermind.R;
import org.ohmstheresistance.mastermind.activities.MainActivity;

import static org.ohmstheresistance.mastermind.activities.MainPageActivity.MASTERMIND_REQUEST_CODE;

public class MastermindInstructions extends DialogFragment implements View.OnClickListener, View.OnTouchListener {

    private Button gotItButton;
    private Button playNowFromInstructionsButton;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getActivity(), R.style.WideDialog);
    }

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mastermind_intructions, container, false);


        gotItButton = view.findViewById(R.id.got_it_button);
        playNowFromInstructionsButton = view.findViewById(R.id.play_now_from_instructions_button);

        playNowFromInstructionsButton.setOnTouchListener(this);
        gotItButton.setOnTouchListener(this);

        playNowFromInstructionsButton.setOnClickListener(this);
        gotItButton.setOnClickListener(this);

        return view;
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

            case R.id.got_it_button:

                getDialog().dismiss();
                break;

            case R.id.play_now_from_instructions_button:

                getDialog().dismiss();
                Intent navigationIntent = new Intent(getActivity(), MainActivity.class);
                getActivity().startActivityForResult(navigationIntent, MASTERMIND_REQUEST_CODE);
                getActivity().overridePendingTransition(0, 0);
                break;
        }
    }
}
