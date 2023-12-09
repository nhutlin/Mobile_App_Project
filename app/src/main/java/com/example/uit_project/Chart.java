package com.example.uit_project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.uit_project.model.weather.Humidity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.EntryXComparator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Chart extends AppCompatActivity {

    private LineChart lineChart;

    private AutoCompleteTextView selectAttribute;
    private AutoCompleteTextView selectTimeframe;
    private ArrayAdapter<String> adapterAttribute;
    private ArrayAdapter<String> adapterTimeframe;
    private EditText selectDate;

    private long toTimestamp;
    private long fromTimestamp;
    private int selectedSecond = 0;
    private int selectedDay = 0;
    private int selectedMonth = 0;
    private int selectedYear = 0;
    private int selectedHour = 0;
    private int selectedMinute = 0;
    private Button show;
    List<String> xValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        show = findViewById(R.id.btn_show);

        showInput();
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChart();
            }
        });
    }

    public void showInput() {
        String[] attributes = {getString(R.string.humidity) + " (%)",
                getString(R.string.temperature) + " (\u2103)",
                getString(R.string.rainfall) + " (mm)",
                getString(R.string.wind_speed) + " (km/h)"};
        String[] timeframes = {getString(R.string.hour),
                getString(R.string.day),
                getString(R.string.week),
                getString(R.string.month),
                getString(R.string.year)};

        selectAttribute = findViewById(R.id.select_attribute_input);
        selectTimeframe = findViewById(R.id.select_timeframe_input);
        selectDate = findViewById(R.id.select_date_input);

        adapterAttribute = new ArrayAdapter<String>(this, R.layout.list_attribute, attributes);
        adapterTimeframe = new ArrayAdapter<String>(this, R.layout.list_timeframe, timeframes);


        selectAttribute.setAdapter(adapterAttribute);
        selectTimeframe.setAdapter(adapterTimeframe);

        selectAttribute.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String attribute = adapterView.getItemAtPosition(position).toString();
            }
        });

        selectTimeframe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String timeframe = adapterView.getItemAtPosition(position).toString();
            }
        });

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePickerDialog();
            }
        });

    }

    public void showDateTimePickerDialog() {
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
                        // Xử lý ngày tháng năm được chọn
//                        selectedYear = year;
//                        selectedMonth = month;
//                        selectedDay = dayOfMonth;
                        String date = dayOfMonth + "/" + (month + 1) + "/" + year;

                        // TimePickerDialog
                        TimePickerDialog timePickerDialog = new TimePickerDialog(Chart.this,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        // Xử lý giờ phút được chọn
                                        String time = hourOfDay + ":" + minute + ":" + 0;

                                        // Kết hợp ngày, giờ và giây để có ngày giờ đầy đủ
                                        String selectedDateTime = date + " " +
                                                String.format(Locale.getDefault(), "%02d:%02d:%02d", hourOfDay, minute, selectedSecond);

                                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

                                        try {
                                            // Define the date format
                                            Date date = dateFormat.parse(selectedDateTime);

                                            // Convert to epoch time in milliseconds
                                            toTimestamp = date.getTime();
                                            Toast.makeText(Chart.this, String.valueOf(toTimestamp), Toast.LENGTH_SHORT).show();


                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                            Toast.makeText(Chart.this, "FAIL", Toast.LENGTH_SHORT).show();
                                        }
                                        // Hiển thị lên EditText
                                        selectDate.setText(selectedDateTime);
                                    }
                                }, hour, minute, true); // Đặt true để hiển thị đồng hồ 24 giờ

                        // Hiển thị hộp thoại chọn giờ
                        timePickerDialog.show();
                    }
                }, year, month, day);

        // Hiển thị hộp thoại chọn ngày
        datePickerDialog.show();

    }

    public void showChart() {

        String inputTimeStamp = selectTimeframe.getText().toString();
        String inputAttribute = selectAttribute.getText().toString();
        lineChart = findViewById(R.id.chart);
        Description description = new Description();
        description.setPosition(150f, 15f);
        xValue = new ArrayList<>();
        long step = 0;
        int count = 0;
        switch (inputTimeStamp) {
            case "Hour":
                fromTimestamp = toTimestamp - 3600;
                step = 600;
                break;

            case "Day":
                fromTimestamp = toTimestamp - 86400;
                step = 3600;
                break;

            case "Week":
                fromTimestamp = toTimestamp - 604800;
                step = 3600;
                break;

            case "Month":
                fromTimestamp = toTimestamp - 2629743;
                step = 86400;
                break;

            case "Year":
                fromTimestamp = toTimestamp - 31556926;
                step = 2629743;
                break;

            default:
                break;
        }

        switch (inputAttribute) {
            case "Temperature":
                description.setText("Temperature");
                break;
            case "Humidity":
                description.setText("Humidity");
                break;
            case "Rainfall":
                description.setText("Rainfall");
                break;
            case "Wind speed":
                description.setText("Wind speed");
                break;
            default:
                break;
        }
        for(long i = fromTimestamp; i <= toTimestamp; i += step) {
            Date date = new Date(i);

            // Define the date format
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            count++;
            // Format the date as a human-readable string
            String humanReadableDate = dateFormat.format(date);
            xValue.add(humanReadableDate);
            Log.v("FOR", "OK");
        }



//        lineChart.setDescription(description);
//        lineChart.getAxisRight().setDrawLabels(false);
//
//        XAxis xAxis = lineChart.getXAxis();
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setValueFormatter(new IndexAxisValueFormatter(xValue));
//        xAxis.setLabelCount(count);
//        xAxis.setGranularity(1f);
//
//        YAxis yAxis = lineChart.getAxisLeft();
//        yAxis.setAxisMinimum(0f);
//        yAxis.setAxisMaximum(100f);
//        yAxis.setAxisLineWidth(2f);
//        yAxis.setAxisLineColor(Color.BLACK);
//
//        List<Entry> entries1 = new ArrayList<>();
//        entries1.add(new Entry(0, 10f));
//        entries1.add(new Entry(1, 10f));
//        entries1.add(new Entry(2, 20f));
//
//
//
//
//        List<Entry> entries2 = new ArrayList<>();
//        entries2.add(new Entry(0, 10f));
//        entries2.add(new Entry(1, 10f));
//        entries2.add(new Entry(2, 15f));
//
//
//
//        LineDataSet dataSet1 = new LineDataSet(entries1, "Math");
//        dataSet1.setColor(Color.BLUE);
//
//        LineDataSet dataSet2 = new LineDataSet(entries2, "English");
//        dataSet2.setColor(Color.RED);
//
//        LineData lineData = new LineData(dataSet1, dataSet2);
//        lineChart.setData(lineData);
//        lineChart.invalidate();
    }

}