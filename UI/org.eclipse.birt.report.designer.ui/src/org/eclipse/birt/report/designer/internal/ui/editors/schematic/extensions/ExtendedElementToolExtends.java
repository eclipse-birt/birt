/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation .
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.extensions;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.AbstractToolHandleExtends;
import org.eclipse.birt.report.designer.ui.extensions.IReportItemBuilder;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.jface.window.Window;

/**
 * Provides creation function for extended element
 *  
 */
public class ExtendedElementToolExtends extends AbstractToolHandleExtends
{

	private String extensionName;
	private IReportItemBuilder builder = null;

	/**
	 * @param builder
	 */
	public ExtendedElementToolExtends( String extensionName,
			IReportItemBuilder builder )
	{
		super( );
		this.extensionName = extensionName;
		setBuilder( builder );
	}

	public boolean preHandleMouseUp( )
	{
		if ( builder != null )
		{
			ExtendedItemHandle handle = SessionHandleAdapter.getInstance( )
					.getReportDesignHandle( )
					.getElementFactory( )
					.newExtendedItem( null, extensionName );
			//Open the builder for new element
			if ( handle != null && builder.open( handle ) == Window.OK )
			{
				setModel( handle );

				//If the dialog popup, mouse up event will not be called
				// automatically, call it explicit
				return super.preHandleMouseUp( );
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.designer.internal.ui.editors.schematic.tools.AbstractToolHandleExtends#preHandleMouseDown()
	 */
	public boolean preHandleMouseDown( )
	{
		return false;
	}

	/**
	 * Gets the builder
	 * 
	 * @return
	 */
	public IReportItemBuilder getbuilder( )
	{
		return builder;
	}

	/**
	 * Sets the builder
	 * 
	 * @param builder
	 */
	public void setBuilder( IReportItemBuilder builder )
	{
		this.builder = builder;
	}
}