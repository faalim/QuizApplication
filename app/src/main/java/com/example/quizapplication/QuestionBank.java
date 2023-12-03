package com.example.quizapplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

public class QuestionBank {
    public ArrayList<Question> questions;
    public ArrayList<Question> originalQuestions;
    Context context;

    public QuestionBank(Context context) {
        this.context = context;

        // Obtain the array of color resources
        ArrayList<Integer> colors = getColorsList();
        // Initialize the questions ArrayList
        MyApp myApp = (MyApp) context.getApplicationContext();
        questions = myApp.getQuestionsBank();
        originalQuestions = myApp.getOriginalQuestions();

        if (questions == null || originalQuestions == null) {
            // If questions array or originalQuestions is not set in MyApp, create and set them
            questions = new ArrayList<>();
            originalQuestions = new ArrayList<>();

            setQuestions(originalQuestions);  // Add questions to the ArrayList
            Collections.shuffle(originalQuestions);  // Shuffle the original questions

            myApp.setOriginalQuestions(originalQuestions);
            myApp.setQuestionsBank(questions);  // Save the questions and original questions in MyApp for future use
        }
    }

    private void setQuestions(ArrayList<Question> originalQuestions) {
        String[] questionStatements = context.getResources().getStringArray(R.array.question_statements);

        boolean[] questionAnswers = getBooleanArray(R.array.question_answers);
        ArrayList<Integer> colors = getColorsList();

        for (int i = 0; i < questionStatements.length; i++) {
            Log.d("QuestionSetup", "Statement: " + questionStatements[i] +  ", Boolean Answer: " + questionAnswers[i] + ", Color: " + colors.get(i));

            Question question = new Question(questionStatements[i], questionAnswers[i], colors.get(i));
            questions.add(question);
            originalQuestions.add(question);
        }
    }

    private boolean[] getBooleanArray(int resId) {
        TypedArray typedArray = context.getResources().obtainTypedArray(resId);
        boolean[] array = new boolean[typedArray.length()];
        for (int i = 0; i < typedArray.length(); i++) {
            boolean value = typedArray.getBoolean(i, false);
            Log.d("BooleanArray", "Index: " + i + ", Value: " + value);
            array[i] = typedArray.getBoolean(i, false);
        }
        typedArray.recycle();
        return array;
    }

    private ArrayList<Integer> getColorsList() {
        MyApp myApp = (MyApp) context.getApplicationContext();
        ArrayList<Integer> colors = myApp.getBackgroundColor();

        if (colors == null) {
            // If colors array is not set in MyApp, obtain it from resources
            colors = new ArrayList<>();
            int[] colorArray = context.getResources().getIntArray(R.array.colors_array);
            for (int color : colorArray) {
                colors.add(color);
            }
            Collections.shuffle(colors);
            myApp.setBackgroundColor(colors);  // Save the colors array in MyApp for future use
        }

        return colors;
    }
}