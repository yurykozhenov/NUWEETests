package ua.edu.nuwm.nuwmtests;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.GsonBuilder;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import ua.edu.nuwm.nuwmtests.models.Test;

public class TestsActivity extends BaseActivity {
    public static final String EXTRA_TEST_ID = "test_id";
    public static final String EXTRA_TEST_NAME = "test_name";

    private ArrayAdapter<String> adapter;
    private String categoryId;
    private Test[] tests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tests);

        Intent intent = getIntent();
        categoryId = intent.getStringExtra(CategoriesActivity.EXTRA_CATEGORY_ID);
        String categoryName = intent.getStringExtra(CategoriesActivity.EXTRA_CATEGORY_NAME);

        setTitle(categoryName);

        adapter = new ArrayAdapter<>(
                this, R.layout.list_item, R.id.text_view
        );

        ListView listView = this.findViewById(R.id.tests_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int i, long l) {
                Intent testIntent = new Intent(TestsActivity.this, TestActivity.class);
                testIntent.putExtra(EXTRA_TEST_ID, tests[i]._id);
                testIntent.putExtra(EXTRA_TEST_NAME, tests[i].name);

                startActivity(testIntent);
            }
        });

        loadTests();
    }

    private void loadTests() {
        RequestParams params = new RequestParams();
        params.add("category", categoryId);

        RestClient.get("tests", params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                tests = new GsonBuilder().create().fromJson(response, Test[].class);

                for (Test test : tests) {
                    adapter.add(test.name);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String response,
                                  Throwable throwable) {
                tests = new GsonBuilder().create().fromJson(
                        "[ { \"questions\": [ { \"answers\": [ { \"_id\": \"5ad38b7fc3154947f418ec65\", \"correct\": true, \"text\": \"sdsgsd\" }, { \"_id\": \"5ad38b7fc3154947f418ec64\", \"text\": \"sgsdggsgsg\" } ], \"_id\": \"5ad38b7fc3154947f418ec63\", \"question\": \"Test\" }, { \"answers\": [ { \"_id\": \"5ad38b7fc3154947f418ec62\", \"text\": \"dfgfddf\", \"correct\": true }, { \"_id\": \"5ad38b7fc3154947f418ec61\", \"text\": \"gdfgdgd\" } ], \"_id\": \"5ad38b7fc3154947f418ec60\", \"question\": \"dfgdgdfdfg\" } ], \"_id\": \"5ad38b7fc3154947f418ec5f\", \"subject\": \"5ad38a9859b1cb4669c2db44\", \"category\": \"5ad38ad1f67bbe469a617f64\", \"__v\": 0, \"name\": \"\\u0422\\u0435\\u0441\\u0442 1\" }, { \"questions\": [ { \"answers\": [ { \"_id\": \"5ad3cd68d46ac41c9a9bf149\", \"correct\": true, \"text\": \"fdgdgfdgdg\" }, { \"_id\": \"5ad3cd68d46ac41c9a9bf148\", \"text\": \"dfgdfgdfgdf\" } ], \"_id\": \"5ad3cd68d46ac41c9a9bf147\", \"question\": \"<span class=\\\"rendered-latex mathquill-rendered-math hasCursor\\\" contenteditable=\\\"false\\\"><span class=\\\"selectable\\\">$+-$<\\/span><span class=\\\"\\\">+<\\/span><span class=\\\"binary-operator\\\">&minus;<\\/span><\\/span> tets\" } ], \"_id\": \"5ad3cd68d46ac41c9a9bf146\", \"subject\": \"5ad38ac1f67bbe469a617f63\", \"category\": \"5ad38ad7f67bbe469a617f65\", \"__v\": 0, \"name\": \"\\u0422\\u0435\\u0441\\u0442 2\" } ]",
                        Test[].class
                );

                for (Test test : tests) {
                    adapter.add(test.name);
                }
            }
        });
    }
}
