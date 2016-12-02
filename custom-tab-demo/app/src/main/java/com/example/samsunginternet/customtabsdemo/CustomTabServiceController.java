package com.example.samsunginternet.customtabsdemo;

import android.content.ComponentName;
import android.content.Context;
import android.net.Uri;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.text.TextUtils;

import java.lang.ref.WeakReference;

/**
 * Based on the example by Sergio R. Angeles:
 * https://github.com/sergiorangeles/Android-Custom-Tabs-Example
 */
public class CustomTabServiceController extends CustomTabsServiceConnection {

    private WeakReference<Context> mContextWeakRef;
    private String mUrlToLoad;
    private CustomTabsSession mCustomTabsSession;

    public CustomTabServiceController(Context context, String urlToLoad) {
        mContextWeakRef = new WeakReference<>(context);
        mUrlToLoad = urlToLoad;
    }

    @Override
    public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient customTabsClient) {

        if (customTabsClient != null) {

            // To make the page load faster
            customTabsClient.warmup(0L);

            // Create a new session
            mCustomTabsSession = customTabsClient.newSession(null);

            if (!TextUtils.isEmpty(mUrlToLoad)) {
                Uri uri = Uri.parse(mUrlToLoad);
                if (uri != null && mCustomTabsSession != null) {
                    // Let the session know that it may launch this URL soon
                    mCustomTabsSession.mayLaunchUrl(uri, null, null);
                }
            }
        }

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mCustomTabsSession = null;
    }

    public void bindCustomTabService() {
        Context ctx = mContextWeakRef.get();
        if (ctx != null) {
            CustomTabsClient.bindCustomTabsService(ctx, CustomTabHelper.getPackageNameToUse(ctx), this);
        }
    }

    public void unbindCustomTabService() {
        Context ctx = mContextWeakRef.get();
        if (ctx != null) {
            ctx.unbindService(this);
        }
    }

}
