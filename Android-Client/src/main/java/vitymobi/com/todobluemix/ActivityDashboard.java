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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import controller.ChannelConstants;
import controller.PubNubHelper;
import controller.ToDoAppInstance;
import modals.HandlerResponseMessage;

/**
 * Created by manishautomatic on 22/03/16.
 */
public class ActivityDashboard extends AppCompatActivity implements View.OnClickListener {


private Button mBtnTasks, mBtnAddressBook, mBtnLogout;
    private TextView mTxtVwWelcomeMsg;
    private Handler mHandler;
    private ProgressDialog progressDialog;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        initUI();
        this.setTitle("Dashboard");
        initHandler();
    }


    private void initUI(){
        mBtnAddressBook=(Button)findViewById(R.id.btnContacts);
        mBtnTasks=(Button)findViewById(R.id.btnTasks);
        mBtnTasks.setOnClickListener(this);
        mBtnAddressBook.setOnClickListener(this);
        mTxtVwWelcomeMsg=(TextView)findViewById(R.id.txtvwWelcomeMessage);
        mTxtVwWelcomeMsg.setText("Welcome " + ToDoAppInstance.getInstance()
                .getPrefValue(ChannelConstants.PREF_KEY_USER_DISPLAY_NAME));
        mBtnLogout=(Button)findViewById(R.id.btnLogout);
        mBtnLogout.setOnClickListener(this);
        progressDialog =new ProgressDialog(ActivityDashboard.this);
        progressDialog.setTitle("ToDo App");
        progressDialog.setCancelable(false);
    }


    private void initHandler(){
        mHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message message) {

                HandlerResponseMessage responseMessage = (HandlerResponseMessage) message.obj;

                if(responseMessage.getResponseCode()== ChannelConstants.HANDLER_CODE_INIT){
                    progressDialog.show();
                } if(responseMessage.getResponseCode()== ChannelConstants.HANDLER_CODE_SUCCESS){
                    progressDialog.dismiss();

                    ToDoAppInstance.getInstance().saveInPrefs(ChannelConstants.PREF_KEY_USER_ID, "");
                    ToDoAppInstance.getInstance().saveInPrefs(ChannelConstants.PREF_KEY_USER_EMAIL, "");
                    ToDoAppInstance.getInstance().saveInPrefs(ChannelConstants.PREF_KEY_USER_DISPLAY_NAME, "");
                    startActivity(new Intent(ActivityDashboard.this, ActivityLogin.class));
                    finish();
                   // updateListView();
                    // ToDoAppInstance.getInstance().showAlertWithMessage((String) message.obj, ActivityLogin.this);

                } if(responseMessage.getResponseCode()== ChannelConstants.HANDLER_CODE_ERROR){
                    progressDialog.dismiss();
                    ToDoAppInstance.getInstance().showAlertWithMessage(responseMessage.getResponseMessage(), ActivityDashboard.this);
                } if(responseMessage.getResponseCode()== ChannelConstants.HANDLER_CODE_FAILURE){
                    progressDialog.dismiss();
                    ToDoAppInstance.getInstance().showAlertWithMessage((String)responseMessage.getResponseMessage(), ActivityDashboard.this);
                }


            }
        };
    }

    @Override
    public void onClick(View view) {
        if(view==mBtnAddressBook){
           // open addressbook
            startActivity(new Intent(ActivityDashboard.this,ActivityAddressBook.class));

        }if(view==mBtnTasks){
            //open task screen
            startActivity(new Intent(ActivityDashboard.this,ActivityTaskList.class));
        }if(view==mBtnLogout){
            showConfirmationDialog();
        }
    }


    private void showConfirmationDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        progressDialog.setMessage("Logging out...");
                        progressDialog.show();
                       new PubNubHelper(ActivityDashboard.this).performLogout(mHandler);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
