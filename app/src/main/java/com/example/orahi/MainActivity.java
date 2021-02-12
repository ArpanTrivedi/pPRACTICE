package com.example.orahi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper myDB;

    private static final String url = "https://demo5636362.mockable.io/stats";
    public static ProgressDialog progressDialog;
    BarChart barChart;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        barChart = findViewById(R.id.barChart);
        requestQueue = Volley.newRequestQueue(this);

        myDB = new DatabaseHelper(this);
        getAllData();
    }

//    private void addData() {
//        boolean isInserted = myDB.insertIntoDB("name", 12);
//    }

    private void getAllData() {
        Cursor cursor = myDB.getDataFromDB();
        StringBuffer stringBuffer = new StringBuffer();

        if (cursor.getCount() == 0) {
            getApiData();
        }


        List<BarEntry> barEntries = new ArrayList<>();
        List<String> labelsName = new ArrayList<>();
        int i = 0;
        while (cursor.moveToNext()) {

            barEntries.add(new BarEntry(i, cursor.getInt(2)));
            labelsName.add(cursor.getString(1));
            i++;

        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "Month Stat");
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);

        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        //barDataSet.setColors(ColorTemplate.PASTEL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(22f);
        barChart.setFitBars(true);
        XAxis axis = barChart.getXAxis();
        axis.setValueFormatter(new IndexAxisValueFormatter(labelsName));
        axis.setPosition(XAxis.XAxisPosition.TOP_INSIDE);
        axis.setDrawAxisLine(false);
        axis.setDrawGridLines(false);
        axis.setGranularity(1f);
        axis.setLabelCount(labelsName.size());
        Description description = new Description();
        description.setText("Month Stat");
        barChart.setDescription(description);
        axis.setLabelRotationAngle(270);
        barChart.animateY(3000);
        barChart.invalidate();
    }
    private void getApiData() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.i("JSON","JSON:"+jsonObject);
                try {

                    String info = jsonObject.getString("data");
                    JSONArray jsonArray = new JSONArray(info);

                    List<BarEntry> barEntries = new ArrayList<>();
                    List<String> labelsName = new ArrayList<>();
                    progressDialog.dismiss();
                    for(int i = 0; i < jsonArray.length(); i++) {

                        JSONObject parObj = jsonArray.getJSONObject(i);
                        String month = parObj.getString("month");
                        String stat = parObj.getString("stat");
                        int statNumber = Integer.parseInt(stat);
                        boolean isInserted = myDB.insertIntoDB(month, statNumber);
                        barEntries.add(new BarEntry(i, statNumber));
                        labelsName.add(month);
                    }

                    BarDataSet barDataSet = new BarDataSet(barEntries, "Month Stat");
                    BarData barData = new BarData(barDataSet);
                    barChart.setData(barData);

                    barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                    //barDataSet.setColors(ColorTemplate.PASTEL_COLORS);
                    barDataSet.setValueTextColor(Color.BLACK);
                    barDataSet.setValueTextSize(22f);
                    barChart.setFitBars(true);
                    XAxis axis = barChart.getXAxis();
                    axis.setValueFormatter(new IndexAxisValueFormatter(labelsName));
                    axis.setPosition(XAxis.XAxisPosition.TOP_INSIDE);
                    axis.setDrawAxisLine(false);
                    axis.setDrawGridLines(false);
                    axis.setGranularity(1f);
                    axis.setLabelCount(labelsName.size());
                    Description description = new Description();
                    description.setText("Month Stat");
                    barChart.setDescription(description);
                    axis.setLabelRotationAngle(270);
                    barChart.animateY(3000);
                    barChart.invalidate();

                } catch (JSONException e) {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this,"Some error comes",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this,"Some error comes please check your Internet connection",Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, login.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}