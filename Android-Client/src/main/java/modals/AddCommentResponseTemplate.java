package modals;

/**
 * Created by manishautomatic on 30/05/16.
 */
public class AddCommentResponseTemplate {

    private int response_code=1;
    private String type="";
    private LoginDetailsObject details= new LoginDetailsObject();
    private String response_message="";
    private CommentResultTemplate result = new CommentResultTemplate();


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

    public LoginDetailsObject getDetails() {
        return details;
    }

    public void setDetails(LoginDetailsObject details) {
        this.details = details;
    }

    public String getResponse_message() {
        return response_message;
    }

    public void setResponse_message(String response_message) {
        this.response_message = response_message;
    }

    public CommentResultTemplate getResult() {
        return result;
    }

    public void setResult(CommentResultTemplate result) {
        this.result = result;
    }
}
