/**
 * 
 */

package org.eclipse.birt.report.item.crosstab.internal.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.ui.extension.IAggregationCellViewProvider;
import org.eclipse.birt.report.item.crosstab.ui.extension.SwitchCellInfo;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;

/**
 * @author Administrator
 * 
 */
public class AggregationCellProviderWrapper
{

	ExtendedItemHandle handle;
	CrosstabReportItemHandle crosstab;
	private IAggregationCellViewProvider[] providers;
	private List<AggregationCellHandle> filterCellList = new ArrayList<AggregationCellHandle>( );
	private List<SwitchCellInfo> switchList = new ArrayList<SwitchCellInfo>( );
	
	/**
	 * 
	 * @param handle
	 */
	public AggregationCellProviderWrapper( ExtendedItemHandle handle )
	{

		IReportItem reportItem = null;
		try
		{
			reportItem = handle.getReportItem( );
		}
		catch ( ExtendedElementException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace( );
		}
		assert ( reportItem != null && reportItem instanceof CrosstabReportItemHandle );
		this.crosstab = (CrosstabReportItemHandle) reportItem;
		this.handle = handle;
		inilitializeProviders( );
	}

	public AggregationCellProviderWrapper( CrosstabReportItemHandle crosstab )
	{
		this( (ExtendedItemHandle) crosstab.getModelHandle( ) );
	}

	private void inilitializeProviders( )
	{
		Object obj = ElementAdapterManager.getAdapters( handle,
				IAggregationCellViewProvider.class );
		if ( obj instanceof Object[] )
		{
			Object arrays[] = (Object[]) obj;
			providers = new IAggregationCellViewProvider[arrays.length + 1];
			providers[0] = null;
			for ( int i = 0; i < arrays.length; i++ )
			{
				IAggregationCellViewProvider tmp = (IAggregationCellViewProvider) arrays[i];
				providers[i + 1] = tmp;
			}
		}
	}

	public IAggregationCellViewProvider[] getAllProviders( )
	{
		return providers;
	}

	public boolean switchView( String expectedView, AggregationCellHandle cell )
	{
		boolean ret = false;
		

		IAggregationCellViewProvider provider = getMatchProvider( cell );
		if(provider != null)
		{
			// if current view is the same view with the expected one, then don't restore
			if(! provider.getViewName( ).equals( expectedView ))
			{
				provider.restoreView( cell );
			}
		}
		
		provider = getProvider( expectedView );
		if ( provider == null )
		{
			return ret;
		}
		ret = true;
		
		provider.switchView( cell );
		filterCellList.add( cell );
		return ret;
	}

	public IAggregationCellViewProvider getProvider( String viewName )
	{
		IAggregationCellViewProvider retProvider = null;
		if ( viewName == null || providers == null || providers.length <= 0 )
		{
			return null;
		}
		for ( int i = 0; i < providers.length; i++ )
		{
			if ( providers[i] == null )
			{
				continue;
			}
			if ( providers[i].getViewName( ).equals( viewName ) )
			{
				retProvider = providers[i];
				break;
			}
		}
		return retProvider;
	}

	public IAggregationCellViewProvider getMatchProvider(
			AggregationCellHandle cell )
	{
		IAggregationCellViewProvider retProvider = null;
		if ( providers == null || providers.length <= 0 )
		{
			return null;
		}
		for ( int i = 0; i < providers.length; i++ )
		{
			if ( providers[i] == null )
			{
				continue;
			}
			if ( providers[i].matchView( cell ) )
			{
				retProvider = providers[i];
				break;
			}
		}
		return retProvider;
	}

	public void updateAggregationCell( AggregationCellHandle cell )
	{
		IAggregationCellViewProvider provider = getMatchProvider( cell );
		if ( provider != null )
		{
			provider.updateView( cell );
		}

	}

	public void addSwitchInfo(SwitchCellInfo info)
	{
		switchList.add( info );
	}
	
	public void updateAllAggregationCells( )
	{
		int measureCount = crosstab.getMeasureCount( );
		for ( int i = 0; i < measureCount; i++ )
		{
			MeasureViewHandle measure = crosstab.getMeasure( i );
			AggregationCellHandle cell = measure.getCell( );
			if ( filterCellList.indexOf( cell ) < 0 )
			{
				updateAggregationCell( cell );
			}

			for ( int j = 0; j < measure.getAggregationCount( ); j++ )
			{
				cell = measure.getAggregationCell( j );
				if ( filterCellList.indexOf( cell ) >= 0 )
				{
					continue;
				}
				updateAggregationCell( cell );
			}
		}

		filterCellList.clear( );
	}
	
	public void switchViews()
	{
		for(int i = 0; i < switchList.size( ); i ++)
		{
			SwitchCellInfo info = switchList.get( i );
			AggregationCellHandle cell = info.getAggregationCell( );
			String expectedView = info.getExpectedView( );
			switchView(expectedView, cell);			
		}
		switchList.clear( );
	}
}
