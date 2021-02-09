package com.example.orahi;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

//    BarChart barChart;
//    BarData barData;
//    BarDataSet barDataSet;
//    ArrayList<BarEntry> barEntries;
    ArrayList<String> labelsNames;
    RequestQueue requestQueue;
    TextView text;
    ArrayList<DataDAO> apiDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = findViewById(R.id.text);

        apiDetails = new ArrayList<>();
        labelsNames = new ArrayList<>();

        requestQueue = Volley.newRequestQueue(this);
        getApiData();
        getEntries();
//        barChart = findViewById(R.id.barChart);
//
//        getEntries();
//
//        barDataSet = new BarDataSet(barEntries, "Month wise stat");
//        barData = new BarData(barDataSet);
//        barChart.setData(barData);
//
//        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
//        //barDataSet.setColors(ColorTemplate.PASTEL_COLORS);
//        barDataSet.setValueTextColor(Color.BLACK);
//        barDataSet.setValueTextSize(16f);
//        barChart.setFitBars(true);
//        XAxis axis = barChart.getXAxis();
//        axis.setValueFormatter(new IndexAxisValueFormatter(labelsNames));
//        axis.setPosition(XAxis.XAxisPosition.TOP);
//        axis.setDrawAxisLine(false);
//        axis.setDrawGridLines(false);
//        axis.setGranularity(1f);
//        axis.setLabelCount(labelsNames.size());
//        axis.setLabelRotationAngle(270);
//        barChart.animateY(3000);
//        barChart.invalidate();
    }

    private void getApiData() {
        String api = "https://demo5636362.mockable.io/stats";
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, api, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.i("JSON","JSON:"+jsonObject);
                try {

                    String info = jsonObject.getString("data");
                    JSONArray jsonArray = new JSONArray(info);

                    for(int i = 0; i < jsonArray.length(); i++) {

                        JSONObject parObj = jsonArray.getJSONObject(i);
                        String month = parObj.getString("month");
                        String stat = parObj.getString("stat");
                        int statNumber = Integer.parseInt(stat);
                        DataDAO dataDAO = new DataDAO(month, statNumber);
                        apiDetails.add(dataDAO);
                        Log.i("NEW APIDE",
                                "" + apiDetails.get(i).getMonth());

                    }
                } catch (JSONException e) {
                    Toast.makeText(MainActivity.this,"Some error comes",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"Some error comes please check your Internet connection",Toast.LENGTH_SHORT).show();
            }
        });
        Log.i("ARRAYLIST SIZE", "=" +apiDetails.size());
        requestQueue.add(jsonObjectRequest);
    }


    private void getEntries() {
        //barEntries = new ArrayList<>();

        for (int i = 0; i < apiDetails.size(); i++) {
            Log.w("NEW APIDE",
                    "NEW DATA:" + apiDetails.get(i).toString());
            String month = apiDetails.get(i).getMonth();
            int stats = apiDetails.get(i).getStat();
            //barEntries.add(new BarEntry(i, stats));
            month += " Stat:= " + stats;
            Log.i("MONTH DETAILS ", "="+month);
            labelsNames.add(month);
        }
        String s = "";
        for (int i = 0; i < labelsNames.size(); i++)
            s += labelsNames.get(i);
        text.setText(s);
    }
}