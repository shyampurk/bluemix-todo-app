package com.example.todo;

import java.util.ArrayList;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;

public class TaskUI extends Activity {

	private static final String TAG = "TaskUI";
	
	private static final String channel = "todochannel";
	String action;
	String collaborator;
	private ListView listView;
	private ArrayList<Task> tasks;
	private Task task,newtask,edittask;
	/** Called when the activity is first created. */
	private TaskAdapter taskAdapter;
	protected int requestcode,index;
	private Pubnub pubnub=Globalvars.pubnub;;
	TextView tasklistheader;
	private static Globalvars globalvars;
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		  // Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Dismiss Notification
        notificationmanager.cancel(0);
		setContentView(R.layout.activity_taskui);
		Log.i("TaskUI", "PubNub Activity Started!");
		globalvars=Globalvars.getInstance();
		tasklistheader=(TextView) findViewById(R.id.tasklistheader);
		tasklistheader.setText("Tasks ("+Globalvars.donetaskcnt+"/"+Globalvars.taskcnt+")");
		listView = (ListView)findViewById(R.id.listView1);
		Log.d(TAG,"Calling oncreate" );
	    listView.setClickable(true);
		listView.setOnItemClickListener(new OnItemClickListener() {
			 @Override
			 public void onItemClick(AdapterView<?> parent, View view,
					 int position, long id) {
				 Log.d(TAG,"listview item clicked");
				 Intent intent=new Intent(getApplicationContext(),ModifyTaskUI.class);
				 
				 intent.putExtra("action", "modify");
				 index=position;
				 task=(Task)listView.getItemAtPosition(position);
				 intent.putExtra("task",task );
				 Log.d(TAG,"Modifying task "+task.toString());
				 requestcode=1;
				 startActivityForResult(intent,requestcode);
			 }
		 });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.action_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_add:
			Intent intent=new Intent(getApplicationContext(),ModifyTaskUI.class);
			intent.putExtra("collaborator", collaborator);
			intent.putExtra("action", "add");
			requestcode=2;
			startActivityForResult(intent,requestcode);
			break;	
		}
		return false;
	}

	// Call Back method  to get the Message form other Activity  
	@Override  
	protected void onActivityResult(int requestCode, int resultCode, Intent data)  
	{  
		super.onActivityResult(requestCode, resultCode, data);  
		if (data!=null) {

			// check if the request code is same as what is passed  here it is 2  
			if(requestCode==2)  
			{  
				newtask=data.getParcelableExtra("edittask"); 	      
				Callback callback = new Callback() {
					public void successCallback(String channel, Object response) {
						Log.d("PUBNUB","finished publishing" +response.toString());
					}
					public void errorCallback(String channel, PubnubError error) {
						Log.d("PUBNUB",error.toString());
					}
				};
				Log.d("PUBNUB","calling publish" + newtask.toString() );
				pubnub.publish(channel, "NEW"+";"+newtask.getId()+";"+newtask.getDescription()+";"+newtask.getOwner()+";"+newtask.getUpdatetime()+";"+newtask.getStatus(), callback);
			}
			if (requestCode==1) {
				if(resultCode==2){         	   
					edittask=data.getParcelableExtra("edittask"); 
					Log.d(TAG,"Modifying task "+edittask.toString()+"at index"+index);

					Callback callback = new Callback() {
						public void successCallback(String channel, Object response) {
							Log.d("PUBNUB","finished publishing MODIFY" +response.toString());


						}
						public void errorCallback(String channel, PubnubError error) {
							Log.d("PUBNUB",error.toString());
						}
					};
					Log.d("PUBNUB","calling publish for modify" + edittask.toString() );
					pubnub.publish(channel, "MODIFY"+";"+edittask.getId()+";"+edittask.getDescription()+";"+edittask.getOwner()+";"+edittask.getUpdatetime()+";"+edittask.getStatus(), callback);
				} 

			}
		}

	}  

	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString("collaborator", collaborator);
		outState.putString("action", action);
		outState.putInt("index",index);
		outState.putParcelableArrayList("tasks", tasks);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		collaborator=savedInstanceState.getString("collaborator");
		action=savedInstanceState.getString("action");
		index=savedInstanceState.getInt("index");
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onResume() {

		if (!Globalvars.isSubscribed) {
			subscribe();
			}
		Globalvars.activityResumed();
		Log.d(TAG,"Calling onresume, issubscribed=" +Globalvars.isSubscribed.toString());
		tasks=globalvars.getTasks();
		Log.d(TAG,"Calling onresume" +tasks.toString());
		taskAdapter = new TaskAdapter(TaskUI.this,R.id.list_item, tasks);
		taskAdapter.notifyDataSetChanged();
		listView.setAdapter(taskAdapter);
		
		super.onResume();
	}
	
	private void subscribe() {
		
		//Start our own service
		Intent serviceIntent = new Intent(this, PubnubService.class);
		startService(serviceIntent);
	}


	@Override
	protected void onPause() {
		Globalvars.activityPaused();
		super.onPause();
	}
	

}
