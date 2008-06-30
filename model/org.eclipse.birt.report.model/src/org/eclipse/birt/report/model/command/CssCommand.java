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

import java.util.List;

import org.eclipse.birt.report.model.activity.AbstractElementCommand;
import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.ActivityStackEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.CssException;
import org.eclipse.birt.report.model.api.command.CssReloadedEvent;
import org.eclipse.birt.report.model.api.css.StyleSheetException;
import org.eclipse.birt.report.model.api.elements.structures.IncludedCssStyleSheet;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.core.CachedMemberRef;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.css.CssStyleSheet;
import org.eclipse.birt.report.model.css.CssStyleSheetAdapter;
import org.eclipse.birt.report.model.elements.ICssStyleSheetOperation;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
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
					fileName, e );
		}

		doAddCssSheet( cssStruct, sheet, APPEND_POS );
	}

	/**
	 * Adds new css file to report design.
	 * 
	 * @param sheet
	 *            css style sheet
	 * @throws SemanticException
	 *             if failed to add <code>IncludedCssStyleSheet</code> strcutre
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
			propCommand.addItem( new CachedMemberRef( propDefn ), css );
		else
			propCommand.insertItem( new CachedMemberRef( propDefn ), css, posn );
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
		activityStack.startTrans( );

		CssRecord record = null;

		if ( posn == APPEND_POS )
			record = new CssRecord( module, element, sheet, true );
		else
			record = new CssRecord( module, element, sheet, true, posn );
		
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
		boolean contains = cssOperation.getCsses( ).contains( sheet );

		if ( css == null || !contains )
		{
			throw new CssException( module, new String[]{fileName},
					CssException.DESIGN_EXCEPTION_CSS_NOT_FOUND );
		}

		ActivityStack stack = getActivityStack( );
		stack.startTrans( );

		CssRecord record = new CssRecord( module, element, sheet, false );
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
		propCommand.removeItem( new CachedMemberRef( propDefn ), css );
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
		List csses = sheet.getCsses( );
		return CssStyleSheetAdapter.getCssStyleSheetByLocation( module, csses,
				location );
	}

	/**
	 * Gets include css style sheet.
	 * 
	 * @param location
	 *            absolute file name
	 * @return include css style sheet
	 */

	private IncludedCssStyleSheet getIncludedCssStyleSheetByLocation(
			String location )
	{
		if ( location == null )
			return null;

		ICssStyleSheetOperation sheet = (ICssStyleSheetOperation) element;
		List csses = sheet.getCsses( );
		int position = CssStyleSheetAdapter.getPositionOfCssStyleSheet( module,
				csses, location );

		if ( position == -1 )
			return null;
		csses = element.getListProperty( module, IReportDesignModel.CSSES_PROP );

		return (IncludedCssStyleSheet) csses.get( position );
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

		ActivityStack stack = module.getActivityStack( );
		stack.startSilentTrans( true );

		CssStyleSheet newStyleSheet = null;
		try
		{
			int posn = findIncludedCssStyleSheetPosition( sheet );
			dropCss( sheet );

			String fileName = sheet.getFileName( );

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
		List<IncludedCssStyleSheet> includedCss = element.getListProperty(
				module, IReportDesignModel.CSSES_PROP );
		for ( int i = 0; i < includedCss.size( ); i++ )
		{
			IncludedCssStyleSheet oneCss = includedCss.get( i );
			assert oneCss.getFileName( ) != null;
			if ( oneCss.getFileName( ).equalsIgnoreCase( sheet.getFileName( ) ) )
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

}
