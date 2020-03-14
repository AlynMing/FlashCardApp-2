package com.example.flashcardapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    boolean isShowingAnswer = false;
    String answer = "Barack Obama";
    FlashcardDatabase flashcardDatabase;
    List<Flashcard> allFlashCards;
    int currrentCardDisplayedIndex = 0;
    boolean noFlashCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flashcardDatabase = new FlashcardDatabase(getApplicationContext());
        allFlashCards = flashcardDatabase.getAllCards();

        if (allFlashCards != null && allFlashCards.size() > 0) {
            ((TextView) findViewById(R.id.card_question)).setText(allFlashCards.get(0).getQuestion());
            ((TextView) findViewById(R.id.answer1)).setText(allFlashCards.get(0).getAnswer());
            ((TextView) findViewById(R.id.answer2)).setText(allFlashCards.get(0).getWrongAnswer1());
            ((TextView) findViewById(R.id.answer3)).setText(allFlashCards.get(0).getWrongAnswer2());
            noFlashCards = false;
        }else{
            noFlashCards = true;
        }

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

        findViewById(R.id.next_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allFlashCards.size() > 0){
                    // advance our pointer index so we can show the next card
                    currrentCardDisplayedIndex++;

                    // make sure we don't get an IndexOutOfBoundsError if we are viewing the last indexed card in our list
                    if (currrentCardDisplayedIndex > allFlashCards.size() - 1) {
                        currrentCardDisplayedIndex = 0;
                    }

                    // set the question and answer TextViews with data from the database
                    ((TextView) findViewById(R.id.card_question)).setText(allFlashCards.get(currrentCardDisplayedIndex).getQuestion());
                    ((TextView) findViewById(R.id.answer1)).setText(allFlashCards.get(currrentCardDisplayedIndex).getAnswer());
                    ((TextView) findViewById(R.id.answer2)).setText(allFlashCards.get(currrentCardDisplayedIndex).getWrongAnswer1());
                    ((TextView) findViewById(R.id.answer3)).setText(allFlashCards.get(currrentCardDisplayedIndex).getWrongAnswer2());
                    answer = allFlashCards.get(currrentCardDisplayedIndex).getAnswer();

                    //change colors
                    findViewById(R.id.answer1).setBackgroundColor(getResources().getColor(R.color.ans1));
                    findViewById(R.id.answer2).setBackgroundColor(getResources().getColor(R.color.ans2));
                    findViewById(R.id.answer3).setBackgroundColor(getResources().getColor(R.color.ans3));

                }
            }
        });

        findViewById(R.id.delete_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashcardDatabase.deleteCard(((TextView) findViewById(R.id.card_question)).getText().toString());
                allFlashCards = flashcardDatabase.getAllCards();

                if(allFlashCards.size() != 0){
                    // make sure we don't get an IndexOutOfBoundsError if we are viewing the last indexed card in our list
                    if (currrentCardDisplayedIndex > allFlashCards.size() - 1) {
                        currrentCardDisplayedIndex = 0;
                    }

                    // set the question and answer TextViews with data from the database
                    ((TextView) findViewById(R.id.card_question)).setText(allFlashCards.get(currrentCardDisplayedIndex).getQuestion());
                    ((TextView) findViewById(R.id.answer1)).setText(allFlashCards.get(currrentCardDisplayedIndex).getAnswer());
                    ((TextView) findViewById(R.id.answer2)).setText(allFlashCards.get(currrentCardDisplayedIndex).getWrongAnswer1());
                    ((TextView) findViewById(R.id.answer3)).setText(allFlashCards.get(currrentCardDisplayedIndex).getWrongAnswer2());
                }else{
                    ((TextView) findViewById(R.id.card_question)).setText("Add a FlashCard");
                    ((TextView) findViewById(R.id.answer1)).setText("");
                    ((TextView) findViewById(R.id.answer2)).setText("");
                    ((TextView) findViewById(R.id.answer3)).setText("");

                    findViewById(R.id.answer1).setVisibility(View.INVISIBLE);
                    findViewById(R.id.answer2).setVisibility(View.INVISIBLE);
                    findViewById(R.id.answer3).setVisibility(View.INVISIBLE);
                    isShowingAnswer = false;

                    //change colors
                    findViewById(R.id.answer1).setBackgroundColor(getResources().getColor(R.color.ans1));
                    findViewById(R.id.answer2).setBackgroundColor(getResources().getColor(R.color.ans2));
                    findViewById(R.id.answer3).setBackgroundColor(getResources().getColor(R.color.ans3));
                }
            }
        });
    }

    private void changeVisibility(){
        if(isShowingAnswer ){
            findViewById(R.id.answer1).setVisibility(View.INVISIBLE);
            findViewById(R.id.answer2).setVisibility(View.INVISIBLE);
            findViewById(R.id.answer3).setVisibility(View.INVISIBLE);
            isShowingAnswer = false;

            //change colors
            findViewById(R.id.answer1).setBackgroundColor(getResources().getColor(R.color.ans1));
            findViewById(R.id.answer2).setBackgroundColor(getResources().getColor(R.color.ans2));
            findViewById(R.id.answer3).setBackgroundColor(getResources().getColor(R.color.ans3));

        } else {
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

            String answer1 = data.getExtras().getString("answer1");
            String answer2 = data.getExtras().getString("answer2");
            String answer3 = data.getExtras().getString("answer3");

            String question = data.getExtras().getString("question");

            ((TextView) findViewById(R.id.card_question)).setText(question);
            ((TextView) findViewById(R.id.answer1)).setText(answer1);
            ((TextView) findViewById(R.id.answer2)).setText(answer2);
            ((TextView) findViewById(R.id.answer3)).setText(answer3);

            answer = data.getExtras().getString("correctAnswer");

            Flashcard f = new Flashcard(question, answer);
            f.setWrongAnswer1(answer2);
            f.setWrongAnswer2(answer3);

            flashcardDatabase.insertCard(f);
            allFlashCards = flashcardDatabase.getAllCards();
            noFlashCards = false;

            //Toast.makeText(getApplicationContext(), "Card Successfully Changed!", Toast.LENGTH_SHORT).show();
            Snackbar.make(findViewById(R.id.card_question),
                    "Card Successfully Changed!",
                    Snackbar.LENGTH_SHORT)
                    .show();
        }
    }
}
