package controller;


import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;


import com.pubnub.api.Pubnub;

import java.util.ArrayList;

import modals.CommentTemplate;
import modals.ContactTemplate;
import modals.TaskDetailsTemplate;
import modals.TaskSummaryTemplate;

public class ToDoAppInstance extends Application {


    private final String PUBNUB_SUBSCRIBE_KEY="sub-c-8ad89b4e-a95e-11e5-a65d-02ee2ddab7fe";
    private final String PUBNUB_PUBLISH_KEY="pub-c-913ab39c-d613-44b3-8622-2e56b8f5ea6d";

    private static ToDoAppInstance instance;
    private  Pubnub PUBNUB_INSTANCE=null;
    private SharedPreferences appPrefs;
    private TaskSummaryTemplate[] currentTasks ;
    private String CURRENT_SELECTED_TASK_ID="";
    private TaskDetailsTemplate CURRENT_SELECTED_TASK_DETAILS;
    private ArrayList<ContactTemplate> CURRENT_ADDRESS_BOOK=new ArrayList<>();


    @Override
    public void onCreate(){
        super.onCreate();
       // Mobiprobe.activate(this,"3a4fe437");
        instance= this;
        appPrefs = this.getSharedPreferences("APP_PREFS",MODE_PRIVATE);
        initPubNub();
    }

    public TaskDetailsTemplate getCURRENT_SELECTED_TASK_DETAILS() {
        return CURRENT_SELECTED_TASK_DETAILS;
    }

    public ArrayList<ContactTemplate> getCURRENT_ADDRESS_BOOK() {
        return CURRENT_ADDRESS_BOOK;
    }

    public void setCURRENT_ADDRESS_BOOK(ArrayList<ContactTemplate> CURRENT_ADDRESS_BOOK_) {
        this.CURRENT_ADDRESS_BOOK.clear();
        this.CURRENT_ADDRESS_BOOK.addAll(CURRENT_ADDRESS_BOOK_);
    }

    public void setCURRENT_SELECTED_TASK_DETAILS(TaskDetailsTemplate CURRENT_SELECTED_TASK_DETAILS) {
        this.CURRENT_SELECTED_TASK_DETAILS = CURRENT_SELECTED_TASK_DETAILS;
    }

    // initialize PubNub
    private void initPubNub() {
            PUBNUB_INSTANCE =new Pubnub(this.PUBNUB_PUBLISH_KEY,this.PUBNUB_SUBSCRIBE_KEY);
    }


    public  Pubnub getPubnubInstance(){
        return PUBNUB_INSTANCE;
    }


    public String getPrefValue(String key){
        return  appPrefs.getString(key,"");
    }

    public void saveInPrefs(String key, String value){
        appPrefs.edit().putString(key,value).commit();
    }

    public static ToDoAppInstance getInstance(){
        if(instance==null){
            throw new IllegalStateException("Application not created yet");
        }return instance;
    }


    public void showAlertWithMessage(String message, Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public String getCURRENT_SELECTED_TASK_ID() {
        return CURRENT_SELECTED_TASK_ID;
    }

    public void setCURRENT_SELECTED_TASK_ID(String CURRENT_SELECTED_TASK_ID) {
        this.CURRENT_SELECTED_TASK_ID = CURRENT_SELECTED_TASK_ID;
    }

    public TaskSummaryTemplate[] getCurrentTasks() {
        return currentTasks;
    }

    public void setCurrentTasks(TaskSummaryTemplate[] currentTasks) {
        this.currentTasks = currentTasks;
    }
}

