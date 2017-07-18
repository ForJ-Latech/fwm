# Welcome to the Beta build of the Fantasy World Manager!

We hope that you enjoy using our software to realize your fantasy world!

## Important notes if you use an ssd

This app defaults to saving on every key stroke, so that you never need to hit ctrl + s. If you are using an SSD this is an obvious issue. Manual saving only can be enabled by clicking on world settings -> open world settings -> and then turning manual save only on. As soon as you change that setting it will be enabled in the entire application. When manual save only is enabled, ctrl + s and ctrl + shift + s are the only ways that you can save what you are currently viewing. ctrl + shift + s saves all open tabs, while ctrl + s saves only the one that you are currently on.  

## Application Features

* The application helps you manage your fantasy world.
* Npcs can be tracked; it helps you to remember their personality, their goals, and what they look like rapidly. 
* Gods have worshipers, regions that they infleunce, and groups that they belong to. 
* Regions have super and sub regions, so that you can go from the continent down to a specific city very quickly
* Groups contain npcs, gods, and a region that they exist in. 
* After you have created a couple of templates, you can quickly generate random NPCs that fit in your fantasy setting.
* Various NPC statblocks can be tracked easily, but are out of the way when you don't need them
* Default Statblocks so that you don't need to retype int, str, dex however you can make your statblocks for whatever setting you are dealing with. 
* It can show what you want to your players if you have another screen 
* Players can explore the world you have created by connecting to the webservice

## Running the application
__This application requires java to run.__ 

To run, execute: 'java -jar FWM-beta.jar', or double click on the .jar file if your environment supports that.

FWM-properties is where your application properties files are stored.

worlds/\*\* is where your world is stored (file ends in .db), and world properties will be stored (file ends in .properties).

lib/\*\* is nerd stuff, and is required in the same directory as fwm-*.jar. Just don't change it unless you know what you are doing. 

Change any hotkeys that are not to your liking in global settings -> edit hotkeys. 

## Application Improvements
* We originally wanted this application to be packaged into only one jar file, however there was some technical complexity that got in the way. We have since resolved this and getting the application into one jar file should only take around 5 hours, this might get done in the future. 

* We should make the Jetty Controller port be changeable by default. 

* The help section in the application is lacking

* This code will be hosted on github in the future probably at https://github.com/ForJ-Latech/fwm

##Reporting a bug
Please report / open issues here: https://github.com/ForJ-Latech/fwm/issues
If you just want to talk to a developer: https://discord.gg/FzEjsff
