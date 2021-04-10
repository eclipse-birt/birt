/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.executor;

import javax.olap.OLAPException;
import javax.olap.cursor.CubeCursor;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.ICubeResultSet;
import org.eclipse.birt.report.engine.extension.IQueryResultSet;
import org.eclipse.birt.report.engine.ir.DataItemDesign;

/**
 * <code>DataItemExecutor</code> is a concrete subclass of
 * <code>QueryItemExecutor</code> that manipulates data items from database
 * columns, expressions and so on.
 * <p>
 * Data item executor calculates expressions in data item design, generate a
 * data content instance, evaluate styles, bookmark, action property and pass
 * this instance to emitter.
 * 
 */
public class DataItemExecutor extends QueryItemExecutor {
	/**
	 * construct a data item executor by giving execution context and report
	 * executor visitor
	 * 
	 * @param context     the executor context
	 * @param itemEmitter the emitter
	 */
	public DataItemExecutor(ExecutorManager manager) {
		super(manager, ExecutorManager.DATAITEM);
	}

	/**
	 * execute the data item.
	 * 
	 * <li>create the data content object
	 * <li>push it to the stack
	 * <li>open the data set, seek to the first record
	 * <li>intialize the content object
	 * <li>process the style, visiblitly, action and bookmark
	 * <li>evaluate the expression, and map it to a predefined value.
	 * <li>call the onCreate if necessary
	 * <li>pass it to emitter
	 * <li>close the data set if any
	 * <li>pop the stack.
	 * 
	 * @see org.eclipse.birt.report.engine.excutor.ReportItemLoader#execute(IContentEmitter)
	 */
	public IContent execute() {
		DataItemDesign dataDesign = (DataItemDesign) getDesign();
		IDataContent dataContent = report.createDataContent();
		setContent(dataContent);

		executeQuery();

		initializeContent(dataDesign, dataContent);

		processAction(dataDesign, dataContent);
		processBookmark(dataDesign, dataContent);
		processStyle(dataDesign, dataContent);
		processVisibility(dataDesign, dataContent);
		processUserProperties(dataDesign, dataContent);

		Object value = null;
		IBaseResultSet rset = context.getResultSet();
		if (rset != null) {
			if (rset.getType() == IBaseResultSet.QUERY_RESULTSET) {
				String bindingColumn = dataDesign.getBindingColumn();
				if (bindingColumn != null) {
					try {
						value = ((IQueryResultSet) rset).getValue(bindingColumn);
					} catch (BirtException ex) {
						context.addException(dataDesign, ex);
					}
				}
			} else if (rset.getType() == IBaseResultSet.CUBE_RESULTSET) {
				String bindingColumn = dataDesign.getBindingColumn();
				if (bindingColumn != null) {
					try {
						CubeCursor cursor = ((ICubeResultSet) rset).getCubeCursor();
						// support null cube cursor 60671 61923
						if (cursor == null) {
							value = null;
						} else {
							value = cursor.getObject(bindingColumn);
						}
					} catch (OLAPException ex) {
						context.addException(dataDesign, new EngineException(ex));
					}
				}
			}

		}

		dataContent.setValue(value);

		// get the mapping value
		processMappingValue(dataDesign, dataContent);

		if (context.isInFactory()) {
			handleOnCreate(dataContent);
		}

		startTOCEntry(dataContent);

		return content;
	}

	public void close() throws BirtException {
		finishTOCEntry();
		closeQuery();
		super.close();
	}
}