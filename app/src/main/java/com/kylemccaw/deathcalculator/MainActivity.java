package com.kylemccaw.deathcalculator;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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

    Button selectBirthDate, selectDeathDate, calcMissing;
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
            public void onClick(View view) {
                years.setText("");
                days.setText("");
            }
        });

        calcMissing.setOnClickListener(new OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if (birthDateToggle.isChecked() && deathDateToggle.isChecked()) {
                    // calculate lifeLength
                    birth = parser((String) birthDate.getText());
                    death = parser((String) deathDate.getText());
                    double yearLong = Math.floor(Duration.between(birth, death).toDays());
                    years.setText((int) (yearLong / 365));
                    days.setText((int) Math.floor(Duration.between(birth, death).toDays()));
                } else if (birthDateToggle.isChecked() && lifeLengthToggle.isChecked()) {
                    // calculate deathDate
                } else if (deathDateToggle.isChecked() && lifeLengthToggle.isChecked()) {
                    // calculate birthDate
                }
            }
        });
    }


    // code guidance provided by GitHub user codingdemos
    public void setButtonListener(Button b, final TextView tv) {
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

    public void setToggleListener(final ToggleButton tb, final Button b, final TextView tv) {
        tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (threeToggled()) {
                    toggleDialog();
                    tb.setChecked(false);
                    return;
                } else if (!tb.isChecked()) {
                    b.setClickable(false);
                    tv.setText("");
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
                    toggleDialog();
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

    public void toggleDialog() {
        new AlertDialog.Builder(this)
                .setTitle("You've exceeded your toggles!")
                .setMessage("You can only select two fields.")
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void calculate(LocalDate birth, LocalDate death) {
        Duration difference = Duration.between(birth.atStartOfDay(), death.atStartOfDay());
        long diffDays = difference.toDays();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public LocalDate parser(String date) {
        String[] stringArray = date.split("/");
        if (stringArray[0].length() == 1) {
            StringBuilder sb = new StringBuilder(stringArray[0]);
            sb.insert(0, "0");
            stringArray[0] = sb.toString();
        }
        int day = Integer.parseInt(stringArray[0]);
        if (stringArray[1].length() == 1) {
            StringBuilder sb = new StringBuilder(stringArray[1]);
            sb.insert(0, "0");
            stringArray[1] = sb.toString();
        }
        int month = Integer.parseInt(stringArray[1];
        int year = Integer.parseInt(stringArray[2]);
        LocalDate parsedDate = LocalDate.of(year, month, day);
        return parsedDate;
    }
}

