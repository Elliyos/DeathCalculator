package com.kylemccaw.deathcalculator;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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

import java.time.Duration;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {

    Button selectBirthDate, selectDeathDate;
    TextView birthDate, deathDate;
    DatePickerDialog datePickerDialog;
    int year, month, dayOfMonth, togglesActive;
    Calendar calendar;
    ImageButton birthDateReset, deathDateReset, lifeLengthReset;
    EditText years, days;
    ToggleButton birthDateToggle, deathDateToggle, lifeLengthToggle;
    LocalDate birth, death;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        togglesActive = 0;

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

        setButtonListener(selectBirthDate, birthDate, true);
        setButtonListener(selectDeathDate, deathDate, false);
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
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setButtonListener(Button b, final TextView tv, boolean bOrD){
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

        if (bOrD){
            birth = LocalDate.parse(tv.getText());
        } else {
            death = LocalDate.parse(tv.getText());
        }
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

    public void setToggleListener(final ToggleButton tb, final Button b, final TextView tv){

        tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (togglesActive == 2){
                    toggleDialog();
                    tb.setChecked(false);
                    return;
                } else if (!isChecked) {
                    b.setClickable(false);
                    tv.setText("");
                    --togglesActive;
                } else {
                    b.setClickable(true);
                    togglesActive++;
                }
            }
        });
    }

    public void setToggleListener(final ToggleButton tb, final EditText years, final EditText days){
        tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (togglesActive == 2){
                    toggleDialog();
                    tb.setChecked(false);
                    return;
                } else if (!isChecked) {
                    years.setInputType(0);
                    days.setInputType(0);
                    years.setText("");
                    days.setText("");
                    --togglesActive;
                } else {
                    years.setInputType(1002);
                    days.setInputType(1002);
                    togglesActive++;
                }
            }
        });
    }

    public void toggleDialog(){
        new AlertDialog.Builder(this)
                .setTitle("You've exceeded your toggles!")
                .setMessage("You can only select two fields.")
                .setNegativeButton("Okay", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void calculate(LocalDate birth, LocalDate death){
        Duration difference = Duration.between(birth.atStartOfDay(), death.atStartOfDay());
        long diffDays = difference.toDays();
    }

    public void calculate(String date, int years, int days, boolean bOrD){

    }

}

