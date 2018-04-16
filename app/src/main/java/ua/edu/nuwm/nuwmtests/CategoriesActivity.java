package ua.edu.nuwm.nuwmtests;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.GsonBuilder;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import ua.edu.nuwm.nuwmtests.models.Category;

public class CategoriesActivity extends AppCompatActivity {
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        adapter = new ArrayAdapter<>(
                this, R.layout.list_item, R.id.text_view
        );

        ListView listView = this.findViewById(R.id.categories_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int i, long l) {
                Intent testsIntent = new Intent(CategoriesActivity.this, TestsActivity.class);
                startActivity(testsIntent);
            }
        });

        loadCategories();
    }

    private void loadCategories() {
        RestClient.get("categories", null, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                Category[] categories = new GsonBuilder().create().fromJson(response, Category[].class);

                for (Category category : categories) {
                    adapter.add(category.name);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String response,
                                  Throwable throwable) {
                Category[] categories = new GsonBuilder().create().fromJson(
                        "[ { \"_id\": \"5ad26a97f3e1ae1e20bbf198\", \"name\": \"ddd\", \"subject\": \"5ad26a97f3e1ae1e20bbf197\", \"__v\": 0 }, { \"_id\": \"5ad38ad1f67bbe469a617f64\", \"subject\": \"5ad38a9859b1cb4669c2db44\", \"name\": \"Test1\", \"__v\": 0 }, { \"_id\": \"5ad38ad7f67bbe469a617f65\", \"subject\": \"5ad38ac1f67bbe469a617f63\", \"name\": \"Test2\", \"__v\": 0 } ]",
                        Category[].class
                );

                for (Category category : categories) {
                    adapter.add(category.name);
                }
            }
        });
    }
}
