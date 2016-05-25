package modals;

/**
 * Created by manishautomatic on 28/03/16.
 */
public class CommentTemplate {

    private String DISPLAY_NAME="";
    private String COMMENT_CREATION_DATE="";
    private String COMMENT_DESCRIPTION="";
    private String ORDER="";
    private String TASK_ID="";

    public String getDISPLAY_NAME() {
        return DISPLAY_NAME;
    }

    public void setDISPLAY_NAME(String DISPLAY_NAME) {
        this.DISPLAY_NAME = DISPLAY_NAME;
    }

    public String getCOMMENT_CREATION_DATE() {
        return COMMENT_CREATION_DATE;
    }

    public void setCOMMENT_CREATION_DATE(String COMMENT_CREATION_DATE) {
        this.COMMENT_CREATION_DATE = COMMENT_CREATION_DATE;
    }

    public String getCOMMENT_DESCRIPTION() {
        return COMMENT_DESCRIPTION;
    }

    public void setCOMMENT_DESCRIPTION(String COMMENT_DESCRIPTION) {
        this.COMMENT_DESCRIPTION = COMMENT_DESCRIPTION;
    }

    public String getORDER() {
        return ORDER;
    }

    public void setORDER(String ORDER) {
        this.ORDER = ORDER;
    }

    public String getTASK_ID() {
        return TASK_ID;
    }

    public void setTASK_ID(String TASK_ID) {
        this.TASK_ID = TASK_ID;
    }
}
