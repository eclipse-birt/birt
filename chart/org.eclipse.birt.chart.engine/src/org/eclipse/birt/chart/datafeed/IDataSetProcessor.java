/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.datafeed;

import org.eclipse.birt.chart.exception.DataSetException;
import org.eclipse.birt.chart.model.data.DataSet;

/**
 *  
 */
public interface IDataSetProcessor
{

    public DataSet fromString(String sDataSetRepresentation, DataSet ds) throws DataSetException;

    public void populate(Object oResultSetDef, DataSet ds) throws DataSetException;

    public Object getMinimum(DataSet ds, int iAxisType) throws DataSetException;

    public Object getMaximum(DataSet ds, int iAxisType) throws DataSetException;

    public String getExpectedStringFormat();
}