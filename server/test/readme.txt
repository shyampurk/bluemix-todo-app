NOTE: These scripts are only for testing purpose. They are not part of the mail application.
	1)client.py - This program is used to test the functionality of the server without the client app. THis script can simulate all requests sent from teh client to server.
	
	Instructions:Select any of these tasks by their number
			 0)logOut 
			 1)getAddressBook 
			 2)getTaskList
			 3) addNewTask
			 4)addComment
			 5)updateTaskStatus 
			 6)getTaskDetails
		0)logOut --> select 0 you will be logged out
		1)getAddressBook --> select 1 you will get the contact list
		2)getTaskList --> select 2 you will get the Tasklist
		3)addNewTask --> select 3 to add new task, when you select it you will be asked to enter 
				the task_name,task_desc enter those to add the task
		4)addComment --> select 4 to add comment, when you select it you will be asked to enter 
				the task_id, and the comment you want to do.
				
		5) updateTaskStatus --> you have to enter the Task id of the task
			and you have to enter the to which you wants to change the task
			open = 1
			close = 0
			(enter the numeric)
		6)getTaskDetails --> select 6 to get the specific task, you will be asked to enter the 
				task_id and the start_index enter those to get the details

	2)reset.py This Program will help you when one of the login session has expired and the user is not able to login using that login credential from another app. This script will terminate the dormain login session which could have been left idle either by the app or in the client.py script.

	
