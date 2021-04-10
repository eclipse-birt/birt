
package org.eclipse.birt.report.engine.executor;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.ir.MasterPageDesign;

public interface IReportExecutor {

	public IReportItemExecutor createPageExecutor(long pageNumber, MasterPageDesign pageDesign) throws BirtException;

	public IReportContent execute() throws BirtException;

	/**
	 * close the executor, if the executor is closed, all sub executor will be
	 * termiante also.
	 * 
	 * @throws BirtException
	 */
	void close() throws BirtException;

	/**
	 * does the executor has child executor
	 * 
	 * @return
	 * @throws BirtException
	 */
	boolean hasNextChild() throws BirtException;

	IReportItemExecutor getNextChild() throws BirtException;

}
