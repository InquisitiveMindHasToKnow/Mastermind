package org.ohmstheresistance.mastermind.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.ohmstheresistance.mastermind.R;

public class MainPageActivity extends AppCompatActivity {

    private Button playNowButton;
    private Button rulesButton;
    private Intent navigationIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        setViews();
    }

    private void setViews(){

        playNowButton = findViewById(R.id.play_now_button);
        rulesButton = findViewById(R.id.rules_button);

        playNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navigationIntent = new Intent(MainPageActivity.this, MainActivity.class);
                startActivity(navigationIntent);

            }
        });

        rulesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                navigationIntent = new Intent(MainPageActivity.this, GameRules.class);
//                startActivity(navigationIntent);
                Toast.makeText(MainPageActivity.this, "Game Rules", Toast.LENGTH_SHORT).show();
            }
        });

    }

}

