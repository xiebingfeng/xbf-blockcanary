/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.moduth.blockcanary;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.github.moduth.blockcanary.internal.BlockInfo;
import com.github.moduth.blockcanary.ui.DisplayActivity;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static android.os.Build.VERSION_CODES.HONEYCOMB;

final class DisplayService implements BlockInterceptor {

    private static final String TAG = "DisplayService";

    @Override
    public void onBlock(Context context, BlockInfo blockInfo) {
        Intent intent = new Intent(context, DisplayActivity.class);
        intent.putExtra("show_latest", blockInfo.timeStart);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, FLAG_UPDATE_CURRENT);
        String contentTitle = context.getString(R.string.block_canary_class_has_blocked, blockInfo.timeStart);
        String contentText = context.getString(R.string.block_canary_notification_message);
        show(context, contentTitle, contentText, pendingIntent);
    }

    @TargetApi(HONEYCOMB)
    private void show(Context context, String contentTitle, String contentText, PendingIntent pendingIntent) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        String id = "my_channel_01";
        String name = "我是渠道名字";
        int smallIconId = R.drawable.block_canary_notification;

        Notification notification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW);

            Log.i(TAG, mChannel.toString());
            notificationManager.createNotificationChannel(mChannel);
            notification = new Notification.Builder(context, id)
                    .setSmallIcon(smallIconId)
                    .setAutoCancel(true)
                    .setContentText(contentText)
                    .setWhen(System.currentTimeMillis())
                    .setContentIntent(pendingIntent)
                    .setContentTitle(contentTitle)
                    .setSmallIcon(R.drawable.block_canary_icon).build();

            notification.defaults |= Notification.DEFAULT_SOUND;
        } else {
            notification = new NotificationCompat.Builder(context, id)
                    .setSmallIcon(smallIconId)
                    .setContentTitle(contentTitle)
                    .setContentText(contentText)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setSmallIcon(R.drawable.block_canary_icon)
                    .setContentIntent(pendingIntent)
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true).build();
        }
        notificationManager.notify(111123, notification);
    }
}
