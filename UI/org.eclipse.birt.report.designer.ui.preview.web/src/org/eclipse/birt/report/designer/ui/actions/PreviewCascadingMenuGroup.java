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
import org.eclipse.birt.report.designer.ui.preview.IPreviewConstants;
import org.eclipse.birt.report.designer.ui.util.UIHelper;
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

	private static Map typeMap = new HashMap( );
	public static final String TYPE_DOC = "doc"; //$NON-NLS-1$
	public static final String TYPE_HTML = "html"; //$NON-NLS-1$
	public static final String TYPE_PDF = "pdf"; //$NON-NLS-1$
	public static final String TYPE_PPT = "ppt"; //$NON-NLS-1$
	public static final String TYPE_PS = "postscript"; //$NON-NLS-1$
	public static final String TYPE_XLS = "xls"; //$NON-NLS-1$
	public static final String IMG_FILE_DEFAULT = "icons/etool16/preview.gif"; //$NON-NLS-1$
	public static final String IMG_FILE_WEB = "icons/etool16/preview_web.gif"; //$NON-NLS-1$
	public static final String IMG_FILE_DOC = "icons/etool16/preview_doc.gif"; //$NON-NLS-1$
	public static final String IMG_FILE_HTML = "icons/etool16/preview_html.gif"; //$NON-NLS-1$
	public static final String IMG_FILE_PDF = "icons/etool16/preview_pdf.gif"; //$NON-NLS-1$
	public static final String IMG_FILE_PPT = "icons/etool16/preview_ppt.gif"; //$NON-NLS-1$
	public static final String IMG_FILE_PS = "icons/etool16/preview_ps.gif"; //$NON-NLS-1$
	public static final String IMG_FILE_XLS = "icons/etool16/preview_xls.gif"; //$NON-NLS-1$

	static
	{
		typeMap.put( TYPE_DOC, IMG_FILE_DOC );
		typeMap.put( TYPE_HTML, IMG_FILE_HTML );
		typeMap.put( TYPE_PDF, IMG_FILE_PDF );
		typeMap.put( TYPE_PPT, IMG_FILE_PPT );
		typeMap.put( TYPE_PS, IMG_FILE_PS );
		typeMap.put( TYPE_XLS, IMG_FILE_XLS );
	}

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

	/**
	 * @see org.eclipse.ui.IWorkbenchWindowPulldownDelegate#getMenu(org.eclipse.swt
	 *      .widgets.Control)
	 */
	public Menu getMenu( Control parent )
	{
		Menu menu = new Menu( parent );
		fMenu = menu;
		fillMenu( fMenu );
		return fMenu;
	}

	/**
	 * @see org.eclipse.jface.action.IMenuCreator#getMenu(org.eclipse.swt.widgets
	 *      .Menu)
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
		java.util.Arrays.sort( supportedFormats );

		MenuItem previewWebViewer = new MenuItem( menu, SWT.PUSH );
		previewWebViewer.setText( "&1 " + Messages.getString( "designer.preview.run.webviewer" ) ); //$NON-NLS-1$ //$NON-NLS-2$
		previewWebViewer.setImage( UIHelper.getImage( IMG_FILE_WEB ) );
		previewWebViewer.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				preview( TYPE_HTML, true ); //$NON-NLS-1$
			}
		} );

		for ( int i = 0; i < supportedFormats.length; i++ )
		{
			final String format = supportedFormats[i];
			MenuItem previewOption = new MenuItem( menu, SWT.PUSH );
			String indexPrefix = i > 7 ? " " : "&" + ( i + 2 ); //$NON-NLS-1$ //$NON-NLS-2$
			previewOption.setText( indexPrefix
					+ " " + Messages.getFormattedString( "designer.preview.run", //$NON-NLS-1$ //$NON-NLS-2$
							new Object[]{
								format.toUpperCase( )
							} ) );
			if ( typeMap.containsKey( format ) )
			{
				previewOption.setImage( (Image) UIHelper.getImage( (String) typeMap.get( format ) ) );
			}
			else
			{
				previewOption.setImage( UIHelper.getImage( IMG_FILE_DEFAULT ) );
			}
			previewOption.addSelectionListener( new SelectionAdapter( ) {

				public void widgetSelected( SelectionEvent e )
				{
					preview( format, false );
				}
			} );
		}
	}

	protected boolean prePreview( )
	{
		System.clearProperty( IPreviewConstants.SID );
		System.clearProperty( IPreviewConstants.MAX_DATASET_ROWS );
		System.clearProperty( IPreviewConstants.MAX_CUBE_ROW_LEVELS );
		System.clearProperty( IPreviewConstants.MAX_CUBE_COLUMN_LEVELS );
		return true;
	}

	protected void preview( String format, boolean allowPage )
	{
		if ( !prePreview( ) )
		{
			return;
		}

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
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action
	 *      .IAction, org.eclipse.jface.viewers.ISelection)
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
			for ( IContentType type : contentTypes )
			{
				if ( type.getId( )
						.equals( "org.eclipse.birt.report.designer.ui.editors.reportdesign" )
						|| type.getId( )
								.equals( "org.eclipse.birt.report.designer.ui.editors.reporttemplate" ) )
					return true;
			}
		}
		return false;
	}

	/**
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#init(org.eclipse.ui.
	 *      IWorkbenchWindow)
	 */
	public void init( IWorkbenchWindow window )
	{
	}

	/**
	 * @see org.eclipse.birt.report.designer.ui.actions.PreviewAction#dispose()
	 */
	public void dispose( )
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
