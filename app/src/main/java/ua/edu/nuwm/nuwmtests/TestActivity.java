package ua.edu.nuwm.nuwmtests;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.GsonBuilder;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import ua.edu.nuwm.nuwmtests.models.Answer;
import ua.edu.nuwm.nuwmtests.models.Question;
import ua.edu.nuwm.nuwmtests.models.Test;

public class TestActivity extends BaseActivity {
    private Test test;
    private String testId;
    private LinearLayout layout;
    private Map<Integer, List<String>> userAnswers = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Intent intent = getIntent();
        testId = intent.getStringExtra(TestsActivity.EXTRA_TEST_ID);
        String testName = intent.getStringExtra(TestsActivity.EXTRA_TEST_NAME);

        setTitle(testName);

        layout = findViewById(R.id.test_layout);

        loadTest();
    }

    private void loadTest() {
        RestClient.get(String.format("tests/%s", testId), null, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                test = new GsonBuilder().create().fromJson(response, Test.class);

                createTestForm();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String response,
                                  Throwable throwable) {
                test = new GsonBuilder().create().fromJson(
                        "{\"questions\":[{\"table\":[],\"matchingQuestionAnswers\":[],\"simpleQuestionAnswers\":[{\"_id\":\"5b0dce341e40100020b13cad\",\"text\":\"fhjkl;lkjhgfds\",\"correct\":true},{\"_id\":\"5b0dce341e40100020b13cac\",\"text\":\"dfghjkl;lkjhgf\"},{\"_id\":\"5b0dce341e40100020b13cab\",\"text\":\"sdfhkhl;lkjhgfd\"}],\"_id\":\"5b0dce341e40100020b13caa\",\"matchingQuestion\":false,\"question\":\"eyhuiklpkjhgfdsdfghj\"},{\"table\":[],\"matchingQuestionAnswers\":[],\"simpleQuestionAnswers\":[{\"_id\":\"5b0dce341e40100020b13ca9\",\"text\":\"gsg\",\"correct\":true},{\"_id\":\"5b0dce341e40100020b13ca8\",\"text\":\"hjjhgfdagh\",\"correct\":true},{\"_id\":\"5b0dce341e40100020b13ca7\",\"text\":\"jhgfawdfg\"},{\"_id\":\"5b0dce341e40100020b13ca6\",\"text\":\"kjel;\"}],\"_id\":\"5b0dce341e40100020b13ca5\",\"matchingQuestion\":false,\"question\":\"hjjfhshhs&nbsp;<sub>fhjggdshjkjhg&nbsp;</sub>&nbsp;<span class=\\\"rendered-latex mathquill-rendered-math hasCursor\\\" contenteditable=\\\"false\\\"><span class=\\\"selectable\\\">$\\\\left[fdjkj\\\\right]$</span><span class=\\\"non-leaf\\\"><span class=\\\"scaled paren\\\" style=\\\"transform: scale(0.998758, 1.04348);\\\">[</span><span class=\\\"non-leaf\\\"><var class=\\\"florin\\\">&fnof;</var><span style=\\\"display: inline-block; width: 0;\\\">&nbsp;</span><var>d</var><var>j</var><var>k</var><var>j</var></span><span class=\\\"scaled paren\\\" style=\\\"transform: scale(0.998758, 1.04348);\\\">]</span></span></span>&nbsp;\"},{\"tableTitles\":{\"title1\":\"hklkjhgewaghjklk\",\"title2\":\"jeshrjklkjhf\"},\"table\":[[\"fdh\",\"segsgesg\",\"sgsgesegg\",\"fghjkl;lkjhf\"],[\"gshsg\",\"egsg\",\"segeg\",\"gjklkjhgfd\"]],\"matchingQuestionAnswers\":[\"1\",\"2\",\"1\",\"3\"],\"simpleQuestionAnswers\":[],\"_id\":\"5b0dce341e40100020b13ca4\",\"matchingQuestion\":true,\"question\":\"hjkl;''dsdfjkl;\",\"numberedAnswersQuantity\":4,\"letteredAnswersQuantity\":4}],\"_id\":\"5b0dce341e40100020b13ca3\",\"subject\":\"5b0dcd111e40100020b13ca1\",\"category\":\"5b0dcd161e40100020b13ca2\",\"name\":\"dfghjkljhgf\",\"comment\":\"&nbsp;<span class=\\\"rendered-latex mathquill-rendered-math hasCursor\\\" contenteditable=\\\"false\\\"><span class=\\\"selectable\\\">$\\\\frac{aadd}{dagg}^{egshj}$</span><span class=\\\"fraction non-leaf\\\"><span class=\\\"numerator\\\"><var>a</var><var>a</var><var>d</var><var>d</var></span><span class=\\\"denominator\\\"><var>d</var><var>a</var><var>g</var><var>g</var></span><span style=\\\"display: inline-block; width: 0;\\\">&nbsp;</span></span><sup class=\\\"non-leaf\\\"><var>e</var><var>g</var><var>s</var><var>h</var><var>j</var></sup></span>&nbsp;\",\"__v\":0}",
                        Test.class
                );

                createTestForm();
            }
        });
    }

    private void createTestForm() {
        if (test.comment != null && !test.comment.isEmpty()) {
            this.createComment();
        }

        this.createQuestions();

        Button submitButton = new Button(this);
        submitButton.setText("Перевірити");
        submitButton.setTextColor(Color.WHITE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            submitButton.setBackgroundColor(getColor(R.color.colorPrimary));
        }

        final TextView scoreTextView = new TextView(TestActivity.this);
        scoreTextView.setPadding(0, 16, 0, 16);
        scoreTextView.setTextColor(Color.BLACK);
        scoreTextView.setTextSize(16);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int score = 0;
                List<Integer> correctAnswers = new ArrayList<>();

                for (int i = 0; i < test.questions.length; i++) {
                    Question question = test.questions[i];
                    List<String> userAnswer = userAnswers.get(i);

                    if (userAnswer == null) {
                        continue;
                    }

                    for (Answer answer : question.simpleQuestionAnswers) {
                        if (userAnswer.contains(answer.text) && answer.correct) {
                            score += 1;
                            correctAnswers.add(i);
                            break;
                        }
                    }
                }

                String scoreText;

                if (score > 0 && score < 5) {
                    scoreText = String.format("Ви відповіли правильно на %s питання! ", score);
                } else {
                    scoreText = String.format("Ви відповіли правильно на %s питаннь! ", score);
                }

                scoreText = scoreText.concat(correctAnswers.toString());
                scoreTextView.setText(scoreText);
            }
        });

        layout.addView(submitButton);
        layout.addView(scoreTextView);
    }

    private void createComment() {
        TextView commentLabel = new TextView(this);
        commentLabel.setText("Примітка: ");
        layout.addView(commentLabel);

        WebView commentView = createWebViewFromHtml(test.comment);
        LayoutParams commentLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        commentLayoutParams.setMargins(0, 0, 0, 40);
        commentView.setLayoutParams(commentLayoutParams);

        layout.addView(commentView);
        layout.addView(this.createDivider());
    }

    private void createQuestions() {
        for (int i = 0; i < test.questions.length; i++) {
            Question question = test.questions[i];

            this.createQuestionLabel(i);

            layout.addView(createWebViewFromHtml(question.question));

            if (question.matchingQuestion) {
                this.createMatchingQuestion(question);
            } else {
                this.createSimpleQuestion(question, i);
            }

            layout.addView(this.createDivider());
        }
    }

    private void createQuestionLabel(int index) {
        TextView questionLabel = new TextView(this);
        questionLabel.setText(String.format("Завдання %s", index + 1));
        questionLabel.setTextSize(20);
        questionLabel.setTextColor(Color.BLACK);
        LayoutParams questionLabelLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        questionLabelLayoutParams.setMargins(0, 40, 0, 16);
        questionLabel.setLayoutParams(questionLabelLayoutParams);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            questionLabel.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        }

        layout.addView(questionLabel);
    }

    private void createSimpleQuestion(Question question, int questionIndex) {
        int correctAnswers = 0;

        for (Answer answer : question.simpleQuestionAnswers) {
            if (answer.correct) {
                correctAnswers += 1;
            }
        }

        if (correctAnswers == 1) {
            this.createSimpleQuestionWithOneCorrectAnswer(question, questionIndex);
        } else {
            this.createSimpleQuestionWithMultipleCorrectAnswers(question, questionIndex);
        }
    }

    private void createSimpleQuestionWithOneCorrectAnswer(Question question, final int questionIndex) {
        TextView taskLabel = new TextView(this);
        taskLabel.setText("Виберіть одну відповідь:");
        taskLabel.setPadding(16, 0, 0, 0);
        layout.addView(taskLabel);

        RadioGroup answersRadioGroup = new RadioGroup(this);
        LayoutParams radioGroupLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        radioGroupLayoutParams.setMargins(0, 0, 0, 40);
        answersRadioGroup.setLayoutParams(radioGroupLayoutParams);

        for (Answer answer : question.simpleQuestionAnswers) {
            RadioButton answerRadioButton = new RadioButton(this);
            answerRadioButton.setPadding(0, 24, 0, 24);
            answerRadioButton.setText((Html.fromHtml(answer.text)));

            answersRadioGroup.addView(answerRadioButton);
        }

        answersRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = findViewById(radioGroup.getCheckedRadioButtonId());

                List<String> answersList = new ArrayList<>();
                answersList.add(radioButton.getText().toString());

                userAnswers.put(questionIndex, answersList);
            }
        });

        layout.addView(answersRadioGroup);
    }

    private void createSimpleQuestionWithMultipleCorrectAnswers(Question question, final int questionIndex) {
        TextView taskLabel = new TextView(this);
        taskLabel.setText("Виберіть одну або декілька відповідей:");
        taskLabel.setPadding(16, 0, 0, 0);
        layout.addView(taskLabel);

        for (Answer answer : question.simpleQuestionAnswers) {
            CheckBox answerCheckbox = new CheckBox(this);
            answerCheckbox.setPadding(0, 24, 0, 24);
            answerCheckbox.setText((Html.fromHtml(answer.text)));

            answerCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (userAnswers.containsKey(questionIndex)) {
                        List<String> answersList = userAnswers.get(questionIndex);

                        // TODO: Remove if checkbox was unchecked
                        if (b) {
                            answersList.add(compoundButton.getText().toString());
                        } else {
//                            answersList.remove()
                        }
                    } else {
                        List<String> answersList = new ArrayList<>();
                        answersList.add(compoundButton.getText().toString());

                        userAnswers.put(questionIndex, answersList);
                    }
                }
            });

            layout.addView(answerCheckbox);
        }
    }

    private void createMatchingQuestion(Question question) {
        this.createMatchingQuestionAnswers(question);

        LinearLayout answersLayout = new LinearLayout(this);
        answersLayout.setOrientation(LinearLayout.VERTICAL);
        LayoutParams answersLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        answersLayout.setLayoutParams(answersLayoutParams);

        for (int i = 0; i < question.numberedAnswersQuantity; i++) {
            LinearLayout numberedAnswersLayout = new LinearLayout(this);
            numberedAnswersLayout.setOrientation(LinearLayout.HORIZONTAL);
            LayoutParams numberedAnswersLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1);
            numberedAnswersLayout.setLayoutParams(numberedAnswersLayoutParams);

            RadioGroup answersRadioGroup = new RadioGroup(this);
            answersRadioGroup.setOrientation(LinearLayout.HORIZONTAL);
            LayoutParams radioGroupLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            radioGroupLayoutParams.setMargins(0, 0, 0, 40);
            answersRadioGroup.setLayoutParams(radioGroupLayoutParams);

            for (int j = 0; j < question.letteredAnswersQuantity; j++) {
                RadioButton answerRadioButton = new RadioButton(this);
                answerRadioButton.setPadding(24, 0, 24, 0);
                answerRadioButton.setText(String.format("%s - %c", i + 1, j + 65));

                answersRadioGroup.addView(answerRadioButton);
            }

            numberedAnswersLayout.addView(answersRadioGroup);
            answersLayout.addView(numberedAnswersLayout);
        }

        layout.addView(answersLayout);
    }

    private void createMatchingQuestionAnswers(Question question) {
        for (int i = 0; i < question.numberedAnswersQuantity; i++) {
            layout.addView(createWebViewFromHtml(String.format("%s. %s", i + 1, question.table[0][i])));
        }

        for (int i = 0; i < question.letteredAnswersQuantity; i++) {
            layout.addView(createWebViewFromHtml(String.format("%c. %s", i + 65, question.table[0][i])));
        }
    }

    private WebView createWebViewFromHtml(String html) {
        WebView webView = new WebView(this);

        html = "<link rel=\"stylesheet\" type=\"text/css\" href=\"mathquill.css\" />" + html;
        webView.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "utf-8", null);

        webView.setBackgroundColor(Color.TRANSPARENT);

        return webView;
    }

    private View createDivider() {
        View divider = new View(this);

        divider.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 1));
        divider.setBackgroundColor(Color.LTGRAY);

        return divider;
    }
}
