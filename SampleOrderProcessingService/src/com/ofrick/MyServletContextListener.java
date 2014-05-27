package com.ofrick; 

import java.util.concurrent.*;
import javax.servlet.*;

public class MyServletContextListener implements ServletContextListener
  {
  private ServletContext context = null;

  /*This method is invoked when the Web Application has been removed 
  and is no longer able to accept requests
  */

  /**
   *
   * @param event
   */
  /** {@inheritDoc} */  //  @Override
  public void contextDestroyed(ServletContextEvent event)
  {
      //DBConnection.shutdown();
      //Output a simple message to the server's console
      System.out.println("Telalert backend is shutting down");
      this.context = null;

  }


  //This method is invoked when the Web Application
  //is ready to service requests

  /**
   *
   * @param event
  */
  /** {@inheritDoc} */  //  @Override
  public void contextInitialized(ServletContextEvent event)
  {
      this.context = event.getServletContext();

      //Output a simple message to the server's console
      System.out.println("Telalert backend is starting");

      ConcurrentLinkedQueue<TelalertEntry> jobQueue = new ConcurrentLinkedQueue<TelalertEntry>();
      this.context.setAttribute("jobQueue", jobQueue);

  }
}

