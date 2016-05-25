package controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.pubnub.api.Callback;
import com.pubnub.api.PubnubError;


import org.json.JSONObject;

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
    private Callback pubnubCallback;


    public PubNubHelper(Context context) {
        this.parentReference = context;

    }


    public String performLogin(final String email, final String password,
                               final ProgressCallback progressCallback,
                               final Handler handlerInstance) {
        try {
            ToDoAppInstance.getInstance()
                    .getPubnubInstance()
                    .subscribe(ChannelConstants.LOGIN, new Callback() {
                        @Override
                        public void connectCallback(String channel, Object message) {

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
                                    jObject, new Callback() {
                                    });
                        }

                        @Override
                        public void disconnectCallback(String channel, Object message) {
                            System.out.println("SUBSCRIBE : DISCONNECT on channel:" + channel
                                    + " : " + message.getClass() + " : "
                                    + message.toString());
                        }

                        public void reconnectCallback(String channel, Object message) {
                            System.out.println("SUBSCRIBE : RECONNECT on channel:" + channel
                                    + " : " + message.getClass() + " : "
                                    + message.toString());
                        }

                        @Override
                        public void successCallback(String channel, Object message) {
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

                        @Override
                        public void errorCallback(String channel, PubnubError error) {
                            System.out.println("SUBSCRIBE : ERROR on channel " + channel
                                    + " : " + error.toString());

                            HandlerResponseMessage responseMessage = new HandlerResponseMessage();
                            responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_SUCCESS);
                            responseMessage.setResponseMessage(error.getErrorString());

                            Message mm = handlerInstance.obtainMessage(ChannelConstants.HANDLER_CODE_ERROR,
                                    responseMessage);
                            mm.sendToTarget();
                        }
                    });
        } catch (Exception e) {

        }


        return "";
    }


    public String performLogout(  final Handler handlerInstance) {
        try {
            ToDoAppInstance.getInstance()
                    .getPubnubInstance()
                    .subscribe(ChannelConstants.LOGOUT, new Callback() {
                        @Override
                        public void connectCallback(String channel, Object message) {

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
                        public void disconnectCallback(String channel, Object message) {
                            System.out.println("SUBSCRIBE : DISCONNECT on channel:" + channel
                                    + " : " + message.getClass() + " : "
                                    + message.toString());
                        }

                        public void reconnectCallback(String channel, Object message) {
                            System.out.println("SUBSCRIBE : RECONNECT on channel:" + channel
                                    + " : " + message.getClass() + " : "
                                    + message.toString());
                        }

                        @Override
                        public void successCallback(String channel, Object message) {
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
                            System.out.println("SUBSCRIBE : ERROR on channel " + channel
                                    + " : " + error.toString());
                            HandlerResponseMessage responseMessage = new HandlerResponseMessage();
                            responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_ERROR);
                            responseMessage.setResponseMessage(error.getErrorString());
                            Message mm = handlerInstance.obtainMessage(ChannelConstants.HANDLER_CODE_ERROR,
                                    responseMessage);
                            mm.sendToTarget();
                        }
                    });
        } catch (Exception e) {

        }


        return "";
    }

    public String addNewTask(final String taskName, final String taskDescription,  final Handler handlerInstance) {
        try {
            ToDoAppInstance.getInstance()
                    .getPubnubInstance()
                    .subscribe(ChannelConstants.ADD_NEW_TASK, new Callback() {
                        @Override
                        public void connectCallback(String channel, Object message) {

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
                                    jObject, new Callback() {
                                    });
                        }

                        @Override
                        public void disconnectCallback(String channel, Object message) {
                            System.out.println("SUBSCRIBE : DISCONNECT on channel:" + channel
                                    + " : " + message.getClass() + " : "
                                    + message.toString());
                        }

                        public void reconnectCallback(String channel, Object message) {
                            System.out.println("SUBSCRIBE : RECONNECT on channel:" + channel
                                    + " : " + message.getClass() + " : "
                                    + message.toString());
                        }

                        @Override
                        public void successCallback(String channel, Object message) {
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
                                            responseMessage );
                                    mm.sendToTarget();
                                }
                                if (responseTemplate.getResponse_code() == 1) {
                                    responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_FAILURE);
                                    responseMessage.setResponseMessage(responseTemplate.getResponse_message());
                                    Message mm = handlerInstance.obtainMessage(ChannelConstants.HANDLER_CODE_FAILURE,
                                            responseMessage );
                                    mm.sendToTarget();

                                }
                                ToDoAppInstance.getInstance().getPubnubInstance().unsubscribe(ChannelConstants.ADD_NEW_TASK);
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
                            System.out.println("SUBSCRIBE : ERROR on channel " + channel
                                    + " : " + error.toString());
                            HandlerResponseMessage responseMessage = new HandlerResponseMessage();
                            responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_ERROR);
                            responseMessage.setResponseMessage(error.getErrorString());
                            Message mm = handlerInstance.obtainMessage(ChannelConstants.HANDLER_CODE_ERROR,
                                    error.getErrorString());
                            mm.sendToTarget();
                        }
                    });
        } catch (Exception e) {

        }


        return "";
    }

    public String getTaskList(  final Handler handlerInstance) {
        try {
            ToDoAppInstance.getInstance()
                    .getPubnubInstance()
                    .subscribe(ChannelConstants.GET_TASK_LIST, new Callback() {
                        @Override
                        public void connectCallback(String channel, Object message) {

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
                                    jObject, new Callback() {
                                    });
                        }

                        @Override
                        public void disconnectCallback(String channel, Object message) {
                            System.out.println("SUBSCRIBE : DISCONNECT on channel:" + channel
                                    + " : " + message.getClass() + " : "
                                    + message.toString());
                        }

                        public void reconnectCallback(String channel, Object message) {
                            System.out.println("SUBSCRIBE : RECONNECT on channel:" + channel
                                    + " : " + message.getClass() + " : "
                                    + message.toString());
                        }

                        @Override
                        public void successCallback(String channel, Object message) {
                            System.out.println("SUBSCRIBE : " + channel + " : "
                                    + message.getClass() + " : " + message.toString());
                            try {
                                Gson g = new Gson();
                                final TaskListResponse responseTemplate = (TaskListResponse) g.fromJson(message.toString(),
                                        TaskListResponse.class);
                                if (responseTemplate.getType().equalsIgnoreCase("request"))
                                    return;


                                HandlerResponseMessage responseMessage = new HandlerResponseMessage();
                                if (responseTemplate.getResponse_code() == 0) {

                                    responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_SUCCESS);
                                    responseMessage.setResponseMessage(responseTemplate.getResponse_message());
                                    ToDoAppInstance.getInstance()
                                            .setCurrentTasks(responseTemplate.getResult());
                                    Message mm = handlerInstance.obtainMessage(ChannelConstants.HANDLER_CODE_SUCCESS,
                                           responseMessage);
                                    mm.sendToTarget();
                                }
                                if (responseTemplate.getResponse_code() == 1) {
                                    responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_FAILURE);
                                    responseMessage.setResponseMessage(responseTemplate.getResponse_message());
                                    Message mm = handlerInstance.obtainMessage(ChannelConstants.HANDLER_CODE_FAILURE,
                                            responseMessage);
                                    mm.sendToTarget();
                                }
                                ToDoAppInstance.getInstance().getPubnubInstance().unsubscribe(ChannelConstants.GET_TASK_LIST);
                            } catch (Exception e) {
                                HandlerResponseMessage responseMessage = new HandlerResponseMessage();
                                responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_ERROR);
                                responseMessage.setResponseMessage(e.getMessage());
                                Message mm = handlerInstance.obtainMessage(ChannelConstants.HANDLER_CODE_ERROR,
                                        responseMessage);
                                mm.sendToTarget();
                            }
                        }

                        @Override
                        public void errorCallback(String channel, PubnubError error) {
                            System.out.println("SUBSCRIBE : ERROR on channel " + channel
                                    + " : " + error.toString());
                            HandlerResponseMessage responseMessage = new HandlerResponseMessage();
                            responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_ERROR);
                            responseMessage.setResponseMessage(error.getErrorString());
                            Message mm = handlerInstance.obtainMessage(ChannelConstants.HANDLER_CODE_ERROR,
                                    responseMessage);
                            mm.sendToTarget();
                        }
                    });
        } catch (Exception e) {

        }


        return "";
    }

    public String getTaskDetails(final String task_ID, final String start_Index,  final Handler handlerInstance) {
        try {
            ToDoAppInstance.getInstance()
                    .getPubnubInstance()
                    .subscribe(ChannelConstants.GET_TASK_DETAILS, new Callback() {
                        @Override
                        public void connectCallback(String channel, Object message) {

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
                                    jObject, new Callback() {
                                    });
                        }

                        @Override
                        public void disconnectCallback(String channel, Object message) {
                            System.out.println("SUBSCRIBE : DISCONNECT on channel:" + channel
                                    + " : " + message.getClass() + " : "
                                    + message.toString());
                        }

                        public void reconnectCallback(String channel, Object message) {
                            System.out.println("SUBSCRIBE : RECONNECT on channel:" + channel
                                    + " : " + message.getClass() + " : "
                                    + message.toString());
                        }

                        @Override
                        public void successCallback(String channel, Object message) {
                            System.out.println("SUBSCRIBE : " + channel + " : "
                                    + message.getClass() + " : " + message.toString());
                            try {
                                Gson g = new Gson();
                                final TaskDetailsReponse responseTemplate = (TaskDetailsReponse) g.fromJson(message.toString(),
                                        TaskDetailsReponse.class);
                                if (responseTemplate.getType().equalsIgnoreCase("request"))
                                    return;


                                HandlerResponseMessage responseMessage = new HandlerResponseMessage();
                                if (responseTemplate.getResponse_code() == 0) {

                                    responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_SUCCESS);
                                    responseMessage.setResponseMessage(responseTemplate.getResponse_message());
                                    ToDoAppInstance.getInstance()
                                            .setCURRENT_SELECTED_TASK_DETAILS(responseTemplate.getResult());
                                    Message mm = handlerInstance.obtainMessage(ChannelConstants.HANDLER_CODE_SUCCESS,
                                            responseMessage);
                                    mm.sendToTarget();
                                }
                                if (responseTemplate.getResponse_code() == 1) {
                                    responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_FAILURE);
                                    responseMessage.setResponseMessage(responseTemplate.getResponse_message());
                                    Message mm = handlerInstance.obtainMessage(ChannelConstants.HANDLER_CODE_FAILURE,
                                            responseMessage);
                                    mm.sendToTarget();
                                }
                                ToDoAppInstance.getInstance().getPubnubInstance().unsubscribe(ChannelConstants.GET_TASK_DETAILS);
                            } catch (Exception e) {
                                HandlerResponseMessage responseMessage = new HandlerResponseMessage();
                                responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_ERROR);
                                responseMessage.setResponseMessage(e.getMessage());
                                Message mm = handlerInstance.obtainMessage(ChannelConstants.HANDLER_CODE_ERROR,
                                        responseMessage);
                                mm.sendToTarget();
                            }
                        }

                        @Override
                        public void errorCallback(String channel, PubnubError error) {
                            System.out.println("SUBSCRIBE : ERROR on channel " + channel
                                    + " : " + error.toString());
                            HandlerResponseMessage responseMessage = new HandlerResponseMessage();
                            responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_ERROR);
                            responseMessage.setResponseMessage(error.getErrorString());
                            Message mm = handlerInstance.obtainMessage(ChannelConstants.HANDLER_CODE_ERROR,
                                    responseMessage);
                            mm.sendToTarget();
                        }
                    });
        } catch (Exception e) {

        }


        return "";
    }

    public String addNewComment(final String taskID, final String comment,  final Handler handlerInstance) {
        try {
            ToDoAppInstance.getInstance()
                    .getPubnubInstance()
                    .subscribe(ChannelConstants.ADD_COMMENT, new Callback() {
                        @Override
                        public void connectCallback(String channel, Object message) {

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
                                    jObject, new Callback() {
                                    });
                        }

                        @Override
                        public void disconnectCallback(String channel, Object message) {
                            System.out.println("SUBSCRIBE : DISCONNECT on channel:" + channel
                                    + " : " + message.getClass() + " : "
                                    + message.toString());
                        }

                        public void reconnectCallback(String channel, Object message) {
                            System.out.println("SUBSCRIBE : RECONNECT on channel:" + channel
                                    + " : " + message.getClass() + " : "
                                    + message.toString());
                        }

                        @Override
                        public void successCallback(String channel, Object message) {
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
                                            responseMessage );
                                    mm.sendToTarget();
                                }
                                if (responseTemplate.getResponse_code() == 1) {
                                    responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_FAILURE);
                                    responseMessage.setResponseMessage(responseTemplate.getResponse_message());
                                    Message mm = handlerInstance.obtainMessage(ChannelConstants.HANDLER_CODE_FAILURE,
                                            responseMessage );
                                    mm.sendToTarget();

                                }
                                ToDoAppInstance.getInstance().getPubnubInstance().unsubscribe(ChannelConstants.ADD_COMMENT);
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
                            System.out.println("SUBSCRIBE : ERROR on channel " + channel
                                    + " : " + error.toString());
                            HandlerResponseMessage responseMessage = new HandlerResponseMessage();
                            responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_ERROR);
                            responseMessage.setResponseMessage(error.getErrorString());
                            Message mm = handlerInstance.obtainMessage(ChannelConstants.HANDLER_CODE_ERROR,
                                    error.getErrorString());
                            mm.sendToTarget();
                        }
                    });
        } catch (Exception e) {

        }


        return "";
    }

    public String getAddressBook( final Handler handlerInstance) {
        try {
            ToDoAppInstance.getInstance()
                    .getPubnubInstance()
                    .subscribe(ChannelConstants.GET_ADDRESS_BOOK, new Callback() {
                        @Override
                        public void connectCallback(String channel, Object message) {

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
                                    .getPubnubInstance().publish(ChannelConstants.GET_ADDRESS_BOOK,
                                    jObject, new Callback() {
                                    });
                        }

                        @Override
                        public void disconnectCallback(String channel, Object message) {
                            System.out.println("SUBSCRIBE : DISCONNECT on channel:" + channel
                                    + " : " + message.getClass() + " : "
                                    + message.toString());
                        }

                        public void reconnectCallback(String channel, Object message) {
                            System.out.println("SUBSCRIBE : RECONNECT on channel:" + channel
                                    + " : " + message.getClass() + " : "
                                    + message.toString());
                        }

                        @Override
                        public void successCallback(String channel, Object message) {
                            System.out.println("SUBSCRIBE : " + channel + " : "
                                    + message.getClass() + " : " + message.toString());
                            try {
                                Gson g = new Gson();
                                final GetAddressBookResponseTemplate responseTemplate = (GetAddressBookResponseTemplate) g.fromJson(message.toString(),
                                        GetAddressBookResponseTemplate.class);
                                if (responseTemplate.getType().equalsIgnoreCase("request"))
                                    return;

                                HandlerResponseMessage responseMessage = new HandlerResponseMessage();
                                if (responseTemplate.getResponse_code() == 0) {
                                    ToDoAppInstance
                                            .getInstance()
                                            .setCURRENT_ADDRESS_BOOK(responseTemplate.getResult());
                                    responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_SUCCESS);
                                    responseMessage.setResponseMessage(responseTemplate.getResponse_message());
                                    Message mm = handlerInstance.obtainMessage(ChannelConstants.HANDLER_CODE_SUCCESS,
                                            responseMessage );
                                    mm.sendToTarget();
                                }
                                if (responseTemplate.getResponse_code() == 1) {
                                    responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_FAILURE);
                                    responseMessage.setResponseMessage(responseTemplate.getResponse_message());
                                    Message mm = handlerInstance.obtainMessage(ChannelConstants.HANDLER_CODE_FAILURE,
                                            responseMessage );
                                    mm.sendToTarget();

                                }
                                ToDoAppInstance.getInstance().getPubnubInstance().unsubscribe(ChannelConstants.GET_ADDRESS_BOOK);
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
                            System.out.println("SUBSCRIBE : ERROR on channel " + channel
                                    + " : " + error.toString());
                            HandlerResponseMessage responseMessage = new HandlerResponseMessage();
                            responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_ERROR);
                            responseMessage.setResponseMessage(error.getErrorString());
                            Message mm = handlerInstance.obtainMessage(ChannelConstants.HANDLER_CODE_ERROR,
                                    error.getErrorString());
                            mm.sendToTarget();
                        }
                    });
        } catch (Exception e) {

        }


        return "";
    }

    public String updateTaskStatus(final String taskID, final String update_state,  final Handler handlerInstance) {
        try {
            ToDoAppInstance.getInstance()
                    .getPubnubInstance()
                    .subscribe(ChannelConstants.UPDATE_TASK_STATUS, new Callback() {
                        @Override
                        public void connectCallback(String channel, Object message) {

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
                                    jObject, new Callback() {
                                    });
                        }

                        @Override
                        public void disconnectCallback(String channel, Object message) {
                            System.out.println("SUBSCRIBE : DISCONNECT on channel:" + channel
                                    + " : " + message.getClass() + " : "
                                    + message.toString());
                        }

                        public void reconnectCallback(String channel, Object message) {
                            System.out.println("SUBSCRIBE : RECONNECT on channel:" + channel
                                    + " : " + message.getClass() + " : "
                                    + message.toString());
                        }

                        @Override
                        public void successCallback(String channel, Object message) {
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
                                            responseMessage );
                                    mm.sendToTarget();
                                }
                                if (responseTemplate.getResponse_code() == 1) {
                                    responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_FAILURE);
                                    responseMessage.setResponseMessage(responseTemplate.getResponse_message());
                                    Message mm = handlerInstance.obtainMessage(ChannelConstants.HANDLER_CODE_FAILURE,
                                            responseMessage );
                                    mm.sendToTarget();

                                }
                                ToDoAppInstance.getInstance().getPubnubInstance().unsubscribe(ChannelConstants.UPDATE_TASK_STATUS);
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
                            System.out.println("SUBSCRIBE : ERROR on channel " + channel
                                    + " : " + error.toString());
                            HandlerResponseMessage responseMessage = new HandlerResponseMessage();
                            responseMessage.setResponseCode(ChannelConstants.HANDLER_CODE_ERROR);
                            responseMessage.setResponseMessage(error.getErrorString());
                            Message mm = handlerInstance.obtainMessage(ChannelConstants.HANDLER_CODE_ERROR,
                                    error.getErrorString());
                            mm.sendToTarget();
                        }
                    });
        } catch (Exception e) {

        }


        return "";
    }





}
