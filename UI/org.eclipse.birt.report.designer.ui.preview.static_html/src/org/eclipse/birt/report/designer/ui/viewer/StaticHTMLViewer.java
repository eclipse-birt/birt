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
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.birt.report.designer.ui.controller.InputParameterDialog;
import org.eclipse.birt.report.designer.ui.controller.StaticHTMLController;
import org.eclipse.birt.report.designer.ui.preview.editors.SWTAbstractViewer;
import org.eclipse.birt.report.designer.ui.preview.static_html.StaticHTMLPrviewPlugin;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLActionHandler;
import org.eclipse.birt.report.engine.api.HTMLCompleteImageHandler;
import org.eclipse.birt.report.engine.api.HTMLEmitterConfig;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IAction;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

public class StaticHTMLViewer extends SWTAbstractViewer
{

	private static final String TMP_FOLDER = System.getProperty( "java.io.tmpdir" ) + File.separator + "BIRT"; //$NON-NLS-1$ //$NON-NLS-2$

	private final HTMLRenderOption renderOption = new HTMLRenderOption( );
	private final EngineConfig engineConfig = new HyperlinkEngineConfig( );
	private final Display display = Display.getCurrent( );

	/** The Logger instance is used to log messages. */
	protected static Logger logger = Logger.getLogger( StaticHTMLViewer.class.getName( ) );

	private Composite ui = null;
	private StaticHTMLController controller;
	private Browser browser = null;
	private File indexPageFile = null;
	private String reportDesignFile;
	private Map paramValues;

	private boolean isInitialize;

	private FormToolkit toolkit;

	private ScrolledForm form;

	private SashForm sashForm;

	private Action paramAction;

	private Action tocAction;

	private Composite browserContainer;

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
		form = toolkit.createScrolledForm( parent );
		toolkit.decorateFormHeading( form.getForm( ) );
		form.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		form.setText( "Report Title" ); //$NON-NLS-1$
		//		TableWrapLayout layout = new TableWrapLayout();
		GridLayout layout = new GridLayout( );
		layout.horizontalSpacing = layout.verticalSpacing = 0;
		layout.marginWidth = layout.marginHeight = 2;
		form.getBody( ).setLayout( layout );

		paramAction = new Action( "test", Action.AS_PUSH_BUTTON ) { //$NON-NLS-1$

			public void run( )
			{
				renderReport( reportDesignFile, getParameterValues( ) );
			}
		};
		//		haction.setChecked(true);
		paramAction.setToolTipText( "Open Parameters Dialog" ); //$NON-NLS-1$
		paramAction.setImageDescriptor( StaticHTMLPrviewPlugin.getDefault( )
				.getImageRegistry( )
				.getDescriptor( StaticHTMLPrviewPlugin.IMG_PARAMS ) );
		form.getToolBarManager( ).add( paramAction );

		tocAction = new Action( "TOC", Action.AS_RADIO_BUTTON ) { //$NON-NLS-1$

			public void run( )
			{
				if(sashForm.getMaximizedControl( ) !=null){
					sashForm.setMaximizedControl( null );
					setChecked( true );
				}else{
					sashForm.setMaximizedControl( browserContainer );
					setChecked( false );
				}
			}
		};
		//		haction.setChecked(true);
		tocAction.setToolTipText( "Show TOC" ); //$NON-NLS-1$
		tocAction.setImageDescriptor( StaticHTMLPrviewPlugin.getDefault( )
				.getImageRegistry( )
				.getDescriptor( StaticHTMLPrviewPlugin.IMG_TOC ) );
		tocAction.setChecked(false);
		form.getToolBarManager( ).add( tocAction );

		form.getToolBarManager( ).add( new Separator( ) );

		Action action = new Action( "First", Action.AS_PUSH_BUTTON ) { //$NON-NLS-1$

			public void run( )
			{
			}
		};
		
		action.setToolTipText( "First" ); //$NON-NLS-1$
		action.setImageDescriptor( StaticHTMLPrviewPlugin.getDefault( )
				.getImageRegistry( )
				.getDescriptor( StaticHTMLPrviewPlugin.IMG_NAV_FIRST ) );
		form.getToolBarManager( ).add( action );

		action = new Action( "Pre", Action.AS_PUSH_BUTTON ) { //$NON-NLS-1$

			public void run( )
			{
			}
		};
		
		action.setToolTipText( "Pre" ); //$NON-NLS-1$
		action.setImageDescriptor( StaticHTMLPrviewPlugin.getDefault( )
				.getImageRegistry( )
				.getDescriptor( StaticHTMLPrviewPlugin.IMG_NAV_PRE ) );
		form.getToolBarManager( ).add( action );

		action = new Action( "test", Action.AS_PUSH_BUTTON ) { //$NON-NLS-1$

			public void run( )
			{
			}
		};
		
		action.setToolTipText( "Next" ); //$NON-NLS-1$
		action.setImageDescriptor( StaticHTMLPrviewPlugin.getDefault( )
				.getImageRegistry( )
				.getDescriptor( StaticHTMLPrviewPlugin.IMG_NAV_NEXT ) );
		form.getToolBarManager( ).add( action );

		action = new Action( "Last", Action.AS_PUSH_BUTTON ) { //$NON-NLS-1$

			public void run( )
			{
			}
		};
		
		action.setToolTipText( "Last" ); //$NON-NLS-1$
		action.setImageDescriptor( StaticHTMLPrviewPlugin.getDefault( )
				.getImageRegistry( )
				.getDescriptor( StaticHTMLPrviewPlugin.IMG_NAV_LAST ) );
		form.getToolBarManager( ).add( action );

		form.getToolBarManager( ).add( new Separator( ) );

		action = new Action( "Select pages", Action.AS_PUSH_BUTTON ) { //$NON-NLS-1$

			public void run( )
			{
			}
		};
		
		action.setToolTipText( "Go to page" ); //$NON-NLS-1$
		action.setImageDescriptor( StaticHTMLPrviewPlugin.getDefault( )
				.getImageRegistry( )
				.getDescriptor( StaticHTMLPrviewPlugin.IMG_NAV_PAGE ) );
		form.getToolBarManager( ).add( action );

		form.updateToolBar( );

		sashForm = new SashForm( form.getBody( ), SWT.NULL );
		sashForm.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		sashForm.setLayout( layout );
		toolkit.adapt(sashForm, false, false);

		createTOCSection( sashForm );
		createBrowserSection( sashForm );

		sashForm.setWeights( new int[]{
				2, 8
		} );

		//		if ( newUI != null )
		//		{
		//			if ( ui != null )
		//			{
		//				ui.dispose( );
		//			}
		//			ui = newUI;
		//			ui.setLayout( new GridLayout( 1, false ) );
		//			ui.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		//			controller = new StaticHTMLController( );
		//			controller.addButton( "Parameters",
		//					"Open parameter dialog",
		//					new SelectionListener( ) {
		//
		//						public void widgetDefaultSelected( SelectionEvent e )
		//						{
		//							// TODO Auto-generated method stub
		//
		//						}
		//
		//						public void widgetSelected( SelectionEvent e )
		//						{
		//							renderReport( reportDesignFile,
		//									getParameterValues( ) );
		//						}
		//					} );
		//			controller.setViewer( this );
		//			browser = new Browser( ui, SWT.NONE );
		//			browser.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		//			ui.layout( );
		//
		//			new ReportLocationListener( browser, this );
		//
		//		}

		toolkit.paintBordersFor( form.getBody( ) );

		return this.form;
	}

	private void createBrowserSection( Composite parent )
	{
		browserContainer = toolkit.createComposite( parent );
		browserContainer.setLayoutData( new GridData( GridData.FILL_BOTH ) );
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.horizontalSpacing = 0;
        layout.verticalSpacing = 0;
        layout.numColumns = 1;
		browserContainer.setLayout( layout );
		
		
		browser = new Browser( browserContainer, SWT.NONE );
		browser.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		sashForm.setMaximizedControl( browserContainer );

	}

	private void createTOCSection( Composite parent )
	{
		Composite toc = toolkit.createComposite( parent );
		toc.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.horizontalSpacing = 0;
        layout.verticalSpacing = 0;
        layout.numColumns = 1;
		toc.setLayout( new GridLayout( ) );

		toolkit.createLabel( toc, "Table of Contents:" );
		Tree t = toolkit.createTree(toc, SWT.NULL);
		t.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		toolkit.paintBordersFor(toc);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.preview.extension.IViewer#getUI()
	 */
	public Composite getUI( )
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

	public static void createIndexPageHtml( Writer writer, String name,
			String defaultPage ) throws IOException
	{
		writer.write( "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n" );
		writer.write( "<!--NewPage-->\n" );
		writer.write( "<HTML>\n" );
		writer.write( "<HEAD>\n" );
		writer.write( "<meta name=\"collection\" content=\"exclude\">\n" );
		writer.write( "\n" );
		writer.write( "<!-- Generated by javadoc on Wed Aug 11 07:30:38 PDT 2004-->\n" );
		writer.write( "<TITLE>\n" );
		writer.write( name );
		writer.write( "</TITLE>\n" );
		writer.write( "<SCRIPT type=\"text/javascript\">\n" );
		writer.write( "targetPage = \"\" + window.location.search;\n" );
		writer.write( "if (targetPage != \"\" && targetPage != \"undefined\")\n" );
		writer.write( "targetPage = targetPage.substring(1);\n" );
		writer.write( "function loadFrames() {\n" );
		writer.write( "if (targetPage != \"\" && targetPage != \"undefined\")\n" );
		writer.write( "top.classFrame.location = top.targetPage;\n" );
		writer.write( "}\n" );
		writer.write( "</SCRIPT>\n" );
		writer.write( "<NOSCRIPT>\n" );
		writer.write( "</NOSCRIPT>\n" );
		writer.write( "</HEAD>\n" );
		writer.write( "\n" );
		writer.write( "<FRAMESET cols=\"0%,100%\" border=0 title=\"\" onLoad=\"top.loadFrames()\">\n" );
		//		writer.write( "<FRAMESET rows=\"50%,50%\" title=\"\" onLoad=\"top.loadFrames()\">\n" );
		//		writer.write( "<FRAME src=\"overview-frame.html\" name=\"pageListFrame\" title=\"Pages List\">\n" );
		//		writer.write( "<FRAME src=\"toc-frame.html\" name=\"tocFrame\" title=\"TOC\">\n" );
		//		writer.write( "</FRAMESET>\n" );
		writer.write( "<FRAME src=\"overview-frame.html\" name=\"pageListFrame\" title=\"Pages List\">\n" );
		writer.write( "<FRAME src=\""
				+ defaultPage
				+ "\" name=\"pageFrame\" title=\"Page 1\">\n" );

		writer.write( "<NOFRAMES>\n" );
		writer.write( "<H2>Frame Alert</H2>\n" );
		writer.write( "<P>\n" );
		writer.write( "This document is designed to be viewed using the frames feature. If you see this message, you are using a non-frame-capable web client.\n" );
		writer.write( "<BR>\n" );
		writer.write( "Link to<A HREF=\"overview-frame.html\">Non-frame version.</A>\n" );
		writer.write( "</NOFRAMES>\n" );
		writer.write( "</FRAMESET>\n" );
		writer.write( "</HTML>\n" );
		writer.flush( );
	}

	public static void createOverviewFrameHtml( Writer writer, int pageCount,
			String name ) throws IOException
	{
		writer.write( "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n" );
		writer.write( "<!--NewPage-->\n" );
		writer.write( "<HTML>\n" );
		writer.write( "<HEAD>\n" );
		// writer.write( "<META http-equiv="Content-Type" content="text/html;
		// charset=gb2312">\n" );
		writer.write( "<TITLE>\n" );
		writer.write( name + "\n" );
		writer.write( "</TITLE>\n" );
		writer.write( "</HEAD>\n" );
		writer.write( "\n" );
		writer.write( "<BODY BGCOLOR=\"white\">\n" );
		writer.write( "<TABLE BORDER=\"0\" WIDTH=\"100%\" SUMMARY=\"\">\n" );
		writer.write( "<TR>\n" );
		writer.write( "<TH ALIGN=\"left\" NOWRAP><FONT size=\"+1\" CLASS=\"FrameTitleFont\">\n" );
		writer.write( "<B><b>Page List</b></B></FONT></TH>\n" );
		writer.write( "</TR>\n" );
		writer.write( "</TABLE>\n" );
		writer.write( "\n" );
		writer.write( "<TABLE BORDER=\"0\" WIDTH=\"100%\" SUMMARY=\"\">\n" );
		writer.write( "<TR>\n" );
		writer.write( "<TD NOWRAP>\n" );

		for ( int i = 1; i <= pageCount; i++ )
		{
			writer.write( "<FONT CLASS=\"FrameItemFont\"><A HREF=\"" );
			writer.write( name
					+ "-"
					+ i
					+ ".html\" target=\"pageFrame\">Page "
					+ i
					+ "</A></FONT>\n" );

			writer.write( "<BR>\n" );
		}

		writer.write( "</TD>\n" );
		writer.write( "</TR>\n" );
		writer.write( "</TABLE>\n" );
		writer.write( "<P>\n" );
		writer.write( "&nbsp;\n" );
		writer.write( "</BODY>\n" );
		writer.write( "</HTML>\n" );
		writer.flush( );
	}

	public void setInput( final Object input )
	{
		if ( input instanceof String )
			this.reportDesignFile = (String) input;
	}

	public void renderReport( final String reportDesignFile,
			final Map parameters )
	{
		//		controller.setBusy( true );
		File reportFile = new File( reportDesignFile );
		try
		{
			int pageNum = 1;
			do
			{
				try
				{
					createReportOutput( reportDesignFile,
							TMP_FOLDER,
							reportFile.getName( ) + "-" + pageNum + ".html",
							parameters,
							pageNum );
				}
				catch ( EngineException e )
				{
					break;
				}
				pageNum++;
			} while ( true );

			int pageCount = pageNum - 1;
			indexPageFile = new File( TMP_FOLDER
					+ File.separator
					+ reportFile.getName( ) + "-1.html" );

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
		try
		{
			browser.setUrl( indexPageFile.toURI( ).toURL( ).toString( ) );
		}
		catch ( MalformedURLException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace( );
		}
		//		controller.setBusy( false );
	}

	protected Map getParameterValues( )
	{
		List params = getInputParameters( reportDesignFile );
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

	public void render( )
	{

		Display.getCurrent( ).asyncExec( new Runnable( ) {

			public void run( )
			{
				if ( !isInitialize )
				{
					init( );
					isInitialize = true;
				}
				renderReport( reportDesignFile, getParameterValues( ) );
			}

		} );

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
