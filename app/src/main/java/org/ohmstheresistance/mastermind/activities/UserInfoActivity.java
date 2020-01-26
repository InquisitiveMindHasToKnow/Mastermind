package org.ohmstheresistance.mastermind.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;

import org.ohmstheresistance.mastermind.R;
import org.ohmstheresistance.mastermind.database.UserInfoDatabaseHelper;
import org.ohmstheresistance.mastermind.model.UserInfo;

public class UserInfoActivity extends AppCompatActivity {

    public String userName;
   private UserInfoDatabaseHelper userInfoDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_activty);

        userInfoDatabaseHelper = UserInfoDatabaseHelper.getInstance(this);

        final AlertDialog.Builder alert = new AlertDialog.Builder(this, R.style.UserInfoDialog);

        final EditText getUserNameEdittext = new EditText(this);

        int maxLength = 12;
        getUserNameEdittext.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
        getUserNameEdittext.setTextColor(getResources().getColor(R.color.dialogEditTextTextColor));
        getUserNameEdittext.setGravity(Gravity.CENTER);

        alert.setTitle("Hello!");
        alert.setMessage("Please enter your name.");
        alert.setCancelable(false);
        alert.setView(getUserNameEdittext);

        alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                if (!TextUtils.isEmpty(getUserNameEdittext.getText())) {

                    userName = getUserNameEdittext.getText().toString();
                    userInfoDatabaseHelper.addUserInfo(UserInfo.from(userName));

                    Intent toMainPageIntent = new Intent(UserInfoActivity.this, MainPageActivity.class);
                    startActivity(toMainPageIntent);
                    overridePendingTransition(0, 0);
                    UserInfoActivity.this.finish();
                }

            }
        });

        alert.show();

    }
}
