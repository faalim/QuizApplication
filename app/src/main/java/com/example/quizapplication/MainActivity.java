package com.example.quizapplication;//package com.example.quizapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AlertDialogFragment.OnInputListener {
    Button trueButton, falseButton;
    FrameLayout frameLayout;
    ProgressBar progressBar;
    int currentQuestionIndex;
    int correctAnswersCount;
    ArrayList<Question> questionsBank;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize questionsBank and add questions only if savedInstanceState is null
        if (savedInstanceState == null) {
            QuestionBank questionBank = new QuestionBank(this);
            questionsBank = questionBank.questions;
        } else {
            // Restore the saved state
            questionsBank = ((MyApp) getApplication()).getQuestionsBank();
            currentQuestionIndex = ((MyApp) getApplication()).getCurrentQuestionIndex();
            correctAnswersCount = ((MyApp) getApplication()).getCorrectAnswersCount();
        }

        trueButton = findViewById(R.id.trueButton);
        falseButton = findViewById(R.id.falseButton);
        frameLayout = findViewById(R.id.qustionsLayout);
        progressBar = findViewById(R.id.progressBar);

        trueButton.setOnClickListener(this);
        falseButton.setOnClickListener(this);


        showQuestion();
        updateProgressBar();

    }

    private void showQuestion() {
        questionsBank = ((MyApp)getApplication()).getQuestionsBank();
        if (currentQuestionIndex < questionsBank.size()) {
            frameLayout.setBackgroundColor(questionsBank.get(currentQuestionIndex).getColors());
            nextQuestion();
        } else {
            // Quiz completed
            showQuizCompleteDialog();
            Toast.makeText(this, getString(R.string.quiz_completed), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.trueButton:
            case R.id.falseButton:
                // Check if the answer is correct and update the count
                if (isAnswerCorrect(v.getId() == R.id.trueButton)) {
                    Toast.makeText(this, getString(R.string.correct), Toast.LENGTH_SHORT).show();
                    correctAnswersCount++;
                }
                // Move to the next question
                currentQuestionIndex++;

                // Save the state in MyApp
                ((MyApp) getApplication()).setCurrentQuestionIndex(currentQuestionIndex);
                ((MyApp) getApplication()).setCorrectAnswersCount(correctAnswersCount);
                // Show the next question
                showQuestion();
                updateProgressBar();
                break;
        }
    }

    private boolean isAnswerCorrect(boolean selectedAnswer) {
        if (currentQuestionIndex < questionsBank.size()) {
            questionsBank.get(currentQuestionIndex).isAnswer();
            return questionsBank.get(currentQuestionIndex).isAnswer() == selectedAnswer;
        }
        return false;
    }

    private void nextQuestion() {
        FragmentQuestions ff = FragmentQuestions.newInstance(
                questionsBank.get(currentQuestionIndex).getQuestion(),
                questionsBank.get(currentQuestionIndex).getColors()
        );

        getSupportFragmentManager().beginTransaction().replace(R.id.qustionsLayout, ff).commit();
    }

    private void updateProgressBar() {
        int totalQuestions = questionsBank.size();
        int progress = (int) (((float) currentQuestionIndex / totalQuestions) * 100) + 1;
        progressBar.setProgress(progress);
    }

    private void showQuizCompleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.Result));
        builder.setMessage(getString(R.string.rp1)+" " + correctAnswersCount +" " + getString(R.string.rp2)+" " + questionsBank.size()
         );
        builder.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveProgressToFile();
                Toast.makeText(MainActivity.this, getString(R.string.progSaved), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                restartQuiz();

            }
        });
        builder.setNegativeButton(getString(R.string.Ignore), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                restartQuiz();
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void saveProgressToFile() {
        // Get the number of correct answers
        int numberOfCorrectAnswers = correctAnswersCount;
        int totalQuestions = questionsBank.size(); //<--can be anysize
        // Read current values from the file
        FileManager fileManager = new FileManager();
        int[] currentResults = fileManager.readResultsFromFile(this);
        // Update the values
        int totalCorrectAnswers = currentResults[1] + numberOfCorrectAnswers;
        int totalAttempts = currentResults[0] + 1;
        totalQuestions +=currentResults[2];
        // Create a string with the updated results
        String resultString = "Correct Answers: " + totalCorrectAnswers + "\nAttempts: " + totalAttempts + "\nTotal Questions: " +totalQuestions;
        // Write the updated results back to the file
        fileManager.writeResultsToFile(this, resultString);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        FileManager fileManager = new FileManager();
        int[] currentResults = fileManager.readResultsFromFile(this);

        int totalCorrectAnswers = currentResults[1];
        int totalQuestions = currentResults[2];
        int totalAttempts = currentResults[0];
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        switch (item.getItemId()) {
            case R.id.avgResult:

                builder.setMessage(getString(R.string.avgResult1)+" " +
                        totalCorrectAnswers + " "+getString(R.string.avgResult2)+ " "+
                        totalQuestions +" "+getString(R.string.avgResult3) + " "+ getString(R.string.rp3)+ " "+ totalAttempts
                );
                builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {dialog.dismiss();
                            dialog.dismiss();
                    }
                });
                builder.create().show();
                return true;
            case R.id.resetResults:
                builder.setMessage(getString(R.string.restratQ));
                builder.setPositiveButton(getString(R.string.Restart), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fileManager.resetFileContent(MainActivity.this);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                return true;
            case R.id.numOfQuestions:
                AlertDialogFragment alertDialogFragment = new AlertDialogFragment();

                alertDialogFragment.show(getSupportFragmentManager(), "AlertDialogFragment");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void restartQuiz() {
     // Use QuestionBank to get the original questions
    ArrayList<Question> originalQuestions = ((MyApp) getApplication()).getOriginalQuestions();
    ArrayList<Question> shuffledQuestions = new ArrayList<>(originalQuestions);
    Collections.shuffle(shuffledQuestions);
    // Restore the original quantity of questions
    ((MyApp) getApplication()).setQuestionsBank(shuffledQuestions);
    currentQuestionIndex = 0;
    correctAnswersCount = 0;
    showQuestion();
    updateProgressBar();
}


    @Override
    public void onQuantityEntered(int quantity) {
        currentQuestionIndex = 0;
        correctAnswersCount = 0;
        ArrayList<Question>  questions = ((MyApp) getApplication()).getQuestionsBank();

        ArrayList<Question> tempArray = new ArrayList<>(questions); // Copy existing questions to TempArray
        questions.clear(); // Clear the existing questions
        Collections.shuffle(tempArray);

        for (int i = 0; i < quantity; i++) {
            questions.add(tempArray.get(i));
        }
        ((MyApp) getApplication()).setQuestionsBank(questions);
        // Save the updated state in MyApp
        ((MyApp) getApplication()).setCurrentQuestionIndex(currentQuestionIndex);
        ((MyApp) getApplication()).setCorrectAnswersCount(correctAnswersCount);
//        // Show the first question of the updated set
        showQuestion();
        updateProgressBar();
    }
    public void addnewQuestiton(){

    }



}
