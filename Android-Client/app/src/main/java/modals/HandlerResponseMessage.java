package modals;

/**
 * Created by manishautomatic on 20/05/16.
 */
public class HandlerResponseMessage {

    private int responseCode=-1;
    private String responseMessage="";


    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}
