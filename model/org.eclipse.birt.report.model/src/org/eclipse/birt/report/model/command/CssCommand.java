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
		try
		{
			CssStyleSheet sheet = module.loadCss( fileName );
			addCss( sheet );
		}
		catch ( StyleSheetException e )
		{
			throw ModelUtil.convertSheetExceptionToCssException( module,
					fileName, e );
		}
	}

	/**
	 * Adds new css file to report design.
	 * 
	 * @param sheet
	 *            css style sheet
	 * @throws SemanticException
	 *             if failed to add <code>IncludedCssStyleSheet</code>
	 *             strcutre
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

		doAddCssSheet( sheet );
	}

	/**
	 * Adds one css style in the module.
	 * 
	 * @param fileName
	 *            css file name
	 * @throws SemanticException
	 */

	private void doAddCssStruct( IncludedCssStyleSheet css )
			throws SemanticException
	{
		assert css != null;

		ElementPropertyDefn propDefn = element
				.getPropertyDefn( IReportDesignModel.CSSES_PROP );
		ComplexPropertyCommand propCommand = new ComplexPropertyCommand(
				module, element );
		propCommand.addItem( new CachedMemberRef( propDefn ), css );
	}

	/**
	 * Adds one css style in the module.
	 * 
	 * @param fileName
	 *            css file name
	 * @throws SemanticException
	 */

	private void doAddCssSheet( CssStyleSheet sheet ) throws SemanticException
	{
		if ( sheet == null )
			return;

		ActivityStack activityStack = getActivityStack( );
		activityStack.startTrans( );

		CssRecord record = new CssRecord( module, element, sheet, true );
		getActivityStack( ).execute( record );

		IncludedCssStyleSheet css = StructureFactory
				.createIncludedCssStyleSheet( );
		css.setFileName( sheet.getFileName( ) );

		// Add includedCsses

		try
		{
			doAddCssStruct( css );
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

			if ( newStyleSheet == null )
			{
				// if failed, just add the structure into the list.

				IncludedCssStyleSheet css = StructureFactory
						.createIncludedCssStyleSheet( );
				css.setFileName( sheet.getFileName( ) );

				doAddCssStruct( css );
			}

			// if failed, newStyleSheet == null, this method should do nothing.

			doAddCssSheet( newStyleSheet );
		}
		catch ( SemanticException e )
		{
			stack.rollback( );
			throw e;
		}

		doPostReloadAction( newStyleSheet );
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
