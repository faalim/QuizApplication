package com.example.quizapplication;

import java.io.Serializable;

public class Question implements Serializable {
    private String question;
    private boolean answer;
    private int colors;

    public Question(String question, boolean answer,int colors) {
        this.question = question;
        this.answer = answer;
        this.colors = colors;
    }
    public Question(){}
    public String getQuestion() {
        return question;
    }

    public boolean isAnswer() {
        return answer;
    }
    public int getColors() {
        return colors;
    }


}