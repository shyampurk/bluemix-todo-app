package vitymobi.com.todobluemix;

import android.app.ProgressDialog;
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
