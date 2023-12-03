package com.example.quizapplication;

import android.app.Application;

import java.util.ArrayList;

public class MyApp extends Application {
    ArrayList<Question> questionsBank;
    ArrayList<Integer> backgroundColor;
    int correctAnswersCount;
    int currentQuestionIndex;
    ArrayList<Question> originalQuestions;


    public ArrayList<Question> getOriginalQuestions() {
        return originalQuestions;
    }

    public void setOriginalQuestions(ArrayList<Question> originalQuestions) {
        this.originalQuestions = originalQuestions;
    }


    public ArrayList<Question> getQuestionsBank() {
        return questionsBank;
    }
    public void setQuestionsBank(ArrayList<Question> questionsBank) {
        this.questionsBank = questionsBank;
    }
    public ArrayList<Integer> getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(ArrayList<Integer> backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public void setCurrentQuestionIndex(int currentQuestionIndex) {
        this.currentQuestionIndex = currentQuestionIndex;
    }

    public int getCorrectAnswersCount() {
        return correctAnswersCount;
    }

    public void setCorrectAnswersCount(int correctAnswersCount) {
        this.correctAnswersCount = correctAnswersCount;
    }


}
