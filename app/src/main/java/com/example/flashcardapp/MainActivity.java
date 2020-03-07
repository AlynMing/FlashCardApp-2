package com.example.flashcardapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    boolean isShowingAnswer = false;
    String answer = "Barack Obama";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.answer1).setVisibility(View.INVISIBLE);
        findViewById(R.id.answer2).setVisibility(View.INVISIBLE);
        findViewById(R.id.answer3).setVisibility(View.INVISIBLE);
        isShowingAnswer = false;

        findViewById(R.id.plus_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add = new Intent(MainActivity.this, AddCardActivity.class);
                MainActivity.this.startActivityForResult(add, 1);
            }
        });

        findViewById(R.id.toggle_choices_visibility).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeVisibility();
            }
        });

        findViewById(R.id.card_question).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeVisibility();
            }
        });

        findViewById(R.id.answer1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView t = findViewById(R.id.answer1);
                checkCorrect(t);
            }
        });

        findViewById(R.id.answer2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView t = findViewById(R.id.answer2);
                checkCorrect(t);
            }
        });

        findViewById(R.id.answer3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView t = findViewById(R.id.answer3);
                checkCorrect(t);
            }
        });
    }

    private void changeVisibility(){
        if(isShowingAnswer){
            findViewById(R.id.answer1).setVisibility(View.INVISIBLE);
            findViewById(R.id.answer2).setVisibility(View.INVISIBLE);
            findViewById(R.id.answer3).setVisibility(View.INVISIBLE);
            isShowingAnswer = false;

            //change colors
            findViewById(R.id.answer1).setBackgroundColor(getResources().getColor(R.color.ans1));
            findViewById(R.id.answer2).setBackgroundColor(getResources().getColor(R.color.ans2));
            findViewById(R.id.answer3).setBackgroundColor(getResources().getColor(R.color.ans3));

        }
        else{
            findViewById(R.id.answer1).setVisibility(View.VISIBLE);
            findViewById(R.id.answer2).setVisibility(View.VISIBLE);
            findViewById(R.id.answer3).setVisibility(View.VISIBLE);

            isShowingAnswer = true;
        }
    }

    private void checkCorrect(TextView t){
        if(answer.equals(t.getText().toString())) {
            t.setBackgroundColor(getResources().getColor(R.color.green));
        }else{
            t.setBackgroundColor(getResources().getColor(R.color.red));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 1 && resultCode == RESULT_OK){

            ArrayList<String> answers = data.getExtras().getStringArrayList("answers");

            Collections.shuffle(answers); //to shuffle the answers

            ((TextView) findViewById(R.id.card_question)).setText(data.getExtras().getString("question"));
            ((TextView) findViewById(R.id.answer1)).setText(answers.get(0));
            ((TextView) findViewById(R.id.answer2)).setText(answers.get(1));
            ((TextView) findViewById(R.id.answer3)).setText(answers.get(2));

            answer = data.getExtras().getString("correctAnswer");

            //Toast.makeText(getApplicationContext(), "Card Successfully Changed!", Toast.LENGTH_SHORT).show();
            Snackbar.make(findViewById(R.id.card_question),
                    "Card Successfully Changed!",
                    Snackbar.LENGTH_SHORT)
                    .show();
        }
    }
}
