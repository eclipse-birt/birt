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
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.IncludedCssStyleSheetHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.CssException;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.birt.report.model.api.elements.structures.IncludedCssStyleSheet;
import org.eclipse.birt.report.model.command.CssCommand;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.ICssStyleSheetOperation;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.Theme;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.birt.report.model.elements.interfaces.IThemeModel;

/**
 * Adapter of CssStyleSheet operation of ThemeHandle/ReportDesignHandle.
 * 
 */

public class CssStyleSheetHandleAdapter
{

	private final Module module;

	// element is report design / theme.

	private final DesignElement element;

	/**
	 * Constructor
	 * 
	 * @param module
	 * @param element
	 */

	public CssStyleSheetHandleAdapter( Module module, DesignElement element )
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
	 *             if error is encountered when handling
	 *             <code>CssStyleSheet</code> structure list.
	 */

	public final void addCss( CssStyleSheetHandle sheetHandle )
			throws SemanticException
	{
		if ( sheetHandle == null )
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
	 *             if error is encountered when handling
	 *             <code>CssStyleSheet</code> structure list.
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
	 *             <code>CssStyleSheet</code> structure list. Or it maybe
	 *             because that the given css is not found in the design. Or
	 *             that the css has descedents in the current module
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
	 * 
	 * @param sheetHandle
	 * @return <code>true</code> can be dropped.else return <code>false</code>
	 */

	public final boolean canDropCssStyleSheet( CssStyleSheetHandle sheetHandle )
	{
		// element is read-only
		if ( !element.canEdit( module ) )
		{
			return false;
		}

		if ( sheetHandle == null )
			return false;

		String fileName = sheetHandle.getFileName( );

		// css not found.

		int position = CssStyleSheetAdapter.getPositionOfCssStyleSheet( module,
				( (ICssStyleSheetOperation) element ).getCsses( ), fileName );;
		if ( position == -1 )
		{
			return false;
		}
		return true;
	}

	/**
	 * Check style sheet can be added or not.
	 * 
	 * @param fileName
	 * @return <code>true</code> can be added.else return <code>false</code>
	 */

	public final boolean canAddCssStyleSheet( String fileName )
	{
		// element is read-only

		if ( !element.canEdit( module ) )
		{
			return false;
		}
		if ( fileName == null )
		{
			return false;
		}

		URL url = module.findResource( fileName,
				IResourceLocator.CASCADING_STYLE_SHEET );
		if ( url == null )
		{
			return false;
		}

		CssStyleSheet sheet = CssStyleSheetAdapter.getCssStyleSheetByLocation(
				module, ( (ICssStyleSheetOperation) element ).getCsses( ), url );
		if ( sheet != null )
		{
			return false;
		}

		return true;
	}

	/**
	 * Check style sheet can be added or not.
	 * 
	 * @param sheetHandle
	 * @return <code>true</code> can be added.else return <code>false</code>
	 */

	public final boolean canAddCssStyleSheet( CssStyleSheetHandle sheetHandle )
	{
		// element is read-only
		if ( !element.canEdit( module ) )
		{
			return false;
		}
		if ( sheetHandle == null )
		{
			return false;
		}
		String fileName = sheetHandle.getFileName( );
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
	 *             <code>IncludeCssStyleSheet</code> structure list. Or it maybe
	 *             because that the given css is not found in the design. Or
	 *             that the css has descedents in the current module
	 */

	public final void reloadCss( CssStyleSheetHandle sheetHandle )
			throws SemanticException
	{
		if ( sheetHandle == null )
			return;

		CssCommand command = new CssCommand( module, element );
		command.reloadCss( sheetHandle.getStyleSheet( ) );
	}

	/**
	 * Includes one css with the given CSS structure. The new css will be
	 * appended to the css list.
	 * 
	 * @param cssStruct
	 *            the CSS structure
	 * @throws SemanticException
	 *             if error is encountered when handling
	 *             <code>CssStyleSheet</code> structure list.
	 */

	public final void addCss( IncludedCssStyleSheet cssStruct )
			throws SemanticException
	{
		if ( cssStruct == null )
			return;

		CssCommand command = new CssCommand( module, element );
		command.addCss( cssStruct );
	}

	/**
	 * Gets <code>IncludedCssStyleSheetHandle</code> by file name.
	 * 
	 * @param fileName
	 *            the file name
	 * @return the includedCssStyleSheet handle.
	 */
	public IncludedCssStyleSheetHandle findIncludedCssStyleSheetHandleByFileName(
			String fileName )
	{

		if ( fileName == null )
			return null;

		String propName = null;
		if ( element instanceof ReportDesign )
		{
			propName = IReportDesignModel.CSSES_PROP;

		}
		else if ( element instanceof Theme )
		{

			propName = IThemeModel.CSSES_PROP;
		}

		PropertyHandle propHandle = element.getHandle( module )
				.getPropertyHandle( propName );

		Iterator handleIter = propHandle.iterator( );
		while ( handleIter.hasNext( ) )
		{
			IncludedCssStyleSheetHandle handle = (IncludedCssStyleSheetHandle) handleIter
					.next( );
			if ( fileName.equals( handle.getFileName( ) ) )
			{
				return handle;
			}
		}

		return null;
	}

	/**
	 * Gets <code>CssStyleSheetHandle</code> by file name.
	 * 
	 * @param fileName
	 *            the file name.
	 * 
	 * @return the cssStyleSheet handle.
	 */
	public CssStyleSheetHandle findCssStyleSheetHandleByFileName(
			String fileName )
	{
		if ( fileName == null )
			return null;

		List list = ( (ICssStyleSheetOperation) element ).getCsses( );
		for ( int i = 0; i < list.size( ); i++ )
		{
			CssStyleSheet css = (CssStyleSheet) list.get( i );
			if ( fileName.equals( css.getFileName( ) ) )
			{
				return css.handle( module );
			}
		}
		return null;
	}

	/**
	 * Renames as new file name.
	 * 
	 * @param handle
	 *            the includedCssStyleSheet handle
	 * @param newFileName
	 *            the new file name.
	 */
	public void renameCss( IncludedCssStyleSheetHandle handle,
			String newFileName ) throws SemanticException

	{
		if ( newFileName == null || handle == null )
			return;

		CssCommand command = new CssCommand( module, element );

		IncludedCssStyleSheet includedCssStyleSheet = command
				.getIncludedCssStyleSheetByLocation( handle.getFileName( ) );
		command.renameCss( includedCssStyleSheet, newFileName );
	}

	/**
	 * Checks css style sheet can be renamed or not.
	 * 
	 * @param sheetHandle
	 *            the included css style sheet handle
	 * @param newFileName
	 *            the new file name.
	 * @return <code>true</code> can be renamed.else return <code>false</code>
	 */
	public boolean canRenameCss( IncludedCssStyleSheetHandle sheetHandle,
			String newFileName )
	{
		if ( newFileName == null || sheetHandle == null )
			return false;

		// check the same file name.
		
		if ( sheetHandle.getFileName( ).equals( newFileName ) )
			return false;
		
		CssCommand command = new CssCommand( module, element );
		IncludedCssStyleSheet includedCssStyleSheet = command
				.getIncludedCssStyleSheetByLocation( sheetHandle.getFileName( ) );

		IncludedCssStyleSheet foundIncludedCssStyleSheet = null;
		try
		{
			foundIncludedCssStyleSheet = command.checkRenameCss(
					includedCssStyleSheet, newFileName );
		}
		catch ( CssException e )
		{
			return false;
		}

		// check the same location
		
		if ( foundIncludedCssStyleSheet == sheetHandle.getStructure( ) )
			return false;

		return true;
	}

}
