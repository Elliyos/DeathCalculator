package com.kylemccaw.deathcalculator;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Button selectBirthDate, selectDeathDate;
    TextView birthDate, deathDate;
    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;
    ImageButton birthDateReset, deathDateReset, lifeLengthReset;
    EditText years, days;
    ToggleButton birthDateToggle, deathDateToggle, lifeLengthToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectBirthDate = findViewById(R.id.btnBirthDate);
        selectDeathDate = findViewById(R.id.btnDeathDate);
        birthDate = findViewById(R.id.birthDate);
        deathDate = findViewById(R.id.deathDate);
        birthDateReset = findViewById(R.id.birthDateReset);
        deathDateReset = findViewById(R.id.deathDateReset);
        lifeLengthReset = findViewById(R.id.lifeLengthReset);
        years = findViewById(R.id.years);
        days = findViewById(R.id.days);
        birthDateToggle = findViewById(R.id.birthDateToggle);
        deathDateToggle = findViewById(R.id.deathDateToggle);
        lifeLengthToggle = findViewById(R.id.lifeLengthToggle);

        setButtonListener(selectBirthDate, birthDate);
        setButtonListener(selectDeathDate, deathDate);
        setResetListener(birthDateReset, birthDate);
        setResetListener(deathDateReset, deathDate);
        setToggleListener(birthDateToggle, selectBirthDate, birthDate);
        setToggleListener(deathDateToggle, selectDeathDate, deathDate);
        setToggleListener(lifeLengthToggle, years, days);

        selectBirthDate.setClickable(false);
        selectDeathDate.setClickable(false);
        years.setInputType(0);
        days.setInputType(0);


        lifeLengthReset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick (View view){
               years.setText("");
               days.setText("");
            }
        });
    }


    // code guidance provided by GitHub user codingdemos
    public void setButtonListener(Button b, final TextView tv){
        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                resetCalendar();
                datePickerDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                tv.setText(day + "/" + (month + 1) + "/" + year);
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });
    }

    public void setResetListener(ImageButton ib, final TextView tv){
        ib.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick (View view){
                tv.setText("");
            }
        });
    }

    public void resetCalendar(){
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
    }

    public void setToggleListener(ToggleButton tb, final Button b, final TextView tv){
        tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    b.setClickable(true);
                } else {
                    b.setClickable(false);
                    tv.setText("");
                }
            }
        });
    }

    public void setToggleListener(ToggleButton tb, final EditText years, final EditText days){
        tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    years.setInputType(1002);
                    days.setInputType(1002);

                } else {
                    years.setInputType(0);
                    days.setInputType(0);
                    years.setText("");
                    days.setText("");
                }
            }
        });
    }
}

