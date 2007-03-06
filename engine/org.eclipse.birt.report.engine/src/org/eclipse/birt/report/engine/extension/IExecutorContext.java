package org.eclipse.birt.report.engine.extension;

import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.data.IResultSet;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;


public interface IExecutorContext extends IReportContext
{
	/**
	 * get the class loader used to load user defined classes.
	 */
	ClassLoader getApplicationClassLoader( );
	
	/**
	 * create IReportItemExecutor of handle, the extendedItem is child.
	 */
	IReportItemExecutor createExecutor( IReportItemExecutor parent, Object handle );

	/**
	 * return IReportContent. User can use it to create content for
	 * extendedItem.
	 */
	IReportContent getReportContent( );

	/**
	 * execute query
	 */
	IResultSet executeQuery( IResultSet parent, IBaseQueryDefinition query );
	
	/**
	 * get the queries of the handle
	 * @param handle
	 * @return queries
	 */
	IBaseQueryDefinition[] getQueries( Object handle );
}
