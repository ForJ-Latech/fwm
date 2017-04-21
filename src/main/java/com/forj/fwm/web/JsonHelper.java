package com.forj.fwm.web;

import com.google.gson.Gson;

public class JsonHelper {
	private StringBuilder s = new StringBuilder();
	private Gson g = new Gson();

	public JsonHelper() {
		s.append("{");
	}

	public void addAttribute(String key, Object attr) {
		s.append("\"" + key + "\":");
		if (attr == null) {
			s.append("undefined");
		} else {
			s.append(g.toJson(attr));
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
}
