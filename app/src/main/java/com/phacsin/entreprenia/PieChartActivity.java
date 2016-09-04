package com.phacsin.entreprenia;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.WindowManager;


import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.phacsin.entreprenia.main.ContactUs;
import com.phacsin.entreprenia.main.Entreprenia;
import com.phacsin.entreprenia.main.EventsActivity;
import com.phacsin.entreprenia.main.partners.Partners;
import com.phacsin.entreprenia.main.speeker.Speakers;
import com.phacsin.entreprenia.registration.SignupActivity;

import java.util.ArrayList;

/**
 * Created by Bineesh P Babu on 21-08-2016.
 */
public class PieChartActivity extends DemoBase  implements OnChartValueSelectedListener {
    private PieChart mChart;
    String[] mMonths = new String[] {"ENTREPRENIA", "SPEAKERS", "EVENTS", "REGISTER", "CONTACT US", "PARTNERS"};
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_piechart);
        sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);

        mChart = (PieChart) findViewById(R.id.chart1);



        mChart.setUsePercentValues(false);
        mChart.setDescription("www.entreprenia.org");
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setOnChartValueSelectedListener(this);

        mChart.setDragDecelerationFrictionCoef(0.95f);

        mChart.setCenterTextTypeface(mTfLight);
        mChart.setCenterText(generateCenterSpannableText());

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);
        mChart.setHighlightPerTapEnabled(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        mChart.setClickable(true);

        setData(6, 100);

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        // entry label styling
        mChart.setEntryLabelColor(Color.WHITE);
        mChart.setEntryLabelTypeface(mTfRegular);
        mChart.setEntryLabelTextSize(13f);


    }

 private void setData(int count, float range) {


        final ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.

            entries.add(new PieEntry((float) 16.10,"ENTREPRENIA"));
            entries.add(new PieEntry((float) 16.11,"SPEAKERS"));
            entries.add(new PieEntry((float) 16.12,"EVENTS"));
            if(sharedPreferences.getString("email","").equals(""))
                entries.add(new PieEntry((float) 16.13,"REGISTER"));
            else
                entries.add(new PieEntry((float) 16.13,"PROFILE"));
            entries.add(new PieEntry((float) 16.14,"CONTACT"));
            entries.add(new PieEntry((float) 16.15,"PARTNERS"));



     PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        /*dataSet.setSelectionShift(0f);*/
        PieData data = new PieData(dataSet);
        data.setValueTextSize(0f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(mTfLight);
     mChart.setData(data);

        // undo all highlights


     mChart.highlightValues(null);
      mChart.invalidate();

     mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
         @Override
         public void onValueSelected(Entry e, Highlight h) {
            float val=e.getY();
           String va= Float.toString(val);
             if(va.equals("16.1")){
                 Intent i= new Intent(getApplicationContext(),Entreprenia.class);
                 startActivity(i);

             }
             else if(va.equals("16.11")){
                 Intent i= new Intent(getApplicationContext(),Speakers.class);
                 startActivity(i);             }
             else if(va.equals("16.12")){
                 Intent i= new Intent(getApplicationContext(),EventsActivity.class);
                 startActivity(i);
             }else if(va.equals("16.13")){
                 if(sharedPreferences.getString("email","").equals("")){
                     Intent i = new Intent(getApplicationContext(), SignupActivity.class);
                     startActivity(i);
                 }
                 else
                 {
                     Intent i = new Intent(getApplicationContext(), Profile.class);
                     startActivity(i);
                 }
             }else if(va.equals("16.14")){
                 Intent i= new Intent(getApplicationContext(),ContactUs.class);
                 startActivity(i);
             }else if(va.equals("16.15")){
                 Intent i= new Intent(getApplicationContext(),Partners.class);
                 startActivity(i);             }

         }

         @Override
         public void onNothingSelected() {

         }
     });
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("Entreprenia'16\nby iEDC NSSCE");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 15, 0);

        s.setSpan(new StyleSpan(Typeface.NORMAL), 0, 14, 0);

        s.setSpan(new ForegroundColorSpan(Color.GRAY), 15, 17, 0);

        s.setSpan(new RelativeSizeSpan(.8f), 18,28, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), 18, 28, 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), 18,28, 0);
        return s;
    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
