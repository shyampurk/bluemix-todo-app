package modals;

import java.util.ArrayList;

/**
 * Created by manishautomatic on 28/03/16.
 */
public class TaskTemplate {
    private String mStrTitle="";
    private String mStrCreatedBy="";
    private String mStrCreatedOn="";
    private String mStrTaskDescription="";

    private ArrayList<CommentTemplate>taskCommentsList = new ArrayList<>();


    public String getmStrTaskDescription() {
        return mStrTaskDescription;
    }

    public void setmStrTaskDescription(String mStrTaskDescription) {
        this.mStrTaskDescription = mStrTaskDescription;
    }

    public String getmStrTitle() {
        return mStrTitle;
    }

    public void setmStrTitle(String mStrTitle) {
        this.mStrTitle = mStrTitle;
    }

    public String getmStrCreatedBy() {
        return mStrCreatedBy;
    }

    public void setmStrCreatedBy(String mStrCreatedBy) {
        this.mStrCreatedBy = mStrCreatedBy;
    }

    public String getmStrCreatedOn() {
        return mStrCreatedOn;
    }

    public void setmStrCreatedOn(String mStrCreatedOn) {
        this.mStrCreatedOn = mStrCreatedOn;
    }

    public ArrayList<CommentTemplate> getTaskCommentsList() {
        return taskCommentsList;
    }

    public void setTaskCommentsList(ArrayList<CommentTemplate> taskCommentsList) {
        this.taskCommentsList = taskCommentsList;
    }
}
