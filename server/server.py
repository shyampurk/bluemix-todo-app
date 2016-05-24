# IMPORTING THE REQUIRED MODULES
import ibm_db
import time
import os
import sys
from ibm_db import connect
from ibm_db import active
import datetime
from pubnub import Pubnub
import logging


# PUBNUB KEYS



pub_key = 'pub-c-913ab39c-d613-44b3-8622-2e56b8f5ea6d'
sub_key = 'sub-c-8ad89b4e-a95e-11e5-a65d-02ee2ddab7fe'

LOG_FILENAME = 'ToDoApp_logs.log'
logging.basicConfig(filename=LOG_FILENAME,level=logging.DEBUG,format='%(asctime)s, %(levelname)s, %(message)s', datefmt='%Y-%m-%d %H:%M:%S')


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
Function Name 	:	dBop_WhereFetchwithLimit  (dashdB operation)
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
			pbreturn = pubnub.publish(channel = channel ,message = {"type":"response","response_code":response_code,"response_message":response_message,"result":result,"details":details},error=error)
			
			if (pbreturn[0] == 1):
				return None
			elif(pbreturn[0] == 0):
				logging.error("The publish return error  %s for the Task %s for the client %s"%(pbreturn[1],channel,details['DISPLAY_NAME']))
				pbtry+=1
			else:
				pass
		except Exception as error_pdhandler:
			logging.error("The publish function Exception %s,%s for the Task %s for the client %s"%(error_pdhandler,type(error_pdhandler),channel,details['DISPLAY_NAME']))
			pbtry+=1
	

'''****************************************************************************************
Function Name 	:	sessionexpirecheck_handler  ( operation)
Description		:	Function which checks whether the user's session expired or not
Parameters 		:   details  -  Distionary contains Name of the user,userid,email,password
****************************************************************************************'''
def sessionexpirecheck_handler(details,channel):
	# SESSION EXPIRY CHECK
	logging.info("ENTERED SESSIONEXPIRECHECK_HANDLER FOR %s"%(str(details['DISPLAY_NAME'])))

	global connection
	tableName = 'DASH6139.USERTABLE'
	select = 'LAST_ACTIVITY_TIME'
	anotherselect = ''
	conditionColumnName = 'USER_ID'
	conditionColumnValue = details['USER_ID']
	
	sessionexpire_check = dBop_WhereFetch(tableName,select,anotherselect,conditionColumnName,conditionColumnValue)
	logging.debug("Performed dBop_WhereFetch operation in sessionexpire check for the client %s"%(details['DISPLAY_NAME']))
	
	lastactivitytime = sessionexpire_check['LAST_ACTIVITY_TIME']
	lastactivitytime = datetime.datetime.strptime(lastactivitytime, "%Y-%m-%d %H:%M:%S.%f")
	presenttime = datetime.datetime.utcnow()
	diff = presenttime-lastactivitytime
	diff_minutes = (diff.days * 24 * 60) + (diff.seconds/60)
	
	if (diff_minutes <=30):
		session_expiry = False
	else:
		logging.error("SESSION EXPIRED FOR THE CLIENT %s"%(details['DISPLAY_NAME']))
		session_expiry = True

		try:
			tableName_expire = 'DASH6139.USERTABLE'
			columnName_expire = 'USER_STATE'
			conditionColumnName_expire = 'USER_ID'
			conditionColumnValue_expire = details['USER_ID']
			updatevalue_expire = 0

			dBop_update(tableName_expire,columnName_expire,conditionColumnName_expire,conditionColumnValue_expire,updatevalue_expire)
			logging.debug("Performed dBop_update operation for client %s for the Task %s"%(details['DISPLAY_NAME'],channel))
		
		except Exception  as error_expirerecover_updatetask:
			logging.error("The error_expirerecover_updatetask exception is %s,%s -->CLIENT %s AT THE TIME %s"%(error_expirerecover_updatetask,type(error_expirerecover_updatetask),details['DISPLAY_NAME']))
	
	
	logging.info("EXITED SESSIONEXPIRECHECK_HANDLER FOR %s"%(str(details['DISPLAY_NAME'])))

	return session_expiry


'''****************************************************************************************
Function Name 	:	loggedincheck_handler  ( operation)
Description		:	Fucntion which performs the check whether the user loggedin or not
Parameters 		:   details  - Distionary contains Name of the user,userid,email,password
					
****************************************************************************************'''
def loggedincheck_handler(details):	
	global connection
	logging.info("ENTERED LOGGEDINCHECK_HANDLER FOR %s"%(str(details['DISPLAY_NAME'])))
	

    #LOGGED IN OR NOT CHECK
	tableName = 'DASH6139.USERTABLE'
	select = 'USER_STATE'
	anotherselect = ''
	conditionColumnName = 'USER_ID'
	conditionColumnValue = details['USER_ID']
	
	loggedin_check = dBop_WhereFetch(tableName,select,anotherselect,conditionColumnName,conditionColumnValue)
	logging.debug("Performed dBop_WhereFetch operation in login check for the client %s"%(details['DISPLAY_NAME']))
	
	user_state = loggedin_check['USER_STATE']
	if (int(user_state)!=0):
		loggedin = True
	else:
		logging.error("CLIENT %s NOT LOGGEDIN "%(details['DISPLAY_NAME']))
		
		loggedin = False
	logging.info("EXITED LOGGEDINCHECK_HANDLER FOR %s"%(str(details['DISPLAY_NAME'])))
	
	return loggedin	



'''****************************************************************************************
Function Name 	:	getAddressBook_handler  (App operation)
Description		:	Function which performs getting the Contacts list in the DataBase
Parameters 		:   details  - Distionary contains Name of the user,userid,email,password
					channel  - channel used for communication between client and the server
****************************************************************************************'''
def getAddressBook_handler(details,channel):
	global connection
	logging.info("ENTERED GETADDRESSBOOK_HANDLER FOR %s"%(str(details['DISPLAY_NAME'])))
	
	Address_book = []
	
	tableName = "DASH6139.USERTABLE"
	columnName = 'LAST_ACTIVITY_TIME'
	conditionColumnName = 'USER_ID'
	conditionColumnValue = details['USER_ID']
	updatevalue = datetime.datetime.utcnow()
	select = '*'

	session_expiry = sessionexpirecheck_handler(details,channel)
	loggedin = loggedincheck_handler(details)
	
	if (session_expiry == False and loggedin == True):
	
		try:
			Address_book = dBop_Fetchall(tableName,select)
			logging.debug("Performed dBop_Fetcahall operation for client %s for the Task %s"%(details['DISPLAY_NAME'],channel))
			response_code = 0
			response_message = "Fetching AddressBook operation performed successfully"
			dBop_update(tableName,columnName,conditionColumnName,conditionColumnValue,updatevalue)
			logging.debug("Performed dBop_update operation for client %s for the Task %s"%(details['DISPLAY_NAME'],channel))
			
		except Exception as error_fetchaddressbook:
			response_code = 1
			response_message = "DataBase Internal Error"
			logging.error("The error_fetchaddressbook exception  %s,%s -->CLIENT %s"%(error_fetchaddressbook,type(error_fetchaddressbook),details['DISPLAY_NAME']))
		
			
		publish_handler(channel,response_code,response_message,details,Address_book)
		logging.info("EXITED GETADDRESSBOOK_HANDLER FOR %s"%(str(details['DISPLAY_NAME'])))
			
	
	else:
		response_code = 1
		if (session_expiry == True):
			response_message = "session Expired"
			
		else:
			response_message = "Not Logged in"	
		publish_handler(channel,response_code,response_message,details,Address_book)
		logging.info("EXITED GETADDRESSBOOK_HANDLER FOR %s"%(str(details['DISPLAY_NAME'])))
				

'''****************************************************************************************
Function Name 	:	remove_duplicates()  (Internal App operation)
Description		:	Fucntion which deletes the duplicates values in a list
Parameters 		:   values  - A list contained some values
****************************************************************************************'''
def remove_duplicates(values):
    output = []
    seen = set()
    for value in values:
        # If value has not been encountered yet,
        # ... add it to both list and set.
        if value not in seen:
            output.append(value)
            seen.add(value)
    return output

'''****************************************************************************************
Function Name 	:	getTaskList_handler  (App operation)
Description		:	Fucntion which performs Getting All Tasks in the Database
Parameters 		:   details  - Distionary contains Name of the user,userid,email,password
					channel  - channel used for communication between client and the server
****************************************************************************************'''
def getTaskList_handler(details,channel):
	logging.info("ENTERED GETTASKLIST_HANDLER FOR %s"%(str(details['DISPLAY_NAME'])))
	
	tableName = "DASH6139.TASKTABLE"
	update_tableName = "DASH6139.USERTABLE"
	columnName = 'LAST_ACTIVITY_TIME'
	conditionColumnName = 'USER_ID'
	conditionColumnValue = details['USER_ID']
	updatevalue = datetime.datetime.utcnow()
	select = '*'

	Task_list = []
	session_expiry = sessionexpirecheck_handler(details,channel)
	loggedin = loggedincheck_handler(details)
	
	if (session_expiry == False and loggedin == True):
		try:	
			Task_list = dBop_Fetchall(tableName,select)
			logging.debug("Performed dBop_Fetcahall operation for client %s for the Task %s"%(details['DISPLAY_NAME'],channel))
			
			
			userid_list = []
			values = []
			for i in range(len(Task_list)):
				values.append(Task_list[i]['USER_ID'])
			
			userid_list = remove_duplicates(values)	
			username_list = []
	
			try:
				for j in range(len(userid_list)):
					tableName = 'USERTABLE'
					select1 = 'DISPLAY_NAME'
					select2 = ''
					conditionColumnName_task = 'USER_ID' 
					conditionColumnValue_task = userid_list[j]
					username_query = dBop_WhereFetch(update_tableName,select1,select2,conditionColumnName_task,conditionColumnValue_task)
					logging.debug("Performed dBop_WhereFetch operation for client %s for the Task %s"%(details['DISPLAY_NAME'],channel))
			
					username_list.append([userid_list[j],username_query['DISPLAY_NAME']])
			except Exception as error_dbwherfetchappop:
				logging.error("The error_dbwherfetchappop exception is %s,%s-->CLIENT %s "%(error_dbwherfetchappop,type(error_dbwherfetchappop),details['DISPLAY_NAME']))
		
			for k in range(len(username_list)):	
				for l in range(len(Task_list)):
					if (int(username_list[k][0]) == int(Task_list[l]['USER_ID'])):
						Task_list[l].update({'DISPLAY_NAME':username_list[k][1]})
			for m in range(len(Task_list)):		
				del Task_list[m]['USER_ID']
			
			response_code = 0
			response_message = "Fetching TaskList operation performed successfully"
			dBop_update(update_tableName,columnName,conditionColumnName,conditionColumnValue,updatevalue)
			logging.debug("Performed dBop_update operation for client %s for the Task %s"%(details['DISPLAY_NAME'],channel))
			
		except Exception as error_fetchtasklist:
			response_code = 1
			response_message = "DataBase Internal error"
			logging.error("The error_fetchtasklist Exception is %s,%s -->CLIENT %s"%(error_fetchtasklist,type(error_fetchtasklist),details['DISPLAY_NAME']))
			
		publish_handler(channel,response_code,response_message,details,Task_list)
		logging.info("EXITED GETTASKLIST_HANDLER FOR %s"%(str(details['DISPLAY_NAME'])))
	
	else:
		response_code = 1
		if (session_expiry == True):
			response_message = "session Expired"
		else:
			response_message = "Not Logged in"
		publish_handler(channel,response_code,response_message,details,Task_list)
		logging.info("EXITED GETTASKLIST_HANDLER FOR %s"%(str(details['DISPLAY_NAME'])))
					
'''****************************************************************************************
Function Name 	:	getTaskDetails_handler  (App operation)
Description		:	Function which performs Getting the specified Task Details
Parameters 		:   details     - Distionary contains Name of the user,userid,email,password
					channel     - channel used for communication between client and the server
					task_id     - Task identity number in the TaskTable
					start_index - The starting number from which the comments should be shown 
****************************************************************************************'''
def getTaskDetails_handler(details,channel,task_id,start_index):
	logging.info("ENTERED GETTASKDETAILS_HANDLER FOR %s"%(str(details['DISPLAY_NAME'])))
		
	tableName = "DASH6139.TASKTABLE"
	dateofcreate = datetime.datetime.utcnow()
	columnName = 'LAST_ACTIVITY_TIME'
	conditionColumnName = 'USER_ID'
	conditionColumnValue = details['USER_ID']
	updatevalue = dateofcreate

	# LAST_ACTIVITY_TIME UPDATE
	update_tableName = "DASH6139.USERTABLE"
	
	wherefetch_tableName = 'DASH6139.COMMENTTABLE'
	start_index = int(start_index) - 1
	end_index = int(start_index)+50
	
	Task_details = {}

	session_expiry = sessionexpirecheck_handler(details,channel)
	loggedin = loggedincheck_handler(details)
	if (session_expiry == False and loggedin == True):
		select1 = '*'
		select2 = ''
		conditionColumnName_task = 'TASK_ID'
		conditionColumnValue_task = task_id

		try:
			taskdetails = dBop_WhereFetch(tableName,select1,select2,conditionColumnName_task,conditionColumnValue_task)
			logging.debug("Performed dBop_WhereFetch operation for client %s for the Task %s"%(details['DISPLAY_NAME'],channel))
			
			
			# userid   = taskdetails['USER_ID']
			select1  = 'DISPLAY_NAME' 
			select2	 = ''
			conditionColumnName_task = 'USER_ID' 
			conditionColumnValue_task = taskdetails['USER_ID']

			username = dBop_WhereFetch(update_tableName,select1,select2,conditionColumnName_task,conditionColumnValue_task)
			logging.debug("Performed dBop_WhereFetch operation for client %s for the Task %s"%(details['DISPLAY_NAME'],channel))
			
			taskdetails.update({'DISPLAY_NAME':username['DISPLAY_NAME']})
			del taskdetails['USER_ID']
			
			select1 = '*'
			select2 = ''
			conditionColumnName_task = 'TASK_ID'
			conditionColumnValue_task = task_id
				
			commentdetails = dBop_WhereFetchwithLimit(wherefetch_tableName,select1,select2,conditionColumnName_task,conditionColumnValue_task,start_index,end_index)
			logging.debug("Performed dBop_WhereFetchwithLimit operation for client %s for the Task %s"%(details['DISPLAY_NAME'],channel))
			
			
			userid_list = []
			username_list = []
			
			for i in range(len(commentdetails)):
				userid_list.append(commentdetails[i]['USER_ID'])
			values = userid_list	
			if (len(userid_list)>1):
				userid_list = remove_duplicates(values)	

			
			try:
				for j in range(len(userid_list)):
					update_tableName = 'USERTABLE'
					select1 = 'DISPLAY_NAME'
					select2 = ''
					conditionColumnName_task = 'USER_ID' 
					conditionColumnValue_task = userid_list[j]
					username_query = dBop_WhereFetch(update_tableName,select1,select2,conditionColumnName_task,conditionColumnValue_task)
					logging.debug("Performed dBop_WhereFetch operation for client %s for the Task %s"%(details['DISPLAY_NAME'],channel))
			
					username_list.append([userid_list[j],username_query['DISPLAY_NAME']])
			except Exception as error_dbwherfetchappop:
				logging.error("oops! The error_dbwherfetchappop exception is %s,%s -->CLIENT %s "%(error_dbwherfetchappop,type(error_dbwherfetchappop),details['DISPLAY_NAME']))
		
			
			for k in range(len(username_list)):	
				for l in range(len(commentdetails)):
					if (int(username_list[k][0]) == int(commentdetails[l]['USER_ID'])):
						commentdetails[l].update({'DISPLAY_NAME':username_list[k][1]})
			
			for m in range(len(commentdetails)):		
				del commentdetails[m]['USER_ID']	
			

			Task_details = taskdetails
			
			Task_details.update({"comments":commentdetails})
			
			response_code = 0
			response_message = 'task details fetched'
			
			dBop_update(update_tableName,columnName,conditionColumnName,conditionColumnValue,updatevalue)
			logging.debug("Performed dBop_update operation for client %s for the Task %s"%(details['DISPLAY_NAME'],channel))
			
		except Exception as error_taskdetails:
			response_code = 1

			Task_details = []
			response_message = 'DataBase Internal Error'
			logging.error("The error_taskdetails Exception is %s,%s -->CLIENT %s"%(error_taskdetails,type(taskdetails),details['DISPLAY_NAME'],str(datetime.datetime.utcnow())))
		
		
		
		
		publish_handler(channel,response_code,response_message,details,Task_details)
		logging.info("EXITED GETTASKDETAILS_HANDLER FOR %s"%(str(details['DISPLAY_NAME'])))
	
	else:

		Task_details = []

		response_code = 1
		if (session_expiry == True):
			response_message = "session Expired"
			
		else:
			response_message = "Not logged in"	
		publish_handler(channel,response_code,response_message,details,Task_details)
		logging.info("EXITED GETTASKDETAILS_HANDLER FOR %s"%(str(details['DISPLAY_NAME'])))
					
'''****************************************************************************************
Function Name 	:	addNewTask_handler  (App operation)
Description		:	Function which performs adding new task to the Table
Parameters 		:   details  - Distionary contains Name of the user,userid,email,password
					channel  - channel used for communication between client and the server
					task_name - Task Name in the TaskTable
					task_desc - About the Task  Task identity number in the TaskTable
****************************************************************************************'''
def addNewTask_handler(details,channel,task_name,task_desc):
	logging.info("ENTERED ADDNEWTASK_HANDLER FOR %s"%(str(details['DISPLAY_NAME'])))
	
	status = 0
	dateofcreate = datetime.datetime.utcnow()
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
	
	
	session_expiry = sessionexpirecheck_handler(details,channel)
	loggedin = loggedincheck_handler(details)
	
	if (session_expiry == False and loggedin == True):	
		
		try:
			insert_op = dBop_addNewTaskinsert(task_name,owner_id,task_desc,dateofcreate,lastupdate,noofcomment,status)
			logging.debug("Performed dBop_addNewTaskinsert operation for client %s for the Task %s"%(details['DISPLAY_NAME'],channel))
			
			response_code = 0
			response_message = "New Task Added Successfully" 
			dBop_update(update_tableName,columnName,conditionColumnName,conditionColumnValue,updatevalue)
			logging.debug("Performed dBop_update operation for client %s for the Task %s"%(details['DISPLAY_NAME'],channel))
			
		except Exception as error_insertop:
			response_code = 1
			response_message = "DataBase Internal Error"
			logging.error("The error_insertop Exception is %s,%s -->CLIENT %s"%(error_insertop,type(error_insertop),details['DISPLAY_NAME']))
		
		
		publish_handler(channel,response_code,response_message,details,result)
		logging.info("EXITED ADDNEWTASK_HANDLER FOR %s"%(str(details['DISPLAY_NAME'])))
		
	else:
		response_code = 1
		if (session_expiry == True):
			response_message = "session Expired"
			
		else:
			response_message = "Not Logged in"	
		publish_handler(channel,response_code,response_message,details,result)
		logging.info("EXITED ADDNEWTASK_HANDLER FOR %s"%(str(details['DISPLAY_NAME'])))
					

'''****************************************************************************************
Function Name 	:	addComment_handler  (App operation)
Description		:	Function which performs adding comment to the particular choice
Parameters 		:   details  - Distionary contains Name of the user,userid,email,password
					channel  - channel used for communication between client and the server
					task_id  - Task identity number in the TaskTable
					comment  - comment that client wants to make for a task
****************************************************************************************'''
def addComment_handler(details,channel,task_id,comment):
	logging.info("ENTERED ADDCOMMENT_HANDLER FOR %s"%(str(details['DISPLAY_NAME'])))
		
	dateofcreate = datetime.datetime.utcnow()
	update_tableName = 'DASH6139.USERTABLE'
	tableName = 'DASH6139.TASKTABLE'
	columnName = 'LAST_ACTIVITY_TIME'
	conditionColumnName = 'USER_ID'
	conditionColumnValue = details['USER_ID']
	updatevalue = dateofcreate
	result = 0
	
	
	session_expiry = sessionexpirecheck_handler(details,channel)
	loggedin = loggedincheck_handler(details)
	
	if (session_expiry == False and loggedin == True):	
		try:
			select1 = 'TASK_STATUS'
			select2 = ''
			conditionColumnName_task = 'TASK_ID'
			conditionColumnValue_task = task_id

			task_status = dBop_WhereFetch(tableName,select1,select2,conditionColumnName_task,conditionColumnValue_task)
			logging.debug("Performed dBop_WhereFetch operation for client %s for the Task %s"%(details['DISPLAY_NAME'],channel))
			
			if(int(task_status['TASK_STATUS']) == 1):
				response_code = 1
				response_message = "Task status is closed"
					
			else:
				owner_id = details['USER_ID']
				select1 = 'NO_OF_COMMENTS'
				select2 = ''
				conditionColumnValue_task = 'TASK_ID'
				conditionColumnValue = task_id
				order = dBop_WhereFetch(tableName,select1,select2,conditionColumnValue_task,conditionColumnValue)
				logging.debug("Performed dBop_WhereFetch operation for client %s for the Task %s"%(details['DISPLAY_NAME'],channel))
			
				order = int(order['NO_OF_COMMENTS'])+1
					

				try:
					upload_query = "INSERT INTO DASH6139.COMMENTTABLE VALUES (\'"+str(comment)+"\',\'"+str(dateofcreate)+"\',\'"+str(task_id)+"\',\'"+str(owner_id)+"\',\'"+str(order)+"\')"
					logging.debug("Performed insert data operation for client %s for the Task %s"%(details['DISPLAY_NAME'],channel))
			
					stmt = ibm_db.exec_immediate(connection, upload_query)
					ibm_db.free_stmt(stmt)
				except Exception as error_uploadcomment:
					logging.error("The error_uploadcomment exception is %s,%s -->CLIENT %s"%(error_uploadcomment,details['DISPLAY_NAME']))
		
					
				# FOR NO.OF.COMMENTS UPDATE
				tableName = 'DASH6139.TASKTABLE'
				columnName_task = 'NO_OF_COMMENTS'
				conditionColumnName_task = 'TASK_ID'
				conditionColumnValue_task = task_id
				updatevalue_task = order
				
				dBop_update(tableName,columnName_task,conditionColumnName_task,conditionColumnValue_task,updatevalue_task)
				logging.debug("Performed dBop_update operation for client %s for the Task %s"%(details['DISPLAY_NAME'],channel))
			
				# LAST_UPDATED   TASKTABLE
				
				columnName_task = 'LAST_UPDATED'
				conditionColumnName_task = 'TASK_ID'
				conditionColumnValue_task = task_id
				updatevalue_task = datetime.datetime.utcnow()
				update_op = dBop_update(tableName,columnName_task,conditionColumnName_task,conditionColumnValue_task,updatevalue_task)
				logging.debug("Performed dBop_update operation for client %s for the Task %s"%(details['DISPLAY_NAME'],channel))
					
				response_code = 0
				response_message = "Comment Added Successfully"
				dBop_update(update_tableName,columnName,conditionColumnName,conditionColumnValue,updatevalue)
				logging.debug("Performed dBop_update operation for client %s for the Task %s"%(details['DISPLAY_NAME'],channel))
			
		except Exception as error_addcomment:
			response_code = 1
			response_message = "DataBase Internal Error"
			logging.error("The error_addcomment Exception is %s,%s -->CLIENT %s"%(error_addcomment,type(addComment),details['DISPLAY_NAME']))
		

		publish_handler(channel,response_code,response_message,details,result)
		logging.info("EXITED ADDCOMMENT_HANDLER FOR %s AT TIME %s",str(details['DISPLAY_NAME']),str(datetime.datetime.utcnow()))
				
	else:
		response_code = 1
		if (session_expiry == True):
			response_message = "session Expired"		
		else:
			response_message = "Not Logged in"	
		publish_handler(channel,response_code,response_message,details,result)
		logging.info("EXITED ADDCOMMENT_HANDLER FOR %s"%(str(details['DISPLAY_NAME'])))
		




'''****************************************************************************************
Function Name 	:	updateTaskStatus_handler  ( operation)
Description		:	Function which performs the updation of the Task Status
Parameters 		:   details      - Distionary contains Name of the user,userid,email,password
					channel      - channel used for communication between client and the server
					task_id      - Task identity number in the TaskTable
					update_state - open(0)/close(1) value of your choice either to close or open the Task 
****************************************************************************************'''
def updateTaskStatus_handler(details,channel,task_id,update_state):
	logging.info("ENTERED UPDATETASKSTATUS_HANDLER FOR %s"%(str(details['DISPLAY_NAME'])))
		
	result = 0
	tableName = 'DASH6139.TASKTABLE'
	
	
	session_expiry = sessionexpirecheck_handler(details,channel)
	loggedin = loggedincheck_handler(details)
	
	if (session_expiry == False and loggedin == True):
		select1 = 'USER_ID'
		select2 = ', TASK_STATUS'
		conditionColumnName = 'TASK_ID'
		conditionColumnValue = task_id
	
		try:
			owner_taskstatus = dBop_WhereFetch(tableName,select1,select2,conditionColumnName,conditionColumnValue)
			logging.debug("Performed dBop_WhereFetch operation for client %s for the Task %s"%(details['DISPLAY_NAME'],channel))
			
			
			owner_id = int(owner_taskstatus['USER_ID'])
			task_status = int(owner_taskstatus['TASK_STATUS'])
			proceed = True
		
			if (details['USER_ID'] == owner_id):
				if (int(task_status) != int(update_state)):
					tableName = 'DASH6139.TASKTABLE'
					columnName_task = 'TASK_STATUS'
					conditionColumnName_task = 'TASK_ID'
					conditionColumnValue_task = task_id
					updatevalue_task = update_state 
					update_op = dBop_update(tableName,columnName_task,conditionColumnName_task,conditionColumnValue_task,updatevalue_task)
					logging.debug("Performed dBop_update operation for client %s for the Task %s"%(details['DISPLAY_NAME'],channel))
			
					response_code = 0
					response_message = "Task_Status updated Successfully"
					
			
				else:
					response_code = 1
					response_message = "Task status is the same as you given"
					logging.warning("oops! %s -->CLIENT %s"%(response_message,details['DISPLAY_NAME']))
	
			else:
				response_code = 1
				response_message = "Given credentials doesnt have permission to do changes"
				logging.warning("oops! %s -->CLIENT %s"%(response_message,details['DISPLAY_NAME']))
	
			# LAST_ACTIVITY_TIME USERTABLE
			update_tableName = 'DASH6139.USERTABLE'
			columnName = 'LAST_ACTIVITY_TIME'
			conditionColumnName = 'USER_ID'
			conditionColumnValue = details['USER_ID']
			updatevalue = datetime.datetime.utcnow()
			update_op = dBop_update(update_tableName,columnName,conditionColumnName,conditionColumnValue,updatevalue)
			logging.debug("Performed dBop_update operation for client %s for the Task %s"%(details['DISPLAY_NAME'],channel))
				
			
			# LAST_UPDATED   TASKTABLE
			
			columnName_task = 'LAST_UPDATED'
			conditionColumnName_task = 'TASK_ID'
			conditionColumnValue_task = task_id
			updatevalue_task = datetime.datetime.utcnow()
			update_op = dBop_update(tableName,columnName_task,conditionColumnName_task,conditionColumnValue_task,updatevalue_task)
			logging.debug("Performed dBop_update operation for client %s for the Task %s"%(details['DISPLAY_NAME'],channel))
			
		except Exception as error_updatestate:
			response_code = 1
			response_message = "DataBase Internal Error"
			logging.error("The error_updatestate Exception is %s,%s -->CLIENT %s"%(error_updatestate,type(error_updatestate),details['DISPLAY_NAME']))
		
		
		publish_handler(channel,response_code,response_message,details,result)

		logging.info("EXITED UPDATETASKSTATUS_HANDLER FOR %s"%(str(details['DISPLAY_NAME'])))
					
	else:
		response_code = 1
		if (session_expiry == True):
			response_message = "session Expired"
		else:
			response_message = "Not Logged in"	
		publish_handler(channel,response_code,response_message,details,result)
		logging.info("EXITED UPDATETASKSTATUS_HANDLER FOR %s"%(str(details['DISPLAY_NAME'])))




'''****************************************************************************************
Function Name 	:	doLogin_handler  (App operation)
Description		:	Function which performs the login operation 
Parameters 		:   login   - client's email id
					passd   - client's password
					channel - channel used for communication between client and the server
****************************************************************************************'''
def doLogin_handler(login,passd,channel):
	logging.info("ENTERED DOLOGIN_HANDLER FOR %s"%(str(login)))
		
	result = 0 
	global connection
	result = 0
	details = {"EMAIL":login,"PASSWORD":passd}	
				
	try:
		login_query = "SELECT * FROM DASH6139.USERTABLE WHERE EMAIL = \'"+login+"\' AND PASSWORD = \'"+passd+"\'"
		loginquery_stmt = ibm_db.exec_immediate(connection, login_query)
		dictionary = ibm_db.fetch_assoc(loginquery_stmt)
		
		logging.debug("Performed login_query for client %s for the Task %s"%(login,channel))
		
		

		if (dictionary == False):
			response_code = 1
			response_message = "Login procedure Failed"
			logging.error("oops %s -->CLIENT %s"%(response_message,login))
				
		else:
			while dictionary!=False:
				details = {"DISPLAY_NAME":dictionary['DISPLAY_NAME'],"EMAIL":dictionary['EMAIL'],"USER_ID":dictionary['USER_ID']}	
				user_state = dictionary['USER_STATE']
				dictionary = ibm_db.fetch_assoc(loginquery_stmt)
				
			if (user_state!=1):
				response_code = 0
				response_message = "WELCOME %s "%(details['DISPLAY_NAME'])
					
				#UPDATING THE USERSTATE AND THE LAST_ACTIVITY_TIME FOR THE CLIENT 
				date = datetime.datetime.utcnow()
				update_state = "UPDATE DASH6139.USERTABLE SET USER_STATE = '1',LAST_ACTIVITY_TIME=\'"+str(date)+"\'  WHERE EMAIL = \'"+login+"\' AND PASSWORD = \'"+passd+"\'"
				stmt = ibm_db.exec_immediate(connection, update_state)
				ibm_db.free_stmt(stmt)
				logging.debug("Performed update operation for client %s for the Task %s"%(login,channel))
			
			else:
				response_code = 1
				response_message = "Logged in already with the given credentials"
				logging.warning("oops %s -->CLIENT %s"%(response_message,str(login)))
		
				
	except Exception as error_login:
		response_code = 1
		response_message = "Login procedure Failed"
		logging.error("The error_login Exception is %s,%s -->CLIENT %s"%(error_login,type(error_login),login))
	
	publish_handler(channel,response_code,response_message,details,result)
 


	logging.info("EXITED DOLOGIN_HANDLER FOR %s"%(str(login)))
			
	ibm_db.free_stmt(loginquery_stmt)
	
'''****************************************************************************************
Function Name 	:	doLogout_handler  (App operation)
Description		:	Function performs logout operation of the client
Parameters 		:   details  - Distionary contains Name of the user,userid,email,password
					channel  - channel used for communication between client and the server
****************************************************************************************'''
def doLogout_handler(details,channel):
	logging.info("ENTERED DOLOGOUT_HANDLER FOR %s"%(str(details['DISPLAY_NAME'])))
	
	# FOR USER_STATE UPDATE
	update_tableName = 'DASH6139.USERTABLE'
	columnName = 'USER_STATE'
	conditionColumnName = 'USER_ID'
	conditionColumnValue = details['USER_ID']
	
	updatevalue_logout = 0
	updatevalue = datetime.datetime.utcnow()

	# FOR LAST_ACTIVITY_TIME UPDATE
	columnName_timeupdate = 'LAST_ACTIVITY_TIME'
	
	
	session_expiry = sessionexpirecheck_handler(details,channel)
	loggedin = loggedincheck_handler(details)
	
	result = 0

	if (loggedin == True):	

		try:
			update_op = dBop_update(update_tableName,columnName,conditionColumnName,conditionColumnValue,updatevalue_logout)
			logging.debug("Performed dBop_update operation for client %s for the Task %s"%(details['DISPLAY_NAME'],channel))
			
			response_code = 0
			response_message = "Successfully Logged out"
			dBop_update(update_tableName,columnName_timeupdate,conditionColumnName,conditionColumnValue,updatevalue)
			logging.debug("Performed dBop_update operation for client %s for the Task %s"%(details['DISPLAY_NAME'],channel))
			
		except Exception as error_logout:
			logging.error("The error_logout exception is %s,%s -->CLIENT %s"%(error_logout,type(error_logout),details['DISPLAY_NAME']))
			response_code = 1
			response_message = "Oops Failed Logged out"
		
		publish_handler(channel,response_code,response_message,details,result)
		logging.info("EXITED DOLOGOUT_HANDLER FOR %s"%(str(details['DISPLAY_NAME'])))
	
	
	else:
		response_code = 1
		response_message = "Already Logged out"
		publish_handler(channel,response_code,response_message,details,result)
		logging.info("EXITED DOLOGOUT_HANDLER FOR %s"%(str(details['DISPLAY_NAME'])))


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
		logging.error("The channelsubserror is %s %s"%(channelsubserror,type(channelsubserror)))

'''****************************************************************************************
Function Name 	:	pub_Init
Description		:	Function to initiate the pubnub
Parameters 		:   - 
****************************************************************************************'''
			
def pub_Init():
	global pubnub
	
	try:
		pubnub = Pubnub(publish_key=pub_key,subscribe_key=sub_key) 
		return True
	except Exception as pubException:
		logging.error("The pubException is %s %s"%(pubException,type(pubException)))
		
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
			logging.error("dberror Exception %s"%dberror)
			dbtry+=1
	return False




'''****************************************************************************************
Function Name 	:	Init()
Description		:	Function which starts all the operations
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




