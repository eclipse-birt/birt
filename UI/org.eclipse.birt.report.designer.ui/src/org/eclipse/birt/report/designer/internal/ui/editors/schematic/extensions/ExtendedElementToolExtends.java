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
import org.eclipse.birt.report.designer.internal.ui.extension.ExtensionPointManager;
import org.eclipse.birt.report.designer.ui.extensions.IReportItemBuilderUI;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.jface.window.Window;

/**
 * Provides creation function for extended element
 *  
 */
public class ExtendedElementToolExtends extends AbstractToolHandleExtends
{

	private String extensionName;

	/**
	 * @param builder
	 */
	public ExtendedElementToolExtends( String extensionName )
	{
		super( );
		this.extensionName = extensionName;
	}

	public boolean preHandleMouseUp( )
	{
		ExtendedItemHandle handle = SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( )
				.getElementFactory( )
				.newExtendedItem( null, extensionName );
		if ( handle == null )
		{
			return false;
		}
		IReportItemBuilderUI builder = getbuilder( );
		if ( builder != null )
		{
			//Open the builder for new element
			if ( builder.open( handle ) == Window.CANCEL )
			{
				return false;
			}
		}
		setModel( handle );
		return super.preHandleMouseUp( );
	} /*
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
	private IReportItemBuilderUI getbuilder( )
	{
		return ExtensionPointManager.getInstance( )
				.getExtendedElementPoint( extensionName )
				.getReportItemBuilderUI( );
				
	}

}