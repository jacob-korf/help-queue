###Max Hollfelder, Jacob Korf

##Start up:
Our help queue program has four Jar files that have to be run for the program to work:
	-HelpQueueAdministrator, HelpQueueClient, HelpeQueueDisplay, and HelpQueueServer
		-The first jar file that has to be run is HelpQueueServer. 
			-After the HelpQueueServer is running, you can run HelpQueueAdministrator, HelpQueueClient, or HelpQueueDisplay
			-Without a HelpQueueServer running, all other programs will fail gracefully and exit.

Everything is now running so you can do any of the following : 

##HelpQueueAdministrator:
	-The first screen that pops up is an authentication display.
		-Within this program, you must enter a username and password that correspond to values within the database
			-Currently you may use ‘username’ and ‘password’ for an easy login
			-In the future, each username and password will be user-specific
			-On a failure, you will be prompted to try again
			-On a success, a new display will open with the following components.
		-You can initialize or reset the help queue by clicking the green “Initialize/Reset help queue” button on the top.
		-This clears all help requests currently running and lets all the students know that the request was cleared.
		-You can cancel a workstation by entering the workstation name and clicking the green “Submit” button to the right of the text field. 
		-It must be the workstation name and not the preferred name.
		-You can add a class to the database. You need to fill in the following fields then click the green “Submit course button in the bottom right. 
			-Class course number 
			-Section number (Integer)
			-Start date ( Format: Month Day, Year. example: May 15, 2020)
			-End date (Same format as above)
			-Start time (Format: HH:MM ranging from 0 to 24)
			-End time (Same format as above)
			-Days of the week: 
				-Check the days that it will be held.
 
##HelpQueueDisplay: 
	-There is nothing that you have to do here, the display will just show the current running help request. 

##HelpQueueClient: 
	-The HelpQueueClient application has three buttons: 
	-The first button is the green “Submit Help Request” button, click this to submit a help request. 
		-Once clicked it will turn to grey and be disabled, also a note should appear at the bottom of the gui saying “Help Request successfully sent to queue”
	-The second button is “Cancel Help Request”, upon first startup it will be disabled and grey. When a help request is submitted it will enable and change to red. 
		-When it is red you can click it to cancel the help request, upon clicking the button it should go back to disabled and grey. A message should appear at the bottom saying “Help request successfully cancelled from queue”. 
	-The third button is to allow you to change your preferred name. Simply enter your preferred name and hit Submit. You should see your new preferred name at the top of the Client program.


