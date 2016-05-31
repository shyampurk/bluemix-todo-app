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

Step 6: In the Side Main Menu, click Tables,Here you can create the table for this application.
		
		Copy and paste the the SQL Table commands from SQL file 


Step 7: Then click the button "Run DDL".
		You can see the newly created table by selecting your schema and table name. Your schema is same as your username as displayed in Connect > Connection Information menu.

Step 8: Repeat step 6 and 7 for all tables defined in SQL file.

Step 9: Create the predefined User accounts 



### Hosting the Application Server on Bluemix

Step 1 - Clone this github repository

Step 2 - Update the parameters in the code 

	pub_key = PubNub Publish Key
	sub_key = PubNub Subscribe Key
	db_schema = User ID of the DashDB instance , in caps
	db_name = Database name
	db_host = Host Name
	table_name = Table name is set to KITCHENTRACKERAPP
	username = User ID of the DashDB instance
	pwd = Password of dashDB instance
	port = Port Number
	expiry = 0 ( Leave it to default value of zero)
	

Step 3 - Open the [manifest file](https://github.com/shyampurk/kitchen-tracker/blob/master/kitchen_tracker/manifest.yml) and update the follwing entries

		applicationa:
			- name : <name of the application on server>
	
		services
			- <dashdb instance name>

		where 
			<name of the application on server> - Any desired name for the application
			<dashdb instance name> - name of the dashdb service instance that you have created in the previous section.


Step 4 - Login to Bluemix console via cf tool and select the space.

Step 5 - Change directory to the server application root (kitchen_tracker) under the cloned github repository.

Step 6 - Run the following command to push the application code to bluemix

		'cf push' 

Once successfully pushed, the server application will be automatically started. You can check its state in your bluemix dashboard and see that its state is set to 'Running'


### Building the Android App

Follow the standard build procedures for building the APK package for this android App.

Before building , ensure that you select the PubNub keys as follows

    - Set the Publish keys in file _________________ at Line number __
    - Set the Subscribe keys in file _________________ at Line number __

Ensure that the keys used in the app are same as the ones used in server.


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


