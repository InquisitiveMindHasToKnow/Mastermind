package org.ohmstheresistance.mastermind.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.ohmstheresistance.mastermind.R;

public class MastermindInstructions extends DialogFragment {

    private Button gotItButton;
    private Button playNowFromInstructionsButton;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mastermind_intructions, container, false);


        gotItButton = view.findViewById(R.id.got_it_button);
        playNowFromInstructionsButton = view.findViewById(R.id.play_now_from_instructions_button);

        gotItButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        playNowFromInstructionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
                Intent navigationIntent = new Intent(getContext(), MainActivity.class);
                startActivity(navigationIntent);
                getActivity().overridePendingTransition(0, 0);
            }
        });
        return view;
    }
}
