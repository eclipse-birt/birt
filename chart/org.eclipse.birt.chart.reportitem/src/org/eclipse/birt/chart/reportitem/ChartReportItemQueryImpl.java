/***********************************************************************
 * Copyright (c) 2005, 2007, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.reportitem;

import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.reportitem.i18n.Messages;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.report.engine.extension.ReportItemQueryBase;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.re.CrosstabQueryUtil;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.MultiViewsHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;

/**
 * Customized query implementation for Chart.
 */
public final class ChartReportItemQueryImpl extends ReportItemQueryBase
{

	private Chart cm = null;

	private ExtendedItemHandle eih = null;

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.reportitem/trace" ); //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.extension.IReportItemQuery#setModelObject(org.eclipse.birt.report.model.api.ExtendedItemHandle)
	 */
	public void setModelObject( ExtendedItemHandle eih )
	{
		IReportItem item;
		try
		{
			item = eih.getReportItem( );
			if ( item == null )
			{
				try
				{
					eih.loadExtendedElement( );
				}
				catch ( ExtendedElementException eeex )
				{
					logger.log( eeex );
				}
				item = eih.getReportItem( );
				if ( item == null )
				{
					logger.log( ILogger.ERROR,
							Messages.getString( "ChartReportItemQueryImpl.log.UnableToLocate" ) ); //$NON-NLS-1$
					return;
				}
			}
		}
		catch ( ExtendedElementException e )
		{
			logger.log( ILogger.ERROR,
					Messages.getString( "ChartReportItemQueryImpl.log.UnableToLocate" ) ); //$NON-NLS-1$
			return;
		}
		cm = (Chart) ( (ChartReportItemImpl) item ).getProperty( "chart.instance" ); //$NON-NLS-1$
		this.eih = eih;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.extension.ReportItemQueryBase#createReportQueries(org.eclipse.birt.data.engine.api.IDataQueryDefinition)
	 */
	public IDataQueryDefinition[] createReportQueries(
			IDataQueryDefinition parent ) throws BirtException
	{
		logger.log( ILogger.INFORMATION,
				Messages.getString( "ChartReportItemQueryImpl.log.getReportQueries.start" ) ); //$NON-NLS-1$

		IDataQueryDefinition idqd = createQuery( eih, parent );
		logger.log( ILogger.INFORMATION,
				Messages.getString( "ChartReportItemQueryImpl.log.getReportQueries.end" ) ); //$NON-NLS-1$

		return new IDataQueryDefinition[]{
			idqd
		};
	}

	/**
	 * Create query definition by report item handle.
	 * 
	 * @param handle
	 * @param parent
	 * @return
	 * @throws BirtException
	 */
	IDataQueryDefinition createQuery( ExtendedItemHandle handle,
			IDataQueryDefinition parent ) throws BirtException
	{
		if ( handle.getDataSet( ) != null
				|| parent instanceof IBaseQueryDefinition )
		{
			// If chart is in multiple view, it means chart shares host query
			// with table, don't create query by chart, directly use query on
			// table.
			if ( handle.getContainer( ) instanceof MultiViewsHandle )
			{
				ReportItemHandle tableHandle = (ReportItemHandle) handle.getContainer( ).getContainer( );
				if ( tableHandle == null)
				{
					return null;
				}
				
				return new ChartMultiViewQueryHelper(  tableHandle ,
						cm ).createQuery( parent );
			}
			
			return new ChartBaseQueryHelper( handle, cm ).createBaseQuery( parent );
		}
		else if ( handle.getCube( ) != null
				|| parent instanceof ICubeQueryDefinition )
		{
			if ( handle.getContainer( ) instanceof MultiViewsHandle )
			{
				CrosstabReportItemHandle crosstabItem = null;
				crosstabItem = (CrosstabReportItemHandle) ((ExtendedItemHandle)handle.getContainer( ).getContainer( )).getReportItem( );
				// Always cube query returned
				return CrosstabQueryUtil.createCubeQuery( crosstabItem,
						null,
						false,
						true,
						true,
						true,
						true,
						true );
			}
				
			return new ChartCubeQueryHelper( handle, cm ).createCubeQuery( parent );
		}
		
		return null;
	}
}