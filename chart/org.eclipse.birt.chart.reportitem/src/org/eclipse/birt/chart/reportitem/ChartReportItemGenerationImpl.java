/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.reportitem;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.report.engine.extension.IRowSet;
import org.eclipse.birt.report.engine.extension.ReportItemGenerationBase;

/**
 * ChartReportItemGenerationImpl
 */
public class ChartReportItemGenerationImpl extends ReportItemGenerationBase
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.extension.IReportItemGeneration#onRowSets(org.eclipse.birt.report.engine.extension.IRowSet[])
	 */
	public void onRowSets(IRowSet[] rowSets) throws BirtException
	{
		// check
		if ( rowSets == null
				|| rowSets.length != 1
				|| rowSets[0] == null 
				|| queries == null
				|| queries[0] == null )
		{
			// if the Data rows are null/empty, do nothing.
			return ;
		}
		else
		{
			// Evaluate the expressions so that they are registered by the DtE in the Report
			// Document
			IRowSet rowSet = rowSets[0];
			Collection expressions = queries[0].getRowExpressions();
			while (rowSet.next())
			{
				for (Iterator iter = expressions.iterator(); iter.hasNext();)
				{
					rowSet.evaluate((IBaseExpression)iter.next());
				}
			}
		}
		
	}
}
