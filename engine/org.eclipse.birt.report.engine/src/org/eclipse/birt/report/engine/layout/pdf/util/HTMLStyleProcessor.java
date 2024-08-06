/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.layout.pdf.util;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.batik.util.CSSConstants;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.dom.StyleDeclaration;
import org.eclipse.birt.report.engine.css.engine.BIRTCSSEngine;
import org.eclipse.birt.report.engine.css.engine.CSSEngine;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.css.engine.value.URIValue;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.util.FileUtil;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.css.CSSValue;

/**
 * Converts the deprecated element according to the HTML 4.0 specification and
 * parses the style attribute of the HTML element.
 *
 */
public class HTMLStyleProcessor {

	/** the logger */
	private static Logger logger = Logger.getLogger(HTMLStyleProcessor.class.getName());

	private ReportDesignHandle report;

	/** the CSS2.0 Parser */
	private CSSEngine cssEngine;

	private static Pattern pattern = Pattern.compile("[\\s|\t|\r|\n]*([^:]*)[ ]*:[ ]*([^;]*)[ ]*[;]*");

	private static String LIST_STYLE_TYPE = "list-style-type";

	/**
	 * Constructor
	 *
	 * @param report  report design handle
	 */
	public HTMLStyleProcessor(ReportDesignHandle report) {
		// Takes the zero-length string as parameter just for keeping to the
		// interface of constructor
		cssEngine = new BIRTCSSEngine();
		this.report = report;
	}

	protected void processBackgroundImage(IStyle style, Map context) {
		if (style != null) {
			CSSValue value = style.getProperty(StyleConstants.STYLE_BACKGROUND_IMAGE);
			if (value instanceof URIValue) {
				String bgi = ((URIValue) value).getStringValue();
				if ((null != bgi) && (!"".equals(bgi))) //$NON-NLS-1$
				{
					if (report != null) {
						if (FileUtil.isLocalResource(bgi)) {
							URL url = report.findResource(bgi, IResourceLocator.IMAGE, context);
							if (url != null) {
								String fileName = url.getFile();
								if (fileName != null) {
									bgi = fileName;
								}
							}
						} else {
							// bgi = "url(" + bgi + ")"; //$NON-NLS-1$//$NON-NLS-2$
						}
					}
					// Puts the modified URI of the resource
					style.setBackgroundImage(bgi);
				}
			}
		}
	}

	protected StyleProperties getStyleProperties(Element ele, HashMap<Element, StyleProperties> styles) {
		StyleProperties sp = styles.get(ele);
		if (sp == null) {
			sp = new StyleProperties(new StyleDeclaration(cssEngine));
			styles.put(ele, sp);
		}
		return sp;
	}

	/**
	 * Parses the style attribute of the element node and converts the deprecated
	 * element node in HTML 4.0, and calls it on its children element nodes
	 * recursively
	 *
	 * @param ele     the element node in the DOM tree
	 * @param styles  element map and the according style properties
	 * @param context the text content object
	 */
	public void execute(Element ele, HashMap<Element, StyleProperties> styles, Map context) {

		StringBuffer strStyle = new StringBuffer();
		StyleProperties sp = getStyleProperties(ele, styles);
		try {
			String inlineStyle = ele.getAttribute("style"); //$NON-NLS-1$
			if (null != inlineStyle && !"".equals(inlineStyle)) //$NON-NLS-1$
			{
				StringBuffer buffer = new StringBuffer();
				Matcher matcher = pattern.matcher(inlineStyle);
				while (matcher.find()) {
					String name = matcher.group(1);
					String value = matcher.group(2);
					if (name != null && name.length() > 0 && value != null && value.length() > 0) {
						ShortHandProcessor.process(buffer, name, value, cssEngine);
					}
					if (StyleProperties.WIDTH.equals(name)) {
						if (value != null && value.length() > 0) {
							DimensionType d = DimensionType.parserUnit(value);
							if (d != null) {
								sp.addProperty(StyleProperties.WIDTH, d);
							}
						}
					}
					if (StyleProperties.HEIGHT.equals(name)) {
						if (value != null && value.length() > 0) {
							DimensionType d = DimensionType.parserUnit(value);
							if (d != null) {
								sp.addProperty(StyleProperties.HEIGHT, d);
							}
						}
					}
					// support list-style-type
					if (LIST_STYLE_TYPE.equals(name)) {
						if (value != null && value.length() > 0) {
							sp.addProperty(LIST_STYLE_TYPE, value);
						}
					}
					// support text-decoration
					if (CSSConstants.CSS_TEXT_DECORATION_PROPERTY.equals(name)) {
						if (value != null && value.length() > 0) {
							IStyle style = sp.getStyle();
							String[] vs = value.split(" ");
							for (int i = 0; i < vs.length; i++) {
								if (CSSConstants.CSS_UNDERLINE_VALUE.equals(vs[i])) {
									style.setTextUnderline(CSSConstants.CSS_UNDERLINE_VALUE);
								} else if (CSSConstants.CSS_LINE_THROUGH_VALUE.equals(vs[i])) {
									style.setTextLineThrough(CSSConstants.CSS_LINE_THROUGH_VALUE);
								} else if (CSSConstants.CSS_OVERLINE_VALUE.equals(vs[i])) {
									style.setTextOverline(CSSConstants.CSS_OVERLINE_VALUE);
								}

							}
						}
					}
				}
				strStyle.append(buffer.toString());
			}
			if (strStyle.length() > 0) {
				sp.getStyle().setProperties(
						(StyleDeclaration) cssEngine.parseStyleDeclaration(strStyle.toString().toLowerCase()));
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "The css statement is:" //$NON-NLS-1$
					+ ele.getAttribute("style"), e); //$NON-NLS-1$
		}
		ele.removeAttribute("style"); //$NON-NLS-1$

		// handle background image
		processBackgroundImage(sp.getStyle(), context);

		// Walks on its children nodes recursively
		for (int i = 0; i < ele.getChildNodes().getLength(); i++) {
			Node child = ele.getChildNodes().item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				execute((Element) child, styles, context);
			}
		}
	}

}
