package ua.edu.nuwm.nuwmtests;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ua.edu.nuwm.nuwmtests.models.Subject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, R.layout.list_item, R.id.text_view
        );

        Gson gson = new GsonBuilder().create();
        Subject[] subjects = gson.fromJson(
                "[ { \"_id\": \"5ad38a9859b1cb4669c2db44\", \"name\": \"Test1\", \"__v\": 0 }, { \"_id\": \"5ad38ac1f67bbe469a617f63\", \"name\": \"Test2\", \"__v\": 0 } ]",
                Subject[].class
        );

        for (Subject subject : subjects) {
            adapter.add(subject.name);
        }

        ListView listView = this.findViewById(R.id.subjects_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int i, long l) {
                Intent categoriesIntent = new Intent(MainActivity.this, CategoriesActivity.class);
                startActivity(categoriesIntent);
            }
        });
    }
}
