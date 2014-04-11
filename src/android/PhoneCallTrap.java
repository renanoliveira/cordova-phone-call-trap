package io.gvox.phonecalltrap;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.app.IntentService;
import android.util.Log;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import org.json.JSONException;
import org.json.JSONArray;


public class PhoneCallTrap extends CordovaPlugin {

    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        Activity activity = cordova.getActivity();

        TelephonyManager TelephonyMgr = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        TelephonyMgr.listen(new CallStateListener(activity, callbackContext), PhoneStateListener.LISTEN_CALL_STATE);

        return true;
    }
}

class CallStateListener extends PhoneStateListener {

    private CallbackContext callbackContext;
    private Activity context;

    CallStateListener(Activity context, CallbackContext callbackContext) {
    	this.context = context;
        this.callbackContext = callbackContext;
    }

    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);

        String msg = "";

        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
                msg = "IDLE";
                break;

            case TelephonyManager.CALL_STATE_OFFHOOK:
                msg = "OFFHOOK";
                break;

            case TelephonyManager.CALL_STATE_RINGING:
                msg = "RINGING";
                break;
        }

        PluginResult result = new PluginResult(PluginResult.Status.OK, msg);
        result.setKeepCallback(true);
        callbackContext.sendPluginResult(result);
    }
}
