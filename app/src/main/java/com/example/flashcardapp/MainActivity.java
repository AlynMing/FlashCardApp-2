package com.example.flashcardapp;

import android.animation.Animator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.plattysoft.leonids.ParticleSystem;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    boolean isShowingAnswer = false;
    String answer = "Barack Obama";
    FlashcardDatabase flashcardDatabase;
    List<Flashcard> allFlashCards;
    int currrentCardDisplayedIndex = 0;
    boolean noFlashCards;
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flashcardDatabase = new FlashcardDatabase(getApplicationContext());
        allFlashCards = flashcardDatabase.getAllCards();
        countDownTimer = new CountDownTimer(16000, 1000) {
            public void onTick(long millisUntilFinished) {
                ((TextView) findViewById(R.id.timer)).setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                ((TextView) findViewById(R.id.timer)).setText("Time Out!");
            }
        };

        if (allFlashCards != null && allFlashCards.size() > 0) {
            ((TextView) findViewById(R.id.card_question)).setText(allFlashCards.get(0).getQuestion());
            ((TextView) findViewById(R.id.answer1)).setText(allFlashCards.get(0).getAnswer());
            ((TextView) findViewById(R.id.answer2)).setText(allFlashCards.get(0).getWrongAnswer1());
            ((TextView) findViewById(R.id.answer3)).setText(allFlashCards.get(0).getWrongAnswer2());
            answer = allFlashCards.get(0).getAnswer();
            noFlashCards = false;
            startTimer();
        } else {
            noFlashCards = true;
            startTimer();
        }

        makeInvisible();

        findViewById(R.id.plus_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add = new Intent(MainActivity.this, AddCardActivity.class);
                MainActivity.this.startActivityForResult(add, 1);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
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
                if (allFlashCards.size() > 0) {
                    //get animations
                    final Animation rightInAnim = AnimationUtils.loadAnimation(v.getContext(), R.anim.right_in);
                    final Animation leftOutAnim = AnimationUtils.loadAnimation(v.getContext(), R.anim.left_out);

                    leftOutAnim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            // this method is called when the animation first starts
                            overridePendingTransition(R.anim.left_out_animation, R.anim.right_in);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            // this method is called when the animation is finished playing
                            overridePendingTransition(R.anim.left_in, R.anim.right_out);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                            // we don't need to worry about this method
                        }
                    });

                    nextCard();
                    findViewById(R.id.card_question).startAnimation(leftOutAnim);
                }
            }
        });

        findViewById(R.id.delete_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashcardDatabase.deleteCard(((TextView) findViewById(R.id.card_question)).getText().toString());
                allFlashCards = flashcardDatabase.getAllCards();

                if (allFlashCards.size() != 0) {
                    // make sure we don't get an IndexOutOfBoundsError if we are viewing the last indexed card in our list
                    if (currrentCardDisplayedIndex > allFlashCards.size() - 1) {
                        currrentCardDisplayedIndex = 0;
                    }
                    setNextCard();
                    makeInvisible();
                } else {
                    makeInvisible();
                    makeAllPadsEmpty();
                    changeAnswerColors();
                    ((TextView) findViewById(R.id.timer)).setText("");
                }
            }
        });
    }

    private void startTimer() {
        countDownTimer.cancel();
        countDownTimer.start();
    }
    private void nextCard() {
        // advance our pointer index so we can show the next card
        currrentCardDisplayedIndex++;

        // make sure we don't get an IndexOutOfBoundsError if we are viewing the last indexed card in our list
        if (currrentCardDisplayedIndex > allFlashCards.size() - 1) {
            currrentCardDisplayedIndex = 0;
        }
        setNextCard();
        makeInvisible();
        answer = allFlashCards.get(currrentCardDisplayedIndex).getAnswer();
        changeAnswerColors();
    }


    private void setNextCard() {
        // set the question and answer TextViews with data from the database
        ((TextView) findViewById(R.id.card_question)).setText(allFlashCards.get(currrentCardDisplayedIndex).getQuestion());
        ((TextView) findViewById(R.id.answer1)).setText(allFlashCards.get(currrentCardDisplayedIndex).getAnswer());
        ((TextView) findViewById(R.id.answer2)).setText(allFlashCards.get(currrentCardDisplayedIndex).getWrongAnswer1());
        ((TextView) findViewById(R.id.answer3)).setText(allFlashCards.get(currrentCardDisplayedIndex).getWrongAnswer2());
        answer = allFlashCards.get(currrentCardDisplayedIndex).getAnswer();
        startTimer();
    }

    private void makeAllPadsEmpty() {
        ((TextView) findViewById(R.id.card_question)).setText("Add a FlashCard");
        ((TextView) findViewById(R.id.answer1)).setText("");
        ((TextView) findViewById(R.id.answer2)).setText("");
        ((TextView) findViewById(R.id.answer3)).setText("");
        answer = "";
    }

    private void changeVisibility() {
        if (isShowingAnswer) {
            makeInvisible();
            changeAnswerColors();
        } else {
            makeVisible();
        }
    }

    private void changeAnswerColors() {
        //change colors
        findViewById(R.id.answer1).setBackgroundColor(getResources().getColor(R.color.ans1));
        findViewById(R.id.answer2).setBackgroundColor(getResources().getColor(R.color.ans2));
        findViewById(R.id.answer3).setBackgroundColor(getResources().getColor(R.color.ans3));
    }

    private void makeInvisible() {
        findViewById(R.id.answer1).setVisibility(View.INVISIBLE);
        findViewById(R.id.answer2).setVisibility(View.INVISIBLE);
        findViewById(R.id.answer3).setVisibility(View.INVISIBLE);
        isShowingAnswer = false;

        changeAnswerColors();
    }

    private void makeVisible() {
        if (allFlashCards.size() == 0 && !answer.matches("Barack Obama")) return; //if empty don't make visible
        TextView v = findViewById(R.id.answer1);
        revealAnim(v);
        v = findViewById(R.id.answer2);
        revealAnim(v);
        v = findViewById(R.id.answer3);
        revealAnim(v);

        isShowingAnswer = true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void revealAnim(TextView answerSideView ){
        // get the center for the clipping circle
        int cx = answerSideView.getWidth() / 2;
        int cy = answerSideView.getHeight() / 2;

        // get the final radius for the clipping circle
        float finalRadius = (float) Math.hypot(cx, cy);

        // create the animator for this view (the start radius is zero)
        Animator anim = ViewAnimationUtils.createCircularReveal(answerSideView, cx, cy, 0f, finalRadius);

        // hide the question and show the answer to prepare for playing the animation!
        answerSideView.setVisibility(View.VISIBLE);

        anim.setDuration(1000);
        anim.start();
    }

    private void checkCorrect(TextView t) {
        if (answer.equals(t.getText().toString())) {
            t.setBackgroundColor(getResources().getColor(R.color.white));
            new ParticleSystem(MainActivity.this, 100, R.drawable.confetti, 3000)
                    .setSpeedRange(0.2f, 0.5f)
                    .oneShot(t, 500);
            countDownTimer.cancel();
            ((TextView) findViewById(R.id.timer)).setText("On Time!");
        } else {
            t.setBackgroundColor(getResources().getColor(R.color.red));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {

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
            startTimer();
        }
    }
}