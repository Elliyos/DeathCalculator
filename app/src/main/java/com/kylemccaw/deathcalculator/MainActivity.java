package com.kylemccaw.deathcalculator;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button selectBirthDate, selectDeathDate, calcMissing, reset;
    TextView birthDate, deathDate;
    DatePickerDialog datePickerDialog;
    int year, month, dayOfMonth;
    Calendar calendar;
    ImageButton birthDateReset, deathDateReset, lifeLengthReset;
    EditText years, days;
    ToggleButton birthDateToggle, deathDateToggle, lifeLengthToggle;
    LocalDate birth, death;

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
        calcMissing = findViewById(R.id.calcMissing);
        reset = findViewById(R.id.reset);

        setButtonListener(selectBirthDate,true);
        setButtonListener(selectDeathDate, false);
        setResetListener(birthDateReset, birthDate);
        setResetListener(deathDateReset, deathDate);
        setToggleListener(birthDateToggle, selectBirthDate, birthDate, true);
        setToggleListener(deathDateToggle, selectDeathDate, deathDate, false);
        setToggleListener(lifeLengthToggle, years, days);

        selectBirthDate.setClickable(false);
        selectDeathDate.setClickable(false);
        years.setInputType(0);
        days.setInputType(0);

        reset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                years.setText("");
                days.setText("");
                birthDate.setText("");
                deathDate.setText("");
                birthDateToggle.setChecked(false);
                deathDateToggle.setChecked(false);
                lifeLengthToggle.setChecked(false);
                birth = null;
                death = null;
            }
        });
        calcMissing.setOnClickListener(new OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if (birthDateToggle.isChecked() && deathDateToggle.isChecked()) {
                    // calculate lifeLength
                    if (birth == null || death == null){
                        toggleDialog("Empty field(s)!", "One or more of your fields is empty!");
                        return;
                    }
                    double yearLong = Math.floor(Duration.between(birth.atStartOfDay(), death.atStartOfDay()).toDays());
                    int yearTemp = (int) (yearLong / 365);
                    years.setText(String.valueOf(yearTemp));
                    days.setText(String.valueOf((int)(yearLong - (yearTemp * 365))));
                } else if (birthDateToggle.isChecked() && lifeLengthToggle.isChecked()) {
                    // calculate deathDate
                    if (birth == null || (years.getText().toString().trim().length() == 0 || days.getText().toString().trim().length() == 0)){
                        toggleDialog("Empty field(s)!", "One or more of your fields is empty!");
                        return;
                    }
                    int yearTemp = Integer.parseInt(years.getText().toString());
                    int dayTemp = Integer.parseInt(days.getText().toString()) + (yearTemp * 365);
                    birth = birth.plusDays(dayTemp);
                    deathDate.setText(birth.toString());
                } else if (deathDateToggle.isChecked() && lifeLengthToggle.isChecked()) {
                    // calculate birthDate
                    if (death == null || (years.getText().toString().trim().length() == 0 || days.getText().toString().trim().length() == 0)){
                        toggleDialog("Empty field(s)!", "One or more of your fields is empty!");
                        return;
                    }
                    int yearTemp = Integer.parseInt(years.getText().toString());
                    int dayTemp = (Integer.parseInt(days.getText().toString())) + (yearTemp * 365);
                    death = death.minusDays(dayTemp);
                    birthDate.setText(death.toString());
                }
            }
        });
    }


    // code guidance provided by GitHub user codingdemos
    public void setButtonListener(Button b, final boolean bOrD) {
        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                resetCalendar();
                datePickerDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                if (bOrD){
                                    birth = LocalDate.of(year, month, day);
                                    birthDate.setText(birth.toString());
                                } else {
                                    death = LocalDate.of(year, month, day);
                                    deathDate.setText(death.toString());
                                }
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });
    }

    public void setResetListener(ImageButton ib, final TextView tv) {
        ib.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                tv.setText("");
            }
        });
    }

    public void resetCalendar() {
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
    }

    public void setToggleListener(final ToggleButton tb, final Button b, final TextView tv, final boolean bOrD) {
        tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (threeToggled()) {
                    toggleDialog("Too many fields!", "You can only select two fields!");
                    tb.setChecked(false);
                    return;
                } else if (!tb.isChecked() && bOrD) {
                    b.setClickable(false);
                    tv.setText("");
                    birth = null;
                } else if (!tb.isChecked() && !bOrD){
                    b.setClickable(false);
                    tv.setText("");
                    death = null;
                } else if (tb.isChecked()) {
                    b.setClickable(true);
                }
            }
        });
    }

    public void setToggleListener(final ToggleButton tb, final EditText years, final EditText days) {
        tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (threeToggled()) {
                    toggleDialog("Too many fields!", "You can only select two fields!");
                    tb.setChecked(false);
                    return;
                } else if (!tb.isChecked()) {
                    years.setInputType(0);
                    days.setInputType(0);
                    years.setText("");
                    days.setText("");
                } else if (tb.isChecked()) {
                    years.setInputType(1002);
                    days.setInputType(1002);
                }
            }
        });
    }

    public void toggleDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton("Okay", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public boolean threeToggled() {
        if (birthDateToggle.isChecked() && deathDateToggle.isChecked() && lifeLengthToggle.isChecked()) {
            return true;
        }
        return false;
    }

    public boolean numChecker(){
        if (!years.getText().toString().contains("[a-zA-Z]+") || !days.getText().toString().contains("[a-zA-Z]+")){
            toggleDialog("Letters?!", "Your life length fields contain letters! Remove them!");
            return false;
        }
        return true;
    }
}

