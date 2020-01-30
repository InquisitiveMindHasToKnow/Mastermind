package org.ohmstheresistance.mastermind.dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import android.widget.TextView;
import android.widget.Toast;

import org.ohmstheresistance.mastermind.R;
import org.ohmstheresistance.mastermind.database.UserInfoDatabaseHelper;
import org.ohmstheresistance.mastermind.model.UserInfo;

public class EditUserInfoDialog extends DialogFragment implements View.OnClickListener, View.OnTouchListener {

    private TextView userNameTextView, notUserTextView;
    private EditText editUserInfoEditText;
    private Button editUserInfoCancelButton, editUserInfoUpdateButton;
    private UserInfoDatabaseHelper userInfoDatabaseHelper;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getActivity(), R.style.WideDialog);
    }

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_user_info_dialog, container, false);

        editUserInfoEditText = view.findViewById(R.id.edit_user_info_edittext);
        userNameTextView = getActivity().findViewById(R.id.user_name_textview);
        notUserTextView = getActivity().findViewById(R.id.not_user_textview);
        editUserInfoUpdateButton = view.findViewById(R.id.edit_user_info_update_button);
        editUserInfoCancelButton = view.findViewById(R.id.edit_user_info_cancel_button);

        userInfoDatabaseHelper = UserInfoDatabaseHelper.getInstance(getActivity());


        editUserInfoUpdateButton.setOnClickListener(this);
        editUserInfoCancelButton.setOnClickListener(this);
        editUserInfoUpdateButton.setOnTouchListener(this);
        editUserInfoCancelButton.setOnTouchListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {

            case R.id.edit_user_info_update_button:

                if (TextUtils.isEmpty(editUserInfoEditText.getText())) {

                    Toast.makeText(getActivity(), "This field cannot be empty.", Toast.LENGTH_LONG).show();

                } else {
                    String newUserName = editUserInfoEditText.getText().toString();

                    userInfoDatabaseHelper.updateUserName(UserInfo.from(newUserName));

                    userNameTextView.setText(newUserName + "!");
                    notUserTextView.setText("Not " + newUserName + "?");

                    getDialog().dismiss();

                }
                break;

            case R.id.edit_user_info_cancel_button:

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
