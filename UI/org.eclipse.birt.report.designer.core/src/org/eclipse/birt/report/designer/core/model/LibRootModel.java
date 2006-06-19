/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

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
	
	public Object getModel()
	{
		return library;
	}
		
}
