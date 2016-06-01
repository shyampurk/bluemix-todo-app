package com.example.todo;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class AppUI extends Activity {
	String collaborator,time;
	Globalvars globalvars;
	TextView updatetimetext;
	Button tasks;
	Integer cnt,taskcnt,donetaskcnt;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appui);
		
		updatetimetext=(TextView) findViewById(R.id.updatetimetext);
		
		tasks=(Button) findViewById(R.id.taskbutton);
		tasks.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent=new Intent(AppUI.this,TaskUI.class);
				startActivity(intent);

			}
		});
	}

	@Override
	protected void onResume() {
		
		globalvars=Globalvars.getInstance();
		time=globalvars.getLastupdatetime();
		if (time!=null)  updatetimetext.setText(time);
		cnt=globalvars.getTaskcnt();
		tasks.setText("Tasks "+ Globalvars.donetaskcnt+"/"+Globalvars.taskcnt);
		super.onResume();
	}

}
