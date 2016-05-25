package modals;

/**
 * Created by manishautomatic on 28/03/16.
 */
public class ContactTemplate {




    private String DISPLAY_NAME;
    private String USER_STATE;
    private String EMAIL="";
    private String LAST_ACTIVITY_TIME="";
    private String USER_ID="";


    public String getDISPLAY_NAME() {
        return DISPLAY_NAME;
    }

    public void setDISPLAY_NAME(String DISPLAY_NAME) {
        this.DISPLAY_NAME = DISPLAY_NAME;
    }

    public String getUSER_STATE() {
        return USER_STATE;
    }

    public void setUSER_STATE(String USER_STATE) {
        this.USER_STATE = USER_STATE;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getLAST_ACTIVITY() {
        return LAST_ACTIVITY_TIME;
    }

    public void setLAST_ACTIVITY(String LAST_ACTIVITY) {
        this.LAST_ACTIVITY_TIME = LAST_ACTIVITY;
    }

    public String getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }
}
