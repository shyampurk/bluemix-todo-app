package vitymobi.com.todobluemix;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

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


    private ListView mLstVwAddressBook;
    private AddressBookAdapter mAdapter;
    private ArrayList<ContactTemplate> addressBook = new ArrayList<>();
    private Handler mHandler;
    private ProgressDialog progressDialog;



    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_contacts_list);
        setTitle("Address Book");
        initUI();
        initHandler();
        progressDialog.show();
        new PubNubHelper(this).getAddressBook(mHandler);
    }



    private void initHandler(){
        mHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message message) {

                HandlerResponseMessage responseMessage =(HandlerResponseMessage)message.obj;
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
        };
    }


    private void initUI(){
        mLstVwAddressBook=(ListView)findViewById(R.id.lstVwAddressBook);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading please wait...");
        progressDialog.setCancelable(false);

    }

    private void populateAddressBook(){

        mAdapter=new AddressBookAdapter(ActivityAddressBook.this,ToDoAppInstance.getInstance().getCURRENT_ADDRESS_BOOK());
        mLstVwAddressBook.setAdapter(mAdapter);
    }




    @Override
    public void onBackPressed(){
        finish();
    }
}
