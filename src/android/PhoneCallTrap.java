package io.gvox.phonecalltrap;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import java.util.ArrayList;


public class PhoneCallTrap extends CordovaPlugin {

    CallStateListener listener;

    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        prepareListener();

        listener.setCallbackContext(callbackContext);

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

class CallStateListener extends PhoneStateListener {
    private String lastCalledNumber;
    private CallbackContext callbackContext;

    public void setCallbackContext(CallbackContext callbackContext) {
        this.callbackContext = callbackContext;
    }

    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);
        this.lastCalledNumber = incomingNumber;
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

        ArrayList<JSONObject> res = new ArrayList<JSONObject>();

        
        JSONObject json = new JSONObject();
        try{
            json.put( "msg", msg );
            json.put( "number", incomingNumber.toString() );
            res.add( json );
        } catch(Exception e){
            // return false;            // Always must return something
        }

            // callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, ar));

        PluginResult result = new PluginResult(PluginResult.Status.OK, json);
        result.setKeepCallback(true);

        callbackContext.sendPluginResult(result);
    }

    public void getLastCalledNumber() {        
        if (callbackContext == null) return;

        PluginResult result = new PluginResult(PluginResult.Status.OK, this.lastCalledNumber );
        result.setKeepCallback(true);

        callbackContext.sendPluginResult(result);
    }
}
