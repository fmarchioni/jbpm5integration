package com.sample;

import java.io.IOException;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.drools.SystemEventListenerFactory;
 
import org.jbpm.task.service.TaskService;
import org.jbpm.task.service.mina.MinaTaskServer;

 
//import org.jbpm.test.JBPMHelper;

/**
 * Servlet implementation class StartupServlet
 */
@WebServlet(value="/StartupServlet", loadOnStartup=1)

public class StartupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
 
	public void init(ServletConfig config) throws ServletException
    {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("org.jbpm.task");

		TaskService taskService = new TaskService(emf, SystemEventListenerFactory.getSystemEventListener());

		MinaTaskServer server = new MinaTaskServer( taskService );

		Thread thread = new Thread( server );

		thread.start();

		System.out.println("Started task service!");
    }
    public StartupServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
