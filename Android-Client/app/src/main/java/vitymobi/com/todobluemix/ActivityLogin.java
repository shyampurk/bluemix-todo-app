package vitymobi.com.todobluemix;

import android.app.ProgressDialog;
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


import java.util.logging.LogRecord;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import controller.ChannelConstants;
import controller.ProgressCallback;
import controller.PubNubHelper;
import controller.ToDoAppInstance;
import modals.HandlerResponseMessage;

/**
 * Created by manishautomatic on 22/03/16.
 */
public class ActivityLogin extends AppCompatActivity implements View.OnClickListener, ProgressCallback {

    private EditText mEdtxtUsername, mEdtxtPassword;
    private Button mBtnLogin;
    private ProgressDialog progressDialog;
    private Handler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initlLayout();
        initProgressDialog();
        initHandler();
        checkIfUserLoggedIn();
    }


    private void checkIfUserLoggedIn(){
        if(!ToDoAppInstance.getInstance()
                .getPrefValue(ChannelConstants.PREF_KEY_USER_DISPLAY_NAME)
                .equalsIgnoreCase("")){
            startActivity(new Intent(ActivityLogin.this,ActivityDashboard.class));
            finish();
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
                   // ToDoAppInstance.getInstance().showAlertWithMessage((String) message.obj, ActivityLogin.this);
                    startActivity(new Intent(ActivityLogin.this,ActivityDashboard.class));
                    finish();
                } if(responseMessage.getResponseCode()== ChannelConstants.HANDLER_CODE_ERROR){
                    progressDialog.dismiss();
                    ToDoAppInstance.getInstance().showAlertWithMessage((String) message.obj, ActivityLogin.this);
                } if(responseMessage.getResponseCode()== ChannelConstants.HANDLER_CODE_FAILURE){
                    progressDialog.dismiss();
                    ToDoAppInstance.getInstance().showAlertWithMessage(responseMessage.getResponseMessage(), ActivityLogin.this);
                }


            }
        };
    }

    private void initlLayout() {
        mEdtxtPassword=(EditText)findViewById(R.id.edtxtpassword);
        mEdtxtUsername=(EditText)findViewById(R.id.edtxtusername);
        mBtnLogin=(Button)findViewById(R.id.btnLogin);
        mBtnLogin.setOnClickListener(this);
    }


    private void initProgressDialog(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("ToDo App");
        progressDialog.setMessage("please wait...");

    }

    @Override
    public void onClick(View view) {
        if(view==mBtnLogin){
            // validate credentials
            if(validanteCredentials()){
                new PubNubHelper(this).performLogin(mEdtxtUsername.getText().toString().trim(),
                        mEdtxtPassword.getText().toString().trim(),this,mHandler);
                //new PubNubHelper(this).performLogout("peter@sitename.com","peter123",this,mHandler);
                progressDialog.show();
            }




        }
    }


    private boolean validanteCredentials(){
        if(!emailValidator(mEdtxtUsername.getText().toString().trim())){
            ToDoAppInstance.getInstance().showAlertWithMessage("Please provide valid email",this);
            return false;
        }if(mEdtxtPassword.getText().toString().trim().equalsIgnoreCase("")){
            ToDoAppInstance.getInstance().showAlertWithMessage("Please provide valid password",this);
            return false;
        }
            return true;
    }


    public boolean emailValidator(String email)
    {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public void initRequest(String message) {
        progressDialog.setMessage("Loggin in, please wait...");
        progressDialog.show();

    }

    @Override
    public void requestSuccess(String message) {

        progressDialog.dismiss();
    }

    @Override
    public void requestFailure(String message) {
        progressDialog.dismiss();
        Toast.makeText(ActivityLogin.this,message,Toast.LENGTH_LONG).show();
//        ToDoAppInstance.getInstance().showAlertWithMessage(message,this);
    }

    @Override
    public void requestError(String message) {
        progressDialog.dismiss();
        ToDoAppInstance.getInstance().showAlertWithMessage(message, this);
    }
}
