package com.example.samsunginternet.customtabsdemo;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final String CUSTOM_TABS_EXTRA_SESSION =
            "android.support.customtabs.extra.SESSION";
    private static final String CUSTOM_TABS_TOOLBAR_COLOR =
            "android.support.customtabs.extra.TOOLBAR_COLOR";

    private static final String URL_TO_LOAD = "http://developer.samsung.com/internet";
    private static final int TOOLBAR_COLOR = Color.rgb(55, 73, 113);

    private CustomTabServiceController customTabServiceController;
    private Intent customTabIntent;

    protected void setupCustomTabService() {
        customTabServiceController = new CustomTabServiceController(this, URL_TO_LOAD);
        customTabServiceController.bindCustomTabService();
    }

    protected void setupCustomTabIntent() {

        customTabIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL_TO_LOAD));
        customTabIntent.setPackage(CustomTabHelper.getPackageNameToUse(this));

        Bundle extras = new Bundle();
        // Used to match session. Even if not used, has to be present with null to launch custom tab
        extras.putBinder(CUSTOM_TABS_EXTRA_SESSION, null);
        extras.putInt(CUSTOM_TABS_TOOLBAR_COLOR, TOOLBAR_COLOR);
        customTabIntent.putExtras(extras);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupCustomTabService();
        setupCustomTabIntent();

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
