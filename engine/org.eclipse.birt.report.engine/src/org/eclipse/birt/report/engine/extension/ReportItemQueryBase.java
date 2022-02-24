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
 * Base implementation for report item query generation time interface
 */
public class ReportItemQueryBase implements IReportItemQuery {

	protected ExtendedItemHandle modelHandle;

	protected IQueryContext context;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.extension.IReportItemQuery#setModelObject(org.
	 * eclipse.birt.report.model.api.ExtendedItemHandle)
	 */
	public void setModelObject(ExtendedItemHandle modelHandle) {
		this.modelHandle = modelHandle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.extension.IReportItemQuery#getReportQueries(
	 * org.eclipse.birt.data.engine.api.IBaseQueryDefinition)
	 */
	public IBaseQueryDefinition[] getReportQueries(IBaseQueryDefinition parent) throws BirtException {
		return null;
	}

	public IDataQueryDefinition[] createReportQueries(IDataQueryDefinition parent) throws BirtException {
		if (parent instanceof IBaseQueryDefinition) {
			return getReportQueries((IBaseQueryDefinition) parent);
		}
		return getReportQueries(null);
	}

	public void setQueryContext(IQueryContext context) {
		this.context = context;
	}

}
