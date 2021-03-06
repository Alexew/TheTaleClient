package com.wrewolf.thetaleclient.api.request;

import android.os.Build;

import com.wrewolf.thetaleclient.BuildConfig;
import com.wrewolf.thetaleclient.R;
import com.wrewolf.thetaleclient.TheTaleClientApplication;
import com.wrewolf.thetaleclient.api.AbstractApiRequest;
import com.wrewolf.thetaleclient.api.ApiResponseCallback;
import com.wrewolf.thetaleclient.api.HttpMethod;
import com.wrewolf.thetaleclient.api.response.ThirdPartyAuthResponse;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hamster
 * @since 29.10.2014
 */
public class ThirdPartyAuthRequest extends AbstractApiRequest<ThirdPartyAuthResponse> {

    public ThirdPartyAuthRequest() {
        super(HttpMethod.POST, "accounts/third-party/tokens/api/request-authorisation", "1.0", true);
    }

    public void execute(final ApiResponseCallback<ThirdPartyAuthResponse> callback) {
        final Map<String, String> postParams = new HashMap<>(3);
        postParams.put("application_name", TheTaleClientApplication.getContext().getString(R.string.app_name));
        postParams.put("application_info", String.format("%s %s, %s %s (%d)", Build.BRAND, Build.MODEL,
                TheTaleClientApplication.getContext().getPackageName(), BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE));
        postParams.put("application_description", TheTaleClientApplication.getContext().getString(R.string.app_description));

        execute(null, postParams, callback);
    }

    @Override
    protected ThirdPartyAuthResponse getResponse(final String response) throws JSONException {
        return new ThirdPartyAuthResponse(response);
    }

    @Override
    protected long getStaleTime() {
        return 0;
    }

}
