package com.example.flashcardapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class AddCardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        findViewById(R.id.cancel_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.save_new_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<String> answers = new ArrayList<>();
                String question = ((EditText) findViewById(R.id.question)).getText().toString();

                answers.add(((EditText) findViewById(R.id.answer1)).getText().toString());
                answers.add(((EditText) findViewById(R.id.answer2)).getText().toString());
                answers.add(((EditText) findViewById(R.id.answer3)).getText().toString());

                if(!isValid(answers, question)){

                }else{
                    Intent getNew = new Intent();
                    getNew.putExtra("question", question);
                    getNew.putExtra("answers", answers);
                    getNew.putExtra("correctAnswer", answers.get(0));
                    setResult(RESULT_OK, getNew);
                    finish();
                }
                Toast.makeText(getApplicationContext(), "Must enter data into all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValid(ArrayList<String> answers, String question){
        for(String x: answers){
            if(x.matches("")) return false;
        }

        if(question.matches("")) return false;

        return true;
    }
}
