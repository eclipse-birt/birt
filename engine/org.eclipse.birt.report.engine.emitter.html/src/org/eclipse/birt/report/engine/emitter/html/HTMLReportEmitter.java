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

package org.eclipse.birt.report.engine.emitter.html;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.api.IEmitterServices;
import org.eclipse.birt.report.engine.api.IHyperlinkProcessor;
import org.eclipse.birt.report.engine.content.IStyledElementContent;
import org.eclipse.birt.report.engine.emitter.DefaultHyperlinkProcessor;
import org.eclipse.birt.report.engine.emitter.EmbeddedHyperlinkProcessor;
import org.eclipse.birt.report.engine.emitter.IContainerEmitter;
import org.eclipse.birt.report.engine.emitter.IPageSetupEmitter;
import org.eclipse.birt.report.engine.emitter.IReportEmitter;
import org.eclipse.birt.report.engine.emitter.IReportItemEmitter;
import org.eclipse.birt.report.engine.emitter.ITableEmitter;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.StyleDesign;
import org.eclipse.birt.report.engine.ir.VisibilityDesign;
import org.eclipse.birt.report.engine.resource.IRepository;
import org.eclipse.birt.report.engine.resource.ResourceManager;

/**
 * <code>HTMLReportEmitter</code> is a concrete class that implements
 * IReportEmitter interface to output IARD Report ojbects to HTML file. It
 * creates HTMLWriter and HTML related Emitters say, HTMLTextEmitter,
 * HTMLTableEmitter, etc. Only one copy of each Emitter class exists.
 * 
 * @version $Revision: 1.10 $ $Date: 2005/03/11 07:52:51 $
 */
public class HTMLReportEmitter implements IReportEmitter
{

	public static String OUTPUT_FORMAT_HTML = "HTML"; //$NON-NLS-1$

	public static final String REPORT_FILE = "report.html"; //$NON-NLS-1$

	public static final String IMAGE_FOLDER = "image"; //$NON-NLS-1$

	public static final String CSS_FILE = "report.css"; //$NON-NLS-1$

	/**
	 * The <code>Report</code> object.
	 */
	private Report report;

	/**
	 * The
	 * <code>HTMLWriter<code> object that Emitters use to output HTML content.
	 */
	private HTMLWriter writer;

	/**
	 * The <code>HTMLImageEmitter</code> object that outputs image content.
	 */
	private HTMLImageEmitter imageEmitter;

	/**
	 * The <code>HTMLPageSetupEmitter</code> object that outputs page setup
	 * information content.
	 */
	private HTMLPageSetupEmitter pageSetupEmitter;

	/**
	 * The <code>HTMLTableEmitter</code> object that outputs table/grid
	 * content.
	 */
	private HTMLTableEmitter tableEmitter;

	/**
	 * The <code>HTMLTextEmitter</code> object that outputs text/label/data
	 * content.
	 */
	private HTMLTextEmitter textEmitter;

	/**
	 * The <code>HTMLContainerEmitter</code> object that outputs
	 * List/ListBand/FreeForm content.
	 */
	private HTMLContainerEmitter containerEmitter;

	/** Indicates that the styled element is hidden or not */
	protected Stack stack = new Stack( );

	/**
	 * An Log object that <code>HTMLReportEmitter</code> use to log the error,
	 * debug, information messages.
	 */
	protected static Logger logger = Logger.getLogger( HTMLReportEmitter.class
			.getName( ) );

	/**
	 * A resource manager.
	 */
	private ResourceManager resourceManager;

	/**
	 * A <code>IHyperlinkProcessor</code> object that customizes hyperlink.
	 */
	private IHyperlinkProcessor hyperlinkProcessor;

	/**
	 * emitter services
	 */
	private IEmitterServices services;

	/**
	 * Dow e need to save image files in temp location?
	 */
	private boolean saveImgFile = false;

	/**
	 * A <code>HashMap</code> object that maps a style name to actual output
	 * name of the style.
	 */
	private HashMap styleNameMapping = new HashMap( );

	/**
	 * Create a new <code>HTMLReportEmitter</code> object to output all the
	 * pages.
	 * 
	 * @param repository
	 *            The resource manager.
	 * @param hyperlinkProcessor
	 *            The hyperlink transformation object.
	 */
	public HTMLReportEmitter( )
	{
	}

	public void initialize( IEmitterServices services )
	{
		this.services = services;
		IRepository repository = services.getRepository( );
		saveImgFile = ( services.getEngineMode( ) == IEmitterServices.ENGINE_STANDALONE_MODE );

		writer = new HTMLWriter( );

		resourceManager = new ResourceManager( repository );

		if ( services.getEngineMode( ) == IEmitterServices.ENGINE_EMBEDDED_MODE )
			hyperlinkProcessor = new EmbeddedHyperlinkProcessor( services
					.getBaseURL( ) );
		else if ( services.getEngineMode( ) == IEmitterServices.ENGINE_STANDALONE_MODE )
			hyperlinkProcessor = new DefaultHyperlinkProcessor( );

		imageEmitter = new HTMLImageEmitter( this );
		pageSetupEmitter = new HTMLPageSetupEmitter( this );
		tableEmitter = new HTMLTableEmitter( this );
		textEmitter = new HTMLTextEmitter( this );
		containerEmitter = new HTMLContainerEmitter( this );
	}

	/**
	 * @return The <code>HTMLWriter</code> object.
	 */
	public HTMLWriter getWriter( )
	{
		return writer;
	}

	/**
	 * @return The hyperlink transformation object.
	 */
	public IHyperlinkProcessor getHyperlinkBuilder( )
	{
		return hyperlinkProcessor;
	}

	/**
	 * @return The resource manager.
	 */
	public ResourceManager getResourceManager( )
	{
		return resourceManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IReportEmitter#getTableEmitter()
	 */
	public ITableEmitter getTableEmitter( )
	{
		if ( pageSetupEmitter.isStartedMasterPage( ) )
		{
			return null;
		}

		return tableEmitter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IReportEmitter#getTextEmitter()
	 */
	public IReportItemEmitter getEmitter( String type )
	{
		if ( pageSetupEmitter.isStartedMasterPage( ) )
			return null;

		//TODO: Make this implementation more generic
		if ( type.equals( "image" ) ) //$NON-NLS-1$
			return imageEmitter;
		else if ( type.equals( "text" ) ) //$NON-NLS-1$
			return textEmitter;
		else
			return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IReportEmitter#getContainerEmitter()
	 */
	public IContainerEmitter getContainerEmitter( )
	{
		if ( pageSetupEmitter.isStartedMasterPage( ) )
		{
			return null;
		}

		return containerEmitter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IReportEmitter#getPageSetupEmitter()
	 */
	public IPageSetupEmitter getPageSetupEmitter( )
	{
		return pageSetupEmitter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IReportEmitter#getContainerEmitter()
	 */
	public void startPage( String masterPageRef )
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IReportEmitter#startEmitter()
	 */
	public void startReport( Report report )
	{
		logger.log( Level.FINE, "[HTMLReportEmitter] Start emitter." ); //$NON-NLS-1$

		this.report = report;

		OutputStream out = this.resourceManager.openOutputStream( REPORT_FILE );
		writer.open( out, "UTF-8" ); //$NON-NLS-1$

		writer.startWriter( );
		writer.openTag( HTMLTags.TAG_HTML ); //$NON-NLS-1$
		writer.openTag( HTMLTags.TAG_META ); //$NON-NLS-1$
		writer.attribute( HTMLTags.ATTR_HTTP_EQUIV, "Content-Type" ); //$NON-NLS-1$ 
		writer.attribute( HTMLTags.ATTR_CONTENT, "text/html; charset=UTF-8" ); //$NON-NLS-1$ 
		writer.closeNoEndTag( );

		writer.openTag( HTMLTags.TAG_HEAD ); 

		writer.openTag( HTMLTags.TAG_STYLE ); 
		writer.attribute( HTMLTags.ATTR_TYPE, 
				"text/css" ); //$NON-NLS-1$

		// output general styles
		writer
				.style(
						"table", "border-collapse: collapse; border-color: black; empty-cells: show;", true ); //$NON-NLS-1$ //$NON-NLS-2$ 

		StyleDesign style;
		StringBuffer styleBuffer = new StringBuffer( );
		if ( report == null )
		{
			logger.log( Level.WARNING,
					"[HTMLReportEmitter] Report object is null." ); //$NON-NLS-1$
		}
		else
		{
			ArrayList styleList = new ArrayList( );
			int styleNum = 0;
			int m;
			for ( int n = 0; n < report.getStyleCount( ); n++ )
			{
				styleBuffer.delete( 0, styleBuffer.capacity( ) );
				style = report.getStyle( n );
				if ( style != null )
				{
					if ( style.entrySet( ).size( ) == 0 )
					{
						styleNameMapping.put( style.getName( ), null );
					}
					else
					{
						StyleDesign tempStyle;
						for ( m = 0; m < styleNum; m++ )
						{
							tempStyle = (StyleDesign) styleList.get( m );
							if ( style.isSameStyle( tempStyle ) )
							{
								styleNameMapping.put( style.getName( ),
										tempStyle.getName( ) );
								break;
							}
						}

						if ( m == styleNum )
						{
							AttributeBuilder.buildStyle( styleBuffer, style,
									this );
							if ( styleBuffer.length( ) > 0 )
							{
								styleList.add( style );
								styleNum++;
								styleNameMapping.put( style.getName( ), style
										.getName( ) );

								writer.style( style.getName( ), styleBuffer
										.toString( ), false );
							}
							else
							{
								styleNameMapping.put( style.getName( ), null );
							}
						}
					}
				}
			}
		}

		writer.closeTag( HTMLTags.TAG_STYLE ); //$NON-NLS-1$

		writer.closeTag( HTMLTags.TAG_HEAD ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IReportEmitter#endEmitter()
	 */
	public void endReport( )
	{
		logger.log( Level.FINE, "[HTMLReportEmitter] End emitter." ); //$NON-NLS-1$
		writer.closeTag( HTMLTags.TAG_HTML ); 
		writer.endWriter( );
		writer.close( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IReportEmitter#startBody()
	 */
	public void startBody( )
	{
		logger.log( Level.FINE, "[HTMLReportEmitter] Start body." ); //$NON-NLS-1$

		writer.openTag( HTMLTags.TAG_BODY ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IReportEmitter#endBody()
	 */
	public void endBody( )
	{
		logger.log( Level.FINE, "[HTMLReportEmitter] End body." ); //$NON-NLS-1$

		writer.closeTag( HTMLTags.TAG_BODY ); //$NON-NLS-1$
	}

	/**
	 * @return the <code>Report</code> object.
	 */
	public Report getReport( )
	{
		return report;
	}

	/**
	 * Pushes the Boolean indicating whether or not the item is hidden according
	 * to the StyledELementItem
	 * 
	 * @param item
	 *            the StyledElementContent
	 */
	public void push( IStyledElementContent item )
	{
		boolean isHidden = false;
		if ( !stack.empty( ) )
		{
			isHidden = ( (Boolean) stack.peek( ) ).booleanValue( );
		}
		if ( !isHidden && item.isHidden( VisibilityDesign.FORMAT_TYPE_VIEWER ) )
		{
			isHidden = true;
		}
		stack.push( new Boolean( isHidden ) );
	}

	/**
	 * Pops the peek element of the stack and returns
	 * 
	 * @return Returns the boolean indicating whether or not the item is hidden
	 */
	public boolean pop( )
	{
		return ( (Boolean) stack.pop( ) ).booleanValue( );
	}

	/**
	 * Checks if the current item is hidden
	 * 
	 * @return Returns the boolean
	 */
	public boolean isHidden( )
	{
		return ( (Boolean) stack.peek( ) ).booleanValue( );
	}

	/**
	 * @return the <code>saveImgFile</code> flag.
	 */
	public boolean needSaveImgFile( )
	{
		return saveImgFile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IReportEmitter#getEmitterServices()
	 */
	public IEmitterServices getEmitterServices( )
	{
		return services;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IReportEmitter#getOutputFormat()
	 */
	public String getOutputFormat( )
	{
		// TODO Auto-generated method stub
		return OUTPUT_FORMAT_HTML;
	}

	/**
	 * @param name
	 *            the original style name
	 * @return the actual output style name
	 */
	public String getMappedStyleName( String name )
	{
		return (String) styleNameMapping.get( name );
	}
}