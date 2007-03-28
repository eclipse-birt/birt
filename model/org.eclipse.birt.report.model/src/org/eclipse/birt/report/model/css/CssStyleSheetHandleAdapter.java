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

package org.eclipse.birt.report.model.css;

import java.net.URL;

import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.birt.report.model.command.CssCommand;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.ICssStyleSheetOperation;

/**
 * Adapter of CssStyleSheet operation of ThemeHandle/ReportDesignHandle.
 *
 */

public class CssStyleSheetHandleAdapter
{
	private final Module module ;
	
	//element is report design / theme.
	
	private final DesignElement element;
	
	/**
	 * Constructor
	 * @param module
	 * @param element
	 */
	
	public CssStyleSheetHandleAdapter( Module module , DesignElement element )
	{
		this.module = module;
		this.element = element;
	}
	
	/**
	 * Includes one css with the given css file name. The new css will be
	 * appended to the css list.
	 * 
	 * @param sheetHandle
	 *            css style sheet handle
	 * @throws SemanticException
	 *             if error is encountered when handling <code>CssStyleSheet</code>
	 *             structure list.
	 */

	public final void addCss( CssStyleSheetHandle sheetHandle ) throws SemanticException
	{
		if ( sheetHandle  == null )
			return; 
		CssCommand command = new CssCommand( module, element );
		command.addCss( sheetHandle.getStyleSheet( ) );
	}
	

	/**
	 * Includes one css with the given css file name. The new css will be
	 * appended to the css list.
	 * 
	 * @param fileName
	 *            css file name
	 * @throws SemanticException
	 *             if error is encountered when handling <code>CssStyleSheet</code>
	 *             structure list.
	 */

	public final void addCss( String fileName ) throws SemanticException
	{
		if ( fileName == null )
			return;

		CssCommand command = new CssCommand( module, element );
		command.addCss( fileName );
	}

	/**
	 * Drops the given css style sheet of this design file.
	 * 
	 * @param sheetHandle
	 *            the css to drop
	 * @throws SemanticException
	 *             if error is encountered when handling
	 *             <code>CssStyleSheet</code> structure list. Or it
	 *             maybe because that the given css is not found in the design.
	 *             Or that the css has descedents in the current module
	 */

	public final void dropCss( CssStyleSheetHandle sheetHandle )
			throws SemanticException
	{
		if ( sheetHandle == null )
			return;

		CssCommand command = new CssCommand( module, element );
		command.dropCss( sheetHandle.getStyleSheet( ) );
	}

	/**
	 * Check style sheet can be droped or not.
	 * @param sheetHandle
	 * @return <code>true</code> can be dropped.else return <code>false</code>
	 */
	
	public final boolean canDropCssStyleSheet( CssStyleSheetHandle sheetHandle )
	{
		//element is read-only
		if( !element.canEdit( module ))
		{
			return false;
		}
		
		if( sheetHandle == null )
			return false;
		
		String fileName = sheetHandle.getFileName( );
		
		// css not found.
		
		CssStyleSheet sheet = CssStyleSheetAdapter.getCssStyleSheetByLocation( module, 
				( (ICssStyleSheetOperation)element ).getCsses( ), fileName );;
		if ( sheet == null )
		{
			return false;
		}
		return true;
	}
	
	/**
	 * Check style sheet can be added or not.
	 * @param fileName
	 * @return <code>true</code> can be added.else return <code>false</code>
	 */
	
	public final boolean canAddCssStyleSheet( String fileName )
	{
		//element is read-only
		
		if( !element.canEdit( module ))
		{
			return false;
		}
		if( fileName == null )
		{
			return false;
		}
		
		URL url = module.findResource( fileName,
				IResourceLocator.CASCADING_STYLE_SHEET );
		if( url == null )
		{
			return false;
		}
		
		CssStyleSheet sheet = CssStyleSheetAdapter.getCssStyleSheetByLocation(
				module , ( (ICssStyleSheetOperation) element ).getCsses( ), url.getFile( ) );;
		if ( sheet != null )
		{
			return false;
		}
		
		return true;
	}
	

	/**
	 * Check style sheet can be added or not.
	 * @param sheetHandle
	 * @return <code>true</code> can be added.else return <code>false</code>
	 */
	
	public final boolean canAddCssStyleSheet( CssStyleSheetHandle sheetHandle )
	{
		//element is read-only
		if( !element.canEdit( module ))
		{
			return false;
		}
		if( sheetHandle == null )
		{
			return false;
		}
		String fileName = sheetHandle.getFileName();
		return canAddCssStyleSheet( fileName );
	}
	 
	/**
	 * Reloads the css with the given css file path. If the css style sheet
	 * already is included directly or indirectly, reload it. If the css is not
	 * included, exception will be thrown.
	 * 
	 * @param sheetHandle
	 *            css style sheet handle
	 * @throws SemanticException
	 *             if error is encountered when handling
	 *             <code>IncludeCssStyleSheet</code> structure list. Or it
	 *             maybe because that the given css is not found in the design.
	 *             Or that the css has descedents in the current module
	 */

	 public final void reloadCss( CssStyleSheetHandle sheetHandle ) throws SemanticException
	 {
		 if ( sheetHandle == null )
			 return;
	
		 CssCommand command = new CssCommand( module, element );
		 command.reloadCss( sheetHandle.getStyleSheet( ) );
	 }
}
