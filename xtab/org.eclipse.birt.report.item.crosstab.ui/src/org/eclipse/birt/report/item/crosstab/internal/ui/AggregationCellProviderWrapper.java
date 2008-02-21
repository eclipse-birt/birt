/**
 * 
 */
package org.eclipse.birt.report.item.crosstab.internal.ui;

import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.ui.extension.IAggregationCellViewProvider;
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
			e.printStackTrace();
		}
		assert(reportItem != null && reportItem  instanceof CrosstabReportItemHandle);
		this.crosstab = (CrosstabReportItemHandle)reportItem;
		this.handle = handle;	
		inilitializeProviders();
	}
	
	public AggregationCellProviderWrapper(CrosstabReportItemHandle crosstab)
	{
		this((ExtendedItemHandle)crosstab.getModelHandle( ));
	}
	
	private void inilitializeProviders()
	{
		Object obj = ElementAdapterManager.getAdapters( handle, IAggregationCellViewProvider.class);
		if(obj instanceof Object[])
		{
			Object arrays[] = (Object[])obj;
			providers = new IAggregationCellViewProvider[arrays.length + 1];
			providers[0] = null;
			for(int i = 0; i < arrays.length; i ++)
			{
				IAggregationCellViewProvider tmp = (IAggregationCellViewProvider)arrays[i];
				providers[i + 1] = tmp;
			}
		}
	}
	
	public IAggregationCellViewProvider getProvider(String viewName)
	{
		IAggregationCellViewProvider retProvider = null;
		if(viewName == null || providers == null || providers.length <= 0)
		{
			return null;
		}
		for(int i = 0; i < providers.length; i ++)
		{
			if(providers[i] == null)
			{
				continue;
			}
			if(providers[i].getViewName( ).equals( viewName ))
			{
				retProvider = providers[i];
				break;
			}
		}
		return retProvider;
	}
	
	public IAggregationCellViewProvider getMatchProvider(AggregationCellHandle cell)
	{
		IAggregationCellViewProvider retProvider = null;
		if(providers == null || providers.length <= 0)
		{
			return null;
		}
		for(int i = 0; i < providers.length; i ++)
		{
			if(providers[i] == null)
			{
				continue;
			}
			if(providers[i].matchView( cell ))
			{
				retProvider = providers[i];
				break;
			}
		}
		return retProvider;
	}
	
	public void updateAggregationCell(AggregationCellHandle cell)
	{
		IAggregationCellViewProvider provider = getMatchProvider(cell);
		provider.updateView( cell );
	}
	
	public void updateAllAggregationCells()
	{
		int measureCount = crosstab.getMeasureCount( );
		for(int i = 0; i < measureCount; i ++)
		{
			MeasureViewHandle measure = crosstab.getMeasure( i );
			AggregationCellHandle cell = measure.getCell( );
			updateAggregationCell(cell);
			for(int j = 0; j < measure.getAggregationCount( ); j ++)
			{
				cell = measure.getAggregationCell( j );
				updateAggregationCell(cell);
			}
		}
	}
}
