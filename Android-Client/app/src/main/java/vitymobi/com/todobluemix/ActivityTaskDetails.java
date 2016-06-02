package vitymobi.com.todobluemix;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import adapters.TaskCommentsAdapter;
import controller.ChannelConstants;
import controller.ProgressCallback;
import controller.PubNubHelper;
import controller.ToDoAppInstance;
import modals.CommentTemplate;
import modals.HandlerResponseMessage;

/**
 * Created by manishautomatic on 22/03/16.
 */
public class ActivityTaskDetails extends AppCompatActivity implements View.OnClickListener {


    private ListView mLstVwTaskComments;
    private TaskCommentsAdapter mAdapter;
    private ArrayList<CommentTemplate> commentsList = new ArrayList<>();
    private Handler mHandler;
   // private ProgressDialog progressDialog;
    private TextView mTxtVwTaskName;
    private TextView mTxtVWCreatedOn;
    private TextView mTxtVwTaskDescription;
    private EditText mEdtxtTaskNewComment;
    private Button mBtnAddNewComment;


    private Button mBtnToggleStatus;

    private Context context;
    private ProgressDialog progressDialog;
    private Gson gson = new Gson();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.setTitle("Task Details");
        setContentView(R.layout.activity_task_details);
        int code=this.hashCode();
        context = ActivityTaskDetails.this;
        initUI();
        LocalBroadcastManager.getInstance(this).registerReceiver(getTaskDetails,
                new IntentFilter(ChannelConstants.GET_TASK_DETAILS));
        LocalBroadcastManager.getInstance(this).registerReceiver(addTaskComment,
                new IntentFilter(ChannelConstants.ADD_COMMENT));
        LocalBroadcastManager.getInstance(this).registerReceiver(toggleTaskStatus,
                new IntentFilter(ChannelConstants.UPDATE_TASK_STATUS));
        prepareProgressDialog();
        prepareCommentsList();


    }

    @Override
    public void onResume() {
        super.onResume();


    }

    private void prepareProgressDialog(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading please wait...");
    }



    private void processToggleStatusCallback(HandlerResponseMessage responseMessage) {

        if(responseMessage.getResponseCode()== ChannelConstants.HANDLER_CODE_INIT){
            progressDialog.show();
        } if(responseMessage.getResponseCode()== ChannelConstants.HANDLER_CODE_SUCCESS){
            progressDialog.dismiss();
            mEdtxtTaskNewComment.setText("");
            if(ToDoAppInstance.getInstance().getCURRENT_SELECTED_TASK_DETAILS().getTASK_STATUS().equalsIgnoreCase("0")){
                ToDoAppInstance.getInstance().getCURRENT_SELECTED_TASK_DETAILS().setTASK_STATUS("1");
                mBtnToggleStatus.setTextColor(Color.RED);
                mBtnToggleStatus.setText("STATUS : CLOSED");
            }else{
                ToDoAppInstance.getInstance().getCURRENT_SELECTED_TASK_DETAILS().setTASK_STATUS("0");
                mBtnToggleStatus.setTextColor(Color.rgb(0,100,0));
                mBtnToggleStatus.setText("STATUS : OPEN");
            }




            Toast.makeText(ActivityTaskDetails.this,"Status changed successfully",Toast.LENGTH_LONG).show();
            updateListView();
        } if(responseMessage.getResponseCode()== ChannelConstants.HANDLER_CODE_ERROR){
            progressDialog.dismiss();
            ToDoAppInstance.getInstance().showAlertWithMessage(responseMessage.getResponseMessage(), ActivityTaskDetails.this);
        } if(responseMessage.getResponseCode()== ChannelConstants.HANDLER_CODE_FAILURE){
            progressDialog.dismiss();
            ToDoAppInstance.getInstance().showAlertWithMessage(responseMessage.getResponseMessage(), ActivityTaskDetails.this);
        }if(responseMessage.getResponseCode()== ChannelConstants.HANDLER_CODE_SESSION_EXPIRED){
            progressDialog.dismiss();
            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityTaskDetails.this);
            builder.setMessage("Session Expired, please login again")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(ActivityTaskDetails.this, ActivityLogin.class);
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


    private void populateTaskDetails(){
        mTxtVwTaskName.setText(ToDoAppInstance.getInstance()
                .getCURRENT_SELECTED_TASK_DETAILS().getTASK_NAME());
        mTxtVWCreatedOn.setText("Created on: "+ToDoAppInstance.getInstance()
                .getCURRENT_SELECTED_TASK_DETAILS().getTASK_CREATION_DATE());
        mTxtVwTaskDescription.setText(ToDoAppInstance.getInstance()
                .getCURRENT_SELECTED_TASK_DETAILS().getTASK_DESCRIPTION());
    }

    private void updateListView(){
        mAdapter = new TaskCommentsAdapter(this,
                ToDoAppInstance.getInstance().getCURRENT_SELECTED_TASK_DETAILS().getComments());
        mLstVwTaskComments.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View view) {
        if(view==mBtnAddNewComment){
            if(validateInputs()){
                new PubNubHelper(ActivityTaskDetails.this).addNewComment(
                        ToDoAppInstance.getInstance().getCURRENT_SELECTED_TASK_ID(),
                        mEdtxtTaskNewComment.getText().toString()

                );
                progressDialog.show();
            }

        }if(view==mBtnToggleStatus){
            String newStatus="";
            if(ToDoAppInstance.getInstance().getCURRENT_SELECTED_TASK_DETAILS().getTASK_STATUS().equalsIgnoreCase("0")){
                newStatus="1";
            }else{
                newStatus="0";
            }



            new PubNubHelper(ActivityTaskDetails.this)
                    .updateTaskStatus(
                            ToDoAppInstance.getInstance().getCURRENT_SELECTED_TASK_ID(),
                            newStatus
                    );
            progressDialog.show();
        }
    }

    private boolean validateInputs(){
        if(mEdtxtTaskNewComment.getText().toString().trim().equalsIgnoreCase("")){
            ToDoAppInstance.getInstance().showAlertWithMessage("Please enter comment", ActivityTaskDetails.this);
            return false;
        }
            return  true;
    }

    private void initUI(){
        mLstVwTaskComments=(ListView)findViewById(R.id.lstVwTaskComments);
        mTxtVwTaskName=(TextView)findViewById(R.id.txtvwTaskTitle);
        mTxtVWCreatedOn=(TextView)findViewById(R.id.txtvwTaskCreatedOn);
        mTxtVwTaskDescription=(TextView)findViewById(R.id.txtvwTaskDescription);
        mBtnAddNewComment=(Button)findViewById(R.id.btnAddComment);
        mEdtxtTaskNewComment=(EditText)findViewById(R.id.edtxtTaskComment);
        mBtnAddNewComment.setOnClickListener(this);
        mBtnToggleStatus=(Button)findViewById(R.id.btnToggleStatus);
        mBtnToggleStatus.setOnClickListener(this);


    }

    private void prepareCommentsList(){

        progressDialog.show();
      //  new PubNubHelper(this).getTaskDetails(ToDoAppInstance.getInstance().getCURRENT_SELECTED_TASK_ID(),
        //        "1", mHandler);
        new PubNubHelper(context).getTaskDetails(context, ToDoAppInstance.getInstance().getCURRENT_SELECTED_TASK_ID(),
                "1");
    }


    @Override
    public void onBackPressed(){

       // mHandler.removeCallbacksAndMessages(null);
        //context=null;
        LocalBroadcastManager.getInstance(this).unregisterReceiver(getTaskDetails);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(addTaskComment);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(toggleTaskStatus);
        finish();
    }






    private BroadcastReceiver getTaskDetails = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String dispatch = intent.getStringExtra("message");
            HandlerResponseMessage message = new HandlerResponseMessage();
            message = gson.fromJson(dispatch,HandlerResponseMessage.class);
            processFetchDetailsCallback(message);
        }
    };
    private BroadcastReceiver addTaskComment = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String dispatch = intent.getStringExtra("message");
            HandlerResponseMessage message = new HandlerResponseMessage();
            message = gson.fromJson(dispatch,HandlerResponseMessage.class);
            processAddCommentCallback(message);
        }
    };
    private BroadcastReceiver toggleTaskStatus = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String dispatch = intent.getStringExtra("message");
            HandlerResponseMessage message = new HandlerResponseMessage();
            message = gson.fromJson(dispatch,HandlerResponseMessage.class);
            processToggleStatusCallback(message);
        }
    };


    private void processFetchDetailsCallback(HandlerResponseMessage responseMessage){
            if(responseMessage.getResponseCode()== ChannelConstants.HANDLER_CODE_INIT){
                progressDialog.show();
            } if(responseMessage.getResponseCode()== ChannelConstants.HANDLER_CODE_SUCCESS){
            progressDialog.dismiss();
            populateTaskDetails();
             updateListView();
            if(ToDoAppInstance.getInstance().getCURRENT_SELECTED_TASK_DETAILS().getTASK_STATUS().equalsIgnoreCase("0")){
                mBtnToggleStatus.setTextColor(Color.rgb(0,100,0));
                mBtnToggleStatus.setText("STATUS : OPEN");
            }else{
                mBtnToggleStatus.setTextColor(Color.RED);
                mBtnToggleStatus.setText("STATUS : CLOSE");
            }
            if(!ToDoAppInstance.getInstance().getCURRENT_SELECTED_TASK_DETAILS().getDISPLAY_NAME().equalsIgnoreCase(
                    ToDoAppInstance.getInstance().getPrefValue(ChannelConstants.PREF_KEY_USER_DISPLAY_NAME)
            )){
                mBtnToggleStatus.setEnabled(false);
            }
        } if(responseMessage.getResponseCode()== ChannelConstants.HANDLER_CODE_ERROR){
            progressDialog.dismiss();
            ToDoAppInstance.getInstance().showAlertWithMessage(responseMessage.getResponseMessage(), ActivityTaskDetails.this);
        } if(responseMessage.getResponseCode()== ChannelConstants.HANDLER_CODE_FAILURE){
            progressDialog.dismiss();
            ToDoAppInstance.getInstance().showAlertWithMessage(responseMessage.getResponseMessage(), ActivityTaskDetails.this);
        }if(responseMessage.getResponseCode()== ChannelConstants.HANDLER_CODE_SESSION_EXPIRED){
            progressDialog.dismiss();
            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityTaskDetails.this);
            builder.setMessage("Session Expired, please login again")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(ActivityTaskDetails.this, ActivityLogin.class);
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


    private void processAddCommentCallback(HandlerResponseMessage responseMessage){

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }


            if(responseMessage.getResponseCode()== ChannelConstants.HANDLER_CODE_INIT){
                progressDialog.show();
            } if(responseMessage.getResponseCode()== ChannelConstants.HANDLER_CODE_SUCCESS){
            progressDialog.dismiss();
            mEdtxtTaskNewComment.setText("");
            Toast.makeText(ActivityTaskDetails.this,"Comment added successfully",Toast.LENGTH_LONG).show();
            updateListView();
        } if(responseMessage.getResponseCode()== ChannelConstants.HANDLER_CODE_ERROR){
            progressDialog.dismiss();
            ToDoAppInstance.getInstance().showAlertWithMessage(responseMessage.getResponseMessage(), ActivityTaskDetails.this);
        } if(responseMessage.getResponseCode()== ChannelConstants.HANDLER_CODE_FAILURE){
            progressDialog.dismiss();
            ToDoAppInstance.getInstance().showAlertWithMessage(responseMessage.getResponseMessage(), ActivityTaskDetails.this);
        }if(responseMessage.getResponseCode()== ChannelConstants.HANDLER_CODE_SESSION_EXPIRED){
            progressDialog.dismiss();
            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityTaskDetails.this);
            builder.setMessage("Session Expired, please login again")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(ActivityTaskDetails.this, ActivityLogin.class);
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




}
