package io.gvox.plugin.phonecalltrap;

import org.apache.cordova.CordovaPlugin;
import android.util.Log;

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
    }
}