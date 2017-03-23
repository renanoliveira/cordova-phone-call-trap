package io.gvox.phonecalltrap;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import org.json.JSONException;
import org.json.JSONArray;


public class PhoneCallTrap extends CordovaPlugin {

    CallStateListener listener;

    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (callbackContext == null) return true;

        if ("onCall".equals(action)) {
            prepareListener();
            listener.setCallbackContext(callbackContext);
        } else if ("getCurrentState".equals(action)) {
            TelephonyManager manager = (TelephonyManager) cordova.getActivity().getSystemService(Context.TELEPHONY_SERVICE);
            String msg = Helper.getState(manager.getCallState());
            PluginResult result = new PluginResult(PluginResult.Status.OK, msg);
            //result.setKeepCallback(true);
            callbackContext.sendPluginResult(result);
        }

        return true;
    }

    private void prepareListener() {
        if (listener == null) {
            listener = new CallStateListener();
            TelephonyManager TelephonyMgr = (TelephonyManager) cordova.getActivity().getSystemService(Context.TELEPHONY_SERVICE);
            TelephonyMgr.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }
}

class Helper {
    public static String getState(int state) {
        switch (state) {
            case TelephonyManager.CALL_STATE_OFFHOOK:
                return "OFFHOOK";
            case TelephonyManager.CALL_STATE_RINGING:
                return "RINGING";
            default:
                return "IDLE";
        }
    }
}

class CallStateListener extends PhoneStateListener {

    private CallbackContext callbackContext;

    public void setCallbackContext(CallbackContext callbackContext) {
        this.callbackContext = callbackContext;
    }

    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);

        if (callbackContext == null) return;

        String msg = Helper.getState(state);

        PluginResult result = new PluginResult(PluginResult.Status.OK, msg);
        result.setKeepCallback(true);

        callbackContext.sendPluginResult(result);
    }
}
