package io.gvox.plugin.phonecalltrap;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;

import android.util.Log;

import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

public class PhoneCallTrap extends CordovaPlugin {
	
	/**
     * Executes the request and returns PluginResult.
     * @param action 		The action to execute.
     * @param args 			JSONArry of arguments for the plugin.
     * @param callbackContext		The callback context used when calling back into JavaScript.
     * @return 				A PluginResult object with a status and message.
     */
	@Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    	Log.d("Bolha", action);
        
        callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, "JOCA"));
        
        return true;
    }
}