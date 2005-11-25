package org.eclipse.birt.report.designer.core.model;

import org.eclipse.birt.report.model.api.LibraryHandle;


public class LibRootModel
{
	private Object library;

	public LibRootModel(Object lib)
	{
		this.library = lib;
	}
	
	public boolean isEmpty()
	{
		if(library !=null && library instanceof LibraryHandle)
		{
			return ((LibraryHandle)library).getComponents().getCount()==0;
		}
		return true;
	}
}
