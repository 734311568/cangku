package net.codejava.spring.controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

	@RequestMapping(value="/")
	public ModelAndView goHome(HttpServletResponse response) throws IOException{
		return new ModelAndView("home");
	}
	
	@RequestMapping(value="/viewXSLT")
	public ModelAndView viewXSLT(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		// builds absolute path of the XML file
		String xmlFile = "resources/citizens.xml";
		String contextPath = request.getServletContext().getRealPath("");
		String xmlFilePath = contextPath + File.separator + xmlFile;
		
		Source source = new StreamSource(new File(xmlFilePath)); 

		// adds the XML source file to the model so the XsltView can detect
		ModelAndView model = new ModelAndView("XSLTView");
		model.addObject("xmlSource", source);
		
		return model;
	}
}