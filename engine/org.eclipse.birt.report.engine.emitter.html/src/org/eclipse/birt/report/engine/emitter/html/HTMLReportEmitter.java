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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.api.HTMLEmitterConfig;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IHTMLActionHandler;
import org.eclipse.birt.report.engine.api.IHTMLImageHandler;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.RenderOptionBase;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.IStyledElementContent;
import org.eclipse.birt.report.engine.emitter.IContainerEmitter;
import org.eclipse.birt.report.engine.emitter.IPageSetupEmitter;
import org.eclipse.birt.report.engine.emitter.IReportEmitter;
import org.eclipse.birt.report.engine.emitter.IReportItemEmitter;
import org.eclipse.birt.report.engine.emitter.ITableEmitter;
import org.eclipse.birt.report.engine.extension.IEmitterServices;
import org.eclipse.birt.report.engine.ir.VisibilityDesign;

/**
 * <code>HTMLReportEmitter</code> is a concrete class that implements
 * IReportEmitter interface to output IARD Report ojbects to HTML file. It
 * creates HTMLWriter and HTML related Emitters say, HTMLTextEmitter,
 * HTMLTableEmitter, etc. Only one copy of each Emitter class exists.
 * 
 * @version $Revision: 1.25 $ $Date: 2005/04/27 07:03:39 $
 */
public class HTMLReportEmitter implements IReportEmitter
{

	public static String OUTPUT_FORMAT_HTML = "HTML"; //$NON-NLS-1$

	public static final String REPORT_FILE = "report.html"; //$NON-NLS-1$

	public static final String IMAGE_FOLDER = "image"; //$NON-NLS-1$

	protected OutputStream out = null;
	/**
	 * The <code>Report</code> object.
	 */
	protected IReportContent report;
	
	protected IReportRunnable runnable;
	
	protected IRenderOption renderOption;

	/**
	 * Specifies if the HTML output is embeddable.
	 */
	protected boolean isEmbeddable = false;

	/**
	 * The
	 * <code>HTMLWriter<code> object that Emitters use to output HTML content.
	 */
	protected HTMLWriter writer;
	
	protected Object renderContext;

	/**
	 * The <code>HTMLImageEmitter</code> object that outputs image content.
	 */
	protected HTMLImageEmitter imageEmitter;

	/**
	 * The <code>HTMLPageSetupEmitter</code> object that outputs page setup
	 * information content.
	 */
	protected HTMLPageSetupEmitter pageSetupEmitter;

	/**
	 * The <code>HTMLTableEmitter</code> object that outputs table/grid
	 * content.
	 */
	protected HTMLTableEmitter tableEmitter;

	/**
	 * The <code>HTMLTextEmitter</code> object that outputs text/label/data
	 * content.
	 */
	protected HTMLTextEmitter textEmitter;

	/**
	 * The <code>HTMLContainerEmitter</code> object that outputs
	 * List/ListBand/FreeForm content.
	 */
	protected HTMLContainerEmitter containerEmitter;

	/** Indicates that the styled element is hidden or not */
	protected Stack stack = new Stack( );

	/**
	 * An Log object that <code>HTMLReportEmitter</code> use to log the error,
	 * debug, information messages.
	 */
	protected static Logger logger = Logger.getLogger( HTMLReportEmitter.class
			.getName( ) );


	protected IHTMLImageHandler imageHandler;
	protected IHTMLActionHandler actionHandler;



	/**
	 * emitter services
	 */
	protected IEmitterServices services;



	/**
	 * A <code>HashMap</code> object that maps a style name to actual output
	 * name of the style.
	 */
	protected HashMap styleNameMapping = new HashMap( );

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

		Object fd = services.getOption( RenderOptionBase.OUTPUT_FILE_NAME );
		File file = null;
		try
		{
			if(fd!=null)
			{
				file = new File(fd.toString());
				out = new BufferedOutputStream( new FileOutputStream( file ) );
			}
		}
		catch( FileNotFoundException e )
		{
			logger.log( Level.WARNING, e.getMessage( ), e );
		}
		
		if( out == null )
		{
			Object value = services.getOption( RenderOptionBase.OUTPUT_STREAM );
			if( value != null && value instanceof OutputStream )
			{
				out = ( OutputStream ) value;
			}
			else
			{
				try
				{
					//FIXME
					file = new File( REPORT_FILE );
					out = new BufferedOutputStream( new FileOutputStream( file ) );
				}
				catch ( FileNotFoundException e )
				{
					// FIXME
					logger.log(Level.SEVERE, e.getMessage( ), e );
				}
			}
		}
		
		Object emitterConfig = services.getEmitterConfig().get("html"); //$NON-NLS-1$
		if(emitterConfig!=null && emitterConfig instanceof HTMLEmitterConfig)
		{
			imageHandler = ((HTMLEmitterConfig)emitterConfig).getImageHandler();
			actionHandler = ((HTMLEmitterConfig)emitterConfig).getActionHandler();
		}
		
		Object im = services.getOption(HTMLRenderOption.IMAGE_HANDLER);
		if(im!=null && im instanceof IHTMLImageHandler)
		{
			imageHandler = (IHTMLImageHandler)im;
		}
		
		Object ac = services.getOption(HTMLRenderOption.ACTION_HANDLER);
		if(ac!=null && ac instanceof IHTMLActionHandler)
		{
			actionHandler = (IHTMLActionHandler)ac;
		}
				
		renderContext = services.getRenderContext();
		renderOption = services.getRenderOption();
		runnable = services.getReportRunnable();
		HTMLRenderOption renderOption = (HTMLRenderOption)services.getRenderOption();
		if(renderOption!=null)
		{
			isEmbeddable = renderOption.getEmbeddable();
		}

		writer = new HTMLWriter( );

		imageEmitter = new HTMLImageEmitter( this, isEmbeddable );
		pageSetupEmitter = new HTMLPageSetupEmitter( this, isEmbeddable );
		tableEmitter = new HTMLTableEmitter( this, isEmbeddable );
		textEmitter = new HTMLTextEmitter( this, isEmbeddable );
		containerEmitter = new HTMLContainerEmitter( this, isEmbeddable );
	}

	/**
	 * @return The <code>HTMLWriter</code> object.
	 */
	public HTMLWriter getWriter( )
	{
		return writer;
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
	public void startReport( IReportContent report )
	{
		logger.log( Level.FINE, "[HTMLReportEmitter] Start emitter." ); //$NON-NLS-1$

		this.report = report;
		writer.open( out, "UTF-8" ); //$NON-NLS-1$

		if ( isEmbeddable )
		{
			fixPng( );
			return;
		}

		writer.startWriter( );
		writer.openTag( HTMLTags.TAG_HTML );
		writer.openTag( HTMLTags.TAG_META );
		writer.attribute( HTMLTags.ATTR_HTTP_EQUIV, "Content-Type" ); //$NON-NLS-1$ 
		writer.attribute( HTMLTags.ATTR_CONTENT, "text/html; charset=UTF-8" ); //$NON-NLS-1$ 
		writer.closeNoEndTag( );

		writer.openTag( HTMLTags.TAG_HEAD );

		writer.openTag( HTMLTags.TAG_STYLE );
		writer.attribute( HTMLTags.ATTR_TYPE, "text/css" ); //$NON-NLS-1$

		// output general styles
		writer.style(
				"table", "border-collapse: collapse; empty-cells: show;", true ); //$NON-NLS-1$ //$NON-NLS-2$ 
		//MOZILLA/IE use middle as the default vertical align, but ROM defines the 
		//baseline instead. CSS uses baseline also. 
		writer.style( "td", "vertical-align: baseline;", true ); //$NON-NLS-1$ //$NON-NLS-2$ 

		IStyle style;
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
					if ( style.isEmpty( ) )
					{
						styleNameMapping.put( style.getName( ), null );
					}
					else
					{
						IStyle tempStyle;
						for ( m = 0; m < styleNum; m++ )
						{
							tempStyle = (IStyle) styleList.get( m );
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

		writer.closeTag( HTMLTags.TAG_STYLE );
		fixPng( );
		writer.closeTag( HTMLTags.TAG_HEAD );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IReportEmitter#endEmitter()
	 */
	public void endReport( )
	{
		logger.log( Level.FINE, "[HTMLReportEmitter] End emitter." ); //$NON-NLS-1$
		if ( !isEmbeddable )
		{
			writer.closeTag( HTMLTags.TAG_HTML );
		}
		writer.endWriter( );
		writer.close( );
		if( out != null )
		{
			try
			{
				out.close( );
			}
			catch ( IOException e )
			{
				logger.log( Level.WARNING, e.getMessage( ), e );
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IReportEmitter#startBody()
	 */
	public void startBody( )
	{
		logger.log( Level.FINE, "[HTMLReportEmitter] Start body." ); //$NON-NLS-1$
		IStyle bodyStyle = null;

		//in unittest, report may be null.
		if ( report != null )
		{
			bodyStyle = report.getBodyStyle( );
		}
		
		if ( !isEmbeddable )
		{
			writer.openTag( HTMLTags.TAG_BODY );
			if ( bodyStyle != null )
			{
				writer.attribute( HTMLTags.ATTR_CLASS,
						getMappedStyleName( bodyStyle.getName( ) ) );
			}
		}
		else
		{
			writer.openTag( HTMLTags.TAG_DIV );
			if ( bodyStyle != null )
			{
				StringBuffer styleBuffer = new StringBuffer( );
				AttributeBuilder.buildStyle( styleBuffer, bodyStyle, this );
				writer.attribute( HTMLTags.ATTR_STYLE, styleBuffer.toString( ) );
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IReportEmitter#endBody()
	 */
	public void endBody( )
	{
		logger.log( Level.FINE, "[HTMLReportEmitter] End body." ); //$NON-NLS-1$

		if ( !isEmbeddable )
		{
			writer.closeTag( HTMLTags.TAG_BODY );
		}
		else
		{
			writer.closeTag( HTMLTags.TAG_DIV );
		}
	}

	/**
	 * @return the <code>Report</code> object.
	 */
	public IReportContent getReport( )
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

	protected void fixPng( )
	{
		writer.writeCode( "<!--[if gte IE 5.5000]>" ); //$NON-NLS-1$
		writer
				.writeCode( "   <script language=\"JavaScript\"> var ie55up = true </script>" ); //$NON-NLS-1$
		writer.writeCode( "<![endif]-->" ); //$NON-NLS-1$
		writer.writeCode( "<script language=\"JavaScript\">" ); //$NON-NLS-1$
		writer
				.writeCode( "   function fixPNG(myImage) // correctly handle PNG transparency in Win IE 5.5 or higher." ); //$NON-NLS-1$
		writer.writeCode( "      {" ); //$NON-NLS-1$
		writer.writeCode( "      if (window.ie55up)" ); //$NON-NLS-1$
		writer.writeCode( "         {" ); //$NON-NLS-1$
		writer
				.writeCode( "         var imgID = (myImage.id) ? \"id='\" + myImage.id + \"' \" : \"\"" ); //$NON-NLS-1$
		writer
				.writeCode( "         var imgClass = (myImage.className) ? \"class='\" + myImage.className + \"' \" : \"\"" ); //$NON-NLS-1$
		writer
				.writeCode( "         var imgTitle = (myImage.title) ? \"title='\" + myImage.title + \"' \" : \"title='\" + myImage.alt + \"' \"" ); //$NON-NLS-1$
		writer
				.writeCode( "         var imgStyle = \"display:inline-block;\" + myImage.style.cssText" ); //$NON-NLS-1$
		writer
				.writeCode( "         var strNewHTML = \"<span \" + imgID + imgClass + imgTitle" ); //$NON-NLS-1$
		writer
				.writeCode( "         strNewHTML += \" style=\\\"\" + \"width:\" + myImage.width + \"px; height:\" + myImage.height + \"px;\" + imgStyle + \";\"" ); //$NON-NLS-1$
		writer
				.writeCode( "         strNewHTML += \"filter:progid:DXImageTransform.Microsoft.AlphaImageLoader\"" ); //$NON-NLS-1$
		writer
				.writeCode( "         strNewHTML += \"(src=\\'\" + myImage.src + \"\\', sizingMethod='scale');\\\"></span>\"" ); //$NON-NLS-1$
		writer.writeCode( "         myImage.outerHTML = strNewHTML" ); //$NON-NLS-1$
		writer.writeCode( "         }" ); //$NON-NLS-1$
		writer.writeCode( "      }" ); //$NON-NLS-1$
		writer.writeCode( "</script>" ); //$NON-NLS-1$
	}

}