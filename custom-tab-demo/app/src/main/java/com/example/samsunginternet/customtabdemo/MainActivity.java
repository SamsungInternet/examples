package com.example.samsunginternet.customtabdemo;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.samsunginternet.customtabdemo.customtab.CustomTabHelper;
import com.example.samsunginternet.customtabdemo.customtab.CustomTabServiceController;

public class MainActivity extends AppCompatActivity {

    private static final String URL_TO_LOAD = "https://medium.com/samsung-internet-dev";
    private static final int TOOLBAR_COLOR = Color.rgb(152, 127, 255);

    private CustomTabServiceController customTabServiceController;
    private Intent customTabIntent;

    protected void setupCustomTabs() {
        customTabServiceController = new CustomTabServiceController(this, URL_TO_LOAD);
        customTabServiceController.bindCustomTabService();
        customTabIntent = customTabServiceController.createCustomTabIntent(null, TOOLBAR_COLOR);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupCustomTabs();

        final Button customTabButton = (Button) findViewById(R.id.buttonCustomTab);

        customTabButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(customTabIntent);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        customTabServiceController.unbindCustomTabService();
    }
}
