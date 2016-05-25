package modals;

/**
 * Created by manishautomatic on 20/05/16.
 */
public class TaskSummaryTemplate {

    private String DISPLAY_NAME="";
    private String TASK_STATUS="";
    private String TASK_DESCRIPTION="";
    private String NO_OF_COMMENTS="";
    private String TASK_CREATION_DATE="";
    private String TASK_NAME="";
    private String TASK_ID="";
    private String LAST_UPDATED="";


    public String getDISPLAY_NAME() {
        return DISPLAY_NAME;
    }

    public void setDISPLAY_NAME(String DISPLAY_NAME) {
        this.DISPLAY_NAME = DISPLAY_NAME;
    }

    public String getTASK_STATUS() {
        return TASK_STATUS;
    }

    public void setTASK_STATUS(String TASK_STATUS) {
        this.TASK_STATUS = TASK_STATUS;
    }

    public String getTASK_DESCRIPTION() {
        return TASK_DESCRIPTION;
    }

    public void setTASK_DESCRIPTION(String TASK_DESCRIPTION) {
        this.TASK_DESCRIPTION = TASK_DESCRIPTION;
    }

    public String getNO_OF_COMMENTS() {
        return NO_OF_COMMENTS;
    }

    public void setNO_OF_COMMENTS(String NO_OF_COMMENTS) {
        this.NO_OF_COMMENTS = NO_OF_COMMENTS;
    }

    public String getTASK_CREATION_DATE() {
        return TASK_CREATION_DATE;
    }

    public void setTASK_CREATION_DATE(String TASK_CREATION_DATE) {
        this.TASK_CREATION_DATE = TASK_CREATION_DATE;
    }

    public String getTASK_NAME() {
        return TASK_NAME;
    }

    public void setTASK_NAME(String TASK_NAME) {
        this.TASK_NAME = TASK_NAME;
    }

    public String getTASK_ID() {
        return TASK_ID;
    }

    public void setTASK_ID(String TASK_ID) {
        this.TASK_ID = TASK_ID;
    }

    public String getLAST_UPDATED() {
        return LAST_UPDATED;
    }

    public void setLAST_UPDATED(String LAST_UPDATED) {
        this.LAST_UPDATED = LAST_UPDATED;
    }
}
