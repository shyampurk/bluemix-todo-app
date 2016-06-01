package com.example.todo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ModifyTaskUI extends Activity {

	private static final String TAG = "ModifyTaskUI";
	private String action;
	private String collaborator;
	private Task task;
	private TextView tasktext;
	private TextView descrtext;
	private CheckBox checkBox;
	private Spinner spinner;
	SharedPreferences prefs;

	Globalvars globalvars;
	Task newtask,edittask;
	// Data Source
	String owners[] = { "Peter" ,"Sam", "Eric" };

	// Adapter
	ArrayAdapter<String> adapterOwners;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modifytaskui);

		
		action=getIntent().getStringExtra("action");
		tasktext=(TextView) findViewById(R.id.task);
		descrtext=(TextView) findViewById(R.id.descrtext);
		checkBox=(CheckBox) findViewById(R.id.donecheck);
		spinner=(Spinner) findViewById(R.id.ownerspinner);
		adapterOwners = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, owners);
		spinner.setAdapter(adapterOwners);
		spinner.setSelection(adapterOwners.getPosition(Globalvars.getCollaborator()));

		Button button=(Button) findViewById(R.id.addmodifytaskbutton); 
		if (action.equalsIgnoreCase("add")) 
		{
			tasktext.setText("Add Task");
			button.setText("Add");
		}
		else {
			tasktext.setText("Modify Task");
			button.setText("Modify");
			task=getIntent().getParcelableExtra("task");
			descrtext.setText(task.getDescription());
			checkBox.setChecked(task.getStatus());
			int child=spinner.getCount();
			
			int ownerpos=0;
			for (int x=0;x<child;x++) {
			
				String item=spinner.getItemAtPosition(x).toString();
				Log.d(TAG," item="+item);
				if (item.equalsIgnoreCase(task.getOwner())) {
					ownerpos=x;
					Log.d(TAG," ownerpos "+ownerpos);

				}
				
			}
			spinner.setSelection(ownerpos);
			
			if (!(Globalvars.getCollaborator().equalsIgnoreCase(task.getOwner().toString()))) {
				descrtext.setEnabled(false);
				checkBox.setEnabled(false);
				spinner.setEnabled(false);
				button.setEnabled(false);
				Toast.makeText(getApplicationContext(), "Only owner of a task has the rights to modify", Toast.LENGTH_SHORT).show();
			}
		}

		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DateFormat dateFormat=new SimpleDateFormat("yy/MM/dd hh:mm",Locale.getDefault());

				if (action.equalsIgnoreCase("add")) 
				{			
					newtask=new Task(descrtext.getText().toString(),spinner.getSelectedItem().toString(),dateFormat.format(new Date()),checkBox.isChecked());
					Log.d(TAG,"adding task "+newtask.toString());
					Intent intent=new Intent();  
					intent.putExtra("edittask",newtask);  
					setResult(2,intent);
					finish();
				}
				else {

					task.setDescription(descrtext.getText().toString());
					task.setOwner(spinner.getSelectedItem().toString());
					String time=dateFormat.format(new Date());
					task.setUpdatetime(time);

					task.setStatus(checkBox.isChecked());
					edittask=new Task(task.getId(),descrtext.getText().toString(),spinner.getSelectedItem().toString(),time,checkBox.isChecked());
					Log.d(TAG,"Modifying task "+edittask.toString());
					Intent intent=new Intent();  
					intent.putExtra("edittask",edittask);  
					setResult(2,intent);
					finish();//finishing activity 
				}
			}
		});
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString("collaborator", collaborator);
		outState.putString("action", action);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		collaborator=savedInstanceState.getString("collaborator");
		action=savedInstanceState.getString("action");
		super.onRestoreInstanceState(savedInstanceState);
	}


}
