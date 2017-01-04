package com.example.samsunginternet.customtabdemo.customtab;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
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

    private static final String CUSTOM_TABS_EXTRA_SESSION =
            "android.support.customtabs.extra.SESSION";
    private static final String CUSTOM_TABS_TOOLBAR_COLOR =
            "android.support.customtabs.extra.TOOLBAR_COLOR";

    private WeakReference<Context> contextWeakRef;
    private String urlToLoad;
    private CustomTabsSession customTabsSession;

    public CustomTabServiceController(Context context, String urlToLoad) {
        contextWeakRef = new WeakReference<>(context);
        this.urlToLoad = urlToLoad;
    }

    @Override
    public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient customTabsClient) {

        if (customTabsClient != null) {

            // To make the page load faster (flag argument is reserved for future use)
            customTabsClient.warmup(0L);

            // Create a new session
            customTabsSession = customTabsClient.newSession(null);

            if (!TextUtils.isEmpty(urlToLoad)) {
                Uri uri = Uri.parse(urlToLoad);
                if (uri != null && customTabsSession != null) {
                    // Let session know that it may launch this URL soon, for performance optimisation
                    customTabsSession.mayLaunchUrl(uri, null, null);
                }
            }
        }

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        customTabsSession = null;
    }

    public void bindCustomTabService() {
        Context ctx = contextWeakRef.get();
        if (ctx != null) {
            CustomTabsClient.bindCustomTabsService(ctx, CustomTabHelper.getPackageNameToUse(ctx), this);
        }
    }

    public void unbindCustomTabService() {
        Context ctx = contextWeakRef.get();
        if (ctx != null) {
            ctx.unbindService(this);
        }
    }

    public Intent createCustomTabIntent(Binder session, int toolbarColor) {
        Context ctx = contextWeakRef.get();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlToLoad));
        intent.setPackage(CustomTabHelper.getPackageNameToUse(ctx));

        Bundle extras = new Bundle();
        extras.putInt(CUSTOM_TABS_TOOLBAR_COLOR, toolbarColor);
        // Used to match session. Even if not used, has to be present with null to launch custom tab
        extras.putBinder(CUSTOM_TABS_EXTRA_SESSION, session);
        // Add the referrer
        extras.putParcelable(Intent.EXTRA_REFERRER, Uri.parse(Intent.URI_ANDROID_APP_SCHEME + "//" + ctx.getPackageName()));

        intent.putExtras(extras);

        return intent;
    }

}
