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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools;

import java.util.Iterator;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.runtime.GUIException;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ExtendsException;
import org.eclipse.jface.util.Assert;

/**
 * The tool handle extends used by elements in the library
 */

public class LibraryElementsToolHandleExtends extends AbstractToolHandleExtends
{

	private DesignElementHandle elementHandle;

	/**
	 * Constructor. Creates a new extends for the given element.
	 * 
	 * @param elementHandle
	 *            the handle of the element
	 */
	public LibraryElementsToolHandleExtends( DesignElementHandle elementHandle )
	{
		super( );
		Assert.isLegal( elementHandle.getRoot( ) instanceof LibraryHandle );
		this.elementHandle = elementHandle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.AbstractToolHandleExtends#preHandleMouseUp()
	 */
	public boolean preHandleMouseUp( )
	{
		ModuleHandle moduleHandle = SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( );
		LibraryHandle library = (LibraryHandle) elementHandle.getRoot( );
		try
		{					
			if ( UIUtil.includeLibrary( moduleHandle, library ) )
			{
				if(elementHandle instanceof ThemeHandle)
				{
					ThemeHandle model = applyTheme( (ThemeHandle)elementHandle,moduleHandle,library);
					if(model != null)
					{
						setModel(elementHandle);
					}					
				}else
				{
					setModel(  moduleHandle.getElementFactory( )
						.newElementFrom( elementHandle, null ) );
				}
			}
		}
		catch ( ExtendsException e )
		{
			GUIException exception = GUIException.createGUIException( ReportPlugin.REPORT_UI,
					e,
					"Library.DND.messages.outofsync" );//$NON-NLS-1$
			ExceptionHandler.handle( exception );
		}
		catch ( Exception e )
		{
			ExceptionHandler.handle( e );
		}
		return super.preHandleMouseUp( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.AbstractToolHandleExtends#preHandleMouseDown()
	 */
	public boolean preHandleMouseDown( )
	{
		return false;
	}		

	private ThemeHandle themeInModuleHandle(ThemeHandle handle, ModuleHandle moduleHandle)
	{
		
		String themeName = handle.getName( ).trim( );
		String themeFileName = handle.getModuleHandle( ).getFileName( );

		LibraryHandle libHandle = moduleHandle.findLibrary(themeFileName);
		if(libHandle == null)
		{
			return null;
		}
		Iterator iterator = moduleHandle.getAllThemes( ).iterator( );

		if ( iterator != null )
		{
			while ( iterator.hasNext( ) )
			{
				ReportElementHandle elementHandle = (ReportElementHandle) iterator.next( );
				
				
				if(elementHandle.getName( ).trim( ).equals( themeName ) 
				&& elementHandle.getRoot( ) == libHandle)								
				{			
					return (ThemeHandle)elementHandle;
				}
				
			}
		}		
			
		return null;
	}
	
	private ThemeHandle applyTheme(ThemeHandle handle, ModuleHandle moduleHandle, LibraryHandle library)
	{
		
		ThemeHandle applyThemeHandle = themeInModuleHandle(handle,moduleHandle);
		if(applyThemeHandle != null)
		{
			try
			{
				moduleHandle.setTheme( applyThemeHandle );
//				ThemeHandle a = moduleHandle.getTheme( );
//				moduleHandle.setTheme( a );
			}
			catch ( SemanticException e )
			{
				GUIException exception = GUIException.createGUIException( ReportPlugin.REPORT_UI,
						e,
						"Library.DND.messages.cannotApplyTheme" );//$NON-NLS-1$
				ExceptionHandler.handle( exception );
				e.printStackTrace();
				
			}
		}
		return applyThemeHandle;
		
	}
}
