package org.ohmstheresistance.mastermind.dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.ohmstheresistance.mastermind.R;
import org.ohmstheresistance.mastermind.activities.MainPageActivity;
import org.ohmstheresistance.mastermind.database.UserInfoDatabaseHelper;
import org.ohmstheresistance.mastermind.model.UserInfo;

public class AddUserInfoDialog extends DialogFragment implements View.OnClickListener, View.OnTouchListener {

    private EditText addUserInfoEditText;
    private Button addUserInfoEnterButton;
    public String userName;
    private UserInfoDatabaseHelper userInfoDatabaseHelper;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getActivity(), R.style.WideDialog);
    }

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_user_info_dialog, container, false);
        setCancelable(false);

        addUserInfoEditText = view.findViewById(R.id.add_user_info_edittext);
        addUserInfoEnterButton = view.findViewById(R.id.add_user_info_enter_button);

        userInfoDatabaseHelper = UserInfoDatabaseHelper.getInstance(getActivity());

        addUserInfoEnterButton.setOnClickListener(this);
        addUserInfoEnterButton.setOnTouchListener(this);

        return view;
    }
    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {

            case R.id.add_user_info_enter_button:

                if (TextUtils.isEmpty(addUserInfoEditText.getText())) {

                    Toast.makeText(getActivity(), "This field cannot be empty.", Toast.LENGTH_LONG).show();

                } else {

                    userName = addUserInfoEditText.getText().toString();
                    userInfoDatabaseHelper.addUserInfo(UserInfo.from(userName));

                    Intent toMainPageIntent = new Intent(getContext(), MainPageActivity.class);
                    startActivity(toMainPageIntent);
                    getActivity().finish();
                    getActivity().overridePendingTransition(0, 0);
                    getDialog().dismiss();
                }
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
