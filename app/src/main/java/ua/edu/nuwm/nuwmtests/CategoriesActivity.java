package ua.edu.nuwm.nuwmtests;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ua.edu.nuwm.nuwmtests.models.Category;

public class CategoriesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, R.layout.list_item, R.id.text_view
        );

        Gson gson = new GsonBuilder().create();
        Category[] categories = gson.fromJson(
                "[ { \"_id\": \"5ad26a97f3e1ae1e20bbf198\", \"name\": \"ddd\", \"subject\": \"5ad26a97f3e1ae1e20bbf197\", \"__v\": 0 }, { \"_id\": \"5ad38ad1f67bbe469a617f64\", \"subject\": \"5ad38a9859b1cb4669c2db44\", \"name\": \"Test1\", \"__v\": 0 }, { \"_id\": \"5ad38ad7f67bbe469a617f65\", \"subject\": \"5ad38ac1f67bbe469a617f63\", \"name\": \"Test2\", \"__v\": 0 } ]",
                Category[].class
        );

        for (Category category : categories) {
            adapter.add(category.name);
        }

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
    }
}
