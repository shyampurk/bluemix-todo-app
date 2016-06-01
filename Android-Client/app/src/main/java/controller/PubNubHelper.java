package controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.provider.SyncStateContract;
import android.support.v4.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.pubnub.api.Callback;
import com.pubnub.api.PubnubError;


import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Constructor;

import modals.AddCommentResponseTemplate;
import modals.CommentTemplate;
import modals.GetAddressBookResponseTemplate;
import modals.HandlerResponseMessage;
import modals.LoginResponseTemplate;
import modals.TaskDetailsReponse;
import modals.TaskListResponse;

/**
 * Created by manishautomatic on 16/05/16.
 */
public class PubNubHelper {

    private Context parentReference;

    private Callback mPbNbCallbackGetAddressBook;
    private Callback mPbNbCallbackDoLogin;
    private Callback mPbNbCallbackDoLogout;
    private Callback mPbNbCallbackGetTaskList;
    private Callback mPbNbCallbackGetTaskDetails;
    private Callback mPbNbCallbackAddComment;
    private Callback mPbNbCallbackToggleTaskStatus;
    private Callback mPbNbCallbackAddNewTask;
    private Gson g = new Gson();




    public PubNubHelper(Context context) {
        this.parentReference = context;

    }


    public void performLogin(final String email, final String password, final ProgressCallback progressCallback, final Handler handlerInstance) {
        try {
            initCallbackDoLogin(handlerInstance,email,password);
            String[] channelsSubscribed = ToDoAppInstance.getInstance().getPubnubInstance().getSubscribedChannelsArray();
            for(String channelName :channelsSubscribed){
                if(channelName.equalsIgnoreCase(ChannelConstants.LOGIN)){
                    JSONObject jObject = new JSONObject();
                        try {
                            jObject.put("type", "request");
                            jObject.put("email", email);
                            jObject.put("password", password);
                        } catch (Exception e) {
                            e.printStackTrace();
                            ;
                        }
                    ToDoAppInstance.getInstance()
                            .getPubnubInstance().publish(ChannelConstants.LOGIN,
                            jObject, mPbNbCallbackDoLogin);
                    return;
                }
            }
            ToDoAppInstance.getInstance()
                    .getPubnubInstance()
                    .subscribe(ChannelConstants.LOGIN, mPbNbCallbackDoLogin);
        } catch (Exception e) {

        }



    }

    public void performLogout(  final Handler handlerInstance) {
        try {
                initCallbackLogout(handlerInstance);
            ToDoAppInstance.getInstance()
                    .getPubnubInstance()
                    .subscribe(ChannelConstants.LOGOUT, mPbNbCallbackDoLogout);
        } catch (Exception e) {

        }

    }

    public void addNewTask(final String taskName, final String taskDescription ) {
        try {
            initAddTaskCallback( taskName, taskDescription);

            String[] channelsSubscribed = ToDoAppInstance.getInstance().getPubnubInstance().getSubscribedChannelsArray();
            for(String channelName :channelsSubscribed){
                if(channelName.equalsIgnoreCase(ChannelConstants.ADD_NEW_TASK)){
                    JSONObject jObject = new JSONObject();
                    JSONObject detailsObject = new JSONObject();
                    try {
                        jObject.put("type", "request");

                        detailsObject.put("EMAIL", ToDoAppInstance.getInstance()
                                .getPrefValue(ChannelConstants.PREF_KEY_USER_EMAIL));
                        detailsObject.put("USER_ID", ToDoAppInstance.getInstance()
                                .getPrefValue(ChannelConstants.PREF_KEY_USER_ID));
                        detailsObject.put("DISPLAY_NAME", ToDoAppInstance.getInstance()
                                .getPrefValue(ChannelConstants.PREF_KEY_USER_DISPLAY_NAME));
                        jObject.put("details", detailsObject);
                        jObject.put("task_name",taskName);
                        jObject.put("task_desc",taskDescription);

                    } catch (Exception e) {
                        e.printStackTrace();
                        ;
                    }
                    ToDoAppInstance.getInstance()
                            .getPubnubInstance().publish(ChannelConstants.ADD_NEW_TASK,
                            jObject, mPbNbCallbackAddNewTask);

                    return;
                }
            }


            ToDoAppInstance.getInstance()
                    .getPubnubInstance()
                    .subscribe(ChannelConstants.ADD_NEW_TASK, mPbNbCallbackAddNewTask);
        } catch (Exception e) {

        }
    }

    public  void getTaskList(  ) {
        try {
            initGetTasksCallback();
            String[] channelsSubscribed = ToDoAppInstance.getInstance().getPubnubInstance().getSubscribedChannelsArray();
            for(String channelName :channelsSubscribed){
                if(channelName.equalsIgnoreCase(ChannelConstants.GET_TASK_LIST)){
                    JSONObject jObject = new JSONObject();
                    JSONObject detailsObject = new JSONObject();
                    try {
                        jObject.put("type", "request");

                        detailsObject.put("EMAIL", ToDoAppInstance.getInstance()
                                .getPrefValue(ChannelConstants.PREF_KEY_USER_EMAIL));
                        detailsObject.put("USER_ID", ToDoAppInstance.getInstance()
                                .getPrefValue(ChannelConstants.PREF_KEY_USER_ID));
                        detailsObject.put("DISPLAY_NAME", ToDoAppInstance.getInstance()
                                .getPrefValue(ChannelConstants.PREF_KEY_USER_DISPLAY_NAME));
                        jObject.put("details", detailsObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ToDoAppInstance.getInstance()
                            .getPubnubInstance().publish(ChannelConstants.GET_TASK_LIST,
                            jObject, mPbNbCallbackGetTaskList);
                    return ;
                }
            }
            ToDoAppInstance.getInstance()
                    .getPubnubInstance()
                    .subscribe(ChannelConstants.GET_TASK_LIST, mPbNbCallbackGetTaskList);
        } catch (Exception e) {

        }


        return ;
    }

    public  void getTaskDetails(final Context con,final String task_ID, final String start_Index
                                      ) {
        try {
        initCallbackTaskDetails(con,
                task_ID, start_Index);
            String[] channelsSubscribed = ToDoAppInstance.getInstance().getPubnubInstance().getSubscribedChannelsArray();
            for(String channelName :channelsSubscribed){
                if(channelName.equalsIgnoreCase(ChannelConstants.GET_TASK_DETAILS)){
                    JSONObject jObject = new JSONObject();
                    JSONObject detailsObject = new JSONObject();
                    try {
                        jObject.put("type", "request");
                        jObject.put("task_id",task_ID);
                        jObject.put("start_index",start_Index);
                        detailsObject.put("EMAIL", ToDoAppInstance.getInstance()
                                .getPrefValue(ChannelConstants.PREF_KEY_USER_EMAIL));
                        detailsObject.put("USER_ID", ToDoAppInstance.getInstance()
                                .getPrefValue(ChannelConstants.PREF_KEY_USER_ID));
                        detailsObject.put("DISPLAY_NAME", ToDoAppInstance.getInstance()
                                .getPrefValue(ChannelConstants.PREF_KEY_USER_DISPLAY_NAME));
                        jObject.put("details", detailsObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                        ;
                    }
                    ToDoAppInstance.getInstance()
                            .getPubnubInstance().publish(ChannelConstants.GET_TASK_DETAILS,
                            jObject, mPbNbCallbackGetTaskDetails);

                return;
                }
            }
            ToDoAppInstance.getInstance()
                    .getPubnubInstance()
                    .subscribe(ChannelConstants.GET_TASK_DETAILS, mPbNbCallbackGetTaskDetails);
        } catch (Exception e) {

        }
        return ;
    }

    public void addNewComment(final String taskID, final String comment) {
        try {
            initCallbackAddComment(taskID,comment);


            String[] channelsSubscribed = ToDoAppInstance.getInstance().getPubnubInstance().getSubscribedChannelsArray();
            for(String channelName :channelsSubscribed){
                if(channelName.equalsIgnoreCase(ChannelConstants.ADD_COMMENT)){
                    JSONObject jObject = new JSONObject();
                    JSONObject detailsObject = new JSONObject();
                    try {
                        jObject.put("type", "request");

                        detailsObject.put("EMAIL", ToDoAppInstance.getInstance()
                                .getPrefValue(ChannelConstants.PREF_KEY_USER_EMAIL));
                        detailsObject.put("USER_ID", ToDoAppInstance.getInstance()
                                .getPrefValue(ChannelConstants.PREF_KEY_USER_ID));
                        detailsObject.put("DISPLAY_NAME", ToDoAppInstance.getInstance()
                                .getPrefValue(ChannelConstants.PREF_KEY_USER_DISPLAY_NAME));
                        jObject.put("details", detailsObject);
                        jObject.put("task_id",taskID);
                        jObject.put("comment",comment);

                    } catch (Exception e) {
                        e.printStackTrace();
                        ;
                    }
                    ToDoAppInstance.getInstance()
                            .getPubnubInstance().publish(ChannelConstants.ADD_COMMENT,
                            jObject, mPbNbCallbackAddComment);


                    return;
                }
            }

            ToDoAppInstance.getInstance()
                    .getPubnubInstance()
                    .subscribe(ChannelConstants.ADD_COMMENT, mPbNbCallbackAddComment);
        } catch (Exception e) {

        }


        return ;
    }

    public void getAddressBook( ) {
        try {
            initCallbackAddressBook();
            String[] channelsSubscribed = ToDoAppInstance.getInstance().getPubnubInstance().getSubscribedChannelsArray();
            for(String channelName :channelsSubscribed){
                if(channelName.equalsIgnoreCase(ChannelConstants.GET_ADDRESS_BOOK)){
                    JSONObject jObject = new JSONObject();
                    JSONObject detailsObject = new JSONObject();
                    try {
                        jObject.put("type", "request");
                        detailsObject.put("EMAIL", ToDoAppInstance.getInstance()
                                .getPrefValue(ChannelConstants.PREF_KEY_USER_EMAIL));
                        detailsObject.put("USER_ID", ToDoAppInstance.getInstance()
                                .getPrefValue(ChannelConstants.PREF_KEY_USER_ID));
                        detailsObject.put("DISPLAY_NAME", ToDoAppInstance.getInstance()
                                .getPrefValue(ChannelConstants.PREF_KEY_USER_DISPLAY_NAME));
                        jObject.put("details", detailsObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ToDoAppInstance.getInstance()
                            .getPubnubInstance().publish(ChannelConstants.GET_ADDRESS_BOOK,
                            jObject, mPbNbCallbackGetAddressBook);
                    return;
                }
            }

            ToDoAppInstance.getInstance()
                    .getPubnubInstance()
                    .subscribe(ChannelConstants.GET_ADDRESS_BOOK, mPbNbCallbackGetAddressBook);
        } catch (Exception e) {

        }


    }

    public void updateTaskStatus(final String taskID, final String update_state) {
        try {
            initCallbackToggleStatus(
                    taskID, update_state);
            String[] channelsSubscribed = ToDoAppInstance.getInstance().getPubnubInstance().getSubscribedChannelsArray();
            for(String channelName :channelsSubscribed){
                if(channelName.equalsIgnoreCase(ChannelConstants.UPDATE_TASK_STATUS)){
                    JSONObject jObject = new JSONObject();
                    JSONObject detailsObject = new JSONObject();
                    try {
                        jObject.put("type", "request");

                        detailsObject.put("EMAIL", ToDoAppInstance.getInstance()
                                .getPrefValue(ChannelConstants.PREF_KEY_USER_EMAIL));
                        int user_id=Integer.parseInt(ToDoAppInstance.getInstance()
                                .getPrefValue(ChannelConstants.PREF_KEY_USER_ID));
                        detailsObject.put("USER_ID", user_id);
                        detailsObject.put("DISPLAY_NAME", ToDoAppInstance.getInstance()
                                .getPrefValue(ChannelConstants.PREF_KEY_USER_DISPLAY_NAME));
                        jObject.put("details", detailsObject);
                        jObject.put("task_id",taskID);
                        jObject.put("update_state",update_state);

                    } catch (Exception e) {
                        e.printStackTrace();
                        ;
                    }
                    ToDoAppInstance.getInstance()
                            .getPubnubInstance().publish(ChannelConstants.UPDATE_TASK_STATUS,
                            jObject, mPbNbCallbackToggleTaskStatus);

                return;
                }
            }


            ToDoAppInstance.getInstance()
                    .getPubnubInstance()
                    .subscribe(ChannelConstants.UPDATE_TASK_STATUS,
                       mPbNbCallbackToggleTaskStatus);
        } catch (Exception e) {

        }


        return ;
    }


    private void initCallbackAddressBook(){
        mPbNbCallbackGetAddressBook = new Callback() {
            @Override
            public void successCallback(String channel, Object message) {
                super.successCallback(channel, message);
                if(message instanceof JSONArray)return;;
                System.out.println("SUBSCRIBE : " + channel + " : "
                        + message.getClass() + " : " + message.toString());

                if(message instanceof JSONArray)return;
                try {
                    Gson g = new Gson();
                    final GetAddressBookResponseTemplate responseTemplate = (GetAddressBookResponseTemplate) g.fromJson(message.toString(),
                            GetAddressBookResponseTemplate.class);
                    if (responseTemplate.getType().equalsIgnoreCase("request"))
                        return;


                    if (responseTemplate.getResponse_code() == 0) {

                        Activity a = (Activity)parentReference;
                        a.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                HandlerResponseMessage responseMessage = new HandlerResponseMessage();
                                responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_SUCCESS);
                                responseMessage.setResponseMessage(responseTemplate.getResponse_message());
                                ToDoAppInstance.getInstance().setCURRENT_ADDRESS_BOOK(responseTemplate.getResult());
                                Intent intent = new Intent(ChannelConstants.GET_ADDRESS_BOOK);
                                intent.putExtra("message", new Gson().toJson(responseMessage).toString());
                                LocalBroadcastManager.getInstance(parentReference).sendBroadcast(intent);
                                // handlerInstance.requestSuccess("");
                            }
                        });



                    }
                    if (responseTemplate.getResponse_code() == 1) {
                        if(responseTemplate.getResponse_message().equalsIgnoreCase(ChannelConstants.SESSION_EXPIRED)){

                            Activity a = (Activity)parentReference;
                            a.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    HandlerResponseMessage responseMessage = new HandlerResponseMessage();
                                    responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_SESSION_EXPIRED);
                                    responseMessage.setResponseMessage(responseTemplate.getResponse_message());
                                    Intent intent = new Intent(ChannelConstants.GET_ADDRESS_BOOK);
                                    intent.putExtra("message", new Gson().toJson(responseMessage).toString());
                                    LocalBroadcastManager.getInstance(parentReference).sendBroadcast(intent);
                                    // handlerInstance.requestSuccess("");
                                }
                            });
                            return;
                        }

                        Activity a = (Activity)parentReference;
                        a.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                HandlerResponseMessage responseMessage = new HandlerResponseMessage();
                                responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_FAILURE);
                                responseMessage.setResponseMessage(responseTemplate.getResponse_message());
                                Intent intent = new Intent(ChannelConstants.GET_ADDRESS_BOOK);
                                intent.putExtra("message", new Gson().toJson(responseMessage).toString());
                                LocalBroadcastManager.getInstance(parentReference).sendBroadcast(intent);
                                // handlerInstance.requestSuccess("");
                            }
                        });



                    }
                    //ToDoAppInstance.getInstance().getPubnubInstance().unsubscribe(ChannelConstants.UPDATE_TASK_STATUS);
                } catch (final Exception e) {


                    Activity a = (Activity)parentReference;
                    a.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            HandlerResponseMessage responseMessage = new HandlerResponseMessage();
                            responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_ERROR);
                            responseMessage.setResponseMessage(e.getMessage());
                            Intent intent = new Intent(ChannelConstants.GET_ADDRESS_BOOK);
                            intent.putExtra("message", new Gson().toJson(responseMessage).toString());
                            LocalBroadcastManager.getInstance(parentReference).sendBroadcast(intent);
                        }
                    });
                }
            }



            @Override
            public void errorCallback(String channel, PubnubError error) {
                super.errorCallback(channel, error);
            }

            @Override
            public void connectCallback(String channel, Object message) {
                super.connectCallback(channel, message);

                JSONObject jObject = new JSONObject();
                JSONObject detailsObject = new JSONObject();
                try {
                    jObject.put("type", "request");

                    detailsObject.put("EMAIL", ToDoAppInstance.getInstance()
                            .getPrefValue(ChannelConstants.PREF_KEY_USER_EMAIL));
                    int user_id=Integer.parseInt(ToDoAppInstance.getInstance()
                            .getPrefValue(ChannelConstants.PREF_KEY_USER_ID));
                    detailsObject.put("USER_ID", user_id);


                    detailsObject.put("DISPLAY_NAME", ToDoAppInstance.getInstance()
                            .getPrefValue(ChannelConstants.PREF_KEY_USER_DISPLAY_NAME));
                    jObject.put("details", detailsObject);



                } catch (Exception e) {
                    e.printStackTrace();
                    ;
                }
                ToDoAppInstance.getInstance()
                        .getPubnubInstance().publish(ChannelConstants.GET_ADDRESS_BOOK,
                        jObject, mPbNbCallbackGetAddressBook);
            }

            @Override
            public void reconnectCallback(String channel, Object message) {
                super.reconnectCallback(channel, message);
            }

            @Override
            public void disconnectCallback(String channel, Object message) {
                super.disconnectCallback(channel, message);
            }
        };
    }

    private void initCallbackDoLogin(final Handler handlerInstance, final String email, final String password ){
        {
            if (mPbNbCallbackDoLogin != null) return;

            mPbNbCallbackDoLogin = new Callback() {
                @Override
                public void successCallback(String channel, Object message){
                    {if(message instanceof JSONArray)return;;
                        System.out.println("SUBSCRIBE : " + channel + " : "
                                + message.getClass() + " : " + message.toString());
                        try {
                            Gson g = new Gson();
                            final LoginResponseTemplate responseTemplate = (LoginResponseTemplate) g.fromJson(message.toString(),
                                    LoginResponseTemplate.class);
                            if (responseTemplate.getType().equalsIgnoreCase("request"))
                                return;
                            if (responseTemplate.getResponse_code() == 0) {
                                ToDoAppInstance.getInstance().saveInPrefs(ChannelConstants.PREF_KEY_USER_ID, responseTemplate.getDetails().getUSER_ID());
                                ToDoAppInstance.getInstance().saveInPrefs(ChannelConstants.PREF_KEY_USER_EMAIL, responseTemplate.getDetails().getEMAIL());
                                ToDoAppInstance.getInstance().saveInPrefs(ChannelConstants.PREF_KEY_USER_DISPLAY_NAME, responseTemplate.getDetails().getDISPLAY_NAME());
                                HandlerResponseMessage responseMessage = new HandlerResponseMessage();
                                responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_SUCCESS);
                                responseMessage.setResponseMessage(responseTemplate.getResponse_message());

                                Message mm = handlerInstance.obtainMessage(ChannelConstants.HANDLER_CODE_SUCCESS,
                                        responseMessage);
                                mm.sendToTarget();
                            }
                            if (responseTemplate.getResponse_code() == 1) {
                                HandlerResponseMessage responseMessage = new HandlerResponseMessage();
                                if (responseTemplate.getResponse_message().equalsIgnoreCase(ChannelConstants.LOGED_IN_ALREADY)) {
                                    ToDoAppInstance.getInstance().saveInPrefs(ChannelConstants.PREF_KEY_USER_ID, responseTemplate.getDetails().getUSER_ID());
                                    ToDoAppInstance.getInstance().saveInPrefs(ChannelConstants.PREF_KEY_USER_EMAIL, responseTemplate.getDetails().getEMAIL());
                                    ToDoAppInstance.getInstance().saveInPrefs(ChannelConstants.PREF_KEY_USER_DISPLAY_NAME, responseTemplate.getDetails().getDISPLAY_NAME());

                                    responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_SUCCESS);
                                    responseMessage.setResponseMessage(responseTemplate.getResponse_message());

                                    Message mm = handlerInstance.obtainMessage(ChannelConstants.HANDLER_CODE_SUCCESS,
                                            responseMessage);
                                    mm.sendToTarget();
                                    return;
                                }
                                if (responseTemplate.getResponse_message().equalsIgnoreCase(ChannelConstants.SESSION_EXPIRED)) {
                                    responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_SESSION_EXPIRED);
                                    responseMessage.setResponseMessage(responseTemplate.getResponse_message());
                                    Message mm = handlerInstance.obtainMessage(ChannelConstants.HANDLER_CODE_SESSION_EXPIRED,
                                            responseMessage);
                                    mm.sendToTarget();
                                    return;
                                }
                                // HandlerResponseMessage responseMessage = new HandlerResponseMessage();
                                responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_FAILURE);
                                responseMessage.setResponseMessage(responseTemplate.getResponse_message());

                                Message mm = handlerInstance.obtainMessage(ChannelConstants.HANDLER_CODE_FAILURE,
                                        responseMessage);


                                mm.sendToTarget();
                            }
                            ToDoAppInstance.getInstance().getPubnubInstance().unsubscribe(ChannelConstants.LOGIN);
                        } catch (Exception e) {
                            HandlerResponseMessage responseMessage = new HandlerResponseMessage();
                            responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_SUCCESS);
                            responseMessage.setResponseMessage(e.getMessage());

                            Message mm = handlerInstance.obtainMessage(ChannelConstants.HANDLER_CODE_ERROR,
                                    responseMessage);
                            mm.sendToTarget();
                        }
                    }

                }
                @Override
                public void errorCallback(String channel, PubnubError error) {
                    super.errorCallback(channel, error);
                    HandlerResponseMessage responseMessage = new HandlerResponseMessage();
                    responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_SUCCESS);
                    responseMessage.setResponseMessage(error.getErrorString());

                    Message mm = handlerInstance.obtainMessage(ChannelConstants.HANDLER_CODE_ERROR,
                            responseMessage);
                    mm.sendToTarget();
                }

                @Override
                public void connectCallback(String channel, Object message) {
                    super.connectCallback(channel, message);

                    JSONObject jObject = new JSONObject();
                    try {
                        jObject.put("type", "request");
                        jObject.put("email", email);
                        jObject.put("password", password);
                    } catch (Exception e) {
                        e.printStackTrace();
                        ;
                    }

                    ToDoAppInstance.getInstance()
                            .getPubnubInstance().publish(ChannelConstants.LOGIN,
                            jObject, mPbNbCallbackDoLogin);
                }

                @Override
                public void reconnectCallback(String channel, Object message) {
                    super.reconnectCallback(channel, message);
                }

                @Override
                public void disconnectCallback(String channel, Object message) {
                    super.disconnectCallback(channel, message);
                }
            };
        }

    }

    private void initCallbackLogout( final Handler handlerInstance) {
        mPbNbCallbackDoLogout = new Callback() {
            @Override
            public void successCallback(String channel, Object message) {
                super.successCallback(channel, message);
                if(message instanceof JSONArray)return;;
                System.out.println("SUBSCRIBE : " + channel + " : "
                        + message.getClass() + " : " + message.toString());
                try {
                    Gson g = new Gson();
                    final LoginResponseTemplate responseTemplate = (LoginResponseTemplate) g.fromJson(message.toString(),
                            LoginResponseTemplate.class);
                    if (responseTemplate.getType().equalsIgnoreCase("request"))
                        return;

                    HandlerResponseMessage responseMessage = new HandlerResponseMessage();

                    if (responseTemplate.getResponse_code() == 0) {
                        responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_SUCCESS);
                        responseMessage.setResponseMessage(responseTemplate.getResponse_message());
                        Message mm = handlerInstance.obtainMessage(ChannelConstants.HANDLER_CODE_SUCCESS,
                                responseMessage);
                        mm.sendToTarget();
                    }
                    if (responseTemplate.getResponse_code() == 1) {
                        if (responseTemplate.getResponse_message().equalsIgnoreCase(ChannelConstants.SESSION_EXPIRED)) {
                            responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_SESSION_EXPIRED);
                            responseMessage.setResponseMessage(responseTemplate.getResponse_message());
                            Message mm = handlerInstance.obtainMessage(ChannelConstants.HANDLER_CODE_SESSION_EXPIRED,
                                    responseMessage);
                            mm.sendToTarget();
                            return;
                        }
                        responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_FAILURE);
                        responseMessage.setResponseMessage(responseTemplate.getResponse_message());
                        Message mm = handlerInstance.obtainMessage(ChannelConstants.HANDLER_CODE_FAILURE,
                                responseMessage);
                        mm.sendToTarget();
                    }
                    ToDoAppInstance.getInstance().getPubnubInstance().unsubscribe(ChannelConstants.LOGOUT);
                } catch (Exception e) {
                    HandlerResponseMessage responseMessage = new HandlerResponseMessage();
                    responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_ERROR);
                    responseMessage.setResponseMessage(e.getMessage());
                    Message mm = handlerInstance.obtainMessage(ChannelConstants.HANDLER_CODE_ERROR,
                            e.getMessage());
                    mm.sendToTarget();
                }

            }

            @Override
            public void errorCallback(String channel, PubnubError error) {
                super.errorCallback(channel, error);
                HandlerResponseMessage responseMessage = new HandlerResponseMessage();
                responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_ERROR);
                responseMessage.setResponseMessage(error.getErrorString());
                Message mm = handlerInstance.obtainMessage(ChannelConstants.HANDLER_CODE_ERROR,
                        responseMessage);
                mm.sendToTarget();
            }

            @Override
            public void connectCallback(String channel, Object message) {
                super.connectCallback(channel, message);

                JSONObject jObject = new JSONObject();
                JSONObject detailsObject = new JSONObject();
                try {
                    jObject.put("type", "request");

                    detailsObject.put("EMAIL", ToDoAppInstance.getInstance()
                            .getPrefValue(ChannelConstants.PREF_KEY_USER_EMAIL));
                    detailsObject.put("USER_ID", ToDoAppInstance.getInstance()
                            .getPrefValue(ChannelConstants.PREF_KEY_USER_ID));
                    detailsObject.put("DISPLAY_NAME", ToDoAppInstance.getInstance()
                            .getPrefValue(ChannelConstants.PREF_KEY_USER_DISPLAY_NAME));
                    jObject.put("details", detailsObject);

                } catch (Exception e) {
                    e.printStackTrace();
                    ;
                }
                ToDoAppInstance.getInstance()
                        .getPubnubInstance().publish(ChannelConstants.LOGOUT,
                        jObject, new Callback() {
                        });
            }

            @Override
            public void reconnectCallback(String channel, Object message) {
                super.reconnectCallback(channel, message);
            }

            @Override
            public void disconnectCallback(String channel, Object message) {
                super.disconnectCallback(channel, message);
            }

        };

    }

    private void initAddTaskCallback( final String taskName, final String taskDescription){

        mPbNbCallbackAddNewTask = new Callback() {
            @Override
            public void successCallback(String channel, Object message) {
                super.successCallback(channel, message);
                if(message instanceof JSONArray)return;;
                    System.out.println("SUBSCRIBE : " + channel + " : "
                            + message.getClass() + " : " + message.toString());
                    try {
                        Gson g = new Gson();
                        final LoginResponseTemplate responseTemplate = (LoginResponseTemplate) g.fromJson(message.toString(),
                                LoginResponseTemplate.class);
                        if (responseTemplate.getType().equalsIgnoreCase("request"))
                            return;


                        if (responseTemplate.getResponse_code() == 0) {
                            Activity a = (Activity)parentReference;
                            a.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    HandlerResponseMessage responseMessage = new HandlerResponseMessage();
                                    responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_SUCCESS);
                                    responseMessage.setResponseMessage(responseTemplate.getResponse_message());
                                    Intent intent = new Intent(ChannelConstants.ADD_NEW_TASK);
                                    intent.putExtra("message", new Gson().toJson(responseMessage).toString());
                                    LocalBroadcastManager.getInstance(parentReference).sendBroadcast(intent);
                                    // handlerInstance.requestSuccess("");
                                }
                            });


                        }
                        if (responseTemplate.getResponse_code() == 1) {
                            if(responseTemplate.getResponse_message().equalsIgnoreCase(ChannelConstants.SESSION_EXPIRED)){

                                Activity a = (Activity)parentReference;
                                a.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        HandlerResponseMessage responseMessage = new HandlerResponseMessage();
                                        responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_SESSION_EXPIRED);
                                        responseMessage.setResponseMessage(responseTemplate.getResponse_message());
                                        Intent intent = new Intent(ChannelConstants.ADD_NEW_TASK);
                                        intent.putExtra("message", new Gson().toJson(responseMessage).toString());
                                        LocalBroadcastManager.getInstance(parentReference).sendBroadcast(intent);
                                        // handlerInstance.requestSuccess("");
                                    }
                                });
                                return;
                            }

                            Activity a = (Activity)parentReference;
                            a.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    HandlerResponseMessage responseMessage = new HandlerResponseMessage();
                                    responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_FAILURE);
                                    responseMessage.setResponseMessage(responseTemplate.getResponse_message());
                                    Intent intent = new Intent(ChannelConstants.ADD_NEW_TASK);
                                    intent.putExtra("message", new Gson().toJson(responseMessage).toString());
                                    LocalBroadcastManager.getInstance(parentReference).sendBroadcast(intent);
                                    // handlerInstance.requestSuccess("");
                                }
                            });
                        }
                        //ToDoAppInstance.getInstance().getPubnubInstance().unsubscribe(ChannelConstants.ADD_NEW_TASK);
                    } catch (final Exception e) {

                        Activity a = (Activity)parentReference;
                        a.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                HandlerResponseMessage responseMessage = new HandlerResponseMessage();
                                responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_ERROR);
                                responseMessage.setResponseMessage(e.getMessage());
                                Intent intent = new Intent(ChannelConstants.ADD_NEW_TASK);
                                intent.putExtra("message", new Gson().toJson(responseMessage).toString());
                                LocalBroadcastManager.getInstance(parentReference).sendBroadcast(intent);
                                // handlerInstance.requestSuccess("");
                            }
                        });

                    }

            }

            @Override
            public void errorCallback(String channel,final  PubnubError error) {
                super.errorCallback(channel, error);

                    System.out.println("SUBSCRIBE : ERROR on channel " + channel
                            + " : " + error.toString());

                Activity a = (Activity)parentReference;
                a.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        HandlerResponseMessage responseMessage = new HandlerResponseMessage();
                        responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_ERROR);
                        responseMessage.setResponseMessage(error.getErrorString());
                        Intent intent = new Intent(ChannelConstants.ADD_NEW_TASK);
                        intent.putExtra("message", new Gson().toJson(responseMessage).toString());
                        LocalBroadcastManager.getInstance(parentReference).sendBroadcast(intent);
                        // handlerInstance.requestSuccess("");
                    }
                });
            }

            @Override
            public void connectCallback(String channel, Object message) {
                super.connectCallback(channel, message);


                    JSONObject jObject = new JSONObject();
                    JSONObject detailsObject = new JSONObject();
                    try {
                        jObject.put("type", "request");

                        detailsObject.put("EMAIL", ToDoAppInstance.getInstance()
                                .getPrefValue(ChannelConstants.PREF_KEY_USER_EMAIL));
                        detailsObject.put("USER_ID", ToDoAppInstance.getInstance()
                                .getPrefValue(ChannelConstants.PREF_KEY_USER_ID));
                        detailsObject.put("DISPLAY_NAME", ToDoAppInstance.getInstance()
                                .getPrefValue(ChannelConstants.PREF_KEY_USER_DISPLAY_NAME));
                        jObject.put("details", detailsObject);
                        jObject.put("task_name",taskName);
                        jObject.put("task_desc",taskDescription);

                    } catch (Exception e) {
                        e.printStackTrace();
                        ;
                    }
                    ToDoAppInstance.getInstance()
                            .getPubnubInstance().publish(ChannelConstants.ADD_NEW_TASK,
                            jObject, mPbNbCallbackAddNewTask);

            }

            @Override
            public void reconnectCallback(String channel, Object message) {
                super.reconnectCallback(channel, message);
            }

            @Override
            public void disconnectCallback(String channel, Object message) {
                super.disconnectCallback(channel, message);
            }
        };
    }

    private  void initGetTasksCallback(){
        mPbNbCallbackGetTaskList = new Callback() {
            @Override
            public void successCallback(String channel, Object message) {
                super.successCallback(channel, message);
                if(message instanceof JSONArray)return;;
                System.out.println("SUBSCRIBE : " + channel + " : "
                        + message.getClass() + " : " + message.toString());
                try {

                    final TaskListResponse responseTemplate = (TaskListResponse) g.fromJson(message.toString(),
                            TaskListResponse.class);
                    if (responseTemplate.getType().equalsIgnoreCase("request"))
                        return;



                    if (responseTemplate.getResponse_code() == 0) {


                        Activity a = (Activity)parentReference;
                        a.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                HandlerResponseMessage responseMessage = new HandlerResponseMessage();
                                responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_SUCCESS);
                                responseMessage.setResponseMessage(responseTemplate.getResponse_message());
                                ToDoAppInstance.getInstance()
                                        .setCurrentTasks(responseTemplate.getResult());
                                Intent intent = new Intent(ChannelConstants.GET_TASK_LIST);
                                intent.putExtra("message", g.toJson(responseMessage).toString());
                                LocalBroadcastManager.getInstance(parentReference).sendBroadcast(intent);
                                // handlerInstance.requestSuccess("");
                            }
                        });


                    }
                    if (responseTemplate.getResponse_code() == 1) {
                        if (responseTemplate.getResponse_message().equalsIgnoreCase(ChannelConstants.SESSION_EXPIRED)) {

                            Activity a = (Activity)parentReference;
                            a.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    HandlerResponseMessage responseMessage = new HandlerResponseMessage();
                                    responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_SESSION_EXPIRED);
                                    responseMessage.setResponseMessage(responseTemplate.getResponse_message());
                                    Intent intent = new Intent(ChannelConstants.GET_TASK_LIST);
                                    intent.putExtra("message", g.toJson(responseMessage).toString());
                                    LocalBroadcastManager.getInstance(parentReference).sendBroadcast(intent);
                                    // handlerInstance.requestSuccess("");
                                }
                            });
                           return;
                        }
                        Activity a = (Activity)parentReference;
                        a.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                HandlerResponseMessage responseMessage = new HandlerResponseMessage();
                                responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_FAILURE);
                                responseMessage.setResponseMessage(responseTemplate.getResponse_message());
                                Intent intent = new Intent(ChannelConstants.GET_TASK_LIST);
                                intent.putExtra("message", g.toJson(responseMessage).toString());
                                LocalBroadcastManager.getInstance(parentReference).sendBroadcast(intent);
                                // handlerInstance.requestSuccess("");
                            }
                        });

                    }
                    //ToDoAppInstance.getInstance().getPubnubInstance().unsubscribe(ChannelConstants.GET_TASK_LIST);
                } catch (final Exception e) {
                    Activity a = (Activity)parentReference;
                    a.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            HandlerResponseMessage responseMessage = new HandlerResponseMessage();
                            responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_ERROR);
                            responseMessage.setResponseMessage(e.getMessage());
                            Intent intent = new Intent(ChannelConstants.GET_TASK_LIST);
                            intent.putExtra("message", g.toJson(responseMessage).toString());
                            LocalBroadcastManager.getInstance(parentReference).sendBroadcast(intent);
                            // handlerInstance.requestSuccess("");
                        }
                    });


                }

            }

            @Override
            public void errorCallback(String channel, final PubnubError error) {
                super.errorCallback(channel, error);
                {
                    System.out.println("SUBSCRIBE : ERROR on channel " + channel
                            + " : " + error.toString());

                    Activity a = (Activity)parentReference;
                    a.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            HandlerResponseMessage responseMessage = new HandlerResponseMessage();
                            responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_ERROR);
                            responseMessage.setResponseMessage(error.getErrorString());
                            Intent intent = new Intent(ChannelConstants.GET_TASK_LIST);
                            intent.putExtra("message", g.toJson(responseMessage).toString());
                            LocalBroadcastManager.getInstance(parentReference).sendBroadcast(intent);
                            // handlerInstance.requestSuccess("");
                        }
                    });


                }
            }

            @Override
            public void connectCallback(String channel, Object message) {
                super.connectCallback(channel, message);
                {

                    JSONObject jObject = new JSONObject();
                    JSONObject detailsObject = new JSONObject();
                    try {
                        jObject.put("type", "request");

                        detailsObject.put("EMAIL", ToDoAppInstance.getInstance()
                                .getPrefValue(ChannelConstants.PREF_KEY_USER_EMAIL));
                        detailsObject.put("USER_ID", ToDoAppInstance.getInstance()
                                .getPrefValue(ChannelConstants.PREF_KEY_USER_ID));
                        detailsObject.put("DISPLAY_NAME", ToDoAppInstance.getInstance()
                                .getPrefValue(ChannelConstants.PREF_KEY_USER_DISPLAY_NAME));
                        jObject.put("details", detailsObject);


                    } catch (Exception e) {
                        e.printStackTrace();
                        ;
                    }
                    ToDoAppInstance.getInstance()
                            .getPubnubInstance().publish(ChannelConstants.GET_TASK_LIST,
                            jObject, mPbNbCallbackGetTaskList);
                }
            }

            @Override
            public void reconnectCallback(String channel, Object message) {
                super.reconnectCallback(channel, message);
            }

            @Override
            public void disconnectCallback(String channel, Object message) {
                super.disconnectCallback(channel, message);
            }

            };
        }

    public  void initCallbackTaskDetails( final Context context, final String task_ID, final String start_Index){
        mPbNbCallbackGetTaskDetails = new Callback() {
            @Override
            public void successCallback(String channel, Object message) {
                super.successCallback(channel, message);
                if(message instanceof JSONArray)return;;
                    System.out.println("SUBSCRIBE : " + channel + " : "
                            + message.getClass() + " : " + message.toString());
                    try {
                       final  Gson g = new Gson();
                        final TaskDetailsReponse responseTemplate = (TaskDetailsReponse) g.fromJson(message.toString(),
                                TaskDetailsReponse.class);
                        if (responseTemplate.getType().equalsIgnoreCase("request"))
                            return;


                        final HandlerResponseMessage responseMessage = new HandlerResponseMessage();
                        if (responseTemplate.getResponse_code() == 0) {
                             responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_SUCCESS);
                            responseMessage.setResponseMessage(responseTemplate.getResponse_message());
                            ToDoAppInstance.getInstance()
                                    .setCURRENT_SELECTED_TASK_DETAILS(responseTemplate.getResult());
                            Activity a = (Activity)context;
                            a.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    Intent intent = new Intent(ChannelConstants.GET_TASK_DETAILS);
                                    intent.putExtra("message",g.toJson(responseMessage).toString());
                                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                                    // handlerInstance.requestSuccess("");
                                }
                            });


                        }
                        if (responseTemplate.getResponse_code() == 1) {
                            if(responseTemplate.getResponse_message().equalsIgnoreCase(ChannelConstants.SESSION_EXPIRED)){
                                responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_SESSION_EXPIRED);
                                responseMessage.setResponseMessage(responseTemplate.getResponse_message());
                                Intent intent = new Intent(ChannelConstants.GET_TASK_DETAILS);
                                intent.putExtra("message", g.toJson(responseMessage).toString());
                                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                               // Message mm = handlerInstance.obtainMessage(ChannelConstants.HANDLER_CODE_SESSION_EXPIRED,
                                 //       responseMessage);
                                //mm.sendToTarget();
                                return;
                            }
                            responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_FAILURE);
                            responseMessage.setResponseMessage(responseTemplate.getResponse_message());
                            Intent intent = new Intent(ChannelConstants.GET_TASK_DETAILS);
                            intent.putExtra("message", g.toJson(responseMessage).toString());
                            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                           // Message mm = handlerInstance.obtainMessage(ChannelConstants.HANDLER_CODE_FAILURE,
                             //       responseMessage);
                            //mm.sendToTarget();
                        }
                        //ToDoAppInstance.getInstance().getPubnubInstance().unsubscribe(ChannelConstants.GET_TASK_DETAILS);
                    } catch (Exception e) {
                        HandlerResponseMessage responseMessage = new HandlerResponseMessage();
                        responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_ERROR);
                        responseMessage.setResponseMessage(e.getMessage());
                        Intent intent = new Intent(ChannelConstants.GET_TASK_DETAILS);
                        intent.putExtra("message", g.toJson(responseMessage).toString());
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                        //Message mm = handlerInstance.obtainMessage(ChannelConstants.HANDLER_CODE_ERROR,
                          //      responseMessage);
                        //mm.sendToTarget();
                    }
            }

            @Override
            public void errorCallback(String channel, PubnubError error) {
                super.errorCallback(channel, error);
                {
                    System.out.println("SUBSCRIBE : ERROR on channel " + channel
                            + " : " + error.toString());
                    HandlerResponseMessage responseMessage = new HandlerResponseMessage();
                    responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_ERROR);
                    responseMessage.setResponseMessage(error.getErrorString());
                    Intent intent = new Intent(ChannelConstants.GET_TASK_DETAILS);
                    intent.putExtra("message", g.toJson(responseMessage).toString());
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                   // Message mm = handlerInstance.obtainMessage(ChannelConstants.HANDLER_CODE_ERROR,
                     //       responseMessage);
                    //mm.sendToTarget();
                }
            }

            @Override
            public void connectCallback(String channel, Object message) {
                super.connectCallback(channel, message);

                JSONObject jObject = new JSONObject();
                JSONObject detailsObject = new JSONObject();
                try {
                    jObject.put("type", "request");
                    jObject.put("task_id",task_ID);
                    jObject.put("start_index",start_Index);
                    detailsObject.put("EMAIL", ToDoAppInstance.getInstance()
                            .getPrefValue(ChannelConstants.PREF_KEY_USER_EMAIL));
                    detailsObject.put("USER_ID", ToDoAppInstance.getInstance()
                            .getPrefValue(ChannelConstants.PREF_KEY_USER_ID));
                    detailsObject.put("DISPLAY_NAME", ToDoAppInstance.getInstance()
                            .getPrefValue(ChannelConstants.PREF_KEY_USER_DISPLAY_NAME));
                    jObject.put("details", detailsObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ToDoAppInstance.getInstance()
                        .getPubnubInstance().publish(ChannelConstants.GET_TASK_DETAILS,
                        jObject, mPbNbCallbackGetTaskDetails);

            }

            @Override
            public void reconnectCallback(String channel, Object message) {
                super.reconnectCallback(channel, message);
            }

            @Override
            public void disconnectCallback(String channel, Object message) {
                super.disconnectCallback(channel, message);
            }
        };
    }

    private void initCallbackToggleStatus(final String taskID, final String update_state){

        mPbNbCallbackToggleTaskStatus = new Callback() {
            @Override
            public void successCallback(String channel, Object message) {
                super.successCallback(channel, message);
                if(message instanceof JSONArray)return;;
                {
                    System.out.println("SUBSCRIBE : " + channel + " : "
                            + message.getClass() + " : " + message.toString());
                    try {

                        final LoginResponseTemplate responseTemplate = (LoginResponseTemplate) g.fromJson(message.toString(),
                                LoginResponseTemplate.class);
                        if (responseTemplate.getType().equalsIgnoreCase("request"))
                            return;

                        HandlerResponseMessage responseMessage = new HandlerResponseMessage();
                        if (responseTemplate.getResponse_code() == 0) {

                            Activity a = (Activity)parentReference;
                            a.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    HandlerResponseMessage responseMessage = new HandlerResponseMessage();
                                    responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_SUCCESS);
                                    responseMessage.setResponseMessage(responseTemplate.getResponse_message());
                                    Intent intent = new Intent(ChannelConstants.UPDATE_TASK_STATUS);
                                    intent.putExtra("message", g.toJson(responseMessage).toString());
                                    LocalBroadcastManager.getInstance(parentReference).sendBroadcast(intent);
                                    // handlerInstance.requestSuccess("");
                                }
                            });

                        }
                        if (responseTemplate.getResponse_code() == 1) {
                            if(responseTemplate.getResponse_message().equalsIgnoreCase(ChannelConstants.SESSION_EXPIRED)){

                                Activity a = (Activity)parentReference;
                                a.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        HandlerResponseMessage responseMessage = new HandlerResponseMessage();
                                        responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_SESSION_EXPIRED);
                                        responseMessage.setResponseMessage(responseTemplate.getResponse_message());
                                        Intent intent = new Intent(ChannelConstants.UPDATE_TASK_STATUS);
                                        intent.putExtra("message", g.toJson(responseMessage).toString());
                                        LocalBroadcastManager.getInstance(parentReference).sendBroadcast(intent);
                                        // handlerInstance.requestSuccess("");
                                    }
                                });
                                return;
                            }
                            Activity a = (Activity)parentReference;
                            a.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    HandlerResponseMessage responseMessage = new HandlerResponseMessage();
                                    responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_FAILURE);
                                    responseMessage.setResponseMessage(responseTemplate.getResponse_message());
                                    Intent intent = new Intent(ChannelConstants.UPDATE_TASK_STATUS);
                                    intent.putExtra("message", g.toJson(responseMessage).toString());
                                    LocalBroadcastManager.getInstance(parentReference).sendBroadcast(intent);
                                }
                            });
                        }

                    } catch (final Exception e) {

                        Activity a = (Activity)parentReference;
                        a.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                HandlerResponseMessage responseMessage = new HandlerResponseMessage();
                                responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_ERROR);
                                responseMessage.setResponseMessage(e.getMessage());
                                Intent intent = new Intent(ChannelConstants.UPDATE_TASK_STATUS);
                                intent.putExtra("message", g.toJson(responseMessage).toString());
                                LocalBroadcastManager.getInstance(parentReference).sendBroadcast(intent);
                                // handlerInstance.requestSuccess("");
                            }
                        });
                    }
                }

            }

            @Override
            public void errorCallback(String channel, PubnubError error) {
                super.errorCallback(channel, error);
                {
                    System.out.println("SUBSCRIBE : ERROR on channel " + channel
                            + " : " + error.toString());
                    HandlerResponseMessage responseMessage = new HandlerResponseMessage();
                    responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_ERROR);
                    responseMessage.setResponseMessage(error.getErrorString());
                    Intent intent = new Intent(ChannelConstants.UPDATE_TASK_STATUS);
                    intent.putExtra("message", g.toJson(responseMessage).toString());
                    LocalBroadcastManager.getInstance(parentReference).sendBroadcast(intent);
                }
            }

            @Override
            public void connectCallback(String channel, Object message) {
                super.connectCallback(channel, message);

                {

                    JSONObject jObject = new JSONObject();
                    JSONObject detailsObject = new JSONObject();
                    try {
                        jObject.put("type", "request");

                        detailsObject.put("EMAIL", ToDoAppInstance.getInstance()
                                .getPrefValue(ChannelConstants.PREF_KEY_USER_EMAIL));
                        detailsObject.put("USER_ID", ToDoAppInstance.getInstance()
                                .getPrefValue(ChannelConstants.PREF_KEY_USER_ID));
                        detailsObject.put("DISPLAY_NAME", ToDoAppInstance.getInstance()
                                .getPrefValue(ChannelConstants.PREF_KEY_USER_DISPLAY_NAME));
                        jObject.put("details", detailsObject);
                        jObject.put("task_id",taskID);
                        jObject.put("update_state",update_state);

                    } catch (Exception e) {
                        e.printStackTrace();
                        ;
                    }
                    ToDoAppInstance.getInstance()
                            .getPubnubInstance().publish(ChannelConstants.UPDATE_TASK_STATUS,
                            jObject, mPbNbCallbackToggleTaskStatus);
                }
            }

            @Override
            public void reconnectCallback(String channel, Object message) {
                super.reconnectCallback(channel, message);
            }

            @Override
            public void disconnectCallback(String channel, Object message) {
                super.disconnectCallback(channel, message);
            }
        };

    }

    private void initCallbackAddComment( final String taskID,final String comment){
        mPbNbCallbackAddComment = new Callback() {
            @Override
            public void successCallback(String channel, Object message) {
                super.successCallback(channel, message);
                if(message instanceof JSONArray)return;;
                    System.out.println("SUBSCRIBE : " + channel + " : "
                            + message.getClass() + " : " + message.toString());
                    try {
                       // Gson g = new Gson();
                        final AddCommentResponseTemplate responseTemplate = (AddCommentResponseTemplate) g.fromJson(message.toString(),
                                AddCommentResponseTemplate.class);
                        if (responseTemplate.getType().equalsIgnoreCase("request"))
                            return;


                        if (responseTemplate.getResponse_code() == 0) {
                            if(responseTemplate.getResult().getTaskId()
                                    .equalsIgnoreCase(ToDoAppInstance
                                            .getInstance().getCURRENT_SELECTED_TASK_ID())){
                                //  update the comment list..
                                CommentTemplate commentTemplate = new CommentTemplate();
                                commentTemplate.setCOMMENT_CREATION_DATE(responseTemplate.getResult().getComment().getPostedAt());
                                commentTemplate.setCOMMENT_DESCRIPTION(responseTemplate.getResult().getComment().getCommentText());
                                commentTemplate.setDISPLAY_NAME(responseTemplate.getResult().getComment().getDISPLAY_NAME());
                                commentTemplate.setORDER("");
                                commentTemplate.setTASK_ID(responseTemplate.getResult().getTaskId());
                                // now update the comments list..

                                ToDoAppInstance
                                        .getInstance()
                                        .getCURRENT_SELECTED_TASK_DETAILS()
                                        .getComments().add(commentTemplate);
                            }


                            Activity a = (Activity)parentReference;
                            a.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    HandlerResponseMessage responseMessage = new HandlerResponseMessage();
                                    responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_SUCCESS);
                                    responseMessage.setResponseMessage(responseTemplate.getResponse_message());
                                    Intent intent = new Intent(ChannelConstants.ADD_COMMENT);
                                    intent.putExtra("message", g.toJson(responseMessage).toString());
                                    LocalBroadcastManager.getInstance(parentReference).sendBroadcast(intent);
                                    // handlerInstance.requestSuccess("");
                                }
                            });
                        }
                        if (responseTemplate.getResponse_code() == 1) {
                            if(responseTemplate.getResponse_message().equalsIgnoreCase(ChannelConstants.SESSION_EXPIRED)){

                                Activity a = (Activity)parentReference;
                                a.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        HandlerResponseMessage responseMessage = new HandlerResponseMessage();
                                        responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_SESSION_EXPIRED);
                                        responseMessage.setResponseMessage(responseTemplate.getResponse_message());
                                        Intent intent = new Intent(ChannelConstants.ADD_COMMENT);
                                        intent.putExtra("message", g.toJson(responseMessage).toString());
                                        LocalBroadcastManager.getInstance(parentReference).sendBroadcast(intent);
                                        // handlerInstance.requestSuccess("");
                                    }
                                });
                                return;
                            }

                            Activity a = (Activity)parentReference;
                            a.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    HandlerResponseMessage responseMessage = new HandlerResponseMessage();
                                    responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_FAILURE);
                                    responseMessage.setResponseMessage(responseTemplate.getResponse_message());
                                    Intent intent = new Intent(ChannelConstants.ADD_COMMENT);
                                    intent.putExtra("message", g.toJson(responseMessage).toString());
                                    LocalBroadcastManager.getInstance(parentReference).sendBroadcast(intent);
                                }
                            });

                        }
                        //ToDoAppInstance.getInstance().getPubnubInstance().unsubscribe(ChannelConstants.ADD_COMMENT);
                    } catch (final Exception e) {

                        Activity a = (Activity)parentReference;
                        a.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                HandlerResponseMessage responseMessage = new HandlerResponseMessage();
                                responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_ERROR);
                                responseMessage.setResponseMessage(e.getMessage());
                                Intent intent = new Intent(ChannelConstants.ADD_COMMENT);
                                intent.putExtra("message", g.toJson(responseMessage).toString());
                                LocalBroadcastManager.getInstance(parentReference).sendBroadcast(intent);
                                // handlerInstance.requestSuccess("");
                            }
                        });
                    }

            }

            @Override
            public void errorCallback(String channel, final PubnubError error) {
                super.errorCallback(channel, error);
                {
                    System.out.println("SUBSCRIBE : ERROR on channel " + channel
                            + " : " + error.toString());

                    Activity a = (Activity)parentReference;
                    a.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            HandlerResponseMessage responseMessage = new HandlerResponseMessage();
                            responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_ERROR);
                            responseMessage.setResponseMessage(error.getErrorString());
                            Intent intent = new Intent(ChannelConstants.ADD_COMMENT);
                            intent.putExtra("message", g.toJson(responseMessage).toString());
                            LocalBroadcastManager.getInstance(parentReference).sendBroadcast(intent);
                            // handlerInstance.requestSuccess("");
                        }
                    });
                }
            }

            @Override
            public void connectCallback(String channel, Object message) {
                super.connectCallback(channel, message);


                    JSONObject jObject = new JSONObject();
                    JSONObject detailsObject = new JSONObject();
                    try {
                        jObject.put("type", "request");

                        detailsObject.put("EMAIL", ToDoAppInstance.getInstance()
                                .getPrefValue(ChannelConstants.PREF_KEY_USER_EMAIL));
                        detailsObject.put("USER_ID", ToDoAppInstance.getInstance()
                                .getPrefValue(ChannelConstants.PREF_KEY_USER_ID));
                        detailsObject.put("DISPLAY_NAME", ToDoAppInstance.getInstance()
                                .getPrefValue(ChannelConstants.PREF_KEY_USER_DISPLAY_NAME));
                        jObject.put("details", detailsObject);
                        jObject.put("task_id",taskID);
                        jObject.put("comment",comment);

                    } catch (Exception e) {
                        e.printStackTrace();
                        ;
                    }
                    ToDoAppInstance.getInstance()
                            .getPubnubInstance().publish(ChannelConstants.ADD_COMMENT,
                            jObject, mPbNbCallbackAddComment);



            }

            @Override
            public void reconnectCallback(String channel, Object message) {
                super.reconnectCallback(channel, message);
            }

            @Override
            public void disconnectCallback(String channel, Object message) {
                super.disconnectCallback(channel, message);
            }
        };
    }
}
