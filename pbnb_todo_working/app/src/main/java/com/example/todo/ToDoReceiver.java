package com.example.todo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class ToDoReceiver extends BroadcastReceiver {

	private static final String TAG = "TodoReceiver";
	Task newtask,edittask;
	 public static final int NOTIFICATION_ID = 1;
	
	    
	    NotificationCompat.Builder builder;
	@Override
	public void onReceive(Context context, Intent intent) {
	
		Globalvars globalvars=Globalvars.getInstance();
				  String message = intent.getStringExtra("message");
				  Log.d(TAG,"Received Broadcast" +message);
				
				  String msg = message.toString();
					String[] msgparts = msg.split(";");
					DateFormat dateFormat = new SimpleDateFormat(
							"yy/MM/dd hh:mm", Locale.getDefault());
					globalvars.setLastupdatetime(dateFormat
							.format(new Date()));
					String todo = msgparts[0];
					Integer newCount;
					if (todo.equalsIgnoreCase("NEW")) {
						newCount = Integer.valueOf(msgparts[1]);
						Log.d(TAG,"Received msg with count " +newCount +" whereas my count is "+Task.getCount());
						//increment count if i am not in sync
						if (Task.getCount() < newCount) {
							Task.setCount(newCount);
						} 
						String desc=msgparts[2];
						newtask = new Task(Task.getCount(), desc,
								msgparts[3], msgparts[4], Boolean
								.valueOf(msgparts[5]));
						Log.d(TAG,"Adding task with count " +Task.getCount()+1);
								globalvars.getTasks().add(newtask);
								globalvars.updateTaskcnt();
								if (Globalvars.isActivityVisible()) {
									Intent i = new Intent();
							        i.setClassName("com.example.todo", "com.example.todo.TaskUI");
							        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
							        context.startActivity(i);
								} else {
									Notification(context, desc,"New ToDo");
								}

					} else if (todo.equalsIgnoreCase("MODIFY")) {
						String desc=msgparts[2];
						edittask = new Task(Integer.valueOf(msgparts[1]),
								desc, msgparts[3], msgparts[4],
								Boolean.valueOf(msgparts[5]));
								globalvars.editTask(edittask);
								globalvars.updateTaskcnt();
								
								if (Globalvars.isActivityVisible()) {
									Intent i = new Intent();
							        i.setClassName("com.example.todo", "com.example.todo.TaskUI");
							        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
							        context.startActivity(i);
								} else {
									Notification(context, desc,"Modify ToDo");
								}
												
					}
			

		
	}
	
	public void Notification(Context context, String message,String title) {
		
		// Open NotificationView Class on Notification Click
		Intent intent = new Intent(context, TaskUI.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
	
		// Open NotificationView.java Activity
		PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
 
		// Create Notification using NotificationCompat.Builder
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				context)
				// Set Icon
				.setSmallIcon(R.drawable.ic_launcher)
				// Set Ticker Message
				.setTicker(message)
				// Set Title
				.setContentTitle(title)
				// Set Text
				.setContentText(message)
				// Add an Action Button below Notification
				//.addAction(R.drawable.ic_launcher, "Action Button", pIntent)
				// Set PendingIntent into Notification
				.setContentIntent(pIntent)
				// Dismiss Notification
				.setAutoCancel(true);
 
		// Create Notification Manager
		NotificationManager notificationmanager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// Build Notification with Notification Manager
		notificationmanager.notify(0, builder.build());
 
	}
}

