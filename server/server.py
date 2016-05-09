# IMPORTING THE REQUIRED MODULES
import ibm_db
import time
import os
import sys
from ibm_db import connect
from ibm_db import active
import datetime
import ConfigParser
from pubnub import Pubnub
import logging

LOG_FILENAME = 'logfile.log'
logging.basicConfig(filename=LOG_FILENAME,level=logging.DEBUG)


# PUBNUB KEYS

pub_key = 'pub-c-913ab39c-d613-44b3-8622-2e56b8f5ea6d'
sub_key = 'sub-c-8ad89b4e-a95e-11e5-a65d-02ee2ddab7fe'


'''****************************************************************************************
Function Name 	:	connectioncheck_handler() (dB operation)
Description		:	Function to check the connection to the DataBase is True/False
Parameters 		:	- 
****************************************************************************************'''

def connectioncheck_handler():
	global connection,url
	if (active(connection) == False):
		connection = ibm_db.pconnect(url ,'' ,'')
	return None	


'''****************************************************************************************
Function Name 	:	dBop_Fetchall
Description		:	Function used to fetch the Data from a Table in DataBase
Parameters 		:	tableNAme - Name of the Table in DataBase from where Data is being Extracted  
					select    - Field Name in the Table of our interest 
****************************************************************************************'''
def dBop_Fetchall(tableName,select):
	global connection
	connectioncheck_handler()
	fetchall_list = []
	
	fetchall_query = "SELECT "+select+" FROM "+tableName+""
	fetchall_stmt = ibm_db.exec_immediate(connection, fetchall_query)
	dictionary = ibm_db.fetch_assoc(fetchall_stmt)
	
	if(dictionary == False):
		return fetchall_list	
	else:	
		while(dictionary!=False):
			for key in dictionary.keys():
				dictionary[key] = str(dictionary[key])
			fetchall_list.append(dictionary)
			dictionary = ibm_db.fetch_assoc(fetchall_stmt)

	ibm_db.free_stmt(fetchall_stmt)
	return fetchall_list	
	
'''****************************************************************************************
Function Name 	:	dBop_update  (dashdB operation)
Description		:	Function used to update Data to the Table in DataBase
Parameters 		:	tableNAme            - Name of the Table in DataBase from where Data is being Extracted  
					columnName           - Column Name in the Table of our interest Getting updated
					conditionColumnName  - Colunm Name in the Table used for the where clause condition
					conditionColumnValue - Colunn value in the Table used for the where clause condition
					updatevalue          - The Value 
****************************************************************************************'''
def dBop_update(tableName,columnName,conditionColumnName,conditionColumnValue,updatevalue):
	global connection
	connectioncheck_handler()
	update_query = "UPDATE "+tableName+" SET "+columnName+" = \'"+str(updatevalue)+"\'  WHERE "+conditionColumnName+" = \'"+str(conditionColumnValue)+"\'"
	stmt = ibm_db.exec_immediate(connection, update_query)
	ibm_db.free_stmt(stmt)

'''****************************************************************************************
Function Name 	:	dBop_addNewTaskinsert  (dashdB operation)
Description		:	Function used to insert the Data to the Table in DataBase 
Parameters 		:	task_id   	 - Task identity number in the DataBase 
					task_name 	 - Task name in the DataBase
					owner_id  	 - User identity number 
					task_desc 	 - About the Task
					dateofcreate - Date when the task is created
					lastupdate   - Date when task is updated recently
					noofcomment  - Number comments for that particular task
					status       - Task status open(0)/close(1)
****************************************************************************************'''
def dBop_addNewTaskinsert(task_name,owner_id,task_desc,dateofcreate,lastupdate,noofcomment,status):
	global connection
	connectioncheck_handler()	
	upload_query = "INSERT INTO DASH6139.TASKTABLE VALUES (DEFAULT,\'"+str(task_name)+"\',\'"+str(owner_id)+"\',\'"+str(task_desc)+"\',\'"+str(dateofcreate)+"\',\'"+str(lastupdate)+"\',\'"+str(noofcomment)+"\',\'"+str(status)+"\')"
	stmt = ibm_db.exec_immediate(connection, upload_query)
	ibm_db.free_stmt(stmt)
	
'''****************************************************************************************
Function Name 	:	dBop_WhereFetch  (dashdB operation)
Description		:	Function used to fetch the Data from a Table in DataBase using where clause
Parameters 		:	tableNAme            - Name of the Table in DataBase from where Data is being Extracted  
					select               - column in the Table from where Data is being fetched
					anotherselect        - column in the Table from where Data is being fetched
					conditionColumnName  - Colunm Name in the Table used for the where clause condition
					conditionColumnValue - Colunn value in the Table used for the where clause condition
					 
****************************************************************************************'''
def dBop_WhereFetch(tableName,select,anotherselect,conditionColumnName,conditionColumnValue):
	global connection
	connectioncheck_handler()
	wherefetch_list = {}
	wherefetch_query = "SELECT "+select+" "+anotherselect+" FROM "+tableName+" WHERE "+conditionColumnName+" = "+str(conditionColumnValue)+""
	wherefetch_stmt = ibm_db.exec_immediate(connection, wherefetch_query)
	dictionary = ibm_db.fetch_assoc(wherefetch_stmt)
	if(dictionary == False):
		return wherefetch_list
	else:	
		while(dictionary!=False):
			for key in dictionary.keys():
				dictionary[key] = str(dictionary[key])
			wherefetch_list.update(dictionary)
			dictionary = ibm_db.fetch_assoc(wherefetch_stmt)

	ibm_db.free_stmt(wherefetch_stmt)
	return wherefetch_list

'''****************************************************************************************
Function Name 	:	dBop_WhereFetchwihtLimit  (dashdB operation)
Description		:	Function which performs Fetching the data from Database using the where clause and limit
Parameters 		:	tableNAme            - Name of the Table in DataBase from where Data is being Extracted  
					select               - column in the Table from where Data is being fetched
					anotherselect        - column in the Table from where Data is being fetched    
					conditionColumnName  - Colunm Name in the Table used for the where clause condition
					conditionColumnValue - Colunn value in the Table used for the where clause condition
					limitmin             - Minimum limit value for the Database operatin to limit the no.of outputs
					limitmax             - Maximum limit value for the Database operatin to limit the no.of outputs
****************************************************************************************'''

def dBop_WhereFetchwithLimit(tableName,select,anotherselect,conditionColumnName,conditionColumnValue,limitmin,limitmax):
	global connection
	connectioncheck_handler()
	wherefetchwithlimit_list = []
	

	wherefetchwithlimit_query = "SELECT "+select+" "+anotherselect+" FROM "+tableName+" WHERE "+conditionColumnName+" = "+str(conditionColumnValue)+" LIMIT \'"+str(limitmin)+"\',\'"+str(limitmax)+"\'"
	wherefetchwithlimit_stmt = ibm_db.exec_immediate(connection, wherefetchwithlimit_query)
	dictionary = ibm_db.fetch_assoc(wherefetchwithlimit_stmt)
	if(dictionary == False):
		return wherefetchwithlimit_list
	else:	
		while(dictionary!=False):
			for key in dictionary.keys():
				dictionary[key] = str(dictionary[key])
				
			wherefetchwithlimit_list.append(dictionary)
			dictionary = ibm_db.fetch_assoc(wherefetchwithlimit_stmt)

		ibm_db.free_stmt(wherefetchwithlimit_stmt) 
		return wherefetchwithlimit_list

'''****************************************************************************************
Function Name 	:	publish_handler  (pubnub operation)
Description		:	Function used to publish the data to the client
Parameters 		:	channel          - Name of the Table in DataBase from where Data is being Extracted  
					response_code    - code value for the operation success/failure
					response_message - message regarding the operation's success/failure    
					details  		 - Distionary contains Name of the user,userid,email,password
					result           - The result i.e., Addressbook/Tasklist/TaskDetails..  
****************************************************************************************'''

def publish_handler(channel,response_code,response_message,details,result):
	pbtry = 0
	while (pbtry<3):
		try:
			pubnub.publish(channel = channel ,message = {"type":"response","response_code":response_code,"response_message":response_message,"result":result,"details":details},error=error)
			return True
		except Exception as error_pdhandler:
			logging.debug("The error_pdhandler Exception is %s %s"%(error_pdhandler,type(error_pdhandler)))
			pbtry+=1
	return False		

'''****************************************************************************************
Function Name 	:	sessionexpirecheck_handler  ( operation)
Description		:	Function which checks whether the user's session expired or not
Parameters 		:   details  -  Distionary contains Name of the user,userid,email,password
****************************************************************************************'''

def sessionexpirecheck_handler(details):
	# SESSION EXPIRY CHECK
	global connection
	tableName = 'DASH6139.USERTABLE'
	select = 'LAST_ACTIVITY_TIME'
	anotherselect = ''
	conditionColumnName = 'USER_ID'
	conditionColumnValue = details['USER_ID']
	sessionexpire_check = dBop_WhereFetch(tableName,select,anotherselect,conditionColumnName,conditionColumnValue)
	lastactivitytime = sessionexpire_check['LAST_ACTIVITY_TIME']
	lastactivitytime = datetime.datetime.strptime(lastactivitytime, "%Y-%m-%d %H:%M:%S.%f")
	presenttime = datetime.datetime.now()
	diff = presenttime-lastactivitytime
	diff_minutes = (diff.days * 24 * 60) + (diff.seconds/60)
	
	if (diff_minutes <=30):
		session_expiry = False
	else:
		session_expiry = True
	return session_expiry

'''****************************************************************************************
Function Name 	:	loggedincheck_handler  ( operation)
Description		:	Fucntion which performs the check whether the user loggedin or not
Parameters 		:   details  - Distionary contains Name of the user,userid,email,password
					
****************************************************************************************'''

def loggedincheck_handler(details):	
	global connection
	#LOGGED IN OR NOT CHECK
	tableName = 'DASH6139.USERTABLE'
	select = 'USER_STATE'
	anotherselect = ''
	conditionColumnName = 'USER_ID'
	conditionColumnValue = details['USER_ID']
	loggedin_check = dBop_WhereFetch(tableName,select,anotherselect,conditionColumnName,conditionColumnValue)
	user_state = loggedin_check['USER_STATE']
	if (int(user_state)!=0):
		loggedin = True
	else:
		loggedin = False
	return loggedin	





'''****************************************************************************************
Function Name 	:	getAddressBook_handler  (App operation)
Description		:	Function which performs getting the Contacts list in the DataBase
Parameters 		:   details  - Distionary contains Name of the user,userid,email,password
					channel  - channel used for communication between client and the server
****************************************************************************************'''

def getAddressBook_handler(details,channel):
	global connection
	Address_book = {}
	
	tableName = "DASH6139.USERTABLE"
	columnName = 'LAST_ACTIVITY_TIME'
	conditionColumnName = 'USER_ID'
	conditionColumnValue = details['USER_ID']
	updatevalue = datetime.datetime.now()
	select = '*'

	session_expiry = sessionexpirecheck_handler(details)
	loggedin = loggedincheck_handler(details)
	
	if (session_expiry == False and loggedin == True):
	
		try:
			Address_book = dBop_Fetchall(tableName,select)
			response_code = 0
			response_message = "Fetching AddressBook operation performed successfully"
			dBop_update(tableName,columnName,conditionColumnName,conditionColumnValue,updatevalue)
		except Exception as error_fetchaddressbook:
			logging.debug("The error_fetchaddressbook Exception is %s %s"%(error_fetchaddressbook,type(error_fetchaddressbook)))
			response_code = 1
			response_message = "DataBase Internal Error"

			
		publish_handler(channel,response_code,response_message,details,Address_book)
		
	else:
		# HERE SOME MESSAGE SHOULD BE THERE 
		response_code = 1
		response_message = "session Expire/Already loggedin"
		publish_handler(channel,response_code,response_message,details,Address_book)
		
		
'''****************************************************************************************
Function Name 	:	getTaskList_handler  (App operation)
Description		:	Fucntion which performs Getting All Tasks in the Database
Parameters 		:   details  - Distionary contains Name of the user,userid,email,password
					channel  - channel used for communication between client and the server
****************************************************************************************'''


def getTaskList_handler(details,channel):
	tableName = "DASH6139.TASKTABLE"
	update_tableName = "DASH6139.USERTABLE"
	columnName = 'LAST_ACTIVITY_TIME'
	conditionColumnName = 'USER_ID'
	conditionColumnValue = details['USER_ID']
	updatevalue = datetime.datetime.now()
	select = '*'

	Task_list = {}
	session_expiry = sessionexpirecheck_handler(details)
	loggedin = loggedincheck_handler(details)
	
	if (session_expiry == False and loggedin == True):
		try:	
			Task_list = dBop_Fetchall(tableName,select)
		
			response_code = 0
			response_message = "Fetching TaskList operation performed successfully"
			dBop_update(update_tableName,columnName,conditionColumnName,conditionColumnValue,updatevalue)
		
		except Exception as error_fetchtasklist:
			logging.debug("The error_fetchtasklist Exception is %s %s"%(error_fetchtasklist,type(error_fetchtasklist)))
			response_code = 1
			response_message = "DataBase Internal error"
			
		publish_handler(channel,response_code,response_message,details,Task_list)
	else:
		# HERE SOME MESSAGE SHOULD BE THERE 
		response_code = 1
		if (session_expiry == True):
			response_message = "session Expired"
		else:
			response_message = "Already logged in"	
		publish_handler(channel,response_code,response_message,details,Task_list)
				
			
			

'''****************************************************************************************
Function Name 	:	getTaskDetails_handler  (App operation)
Description		:	Function which performs Getting the specified Task Details
Parameters 		:   details     - Distionary contains Name of the user,userid,email,password
					channel     - channel used for communication between client and the server
					task_id     - Task identity number in the TaskTable
					start_index - The starting number from which the comments should be shown 
****************************************************************************************'''

def getTaskDetails_handler(details,channel,task_id,start_index):
		
	tableName = "DASH6139.TASKTABLE"
	dateofcreate = datetime.datetime.now()
	columnName = 'LAST_ACTIVITY_TIME'
	conditionColumnName = 'USER_ID'
	conditionColumnValue = details['USER_ID']
	updatevalue = dateofcreate

	# LAST_ACTIVITY_TIME UPDATE
	update_tableName = "DASH6139.USERTABLE"
	
	wherefetch_tableName = 'DASH6139.COMMENTTABLE'
	end_index = int(start_index)+50
	Task_details = {}

	session_expiry = sessionexpirecheck_handler(details)
	loggedin = loggedincheck_handler(details)
	if (session_expiry == False and loggedin == True):
		select1 = '*'
		select2 = ''
		conditionColumnName_task = 'TASK_ID'
		conditionColumnValue_task = task_id

		try:
			taskdetails = dBop_WhereFetch(tableName,select1,select2,conditionColumnName_task,conditionColumnValue_task)
			commentdetails = dBop_WhereFetchwithLimit(wherefetch_tableName,select1,select2,conditionColumnName_task,conditionColumnValue_task,start_index,end_index)
			Task_details = taskdetails
			Task_details.update({"comments":commentdetails})
			response_code = 0
			
			response_message = 'task details fetched'
			dBop_update(update_tableName,columnName,conditionColumnName,conditionColumnValue,updatevalue)
		
		except Exception as error_taskdetails:
			logging.debug("The error_taskdetails Exception is %s %s"%(error_taskdetails,type(error_taskdetails)))
			response_code = 1
			response_message = 'DataBase Internal Error'
		
		
		
		publish_handler(channel,response_code,response_message,details,Task_details)
	
	else:
		# HERE SOME MESSAGE SHOULD BE THERE 
		response_code = 1
		if (session_expiry == True):
			response_message = "session Expired"
		else:
			response_message = "Already logged in"	
		publish_handler(channel,response_code,response_message,details,TaskDetails)
					



'''****************************************************************************************
Function Name 	:	addNewTask_handler  (App operation)
Description		:	Function which performs adding new task to the Table
Parameters 		:   details  - Distionary contains Name of the user,userid,email,password
					channel  - channel used for communication between client and the server
					task_name - Task Name in the TaskTable
					task_desc - About the Task  Task identity number in the TaskTable
****************************************************************************************'''


def addNewTask_handler(details,channel,task_name,task_desc):

	status = 0
	dateofcreate = datetime.datetime.now()
	lastupdate = dateofcreate
	noofcomment = 0
	tableName = 'DASH6139.TASKTABLE'
	update_tableName = 'DASH6139.USERTABLE'
	
	result = 0 
	owner_id = details['USER_ID']

	columnName = 'LAST_ACTIVITY_TIME'
	conditionColumnName = 'USER_ID'
	conditionColumnValue = details['USER_ID']
	updatevalue = dateofcreate
	
	
	session_expiry = sessionexpirecheck_handler(details)
	loggedin = loggedincheck_handler(details)
	
	if (session_expiry == False and loggedin == True):	
		
		try:
			insert_op = dBop_addNewTaskinsert(task_name,owner_id,task_desc,dateofcreate,lastupdate,noofcomment,status)
			response_code = 0
			response_message = "New Task Added Successfully" 
			dBop_update(update_tableName,columnName,conditionColumnName,conditionColumnValue,updatevalue)
		
		except Exception as error_insertop:
			logging.debug("The error_insertop Exception is %s %s"%(error_insertop,type(error_insertop)))
		
			response_code = 1
			response_message = "DataBase Internal Error"
		
		publish_handler(channel,response_code,response_message,details,result)
	else:
		# HERE SOME MESSAGE SHOULD BE THERE 
		response_code = 1
		if (session_expiry == True):
			response_message = "session Expired"
		else:
			response_message = "Already logged in"	
		publish_handler(channel,response_code,response_message,details,result)
					

	
'''****************************************************************************************
Function Name 	:	addComment_handler  (App operation)
Description		:	Function which performs adding comment to the particular choice
Parameters 		:   details  - Distionary contains Name of the user,userid,email,password
					channel  - channel used for communication between client and the server
					task_id  - Task identity number in the TaskTable
					comment  - comment that client wants to make for a task
****************************************************************************************'''

def addComment_handler(details,channel,task_id,comment):
	
	dateofcreate = datetime.datetime.now()
	update_tableName = 'DASH6139.USERTABLE'
	tableName = 'DASH6139.TASKTABLE'
	columnName = 'LAST_ACTIVITY_TIME'
	conditionColumnName = 'USER_ID'
	conditionColumnValue = details['USER_ID']
	updatevalue = dateofcreate
	result = 0
	
	
	session_expiry = sessionexpirecheck_handler(details)
	loggedin = loggedincheck_handler(details)
	
	if (session_expiry == False and loggedin == True):	
		try:
			select1 = 'TASK_STATUS'
			select2 = ''
			conditionColumnName_task = 'TASK_ID'
			conditionColumnValue_task = task_id

			task_status = dBop_WhereFetch(tableName,select1,select2,conditionColumnName_task,conditionColumnValue_task)
			
			if(int(task_status['TASK_STATUS']) == 1): #[0][0]
				response_code = 1
				response_message = "Task status is closed"
				publish_handler(channel,response_code,response_message,details,result)
					
			else:
				owner_id = details['USER_ID']
				select1 = 'NO_OF_COMMENTS'
				select2 = ''
				conditionColumnValue_task = 'TASK_ID'
				conditionColumnValue = task_id
				order = dBop_WhereFetch(tableName,select1,select2,conditionColumnValue_task,conditionColumnValue)
				order = int(order['NO_OF_COMMENTS'])+1
					

				try:
					upload_query = "INSERT INTO DASH6139.COMMENTTABLE VALUES (\'"+str(comment)+"\',\'"+str(dateofcreate)+"\',\'"+str(task_id)+"\',\'"+str(owner_id)+"\',\'"+str(order)+"\')"
					stmt = ibm_db.exec_immediate(connection, upload_query)
					ibm_db.free_stmt(stmt)
				except Exception as error_uploadcomment:
					logging.debug("The error_uploadcomment Exception is %s %s"%(error_uploadcomment,type(error_uploadcomment)))

					# print 'error_uploadcomment',error_uploadcomment	

				# FOR NO.OF.COMMENTS UPDATE
				tableName = 'DASH6139.TASKTABLE'
				columnName_task = 'NO_OF_COMMENTS'
				conditionColumnName_task = 'TASK_ID'
				conditionColumnValue_task = task_id
				updatevalue_task = order
				
				dBop_update(tableName,columnName_task,conditionColumnName_task,conditionColumnValue_task,updatevalue_task)
				
				# LAST_UPDATED   TASKTABLE
				
				columnName_task = 'LAST_UPDATED'
				conditionColumnName_task = 'TASK_ID'
				conditionColumnValue_task = task_id
				updatevalue_task = datetime.datetime.now()
				update_op = dBop_update(tableName,columnName_task,conditionColumnName_task,conditionColumnValue_task,updatevalue_task)
						
				response_code = 0
				response_message = "Comment Added Successfully"
				dBop_update(update_tableName,columnName,conditionColumnName,conditionColumnValue,updatevalue)
			
		except Exception as error_addcomment:
			logging.debug("The error_addcomment Exception is %s %s"%(error_addcomment,type(error_addcomment)))
		
			response_code = 1
			response_message = "DataBase Internal Error"
		publish_handler(channel,response_code,response_message,details,result)
				
	else:
		# HERE SOME MESSAGE SHOULD BE THERE 
		response_code = 1
		if (session_expiry == True):
			response_message = "session Expired"
		else:
			response_message = "Already logged in"	
		publish_handler(channel,response_code,response_message,details,result)
		
'''****************************************************************************************
Function Name 	:	updateTaskStatus_handler  ( operation)
Description		:	Function which performs the updation of the Task Status
Parameters 		:   details      - Distionary contains Name of the user,userid,email,password
					channel      - channel used for communication between client and the server
					task_id      - Task identity number in the TaskTable
					update_state - open(0)/close(1) value of your choice either to close or open the Task 
****************************************************************************************'''


def updateTaskStatus_handler(details,channel,task_id,update_state):
	
	result = 0
	tableName = 'DASH6139.TASKTABLE'
	
	
	session_expiry = sessionexpirecheck_handler(details)
	loggedin = loggedincheck_handler(details)
	
	if (session_expiry == False and loggedin == True):
		select1 = 'USER_ID'
		select2 = ', TASK_STATUS'
		conditionColumnName = 'TASK_ID'
		conditionColumnValue = task_id
	
		try:
			owner_taskstatus = dBop_WhereFetch(tableName,select1,select2,conditionColumnName,conditionColumnValue)
			
			if owner_taskstatus == False:
				proceed = False

				logging.info('no task with the given taskid')
				response_code = 1
				response_message = "no task with the given taskid"
				publish_handler(channel,response_code,response_message,details,result)
				
			
			else:
				owner_id = int(owner_taskstatus['USER_ID'])#int(owner_taskstatus[0][1])
				task_status = int(owner_taskstatus['TASK_STATUS'])#int(owner_taskstatus[0][0])
				proceed = True
			
			if (proceed == True):	
				

				if (details['USER_ID'] == owner_id):
					if (int(task_status) != int(update_state)):
						tableName = 'DASH6139.TASKTABLE'
						columnName_task = 'TASK_STATUS'
						conditionColumnName_task = 'TASK_ID'
						conditionColumnValue_task = task_id
						updatevalue_task = update_state 
						update_op = dBop_update(tableName,columnName_task,conditionColumnName_task,conditionColumnValue_task,updatevalue_task)
				
						response_code = 0
						response_message = "Task_Status updated Successfully"
						
				
					else:
						response_code = 1
						response_message = "Task status is the same as you given"
						publish_handler(channel,response_code,response_message,details,result)
				
				else:
					response_code = 1
					response_message = "Give credentials doesnt have permission to do changes"
					publish_handler(channel,response_code,response_message,details,result)

				# LAST_ACTIVITY_TIME USERTABLE
				update_tableName = 'DASH6139.USERTABLE'
				columnName = 'LAST_ACTIVITY_TIME'
				conditionColumnName = 'USER_ID'
				conditionColumnValue = details['USER_ID']
				updatevalue = datetime.datetime.now()
				update_op = dBop_update(update_tableName,columnName,conditionColumnName,conditionColumnValue,updatevalue)
					
				
				# LAST_UPDATED   TASKTABLE
				
				columnName_task = 'LAST_UPDATED'
				conditionColumnName_task = 'TASK_ID'
				conditionColumnValue_task = task_id
				updatevalue_task = datetime.datetime.now()
				update_op = dBop_update(tableName,columnName_task,conditionColumnName_task,conditionColumnValue_task,updatevalue_task)
		except Exception as errro_updatestate:
			logging.debug("The errro_updatestate Exception is %s %s"%(errro_updatestate,type(errro_updatestate)))
			response_code = 1
			response_message = "DataBase Internal Error"

		publish_handler(channel,response_code,response_message,details,result)	
					
	else:
		# HERE SOME MESSAGE SHOULD BE THERE 
		response_code = 1
		if (session_expiry == True):
			response_message = "session Expired"
		else:
			response_message = "Already logged in"	
		publish_handler(channel,response_code,response_message,details,result)
				

			

'''****************************************************************************************
Function Name 	:	doLogin_handler  (App operation)
Description		:	Function which performs the login operation 
Parameters 		:   login   - client's email id
					passd   - client's password
					channel - channel used for communication between client and the server
****************************************************************************************'''



def doLogin_handler(login,passd,channel):
	result = 0 #For the publish handler
	global connection
	result = 0
	try:
		login_query = "SELECT * FROM DASH6139.USERTABLE WHERE EMAIL = \'"+login+"\' AND PASSWORD = \'"+passd+"\'"
		loginquery_stmt = ibm_db.exec_immediate(connection, login_query)
		dictionary = ibm_db.fetch_assoc(loginquery_stmt)
	except Exception as error_login:
		logging.debug("The error_login Exception is %s %s"%(error_login,type(error_login)))
 

	if (dictionary == False):
		details = {"EMAIL":login,"PASSWORD":passd}	
		response_code = 1
		response_message = "Login procedure Failed"
		publish_handler(channel,response_code,response_message,details,result)
		return False
	else:
		if (dictionary['USER_STATE']!=1):
			while dictionary!=False:
				response_code = 0
				response_message = "WELCOME %s "%(dictionary['DISPLAY_NAME'])
				details = {"DISPLAY_NAME":dictionary['DISPLAY_NAME'],"EMAIL":dictionary['EMAIL'],"USER_ID":dictionary['USER_ID'],"PASSWORD":dictionary['PASSWORD']}	
				publish_handler(channel,response_code,response_message,details,result)
				dictionary = ibm_db.fetch_assoc(loginquery_stmt)
				# HAVE TO UPDATE THE USERSTATE
			try:
				date = datetime.datetime.now()
				update_state = "UPDATE DASH6139.USERTABLE SET USER_STATE = '1',LAST_ACTIVITY_TIME=\'"+str(date)+"\'  WHERE EMAIL = \'"+login+"\' AND PASSWORD = \'"+passd+"\'"
				stmt = ibm_db.exec_immediate(connection, update_state)
				ibm_db.free_stmt(stmt)
			except Exception as error_updatestatedb:
				logging.debug("The error_updatestatedb Exception is %s %s"%(error_updatestatedb,type(error_updatestatedb)))
				
		else:
			response_code = 1
			response_message = "Logged in already with the given credentials"
			
			
			details = {"EMAIL":login,"PASSWORD":passd}	
			
			publish_handler(channel,response_code,response_message,details,result)
			

	ibm_db.free_stmt(loginquery_stmt)	

'''****************************************************************************************
Function Name 	:	doLogout_handler  (App operation)
Description		:	Function performs logout operation of the client
Parameters 		:   details  - Distionary contains Name of the user,userid,email,password
					channel  - channel used for communication between client and the server
****************************************************************************************'''


def doLogout_handler(details,channel):
	# FOR USER_STATE UPDATE
	update_tableName = 'DASH6139.USERTABLE'
	columnName = 'USER_STATE'
	conditionColumnName = 'USER_ID'
	conditionColumnValue = details['USER_ID']
	
	updatevalue_logout = 0
	updatevalue = datetime.datetime.now()

	# FOR LAST_ACTIVITY_TIME UPDATE
	columnName_timeupdate = 'LAST_ACTIVITY_TIME'
	
	
	session_expiry = sessionexpirecheck_handler(details)
	loggedin = loggedincheck_handler(details)
	
	result = 0

	if (loggedin == True):	

		try:
			update_op = dBop_update(update_tableName,columnName,conditionColumnName,conditionColumnValue,updatevalue_logout)
			response_code = 0
			response_message = "Successfully Logged out"
			dBop_update(update_tableName,columnName_timeupdate,conditionColumnName,conditionColumnValue,updatevalue)
		
		except Exception as error_logout:
			logging.debug("The error_logout Exception is %s %s"%(error_logout,type(error_logout)))
			
			response_code = 1
			response_message = "Oops Failed Logged out"
		
		publish_handler(channel,response_code,response_message,details,result)
	
	else:
		response_code = 1
		response_message = "Already Logged out"
		publish_handler(channel,response_code,response_message,details,result)

			



'''****************************************************************************************
Function Name 	:	updateTaskStatus_callback
Description		:	Callback function listens to the channel for the messages
Parameters 		:   message  - message from the client
					channel  - channel used for communication between client and the server
****************************************************************************************'''

def updateTaskStatus_callback(message,channel):
	if(message['type'] == "request"):
		updatestatus_return = updateTaskStatus_handler(message['details'],channel,message['task_id'],message['update_state'])

'''****************************************************************************************
Function Name 	:	addComment_callback
Description		:	Callback function listens to the channel for the messages
Parameters 		:   message  - message from the client
					channel  - channel used for communication between client and the server
****************************************************************************************'''
			
def addComment_callback(message,channel):
	if (message['type'] == 'request'):
		addcomment_return = addComment_handler(message['details'],channel,message['task_id'],message['comment'])		

'''****************************************************************************************
Function Name 	:	addNewTask_callback
Description		:	Callback function listens to the channel for the messages
Parameters 		:   message  - message from the client
					channel  - channel used for communication between client and the server
****************************************************************************************'''

def addNewTask_callback(message,channel):
	if (message['type'] == 'request'):
		addnewtask_return = addNewTask_handler(message['details'],channel,message['task_name'],message['task_desc'])

'''****************************************************************************************
Function Name 	:	getTaskList_callback
Description		:	Callback function listens to the channel for the messages
Parameters 		:   message  - message from the client
					channel  - channel used for communication between client and the server
****************************************************************************************'''

def getTaskList_callback(message,channel):
	if(message['type'] == "request"):
		gettasklist_return = getTaskList_handler(message['details'],channel)
'''****************************************************************************************
Function Name 	:	getTaskList_callback
Description		:	Callback function listens to the channel for the messages
Parameters 		:   message  - message from the client
					channel  - channel used for communication between client and the server
****************************************************************************************'''
def getTaskDetails_callback(message,channel):
	if(message['type'] == 'request'):
		gettaskdetails_return = getTaskDetails_handler(message['details'],channel,message['task_id'],message['start_index'])



'''****************************************************************************************
Function Name 	:	getAddressBook_callback
Description		:	Callback function listens to the channel for the messages
Parameters 		:   message  - message from the client
					channel  - channel used for communication between client and the server
****************************************************************************************'''

def getAddressBook_callback(message,channel):
	if(message['type'] == "request"):
		getaddressbook_return = getAddressBook_handler(message['details'],channel)


'''****************************************************************************************
Function Name 	:	doLogin_callback
Description		:	Callback function listens to the channel for the messages
Parameters 		:   message  - message from the client
					channel  - channel used for communication between client and the server
****************************************************************************************'''
def doLogin_callback(message,channel):
	if(message['type'] == "request"):
		userlogin_return = doLogin_handler(message['email'],message['password'],channel)

'''****************************************************************************************
Function Name 	:	doLogout_callback (callback operation)
Description		:	Callback function listens to the channel for the messages
Parameters 		:   message  - message from the client
					channel  - channel used for communication between client and the server
****************************************************************************************'''

def doLogout_callback(message,channel):
	if(message['type'] == "request"):
		dologout_return = doLogout_handler(message['details'],channel)


'''****************************************************************************************
Function Name 	:	error
Description		:	If error in the channel, prints the error
Parameters 		:	message - error message
****************************************************************************************'''
def error(message):
    logging.error("ERROR on Pubnub: " + str(message))

'''****************************************************************************************
Function Name 	:	connect
Description		:	Responds if server connects with pubnub
Parameters 		:	message - connect message
****************************************************************************************'''	
def connect(message):
	logging.info("CONNECTED")

'''****************************************************************************************
Function Name 	:	reconnect
Description		:	Responds if server reconnects with pubnub
Parameters 		:	message - reconnect message
****************************************************************************************'''	
def reconnect(message):
    logging.info("RECONNECTED")

'''****************************************************************************************
Function Name 	:	disconnect
Description		:	Responds if server disconnects from pubnub
Parameters 		:	message - disconnect message
****************************************************************************************'''
def disconnect(message):
     logging.info("DISCONNECTED")


'''****************************************************************************************
Function Name 	:  channel_subscriptions
Description		:  Function for subscribing to the channels	 
Parameters 		:  -
****************************************************************************************'''
def channel_subscriptions():
	global pubnub
	try:
		pubnub.subscribe(channels='doLogin', callback=doLogin_callback,error=error,
		connect=connect, reconnect=reconnect, disconnect=disconnect)
		pubnub.subscribe(channels='getAddressBook', callback=getAddressBook_callback,error=error,
		connect=connect, reconnect=reconnect, disconnect=disconnect)
		pubnub.subscribe(channels='getTaskList', callback=getTaskList_callback,error=error,
		connect=connect, reconnect=reconnect, disconnect=disconnect)
		pubnub.subscribe(channels='getTaskDetails', callback=getTaskDetails_callback,error=error,
		connect=connect, reconnect=reconnect, disconnect=disconnect)
		
		pubnub.subscribe(channels='addNewTask', callback=addNewTask_callback,error=error,
		connect=connect, reconnect=reconnect, disconnect=disconnect)
		pubnub.subscribe(channels='addComment', callback=addComment_callback,error=error,
		connect=connect, reconnect=reconnect, disconnect=disconnect)
		pubnub.subscribe(channels='doLogout', callback=doLogout_callback,error=error,
		connect=connect, reconnect=reconnect, disconnect=disconnect)
		pubnub.subscribe(channels='updateTaskStatus', callback=updateTaskStatus_callback,error=error,
		connect=connect, reconnect=reconnect, disconnect=disconnect)
				


	except Exception as channelsubserror:
		logging.debug("The channelsubserror is %s %s"%(channelsubserror,type(channelsubserror)))

'''****************************************************************************************
Function Name 	:	pub_Init
Description		:	Function to initiate the pubnub
Parameters 		:   - 
****************************************************************************************'''
			
def pub_Init():
	global pubnub
	# pubnub = Pubnub(publish_key=pub_key,subscribe_key=sub_key)

	try:
		pubnub = Pubnub(publish_key=pub_key,subscribe_key=sub_key) 
		return True
	except Exception as pubException:
		logging.debug("The pubException is %s %s"%(pubException,type(pubException)))
		# print "The pubException is %s %s"%(pubException,type(pubException))
		
		return False
'''****************************************************************************************
Function Name 	:	dashdB_Init
Description		:	Initalize the Database and establishing the connection between the 
					database and the kitchen-tracker
Parameters 		:	None
****************************************************************************************'''

def dashdB_Init():
	global connection,url
	dbtry = 0
	while(dbtry <3):
		try:
			if 'VCAP_SERVICES' in os.environ:
			    hasVcap = True
			    import json
			    vcap_services = json.loads(os.environ['VCAP_SERVICES'])
			    if 'dashDB' in vcap_services:
			        hasdashDB = True
			        service = vcap_services['dashDB'][0]
			        credentials = service["credentials"]
			        url = 'DATABASE=%s;uid=%s;pwd=%s;hostname=%s;port=%s;' % ( credentials["db"],credentials["username"],credentials["password"],credentials["host"],credentials["port"])
			    else:
			        hasdashDB = False
			  
			else:
			    hasVcap = False
			    url = 'DATABASE=%s;uid=%s;pwd=%s;hostname=%s;port=%s;' % ( "BLUDB","dash6139","ReD6Yta34IZ9","dashdb-entry-yp-dal09-07.services.dal.bluemix.net","50000")
   
			connection = ibm_db.connect(url, '', '')
			if (active(connection)):
				return connection
		except Exception as dberror:
			logging.debug("dberror Exception %s"%dberror)
			dbtry+=1
	return False




'''****************************************************************************************
Function Name 	:	Init  
Description		:	
Parameters 		:   -
****************************************************************************************'''

def Init():
	dBreturn = dashdB_Init()
	pbreturn = pub_Init()
	if (dBreturn == False or pbreturn == False):
		logging.error("Program Terminated")
		sys.exit()
	else:
		channel_subscriptions()	

if __name__ == '__main__':
	Init()
