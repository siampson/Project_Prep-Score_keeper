package com.example.android.courtcounter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    int scoreTeamA = 0 ;
    int scoreTeamB = 0 ;
    private Button startButton;
    private Button pauseButton;
    private Button resetButton;
    private Button teamA3Pointer;
    private Button teamB3Pointer;
    private Button teamA2Pointer;
    private Button teamB2Pointer;
    private Button teamAfreeThrow ;
    private Button teamBfreeThrow ;
    private TextView timerValue;
    private long startTime = 0L;
    private Handler customHandler = new Handler();
    private TextView quarterTextView;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    String reportTeamA = "";
    String reportTeamB = "";
    String report = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main) ;
        quarterTextView = (TextView) findViewById(R.id.quarter);
        resetButton = (Button) findViewById(R.id.resetButton);
        teamA3Pointer = (Button) findViewById(R.id.team_a_3pointer);
        teamB3Pointer = (Button) findViewById(R.id.team_b_3pointer);
        teamA2Pointer = (Button)findViewById(R.id.team_a_2pointer);
        teamB2Pointer = (Button) findViewById(R.id.team_b_2pointer);
        teamAfreeThrow = (Button) findViewById(R.id.team_a_free_throw);
        teamBfreeThrow = (Button) findViewById(R.id.team_b_free_throw);
        timerValue = (TextView) findViewById(R.id.timerValue);
        startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            /**
             * up on click of the pause button starts the timer.
             * It also sets deactivates the start button and free throw buttons for both team.
             * it activates the pause button, 3 and 2 pointer buttons for both teams.
             * @param view
             */
            public void onClick(View view) {
                startTime = SystemClock.uptimeMillis();
                customHandler.postDelayed(updateTimerThread, 0);
                startButton.setEnabled(false);
                pauseButton.setEnabled(true);
                startButton.setText(R.string.resumeButtonLabel);
                teamA3Pointer.setEnabled(true);
                teamA2Pointer.setEnabled(true);
                teamB3Pointer.setEnabled(true);
                teamB2Pointer.setEnabled(true);
                teamAfreeThrow.setEnabled(false);
                teamBfreeThrow.setEnabled(false);

            }
        });

        pauseButton = (Button) findViewById(R.id.pauseButton);

        pauseButton.setOnClickListener(new View.OnClickListener() {
            /**
             * up on click of the pause button pauses the timer.
             * It also sets activates the start button and free throw buttons for both team.
             * it deactivates the pause button, 3 and 2 pointer buttons for both teams.
             * @param view
             */
            public void onClick(View view) {

                timeSwapBuff += timeInMilliseconds;
                customHandler.removeCallbacks(updateTimerThread);
                startButton.setEnabled(true);
                pauseButton.setEnabled(false);
                teamA3Pointer.setEnabled(false);
                teamA2Pointer.setEnabled(false);
                teamB3Pointer.setEnabled(false);
                teamB2Pointer.setEnabled(false);
                teamAfreeThrow.setEnabled(true);
                teamBfreeThrow.setEnabled(true);
            }
        });
    }

    /**
     * Displays the given score for Team A.
     * @param score the current score of team.
     */
    public void displayForTeamA(int score) {
        TextView scoreView = (TextView) findViewById(R.id.team_a_score);
        scoreView.setText(String.valueOf(score)) ;
    }

    /**
     * Displays the given score for Team B.
     * @param score the current score of team.
     */
    public void displayForTeamB(int score) {
        TextView scoreView = (TextView) findViewById(R.id.team_b_score);
        scoreView.setText(String.valueOf(score)) ;
    }

    /**
     * Adds 3 points to the team A's score
     */
    public void threePointerA(View view) {
        scoreTeamA = scoreTeamA + 3;
        reportTeamA(getString(R.string.threePointer));
        displayForTeamA(scoreTeamA);
        pauseClock();
    }
    /**
     * Adds 2 points to the team A's score
     */
    public void twoPointerA(View view) {
        scoreTeamA = scoreTeamA + 2 ;
        reportTeamA(getString(R.string.twoPointer));
        displayForTeamA(scoreTeamA);
        pauseClock();
    }

    /**
     * Adds 1 points to the team A's score
     */
    public void freeThrowA(View view) {
        scoreTeamA = scoreTeamA + 1 ;
        reportTeamA(getString(R.string.freeThrow));
        displayForTeamA(scoreTeamA);
        pauseClock();
    }

    /**
     * Adds 3 points to the team B's score
     */
    public void threePointerB(View view) {
        scoreTeamB = scoreTeamB + 3;
        reportTeamB(getString(R.string.threePointer));
        displayForTeamB(scoreTeamB);
        pauseClock();
    }
    /**
     * Adds 2 points to the team B's score
     */
    public void twoPointerB(View view) {
        scoreTeamB = scoreTeamB + 2 ;
        reportTeamB(getString(R.string.twoPointer));
        displayForTeamB(scoreTeamB);
        pauseClock();
    }

    /**
     * Adds 1 points to the team B's score
     */
    public void freeThrowB(View view) {
        scoreTeamB = scoreTeamB + 1 ;
        reportTeamB(getString(R.string.freeThrow));
        displayForTeamB(scoreTeamB);
        pauseClock();
    }

    /**
     * resets clock and go to next quarter
     * Sets the quarter value to the next quarter
     * send the quarter report to emailReport method to be sent to email App
     * In case the current quarter is the 4th quarter set the quarter value to 1st quarter,
     * sends the report to email App and resets timer and scores for the next game.
     */
    public void nextQuarter(View view){
        if (quarterTextView.getText().toString().equals(getString(R.string.firstQuarter))) {
            timeSwapBuff = 0L;
            timeInMilliseconds = SystemClock.uptimeMillis();
            startButton.setEnabled(true);
            resetButton.setEnabled(false);
            startButton.setText(R.string.start);
            timerValue.setText(R.string.timerVal);
            emailReport (getString(R.string.firstQuarter) + " " + getString(R.string.report));
            quarterTextView.setText(R.string.secondQuarter);
        }
        else if (quarterTextView.getText().toString().equals(getString(R.string.secondQuarter))) {
            timeSwapBuff = 0L;
            timeInMilliseconds = SystemClock.uptimeMillis();
            startButton.setEnabled(true);
            resetButton.setEnabled(false);
            startButton.setText(R.string.start);
            timerValue.setText(R.string.timerVal);
            emailReport (getString(R.string.secondQuarter) + " " + getString(R.string.report));
            quarterTextView.setText(R.string.thirdQuarter);
        }
        else if (quarterTextView.getText().toString().equals(getString(R.string.thirdQuarter))) {
            timeSwapBuff = 0L;
            timeInMilliseconds = SystemClock.uptimeMillis();
            startButton.setEnabled(true);
            resetButton.setEnabled(false);
            startButton.setText(R.string.start);
            timerValue.setText(R.string.timerVal);
            emailReport (getString(R.string.thirdQuarter) + " " + getString(R.string.report));
            quarterTextView.setText(R.string.forthQuarter);
        }
        else {
            timeSwapBuff = 0L;
            timeInMilliseconds = SystemClock.uptimeMillis();
            startButton.setEnabled(true);
            resetButton.setEnabled(false);
            startButton.setText(R.string.start);
            timerValue.setText(R.string.timerVal);
            emailReport (getString(R.string.forthQuarter) + " " + getString(R.string.report));
            quarterTextView.setText(R.string.firstQuarter);
            resetScores();
        }
    }

    /**
     * resets the scores of both teams
     */
    public void resetScores() {
        scoreTeamA = 0 ;
        displayForTeamA(scoreTeamA) ;
        scoreTeamB = 0 ;
        displayForTeamB(scoreTeamB);
        report = "";
    }

    private Runnable updateTimerThread = new Runnable() {
        /**
         * runs a thread that manages the timer and displays a count down timer (mm:ss)
         */
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            int mins = 0 - secs / 60;
            secs = 59 - secs % 60;
            timerValue.setText("" + String.format("%02d", mins) + ":"
                    + String.format("%02d", secs)
            );
            customHandler.postDelayed(this, 0);
            if (updatedTime>=60000){
                pauseClock();
                startButton.setEnabled(false);
                timerValue.setText(getString(R.string.timerVal0));
                resetButton.setEnabled(true);

            }
        }

    };

    /**
     * pauses the timer
     * diactivates the pause, 3 pointer adn 2 pointer buttons for both teams.
     */
    public void pauseClock() {

        timeSwapBuff += timeInMilliseconds;
        customHandler.removeCallbacks(updateTimerThread);
        startButton.setEnabled(true);
        pauseButton.setEnabled(false);
        teamA3Pointer.setEnabled(false);
        teamA2Pointer.setEnabled(false);
        teamB3Pointer.setEnabled(false);
        teamB2Pointer.setEnabled(false);
        teamAfreeThrow.setEnabled(true);
        teamBfreeThrow.setEnabled(true);

    }

    /**
     * keeps the track of the points scored by Team A. including the time they have been scored.
     * @param point the points that has been scored at a time.
     */

    public void reportTeamA (String point) {
        reportTeamA = reportTeamA + getString(R.string.teamA) + " " + getString(R.string.team) + " " + getString(R.string.scores)
                + " " +getString(R.string.a) + " " + point + " " + getString(R.string.at)+ " " + timeCalculator() +"\n";
    }
    /**
     * keeps the track of the points scored by Team B. including the time they have been scored.
     * @param point the points that has been scored at a time.
     */
    public void reportTeamB (String point) {
        reportTeamB = reportTeamB +  getString(R.string.teamB) + " " + getString(R.string.team) + " " + getString(R.string.scores)
                + " " +getString(R.string.a) + " "+ point + " " + getString(R.string.at)+ " " + timeCalculator() +"\n";
    }

    /**
     * puts together the reports from team A and team B to create the quarter report.
     * send the rerport to email App
     * @param subject Emails subject
     */
    public void emailReport (String subject) {
        report = report + quarterTextView.getText().toString() + " " + getString(R.string.report) + "\n"
                + getString(R.string.teamA)  + ":" + "\n"+ reportTeamA
                + getString(R.string.teamB)  + ":" + "\n" + reportTeamB + "-----------" + "\n";
        reportTeamA = "";
        reportTeamB = "";
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:siamak@intcomnet.com"));
        intent.putExtra(Intent.EXTRA_SUBJECT,subject);
        intent.putExtra(Intent.EXTRA_TEXT, report) ;
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * calculates the time passed in each quarter.
     * @return current time which is the time that a point has been scored
     */
    public String timeCalculator(){

        int secs = (int) (updatedTime / 1000);
        int mins = secs / 60;
        secs = secs % 60;
        String currentTime = ("" + String.format("%02d", mins) + ":"
                        + String.format("%02d", secs)
        );
        return currentTime;
    }
}