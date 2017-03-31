# Readme for configuring our app

## Ant
This application uses ant to build.


1) When configuring in eclipse show the 'ant' window
2) Then drag the build.xml into this window
3) drop down build.xml and you should see some options
4) double click to use that option
5) this app must be configured before running dist (which will make a jar file for you.)
6) you must run maven process-sources with id copy-jars before trying to ant dist.

## The configure folder
The configure folder currently takes things in both to /main/resources
then the selected environment, either prod or eclipse.

If something exists in both, and TARGET\_ENVIRONMENT, it will be overwritten by TARGET\_ENVIRONMENT.