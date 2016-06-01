package vitymobi.com.todobluemix;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;


public class MainActivity extends AppCompatActivity {

//this class is scheduled for deletion...
    /**
    private final String PUBLISH_CHANNEL_KEY="pub-c-c772277c-233e-4414-8644-b253fb6522d8";
    private final String SUBSCRIBE_CHANNEL_KEY="sub-c-af53005e-efea-11e5-ab43-02ee2ddab7fe";
    private final String CHANNEL_NAME= "todochannel";
    private Button mBtnPublish;
     Pubnub pubnub = new Pubnub(PUBLISH_CHANNEL_KEY,
            SUBSCRIBE_CHANNEL_KEY);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnPublish=(Button)findViewById(R.id.btnPublish);
        pubnub = new Pubnub(PUBLISH_CHANNEL_KEY,
                SUBSCRIBE_CHANNEL_KEY);
        setupPubNub();
        setupPresence();
        mBtnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Callback callback = new Callback() {
                    public void successCallback(String channel, Object response) {
                        System.out.println(response.toString());
                    }
                    public void errorCallback(String channel, PubnubError error) {
                        System.out.println(error.toString());
                    }
                };
                pubnub.publish(CHANNEL_NAME, "Hello from the PubNub Java SDK!" , callback);


            }
        });
    }



    private void setupPresence(){
        Callback callback = new Callback() {
            @Override
            public void connectCallback(String channel, Object message) {
                System.out.println("CONNECT on channel:" + channel
                        + " : " + message.getClass() + " : "
                        + message.toString());
            }

            @Override
            public void disconnectCallback(String channel, Object message) {
                System.out.println("DISCONNECT on channel:" + channel
                        + " : " + message.getClass() + " : "
                        + message.toString());
            }

            @Override
            public void reconnectCallback(String channel, Object message) {
                System.out.println("RECONNECT on channel:" + channel
                        + " : " + message.getClass() + " : "
                        + message.toString());
            }

            @Override
            public void successCallback(String channel, Object message) {
                System.out.println(channel + " : "
                        + message.getClass() + " : " + message.toString());
            }

            @Override
            public void errorCallback(String channel, PubnubError error) {
                System.out.println("ERROR on channel " + channel
                        + " : " + error.toString());
            }
        };

        try {
            pubnub.presence(CHANNEL_NAME, callback);
        } catch (PubnubException e) {
            System.out.println(e.toString());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    private void setupPubNub(){


        try {
            pubnub.subscribe(CHANNEL_NAME, new Callback() {
                        @Override
                        public void connectCallback(String channel, Object message) {
                            pubnub.publish(CHANNEL_NAME, "Hello from the PubNub Java SDK", new Callback() {});
                        }

                        @Override
                        public void disconnectCallback(String channel, Object message) {
                            System.out.println("SUBSCRIBE : DISCONNECT on channel:" + channel
                                    + " : " + message.getClass() + " : "
                                    + message.toString());
                        }

                        public void reconnectCallback(String channel, Object message) {
                            System.out.println("SUBSCRIBE : RECONNECT on channel:" + channel
                                    + " : " + message.getClass() + " : "
                                    + message.toString());
                        }

                        @Override
                        public void successCallback(String channel, Object message) {
                            System.out.println("SUBSCRIBE : " + channel + " : "
                                    + message.getClass() + " : " + message.toString());
                        }

                        @Override
                        public void errorCallback(String channel, PubnubError error) {
                            System.out.println("SUBSCRIBE : ERROR on channel " + channel
                                    + " : " + error.toString());
                        }
                    }
            );
        } catch (PubnubException e) {
            System.out.println(e.toString());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    **/
}
