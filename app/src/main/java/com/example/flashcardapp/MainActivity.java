package com.example.flashcardapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

        findViewById(R.id.add_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add = new Intent(MainActivity.this, AddCardActivity.class);
                MainActivity.this.startActivityForResult(add, 1);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 1 && resultCode == RESULT_OK){
            String q = data.getExtras().getString("question");
            String a = data.getExtras().getString("answer");

            ((TextView) findViewById(R.id.card_question)).setText(q);
            ((TextView) findViewById(R.id.card_answer)).setText(a);
            System.out.println("here");
        }
    }
}
