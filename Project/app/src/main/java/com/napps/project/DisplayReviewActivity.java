package com.napps.project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
public class DisplayReviewActivity extends AppCompatActivity {
    // Declare the Variable
    private TextView mId;
    private TextView mName;
    private TextView mBirth;
    private TextView mSex;
    private TextView mInterest;
    private RatingBar mChineseRating;
    private TextView mWorkExp;
    private String id;
    private String name;
    private String birth;
    private String sex;
    private String interest;
    private String workExp;
    private double chineseRating;
    private List<String> mReviewData = new ArrayList<String>();
    private PersonDataHelper mDataHelper;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_review);
        ActionBar bar = getSupportActionBar();
        if(bar!=null)
            bar.setDefaultDisplayHomeAsUpEnabled(true);
        mId = (TextView)findViewById(R.id.personId);
        mName = (TextView) findViewById(R.id.PersonName);
        mBirth = (TextView) findViewById(R.id.personDOB);
        mSex = (TextView) findViewById(R.id.personGender);
        mInterest = (TextView) findViewById(R.id.personInterests);
        mChineseRating = (RatingBar) findViewById(R.id.personRatingBar);
        mWorkExp = (TextView) findViewById(R.id.personWorkExperience);
        /* Extract the id sent along with the intent */
        Intent mIntent = getIntent();
        String id = mIntent.getStringExtra("id");
        /*
         * Fetch data for the movie with particular id and populate it in a List
         * variable
         */
        this.mDataHelper = new PersonDataHelper(this);
        mReviewData.addAll(this.mDataHelper.selectById(id));
        /*
         * Separate the various fields from the fetched record by calling a
         * user-defined function, breakString(). Populate the widgets in the
         * Display Review activity with the values of the various fields.
         */
        breakString(mReviewData.toString());
        /* Initialize the values of the various fields */
        mId.setText(this.id);
        mName.setText(name);
        mBirth.setText(birth);
        mSex.setText(sex);
        mChineseRating.setRating((float) chineseRating);
        mInterest.setText(interest);
        mWorkExp.setText(workExp);
    }
    /*
     * The breakString() method extracts the values of the various fields from a
     * record sent to it as a String parameter
     */
    public void breakString(String str) {
        id = str.substring(1, str.indexOf(";"));
        str = str.substring(id.length() + 2, str.length());
        name = str.substring(0, str.indexOf(";"));
        str = str.substring(name.length() + 1, str.length());
        birth = str.substring(0, str.indexOf(";"));
        str = str.substring(birth.length() + 1, str.length());
        sex = str.substring(0, str.indexOf(";"));
        str = str.substring(sex.length() + 1, str.length());
        String str1 = str.substring(0, str.indexOf(";"));
        interest = str.substring(0, str.indexOf(";"));
        str = str.substring(str1.length() + 1, str.length());
        str1 = str.substring(0, str.indexOf(";"));
        chineseRating = Double.valueOf(str1);
        str = str.substring(str1.length() + 1, str.length());
        workExp = str.substring(0, str.length()-1);
        //workExp = str;
    }
}