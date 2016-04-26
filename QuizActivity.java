package com.bignerdranch.com.geoquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends ActionBarActivity {

   // Variables
   private static final String TAG = "QuizActivity";
   // variable name string for the savedInstantState question currently in use
   private static final String KEY_INDEX = "index";
   // variable name string for the savedInstantState status of whether the player cheated or not
   private static final String KEY_CHEATER = "cheater";
   // variable name string for the savedInstantState status of whether each question was cheated on or not
   private static final String KEY_CHEAT_ARRAY = "cheatarray";

   private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private TextView mQuestionTextView;
    private TrueFalse[] mQuestionBank = new TrueFalse[] {
          new TrueFalse(R.string.question_oceans, true),
          new TrueFalse(R.string.question_mideast, false),
          new TrueFalse(R.string.question_africa, false),
          new TrueFalse(R.string.question_americas, true),
          new TrueFalse(R.string.question_asia, true),
    };
    // int array to hold the status of whether or not each question was cheated on 1=no cheat, 2=cheated
    private int[] mCheatBank = {1, 1, 1, 1, 1};
    private int mCurrentIndex = 0;
    private boolean mIsCheater;
    private void updateQuestion() {
       //Log.d(TAG, "Updating question text for question #" + mCurrentIndex, new Exception());
       int question = mQuestionBank[mCurrentIndex].getQuestion();
       mQuestionTextView.setText(question);
    }
    private void checkAnswer(boolean userPressedTrue) {
       boolean answerIsTrue = mQuestionBank[mCurrentIndex].isTrueQuestion();
       int messageResId = 0;

      if (userPressedTrue == answerIsTrue) {
         if (mCheatBank[mCurrentIndex] == 2) {// if the index # of the current question is set in the question cheat int array
            messageResId = R.string.judgment_toast;// display a chastisement
         } else {// if it's not then display regular message
            messageResId = R.string.correct_toast;
         }
      } else {
         if (mCheatBank[mCurrentIndex] == 2) {
            messageResId = R.string.judgment_toast;
         } else {
            messageResId = R.string.incorrect_toast;
         }
      }
       Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }


   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called for GeoQuiz");
        setContentView(R.layout.activity_quiz);
        mQuestionTextView = (TextView)findViewById(R.id.question_text_view);
        mTrueButton = (Button)findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               checkAnswer(true);
            }
        });
        mFalseButton = (Button)findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               checkAnswer(false);
            }
        });
        mNextButton = (ImageButton)findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {

         @Override
         public void onClick(View arg0) {
            // TODO Auto-generated method stub
            mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
            updateQuestion();
         }
      });
        updateQuestion();

        mPrevButton = (ImageButton)findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {

         @Override
         public void onClick(View arg0) {
            // TODO Auto-generated method stub
            mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
            updateQuestion();
         }
      });
        updateQuestion();

        mQuestionTextView = (TextView)findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {

         @Override
         public void onClick(View arg0) {
            mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
            updateQuestion();

         }
      });
        if (savedInstanceState != null) {// if the instance state was saved
           mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);// the current question index # pulled from that saved state
           mIsCheater = savedInstanceState.getBoolean(KEY_CHEATER, false);// the current status of whether the user cheated on this ? is pulled
           mCheatBank = savedInstanceState.getIntArray(KEY_CHEAT_ARRAY);// the current status of the int array that holds cheat/not for each ? pulled
        }
        mCheatButton = (Button)findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {

         @Override
         public void onClick(View arg0) {
            // start cheat activity
            Intent i = new Intent(QuizActivity.this, CheatActivity.class);
            boolean answerIsTrue = mQuestionBank[mCurrentIndex].isTrueQuestion();
            i.putExtra(CheatActivity.EXTRA_ANSWER_IS_TRUE, answerIsTrue);
            startActivityForResult(i, 0);

         }
      });
        updateQuestion();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if (data == null) {
          return;
       }
       // pull the cheat status reported back by CheatActivity
       mIsCheater = data.getBooleanExtra(CheatActivity.EXTRA_ANSWER_SHOWN, false);
       if (mIsCheater) {// if they did cheat
          mCheatBank[mCurrentIndex] = 2;// set the current ?'s cheat status to cheated on in the array(2)
       }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
       super.onSaveInstanceState(savedInstanceState);
       Log.i(TAG, "onSaveInstanceState");
       savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);// save the index # of the current ? onSaveInstanceState issuance
       savedInstanceState.putBoolean(KEY_CHEATER, mIsCheater);// save the cheater status of the user onSaveInstanceState issuance
       savedInstanceState.putIntArray(KEY_CHEAT_ARRAY, mCheatBank);// save the cheat status of all ?'s onSaveInstanceState issuance
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called for GeoQuiz");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called for GeoQuiz");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called for GeoQuiz");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called for GeoQuiz");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called for GeoQuiz");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.quiz, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
