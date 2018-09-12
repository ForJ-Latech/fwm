1. Install eclipse & java jdk (at the time this was written java8 was the latest, this code is only gaurenteed to run on java 8...) 

TODO port to java 11 open jdk or something. QQ

1.1 you may also need openjfx (if not using Oracle Java) install that too. 
2. Enable polling (for ease of us) help -> preferences -> workspaces -> general -> enable polling
3. Make sure you have a maven integration so that it pulls the libraries that you need from the pom. 
4. Application will not startup unless it has been built using ant as configure - eclipse, it requires a resources thing.
