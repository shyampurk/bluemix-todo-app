package vitymobi.com.todobluemix;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import adapters.TaskCommentsAdapter;
import controller.ChannelConstants;
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
    private ProgressDialog progressDialog;
    private TextView mTxtVwTaskName;
    private TextView mTxtVWCreatedOn;
    private TextView mTxtVwTaskDescription;
    private EditText mEdtxtTaskNewComment;
    private Button mBtnAddNewComment;
    private Handler mHanderNewComment;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.setTitle("Task Details");
        setContentView(R.layout.activity_task_details);
        initUI();
        prepareProgressDialog();
        initHandler();
        prepareCommentsList();
    }



    private void prepareProgressDialog(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading please wait...");
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
                    populateTaskDetails();
                    updateListView();
                } if(responseMessage.getResponseCode()== ChannelConstants.HANDLER_CODE_ERROR){
                    progressDialog.dismiss();
                    ToDoAppInstance.getInstance().showAlertWithMessage((String) message.obj, ActivityTaskDetails.this);
                } if(responseMessage.getResponseCode()== ChannelConstants.HANDLER_CODE_FAILURE){
                    progressDialog.dismiss();
                    ToDoAppInstance.getInstance().showAlertWithMessage((String) message.obj, ActivityTaskDetails.this);
                }
            }
        };

        mHanderNewComment = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message message) {


                HandlerResponseMessage responseMessage = (HandlerResponseMessage) message.obj;
                if(responseMessage.getResponseCode()== ChannelConstants.HANDLER_CODE_INIT){
                    progressDialog.show();
                } if(responseMessage.getResponseCode()== ChannelConstants.HANDLER_CODE_SUCCESS){
                    progressDialog.dismiss();
                    mEdtxtTaskNewComment.setText("");
                    progressDialog.setMessage("Updating comment list...");
                    progressDialog.show();
                    new PubNubHelper(ActivityTaskDetails.this).getTaskDetails(ToDoAppInstance
                                    .getInstance().getCURRENT_SELECTED_TASK_ID(),
                            "1", mHandler);
                } if(responseMessage.getResponseCode()== ChannelConstants.HANDLER_CODE_ERROR){
                    progressDialog.dismiss();
                    ToDoAppInstance.getInstance().showAlertWithMessage((String) message.obj, ActivityTaskDetails.this);
                } if(responseMessage.getResponseCode()== ChannelConstants.HANDLER_CODE_FAILURE){
                    progressDialog.dismiss();
                    ToDoAppInstance.getInstance().showAlertWithMessage(responseMessage.getResponseMessage(), ActivityTaskDetails.this);
                }
            }
        };
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
                        mEdtxtTaskNewComment.getText().toString(),
                        mHanderNewComment
                );
                progressDialog.show();
            }

        }
    }



    private boolean validateInputs(){
        if(mEdtxtTaskNewComment.getText().toString().trim().equalsIgnoreCase("")){
            ToDoAppInstance.getInstance().showAlertWithMessage("Please enter comment",ActivityTaskDetails.this);
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


    }


    private void prepareCommentsList(){

        progressDialog.show();
        new PubNubHelper(this).getTaskDetails(ToDoAppInstance.getInstance().getCURRENT_SELECTED_TASK_ID(),
                "1",mHandler);
    }
}
