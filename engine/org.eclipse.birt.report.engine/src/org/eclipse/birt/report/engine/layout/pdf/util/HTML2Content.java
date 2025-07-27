/***********************************************************************
 * Copyright (c) 2004, 2025 Actuate Corporation and others
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.pdf.util;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.impl.ActionContent;
import org.eclipse.birt.report.engine.content.impl.Column;
import org.eclipse.birt.report.engine.content.impl.ObjectContent;
import org.eclipse.birt.report.engine.content.impl.ReportContent;
import org.eclipse.birt.report.engine.content.impl.TextContent;
import org.eclipse.birt.report.engine.css.dom.StyleDeclaration;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.css.engine.value.FloatValue;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSValueConstants;
import org.eclipse.birt.report.engine.internal.util.DataProtocolUtil;
import org.eclipse.birt.report.engine.internal.util.DataProtocolUtil.DataUrlInfo;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.parser.TextParser;
import org.eclipse.birt.report.engine.util.FileUtil;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSValue;

/**
 * Class <code>HTML2Content</code> encapsulates the logic of converting a
 * section of HTML text to report content. Currently the supported tags are:
 * <table border=1>
 * <tr>
 * <td><b>Tag Name</b></td>
 * <td><b>Supported Attributes</b></td>
 * </tr>
 * <tr>
 * <td>"a"</td>
 * <td>id, name, href, target, style</td>
 * </tr>
 * <tr>
 * <td>"address"</td>
 * <td>style
 * <td>
 * </tr>
 * <tr>
 * <td>"b"</td>
 * <td>style
 * <td>
 * </tr>
 * <tr>
 * <td>"big"</td>
 * <td>style
 * <td>
 * </tr>
 * <tr>
 * <td>"center"</td>
 * <td>style
 * <td>
 * </tr>
 * <tr>
 * <td>"code"</td>
 * <td>style
 * <td>
 * </tr>
 * <tr>
 * <td>"col"</td>
 * <td>width, style</td>
 * </tr>
 * <tr>
 * <td>"del"</td>
 * <td>style
 * <td>
 * </tr>
 * <tr>
 * <td>"dd"</td>
 * <td>style
 * <td>
 * </tr>
 * <tr>
 * <td>"div"</td>
 * <td>style
 * <td>
 * </tr>
 * <tr>
 * <td>"dl"</td>
 * <td>style
 * <td>
 * </tr>
 * <tr>
 * <td>"dt"</td>
 * <td>style
 * <td>
 * </tr>
 * <tr>
 * <td>"em"</td>
 * <td>style
 * <td>
 * </tr>
 * <tr>
 * <td>"font"</td>
 * <td>size, color, face, style</td>
 * </tr>
 * <tr>
 * <td>"h1"</td>
 * <td>align, style</td>
 * </tr>
 * <tr>
 * <td>"h2"</td>
 * <td>align, style</td>
 * </tr>
 * <tr>
 * <td>"h3"</td>
 * <td>align, style</td>
 * </tr>
 * <tr>
 * <td>"h4"</td>
 * <td>align, style</td>
 * </tr>
 * <tr>
 * <td>"h5"</td>
 * <td>align, style</td>
 * </tr>
 * <tr>
 * <td>"h6"</td>
 * <td>align, style</td>
 * </tr>
 * <tr>
 * <td>"hr"</td>
 * <td>width, style</td>
 * </tr>
 * <tr>
 * <td>"i"</td>
 * <td>style
 * <td>
 * </tr>
 * <tr>
 * <td>"img"</td>
 * <td>src, alt, height, width, border, style</td>
 * </tr>
 * <tr>
 * <td>"ins"</td>
 * <td>style
 * <td>
 * </tr>
 * <tr>
 * <td>"li"</td>
 * <td>style
 * <td>
 * </tr>
 * <tr>
 * <td>"object"</td>
 * <td>width, height, align, border, style</td>
 * </tr>
 * <tr>
 * <td>"ol"</td>
 * <td>style
 * <td>
 * </tr>
 * <tr>
 * <td>"p"</td>
 * <td>align, style</td>
 * </tr>
 * <tr>
 * <td>"pre"</td>
 * <td>style
 * <td>
 * </tr>
 * <tr>
 * <td>"span"</td>
 * <td>align, style</td>
 * </tr>
 * <tr>
 * <td>"strong"</td>
 * <td>style
 * <td>
 * </tr>
 * <tr>
 * <td>"strike"</td>
 * <td>style
 * <td>
 * </tr>
 * <tr>
 * <td>"small"</td>
 * <td>style
 * <td>
 * </tr>
 * <tr>
 * <td>"sub"</td>
 * <td>style
 * <td>
 * </tr>
 * <tr>
 * <td>"sup"</td>
 * <td>style
 * <td>
 * </tr>
 * <tr>
 * <td>"table"</td>
 * <td>width, border, bgcolor, cellpadding, style</td>
 * </tr>
 * <tr>
 * <td>"tbody"</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>"td"</td>
 * <td>rowspan, colspan, align, valign, bgcolor, style</td>
 * </tr>
 * <tr>
 * <td>"tfoot"</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>"thead"</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>"tr"</td>
 * <td>align, valign, bgcolor, height, style</td>
 * </tr>
 * <tr>
 * <td>"tt"</td>
 * <td>style
 * <td>
 * </tr>
 * <tr>
 * <td>"u"</td>
 * <td>style
 * <td>
 * </tr>
 * <tr>
 * <td>"ul"</td>
 * <td>style
 * <td>
 * </tr>
 * </table>
 * All the supported HTML tags can use attribute "style" to specify HTML inline
 * style information. The syntax of the style attribute value is CSS inline
 * style syntax. The supported CSS properties are listed below:<br>
 * <b>Text and Fonts</b>
 * <ul>
 * <li>font-family</li>
 * <li>font-size</li>
 * <li>font-weight</li>
 * <li>font-style</li>
 * <li>line-height</li>
 * <li>letter-spacing</li>
 * <li>word-spacing</li>
 * <li>text-align</li>
 * <li>text-decoration</li>
 * <li>text-indent</li>
 * <li>text-transform</li>
 * <li>vertical-align</li>
 * </ul>
 * <b>Colors and Backgrounds</b>
 * <ul>
 * <li>background-color</li>
 * <li>background-image</li>
 * <li>background-repeat</li>
 * </ul>
 * <b>The Box Model - dimensions, padding, margin and borders</b>
 * <ul>
 * <li>width, height</li>
 * <li>padding-top, padding-right, padding-bottom, padding-left</li>
 * <li>border-top, border-right, border-bottom, border-left</li>
 * <li>border-top-style, border-right-style, border-bottom-style,
 * border-left-style</li>
 * <li>border-top-color, border-right-color, border-bottom-color,
 * border-left-color</li>
 * <li>border-top-width, border-right-width, border-bottom-width,
 * border-left-width</li>
 * <li>margin-top, margin-right, margin-bottom, margin-left</li>
 * </ul>
 * <b>Positioning and Display</b>
 * <ul>
 * <li>display</li>
 * <li>visibility</li>
 * </ul>
 *
 * Supported css shorthand as following
 * <table border=1>
 * <tr>
 * <td><b>Shorthand</b></td>
 * <td><b>Comments</b></td>
 * </tr>
 * <tr>
 * <td><b>font</b></td>
 * <td>support font-style, font-weight, font-size, line-height, font-family in
 * font shorthand, can not support system fonts such as caption, icon, menu
 * etc</td>
 * </tr>
 * <tr>
 * <td><b>background</b></td>
 * <td>support background-color and background-image in background
 * shorthand</td>
 * </tr>
 * <tr>
 * <td><b>border</b></td>
 * <td></td>
 * </tr>
 * <tr>
 * <td><b>border-left</b></td>
 * <td></td>
 * </tr>
 * <tr>
 * <td><b>border-right</b></td>
 * <td></td>
 * </tr>
 * <tr>
 * <td><b>border-top</b></td>
 * <td></td>
 * </tr>
 * <tr>
 * <td><b>border-bottom</b></td>
 * <td></td>
 * </tr>
 * <tr>
 * <td><b>padding</b></td>
 * <td></td>
 * </tr>
 * <tr>
 * <td><b>margin</b></td>
 * <td></td>
 * </tr>
 * </table>
 */
public class HTML2Content implements HTMLConstants {

	protected static final HashSet<String> htmlBlockDisplay = new HashSet<String>();

	protected static final HashSet<String> htmlInlineDisplay = new HashSet<String>();

	protected static final HashMap<String, String> textTypeMapping = new HashMap<String, String>();

	private static final String LIST_STYLE_TYPE = "list-style-type";

	private static final int DEFAULT_LIST_ICON_PADDING_NUMBER = 2500;

	private static final int DEFAULT_LIST_ICON_PADDING_SYMBOLE = 5000;

	static {
		htmlInlineDisplay.add(TAG_I);
		htmlInlineDisplay.add(TAG_FONT);
		htmlInlineDisplay.add(TAG_B);
		htmlInlineDisplay.add(TAG_A);
		htmlInlineDisplay.add(TAG_CODE);
		htmlInlineDisplay.add(TAG_EM);
		htmlInlineDisplay.add(TAG_OBJECT);
		htmlInlineDisplay.add(TAG_IMG);
		htmlInlineDisplay.add(TAG_INS);
		htmlInlineDisplay.add(TAG_SPAN);
		htmlInlineDisplay.add(TAG_STRONG);
		htmlInlineDisplay.add(TAG_SUB);
		htmlInlineDisplay.add(TAG_SUP);
		htmlInlineDisplay.add(TAG_TT);
		htmlInlineDisplay.add(TAG_U);
		htmlInlineDisplay.add(TAG_DEL);
		htmlInlineDisplay.add(TAG_STRIKE);
		htmlInlineDisplay.add(TAG_S);
		htmlInlineDisplay.add(TAG_BIG);
		htmlInlineDisplay.add(TAG_SMALL);
	}

	static {
		// block-level elements
		htmlBlockDisplay.add(TAG_DD); // $NON-NLS-1$
		htmlBlockDisplay.add(TAG_DIV); // $NON-NLS-1$
		htmlBlockDisplay.add(TAG_DL); // $NON-NLS-1$
		htmlBlockDisplay.add(TAG_DT); // $NON-NLS-1$
		htmlBlockDisplay.add(TAG_H1); // $NON-NLS-1$
		htmlBlockDisplay.add(TAG_H2); // $NON-NLS-1$
		htmlBlockDisplay.add(TAG_H3); // $NON-NLS-1$
		htmlBlockDisplay.add(TAG_H4); // $NON-NLS-1$
		htmlBlockDisplay.add(TAG_H5); // $NON-NLS-1$
		htmlBlockDisplay.add(TAG_H6); // $NON-NLS-1$
		htmlBlockDisplay.add(TAG_HR); // $NON-NLS-1$
		htmlBlockDisplay.add(TAG_OL); // $NON-NLS-1$
		htmlBlockDisplay.add(TAG_P); // $NON-NLS-1$
		htmlBlockDisplay.add(TAG_PRE); // $NON-NLS-1$
		htmlBlockDisplay.add(TAG_UL); // $NON-NLS-1$
		htmlBlockDisplay.add(TAG_LI); // $NON-NLS-1$
		htmlBlockDisplay.add(TAG_ADDRESS); // $NON-NLS-1$
		htmlBlockDisplay.add(TAG_BODY); // $NON-NLS-1$
		htmlBlockDisplay.add(TAG_CENTER); // $NON-NLS-1$
		htmlBlockDisplay.add(TAG_TABLE); // $NON-NLS-1$
		htmlBlockDisplay.add(TAG_TD); // $NON-NLS-1$
		htmlBlockDisplay.add(TAG_TR); // $NON-NLS-1$

		textTypeMapping.put(IForeignContent.HTML_TYPE, TextParser.TEXT_TYPE_HTML);
		textTypeMapping.put(IForeignContent.TEXT_TYPE, TextParser.TEXT_TYPE_PLAIN);
		textTypeMapping.put(IForeignContent.UNKNOWN_TYPE, TextParser.TEXT_TYPE_AUTO);

	}

	protected static char[] listChar = { '\u2022', '\u25E6', '\u25AA' };

	/**
	 * Get the list of char
	 *
	 * @param nestCount count of chars
	 * @return char
	 */
	public static char getListChar(int nestCount) {
		if (nestCount <= 2) {
			return listChar[nestCount];
		}
		return listChar[2];
	}

	/**
	 * Convert HTML to content
	 *
	 * @param foreign foreign content
	 */
	public static void html2Content(IForeignContent foreign) {
		processForeignData(foreign);
	}

	protected static void processForeignData(IForeignContent foreign) {
		if (foreign.getChildren() != null && foreign.getChildren().size() > 0) {
			return;
		}

		HashMap<Element, StyleProperties> styleMap = new HashMap<Element, StyleProperties>();
		ReportDesignHandle reportDesign = foreign.getReportContent().getDesign().getReportDesign();
		HTMLStyleProcessor htmlProcessor = new HTMLStyleProcessor(reportDesign);
		Object rawValue = foreign.getRawValue();
		Document doc = null;
		if (null != rawValue) {
			doc = new TextParser().parse(foreign.getRawValue().toString(),
					textTypeMapping.get(foreign.getRawType()));
		}

		Element body = null;
		if (doc != null) {
			Node node = doc.getFirstChild();
			// The following must be true
			if (node instanceof Element) {
				body = (Element) node;
			}
		}
		if (body != null) {
			htmlProcessor.execute(body, styleMap, foreign.getReportContent().getReportContext() == null ? null
					: foreign.getReportContent().getReportContext().getAppContext());
			IContainerContent container = foreign.getReportContent().createContainerContent();

			IStyle parentStyle = foreign.getStyle();
			if (CSSValueConstants.INLINE_VALUE.equals(parentStyle.getProperty(StyleConstants.STYLE_DISPLAY))) {
				container.getStyle().setProperty(StyleConstants.STYLE_DISPLAY, CSSValueConstants.INLINE_VALUE);
			}
			addChild(foreign, container);
			processNodes(body, styleMap, container, null, 0);
			// formalizeInlineContainer( new ArrayList( ), foreign, container );
		}
	}

	protected static ILabelContent createLabel(String text, IContent parent) {
		ILabelContent label = parent.getReportContent().createLabelContent();
		addChild(parent, label);
		label.setText(text);
		StyleDeclaration inlineStyle = new StyleDeclaration(parent.getCSSEngine());
		inlineStyle.setProperty(StyleConstants.STYLE_DISPLAY, CSSValueConstants.INLINE_VALUE);
		label.setInlineStyle(inlineStyle);
		return label;
	}

	/**
	 * Visits the children nodes of the specific node
	 *
	 * @param ele        the specific node
	 * @param needEscape the flag indicating the content needs escaping
	 * @param cssStyles
	 * @param content    the parent content of the element
	 *
	 */
	static void processNodes(Element ele, Map<Element, StyleProperties> cssStyles, IContent content,
			ActionContent action, int nestCount) {
		int level = 0;

		// ordered list, handling of start level by attribute start
		if (ele.getTagName().toLowerCase().equals(TAG_OL) && ele.hasAttribute(PROPERTY_OL_START)) {
			int olStartIndex = 0;
			try {
				olStartIndex = Integer.valueOf(ele.getAttribute(PROPERTY_OL_START));
				level += olStartIndex - 1;
			} catch (NumberFormatException nfe) {
				// on error the default index will be used
			}
		}
		for (Node node = ele.getFirstChild(); node != null; node = node.getNextSibling()) {
			if (node.getNodeName().equals(TAG_VALUEOF)) // $NON-NLS-1$
			{
				if (node.getFirstChild() instanceof Element) {
					processNodes((Element) node.getFirstChild(), cssStyles, content, action, nestCount);
				}
			} else if (node.getNodeName().equals(TAG_IMAGE)) // $NON-NLS-1$
			{
				if (node.getFirstChild() instanceof Element) {
					processNodes((Element) node.getFirstChild(), cssStyles, content, action, nestCount);
				}
			} else if (node.getNodeName().equals(TAG_SCRIPT)) // $NON-NLS-1$
			{
			} else if (node.getNodeType() == Node.TEXT_NODE) {
				ILabelContent label = createLabel(node.getNodeValue(), content);
				if (action != null) {
					label.setHyperlinkAction(action);
				}
			} else if (node.getNodeType() == Node.ELEMENT_NODE) {
				handleElement((Element) node, cssStyles, content, action, ++level, nestCount);
			}
		}
	}

	static void handleElement(Element ele, Map<Element, StyleProperties> cssStyles, IContent content,
			ActionContent action, int index, int nestCount) {
		StyleProperties sp = cssStyles.get(ele);
		if (sp != null) {
			if ("none".equals(sp.getStyle().getDisplay())) //$NON-NLS-1$
			{
				// Check if the display mode is none.
				return;
			}
		}

		String lTagName = ele.getTagName().toLowerCase();
		if (lTagName.equals(TAG_A)) // $NON-NLS-1$
		{
			IContainerContent container = content.getReportContent().createContainerContent();
			addChild(content, container);
			handleStyle(ele, cssStyles, container);
			ActionContent actionContent = handleAnchor(ele, container, action);
			processNodes(ele, cssStyles, content, actionContent, 0);
		} else if (lTagName.equals(TAG_IMG)) // $NON-NLS-1$
		{
			outputImg(ele, cssStyles, content);
		} else if (lTagName.equals(TAG_OBJECT)) // $NON-NLS-1$
		{
			outputEmbedContent(ele, cssStyles, content);
		} else if (lTagName.equals(TAG_BR)) // $NON-NLS-1$
		{

			ILabelContent label = content.getReportContent().createLabelContent();
			addChild(content, label);
			label.setText("\n"); //$NON-NLS-1$
			StyleDeclaration inlineStyle = new StyleDeclaration(content.getCSSEngine());
			inlineStyle.setProperty(StyleConstants.STYLE_DISPLAY, CSSValueConstants.INLINE_VALUE);
			label.setInlineStyle(inlineStyle);
		} else if (lTagName.equals(TAG_UL) || lTagName.equals(TAG_OL))// $NON-NLS-1$
		{
			IReportContent report = content.getReportContent();
			ITableContent table = report.createTableContent();
			table.setTagType("L");
			addChild(content, table);
			Column column1 = new Column(report);
			column1.setWidth(new DimensionType(2, "em"));
			table.addColumn(column1);
			column1 = new Column(report);
			table.addColumn(column1);
			handleStyle(ele, cssStyles, table);
			processNodes(ele, cssStyles, table, action, nestCount);

		} else if (lTagName.equals(TAG_LI) // $NON-NLS-1$
				&& ele.getParentNode().getNodeType() == Node.ELEMENT_NODE) {
			IReportContent report = content.getReportContent();

			IRowContent row = report.createRowContent();
			addChild(content, row);
			handleStyle(ele, cssStyles, row);
			row.setTagType("LI");

			// fix scr 157259In PDF <li> effect is incorrect when page break happens.
			// add a container to number serial, keep consistent page-break

			StyleDeclaration styleListIcon = new StyleDeclaration(content.getCSSEngine());
			// list item, set text alignment
			if (!content.isDirectionRTL()) {
				styleListIcon.setProperty(StyleConstants.STYLE_TEXT_ALIGN, CSSValueConstants.RIGHT_VALUE);
			}
			styleListIcon.setProperty(StyleConstants.STYLE_VERTICAL_ALIGN, CSSValueConstants.TOP_VALUE);
			styleListIcon.setProperty(StyleConstants.STYLE_PADDING_BOTTOM, CSSValueConstants.NUMBER_0);
			styleListIcon.setProperty(StyleConstants.STYLE_PADDING_LEFT, CSSValueConstants.NUMBER_0);
			styleListIcon.setProperty(StyleConstants.STYLE_PADDING_TOP, CSSValueConstants.NUMBER_0);
			ICellContent orderCell = report.createCellContent();
			orderCell.setRowSpan(1);
			orderCell.setColumn(0);
			orderCell.setColSpan(1);
			orderCell.setInlineStyle(styleListIcon);
			addChild(row, orderCell);
			TextContent text = (TextContent) report.createTextContent();
			addChild(orderCell, text);
			boolean nestList = false;
			int count = ele.getChildNodes().getLength();
			if (count == 1) {
				Node firstChild = ele.getFirstChild();
				String nodeName = firstChild.getNodeName();
				if (TAG_OL.equals(nodeName) || TAG_UL.equals(nodeName)) {
					nestList = true;
				}
			}
			Object value = cssStyles.get(ele.getParentNode()).getProperty(LIST_STYLE_TYPE);
			String styleType = "";
			if (value != null) {
				styleType = value.toString();
			}
			if (ele.getParentNode().getNodeName().equals(TAG_OL) && !nestList) // $NON-NLS-1$
			{
				// set default style type to the <ol> tag;
				if ("".equals(styleType)) {
					styleType = BulletFrame.CSS_LISTSTYLETYPE_DECIMAL;
				}
				BulletFrame frame = new BulletFrame(styleType);
				// index mean the order in the list
				text.setText(frame.paintBullet(index) + "."); //$NON-NLS-1$
				styleListIcon.setProperty(StyleConstants.STYLE_PADDING_RIGHT,
						new FloatValue(CSSPrimitiveValue.CSS_NUMBER, DEFAULT_LIST_ICON_PADDING_NUMBER));
			} else if (ele.getParentNode().getNodeName().equals(TAG_UL) && !nestList) // $NON-NLS-1$
			{
				BulletFrame frame = new BulletFrame(styleType);
				text.setText(frame.paintBullet(index));
				if ("".equals(text.getText())) // add default list type when tag <ul> attribute is empty.
				{
					text.setText("\u2022"); // the disc type
					text.setTagType("Lbl");
				}
				styleListIcon.setProperty(StyleConstants.STYLE_PADDING_RIGHT,
						new FloatValue(CSSPrimitiveValue.CSS_NUMBER, DEFAULT_LIST_ICON_PADDING_SYMBOLE));
			}
			StyleDeclaration styleListItem = new StyleDeclaration(content.getCSSEngine());
			styleListItem.setProperty(StyleConstants.STYLE_VERTICAL_ALIGN, CSSValueConstants.TOP_VALUE);
			styleListItem.setProperty(StyleConstants.STYLE_PADDING_BOTTOM, CSSValueConstants.NUMBER_0);
			styleListItem.setProperty(StyleConstants.STYLE_PADDING_LEFT, CSSValueConstants.NUMBER_0);
			styleListItem.setProperty(StyleConstants.STYLE_PADDING_RIGHT, CSSValueConstants.NUMBER_0);
			styleListItem.setProperty(StyleConstants.STYLE_PADDING_TOP, CSSValueConstants.NUMBER_0);
			ICellContent childCell = report.createCellContent();
			childCell.setRowSpan(1);
			childCell.setColumn(1);
			childCell.setColSpan(1);
			childCell.setInlineStyle(styleListItem);
			childCell.setTagType("LBody");
			addChild(row, childCell);

			processNodes(ele, cssStyles, childCell, action, nestCount + 1);
		}

		else if (lTagName.equals(TAG_DD) || lTagName.equals(TAG_DT)) // $NON-NLS-1$
		{
			IContainerContent container = content.getReportContent().createContainerContent();
			addChild(content, container);
			handleStyle(ele, cssStyles, container);

			if (lTagName.equals(TAG_DD)) // $NON-NLS-1$
			{
				StyleDeclaration style = new StyleDeclaration(content.getCSSEngine());
				style.setProperty(StyleConstants.STYLE_DISPLAY, CSSValueConstants.INLINE_VALUE);
				style.setProperty(StyleConstants.STYLE_VERTICAL_ALIGN, CSSValueConstants.TOP_VALUE);
				TextContent text = (TextContent) content.getReportContent().createTextContent();
				addChild(container, text);
				if (ele.getParentNode().getNodeName().equals(TAG_DL)) // $NON-NLS-1$
				{
					text.setText(" "); //$NON-NLS-1$
				}
				style.setTextIndent("2em"); //$NON-NLS-1$
				text.setInlineStyle(style);

				IContainerContent childContainer = content.getReportContent().createContainerContent();
				childContainer.setInlineStyle(style);
				addChild(container, childContainer);

				processNodes(ele, cssStyles, container, action, nestCount + 1);

			} else {
				processNodes(ele, cssStyles, container, action, nestCount);
			}

		} else if (TAG_TABLE.equals(lTagName)) // $NON-NLS-1$
		{
			TableProcessor.processTable(ele, cssStyles, content, action);
		} else if (htmlBlockDisplay.contains(lTagName) || htmlInlineDisplay.contains(lTagName)) {
			//
			StyleProperties spEle = cssStyles.get(ele);
			if (spEle.getStyle().getMarginTop() == null) {
				spEle.getStyle().setMarginTop("0px");
			}
			if (spEle.getStyle().getMarginBottom() == null) {
				String marginBottom = (lTagName.equals(TAG_P)) ? "1em" : "0px";
				spEle.getStyle().setMarginBottom(marginBottom);
			}
			IContainerContent container = content.getReportContent().createContainerContent();
			handleStyle(ele, cssStyles, container);
			mapHtmlTagToPdfTag(container, lTagName);
			addChild(content, container);
			processNodes(ele, cssStyles, container, action, nestCount);
		} else {
			processNodes(ele, cssStyles, content, action, nestCount);
		}
	}

	// FIXME: The mapping should be configurable.
	// FIXME: In particular, it should be possible to adjust the heading levels,
	// (i.e to specify a positive or negative offset, such that H1->H2, H2->H3).
	static final Map<String, String> HTML_TAG_TO_PDF_TAG = Map.of(
			"DIV", "DIV",
			"P", "P",
			"H1", "H1",
			"H2", "H2",
			"H3", "H3",
			"H4", "H4"
		);

	/**
	 * This sets the container's tag type corresponding to the HTML tag, if
	 * possible.
	 *
	 * @param container
	 * @param lTagName
	 */
	private static void mapHtmlTagToPdfTag(IContainerContent container, String lTagName) {
		if (lTagName == null)
			return;
		String mapped = HTML_TAG_TO_PDF_TAG.get(lTagName.toUpperCase());
		if (mapped != null) {
			container.setTagType(mapped);
		}
	}

	/**
	 * Outputs the A element
	 *
	 * @param ele the A element instance
	 */
	protected static ActionContent handleAnchor(Element ele, IContent content, ActionContent defaultAction) {
		// If the "id" attribute is not empty, then use it,
		// otherwise use the "name" attribute.
		ActionContent result = defaultAction;
		if (ele.getAttribute(PROPERTY_ID).trim().length() != 0) // $NON-NLS-1$
		{
			content.setBookmark(ele.getAttribute(PROPERTY_ID)); // $NON-NLS-1$
		} else {
			content.setBookmark(ele.getAttribute(PROPERTY_NAME));// $NON-NLS-1$
		}

		if (ele.getAttribute(PROPERTY_HREF).length() > 0) // $NON-NLS-1$
		{
			String href = ele.getAttribute(PROPERTY_HREF); // $NON-NLS-1$
			if (null != href && !"".equals(href)) //$NON-NLS-1$
			{
				ActionContent action = new ActionContent();
				if (href.startsWith("#")) //$NON-NLS-1$
				{
					action.setBookmark(href.substring(1));
				} else {
					String target = ele.getAttribute(PROPERTY_TARGET);
					if ("".equals(target)) {
						target = "_blank";
					}
					action.setHyperlink(href, target);
				}
				result = action;
			}

		}
		return result;
	}

	static void handleStyle(Element ele, Map<Element, StyleProperties> cssStyles, IContent content) {
		StyleProperties sp = cssStyles.get(ele);
		if (sp == null) {
			sp = new StyleProperties(new StyleDeclaration(content.getCSSEngine()));
			cssStyles.put(ele, sp);
		}
		String tagName = ele.getTagName();
		Tag2Style tag2Style = Tag2Style.getStyleProcess(tagName);
		if (tag2Style != null) {
			tag2Style.process(ele, sp);
		}
		content.setInlineStyle(sp.getStyle());
		sp.setProperties(content);
	}

	/**
	 * Outputs the embed content. Currently only support flash.
	 *
	 * @param ele
	 * @param cssStyles
	 * @param content
	 */
	protected static void outputEmbedContent(Element ele, Map<Element, StyleProperties> cssStyles, IContent content) {
		String classId = ele.getAttribute(PROPERTY_CLASSID);
		if ("clsid:D27CDB6E-AE6D-11cf-96B8-444553540000".equalsIgnoreCase(classId)) {
			outputFlash(ele, cssStyles, content);
		}
	}

	/**
	 * Outputs the flash.
	 *
	 * @param ele
	 * @param cssStyles
	 * @param content
	 */
	protected static void outputFlash(Element ele, Map<Element, StyleProperties> cssStyles, IContent content) {
		String src = null;
		String flashVars = null;
		String alt = null;
		NodeList list = ele.getElementsByTagName(PROPERTY_PARAM);
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if (node instanceof Element) {
				if ("movie".equalsIgnoreCase(((Element) node).getAttribute(PROPERTY_NAME))) {
					src = ((Element) node).getAttribute(PROPERTY_VALUE);
				} else if ("flashvars".equalsIgnoreCase(((Element) node).getAttribute(PROPERTY_NAME))) {
					flashVars = ((Element) node).getAttribute(PROPERTY_VALUE);
				} else if ("alt".equalsIgnoreCase(((Element) node).getAttribute(PROPERTY_NAME))) {
					alt = ((Element) node).getAttribute(PROPERTY_VALUE);
				}
			}
		}
		if (src != null) {
			ObjectContent flash = (ObjectContent) ((ReportContent) (content.getReportContent())).createObjectContent();
			flash.setExtension(".swf");
			flash.setMIMEType("application/x-shockwave-flash");
			addChild(content, flash);
			handleStyle(ele, cssStyles, flash);

			if (!FileUtil.isLocalResource(src)) {
				flash.setImageSource(IImageContent.IMAGE_URL);
				flash.setURI(src);
			} else {
				ReportDesignHandle handle = content.getReportContent().getDesign().getReportDesign();
				URL url = handle.findResource(src, IResourceLocator.IMAGE,
						content.getReportContent().getReportContext() == null ? null
								: content.getReportContent().getReportContext().getAppContext());
				if (url != null) {
					src = url.toString();
				}
				flash.setImageSource(IImageContent.IMAGE_FILE);
				flash.setURI(src);
			}

			IForeignContent foreign = getForeignRoot(content);
			if (null != foreign) {
				flash.setWidth(foreign.getWidth());
				flash.setHeight(foreign.getHeight());
			}

			if (flashVars != null && !"".equals(flashVars)) //$NON-NLS-1$
			{
				flash.addParam("FlashVars", flashVars); //$NON-NLS-1$
			}
			if (alt == null) // $NON-NLS-1$
			{
				alt = ele.getAttribute(PROPERTY_ALT);
			}
			if (alt != null && !"".equals(alt)) {
				flash.setAltText(alt); // $NON-NLS-1$
			}
		}
	}

	private static IForeignContent getForeignRoot(IContent content) {
		while (!(content instanceof IForeignContent)) {
			content = (IContent) content.getParent();
			if (content == null) {
				return null;
			}
		}
		return (IForeignContent) content;
	}

	/**
	 * Outputs the image
	 *
	 * @param ele the IMG element instance
	 */
	protected static void outputImg(Element ele, Map<Element, StyleProperties> cssStyles, IContent content) {
		String src = ele.getAttribute("src"); //$NON-NLS-1$
		if (src != null) {
			IImageContent image = content.getReportContent().createImageContent();
			addChild(content, image);
			handleStyle(ele, cssStyles, image);

			if (!FileUtil.isLocalResource(src)) {
				image.setImageSource(IImageContent.IMAGE_URL);
				image.setURI(src);
			} else if (src.startsWith(DataProtocolUtil.DATA_PROTOCOL)) {
				DataUrlInfo parseDataUrl = DataProtocolUtil.parseDataUrl(src);
				image.setImageSource(IImageContent.IMAGE_URL);
				image.setMIMEType(parseDataUrl.getMediaType());
				image.setURI(src);
			} else {
				ReportDesignHandle handle = content.getReportContent().getDesign().getReportDesign();
				URL url = handle.findResource(src, IResourceLocator.IMAGE,
						content.getReportContent().getReportContext() == null ? null
								: content.getReportContent().getReportContext().getAppContext());
				if (url != null) {
					src = url.toString();
				}
				image.setImageSource(IImageContent.IMAGE_FILE);
				image.setURI(src);
			}

			if (null != ele.getAttribute(PROPERTY_WIDTH) && !"".equals(ele.getAttribute(PROPERTY_WIDTH))) //$NON-NLS-1$
																											// //$NON-NLS-3$
			{
				image.setWidth(PropertyUtil.getDimensionAttribute(ele, PROPERTY_WIDTH)); // $NON-NLS-1$
			}
			if (ele.getAttribute(PROPERTY_HEIGHT) != null && !"".equals(ele.getAttribute(PROPERTY_HEIGHT))) //$NON-NLS-1$
																											// //$NON-NLS-3$
			{
				image.setHeight(PropertyUtil.getDimensionAttribute(ele, PROPERTY_HEIGHT)); // $NON-NLS-1$
			}
			if (ele.getAttribute(PROPERTY_ALT) != null && !"".equals(ele.getAttribute(PROPERTY_ALT))) //$NON-NLS-1$
																										// //$NON-NLS-3$
			{
				image.setAltText(ele.getAttribute(PROPERTY_ALT)); // $NON-NLS-1$
			}
		}
	}

	protected static void addChild(IContent parent, IContent child) {

		if (parent != null && child != null) {
			Collection<IContent> children = parent.getChildren();
			if (!children.contains(child)) {
				children.add(child);
				child.setParent(parent);
			}
		}
	}

	/**
	 * Formalize the inline container
	 *
	 * @param parentChildren parent children list
	 * @param parent         parent content
	 * @param content        current content
	 */
	protected static void formalizeInlineContainer(List<IContent> parentChildren, IContent parent, IContent content) {
		IStyle style = content.getStyle();

		CSSValue display = style.getProperty(StyleConstants.STYLE_DISPLAY);

		if (CSSValueConstants.INLINE_VALUE.equals(display)) {

			Iterator<IContent> iter = content.getChildren().iterator();
			ArrayList<IContent> contentChildren = new ArrayList<IContent>();
			IContainerContent clonedBlock = null;
			while (iter.hasNext()) {
				IContent child = iter.next();
				boolean isContainer = child.getChildren().size() > 0;
				if (isContainer) {
					formalizeInlineContainer(contentChildren, content, child);
				}
				if (clonedBlock == null) {
					CSSValue childDisplay = child.getStyle().getProperty(StyleConstants.STYLE_DISPLAY);
					if (CSSValueConstants.BLOCK_VALUE.equals(childDisplay)) {
						IReportContent report = content.getReportContent();
						clonedBlock = report.createContainerContent();
						IStyle clonedStyle = report.createStyle();
						clonedStyle.setProperties(content.getStyle());
						clonedStyle.setProperty(StyleConstants.STYLE_DISPLAY, CSSValueConstants.BLOCK_VALUE);
						clonedBlock.setInlineStyle(clonedStyle);
						clonedBlock.getChildren().add(child);
					} else if (!isContainer) {
						contentChildren.add(child);
					}
				} else {
					iter.remove();
					clonedBlock.getChildren().add(child);
				}
			}
			content.getChildren().clear();
			if (contentChildren.size() > 0) {
				content.getChildren().addAll(contentChildren);
			}

			if (content.getChildren().size() > 0) {
				parentChildren.add(content);
			}

			if (clonedBlock != null) {
				parentChildren.add(clonedBlock);
			}
		} else {
			Iterator<IContent> iter = content.getChildren().iterator();
			ArrayList<IContent> newChildren = new ArrayList<IContent>();
			while (iter.hasNext()) {
				IContent child = iter.next();
				boolean isContainer = child.getChildren().size() > 0;
				if (isContainer) {
					formalizeInlineContainer(newChildren, content, child);

				} else {
					newChildren.add(child);
				}
			}
			content.getChildren().clear();
			if (newChildren.size() > 0) {
				content.getChildren().addAll(newChildren);
				parentChildren.add(content);
			}

		}
	}

	/**
	 * Main method
	 *
	 * @param args arguments
	 */
	public static void main(String[] args) {
		/*
		 * ReportContent report = new ReportContent( ); IContent root =
		 * createBlockContent( report ); IContent block = createBlockContent( report );
		 * root.getChildren( ).add( block ); IContent inlineContent =
		 * createInlineContent( report ); block.getChildren( ).add( createBlockContent(
		 * report ) ); block.getChildren( ).add( inlineContent ); block.getChildren(
		 * ).add( createInlineContent( report ) ); inlineContent.getChildren( ).add(
		 * createInlineContent( report ) ); inlineContent.getChildren( ).add(
		 * createBlockContent( report ) ); inlineContent.getChildren( ).add(
		 * createInlineContent( report ) ); ArrayList list = new ArrayList( );
		 */

		/*
		 * ReportContent report = new ReportContent( ); IContent root =
		 * createBlockContent( report ); IContent inline = createInlineContent( report
		 * ); root.getChildren( ).add( inline ); IContent inlineContent =
		 * createInlineContent( report ); inlineContent.getChildren( ).add(
		 * createInlineContent( report ) ); inline.getChildren( ).add( inlineContent );
		 * ArrayList list = new ArrayList( );
		 */

		/*
		 * ReportContent report = new ReportContent( ); IContent root =
		 * createBlockContent( report ); IContent inline = createInlineContent( report
		 * ); root.getChildren( ).add( inline ); IContent inlineContent =
		 * createInlineContent( report ); inline.getChildren( ).add( inlineContent );
		 * inline.getChildren( ).add( createBlockContent( report ) ); ArrayList list =
		 * new ArrayList( );
		 */

		ReportContent report = new ReportContent();
		IContent root = createBlockContent(report);
		IContent inline = createInlineContent(report);
		root.getChildren().add(inline);
		IContent inlineContent = createInlineContent(report);
		inline.getChildren().add(inlineContent);
		inline.getChildren().add(createBlockContent(report));
		ArrayList<IContent> list = new ArrayList<IContent>();

		formalizeInlineContainer(list, root, inline);
		root.getChildren().clear();
		if (list.size() > 0) {
			root.getChildren().addAll(list);
		}
	}

	protected static IContent createInlineContent(ReportContent report) {
		IContent content = report.createContainerContent();
		content.getStyle().setProperty(StyleConstants.STYLE_DISPLAY, CSSValueConstants.INLINE_VALUE);
		return content;
	}

	protected static IContent createBlockContent(ReportContent report) {
		IContent content = report.createContainerContent();
		content.getStyle().setProperty(StyleConstants.STYLE_DISPLAY, CSSValueConstants.BLOCK_VALUE);
		return content;
	}

}
