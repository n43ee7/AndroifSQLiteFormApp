package com.napps.project;

import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditActivity extends AppCompatActivity {
    private PersonDataHelper mDataHelper;

    String id;
    String name;
    String birth;
    String sex;
    String interests;
    double chineseLevel;
    String workExp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        this.mDataHelper = new PersonDataHelper(this);
        Button btnSave = (Button) findViewById(R.id.Save);
        btnSave.setOnClickListener(new SaveHandler());
    }

    // RESETS ALL USER CONTENT DATA
    public void resetAll() {
        EditText sn = (EditText) findViewById(R.id.editText_ID);
        sn.setText("");

        EditText etname = (EditText) findViewById(R.id.et_name);
        etname.setText("");

        Spinner yearSpinner = (Spinner) findViewById(R.id.birthyear);
        yearSpinner.setSelection(0);

        Spinner monthSpinner = (Spinner) findViewById(R.id.birthmonth);
        monthSpinner.setSelection(0);

        RadioButton maleRadio = (RadioButton) findViewById(R.id.male);
        maleRadio.setChecked(true);

        RadioButton femaleRadio = (RadioButton) findViewById(R.id.female);
        femaleRadio.setChecked(false);

        CheckBox readingCheck = (CheckBox) findViewById(R.id.Reading);
        readingCheck.setChecked(false);

        CheckBox foodCheck = (CheckBox) findViewById(R.id.food);
        foodCheck.setChecked(false);

        CheckBox musicCheck = (CheckBox) findViewById(R.id.Music);
        musicCheck.setChecked(false);

        CheckBox travelCheck = (CheckBox) findViewById(R.id.travel);
        travelCheck.setChecked(false);

        RatingBar chineseBar = (RatingBar) findViewById(R.id.ratingBar1);
        chineseBar.setRating(0);

        EditText workExpEdit = (EditText) findViewById(R.id.EditWorkExperience);
        workExpEdit.setText("");
    }


    class SaveHandler implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            boolean flag = true;
            StringBuilder sb = new StringBuilder();
            EditText sn = (EditText) findViewById(R.id.editText_ID);

            if (sn.getText() != null && ! "".equals(sn.getText().toString())) {
                id = sn.getText().toString();
                sb.append("ID:").append(sn.getText().toString());

            } else {
                flag = false;
            }

            EditText etname = (EditText) findViewById(R.id.et_name);
            if (etname.getText() != null && !"".equals(etname.getText().toString())) {
                name = etname.getText().toString();
                sb.append("Name:").append(etname.getText().toString());
            } else {
                flag = false;
            }

            Spinner yearSpinner = (Spinner) findViewById(R.id.birthyear);
            sb.append("birth year:").append(yearSpinner.getSelectedItem().toString());

            Spinner monthSpinner = (Spinner) findViewById(R.id.birthmonth);
            sb.append("birth month:").append(monthSpinner.getSelectedItem().toString());

            birth = yearSpinner.getSelectedItem().toString() + "- " + monthSpinner.getSelectedItem().toString();

            RadioButton maleRadio = (RadioButton) findViewById(R.id.male);
            RadioButton femaleRadio = (RadioButton) findViewById(R.id.female);
            RadioButton othersRadio = (RadioButton) findViewById(R.id.others);

            if (maleRadio.isChecked()) {
                sb.append("Gender:").append("Male");
                sex = "Male";
            } else if (femaleRadio.isChecked()) {
                sb.append("Gender:Female");
                sex = "Female";
            } else if (othersRadio.isChecked()) {
                sb.append("Gender:Others");
                sex = "Others";
            } else if (!femaleRadio.isChecked() && !maleRadio.isChecked()) {
                flag = false;
            }
            sb.append("Interests:");
            CheckBox readingCheck = (CheckBox) findViewById(R.id.Reading);
            if (readingCheck.isChecked()) {
                sb.append("Reading ");
                interests = "Reading";
            }
            CheckBox foodCheck = (CheckBox) findViewById(R.id.food);
            if (foodCheck.isChecked()) {
                sb.append("Food ");
                interests += ", Food";
            }
            CheckBox musicCheck = (CheckBox) findViewById(R.id.Music);
            if (musicCheck.isChecked()) {
                sb.append("Music ");
                interests += ", Music";
            }
            CheckBox travelCheck = (CheckBox) findViewById(R.id.travel);
            if (travelCheck.isChecked()) {
                sb.append("Travel");
                interests += ", Travel";
            }
            RatingBar chineseBar = (RatingBar) findViewById(R.id.ratingBar1);
            float r = chineseBar.getRating();
            if (r != 0) {
                sb.append("chinese HSK Level:").append(r);
                chineseLevel = r;
            } else {
                flag = false;
            }
            EditText workExpEdit = (EditText) findViewById(R.id.EditWorkExperience);
            workExpEdit.getText().toString();
            sb.append("Work Experience:").append(workExpEdit.getText().toString());
            workExp = workExpEdit.getText().toString();
            if (flag) {
                try {
                    mDataHelper.insert(id, name, birth, sex, interests, chineseLevel, workExp);
                    resetAll();
                    Toast t = Toast.makeText(EditActivity.this, "Save Successfully", Toast.LENGTH_LONG);
                    t.show();
                    finish();
                } catch (SQLiteConstraintException sce) {
                    Toast t = Toast.makeText(EditActivity.this, "Already have this ID, Please Change It.", Toast.LENGTH_LONG);
                    t.show();
                }
            } else {
                Toast t = Toast.makeText(EditActivity.this, "Please Fill all the fields Requirement", Toast.LENGTH_LONG);
                t.show();
            }
        }
    }
}

