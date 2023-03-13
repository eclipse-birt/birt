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

package org.eclipse.birt.report.engine.executor.css;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.birt.report.engine.util.FileUtil;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

//TODO: review, we should remove the CssParser from this class.
//TODO: review, in HTML, we need support a RESOURCE tag like VALUE-OF which create a URL.
/**
 * Converts the deprecated element according to the HTML 4.0 specification and
 * parses the style attribute of the HTML element.
 *
 */
public class HTMLProcessor {

	/** the logger */
	private static Logger logger = Logger.getLogger(HTMLProcessor.class.getName());

	/** the possible values for property SIZE of HTML element FONT */
	private static String[] FONT_SIZE = { "7.5pt", //$NON-NLS-1$
			"7.5pt", "7.5pt", //$NON-NLS-1$ //$NON-NLS-2$
			"7.5pt", "7.5pt", //$NON-NLS-1$//$NON-NLS-2$
			"7.5pt", "10pt", //$NON-NLS-1$ //$NON-NLS-2$
			"7.5pt", "7.5pt", //$NON-NLS-1$ //$NON-NLS-2$
			"10pt", "12pt", //$NON-NLS-1$ //$NON-NLS-2$
			"13.8pt", "18pt", //$NON-NLS-1$//$NON-NLS-2$
			"23pt", "36pt" }; //$NON-NLS-1$//$NON-NLS-2$

	private static final Pattern IN_BRACKET_PATTERN = Pattern.compile("\\(.*?\\)");

	private static final Pattern STYLE_PAIR_PATTERN = Pattern.compile("[ ]*([^:]*)[ ]*:[ ]*([^;]*)[ ]*[;]*");

	private static final String BRACKETED_REPLACEMENT = "___BRACKETED___";

	// private static String[] FONT_SIZE = new String[]{"xx-small", "x-small",
	// //$NON-NLS-1$ //$NON-NLS-2$
	// "small", "medium", //$NON-NLS-1$ //$NON-NLS-2$
	// "large", "x-large", //$NON-NLS-1$//$NON-NLS-2$
	// "xx-large", "xxx-large"}; //$NON-NLS-1$//$NON-NLS-2$

	protected ReportDesignHandle design;

	protected String rootPath;

	private Map appContext;

	/**
	 * Constructor
	 *
	 * @param context the execution context
	 */
	public HTMLProcessor(ReportDesignHandle design, Map context) {
		this.design = design;
		this.rootPath = null;
		// Takes the zero-length string as parameter just for keeping to the
		// interface of constructor
		this.appContext = context;
	}

	public HTMLProcessor(String rootPath) {
		this.design = null;
		this.rootPath = rootPath;
		// Takes the zero-length string as parameter just for keeping to the
		// interface of constructor
	}

	/**
	 * Parses the style attribute of the element node and converts the deprecated
	 * element node in HTML 4.0, and calls it on its children element nodes
	 * recursively
	 *
	 * @param ele  the element node in the DOM tree
	 * @param text the text content object
	 */
	public void execute(Element ele, HashMap styles) {
		HashMap cssStyle = new HashMap();
		if (ele.hasAttribute("style")) //$NON-NLS-1$
		{
			String inlineStyle = ele.getAttribute("style"); //$NON-NLS-1$
			if (null != inlineStyle && !"".equals(inlineStyle)) //$NON-NLS-1$
			{

				ArrayList<String> bracketed = new ArrayList<>();
				// replace all bracketed content to avoid : and ; in the bracket.
				Matcher matcher = IN_BRACKET_PATTERN.matcher(inlineStyle);
				while (matcher.find()) {
					bracketed.add(matcher.group(0));
				}
				inlineStyle = matcher.replaceAll(BRACKETED_REPLACEMENT);

				int replacementIndex = 0;
				matcher = STYLE_PAIR_PATTERN.matcher(inlineStyle);
				while (matcher.find()) {
					String name = matcher.group(1);
					String value = matcher.group(2);
					if (name != null && name.length() > 0 && value != null && value.length() > 0) {
						while (value.contains(BRACKETED_REPLACEMENT)) {
							value = value.replace(BRACKETED_REPLACEMENT, bracketed.get(replacementIndex++));
						}
						cssStyle.put(name, value.trim());
					}
				}
				ele.removeAttribute("style"); //$NON-NLS-1$
				// If the background image is a local resource, then get its
				// global
				// URI.
				String src = (String) cssStyle.get("background-image"); //$NON-NLS-1$
				if (src != null) {
					// The resource is surrounded with "url(" and ")", or "\"",
					// or
					// "\'". Removes them.
					if (src.startsWith("url(") && src.length() > 5) //$NON-NLS-1$
					{
						src = src.substring(4, src.length() - 1);
					} else if ((src.startsWith("\"") || src.startsWith("\'")) //$NON-NLS-1$ //$NON-NLS-2$
							&& src.length() > 2) {
						src = src.substring(1, src.length() - 1);
					}
					if (design != null) {
						URL url = design.findResource(src, IResourceLocator.IMAGE, appContext);
						if (url != null) {
							src = url.toExternalForm();
						}
					}
					if (rootPath != null) {
						// Checks if the resource is local
						if (FileUtil.isLocalResource(src)) {
							src = FileUtil.getAbsolutePath(rootPath, src);
						}
					}
					if (src != null) {
						// Puts the modified URI of the resource
						cssStyle.put("background-image", src); //$NON-NLS-1$
					} else {
						// If the resource does not exist, then removes this
						// item.
						cssStyle.remove("background-image"); //$NON-NLS-1$
					}
				}
			}

			// FOR HTML 4.0 COMPATIBILITY
			if ("b".equals(ele.getTagName())) //$NON-NLS-1$
			{
				addToStyle(cssStyle, "font-weight", "bold"); //$NON-NLS-1$//$NON-NLS-2$
				// Re-points to the element node in the tree
				ele = replaceElement(ele, "span"); //$NON-NLS-1$
			} else if ("center".equals(ele.getTagName())) //$NON-NLS-1$
			{
				addToStyle(cssStyle, "text-align", "center"); //$NON-NLS-1$ //$NON-NLS-2$
				ele = replaceElement(ele, "div"); //$NON-NLS-1$
			} else if ("font".equals(ele.getTagName())) //$NON-NLS-1$
			{
				addToStyle(cssStyle, "color", ele.getAttribute("color")); //$NON-NLS-1$ //$NON-NLS-2$
				addToStyle(cssStyle, "font-family", ele.getAttribute("face")); //$NON-NLS-1$ //$NON-NLS-2$
				if (ele.hasAttribute("size")) //$NON-NLS-1$
				{
					try {
						int size = Integer.parseInt(ele.getAttribute("size")); //$NON-NLS-1$
						addToStyle(cssStyle, "font-size", FONT_SIZE[size + 7]); //$NON-NLS-1$
					} catch (Exception e) {
						logger.log(Level.SEVERE,
								"There is a invalid value for property SIZE of element FONT in the HTML."); //$NON-NLS-1$
					}
				}
				// Removes these attributes to avoid for being copied again.
				ele.removeAttribute("color"); //$NON-NLS-1$
				ele.removeAttribute("face"); //$NON-NLS-1$
				ele.removeAttribute("size"); //$NON-NLS-1$
				ele = replaceElement(ele, "span"); //$NON-NLS-1$
			} else if ("i".equals(ele.getTagName())) //$NON-NLS-1$
			{
				addToStyle(cssStyle, "font-style", "italic"); //$NON-NLS-1$ //$NON-NLS-2$
				ele = replaceElement(ele, "span"); //$NON-NLS-1$
			} else if ("u".equals(ele.getTagName())) //$NON-NLS-1$
			{
				String decoration = (String) cssStyle.get("text-decoration"); //$NON-NLS-1$
				// The property "text-decoration" is made of more than one
				// token.
				if (decoration != null && decoration.indexOf("underline") == -1 //$NON-NLS-1$
						&& decoration.indexOf("none") == -1 //$NON-NLS-1$
						&& decoration.indexOf("inherit") == -1) //$NON-NLS-1$
				{
					decoration = decoration + " underline"; //$NON-NLS-1$
				} else if (decoration == null) {
					decoration = "underline"; //$NON-NLS-1$
				}
				cssStyle.put("text-decoration", decoration); //$NON-NLS-1$
				ele = replaceElement(ele, "span"); //$NON-NLS-1$
			} else if ("img".equals(ele.getTagName())) {
				String src = ele.getAttribute("src"); //$NON-NLS-1$
				if (src != null) {
					// The resource is surrounded with "url(" and ")", or "\"",
					// or
					// "\'". Removes them.
					if ((src.startsWith("\"") || src.startsWith("\'")) //$NON-NLS-1$ //$NON-NLS-2$
							&& src.length() > 2) {
						src = src.substring(1, src.length() - 1);
					}
					if (design != null) {
						URL url = design.findResource(src, IResourceLocator.IMAGE, appContext);
						if (url != null) {
							src = url.toExternalForm();
						}
					}
					if (rootPath != null) {
						// Checks if the resource is local
						if (FileUtil.isLocalResource(src)) {
							src = FileUtil.getAbsolutePath(rootPath, src);
						}
					}
					if (src != null) {
						// Puts the modified URI of the resource
						ele.removeAttribute("src"); //$NON-NLS-1$
						ele.setAttribute("src", src);
					}
				}
			}
			styles.put(ele, cssStyle);
		}

		// Walks on its children nodes recursively
		for (int i = 0; i < ele.getChildNodes().getLength(); i++) {
			Node child = ele.getChildNodes().item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				execute((Element) child, styles);
			}
		}

	}

	/**
	 * Replaces the previous element with the new tag name in the same position and
	 * return it
	 *
	 * @param oldEle the replaced element
	 * @param tag    the tag name of the new HTML element
	 * @return the new HTML element
	 */
	private Element replaceElement(Element oldEle, String tag) {
		Element newEle = oldEle.getOwnerDocument().createElement(tag);
		// Copies the attributes
		for (int i = 0; i < oldEle.getAttributes().getLength(); i++) {
			String attrName = oldEle.getAttributes().item(i).getNodeName();
			newEle.setAttribute(attrName, oldEle.getAttribute(attrName));
		}
		// Copies the children nodes
		// Note: After the child node is moved to another parent node, then
		// relationship between it and its sibling is removed. So here calls
		// <code>Node.getFirstChild()</code>again and again till it is null.
		for (Node child = oldEle.getFirstChild(); child != null; child = oldEle.getFirstChild()) {
			newEle.appendChild(child);
		}
		oldEle.getParentNode().replaceChild(newEle, oldEle);
		return newEle;
	}

	/**
	 * Adds the attribute name and value to the style if attribute value is not null
	 * and a zero-length string and the added attribute is not in the style.
	 *
	 * @param style     the style attribute for HTML element
	 * @param attrName  the added attribute name
	 * @param attrValue the added attribute value
	 */

	private void addToStyle(HashMap style, String attrName, String attrValue) {
		if (attrValue == null || attrValue.trim().length() == 0) {
			return;
		}
		if (style.get(attrName) == null) {
			style.put(attrName, attrValue);
		}
	}
}
