/*************************************************************************************
 * Copyright (c) 2006 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.ui.viewer;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.designer.ui.controller.InputParameterDialog;
import org.eclipse.birt.report.designer.ui.preview.editors.SWTAbstractViewer;
import org.eclipse.birt.report.designer.ui.preview.static_html.StaticHTMLPrviewPlugin;
import org.eclipse.birt.report.designer.ui.viewer.job.AbstractJob;
import org.eclipse.birt.report.designer.ui.viewer.job.AbstractUIJob;
import org.eclipse.birt.report.designer.ui.viewer.job.RenderJobRunner;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLActionHandler;
import org.eclipse.birt.report.engine.api.HTMLCompleteImageHandler;
import org.eclipse.birt.report.engine.api.HTMLEmitterConfig;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IAction;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class StaticHTMLViewer extends SWTAbstractViewer
{

	private static final String TMP_FOLDER = System.getProperty( "java.io.tmpdir" ) + "BIRT"; //$NON-NLS-1$ //$NON-NLS-2$

	private static final String TITLE_MESSAGE = "Showing page {0} of {1}";

	private final HTMLRenderOption renderOption = new HTMLRenderOption( );
	private final EngineConfig engineConfig = new HyperlinkEngineConfig( );

	private Browser browser = null;
	private File indexPageFile = null;

	/**
	 * The report design file to render.
	 */
	private String reportDesignFile;

	/**
	 * The parameter values for current report design.
	 */
	private Map paramValues;

	/**
	 * The render output file path.
	 */
	private String outputLocation;

	private boolean isInitialize;

	private FormToolkit toolkit;

	private Form form;

	private SashForm sashForm;

	private Action paramAction;

	private Action tocAction;

	private Composite browserContainer;

	private List inputParameters;

	private long currentPageNum = 1;
	private long totalPageNum = 0;

	private Action navFirstAction;

	private Action navPreAction;

	private Action navNextAction;

	private Action navLastAction;

	private Action navGoAction;

	private Text goPageInput;

	public void init( )
	{
		super.init( );
		configEngine( );
		//		configRender( );
	}

	protected void configEngine( )
	{
		HTMLEmitterConfig emitterConfig = new HTMLEmitterConfig( );

		emitterConfig.setActionHandler( new HTMLActionHandler( ) {

			public String getURL( IAction actionDefn, Object context )
			{
				if ( actionDefn.getType( ) == IAction.ACTION_DRILLTHROUGH )
					return "birt://"
							+ URLEncoder.encode( super.getURL( actionDefn,
									context ) );
				return super.getURL( actionDefn, context );
			}

		} );
		emitterConfig.setImageHandler( new HTMLCompleteImageHandler( ) );
		//		emitterConfig.setImageHandler( new HTMLImageHandler( ) );
		engineConfig.getEmitterConfigs( ).put( "html", emitterConfig ); //$NON-NLS-1$
	}

	protected void configRender( )
	{
		renderOption.setOutputFormat( RenderOption.OUTPUT_FORMAT_HTML );
		renderOption.setEmitterID( "org.eclipse.birt.report.engine.emitter.html" ); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.ui.preview.extension.IViewer#createUI(org.eclipse.swt.widgets.Composite)
	 */
	public Control createUI( Composite parent )
	{
		if ( parent == null )
		{
			return null;
		}
		toolkit = new FormToolkit( parent.getDisplay( ) );
		form = toolkit.createForm( parent );

		form.setFont( JFaceResources.getFontRegistry( )
				.get( JFaceResources.BANNER_FONT ) );
		form.setImage( StaticHTMLPrviewPlugin.getDefault( )
				.getImageRegistry( )
				.get( "form_title.gif" ) );

		toolkit.decorateFormHeading( form );
		form.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		GridLayout layout = new GridLayout( );
		layout.horizontalSpacing = layout.verticalSpacing = 0;
		layout.marginWidth = layout.marginHeight = 0;
		form.getBody( ).setLayout( layout );

		//paramAction
		paramAction = new Action( "test", Action.AS_PUSH_BUTTON ) { //$NON-NLS-1$

			public void run( )
			{
				render( );
			}
		};
		paramAction.setToolTipText( "Open Parameters Dialog" ); //$NON-NLS-1$
		paramAction.setImageDescriptor( StaticHTMLPrviewPlugin.getDefault( )
				.getImageRegistry( )
				.getDescriptor( StaticHTMLPrviewPlugin.IMG_PARAMS ) );
		form.getToolBarManager( ).add( paramAction );

		//tocAction
		tocAction = new Action( "TOC", Action.AS_RADIO_BUTTON ) { //$NON-NLS-1$

			public void run( )
			{
				if ( sashForm.getMaximizedControl( ) != null )
				{
					sashForm.setMaximizedControl( null );
					setChecked( true );
				}
				else
				{
					sashForm.setMaximizedControl( browserContainer );
					setChecked( false );
				}
			}
		};
		tocAction.setToolTipText( "Show TOC" ); //$NON-NLS-1$
		tocAction.setImageDescriptor( StaticHTMLPrviewPlugin.getDefault( )
				.getImageRegistry( )
				.getDescriptor( StaticHTMLPrviewPlugin.IMG_TOC ) );
		tocAction.setChecked( false );
		form.getToolBarManager( ).add( tocAction );

		form.getToolBarManager( ).add( new Separator( ) );

		//navFirstAction
		navFirstAction = new Action( "First", Action.AS_PUSH_BUTTON ) { //$NON-NLS-1$

			public void run( )
			{
				currentPageNum = 1;
				render( );
			}
		};
		navFirstAction.setToolTipText( "First" ); //$NON-NLS-1$
		navFirstAction.setImageDescriptor( StaticHTMLPrviewPlugin.getDefault( )
				.getImageRegistry( )
				.getDescriptor( StaticHTMLPrviewPlugin.IMG_NAV_FIRST ) );
		form.getToolBarManager( ).add( navFirstAction );

		//navPreAction
		navPreAction = new Action( "Pre", Action.AS_PUSH_BUTTON ) { //$NON-NLS-1$

			public void run( )
			{
				if ( currentPageNum > 1 )
				{
					currentPageNum--;
					render( );
				}
			}
		};

		navPreAction.setToolTipText( "Pre" ); //$NON-NLS-1$
		navPreAction.setImageDescriptor( StaticHTMLPrviewPlugin.getDefault( )
				.getImageRegistry( )
				.getDescriptor( StaticHTMLPrviewPlugin.IMG_NAV_PRE ) );
		form.getToolBarManager( ).add( navPreAction );

		//navNextAction
		navNextAction = new Action( "test", Action.AS_PUSH_BUTTON ) { //$NON-NLS-1$

			public void run( )
			{
				if ( currentPageNum < totalPageNum )
				{
					currentPageNum++;
					render( );
				}
			}
		};

		navNextAction.setToolTipText( "Next" ); //$NON-NLS-1$
		navNextAction.setImageDescriptor( StaticHTMLPrviewPlugin.getDefault( )
				.getImageRegistry( )
				.getDescriptor( StaticHTMLPrviewPlugin.IMG_NAV_NEXT ) );
		form.getToolBarManager( ).add( navNextAction );

		//navLastAction
		navLastAction = new Action( "Last", Action.AS_PUSH_BUTTON ) { //$NON-NLS-1$

			public void run( )
			{
				currentPageNum = totalPageNum;
				render( );
			}
		};

		navLastAction.setToolTipText( "Last" ); //$NON-NLS-1$
		navLastAction.setImageDescriptor( StaticHTMLPrviewPlugin.getDefault( )
				.getImageRegistry( )
				.getDescriptor( StaticHTMLPrviewPlugin.IMG_NAV_LAST ) );
		form.getToolBarManager( ).add( navLastAction );

		form.getToolBarManager( ).add( new Separator( ) );

		ContributionItem inputText = new ContributionItem( ) {

			public void fill( ToolBar parent, int index )
			{
				ToolItem toolitem = new ToolItem( parent, SWT.SEPARATOR, index );
				Composite container = new Composite( parent, SWT.NULL );
				//				container.setFont( JFaceResources.getFontRegistry( )
				//						.get( JFaceResources.TEXT_FONT ) );
				GridLayout layout = new GridLayout( );
				layout.numColumns = 2;
				layout.marginWidth = layout.marginHeight = 1;
				container.setLayout( layout );
				Label label = new Label( container, SWT.NULL );
				label.setFont( container.getFont( ) );
				label.setText( "Go to page:" );

				goPageInput = toolkit.createText( container, "", SWT.BORDER );
				goPageInput.setFont( container.getFont( ) );

				goPageInput.setLayoutData( new GridData( GridData.FILL_BOTH ) );
				//				goPageInput.addKeyListener( new KeyAdapter( ) {
				//
				//					public void keyPressed( KeyEvent e )
				//					{
				//						if ( e.keyCode != SWT.DEL && e.keyCode != SWT.BS )
				//						{
				//							try
				//							{
				//								long page = Long.parseLong( goPageInput.getText( )
				//										+ e.character );
				//								if ( page > 0 && page <= totalPageNum )
				//									e.doit = true;
				//								else
				//									e.doit = false;
				//							}
				//							catch ( NumberFormatException e1 )
				//							{
				//								e.doit = false;
				//							}
				//						}
				//					}
				//
				//				} );

				goPageInput.addModifyListener( new ModifyListener( ) {

					/**
					 * last valid status
					 */
					private boolean isValid = true;

					public void modifyText( ModifyEvent e )
					{
						if ( !"".equals( goPageInput.getText( ) ) )
						{
							try
							{
								long page = Long.parseLong( goPageInput.getText( ) );
								if ( page > 0 && page <= totalPageNum )
								{
									if ( !isValid )
									{
										form.setMessage( null );
										isValid = true;
									}
									navGoAction.setEnabled( true );
								}
								else
								{
									form.setMessage( "Page Number '"
											+ page
											+ "' is invalid!",
											IMessageProvider.ERROR );
									isValid = false;
									navGoAction.setEnabled( false );
								}
							}
							catch ( NumberFormatException e1 )
							{
								form.setMessage( "Page Number '"
										+ goPageInput.getText( )
										+ "' is invalid!",
										IMessageProvider.ERROR );
								isValid = false;
								navGoAction.setEnabled( false );
							}
						}
						else
						{
							form.setMessage( null );
							isValid = true;
						}
					}
				} );

				toolitem.setWidth( label.computeSize( SWT.DEFAULT, SWT.DEFAULT ).x + 40 );
				toolitem.setControl( container );
			}

		};
		inputText.setVisible( true );
		form.getToolBarManager( ).add( inputText );

		//navSelectAction
		navGoAction = new Action( "Go to page", Action.AS_PUSH_BUTTON ) { //$NON-NLS-1$

			public void run( )
			{
				if ( goPageInput != null && !goPageInput.isDisposed( ) )
				{
					currentPageNum = Long.parseLong( goPageInput.getText( ) );
					render( );
				}
			}
		};

		navGoAction.setToolTipText( "Go to page" ); //$NON-NLS-1$
		navGoAction.setImageDescriptor( StaticHTMLPrviewPlugin.getDefault( )
				.getImageRegistry( )
				.getDescriptor( StaticHTMLPrviewPlugin.IMG_NAV_GO ) );
		form.getToolBarManager( ).add( navGoAction );

		form.updateToolBar( );

		sashForm = new SashForm( form.getBody( ), SWT.NULL );
		sashForm.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		sashForm.setLayout( layout );
		toolkit.adapt( sashForm, false, false );

		createTOCSection( sashForm );
		createBrowserSection( sashForm );

		sashForm.setWeights( new int[]{
				2, 8
		} );

		toolkit.paintBordersFor( form.getBody( ) );

		return this.form;
	}

	private void createBrowserSection( Composite parent )
	{
		browserContainer = toolkit.createComposite( parent );
		browserContainer.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		GridLayout layout = new GridLayout( );
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.numColumns = 1;
		browserContainer.setLayout( layout );

		browser = new Browser( browserContainer, SWT.NONE );
		browser.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		sashForm.setMaximizedControl( browserContainer );

	}

	private void createTOCSection( Composite parent )
	{
		Composite toc = toolkit.createComposite( parent );
		toc.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		GridLayout layout = new GridLayout( );
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.numColumns = 1;
		toc.setLayout( new GridLayout( ) );

		toolkit.createLabel( toc, "Table of Contents:" );
		Tree t = toolkit.createTree( toc, SWT.NULL );
		t.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		toolkit.paintBordersFor( toc );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.preview.extension.IViewer#getUI()
	 */
	public Control getUI( )
	{
		return this.form;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.preview.extension.IViewer#getEngingConfig()
	 */
	public EngineConfig getEngineConfig( )
	{
		return engineConfig;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.preview.extension.IViewer#getRenderOption()
	 */
	public IRenderOption getRenderOption( )
	{
		return renderOption;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.ui.preview.extension.IViewer#setInput(java.lang.Object)
	 */
	public void setInput( final Object input )
	{
		if ( input instanceof String )
			this.reportDesignFile = (String) input;
	}

	/**
	 * Get the path of the report design file to be rendered. 
	 * @return
	 */
	public String getReportDesignFile( )
	{
		return reportDesignFile;
	}

	/**
	 * Get the parameters pair values for current report design.
	 * This method will raise a dialog if the report design has some parameters.
	 * TODO change to check if there are any required parameters not set then open dialog.
	 * @return
	 */
	public Map getParameterValues( List params )
	{
		if ( params != null && params.size( ) > 0 )
		{
			InputParameterDialog dialog = new InputParameterDialog( Display.getCurrent( )
					.getActiveShell( ),
					params,
					paramValues );
			if ( dialog.open( ) == Window.OK )
			{
				paramValues = dialog.getParameters( );
				return paramValues;
			}
		}
		else
		{
			paramAction.setEnabled( false );
			paramAction.setToolTipText( "No Parameters" );
		}
		return paramValues == null ? Collections.EMPTY_MAP : paramValues;
	}

	public void renderReport( IProgressMonitor monitor )
	{
		monitor.subTask( "Collecting parameters" );
		//		getParameterValues( );

		if ( monitor.isCanceled( ) )
		{
			return;
		}
		monitor.worked( 1 );

		monitor.subTask( "Rendering report" );
		if ( monitor.isCanceled( ) )
		{
			return;
		}

		File reportFile = new File( reportDesignFile );
		try
		{
			if ( currentPageNum > 0 )
			{
				try
				{
					this.outputLocation = TMP_FOLDER
							+ File.separator
							+ reportFile.getName( )
							+ ".html";
					this.totalPageNum = createReportOutput( reportDesignFile,
							TMP_FOLDER,
							reportFile.getName( ) + ".html",
							this.paramValues,
							currentPageNum );
				}
				catch ( EngineException e )
				{
				}
			}
			else
			{
				do
				{
					try
					{
						createReportOutput( reportDesignFile,
								TMP_FOLDER,
								reportFile.getName( )
										+ "-"
										+ currentPageNum
										+ ".html",
								this.paramValues,
								currentPageNum );
					}
					catch ( EngineException e )
					{
						break;
					}
					currentPageNum++;
				} while ( true );
				this.outputLocation = reportFile.getName( ) + "-1.html";
			}

			//			try
			//			{
			//				createOverviewFrameHtml( new FileWriter( TMP_FOLDER
			//						+ File.separator
			//						+ "overview-frame.html" ),
			//						pageCount,
			//						reportFile.getName( ) );
			//
			//				createIndexPageHtml( new FileWriter( indexPageFile ),
			//						reportFile.getName( ),
			//						reportFile.getName( ) + "-1.html" );
			//			}
			//			catch ( IOException e )
			//			{
			//			}
		}
		catch ( IOException e )
		{
			e.printStackTrace( );
		}

		if ( monitor.isCanceled( ) )
		{
			return;
		}
		monitor.worked( 3 );
	}

	public void render( )
	{
		form.setText( "Running report..." );
		form.setBusy( true );

		Job initJob = new AbstractJob( ) {

			public void work( IProgressMonitor monitor )
			{
				if ( !isInitialize )
				{
					monitor.subTask( "Initialize engine" );
					init( );
					isInitialize = true;
				}
			}
		};

		final Job prepairParameterJob = new AbstractJob( ) {

			public void work( IProgressMonitor monitor )
			{
				monitor.subTask( "Prepair collect parameters" );
				setParameters( getInputParameters( reportDesignFile ) );
			}
		};

		initJob.addJobChangeListener( new JobChangeAdapter( ) {

			public void done( IJobChangeEvent event )
			{
				super.done( event );
				RenderJobRunner.runRenderJob( prepairParameterJob );
			}
		} );

		final Job getParameterJob = new AbstractUIJob( ) {

			public void work( IProgressMonitor monitor )
			{
				monitor.subTask( "Collecting parameters" );
				getParameterValues( inputParameters );
			}
		};

		prepairParameterJob.addJobChangeListener( new JobChangeAdapter( ) {

			public void done( IJobChangeEvent event )
			{
				super.done( event );
				RenderJobRunner.runRenderJob( getParameterJob );
			}
		} );

		final Job renderJob = new AbstractJob( ) {

			public void work( IProgressMonitor monitor )
			{
				monitor.subTask( "Collecting parameters" );
				renderReport( monitor );
			}
		};

		getParameterJob.addJobChangeListener( new JobChangeAdapter( ) {

			public void done( IJobChangeEvent event )
			{
				super.done( event );
				RenderJobRunner.runRenderJob( renderJob );
			}
		} );

		final Job showJob = new AbstractUIJob( ) {

			public void work( IProgressMonitor monitor )
			{
				monitor.subTask( "Show report in Browser" );
				if ( !form.isDisposed( ) )
				{
					browser.setUrl( outputLocation );
					if ( currentPageNum < totalPageNum )
					{
						navNextAction.setEnabled( true );
						navLastAction.setEnabled( true );
					}
					else
					{
						navNextAction.setEnabled( false );
						navLastAction.setEnabled( false );
					}
					if ( currentPageNum > 1 )
					{
						navPreAction.setEnabled( true );
						navFirstAction.setEnabled( true );
					}
					else
					{
						navPreAction.setEnabled( false );
						navFirstAction.setEnabled( false );
					}
					goPageInput.setText( currentPageNum + "" );
					form.setBusy( false );
					form.setText( MessageFormat.format( TITLE_MESSAGE,
							new Object[]{
									new Long( currentPageNum ),
									new Long( totalPageNum )
							} ) );
				}
			}
		};

		renderJob.addJobChangeListener( new JobChangeAdapter( ) {

			public void done( IJobChangeEvent event )
			{
				super.done( event );
				RenderJobRunner.runRenderJob( showJob );
			}
		} );

		RenderJobRunner.runRenderJob( initJob );
		//		Display.getCurrent( ).asyncExec( new Runnable( ) {
		//
		//			public void run( )
		//			{
		//				if ( !isInitialize )
		//				{
		//					init( );
		//					isInitialize = true;
		//				}
		//				renderReport( reportDesignFile, getParameterValues( ) );
		//			}
		//
		//		} );

	}

	protected void setParameters( List inputParameters )
	{
		this.inputParameters = inputParameters;
	}

}

/**
 * A engine config class, is used to create a hyperlink preview.
 */
class HyperlinkEngineConfig extends EngineConfig
{

	/** Path of image files. */
	public static final String IMAGE_PATH = "image"; //$NON-NLS-1$

	/**
	 * constructor
	 */
	public HyperlinkEngineConfig( )
	{
		super( );

		HTMLEmitterConfig emitterConfig = (HTMLEmitterConfig) getEmitterConfigs( ).get( RenderOption.OUTPUT_FORMAT_HTML );

		emitterConfig.setImageHandler( new HTMLCompleteImageHandler( ) );
	}
}
