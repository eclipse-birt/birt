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

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;

/**
 * Defines the extended item query extension, which handles report query
 * preparation.
 */
public interface IReportItemQuery {

	/**
	 * passes a handle to the extended report item model to the query extension
	 * 
	 * @param modelHandle a handle to the extended item model object
	 */
	public void setModelObject(ExtendedItemHandle modelHandle);

	/**
	 * returns the report queries that the extension uses. Report queries provide
	 * data requirement specification to allow the data module in engine to prepare
	 * for data access.
	 * 
	 * @param parent an <I>in</I> parameter specifying the parent query for the
	 *               queries generated for this extended item. Could be null if the
	 *               extended item defines its own data set
	 * @return an array of report queries that is used for data preparation, null if
	 *         no queries
	 * @throws BirtException throwed when the extension fails to construct the query
	 *                       array
	 * @deprecated since 2.2
	 */
	public IBaseQueryDefinition[] getReportQueries(IBaseQueryDefinition parent) throws BirtException;

	public IDataQueryDefinition[] createReportQueries(IDataQueryDefinition parent) throws BirtException;

	/**
	 * set query context
	 */
	void setQueryContext(IQueryContext context);

}
