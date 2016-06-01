package com.example.todo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;


public class MainActivity extends Activity {
	String collaborator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	
		Spinner spinner=(Spinner)findViewById(R.id.collaboratorspinner);
		Button submit=(Button) findViewById(R.id.submitbutton);
			spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
					public void  onItemSelected(AdapterView<?> parent, View view,int pos, long id) {
						collaborator=(parent.getItemAtPosition(pos).toString());
						Log.d("MainActivity"," collaborator "+collaborator);
						Globalvars.setCollaborator(collaborator);
					}
					@Override
					public void onNothingSelected(AdapterView<?> parent) {
					}
				});	
			submit.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent=new Intent(MainActivity.this,AppUI.class);
					startActivity(intent);
				}
			});
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString("collaborator", collaborator);
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		collaborator=savedInstanceState.getString("collaborator");
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		
	}
	
}
