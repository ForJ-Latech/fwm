package com.forj.fwm.web;

import java.net.InetAddress;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.*;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

import com.forj.fwm.gui.JettyController;
import com.forj.fwm.gui.MainController;

@Controller
public class HelloController{
	
	/**
	 * This is the way that the majority of our web pages should work.
	 * This returns the index page.
	 * @param modelMap
	 * @return
	 */
	// All views should be created with "HttpServletRequest request" as a second parameter for getting client IP
	@RequestMapping("/")
    public ModelAndView home(ModelMap modelMap, HttpServletRequest request)
    {
        return new ModelAndView("index");
    }
	
	@RequestMapping("/startwebservice1_0")
	public ModelAndView startWS1_0(ModelMap modelMap, HttpServletRequest request)
	{
		promptPassword(request);
		return new ModelAndView("redirect:/webservice1_0bs");
	}
	
	@RequestMapping("/startwebservice1_5")
	public ModelAndView startWS1_5(ModelMap modelMap, HttpServletRequest request)
	{
		promptPassword(request);
		return new ModelAndView("redirect:/webservice1_5");
	}
	
	/**
	 * This is the way that ajax calls will need to work.
	 * '@ResponseBody' is pretty cool maybe?
	 * 
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/test")
	@ResponseBody
	public String test(ModelMap modelMap, HttpServletRequest request)
    {
		promptPassword(request);
		return "[0, 1, 2, 3]";
    }
	
	/* I will likely show a custom view prompting for a password if not "logged in" every time a view is accessed.
	 * If the password matches the password saved in the JettyController, will add client IP to a volatile list of
	 * IP's that are "logged in", at which point the password prompt will cease to be shown for clients with IP's
	 * contained within that array. If there's a better way let me know. - Ryan
	 */
	public static void promptPassword(HttpServletRequest request){
		String enteredPasswordTest = "password123";
		String ip = request.getRemoteAddr();  // client IP
		if (!JettyController.getIsLoggedIn(ip)) {
			System.out.println("show password prompt");
			JettyController.logIn(enteredPasswordTest, ip);
			System.out.println("pretend we just entered the password hard-coded in HelloController");
		} else {
			System.out.println("already logged in");
		}
	}
}