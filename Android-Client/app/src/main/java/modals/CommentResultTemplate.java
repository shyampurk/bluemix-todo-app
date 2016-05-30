package modals;

/**
 * Created by manishautomatic on 30/05/16.
 */
public class CommentResultTemplate {

    private String TaskId="";
    private CommentCallbackObject comment = new CommentCallbackObject();


    public String getTaskId() {
        return TaskId;
    }

    public void setTaskId(String taskId) {
        TaskId = taskId;
    }

    public CommentCallbackObject getComment() {
        return comment;
    }

    public void setComment(CommentCallbackObject comment) {
        this.comment = comment;
    }
}
