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
import android.provider.CallLog;
import java.util.HashMap;
import android.util.Log;
import java.util.Date;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;


public class PhoneCallTrap extends CordovaPlugin {

    CallStateListener listener;

    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        prepareListener();

        if( action.equals("minimise") ){
            this.cordova.getActivity().moveTaskToBack(true);
        } else if( action.equals("onCall") ){
            listener.setCallbackContext(callbackContext);
            listener.setContext( this.cordova.getActivity() );
        }else if( action.equals( "getCallData" ) ){
            listener.setCallbackContext(callbackContext);
            listener.setContext( this.cordova.getActivity() );
            listener.getCallData( args.getString(0), null );
        } else {
            String dateGiven = args.getString(0);
            // getting history
            ArrayList<String> calls = new ArrayList<String>();            
            ArrayList<String> lastCall = new ArrayList<String>();            

            Uri contacts = CallLog.Calls.CONTENT_URI;
            Cursor managedCursor = this.cordova.getActivity().getContentResolver().query(contacts, null, null, null, null);
            int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
            int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
            int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
            int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
            while (managedCursor.moveToNext()) {

                HashMap<String, String> rowDataCall = new HashMap<String, String>();

                String phNumber = managedCursor.getString(number);
                String callType = managedCursor.getString(type);
                String callDate = managedCursor.getString(date);
                String callDayTime = new Date(Long.valueOf(callDate)).toString();
                // long timestamp = convertDateToTimestamp(callDayTime);
                String callDuration = managedCursor.getString(duration);
                String dir = null;
                int dircode = Integer.parseInt(callType);
                
                if( dateGiven == null || new Date(dateGiven).compareTo(new Date(Long.valueOf(callDate))) >= 0 ){
                    calls.add( phNumber );
                }                
                lastCall.clear();
                lastCall.add( phNumber );
                // sb.append("\nPhone Number:--- " + phNumber + " \nCall Type:--- " + dir + " \nCall Date:--- " + callDayTime + " \nCall duration in sec :--- " + callDuration);
                // sb.append("\n----------------------------------");


            }
            managedCursor.close();

            JSONObject json = new JSONObject();
            if( action.equals("getCallHistory" ) ){
                json.put( "calls", calls );
            } else {
                json.put( "call", lastCall );
            }
            PluginResult result = new PluginResult(PluginResult.Status.OK, json);
            result.setKeepCallback(true);
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

    public void getCallData( String incomingNumber, String msg ){
        JSONObject json = new JSONObject();
        if( incomingNumber != Uri.encode(incomingNumber) && !incomingNumber.substring(0, 1).equals("+")){
            incomingNumber = Uri.encode(incomingNumber);
        }

        String contactName = "John Dow";
        int phoneContactID = new Random().nextInt();
        Cursor contactLookupCursor = this.given.getContentResolver().query(
            Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, incomingNumber), 
            new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID}, 
            null, null, null);
        while (contactLookupCursor.moveToNext()) {
            phoneContactID = contactLookupCursor.getInt(contactLookupCursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID));
            contactName = contactLookupCursor.getString(contactLookupCursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
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
        try{
            json.put( "number", incomingNumber.toString() );
        } catch(Exception e){
            // return false;            // Always must return something
        }

        try{
            json.put( "displayName", contactName );
        } catch(Exception e){
            // return false;            // Always must return something
        }

        if( msg != null ){            
            try{
                json.put( "msg", msg );
            } catch(Exception e){
                // return false;            // Always must return something
            }
        }

        PluginResult result = new PluginResult(PluginResult.Status.OK, json);
        result.setKeepCallback(true);

        callbackContext.sendPluginResult(result);
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
        if( incomingNumber != null && !incomingNumber.isEmpty()){
            this.getCallData( incomingNumber, msg );
        }            
    }

    public void getLastCalledNumber() {        
        if (callbackContext == null) return;

        PluginResult result = new PluginResult(PluginResult.Status.OK, this.lastCalledNumber );
        result.setKeepCallback(true);

        callbackContext.sendPluginResult(result);
    }
}
