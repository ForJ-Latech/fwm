package com.forj.fwm.web;

import org.apache.log4j.Logger;
import org.eclipse.jetty.util.log.Log;

import com.google.gson.Gson;

public class JsonHelper {
	private static Logger log = Logger.getLogger(JsonHelper.class);
	
	private StringBuilder s = new StringBuilder();
	private Gson g = new Gson();

	public JsonHelper() {
		s.append("{");
	}
	
	public JsonHelper(String otherJsonHelperToString){
		s = new StringBuilder(otherJsonHelperToString);
		s.deleteCharAt(s.length() - 1); //remove our '}'
		s.append(",");
	}
	
	public void addAttribute(String key, Object attr) {
		s.append("\"" + key + "\":");
		if (attr == null) {
			s.append("null");
		} else {
			s.append(g.toJson(attr));
		}
		s.append(",");

	}
	
	public void addRawString(String key, String attr){
		s.append("\"" + key + "\":");
		if (attr == null) {
			s.append("null");
		} else {
			s.append(attr);
		}
		s.append(",");

	}

	public String getString() {
		String ret = "";
		if (s.lastIndexOf(",") > 1) {
			ret = s.substring(0, s.length() - 1);
		}
		ret += "}";
		return ret;
	}
	
	@Override
	public String toString(){
		return getString();
	}
}
