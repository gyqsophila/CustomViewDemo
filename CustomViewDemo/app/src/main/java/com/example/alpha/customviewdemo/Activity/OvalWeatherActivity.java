package com.example.alpha.customviewdemo.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.example.alpha.customviewdemo.R;
import com.example.alpha.customviewdemo.UI.OvalDegree;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 放置OvalWeatherView的demo
 * Created by Alpha on 2016/7/29.
 */
public class OvalWeatherActivity extends AppCompatActivity {
    @BindView(R.id.weather_view)
    OvalDegree weatherView;

    public static final String TAG = "OvalWeatherActivity";
    @BindView(R.id.currentTemp)
    EditText currentTemp;
    @BindView(R.id.weatherType)
    EditText weatherType;
    @BindView(R.id.changeWeather)
    Button changeWeather;
    @BindView(R.id.minTemp)
    EditText minTemp;
    @BindView(R.id.maxTemp)
    EditText maxTemp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lauout_oval_weather);
        ButterKnife.bind(this);
        weatherView.setWeatherType("多云转晴");
    }

    @OnClick(R.id.changeWeather)
    public void onClick() {
        weatherView.setMinTemperature(Integer.parseInt(minTemp.getText().toString()));
        weatherView.setMaxTemperature(Integer.parseInt(maxTemp.getText().toString()));
        weatherView.setCurrentTemperature(Integer.valueOf(currentTemp.getText().toString()));
        weatherView.setWeatherType(weatherType.getText().toString());
        weatherView.setDrawScale(false);
    }
}
