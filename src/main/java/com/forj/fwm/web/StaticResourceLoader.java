package com.forj.fwm.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.forj.fwm.conf.AppConfig;

@Controller
public class StaticResourceLoader{
	
	@RequestMapping("/node/*")
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception{
		if (AppConfig.getProd()) {
			response.sendRedirect(request.getRequestURL().toString().replace("node", "node_resources"));
		} else {
			// redirect to running node server
			response.sendRedirect(request.getRequestURL().toString().replace("8080", "8081"));
		}
	}
}