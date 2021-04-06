/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.executor;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.report.engine.api.IProgressMonitor;
import org.eclipse.birt.report.engine.data.dte.BlankResultSet;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.IQueryResultSet;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;

abstract public class QueryItemExecutor extends StyledItemExecutor {
	protected boolean rsetEmpty;

	protected QueryItemExecutor(ExecutorManager manager, int type) {
		super(manager, type);
	}

	/**
	 * close dataset if the dataset is not null:
	 * <p>
	 * <ul>
	 * <li>close the dataset.
	 * <li>exit current script scope.
	 * </ul>
	 * 
	 * @param ds the dataset object, null is valid
	 */
	protected void closeQuery() {
		if (rset != null) {
			rset.close();
			rset = null;
			context.setResultSets(parentRsets);
		}
	}

	/**
	 * register dataset of this item.
	 * <p>
	 * if dataset design of this item is not null, create a new <code>DataSet</code>
	 * object by the dataset design. open the dataset, move cursor to the first
	 * record , register the first row to script context, and return this
	 * <code>DataSet</code> object if dataset design is null, or open error, or
	 * empty resultset, return null.
	 * 
	 * @param item the report item design
	 * @return the DataSet object if not null, else return null
	 */
	protected void executeQuery() {
		rset = null;
		boolean useCache = design.useCachedResult();
		IDataQueryDefinition query = design.getQuery();
		IBaseResultSet parentRset = getParentResultSet();
		context.setResultSet(parentRset);
		if (query != null) {
			try {
				context.getProgressMonitor().onProgress(IProgressMonitor.START_QUERY, (int) design.getID());

				rset = (IQueryResultSet) context.executeQuery(parentRset, query, design.getHandle(), useCache);
				context.setResultSet(rset);

				context.getProgressMonitor().onProgress(IProgressMonitor.END_QUERY, (int) design.getID());

				if (rset != null) {
					rsetEmpty = !rset.next();
					return;
				}
			} catch (BirtException ex) {
				rsetEmpty = true;
				context.addException(this.getDesign(), ex);
			}
		}
	}

	protected void createQueryForShowIfBlank() {
		IQueryResultSet blankRset = new BlankResultSet(rset);
		rset = blankRset;
		context.setResultSet(rset);
		rsetEmpty = false;
	}

	protected void accessQuery(ReportItemDesign design, IContentEmitter emitter) {
	}

	public void close() throws BirtException {
		rset = null;
		rsetEmpty = false;
		super.close();
	}
}
