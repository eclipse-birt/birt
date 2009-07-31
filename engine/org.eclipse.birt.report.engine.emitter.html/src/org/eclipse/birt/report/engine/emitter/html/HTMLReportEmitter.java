/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.html;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLEmitterConfig;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IHTMLActionHandler;
import org.eclipse.birt.report.engine.api.IHTMLImageHandler;
import org.eclipse.birt.report.engine.api.IImage;
import org.eclipse.birt.report.engine.api.IMetadataFilter;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.impl.Action;
import org.eclipse.birt.report.engine.api.impl.Image;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IColumn;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IElement;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IGroupContent;
import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IListBandContent;
import org.eclipse.birt.report.engine.content.IListGroupContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITableGroupContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter;
import org.eclipse.birt.report.engine.emitter.EmitterUtil;
import org.eclipse.birt.report.engine.emitter.HTMLTags;
import org.eclipse.birt.report.engine.emitter.HTMLWriter;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;
import org.eclipse.birt.report.engine.emitter.html.util.DiagonalLineImage;
import org.eclipse.birt.report.engine.emitter.html.util.HTMLEmitterUtil;
import org.eclipse.birt.report.engine.executor.ExecutionContext.ElementExceptionInfo;
import org.eclipse.birt.report.engine.executor.css.HTMLProcessor;
import org.eclipse.birt.report.engine.i18n.EngineResourceHandle;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.SimpleMasterPageDesign;
import org.eclipse.birt.report.engine.ir.StyledElementDesign;
import org.eclipse.birt.report.engine.ir.TemplateDesign;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.eclipse.birt.report.engine.parser.TextParser;
import org.eclipse.birt.report.engine.presentation.ContentEmitterVisitor;
import org.eclipse.birt.report.engine.util.SvgFile;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.IncludedCssStyleSheetHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.css.CSSValue;

import com.ibm.icu.util.ULocale;

/**
 * <code>HTMLReportEmitter</code> is a subclass of
 * <code>ContentEmitterAdapter</code> that implements IContentEmitter
 * interface to output IARD Report ojbects to HTML file.
 * 
 * <br>
 * Metadata information:<br>
 * <table border="solid;1px">
 * <tr>
 * <td rowspan="2">Item</td>
 * <td colspan="2">Output position</td>
 * </tr>
 * <tr>
 * <td>EnableMetadata=true</td>
 * <td>EnableMetadata=false</td>
 * </tr>
 * <tr>
 * <td>Container</td>
 * <td>On self</td>
 * <td>On self</td>
 * <tr>
 * <td>Table</td>
 * <td>On self</td>
 * <td>On self</td>
 * </tr>
 * <tr>
 * <td>Image</td>
 * <td>On container( for select handle )</td>
 * <td>Only bookmark is output on self</td>
 * </tr>
 * <tr>
 * <td>Chart</td>
 * <td>On container( for select handle )</td>
 * <td>Only bookmark is output on self</td>
 * </tr>
 * <tr>
 * <td>Foreign</td>
 * <td>On container( for select handle )</td>
 * <td>Only bookmark is output on self</td>
 * </tr>
 * <tr>
 * <td>Label</td>
 * <td>On container( for select handle )</td>
 * <td>Only bookmark is output on self</td>
 * </tr>
 * <tr>
 * <td>Template Items( including template table, template label, etc.)</td>
 * <td>On container( for select handle )</td>
 * <td>Only bookmark is output on self</td>
 * </tr>
 * </table>
 * 
 */
public class HTMLReportEmitter extends ContentEmitterAdapter
{

	protected boolean hasCsslinks = true;
	/**
	 * the output format
	 */
	public static final String OUTPUT_FORMAT_HTML = "html"; //$NON-NLS-1$

	/**
	 * the default target report file name
	 */
	public static final String REPORT_FILE = "report.html"; //$NON-NLS-1$

	/**
	 * the default image folder
	 */
	public static final String IMAGE_FOLDER = "image"; //$NON-NLS-1$

	/**
	 * output stream
	 */
	protected OutputStream out = null;

	/**
	 * the report content
	 */
	protected IReportContent report;

	/**
	 * the report runnable instance
	 */
	protected IReportRunnable runnable;

	/**
	 * the render options
	 */
	protected IRenderOption renderOption;

	/**
	 * should output the page header & footer
	 */
	protected boolean outputMasterPageContent = true;

	/**
	 * specifies if the HTML output is embeddable
	 */
	protected boolean isEmbeddable = false;

	/**
	 * the url encoding
	 */
	protected String urlEncoding = null;
	
	/**
	 * should we output the report as Right To Left
	 */
	protected boolean htmlRtLFlag = false;
	
	protected boolean pageFooterFloatFlag = true;

	protected boolean enableMetadata = false;

	protected List ouputInstanceIDs = null;

	/**
	 * specified the current page number, starting from 0
	 */
	protected int pageNo = 0;

	/**
	 * the <code>HTMLWriter<code> object that is used to output HTML content
	 */
	protected HTMLWriter writer;

	/**
	 * the context used to execute the report
	 */
	protected IReportContext reportContext;

	/**
	 * indicates that the styled element is hidden or not
	 */
	protected Stack stack = new Stack( );
	
	HashMap<Long, String> diagonalCellImageMap = new HashMap( );

	/**
	 * An Log object that <code>HTMLReportEmitter</code> use to log the error,
	 * debug, information messages.
	 */
	protected static Logger logger = Logger.getLogger( HTMLReportEmitter.class
			.getName( ) );

	/**
	 * html image handler
	 */
	protected IHTMLImageHandler imageHandler;

	/**
	 * html action handler
	 */
	protected IHTMLActionHandler actionHandler;

	/**
	 * emitter services
	 */
	protected IEmitterServices services;

	/**
	 * display type of Block
	 */
	protected static final int DISPLAY_BLOCK = 1;

	/**
	 * display type of Inline
	 */
	protected static final int DISPLAY_INLINE = 2;

	/**
	 * display type of Inline-Block
	 */
	protected static final int DISPLAY_INLINE_BLOCK = 4;

	/**
	 * display type of none
	 */
	protected static final int DISPLAY_NONE = 8;

	/**
	 * display flag which contains all display types
	 */
	protected static final int DISPLAY_FLAG_ALL = 0xffff;

	/**
	 * content visitor that is used to handle page header/footer
	 */
	protected ContentEmitterVisitor contentVisitor;

	protected MetadataEmitter metadataEmitter;
	
	protected IDGenerator idGenerator = new IDGenerator( );
	
	private String layoutPreference;
	private boolean fixedReport = false;
	private boolean enableAgentStyleEngine;
	private boolean outputMasterPageMargins;
	private IMetadataFilter metadataFilter = null;

	private boolean needOutputBackgroundSize = false;
	private boolean enableInlineStyle = false;

	/**
	 * Following names will be name spaced by htmlIDNamespace: a.CSS style name.
	 * b.id (bookmark). c.script name, which is created by BIRT.
	 */
	protected String htmlIDNamespace;
	protected int browserVersion;
	
	protected int imageDpi = -1;
	
	protected HTMLEmitter htmlEmitter;
	protected Stack tableDIVWrapedFlagStack = new Stack( );
	protected Stack<DimensionType> fixedRowHeightStack = new Stack<DimensionType>( );
	protected HashSet<String> outputBookmarks = new HashSet<String>( );
	
	/**
	 * This set is used to store the style class which has been outputted.
	 */
	private Set outputtedStyles = new HashSet();
	
	protected boolean needFixTransparentPNG = false;
	private ITableContent cachedStartTable = null;

	/**
	 * the constructor
	 */
	public HTMLReportEmitter( )
	{
		contentVisitor = new ContentEmitterVisitor( this );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#initialize(org.eclipse.birt.report.engine.emitter.IEmitterServices)
	 */
	public void initialize( IEmitterServices services ) throws EngineException
	{
		this.services = services;

		this.out = EmitterUtil.getOuputStream( services, REPORT_FILE );
		
		//FIXME: code review: solve the deprecated problem.
		Object emitterConfig = services.getEmitterConfig( ).get( "html" ); //$NON-NLS-1$
		if ( emitterConfig != null
				&& emitterConfig instanceof HTMLEmitterConfig )
		{
			imageHandler = ( (HTMLEmitterConfig) emitterConfig )
					.getImageHandler( );
			actionHandler = ( (HTMLEmitterConfig) emitterConfig )
					.getActionHandler( );
		}

		Object im = services.getOption( HTMLRenderOption.IMAGE_HANDLER );
		if ( im != null && im instanceof IHTMLImageHandler )
		{
			imageHandler = (IHTMLImageHandler) im;
		}

		Object ac = services.getOption( HTMLRenderOption.ACTION_HANDLER );
		if ( ac != null && ac instanceof IHTMLActionHandler )
		{
			actionHandler = (IHTMLActionHandler) ac;
		}

		reportContext = services.getReportContext( );

		renderOption = services.getRenderOption( );
		runnable = services.getReportRunnable( );
		writer = creatWriter( );
		if ( renderOption != null )
		{
			HTMLRenderOption htmlOption = new HTMLRenderOption( renderOption );
			isEmbeddable = htmlOption.getEmbeddable( );
			Map options = renderOption.getOutputSetting( );
			if ( options != null )
			{
				urlEncoding = (String) options
						.get( HTMLRenderOption.URL_ENCODING );
			}
			outputMasterPageContent = htmlOption.getMasterPageContent( );
			IHTMLActionHandler actHandler = htmlOption.getActionHandle( );
			if ( ac != null )
			{
				actionHandler = actHandler;
			}
			pageFooterFloatFlag = htmlOption.getPageFooterFloatFlag( );
			//htmlRtLFlag = htmlOption.getHtmlRtLFlag( );
			enableMetadata = htmlOption.getEnableMetadata( );
			if ( enableMetadata )
			{
				metadataFilter = htmlOption.getMetadataFilter( );
				if ( metadataFilter == null )
				{
					metadataFilter = new MetadataFilter( );
				}
			}
			ouputInstanceIDs = htmlOption.getInstanceIDs( );
			metadataEmitter = creatMetadataEmitter( writer, htmlOption );
			layoutPreference = htmlOption.getLayoutPreference( );
			enableAgentStyleEngine = htmlOption.getEnableAgentStyleEngine( );
			outputMasterPageMargins = htmlOption.getOutputMasterPageMargins( );
			htmlIDNamespace = htmlOption.getHTMLIDNamespace( );
			if ( null != htmlIDNamespace )
			{
				if( htmlIDNamespace.length( ) > 0 ) 
				{
					htmlIDNamespace += "_";
					metadataEmitter.setHTMLIDNamespace( htmlIDNamespace );
				}
				else
				{
					htmlIDNamespace = null;
				}
			}
			writer.setIndent( htmlOption.getHTMLIndent( ) );
			if ( isEmbeddable )
			{
				enableInlineStyle = htmlOption.getEnableInlineStyle( );
			}
			browserVersion = HTMLEmitterUtil.getBrowserVersion( htmlOption.getUserAgent( ) );
			if ( browserVersion == HTMLEmitterUtil.BROWSER_IE5
					|| browserVersion == HTMLEmitterUtil.BROWSER_IE6 )
			{
				needFixTransparentPNG = true;
			}
		}
	}
	
	protected HTMLWriter creatWriter( )
	{
		return new HTMLWriter( );
	}
	
	protected MetadataEmitter creatMetadataEmitter( HTMLWriter writer,
			HTMLRenderOption htmlOption )
	{
		return new MetadataEmitter( writer, htmlOption, null, idGenerator, this );
	}

	/**
	 * @return the <code>Report</code> object.
	 */
	public IReportContent getReport( )
	{
		return report;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#getOutputFormat()
	 */
	public String getOutputFormat( )
	{
		return OUTPUT_FORMAT_HTML;
	}

	/**
	 * Fixes a PNG problem related to transparency. See
	 * http://homepage.ntlworld.com/bobosola/ for detail.
	 */
	protected void fixTransparentPNG( )
	{
		// Because the IE 7 start to support the transparent PNG directly, this
		// function only be needed for the IE5.5 and IE6.
		writer.writeCode( "<!--[if (gte IE 5.5000)&(lt IE 7)]>" ); //$NON-NLS-1$
		writer.writeCode( "   <script  type=\"text/javascript\">" ); //$NON-NLS-1$
		writer.writeCode( "    //<![CDATA" ); //$NON-NLS-1$
		if ( htmlIDNamespace == null )
		{
			writer.writeCode( "      var ie55up = true;" ); //$NON-NLS-1$
		}
		else
		{
			writer.writeCode( "      var " + htmlIDNamespace + "ie55up = true;" ); //$NON-NLS-1$
		}
		writer.writeCode( "    //]]>" ); //$NON-NLS-1$
		writer.writeCode( "   </script>" ); //$NON-NLS-1$
		writer.writeCode( "<![endif]-->" ); //$NON-NLS-1$
		writer.writeCode( "<script type=\"text/javascript\">" ); //$NON-NLS-1$
		writer.writeCode( " //<![CDATA[" ); //$NON-NLS-1$
		if ( null == htmlIDNamespace )
		{
			writer.writeCode( "   function fixPNG(myImage) // correctly handle PNG transparency in Win IE 5.5 or IE 6." ); //$NON-NLS-1$
			writer.writeCode( "      {" ); //$NON-NLS-1$
			writer.writeCode( "       if ( window.ie55up )" ); //$NON-NLS-1$
		}
		else
		{
			writer.writeCode( "   function " + htmlIDNamespace + "fixPNG(myImage) // correctly handle PNG transparency in Win IE 5.5 or higher." ); //$NON-NLS-1$
			writer.writeCode( "      {" ); //$NON-NLS-1$
			writer.writeCode( "       if ( window." + htmlIDNamespace + "ie55up )" ); //$NON-NLS-1$
		}
		writer.writeCode( "          {" ); //$NON-NLS-1$
		writer.writeCode( "           var imgID = (myImage.id) ? \"id='\" + myImage.id + \"' \" : \"\";" ); //$NON-NLS-1$
		writer.writeCode( "           var imgClass = (myImage.className) ? \"class='\" + myImage.className + \"' \" : \"\";" ); //$NON-NLS-1$
		writer.writeCode( "           var imgTitle = (myImage.title) ? \"title='\" + myImage.title + \"' \" : \"title='\" + myImage.alt + \"' \";" ); //$NON-NLS-1$
		writer.writeCode( "           var imgStyle = \"display:inline-block;\" + myImage.style.cssText;" ); //$NON-NLS-1$
		writer.writeCode( "           var strNewHTML = \"<span \" + imgID + imgClass + imgTitle;" ); //$NON-NLS-1$
		writer.writeCode( "           strNewHTML += \" style=\\\"\" + \"width:\" + myImage.width + \"px; height:\" + myImage.height + \"px;\" + imgStyle + \";\";" ); //$NON-NLS-1$
		writer.writeCode( "           strNewHTML += \"filter:progid:DXImageTransform.Microsoft.AlphaImageLoader\";" ); //$NON-NLS-1$
		writer.writeCode( "           strNewHTML += \"(src=\\'\" + myImage.src + \"\\', sizingMethod='scale');\\\"></span>\";" ); //$NON-NLS-1$
		writer.writeCode( "           myImage.outerHTML = strNewHTML;" ); //$NON-NLS-1$
		writer.writeCode( "          }" ); //$NON-NLS-1$
		writer.writeCode( "      }" ); //$NON-NLS-1$
		writer.writeCode( " //]]>" ); //$NON-NLS-1$
		writer.writeCode( "</script>" ); //$NON-NLS-1$
	}
	
	/**
	 * Fixes the security issues when redirecting page in IE7.
	 */
	protected void fixRedirect( )
	{
		writer.writeCode( "<script type=\"text/javascript\">" ); //$NON-NLS-1$
		writer.writeCode( " //<![CDATA[" ); //$NON-NLS-1$
		if ( htmlIDNamespace == null )
		{
			writer.writeCode( "   function redirect(target, url){" ); //$NON-NLS-1$
		}
		else
		{
			writer.writeCode( "   function " + htmlIDNamespace + "redirect(target, url){" ); //$NON-NLS-1$
		}
		writer.writeCode( "       if (target =='_blank'){" ); //$NON-NLS-1$
		writer.writeCode( "           open(url);" ); //$NON-NLS-1$
		writer.writeCode( "       }" ); //$NON-NLS-1$
		writer.writeCode( "       else if (target == '_top'){" ); //$NON-NLS-1$
		writer.writeCode( "           window.top.location.href=url;" ); //$NON-NLS-1$                                                                                                                                         
		writer.writeCode( "       }" ); //$NON-NLS-1$
		writer.writeCode( "       else if (target == '_parent'){" ); //$NON-NLS-1$
		writer.writeCode( "           location.href=url;" ); //$NON-NLS-1$                                                                                                                                    
		writer.writeCode( "       }" ); //$NON-NLS-1$
		writer.writeCode( "       else if (target == '_self'){" );//$NON-NLS-1$
		writer.writeCode( "           location.href =url;" ); //$NON-NLS-1$                                                                                                                                   
		writer.writeCode( "       }" ); //$NON-NLS-1$                                    
		writer.writeCode( "       else{" );//$NON-NLS-1$
		writer.writeCode( "           open(url);" ); //$NON-NLS-1$
		writer.writeCode( "       }" ); //$NON-NLS-1$
		writer.writeCode( "      }" ); //$NON-NLS-1$
		writer.writeCode( " //]]>" ); //$NON-NLS-1$
		writer.writeCode( "</script>" ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#start(org.eclipse.birt.report.engine.content.IReportContent)
	 */
	public void start( IReportContent report )
	{
		logger.log( Level.FINE, "[HTMLReportEmitter] Start emitter." ); //$NON-NLS-1$

		this.report = report;
		writer.open( out, "UTF-8" ); //$NON-NLS-1$
		
		ReportDesignHandle designHandle= null;
		Report reportDesign = null;
		if ( report != null )
		{
			reportDesign = report.getDesign( );
			designHandle = reportDesign.getReportDesign( );

			// Get dpi.
			Map appContext = reportContext.getAppContext( );
			if ( appContext != null )
			{
				Object tmp = appContext.get( EngineConstants.APPCONTEXT_CHART_RESOLUTION );
				if ( tmp != null && tmp instanceof Number )
				{
					imageDpi = ( (Number) tmp ).intValue( );
				}
			}
			if ( imageDpi <= 0 )
			{
				imageDpi = designHandle.getImageDPI( );
			}
			if ( imageDpi <= 0 )
			{
				// Set default image dpi.
				imageDpi = 96;
			}
		}
		retrieveRtLFlag( ); // bidi_hcg
		if ( null == layoutPreference )
		{
			// get the layout preference from the report design.
			if ( designHandle != null )
			{
				String reportLayoutPreference = designHandle.getLayoutPreference( );
				if ( DesignChoiceConstants.REPORT_LAYOUT_PREFERENCE_FIXED_LAYOUT.equals( reportLayoutPreference ) )
				{
					layoutPreference = HTMLRenderOption.LAYOUT_PREFERENCE_FIXED;
					fixedReport = true;
				}
				else if ( DesignChoiceConstants.REPORT_LAYOUT_PREFERENCE_AUTO_LAYOUT.equals( reportLayoutPreference ) )
				{
					layoutPreference = HTMLRenderOption.LAYOUT_PREFERENCE_AUTO;
					fixedReport = false;
				}
			}
		}
		else
		{
			fixedReport = HTMLRenderOption.LAYOUT_PREFERENCE_FIXED.equals( layoutPreference );
		}
		if ( enableAgentStyleEngine )
		{
			htmlEmitter = new HTMLPerformanceOptimize( this,
					writer,
					fixedReport,
					enableInlineStyle,
					browserVersion );
		}
		else
		{
			// we will use HTMLVisionOptimize as the default emitter.
			htmlEmitter = new HTMLVisionOptimize( this,
					writer,
					fixedReport,
					enableInlineStyle,
					htmlRtLFlag,
					browserVersion );
		}
		
		if ( isEmbeddable )
		{
			outputCSSStyles( reportDesign, designHandle );
			if ( needFixTransparentPNG )
			{
				fixTransparentPNG( );
			}
			fixRedirect( );

			openRootTag( );
			writeBidiFlag( );
			// output the report default style
			if ( report != null )
			{
				String defaultStyleName = report.getDesign( ).getRootStyleName( );
				if ( defaultStyleName != null )
				{
					if ( enableInlineStyle )
					{
						StringBuffer defaultStyleBuffer = new StringBuffer( );
						IStyle defaultStyle = report.findStyle( defaultStyleName );
						htmlEmitter.buildDefaultStyle( defaultStyleBuffer,
								defaultStyle );
						if ( defaultStyleBuffer.length( ) > 0 )
						{
							writer.attribute( HTMLTags.ATTR_STYLE,
									defaultStyleBuffer.toString( ) );
						}
					}
					else
					{
						if ( htmlIDNamespace != null )
						{
							writer.attribute( HTMLTags.ATTR_CLASS, htmlIDNamespace
									+ defaultStyleName );
						}
						else
						{
							writer.attribute( HTMLTags.ATTR_CLASS, defaultStyleName );
						}
					}
				}
			}

			return;
		}

		openRootTag( );
        writeBidiFlag( );
		writer.openTag( HTMLTags.TAG_HEAD );
		
		// write the title of the report in html.
		outputReportTitle( designHandle );
				
		writer.openTag( HTMLTags.TAG_META );
		writer.attribute( HTMLTags.ATTR_HTTP_EQUIV, "Content-Type" ); //$NON-NLS-1$ 
		writer.attribute( HTMLTags.ATTR_CONTENT, "text/html; charset=utf-8" ); //$NON-NLS-1$ 
		writer.closeTag( HTMLTags.TAG_META );

		outputCSSStyles( reportDesign, designHandle );

		if ( needFixTransparentPNG )
		{
			fixTransparentPNG( );
		}
		fixRedirect( );
		writer.closeTag( HTMLTags.TAG_HEAD );

		writer.openTag( HTMLTags.TAG_BODY );
		// output the report default style
		StringBuffer defaultStyleBuffer = new StringBuffer( );
		if ( report != null )
		{
			String defaultStyleName = report.getDesign( ).getRootStyleName( );
			if ( defaultStyleName != null )
			{
				if ( enableInlineStyle )
				{
					IStyle defaultStyle = report.findStyle( defaultStyleName );
					htmlEmitter.buildDefaultStyle( defaultStyleBuffer,
							defaultStyle );
				}
				else
				{
					if ( htmlIDNamespace != null )
					{
						writer.attribute( HTMLTags.ATTR_CLASS, htmlIDNamespace
								+ defaultStyleName );
					}
					else
					{
						writer.attribute( HTMLTags.ATTR_CLASS, defaultStyleName );
					}
				}
			}
		}
		
		if ( outputMasterPageContent )
		{
			// remove the default margin of the html body
			defaultStyleBuffer.append( " margin:0px;" );
		}

		if ( defaultStyleBuffer.length( ) > 0 )
		{
			writer.attribute( HTMLTags.ATTR_STYLE,
					defaultStyleBuffer.toString( ) );
		}
	}
	
	/**
	 * open the report root tag.
	 */
	protected void openRootTag( )
	{
		if ( isEmbeddable )
		{
			writer.openTag( HTMLTags.TAG_DIV );
		}
		else
		{
			//The document type must be output before open the "html" tag.
			writer.outputDoctype( );
			writer.openTag( HTMLTags.TAG_HTML );
		}
	}
	
	/**
	 * output the report title.
	 */
	protected void outputReportTitle( ReportDesignHandle designHandle )
	{
		// write the title of the report in HTML.
		String title = null;
		if ( designHandle != null )
		{
			title = designHandle.getStringProperty( IModuleModel.TITLE_PROP );
		}
		if ( title == null )
		{
			// set the default title
			if ( renderOption != null )
			{
				HTMLRenderOption htmlOption = new HTMLRenderOption( renderOption );
				title = htmlOption.getHtmlTitle( );
			}
		}
		if ( title != null )
		{
			writer.openTag( HTMLTags.TAG_TITLE );
			writer.text( title );
			writer.closeTag( HTMLTags.TAG_TITLE );
		}
	}
	
	private void outputCSSStyles( Report reportDesign,
			ReportDesignHandle designHandle )
	{
		if ( report == null )
		{
			logger.log( Level.WARNING,
					"[HTMLReportEmitter] Report object is null." ); //$NON-NLS-1$
		}
		else
		{
			// output the report CSS styles
			if ( !enableInlineStyle )
			{
				openStyleSheet( );
				String styleNamePrefix;
				if ( null != htmlIDNamespace )
				{
					styleNamePrefix = "." + htmlIDNamespace;
				}
				else
				{
					styleNamePrefix = ".";
				}
				String defaultStyleName = reportDesign.getRootStyleName( );
				Map styles = reportDesign.getStyles( );
				Iterator iter = styles.entrySet( ).iterator( );
				while ( iter.hasNext( ) )
				{
					Map.Entry entry = (Map.Entry) iter.next( );
					String styleName = (String) entry.getKey( );
					if ( styleName != null )
					{
						IStyle style = (IStyle) entry.getValue( );
						StringBuffer styleBuffer = new StringBuffer( );
						if ( styleName.equals( defaultStyleName ) )
						{
							htmlEmitter.buildDefaultStyle( styleBuffer, style );
						}
						else
						{
							htmlEmitter.buildStyle( styleBuffer, style );
						}

						if ( styleBuffer.length( ) > 0 )
						{
							writer.style( styleNamePrefix + styleName,
									styleBuffer.toString( ) );
							outputtedStyles.add( styleName );
						}
					}
				}
				closeStyleSheet( );
			}
		}

		if ( hasCsslinks )
		{
			// export the CSS links in the HTML
			hasCsslinks = false;
			if ( designHandle != null )
			{
				List externalCsses = designHandle.getAllExternalIncludedCsses( );
				if ( null != externalCsses )
				{
					Iterator iter = externalCsses.iterator( );
					while ( iter.hasNext( ) )
					{
						IncludedCssStyleSheetHandle cssStyleSheetHandle = (IncludedCssStyleSheetHandle) iter.next( );
						String href = cssStyleSheetHandle.getExternalCssURI( );
						if ( href != null )
						{
							hasCsslinks = true;
							writer.openTag( HTMLTags.TAG_LINK );
							writer.attribute( HTMLTags.ATTR_REL, "stylesheet" );
							writer.attribute( HTMLTags.ATTR_TYPE, "text/css" );
							writer.attribute( HTMLTags.ATTR_HREF, href );
							writer.closeTag( HTMLTags.TAG_LINK );
						}
					}
				}
			}
		}
	}

	protected void openStyleSheet( )
	{
		writer.openTag( HTMLTags.TAG_STYLE );
		writer.attribute( HTMLTags.ATTR_TYPE, "text/css" ); //$NON-NLS-1$
	}

	protected void closeStyleSheet( )
	{
		writer.closeTag( HTMLTags.TAG_STYLE );
	}

	private void appendErrorMessage(EngineResourceHandle rc, int index, ElementExceptionInfo info )
	{
		writer.writeCode( "			<div>" );
		writer.writeCode( "				<div  id=\"error_title\" style=\"text-decoration:underline\">" );
		String name = info.getName( );
		if ( name != null )
		{
			writer.text( rc.getMessage( MessageConstants.REPORT_ERROR_MESSAGE,
				new Object[]{info.getType( ), name } ), false );
		}
		else
		{
			writer.text( rc.getMessage( MessageConstants.REPORT_ERROR_MESSAGE_WITH_ID,
					new Object[]{info.getType( ), info.getID( ) } ), false );
		}
			writer.writeCode( "</div>" );//$NON-NLS-1$
			
		ArrayList errorList = info.getErrorList( );
		ArrayList countList = info.getCountList( );
		for ( int i = 0; i < errorList.size( ); i++ )
		{
			String errorId = "document.getElementById('error_detail" + index + "_" + i + "')";
			String errorIcon = "document.getElementById('error_icon" + index + "_" + i + "')";
			String onClick = "if (" + errorId + ".style.display == 'none') { "
					+ errorIcon + ".innerHTML = '- '; " + errorId
					+ ".style.display = 'block'; }" + "else { " + errorIcon
					+ ".innerHTML = '+ '; " + errorId + ".style.display = 'none'; }";
			writer.writeCode("<div>");
			BirtException ex = (BirtException) errorList.get( i );
			writer.writeCode( "<span id=\"error_icon" + index + "_" + i
					+ "\"  style=\"cursor:pointer\" onclick=\"" + onClick
					+ "\" > + </span>" );
			
			writer.text( ex.getLocalizedMessage( ) ); 
			
			
		
			writer.writeCode( "				<pre id=\"error_detail" + index + "_" + i //$NON-NLS-1$
				+ "\" style=\"display:none;\" >" );//$NON-NLS-1$
		
			

			String messageTitle = rc.getMessage(
					MessageConstants.REPORT_ERROR_ID, new Object[]{
							ex.getErrorCode( ) ,
							countList.get( i )} );
			String detailTag = rc
					.getMessage( MessageConstants.REPORT_ERROR_DETAIL );
			String messageBody = getDetailMessage( ex );
			boolean indent = writer.isIndent( );
			writer.setIndent( false );
			writer.text( messageTitle, false );
			writer.writeCode( "\r\n" );//$NON-NLS-1$
			writer.text( detailTag, false );
			writer.text( messageBody, false );
			writer.setIndent( indent );
			writer.writeCode( "				</pre>" ); //$NON-NLS-1$
			writer.writeCode("</div>");
		}
		
		writer.writeCode( "</div>" ); //$NON-NLS-1$
		writer.writeCode( "<br>" ); //$NON-NLS-1$
	}

	private String getDetailMessage( Throwable t )
	{
		StringWriter out = new StringWriter( );
		PrintWriter print = new PrintWriter( out );
		try
		{
			t.printStackTrace( print );
		}
		catch ( Throwable ex )
		{
		}
		print.flush( );
		return out.getBuffer( ).toString( );
	}

	protected boolean outputErrors( List errors )
	{
		// Outputs the error message at the end of the report
		if ( errors != null && !errors.isEmpty( ) )
		{
			writer.writeCode( "	<hr style=\"color:red\"/>" );
			writer.writeCode( "	<div style=\"color:red\">" );
			writer.writeCode( "		<div>" );
			Locale locale = reportContext.getLocale( );
			if(locale==null)
			{
				locale = Locale.getDefault();
			}
			EngineResourceHandle rc = new EngineResourceHandle( ULocale.forLocale(locale) );
			writer.text( rc.getMessage(
					MessageConstants.ERRORS_ON_REPORT_PAGE ), false );
			
			writer.writeCode( "</div>" );//$NON-NLS-1$
			writer.writeCode( "<br>") ;//$NON-NLS-1$
			Iterator it = errors.iterator( );
			int index = 0;
			while ( it.hasNext( ) )
			{
				appendErrorMessage(rc, index++, (ElementExceptionInfo) it.next( ) );
			}
			writer.writeCode( "</div>" );
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#end(org.eclipse.birt.report.engine.content.IReportContent)
	 */
	public void end( IReportContent report )
	{
		logger.log( Level.FINE, "[HTMLReportEmitter] End body." ); //$NON-NLS-1$
		if ( report != null )
		{
			List errors = report.getErrors( );
			if ( errors != null && !errors.isEmpty( ) )
			{
				outputErrors( errors );
			}
		}
		if ( !isEmbeddable )
		{
			writer.closeTag( HTMLTags.TAG_BODY );
			writer.closeTag( HTMLTags.TAG_HTML );
		}
		else
		{
			writer.closeTag( HTMLTags.TAG_DIV );
		}

		writer.endWriter( );
		writer.close( );
		if ( out != null )
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

	private boolean isSameUnit( String unit1, String unit2 )
	{
		if ( unit1 == unit2 )
		{
			return true;
		}
		if ( unit1 != null && unit1.equals( unit2 ) )
		{
			return true;
		}
		return false;
	}

	private DimensionType getPageWidth( IPageContent page )
	{
		DimensionType pageWidth = page.getPageWidth( );
		if ( !outputMasterPageMargins )
		{
			DimensionType leftMargin = page.getMarginLeft( );
			DimensionType rightMargin = page.getMarginRight( );
			return removeMargin( pageWidth, leftMargin, rightMargin );
		}
		return pageWidth;

	}

	private DimensionType getPageHeight( IPageContent page )
	{
		DimensionType pageHeight = page.getPageHeight( );
		if ( !outputMasterPageMargins )
		{
			DimensionType topMargin = page.getMarginTop( );
			DimensionType bottomMargin = page.getMarginBottom( );

			return removeMargin( pageHeight, topMargin, bottomMargin );
		}

		return pageHeight;
	}

	private DimensionType removeMargin( DimensionType pageWidth,
			DimensionType leftMargin, DimensionType rightMargin )
	{
		double measure = pageWidth.getMeasure( );
		String unit = pageWidth.getUnits( );
		if ( leftMargin != null && isSameUnit( unit, leftMargin.getUnits( ) ) )
		{
			measure -= leftMargin.getMeasure( );
		}
		if ( rightMargin != null && isSameUnit( unit, rightMargin.getUnits( ) ) )
		{
			measure -= rightMargin.getMeasure( );
		}
		if ( measure > 0 )
		{
			return new DimensionType( measure, unit );
		}
		return pageWidth;
	}

	private void outputColumn( DimensionType dm )
	{
		writer.openTag( HTMLTags.TAG_COL );

		StringBuffer styleBuffer = new StringBuffer( );
		styleBuffer.append( "width: " );
		if ( dm != null )
		{
			styleBuffer.append( dm.toString( ) );
		}
		else
		{
			styleBuffer.append( "0pt" );
		}
		styleBuffer.append( ";" );
		writer.attribute( HTMLTags.ATTR_STYLE, styleBuffer.toString( ) );
		writer.closeTag( HTMLTags.TAG_COL );
	}

	private void outputVMargin( DimensionType margin )
	{
		// If margin isn't null, output a row to implement it.
		if ( null != margin )
		{
			writer.openTag( HTMLTags.TAG_TR );
			StringBuffer styleBuffer = new StringBuffer( );
			styleBuffer.append( "height: " );
			styleBuffer.append( margin.toString( ) );
			styleBuffer.append( ";" );
			writer.attribute( HTMLTags.ATTR_STYLE, styleBuffer.toString( ) );
			writer.openTag( HTMLTags.TAG_TD );
			writer.attribute( HTMLTags.ATTR_COLSPAN, 3 );
			writer.closeTag( HTMLTags.TAG_TD );
			writer.closeTag( HTMLTags.TAG_TR );
		}
	}

	private void outputHMargin( DimensionType margin )
	{
		writer.openTag( HTMLTags.TAG_TD );
		if ( null != margin )
		{
			writer.openTag( HTMLTags.TAG_DIV );
			StringBuffer styleBuffer = new StringBuffer( );
			styleBuffer.append( "width: " );
			styleBuffer.append( margin.toString( ) );
			styleBuffer.append( ";" );
			writer.attribute( HTMLTags.ATTR_STYLE, styleBuffer.toString( ) );
			writer.closeTag( HTMLTags.TAG_DIV );
		}
		writer.closeTag( HTMLTags.TAG_TD );
	}

	boolean showPageHeader( IPageContent page )
	{
		boolean showHeader = true;
		Object genBy = page.getGenerateBy( );
		if ( genBy instanceof SimpleMasterPageDesign )
		{
			SimpleMasterPageDesign masterPage = (SimpleMasterPageDesign) genBy;
			if ( !masterPage.isShowHeaderOnFirst( ) )
			{
				if ( page.getPageNumber( ) == 1 )
				{
					showHeader = false;
				}
			}
		}
		return showHeader;
	}

	boolean showPageFooter( IPageContent page )
	{
		boolean showFooter = true;
		Object genBy = page.getGenerateBy( );
		if ( genBy instanceof SimpleMasterPageDesign )
		{
			SimpleMasterPageDesign masterPage = (SimpleMasterPageDesign) genBy;
			if ( !masterPage.isShowFooterOnLast( ) )
			{
				long totalPage = page.getPageNumber( );
				IReportContent report = page.getReportContent( );
				if ( report != null )
				{
					totalPage = report.getTotalPage( );
				}
				if ( page.getPageNumber( ) == totalPage )
				{
					showFooter = false;
				}
			}
		}
		return showFooter;
	}

	private void outputPageBand( IPageContent page, IContent band )
			throws BirtException
	{
		writer.openTag( HTMLTags.TAG_TD );
		writeBidiFlag( );
		StringBuffer styleBuffer = new StringBuffer( );
		htmlEmitter.buildPageBandStyle( styleBuffer, page.getStyle( ) );
		// output the page header attribute
		writer.attribute( HTMLTags.ATTR_STYLE, styleBuffer.toString( ) );

		contentVisitor.visitChildren( band, null );

		// close the page header
		writer.closeTag( HTMLTags.TAG_TD );
	}

	/**
	 * The page layout is controlled by three render options:
	 * 
	 * <ul>
	 * <li>OUTPUT-MASTER-PAGE</li>
	 * <li>OUTPUT-MARGIN</li>
	 * <li>FLOATING-FOOTER</li>
	 * </ul>
	 * 
	 * The layout effect matrix are demostrate in following table:
	 * 
	 * <table border="all">
	 * <tr>
	 * <th>PAGE</th>
	 * <th>MARGIN</th>
	 * <th>FOOTER</th>
	 * <th>effect</th>
	 * </tr>
	 * <tr valign="top">
	 * <td rowspan="4">TRUE</td>
	 * <td rowspan="2">TRUE</td>
	 * <td >FALSE</td>
	 * <td> <table border="all" style="width:2in;height:2in;"> <col
	 * width="0.3in"/> <col width="100%"/> <col width="0.3in"/>
	 * <tr style="height:0.2in;">
	 * <td colspan="3">top-margin</td>
	 * </tr>
	 * <tr>
	 * <td>LM</td>
	 * <td valign="top">header</td>
	 * <td>RM</td>
	 * </tr>
	 * <tr style="height:100%">
	 * <td>LM</td>
	 * <td><div>body</div></td>
	 * <td>RM</td>
	 * </tr>
	 * <tr>
	 * <td>LM</td>
	 * <td><div>footer</div></td>
	 * <td>RM</td>
	 * </tr>
	 * <tr style="height:0.2in" >
	 * <td colspan="3"><div>bottom-margin</div></td>
	 * </tr>
	 * <table> </td>
	 * </table>
	 * <tr valign="top">
	 * <td>TRUE</td>
	 * <td> <table border="all" style="width:2in;"> <col width="0.3in"/> <col
	 * width="100%"/> <col width="0.3in"/>
	 * <tr style="height:0.2in;">
	 * <td colspan="3">top-margin</td>
	 * </tr>
	 * <tr>
	 * <td >LM</td>
	 * <td valign="top"><div>header</div></td>
	 * <td >RM</td>
	 * </tr>
	 * <tr>
	 * <td>LM</td>
	 * <td><div>body</div></td>
	 * <td>RM</td>
	 * </tr>
	 * <tr style="height:100%">
	 * <td>LM</td>
	 * <td valign="top"><div>footer</div></td>
	 * <td>RM</td>
	 * </tr>
	 * <tr style="height:0.2in" >
	 * <td colspan="3"><div>bottom-margin</div></td>
	 * </tr>
	 * </table> </td>
	 * </tr>
	 * <tr valign="top">
	 * <td rowspan="2">FALSE</td>
	 * <td>TRUE</td>
	 * <td> <table border="all" style="width:1.6in;height:1in;"> <col/>
	 * <tr>
	 * <td valign="top"><div>header</div></td>
	 * </tr>
	 * <tr style="height:100%">
	 * <td><div>body</div></td>
	 * </tr>
	 * <tr>
	 * <td><div>footer</div></td>
	 * </tr>
	 * </table> </td>
	 * </tr>
	 * <tr>
	 * <td>FALSE</td>
	 * <td> <table border="all" style="width:1.6in;"> <col/>
	 * <tr>
	 * <td valign="top"><div>header</div></td>
	 * </tr>
	 * <tr>
	 * <td><div>body</div></td>
	 * </tr>
	 * <tr>
	 * <td><div>footer</div></td>
	 * </tr>
	 * </table> </td>
	 * </tr>
	 * <tr>
	 * <td>FALSE</td>
	 * <td>ANY</td>
	 * <td>ANY</td>
	 * <td>
	 * 
	 * <table border="all" style="width:1.6in;">
	 * <tr>
	 * <td>BODY</td>
	 * </tr>
	 * </table> </td>
	 * </tr>
	 * </table>
	 */
	public void startPage( IPageContent page ) throws BirtException
	{
		pageNo++;

		if ( pageNo > 1 && outputMasterPageContent == false )
		{
			writer.openTag( "hr" );
			writer.closeTag( "hr" );
		}

		if ( pageNo > 1 )
		{
			writer.writeCode( " <div style=\"visibility: hidden; height: 0px; overflow: hidden; page-break-after: always;\">page separator</div>" );
		}

		// out put the page tag
		DimensionType width = null;
		DimensionType height = null;
		if ( page != null && outputMasterPageContent )
		{
			width = getPageWidth( page );
			height = getPageHeight( page );
			if ( width != null && height != null && fixedReport
					&& !pageFooterFloatFlag )
				startBackgroundContainer( page.getStyle( ), width, height );
		}

		StringBuffer styleBuffer = new StringBuffer( );
		writer.openTag( HTMLTags.TAG_TABLE );
		writer.attribute( "cellpadding", "0" );
		styleBuffer.append( " border-collapse: collapse; empty-cells: show;" ); //$NON-NLS-1$

		if ( page != null && outputMasterPageContent )
		{
			htmlEmitter.buildPageStyle( page, styleBuffer,
					needOutputBackgroundSize );
			// build the width
			if ( fixedReport && width != null )
			{
				styleBuffer.append( " width:" );
				styleBuffer.append( width.toString( ) );
				styleBuffer.append( ";" );
			}
			else if ( !fixedReport )
			{
				styleBuffer.append( " width:100%;" );
			}

			if ( !pageFooterFloatFlag && height != null )
			{
				styleBuffer.append( " height:" );
				styleBuffer.append( height.toString( ) );
				styleBuffer.append( ";" );
			}

			if ( fixedReport )
			{
				// hide the overflow
				styleBuffer.append( " overflow: hidden;" );
				styleBuffer.append( " table-layout:fixed;" );
			}
		}
		else
		{
			styleBuffer.append( "width:100%;" );
		}
		writer.attribute( HTMLTags.ATTR_STYLE, styleBuffer.toString( ) );

		if ( page != null && outputMasterPageContent )
		{
			if ( outputMasterPageMargins )
			{
				// Implement left margin.
				outputColumn( page.getMarginLeft( ) );
			}

			writer.openTag( HTMLTags.TAG_COL );
			writer.closeTag( HTMLTags.TAG_COL );

			if ( outputMasterPageMargins )
			{
				// Implement right margin.
				outputColumn( page.getMarginLeft( ) );

				// If top margin isn't null, output a row to implement it.
				outputVMargin( page.getMarginTop( ) );
			}

			// we need output the page header
			if ( showPageHeader( page ) )
			{
				writer.openTag( HTMLTags.TAG_TR );
				if ( outputMasterPageMargins )
				{
					outputHMargin( page.getMarginLeft( ) );
				}
				outputPageBand( page, page.getPageHeader( ) );
				if ( outputMasterPageMargins )
				{
					outputHMargin( page.getMarginRight( ) );
				}
				writer.closeTag( HTMLTags.TAG_TR );
			}
		}

		// output the page body
		writer.openTag( HTMLTags.TAG_TR );
		if ( !pageFooterFloatFlag )
		{
			writer.attribute( HTMLTags.ATTR_STYLE, "height:100%;" );
		}
		if ( page != null && outputMasterPageContent && outputMasterPageMargins )
		{
			outputHMargin( page.getMarginLeft( ) );
		}
		writer.openTag( HTMLTags.TAG_TD );
		writer.attribute( "valign", "top" );
		writeBidiFlag( );
	}

	private void startBackgroundContainer( IStyle style,
			DimensionType pageWidth, DimensionType pageHeight )
	{
		String backgroundHeight = parseBackgroundSize( style
				.getBackgroundHeight( ), pageHeight );
		String backgroundWidth = parseBackgroundSize( style
				.getBackgroundWidth( ), pageWidth );
		if ( backgroundHeight == null && backgroundWidth == null )
		{
			return;
		}
		else
		{
			if ( backgroundHeight == null )
			{
				backgroundHeight = "auto";
			}
			if ( backgroundWidth == null )
			{
				backgroundWidth = "auto";
			}
		}

		String image = style.getBackgroundImage( );
		if ( image == null || "none".equalsIgnoreCase( image ) ) //$NON-NLS-1$
		{
			return;
		}
		needOutputBackgroundSize = true;
		String backgroundPositionX = style.getBackgroundPositionX( );
		if ( backgroundPositionX == null )
		{
			backgroundPositionX = "0px";
		}
		String backgroundPositionY = style.getBackgroundPositionY( );
		if ( backgroundPositionY == null )
		{
			backgroundPositionY = "0px";
		}

		writer.openTag( HTMLTags.TAG_DIV );
		writer.attribute( HTMLTags.ATTR_STYLE, "width:" + pageWidth
				+ ";height:" + pageHeight + ";" );
		writer.openTag( HTMLTags.TAG_IMAGE );
		writer.attributeAllowEmpty( HTMLTags.ATTR_ALT, "" );

		image = handleStyleImage( image, true );
		if ( image != null && image.length( ) > 0 )
		{
			writer.attribute( HTMLTags.ATTR_SRC, image );
			writer.attribute( HTMLTags.ATTR_STYLE, "width:" + backgroundWidth
					+ ";height:" + backgroundHeight
					+ ";position:absolute;left:" + backgroundPositionX
					+ ";top:" + backgroundPositionY + ";z-index:-1" );
		}
		writer.closeTag( HTMLTags.TAG_IMAGE );
	}

	private String parseBackgroundSize( String backgroundHeight,
			DimensionType pageHeight )
	{
		if ( backgroundHeight == null )
			return null;
		backgroundHeight = backgroundHeight.trim( );
		if ( backgroundHeight.endsWith( "%" ) )
		{
			try
			{
				String percent = backgroundHeight.substring( 0,
						backgroundHeight.length( ) - 1 );
				int percentValue = Integer.valueOf( percent ).intValue( );
				return pageHeight.getMeasure( ) * percentValue / 100
						+ pageHeight.getUnits( );
			}
			catch ( NumberFormatException e )
			{
				return null;
			}
		}
		return backgroundHeight;
	}

	private void endBackgroundContainer( )
	{
		writer.closeTag( HTMLTags.TAG_DIV );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#endPage(org.eclipse.birt.report.engine.content.IPageContent)
	 */
	public void endPage( IPageContent page ) throws BirtException
	{

		logger.log( Level.FINE, "[HTMLReportEmitter] End page." ); //$NON-NLS-1$

		// close the page body (TR)
		writer.closeTag( HTMLTags.TAG_TD );

		// output the right margin
		if ( page != null && outputMasterPageContent && outputMasterPageMargins )
		{
			outputHMargin( page.getMarginRight( ) );
		}
		writer.closeTag( HTMLTags.TAG_TR );

		// output the footer and bottom margin
		if ( page != null && outputMasterPageContent )
		{
			if ( showPageFooter( page ) )
			{
				writer.openTag( HTMLTags.TAG_TR );
				if ( outputMasterPageMargins )
				{
					outputHMargin( page.getMarginLeft( ) );
				}
				outputPageBand( page, page.getPageFooter( ) );
				if ( outputMasterPageMargins )
				{
					outputHMargin( page.getMarginRight( ) );
				}
				writer.closeTag( HTMLTags.TAG_TR );
			}
			if ( outputMasterPageMargins )
			{
				outputVMargin( page.getMarginBottom( ) );
			}
		}
		// close the page tag ( TABLE )
		writer.closeTag( HTMLTags.TAG_TABLE );
		if ( needOutputBackgroundSize )
		{
			endBackgroundContainer( );
			needOutputBackgroundSize = false;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#startTable(org.eclipse.birt.report.engine.content.ITableContent)
	 */
	public void startTable( ITableContent table )
	{
		cachedStartTable = table;
	}

	protected void doStartTable( ITableContent table )
	{
		assert table != null;

		boolean DIVWrap = false;
		// The method getStyle( ) will nevel return a null value;
		IStyle style = table.getStyle( );
		
		// If the top level table has the property text-align, the table should
		// be align to the page box.
		if ( needImplementAlignTable( table ) )
		{
			writer.openTag( HTMLTags.TAG_DIV );
			DIVWrap = true;
			writer.attribute( HTMLTags.ATTR_ALIGN, style.getTextAlign( ) );
		}
		
		// implement the inline table
		CSSValue display = style.getProperty( IStyle.STYLE_DISPLAY );
		if ( IStyle.INLINE_VALUE == display || IStyle.INLINE_BLOCK_VALUE == display )
		{
			if( !DIVWrap )
			{
				writer.openTag( HTMLTags.TAG_DIV );
				DIVWrap = true;
			}
			// For the IE6 the display value will be "inline", because the IE6
			// can't identify the "!important".
			// The IE7, IE8 and Firefox support "!important".
			// IE7 hasn't impelmented inline-block. So we must output special
			// diaplsy string for IE7.
			// For the Firefox 1.5 and 2 the display value will be
			// "-moz-inline-box", because only the Firefox 3 implement the
			// "inline-block".
			// For the Firefox 3 the display value will be "inline-block".
			if ( browserVersion == HTMLEmitterUtil.BROWSER_IE7 )
			{
				// IE7 hasn't impelmented inline-block. 
				writer.attribute( HTMLTags.ATTR_STYLE,
						" display:-moz-inline-box !important; display:inline;" );
			}
			else
			{
				writer.attribute( HTMLTags.ATTR_STYLE,
						" display:-moz-inline-box !important; display:inline-block !important; display:inline;" );
			}
		}
		
		tableDIVWrapedFlagStack.push( new Boolean( DIVWrap ) );
		
		logger.log( Level.FINE, "[HTMLTableEmitter] Start table" ); //$NON-NLS-1$
		//FIXME: code review: use "metadataEmitter != null" to instead of enableMetadata.
		if ( enableMetadata )
		{
			metadataEmitter.startWrapTable( table );
		}
		writer.openTag( HTMLTags.TAG_TABLE );
		
		// output class attribute.
		String styleClass = table.getStyleClass( );
		setStyleName( styleClass, table );

		//FIXME: we need reimplement the table's inline value.
		StringBuffer styleBuffer = new StringBuffer( );
		htmlEmitter.buildTableStyle( table, styleBuffer );
		// output style
		writer.attribute( HTMLTags.ATTR_STYLE, styleBuffer.toString( ) );

		boolean bookmarkOutput = false;
		if ( metadataFilter != null )
		{
			bookmarkOutput = metadataEmitter.outputMetadataProperty( metadataFilter.needMetaData( HTMLEmitterUtil.getElementHandle( table ) ),
					table,
					HTMLTags.TAG_TABLE );
		}
		if ( !bookmarkOutput )
		{
			// bookmark
			String bookmark = table.getBookmark( );
			if ( bookmark == null )
			{
				bookmark = idGenerator.generateUniqueID( );
				table.setBookmark( bookmark );
			}
			outputBookmark( table, HTMLTags.TAG_TABLE );
		}

		//table summary
		String summary = table.getSummary( );
		writer.attribute( HTMLTags.ATTR_SUMMARY, summary );
		
		// table caption
		String caption = table.getCaption( );
		if ( caption != null && caption.length( ) > 0 )
		{
			writer.openTag( HTMLTags.TAG_CAPTION );
			writer.text( caption );
			writer.closeTag( HTMLTags.TAG_CAPTION );
		}

		// include select handle table
		if ( enableMetadata )
		{
			metadataEmitter.startTable( table );
		}

		writeColumns( table );
	}

	protected void writeColumns( ITableContent table )
	{
		for ( int i = 0; i < table.getColumnCount( ); i++ )
		{
			IColumn column = table.getColumn( i );
			
			writer.openTag( HTMLTags.TAG_COL );
			
			// output class attribute.
			if ( enableAgentStyleEngine )
			{
				// only performance optimize model needs output the column's
				// class attribute. In vision optimize model the column style is
				// output in Cell's columnRelatedStyle, except the column's
				// width.
				String styleClass = column.getStyleClass( );
				setStyleName( styleClass,table );
			}

			//column style is output in Cell's columnRelatedStyle

			// width
			StringBuffer styleBuffer = new StringBuffer( );
			htmlEmitter.buildColumnStyle( column, styleBuffer );
			writer.attribute( HTMLTags.ATTR_STYLE, styleBuffer.toString( ) );
			htmlEmitter.handleColumnAlign( column );
			
			if ( metadataFilter != null )
			{
				metadataEmitter.outputMetadataProperty( metadataFilter.needMetaData( HTMLEmitterUtil.getElementHandle( column ) ),
						column,
						HTMLTags.TAG_COL );
			}
			
			writer.closeTag( HTMLTags.TAG_COL );
		}
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#endTable(org.eclipse.birt.report.engine.content.ITableContent)
	 */
	public void endTable( ITableContent table )
	{
		if ( cachedStartTable != null )
		{
			cachedStartTable = null;
			return;
		}
		//	include select handle table
		if ( enableMetadata )
		{
			metadataEmitter.endTable( table );
		}
				
		writer.closeTag( HTMLTags.TAG_TABLE );

		if ( enableMetadata )
		{
			metadataEmitter.endWrapTable( table );
		}
		
		boolean DIVWrap = ( (Boolean) tableDIVWrapedFlagStack.pop( ) ).booleanValue( );
		if ( DIVWrap )
		{
			writer.closeTag( HTMLTags.TAG_DIV );
		}

		logger.log( Level.FINE, "[HTMLTableEmitter] End table" ); //$NON-NLS-1$
	}

	/**
	 * Judge needing implement the align table or not.
	 * The align table should be align according to the page box.
	 * @param table
	 * @return
	 */
	protected boolean needImplementAlignTable( ITableContent table )
	{
		// the table should be the top level.
		if ( report.getRoot( ) == table.getParent( ) )
		{
			// The table must has the width, and the width is not 100%.
			DimensionType width = table.getWidth( );
			if ( null != width && !"100%".equals( width.toString( ) ) )
			{
				// The table must be a block table.
				IStyle style = table.getStyle( );
				CSSValue display = style.getProperty( IStyle.STYLE_DISPLAY );
				if ( null == display || IStyle.BLOCK_VALUE == display )
				{
					// The text-align value must be center or right.
					CSSValue align = style.getProperty( IStyle.STYLE_TEXT_ALIGN );

					// bidi_hcg start
					// If alignment is inconsistent with direction we need to
					// be explicit for non-center alignment (i.e. alignment
					// left and dir is RTL or alignment right and dir is LTR.
					if ( IStyle.CENTER_VALUE.equals( align ) )
							// XXX Is justify here applicable?
							// || IStyle.JUSTIFY_VALUE.equals( align ) )
					{
						return true;
					}
					CSSValue direction = style.getProperty( IStyle.STYLE_DIRECTION );
					if ( IStyle.RTL_VALUE.equals(direction) )
					{
						if ( !IStyle.RIGHT_VALUE.equals( align ) )
						{
							return true;
						}
					}
					else
					// bidi_hcg end

					if( /*IStyle.CENTER_VALUE == align || */IStyle.RIGHT_VALUE == align)
					{
						return true;
					}
				}
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#startTableHeader(org.eclipse.birt.report.engine.content.ITableBandContent)
	 */
	public void startTableHeader( ITableBandContent band )
	{
		writer.openTag( HTMLTags.TAG_THEAD );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#endTableHeader(org.eclipse.birt.report.engine.content.ITableBandContent)
	 */
	public void endTableHeader( ITableBandContent band )
	{
		writer.closeTag( HTMLTags.TAG_THEAD );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#startTableBody(org.eclipse.birt.report.engine.content.ITableBandContent)
	 */
	public void startTableBody( ITableBandContent band )
	{
		writer.openTag( HTMLTags.TAG_TBODY );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#endTableBody(org.eclipse.birt.report.engine.content.ITableBandContent)
	 */
	public void endTableBody( ITableBandContent band )
	{
		writer.closeTag( HTMLTags.TAG_TBODY );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#startTableFooter(org.eclipse.birt.report.engine.content.ITableBandContent)
	 */
	public void startTableFooter( ITableBandContent band )
	{
		writer.openTag( HTMLTags.TAG_TFOOT );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#endTableFooter(org.eclipse.birt.report.engine.content.ITableBandContent)
	 */
	public void endTableFooter( ITableBandContent band )
	{
		writer.closeTag( HTMLTags.TAG_TFOOT );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#startRow(org.eclipse.birt.report.engine.content.IRowContent)
	 */
	public void startRow( IRowContent row )
	{
		assert row != null;

		if ( cachedStartTable != null )
		{
			doStartTable( cachedStartTable );
			cachedStartTable = null;
		}

		writer.openTag( HTMLTags.TAG_TR );
		if ( metadataFilter != null )
		{
			metadataEmitter.outputMetadataProperty( metadataFilter.needMetaData( HTMLEmitterUtil.getElementHandle( row ) ),
					row,
					HTMLTags.TAG_TR );
		}
		if ( enableMetadata )
		{
			metadataEmitter.startRow( row );
		}

		// output class attribute.
		String styleClass = row.getStyleClass( );
		setStyleName( styleClass, row );

		// bookmark
		outputBookmark(  row, null );

		StringBuffer styleBuffer = new StringBuffer( );
		htmlEmitter.buildRowStyle( row, styleBuffer );
		writer.attribute( HTMLTags.ATTR_STYLE, styleBuffer.toString( ) );
		htmlEmitter.handleRowAlign( row );
		
		if ( !startedGroups.isEmpty( ) )
		{
			IGroupContent group = (IGroupContent) startedGroups.firstElement( );
			String bookmark = group.getBookmark( );
			if ( bookmark == null )
			{
				bookmark = idGenerator.generateUniqueID( );
				group.setBookmark( bookmark );
			}
			outputBookmark( group, null );
			startedGroups.remove( group );
		}
		
		if ( fixedReport )
		{
			DimensionType rowHeight = row.getHeight( );
			if ( rowHeight != null && !"%".equals( rowHeight.getUnits( ) ) )
			{
				fixedRowHeightStack.push( rowHeight );
			}
			else
			{
				fixedRowHeightStack.push( null );
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#endRow(org.eclipse.birt.report.engine.content.IRowContent)
	 */
	public void endRow( IRowContent row )
	{
		if ( enableMetadata )
		{
			metadataEmitter.endRow( row );
		}
		// assert currentData != null;
		//
		// currentData.adjustCols( );
		writer.closeTag( HTMLTags.TAG_TR );

		if ( fixedReport )
		{
			fixedRowHeightStack.pop( );
		}
	}

	private boolean isCellInHead( ICellContent cell )
	{
		IElement row = cell.getParent( );
		if ( row instanceof IRowContent )
		{
			IElement tableBand = row.getParent( );
			if ( tableBand instanceof ITableBandContent )
			{
				int type = ( (ITableBandContent) tableBand ).getBandType( );
				if ( type == ITableBandContent.BAND_HEADER )
				{
					// is the table head
					return true;
				}
			}

			IColumn column = cell.getColumnInstance( );
			if ( null != column )
			{
				// return whether this column is a column header.
				return column.isColumnHeader( );
			}
		}
		
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#startCell(org.eclipse.birt.report.engine.content.ICellContent)
	 */
	public void startCell( ICellContent cell )
	{				
		logger.log( Level.FINE, "[HTMLTableEmitter] Start cell." ); //$NON-NLS-1$
			
		// output 'th' tag in table head, otherwise 'td' tag
		String tagName = null;
		boolean isHead = isCellInHead( cell ); 
		if ( isHead )
		{
			tagName = HTMLTags.TAG_TH;
		}
		else
		{
			tagName = HTMLTags.TAG_TD;
		}
		writer.openTag( tagName ); //$NON-NLS-1$
		writer.attribute( "scope", cell.getScope( ) );
		writer.attribute( "id", cell.getBookmark( ) );
		writer.attribute( "headers", cell.getHeaders( ) );
		// output class attribute.
		String styleClass = cell.getStyleClass( );
		setStyleName( styleClass, cell );

		// colspan
		int colSpan = cell.getColSpan( );

		if ( colSpan > 1 )
		{
			writer.attribute( HTMLTags.ATTR_COLSPAN, colSpan );
		}

		boolean fixedCellHeight = false;
		DimensionType cellHeight = null;
		// rowspan
		int rowSpan = cell.getRowSpan( );
		if ( rowSpan > 1 )
		{
			writer.attribute( HTMLTags.ATTR_ROWSPAN, rowSpan );
		}
		else if ( fixedReport )
		{
			// fixed cell height requires the rowspan to be 1.
			cellHeight = (DimensionType) fixedRowHeightStack.peek( );
			if ( cellHeight != null )
			{
				fixedCellHeight = true;
			}
		}

		StringBuffer styleBuffer = new StringBuffer( );
		htmlEmitter.buildCellStyle( cell, styleBuffer, isHead, fixedCellHeight );
		writer.attribute( HTMLTags.ATTR_STYLE, styleBuffer.toString( ) );

		htmlEmitter.handleCellAlign( cell );
		if ( fixedCellHeight )
		{
			// Fixed cell height requires the vertical aline must be top.
			writer.attribute( HTMLTags.ATTR_VALIGN, "top" );
		}
		else
		{
			htmlEmitter.handleCellVAlign( cell );
		}

		boolean bookmarkOutput = false;
		if ( metadataFilter != null )
		{
			bookmarkOutput = metadataEmitter.outputMetadataProperty( metadataFilter.needMetaData( HTMLEmitterUtil.getElementHandle( cell ) ),
					cell,
					tagName );
		}

		if ( !startedGroups.isEmpty( ) )
		{
			if ( !bookmarkOutput )
			{
				IGroupContent group = (IGroupContent) startedGroups.firstElement( );
				String bookmark = group.getBookmark( );
				if ( bookmark == null )
				{
					bookmark = idGenerator.generateUniqueID( );
					group.setBookmark( bookmark );
				}
				outputBookmark( group, null );
				startedGroups.remove( group );
			}
			
			Iterator iter = startedGroups.iterator( );
			while ( iter.hasNext( ) )
			{
				IGroupContent group = (IGroupContent) iter.next( );
				outputBookmark( group );
			}
			startedGroups.clear( );
		}

		if ( fixedCellHeight )
		{
			writer.openTag( HTMLTags.TAG_DIV );
			writer.attribute( HTMLTags.ATTR_STYLE,
					"position: relative; height: 100%;" );
			if ( cell.hasDiagonalLine( ) )
			{
				outputDiagonalImage( cell, cellHeight );
			}
			writer.openTag( HTMLTags.TAG_DIV );
			styleBuffer.setLength( 0 );
			styleBuffer.append( " height: " );
			styleBuffer.append( cellHeight.toString( ) );
			styleBuffer.append( "; width: 100%; overflow: hidden; position: absolute; left: 0px;" );
			writer.attribute( HTMLTags.ATTR_STYLE, styleBuffer.toString( ) );
		}
		else if ( cell.hasDiagonalLine( ) )
		{
			if ( cellHeight == null )
			{
				cellHeight = getCellHeight( cell );
			}
			if ( cellHeight != null && !"%".equals( cellHeight.getUnits( ) ) )
			{
				writer.openTag( HTMLTags.TAG_DIV );
				writer.attribute( HTMLTags.ATTR_STYLE,
						"position: relative; height: 100%;" );
				outputDiagonalImage( cell, cellHeight );
			}
		}

		if ( enableMetadata )
		{
			metadataEmitter.startCell( cell );
		}
	}
	
	protected void outputDiagonalImage( ICellContent cell,
			DimensionType cellHeight )
	{
		String imgUri = diagonalCellImageMap.get( cell.getInstanceID( )
				.getComponentID( ) );
		if ( imgUri == null )
		{
			// prepare to get the diagnal line image.
			DiagonalLineImage imageCreater = new DiagonalLineImage( );
			imageCreater.setDiagonalLine( cell.getDiagonalNumber( ),
					cell.getDiagonalStyle( ),
					cell.getDiagonalWidth( ),
					cell.getDiagonalColor( ) );
			imageCreater.setAntidiagonalLine( cell.getAntidiagonalNumber( ),
					cell.getAntidiagonalStyle( ),
					cell.getAntidiagonalWidth( ),
					cell.getAntidiagonalColor( ) );
			imageCreater.setImageDpi( imageDpi );
			imageCreater.setImageSize( getCellWidth( cell ), cellHeight );
			IStyle cellComputedStyle = cell.getComputedStyle( );
			String strColor = cellComputedStyle.getColor( );
			imageCreater.setColor( PropertyUtil.getColor( strColor ) );
			byte[] imageByteArray = null;
			try
			{
				// draw the diagnal line image.
				imageByteArray = imageCreater.drawImage( );
			}
			catch ( IOException e )
			{
				logger.log( Level.WARNING, e.getMessage( ), e );
			}
			if ( imageByteArray != null )
			{
				// get the image URI.
				Image image = new Image( imageByteArray, cell.getInstanceID( )
						.toUniqueString( ), ".png" );
				image.setReportRunnable( runnable );
				image.setRenderOption( renderOption );
				imgUri = imageHandler.onCustomImage( image, reportContext );
				if ( imgUri != null )
				{
					// Cache the image URI.
					diagonalCellImageMap.put( cell.getInstanceID( )
							.getComponentID( ), imgUri );
				}
			}
		}

		// FIXME: We should continue to improve the HTML source of how to output
		// the diagonal line image.
		// FIXME: We still need to solve the confilct between the cell's
		// background and the diagonal line imag.
		if ( imgUri != null )
		{
			writer.openTag( HTMLTags.TAG_IMAGE );
			writer.attributeAllowEmpty( HTMLTags.ATTR_ALT, "" );
			writer.attribute( HTMLTags.ATTR_SRC, imgUri );
			StringBuffer styleBuffer = new StringBuffer( );
			styleBuffer.append( " min-height: " );
			styleBuffer.append( cellHeight.toString( ) );
			styleBuffer.append( "; height: 100%; width: 100%; position: absolute; z-index: -1; left: 0px;" );
			writer.attribute( HTMLTags.ATTR_STYLE, styleBuffer.toString( ) );
			if ( needFixTransparentPNG )
			{
				if ( null == htmlIDNamespace )
				{
					writer.attribute( HTMLTags.ATTR_ONLOAD, "fixPNG(this)" ); //$NON-NLS-1$
				}
				else
				{
					writer.attribute( HTMLTags.ATTR_ONLOAD, htmlIDNamespace
							+ "fixPNG(this)" ); //$NON-NLS-1$
				}
			}
			writer.closeTag( HTMLTags.TAG_IMAGE );
		}
	}

	protected DimensionType getCellWidth( ICellContent cell )
	{
		IColumn column = cell.getColumnInstance( );
		if ( null != column )
		{
			return column.getWidth( );
		}
		return null;
	}

	protected DimensionType getCellHeight( ICellContent cell )
	{
		IElement row = cell.getParent( );
		if ( row instanceof IRowContent )
		{
			return ( (IRowContent) row ).getHeight( );
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#endCell(org.eclipse.birt.report.engine.content.ICellContent)
	 */
	public void endCell( ICellContent cell )
	{
		logger.log( Level.FINE, "[HTMLReportEmitter] End cell." ); //$NON-NLS-1$

		if ( enableMetadata )
		{
			metadataEmitter.endCell( cell );
		}
		
		boolean fixedRowHeight = ( fixedReport && fixedRowHeightStack.peek( ) != null );
		if ( fixedRowHeight && cell.getRowSpan( ) == 1 )
		{
			writer.closeTag( HTMLTags.TAG_DIV );
			writer.closeTag( HTMLTags.TAG_DIV );
		}
		else if ( cell.hasDiagonalLine( ) )
		{
			DimensionType cellHeight = getCellHeight( cell );
			if ( cellHeight != null && !"%".equals( cellHeight.getUnits( ) ) )
			{
				writer.closeTag( HTMLTags.TAG_DIV );
			}
		}
		
		if ( isCellInHead( cell )	)
		{
			writer.closeTag( HTMLTags.TAG_TH );
		}
		else
		{
			writer.closeTag( HTMLTags.TAG_TD );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#startContainer(org.eclipse.birt.report.engine.content.IContainerContent)
	 */
	// FIXME: code review: only the list element using the startContainer. So
	// rename this method to startList.
	public void startContainer( IContainerContent container )
	{
		logger.log( Level.FINE, "[HTMLReportEmitter] Start container" ); //$NON-NLS-1$

		htmlEmitter.openContainerTag( container );

		// output class attribute.
		String styleClass = container.getStyleClass( );
		setStyleName( styleClass, container );
		
		boolean bookmarkOutput = false;
		if ( metadataFilter != null )
		{
			bookmarkOutput = metadataEmitter.outputMetadataProperty( metadataFilter.needMetaData( HTMLEmitterUtil.getElementHandle( container ) ),
					container,
					HTMLTags.TAG_DIV );
		}
		if ( !bookmarkOutput )
		{
			// bookmark
			String bookmark = container.getBookmark( );

			if ( bookmark == null )
			{
				bookmark = idGenerator.generateUniqueID( );
				container.setBookmark( bookmark );
			}
			outputBookmark( container, HTMLTags.TAG_DIV );
		}

		StringBuffer styleBuffer = new StringBuffer( );
		htmlEmitter.buildContainerStyle( container, styleBuffer );
		writer.attribute( HTMLTags.ATTR_STYLE, styleBuffer.toString( ) );
		htmlEmitter.handleContainerAlign( container );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#endContainer(org.eclipse.birt.report.engine.content.IContainerContent)
	 */
	public void endContainer( IContainerContent container )
	{
		htmlEmitter.closeContainerTag( );

		logger.log( Level.FINE, "[HTMLContainerEmitter] End container" ); //$NON-NLS-1$
	}

	// FIXME: code review: text and foreign need a code review. Including how to
	// implement the vertical, where the properties should be outputted at, the
	// metadata shouldn't open a new tag, and so on.
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#startText(org.eclipse.birt.report.engine.content.ITextContent)
	 */
	public void startText( ITextContent text )
	{
		IStyle mergedStyle = text.getStyle( );

		logger.log( Level.FINE, "[HTMLReportEmitter] Start text" ); //$NON-NLS-1$

		DimensionType x = text.getX( );
		DimensionType y = text.getY( );
		DimensionType width = text.getWidth( );
		DimensionType height = text.getHeight( );
		String textValue = text.getText( );
		if ( textValue == null || "".equals( textValue ) ) //$NON-NLS-1$
		{
			textValue = " "; //$NON-NLS-1$
		}

		int display = htmlEmitter.getTextElementType( x, y, width, height, mergedStyle );
		// action
		String tagName = openTagByType( display, DISPLAY_FLAG_ALL );

		boolean bookmarkOutput = false;
		if ( metadataFilter != null )
		{
			bookmarkOutput = metadataEmitter.outputMetadataProperty( metadataFilter.needMetaData( HTMLEmitterUtil.getElementHandle( text ) ),
					text,
					tagName );
		}
		
		// output class attribute.
		String styleClass = text.getStyleClass( );
		setStyleName( styleClass, text);

		// bookmark
		if ( !bookmarkOutput )
		{
			outputBookmark( text, tagName );
		}
		
		// title
		writer.attribute( HTMLTags.ATTR_TITLE, text.getHelpText( ) ); //$NON-NLS-1$
		
		StringBuffer styleBuffer = new StringBuffer( );
		htmlEmitter.buildTextStyle( text, styleBuffer, display );
		writer.attribute( HTMLTags.ATTR_STYLE, styleBuffer.toString( ) );
		
		htmlEmitter.handleVerticalAlignBegin( text );
		
		String url = validate( text.getHyperlinkAction( ) );
		if ( url != null )
		{
			outputAction( text.getHyperlinkAction( ), url );
			String strColor = mergedStyle.getColor( );
			if ( null != strColor )
			{
				styleBuffer.setLength( 0 );
				styleBuffer.append( " color: " );
				styleBuffer.append( strColor );
				styleBuffer.append( ";" );
				writer.attribute( HTMLTags.ATTR_STYLE, styleBuffer.toString( ) );
			}
			writer.text( textValue );
			writer.closeTag( HTMLTags.TAG_A );
		}
		else
		{
			writer.text( textValue );
		}
		htmlEmitter.handleVerticalAlignEnd( text );
		
		writer.closeTag( tagName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#startForeign(org.eclipse.birt.report.engine.content.IForeignContent)
	 */
	public void startForeign( IForeignContent foreign )
	{
		IStyle mergedStyle = foreign.getStyle( );

		logger.log( Level.FINE, "[HTMLReportEmitter] Start foreign" ); //$NON-NLS-1$

		boolean isTemplate = false;
		boolean wrapTemplateTable = false;
		Object genBy = foreign.getGenerateBy( );
		if ( genBy instanceof TemplateDesign )
		{
			isTemplate = true;
			TemplateDesign design = (TemplateDesign) genBy;
			setupTemplateElement( design, foreign );
			// all the template element should be horizontal center of it's
			// parent.
			writer.openTag( HTMLTags.TAG_DIV );
			writer.attribute( HTMLTags.ATTR_ALIGN, "center" );

			if ( enableMetadata )
			{
				// template table should be wrapped.
				if ( "Table".equals( design.getAllowedType( ) ) )
				{
					wrapTemplateTable = true;
					metadataEmitter.startWrapTable( foreign );
				}
			}
		}
		
		DimensionType x = foreign.getX( );
		DimensionType y = foreign.getY( );
		DimensionType width = foreign.getWidth( );
		DimensionType height = foreign.getHeight( );

		int display;
		display = getElementType( x, y, width, height, mergedStyle );

		// action
		String tagName = openTagByType( display, DISPLAY_FLAG_ALL );
		
		boolean bookmarkOutput = false;
		if ( metadataFilter != null )
		{
			bookmarkOutput = metadataEmitter.outputMetadataProperty( metadataFilter.needMetaData( HTMLEmitterUtil.getElementHandle( foreign ) ),
					foreign,
					tagName );
		}
		
		// output class attribute.
		String styleClass = foreign.getStyleClass( );
		setStyleName( styleClass, foreign);

		// bookmark
		if ( !bookmarkOutput )
		{
			outputBookmark( foreign, tagName );
		}

		// title
		writer.attribute( HTMLTags.ATTR_TITLE, foreign.getHelpText( ) );

		StringBuffer styleBuffer = new StringBuffer( );
		htmlEmitter.buildForeignStyle( foreign, styleBuffer, display );
		writer.attribute( HTMLTags.ATTR_STYLE, styleBuffer.toString( ) );

		String rawType = foreign.getRawType( );
		boolean isHtml = IForeignContent.HTML_TYPE.equalsIgnoreCase( rawType );
		if ( isHtml )
		{
			htmlEmitter.handleVerticalAlignBegin( foreign );
			String url = validate( foreign.getHyperlinkAction( ) );
			if ( url != null )
			{
				outputAction( foreign.getHyperlinkAction( ), url );
				outputHtmlText( foreign );
				writer.closeTag( HTMLTags.TAG_A );
			}
			else
			{
				outputHtmlText( foreign );
			}
			htmlEmitter.handleVerticalAlignEnd( foreign );
		}

		writer.closeTag( tagName );
		if ( isTemplate )
		{
			if ( wrapTemplateTable )
			{
				metadataEmitter.endWrapTable( foreign );
			}
			writer.closeTag( HTMLTags.TAG_DIV );
		}
	}
	
	
	private void outputHtmlText(IForeignContent foreign)
	{
		boolean bIndent = writer.isIndent( );
		writer.setIndent( false );
		Object rawValue = foreign.getRawValue( );
		String text = rawValue == null ? null : rawValue.toString( );
		Document doc = new TextParser( ).parse( text,
				TextParser.TEXT_TYPE_HTML );
		ReportDesignHandle design = (ReportDesignHandle) runnable
				.getDesignHandle( );
		HTMLProcessor htmlProcessor = new HTMLProcessor( design, reportContext
				.getAppContext( ) );

		HashMap styleMap = new HashMap( );

		Element body = null;
		if ( doc != null )
		{
			NodeList bodys = doc.getElementsByTagName( "body" );
			if ( bodys.getLength( ) > 0 )
			{
				body = (Element) bodys.item( 0 );
			}
		}
		if ( body != null )
		{
			htmlProcessor.execute( body, styleMap );
			processNodes( body, styleMap );
		}
		writer.setIndent( bIndent );
	}

	/**
	 * Visits the children nodes of the specific node
	 * 
	 * @param visitor
	 *            the ITextNodeVisitor instance
	 * @param ele
	 *            the specific node
	 */
	private void processNodes( Element ele, HashMap cssStyles )
	{
		for ( Node node = ele.getFirstChild( ); node != null; node = node
				.getNextSibling( ) )
		{
			// At present we only deal with the text, comment and element nodes
			short nodeType = node.getNodeType( );
			if ( nodeType == Node.TEXT_NODE )
			{
				if ( isScriptText( node ) )
				{
					writer.cdata( node.getNodeValue( ) );
				}
				else
				{
					// bug132213 in text item should only deal with the
					// escape special characters: < > &
					// writer.text( node.getNodeValue( ), false, true );
					writer.text( node.getNodeValue( ), false );
				}
			}
			else if ( nodeType == Node.COMMENT_NODE )
			{
				writer.comment( node.getNodeValue( ) );
			}
			else if ( nodeType == Node.ELEMENT_NODE )
			{
				if ( "br".equalsIgnoreCase( node.getNodeName( ) ) )
				{
					// <br/> is correct. <br></br> is not correct. The brower
					// will treat the <br></br> as <br><br>
					boolean bImplicitCloseTag = writer.isImplicitCloseTag( );
					writer.setImplicitCloseTag( true );
					startNode( node, cssStyles );
					processNodes( (Element) node, cssStyles );
					endNode( node );
					writer.setImplicitCloseTag( bImplicitCloseTag );
				}
				else
				{
					startNode( node, cssStyles );
					processNodes( (Element) node, cssStyles );
					endNode( node );
				}
			}
		}
	}
	
	/**
	 * test if the text node is in the script
	 * @param node text node
	 * @return true if the text is a script, otherwise, false.
	 */
	private boolean isScriptText( Node node )
	{
		Node parent = node.getParentNode( );
		if ( parent != null )
		{
			if ( parent.getNodeType( ) == Node.ELEMENT_NODE )
			{
				String tag = parent.getNodeName( );
				if ( HTMLTags.TAG_SCRIPT.equalsIgnoreCase( tag ) )
				{
					return true;
				}
			}
		}
		return false;
	}

	public void startNode( Node node, HashMap cssStyles )
	{
		String nodeName = node.getNodeName( );
		HashMap cssStyle = (HashMap) cssStyles.get( node );
		writer.openTag( nodeName );
		NamedNodeMap attributes = node.getAttributes( );
		if ( attributes != null )
		{
			for ( int i = 0; i < attributes.getLength( ); i++ )
			{
				Node attribute = attributes.item( i );
				String attrName = attribute.getNodeName( );
				String attrValue = attribute.getNodeValue( );

				if ( attrValue != null )
				{
					if ( "img".equalsIgnoreCase( nodeName )
							&& "src".equalsIgnoreCase( attrName ) )
					{
						String attrValueTrue = handleStyleImage( attrValue );
						if ( attrValueTrue != null )
						{
							attrValue = attrValueTrue;
						}
					}
					writer.attribute( attrName, attrValue );
				}
			}
		}
		if ( cssStyle != null )
		{
			StringBuffer buffer = new StringBuffer( );
			Iterator ite = cssStyle.entrySet( ).iterator( );
			while ( ite.hasNext( ) )
			{
				Map.Entry entry = (Map.Entry) ite.next( );
				Object keyObj = entry.getKey( );
				Object valueObj = entry.getValue( );
				if ( keyObj == null || valueObj == null )
				{
					continue;
				}
				String key = keyObj.toString( );
				String value = valueObj.toString( );
				buffer.append( key );
				buffer.append( ":" );
				if ( "background-image".equalsIgnoreCase( key ) )
				{
					String valueTrue = handleStyleImage( value, true );
					if ( valueTrue != null )
					{
						value = valueTrue;
					}
					buffer.append( "url(" );
					buffer.append( value );
					buffer.append( ")" );
				}
				else
				{
					buffer.append( value );
				}
				buffer.append( ";" );
			}
			if ( buffer.length( ) != 0 )
			{
				writer.attribute( "style", buffer.toString( ) );
			}
		}
	}

	public void endNode( Node node )
	{
		writer.closeTag( node.getNodeName( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#startLabel(org.eclipse.birt.report.engine.content.ILabelContent)
	 */
	public void startLabel( ILabelContent label )
	{
		String bookmark = label.getBookmark( );
		if ( bookmark == null ) 
		{
			bookmark = idGenerator.generateUniqueID( );
			label.setBookmark( bookmark );
		}
		startText( label );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#startData(org.eclipse.birt.report.engine.content.IDataContent)
	 */
	public void startData( IDataContent data )
	{
		startText( data );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#startImage(org.eclipse.birt.report.engine.content.IImageContent)
	 */
	public void startImage( IImageContent image )
	{
		assert image != null;
		IStyle mergedStyle = image.getStyle( );


		logger.log( Level.FINE, "[HTMLImageEmitter] Start image" ); //$NON-NLS-1$ 

		StringBuffer styleBuffer = new StringBuffer( );
		int display = checkElementType( image.getX( ), image.getY( ),
				mergedStyle, styleBuffer );

		// In HTML the default display value of image is inline. We use the tag
		// <div> to implement the block of the image.
		String tag = openTagByType( display, DISPLAY_BLOCK );

		// action
		boolean hasAction = handleAction( image.getHyperlinkAction( ) );

		//Image must have a bookmark.
		if ( image.getBookmark( ) == null )
		{
			image.setBookmark( idGenerator.generateUniqueID( ) );
		}
		
		boolean useSVG = ( "image/svg+xml".equalsIgnoreCase( image.getMIMEType( ) ) ) //$NON-NLS-1$
				|| ( ".svg".equalsIgnoreCase( image.getExtension( ) ) ) //$NON-NLS-1$
				|| ( ( image.getURI( ) != null ) && image.getURI( )
						.toLowerCase( ).endsWith( ".svg" ) ); //$NON-NLS-1$
		if ( useSVG )
		{
			image.setMIMEType( "image/svg+xml" ); //$NON-NLS-1$
		}

		boolean useSWT = "application/x-shockwave-flash".equalsIgnoreCase( image.getMIMEType( ) ); //$NON-NLS-1$

		if ( useSVG || useSWT )
		{ // use svg
			outputSVGImage( image, styleBuffer, display );
		}
		else
		{ // use img

			// write image map if necessary
			Object imageMapObject = image.getImageMap( );
			String imageMapId = null;
			boolean hasImageMap = ( imageMapObject != null )
					&& ( imageMapObject instanceof String )
					&& ( ( (String) imageMapObject ).length( ) > 0 );
			if ( hasImageMap )
			{
				imageMapId = idGenerator.generateUniqueID( );
				writer.openTag( HTMLTags.TAG_MAP );
				writer.attribute( HTMLTags.ATTR_ID, imageMapId );
				writer.attribute( HTMLTags.ATTR_NAME, imageMapId );
				writer.cdata( (String) imageMapObject );
				writer.closeTag( HTMLTags.TAG_MAP );
			}

			writer.openTag( HTMLTags.TAG_IMAGE ); //$NON-NLS-1$
			
			outputImageStyleClassBookmark( image, HTMLTags.TAG_IMAGE );

			String ext = image.getExtension( );
			
			String imgUri = getImageURI( image );
			// FIXME special process, such as encoding etc
			writer.attribute( HTMLTags.ATTR_SRC, imgUri );

			if ( hasImageMap )
			{
				// BUGZILLA 119245 request chart (without hyperlink) can't have
				// borders, the BROWSER add blue-solid border to the image with
				// maps.
				if ( !hasAction )
				{
					resetImageDefaultBorders( image, styleBuffer );
				}
				writer.attribute( HTMLTags.ATTR_USEMAP, "#" + imageMapId ); //$NON-NLS-1$ //$NON-NLS-2$
			}

			// alternative text
			String altText = image.getAltText( );
			if ( altText == null )
			{
				writer.attributeAllowEmpty( HTMLTags.ATTR_ALT, "" );
			}
			else
			{
				writer.attribute( HTMLTags.ATTR_ALT, altText );
			}

			// help text
			String titleText = image.getHelpText( );
			if ( titleText == null )
			{
				if ( hasAction )
				{
					titleText = image.getHyperlinkAction( ).getTooltip( );
				}
			}
			writer.attribute( HTMLTags.ATTR_TITLE, titleText );

			// build style
			htmlEmitter.buildImageStyle( image, styleBuffer, display );
			writer.attribute( HTMLTags.ATTR_STYLE, styleBuffer.toString( ) );

			if ( ".PNG".equalsIgnoreCase( ext ) && imageHandler != null ) //$NON-NLS-1$
			{
				if ( needFixTransparentPNG )
				{
					if ( null == htmlIDNamespace )
					{
						writer.attribute( HTMLTags.ATTR_ONLOAD, "fixPNG(this)" ); //$NON-NLS-1$
					}
					else
					{
						writer.attribute( HTMLTags.ATTR_ONLOAD, htmlIDNamespace
								+ "fixPNG(this)" ); //$NON-NLS-1$
					}
				}
			}

			writer.closeTag( HTMLTags.TAG_IMAGE );
		}

		if ( hasAction )
		{
			writer.closeTag( HTMLTags.TAG_A );
		}

		if ( tag != null )
		{
			writer.closeTag( tag );
		}
	}
	
	/**
	 * output image's part of metadata, style class and bookmark.
	 * @param image
	 * @param tag
	 */
	protected void outputImageStyleClassBookmark( IImageContent image, String tag )
	{
		boolean bookmarkOutput = false;
		if ( metadataFilter != null )
		{
			bookmarkOutput = metadataEmitter.outputMetadataProperty( metadataFilter.needMetaData( HTMLEmitterUtil.getElementHandle( image ) ),
					image,
					tag );
		}

		// output class attribute.
		String styleClass = image.getStyleClass( );
		setStyleName( styleClass, image );

		if ( !bookmarkOutput )
		{
			outputBookmark( image, HTMLTags.ATTR_IMAGE ); //$NON-NLS-1$
		}
	}
	
	/**
	 * output the svg image
	 * @param image
	 * @param styleBuffer
	 * @param display
	 */
	protected void outputSVGImage( IImageContent image, StringBuffer styleBuffer,int display )
	{
		writer.openTag( HTMLTags.TAG_EMBED );

		outputImageStyleClassBookmark( image, HTMLTags.TAG_EMBED );

		// onresize gives the SVG a change to change its content
		String htmlBookmark;
		if ( null != htmlIDNamespace )
		{
			htmlBookmark = htmlIDNamespace + image.getBookmark( );
		}
		else
		{
			htmlBookmark = image.getBookmark( );
		}
		writer.attribute( "onresize", "document.getElementById('" + htmlBookmark + "').reload()" ); //$NON-NLS-1$
		
		writer.attribute( HTMLTags.ATTR_TYPE, image.getMIMEType( ) );
		writer.attribute( HTMLTags.ATTR_SRC, getImageURI( image ) );

		// alternative text
		String altText = image.getAltText( );
		if ( altText == null )
		{
			writer.attributeAllowEmpty( HTMLTags.ATTR_ALT, "" );
		}
		else
		{
			writer.attribute( HTMLTags.ATTR_ALT, altText );
		}
		
		if ( enableMetadata )
		{
			writer.attribute( "wmode", "transparent" );
		}
		
		// build style
		htmlEmitter.buildImageStyle( image, styleBuffer, display );
		writer.attribute( HTMLTags.ATTR_STYLE, styleBuffer.toString( ) );
		writer.closeTag( HTMLTags.TAG_EMBED );
	}
	
	/**
	 * 
	 * @param image
	 * @param styleBuffer
	 */
	protected void resetImageDefaultBorders( IImageContent image,
			StringBuffer styleBuffer )
	{
		// disable the border, if the user defines border with the
		// image, it will be overided by the following style setting
		IStyle style = image.getStyle( );
		if ( style.getBorderTopStyle( ) == null )
		{
			// user doesn't define the border, remove it.
			styleBuffer.append( "border-top-style:none;" );
		}
		else
		{
			// use define the border-style, but not define the
			// border color, use the default
			// color.
			if ( style.getBorderTopColor( ) == null )
			{
				styleBuffer.append( "border-top-color:black" );
			}
		}
		if ( style.getBorderBottomStyle( ) == null )
		{
			styleBuffer.append( "border-bottom-style:none;" );
		}
		else
		{
			if ( style.getBorderBottomColor( ) == null )
			{
				styleBuffer.append( "border-bottom-color:black" );
			}
		}
		if ( style.getBorderLeftStyle( ) == null )
		{
			styleBuffer.append( "border-left-style:none;" );
		}
		else
		{
			if ( style.getBorderLeftColor( ) == null )
			{
				styleBuffer.append( "border-left-color:black" );
			}
		}
		if ( style.getBorderRightStyle( ) == null )
		{
			styleBuffer.append( "border-right-style:none;" );
		}
		else
		{
			if ( style.getBorderRightColor( ) == null )
			{
				styleBuffer.append( "border-right-color:black" );
			}
		}
	}
	
	/**
	 * gets the image's URI
	 * 
	 * @param image
	 *            the image content
	 * @return image's URI
	 */
	protected String getImageURI( IImageContent image )
	{
		String imgUri = null;
		if ( imageHandler != null )
		{
			if ( image.getImageSource( ) == IImageContent.IMAGE_URL )
			{
				return image.getURI( );
			}
			Image img = new Image( image );
			img.setRenderOption( renderOption );
			img.setReportRunnable( runnable );
			switch ( img.getSource( ) )
			{
				case IImage.DESIGN_IMAGE :
					imgUri = imageHandler.onDesignImage( img, reportContext );
					break;
				case IImage.URL_IMAGE :
					imgUri = imageHandler.onURLImage( img, reportContext );
					break;
				case IImage.REPORTDOC_IMAGE :
					imgUri = imageHandler.onDocImage( img, reportContext );
					break;
				case IImage.CUSTOM_IMAGE :
					imgUri = imageHandler.onCustomImage( img, reportContext );
					break;
				case IImage.FILE_IMAGE :
					imgUri = imageHandler.onFileImage( img, reportContext );
					break;
				case IImage.INVALID_IMAGE :
					break;
			}
		}

		return imgUri;
	}

	/**
	 * Sets the <code>'class'</code> property and stores the style to styleMap
	 * object.
	 * 
	 * @param styleName
	 *            the style name
	 */
	protected void setStyleName( String styleName,IContent content)
	{
		StringBuffer classBuffer = new StringBuffer();

		if ( enableMetadata )
		{
			String metadataStyleClass = metadataEmitter.getMetadataStyleClass( content );
			if ( null != metadataStyleClass )
			{
				classBuffer.append( metadataStyleClass );
			}
		}
		
		if ( !enableInlineStyle && styleName != null && styleName.length( ) > 0 )
		{
			if ( outputtedStyles.contains( styleName ) )
			{
				if ( classBuffer.length( ) != 0 )
				{
					classBuffer.append( " " );
				}
				if ( null != htmlIDNamespace )
				{
					classBuffer.append( htmlIDNamespace );
				}
				classBuffer.append( styleName );
			}
		}

		if ( hasCsslinks )
		{
			Object genBy = content.getGenerateBy( );
			if ( genBy instanceof StyledElementDesign )
			{
				DesignElementHandle handle = ( (StyledElementDesign) genBy )
						.getHandle( );
				if ( handle != null )
				{
					String name = handle
							.getStringProperty( ReportItemHandle.STYLE_PROP );
					if ( name != null )
					{
						if ( classBuffer.length( ) != 0 )
						{
							classBuffer.append( " " + name );
						}
						else
						{
							classBuffer.append( name );
						}
					}
				}
			}
		}
		
		if (classBuffer.length() != 0)
		{
			writer.attribute( HTMLTags.ATTR_CLASS, classBuffer.toString( ) );
		}
	}

	protected void outputBookmark( IContent content, String tagName )
	{
		String bookmark = content.getBookmark( );
		outputBookmark( writer, tagName, htmlIDNamespace, bookmark );
	}
	
	public void outputBookmark( HTMLWriter writer, String tagName,
			String htmlIDNamespace, String bookmark )
	{
		if ( outputBookmarks != null )
		{
			if ( outputBookmarks.contains( bookmark ) )
			{
				return;
			}
			else
			{
				outputBookmarks.add( bookmark );
			}
		}
		HTMLEmitterUtil.setBookmark( writer, tagName, htmlIDNamespace, bookmark );
	}

	/**
	 * Checks whether the element is block, inline or inline-block level. In
	 * BIRT, the absolute positioning model is used and a box is explicitly
	 * offset with respect to its containing block. When an element's x or y is
	 * set, it will be treated as a block level element regardless of the
	 * 'Display' property set in style. When designating width or height value
	 * to an inline element, it will be treated as inline-block.
	 * 
	 * @param x
	 *            Specifies how far a box's left margin edge is offset to the
	 *            right of the left edge of the box's containing block.
	 * @param y
	 *            Specifies how far an absolutely positioned box's top margin
	 *            edge is offset below the top edge of the box's containing
	 *            block.
	 * @param width
	 *            The width of the element.
	 * @param height
	 *            The height of the element.
	 * @param style
	 *            The <code>IStyle</code> object.
	 * @param styleBuffer
	 *            The <code>StringBuffer</code> object that returns 'style'
	 *            content.
	 * @return The display type of the element.
	 */
	protected int checkElementType( DimensionType x, DimensionType y,
			IStyle style, StringBuffer styleBuffer )
	{
		return checkElementType( x, y, null, null, style, styleBuffer );
	}

	protected int checkElementType( DimensionType x, DimensionType y,
			DimensionType width, DimensionType height, IStyle style,
			StringBuffer styleBuffer )
	{
		//if ( x != null || y != null )
		//{
		//	styleBuffer.append( "position: absolute;" ); //$NON-NLS-1$
		//	AttributeBuilder.buildSize( styleBuffer, HTMLTags.ATTR_LEFT, x );
		//	AttributeBuilder.buildSize( styleBuffer, HTMLTags.ATTR_TOP, y );
		//}
		return getElementType( x, y, width, height, style);
	}

	public int getElementType( DimensionType x, DimensionType y,
			DimensionType width, DimensionType height, IStyle style )
	{
		return htmlEmitter.getElementType( x, y, width, height, style );
	}

	/**
	 * Open a tag according to the display type of the element. Here is the
	 * mapping table:
	 * <p>
	 * <table border=0 cellspacing=3 cellpadding=0 summary="Chart showing
	 * symbol, location, localized, and meaning.">
	 * <tr bgcolor="#ccccff">
	 * <th align=left>Display Type</th>
	 * <th align=left>Tag name</th>
	 * </tr>
	 * <tr valign=middle>
	 * <td>DISPLAY_BLOCK</td>
	 * <td>DIV</td>
	 * </tr>
	 * <tr valign=top bgcolor="#eeeeff">
	 * <td>DISPLAY_INLINE</td>
	 * <td>SPAN</td>
	 * </tr>
	 * </table>
	 * 
	 * @param display
	 *            The display type.
	 * @param mask
	 *            The mask value.
	 * @return Tag name.
	 */
	protected String openTagByType( int display, int mask )
	{
		String tag = HTMLEmitterUtil.getTagByType( display, mask );
		if ( tag != null)
		{
			writer.openTag( tag );
		}
		return tag;
	}

	/**
	 * Checks the Action object and then output corresponding tag and property.
	 * 
	 * @param action
	 *            The <code>IHyperlinkAction</code> object.
	 * @return A <code>boolean</code> value indicating whether the Action
	 *         object is valid or not.
	 */
	protected boolean handleAction( IHyperlinkAction action )
	{
		String url = validate( action );
		if ( url != null )
		{
			outputAction( action, url );
		}
		return url != null;
	}

	/**
	 * Outputs an hyperlink action.
	 * 
	 * @param action
	 *            the hyperlink action.
	 */
	protected void outputAction( IHyperlinkAction action, String url )
	{
		writer.openTag( HTMLTags.TAG_A );
		writer.attribute( HTMLTags.ATTR_HREF, url );
		writer.attribute( HTMLTags.ATTR_TARGET, action.getTargetWindow( ) );
		writer.attribute( HTMLTags.ATTR_TITLE, action.getTooltip( ));
	}

	/**
	 * Judges if a hyperlink is valid.
	 * 
	 * @param action
	 *            the hyperlink action
	 * @return
	 */
	private String validate( IHyperlinkAction action )
	{
		if ( action == null )
		{
			return null;
		}
		String systemId = runnable == null ? null : runnable.getReportName( );
		Action act = new Action( systemId, action );

		if ( actionHandler == null )
		{
			return null;
		}

		String link = actionHandler.getURL( act, reportContext );
		if ( link != null && !link.equals( "" ) )//$NON-NLS-1$
		{
			return link;
		}
		return null;
	}

	/**
	 * handle style image
	 * 
	 * @param uri
	 *            uri in style image
	 * @return
	 */
	public String handleStyleImage( String uri )
	{
		return handleStyleImage( uri, false );
	}
		
	// FIXME: code review: this function needs be handled in the ENGINE( after
	// render , in the localize)? We should calculate the imgUri in the engine
	// part and put the imgUri into the image style. Then we can use the imagUri
	// directly here
	/**
	 * handle style image
	 * 
	 * @param uri
	 *            uri in style image
	 * @param isBackground
	 *            Is this image a used for a background?
	 * @return
	 */
	public String handleStyleImage( String uri, boolean isBackground )
	{
		ReportDesignHandle design = (ReportDesignHandle) runnable
				.getDesignHandle( );
		URL url = design.findResource( uri, IResourceLocator.IMAGE,
				reportContext.getAppContext( ) );
		if ( url == null )
		{
			return uri;
		}
		uri = url.toExternalForm( );
		Image image = null;
		if ( isBackground && SvgFile.isSvg( uri ) )
		{
			try
			{
				byte[] buffer = SvgFile.transSvgToArray( uri );
				image = new Image( buffer, uri, ".jpg" );
			}
			catch ( IOException e )
			{
				image = new Image( uri );
			}
		}
		else
		{
			image = new Image( uri );
		}
		image.setReportRunnable( runnable );
		image.setRenderOption( renderOption );
		String imgUri = null;
		if ( imageHandler != null )
		{
			switch ( image.getSource( ) )
			{

				case IImage.URL_IMAGE :
					imgUri = imageHandler.onURLImage( image, reportContext );
					break;

				case IImage.FILE_IMAGE :
					imgUri = imageHandler.onFileImage( image, reportContext );
					break;

				case IImage.CUSTOM_IMAGE :
					imgUri = imageHandler.onCustomImage( image, reportContext );
					break;
					
				case IImage.INVALID_IMAGE :
					break;

				default :
					assert ( false );
			}
			// imgUri = imgUri.replace( File.separatorChar, '/' );
		}
		return imgUri;
	}
	
	/**
	 * setup chart template and table template element for output.
	 * 
	 * <li>1. set the bookmark if there is no bookmark. </li>
	 * <li>2. chage the styles of the element.</li>
	 * 
	 * @param template
	 *            the design used to create the contnet.
	 * @param content
	 *            the styled element content
	 */
	private void setupTemplateElement( TemplateDesign template, IContent content )
	{
		// set up the bookmark if there is no bookmark for the template
		String bookmark = content.getBookmark( );
		if ( bookmark == null )
		{
			bookmark = idGenerator.generateUniqueID( );
			content.setBookmark( bookmark );
		}

		// setup the styles of the template
		String allowedType = template.getAllowedType( );
		if ( "ExtendedItem".equals( allowedType ) )
		{
			// Resize chart template element
			IStyle style = content.getStyle( );
			style.setProperty( IStyle.STYLE_CAN_SHRINK, IStyle.FALSE_VALUE );
			content.setWidth( new DimensionType( 3, DimensionType.UNITS_IN ) );
			content.setHeight( new DimensionType( 3, DimensionType.UNITS_IN ) );
		}
		else if ( "Table".equals( allowedType ) )
		{
			// Resize table template element
			IStyle style = content.getStyle( );
			style.setProperty( IStyle.STYLE_CAN_SHRINK, IStyle.FALSE_VALUE );
			content.setWidth( new DimensionType( 5, DimensionType.UNITS_IN ) );
			// set lines to dotted lines
			style.setProperty( IStyle.STYLE_BORDER_TOP_STYLE,
					IStyle.DOTTED_VALUE );
			style.setProperty( IStyle.STYLE_BORDER_BOTTOM_STYLE,
					IStyle.DOTTED_VALUE );
			style.setProperty( IStyle.STYLE_BORDER_LEFT_STYLE,
					IStyle.DOTTED_VALUE );
			style.setProperty( IStyle.STYLE_BORDER_RIGHT_STYLE,
					IStyle.DOTTED_VALUE );
			style.setProperty( IStyle.STYLE_FONT_FAMILY,
					IStyle.SANS_SERIF_VALUE );
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter#endGroup(org.eclipse.birt.report.engine.content.IGroupContent)
	 */
	public void endGroup( IGroupContent group )
	{
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter#endListBand(org.eclipse.birt.report.engine.content.IListBandContent)
	 */
	public void endListBand( IListBandContent listBand )
	{
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter#endListGroup(org.eclipse.birt.report.engine.content.IListGroupContent)
	 */
	public void endListGroup( IListGroupContent group )
	{
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter#endTableBand(org.eclipse.birt.report.engine.content.ITableBandContent)
	 */
	public void endTableBand( ITableBandContent band )
	{
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter#endTableGroup(org.eclipse.birt.report.engine.content.ITableGroupContent)
	 */
	public void endTableGroup( ITableGroupContent group )
	{
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter#startGroup(org.eclipse.birt.report.engine.content.IGroupContent)
	 */
	public void startGroup( IGroupContent group )
	{
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter#startListBand(org.eclipse.birt.report.engine.content.IListBandContent)
	 */
	public void startListBand( IListBandContent listBand )
	{
	}

	/**
	 * used to control the output of group bookmarks. 
	 * @see {@link #startTableGroup(ITableGroupContent)}
	 * @see {@link #startListGroup(IListGroupContent)}
	 */
	protected Stack startedGroups = new Stack(); 

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter#startListGroup(org.eclipse.birt.report.engine.content.IListGroupContent)
	 */
	public void startListGroup( IListGroupContent group )
	{
		outputBookmark( group );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter#startTableBand(org.eclipse.birt.report.engine.content.ITableBandContent)
	 */
	public void startTableBand( ITableBandContent band )
	{
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter#startTableGroup(org.eclipse.birt.report.engine.content.ITableGroupContent)
	 */
	public void startTableGroup( ITableGroupContent group )
	{
		startedGroups.push( group );
	}

	private void outputBookmark( IGroupContent group )
	{
		String bookmark = group.getBookmark( );
		if ( bookmark == null )
		{
			bookmark = idGenerator.generateUniqueID( );
			group.setBookmark( bookmark );
		}
		writer.openTag( HTMLTags.TAG_SPAN );
		outputBookmark( group, null );
		writer.closeTag( HTMLTags.TAG_SPAN );
	}
	
	private void writeBidiFlag( )
	{
		// bidi_hcg start
		// RTL attribute is required at HTML or BODY level for the correct
		// scroll bar position.
		if ( htmlRtLFlag )
		{
			writer.attribute( HTMLTags.ATTR_HTML_DIR,
					CSSConstants.CSS_RTL_VALUE );
		}
		// bidi_hcg end
	}

	/**
	 * Figures out the RTL rendering option.
	 *
	 * @param htmlOption
	 * @author bidi_hcg
	 */
	private void retrieveRtLFlag( )
	{
		// If htmlOption has RTL_FLAG option set (likely adopted from an URL
		// parameter), honor this option, otherwise obtain direction from
		// the report design.
		HTMLRenderOption htmlOption = new HTMLRenderOption( renderOption );
		Object bidiFlag = htmlOption.getOption( IRenderOption.RTL_FLAG );
		if ( Boolean.TRUE.equals( bidiFlag ) )
		{
			htmlRtLFlag = true;
		}
		else if ( bidiFlag == null && report != null)
		{
			ReportDesignHandle handle = report.getDesign( ).getReportDesign( );
			if ( handle != null )
			{
				htmlRtLFlag = handle.isDirectionRTL( );
				htmlOption.setHtmlRtLFlag( htmlRtLFlag ); // not necessary though
			}
		}
	}
}

class IDGenerator
{
	protected int bookmarkId = 0;
	IDGenerator( )
	{
		this.bookmarkId = 0;
	}
	protected String generateUniqueID( )
	{
		bookmarkId ++;
		return "AUTOGENBOOKMARK_" + bookmarkId;
	}
}