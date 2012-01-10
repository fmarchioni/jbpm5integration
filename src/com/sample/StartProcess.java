package com.sample;

import java.io.IOException;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.impl.EnvironmentFactory;
import org.drools.io.ResourceFactory;
import org.drools.persistence.jpa.JPAKnowledgeService;
import org.drools.runtime.Environment;
import org.drools.runtime.EnvironmentName;
import org.drools.runtime.KnowledgeSessionConfiguration;
import org.drools.runtime.StatefulKnowledgeSession;
import org.jbpm.process.audit.JPAProcessInstanceDbLog;
import org.jbpm.process.audit.JPAWorkingMemoryDbLogger;
import org.jbpm.process.workitem.wsht.WSHumanTaskHandler;
 

/**
 * Servlet implementation class StartProcess
 */
@WebServlet("/StartProcess")
public class StartProcess extends HttpServlet {
	private static final long serialVersionUID = 1L;
	StatefulKnowledgeSession ksession = null;
	KnowledgeBase kbase = null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StartProcess() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			kbase = readKnowledgeBase();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("org.jbpm.persistence.jpa");
        
        /*
    	 * Create the knowledge session that uses JPA to persists runtime state
    	 */
        StatefulKnowledgeSession ksession = createKnowledgeSession(kbase,emf);
        ksession.getWorkItemManager().registerWorkItemHandler("Human Task", new WSHumanTaskHandler());
		ksession.startProcess("com.sample.bpmn.hello");
		System.out.println("Process started ...");
	}
	
	public static  StatefulKnowledgeSession createKnowledgeSession(KnowledgeBase kbase,EntityManagerFactory emf) {
	    StatefulKnowledgeSession result;
        final KnowledgeSessionConfiguration conf = KnowledgeBaseFactory.newKnowledgeSessionConfiguration();
	   	
		    Environment env = createEnvironment(emf);
		    result = JPAKnowledgeService.newStatefulKnowledgeSession(kbase, conf, env);
		    new JPAWorkingMemoryDbLogger(result);
		   
		    	JPAProcessInstanceDbLog log = new JPAProcessInstanceDbLog(result.getEnvironment());
		    
		    
		
		return result;
	}
	protected static Environment createEnvironment(EntityManagerFactory emf) { 
        Environment env = EnvironmentFactory.newEnvironment();
         env.set(EnvironmentName.ENTITY_MANAGER_FACTORY, emf);
       // env.set(EnvironmentName.TRANSACTION_MANAGER, TransactionManagerServices.getTransactionManager());
        return env;
    }
	private static KnowledgeBase readKnowledgeBase() throws Exception {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource("sample.bpmn"), ResourceType.BPMN2);
		return kbuilder.newKnowledgeBase();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
