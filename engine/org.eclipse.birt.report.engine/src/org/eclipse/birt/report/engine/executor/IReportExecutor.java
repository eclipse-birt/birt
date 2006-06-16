
package org.eclipse.birt.report.engine.executor;

import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.ir.MasterPageDesign;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

public interface IReportExecutor
{

	public IPageContent createPage(long pageNumber, MasterPageDesign pageDesign);
	
	public void execute( ReportDesignHandle reportDesign, IContentEmitter emitter );
	
	public IReportContent execute();

	/**
	 * close the executor, if the executor is closed, all sub executor will be
	 * termiante also.
	 */
	void close( );

	/**
	 * does the executor has child executor
	 * 
	 * @return
	 */
	boolean hasNextChild( );

	IReportItemExecutor getNextChild( );

}
