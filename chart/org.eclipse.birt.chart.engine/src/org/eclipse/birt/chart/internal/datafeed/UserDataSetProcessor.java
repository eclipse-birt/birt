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

package org.eclipse.birt.chart.internal.datafeed;

import org.eclipse.birt.chart.datafeed.IResultSetDataSet;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.data.DataSet;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;

/**
 * An internal processor which populates the user datasets.
 */
public class UserDataSetProcessor
{

	/**
	 * Populates the trigger datasets from given data source. Only Text data is
	 * supported now.
	 * 
	 * @param oResultSetDef
	 * @throws ChartException
	 */
	public DataSet[] populate( Object oResultSetDef ) throws ChartException
	{
		DataSet[] ds = new DataSet[0];

		if ( oResultSetDef instanceof IResultSetDataSet )
		{
			final IResultSetDataSet rsds = (IResultSetDataSet) oResultSetDef;
			final long lRowCount = rsds.getSize( );

			if ( lRowCount <= 0 )
			{
				throw new ChartException( ChartEnginePlugin.ID,
						ChartException.ZERO_DATASET,
						"exception.empty.dataset",//$NON-NLS-1$
						Messages.getResourceBundle( ) );
			}

			final int columnCount = rsds.getColumnCount( );
			ds = new DataSet[columnCount];

			// switch ( rsds.getDataType( ) )
			// {
			// case IConstants.TEXT :
			//

			// !Only support text user data currently.
			final String[][] saDataSet = new String[columnCount][(int) lRowCount];
			int i = 0;
			while ( rsds.hasNext( ) )
			{
				Object[] nextRow = rsds.next( );
				for ( int k = 0; k < columnCount; k++ )
				{
					saDataSet[k][i] = String.valueOf( nextRow[k] );
				}
				i++;
			}

			for ( int k = 0; k < columnCount; k++ )
			{
				ds[k] = TextDataSetImpl.create( saDataSet[k] );
			}

			// break;
			//
			// default :
			// throw new ChartException( ChartEnginePlugin.ID,
			// ChartException.DATA_SET,
			// "exception.unknown.trigger.datatype",//$NON-NLS-1$
			// ResourceBundle.getBundle( Messages.ENGINE,
			// Locale.getDefault( ) ) );
			// }
		}
		else
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.DATA_SET,
					"exception.unknown.custom.dataset", //$NON-NLS-1$
					Messages.getResourceBundle( ) );
		}

		return ds;
	}
}
