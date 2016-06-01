package com.example.todo;
import java.util.List;
import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class TaskAdapter extends ArrayAdapter<Task> {
	Activity context;
    List<Task> tasks;

	public TaskAdapter(Activity context, int resource, List<Task> tasks) {
		super(context, resource, tasks);
		this.context = context;
        this.tasks = tasks;
	}
	
	private class ViewHolder {
    	TextView taskdescr;
    	TextView taskupdate;
    	TextView taskowner;
    	CheckBox donetask;
 	}
 
    public View getView(int position, View convertView, final ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.tasklistitem, null);
            holder = new ViewHolder();
            holder.taskdescr = (TextView) convertView.findViewById(R.id.taskdescrtext);
            holder.taskupdate=(TextView)convertView.findViewById(R.id.taskupdatetext);
            holder.taskowner=(TextView)convertView.findViewById(R.id.taskownertext);
            holder.donetask=(CheckBox)convertView.findViewById(R.id.donetaskcheck);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
       
        Task t = getItem(position);
        Log.d("adapter","showing listitem "+t.toString());
        holder.taskdescr.setText(t.getDescription());
        holder.taskupdate.setText(t.getUpdatetime());
        holder.taskowner.setText("("+t.getOwner()+")");
       	holder.donetask.setChecked(t.getStatus());
        convertView.setBackgroundColor(Color.CYAN);
        return convertView;
    }

	public List<Task> getTasks() {
        return tasks;
    }

	
}
