package org.eclipse.birt.report.designer.internal.ui.extension;

import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;


public class ExtendedDataXtabAdapterHelper
{
	private static final ExtendedDataXtabAdapterHelper instance = new ExtendedDataXtabAdapterHelper();
	
	private ExtendedDataXtabAdapterHelper(){}
	
	public static ExtendedDataXtabAdapterHelper getInstance()
	{
		return instance;
	}
	
	public IExtendedDataXtabAdapter getAdapter()
	{
		return (IExtendedDataXtabAdapter) ElementAdapterManager.getAdapter(this, IExtendedDataXtabAdapter.class);
	}
	
}
