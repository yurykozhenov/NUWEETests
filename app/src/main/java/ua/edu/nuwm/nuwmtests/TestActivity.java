package ua.edu.nuwm.nuwmtests;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ua.edu.nuwm.nuwmtests.models.Answer;
import ua.edu.nuwm.nuwmtests.models.Question;
import ua.edu.nuwm.nuwmtests.models.Test;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Gson gson = new GsonBuilder().create();
        Test test = gson.fromJson(
                "{ \"questions\": [ { \"answers\": [ { \"_id\": \"5ad38b7fc3154947f418ec65\", \"correct\": true, \"text\": \"Answer 1\" }, { \"_id\": \"5ad38b7fc3154947f418ec64\", \"text\": \"Answer 2\" } ], \"_id\": \"5ad38b7fc3154947f418ec63\", \"question\": \"Question 1\" }, { \"answers\": [ { \"_id\": \"5ad38b7fc3154947f418ec62\", \"text\": \"Answer 1\", \"correct\": true }, { \"_id\": \"5ad38b7fc3154947f418ec61\", \"text\": \"Answer 2\" } ], \"_id\": \"5ad38b7fc3154947f418ec60\", \"question\": \"Question 2\" } ], \"_id\": \"5ad38b7fc3154947f418ec5f\", \"subject\": \"5ad38a9859b1cb4669c2db44\", \"category\": \"5ad38ad1f67bbe469a617f64\", \"__v\": 0, \"name\": \"\\u0422\\u0435\\u0441\\u0442 1\" }",
                Test.class
        );

        LinearLayout layout = findViewById(R.id.test_layout);

        for (Question question : test.questions) {
            TextView questionTextView = new TextView(this);
            questionTextView.setText(question.question);
            questionTextView.setTextSize(22);
            layout.addView(questionTextView);

            RadioGroup answersRadioGroup = new RadioGroup(this);
            LayoutParams radioGroupLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            radioGroupLayoutParams.setMargins(0 ,0 ,0 ,40);
            answersRadioGroup.setLayoutParams(radioGroupLayoutParams);

            for (Answer answer : question.answers) {
                RadioButton answerRadioButton = new RadioButton(this);
                answerRadioButton.setPadding(0 ,24 ,0 ,24);
                answerRadioButton.setText(answer.text);

                answersRadioGroup.addView(answerRadioButton);
            }

            layout.addView(answersRadioGroup);
        }
    }
}
