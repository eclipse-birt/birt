/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.ui.preview.editors;

import java.util.HashMap;

import org.eclipse.birt.report.designer.internal.ui.dialogs.InputParameterHtmlDialog;
import org.eclipse.birt.report.designer.internal.ui.editors.FileReportProvider;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.editors.IReportProvider;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.viewer.ViewerPlugin;
import org.eclipse.birt.report.viewer.utilities.WebViewer;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.OpenWindowListener;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.browser.WindowEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

public class ReportPreviewEditor extends EditorPart
{

	private Button bParameter;

	private Browser browser;

	private ProgressBar progressBar;

	protected InputParameterHtmlDialog parameterDialog;

	private Object model;

	private IReportProvider provider;

	private HashMap options;

	/**
	 * Implement this method to save the contents of Report Designer.
	 * <p>
	 * If the save is successful, the part should fire a property changed event
	 * reflecting the new dirty state (<code>PROP_DIRTY</code> property).
	 * </p>
	 * <p>
	 * If the save is cancelled through user action, or for any other reason,
	 * the part should invoke <code>setCancelled</code> on the
	 * <code>IProgressMonitor</code> to inform the caller.
	 * </p>
	 * <p>
	 * This method is long-running; progress and cancellation are provided by
	 * the given progress monitor.
	 * </p>
	 * 
	 * @param monitor
	 *            the progress monitor
	 * @see org.eclipse.ui.ISaveablePart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void doSave( IProgressMonitor monitor )
	{
		IReportProvider provider = getProvider( );
		if ( provider != null )
		{
			provider.saveReport( (ModuleHandle) getModel( ),
					getEditorInput( ),
					monitor );
			firePropertyChange( PROP_DIRTY );
		}
		return;
	}

	protected IReportProvider getProvider( )
	{
		if ( provider == null )
		{
			provider = new FileReportProvider( );
		}

		return provider;
	}

	/**
	 * Is editor in dirty mode.
	 * 
	 * @return edit in dirty mode or not
	 */
	public boolean isDirty( )
	{
		return false;
	}

	/**
	 * Create controls in the preview editor.
	 * 
	 * @param parent
	 *            parent composite
	 */
	public void createPartControl( Composite parent )
	{
		// Create the editor parent composite.
		Composite mainPane = new Composite( parent, SWT.NONE );
		GridLayout layout = new GridLayout( 1, false );
		layout.verticalSpacing = 0;
		mainPane.setLayout( layout );
		mainPane.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		Composite buttonTray = new Composite( mainPane, SWT.NONE );
		buttonTray.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_FILL ) );

		RowLayout rowLayout = new RowLayout( );
		rowLayout.type = SWT.HORIZONTAL;
		rowLayout.spacing = 5;
		buttonTray.setLayout( rowLayout );

		bParameter = new Button( buttonTray, SWT.PUSH );
		bParameter.setToolTipText( Messages.getString( "PreviewEditor.parameter.tooltip" ) ); //$NON-NLS-1$
		bParameter.setText( Messages.getString( "PreviewEditor.parameter.tooltip" ) ); //$NON-NLS-1$

		progressBar = new ProgressBar( mainPane, SWT.INDETERMINATE );
		GridData gd = new GridData( GridData.END, GridData.CENTER, false, false );
		gd.heightHint = 10;
		gd.widthHint = 100;
		progressBar.setLayoutData( gd );
		// When initialize preview, show the progress bar
		progressBar.setVisible( true );

		browser = new Browser( mainPane, SWT.NONE );
		gd = new GridData( GridData.FILL_BOTH );
		gd.horizontalSpan = 1;
		browser.setLayoutData( gd );

		// When change the browser location, show the progress bar
		/*
		 * browser.addLocationListener( new LocationAdapter( ) {
		 * 
		 * public void changing( final LocationEvent e ) {
		 * progressBar.setVisible( true ); } } );
		 */

		// When browser loaded completely, the hide the progress bar
		browser.addProgressListener( new ProgressListener( ) {

			public void changed( ProgressEvent event )
			{
			}

			public void completed( ProgressEvent event )
			{
				progressBar.setVisible( false );
			}
		} );

		parameterDialog = new InputParameterHtmlDialog( Display.getCurrent( )
				.getActiveShell( ),
				InputParameterHtmlDialog.TITLE,
				getFileUri( ),
				browser );

		bParameter.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				parameterDialog.open( );
				// if parameter dialog closed successfully, then preview the
				// current report
				if ( parameterDialog.getReturnCode( ) == InputParameterHtmlDialog.RETURN_CODE_BROWSER_CLOSED )
				{
					display( );
				}
			}

		} );

		browser.addOpenWindowListener( new OpenWindowListener( ) {

			public void open( final WindowEvent event )
			{
				final Shell shell = new Shell( );
				shell.setLayout( new FillLayout( ) );
				Browser browser = new Browser( shell, SWT.NONE );
				initialize( Display.getCurrent( ), browser );
				event.browser = browser;
				shell.open( );
			}
		} );
	}

	/**
	 * initialize browser.
	 * 
	 * @param display
	 *            Display
	 * @param browser
	 *            Browser
	 * 
	 * @return
	 */

	private static void initialize( final Display display, Browser browser )
	{
		browser.addOpenWindowListener( new OpenWindowListener( ) {

			public void open( final WindowEvent event )
			{
				final Shell shell = new Shell( );
				shell.setLayout( new FillLayout( ) );
				Browser browser = new Browser( shell, SWT.NONE );
				initialize( display, browser );
				event.browser = browser;
				shell.open( );
			}
		} );

		browser.addTitleListener( new TitleListener( ) {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.browser.TitleListener#changed(org.eclipse.swt.browser.TitleEvent)
			 */
			public void changed( TitleEvent event )
			{
				if ( event.title != null && event.title.length( ) > 0 )
				{
					Browser browser = (Browser) event.widget;

					Shell shell = browser.getShell( );

					shell.setText( event.title );
				}
			}
		} );
	}

	/**
	 * Get model instance.
	 * 
	 * @return model instance
	 */
	public Object getModel( )
	{
		return model;
	}

	protected void createActions( )
	{
		// // Add page actions
		// Action action = LayoutPageAction.getInstance( );
		// getActionRegistry( ).registerAction( action );
		// getSelectionActions( ).add( action.getId( ) );
		//
		// action = NormalPageAction.getInstance( );
		// getActionRegistry( ).registerAction( action );
		// getSelectionActions( ).add( action.getId( ) );
		//
		// action = MasterPageAction.getInstance( );
		// getActionRegistry( ).registerAction( action );
		// getSelectionActions( ).add( action.getId( ) );
		//
		// action = PreviewPageAction.getInstance( );
		// getActionRegistry( ).registerAction( action );
		// getSelectionActions( ).add( action.getId( ) );
		//
		// action = CodePageAction.getInstance( );
		// getActionRegistry( ).registerAction( action );
		// getSelectionActions( ).add( action.getId( ) );
	}

	/**
	 * Get report design file uri.
	 * 
	 */
	public String getFileUri( )
	{
		IEditorInput input = getEditorInput( );

		if ( input != null )
		{
			IReportProvider provider = getProvider( );
			if ( provider != null )
			{
				return provider.getInputPath( input ).toOSString( );
			}
		}

		return null;
	}

	/**
	 * Refresh swt browser
	 */
	public void display( )
	{
		if ( browser != null )
		{
			String uri = getFileUri( );

			if ( uri != null && uri.length( ) > 0 )
			{
				if ( this.options == null )
				{
					this.options = new HashMap( );
					this.options.put( WebViewer.SERVLET_NAME_KEY,
							InputParameterHtmlDialog.VIEWER_RUN );
					this.options.put( WebViewer.FORMAT_KEY, WebViewer.HTML );
				}
				this.options.put( WebViewer.RESOURCE_FOLDER_KEY,
						ReportPlugin.getDefault( ).getResourceFolder( ) );
				this.options.put( WebViewer.MAX_ROWS_KEY,
						ViewerPlugin.getDefault( )
								.getPluginPreferences( )
								.getString( WebViewer.PREVIEW_MAXROW ) );
				this.options.put( WebViewer.MAX_CUBELEVELS_KEY,
						ViewerPlugin.getDefault( )
								.getPluginPreferences( )
								.getString( WebViewer.PREVIEW_MAXCUBELEVEL ) );
				WebViewer.display( uri, browser, this.options );
			}
		}
	}

	/**
	 * handle something when try to leave the page
	 * 
	 */
	public void handleLeaveThePage( )
	{
		if ( browser != null )
		{
			WebViewer.cancel( browser );
			browser.setUrl( "about:blank" ); //$NON-NLS-1$
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPart#dispose()
	 */
	public void dispose( )
	{
		super.dispose( );

		if ( browser != null )
		{
			WebViewer.cancel( browser );
		}
		
		bParameter = null;
		browser = null;
		// progressBar = null;
		model = null;
	}

	public void doSaveAs( )
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#init(org.eclipse.ui.IEditorSite,
	 *      org.eclipse.ui.IEditorInput)
	 */
	public void init( IEditorSite site, IEditorInput input )
			throws PartInitException
	{
		super.setSite( site );

		model = getProvider( ).getReportModuleHandle( input );
		setInput( input );
	}

	public boolean isSaveAsAllowed( )
	{
		// TODO Auto-generated method stub
		return false;
	}

	public void setFocus( )
	{
		// TODO Auto-generated method stub

	}

	public Browser getBrowser( )
	{
		return this.browser;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#getAdapter(java.lang.Class)
	 */
	public Object getAdapter( Class adapter )
	{
		if ( adapter.equals( ActionRegistry.class ) )
		{
			new ActionRegistry( );
		}
		return super.getAdapter( adapter );
	}
}
