/*******************************************************************************
 * Copyright (c) 2004, 2009, 2023, 2024 Actuate Corporation and others
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.html;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
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
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IHTMLActionHandler;
import org.eclipse.birt.report.engine.api.IHTMLImageHandler;
import org.eclipse.birt.report.engine.api.IHTMLRenderOption;
import org.eclipse.birt.report.engine.api.IImage;
import org.eclipse.birt.report.engine.api.IMetadataFilter;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.ImageSize;
import org.eclipse.birt.report.engine.api.impl.Action;
import org.eclipse.birt.report.engine.api.impl.Image;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.content.IBandContent;
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
import org.eclipse.birt.report.engine.content.impl.ReportContent;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.css.engine.value.birt.BIRTValueConstants;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSValueConstants;
import org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter;
import org.eclipse.birt.report.engine.emitter.EmitterUtil;
import org.eclipse.birt.report.engine.emitter.HTMLTags;
import org.eclipse.birt.report.engine.emitter.HTMLWriter;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;
import org.eclipse.birt.report.engine.emitter.html.util.DiagonalLineImage;
import org.eclipse.birt.report.engine.emitter.html.util.HTMLEmitterUtil;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.executor.ExecutionContext.ElementExceptionInfo;
import org.eclipse.birt.report.engine.executor.css.HTMLProcessor;
import org.eclipse.birt.report.engine.i18n.EngineResourceHandle;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.internal.content.wrap.CellContentWrapper;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.SimpleMasterPageDesign;
import org.eclipse.birt.report.engine.ir.StyledElementDesign;
import org.eclipse.birt.report.engine.ir.TemplateDesign;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.eclipse.birt.report.engine.nLayout.area.style.BackgroundImageInfo;
import org.eclipse.birt.report.engine.parser.TextParser;
import org.eclipse.birt.report.engine.presentation.ContentEmitterVisitor;
import org.eclipse.birt.report.engine.util.ResourceLocatorWrapper;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.IncludedCssStyleSheetHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.util.DimensionUtil;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IStyledElementModel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.css.CSSValue;

import com.ibm.icu.text.Bidi;
import com.ibm.icu.util.ULocale;

/**
 * <code>HTMLReportEmitter</code> is a subclass of
 * <code>ContentEmitterAdapter</code> that implements IContentEmitter interface
 * to output IARD Report ojbects to HTML file.
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
public class HTMLReportEmitter extends ContentEmitterAdapter {
	/**
	 * the name of the root DIV for BIRT html output.
	 */
	public static final String BIRT_ROOT = "__BIRT_ROOT";

	protected boolean hasCsslinks = false;
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
	 * the html client script property
	 */
	public static final String EXTENSION_HTML_CLIENT_SCRIPTS = "html.clientScripts"; //$NON-NLS-1$

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

	protected List<?> ouputInstanceIDs = null;

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
	protected Stack<?> stack = new Stack<Object>();

	HashMap<String, String> diagonalCellImageMap = new HashMap<String, String>();

	/**
	 * An Log object that <code>HTMLReportEmitter</code> use to log the error,
	 * debug, information messages.
	 */
	protected static Logger logger = Logger.getLogger(HTMLReportEmitter.class.getName());

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

	protected IDGenerator idGenerator = new IDGenerator();

	protected String layoutPreference;
	protected boolean fixedReport = false;
	protected boolean enableAgentStyleEngine;
	protected boolean outputMasterPageMargins;
	protected IMetadataFilter metadataFilter = null;

	protected boolean needOutputBackgroundSize = false;
	protected boolean enableInlineStyle = false;

	/**
	 * Following names will be name spaced by htmlIDNamespace: a.CSS style name.
	 * b.id (bookmark). c.script name, which is created by BIRT.
	 */
	protected String htmlIDNamespace;
	protected int browserVersion;
	// The browser supports the inline-block or not. The default state is true;
	protected boolean browserSupportsInlineBlock = true;
	// The browser supports the broken image icon.
	protected boolean browserSupportsBrokenImageIcon = false;
	protected int imageDpi = -1;

	protected HTMLEmitter htmlEmitter;
	protected Stack<Boolean> tableDIVWrapedFlagStack = new Stack<Boolean>();
	protected Stack<DimensionType> fixedRowHeightStack = new Stack<>();

	/**
	 * This set is used to store the style class which has been outputted.
	 */
	protected Set<String> outputtedStyles = new HashSet<String>();

	protected boolean needFixTransparentPNG = false;
	protected ITableContent cachedStartTable = null;

	protected TableLayout tableLayout = new TableLayout(this);

	/**
	 * Default image pixel width.
	 */
	private static int DEFAULT_IMAGE_PX_WIDTH = 200;
	/**
	 * Default image pixel height.
	 */
	private static int DEFAULT_IMAGE_PX_HEIGHT = 200;

	private static final String URL_PROTOCOL_TYPE_FILE = "file:";
	private static final String URL_PROTOCOL_TYPE_DATA = "data:";
	private static final String URL_PROTOCOL_URL_ENCODED_SPACE = "%20";

	/**
	 * the constructor
	 */
	public HTMLReportEmitter() {
		contentVisitor = new ContentEmitterVisitor(this);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.emitter.IContentEmitter#initialize(org.eclipse
	 * .birt.report.engine.emitter.IEmitterServices)
	 */
	@Override
	public void initialize(IEmitterServices services) throws EngineException {
		this.services = services;

		this.out = EmitterUtil.getOuputStream(services, REPORT_FILE);

		// usage of HTMLRenderOption instead of deprecated HTMLEmitterConfig
		Object im = services.getOption(IRenderOption.IMAGE_HANDLER);
		if (im instanceof IHTMLImageHandler) {
			imageHandler = (IHTMLImageHandler) im;
		}

		Object ac = services.getOption(IRenderOption.ACTION_HANDLER);
		if (ac instanceof IHTMLActionHandler) {
			actionHandler = (IHTMLActionHandler) ac;
		}

		reportContext = services.getReportContext();

		renderOption = services.getRenderOption();
		runnable = services.getReportRunnable();
		writer = creatWriter();
		if (renderOption != null) {
			HTMLRenderOption htmlOption = new HTMLRenderOption(renderOption);
			isEmbeddable = htmlOption.getEmbeddable();
			// Map<?, ?> options = renderOption.getOutputSetting();
			Map<?, ?> options = renderOption.getOptions();

			if (options != null) {
				urlEncoding = (String) options.get(IHTMLRenderOption.URL_ENCODING);
			}
			outputMasterPageContent = htmlOption.getMasterPageContent();
			// IHTMLActionHandler actHandler = htmlOption.getActionHandle();
			IHTMLActionHandler actHandler = htmlOption.getActionHandler();
			if (ac != null) {
				actionHandler = actHandler;
			}
			pageFooterFloatFlag = htmlOption.getPageFooterFloatFlag();
			// htmlRtLFlag = htmlOption.getHtmlRtLFlag( );
			enableMetadata = htmlOption.getEnableMetadata();
			if (enableMetadata) {
				metadataFilter = htmlOption.getMetadataFilter();
				if (metadataFilter == null) {
					metadataFilter = new MetadataFilter();
				}
			}
			writer.setEnableCompactMode(htmlOption.isEnableCompactMode());
			ouputInstanceIDs = htmlOption.getInstanceIDs();
			metadataEmitter = creatMetadataEmitter(writer, htmlOption);
			layoutPreference = htmlOption.getLayoutPreference();
			enableAgentStyleEngine = htmlOption.getEnableAgentStyleEngine();
			outputMasterPageMargins = htmlOption.getOutputMasterPageMargins();
			htmlIDNamespace = htmlOption.getHTMLIDNamespace();
			if (null != htmlIDNamespace) {
				if (htmlIDNamespace.length() > 0) {
					htmlIDNamespace += "_";
					metadataEmitter.setHTMLIDNamespace(htmlIDNamespace);
				} else {
					htmlIDNamespace = null;
				}
			}
			writer.setIndent(htmlOption.getHTMLIndent());
			if (isEmbeddable) {
				enableInlineStyle = htmlOption.getEnableInlineStyle();
			}
			browserVersion = HTMLEmitterUtil.getBrowserVersion(htmlOption.getUserAgent());
			if (browserVersion == HTMLEmitterUtil.BROWSER_IE5 || browserVersion == HTMLEmitterUtil.BROWSER_IE6) {
				needFixTransparentPNG = true;
				browserSupportsInlineBlock = false;
			} else if (browserVersion == HTMLEmitterUtil.BROWSER_IE7
					|| browserVersion == HTMLEmitterUtil.BROWSER_FIREFOX1
					|| browserVersion == HTMLEmitterUtil.BROWSER_FIREFOX2) {
				browserSupportsInlineBlock = false;
			}
//  Single Metric: Dont provide alt attribute to the img element in single metric
			if (browserVersion == HTMLEmitterUtil.BROWSER_FIREFOX || browserVersion == HTMLEmitterUtil.BROWSER_FIREFOX1
					|| browserVersion == HTMLEmitterUtil.BROWSER_FIREFOX2) {
				browserSupportsBrokenImageIcon = true;
			}
		}
	}

	protected HTMLWriter creatWriter() {
		return new HTMLWriter();
	}

	protected MetadataEmitter creatMetadataEmitter(HTMLWriter writer, HTMLRenderOption htmlOption) {
		return new MetadataEmitter(writer, htmlOption, null, idGenerator, this);
	}

	/**
	 * @return the <code>Report</code> object.
	 */
	public IReportContent getReport() {
		return report;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#getOutputFormat()
	 */
	@Override
	public String getOutputFormat() {
		return OUTPUT_FORMAT_HTML;
	}

	/**
	 * Fixes a PNG problem related to transparency. See
	 * http://homepage.ntlworld.com/bobosola/ for detail.
	 */
	protected void fixTransparentPNG() {
		// Because the IE 7 start to support the transparent PNG directly, this
		// function only be needed for the IE5.5 and IE6.
		writer.writeCode("<!--[if (gte IE 5.5000)&(lt IE 7)]>"); //$NON-NLS-1$
		writer.writeCode("   <script  type=\"text/javascript\">"); //$NON-NLS-1$
		writer.writeCode("    //<![CDATA"); //$NON-NLS-1$
		if (htmlIDNamespace == null) {
			writer.writeCode("      var ie55up = true;"); //$NON-NLS-1$
		} else {
			writer.writeCode("      var " + htmlIDNamespace + "ie55up = true;"); //$NON-NLS-1$
		}
		writer.writeCode("    //]]>"); //$NON-NLS-1$
		writer.writeCode("   </script>"); //$NON-NLS-1$
		writer.writeCode("<![endif]-->"); //$NON-NLS-1$
		writer.writeCode("<script type=\"text/javascript\">"); //$NON-NLS-1$
		writer.writeCode(" //<![CDATA["); //$NON-NLS-1$
		if (null == htmlIDNamespace) {
			writer.writeCode("   function fixPNG(myImage) // correctly handle PNG transparency in Win IE 5.5 or IE 6."); //$NON-NLS-1$
			writer.writeCode("      {"); //$NON-NLS-1$
			writer.writeCode("       if ( window.ie55up )"); //$NON-NLS-1$
		} else {
			writer.writeCode("   function " + htmlIDNamespace //$NON-NLS-1$
					+ "fixPNG(myImage) // correctly handle PNG transparency in Win IE 5.5 or higher.");
			writer.writeCode("      {"); //$NON-NLS-1$
			writer.writeCode("       if ( window." + htmlIDNamespace + "ie55up )"); //$NON-NLS-1$
		}
		writer.writeCode("          {"); //$NON-NLS-1$
		writer.writeCode("           var imgID = (myImage.id) ? \"id='\" + myImage.id + \"' \" : \"\";"); //$NON-NLS-1$
		writer.writeCode(
				"           var imgClass = (myImage.className) ? \"class='\" + myImage.className + \"' \" : \"\";"); //$NON-NLS-1$
		writer.writeCode(
				"           var imgTitle = (myImage.title) ? \"title='\" + myImage.title + \"' \" : \"title='\" + myImage.alt + \"' \";"); //$NON-NLS-1$
		writer.writeCode("           var imgStyle = \"display:inline-block;\" + myImage.style.cssText;"); //$NON-NLS-1$
		writer.writeCode("           var strNewHTML = \"<span \" + imgID + imgClass + imgTitle;"); //$NON-NLS-1$
		writer.writeCode(
				"           strNewHTML += \" style=\\\"\" + \"width:\" + myImage.width + \"px; height:\" + myImage.height + \"px;\" + imgStyle + \";\";"); //$NON-NLS-1$
		writer.writeCode("           strNewHTML += \"filter:progid:DXImageTransform.Microsoft.AlphaImageLoader\";"); //$NON-NLS-1$
		writer.writeCode(
				"           strNewHTML += \"(src=\\'\" + myImage.src + \"\\', sizingMethod='scale');\\\"></span>\";"); //$NON-NLS-1$
		writer.writeCode("           myImage.outerHTML = strNewHTML;"); //$NON-NLS-1$
		writer.writeCode("          }"); //$NON-NLS-1$
		writer.writeCode("      }"); //$NON-NLS-1$
		writer.writeCode(" //]]>"); //$NON-NLS-1$
		writer.writeCode("</script>"); //$NON-NLS-1$
	}

	/**
	 * Fixes the security issues when redirecting page in IE7.
	 */
	protected void fixRedirect() {
		writer.writeCode("<script type=\"text/javascript\">"); //$NON-NLS-1$
		writer.writeCode(" //<![CDATA["); //$NON-NLS-1$
		if (htmlIDNamespace == null) {
			writer.writeCode("   function redirect(target, url){"); //$NON-NLS-1$
		} else {
			writer.writeCode("   function " + htmlIDNamespace + "redirect(target, url){"); //$NON-NLS-1$
		}
		writer.writeCode("       if (target =='_blank'){"); //$NON-NLS-1$
		writer.writeCode("           open(url);"); //$NON-NLS-1$
		writer.writeCode("       }"); //$NON-NLS-1$
		writer.writeCode("       else if (target == '_top'){"); //$NON-NLS-1$
		writer.writeCode("           window.top.location.href=url;"); //$NON-NLS-1$
		writer.writeCode("       }"); //$NON-NLS-1$
		writer.writeCode("       else if (target == '_parent'){"); //$NON-NLS-1$
		writer.writeCode("           location.href=url;"); //$NON-NLS-1$
		writer.writeCode("       }"); //$NON-NLS-1$
		writer.writeCode("       else if (target == '_self'){");//$NON-NLS-1$
		writer.writeCode("           location.href =url;"); //$NON-NLS-1$
		writer.writeCode("       }"); //$NON-NLS-1$
		writer.writeCode("       else{");//$NON-NLS-1$
		writer.writeCode("           open(url);"); //$NON-NLS-1$
		writer.writeCode("       }"); //$NON-NLS-1$
		writer.writeCode("      }"); //$NON-NLS-1$
		writer.writeCode(" //]]>"); //$NON-NLS-1$
		writer.writeCode("</script>"); //$NON-NLS-1$
	}

	protected void loadBirtJs(String birtJsUrl) {
		writer.writeCode("<script type=\"text/javascript\" src=\"" + birtJsUrl + "\" >");
		writer.writeCode("</script>"); //$NON-NLS-1$
	}

	protected void addCellDiagonalSpecialJs() {
		writer.writeCode("<script type=\"text/javascript\">");
		writer.writeCode(" //<![CDATA["); //$NON-NLS-1$
		writer.writeCode("   function combineBgImageAndDiagonal(id, diagUri) {");
		writer.writeCode("     var nTd = document.getElementById(id);");
		writer.writeCode("     if (nTd) {");
		writer.writeCode("       var nStyle = getComputedStyle(nTd);");
		writer.writeCode("       if (nStyle && nStyle.backgroundImage) {");
		writer.writeCode("         var bgStyle = '';");
		writer.writeCode("         bgStyle += 'background-image:' + diagUri + ', ' + nStyle.backgroundImage + ';'	;");
		writer.writeCode("         bgStyle += 'background-size:100% 100%, ' + nStyle.backgroundSize + ';'			;");
		writer.writeCode("         bgStyle += 'background-repeat:no-repeat, ' + nStyle.backgroundRepeat + ';'		;");
		writer.writeCode("         bgStyle += 'background-position: center, ' + nStyle.backgroundPosition + ';'	;");
		writer.writeCode("         bgStyle += 'background-position-x:' + nStyle.backgroundPositionY + ';'			;");
		writer.writeCode("         bgStyle += 'background-position-y:' + nStyle.backgroundPositionX + ';'			;");
		writer.writeCode("         bgStyle += 'background-attachment:' + nStyle.backgroundAttachment + ';'			;");
		writer.writeCode("         bgStyle += 'overflow:hidden;';");
		writer.writeCode("         nTd.setAttribute('style' , bgStyle);");
		writer.writeCode("       }");
		writer.writeCode("     }");
		writer.writeCode("   }");
		writer.writeCode(" //]]>"); //$NON-NLS-1$
		writer.writeCode("</script>"); //$NON-NLS-1$
	}

	protected void addStyleTextHyperlinkDecorationNone() {
		writer.writeCode(
				"<style type=\"text/css\">.hyperlink-undecorated {text-decoration: none; color: inherit;}</style>"); //$NON-NLS-1$
	}

	protected void outputBirtJs() {
		writer.writeCode("<script type=\"text/javascript\">");
		writer.writeCode(" //<![CDATA["); //$NON-NLS-1$

		writer.writeCode(
				"(function(a,w){function f(a){p[p.length]=a}function m(a){q.className=q.className.replace(RegExp(\"\\\\\\\\b\"+a+\"\\\\b\"),\"\")}function k(a,d){for(var b=0,c=a.length;b<c;b++)d.call(a,a[b],b)}function s(){q.className=q.className.replace(/ (w-|eq-|gt-|gte-|lt-|lte-|portrait|no-portrait|landscape|no-landscape)\\d+/g,\"\");var b=a.innerWidth||q.clientWidth,d=a.outerWidth||a.screen.width;h.screen.innerWidth=b;h.screen.outerWidth=d;f(\"w-\"+b);k(c.screens,function(a){b>a?(c.screensCss.gt&&f(\"gt-\"+a),c.screensCss.gte&&f(\"gte-\"+");
		writer.writeCode(
				"a)):b<a?(c.screensCss.lt&&f(\"lt-\"+a),c.screensCss.lte&&f(\"lte-\"+a)):b===a&&(c.screensCss.lte&&f(\"lte-\"+a),c.screensCss.eq&&f(\"e-q\"+a),c.screensCss.gte&&f(\"gte-\"+a))});var d=a.innerHeight||q.clientHeight,g=a.outerHeight||a.screen.height;h.screen.innerHeight=d;h.screen.outerHeight=g;h.feature(\"portrait\",d>b);h.feature(\"landscape\",d<b)}function r(){a.clearTimeout(u);u=a.setTimeout(s,100)}var n=a.document,g=a.navigator,t=a.location,q=n.documentElement,p=[],c={screens:[240,320,480,640,768,800,1024,1280,");// 2
		writer.writeCode(
				"1440,1680,1920],screensCss:{gt:!0,gte:!1,lt:!0,lte:!1,eq:!1},browsers:[{ie:{min:6,max:10}}],browserCss:{gt:!0,gte:!1,lt:!0,lte:!1,eq:!0},section:\"-section\",page:\"-page\",head:\"head\"};if(a.head_conf)for(var b in a.head_conf)a.head_conf[b]!==w&&(c[b]=a.head_conf[b]);var h=a[c.head]=function(){h.ready.apply(null,arguments)};h.feature=function(a,b,c){if(!a)return q.className+=\" \"+p.join(\" \"),p=[],h;\"[object Function]\"===Object.prototype.toString.call(b)&&(b=b.call());f((b?\"\":\"no-\")+a);h[a]=!!b;c||(m(\"no-\"+");
		writer.writeCode(
				"a),m(a),h.feature());return h};h.feature(\"js\",!0);b=g.userAgent.toLowerCase();g=/mobile|midp/.test(b);h.feature(\"mobile\",g,!0);h.feature(\"desktop\",!g,!0);b=/(chrome|firefox)[ \\/]([\\w.]+)/.exec(b)||/(iphone|ipad|ipod)(?:.*version)?[ \\/]([\\w.]+)/.exec(b)||/(android)(?:.*version)?[ \\/]([\\w.]+)/.exec(b)||/(webkit|opera)(?:.*version)?[ \\/]([\\w.]+)/.exec(b)||/(msie) ([\\w.]+)/.exec(b)||[];g=b[1];b=parseFloat(b[2]);switch(g){case \"msie\":g=\"ie\";b=n.documentMode||b;break;case \"firefox\":g=\"ff\";break;case \"ipod\":case \"ipad\":case \"iphone\":g=");// 4
		writer.writeCode(
				"\"ios\";break;case \"webkit\":g=\"safari\"}h.browser={name:g,version:b};h.browser[g]=!0;for(var v=0,x=c.browsers.length;v<x;v++)for(var i in c.browsers[v])if(g===i){f(i);for(var A=c.browsers[v][i].max,l=c.browsers[v][i].min;l<=A;l++)b>l?(c.browserCss.gt&&f(\"gt-\"+i+l),c.browserCss.gte&&f(\"gte-\"+i+l)):b<l?(c.browserCss.lt&&f(\"lt-\"+i+l),c.browserCss.lte&&f(\"lte-\"+i+l)):b===l&&(c.browserCss.lte&&f(\"lte-\"+i+l),c.browserCss.eq&&f(\"eq-\"+i+l),c.browserCss.gte&&f(\"gte-\"+i+l))}else f(\"no-\"+i);\"ie\"===g&&9>b&&k(\"abbr article aside audio canvas details figcaption figure footer header hgroup mark meter nav output progress section summary time video\".split(\" \"),");
		writer.writeCode(
				"function(a){n.createElement(a)});k(t.pathname.split(\"/\"),function(a,b){if(2<this.length&&this[b+1]!==w)b&&f(this.slice(1,b+1).join(\"-\").toLowerCase()+c.section);else{var g=a||\"index\",h=g.indexOf(\".\");0<h&&(g=g.substring(0,h));q.id=g.toLowerCase()+c.page;b||f(\"root\"+c.section)}});h.screen={height:a.screen.height,width:a.screen.width};s();var u=0;a.addEventListener?a.addEventListener(\"resize\",r,!1):a.attachEvent(\"onresize\",r)})(window);");// 6
		writer.writeCode(
				"(function(a,w){function f(a){var f=a.charAt(0).toUpperCase()+a.substr(1),a=(a+\" \"+r.join(f+\" \")+f).split(\" \"),c;a:{for(c in a)if(k[a[c]]!==w){c=!0;break a}c=!1}return!!c}var m=a.document.createElement(\"i\"),k=m.style,s=\" -o- -moz- -ms- -webkit- -khtml- \".split(\" \"),r=[\"Webkit\",\"Moz\",\"O\",\"ms\",\"Khtml\"],n=a[a.head_conf&&a.head_conf.head||\"head\"],g={gradient:function(){k.cssText=(\"background-image:\"+s.join(\"gradient(linear,left top,right bottom,from(#9f9),to(#fff));background-image:\")+s.join(\"linear-gradient(left top,#eee,#fff);background-image:\")).slice(0,");
		writer.writeCode(
				"-17);return!!k.backgroundImage},rgba:function(){k.cssText=\"background-color:rgba(0,0,0,0.5)\";return!!k.backgroundColor},opacity:function(){return\"\"===m.style.opacity},textshadow:function(){return\"\"===k.textShadow},multiplebgs:function(){k.cssText=\"background:url(//:),url(//:),red url(//:)\";return/(url\\s*\\(.*?){3}/.test(k.background)},boxshadow:function(){return f(\"boxShadow\")},borderimage:function(){return f(\"borderImage\")},borderradius:function(){return f(\"borderRadius\")},cssreflections:function(){return f(\"boxReflect\")},");// 8
		writer.writeCode(
				"csstransforms:function(){return f(\"transform\")},csstransitions:function(){return f(\"transition\")},touch:function(){return\"ontouchstart\"in a},retina:function(){return 1<a.devicePixelRatio},fontface:function(){var a=n.browser.version;switch(n.browser.name){case \"ie\":return 9<=a;case \"chrome\":return 13<=a;case \"ff\":return 6<=a;case \"ios\":return 5<=a;case \"android\":return!1;case \"webkit\":return 5.1<=a;case \"opera\":return 10<=a;default:return!1}}},t;for(t in g)g[t]&&n.feature(t,g[t].call(),!0);n.feature()})(window);");
		writer.writeCode(
				"(function(a,w){function f(){}function m(j,a){if(j){\"object\"===typeof j&&(j=[].slice.call(j));for(var b=0,c=j.length;b<c;b++)a.call(j,j[b],b)}}function k(a,b){var e=Object.prototype.toString.call(b).slice(8,-1);return b!==w&&null!==b&&e===a}function s(a){return k(\"Function\",a)}function r(a){a=a||f;a._done||(a(),a._done=1)}function n(a){var b={};if(\"object\"===typeof a)for(var e in a)a[e]&&(b={name:e,url:a[e]});else b=a.split(\"/\"),b=b[b.length-1],e=b.indexOf(\"?\"),b={name:-1!==e?b.substring(0,e):b,url:a};");// 10
		writer.writeCode(
				"return(a=i[b.name])&&a.url===b.url?a:i[b.name]=b}function g(a){var a=a||i,b;for(b in a)if(a.hasOwnProperty(b)&&a[b].state!==y)return!1;return!0}function t(a,b){b=b||f;a.state===y?b():a.state===D?d.ready(a.name,b):a.state===C?a.onpreload.push(function(){t(a,b)}):(a.state=D,q(a,function(){a.state=y;b();m(x[a.name],function(a){r(a)});u&&g()&&m(x.ALL,function(a){r(a)})}))}function q(j,c){var c=c||f,e;/\\.css[^\\.]*$/.test(j.url)?(e=b.createElement(\"link\"),e.type=\"text/\"+(j.type||\"css\"),e.rel=\"stylesheet\",");
		writer.writeCode(
				"e.href=j.url):(e=b.createElement(\"script\"),e.type=\"text/\"+(j.type||\"javascript\"),e.src=j.url);e.onload=e.onreadystatechange=function(j){j=j||a.event;if(\"load\"===j.type||/loaded|complete/.test(e.readyState)&&(!b.documentMode||9>b.documentMode))e.onload=e.onreadystatechange=e.onerror=null,c()};e.onerror=function(){e.onload=e.onreadystatechange=e.onerror=null;c()};e.async=!1;e.defer=!1;var d=b.head||b.getElementsByTagName(\"head\")[0];d.insertBefore(e,d.lastChild)}function p(){b.body?u||(u=!0,m(h,function(a){r(a)})):");// 12
		writer.writeCode(
				"(a.clearTimeout(d.readyTimeout),d.readyTimeout=a.setTimeout(p,50))}function c(){b.addEventListener?(b.removeEventListener(\"DOMContentLoaded\",c,!1),p()):\"complete\"===b.readyState&&(b.detachEvent(\"onreadystatechange\",c),p())}var b=a.document,h=[],v=[],x={},i={},A=\"async\"in b.createElement(\"script\")||\"MozAppearance\"in b.documentElement.style||a.opera,l,u,B=a.head_conf&&a.head_conf.head||\"head\",d=a[B]=a[B]||function(){d.ready.apply(null,arguments)},C=1,D=3,y=4;d.load=A?function(){var a=arguments,b=a[a.length-");
		writer.writeCode(
				"1],e={};s(b)||(b=null);m(a,function(c,d){c!==b&&(c=n(c),e[c.name]=c,t(c,b&&d===a.length-2?function(){g(e)&&r(b)}:null))});return d}:function(){var a=arguments,b=[].slice.call(a,1),c=b[0];if(!l)return v.push(function(){d.load.apply(null,a)}),d;c?(m(b,function(a){if(!s(a)){var b=n(a);b.state===w&&(b.state=C,b.onpreload=[],q({url:b.url,type:\"cache\"},function(){b.state=2;m(b.onpreload,function(a){a.call()})}))}}),t(n(a[0]),s(c)?c:function(){d.load.apply(null,b)})):t(n(a[0]));return d};d.js=d.load;d.test=");// 14
		writer.writeCode(
				"function(a,b,c,g){a=\"object\"===typeof a?a:{test:a,success:b?k(\"Array\",b)?b:[b]:!1,failure:c?k(\"Array\",c)?c:[c]:!1,callback:g||f};(b=!!a.test)&&a.success?(a.success.push(a.callback),d.load.apply(null,a.success)):!b&&a.failure?(a.failure.push(a.callback),d.load.apply(null,a.failure)):g();return d};d.ready=function(a,c){if(a===b)return u?r(c):h.push(c),d;s(a)&&(c=a,a=\"ALL\");if(\"string\"!==typeof a||!s(c))return d;var e=i[a];if(e&&e.state===y||\"ALL\"===a&&g()&&u)return r(c),d;(e=x[a])?e.push(c):x[a]=[c];");
		writer.writeCode(
				"return d};d.ready(b,function(){g()&&m(x.ALL,function(a){r(a)});d.feature&&d.feature(\"domloaded\",!0)});if(\"complete\"===b.readyState)p();else if(b.addEventListener)b.addEventListener(\"DOMContentLoaded\",c,!1),a.addEventListener(\"load\",p,!1);else{b.attachEvent(\"onreadystatechange\",c);a.attachEvent(\"onload\",p);var z=!1;try{z=null==a.frameElement&&b.documentElement}catch(F){}z&&z.doScroll&&function E(){if(!u){try{z.doScroll(\"left\")}catch(b){a.clearTimeout(d.readyTimeout);d.readyTimeout=a.setTimeout(E,50);");// 16
		writer.writeCode("return}p()}}()}setTimeout(function(){l=!0;m(v,function(a){a()})},300)})(window);");
		writer.writeCode("birt={loader:head};");
		writer.writeCode(" //]]>"); //$NON-NLS-1$
		writer.writeCode("</script>"); //$NON-NLS-1$
	}

	protected void doClientInitialize(String javaScriptLibraries) {
		writer.writeCode("<script type=\"text/javascript\">"); //$NON-NLS-1$
		writer.writeCode(" //<![CDATA["); //$NON-NLS-1$
		writer.writeCode(javaScriptLibraries); // $NON-NLS-1$
		writer.writeCode(" //]]>"); //$NON-NLS-1$
		writer.writeCode("</script>"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.emitter.IContentEmitter#start(org.eclipse.birt
	 * .report.engine.content.IReportContent)
	 */
	@Override
	public void start(IReportContent report) {
		logger.log(Level.FINEST, "[HTMLReportEmitter] Start emitter."); //$NON-NLS-1$

		this.report = report;
		writer.open(out, "UTF-8"); //$NON-NLS-1$

		ReportDesignHandle designHandle = null;
		Report reportDesign = null;
		if (report != null) {
			reportDesign = report.getDesign();
			designHandle = reportDesign.getReportDesign();

			// Get dpi.
			Map<?, ?> appContext = reportContext.getAppContext();
			if (appContext != null) {
				Object tmp = appContext.get(EngineConstants.APPCONTEXT_CHART_RESOLUTION);
				if (tmp instanceof Number) {
					imageDpi = ((Number) tmp).intValue();
				}
			}
			if (imageDpi <= 0) {
				imageDpi = designHandle.getImageDPI();
			}
			if (imageDpi <= 0) {
				// Set default image dpi.
				imageDpi = 96;
			}
		}
		retrieveRtLFlag(); // bidi_hcg
		if (null == layoutPreference) {
			// get the layout preference from the report design.
			if (designHandle != null) {
				String reportLayoutPreference = designHandle.getLayoutPreference();
				if (DesignChoiceConstants.REPORT_LAYOUT_PREFERENCE_FIXED_LAYOUT.equals(reportLayoutPreference)) {
					layoutPreference = IHTMLRenderOption.LAYOUT_PREFERENCE_FIXED;
					fixedReport = true;
				} else if (DesignChoiceConstants.REPORT_LAYOUT_PREFERENCE_AUTO_LAYOUT.equals(reportLayoutPreference)) {
					layoutPreference = IHTMLRenderOption.LAYOUT_PREFERENCE_AUTO;
					fixedReport = false;
				}
			}
		} else {
			fixedReport = IHTMLRenderOption.LAYOUT_PREFERENCE_FIXED.equals(layoutPreference);
		}
		if (enableAgentStyleEngine) {
			htmlEmitter = new HTMLPerformanceOptimize(this, writer, fixedReport, enableInlineStyle, browserVersion);
		} else {
			// we will use HTMLVisionOptimize as the default emitter.
			htmlEmitter = new HTMLVisionOptimize(this, writer, fixedReport, enableInlineStyle, htmlRtLFlag,
					browserVersion);
		}

		/*
		 * if the BIRT-preview called the emitter then the scripts will be added on a
		 * div-tag (there won't be created a full HTML-document)
		 */
		if (isEmbeddable) {
			outputCSSStyles(reportDesign, designHandle);

			// CSS hyperlink text undecoration
			addStyleTextHyperlinkDecorationNone();

			if (needFixTransparentPNG) {
				fixTransparentPNG();
			}
			// diagonal & antidiagonal special function
			addCellDiagonalSpecialJs();

			fixRedirect();

			openRootTag();
			writeBidiFlag();

			// output the report default style
			if (report != null) {
				String defaultStyleName = report.getDesign().getRootStyleName();
				if (defaultStyleName != null) {
					if (enableInlineStyle) {
						StringBuffer defaultStyleBuffer = new StringBuffer();
						IStyle defaultStyle = report.findStyle(defaultStyleName);
						htmlEmitter.buildDefaultStyle(defaultStyleBuffer, defaultStyle);
						if (defaultStyleBuffer.length() > 0) {
							writer.attribute(HTMLTags.ATTR_STYLE, defaultStyleBuffer.toString());
						}
					} else if (htmlIDNamespace != null) {
						writer.attribute(HTMLTags.ATTR_CLASS, htmlIDNamespace + defaultStyleName);
					} else {
						writer.attribute(HTMLTags.ATTR_CLASS, defaultStyleName);
					}
				}
			}

			outputDIVTitle(report);
			outputClientScript(report);

			return;
		}

		openRootTag();
		writeBidiFlag();
		writer.openTag(HTMLTags.TAG_HEAD);


		// write the title of the report in html.
		outputReportTitle(report);

		writer.openTag(HTMLTags.TAG_META);
		writer.attribute(HTMLTags.ATTR_HTTP_EQUIV, "Content-Type"); //$NON-NLS-1$
		writer.attribute(HTMLTags.ATTR_CONTENT, getContentType()); // $NON-NLS-1$

		boolean needCloseTag = !OUTPUT_FORMAT_HTML.equals(getOutputFormat());
		if (needCloseTag) {// bugzilla 295062: ignore writing the close tag in HTML format
			writer.closeTag(HTMLTags.TAG_META);
		}

		// added for mobile device support
		String viewport = new HTMLRenderOption(renderOption).getViewportMeta();
		if (viewport != null) {
			writer.openTag(HTMLTags.TAG_META);
			writer.attribute(HTMLTags.ATTR_NAME, "viewport"); //$NON-NLS-1$
			writer.attribute(HTMLTags.ATTR_CONTENT, viewport); // $NON-NLS-1$
			if (needCloseTag) {
				writer.closeTag(HTMLTags.TAG_META);
			}
		}


		outputCSSStyles(reportDesign, designHandle);

		// CSS hyperlink text un-decoration
		addStyleTextHyperlinkDecorationNone();

		if (needFixTransparentPNG) {
			fixTransparentPNG();
		}
		// diagonal & antidiagonal special function
		addCellDiagonalSpecialJs();

		fixRedirect();
		// client initialize
		String clientInitialize = null;
		if (report != null) {
			clientInitialize = report.getDesign().getReportDesign().getClientInitialize();
		}
		if (!StringUtil.isBlank(clientInitialize)) {
			// get the value of birtJsUrl on ModuleOption
			String birtJsUrl = new HTMLRenderOption(renderOption).getBirtJsUrl();
			if (!StringUtil.isBlank(birtJsUrl)) {
				loadBirtJs(birtJsUrl);
			} else {
				outputBirtJs();
			}
			doClientInitialize(clientInitialize);
		}

		writer.closeTag(HTMLTags.TAG_HEAD);

		writer.openTag(HTMLTags.TAG_BODY);
		// output the report default style
		StringBuffer defaultStyleBuffer = new StringBuffer();
		if (report != null) {
			String defaultStyleName = report.getDesign().getRootStyleName();
			if (defaultStyleName != null) {
				if (enableInlineStyle) {
					IStyle defaultStyle = report.findStyle(defaultStyleName);
					htmlEmitter.buildDefaultStyle(defaultStyleBuffer, defaultStyle);
				} else if (htmlIDNamespace != null) {
					writer.attribute(HTMLTags.ATTR_CLASS, htmlIDNamespace + defaultStyleName);
				} else {
					writer.attribute(HTMLTags.ATTR_CLASS, defaultStyleName);
				}
			}
		}

		if (outputMasterPageContent) {
			// remove the default margin of the html body
			defaultStyleBuffer.append(" margin:0px;");
		}

		if (defaultStyleBuffer.length() > 0) {
			writer.attribute(HTMLTags.ATTR_STYLE, defaultStyleBuffer.toString());
		}

		outputClientScript(report);
	}

	protected void outputDIVTitle(IReportContent report) {
		String title = getReportTitle(report);
		if (title != null) {
			writer.attribute(HTMLTags.ATTR_TITLE, title);
		}
	}

	/**
	 * @return Get the content type of current format.
	 */
	protected String getContentType() {
		return "text/html; charset=utf-8";
	}

	protected void outputClientScript(IReportContent report) {
		if (report != null) {
			IContent root = report.getRoot();
			Map<String, Object> extensions = root.getExtensions();
			if (extensions != null) {
				String clientScripts = (String) extensions.get(EXTENSION_HTML_CLIENT_SCRIPTS);
				if (clientScripts != null) {
					writer.openTag(HTMLTags.TAG_DIV);
					outputBookmark(root, HTMLTags.TAG_DIV);
					writer.attribute(HTMLTags.ATTR_STYLE, "display:none");
					writer.closeTag(HTMLTags.TAG_DIV);
					outputClientScript(root);
				}
			}
		}
	}

	private void outputClientScript(IContent content) {
		Map<String, Object> extensions = content.getExtensions();
		if (extensions != null) {
			String clientScripts = (String) extensions.get(EXTENSION_HTML_CLIENT_SCRIPTS);
			if (clientScripts != null) {
				writer.openTag(HTMLTags.TAG_SCRIPT);
				writer.attribute(HTMLTags.ATTR_TYPE, "text/javascript");
				this.writer.writeCode(clientScripts);
				writer.closeTag(HTMLTags.TAG_SCRIPT);
			}
		}
	}

	/**
	 * open the report root tag.
	 */
	protected void openRootTag() {
		if (isEmbeddable) {
			writer.openTag(HTMLTags.TAG_DIV);
			// output DIV id attribute
			String id = BIRT_ROOT;
			if (htmlIDNamespace != null) {
				id = htmlIDNamespace + id;
			}
			writer.attribute(HTMLTags.ATTR_ID, id);
		} else {
			// The document type must be output before open the "html" tag.
			writer.outputDoctype();
			writer.openTag(HTMLTags.TAG_HTML);
		}
	}

	/**
	 * output the report title.
	 */
	protected void outputReportTitle(IReportContent report) {
		String title = getReportTitle(report);
		if (title != null) {
			writer.openTag(HTMLTags.TAG_TITLE);
			writer.text(title);
			writer.closeTag(HTMLTags.TAG_TITLE);
		}
	}

	protected String getReportTitle(IReportContent report) {
		// write the title of the report in HTML.
		String title = null;
		if (report != null) {
			title = report.getTitle();
		}
		if (title == null) {
			// set the default title
			if (renderOption != null) {
				HTMLRenderOption htmlOption = new HTMLRenderOption(renderOption);
				title = htmlOption.getHtmlTitle();
			}
		}
		return title;
	}

	private void outputCSSStyles(Report reportDesign, ReportDesignHandle designHandle) {
		if (report == null) {
			logger.log(Level.WARNING, "[HTMLReportEmitter] Report object is null."); //$NON-NLS-1$
		} else // output the report CSS styles
		if (!enableInlineStyle) {
			openStyleSheet();
			String styleNamePrefix;
			if (null != htmlIDNamespace) {
				styleNamePrefix = "." + htmlIDNamespace;
			} else {
				styleNamePrefix = ".";
			}
			String defaultStyleName = reportDesign.getRootStyleName();
			Map<?, ?> styles = reportDesign.getStyles();
			Iterator<?> iter = styles.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String styleName = (String) entry.getKey();
				if (styleName != null) {
					IStyle style = (IStyle) entry.getValue();
					StringBuffer styleBuffer = new StringBuffer();
					if (styleName.equals(defaultStyleName)) {
						htmlEmitter.buildDefaultStyle(styleBuffer, style);
					} else {
						htmlEmitter.buildStyle(styleBuffer, style);
					}

					if (styleBuffer.length() > 0) {
						writer.style(styleNamePrefix + styleName, styleBuffer.toString());
						outputtedStyles.add(styleName);
					}
				}
			}
			closeStyleSheet();
		}

		// export the CSS links in the HTML
		if (designHandle != null) {
			List<IncludedCssStyleSheetHandle> externalCsses = designHandle.getAllExternalIncludedCsses();
			if (null != externalCsses) {
				Iterator<IncludedCssStyleSheetHandle> iter = externalCsses.iterator();
				while (iter.hasNext()) {
					IncludedCssStyleSheetHandle cssStyleSheetHandle = iter.next();
					String href = cssStyleSheetHandle.getExternalCssURI();
					if (cssStyleSheetHandle.isUseExternalCss() || href != null) {
						hasCsslinks = true;
					}
					if (href != null) {
						// output the CSS link
						writer.openTag(HTMLTags.TAG_LINK);
						writer.attribute(HTMLTags.ATTR_REL, "stylesheet");
						writer.attribute(HTMLTags.ATTR_TYPE, "text/css");
						writer.attribute(HTMLTags.ATTR_HREF, href);
						writer.closeTag(HTMLTags.TAG_LINK);
					}
				}
			}
		}
	}

	protected void openStyleSheet() {
		writer.openTag(HTMLTags.TAG_STYLE);
		writer.attribute(HTMLTags.ATTR_TYPE, "text/css"); //$NON-NLS-1$
	}

	protected void closeStyleSheet() {
		writer.closeTag(HTMLTags.TAG_STYLE);
	}

	private void appendErrorMessage(EngineResourceHandle rc, int index, ElementExceptionInfo info) {
		writer.writeCode("			<div>");
		writer.writeCode("				<div  id=\"error_title\" style=\"text-decoration:underline\">");
		String name = info.getName();
		if (name != null) {
			writer.text(rc.getMessage(MessageConstants.REPORT_ERROR_MESSAGE, new Object[] { info.getType(), name }),
					false);
		} else {
			writer.text(rc.getMessage(MessageConstants.REPORT_ERROR_MESSAGE_WITH_ID,
					new Object[] { info.getType(), info.getID() }), false);
		}
		writer.writeCode("</div>");//$NON-NLS-1$

		ArrayList<?> errorList = info.getErrorList();
		ArrayList<?> countList = info.getCountList();
		for (int i = 0; i < errorList.size(); i++) {
			String errorId = "document.getElementById('error_detail" + index + "_" + i + "')";
			String errorIcon = "document.getElementById('error_icon" + index + "_" + i + "')";
			String onClick = "if (" + errorId + ".style.display == 'none') { " + errorIcon + ".innerHTML = '- '; "
					+ errorId + ".style.display = 'block'; }" + "else { " + errorIcon + ".innerHTML = '+ '; " + errorId
					+ ".style.display = 'none'; }";
			writer.writeCode("<div>");
			BirtException ex = (BirtException) errorList.get(i);
			writer.writeCode("<span id=\"error_icon" + index + "_" + i + "\"  style=\"cursor:pointer\" onclick=\""
					+ onClick + "\" > + </span>");

			writer.text(ex.getLocalizedMessage());

			writer.writeCode("				<pre id=\"error_detail" + index + "_" + i //$NON-NLS-1$
					+ "\" style=\"display:none;\" >");//$NON-NLS-1$

			String messageTitle = rc.getMessage(MessageConstants.REPORT_ERROR_ID,
					new Object[] { ex.getErrorCode(), countList.get(i) });
			String detailTag = rc.getMessage(MessageConstants.REPORT_ERROR_DETAIL);
			String messageBody = getDetailMessage(ex);
			boolean indent = writer.isIndent();
			writer.setIndent(false);
			writer.text(messageTitle, false);
			writer.writeCode("\r\n");//$NON-NLS-1$
			writer.text(detailTag, false);
			writer.text(messageBody, false);
			writer.setIndent(indent);
			writer.writeCode("				</pre>"); //$NON-NLS-1$
			writer.writeCode("</div>");
		}

		writer.writeCode("</div>"); //$NON-NLS-1$
		writer.writeCode("<br>"); //$NON-NLS-1$
	}

	private String getDetailMessage(Throwable t) {
		StringWriter out = new StringWriter();
		PrintWriter print = new PrintWriter(out);
		try {
			t.printStackTrace(print);
		} catch (Throwable ex) {
		}
		print.flush();
		return out.getBuffer().toString();
	}

	protected boolean outputErrors(List<?> errors) {
		// Outputs the error message at the end of the report
		if (errors != null && !errors.isEmpty()) {
			writer.writeCode("	<hr style=\"color:red\"/>");
			writer.writeCode("	<div style=\"color:red\">");
			writer.writeCode("		<div>");
			Locale locale = reportContext.getLocale();
			if (locale == null) {
				locale = Locale.getDefault();
			}
			EngineResourceHandle rc = new EngineResourceHandle(ULocale.forLocale(locale));
			writer.text(rc.getMessage(MessageConstants.ERRORS_ON_REPORT_PAGE), false);

			writer.writeCode("</div>");//$NON-NLS-1$
			writer.writeCode("<br>");//$NON-NLS-1$
			Iterator<?> it = errors.iterator();
			int index = 0;
			while (it.hasNext()) {
				appendErrorMessage(rc, index++, (ElementExceptionInfo) it.next());
			}
			writer.writeCode("</div>");
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.emitter.IContentEmitter#end(org.eclipse.birt.
	 * report.engine.content.IReportContent)
	 */
	@Override
	public void end(IReportContent report) {
		logger.log(Level.FINEST, "[HTMLReportEmitter] End body."); //$NON-NLS-1$
		if (report != null) {
			List<?> errors = report.getErrors();
			if (errors != null && !errors.isEmpty()) {
				outputErrors(errors);
			}
		}
		if (!isEmbeddable) {
			writer.closeTag(HTMLTags.TAG_BODY);
			writer.closeTag(HTMLTags.TAG_HTML);
		} else {
			writer.closeTag(HTMLTags.TAG_DIV);
		}

		writer.endWriter();
		writer.close();
		if (out != null) {
			try {
				out.close();
			} catch (IOException e) {
				logger.log(Level.WARNING, e.getMessage(), e);
			}
		}
	}

	private boolean isSameUnit(String unit1, String unit2) {
		if ((unit1 == unit2) || (unit1 != null && unit1.equals(unit2))) {
			return true;
		}
		return false;
	}

	protected DimensionType getPageWidth(IPageContent page) {
		DimensionType pageWidth = page.getPageWidth();
		if (!outputMasterPageMargins) {
			DimensionType leftMargin = page.getMarginLeft();
			DimensionType rightMargin = page.getMarginRight();
			return removeMargin(pageWidth, leftMargin, rightMargin);
		}
		return pageWidth;

	}

	protected DimensionType getPageHeight(IPageContent page) {
		DimensionType pageHeight = page.getPageHeight();
		if (!outputMasterPageMargins) {
			DimensionType topMargin = page.getMarginTop();
			DimensionType bottomMargin = page.getMarginBottom();

			return removeMargin(pageHeight, topMargin, bottomMargin);
		}

		return pageHeight;
	}

	private DimensionType removeMargin(DimensionType pageWidth, DimensionType leftMargin, DimensionType rightMargin) {
		double measure = pageWidth.getMeasure();
		String unit = pageWidth.getUnits();
		if (leftMargin != null) {
			if (isSameUnit(unit, leftMargin.getUnits())) {
				measure -= leftMargin.getMeasure();
			} else if (DimensionUtil.isAbsoluteUnit(unit) && DimensionUtil.isAbsoluteUnit(leftMargin.getUnits())) {
				DimensionValue converted = DimensionUtil.convertTo(leftMargin.getMeasure(), leftMargin.getUnits(),
						unit);
				measure -= converted.getMeasure();
			}
		}
		if (rightMargin != null) {
			if (isSameUnit(unit, rightMargin.getUnits())) {
				measure -= rightMargin.getMeasure();

			} else if (DimensionUtil.isAbsoluteUnit(unit) && DimensionUtil.isAbsoluteUnit(rightMargin.getUnits())) {
				DimensionValue converted = DimensionUtil.convertTo(rightMargin.getMeasure(), rightMargin.getUnits(),
						unit);
				measure -= converted.getMeasure();
			}
		}
		if (measure > 0) {
			return new DimensionType(measure, unit);
		}
		return pageWidth;
	}

	protected void outputColumn(DimensionType dm) {
		writer.openTag(HTMLTags.TAG_COL);

		StringBuilder styleBuffer = new StringBuilder();
		styleBuffer.append("width: ");
		if (dm != null) {
			styleBuffer.append(dm.toString());
		} else {
			styleBuffer.append("0pt");
		}
		styleBuffer.append(";");
		writer.attribute(HTMLTags.ATTR_STYLE, styleBuffer.toString());
		writer.closeTag(HTMLTags.TAG_COL);
	}

	protected void outputVMargin(DimensionType margin) {
		// If margin isn't null, output a row to implement it.
		if (null != margin) {
			writer.openTag(HTMLTags.TAG_TR);
			StringBuilder styleBuffer = new StringBuilder();
			styleBuffer.append("height: ");
			styleBuffer.append(margin.toString());
			styleBuffer.append(";");
			writer.attribute(HTMLTags.ATTR_STYLE, styleBuffer.toString());
			writer.openTag(HTMLTags.TAG_TD);
			writer.attribute(HTMLTags.ATTR_COLSPAN, 3);
			writer.closeTag(HTMLTags.TAG_TD);
			writer.closeTag(HTMLTags.TAG_TR);
		}
	}

	protected void outputHMargin(DimensionType margin) {
		writer.openTag(HTMLTags.TAG_TD);
		if (null != margin) {
			writer.openTag(HTMLTags.TAG_DIV);
			StringBuilder styleBuffer = new StringBuilder();
			styleBuffer.append("width: ");
			styleBuffer.append(margin.toString());
			styleBuffer.append(";");
			writer.attribute(HTMLTags.ATTR_STYLE, styleBuffer.toString());
			writer.closeTag(HTMLTags.TAG_DIV);
		}
		writer.closeTag(HTMLTags.TAG_TD);
	}

	protected boolean showPageHeader(IPageContent page) {
		boolean showHeader = true;
		Object genBy = page.getGenerateBy();
		if (genBy instanceof SimpleMasterPageDesign) {
			SimpleMasterPageDesign masterPage = (SimpleMasterPageDesign) genBy;
			if (!masterPage.isShowHeaderOnFirst()) {
				if (page.getPageNumber() == 1) {
					showHeader = false;
				}
			}
		}
		return showHeader;
	}

	protected boolean showPageFooter(IPageContent page) {
		boolean showFooter = true;
		Object genBy = page.getGenerateBy();
		if (genBy instanceof SimpleMasterPageDesign) {
			SimpleMasterPageDesign masterPage = (SimpleMasterPageDesign) genBy;
			if (!masterPage.isShowFooterOnLast()) {
				long totalPage = page.getPageNumber();
				IReportContent report = page.getReportContent();
				if (report != null) {
					totalPage = report.getTotalPage();
				}
				if (page.getPageNumber() == totalPage) {
					showFooter = false;
				}
			}
		}
		return showFooter;
	}

	protected void outputPageBand(IPageContent page, IContent band, DimensionType height) throws BirtException {
		writer.openTag(HTMLTags.TAG_TD);
		writeBidiFlag();
		StringBuffer styleBuffer = new StringBuffer();
		htmlEmitter.buildPageBandStyle(styleBuffer, page.getStyle());
		// output the page header attribute
		writer.attribute(HTMLTags.ATTR_STYLE, styleBuffer.toString());

		boolean fixedHeight = fixedReport && height != null && !band.getChildren().isEmpty();
		if (fixedHeight) {
			writer.openTag(HTMLTags.TAG_DIV);
			styleBuffer.delete(0, styleBuffer.length());
			styleBuffer.append("overflow:hidden; height:");
			styleBuffer.append(height.toString());
			writer.attribute(HTMLTags.ATTR_STYLE, styleBuffer.toString());
		}
		// output the contents of header
		contentVisitor.visitChildren(band, null);

		if (fixedHeight) {
			writer.closeTag(HTMLTags.TAG_DIV);
		}
		// close the page header
		writer.closeTag(HTMLTags.TAG_TD);
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
	 * <td>
	 * <table border="all" style="width:2in;height:2in;">
	 * <col width="0.3in"/> <col width="100%"/> <col width="0.3in"/>
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
	 * <table>
	 * </td>
	 * </table>
	 * <tr valign="top">
	 * <td>TRUE</td>
	 * <td>
	 * <table border="all" style="width:2in;">
	 * <col width="0.3in"/> <col width="100%"/> <col width="0.3in"/>
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
	 * </table>
	 * </td>
	 * </tr>
	 * <tr valign="top">
	 * <td rowspan="2">FALSE</td>
	 * <td>TRUE</td>
	 * <td>
	 * <table border="all" style="width:1.6in;height:1in;">
	 * <col/>
	 * <tr>
	 * <td valign="top"><div>header</div></td>
	 * </tr>
	 * <tr style="height:100%">
	 * <td><div>body</div></td>
	 * </tr>
	 * <tr>
	 * <td><div>footer</div></td>
	 * </tr>
	 * </table>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td>FALSE</td>
	 * <td>
	 * <table border="all" style="width:1.6in;">
	 * <col/>
	 * <tr>
	 * <td valign="top"><div>header</div></td>
	 * </tr>
	 * <tr>
	 * <td><div>body</div></td>
	 * </tr>
	 * <tr>
	 * <td><div>footer</div></td>
	 * </tr>
	 * </table>
	 * </td>
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
	 * </table>
	 * </td>
	 * </tr>
	 * </table>
	 */
	@Override
	public void startPage(IPageContent page) throws BirtException {
		pageNo++;

		if (pageNo > 1 && !outputMasterPageContent) {
			writer.openTag("hr");
			writer.closeTag("hr");
		}

		if (pageNo > 1) {
			writer.writeCode(
					" <div style=\"visibility: hidden; height: 0px; overflow: hidden; page-break-after: always;\">page separator</div>");
		}

		// out put the page tag
		DimensionType width = null;
		DimensionType height = null;
		if (page != null && outputMasterPageContent) {
			width = getPageWidth(page);
			height = getPageHeight(page);
			if (width != null && height != null && fixedReport && !pageFooterFloatFlag) {
				startBackgroundContainer(page.getStyle(), width, height);
			}
		}

		StringBuffer styleBuffer = new StringBuffer();
		writer.openTag(HTMLTags.TAG_TABLE);
		writer.attribute("cellpadding", "0");
		styleBuffer.append("empty-cells: show; display:table-cell; border-collapse:collapse;"); //$NON-NLS-1$

		if (page != null && outputMasterPageContent) {
			htmlEmitter.buildPageStyle(page, styleBuffer, needOutputBackgroundSize);
			// build the width
			if (fixedReport && width != null) {
				styleBuffer.append(" width:");
				styleBuffer.append(width.toString());
				styleBuffer.append(";");
			} else if (!fixedReport) {
				styleBuffer.append(" width:100%;");
			}

			if (!pageFooterFloatFlag && height != null) {
				styleBuffer.append(" height:");
				styleBuffer.append(height.toString());
				styleBuffer.append(";");
			}

			if (fixedReport) {
				// hide the overflow
				styleBuffer.append(" overflow: hidden;");
				styleBuffer.append(" table-layout:fixed;");
			}
		} else {
			styleBuffer.append("width:100%;");
		}
		if (styleBuffer.indexOf(HTMLTags.ATTR_BORDER) < 0) {
			writer.attribute(HTMLTags.ATTR_BORDER, "0");
		}
		writer.attribute(HTMLTags.ATTR_STYLE, styleBuffer.toString());

		if (page != null && outputMasterPageContent) {
			if (outputMasterPageMargins) {
				// Implement left margin.
				outputColumn(page.getMarginLeft());
			}

			writer.openTag(HTMLTags.TAG_COL);
			writer.closeTag(HTMLTags.TAG_COL);

			if (outputMasterPageMargins) {
				// Implement right margin.
				outputColumn(page.getMarginLeft());

				// If top margin isn't null, output a row to implement it.
				outputVMargin(page.getMarginTop());
			}

			// we need output the page header
			if (showPageHeader(page)) {
				writer.openTag(HTMLTags.TAG_TR);
				if (outputMasterPageMargins) {
					outputHMargin(page.getMarginLeft());
				}
				outputPageBand(page, page.getPageHeader(), page.getHeaderHeight());
				if (outputMasterPageMargins) {
					outputHMargin(page.getMarginRight());
				}
				writer.closeTag(HTMLTags.TAG_TR);
			}
		}

		// output the page body
		writer.openTag(HTMLTags.TAG_TR);
		if (!pageFooterFloatFlag) {
			writer.attribute(HTMLTags.ATTR_STYLE, "height:100%;");
		}
		if (page != null && outputMasterPageContent && outputMasterPageMargins) {
			outputHMargin(page.getMarginLeft());
		}
		writer.openTag(HTMLTags.TAG_TD);
		writer.attribute("valign", "top");
		writeBidiFlag();
	}

	private void startBackgroundContainer(IStyle style, DimensionType pageWidth, DimensionType pageHeight) {
		String backgroundHeight = parseBackgroundSize(style.getBackgroundHeight(), pageHeight);
		String backgroundWidth = parseBackgroundSize(style.getBackgroundWidth(), pageWidth);
		if (backgroundHeight == null && backgroundWidth == null) {
			return;
		}
		if (backgroundHeight == null) {
			backgroundHeight = "auto";
		}
		if (backgroundWidth == null) {
			backgroundWidth = "auto";
		}

		String image = style.getBackgroundImage();
		if (image == null || "none".equalsIgnoreCase(image)) //$NON-NLS-1$
		{
			return;
		}
		needOutputBackgroundSize = true;
		writer.openTag(HTMLTags.TAG_DIV);
		StringBuffer sb = new StringBuffer();
		sb.append("width:").append(pageWidth).append(";");
		sb.append("height:").append(pageHeight).append(";");
		AttributeBuilder.buildBackground(sb, style, this, null);
		sb.append("background-size:").append(backgroundWidth).append(" ").append(backgroundHeight).append(";");
		writer.attribute(HTMLTags.ATTR_STYLE, sb.toString());
	}

	protected String parseBackgroundSize(String backgroundHeight, DimensionType pageHeight) {
		if (backgroundHeight == null) {
			return null;
		}
		backgroundHeight = backgroundHeight.trim();
		if (backgroundHeight.endsWith("%")) {
			try {
				String percent = backgroundHeight.substring(0, backgroundHeight.length() - 1);
				int percentValue = Integer.parseInt(percent);
				return pageHeight.getMeasure() * percentValue / 100 + pageHeight.getUnits();
			} catch (NumberFormatException e) {
				return null;
			}
		}
		return backgroundHeight;
	}

	protected void endBackgroundContainer() {
		writer.closeTag(HTMLTags.TAG_DIV);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.emitter.IContentEmitter#endPage(org.eclipse.
	 * birt.report.engine.content.IPageContent)
	 */
	@Override
	public void endPage(IPageContent page) throws BirtException {

		logger.log(Level.FINEST, "[HTMLReportEmitter] End page."); //$NON-NLS-1$

		// close the page body (TR)
		writer.closeTag(HTMLTags.TAG_TD);

		// output the right margin
		if (page != null && outputMasterPageContent && outputMasterPageMargins) {
			outputHMargin(page.getMarginRight());
		}
		writer.closeTag(HTMLTags.TAG_TR);

		// output the footer and bottom margin
		if (page != null && outputMasterPageContent) {
			if (showPageFooter(page)) {
				writer.openTag(HTMLTags.TAG_TR);
				if (outputMasterPageMargins) {
					outputHMargin(page.getMarginLeft());
				}
				outputPageBand(page, page.getPageFooter(), page.getFooterHeight());
				if (outputMasterPageMargins) {
					outputHMargin(page.getMarginRight());
				}
				writer.closeTag(HTMLTags.TAG_TR);
			}
			if (outputMasterPageMargins) {
				outputVMargin(page.getMarginBottom());
			}
		}
		// close the page tag ( TABLE )
		writer.closeTag(HTMLTags.TAG_TABLE);
		if (needOutputBackgroundSize) {
			endBackgroundContainer();
			needOutputBackgroundSize = false;
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.emitter.IContentEmitter#startTable(org.eclipse
	 * .birt.report.engine.content.ITableContent)
	 */
	@Override
	public void startTable(ITableContent table) {
		cachedStartTable = table;
	}

	protected void doStartTable(ITableContent table) {
		assert table != null;

		boolean DIVWrap = false;
		// The method getStyle( ) will nevel return a null value;
		IStyle style = table.getStyle();

		// If the top level table has the property text-align, the table should
		// be align to the page box.
		if (needImplementAlignTable(table)) {
			writer.openTag(HTMLTags.TAG_DIV);
			DIVWrap = true;
			writer.attribute(HTMLTags.ATTR_ALIGN, style.getTextAlign());
		}

		// The IE8, Firefox3, Firefox3.5, Safari4.0, Chrome2.0 have support the
		// inline-block by themselves.
		// implement the inline table for old version browser
		if (!browserSupportsInlineBlock) {
			CSSValue display = style.getProperty(StyleConstants.STYLE_DISPLAY);
			if (CSSValueConstants.INLINE_VALUE == display || CSSValueConstants.INLINE_BLOCK_VALUE == display) {
				if (!DIVWrap) {
					writer.openTag(HTMLTags.TAG_DIV);
					DIVWrap = true;
				}
				// Only the IE5.5, IE6, IE7 can identify the "*+".
				// only the Firefox1.5 and Firefox2 can identify the
				// "-moz-inline-box".
				// For the IE8, Firefox3, Firefox3.5, Safari4.0, Chrome2.0, the
				// value will be "inline-block".
				writer.attribute(HTMLTags.ATTR_STYLE,
						" display:-moz-inline-box; display:inline-block; *+display:inline;");
			}
		}

		tableDIVWrapedFlagStack.push(Boolean.valueOf(DIVWrap));

		logger.log(Level.FINEST, "[HTMLTableEmitter] Start table"); //$NON-NLS-1$
		// FIXME: code review: use "metadataEmitter != null" to instead of
		// enableMetadata.
		if (enableMetadata) {
			metadataEmitter.startWrapTable(table);
		}
		writer.openTag(HTMLTags.TAG_TABLE);

		// output class attribute.
		String styleClass = table.getStyleClass();
		setStyleName(styleClass, table);

		// FIXME: we need reimplement the table's inline value.
		StringBuffer styleBuffer = new StringBuffer();
		htmlEmitter.buildTableStyle(table, styleBuffer);
		// output style
		writer.attribute(HTMLTags.ATTR_STYLE, styleBuffer.toString());

		boolean bookmarkOutput = false;
		if (metadataFilter != null) {
			bookmarkOutput = metadataEmitter.outputMetadataProperty(
					metadataFilter.needMetaData(HTMLEmitterUtil.getElementHandle(table)), table, HTMLTags.TAG_TABLE);
		}
		if (!bookmarkOutput) {
			// bookmark
			String bookmark = table.getBookmark();
			if (bookmark == null) {
				bookmark = idGenerator.generateUniqueID();
				table.setBookmark(bookmark);
			}
			outputBookmark(table, HTMLTags.TAG_TABLE);
		}

		// table summary
		String summary = table.getSummary();
		writer.attribute(HTMLTags.ATTR_SUMMARY, summary);

		// table caption
		String caption = table.getCaption();
		if (caption != null && caption.length() > 0) {
			writer.openTag(HTMLTags.TAG_CAPTION);
			writer.text(caption);
			writer.closeTag(HTMLTags.TAG_CAPTION);
		}

		// include select handle table
		if (enableMetadata) {
			metadataEmitter.startTable(table);
		}

		writeColumns(table);
		tableLayout.startTable(table);

	}

	protected void writeColumns(ITableContent table) {
		for (int i = 0; i < table.getColumnCount(); i++) {
			IColumn column = table.getColumn(i);

			writer.openTag(HTMLTags.TAG_COL);

			// output class attribute.
			if (enableAgentStyleEngine) {
				// only performance optimize model needs output the column's
				// class attribute. In vision optimize model the column style is
				// output in Cell's columnRelatedStyle, except the column's
				// width.
				String styleClass = column.getStyleClass();
				setStyleName(styleClass, table);
			}

			// column style is output in Cell's columnRelatedStyle

			// width
			StringBuffer styleBuffer = new StringBuffer();
			htmlEmitter.buildColumnStyle(column, styleBuffer);
			writer.attribute(HTMLTags.ATTR_STYLE, styleBuffer.toString());
			htmlEmitter.handleColumnAlign(column);

			if (metadataFilter != null) {
				metadataEmitter.outputMetadataProperty(
						metadataFilter.needMetaData(HTMLEmitterUtil.getElementHandle(column)), column,
						HTMLTags.TAG_COL);
			}

			writer.closeTag(HTMLTags.TAG_COL);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.emitter.IContentEmitter#endTable(org.eclipse.
	 * birt.report.engine.content.ITableContent)
	 */
	@Override
	public void endTable(ITableContent table) {
		if (cachedStartTable != null) {
			cachedStartTable = null;
			return;
		}
		// include select handle table
		if (enableMetadata) {
			metadataEmitter.endTable(table);
		}

		writer.closeTag(HTMLTags.TAG_TABLE);

		if (enableMetadata) {
			metadataEmitter.endWrapTable(table);
		}

		boolean DIVWrap = tableDIVWrapedFlagStack.pop().booleanValue();
		if (DIVWrap) {
			writer.closeTag(HTMLTags.TAG_DIV);
		}
		tableLayout.endTable(table);
		logger.log(Level.FINEST, "[HTMLTableEmitter] End table"); //$NON-NLS-1$
	}

	/**
	 * Judge needing implement the align table or not. The align table should be
	 * align according to the page box.
	 *
	 * @param table
	 * @return Return the information about align of the table
	 */
	protected boolean needImplementAlignTable(ITableContent table) {
		// the table should be the top level.
		if (report.getRoot() == table.getParent()) {
			// The table must has the width, and the width is not 100%.
			DimensionType width = table.getWidth();
			if (null != width && !"100%".equals(width.toString())) {
				// The table must be a block table.
				IStyle style = table.getStyle();
				CSSValue display = style.getProperty(StyleConstants.STYLE_DISPLAY);
				if (null == display || CSSValueConstants.BLOCK_VALUE == display) {
					// The text-align value must be center or right.
					CSSValue align = style.getProperty(StyleConstants.STYLE_TEXT_ALIGN);

					// bidi_hcg start
					// If alignment is inconsistent with direction we need to
					// be explicit for non-center alignment (i.e. alignment
					// left and dir is RTL or alignment right and dir is LTR.
					if (CSSValueConstants.CENTER_VALUE.equals(align))
					// XXX Is justify here applicable?
					// || IStyle.JUSTIFY_VALUE.equals( align ) )
					{
						return true;
					}
					CSSValue direction = style.getProperty(StyleConstants.STYLE_DIRECTION);
					if (CSSValueConstants.RTL_VALUE.equals(direction)) {
						if (!CSSValueConstants.RIGHT_VALUE.equals(align)) {
							return true;
						}
					} else
					// bidi_hcg end

					if ( /* IStyle.CENTER_VALUE == align || */CSSValueConstants.RIGHT_VALUE == align) {
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
	 * @see
	 * org.eclipse.birt.report.engine.emitter.IContentEmitter#startTableHeader(org.
	 * eclipse.birt.report.engine.content.ITableBandContent)
	 */
	/**
	 * Write the table header start
	 *
	 * @param band
	 */
	public void startTableHeader(ITableBandContent band) {
		writer.openTag(HTMLTags.TAG_THEAD);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.emitter.IContentEmitter#endTableHeader(org.
	 * eclipse.birt.report.engine.content.ITableBandContent)
	 */
	/**
	 * Write table header end
	 *
	 * @param band
	 */
	public void endTableHeader(ITableBandContent band) {
		writer.closeTag(HTMLTags.TAG_THEAD);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.emitter.IContentEmitter#startTableBody(org.
	 * eclipse.birt.report.engine.content.ITableBandContent)
	 */
	/**
	 * Write table body start
	 *
	 * @param band
	 */
	public void startTableBody(ITableBandContent band) {
		writer.openTag(HTMLTags.TAG_TBODY);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#endTableBody(org.
	 * eclipse.birt.report.engine.content.ITableBandContent)
	 */
	/**
	 * Write table body end
	 *
	 * @param band
	 */
	public void endTableBody(ITableBandContent band) {
		writer.closeTag(HTMLTags.TAG_TBODY);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.emitter.IContentEmitter#startTableFooter(org.
	 * eclipse.birt.report.engine.content.ITableBandContent)
	 */
	/**
	 * Write the table footer start
	 *
	 * @param band
	 */
	public void startTableFooter(ITableBandContent band) {
		writer.openTag(HTMLTags.TAG_TFOOT);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.emitter.IContentEmitter#endTableFooter(org.
	 * eclipse.birt.report.engine.content.ITableBandContent)
	 */
	/**
	 * Write the table footer end
	 *
	 * @param band
	 */
	public void endTableFooter(ITableBandContent band) {
		writer.closeTag(HTMLTags.TAG_TFOOT);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.emitter.IContentEmitter#startRow(org.eclipse.
	 * birt.report.engine.content.IRowContent)
	 */
	@Override
	public void startRow(IRowContent row) {
		assert row != null;

		if (cachedStartTable != null) {
			doStartTable(cachedStartTable);
			cachedStartTable = null;
		}

		writer.openTag(HTMLTags.TAG_TR);
		if (metadataFilter != null) {
			metadataEmitter.outputMetadataProperty(metadataFilter.needMetaData(HTMLEmitterUtil.getElementHandle(row)),
					row, HTMLTags.TAG_TR);
		}
		if (enableMetadata) {
			metadataEmitter.startRow(row);
		}

		// output class attribute.
		String styleClass = row.getStyleClass();
		setStyleName(styleClass, row);

		// bookmark
		outputBookmark(row, null);

		StringBuffer styleBuffer = new StringBuffer();
		htmlEmitter.buildRowStyle(row, styleBuffer);
		writer.attribute(HTMLTags.ATTR_STYLE, styleBuffer.toString());
		htmlEmitter.handleRowAlign(row);

		if (!startedGroups.isEmpty()) {
			IGroupContent group = startedGroups.firstElement();
			String bookmark = group.getBookmark();
			if (bookmark == null) {
				bookmark = idGenerator.generateUniqueID();
				group.setBookmark(bookmark);
			}
			outputBookmark(group, null);
			startedGroups.remove(group);
		}

		if (fixedReport) {
			DimensionType rowHeight = row.getHeight();
			if (rowHeight != null && !"%".equals(rowHeight.getUnits())) {
				fixedRowHeightStack.push(rowHeight);
			} else {
				fixedRowHeightStack.push(null);
			}
		}

		tableLayout.startRow();

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.emitter.IContentEmitter#endRow(org.eclipse.
	 * birt.report.engine.content.IRowContent)
	 */
	@Override
	public void endRow(IRowContent row) {
		tableLayout.endRow();
		if (enableMetadata) {
			metadataEmitter.endRow(row);
		}
		// assert currentData != null;
		//
		// currentData.adjustCols( );
		writer.closeTag(HTMLTags.TAG_TR);
		if (fixedReport) {
			fixedRowHeightStack.pop();
		}
	}

	protected boolean isCellInHead(ICellContent cell) {
		IElement row = cell.getParent();
		if (row instanceof IRowContent) {
			IElement tableBand = row.getParent();
			if (tableBand instanceof ITableBandContent) {
				int type = ((ITableBandContent) tableBand).getBandType();
				if (type == IBandContent.BAND_HEADER) {
					// is the table head
					return true;
				}
			}

			IColumn column = cell.getColumnInstance();
			if (null != column) {
				// return whether this column is a column header.
				return column.isColumnHeader();
			}
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.emitter.IContentEmitter#startCell(org.eclipse.
	 * birt.report.engine.content.ICellContent)
	 */
	@Override
	public void startCell(ICellContent cell) {
		logger.log(Level.FINEST, "[HTMLTableEmitter] Start cell."); //$NON-NLS-1$

		tableLayout.startCell(cell);
		// output 'th' tag in table head, otherwise 'td' tag
		String tagName = null;
		boolean isHead = isCellInHead(cell);
		if (isHead) {
			tagName = HTMLTags.TAG_TH;
		} else {
			tagName = HTMLTags.TAG_TD;
		}
		writer.openTag(tagName); // $NON-NLS-1$
		writer.attribute("scope", cell.getScope());
		writer.attribute("id", cell.getBookmark());
		writer.attribute("headers", cell.getHeaders());
		// output class attribute.
		String styleClass = cell.getStyleClass();
		setStyleName(styleClass, cell);

		// colspan
		int colSpan = cell.getColSpan();
		if (colSpan > 1) {
			writer.attribute(HTMLTags.ATTR_COLSPAN, colSpan);
		}

		// rowspan
		int rowSpan = cell.getRowSpan();
		if (rowSpan > 1) {
			writer.attribute(HTMLTags.ATTR_ROWSPAN, rowSpan);
		}

		boolean fixedCellHeight = useFixedCellHeight(cell);

		StringBuffer styleBuffer = new StringBuffer();

		// handling of diagonal lines
		String tdDiagonalUriCaller = "";
		String tdDiagonalUUID = "";
		Boolean tdDiagonalSpecial = false;
		if (cell.hasDiagonalLine()) {
			DimensionType cellHeight = null;
			if (fixedCellHeight) {
				cellHeight = fixedRowHeightStack.peek();
			} else {
				cellHeight = getCellHeight(cell);
			}
			tdDiagonalUUID = "bg-img-diag-" + UUID.randomUUID().toString();
			writer.attribute(HTMLTags.ATTR_ID, tdDiagonalUUID);
			String imgUri = "url(" + outputDiagonalImageUri(cell, cellHeight) + ")";
			if (imgUri != null) {
				// line image direct on td-cell level
				if (cell.getStyle().getBackgroundImage() == null) {
					styleBuffer.append("background-image:" + imgUri + " ;");
					styleBuffer.append("background-repeat:no-repeat;");
					styleBuffer.append("background-position:center;");
					styleBuffer.append("background-size:100% 100%;");
				} else {
					tdDiagonalUriCaller = "combineBgImageAndDiagonal('" + tdDiagonalUUID + "','" + imgUri + "');";
					tdDiagonalSpecial = true;
				}
			}
		}
		htmlEmitter.buildCellStyle(cell, styleBuffer, isHead, fixedCellHeight);
		writer.attribute(HTMLTags.ATTR_STYLE, styleBuffer.toString());

		htmlEmitter.handleCellAlign(cell);
		if (fixedCellHeight) {
			// Fixed cell height requires the vertical align must be top.
			writer.attribute(HTMLTags.ATTR_VALIGN, "top");
		} else {
			htmlEmitter.handleCellVAlign(cell);
		}

		// CSS function necessary on diagonal and background image at same time
		if (tdDiagonalSpecial) {
			writer.openTag(HTMLTags.TAG_SCRIPT);
			writer.cdata(tdDiagonalUriCaller);
			writer.closeTag(HTMLTags.TAG_SCRIPT);
		}

		boolean bookmarkOutput = false;
		if (metadataFilter != null) {
			bookmarkOutput = metadataEmitter.outputMetadataProperty(
					metadataFilter.needMetaData(HTMLEmitterUtil.getElementHandle(cell)), cell, tagName);
		}

		if (!startedGroups.isEmpty()) {
			if (!bookmarkOutput) {
				IGroupContent group = startedGroups.firstElement();
				String bookmark = group.getBookmark();
				if (bookmark == null) {
					bookmark = idGenerator.generateUniqueID();
					group.setBookmark(bookmark);
				}
				outputBookmark(group, null);
				startedGroups.remove(group);
			}

			Iterator<ITableGroupContent> iter = startedGroups.iterator();
			while (iter.hasNext()) {
				IGroupContent group = iter.next();
				outputBookmark(group);
			}
			startedGroups.clear();
		}

		if (fixedCellHeight) {
			writer.openTag(HTMLTags.TAG_DIV);
			writer.attribute(HTMLTags.ATTR_STYLE, "position: relative; height: 100%;");
			DimensionType cellHeight = fixedRowHeightStack.peek();
			if (cell.hasDiagonalLine()) {
				outputDiagonalImageUri(cell, cellHeight);
			}
			writer.openTag(HTMLTags.TAG_DIV);
			styleBuffer.setLength(0);
			styleBuffer.append(" height: ");
			styleBuffer.append(cellHeight.toString());
			styleBuffer.append("; width: 100%; position: absolute; left: 0px;");
			HTMLEmitterUtil.buildOverflowStyle(styleBuffer, cell.getStyle(), true);
			writer.attribute(HTMLTags.ATTR_STYLE, styleBuffer.toString());
		}

		if (enableMetadata) {
			metadataEmitter.startCell(cell);
		}
	}

	protected boolean useFixedCellHeight(ICellContent cell) {
		// fixed cell height requires the rowspan to be 1.
		if (cell.getRowSpan() > 1) {
			return false;
		}
		if (fixedReport) {
			IStyle style = cell.getStyle();
			if (style != null) {
				String overflow = style.getOverflow();
				if (CSSConstants.CSS_OVERFLOW_SCROLL_VALUE.equals(overflow)) {
					DimensionType cellHeight = fixedRowHeightStack.peek();
					if (cellHeight != null) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Create the uri of the diagonal line image
	 *
	 * @param cell       context of cell
	 * @param cellHeight height of the cell element
	 * @return Return the uri of the diagonal line image
	 * @since 4.13
	 */
	protected String outputDiagonalImageUri(ICellContent cell, DimensionType cellHeight) {

		String componentPropertyId = "";
		// componend id with property value key parts
		componentPropertyId += "ciidcid:" + cell.getInstanceID().getComponentID() + ";";
		componentPropertyId += "cdn:" + cell.getDiagonalNumber() + ";cds:" + cell.getDiagonalStyle() + ";";
		componentPropertyId += "cdw:" + cell.getDiagonalWidth() + ";cdc:" + cell.getDiagonalColor() + ";";
		componentPropertyId += "cadn:" + cell.getAntidiagonalNumber() + ";cads:" + cell.getAntidiagonalStyle() + ";";
		componentPropertyId += "cadw:" + cell.getAntidiagonalWidth() + ";cadc:" + cell.getAntidiagonalColor() + ";";

		String imgUri = diagonalCellImageMap.get(componentPropertyId);

		if (imgUri == null) {

			// prepare width and height of diagonal image
			DimensionType cellWidth = cell.getWidth();
			if (cellWidth == null || cellWidth.getMeasure() == 0.0d) {
				cellWidth = new DimensionType(DEFAULT_IMAGE_PX_WIDTH, "px");
			}
			if (cellHeight == null || cellHeight.getMeasure() == 0.0d) {
				cellHeight = new DimensionType(DEFAULT_IMAGE_PX_HEIGHT, "px");
			}

			// prepare to get the diagonal line image.
			DiagonalLineImage imageCreater = new DiagonalLineImage();
			imageCreater.setDiagonalLine(cell.getDiagonalNumber(), cell.getDiagonalStyle(), cell.getDiagonalWidth(),
					cell.getDiagonalColor());
			imageCreater.setAntidiagonalLine(cell.getAntidiagonalNumber(), cell.getAntidiagonalStyle(),
					cell.getAntidiagonalWidth(), cell.getAntidiagonalColor());
			imageCreater.setImageDpi(imageDpi);
			imageCreater.setImageSize(getCellWidth(cell), cellHeight);
			IStyle cellComputedStyle = cell.getComputedStyle();
			String strColor = cellComputedStyle.getColor();
			imageCreater.setColor(PropertyUtil.getColor(strColor));
			byte[] imageByteArray = null;
			try {
				// draw the diagonal & antidiagonal line image.
				imageByteArray = imageCreater.drawImage();
			} catch (IOException e) {
				logger.log(Level.WARNING, e.getMessage(), e);
			}
			if (imageByteArray != null) {
				// get the image URI.
				Image image = new Image(imageByteArray, cell.getInstanceID().toUniqueString(), ".png");
				image.setReportRunnable(runnable);
				image.setRenderOption(renderOption);
				imgUri = imageHandler.onCustomImage(image, reportContext);
				if (imgUri != null) {
					// Cache the image URI.
					diagonalCellImageMap.put(componentPropertyId, imgUri);
				}
			}
		}
		return imgUri;
	}

	protected DimensionType getCellWidth(ICellContent cell) {
		IColumn column = cell.getColumnInstance();
		if (null != column) {
			return column.getWidth();
		}
		return null;
	}

	protected DimensionType getCellHeight(ICellContent cell) {
		IElement row = cell.getParent();
		if (row instanceof IRowContent) {
			return ((IRowContent) row).getHeight();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.emitter.IContentEmitter#endCell(org.eclipse.
	 * birt.report.engine.content.ICellContent)
	 */
	@Override
	public void endCell(ICellContent cell) {
		logger.log(Level.FINEST, "[HTMLReportEmitter] End cell."); //$NON-NLS-1$

		if (enableMetadata) {
			metadataEmitter.endCell(cell);
		}

		if (useFixedCellHeight(cell)) {
			writer.closeTag(HTMLTags.TAG_DIV);
			writer.closeTag(HTMLTags.TAG_DIV);
		}

		if (isCellInHead(cell)) {
			writer.closeTag(HTMLTags.TAG_TH);
		} else {
			writer.closeTag(HTMLTags.TAG_TD);
		}

		tableLayout.endCell(cell);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.emitter.IContentEmitter#startContainer(org.
	 * eclipse.birt.report.engine.content.IContainerContent)
	 */
	// FIXME: code review: only the list element using the startContainer. So
	// rename this method to startList.
	@Override
	public void startContainer(IContainerContent container) {
		logger.log(Level.FINEST, "[HTMLReportEmitter] Start container"); //$NON-NLS-1$

		htmlEmitter.openContainerTag(container);

		// output class attribute.
		String styleClass = container.getStyleClass();
		setStyleName(styleClass, container);

		boolean bookmarkOutput = false;
		if (metadataFilter != null) {
			bookmarkOutput = metadataEmitter.outputMetadataProperty(
					metadataFilter.needMetaData(HTMLEmitterUtil.getElementHandle(container)), container,
					HTMLTags.TAG_DIV);
		}
		if (!bookmarkOutput) {
			// bookmark
			String bookmark = container.getBookmark();

			if (bookmark == null) {
				bookmark = idGenerator.generateUniqueID();
				container.setBookmark(bookmark);
			}
			outputBookmark(container, HTMLTags.TAG_DIV);
		}

		StringBuffer styleBuffer = new StringBuffer();
		htmlEmitter.buildContainerStyle(container, styleBuffer);
		writer.attribute(HTMLTags.ATTR_STYLE, styleBuffer.toString());
		htmlEmitter.handleContainerAlign(container);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#endContainer(org.
	 * eclipse.birt.report.engine.content.IContainerContent)
	 */
	@Override
	public void endContainer(IContainerContent container) {
		htmlEmitter.closeContainerTag();

		logger.log(Level.FINEST, "[HTMLContainerEmitter] End container"); //$NON-NLS-1$
	}

	// FIXME: code review: text and foreign need a code review. Including how to
	// implement the vertical, where the properties should be outputted at, the
	// metadata shouldn't open a new tag, and so on.
	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.emitter.IContentEmitter#startText(org.eclipse.
	 * birt.report.engine.content.ITextContent)
	 */
	@Override
	public void startText(ITextContent text) {
		IStyle mergedStyle = text.getStyle();

		logger.log(Level.FINEST, "[HTMLReportEmitter] Start text"); //$NON-NLS-1$

		DimensionType x = text.getX();
		DimensionType y = text.getY();
		DimensionType width = text.getWidth();
		DimensionType height = text.getHeight();
		String textValue = text.getText();
		boolean isBlank = false;
		if (textValue == null || "".equals(textValue)) //$NON-NLS-1$
		{
			textValue = " "; //$NON-NLS-1$
			isBlank = true;
		}
		int display = htmlEmitter.getTextElementType(x, y, width, height, mergedStyle);
		// bidi_hcg: fix for bug 307327. If text content is Bidi, treat it as
		// a inline-block element
		if (display == HTMLEmitterUtil.DISPLAY_INLINE
				&& (text.isDirectionRTL() || Bidi.requiresBidi(textValue.toCharArray(), 0, textValue.length()))) {
			display |= HTMLEmitterUtil.DISPLAY_INLINE_BLOCK;
		}
		// action
		String tagName = openTagByType(display, DISPLAY_FLAG_ALL);

		boolean bookmarkOutput = false;
		if (metadataFilter != null) {
			bookmarkOutput = metadataEmitter.outputMetadataProperty(
					metadataFilter.needMetaData(HTMLEmitterUtil.getElementHandle(text)), text, tagName);
		}

		// output class attribute.
		String styleClass = text.getStyleClass();
		setStyleName(styleClass, text);

		// bookmark
		if (!bookmarkOutput) {
			outputBookmark(text, tagName);
		}

		// title
		writer.attribute(HTMLTags.ATTR_TITLE, text.getHelpText()); // $NON-NLS-1$

		StringBuffer styleBuffer = new StringBuffer();
		htmlEmitter.buildTextStyle(text, styleBuffer, display);
		writer.attribute(HTMLTags.ATTR_STYLE, styleBuffer.toString());

		htmlEmitter.handleTextVerticalAlignBegin(text);

		String url = validate(text.getHyperlinkAction());
		if (url != null && !isBlank) {
			outputAction(text.getHyperlinkAction(), url);
			if (mergedStyle.getProperty(StyleConstants.STYLE_TEXT_HYPERLINK_STYLE) == CSSValueConstants.UNDECORATED) {
				writer.attribute(HTMLTags.ATTR_CLASS, "hyperlink-undecorated");
			}
			String strColor = mergedStyle.getColor();
			if (null != strColor) {
				styleBuffer.setLength(0);
				styleBuffer.append(" color: ");
				styleBuffer.append(strColor);
				styleBuffer.append(";");
				writer.attribute(HTMLTags.ATTR_STYLE, styleBuffer.toString());
			}
			writer.text(textValue);
			writer.closeTag(HTMLTags.TAG_A);
		} else if (isBlank) {
			writer.openTag(HTMLTags.TAG_DIV);
			writer.attribute(HTMLTags.ATTR_STYLE, "visibility:hidden");
			writer.text(textValue);
			writer.closeTag(HTMLTags.TAG_DIV);
		} else {
			writer.text(textValue);
		}
		htmlEmitter.handleVerticalAlignEnd(text);

		writer.closeTag(tagName);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.emitter.IContentEmitter#startForeign(org.
	 * eclipse.birt.report.engine.content.IForeignContent)
	 */
	@Override
	public void startForeign(IForeignContent foreign) {
		IStyle mergedStyle = foreign.getStyle();

		logger.log(Level.FINEST, "[HTMLReportEmitter] Start foreign"); //$NON-NLS-1$

		boolean isTemplate = false;
		boolean wrapTemplateTable = false;
		Object genBy = foreign.getGenerateBy();
		if (genBy instanceof TemplateDesign) {
			isTemplate = true;
			TemplateDesign design = (TemplateDesign) genBy;
			setupTemplateElement(design, foreign);
			// all the template element should be horizontal center of it's
			// parent.
			writer.openTag(HTMLTags.TAG_DIV);
			writer.attribute(HTMLTags.ATTR_ALIGN, "center");

			if (enableMetadata) {
				// template table should be wrapped.
				if ("Table".equals(design.getAllowedType())) {
					wrapTemplateTable = true;
					metadataEmitter.startWrapTable(foreign);
				}
			}
		}

		DimensionType x = foreign.getX();
		DimensionType y = foreign.getY();
		DimensionType width = foreign.getWidth();
		DimensionType height = foreign.getHeight();

		int display;
		display = getElementType(x, y, width, height, mergedStyle);

		// action
		String tagName = openTagByType(display, DISPLAY_FLAG_ALL);

		// append script which changes display of div to table from default
		// when no width is specified for HTML button item
		if (tagName.equalsIgnoreCase(HTMLTags.TAG_DIV) && width == null && foreign.getRawValue() != null) {
			String text = foreign.getRawValue().toString();
			if (text.contains("<button")) {
				int beginIndex = text.indexOf("<button id=\"");
				if (beginIndex != -1) {
					// Add the characters count of 12 for start index of (<button id=")
					beginIndex = beginIndex + 12;
					int endIndex = text.indexOf("\"", beginIndex);
					String buttonID = text.substring(beginIndex, endIndex);
					StringBuilder builder = new StringBuilder(text);

					builder.append("<script type=\"text/javascript\">\n");
					builder.append("var x = document.getElementById(\"");
					builder.append(buttonID);
					builder.append("\").parentNode;\n");
					builder.append("if(x.nodeName.toUpperCase() == \"DIV\"){\n");
					builder.append("if(x.style.width == \"\" || x.style.width == 'undefined'){\n");
					builder.append("x.style.display = \"table\";\n");
					builder.append("}\n");
					builder.append("}\n");
					builder.append("</script>\n");

					foreign.setRawValue(builder.toString());
				}
			}
		}

		boolean bookmarkOutput = false;
		if (metadataFilter != null) {
			bookmarkOutput = metadataEmitter.outputMetadataProperty(
					metadataFilter.needMetaData(HTMLEmitterUtil.getElementHandle(foreign)), foreign, tagName);
		}

		// output class attribute.
		String styleClass = foreign.getStyleClass();
		setStyleName(styleClass, foreign);

		// bookmark
		if (!bookmarkOutput) {
			outputBookmark(foreign, tagName);
		}

		// title
		writer.attribute(HTMLTags.ATTR_TITLE, foreign.getHelpText());

		StringBuffer styleBuffer = new StringBuffer();
		htmlEmitter.buildForeignStyle(foreign, styleBuffer, display);
		HTMLEmitterUtil.buildOverflowStyle(styleBuffer, mergedStyle, true);
		writer.attribute(HTMLTags.ATTR_STYLE, styleBuffer.toString());

		String rawType = foreign.getRawType();
		boolean isHtml = IForeignContent.HTML_TYPE.equalsIgnoreCase(rawType);
		if (isHtml) {
			htmlEmitter.handleTextVerticalAlignBegin(foreign);
			String url = validate(foreign.getHyperlinkAction());
			if (url != null) {
				outputAction(foreign.getHyperlinkAction(), url);
				outputHtmlText(foreign);
				writer.closeTag(HTMLTags.TAG_A);
			} else {
				outputHtmlText(foreign);
			}
			htmlEmitter.handleVerticalAlignEnd(foreign);
		}

		writer.closeTag(tagName);
		if (isTemplate) {
			if (wrapTemplateTable) {
				metadataEmitter.endWrapTable(foreign);
			}
			writer.closeTag(HTMLTags.TAG_DIV);
		}
	}

	protected void outputHtmlText(IForeignContent foreign) {
		boolean bIndent = writer.isIndent();
		writer.setIndent(false);
		Object rawValue = foreign.getRawValue();
		String text = rawValue == null ? null : rawValue.toString();
		ReportDesignHandle design = (ReportDesignHandle) runnable.getDesignHandle();

		if (!foreign.isJTidy()) {
			writer.writeCode(text);
		} else {
			Document doc = new TextParser().parse(text, TextParser.TEXT_TYPE_HTML);
			HTMLProcessor htmlProcessor = new HTMLProcessor(design, reportContext.getAppContext());

			HashMap<?, ?> styleMap = new HashMap<>();

			Element body = null;
			if (doc != null) {
				NodeList bodys = doc.getElementsByTagName("body");
				if (bodys.getLength() > 0) {
					body = (Element) bodys.item(0);
				}
			}
			if (body != null) {
				htmlProcessor.execute(body, styleMap);
				processNodes(body, styleMap);
			}
		}
		writer.setIndent(bIndent);
	}

	/**
	 * Visits the children nodes of the specific node
	 *
	 * @param visitor the ITextNodeVisitor instance
	 * @param ele     the specific node
	 */
	private void processNodes(Element ele, HashMap<?, ?> cssStyles) {
		for (Node node = ele.getFirstChild(); node != null; node = node.getNextSibling()) {
			// At present we only deal with the text, comment and element nodes
			short nodeType = node.getNodeType();
			if (nodeType == Node.TEXT_NODE) {
				if (isScriptText(node)) {
					writer.cdata(node.getNodeValue());
				} else {
					// bug132213 in text item should only deal with the
					// escape special characters: < > &
					// writer.text( node.getNodeValue( ), false, true );
					writer.text(node.getNodeValue(), false);
				}
			} else if (nodeType == Node.COMMENT_NODE) {
				writer.comment(node.getNodeValue());
			} else if (nodeType == Node.ELEMENT_NODE) {
				if ("br".equalsIgnoreCase(node.getNodeName())) {
					// <br/> is correct. <br></br> is not correct. The brower
					// will treat the <br></br> as <br><br>
					boolean bImplicitCloseTag = writer.isImplicitCloseTag();
					writer.setImplicitCloseTag(true);
					startNode(node, cssStyles);
					processNodes((Element) node, cssStyles);
					endNode(node);
					writer.setImplicitCloseTag(bImplicitCloseTag);
				} else {
					startNode(node, cssStyles);
					processNodes((Element) node, cssStyles);
					endNode(node);
				}
			}
		}
	}

	/**
	 * test if the text node is in the script
	 *
	 * @param node text node
	 * @return true if the text is a script, otherwise, false.
	 */
	private boolean isScriptText(Node node) {
		Node parent = node.getParentNode();
		if (parent != null) {
			if (parent.getNodeType() == Node.ELEMENT_NODE) {
				String tag = parent.getNodeName();
				if (HTMLTags.TAG_SCRIPT.equalsIgnoreCase(tag)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @param node
	 * @param cssStyles
	 */
	public void startNode(Node node, HashMap<?, ?> cssStyles) {
		String nodeName = node.getNodeName();
		HashMap<?, ?> cssStyle = (HashMap<?, ?>) cssStyles.get(node);
		writer.openTag(nodeName);
		NamedNodeMap attributes = node.getAttributes();
		if (attributes != null) {
			for (int i = 0; i < attributes.getLength(); i++) {
				Node attribute = attributes.item(i);
				String attrName = attribute.getNodeName();
				String attrValue = attribute.getNodeValue();

				if (attrValue != null) {
					if ("img".equalsIgnoreCase(nodeName) && "src".equalsIgnoreCase(attrName)) {
						BackgroundImageInfo img = handleStyleImage(attrValue);
						String attrValueTrue = null;
						if (img != null) {
							attrValueTrue = img.getUri();
							if (attrValueTrue != null) {
								attrValue = attrValueTrue;
							}
						}
					}
					writer.attribute(attrName, attrValue);
				}
			}
		}
		if (cssStyle != null) {
			StringBuilder buffer = new StringBuilder();
			Iterator<?> ite = cssStyle.entrySet().iterator();
			while (ite.hasNext()) {
				Map.Entry entry = (Map.Entry) ite.next();
				Object keyObj = entry.getKey();
				Object valueObj = entry.getValue();
				if (keyObj == null || valueObj == null) {
					continue;
				}
				String key = keyObj.toString();
				String value = valueObj.toString();
				buffer.append(key);
				buffer.append(":");
				if ("background-image".equalsIgnoreCase(key)) {
					String valueTrue = handleStyleImage(value, true).getUri();
					if (valueTrue != null) {
						value = valueTrue;
					}
					buffer.append("url(");
					buffer.append(value);
					buffer.append(")");
				} else {
					buffer.append(value);
				}
				buffer.append(";");
			}
			if (buffer.length() != 0) {
				writer.attribute("style", buffer.toString());
			}
		}
	}

	/**
	 * Write the end node
	 *
	 * @param node
	 */
	public void endNode(Node node) {
		writer.closeTag(node.getNodeName());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.emitter.IContentEmitter#startLabel(org.eclipse
	 * .birt.report.engine.content.ILabelContent)
	 */
	@Override
	public void startLabel(ILabelContent label) {
		String bookmark = label.getBookmark();
		if (bookmark == null) {
			bookmark = idGenerator.generateUniqueID();
			label.setBookmark(bookmark);
		}
		startText(label);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.emitter.IContentEmitter#startData(org.eclipse.
	 * birt.report.engine.content.IDataContent)
	 */
	@Override
	public void startData(IDataContent data) {
		startText(data);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.emitter.IContentEmitter#startImage(org.eclipse
	 * .birt.report.engine.content.IImageContent)
	 */
	@Override
	public void startImage(IImageContent image) {
		assert image != null;
		IStyle mergedStyle = image.getStyle();

		logger.log(Level.FINEST, "[HTMLImageEmitter] Start image"); //$NON-NLS-1$

		StringBuffer styleBuffer = new StringBuffer();
		int display = checkElementType(image.getX(), image.getY(), mergedStyle, styleBuffer);

		// In HTML the default display value of image is inline. We use the tag
		// <div> to implement the block of the image. For inline elements, we
		// need the <span> tag to make them work well.
		String tag = openTagByType(display, DISPLAY_FLAG_ALL);

		IHyperlinkAction hyperlinkAction = image.getHyperlinkAction();
		String url = validate(hyperlinkAction);
		if (url != null) {
			String strWidth = "width:0;";
			DimensionType w = image.getWidth();
			if (w != null) {
				strWidth = "width:" + w.toString();
			}
			writer.attribute(HTMLTags.ATTR_STYLE, strWidth);
		}
		// action
		boolean hasAction = handleAction(hyperlinkAction, url);
		// if the image has url links, force compact mode to avoid unwanted hyphen.
		boolean compactMode = writer.isEnableCompactMode();
		if (hasAction) {
			writer.setEnableCompactMode(true);
		}

		// Image must have a bookmark.
		if (image.getBookmark() == null) {
			image.setBookmark(idGenerator.generateUniqueID());
		}

		boolean useSVG = ("image/svg+xml".equalsIgnoreCase(image.getMIMEType())) //$NON-NLS-1$
				|| (".svg".equalsIgnoreCase(image.getExtension())) //$NON-NLS-1$
				|| ((image.getURI() != null) && image.getURI().toLowerCase().endsWith(".svg")); //$NON-NLS-1$
		if (useSVG) {
			image.setMIMEType("image/svg+xml"); //$NON-NLS-1$
		}

		boolean useSWT = "application/x-shockwave-flash".equalsIgnoreCase(image.getMIMEType()); //$NON-NLS-1$

		if (useSVG || useSWT) { // use svg
			outputSVGImage(image, styleBuffer, display);
		} else { // use img

			// write image map if necessary
			Object imageMapObject = image.getImageMap();
			String imageMapId = null;
			boolean hasImageMap = (imageMapObject != null) && (imageMapObject instanceof String)
					&& (((String) imageMapObject).length() > 0);
			if (hasImageMap) {
				String id = idGenerator.generateUniqueID();
				imageMapId = htmlIDNamespace != null ? htmlIDNamespace + id : id;
				writer.openTag(HTMLTags.TAG_MAP);
				writer.attribute(HTMLTags.ATTR_ID, imageMapId);
				writer.attribute(HTMLTags.ATTR_NAME, imageMapId);
				writer.attribute(HTMLTags.ATTR_STYLE, "display:none");//$NON-NLS-1$
				writer.cdata((String) imageMapObject);
				writer.closeTag(HTMLTags.TAG_MAP);
			}

			writer.openTag(HTMLTags.TAG_IMAGE); // $NON-NLS-1$

			outputImageStyleClassBookmark(image, HTMLTags.TAG_IMAGE);

			String ext = image.getExtension();
			String imgUri = getImageURI(image);
			writer.attribute(HTMLTags.ATTR_SRC, imgUri);

			if (hasImageMap) {
				// BUGZILLA 119245 request chart (without hyperlink) can't have
				// borders, the BROWSER add blue-solid border to the image with
				// maps.
				if (!hasAction) {
					resetImageDefaultBorders(image, styleBuffer);
				}
				writer.attribute(HTMLTags.ATTR_USEMAP, "#" + imageMapId); //$NON-NLS-1$
			}

			// alternative text
			String altText = image.getAltText();
			if (altText == null) {
//				#BIRT-3336 : Single Metric: Dont provide alt attribute to the img element in single metric
				if (!browserSupportsBrokenImageIcon) {
					writer.attributeAllowEmpty(HTMLTags.ATTR_ALT, "");
				}
			} else {
				writer.attribute(HTMLTags.ATTR_ALT, altText);
			}

			// help text
			String titleText = image.getHelpText();
			if (titleText == null) {
				if (hasAction) {
					titleText = hyperlinkAction.getTooltip();
				}
			}
			writer.attribute(HTMLTags.ATTR_TITLE, titleText);

			// build style
			htmlEmitter.buildImageStyle(image, styleBuffer, display);
			writer.attribute(HTMLTags.ATTR_STYLE, styleBuffer.toString());

			if (".PNG".equalsIgnoreCase(ext) && imageHandler != null) //$NON-NLS-1$
			{
				if (needFixTransparentPNG) {
					if (null == htmlIDNamespace) {
						writer.attribute(HTMLTags.ATTR_ONLOAD, "fixPNG(this)"); //$NON-NLS-1$
					} else {
						writer.attribute(HTMLTags.ATTR_ONLOAD, htmlIDNamespace + "fixPNG(this)"); //$NON-NLS-1$
					}
				}
			}

			writer.closeTag(HTMLTags.TAG_IMAGE);
		}

		if (hasAction) {
			writer.closeTag(HTMLTags.TAG_A);
		}

		if (tag != null) {
			writer.closeTag(tag);
		}
		// restore compact mode
		writer.setEnableCompactMode(compactMode);
	}

	/**
	 * output image's part of metadata, style class and bookmark.
	 *
	 * @param image
	 * @param tag
	 */
	protected void outputImageStyleClassBookmark(IImageContent image, String tag) {
		boolean bookmarkOutput = false;
		if (metadataFilter != null) {
			bookmarkOutput = metadataEmitter.outputMetadataProperty(
					metadataFilter.needMetaData(HTMLEmitterUtil.getElementHandle(image)), image, tag);
		}

		// output class attribute.
		String styleClass = image.getStyleClass();
		setStyleName(styleClass, image);

		if (!bookmarkOutput) {
			outputBookmark(image, HTMLTags.ATTR_IMAGE); // $NON-NLS-1$
		}
	}

	/**
	 * output the svg image
	 *
	 * @param image
	 * @param styleBuffer
	 * @param display
	 */
	protected void outputSVGImage(IImageContent image, StringBuffer styleBuffer, int display) {
		writer.openTag(HTMLTags.TAG_EMBED);

		outputImageStyleClassBookmark(image, HTMLTags.TAG_EMBED);

		// onresize gives the SVG a change to change its content
		String htmlBookmark;
		if (null != htmlIDNamespace) {
			htmlBookmark = htmlIDNamespace + image.getBookmark();
		} else {
			htmlBookmark = image.getBookmark();
		}
		writer.attribute("onresize", "document.getElementById('" + htmlBookmark + "').reload()"); //$NON-NLS-1$

		writer.attribute(HTMLTags.ATTR_TYPE, image.getMIMEType());
		writer.attribute(HTMLTags.ATTR_SRC, getImageURI(image));

		// alternative text
		String altText = image.getAltText();
		if (altText == null) {
			writer.attributeAllowEmpty(HTMLTags.ATTR_ALT, "");
		} else {
			writer.attribute(HTMLTags.ATTR_ALT, altText);
		}

		if (enableMetadata) {
			writer.attribute("wmode", "transparent");
		}

		// build style
		htmlEmitter.buildImageStyle(image, styleBuffer, display);

		// hyperlink: avoid forwarded events of the embed-tag
		if (image.getHyperlinkAction() != null) {
			styleBuffer.append(HTMLTags.ATTR_POINTER_EVENTS + ":none;");
		}
		writer.attribute(HTMLTags.ATTR_STYLE, styleBuffer.toString());
		writer.closeTag(HTMLTags.TAG_EMBED);
	}

	/**
	 *
	 * @param image
	 * @param styleBuffer
	 */
	protected void resetImageDefaultBorders(IImageContent image, StringBuffer styleBuffer) {
		// disable the border, if the user defines border with the
		// image, it will be overided by the following style setting
		IStyle style = image.getStyle();
		if (style.getBorderTopStyle() == null) {
			// user doesn't define the border, remove it.
			styleBuffer.append("border-top-style:none;");
		} else // use define the border-style, but not define the
		// border color, use the default
		// color.
		if (style.getBorderTopColor() == null) {
			styleBuffer.append("border-top-color:black;");
		}
		if (style.getBorderBottomStyle() == null) {
			styleBuffer.append("border-bottom-style:none;");
		} else if (style.getBorderBottomColor() == null) {
			styleBuffer.append("border-bottom-color:black;");
		}
		if (style.getBorderLeftStyle() == null) {
			styleBuffer.append("border-left-style:none;");
		} else if (style.getBorderLeftColor() == null) {
			styleBuffer.append("border-left-color:black;");
		}
		if (style.getBorderRightStyle() == null) {
			styleBuffer.append("border-right-style:none;");
		} else if (style.getBorderRightColor() == null) {
			styleBuffer.append("border-right-color:black;");
		}
	}

	/**
	 * Get the image URI
	 *
	 * @param image the image content
	 * @return Return the image URI
	 */
	protected String getImageURI(IImageContent image) {
		String imgUri = null;
		String tmpImgUri = null;
		if (imageHandler != null) {
			imgUri = image.getURI();

			// embedded images w/o URI check
			if (image.getImageSource() != IImageContent.IMAGE_NAME) {
				tmpImgUri = this.verifyURI(imgUri);
				if (imgUri != tmpImgUri) {
					imgUri = tmpImgUri;
					image.setURI(tmpImgUri);
				}
			}

			// image URI with http/https
			if (image.getImageSource() == IImageContent.IMAGE_URL && !imgUri.contains(URL_PROTOCOL_TYPE_FILE)) {

				try {
					// fetch the raw image size
					URL url = new URL(imgUri);
					BufferedImage bImg = ImageIO.read(url);
					image.setImageRawSize(new ImageSize("px", bImg.getWidth(), bImg.getHeight()));
				} catch (Exception ex) {
					image.setImageRawSize(new ImageSize("px", DEFAULT_IMAGE_PX_WIDTH, DEFAULT_IMAGE_PX_HEIGHT));
				}
				return imgUri;
			}

			Image img = new Image(image);
			img.setRenderOption(renderOption);
			img.setReportRunnable(runnable);
			switch (img.getSource()) {
			case IImage.DESIGN_IMAGE:
				imgUri = imageHandler.onDesignImage(img, reportContext);
				break;
			case IImage.URL_IMAGE:
				imgUri = imageHandler.onURLImage(img, reportContext);
				break;
			case IImage.REPORTDOC_IMAGE:
				imgUri = imageHandler.onDocImage(img, reportContext);
				break;
			case IImage.CUSTOM_IMAGE:
				imgUri = imageHandler.onCustomImage(img, reportContext);
				break;
			case IImage.FILE_IMAGE:
				imgUri = imageHandler.onFileImage(img, reportContext);
				break;
			case IImage.INVALID_IMAGE:
				break;
			}
			image.setImageRawSize(img.getImageRawSize());
		}
		return imgUri;
	}

	/**
	 * Sets the <code>'class'</code> property and stores the style to styleMap
	 * object.
	 *
	 * @param styleName the style name
	 */
	protected void setStyleName(String styleName, IContent content) {
		StringBuilder classBuffer = new StringBuilder();

		if (enableMetadata) {
			String metadataStyleClass = metadataEmitter.getMetadataStyleClass(content);
			if (null != metadataStyleClass) {
				classBuffer.append(metadataStyleClass);
			}
		}

		if (!enableInlineStyle && styleName != null && styleName.length() > 0) {
			if (outputtedStyles.contains(styleName)) {
				if (classBuffer.length() != 0) {
					classBuffer.append(" ");
				}
				if (null != htmlIDNamespace) {
					classBuffer.append(htmlIDNamespace);
				}
				classBuffer.append(styleName);
			}
		}

		if (hasCsslinks) {
			Object genBy = content.getGenerateBy();
			if (genBy instanceof StyledElementDesign) {
				DesignElementHandle handle = ((StyledElementDesign) genBy).getHandle();
				if (handle != null) {
					String name = handle.getStringProperty(IStyledElementModel.STYLE_PROP);
					if (name != null) {
						if (classBuffer.length() != 0) {
							classBuffer.append(" " + name);
						} else {
							classBuffer.append(name);
						}
					}
				}
			}
		}

		if (classBuffer.length() != 0) {
			writer.attribute(HTMLTags.ATTR_CLASS, classBuffer.toString());
		}
	}

	protected void outputBookmark(IContent content, String tagName) {
		String bookmark = content.getBookmark();
		outputBookmark(writer, tagName, htmlIDNamespace, bookmark);
	}

	/**
	 * Set bookmark to output
	 *
	 * @param writer
	 * @param tagName
	 * @param htmlIDNamespace
	 * @param bookmark
	 */
	public void outputBookmark(HTMLWriter writer, String tagName, String htmlIDNamespace, String bookmark) {
		HTMLEmitterUtil.setBookmark(writer, tagName, htmlIDNamespace, bookmark);
	}

	/**
	 * Checks whether the element is block, inline or inline-block level. In BIRT,
	 * the absolute positioning model is used and a box is explicitly offset with
	 * respect to its containing block. When an element's x or y is set, it will be
	 * treated as a block level element regardless of the 'Display' property set in
	 * style. When designating width or height value to an inline element, it will
	 * be treated as inline-block.
	 *
	 * @param x           Specifies how far a box's left margin edge is offset to
	 *                    the right of the left edge of the box's containing block.
	 * @param y           Specifies how far an absolutely positioned box's top
	 *                    margin edge is offset below the top edge of the box's
	 *                    containing block.
	 * @param style       The <code>IStyle</code> object.
	 * @param styleBuffer The <code>StringBuffer</code> object that returns 'style'
	 *                    content.
	 * @return The display type of the element.
	 */
	protected int checkElementType(DimensionType x, DimensionType y, IStyle style, StringBuffer styleBuffer) {
		return checkElementType(x, y, null, null, style, styleBuffer);
	}

	protected int checkElementType(DimensionType x, DimensionType y, DimensionType width, DimensionType height,
			IStyle style, StringBuffer styleBuffer) {
		return getElementType(x, y, width, height, style);
	}

	/**
	 * Get element by type
	 *
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param style
	 * @return Return the HTML element
	 */
	public int getElementType(DimensionType x, DimensionType y, DimensionType width, DimensionType height,
			IStyle style) {
		return htmlEmitter.getElementType(x, y, width, height, style);
	}

	/**
	 * Open a tag according to the display type of the element. Here is the mapping
	 * table:
	 * <p>
	 * <table border=0 cellspacing=3 cellpadding=0 summary="Chart showing symbol,
	 * location, localized, and meaning.">
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
	 * @param display The display type.
	 * @param mask    The mask value.
	 * @return Tag name.
	 */
	protected String openTagByType(int display, int mask) {
		String tag = HTMLEmitterUtil.getTagByType(display, mask);
		if (tag != null) {
			writer.openTag(tag);
		}
		return tag;
	}

	/**
	 * Checks the Action object and then output corresponding tag and property.
	 *
	 * @param action The <code>IHyperlinkAction</code> object.
	 * @return A <code>boolean</code> value indicating whether the Action object is
	 *         valid or not.
	 */
	protected boolean handleAction(IHyperlinkAction action) {
		String url = validate(action);
		return handleAction(action, url);
	}

	/**
	 * Verify handle action
	 *
	 * @param action
	 * @param url
	 * @return true, handle is valid
	 */
	protected boolean handleAction(IHyperlinkAction action, String url) {
		if (url != null) {
			outputAction(action, url);
		}
		return url != null;
	}

	/**
	 * Outputs an hyperlink action.
	 *
	 * @param action the hyperlink action.
	 */
	protected void outputAction(IHyperlinkAction action, String url) {
		writer.openTag(HTMLTags.TAG_A);
		writer.attribute(HTMLTags.ATTR_HREF, url);
		writer.attribute(HTMLTags.ATTR_TARGET, action.getTargetWindow());
		writer.attribute(HTMLTags.ATTR_TITLE, action.getTooltip());
	}

	/**
	 * Judges if a hyperlink is valid.
	 *
	 * @param action the hyperlink action
	 * @return true, the hyperlink is valid
	 */
	protected String validate(IHyperlinkAction action) {
		if (action == null) {
			return null;
		}
		String systemId = runnable == null ? null : runnable.getReportName();
		Action act = new Action(systemId, action);

		if (actionHandler == null) {
			return null;
		}

		String link = actionHandler.getURL(act, reportContext);
		if (link != null && !link.equals(""))//$NON-NLS-1$
		{
			return link;
		}
		return null;
	}

	/**
	 * Handle style image
	 *
	 * @param uri uri in style image
	 * @return Return the image URI
	 */
	public BackgroundImageInfo handleStyleImage(String uri) {
		return handleStyleImage(uri, false, null);
	}

	/**
	 * Handle style image
	 *
	 * @param uri          uri in style image
	 * @param isBackground Is this image a used for a background?
	 * @return Return the image URI
	 */
	public BackgroundImageInfo handleStyleImage(String uri, boolean isBackground) {
		return handleStyleImage(uri, isBackground, null);
	}

	/**
	 * Handle style image
	 *
	 * @param style        Is the style object of the image include uri.
	 * @param isBackground Is this image a used for a background?
	 * @return Return the image URI
	 */
	public BackgroundImageInfo handleStyleImage(IStyle style, boolean isBackground) {
		return handleStyleImage(null, isBackground, style);
	}

	// FIXME: code review: this function needs be handled in the ENGINE( after
	// render , in the localize)? We should calculate the imgUri in the engine
	// part and put the imgUri into the image style. Then we can use the imagUri
	// directly here
	/**
	 * Handle the style of image
	 *
	 * @param uri          URI in style image
	 * @param isBackground Is this image a used for a background?
	 * @param imageStyle   Style of the image
	 * @return Return the image URI
	 */
	public BackgroundImageInfo handleStyleImage(String uri, boolean isBackground, IStyle imageStyle) {

		ReportDesignHandle design = (ReportDesignHandle) runnable.getDesignHandle();
		URL url = design.findResource(uri, IResourceLocator.IMAGE, reportContext.getAppContext());
		String fileExtension = null;
		Module module = design.getModule();
		ResourceLocatorWrapper rl = null;
		ExecutionContext exeContext = ((ReportContent) this.report).getExecutionContext();
		if (exeContext != null) {
			rl = exeContext.getResourceLocator();
		}
		BackgroundImageInfo backgroundImage = new BackgroundImageInfo("", null, 0, 0, 0, 0, rl, module);

		if (isBackground && imageStyle != null) {
			String uriString = EmitterUtil.getBackgroundImageUrl(imageStyle, design,
					this.report.getReportContext() == null ? null : this.report.getReportContext().getAppContext());

			backgroundImage = new BackgroundImageInfo(uriString,
					imageStyle.getProperty(StyleConstants.STYLE_BACKGROUND_REPEAT),
					PropertyUtil.getDimensionValue(imageStyle.getProperty(StyleConstants.STYLE_BACKGROUND_POSITION_X)),
					PropertyUtil.getDimensionValue(imageStyle.getProperty(StyleConstants.STYLE_BACKGROUND_POSITION_Y)),
					0, 0, rl, module, imageStyle.getProperty(StyleConstants.STYLE_BACKGROUND_IMAGE_TYPE));
			backgroundImage.setImageSize(imageStyle);

			if (backgroundImage.getSourceType().equalsIgnoreCase(CSSConstants.CSS_EMBED_VALUE)) {
				uri = backgroundImage.getDataUrl();
			}
			fileExtension = backgroundImage.getFileExtension();
		}

		if (fileExtension == null && uri != null && uri.contains(".")) {
			fileExtension = uri.substring(uri.lastIndexOf(".") + 1);
		}
		if (url == null) {
			return backgroundImage;
		}
		uri = url.toExternalForm();
		Image image = null;
		if (isBackground) {
			try {
				byte[] buffer = backgroundImage.getImageData();
				image = new Image(buffer, uri, "." + backgroundImage.getFileExtension());
				image.setMimeType(backgroundImage.getMimeType());
			} catch (Exception e) {
				image = new Image(uri);
			}
		} else {
			image = new Image(uri);
		}
		image.setReportRunnable(runnable);
		image.setRenderOption(renderOption);
		if (image.getMimeType() == null && fileExtension != null) {
			image.setMimeType("image/" + fileExtension);
		}

		String imgUri = null;
		if (imageHandler != null) {
			switch (image.getSource()) {

			case IImage.URL_IMAGE:
				imgUri = imageHandler.onURLImage(image, reportContext);
				break;

			case IImage.FILE_IMAGE:
				imgUri = imageHandler.onFileImage(image, reportContext);
				break;

			case IImage.CUSTOM_IMAGE:
				imgUri = imageHandler.onCustomImage(image, reportContext);
				break;

			case IImage.INVALID_IMAGE:
				break;

			default:
				assert (false);
			}
			if (backgroundImage != null) {
				backgroundImage.setUri(imgUri);
			}
		}
		return backgroundImage;
	}

	/**
	 * setup chart template and table template element for output.
	 *
	 * <li>1. set the bookmark if there is no bookmark.</li>
	 * <li>2. chage the styles of the element.</li>
	 *
	 * @param template the design used to create the contnet.
	 * @param content  the styled element content
	 */
	protected void setupTemplateElement(TemplateDesign template, IContent content) {
		// set up the bookmark if there is no bookmark for the template
		String bookmark = content.getBookmark();
		if (bookmark == null) {
			bookmark = idGenerator.generateUniqueID();
			content.setBookmark(bookmark);
		}

		// setup the styles of the template
		String allowedType = template.getAllowedType();
		if ("ExtendedItem".equals(allowedType)) {
			// Resize chart template element
			IStyle style = content.getStyle();
			style.setProperty(StyleConstants.STYLE_CAN_SHRINK, BIRTValueConstants.FALSE_VALUE);
			content.setWidth(new DimensionType(3, DimensionType.UNITS_IN));
			content.setHeight(new DimensionType(3, DimensionType.UNITS_IN));
		} else if ("Table".equals(allowedType)) {
			// Resize table template element
			IStyle style = content.getStyle();
			style.setProperty(StyleConstants.STYLE_CAN_SHRINK, BIRTValueConstants.FALSE_VALUE);
			content.setWidth(new DimensionType(5, DimensionType.UNITS_IN));
			// set lines to dotted lines
			style.setProperty(StyleConstants.STYLE_BORDER_TOP_STYLE, CSSValueConstants.DOTTED_VALUE);
			style.setProperty(StyleConstants.STYLE_BORDER_BOTTOM_STYLE, CSSValueConstants.DOTTED_VALUE);
			style.setProperty(StyleConstants.STYLE_BORDER_LEFT_STYLE, CSSValueConstants.DOTTED_VALUE);
			style.setProperty(StyleConstants.STYLE_BORDER_RIGHT_STYLE, CSSValueConstants.DOTTED_VALUE);
			style.setProperty(StyleConstants.STYLE_FONT_FAMILY, CSSValueConstants.SANS_SERIF_VALUE);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter#endGroup(org.
	 * eclipse.birt.report.engine.content.IGroupContent)
	 */
	@Override
	public void endGroup(IGroupContent group) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter#endListBand(org.
	 * eclipse.birt.report.engine.content.IListBandContent)
	 */
	@Override
	public void endListBand(IListBandContent listBand) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter#endListGroup(org
	 * .eclipse.birt.report.engine.content.IListGroupContent)
	 */
	@Override
	public void endListGroup(IListGroupContent group) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter#endTableBand(org
	 * .eclipse.birt.report.engine.content.ITableBandContent)
	 */
	@Override
	public void endTableBand(ITableBandContent band) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter#endTableGroup(
	 * org.eclipse.birt.report.engine.content.ITableGroupContent)
	 */
	@Override
	public void endTableGroup(ITableGroupContent group) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter#startGroup(org.
	 * eclipse.birt.report.engine.content.IGroupContent)
	 */
	@Override
	public void startGroup(IGroupContent group) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter#startListBand(
	 * org.eclipse.birt.report.engine.content.IListBandContent)
	 */
	@Override
	public void startListBand(IListBandContent listBand) {
	}

	/**
	 * used to control the output of group bookmarks.
	 *
	 * @see {@link #startTableGroup(ITableGroupContent)}
	 * @see {@link #startListGroup(IListGroupContent)}
	 */
	@SuppressWarnings("javadoc")
	protected Stack<ITableGroupContent> startedGroups = new Stack<ITableGroupContent>();

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter#startListGroup(
	 * org.eclipse.birt.report.engine.content.IListGroupContent)
	 */
	@Override
	public void startListGroup(IListGroupContent group) {
		outputBookmark(group);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter#startTableBand(
	 * org.eclipse.birt.report.engine.content.ITableBandContent)
	 */
	@Override
	public void startTableBand(ITableBandContent band) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter#startTableGroup(
	 * org.eclipse.birt.report.engine.content.ITableGroupContent)
	 */
	@Override
	public void startTableGroup(ITableGroupContent group) {
		startedGroups.push(group);
	}

	protected void outputBookmark(IGroupContent group) {
		String bookmark = group.getBookmark();
		if (bookmark == null) {
			bookmark = idGenerator.generateUniqueID();
			group.setBookmark(bookmark);
		}
		writer.openTag(HTMLTags.TAG_SPAN);
		outputBookmark(group, null);
		writer.closeTag(HTMLTags.TAG_SPAN);
	}

	protected void writeBidiFlag() {
		// bidi_hcg start
		// RTL attribute is required at HTML or BODY level for the correct
		// scroll bar position.
		if (htmlRtLFlag) {
			writer.attribute(HTMLTags.ATTR_HTML_DIR, CSSConstants.CSS_RTL_VALUE);
		}
		// bidi_hcg end
	}

	/**
	 * Figures out the RTL rendering option.
	 *
	 * @author bidi_hcg
	 */
	protected void retrieveRtLFlag() {
		// If htmlOption has RTL_FLAG option set (likely adopted from an URL
		// parameter), honor this option, otherwise obtain direction from
		// the report design.
		HTMLRenderOption htmlOption = new HTMLRenderOption(renderOption);
		Object bidiFlag = htmlOption.getOption(IRenderOption.RTL_FLAG);
		if (Boolean.TRUE.equals(bidiFlag)) {
			htmlRtLFlag = true;
		} else if (bidiFlag == null && report != null) {
			ReportDesignHandle handle = report.getDesign().getReportDesign();
			if (handle != null) {
				htmlRtLFlag = handle.isDirectionRTL();
				htmlOption.setHtmlRtLFlag(htmlRtLFlag); // not necessary though
			}
		}
	}

	/**
	 * Check the URL to be valid and fall back try it like file-URL
	 */
	private String verifyURI(String uri) {
		if (uri != null && !uri.toLowerCase().startsWith(URL_PROTOCOL_TYPE_DATA)) {
			String tmpUrl = uri.replaceAll(" ", URL_PROTOCOL_URL_ENCODED_SPACE);
			try {
				new URL(tmpUrl).toURI();
			} catch (MalformedURLException | URISyntaxException excUrl) {
				// invalid URI try it like "file:"
				try {
					tmpUrl = URL_PROTOCOL_TYPE_FILE + "///" + uri;
					new URL(tmpUrl).toURI();
					uri = tmpUrl;
				} catch (MalformedURLException | URISyntaxException excFile) {
				}
			}
		}
		return uri;
	}
}

class IDGenerator {
	protected int bookmarkId = 0;

	IDGenerator() {
		this.bookmarkId = 0;
	}

	protected String generateUniqueID() {
		bookmarkId++;
		// Ted issue 37427 Proj 1336: in IV failed to drill-down from the second level
		// in a chart in Safari browser
		return "AUTOGENBOOKMARK_" + bookmarkId + "_" + java.util.UUID.randomUUID().toString();
	}
}

class TableLayout {

	/**
	 * status of the current table.
	 */
	private class LayoutStatus {

		/**
		 * current table
		 */
		ITableContent table;
		/**
		 * row id of the cells, used to calculate the row span.
		 */
		int[] cells;
		/**
		 * column count of the table.
		 */
		int columnCount;
		/**
		 * if the column has invisible columns
		 */
		boolean hasHiddenColumn;
		/**
		 * which column is invisible.
		 */
		boolean[] columnHiddens;
		/**
		 * previous cell content.
		 */
		ICellContent currentCell;
	}

	/**
	 * stack used save status of nested table.
	 */
	private Stack<LayoutStatus> statuses = new Stack<>();
	/**
	 * current table content
	 */
	private ITableContent table;
	/**
	 * row position of current tables.
	 */
	private int[] cells;
	/**
	 * column count of current table.
	 */
	private int columnCount;
	/**
	 * if the table has invisible columns.
	 */
	private boolean hasHiddenColumn;
	/**
	 * boolean value to indicate which column is invisible.
	 */
	private boolean[] columnHiddens;
	/**
	 * previous cell content.
	 */
	private ICellContent currentCell;

	/**
	 * HTML emitter to output content.
	 */
	private HTMLReportEmitter emitter = null;
	/**
	 * the cell inserted as invisible.
	 */
	ICellContent insertNoneCell = null;

	TableLayout(HTMLReportEmitter emitter) {
		this.emitter = emitter;
	}

	protected void startTable(ITableContent tableContent) {
		LayoutStatus status = new LayoutStatus();
		status.table = table;
		status.cells = cells;
		status.columnCount = columnCount;
		status.columnHiddens = columnHiddens;
		status.hasHiddenColumn = hasHiddenColumn;
		status.currentCell = currentCell;
		statuses.push(status);

		// create a new status
		table = tableContent;
		columnCount = tableContent.getColumnCount();
		cells = new int[columnCount];
		columnHiddens = new boolean[columnCount];
		for (int i = 0; i < columnCount; i++) {
			IColumn column = tableContent.getColumn(i);
			boolean isHidden = column.getComputedStyle().getProperty(StyleConstants.STYLE_DISPLAY) == CSSValueConstants.NONE_VALUE;
			columnHiddens[i] = isHidden;
			if (isHidden) {
				hasHiddenColumn = true;
			}
		}
	}

	protected void endTable(ITableContent tableContent) {
		if (!statuses.isEmpty()) {
			LayoutStatus status = statuses.pop();
			table = status.table;
			cells = status.cells;
			columnCount = status.columnCount;
			hasHiddenColumn = status.hasHiddenColumn;
			columnHiddens = status.columnHiddens;
			currentCell = status.currentCell;
		}
	}

	protected void startRow() {

	}

	protected void endRow() {
		addEmptyCell();
		currentCell = null;
		for (int i = 0; i < columnCount; i++) {
			cells[i]--;
		}
	}

	protected void startCell(ICellContent cell) {
		// the cell is output as invisible, so output directly.
		if (cell == insertNoneCell) {
			insertNoneCell = null;
			return;
		}
		// if there are any cell missing before the cell,
		// fill the gap with empty cell.
		if (needAddEmptyCell(cell)) {
			addEmptyCell(cell);
		}

		if (!hasHiddenColumn) {
			return;
		}

		int column = cell.getColumn();
		int colSpan = cell.getColSpan();
		int hiddenColumnCount = 0;
		for (int i = 0; i < colSpan; i++) {
			if (columnHiddens[column + i]) {
				hiddenColumnCount++;
			}
		}
		if (hiddenColumnCount != 0) {
			if (colSpan == hiddenColumnCount) {
				// the whole element are hidden, change the display to none.
				cell.getStyle().setProperty(StyleConstants.STYLE_DISPLAY, CSSValueConstants.NONE_VALUE);
			} else {
				// fill hidden cells for invisible columns
				addNoneCell(column, column + hiddenColumnCount, cell);
				// change the merge cell.
				cell.setColumn(column + hiddenColumnCount);
				cell.setColSpan(colSpan - hiddenColumnCount);
				// as we don't define tablecell display constant, so use block
				// here. The value itself won't used by output code.
				cell.getStyle().setProperty(StyleConstants.STYLE_DISPLAY, CSSValueConstants.BLOCK_VALUE);
			}
		}
	}

	protected void endCell(ICellContent cell) {
		currentCell = cell;
		for (int i = cell.getColumn(); i < cell.getColumn() + cell.getColSpan(); i++) {
			cells[i] += cell.getRowSpan();
		}
	}

	protected boolean isInvisibaleRow() {
		for (int i = 0; i < columnCount; i++) {
			if (cells[i] != 0) {
				return false;
			}
			return true;
		}
		return false;
	}

	protected void addEmptyCell() {
		if (isInvisibaleRow()) {
			for (int i = 0; i < columnCount; i++) {
				cells[i]++;
			}
			return;
		}
		for (int i = 0; i < columnCount; i++) {
			if (cells[i] == 0) {
				ICellContent newCell = null;
				if (currentCell != null) {
					newCell = newCell(currentCell, i, i + 1);
				} else if (table != null) {
					newCell = newCell(table.getReportContent().createCellContent(), i, i + 1);
				}
				if (newCell != null) {
					emitter.startCell(newCell);
					emitter.endCell(newCell);
				}
			}
		}
	}

	protected boolean needAddEmptyCell(ICellContent cell) {
		if (cell.getColumn() > 0) {
			if (cells[cell.getColumn() - 1] == 0) {
				return true;
			}
		}
		return false;
	}

	protected ICellContent newCell(ICellContent cell, int startCol, int endCol) {
		CellContentWrapper tempCell = new CellContentWrapper(cell);
		tempCell.setRowSpan(cell.getRowSpan());
		tempCell.setColumn(startCol);
		tempCell.setColSpan(endCol - startCol);
		return tempCell;
	}

	// TODO complex case maybe multiple cells need be added
	protected int getEmptyStartIndex(int columnIndex) {
		for (int i = 0; i < columnIndex; i++) {
			if (cells[i] == 0) {
				return i;
			}
		}
		return columnIndex;
	}

	protected void addEmptyCell(ICellContent cell) {
		int startCol = this.getEmptyStartIndex(cell.getColumn());
		int endCol = cell.getColumn();
		if (startCol < endCol) {
			ICellContent newCell = newCell(currentCell == null ? cell : currentCell, startCol, endCol);
			emitter.startCell(newCell);
			emitter.endCell(newCell);
		}
	}

	/**
	 * append invisible cells before the current one.
	 *
	 * @param startCol start col
	 * @param endCol   end col
	 * @param cell     next visible cell
	 */
	protected void addNoneCell(int startCol, int endCol, ICellContent cell) {
		if (startCol < endCol) {
			ICellContent newCell = newCell(cell, startCol, endCol);
			IStyle cellStyle = newCell.getStyle();
			cellStyle.setProperty(StyleConstants.STYLE_DISPLAY, CSSValueConstants.NONE_VALUE);
			// set the invisible cell, so it won't be handled by following start cell.
			insertNoneCell = newCell;
			emitter.startCell(newCell);
			emitter.endCell(newCell);
		}
	}
}
