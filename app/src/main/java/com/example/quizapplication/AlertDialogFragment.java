package com.example.quizapplication;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class AlertDialogFragment extends DialogFragment {

    public interface OnInputListener {
        void onQuantityEntered(int quantity);
    }
    OnInputListener listener;
    EditText questionSize;
    Button saveButton,cancelButton;

    //the context to the DialogFragment is attached implements the OnInputListener interface,\
    // and it sets the listener field
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (OnInputListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnInputListener");
        }
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       // super.onCreateView(inflater, container, savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = inflater.inflate(R.layout.change_quiz_size,null);


        questionSize = view.findViewById(R.id.numChange);
        saveButton = view.findViewById(R.id.saveQ);
        cancelButton = view.findViewById(R.id.cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String check = questionSize.getText().toString();
                if ( check.isEmpty() ) {
                    // Show a toast message
                    Toast.makeText(getActivity(), getString(R.string.Alert_badQ), Toast.LENGTH_SHORT).show();
                    return;
                }
                int quantity = Integer.parseInt(questionSize.getText().toString());
                if(quantity == 0 || quantity > 10){
                    Toast.makeText(getActivity(), getString(R.string.Alert_badQ), Toast.LENGTH_SHORT).show();
                }
                else{
                    sendInput(quantity);
                    getDialog().dismiss();
                }
            }
        });

        return view;
    }
    private void sendInput(int quantity) {
        if (listener != null) {
            listener.onQuantityEntered(quantity);
        }
    }




}



