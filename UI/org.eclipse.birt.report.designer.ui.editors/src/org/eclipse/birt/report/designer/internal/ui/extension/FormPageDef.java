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

package org.eclipse.birt.report.designer.internal.ui.extension;

import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.ui.editors.IReportEditorPage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;

/**
 * 
 */

public class FormPageDef implements IExtensionConstants
{

	private IConfigurationElement element;

	public String id;
	public String targetEditor;
	public String displayName;
	public String pageClass;
	public boolean visible = true;
	public String relative;
	public int position;

	FormPageDef( IConfigurationElement element )
	{
		this.element = element;
		id = loadStringAttribute( element, ATTRIBUTE_ID );
		displayName = loadStringAttribute( element, ATTRIBUTE_DISPLAY_NAME );
		pageClass = loadStringAttribute( element, ATTRIBUTE_CLASS );
		visible = loadBooleanAttribute( element, ATTRIBUTE_VISIBLE );
	}

	// not implement
	// public int position;
	// public ImageDescriptor icon;

	private String loadStringAttribute( IConfigurationElement element,
			String attributeName )
	{
		return element.getAttribute( attributeName );
	}

	private boolean loadBooleanAttribute( IConfigurationElement element,
			String attributeName )
	{
		String value = element.getAttribute( attributeName );
		if ( value != null )
		{
			return Boolean.valueOf( value ).booleanValue( );
		}
		return false;
	}

	private Object loadClass( IConfigurationElement element,
			String attributeName )
	{
		Object clazz = null;
		try
		{
			clazz = element.createExecutableExtension( attributeName );
		}
		catch ( CoreException e )
		{
			ExceptionHandler.handle( e );
		}
		return clazz;
	}

	public IReportEditorPage createPage( )
	{
		Object def = loadClass( element, ATTRIBUTE_CLASS );
		if ( def instanceof IReportEditorPage )
		{
			return (IReportEditorPage) def;
		}
		return null;
	}
}
