#Android ToDo App built using IBM Bluemix and PubNub

## Introduction
This is a demo android app to showcase the power of Bluemix and PubNub for building efficient and super scalable infrastructure for
the app's server side logic. Its a ToDo app for keeping track of project tasks. The app has following features

  1. Users can see the list of other collaborators in the project
  
  2. Users can see the list of tasks added to the project
  
  3. Users can create tasks
  
  4. Users can add comments to tasks
  
  5. Users can mark tasks as complete
  
  

## Deployment Steps

Before starting the deployment process, clone this github repository.

### Prerequisite

      - You should have a valid IBM account
      - You should have a Bluemix subscription and access to your Bluemix dashboard with atleast one space created
      - You should have the cloudfoundry command line tool installed
        (https://github.com/cloudfoundry/cli/releases)
      - You should have a PubNub subscription

### Setting up DashDB Database

Step 1: Login to Bluemix with your credentials.

Step 2: In your dashboard, goto Catalog and select the Data and Analytics Section
			
			You can see that the dashDB service will be listed under this section or you can search for dashDB 

Step 3: Click on dashDB service icon and create a dashDB service instance for your space by filling following details,
		
			1) Space - Your space name where you want to add this service ( This might have been preselected if you have an existing space)
    	2) App   - You can select "leave unbound"
			3) Service name - Enter a name for the service of your choice
			4) Credential name - Enter a name for the Credential of your choice
			5) Selected Plan - Choose 'Entry'.
			6) Click CREATE to create the dashdb service instance.

Step 4: After creation of the service, go back to dashboard.Now you can see the dashDB service added to your space. Click the service and click the launch button and you can see your newly created dashDB service home page.

Step 5: In the dashDB service home page, under the Side Menu, under the Connect -> Connection information, 
		
	You can see your dashDB Host name,Database name,user id and password.

        Make a note of Host Name, Port Number , Database Name, User ID and Password.

Step 6: In the Side Main Menu, click on "Run SQL"  and you will be presented the Run SQL screen.
		
		- Click on the 'Open' button and choose the SQL [schema file](server/dBdata.sql)
		- The SQL commands will be displayed in the text area.
		- Scroll down the test area untill you see the comment
			-- COMMANDS TO INSERT THE DATA INTO THE USERTABLE
			
		  Followed by four INSERT commands
		- For every INSERT command, replace the last four 'x' character of DASHxxxx with your DashDB User ID (numeric part) shown in the previous step under 'Connection Information'. ( For example , if your user ID is dash7768 , then it will be DASH7768) 
		- Click on the 'Validate' button to ensure that SQLsyntax is valid
		- Click on the 'Run' button to execute the SQL statements.

Step 7: If the Run command executed successfully , you will be able to see the new tables created under your dashDB instance
		- Click on "Tables" sub menu
		- Select the table from "Table Name" dropdown to access the table schema and data
		- You can find three tables listed under the dropdown
		- 	USERTABLE
		- 	TASKTABLE
		- 	COMMENTTABLE





### Hosting the Application Server on Bluemix



Step 1 - Update the parameters in the server [code](server/server.py) 

	Line 14 - In the databaseConnectionInfo dictionary , replace all the existing values with their respective values of your dashDB instance as displayed in dashDB Connection Information page.
	Line 15 - Change the value of DatabaseSchema with your dashDB User id ( in all caps)
	Line 19 - Specify yout PubNub Publish Key
	Line 20 - Specify yout PubNub Subscribe Key
	

Step 2 - Open the [manifest file](https://github.com/shyampurk/bluemix-todo-app/blob/master/server/manifest.yml) and update the follwing entries

	Line 12 - Change 'bluemixtodoapp' to the actual dashDB service name you have given while creating the dashDB service instance ( Step 3 in 'Setting up dashDB Database').


Step 3 - Login to Bluemix console via cf tool and select the space.

Step 4 - Change directory to the server application root (server) under the cloned github repository.

Step 5 - Run the following command to push the application code to bluemix

		'cf push' 

Once successfully pushed, the server application will be automatically started. You can check its state in your bluemix dashboard and see that its state is set to 'Running'


### Building the Android App

Follow the standard build procedures for building the APK package for this android App.

Before building , ensure that you select the PubNub keys as follows and ensure that the keys used in the app are same as the ones used in server.


## Usage

### User Accounts

Since it is a demo app, it has four user accounts which are preconfigured configured to using the app. User can log in to the App
using any of the following user accounts 

    Username : peter@example.com , Password : peter@123
    Username : eric@example.com  , Password : eric@123
    Username : sam@example.com   , Password : sam@123
    Username : mark@example.com  , Password : mark@123
    
### App Flow

Upon App Launch , the user is presented with the login screen. 

<img src="/screenshots/flow-1.png" align="center" width="250" >


Upon successful login, the user is taken to the Dashboard, where the user has option to view the current tasks or the Address Book.

<img src="/screenshots/flow-2.png" align="center" width="250" >

User can see the current set of collaborators by tapping on "Address Book" button.

<img src="/screenshots/flow-3.png" align="center" width="250" >

Once the user taps on the "Tasks" button, a list of all tasks is displayed. There is an option for the user to add a new task as well.

<img src="/screenshots/flow-4.png" align="center" width="250" >

User can also add a comment to a task.

<img src="/screenshots/flow-5.png" align="center" width="250" >

Finally the user can log out of the app by tapping on the "LOGOUT" button under Dashboard

<img src="/screenshots/flow-6.png" align="center" width="250" >


