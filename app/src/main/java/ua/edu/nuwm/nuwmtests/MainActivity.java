package ua.edu.nuwm.nuwmtests;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.GsonBuilder;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import ua.edu.nuwm.nuwmtests.models.Subject;

public class MainActivity extends BaseActivity {
    public static final String EXTRA_SUBJECT_ID = "subject_id";
    public static final String EXTRA_SUBJECT_NAME = "subject_name";

    private ArrayAdapter<String> adapter;
    private Subject[] subjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new ArrayAdapter<>(
                this, R.layout.list_item, R.id.text_view
        );

        ListView listView = this.findViewById(R.id.subjects_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int i, long l) {
                Intent categoriesIntent = new Intent(MainActivity.this, CategoriesActivity.class);
                categoriesIntent.putExtra(EXTRA_SUBJECT_ID, subjects[i]._id);
                categoriesIntent.putExtra(EXTRA_SUBJECT_NAME, subjects[i].name);

                startActivity(categoriesIntent);
            }
        });

        loadSubjects();
    }

    private void loadSubjects() {
        RestClient.get("subjects", null, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                subjects = new GsonBuilder().create().fromJson(response, Subject[].class);

                for (Subject subject : subjects) {
                    adapter.add(subject.name);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String response,
                                  Throwable throwable) {
                subjects = new GsonBuilder().create().fromJson(
                        "[ { \"_id\": \"5ad38a9859b1cb4669c2db44\", \"name\": \"Test1\", \"__v\": 0 }, { \"_id\": \"5ad38ac1f67bbe469a617f63\", \"name\": \"Test2\", \"__v\": 0 } ]",
                        Subject[].class
                );

                for (Subject subject : subjects) {
                    adapter.add(subject.name);
                }
            }
        });
    }
}
