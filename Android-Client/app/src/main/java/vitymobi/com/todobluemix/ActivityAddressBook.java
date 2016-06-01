package vitymobi.com.todobluemix;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import adapters.AddressBookAdapter;
import controller.ChannelConstants;
import controller.PubNubHelper;
import controller.ToDoAppInstance;
import modals.ContactTemplate;
import modals.HandlerResponseMessage;

/**
 * Created by manishautomatic on 28/03/16.
 */
public class ActivityAddressBook extends AppCompatActivity {

private Gson gson = new Gson();
    private ListView mLstVwAddressBook;
    private AddressBookAdapter mAddressBookAdapter;




    private ProgressDialog progressDialog;
    private ArrayList<ContactTemplate> contactList=new ArrayList<>();



    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_contacts_list);
        LocalBroadcastManager.getInstance(this).registerReceiver(getAddressBook,
                new IntentFilter(ChannelConstants.GET_ADDRESS_BOOK));
        setTitle("Address Book");
        initUI();

        progressDialog.show();
        new PubNubHelper(this).getAddressBook();
    }



    private void processResponse(HandlerResponseMessage responseMessage){

                if(responseMessage.getResponseCode()== ChannelConstants.HANDLER_CODE_INIT){
                    progressDialog.show();
                } if(responseMessage.getResponseCode()== ChannelConstants.HANDLER_CODE_SUCCESS){
                    progressDialog.dismiss();
                   populateAddressBook();
                } if(responseMessage.getResponseCode()== ChannelConstants.HANDLER_CODE_ERROR){
                    progressDialog.dismiss();
                    ToDoAppInstance.getInstance().showAlertWithMessage(responseMessage.getResponseMessage(), ActivityAddressBook.this);
                } if(responseMessage.getResponseCode()== ChannelConstants.HANDLER_CODE_FAILURE){
                    progressDialog.dismiss();
                    ToDoAppInstance.getInstance().showAlertWithMessage(responseMessage.getResponseMessage(), ActivityAddressBook.this);
                }if(responseMessage.getResponseCode()== ChannelConstants.HANDLER_CODE_SESSION_EXPIRED){
                    progressDialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityAddressBook.this);
                    builder.setMessage("Session Expired, please login again")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = new Intent(ActivityAddressBook.this, ActivityLogin.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent); //do things
                                    ToDoAppInstance.getInstance().saveInPrefs(ChannelConstants.PREF_KEY_USER_DISPLAY_NAME, "");
                                    finish();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }




    private void initUI(){
            mLstVwAddressBook = (ListView)findViewById(R.id.lstvwAddressBook);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading please wait...");
        progressDialog.setCancelable(false);

    }

    private void populateAddressBook(){

        contactList=ToDoAppInstance.getInstance().getCURRENT_ADDRESS_BOOK();
        mAddressBookAdapter = new AddressBookAdapter(ActivityAddressBook.this,contactList);
            mLstVwAddressBook.setAdapter(mAddressBookAdapter);







    }




    @Override
    public void onBackPressed(){

            finish();
    }


    private BroadcastReceiver getAddressBook = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String dispatch = intent.getStringExtra("message");
            HandlerResponseMessage message = new HandlerResponseMessage();
            message = gson.fromJson(dispatch,HandlerResponseMessage.class);
            processResponse(message);
        }
    };
}
