package com.wrewolf.thetaleclient.service.notifier;

import android.app.PendingIntent;
import android.content.Context;

import com.wrewolf.thetaleclient.api.response.GameInfoResponse;

/**
 * @author Hamster
 * @since 07.12.2014
 */
public interface Notifier {

    void setInfo(GameInfoResponse gameInfoResponse);

    boolean isNotifying();

    String getNotification(Context context);

    PendingIntent getPendingIntent(Context context);

    void onNotificationDelete();

}
