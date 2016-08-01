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

import android.net.Uri;
import android.util.Base64;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import java.io.ByteArrayOutputStream;
import android.util.Log;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;


public class PhoneCallTrap extends CordovaPlugin {

    CallStateListener listener;

    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        prepareListener();

        listener.setCallbackContext(callbackContext);
        listener.setContext( this.cordova.getActivity() );

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
    private Context given;

    public void setCallbackContext(CallbackContext callbackContext) {
        this.callbackContext = callbackContext;
    }

    public void setContext(Context context) {
        this.given = context;
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
                    Log.d( "ANGER CALL1!!!!!!!!!!!!!!!!!", "PRE" + incomingNumber + "PRE" );
        if( incomingNumber != null && !incomingNumber.isEmpty()){
             if( incomingNumber != Uri.encode(incomingNumber)){
                    incomingNumber = Uri.encode(incomingNumber);
                }

            Log.d( "ANGER CALL2!!!!!!!!!!!!!!!!!", incomingNumber );

            int phoneContactID = new Random().nextInt();
            Cursor contactLookupCursor = this.given.getContentResolver().query(
                Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, incomingNumber), 
                new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID}, 
                null, null, null);
            while (contactLookupCursor.moveToNext()) {
                phoneContactID = contactLookupCursor.getInt(contactLookupCursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID));
            }
            contactLookupCursor.close();
            
            ContentResolver cr = this.given.getContentResolver();
            Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.valueOf(phoneContactID));
            InputStream photo_stream = ContactsContract.Contacts.openContactPhotoInputStream(cr, contactUri, true);

            final Bitmap bmp = BitmapFactory.decodeStream(photo_stream);

            if( bmp != null ){
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();

                String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);           

                try {        
                    json.put("image", encoded);
                } catch (Exception e) {

                }
            } else {
                try {        
                    json.put("image", null);
                } catch (Exception e) {

                }
            }                    
        }        
        
        try{
            json.put( "msg", msg );
            json.put( "number", incomingNumber.toString() );
            res.add( json );
        } catch(Exception e){
            // return false;            // Always must return something
        }

            // callbackcallbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, ar));

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
