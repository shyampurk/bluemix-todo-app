package vitymobi.com.todobluemix;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

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
    private Handler mHandler;
    private ProgressDialog progressDialog;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasklist);
        this.setTitle("Task List");

    }


    @Override
    public void onResume(){
        super.onResume();
        initUI();
        initProgressDialog();
        initHandler();
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

        new PubNubHelper(ActivityTaskList.this).getTaskList(mHandler);
    }



    @Override
    public void onClick(View view) {
        if(view==mBtnAddNewTask){
            // open add-new-task-activity
            startActivity(new Intent(ActivityTaskList.this,ActivityAddNewTask.class));
        }
    }



    private void initHandler() {

        mHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message message) {

                HandlerResponseMessage responseMessage = (HandlerResponseMessage) message.obj;

                if(responseMessage.getResponseCode()== ChannelConstants.HANDLER_CODE_INIT){
                    progressDialog.show();
                } if(responseMessage.getResponseCode()== ChannelConstants.HANDLER_CODE_SUCCESS){
                    progressDialog.dismiss();

                    updateListView();
                    // ToDoAppInstance.getInstance().showAlertWithMessage((String) message.obj, ActivityLogin.this);
                    // startActivity(new Intent(ActivityLogin.this,ActivityDashboard.class));
                } if(responseMessage.getResponseCode()== ChannelConstants.HANDLER_CODE_ERROR){
                    progressDialog.dismiss();
                    ToDoAppInstance.getInstance().showAlertWithMessage((String) message.obj, ActivityTaskList.this);
                } if(responseMessage.getResponseCode()== ChannelConstants.HANDLER_CODE_FAILURE){
                    progressDialog.dismiss();
                    ToDoAppInstance.getInstance().showAlertWithMessage((String) message.obj, ActivityTaskList.this);
                }


            }
        };
    }




    private void updateListView(){
        mAdapter =  new TaskListAdapter(ActivityTaskList.this, ToDoAppInstance.getInstance().getCurrentTasks());
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
}
