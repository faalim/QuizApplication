package com.example.quizapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentQuestions extends Fragment {
    static String questionFromQuestionBank;
    static int viewBackgroundColor;

    public static FragmentQuestions newInstance(String question, int color){
        //get the question and color from main activity after true/false button is pressed
        FragmentQuestions ff = new FragmentQuestions();
        questionFromQuestionBank = question;
        viewBackgroundColor = color;
        return ff;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_questions,container,false);
        TextView text = v.findViewById(R.id.question);
        text.setText(questionFromQuestionBank);
        text.setBackgroundColor(viewBackgroundColor);
        return v;
    }




}
