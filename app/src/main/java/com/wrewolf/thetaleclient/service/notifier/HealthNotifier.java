package com.wrewolf.thetaleclient.service.notifier;

import android.app.PendingIntent;
import android.content.Context;

import com.wrewolf.thetaleclient.R;
import com.wrewolf.thetaleclient.TheTaleClientApplication;
import com.wrewolf.thetaleclient.api.response.GameInfoResponse;
import com.wrewolf.thetaleclient.fragment.GameFragment;
import com.wrewolf.thetaleclient.util.PreferencesManager;
import com.wrewolf.thetaleclient.util.UiUtils;
import com.wrewolf.thetaleclient.util.onscreen.OnscreenPart;

/**
 * @author Hamster
 * @since 08.12.2014
 */
public class HealthNotifier implements Notifier {

    private GameInfoResponse gameInfoResponse;

    @Override
    public void setInfo(GameInfoResponse gameInfoResponse) {
        this.gameInfoResponse = gameInfoResponse;
    }

    @Override
    public boolean isNotifying() {
        if((gameInfoResponse.account.hero.basicInfo.isAlive) && (getValue() < PreferencesManager.getNotificationThresholdHealth())) {
            if(PreferencesManager.shouldNotifyHealth()
                    && PreferencesManager.shouldShowNotificationHealth()
                    && !TheTaleClientApplication.getOnscreenStateWatcher().isOnscreen(OnscreenPart.GAME_INFO)) {
                return true;
            }
            PreferencesManager.setShouldShowNotificationHealth(false);
        } else {
            PreferencesManager.setShouldShowNotificationHealth(true);
        }
        return false;
    }

    @Override
    public String getNotification(Context context) {
        return context.getString(R.string.notification_low_health, getValue());
    }

    private int getValue() {
        return gameInfoResponse.account.hero.basicInfo.healthCurrent;
    }

    @Override
    public PendingIntent getPendingIntent(Context context) {
        return UiUtils.getApplicationIntent(context, GameFragment.GamePage.GAME_INFO, true);
    }

    @Override
    public void onNotificationDelete() {
        PreferencesManager.setShouldShowNotificationHealth(false);
    }

}
