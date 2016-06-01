package vitymobi.com.todobluemix;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;

import adapters.TaskListAdapter;
import controller.ChannelConstants;
import controller.PubNubHelper;
import controller.ToDoAppInstance;
import modals.HandlerResponseMessage;
import modals.TaskListing;
import modals.TaskTemplate;

/**
 * Created by manishautomatic on 22/03/16.
 */
public class ActivityTaskList extends AppCompatActivity implements View.OnClickListener {


    private Button mBtnAddNewTask;
    private ListView mLstVwTaskListing;
    private TaskListAdapter mAdapter ;
    TaskListing tListing = new TaskListing();
    private ProgressDialog progressDialog;
    private Gson gson = new Gson();


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasklist);
        this.setTitle("Task List");

    }


    @Override
    public void onResume(){
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(getTaskList,
                new IntentFilter(ChannelConstants.GET_TASK_LIST));
        initUI();
        initProgressDialog();

        prepareList();
    }


    private void initProgressDialog(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
    }



    private void initUI(){

        mBtnAddNewTask=(Button)findViewById(R.id.btnAddNewTask);
        mBtnAddNewTask.setOnClickListener(this);
        mLstVwTaskListing=(ListView)findViewById(R.id.lstvwTaskList);


    }


    private void prepareList(){

        progressDialog.show();

        new PubNubHelper(ActivityTaskList.this).getTaskList();
    }



    @Override
    public void onClick(View view) {
        if(view==mBtnAddNewTask){
            // open add-new-task-activity
            startActivity(new Intent(ActivityTaskList.this,ActivityAddNewTask.class));
        }
    }



    private void handleResponse(HandlerResponseMessage responseMessage) {


                if(responseMessage.getResponseCode()== ChannelConstants.HANDLER_CODE_INIT){
                    progressDialog.show();
                } if(responseMessage.getResponseCode()== ChannelConstants.HANDLER_CODE_SUCCESS){
                    progressDialog.dismiss();

                    updateListView();
                    // ToDoAppInstance.getInstance().showAlertWithMessage(responseMessage.getResponseMessage(), ActivityLogin.this);
                    // startActivity(new Intent(ActivityLogin.this,ActivityDashboard.class));
                } if(responseMessage.getResponseCode()== ChannelConstants.HANDLER_CODE_ERROR){
                    progressDialog.dismiss();
                    ToDoAppInstance.getInstance().showAlertWithMessage(responseMessage.getResponseMessage(), ActivityTaskList.this);
                } if(responseMessage.getResponseCode()== ChannelConstants.HANDLER_CODE_FAILURE){
                    progressDialog.dismiss();
                    ToDoAppInstance.getInstance().showAlertWithMessage(responseMessage.getResponseMessage(), ActivityTaskList.this);
                }if(responseMessage.getResponseCode()== ChannelConstants.HANDLER_CODE_SESSION_EXPIRED){
                    progressDialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityTaskList.this);
                    builder.setMessage("Session Expired, please login again")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent =new Intent(ActivityTaskList.this, ActivityLogin.class);
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






    private void updateListView(){
     TaskListAdapter   mAdapter =  new TaskListAdapter(ActivityTaskList.this, ToDoAppInstance.getInstance().getCurrentTasks());
        mLstVwTaskListing.setAdapter(mAdapter);

        mLstVwTaskListing.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ToDoAppInstance.getInstance().setCURRENT_SELECTED_TASK_ID(
                        ToDoAppInstance.getInstance().getCurrentTasks()[i].getTASK_ID()
                );
                startActivity(new Intent(ActivityTaskList.this, ActivityTaskDetails.class));
            }
        });
    }


    private BroadcastReceiver getTaskList = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String dispatch = intent.getStringExtra("message");
            HandlerResponseMessage message = new HandlerResponseMessage();
            message = gson.fromJson(dispatch,HandlerResponseMessage.class);
            handleResponse(message);
        }
    };
}
