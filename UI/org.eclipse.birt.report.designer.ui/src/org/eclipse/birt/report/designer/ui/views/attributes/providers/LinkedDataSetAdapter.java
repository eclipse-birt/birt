package org.eclipse.birt.report.designer.ui.views.attributes.providers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.TableHandle;

public class LinkedDataSetAdapter {
	
	public List<String> getVisibleLinkedDataSets() {
		ILinkedDataSetHelper helper = (ILinkedDataSetHelper) ElementAdapterManager.getAdapter( this,
				ILinkedDataSetHelper.class );
		if(helper != null)
		{
			return helper.getVisibleLinkedDataSets();
		}
		return new ArrayList<String>();
	}
	
	
	public boolean setLinkedDataModel(ReportItemHandle handle, Object value)
	{
		
		ILinkedDataSetHelper helper = (ILinkedDataSetHelper) ElementAdapterManager.getAdapter( this,
				ILinkedDataSetHelper.class );
		if(helper != null)
		{
			return helper.setLinkedDataModel(handle, value);
		}
		return false;
	}
	
	public boolean isLinkedDataModel(TableHandle tableHandle)
	{
		ILinkedDataSetHelper helper = (ILinkedDataSetHelper) ElementAdapterManager.getAdapter( this,
				ILinkedDataSetHelper.class );
		if(helper != null)
		{
			return helper.isLinkedDataModel(tableHandle);
		}
		return false;
		
	}
	
	public boolean isLinkedDataModel( ReportItemHandle bindingObject)
	{
		ILinkedDataSetHelper helper = (ILinkedDataSetHelper) ElementAdapterManager.getAdapter( this,
				ILinkedDataSetHelper.class );
		if(helper != null)
		{
			return helper.isLinkedDataModel(bindingObject);
		}
		return false;
	}
}
