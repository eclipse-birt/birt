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

package org.eclipse.birt.chart.examples.builder;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider;

/**
 * Provides a basic implementation for simulated data service. Used in launcher.
 * 
 */
public class DefaultDataServiceProviderImpl implements IDataServiceProvider
{

	private transient String sDataSetName = "Dummy DataSet"; //$NON-NLS-1$
	private static final int COLUMN_COUNT = 8;
	private static final int ROW_COUNT = 6;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.interfaces.IDataServiceProvider#getAllDataSets()
	 */
	public String[] getAllDataSets( )
	{
		// TODO Auto-generated method stub
		return new String[]{
			"Dummy DataSet" //$NON-NLS-1$
		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.interfaces.IDataServiceProvider#getCurrentDataSet()
	 */
	public String getBoundDataSet( )
	{
		// TODO Auto-generated method stub
		return sDataSetName;
	}

	public String getReportDataSet( )
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.interfaces.IDataServiceProvider#getPreviewHeader(java.lang.String)
	 */
	public String[] getPreviewHeader( )
	{
		String[] columns = new String[COLUMN_COUNT];
		for ( int i = 0; i < columns.length; i++ )
		{
			columns[i] = "DB Col " + ( i + 1 ); //$NON-NLS-1$
		}
		return columns;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.interfaces.IDataServiceProvider#getPreviewData(java.lang.String)
	 */
	public List getPreviewData( )
	{
		List list = new ArrayList( );
		for ( int rowNum = 0; rowNum < ROW_COUNT; rowNum++ )
		{
			String[] columns = new String[COLUMN_COUNT];
			for ( int i = 0; i < columns.length; i++ )
			{
				columns[i] = String.valueOf( ( rowNum + 1 ) * ( i + 1 ) );
			}
			list.add( columns );
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.interfaces.IDataServiceProvider#setContext(java.lang.Object)
	 */
	public void setContext( Object context )
	{
		// this.context = context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.interfaces.IDataServiceProvider#setDataSet(java.lang.String)
	 */
	public void setDataSet( String datasetName )
	{
		// TODO Auto-generated method stub
		this.sDataSetName = datasetName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.interfaces.IDataServiceProvider#invoke(java.lang.String)
	 */
	public int invoke( int command )
	{
		return 1;// Window.CANCEL;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.interfaces.IDataServiceProvider#getAllStyles()
	 */
	public String[] getAllStyles( )
	{
		return new String[]{};
	}

	public String[] getAllStyleDisplayNames( )
	{
		return getAllStyles( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.interfaces.IDataServiceProvider#getCurrentStyle()
	 */
	public String getCurrentStyle( )
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.interfaces.IDataServiceProvider#setStyle(java.lang.String)
	 */
	public void setStyle( String styleName )
	{
		// TODO Auto-generated method stub
	}

	public Object[] getDataForColumns( String[] sExpressions, int iMaxRecords,
			boolean byRow )
	{
		// Always provide data by column
		byRow = false;
		Object[] array = new Object[sExpressions.length];
		for ( int i = 0; i < sExpressions.length; i++ )// a column
		{
			Object[] innerArray = new Object[ROW_COUNT];// a row
			for ( int j = 0; j < ROW_COUNT; j++ )
			{
				String str = sExpressions[i];
				int intStart = str.lastIndexOf( ' ' ) + 1;
				int index = Integer.valueOf( str.substring( intStart,
						intStart + 1 ) ).intValue( ) - 1;
				innerArray[j] = new Integer( ( (String[]) getPreviewData( ).get( j ) )[index] );
			}
			array[i] = innerArray;
		}
		return array;
	}

	public void dispose( )
	{
		// TODO Auto-generated method stub

	}

	public boolean isLivePreviewEnabled( )
	{
		return true;
	}

	public boolean isInvokingSupported( )
	{
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider#commitDataBinding()
	 */
	public void commitDataBinding( )
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider#rollbackDataBinding()
	 */
	public void rollbackDataBinding( )
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider#startDataBinding()
	 */
	public void startDataBinding( )
	{
		// TODO Auto-generated method stub

	}

}
