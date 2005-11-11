package org.eclipse.birt.report.engine.api;


/**
 * An engine task that runs a report and generates a report document. 
 */
public interface IRunTask extends IEngineTask {

	/**
	  * set up event handler to be called after each page is generated
	  * 
	  * @param callback a callback function that is called after each
	  * checkpoint 
	  */
	public void setPageHandler(IPageHandler callback);
	
	/**
	  * runs the task to generate report document
	  * @param manager an interface for writing to / reading from disk when 
	  * genrating report document
	  *  @param reportDocArchiveName the name for the report document file
      * @throws EngineException throws exception when running report fails
      */
	public abstract void run(IReportDocManager manager,  String reportDocName) throws EngineException;
}
