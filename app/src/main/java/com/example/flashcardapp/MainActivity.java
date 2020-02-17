package com.example.flashcardapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    boolean isShowingAnswer = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.card_question).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.card_question).setVisibility(View.INVISIBLE);
                findViewById(R.id.card_answer).setVisibility(View.VISIBLE);
                isShowingAnswer = true;
            }
        });

        findViewById(R.id.toggle_choices_visibility).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isShowingAnswer){
                    findViewById(R.id.card_answer).setVisibility(View.INVISIBLE);
                    findViewById(R.id.card_question).setVisibility(View.VISIBLE);
                    isShowingAnswer = false;
                }
                else{
                    findViewById(R.id.card_answer).setVisibility(View.VISIBLE);
                    findViewById(R.id.card_question).setVisibility(View.INVISIBLE);
                    isShowingAnswer = true;
                }
            }
        });
    }
}
