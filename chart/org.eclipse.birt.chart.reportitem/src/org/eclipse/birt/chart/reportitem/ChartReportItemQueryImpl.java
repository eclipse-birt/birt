/**
 * 
 */

package org.eclipse.birt.chart.reportitem;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.reportitem.i18n.Messages;
import org.eclipse.birt.chart.reportitem.plugin.ChartReportItemPlugin;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.report.engine.extension.ReportItemQueryBase;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.elements.ExtendedItem;

/**
 * 
 */
public final class ChartReportItemQueryImpl extends ReportItemQueryBase
{

	/**
	 * 
	 */
	private Chart cm = null;

	/**
	 * 
	 */
	private ExtendedItemHandle eih = null;

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.reportitem/trace" ); //$NON-NLS-1$

	/**
	 * 
	 */
	public ChartReportItemQueryImpl( )
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.extension.IReportItemQuery#setModelObject(org.eclipse.birt.report.model.api.ExtendedItemHandle)
	 */
	public void setModelObject( ExtendedItemHandle eih )
	{
		IReportItem item = ( (ExtendedItem) eih.getElement( ) ).getExtendedElement( );
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
			item = ( (ExtendedItem) eih.getElement( ) ).getExtendedElement( );
			if ( item == null )
			{
				logger.log( ILogger.ERROR,
						Messages.getString( "ChartReportItemQueryImpl.log.UnableToLocate" ) ); //$NON-NLS-1$
				return;
			}
		}
		cm = (Chart) ( (ChartReportItemImpl) item ).getProperty( "chart.instance" ); //$NON-NLS-1$
		this.eih = eih;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.extension.IReportItemQuery#getReportQueries(org.eclipse.birt.data.engine.api.IBaseQueryDefinition)
	 */
	public IBaseQueryDefinition[] getReportQueries(
			IBaseQueryDefinition ibqdParent ) throws BirtException
	{
		logger.log( ILogger.INFORMATION,
				Messages.getString( "ChartReportItemQueryImpl.log.getReportQueries.start" ) ); //$NON-NLS-1$

		// BUILD THE QUERY ASSOCIATED WITH THE CHART MODEL
		RunTimeContext rtc = new RunTimeContext( );
		// rtc.setLocale(?);
		IBaseQueryDefinition ibqd = null;
		try
		{
			ibqd = QueryHelper.instance( rtc ).build( eih, ibqdParent, cm );
		}
		catch ( RuntimeException gex )
		{
			logger.log( gex );
			logger.log( ILogger.INFORMATION,
					Messages.getString( "ChartReportItemQueryImpl.log.getReportQueries.exception" ) ); //$NON-NLS-1$
			throw new ChartException( ChartReportItemPlugin.ID,
					ChartException.GENERATION,
					gex );
		}
		logger.log( ILogger.INFORMATION,
				Messages.getString( "ChartReportItemQueryImpl.log.getReportQueries.end" ) ); //$NON-NLS-1$
		return new IBaseQueryDefinition[]{
			ibqd
		};
	}

}