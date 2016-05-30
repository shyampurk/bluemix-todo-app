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
import android.widget.EditText;
import android.widget.Toast;

import controller.ChannelConstants;
import controller.PubNubHelper;
import controller.ToDoAppInstance;
import modals.HandlerResponseMessage;

/**
 * Created by manishautomatic on 22/03/16.
 */
public class ActivityAddNewTask extends AppCompatActivity implements View.OnClickListener {


    private EditText mEdTxtTaskName;
    private EditText mEdTxtTaskDescription;
    private Button mBtnAddTask;
    private Handler mHandler;
    private ProgressDialog progressDialog;




    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_task);
        this.setTitle("Add New Task");
        initUI();
        initProgressDialog();
        initHandler();
    }

    @Override
    public void onClick(View view) {
            if(validateInputs()){
                new PubNubHelper(this).addNewTask(mEdTxtTaskName.getText().toString(),
                        mEdTxtTaskDescription.getText().toString(),
                        mHandler);
                progressDialog.show();
            }

    }

    private void initUI(){
        mEdTxtTaskName=(EditText)findViewById(R.id.edtxtTaskTitle);
        mEdTxtTaskDescription=(EditText)findViewById(R.id.edtxtTaskDescription);
        mBtnAddTask=(Button)findViewById(R.id.btnSaveTask);
        mBtnAddTask.setOnClickListener(this);

    }



    private boolean validateInputs(){
        if(mEdTxtTaskName.getText().toString().trim().equalsIgnoreCase(""))
            return false;
        if(mEdTxtTaskDescription.getText().toString().trim().equalsIgnoreCase(""))
            return false;

            return true;
    }

    private void initHandler() {

        mHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message message) {

                HandlerResponseMessage responseMessage =(HandlerResponseMessage)message.obj;
                if(responseMessage.getResponseCode()== ChannelConstants.HANDLER_CODE_INIT){
                    progressDialog.show();
                } if(responseMessage.getResponseCode()== ChannelConstants.HANDLER_CODE_SUCCESS){
                    progressDialog.dismiss();
                    Toast.makeText(ActivityAddNewTask.this,responseMessage.getResponseMessage(),Toast.LENGTH_LONG).show();
                    finish();
                   // startActivity(new Intent(ActivityLogin.this,ActivityDashboard.class));
                } if(responseMessage.getResponseCode()== ChannelConstants.HANDLER_CODE_ERROR){
                    progressDialog.dismiss();
                    ToDoAppInstance.getInstance().showAlertWithMessage(responseMessage.getResponseMessage(), ActivityAddNewTask.this);
                } if(responseMessage.getResponseCode()== ChannelConstants.HANDLER_CODE_FAILURE){
                    progressDialog.dismiss();
                    ToDoAppInstance.getInstance().showAlertWithMessage(responseMessage.getResponseMessage(), ActivityAddNewTask.this);
                }if(responseMessage.getResponseCode()== ChannelConstants.HANDLER_CODE_SESSION_EXPIRED){
                    progressDialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityAddNewTask.this);
                    builder.setMessage("Session Expired, please login again")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = new Intent(ActivityAddNewTask.this, ActivityLogin.class);
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


    private void initProgressDialog(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
    }
}
