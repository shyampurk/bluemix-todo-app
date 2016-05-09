from pubnub import Pubnub
import uuid
import os
import ibm_db
import time

from ibm_db import connect
from ibm_db import active
pub_key = 'pub-c-913ab39c-d613-44b3-8622-2e56b8f5ea6d'
sub_key = 'sub-c-8ad89b4e-a95e-11e5-a65d-02ee2ddab7fe'

'''****************************************************************************************
Function Name 	:	error
Description		:	If error in the channel, prints the error
Parameters 		:	message - error message
****************************************************************************************'''
def error(message):
    print("ERROR : " + str(message))
'''****************************************************************************************
Function Name 	:	connect
Description		:	Responds if server connects with pubnub
Parameters 		:	message - connect message
****************************************************************************************'''	
def connect(message):
	print("CONNECTED")
'''****************************************************************************************
Function Name 	:	reconnect
Description		:	Responds if server reconnects with pubnub
Parameters 		:	message - reconnect message
****************************************************************************************'''	
def reconnect(message):
    print("RECONNECTED")
'''****************************************************************************************
Function Name 	:	disconnect
Description		:	Responds if server disconnects from pubnub
Parameters 		:	message - disconnect message
****************************************************************************************'''
def disconnect(message):
    print("DISCONNECTED")

def relogin_initiation():
	global email,password
	print '\nenter the details to relogin\n'
	email = raw_input('enter the email:\t')
	password  = raw_input('\nenter the password\t')
	pubnub.publish(channel = 'doLogin',message = {"type":"request","email":email,"password":password},error=error)


def ops(message):

	print "0)logOut \n 1)getAddressBook \n 2)getTaskList\n 3) addNewTask\n 4)addComment\n 5)updateTaskStatus \n 6)getTaskDetails"
	method = int(raw_input('what do you want to do-->'))
	if (method == 0): 
		pubnub.publish(channel = 'doLogout',message = {"type":"request","details":message['details']},error=error)
	
	elif(method == 1):	
		pubnub.publish(channel = 'getAddressBook',message = {"type":"request","details":message['details']},error=error)
	
	elif(method == 2):	
		pubnub.publish(channel = 'getTaskList',message = {"type":"request","details":message['details']},error=error)
	
	elif(method == 3):
		task_name = raw_input('enter the task_name \t')
		task_desc = raw_input('enter the task_desc \t')	
		pubnub.publish(channel = 'addNewTask',message = {"type":"request","details":message['details'],"task_name":task_name,"task_desc":task_desc},error=error)
	elif(method == 4):
		task_id = raw_input('enter the task_id\t')
		comment = raw_input('enter the comment\t')	
		pubnub.publish(channel = 'addComment',message = {"type":"request","details":message['details'],"task_id":task_id,"comment":comment},error=error)
	elif(method == 5):
		task_id = raw_input('enter the task_id\t')
		update_state = raw_input('enter the state\t')	
		pubnub.publish(channel = 'updateTaskStatus',message = {"type":"request","details":message['details'],"task_id":task_id,"update_state":update_state},error=error)
	elif(method == 6):
		task_id = raw_input('enter the task_id\t')
		start_index = raw_input('enter the start_index\t')
		pubnub.publish(channel = 'getTaskDetails',message = {"type":"request","details":message['details'],"task_id":task_id,"start_index":start_index},error=error)
	else:
		pass

	
def doLogin_callback(message,channel):
	if (message['type'] == "response" and message['response_code'] == 0):
		if str(message['details']['EMAIL']) == str(email):
			print "client",message['details']['EMAIL']
			print 'client',message['response_message']
			ops(message)
	elif (message['type'] == "response" and message['response_code'] == 1):
		print message['response_message']
			
	else:
		pass	 
		
def getAddressBook_callback(message,channel):
	if (message['type'] == "response" and message['response_code'] == 0):
		if str(message['details']['EMAIL']) == str(email):
			print 'client',message['details']['EMAIL']
			print 'client',message['response_message']
			print 'client',message['result']
			ops(message)
	
	elif (message['type'] == "response" and message['response_code'] == 1):
		print message['response_message']
	else:
		pass	

def getTaskList_callback(message,channel):
	if (message['type'] == "response" and message['response_code'] == 0):
		if str(message['details']['EMAIL']) == str(email):
			print 'client',message['details']['EMAIL']
			print 'client',message['response_message']
			print 'client',message['result']
			ops(message)
	elif (message['type'] == "response" and message['response_code'] == 1):
		print message['response_message']
	else:
		pass	
def getTaskDetails_callback(message,channel):
	if (message['type'] == "response" and message['response_code'] == 0):
		if str(message['details']['EMAIL']) == str(email):
			print 'client',message['details']['EMAIL']
			print 'client',message['response_message']
			print 'client',message['result']
			ops(message)
	elif (message['type'] == "response" and message['response_code'] == 1):
		print message['response_message']
	else:
		pass

def addNewTask_callback(message,channel):
	if (message['type'] == "response" and message['response_code'] == 0):
		if str(message['details']['EMAIL']) == str(email):
			print 'client',message['details']['EMAIL']
			print 'client',message['response_message']
			ops(message)
	elif (message['type'] == "response" and message['response_code'] == 1):
		print message['response_message']
	else:
		pass
def addComment_callback(message,channel):
	if (message['type'] == "response" and message['response_code'] == 0):
		if str(message['details']['EMAIL']) == str(email):
			print 'client',message['details']['EMAIL']
			print 'client',message['response_message']
			ops(message)
	elif (message['type'] == "response" and message['response_code'] == 1):
		print message['response_message']
	else:
		pass

def doLogout_callback(message,channel):
	if (message['type'] == "response" and message['response_code'] == 0):
		if str(message['details']['EMAIL']) == str(email):
			print 'client',message['details']['EMAIL']
			print 'client',message['response_message']
			time.sleep(5)
			relogin_initiation()

			
	elif (message['type'] == "response" and message['response_code'] == 1):
		print message['response_message'],'here'
	else:
		pass

def updateTaskStatus_callback(message,channel):
	if (message['type'] == "response" and message['response_code'] == 0):
		if str(message['details']['EMAIL']) == str(email):
			print 'client',message['details']['EMAIL']
			print 'client',message['response_message']
			ops(message)
			
	elif (message['type'] == "response" and message['response_code'] == 1):
		print message['response_message']
	else:
		pass



def login_initiation():
	global email,password
	print '\nenter the details to login\n'
	email = raw_input('enter the email:\t')
	password  = raw_input('\nenter the password\t')
	pub_Init()
	pubnub.publish(channel = 'doLogin',message = {"type":"request","email":email,"password":password},error=error)


	
def pub_Init():
	global pubnub
	try:
		pubnub = Pubnub(publish_key=pub_key,subscribe_key=sub_key) 
		pubnub.subscribe(channels='doLogin', callback=doLogin_callback,error=error,
		connect=connect, reconnect=reconnect, disconnect=disconnect)
		pubnub.subscribe(channels='getAddressBook', callback=getAddressBook_callback,error=error,
		connect=connect, reconnect=reconnect, disconnect=disconnect)
		pubnub.subscribe(channels='getTaskList', callback=getTaskList_callback,error=error,
		connect=connect, reconnect=reconnect, disconnect=disconnect)
		pubnub.subscribe(channels='addNewTask', callback=addNewTask_callback,error=error,
		connect=connect, reconnect=reconnect, disconnect=disconnect)
		pubnub.subscribe(channels='addComment', callback=addComment_callback,error=error,
		connect=connect, reconnect=reconnect, disconnect=disconnect)
		pubnub.subscribe(channels='doLogout', callback=doLogout_callback,error=error,
		connect=connect, reconnect=reconnect, disconnect=disconnect)
		pubnub.subscribe(channels='updateTaskStatus', callback=updateTaskStatus_callback,error=error,
		connect=connect, reconnect=reconnect, disconnect=disconnect)
		pubnub.subscribe(channels='getTaskDetails', callback=getTaskDetails_callback,error=error,
		connect=connect, reconnect=reconnect, disconnect=disconnect)
		
		
	except Exception as pubException:
		print "The pubException is %s %s"%(pubException,type(pubException))
		
if __name__ == '__main__':
	login_initiation()