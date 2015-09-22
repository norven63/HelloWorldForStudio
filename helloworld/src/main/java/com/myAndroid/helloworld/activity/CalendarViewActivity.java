package com.myAndroid.helloworld.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;

import com.myAndroid.helloworld.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CalendarViewActivity extends Activity {
    @Bind(R.id.calendarView)
    CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_calendar_view);
        ButterKnife.bind(this);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Toast.makeText(CalendarViewActivity.this, year + "年" + (month + 1) + "月" + dayOfMonth + "日" + "\nTime: " + view.getDate(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
