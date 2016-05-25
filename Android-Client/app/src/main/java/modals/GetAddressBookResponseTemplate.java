package modals;

/**
 * Created by manishautomatic on 20/05/16.
 */
public class GetAddressBookResponseTemplate {

    private int response_code=1;  // 1 = failure, 0=success
    private String type="";

    public int getResponse_code() {
        return response_code;
    }

    public void setResponse_code(int response_code) {
        this.response_code = response_code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getResponse_message() {
        return response_message;
    }

    public void setResponse_message(String response_message) {
        this.response_message = response_message;
    }

    public LoginDetailsObject getDetails() {
        return details;
    }

    public void setDetails(LoginDetailsObject details) {
        this.details = details;
    }

    public ContactTemplate[] getResult() {
        return result;
    }

    public void setResult(ContactTemplate[] result) {
        this.result = result;
    }

    private String response_message ="";
    private LoginDetailsObject details = new LoginDetailsObject();
    private ContactTemplate[] result = {};




}
