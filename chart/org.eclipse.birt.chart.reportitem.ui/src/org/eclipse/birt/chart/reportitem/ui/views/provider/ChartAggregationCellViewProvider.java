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

package org.eclipse.birt.chart.reportitem.ui.views.provider;

import java.util.List;

import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.ui.extension.IAggregationCellViewProvider;
import org.eclipse.birt.report.model.api.DataItemHandle;

/**
 * 
 */

public class ChartAggregationCellViewProvider
		implements
			IAggregationCellViewProvider
{

	public String getViewName( )
	{
		return "Chart"; //$NON-NLS-1$
	}

	public boolean canSwitch( AggregationCellHandle cell )
	{
		List contents = cell.getContents( );
		if ( contents != null && contents.size( ) == 1 )
		{
			return contents.get( 0 ) instanceof DataItemHandle;
		}
		return false;
	}

	public void switchView( AggregationCellHandle cell )
	{
		// cell.setSpanOverOnColumn( level )

	}

}
