/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
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
package org.eclipse.birt.data.engine.olap.driver;

import javax.olap.OLAPException;

import org.eclipse.birt.data.engine.olap.cursor.IRowDataAccessor;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;

public interface IEdgeAxis {

	public IRowDataAccessor getRowDataAccessor();

	public DimensionAxis getDimensionAxis(int index);

	public DimensionAxis[] getAllDimensionAxis();

	public IAggregationResultSet getQueryResultSet();

	public void populateEdgeInfo(boolean isPage) throws OLAPException;
}
