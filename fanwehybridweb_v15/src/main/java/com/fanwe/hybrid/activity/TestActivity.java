package com.fanwe.hybrid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.fanwe.hybrid.constant.ApkConstant;

import cn.fanwe.yi.R;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnUrlOnline;
    private Button btnUrlColleague;
    private Button btnUrlCompany;
    private Button btnUrlHome;
    private Intent intent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        initView();
    }

    private void initView() {
        btnUrlOnline = findViewById(R.id.btn_url_online);
        btnUrlColleague = findViewById(R.id.btn_url_colleague);
        btnUrlCompany = findViewById(R.id.btn_url_company);
        btnUrlHome = findViewById(R.id.btn_url_home);

        btnUrlOnline.setOnClickListener(this);
        btnUrlColleague.setOnClickListener(this);
        btnUrlCompany.setOnClickListener(this);
        btnUrlHome.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_url_online:
                ApkConstant.SERVER_URL = "http://www.fields.gold";
                intent = new Intent(TestActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_url_colleague:
                ApkConstant.SERVER_URL = "http://192.168.2.172:8889";
                intent = new Intent(TestActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_url_company:
                ApkConstant.SERVER_URL = "http://192.168.10.142:8889";
                intent = new Intent(TestActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_url_home:
                ApkConstant.SERVER_URL = "http://192.168.1.102:8889";
                intent = new Intent(TestActivity.this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }
}
