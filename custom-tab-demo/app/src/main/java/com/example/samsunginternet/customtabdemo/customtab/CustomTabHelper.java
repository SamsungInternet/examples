package com.example.samsunginternet.customtabdemo.customtab;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import static android.support.customtabs.CustomTabsService.ACTION_CUSTOM_TABS_CONNECTION;

/**
 * Simplified version based on the CustomTabsHelper from:
 * https://github.com/GoogleChrome/custom-tabs-client
 * Copyright Google Inc. Licence: http://www.apache.org/licenses/LICENSE-2.0
 */
public class CustomTabHelper {

    public static String getPackageNameToUse(Context context) {

        PackageManager pm = context.getPackageManager();

        // Get default VIEW intent handler
        Intent activityIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.samsung.com"));
        ResolveInfo defaultViewHandlerInfo = pm.resolveActivity(activityIntent, 0);

        String defaultViewHandlerPackageName = null;

        if (defaultViewHandlerInfo != null) {
            defaultViewHandlerPackageName = defaultViewHandlerInfo.activityInfo.packageName;
        }

        // Get all apps that can handle VIEW intents
        List<ResolveInfo> resolvedActivityList = pm.queryIntentActivities(activityIntent, 0);
        List<String> packagesSupportingCustomTabs = new ArrayList<>();

        for (ResolveInfo info : resolvedActivityList) {
            Intent serviceIntent = new Intent();
            serviceIntent.setAction(ACTION_CUSTOM_TABS_CONNECTION);
            serviceIntent.setPackage(info.activityInfo.packageName);
            if (pm.resolveService(serviceIntent, 0) != null) {
                packagesSupportingCustomTabs.add(info.activityInfo.packageName);
            }
        }

        // Now our list contains all apps that can handle both VIEW intents & service calls
        if (packagesSupportingCustomTabs.size() > 0) {

            if (!TextUtils.isEmpty(defaultViewHandlerPackageName) &&
                packagesSupportingCustomTabs.contains(defaultViewHandlerPackageName)) {
                // Prefer default
                return defaultViewHandlerPackageName;
            } else {
                // Default to first in the list
                return packagesSupportingCustomTabs.get(0);
            }
        }

        return null;
    }

}
