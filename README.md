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

### Setting up DashDB Database

### Hosting the Application Server on Bluemix

### Building the Android App

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


