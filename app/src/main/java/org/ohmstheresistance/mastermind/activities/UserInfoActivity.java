package org.ohmstheresistance.mastermind.activities;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.ohmstheresistance.mastermind.R;
import org.ohmstheresistance.mastermind.dialogs.AddUserInfoDialog;

public class UserInfoActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_activty);

        AddUserInfoDialog addUserInfoDialog = new AddUserInfoDialog();
        addUserInfoDialog.show(getSupportFragmentManager(), "AddUserInfoDialog");
    }
}