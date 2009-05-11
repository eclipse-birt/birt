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

package org.eclipse.birt.report.model.command;

import java.net.URL;
import java.util.List;

import org.eclipse.birt.report.model.activity.AbstractElementCommand;
import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.ActivityStackEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.CssException;
import org.eclipse.birt.report.model.api.command.CssReloadedEvent;
import org.eclipse.birt.report.model.api.css.StyleSheetException;
import org.eclipse.birt.report.model.api.elements.structures.IncludedCssStyleSheet;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.StructureContext;
import org.eclipse.birt.report.model.css.CssStyleSheet;
import org.eclipse.birt.report.model.css.CssStyleSheetAdapter;
import org.eclipse.birt.report.model.elements.ICssStyleSheetOperation;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.util.CommandLabelFactory;
import org.eclipse.birt.report.model.util.ModelUtil;

/**
 * Represents the command for adding and dropping css from report design or
 * theme.
 * 
 */

public class CssCommand extends AbstractElementCommand
{

	/**
	 * The css style sheet is appended to the list.
	 */

	private static final int APPEND_POS = -1;

	/**
	 * Construct the command with the report design.
	 * 
	 * @param module
	 *            the report design
	 * @param element
	 *            element.
	 * 
	 */

	public CssCommand( Module module, DesignElement element )
	{
		super( module, element );
	}

	/**
	 * Adds new css file to report design.
	 * 
	 * @param fileName
	 *            css file name
	 * @throws SemanticException
	 *             if failed to add <code>CssStyleSheet</code> strcutre
	 */

	public void addCss( String fileName ) throws SemanticException
	{
		IncludedCssStyleSheet cssStruct = StructureFactory
				.createIncludedCssStyleSheet( );
		cssStruct.setFileName( fileName );

		addCss( cssStruct );
	}

	/**
	 * Adds new CSS structure to report design.
	 * 
	 * @param cssStruct
	 *            the CSS structure
	 * @throws SemanticException
	 *             if failed to add <code>CssStyleSheet</code> structure
	 */

	public void addCss( IncludedCssStyleSheet cssStruct )
			throws SemanticException
	{
		String fileName = cssStruct.getFileName( );

		CssStyleSheet sheet = null;
		try
		{
			sheet = module.loadCss( fileName );
		}
		catch ( StyleSheetException e )
		{
			throw ModelUtil.convertSheetExceptionToCssException( module,
					cssStruct, fileName, e );
		}

		doAddCssSheet( cssStruct, sheet, APPEND_POS );
	}

	/**
	 * Adds new css file to report design.
	 * 
	 * @param sheet
	 *            css style sheet
	 * @throws SemanticException
	 *             if failed to add <code>IncludedCssStyleSheet</code> structure
	 */

	public void addCss( CssStyleSheet sheet ) throws SemanticException
	{
		if ( sheet == null )
			return;

		// must be absolute file path.

		String fileName = sheet.getFileName( );
		if ( getCssStyleSheetByLocation( fileName ) != null )
		{
			throw new CssException( module, new String[]{fileName},
					CssException.DESIGN_EXCEPTION_DUPLICATE_CSS );
		}

		IncludedCssStyleSheet css = StructureFactory
				.createIncludedCssStyleSheet( );
		css.setFileName( sheet.getFileName( ) );

		doAddCssSheet( css, sheet, APPEND_POS );
	}

	/**
	 * Adds one css style in the module.
	 * 
	 * @param fileName
	 *            css file name
	 * @throws SemanticException
	 */

	private void doAddCssStruct( IncludedCssStyleSheet css, int posn )
			throws SemanticException
	{
		assert css != null;

		ElementPropertyDefn propDefn = element
				.getPropertyDefn( IReportDesignModel.CSSES_PROP );
		ComplexPropertyCommand propCommand = new ComplexPropertyCommand(
				module, element );
		if ( posn == APPEND_POS )
			propCommand.addItem(
					new StructureContext( element, propDefn, null ), css );
		else
			propCommand.insertItem( new StructureContext( element, propDefn,
					null ), css, posn );
	}

	/**
	 * Adds one css style in the module.
	 * 
	 * @param fileName
	 *            css file name
	 * @throws SemanticException
	 */

	private void doAddCssSheet( IncludedCssStyleSheet cssStruct,
			CssStyleSheet sheet, int posn ) throws SemanticException
	{
		if ( cssStruct == null || sheet == null )
			return;

		ActivityStack activityStack = getActivityStack( );

		CssRecord record = null;

		if ( posn == APPEND_POS )
			record = new CssRecord( module, element, sheet, true );
		else
			record = new CssRecord( module, element, sheet, true, posn );

		activityStack.startTrans( record.getLabel( ) );

		getActivityStack( ).execute( record );

		// Add includedCsses

		try
		{
			doAddCssStruct( cssStruct, posn );
		}
		catch ( SemanticException e )
		{
			activityStack.rollback( );
			throw e;
		}

		activityStack.commit( );
	}

	/**
	 * Drops the given css style sheet.
	 * 
	 * @param sheet
	 *            css style sheet
	 * @throws SemanticException
	 *             if failed to remove <code>CssStyleSheet</code> strcutre
	 */

	public void dropCss( CssStyleSheet sheet ) throws SemanticException
	{
		if ( sheet == null )
			return;

		String fileName = sheet.getFileName( );
		if ( fileName == null )
			return;

		// find position in css style sheets, position of include css style
		// sheet is the same as css style sheet.

		IncludedCssStyleSheet css = getIncludedCssStyleSheetByLocation( fileName );

		ICssStyleSheetOperation cssOperation = (ICssStyleSheetOperation) element;

		int position = cssOperation.getCsses( ).indexOf( sheet );

		if ( css == null || position == -1 )
		{
			throw new CssException( module, css, new String[]{fileName},
					CssException.DESIGN_EXCEPTION_CSS_NOT_FOUND );
		}

		ActivityStack stack = getActivityStack( );
		CssRecord record = new CssRecord( module, element, sheet, false,
				position );

		stack.startTrans( record.getLabel( ) );

		getActivityStack( ).execute( record );
		removeIncludeCss( css );

		getActivityStack( ).commit( );

	}

	/**
	 * Drop one css file.
	 * 
	 * @param fileName
	 *            absolute file name of the css.
	 * 
	 * @throws PropertyValueException
	 */

	private void removeIncludeCss( IncludedCssStyleSheet css )
			throws SemanticException
	{
		ElementPropertyDefn propDefn = element
				.getPropertyDefn( IReportDesignModel.CSSES_PROP );
		ComplexPropertyCommand propCommand = new ComplexPropertyCommand(
				module, element );
		propCommand.removeItem(
				new StructureContext( element, propDefn, null ), css );
	}

	/**
	 * Gets css style sheet
	 * 
	 * @param location
	 *            file name
	 * @return css style sheet.
	 */

	private CssStyleSheet getCssStyleSheetByLocation( String location )
	{
		ICssStyleSheetOperation sheet = (ICssStyleSheetOperation) element;
		List<CssStyleSheet> csses = sheet.getCsses( );

		URL url = module.findResource( location,
				IResourceLocator.CASCADING_STYLE_SHEET );
		if ( url == null )
		{
			return null;
		}

		return CssStyleSheetAdapter.getCssStyleSheetByLocation( module, csses,
				url );
	}

	/**
	 * Gets include css style sheet.
	 * 
	 * @param location
	 *            absolute file name
	 * @return include css style sheet
	 */

	public IncludedCssStyleSheet getIncludedCssStyleSheetByLocation(
			String location )
	{
		if ( location == null )
			return null;

		ICssStyleSheetOperation sheet = (ICssStyleSheetOperation) element;
		List<CssStyleSheet> csses = sheet.getCsses( );
		int position = CssStyleSheetAdapter.getPositionOfCssStyleSheet( module,
				csses, location );

		if ( position == -1 )
			return null;
		List<Object> cssStructs = element.getListProperty( module,
				IReportDesignModel.CSSES_PROP );

		return (IncludedCssStyleSheet) cssStructs.get( position );
	}

	/**
	 * Reloads the css style sheet with the given file path. After reloading,
	 * acticity stack is cleared.
	 * 
	 * @param sheet
	 *            css style sheet
	 * @throws SemanticException
	 * 
	 */

	public void reloadCss( CssStyleSheet sheet ) throws SemanticException
	{
		if ( sheet == null )
			return;

		IncludedCssStyleSheet cssSheet = getIncludedCssStyleSheetByLocation( sheet
				.getFileName( ) );
		String fileName = cssSheet.getFileName( );
		String externalCssURI = cssSheet.getExternalCssURI( );

		ActivityStack stack = module.getActivityStack( );
		stack.startSilentTrans( true );

		CssStyleSheet newStyleSheet = null;
		try
		{
			int posn = findIncludedCssStyleSheetPosition( sheet );
			dropCss( sheet );

			// if exist such css style sheet, but now css file is removed.
			// should drop such css.

			try
			{
				newStyleSheet = module.loadCss( fileName );
			}
			catch ( StyleSheetException e )
			{
				newStyleSheet = null;
			}

			IncludedCssStyleSheet css = StructureFactory
					.createIncludedCssStyleSheet( );
			css.setFileName( sheet.getFileName( ) );
			css.setExternalCssURI( externalCssURI );

			if ( newStyleSheet == null )
			{
				// if failed, just add the structure into the list.

				doAddCssStruct( css, posn );
			}

			// if failed, newStyleSheet == null, this method should do nothing.

			doAddCssSheet( css, newStyleSheet, posn );
		}
		catch ( SemanticException e )
		{
			stack.rollback( );
			throw e;
		}

		doPostReloadAction( newStyleSheet );
	}

	/**
	 * Returns the position that matches file name of the given style sheet.
	 * 
	 * @return 0-based integer. If not found, return -1
	 */

	private int findIncludedCssStyleSheetPosition( CssStyleSheet sheet )
	{
		List<Object> includedCss = element.getListProperty( module,
				IReportDesignModel.CSSES_PROP );
		for ( int i = 0; i < includedCss.size( ); i++ )
		{
			IncludedCssStyleSheet oneCss = (IncludedCssStyleSheet) includedCss
					.get( i );
			assert oneCss.getFileName( ) != null;
			if ( oneCss.getFileName( ).equalsIgnoreCase( sheet.getFileName( ) ) )
				return i;
		}

		return APPEND_POS;
	}

	/**
	 * Returns the includedCssStyleSheet that matches file name of the given
	 * style sheet.
	 * 
	 * @return IncludedCssStyleSheet. If not found, return null.
	 */
	private IncludedCssStyleSheet findIncludedCssStyleSheet( CssStyleSheet sheet )
	{
		List<Object> includedCss = element.getListProperty( module,
				IReportDesignModel.CSSES_PROP );
		int posn = findIncludedCssStyleSheetPosition( sheet );
		if ( posn == APPEND_POS )
			return null;
		return (IncludedCssStyleSheet) includedCss.get( posn );
	}

	/**
	 * Returns the position that matches file name of the given style sheet.
	 * 
	 * @return 0-based integer. If not found, return -1
	 */

	private int findIncludedCssStyleSheetPosition( String fileName )
	{
		List<Object> includedCss = element.getListProperty( module,
				IReportDesignModel.CSSES_PROP );
		for ( int i = 0; i < includedCss.size( ); i++ )
		{
			IncludedCssStyleSheet oneCss = (IncludedCssStyleSheet) includedCss
					.get( i );
			assert oneCss.getFileName( ) != null;
			if ( oneCss.getFileName( ).equalsIgnoreCase( fileName ) )
				return i;
		}

		return APPEND_POS;
	}

	/**
	 * Does some post actions after css style sheet is reloaded. It includes
	 * sending out the CssReloadEvent, commit the stack and flush the stack and
	 * send out the ActivityStackEvent.
	 * 
	 * @param css
	 */

	private void doPostReloadAction( CssStyleSheet css )
	{
		CssReloadedEvent event = new CssReloadedEvent( module, css );
		module.broadcast( event );

		// clear save state mark.

		ActivityStack activityStack = module.getActivityStack( );
		activityStack.commit( );

		// clear all common stack.

		activityStack.flush( );

		module.setSaveState( 0 );
		activityStack.sendNotifcations( new ActivityStackEvent( activityStack,
				ActivityStackEvent.DONE ) );

		// Recheck module.

		module.getModuleHandle( ).checkReport( );
	}

	/**
	 * Checks css style sheet can be renamed or not.
	 * 
	 * @param includedCssSheet
	 *            the included css style sheet
	 * @param newFileName
	 *            the new file name.
	 * @return the matched included css style sheet structure with the same
	 *         location of <code>newFileName</code>
	 * @throws CssException
	 * 
	 */
	public IncludedCssStyleSheet checkRenameCss(
			IncludedCssStyleSheet includedCssSheet, String newFileName )
			throws CssException
	{

		if ( !element.canEdit( module ) )
		{
			throw new CssException( module, new String[]{newFileName},
					CssException.DESIGN_EXCEPTION_READONLY );

		}

		URL url = module.findResource( newFileName,
				IResourceLocator.CASCADING_STYLE_SHEET );
		if ( url == null )
		{
			throw new CssException( module, new String[]{newFileName},
					CssException.DESIGN_EXCEPTION_CSS_NOT_FOUND );
		}

		CssStyleSheet sheet = CssStyleSheetAdapter.getCssStyleSheetByLocation(
				module, ( (ICssStyleSheetOperation) element ).getCsses( ), url );

		if ( sheet != null )
		{
			IncludedCssStyleSheet tmpIncludedCssStyleSheet = findIncludedCssStyleSheet( sheet );
			if ( includedCssSheet != tmpIncludedCssStyleSheet )
				throw new CssException( module, new String[]{newFileName},
						CssException.DESIGN_EXCEPTION_DUPLICATE_CSS );

			return tmpIncludedCssStyleSheet;
		}

		return null;
	}

	/**
	 * Changes the included css style sheet.
	 * 
	 * @param includedCssStyleSheet
	 *            the included css style.
	 * @param newFileName
	 *            the new file name.
	 * @throws SemanticException
	 */
	public void renameCss( IncludedCssStyleSheet includedCssStyleSheet,
			String newFileName ) throws SemanticException
	{
		if ( includedCssStyleSheet == null || newFileName == null )
			return;

		// check the same file name.

		if ( includedCssStyleSheet.getFileName( ).equals( newFileName ) )
			return;

		// check the same location

		IncludedCssStyleSheet foundIncludedCssStyleSheet = checkRenameCss(
				includedCssStyleSheet, newFileName );

		if ( foundIncludedCssStyleSheet == includedCssStyleSheet )
			return;

		String externalCssURI = includedCssStyleSheet.getExternalCssURI( );
		String fileName = includedCssStyleSheet.getFileName( );

		CssStyleSheet sheet = getCssStyleSheetByLocation( fileName );

		IncludedCssStyleSheet css = StructureFactory
				.createIncludedCssStyleSheet( );
		css.setFileName( newFileName );
		css.setExternalCssURI( externalCssURI );

		int posn = findIncludedCssStyleSheetPosition( fileName );
		ActivityStack stack = module.getActivityStack( );

		stack.startTrans( CommandLabelFactory
				.getCommandLabel( MessageConstants.RENAME_CSS_FILE_MESSAGE ) );

		CssStyleSheet newStyleSheet = null;

		try
		{
			if ( sheet == null )
			{
				removeIncludeCss( includedCssStyleSheet );
			}
			else
			{

				dropCss( sheet );
			}

			// if exist such css style sheet, but now css file is removed.
			// should drop such css.

			try
			{
				newStyleSheet = module.loadCss( newFileName );
			}
			catch ( StyleSheetException e )
			{
				newStyleSheet = null;
			}

			if ( newStyleSheet == null )
			{
				// if failed, just add the structure into the list.

				doAddCssStruct( css, posn );
			}

			// if failed, newStyleSheet == null, this method should do
			// nothing.

			doAddCssSheet( css, newStyleSheet, posn );
		}
		catch ( SemanticException e )
		{
			stack.rollback( );
			throw e;
		}

		stack.commit( );
	}
}
