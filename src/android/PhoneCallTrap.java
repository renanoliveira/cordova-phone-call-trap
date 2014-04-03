package io.gvox.plugin.phonecalltrap;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import android.telephony.PhoneStateListener;
import android.widget.Toast;
import android.content.Intent;
import android.os.IBinder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.app.Service;
import android.content.IntentFilter;
import android.telephony.TelephonyManager;

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
		Context ctx = getContext(); 

		TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
		tm.listen(callStateListener, PhoneStateListener.LISTEN_CALL_STATE);

		// Intent intent = new Intent(this, CallDetectService.class);
		// startService(intent);

		// IntentFilter intentFilter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
		// ctx.registerReceiver(outgoingReceiver, intentFilter);

		// callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, "JOCA"));

		return true;
	}
}


class CallStateListener extends PhoneStateListener {

	@Override
	public void onCallStateChanged(int state, String incomingNumber) {
		switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:

			Toast.makeText(ctx, "Incoming: " + incomingNumber, Toast.LENGTH_LONG).show();
			break;
		}
	}
}


// class OutgoingReceiver extends BroadcastReceiver {
// 	public OutgoingReceiver() {}

// 	@Override
// 	public void onReceive(Context context, Intent intent) {
// 		String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);

// 		Toast.makeText(ctx, "Outgoing: "+number, Toast.LENGTH_LONG).show();
// 	}
// }


// class CallDetectService extends Service {
// 	private CallHelper callHelper;

// 	public CallDetectService() {}

// 	@Override
// 	public int onStartCommand(Intent intent, int flags, int startId) {
// 		callHelper = new CallHelper(this);
// 		int res = super.onStartCommand(intent, flags, startId);

// 		callHelper.start();

// 		return res;
// 	}

// 	@Override
// 	public void onDestroy() {
// 		super.onDestroy();
// 		callHelper.stop();
// 	}

// 	@Override
// 	public IBinder onBind(Intent intent) {
// 		// not supporting binding
// 		return null;
// 	}
// }
