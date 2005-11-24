package org.eclipse.birt.report.designer.core.model;

import org.eclipse.birt.report.model.api.LibraryHandle;


public class LibRootModel
{
	private LibraryHandle library;

	public LibRootModel(LibraryHandle lib)
	{
		this.library = lib;
	}
	
	public LibraryHandle getLibraryHandle()
	{
		return library;
	}
	
	public boolean isEmpty()
	{
		if(library !=null)
		{
			return library.getComponents().getCount()==0;
		}
		return true;
	}
}
