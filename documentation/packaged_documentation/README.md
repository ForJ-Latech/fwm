#Welcome to the ALPHA build of the Fantasy World Manager!

We hope that you enjoy using our software to help realize your fantasy world!

##Important notes if you use an ssd

This app defaults to saving on every key stroke, so that you never need to hit ctrl + s. If you are using an SSD this is an obvious issue. Manual saving only can be enabled by clicking on world settings -> open world settings -> and then turning manual save only on. As soon as you change that setting it will be enabled in the entire application. When manual save only is enabled, ctrl + s and ctrl + shift + s are the only ways that you can save what you are currently viewing. ctrl + shift + s saves all open tabs, while ctrl + s saves only the one that you are currently on.  

## Running the application
__THIS APPLICATION REQUIRES A JRE OF VERSION 1.5 OR ABOVE TO RUN.__ 

__IF YOU DONT HAVE A JDK THEN THE WEBSERVICE WILL NOT WORK.__ 

To run, execute: 'java -jar fwm-alpha.jar', or double click on the .jar file if your environment supports that.

FWM-properties is where your application properties files will be stored.

worlds/\*\* is where the your world folders, and world properties will be stored.

lib/\*\* is nerd stuff, and is required in the same directory as fwm-alpha.jar. Just don't change it unless you know what you are doing. 

Change any hotkeys that are not to your liking in global settings -> edit hotkeys. 

##Implemented Features

* The application helps you manage your fantasy world.
    * Npcs can be tracked; it helps you to remember their personality, their goals, and what they look like rapidly. 
    * Gods have worshipers, regions that they reside over, and groups that they belong to. 
    * Regions have super and sub regions, so that you can go from the continent down to a specific city very quickly
    * Groups contain npcs, gods, and a region that they exist in. 

* It can show objects to your players if you have another screen 

* If you have a full JDK it can show objects to your players through another computer


##Planned features
* We will allow players to explore the world through the webservice. Your players will only be able to see objects that you have previously shown, unless you'd rather show them everything.

* We will have super and sub regions show npcs, groups, and gods that affect regions directly related to them.

*  We will implement any of the functionality that does not currently work on the menu bar. 

* We will implement a template for npcs, so that you can generate new npcs on the fly based on a template that you have previously saved

* We will implement default statblocks so that you can quickly add the relevant information into your world without needing to copy and paste frequently. 

* We will enable greater search functionality so that you can find items that you forgot, or just view all the things that you have created of the same type, and see what you most recently edited. 

* Super regions, a group's region, and a npc's god, currently are not easily accessible. 

* Groups do not yet contain multiple stat blocks, for various mob types. 


##Known bugs
* Super regions can be added into sub region's sub regions, causing a circle. But it doesn't break anything.

##Reporting a bug
Email any problems to james(dot)ehlmann{at}gmail[dot]com
