/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.extension;

import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.content.IReportContent;

public interface IExecutorContext extends IReportContext {

	/**
	 * create IReportItemExecutor of handle, the extendedItem is child.
	 */
	IReportItemExecutor createExecutor(IReportItemExecutor parent, Object handle);

	/**
	 * return IReportContent. User can use it to create content for extendedItem.
	 */
	IReportContent getReportContent();

	/**
	 * execute query
	 */
	IBaseResultSet executeQuery(IBaseResultSet parent, IDataQueryDefinition query);

	/**
	 * execute query
	 */
	IBaseResultSet executeQuery(IBaseResultSet parent, IDataQueryDefinition query, Object handle);

	/**
	 * get the queries of the handle
	 * 
	 * @param handle
	 * @return queries
	 */
	IDataQueryDefinition[] getQueries(Object handle);
}
