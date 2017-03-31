# Guide to setting up SVN

#REPO has been modified, location has changed, and you need to drill down into trunk.

## 1) Have eclipse

## 2) go to help 
	-> install software 
	-> select the default site
	-> type svn
	-> install 'collaboration'
	
## 3) eclipse will ask you to restart, do so

## 6) go to window
	-> show view
	-> other
	-> svn
	-> svn repository browser

## 7) right click inside window, add new repository
	the repository url should be : 
	'http://awesomepossums.duckdns.org:6160/svn/forj'
	set user name & password (also save them)
	these were given in the group me.

## 8) then, inside of package explorer, right click
	-> import
	-> from svn
	-> select repository you just created
	-> drill down into fwm/trunk/fwm
	-> head revision
	-> IMPORTANT drill down from into fwm/trunk/fwm and select this.
	
# Using SVN
on right click of project in package explorer
	team -> synchronize with repository before: (this will show you if there are any conflicts that you created, so that you can be warned.)
		team -> commit -> commits things
	update -> checks out things
	
## BE sure to leave a commit message.	
	conflicts suck, try not to cause them.