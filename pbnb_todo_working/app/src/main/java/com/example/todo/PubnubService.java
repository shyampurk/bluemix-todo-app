package com.example.todo;

import org.bouncycastle.crypto.tls.AlertDescription;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubException;

public class PubnubService extends Service {

    public static final String MY_ACTION = "pubnub.android.todo.MESSAGE";

	String channel = "todochannel";
    
    Pubnub pubnub = Globalvars.pubnub ; 
    
    PowerManager.WakeLock wl = null;
   // MessageHandler mMessageHandler = new MessageHandler();
    //MessageReceiver mMessageReceiver = new MessageReceiver();
    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            String pnMsg = msg.obj.toString();

            final Toast toast = Toast.makeText(getApplicationContext(), pnMsg, Toast.LENGTH_SHORT);
            toast.show();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toast.cancel();
                }
            }, 200);
        }
    };
  /*  private void broadcastMessage(JSONObject message)//this method sends broadcast messages
    {
       
    }
    class MessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            try {
                String m = msg.getData().getString("message");
                JSONObject message = (JSONObject) new JSONTokener(m).nextValue();
                broadcastMessage(message);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    };*/
    private void notifyUser(Object message) {
        Message msg = handler.obtainMessage();
        try {
            final String obj = (String) message;
            msg.obj = obj;
            handler.sendMessage(msg);
            Log.i("Received msg : ", obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "PubnubService created...", Toast.LENGTH_LONG).show();
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "SubscribeAtBoot");
        if (wl != null) {
            wl.acquire();
            Log.i("PUBNUB", "Partial Wake Lock : " + wl.isHeld());
            Toast.makeText(this, "Partial Wake Lock : " + wl.isHeld(), Toast.LENGTH_LONG).show();
        }

        Log.i("PUBNUB", "PubnubService created...");
        try {
            	pubnub.subscribe(channel, new Callback() {
    	
            	@Override
            	public void connectCallback(String channel, Object message) {
            		 notifyUser("CONNECT on channel:" + channel);
                     Log.i("Subscribe","CONNECT");
               		 super.connectCallback(channel, message);
            	}
            	
            	@Override
            	public void disconnectCallback(String channel, Object message) {
            	    Log.i("Subscribe","DISCONNECT");
            		super.disconnectCallback(channel, message);
            	}
            	
            	@Override
            	public void reconnectCallback(String channel, Object message) {
            		Log.i("Subscribe","RECONNECT");
            		super.reconnectCallback(channel, message);
            	}
            	@Override
            	public void successCallback(String channel, Object message) {
            		// TODO Auto-generated method stub
            		super.successCallback(channel, message);
            		Log.i("Subscribe","SUCCESS"+message.getClass()+"    "+message.toString());
                    try {
                    	Globalvars.isSubscribed = true;
                        Log.i("Subscribe","SUCCESS sending message"+message.toString());
                       
                        Intent intent = new Intent(MY_ACTION);
                        intent.putExtra("message", message.toString());
                        Log.i("PUBNUB", "Sending broadcast, message :"+message.toString());
                        sendBroadcast(intent);
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            	}
            	
                @Override
                public void errorCallback(String channel, Object message) {
            	    Globalvars.isSubscribed=false;
                    notifyUser(channel + " " + message.toString());
                    Log.i("Subscribe","Error on CONNECT");
                    exitfromapp();
                }
            });
        } catch (PubnubException e) {
        	Globalvars.isSubscribed=false;
        	 Log.i("Subscribeexception",e.toString());
        	 exitfromapp();
        }
    }

    protected void exitfromapp() {
    
        System.exit(0);
	}


	@Override
    public void onDestroy() {
        super.onDestroy();
        if (wl != null) {
            wl.release();
            Log.i("PUBNUB", "Partial Wake Lock : " + wl.isHeld());
            Toast.makeText(this, "Partial Wake Lock : " + wl.isHeld(), Toast.LENGTH_LONG).show();
            wl = null;
        }
        Toast.makeText(this, "PubnubService destroyed...", Toast.LENGTH_LONG).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

}