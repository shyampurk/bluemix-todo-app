package adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import modals.TaskListing;
import modals.TaskSummaryTemplate;
import vitymobi.com.todobluemix.R;

/**
 * Created by manishautomatic on 28/03/16.
 */
public class TaskListAdapter extends BaseAdapter {

    private Context parentContext;
    private LayoutInflater mInflater;
    private TaskSummaryTemplate[] tasksData;


    public TaskListAdapter(Context parentReference, TaskSummaryTemplate[] tasks){
        this.parentContext=parentReference;
        mInflater= LayoutInflater.from(parentReference);
        this.tasksData=tasks;
    }

    @Override
    public int getCount() {
        return tasksData.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view==null){
            holder = new ViewHolder();
            view=mInflater.inflate(R.layout.row_task_list,null);
            holder.mTxtVwTaskTitle=(TextView)view.findViewById(R.id.txtvwTaskTitle);
            holder.mTxtVwTaskDescription=(TextView)view.findViewById(R.id.txtvwTaskDescription);
            holder.mTxtVwCreatedOn=(TextView)view.findViewById(R.id.txtvwTaskCreatedOn);
            holder.mTxtVwTaskCreatedBy=(TextView)view.findViewById(R.id.txtvwTaskCreatedBy);
            holder.mTxtVwTaskUpdatedOn=(TextView)view.findViewById(R.id.txtvwTaskLastUpdatedOn);
            holder.mTxtVwCommentCount=(TextView)view.findViewById(R.id.txtvwTaskCommentCount);
            holder.mTxtVwTaskStatus=(TextView)view.findViewById(R.id.txtvwTaskStatus);
            holder.mTxtVwTaskId=(TextView)view.findViewById(R.id.txtvwTaskId);

            view.setTag(holder);
        }else{
            holder =(ViewHolder)view.getTag();
        }

       holder.mTxtVwTaskTitle.setText(tasksData[i].getTASK_NAME());
        holder.mTxtVwTaskDescription.setText(tasksData[i].getTASK_DESCRIPTION());
        holder.mTxtVwCreatedOn.setText("Created on: "+tasksData[i].getTASK_CREATION_DATE());
        holder.mTxtVwTaskCreatedBy.setText("Created by: "+tasksData[i].getDISPLAY_NAME());
        holder.mTxtVwTaskUpdatedOn.setText("Last updated on: "+tasksData[i].getLAST_UPDATED());
        holder.mTxtVwCommentCount.setText("Comments: "+tasksData[i].getNO_OF_COMMENTS());

        if(tasksData[i].getTASK_STATUS().equalsIgnoreCase("0")){
            holder.mTxtVwTaskStatus.setTextColor(Color.GREEN);
            holder.mTxtVwTaskStatus.setText("Status: Open ");
        }else{
            holder.mTxtVwTaskStatus.setText("Status: Closed ");
            holder.mTxtVwTaskStatus.setTextColor(Color.RED);
        }

        holder.mTxtVwTaskId.setText("Task ID: "+tasksData[i].getTASK_ID());
        return view;
    }


    private class ViewHolder{
        TextView mTxtVwTaskTitle;
        TextView mTxtVwTaskDescription;
        TextView mTxtVwCreatedOn;
        TextView mTxtVwTaskCreatedBy;
        TextView mTxtVwTaskUpdatedOn;
        TextView mTxtVwCommentCount;
        TextView mTxtVwTaskStatus;
        TextView mTxtVwTaskId;


    }

}
