package com.napps.project;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
@SuppressLint("Registered")
public class SearchActivity extends AppCompatActivity {
    private ListView mPersonList;
    private Button mSearchButton;
    private EditText mSearchText;
    private List<String> mReviewData = new ArrayList<String>();
    String id[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        PersonDataHelper mDataHelper = new PersonDataHelper(this);
        mPersonList = (ListView) findViewById(R.id.lvName);
        mSearchButton = (Button) findViewById(R.id.button1);
        mSearchText = (EditText) findViewById(R.id.editText1);
        mPersonList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                String columns[] = new String[] { "id","name", "birth", "sex", "interests", "chineselevel", "workexp" };
                Uri movie = Uri.parse("content://edu.just.provider.person/person/" + (Integer.parseInt(id[arg2])));
                ContentResolver cr = getContentResolver();
                Cursor c = cr.query(movie, columns, null, null, null);
                if (c.moveToFirst()) {
                    // Get the field values and store them in a string.
                    @SuppressLint("Range") String str = "id: " + c.getString(c.getColumnIndex("id")) + "\nName: " + c.getString(c.getColumnIndex("name")) + "\nbirth: " + c.getString(c.getColumnIndex("birth")) + "\nSex: " + c.getString(c.getColumnIndex("sex")) + "\nInterests: " + c.getDouble(c.getColumnIndex("interests")) + "\nworkexp: " + c.getString(c.getColumnIndex("workexp"));
                    Toast.makeText(SearchActivity.this, str,
                            Toast.LENGTH_LONG).show();
                }
                // Closes the cursor object
                if (c != null && !c.isClosed()) {
                    c.close();
                }
            }
        });
        /* Create the onClick listener for the mSearchButton */
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("Range")
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                String mSearchFor = mSearchText.getText().toString().trim();
                String columns[] = new String[] { "id", "name" };
                Uri person = Uri
                        .parse("content://edu.just.provider.person/person/");
                /* Retrieve all the matching records from the table. */
                ContentResolver cr = getContentResolver();
                Cursor c = cr.query(person, columns, "upper(name) like '%"
                        + mSearchFor.toUpperCase() + "%'", null, null);
                id = new String[c.getCount()];
                int i = 0;
                mReviewData.clear();
                if (c.getCount() == 0) {
                    Toast.makeText(SearchActivity.this,
                            "No result found", Toast.LENGTH_LONG).show();
                } else {
                    c.moveToFirst();
                    do {
                        // Get the field values
                        id[i] = c.getString(c.getColumnIndex("id"));
                        mReviewData.add(c.getString(c.getColumnIndex("name")));
                        i++;
                    } while (c.moveToNext());
                }
                /* Close the cursor */
                if (c != null && !c.isClosed()) {
                    c.close();
                }
                ArrayAdapter<String> mReviewList = new ArrayAdapter<String>(
                        SearchActivity.this, R.layout.listentry,
                        mReviewData);
                mPersonList.setAdapter(mReviewList);
            }
        });
    }
}