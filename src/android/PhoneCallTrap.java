package io.gvox.phonecalltrap;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import org.json.JSONException;
import org.json.JSONArray;


public class PhoneCallTrap extends CordovaPlugin {

    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        Activity activity = cordova.getActivity();

        TelephonyManager TelephonyMgr = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        TelephonyMgr.listen(new CallStateListener(callbackContext), PhoneStateListener.LISTEN_CALL_STATE);

        return true;
    }
}


class CallStateListener extends PhoneStateListener {

    private CallbackContext callbackContext;

    CallStateListener(CallbackContext callbackContext) {
        this.callbackContext = callbackContext;
    }

    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);
        callbackContext.success("" + state);

        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
                callbackContext.success("IDLE");
                break;

            case TelephonyManager.CALL_STATE_OFFHOOK:
                callbackContext.success("OFFHOOK");
                break;

            case TelephonyManager.CALL_STATE_RINGING:
                callbackContext.success("RINGING");
                break;
        }
    }
}
