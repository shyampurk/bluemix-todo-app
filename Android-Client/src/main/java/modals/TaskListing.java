package modals;

import java.util.ArrayList;

/**
 * Created by manishautomatic on 28/03/16.
 */
public class TaskListing {


    private ArrayList<TaskTemplate> mTasks = new ArrayList<>();


    public ArrayList<TaskTemplate> getmTasks() {
        return mTasks;
    }

    public void setmTasks(ArrayList<TaskTemplate> mTasks) {
        this.mTasks = mTasks;
    }
}
