package com.example.quizapplication;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileManager {


    String answersFileName = "AnswersFile.txt";

    public void writeResultsToFile(Context context, String resultString) {
        try {
            FileOutputStream fos = context.openFileOutput(answersFileName, Context.MODE_PRIVATE);
            fos.write(resultString.getBytes());
            fos.write("\n".getBytes()); // Add a newline for each result entry
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int[] readResultsFromFile(Context context) {
        int[] results = new int[3]; // [0]: totalAttempts, [1]: totalCorrectAnswers, [2]: totalquestions
        try {
            FileInputStream fis = context.openFileInput(answersFileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Attempts:")) {
                    results[0] += Integer.parseInt(line.split(":")[1].trim());
                } else if (line.startsWith("Correct Answers:")) {
                    results[1] += Integer.parseInt(line.split(":")[1].trim());
                }
                    else if (line.startsWith("Total Questions:")) {
                    results[2] += Integer.parseInt(line.split(":")[1].trim());
                }

            }

            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }
    public void resetFileContent(Context context) {
        try {
            FileOutputStream fos = context.openFileOutput(answersFileName, Context.MODE_PRIVATE);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
