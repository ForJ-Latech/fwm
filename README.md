This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation version 3 of the License.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.


# Development Setup
This application is currently using java 1.8 with the latest version eclipse: 4.6.2
Get the version for java developers.
You'll also need to set up java fx, that includes scenebuilder and the plugin e(fx)clipse for javafx, eclipse development.

This application uses maven for library management.
This application uses ant for configuration stuff.

# Areas of Development

## Main GUI
See workingDocumentation/front-end-standards.txt

## Webservice
.jsp / .css / .png -> src/main/webapp/web-inf
.java controllers -> com.forj.fwm.web

## Backend 
The backend methods should be relatively easy to write. 
We were wrong about above assumption, however everything gets generated in Backend.init() for the database stuff, and it should remain static. 

## Communication between front and back-end
The primary communication between front and back-end should occur through static properties files
that are loaded some time in the application, these are done by the apache configurations class.


# Other Stuff
You may want to turn on "Refresh Automatically".
See Window->Preferences->General->Workspace->Refresh Automatically
and it'll monitor filesystem changes for you.

Need to get server to end with javaFX, else stray threads.
