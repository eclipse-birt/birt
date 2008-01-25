/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - Initial implementation.
 ******************************************************************************/

package org.eclipse.birt.report.designer.ui.actions;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.preview.Activator;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.ReportEngine;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.viewer.utilities.WebViewer;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowPulldownDelegate2;
import org.eclipse.ui.forms.editor.FormEditor;

public class PreviewCascadingMenuGroup implements
		IWorkbenchWindowPulldownDelegate2
{

	private Image previewIcon = Activator.getImageDescriptor( "icons/etool16/preview.gif" ) //$NON-NLS-1$
			.createImage( );
	private Image previewPDFIcon = Activator.getImageDescriptor( "icons/etool16/preview_pdf.gif" ) //$NON-NLS-1$
			.createImage( );

	/**
	 * The menu created by this action.
	 */
	private Menu fMenu;

	/**
	 * The constructor.
	 */
	public PreviewCascadingMenuGroup( )
	{
		super( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.actions.PreviewAction#dispose()
	 */
	public void dispose( )
	{
		previewIcon.dispose( );
		previewPDFIcon.dispose( );
	}

	/**
	 * @see org.eclipse.ui.IWorkbenchWindowPulldownDelegate#getMenu(org.eclipse.swt.widgets.Control)
	 */
	public Menu getMenu( Control parent )
	{
		Menu menu = new Menu( parent );
		fMenu = menu;
		fillMenu( fMenu );
		return fMenu;
	}

	/**
	 * @see org.eclipse.jface.action.IMenuCreator#getMenu(org.eclipse.swt.widgets.Menu)
	 */
	public Menu getMenu( Menu parent )
	{
		Menu menu = new Menu( parent );
		fMenu = menu;
		fillMenu( fMenu );
		return fMenu;
	}

	/**
	 * Fills the drop-down menu according to all the extended report output
	 * format.
	 * 
	 * @param menu
	 *            the menu to fill
	 */
	protected void fillMenu( Menu menu )
	{
		ReportEngine engine = new ReportEngine( new EngineConfig( ) );
		String[] supportedFormats = engine.getSupportedFormats( );

		MenuItem previewWebViewer = new MenuItem( menu, SWT.PUSH );
		previewWebViewer.setText( Messages.getString( "designer.preview.run.webviewer" ) ); //$NON-NLS-1$
		previewWebViewer.setImage( previewIcon ); 
		previewWebViewer.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				preview( "html", true ); //$NON-NLS-1$
			}
		} );

		for ( int i = 0; i < supportedFormats.length; i++ )
		{
			final String format = supportedFormats[i];
			MenuItem previewOption = new MenuItem( menu, SWT.PUSH );
			previewOption.setText( Messages.getFormattedString( "designer.preview.run", //$NON-NLS-1$
					new Object[]{
						format.toUpperCase( )
					} ) );
			if ( format.equals( "pdf" ) ) //$NON-NLS-1$
			{
				previewOption.setImage( previewPDFIcon );
			}
			else
			{
				previewOption.setImage( previewIcon );
			}
			previewOption.addSelectionListener( new SelectionAdapter( ) {

				public void widgetSelected( SelectionEvent e )
				{
					preview( format, false );
				}
			} );
		}
	}

	private void preview( String format, boolean allowPage )
	{
		FormEditor editor = UIUtil.getActiveReportEditor( false );
		ModuleHandle model = SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( );

		if ( editor != null )
		{
			if ( model.needsSave( ) )
			{
				editor.doSave( null );
			}
		}
		Map options = new HashMap( );
		options.put( WebViewer.FORMAT_KEY, format );
		options.put( WebViewer.ALLOW_PAGE_KEY, Boolean.valueOf( allowPage ) );
		options.put( WebViewer.RESOURCE_FOLDER_KEY, ReportPlugin.getDefault( )
				.getResourceFolder( ) );
		WebViewer.display( model.getFileName( ), options );
	}

	/**
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged( IAction action, ISelection selection )
	{
		action.setEnabled( isEnable( ) );
	}

	private boolean isEnable( )
	{
		IEditorPart editor = UIUtil.getActiveEditor( true );
		if ( editor != null )
		{
			IContentType[] contentTypes = Platform.getContentTypeManager( )
					.findContentTypesFor( editor.getEditorInput( ).getName( ) );
			if ( contentTypes.length > 0
					&& contentTypes[0] != null
					&& ( contentTypes[0].getId( )
							.equals( "org.eclipse.birt.report.designer.ui.editors.reportdesign" ) || contentTypes[0].getId( ) //$NON-NLS-1$
							.equals( "org.eclipse.birt.report.designer.ui.editors.reporttemplate" ) ) ) //$NON-NLS-1$
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#init(org.eclipse.ui.IWorkbenchWindow)
	 */
	public void init( IWorkbenchWindow window )
	{

	}

	/**
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run( IAction action )
	{
		// do nothing - this is just a menu
	}
}
