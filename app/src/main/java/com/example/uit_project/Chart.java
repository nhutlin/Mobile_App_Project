package com.example.uit_project;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uit_project.api.APIService;
import com.example.uit_project.model.datapoint.Datapoint;
import com.example.uit_project.model.datapoint.RequestBody;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Chart extends AppCompatActivity {

    String[] attributes = {"temperature","humidity","rainfall","windSpeed"};


    String end;
    AutoCompleteTextView selectAttribute;
    AutoCompleteTextView selectTimeStamp;
    AutoCompleteTextView selectDate;
    ArrayAdapter<String> adapterAttributes;
    ArrayAdapter<String> adapterTimes;

    long toTimeStamp;
    long fromTimeStamp;
    RequestBody body;

    ImageButton back;
    ArrayList<Float> dataList = new ArrayList<>();
    ArrayList<String> xValues = new ArrayList<>();
    ArrayList<String> ending = new ArrayList<>();
    String attributeRequest = "temperature";
    String timeRequest = "Day";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);


        String[] times = {"Hour","Day","Week","Month","Year"};

        selectAttribute = findViewById(R.id.attribute);
        selectTimeStamp = findViewById(R.id.time);
        selectDate = findViewById(R.id.ending);
        LineChart chart = findViewById(R.id.chart);
        back = findViewById(R.id.btn_back);
        Button show = findViewById(R.id.btn_show);

        selectTimeStamp.setText("Day");
        selectAttribute.setText("temperature");
        adapterAttributes = new ArrayAdapter<String>(this,R.layout.list_attribute,attributes);
        selectAttribute.setAdapter(adapterAttributes);


        adapterTimes = new ArrayAdapter<String>(this,R.layout.list_timeframe,times);
        selectTimeStamp.setAdapter(adapterTimes);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        selectAttribute.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String attribute = parent.getItemAtPosition(position).toString();
                attributeRequest = attribute;
            }
        });

        selectTimeStamp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String timestamp = parent.getItemAtPosition(position).toString();
                timeRequest = timestamp;
            }
        });



        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getHour = getString(R.string.hour).toString();
                String getDay = getString(R.string.day).toString();
                String getWeek = getString(R.string.week).toString();
                String getMonth = getString(R.string.month).toString();
                String getYear = getString(R.string.year).toString();

                if(timeRequest == "Hour") { //|| timeRequest == "Giờ"){
                    toTimeStamp = System.currentTimeMillis();
                    fromTimeStamp = toTimeStamp - 3600000;
                    body = new RequestBody((long) fromTimeStamp, (long) toTimeStamp,"string");

                } else if (timeRequest.equalsIgnoreCase("Day")) { //|| timeRequest == "Ngày") {
                    toTimeStamp = System.currentTimeMillis();
                    fromTimeStamp = toTimeStamp - 86400000 ;
                    body = new RequestBody((long) fromTimeStamp, (long) toTimeStamp,"string");
                } else if (timeRequest == "Week"){ //|| timeRequest == "Tuần") {
                    toTimeStamp = System.currentTimeMillis();
                    fromTimeStamp = toTimeStamp - 604800000 ;
                    body = new RequestBody((long) fromTimeStamp, (long) toTimeStamp,"string");
                } else if (timeRequest == "Month"){ //|| timeRequest == "Tháng"){
                    toTimeStamp = System.currentTimeMillis();
                    fromTimeStamp = toTimeStamp - 2678400000L ;
                    body = new RequestBody((long) fromTimeStamp, (long) toTimeStamp,"string");
                } else if (timeRequest == "Year"){
                    toTimeStamp = System.currentTimeMillis();
                    fromTimeStamp = toTimeStamp - 31536000000L ;
                    body = new RequestBody((long) fromTimeStamp, (long) toTimeStamp,"string");
                }
//                if(timeRequest.equalsIgnoreCase(getHour)) { //|| timeRequest == "Giờ"){
//                    toTimeStamp = System.currentTimeMillis();
//                    fromTimeStamp = toTimeStamp - 3600000;
//                    body = new RequestBody((long) fromTimeStamp, (long) toTimeStamp,"string");
//
//                } else if (timeRequest.equalsIgnoreCase(getDay)) { //|| timeRequest == "Ngày") {
//                    toTimeStamp = System.currentTimeMillis();
//                    fromTimeStamp = toTimeStamp - 86400000 ;
//                    body = new RequestBody((long) fromTimeStamp, (long) toTimeStamp,"string");
//                } else if (timeRequest.equalsIgnoreCase(getWeek)){ //|| timeRequest == "Tuần") {
//                    toTimeStamp = System.currentTimeMillis();
//                    fromTimeStamp = toTimeStamp - 604800000 ;
//                    body = new RequestBody((long) fromTimeStamp, (long) toTimeStamp,"string");
//                } else if (timeRequest.equalsIgnoreCase(getMonth)){ //|| timeRequest == "Tháng"){
//                    toTimeStamp = System.currentTimeMillis();
//                    fromTimeStamp = toTimeStamp - 2678400000L ;
//                    body = new RequestBody((long) fromTimeStamp, (long) toTimeStamp,"string");
//                } else if (timeRequest.equalsIgnoreCase(getYear)){
//                    toTimeStamp = System.currentTimeMillis();
//                    fromTimeStamp = toTimeStamp - 31536000000L ;
//                    body = new RequestBody((long) fromTimeStamp, (long) toTimeStamp,"string");
//                }

                Call<JsonArray> call = APIService.apiService.getDatapoint("Bearer " + GlobalVar.token, "5zI6XqkQVSfdgOrZ1MyWEf", attributeRequest,body);
                call.enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        Log.d("API CALL", response.code() + "");

                        JsonArray jsonArray = response.body();
                        if (jsonArray != null) {
                            Gson gson = new Gson();
                            dataList.clear();
                            xValues.clear();
                            Type listType = new TypeToken<List<Datapoint>>() {}.getType();
                            List<Datapoint> dataPoints = gson.fromJson(jsonArray, listType);

                            for (Datapoint dataPoint : dataPoints) {
                                long x = dataPoint.getX();
                                float y = (float)dataPoint.getY();
                                SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
                                String end = sdf1.format(x);
                                ending.add(end);
                                dataList.add(0, y);

                                if(timeRequest == "Hour"){
                                    Date date = new Date(x);
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(date);
                                    int minuteofhour = calendar.get(Calendar.MINUTE);
                                    xValues.add(0,Integer.toString(minuteofhour));
                                } else if (timeRequest == "Day") {
                                    SimpleDateFormat sdf2 = new SimpleDateFormat("HH", Locale.getDefault());
                                    String formattedHour = sdf2.format(x);
                                    xValues.add(0,formattedHour);
                                } else if(timeRequest == "Week"){
                                    Date date = new Date(x);
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(date);
                                    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                                    String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
                                    String dayName = days[dayOfWeek - 1];
                                    xValues.add(0,dayName);
                                } else if (timeRequest == "Month") {
                                    Date date = new Date(x);
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(date);
                                    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                                    xValues.add(0,Integer.toString(dayOfMonth));
                                } else if (timeRequest == "Year") {
                                    Date date = new Date(x);
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(date);
                                    int month = calendar.get(Calendar.MONTH);
                                    xValues.add(0,Integer.toString(month));
                                }
                            }
                            List<Entry> entries = new ArrayList<>();

                            if(timeRequest == "Hour"){
                                int minute = Integer.parseInt(xValues.get(0));
                                xValues.clear();
                                for (int i = 0; i < 12; i++) {
                                    if(Math.abs(minute-i*5)<3){
                                        entries.add(new Entry(i, dataList.get(0)));
                                    }
                                    else {
                                        entries.add(new Entry(i, -1));
                                    }
                                    xValues.add(Integer.toString(i*5));
                                }
                            }
                            else if(timeRequest == "Day"){
                                for (int i = 0; i < dataList.size(); i++) {
                                    entries.add(new Entry(i,dataList.get(i)));
                                }
                            }
                            else if (timeRequest == "Week") {
                                ArrayList<String> list_ei = new ArrayList<>(xValues);
                                xValues.clear();

                                ArrayList<String> week = new ArrayList<>();
                                ArrayList<Float> ave = new ArrayList<>();

                                int temp = 0;
                                float summary = 0;
                                for (int i = 0;i < list_ei.size(); i++){
                                    summary += dataList.get(i);
                                    temp++;
                                    if((i < list_ei.size() - 1 && list_ei.get(i + 1) != list_ei.get(i)) || i == list_ei.size() - 1){
                                        week.add(list_ei.get(i));
                                        if(week.size() > 0){
                                            ave.add(summary /temp);
                                        }
                                        temp = 0;
                                        summary = 0;
                                    }

                                }
                                for (int i=0; i<week.size(); i++){
                                    entries.add(new Entry(i, ave.get(i)));
                                    xValues.add(week.get(i));
                                }
                            } else if (timeRequest == "Month") {
                                ArrayList<String> th = new ArrayList<>(xValues);
                                xValues.clear();

                                ArrayList<String> month = new ArrayList<>();
                                ArrayList<Float> ave = new ArrayList<>();

                                int temp = 0;
                                float summary =0;
                                for (int i = 0; i<th.size(); i++){
                                    summary += dataList.get(i);
                                    temp++;
                                    if((i < th.size() - 1 && th.get(i + 1) != th.get(i)) || i == th.size() - 1){
                                        month.add(th.get(i));
                                        if(month.size() > 0){
                                            ave.add(summary / temp);
                                        }
                                        temp = 0;
                                        summary = 0;
                                    }

                                }
                                for (int i=0;i<month.size();i++){
                                    entries.add(new Entry(i, ave.get(i)));
                                    xValues.add(month.get(i));
                                }
                            } else if (timeRequest == "Year") {
                                ArrayList<String> tw = new ArrayList<>(xValues);
                                xValues.clear();

                                ArrayList<String> year = new ArrayList<>();
                                ArrayList<Float> average = new ArrayList<>();

                                int temp = 0;
                                float summary = 0;
                                for (int i = 0; i < tw.size(); i++){
                                    summary += dataList.get(i);
                                    temp++;
                                    if((i < tw.size() - 1 && tw.get(i + 1) != tw.get(i)) || i == tw.size() - 1){
                                        year.add(tw.get(i));
                                        if(year.size() > 0){
                                            average.add(summary/temp);
                                        }
                                        temp = 0;
                                        summary = 0;
                                    }

                                }
                                for (int i = 0; i < year.size(); i++){
                                    entries.add(new Entry(i, average.get(i)));
                                    xValues.add(year.get(i));
                                }
                            }

                            selectDate.setText(ending.get(0).toString());
                            LineDataSet dataSet = new LineDataSet(entries, attributeRequest); // add entries to dataset
                            if(timeRequest == "Hour"){
                                dataSet.enableDashedLine(0, 1, 0);
                                chart.getXAxis().setLabelCount(entries.size() / 2);
                            } else if (timeRequest == "Day") {
//
                                chart.getXAxis().setLabelCount(entries.size() / 2);
                                dataSet.setDrawFilled(true);
                            } else if (timeRequest == "Week") {
                                dataSet.setDrawFilled(true);
                            } else if (timeRequest == "Month") {
                                chart.getXAxis().setLabelCount(entries.size() / 2);
                                dataSet.setDrawFilled(true);
                            } else if (timeRequest == "Year") {
                                chart.getXAxis().setLabelCount(entries.size() / 4);
                                dataSet.setDrawFilled(true);}

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
                            chart.setData(lineData);

                            chart.getDescription().setEnabled(false);

                            chart.getAxisLeft().setDrawGridLines(true);
                            chart.getAxisLeft().setDrawAxisLine(true);
                            chart.getAxisLeft().setDrawLabels(true);
                            chart.getXAxis().setDrawLabels(true);
                            chart.getXAxis().setDrawGridLines(true);
                            chart.getXAxis().setDrawAxisLine(true); // Only show the x-axis line
                            chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

                            if(xValues.size() == 1){
                                chart.getXAxis().setLabelCount(xValues.size());
                            }
                            chart.getAxisRight().setEnabled(false);
                            chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xValues){});
                            chart.getLegend().setEnabled(true);

                            chart.invalidate();
                        }
                        else {
                            Log.d("API CALL", "Fail to load chart");
                        }
                    }
                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        Log.d("API CALL", t.getMessage().toString());
                    }
                });
            }

        });
    }

}