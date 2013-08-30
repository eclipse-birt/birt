
package org.eclipse.birt.report.designer.ui.views.attributes.providers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;

public class LinkedDataSetAdapter
{
	
	public List<DataSetHandle> getVisibleLinkedDataSetsDataSetHandles(ModuleHandle handle )
	{
		ILinkedDataSetHelper helper = (ILinkedDataSetHelper) ElementAdapterManager.getAdapter( this,
				ILinkedDataSetHelper.class );
		if ( helper != null )
		{
			return helper.getVisibleLinkedDataSetsDataSetHandles(handle);
		}
		return new ArrayList<DataSetHandle>( );
	}

	public List<String> getVisibleLinkedDataSets( )
	{
		ILinkedDataSetHelper helper = (ILinkedDataSetHelper) ElementAdapterManager.getAdapter( this,
				ILinkedDataSetHelper.class );
		if ( helper != null )
		{
			return helper.getVisibleLinkedDataSets( );
		}
		return new ArrayList<String>( );
	}

	public boolean setLinkedDataModel( ReportItemHandle handle, Object value )
	{

		ILinkedDataSetHelper helper = (ILinkedDataSetHelper) ElementAdapterManager.getAdapter( this,
				ILinkedDataSetHelper.class );
		if ( helper != null )
		{
			return helper.setLinkedDataModel( handle, value );
		}
		return false;
	}

	public Iterator getDataSetResLinkedDataModel(String datasetName)
	{
		
		ILinkedDataSetHelper helper = (ILinkedDataSetHelper) ElementAdapterManager.getAdapter( this,
				ILinkedDataSetHelper.class );
		if(helper != null)
		{
			return helper.getResultSetIterator( datasetName );
		}
		return null;
	}
}
