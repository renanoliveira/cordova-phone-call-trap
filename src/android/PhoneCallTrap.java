package io.gvox.phonecalltrap;

import android.Manifest;
import androidx.annotation.RequiresApi;
import android.os.Build;
import android.content.pm.PackageManager;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.telephony.TelephonyCallback;

import org.json.JSONException;
import org.json.JSONArray;


public class PhoneCallTrap extends CordovaPlugin {

    OldCallStateListener oldListener;
    CallStateListener listener;
    CallbackContext callbackContext;
    boolean listenerSet = false;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        prepareListener();
        this.callbackContext = callbackContext;

        if (listenerSet) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                listener.setCallbackContext(callbackContext);
            } else {
                oldListener.setCallbackContext(callbackContext);
            }
        }

        return true;
    }

    @Override
    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) throws JSONException {
        for (int r:grantResults) {
            if(r == PackageManager.PERMISSION_DENIED) {
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, 1));
                return;
            }
        }
        registerListener();
    }

    private void registerListener() {
        TelephonyManager TelephonyMgr = (TelephonyManager) cordova.getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            TelephonyMgr.registerTelephonyCallback(cordova.getThreadPool(), listener);
            if (!listenerSet) {
                listener.setCallbackContext(callbackContext);
            }
            listenerSet = true;
        }
    }

    private void prepareListener() {
        if (!listenerSet) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                listener = new CallStateListener();
                boolean hasPhoneStatePermission = cordova.hasPermission(Manifest.permission.READ_PHONE_STATE);
                if (!hasPhoneStatePermission) {
                    cordova.requestPermissions(this, 0, new String[]{
                        Manifest.permission.READ_PHONE_STATE
                    });
                } else {
                    registerListener();
                }
            } else {
                TelephonyManager TelephonyMgr = (TelephonyManager) cordova.getActivity().getSystemService(Context.TELEPHONY_SERVICE);
                oldListener = new OldCallStateListener();
                TelephonyMgr.listen(oldListener, PhoneStateListener.LISTEN_CALL_STATE);
                listenerSet = true;
            }
        }
    }
}

@RequiresApi(api = Build.VERSION_CODES.S)
class CallStateListener extends TelephonyCallback implements TelephonyCallback.CallStateListener {
    private CallbackContext callbackContext;

    public void setCallbackContext(CallbackContext callbackContext) {
        this.callbackContext = callbackContext;
    }

    @Override
    public void onCallStateChanged(int state) {

        if (callbackContext == null) return;

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

class OldCallStateListener extends PhoneStateListener {

    private CallbackContext callbackContext;

    public void setCallbackContext(CallbackContext callbackContext) {
        this.callbackContext = callbackContext;
    }

    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);

        if (callbackContext == null) return;

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
