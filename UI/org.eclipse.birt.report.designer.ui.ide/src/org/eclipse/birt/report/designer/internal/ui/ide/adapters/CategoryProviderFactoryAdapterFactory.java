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

package org.eclipse.birt.report.designer.internal.ui.ide.adapters;

import org.eclipse.birt.report.designer.internal.ui.ide.propertyeditor.IDECategoryProviderFactory;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ICategoryProviderFactory;
import org.eclipse.core.runtime.IAdapterFactory;

/**
 * 
 */

public class CategoryProviderFactoryAdapterFactory implements IAdapterFactory
{

	public Object getAdapter( Object adaptableObject, Class adapterType )
	{
		if ( adapterType == ICategoryProviderFactory.class )
		{
			return IDECategoryProviderFactory.getInstance( );
		}
		return null;
	}

	public Class[] getAdapterList( )
	{
		return new Class[]{
			ICategoryProviderFactory.class
		};
	}

}
