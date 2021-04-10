
package org.eclipse.birt.report.engine.extension;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;

public interface IReportItemExecutor {
	/**
	 * set extended report item model handle to the extension executor *
	 * 
	 * @param handle a handle to the extended item model object
	 */
	void setModelObject(Object handle);

	/**
	 * set executor context to the extension executor
	 * 
	 * @param context
	 */
	void setContext(IExecutorContext context);

	/**
	 * set parent report item executor
	 * 
	 * @param parent
	 */
	void setParent(IReportItemExecutor parent);

	/**
	 * get parent report item executor
	 */
	IReportItemExecutor getParent();

	/**
	 * get extended report item model handle
	 */
	Object getModelObject();

	/**
	 * get executor context
	 */
	IExecutorContext getContext();

	/**
	 * execute the report item
	 * 
	 * @throws BirtException
	 */
	IContent execute() throws BirtException;

	/**
	 * get QueryResults of the executor
	 */
	IBaseResultSet[] getQueryResults();

	/**
	 * get the content
	 */
	IContent getContent();

	/**
	 * does the executor has child executor
	 * 
	 * @return
	 * @throws BirtException
	 */
	boolean hasNextChild() throws BirtException;

	/**
	 * return the next child's executor
	 * 
	 * @throws BirtException
	 */
	IReportItemExecutor getNextChild() throws BirtException;

	/**
	 * close the executor, if the executor is closed, all sub executor will be
	 * terminate also.
	 * 
	 * @throws BirtException
	 */
	void close() throws BirtException;

}
