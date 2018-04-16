package ua.edu.nuwm.nuwmtests;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.GsonBuilder;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import ua.edu.nuwm.nuwmtests.models.Answer;
import ua.edu.nuwm.nuwmtests.models.Question;
import ua.edu.nuwm.nuwmtests.models.Test;

public class TestActivity extends AppCompatActivity {
    private Test test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        loadTest();
    }

    private void loadTest() {
        RestClient.get("tests", null, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                test = new GsonBuilder().create().fromJson(response, Test[].class)[0];

                createTestForm();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String response,
                                  Throwable throwable) {
                test = new GsonBuilder().create().fromJson(
                        "{\"questions\":[{\"answers\":[{\"_id\":\"5ad409ae7cabff02330b96fa\",\"text\":\"12\",\"correct\":false},{\"_id\":\"5ad409ae7cabff02330b96f9\",\"text\":\"2\",\"correct\":true}],\"_id\":\"5ad409ae7cabff02330b96f8\",\"question\":\"Розв'язати&nbsp;&nbsp;<span class=\\\"rendered-latex mathquill-rendered-math hasCursor\\\" contenteditable=\\\"false\\\"><span class=\\\"selectable\\\">$\\\\int_1^2x^3dx$</span><big>&int;</big><sub class=\\\"non-leaf limit\\\" style=\\\"left: -0.25em;\\\">1</sub><sup class=\\\"non-leaf limit\\\" style=\\\"left: -0.465839em; margin-right: -0.365839em;\\\">2</sup><var>x</var><sup class=\\\"non-leaf\\\">3</sup><var>d</var><var>x</var></span>&nbsp;\"}],\"_id\":\"5ad409ae7cabff02330b96f7\",\"subject\":\"5ad408b87cabff02330b96f5\",\"category\":\"5ad408c17cabff02330b96f6\",\"name\":\"Тест 1\",\"comment\":\"Примітка\",\"__v\":0}",
                        Test.class
                );

                createTestForm();
            }
        });
    }

    private void createTestForm() {
        LinearLayout layout = findViewById(R.id.test_layout);

        TextView commentView = new TextView(this);
        commentView.setText(test.comment);

        layout.addView(commentView);

        for (Question question : test.questions) {
            WebView questionView = new WebView(this);

            question.question = "<link rel=\"stylesheet\" type=\"text/css\" href=\"mathquill.css\" />" + question.question;
            questionView.loadDataWithBaseURL("file:///android_asset/", question.question, "text/html", "utf-8", null);

            layout.addView(questionView);

            RadioGroup answersRadioGroup = new RadioGroup(this);
            LayoutParams radioGroupLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            radioGroupLayoutParams.setMargins(0, 0, 0, 40);
            answersRadioGroup.setLayoutParams(radioGroupLayoutParams);

            for (Answer answer : question.answers) {
                RadioButton answerRadioButton = new RadioButton(this);
                answerRadioButton.setPadding(0, 24, 0, 24);
                answerRadioButton.setText((Html.fromHtml(answer.text)));

                answersRadioGroup.addView(answerRadioButton);
            }

            layout.addView(answersRadioGroup);
        }
    }
}
