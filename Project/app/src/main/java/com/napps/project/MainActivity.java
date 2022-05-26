package com.napps.project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ListView mPersonList;
    private List<Person> mReviewData = new ArrayList<Person>();
    private PersonDataHelper mDataHelper;
    private PersonAdapter mReviewAdapter;
    private TextView mNotice;
    private Intent sharingIntent;
    private final int DELETE = 1;
    private final int SHARE = 2;
    private final int Edit = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPersonList = (ListView) findViewById(R.id.list_view);
        mNotice = findViewById(R.id.Text_View);
        mPersonList.setEmptyView(mNotice);
        this.mDataHelper = new PersonDataHelper(this);
        mReviewData.addAll(this.mDataHelper.selectIdAndName());
        mReviewAdapter = new PersonAdapter(this,
                R.layout.person_item, mReviewData);
        mPersonList.setAdapter(mReviewAdapter);
        mPersonList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent mReviewListIntent = new Intent(MainActivity.this,
                        DisplayReviewActivity.class);
                String selectedId = mReviewData.get(arg2).getId();
                mReviewListIntent.putExtra("id",selectedId);
                startActivity(mReviewListIntent);
            }
        });
        registerForContextMenu(mPersonList);
    }
    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }
    public void refreshList() {
        mReviewData.clear();
        mReviewData.addAll(this.mDataHelper.selectIdAndName());
        mReviewAdapter.notifyDataSetChanged();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                // Create an intent to launch the activity
                Intent intent = new Intent(this,EditActivity.class);
                startActivity(intent);
                return (true);
            case R.id.search:
                Intent search = new Intent(this,SearchActivity.class);
                startActivity(search);
                return (true);
            case R.id.exit:

 System.exit(0);
 return (true);
 }
 return (super.onOptionsItemSelected(item));
 }
 @Override
 public void onCreateContextMenu(ContextMenu menu, View v,
 ContextMenu.ContextMenuInfo menuInfo) {

 menu.setHeaderTitle("Review");
 menu.add(Menu.NONE, Edit, Menu.NONE, "Edit");
 menu.add(Menu.NONE, DELETE, Menu.NONE, "Delete");
 menu.add(Menu.NONE, SHARE, Menu.NONE, "Share");
 }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item
                        .getMenuInfo();
        String id = this.mReviewData.get(info.position).getId();
        String name = this.mReviewData.get(info.position).getName();
        switch (item.getItemId()) {
            case DELETE:
                showDeleteDialog(id);
                break;
            case SHARE:
                sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Assign to: " +name+ "\nId: "+id+", Name: " +name
                        + "\nGender: Male \nInterested: Reading, Food, Tarvel; \nChinese HSK level: 4.5 \n Work Experience: Excellent. \n Person_Manager App by SADI" ;
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share Person Manager App");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                                shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share Person Manager App"));
                return true;
        }
        return (super.onOptionsItemSelected(item));
    }
    class MyItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            System.out.println(parent.getClass());
            System.out.println(view.getClass());
            System.out.println(position);
            System.out.println(id);
            Person f = mReviewData.get(position);
            Toast t = Toast.makeText(MainActivity.this, id + " " + f.getName(),
                    Toast.LENGTH_LONG);
            t.show();
        }
    }
    private void showDeleteDialog(String idStr) {
        final String tempIdStr = idStr;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this person?")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Uri person = Uri

                                        .parse("content://edu.just.provider.person/person/"+tempIdStr);
                                getApplicationContext().getContentResolver().delete(
                                        person,
                                        " id=?", new String[]{tempIdStr});
                                refreshList();
                            }
                        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.show();
    }
    public void show(){
        androidx.appcompat.app.AlertDialog.Builder adb = new
                androidx.appcompat.app.AlertDialog.Builder(this);
        adb.setTitle("alert");
        adb.setMessage("this is a alert dialog");
        adb.setIcon(R.mipmap.ic_launcher);
        adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "You clicked Yes",
                        Toast.LENGTH_SHORT).show();
            }
        });
        adb.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                Toast.makeText(MainActivity.this, "You clicked No",
                        Toast.LENGTH_SHORT).show();
            }
        });
        adb.show();
    }
}
