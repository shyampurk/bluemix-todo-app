package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import modals.CommentTemplate;
import modals.TaskListing;
import vitymobi.com.todobluemix.R;

/**
 * Created by manishautomatic on 28/03/16.
 */
public class TaskCommentsAdapter  extends BaseAdapter {

    private Context parentContext;
    private LayoutInflater mInflater;
    private CommentTemplate[] taskComments;


    public TaskCommentsAdapter(Context parentReference, CommentTemplate[] commentsList){
        this.parentContext=parentReference;
        mInflater= LayoutInflater.from(parentReference);
        taskComments=commentsList;
    }

    @Override
    public int getCount() {
        return taskComments.length;
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
            view=mInflater.inflate(R.layout.row_comments_list,null);
            holder.mTxtVwCommentText=(TextView)view.findViewById(R.id.txtvwCommentText);
            holder.mTxtVwCommentPostedOn=(TextView)view.findViewById(R.id.txtvwCommentPostedOn);
            holder.mTxtVwCommentPostedBy=(TextView)view.findViewById(R.id.txtvwCommentPostedBy);
            view.setTag(holder);
        }else{
            holder =(ViewHolder)view.getTag();
        }

        holder.mTxtVwCommentText.setText(taskComments[i].getCOMMENT_DESCRIPTION());
        holder.mTxtVwCommentPostedOn.setText("Posted On: "+taskComments[i].getCOMMENT_CREATION_DATE());
        holder.mTxtVwCommentPostedBy.setText("Posted By: "+taskComments[i].getDISPLAY_NAME());
        return view;
    }


    private class ViewHolder{
        TextView mTxtVwCommentText;
        TextView mTxtVwCommentPostedOn;
        TextView mTxtVwCommentPostedBy;

    }
}
