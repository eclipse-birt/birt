/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.actions;

import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.UseCssInReportDialog;
import org.eclipse.birt.report.designer.ui.dialogs.UseCssInThemeDialog;
import org.eclipse.birt.report.model.api.IncludedCssStyleSheetHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.birt.report.model.api.elements.structures.IncludedCssStyleSheet;
import org.eclipse.jface.dialogs.Dialog;

/**
 * 
 */

public class EditUseCssStyleAction extends AbstractViewAction
{

	public static final String ID = "org.eclipse.birt.report.designer.internal.ui.views.EditUseCssStyleAction"; //$NON-NLS-1$

	public static final String ACTION_TEXT = Messages.getString( "EditUseCssStyleAction.text" ); //$NON-NLS-1$

	public EditUseCssStyleAction( Object selectedObject )
	{
		this( selectedObject, ACTION_TEXT );
	}

	public EditUseCssStyleAction( Object selectedObject, String text )
	{
		super( selectedObject, text );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.IAction#isEnabled()
	 */
	public boolean isEnabled( )
	{
		// TODO Auto-generated method stub
		Object selection = getSelection( );
		if ( selection == null )
		{
			return false;
		}
		if ( selection instanceof CssStyleSheetHandle )
		{
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run( )
	{
		Object selection = getSelection( );
		assert ( selection instanceof CssStyleSheetHandle );
		CssStyleSheetHandle cssStyle = (CssStyleSheetHandle) selection;
		Object container = cssStyle.getContainerHandle( );
		if ( container instanceof ReportDesignHandle )
		{
			editCssInReportDesign( cssStyle, (ReportDesignHandle) container );
		}
		else if ( container instanceof ThemeHandle )
		{
			editCssInTheme( cssStyle, (ThemeHandle) container );
		}
	}

	private void editCssInTheme( CssStyleSheetHandle cssStyle, ThemeHandle theme )
	{
		UseCssInThemeDialog dialog = new UseCssInThemeDialog( );
		dialog.setDialogTitle( Messages.getString( "EditUseCssStyleAction.EditCssTitle" ) );
		dialog.setTitle( Messages.getString( "EditUseCssStyleAction.EditCssAreaTitle.Libary" ) );
		String relativeFileName = cssStyle.getFileName( );
		IncludedCssStyleSheetHandle includedCss = theme.findIncludedCssStyleSheetHandleByName( relativeFileName );
		dialog.setIncludedCssStyleSheetHandle( includedCss );
		dialog.setTheme( theme );
		if ( dialog.open( ) == Dialog.OK )
		{
			ThemeHandle themeHandle = dialog.getTheme( );
			if ( themeHandle == theme )
			{
				try
				{
//					includedCss.setFileName( dialog.getFileName( ) );
					includedCss.setExternalCssURI( dialog.getURI( ) );
					themeHandle.renameCss( includedCss, dialog.getFileName( ) );
				}
				catch ( SemanticException e )
				{
					ExceptionHandler.handle( e );
				}
			}
			else
			{
				try
				{
					theme.dropCss( cssStyle );
					IncludedCssStyleSheet css = StructureFactory.createIncludedCssStyleSheet( );
					css.setFileName( dialog.getFileName( ) );
					css.setExternalCssURI( dialog.getURI( ) );
					themeHandle.addCss( css );
				}
				catch ( SemanticException e )
				{
					// TODO Auto-generated catch block
					e.printStackTrace( );
				}
			}

		}
	}

	private void editCssInReportDesign( CssStyleSheetHandle cssStyle,
			ReportDesignHandle reportDesign )
	{
		UseCssInReportDialog dialog = new UseCssInReportDialog( );
		dialog.setDialogTitle( Messages.getString( "EditUseCssStyleAction.EditCssTitle" ) );
		dialog.setTitle( Messages.getString( "EditUseCssStyleAction.EditCssAreaTitle.Report" ) );
		String relativeFileName = cssStyle.getFileName( );
		IncludedCssStyleSheetHandle includedCss = reportDesign.findIncludedCssStyleSheetHandleByFileName( relativeFileName );
		dialog.setIncludedCssStyleSheetHandle( includedCss );
		if ( dialog.open( ) == Dialog.OK )
		{
			try
			{
				includedCss.setExternalCssURI( dialog.getURI( ) );
				// reloadAllCssStyle(reportDesign);
				reportDesign.renameCss( includedCss, dialog.getFileName( ) );
			}
			catch ( SemanticException e )
			{
				// TODO Auto-generated catch block
				ExceptionHandler.handle( e );
			}

		}
	}
	


}
