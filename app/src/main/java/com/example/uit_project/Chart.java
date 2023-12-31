package com.example.uit_project;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.uit_project.api.APIService;
import com.example.uit_project.model.datapoint.Datapoint;
import com.example.uit_project.model.datapoint.RequestBodyAsset;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Chart extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spinnerSelectAttribute;
    private Spinner spinnerSelectTimeframe;
    private String txtAttribute;
    private String txtTimeframe = "";
    private String selectedAttribute = "temperature";
    private EditText editTextDateTime;
    private String selectedDateTime;
    private Button show;
    private long toTimestamp;
    private long fromTimestamp;
    private String getDates;
    private RequestBodyAsset body;
    private ArrayList<Float> dataList = new ArrayList<>();
    private ArrayList<String> xValues = new ArrayList<>();
    private ArrayList<String> ending = new ArrayList<>();
    private ImageButton back;

    private Description description;
    private LineChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        spinnerSelectAttribute = findViewById(R.id.select_attributes);
        spinnerSelectTimeframe = findViewById(R.id.select_timeframes);
        show = findViewById(R.id.btn_show);
        back = findViewById(R.id.btn_back);
        chart = findViewById(R.id.chart);
        spinnerSelectTimeframe.setOnItemSelectedListener(this);
        spinnerSelectAttribute.setOnItemSelectedListener(this);
        String[] timeframes = getResources().getStringArray(R.array.timeframes);
        String[] attributes = getResources().getStringArray(R.array.attributes);
        ArrayAdapter adapterAttribute = new ArrayAdapter(this, android.R.layout.simple_spinner_item, attributes);
        ArrayAdapter adapterTimeframe = new ArrayAdapter(this, android.R.layout.simple_spinner_item, timeframes);

        adapterAttribute.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterTimeframe.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSelectAttribute.setAdapter(adapterAttribute);
        spinnerSelectTimeframe.setAdapter(adapterTimeframe);

        editTextDateTime = findViewById(R.id.select_dates);

        editTextDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePickerDialog();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        show.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onClick(View v) {
                getDates = editTextDateTime.getText().toString();
                if(getDates.isEmpty()) {
                    Toast.makeText(Chart.this, getString(R.string.input_warning), Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        toTimestamp = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm")
                                .parse(getDates).getTime();
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    if (txtTimeframe.contains(getString(R.string.hour))) {

                        fromTimestamp = toTimestamp - 3600000;
                        body = new RequestBodyAsset((long) fromTimestamp, (long) toTimestamp, "string");

                    } else if (txtTimeframe.contains(getString(R.string.day))) {

                        fromTimestamp = toTimestamp - 86400000;
                        body = new RequestBodyAsset((long) fromTimestamp, (long) toTimestamp, "string");

                    } else if (txtTimeframe.contains(getString(R.string.week))) {

                        fromTimestamp = toTimestamp - 604800000;
                        body = new RequestBodyAsset((long) fromTimestamp, (long) toTimestamp, "string");

                    } else if (txtTimeframe.contains(getString(R.string.month))) {

                        fromTimestamp = toTimestamp - 2678400000L;
                        body = new RequestBodyAsset((long) fromTimestamp, (long) toTimestamp, "string");

                    } else if (txtTimeframe.contains(getString(R.string.year))) {

                        fromTimestamp = toTimestamp - 31536000000L;
                        body = new RequestBodyAsset((long) fromTimestamp, (long) toTimestamp, "string");

                    }

                    Log.d("TEST TIME", "from: " + fromTimestamp);
                    Log.d("TEST TIME", "to: " + toTimestamp);

                    Call<JsonArray> call = APIService.apiService.getDatapoint("Bearer " + GlobalVar.token,
                            "5zI6XqkQVSfdgOrZ1MyWEf", selectedAttribute, body);
                    call.enqueue(new Callback<JsonArray>() {
                        @Override
                        public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                            Log.d("API CALL", String.valueOf(response.code()));

                            if (response.isSuccessful()) {
                                JsonArray jsonArray = response.body();
                                if (jsonArray != null) {
                                    Gson gson = new Gson();
                                    dataList.clear();
                                    xValues.clear();
                                    Type listType = new TypeToken<List<Datapoint>>() {
                                    }.getType();
                                    List<Datapoint> dataPoints = gson.fromJson(jsonArray, listType);

                                    for (Datapoint dataPoint : dataPoints) {
                                        long x = dataPoint.getX();
                                        float y = (float) dataPoint.getY();
                                        Log.d("CALL POINT", "x: " + x);
                                        Log.d("CALL POINT", "y: " + y);

                                        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm",
                                                editTextDateTime.getTextLocale());
                                        String end = sdf1.format(x);
                                        ending.add(end);
                                        dataList.add(0, y);
                                        Log.d("CALL DATA", "Data list: " + dataList);

                                        if (txtTimeframe.contains(getString(R.string.hour))) {
                                            Date date = new Date(x);
                                            Calendar calendar = Calendar.getInstance();
                                            calendar.setTime(date);
                                            int minuteofhour = calendar.get(Calendar.MINUTE);
                                            xValues.add(0, Integer.toString(minuteofhour));
                                            Log.d("CALL POINT", "x values: " + xValues);


                                        } else if (txtTimeframe.contains(getString(R.string.day))) {
                                            SimpleDateFormat sdf2 = new SimpleDateFormat("HH", Locale.getDefault());
                                            String formattedHour = sdf2.format(x);
                                            xValues.add(0, formattedHour);
                                            Log.d("CALL POINT", "x values: " + xValues);

                                        } else if (txtTimeframe.contains(getString(R.string.week))) {
                                            Date date = new Date(x);
                                            Calendar calendar = Calendar.getInstance();
                                            calendar.setTime(date);
                                            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                                            String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
                                            String dayName = days[dayOfWeek - 1];
                                            xValues.add(0, dayName);
                                            Log.d("CALL POINT", "x values: " + xValues);

                                        } else if (txtTimeframe.contains(getString(R.string.month))) {
                                            Date date = new Date(x);
                                            Calendar calendar = Calendar.getInstance();
                                            calendar.setTime(date);
                                            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                                            xValues.add(0, Integer.toString(dayOfMonth));
                                            Log.d("CALL POINT", "x values: " + xValues);

                                        } else if (txtTimeframe.contains(getString(R.string.year))) {
                                            Date date = new Date(x);
                                            Calendar calendar = Calendar.getInstance();
                                            calendar.setTime(date);
                                            int month = calendar.get(Calendar.MONTH);
                                            xValues.add(0, Integer.toString(month));
                                            Log.d("CALL POINT", "x values: " + xValues);

                                        }

                                    }
                                    Log.d("TEST ENDING", ending.toString());
                                    List<Entry> entries = new ArrayList<>();

                                    if (txtTimeframe.contains(getString(R.string.hour))) {
                                        int minute = Integer.parseInt(xValues.get(0));
                                        xValues.clear();
                                        for (int i = 0; i < 12; i++) {
                                            if (Math.abs(minute - i * 5) < 3) {
                                                entries.add(new Entry(i, dataList.get(0)));
                                            } else {
                                                entries.add(new Entry(i, -1));
                                            }
                                            xValues.add(Integer.toString(i * 5));
                                        }
                                        Log.d("CALL ENTRY", "entries values: " + entries);


                                    } else if (txtTimeframe.contains(getString(R.string.day))) {
                                        for (int i = 0; i < dataList.size(); i++) {
                                            entries.add(new Entry(i, dataList.get(i)));
                                        }
                                    } else if (txtTimeframe.contains(getString(R.string.week))) {
                                        ArrayList<String> list_ei = new ArrayList<>(xValues);
                                        xValues.clear();

                                        ArrayList<String> week = new ArrayList<>();
                                        ArrayList<Float> ave = new ArrayList<>();

                                        int temp = 0;
                                        float summary = 0;
                                        for (int i = 0; i < list_ei.size(); i++) {
                                            summary += dataList.get(i);
                                            temp++;
                                            if ((i < list_ei.size() - 1 && list_ei.get(i + 1) != list_ei.get(i)) || i == list_ei.size() - 1) {
                                                week.add(list_ei.get(i));
                                                if (week.size() > 0) {
                                                    ave.add(summary / temp);
                                                }
                                                temp = 0;
                                                summary = 0;
                                            }
                                        }
                                        for (int i = 0; i < week.size(); i++) {
                                            entries.add(new Entry(i, ave.get(i)));
                                            xValues.add(week.get(i));
                                        }
                                    } else if (txtTimeframe.contains(getString(R.string.month))) {
                                        ArrayList<String> th = new ArrayList<>(xValues);
                                        xValues.clear();

                                        ArrayList<String> month = new ArrayList<>();
                                        ArrayList<Float> ave = new ArrayList<>();

                                        int temp = 0;
                                        float summary = 0;
                                        for (int i = 0; i < th.size(); i++) {
                                            summary += dataList.get(i);
                                            temp++;
                                            if ((i < th.size() - 1 && th.get(i + 1) != th.get(i)) || i == th.size() - 1) {
                                                month.add(th.get(i));
                                                if (month.size() > 0) {
                                                    ave.add(summary / temp);
                                                }
                                                temp = 0;
                                                summary = 0;
                                            }
                                        }
                                        for (int i = 0; i < month.size(); i++) {
                                            entries.add(new Entry(i, ave.get(i)));
                                            xValues.add(month.get(i));
                                        }
                                    } else if (txtTimeframe.contains(getString(R.string.year))) {
                                        ArrayList<String> tw = new ArrayList<>(xValues);
                                        xValues.clear();

                                        ArrayList<String> year = new ArrayList<>();
                                        ArrayList<Float> average = new ArrayList<>();

                                        int temp = 0;
                                        float summary = 0;
                                        for (int i = 0; i < tw.size(); i++) {
                                            summary += dataList.get(i);
                                            temp++;
                                            if ((i < tw.size() - 1 && tw.get(i + 1) != tw.get(i)) || i == tw.size() - 1) {
                                                year.add(tw.get(i));
                                                if (year.size() > 0) {
                                                    average.add(summary / temp);
                                                }
                                                temp = 0;
                                                summary = 0;
                                            }

                                        }
                                        for (int i = 0; i < year.size(); i++) {
                                            entries.add(new Entry(i, average.get(i)));
                                            xValues.add(year.get(i));
                                        }
                                    }

                                    Log.d("CALL ENTRY", "entries values: " + entries);
//                                editTextDateTime.setText(ending.get(0).toString());
                                    LineDataSet dataSet = new LineDataSet(entries, selectedAttribute); // add entries to dataset
                                    if (txtTimeframe.contains(getString(R.string.hour))) {
                                        dataSet.enableDashedLine(0, 1, 0);
                                        chart.getXAxis().setLabelCount(entries.size() / 2);

                                    } else if (txtTimeframe.contains(getString(R.string.day))) {
                                        chart.getXAxis().setLabelCount(entries.size() / 2);
                                        dataSet.setDrawFilled(true);
                                    } else if (txtTimeframe.contains(getString(R.string.week))) {
                                        dataSet.setDrawFilled(true);
                                    } else if (txtTimeframe.contains(getString(R.string.month))) {
                                        chart.getXAxis().setLabelCount(entries.size() / 2);
                                        dataSet.setDrawFilled(true);
                                    } else if (txtTimeframe.contains(getString(R.string.year))) {
                                        chart.getXAxis().setLabelCount(entries.size() / 4);
                                        dataSet.setDrawFilled(true);
                                    }

                                    Log.d("CALL DATA", "data set values: " + dataSet);

                                    chart.setVisibility(View.VISIBLE);
                                    dataSet.setDrawCircles(true);
                                    dataSet.setDrawValues(false);

                                    dataSet.setColor(getColor(R.color.gradient_start));
                                    dataSet.setValueTextColor(Color.BLACK);
                                    dataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
                                    dataSet.setFillColor(getColor(R.color.back_gradient_start));
                                    dataSet.setCircleRadius(3f);
                                    dataSet.setCircleColor(getColor(R.color.gradient_start));
                                    LineData lineData = new LineData(dataSet);

                                    chart.getXAxis().setTextColor(R.color.back_gradient_start);
                                    chart.getAxisRight().setTextColor(R.color.back_gradient_start);
                                    chart.setData(lineData);
                                    if(txtAttribute.contains(getString(R.string.humidity))) {
                                        description = new Description();
                                        description.setPosition(150f, 5f);
                                        description.setText("%");
                                        description.setTextSize(14);
                                        chart.setDescription(description);
                                    }
                                    else if(txtAttribute.contains(getString(R.string.temperature))) {
                                        description = new Description();
                                        description.setPosition(150f, 5f);
                                        description.setTextSize(14);
                                        description.setText("\u2103");
                                        chart.setDescription(description);
                                    }
                                    else if(txtAttribute.contains(getString(R.string.rainfall))) {
                                        description = new Description();
                                        description.setPosition(150f, 5f);
                                        description.setText("mm");
                                        description.setTextSize(14);
                                        chart.setDescription(description);
                                    }
                                    else if(txtAttribute.contains(getString(R.string.wind_speed))) {
                                        description = new Description();
                                        description.setPosition(150f, 5f);
                                        description.setText("km/h");
                                        description.setTextSize(14);
                                        chart.setDescription(description);
                                    }

                                    chart.getDescription().setEnabled(true);
                                    chart.getAxisLeft().setDrawGridLines(true);
                                    chart.getAxisLeft().setDrawAxisLine(true);
                                    chart.getAxisLeft().setDrawLabels(true);
                                    chart.getXAxis().setDrawLabels(true);
                                    chart.getXAxis().setDrawGridLines(true);
                                    chart.getXAxis().setDrawAxisLine(true); // Only show the x-axis line
                                    chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

                                    if (xValues.size() == 1) {
                                        chart.getXAxis().setLabelCount(xValues.size());
                                    }
                                    chart.getAxisRight().setEnabled(false);
                                    chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xValues) {
                                    });
                                    chart.getLegend().setEnabled(true);
                                    chart.invalidate();
                                }
                                else {
                                    Toast.makeText(Chart.this, getString(R.string.warning_api),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } else if(response.code() == 403){
                                Log.d("CALL API", response.body().toString());
                                Toast.makeText(Chart.this, getString(R.string.get_permission),
                                        Toast.LENGTH_SHORT).show();
                                recreate();
                            } else {
                                Toast.makeText(Chart.this, getString(R.string.warning_api),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<JsonArray> call, Throwable t) {
                            Log.d("API CALL", t.getMessage().toString());
                        }
                    });
                }
            }
        });
    }

    private void showDateTimePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = dayOfMonth + "/" + (month + 1) + "/" + year;

                        // TimePickerDialog
                        TimePickerDialog timePickerDialog = new TimePickerDialog(Chart.this,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        String time;
                                        if(minute < 10) {
                                            time = hourOfDay + ":0" + minute;
                                        }
                                        else {
                                            time = hourOfDay + ":" + minute;
                                        }

                                        selectedDateTime = date + " " + time;

                                        editTextDateTime.setText(selectedDateTime);
                                    }
                                }, hour, minute, true); // show 24h clock

                        timePickerDialog.show();
                    }
                }, year, month, day);

        datePickerDialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.select_attributes) {
            txtAttribute = parent.getItemAtPosition(position).toString();
            if(txtAttribute.contains(getString(R.string.humidity))) {
                selectedAttribute = "humidity";
            }
            else if(txtAttribute.contains(getString(R.string.temperature))) {
                selectedAttribute = "temperature";
            }
            else if(txtAttribute.contains(getString(R.string.rainfall))) {
                selectedAttribute = "rainfall";
            }
            else if(txtAttribute.contains(getString(R.string.wind_speed))) {
                selectedAttribute = "windSpeed";
            }
        }

        if(parent.getId() == R.id.select_timeframes) {
            txtTimeframe = parent.getItemAtPosition(position).toString();
        }
        Log.d("SELECT ATTRIBUTE", selectedAttribute);
        Log.d("SELECT TIMEFRAME", txtTimeframe);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}