import ibm_db
import time
import os
from ibm_db import connect
from ibm_db import active
import datetime




def reset_Prog():
	global connection
	date = datetime.datetime.now()
	print date
	print '\n \tNOTE :  THIS PROGRAM WILL HELP YOU WHEN YOUR SESSION EXPIRED  AND YOU COULD NOT LOGIN \n\t AND WHEN YOU TERMINATE THE CLIENT.PY PROGRAM WITH LOGGING OUT(RESPECTIVE CLIENT)'
	print '\n\tSelect the client of your choice from the list below'
	passd = ['peter123','eric123','sam123','mark123']
	login = ['peter@sitename.com','eric@sitename.com','sam@sitename.com','mark@sitename.com']
	print login
	select = int(raw_input('\nenter the number between 0 to 3:\t'))

	if (select == 0):
		i = 0
		print 'you have selected the client %s'%(login[i])
	elif (select == 1):
		i = 1
		print 'you have selected the client %s'%(login[i])
	elif (select == 2):
		i = 2
		print 'you have selected the client %s'%(login[i])
	elif (select == 3):
		print 'you have selected the client %s'%(login[i])
		i = 3
	else:
		print 'wrong selection'
		pass

	tablename = 'DASH6139.USERTABLE'

	# for i in range(len(login)):
	update_state = "UPDATE "+tablename+" SET LAST_ACTIVITY_TIME = \'"+str(date)+"\' WHERE EMAIL = \'"+login[i]+"\' AND PASSWORD = \'"+passd[i]+"\'"
	stmt = ibm_db.exec_immediate(connection, update_state)
	ibm_db.free_stmt(stmt)
		
	# for i in range(0,len(login)):
	update_state = "UPDATE "+tablename+" SET USER_STATE = 0 WHERE EMAIL = \'"+login[i]+"\' AND PASSWORD = \'"+passd[i]+"\'"
	stmt = ibm_db.exec_immediate(connection, update_state)
	ibm_db.free_stmt(stmt)
	print 'done'	




def connectioncheck_handler():
	global connection,url
	if (active(connection) == False):
		connection = ibm_db.pconnect(url ,'' ,'')
	return None


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


def Init():
	dBreturn = dashdB_Init()
	if (dBreturn == False):
		logging.error("Program Terminated")
		sys.exit()
	else:
		reset_Prog()
		
if __name__ == '__main__':
	Init()	
