package com.example.flashcardapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

                String question = ((EditText) findViewById(R.id.question)).getText().toString();

                String a1 = ((EditText) findViewById(R.id.answer1)).getText().toString();
                String a2 = ((EditText) findViewById(R.id.answer2)).getText().toString();
                String a3 = ((EditText) findViewById(R.id.answer3)).getText().toString();

                if(!isValid(question, a1, a2, a3)){
                    Toast.makeText(getApplicationContext(), "Must enter data into all fields", Toast.LENGTH_SHORT).show();
                }else {
                    Intent getNew = new Intent();
                    getNew.putExtra("question", question);
                    getNew.putExtra("answer1", a1);
                    getNew.putExtra("answer2", a2);
                    getNew.putExtra("answer3", a3);
                    getNew.putExtra("correctAnswer", a1);
                    setResult(RESULT_OK, getNew);
                    finish();
                }
            }
        });
    }

    private boolean isValid(String q, String a1, String a2, String a3){
        if(q.matches("") || a1.matches("") || a2.matches("") || a3.matches("") ) return false;
        return true;
    }
}
