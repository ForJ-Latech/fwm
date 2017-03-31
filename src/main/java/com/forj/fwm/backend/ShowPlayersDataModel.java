package com.forj.fwm.backend;

import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.eclipse.jetty.util.log.Log;

/**
 * 
 * This is used to display objects to players.
 * Implements a LinkedList for traversal.
 * Will only hold the 10 most recent objects pushed.
 * 
 * @author jstrickland
 *
 */
public class ShowPlayersDataModel {
	public static class ShowData{
		private Object object;
		private Integer index;
		
		public Object getObject(){
			return object;
		}
		
		public Integer getNewIndex(){
			return index;
		}
		
		public ShowData(Object o, Integer index){
			this.object = o;
			this.index = index;
		}
	}
	
	private static Logger log = Logger.getLogger(ShowPlayersDataModel.class);
	
	private static LinkedList<Object> displayedObjects;
	private static Boolean started = false;
	
	public static void startConnector() {
		displayedObjects = new LinkedList<Object>();
		started = true;
	}
	
	public static void addOne(Object o) {
		displayedObjects.add(0, o);
//		 After 10 objects are shown, the most dated gets removed
		if(displayedObjects.size() > 10) {
			displayedObjects.removeLast();
		}
	}
	
	public static ShowData getDefault(){
		return trySetZero(new Integer(0));
	}
	
	public static Object peekFirst() {
		try {
			return displayedObjects.getFirst();
		} catch (Exception NoSuchElementException) {
			System.out.println("There are no objects to show players");
			NoSuchElementException.printStackTrace();
			return "Error";
		}
	}
	
	public static void deleteLast() {
		try {
			displayedObjects.removeLast();
		} catch (Exception NoSuchElementException) {
			log.error(NoSuchElementException);
		}
	}
	
	// This deletes everything in the list.
	public static void wipe() {
		displayedObjects.clear();
	}
	
	public static ShowData getPrevious(Integer index) {
		if(index + 1 >= 0 && index + 1 < displayedObjects.size()){
			log.debug("Increment called.");
			return new ShowData(displayedObjects.get(index + 1), index + 1); 
		}
		else
		{
			if(index >= 0 && index < displayedObjects.size()){
				return new ShowData(displayedObjects.get(index), index);
			}
			else 
			{
				return trySetZero(index);
			}
		}
	}
	
	public static ShowData getNext(Integer index) {
		if(index -1 >= 0 && index - 1 < displayedObjects.size()){
			log.debug("Decrement called.");
			return new ShowData(displayedObjects.get(index - 1), index - 1); 
		}
		else
		{
			if(index >= 0 && index < displayedObjects.size()){
				return new ShowData(displayedObjects.get(index), index);
			}
			else 
			{
				return trySetZero(index);
			}
		}
	}
	
	public static ShowData trySetZero(Integer index){
		if(displayedObjects.size() > 0){
			index = 0;
			return  new ShowData(displayedObjects.get(0), 0);
		}
		else
		{
			return null;
		}
	}
	
	public static Integer getIndex(Object o) {
		return displayedObjects.indexOf(o);
	}
	
	public static Object getObject(Object o) {
		return displayedObjects.get(displayedObjects.indexOf(o));
	}
	
	public static Boolean getStarted() {
		return started;
	}
	
}
