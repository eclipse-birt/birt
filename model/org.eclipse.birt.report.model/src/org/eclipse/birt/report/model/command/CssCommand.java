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
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.activity.AbstractElementCommand;
import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.ActivityStackEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.CssException;
import org.eclipse.birt.report.model.api.command.CssReloadedEvent;
import org.eclipse.birt.report.model.api.elements.structures.IncludedCssStyleSheet;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.core.CachedMemberRef;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.css.CssStyleSheet;
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
	 * @throws DesignFileException
	 *             if the css file is not found or has fatal errors.
	 * @throws SemanticException
	 *             if failed to add <code>IncludedCssStyleSheet</code>
	 *             strcutre
	 */

	public void addCss( String fileName ) throws DesignFileException,
			SemanticException
	{
		URL url = module.findResource( fileName,
				IResourceLocator.CSS_FILE );
		if( url == null )
		{
			throw new CssException( module , CssException.DESIGN_EXCEPTION_CSS_NOT_FOUND );
		}
		
		String resourcePath = url.getFile( );
		
		if( getCssStyleSheetByLocation( resourcePath ) != null )
		{
			throw new CssException( module,
					CssException.DESIGN_EXCEPTION_DUPLICATE_CSS );
		}

		CssStyleSheet sheet = module.loadCss( element , resourcePath );

		ActivityStack activityStack = getActivityStack( );
		activityStack.startTrans( );

		CssRecord record = new CssRecord( module, element, sheet, true );
		getActivityStack( ).execute( record );

		// Add includedCsses

		IncludedCssStyleSheet css = StructureFactory
				.createIncludedCssStyleSheet( );
		css.setFileName( fileName );
		doAddCss( css );

		activityStack.commit( );
	}

	/**
	 * Adds one css style in the module.
	 * 
	 * @param fileName
	 *            css file name
	 * @throws SemanticException
	 */

	private void doAddCss( IncludedCssStyleSheet css ) throws SemanticException
	{
		assert css != null;

		ElementPropertyDefn propDefn = element
				.getPropertyDefn( IReportDesignModel.CSSES_PROP );
		ComplexPropertyCommand propCommand = new ComplexPropertyCommand(
				module, element );
		propCommand.addItem( new CachedMemberRef( propDefn ), css );
	}

	/**
	 * Drops the given css.
	 * 
	 * @param fileName
	 *            the css file name
	 * @throws SemanticException
	 *             if failed to remove <code>IncludedCssStyleSheet</code>
	 *             strcutre
	 */

	public void dropCss( String fileName ) throws SemanticException
	{
		// css not found.
		
		URL url = module.findResource( fileName,
				IResourceLocator.CSS_FILE );
		if( url == null )
		{
			throw new CssException( module , CssException.DESIGN_EXCEPTION_CSS_NOT_FOUND );
		}
		
		String resourcePath = url.getFile( );
		CssStyleSheet sheet = getCssStyleSheetByLocation( resourcePath );
		if ( sheet == null )
		{
			throw new CssException( module,
					CssException.DESIGN_EXCEPTION_CSS_NOT_FOUND );
		}

		ActivityStack stack = getActivityStack( );
		stack.startTrans( );

		try
		{
			
			removeIncludeCss( fileName );
		}
		catch ( SemanticException ex )
		{
			stack.rollback( );
			throw ex;
		}
		
		CssRecord record = new CssRecord( module, element, sheet, false );
		getActivityStack( ).execute( record );
		
		getActivityStack( ).commit( );
	}

	/**
	 * Drop one css file.
	 * 
	 * @param fileName
	 *            file name of the css.
	 * 
	 * @throws PropertyValueException
	 */

	private void removeIncludeCss( String fileName )
			throws PropertyValueException
	{
		assert fileName != null;

		IncludedCssStyleSheet css = getIncludedCssStyleSheetByLocation( fileName );
		if ( css == null )
		{
			return;
		}

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
		List csses = null;
		if ( element instanceof ICssStyleSheetOperation )
		{
			ICssStyleSheetOperation sheet = (ICssStyleSheetOperation) element;
			csses = sheet.getCsses();
		}
		return ModelUtil.getCssStyleSheetByLocation( element.getRoot( ) , csses , location );
	}
	
	/**
	 * Gets include css style sheet
	 * 
	 * @param location
	 *            file name
	 * @return include css style sheet
	 */

	private IncludedCssStyleSheet getIncludedCssStyleSheetByLocation(
			String location )
	{
		List csses = element.getListProperty( module,
				IReportDesignModel.CSSES_PROP );
		Iterator iterator = csses.iterator( );
		while ( iterator.hasNext( ) )
		{
			IncludedCssStyleSheet css = (IncludedCssStyleSheet) iterator.next( );
			String filename = css.getFileName( );
			if( filename == null )
				continue;
			if ( filename.equalsIgnoreCase( location ) )
			{
				return css;
			}
		}
		return null;
	}

	/**
	 * Reloads the css style sheet with the given file path. After reloading,
	 * acticity stack is cleared.
	 * 
	 * @param location
	 *            the URL file path of the css style sheet file.
	 * @throws DesignFileException
	 *             if the file does no exist.
	 * @throws SemanticException
	 *             if the css is not included in the current module.
	 */

	public void reloadCss( String location ) throws DesignFileException,
			SemanticException
	{
		
		ActivityStack activityStack = getActivityStack( );
		activityStack.startSilentTrans( );

		// reload new css file
		try
		{
			dropCss( location );
			addCss( location );
		}
		catch ( SemanticException e )
		{
			activityStack.rollback( );
			throw e;
		}

		IncludedCssStyleSheet css = getIncludedCssStyleSheetByLocation( location );
		doPostReloadAction( css );
	}

	/**
	 * Does some post actions after css style sheet is reloaded. It includes
	 * sending out the CssReloadEvent, commit the stack and flush the stack and
	 * send out the ActivityStackEvent.
	 * 
	 * @param css
	 */

	private void doPostReloadAction( IncludedCssStyleSheet css )
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
	}

}
