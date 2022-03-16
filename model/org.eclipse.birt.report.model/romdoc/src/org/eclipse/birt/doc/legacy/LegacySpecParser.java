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

package org.eclipse.birt.doc.legacy;

import java.io.FileNotFoundException;

import org.eclipse.birt.doc.util.HTMLParser;
import org.eclipse.birt.doc.util.HtmlDocReader;

public class LegacySpecParser extends HtmlDocReader {
	protected SpecElement element = new SpecElement();

	public LegacySpecParser() {
	}

	public void parse(String fileName, int itemType) throws FileNotFoundException {
		element.type = itemType;
		parser.open(fileName);

		// Get the element name from the file name.

		int posn = fileName.lastIndexOf('.');
		if (posn != -1) {
			element.name = fileName.substring(0, posn);
			posn = element.name.lastIndexOf("/");
			if (posn != -1) {
				element.name = element.name.substring(posn + 1);
			}
		}

		// Parse the document.

		skipTo("body"); //$NON-NLS-1$
		parseHeader();
		parseSummaryText();
		parseBlocks();
		parseProps();
		parser.close();
	}

	private String label(SpecElement element) {
		String label = "[" + Integer.toString(parser.getLineNo()) + "] ";
		if (element.type == SpecElement.ELEMENT) {
			label += "Element ";
		} else {
			label += "Structure ";
		}
		label += element.name + ": ";
		return label;
	}

	private void warning(SpecElement element, String msg) {
		System.out.print(label(element));
		System.out.println(msg);
	}

	private void error(SpecElement element, String msg) {
		System.err.print(label(element));
		System.err.println(msg);
	}

	private String label(SpecElement element, SpecProperty prop) {
		return "[" + Integer.toString(parser.getLineNo()) + "] Property " + element.name + "." + prop.name + ": ";
	}

	private void warning(SpecElement element, SpecProperty prop, String msg) {
		System.out.print(label(element, prop));
		System.out.println(msg);
	}

	private void error(SpecElement element, SpecProperty prop, String msg) {
		System.err.print(label(element, prop));
		System.err.println(msg);
	}

	private String label(SpecElement element, SpecMethod method) {
		return "[" + Integer.toString(parser.getLineNo()) + "] Method " + element.name + "." + method.name + ": ";
	}

	private void warning(SpecElement element, SpecMethod method, String msg) {
		System.out.print(label(element, method));
		System.out.println(msg);
	}

	private String label(SpecElement element, SpecSlot slot) {
		return "[" + Integer.toString(parser.getLineNo()) + "] Method " + element.name + "." + slot.name + ": ";
	}

	private void warning(SpecElement element, SpecSlot slot, String msg) {
		System.out.print(label(element, slot));
		System.out.println(msg);
	}

	private String getHeaderText(String text) {
		int posn = text.lastIndexOf("&nbsp;"); //$NON-NLS-1$
		if (posn != -1) {
			text = text.substring(posn + 6);
		}
		return text;
	}

	private void parseHeader() {
		skipTo("h2"); //$NON-NLS-1$
		element.displayName = getHeaderText(getTextTo("/h2", true)); //$NON-NLS-1$
	}

	private void parseSummaryText() {
		int token = getToken();
		if (!isPara(token, "MsoBodyText")) //$NON-NLS-1$
		{
			pushToken(token);
			return;
		}
		element.summary = getTextTo("/p"); //$NON-NLS-1$
	}

	static String DESIGN_OBJECT = "JavaScript Design-time Object:"; //$NON-NLS-1$
	static String DESIGN_OBJECT2 = "Design Object:"; //$NON-NLS-1$
	static String RUNTIME_OBJECT = "JavaScript Runtime Object:"; //$NON-NLS-1$
	static String RUNTIME_OBJECT2 = "Runtime Object:"; //$NON-NLS-1$
	static String AVAILABILITY = "Availability:"; //$NON-NLS-1$
	static String BASE_ELEMENT = "Base Element:";
	private static final String XML_ELEMENT_NAME = "XML Element Name:";
	private static final String STYLE_NAME = "Predefined Style Name:";
	private static final String CARDINALITY = "Cardinality:";

	protected String copyBlock() {
		parser.ignoreWhitespace(false);
		StringBuilder text = new StringBuilder();
		int token;
		boolean inCell = false;
		boolean inHead = false;
		boolean inCode = false;
		boolean inIgnore = false;
		boolean inList = false;
		for (;;) {
			token = getToken();
			if (token == HTMLParser.EOF) {
				break;
			}
			if (token == HTMLParser.TEXT) {
				String fragment = parser.getTokenText();
				if (fragment.equals("&nbsp;")) {
					continue;
				}
				text.append(fragment);
				continue;
			}
			if (token != HTMLParser.ELEMENT) {
				continue;
			}
			if (isPara(token, "ManpageTitle")) //$NON-NLS-1$
			{
				pushToken(token);
				break;
			}
			String tag = parser.getTokenText().toLowerCase();
			if (isBlockEnd(tag)) {
				pushToken(token);
				break;
			}
			if (tag.equals("thead")) {
				inHead = true;
			} else if (tag.equals("/thead")) {
				inHead = false;
			}
			if (tag.equals("p")) {
				// Convert Word-style bullets to HTML lists

				String value = parser.getAttrib("class");
				if (value != null && value.equals("Bullet")) {
					if (!inList) {
						text.append("\n<ul>\n");
					}
					inList = true;
					text.append("<li>");
					token = getToken();
					if (isElement(token, "span")) {
						token = getToken();
						if (token != HTMLParser.TEXT) {
							pushToken(token);
						}
					} else {
						pushToken(token);
					}
					value = getTextTo("/p", true);
					while (value.startsWith("&nbsp;")) {
						value = value.substring(6);
					}
					text.append(value.trim());
					text.append("</li>\n");
				}

				// Strip out MS Word formatting

				else if (!inCell) {
					if (inList) {
						text.append("</ul>\n");
					}
					inList = false;
					text.append("<p>");
				}
			} else if (tag.equals("span")) {
				// Convert from MS code formatting to HTML

				String value = parser.getAttrib("class");
				if (value != null && value.equals("CodeText")) {
					text.append("<code>");
					inCode = true;
				} else {
					value = parser.getAttrib("style");
					if (value == null) {
						text.append(parser.getFullElement());
					} else if (value.startsWith("font-weight: normal") || value.startsWith("font-style: normal;")) {
						inIgnore = true;
					} else {
						if (inList) {
							text.append("</ul>\n");
							inList = false;
						}
						text.append(parser.getFullElement());
					}
				}
			} else if (tag.equals("/span")) {
				// Convert from MS code formatting to HTML

				if (inCode) {
					text.append("</code>");
					inCode = false;
				} else if (inIgnore) {
					inIgnore = false;
				} else {
					text.append(parser.getFullElement());
				}
			} else if (tag.equals("/p")) {
				// Strip out MS Word formatting

				if (!inCell) {
					text.append("</p>");
				}
			} else if (tag.equals("b") || tag.equals("/b")) {
				if (!inHead) {
					text.append(parser.getFullElement());
				}
			} else if (tag.equals("a") || tag.equals("/a")) {
				// Strip out MS Word anchors
			} else if (tag.equals("table")) {
				// Convert table formatting

				text.append("<table class=\"section-table\">");
			} else if (tag.equals("tr")) {
				// Clean up MS row formatting

				text.append("<tr>");
			} else if (tag.equals("td")) {
				// Clean up MS row formatting

				inCell = true;
				text.append("<td");
				String value = parser.getAttrib("rowspan");
				if (!isBlank(value)) {
					text.append(" rowspan=\"" + value + "\"");
				}
				value = parser.getAttrib("colspan");
				if (!isBlank(value)) {
					text.append(" colspan=\"" + value + "\"");
				}
				text.append(">");
			} else {
				text.append(parser.getFullElement());
				if (tag.equals("/td") || tag.equals("/tr")) {
					inCell = false;
				} else if (tag.equals("/table")) {
					inCell = false;
					inHead = false;
				}
			}
		}
		if (inList) {
			text.append("</ul>\n");
		}
		parser.ignoreWhitespace(true);
		return text.toString().trim();
	}

	private void parseBlocks() {
		for (;;) {
			int token = getToken();
			if (!isPara(token, "ManpageTitle")) //$NON-NLS-1$
			{
				pushToken(token);
				return;
			}
			String heading = getTextTo("/p", true); //$NON-NLS-1$
			if (heading.equalsIgnoreCase("Summary")) { //$NON-NLS-1$
				parseSummaryBlock();
			} else if (heading.equalsIgnoreCase("Inherited Properties")) //$NON-NLS-1$
			{
				parseInheritedPropertyBlock();
			} else if (heading.equalsIgnoreCase("Properties")) //$NON-NLS-1$
			{
				parsePropertyBlock();
			} else if (heading.equalsIgnoreCase("Methods")) //$NON-NLS-1$
			{
				parseMethodBlock();
			} else if (heading.equalsIgnoreCase("Contents")) //$NON-NLS-1$
			{
				parseSlotBlock();
			} else if (heading.equalsIgnoreCase("Description")) //$NON-NLS-1$
			{
				element.description = append(element.description, parseTextBlock());
			} else if (heading.equalsIgnoreCase("XML Summary")) //$NON-NLS-1$
			{
				element.xmlSummary = parseTextBlock();
			} else if (heading.equalsIgnoreCase("See Also")) //$NON-NLS-1$
			{
				element.seeAlso = parseTextBlock();
			} else {
				warning(element, "Unrecognized header: " + heading);
				element.description = append(element.description,
						"\n\n<h4>" + heading + "</h4>\n\n" + copyBlock() + "\n");
			}
		}
	}

	private void skipBlock() {
		int token;
		for (;;) {
			token = getToken();
			if (token == HTMLParser.EOF) {
				break;
			}
			if (token != HTMLParser.ELEMENT) {
				continue;
			}
			if (isPara(token, "ManpageTitle")) //$NON-NLS-1$
			{
				pushToken(token);
				break;
			}
			String tag = parser.getTokenText().toLowerCase();
			if (isBlockEnd(tag)) {
				pushToken(token);
				break;
			}
		}
	}

	private String getSince(String text) {
		if ((text.indexOf("Not in") != -1) || (text.indexOf("After the") != -1)) {
			return "reserved";
		}
		return "1.0";
	}

	private void parseSummaryBlock() {
		int token = getToken();
		while (isPara(token, "MsoBodyText")) //$NON-NLS-1$
		{
			String line = getTextTo("/p", true); //$NON-NLS-1$
			if (startsWith(line, DESIGN_OBJECT)) {
				element.designObjName = getObjName(getTail(line, DESIGN_OBJECT));
			} else if (startsWith(line, DESIGN_OBJECT2)) {
				element.designObjName = getObjName(getTail(line, DESIGN_OBJECT2));
			} else if (startsWith(line, RUNTIME_OBJECT)) {
				element.stateObjName = getObjName(getTail(line, RUNTIME_OBJECT));
			} else if (startsWith(line, RUNTIME_OBJECT2)) {
				element.stateObjName = getObjName(getTail(line, RUNTIME_OBJECT2));
			} else if (startsWith(line, XML_ELEMENT_NAME)) {
				element.xmlElement = getTail(line, XML_ELEMENT_NAME);
			} else if (startsWith(line, STYLE_NAME)) {
				element.styleNames = getTail(line, STYLE_NAME);
			} else if (startsWith(line, AVAILABILITY)) {
				String tail = getTail(line, AVAILABILITY);
				element.since = getSince(tail);
			} else if (startsWith(line, BASE_ELEMENT)) {
				// Ignore
			} else if (startsWith(line, DISPLAY_NAME)) {
				String tail = getObjName(getTail(line, DISPLAY_NAME));
				if (!tail.equals(element.displayName)) {
					tail += " " + element.getTypeName();
				}
				if (!tail.equals(element.displayName)) {
					error(element, "Display name: " + tail + " Does not match heading " + element.displayName);
				}
			} else {
				warning(element, "Unrecognized summary header: " + line);
			}
			token = getToken();
		}
		pushToken(token);
	}

	private String getObjName(String name) {
		int posn = name.indexOf("(");
		if (posn != -1) {
			name = name.substring(0, posn - 1);
		}
		return name.trim();
	}

	public SpecElement getElement() {
		return element;
	}

	private String[] getMemberBlock() {
		int token = getToken();
		String fields[] = new String[2];
		for (;;) {
			if (isPara(token, "Property")) {
				fields[0] = getTextTo("/p", true);
				break;
			} else if (isPara(token, "MsoBodyText")) {
				fields[0] = getTextTo("/p", false);
				if (fields[0].startsWith("<code>")) {
					fields[0] = strip(fields[0], "code");
				}
				break;
			} else {
				pushToken(token);
				return null;
			}
		}

		token = getToken();
		if (isPara(token, "PropertyDescrip")) //$NON-NLS-1$
		{
			fields[1] = getTextTo("/p");
		}
		return fields;
	}

	private void parseInheritedPropertyBlock() {
		for (;;) {
			String fields[] = getMemberBlock();
			if (fields == null) {
				return;
			}

			SpecInheritedProperty prop = new SpecInheritedProperty();
			prop.name = fields[0];
			prop.description = fields[1]; // $NON-NLS-1$
			element.addInheritedProperty(prop);
		}
	}

	private void parsePropertyBlock() {
		for (;;) {
			String fields[] = getMemberBlock();
			if (fields == null) {
				return;
			}

			SpecProperty prop = new SpecProperty();
			prop.name = fields[0];
			prop.isArray = SpecObject.TRI_FALSE;
			if (prop.name.endsWith("[]")) //$NON-NLS-1$
			{
				prop.name = prop.name.substring(0, prop.name.length() - 2);
				prop.isArray = SpecObject.TRI_TRUE;
			} else if (prop.name.endsWith("[ ]")) //$NON-NLS-1$
			{
				prop.name = prop.name.substring(0, prop.name.length() - 3);
				prop.isArray = SpecObject.TRI_TRUE;
			}
			prop.shortDescrip = fields[1];

			// Ignore generic property names like <i>styleProps</i>

			if (!prop.name.startsWith("<i>")) {
				element.addProperty(prop);
			}
		}
	}

	private void parseMethodBlock() {
		for (;;) {
			String fields[] = getMemberBlock();
			if (fields == null) {
				return;
			}

			SpecMethod method = new SpecMethod();
			method.name = fields[0];
			method.shortDescrip = fields[1]; // $NON-NLS-1$
			element.addMethod(method);
		}
	}

	private void parseSlotBlock() {
		for (;;) {
			String fields[] = getMemberBlock();
			if (fields == null) {
				return;
			}

			SpecSlot slot = new SpecSlot();
			slot.name = fields[0]; // $NON-NLS-1$
			slot.shortDescrip = fields[1]; // $NON-NLS-1$
			element.addSlot(slot);
		}
	}

	private String parseDescripBlock() {
		return copyBlock();
	}

	private String parseTextBlock() {
		return strip(parseDescripBlock(), "p");
	}

	private void parseProps() {
		for (;;) {
			int token = getToken();
			if ((token != HTMLParser.ELEMENT) || !parser.getTokenText().equalsIgnoreCase("h3")) { //$NON-NLS-1$
				break;
			}
			String heading = getHeaderText(getTextTo("/h3", true)); //$NON-NLS-1$
			heading = heading.trim();
			if (heading.endsWith("Property")) //$NON-NLS-1$
			{
				heading = heading.substring(0, heading.length() - 9);
				parseProperty(heading);
			} else if (heading.endsWith("Method")) //$NON-NLS-1$
			{
				heading = heading.substring(0, heading.length() - 7);
				parseMethod(heading);
			} else if (heading.endsWith("Slot")) //$NON-NLS-1$
			{
				heading = heading.substring(0, heading.length() - 5);
				parseSlot(heading);
			} else {
				warning(element, ": Unrecognized h3 header: " + heading);
			}
		}
	}

	private void parseProperty(String propName) {
		SpecProperty prop = element.getProperty(propName);
		if (prop == null) {
			prop = new SpecProperty();
			prop.name = propName;
			element.addProperty(prop);
		}
		int token = getToken();
		if (isPara(token, "MsoBodyText")) //$NON-NLS-1$
		{
			prop.summary = getTextTo("/p", true); //$NON-NLS-1$
			token = getToken();
		}
		for (;;) {
			if (!isPara(token, "ManpageTitle")) //$NON-NLS-1$
			{
				pushToken(token);
				return;
			}
			String heading = getTextTo("/p", true); //$NON-NLS-1$
			if (heading.equalsIgnoreCase("Synopsis")) { //$NON-NLS-1$
				skipBlock();
			} else if (heading.equalsIgnoreCase("Summary")) //$NON-NLS-1$
			{
				parsePropertySummaryBlock(prop);
			} else if (heading.equalsIgnoreCase("ROM Summary")) //$NON-NLS-1$
			{
				parsePropertySummaryBlock(prop);
			} else if (heading.equalsIgnoreCase("Description")) //$NON-NLS-1$
			{
				prop.description = append(prop.description, parseTextBlock());
			} else if (heading.equalsIgnoreCase("See Also")) //$NON-NLS-1$
			{
				prop.seeAlso = parseTextBlock();
			} else if (heading.equalsIgnoreCase("Choices")) //$NON-NLS-1$
			{
				parseChoices(prop);
			} else if (heading.equalsIgnoreCase("Runtime Scripting") || heading.equalsIgnoreCase("XML Summary")) {
				prop.description = append(prop.description, "\n\n<h4>" + heading + "</h4>\n\n" + copyBlock() + "\n");
			} else if (heading.equals("&nbsp;")) {
				// Artifact of formatting: ignore.
			} else {
				warning(element, prop, "Unrecognized property header: " + heading);
				prop.description = append(prop.description, "\n\n<h4>" + heading + "</h4>\n\n" + copyBlock() + "\n");
			}
			token = getToken();
		}
	}

	static final String DISPLAY_NAME = "Display Name:"; //$NON-NLS-1$
	static final String ROM_TYPE = "ROM Type:"; //$NON-NLS-1$
	static final String JS_TYPE = "JavaScript Type:"; //$NON-NLS-1$
	static final String DEFAULT_VALUE = "Default value:"; //$NON-NLS-1$
	static final String INHERITED = "Inherited:"; //$NON-NLS-1$
	static final String RUNTIME_SETTABLE = "Settable at runtime:"; //$NON-NLS-1$
	private static final String EXPRESSION_TYPE = "Expression type:";
	private static final String CONTEXT = "Context:";
	private static final String CONTEXT2 = "Expression Context:";
	private static final String ELEMENT_NAME = "Element Name:";
	private static final String USER_VISIBLE = "User Visible:";
	private static final String DEFAULT_STYLE = "Default Style:";
	private static final String REQUIRED = "Required:";
	private static final String DESIGNER_VISIBLE = "Visible in BIRT designer:";

	private void parsePropertySummaryBlock(SpecProperty prop) {
		int token = getToken();
		while (isPara(token, "MsoBodyText")) //$NON-NLS-1$
		{
			String line = getTextTo("/p", true); //$NON-NLS-1$
			if (startsWith(line, DISPLAY_NAME)) {
				prop.displayName = getTail(line, DISPLAY_NAME);
			} else if (startsWith(line, ROM_TYPE)) {
				prop.romType = getTail(line, ROM_TYPE);
			} else if (startsWith(line, REQUIRED)) {
				String tail = getTail(line, REQUIRED);
				prop.required = (tail.indexOf("Yes") != -1) ? SpecObject.TRI_TRUE : SpecObject.TRI_FALSE;
			} else if (line.equals("Required.")) {
				prop.required = SpecObject.TRI_TRUE;
			} else if (startsWith(line, "Name")) {
				// Ignore
			} else if (startsWith(line, "Type")) {
				// Ignore
			} else if (startsWith(line, JS_TYPE)) {
				String tail = getTail(line, JS_TYPE);
				prop.isArray = SpecObject.TRI_FALSE;
				if (tail.startsWith("Array of ")) //$NON-NLS-1$
				{
					tail = tail.substring(9);
					prop.isArray = SpecObject.TRI_TRUE;
					if (tail.endsWith("objects")) //$NON-NLS-1$
					{
						tail = tail.substring(0, tail.length() - 8);
					}
				}
				prop.jsType = tail.trim();
			} else if (startsWith(line, DEFAULT_VALUE)) {
				String tail = getTail(line, DEFAULT_VALUE);
				/*
				 * if ( tail.equals( "False" ) || tail.equals( "True" ) ) prop.defaultValue =
				 * tail.toLowerCase( ); else
				 */ if (!tail.startsWith("None") && !tail.equalsIgnoreCase("See Description.")) {
					prop.defaultValue = tail.toLowerCase();
				}
			} else if (startsWith(line, EXPRESSION_TYPE)) {
				prop.exprType = getObjName(getTail(line, EXPRESSION_TYPE));
			} else if (startsWith(line, INHERITED)) {
				String tail = getTail(line, INHERITED);
				prop.inherited = (tail.indexOf("Yes") != -1) ? SpecObject.TRI_TRUE : SpecObject.TRI_FALSE;
			} else if (startsWith(line, RUNTIME_SETTABLE)) {
				String tail = getTail(line, RUNTIME_SETTABLE);
				prop.runtimeSettable = (tail.indexOf("Yes") != -1) ? SpecObject.TRI_TRUE : SpecObject.TRI_FALSE;
			} else if (startsWith(line, USER_VISIBLE)) {
				String tail = getTail(line, USER_VISIBLE);
				prop.runtimeSettable = (tail.indexOf("Yes") != -1) ? SpecObject.TRI_FALSE : SpecObject.TRI_TRUE;
			} else if (startsWith(line, DESIGNER_VISIBLE)) {
				String tail = getTail(line, DESIGNER_VISIBLE);
				prop.runtimeSettable = (tail.indexOf("Yes") != -1) ? SpecObject.TRI_FALSE : SpecObject.TRI_TRUE;
			} else if (startsWith(line, AVAILABILITY)) {
				String tail = getTail(line, AVAILABILITY);
				prop.since = getSince(tail);
			} else if (startsWith(line, CONTEXT2)) {
				prop.exprContext = getTail(line, CONTEXT2);
			} else {
				warning(element, prop, "Unrecognized summary header: " + line);
				prop.description = append(prop.description, "\n<br>\n" + line);
			}
			token = getToken();
		}
		pushToken(token);
	}

	private void parseChoices(SpecProperty prop) {
		skipTo("table");
		int token = getToken();
		if (isElement(token, "thead")) {
			skipTo("/thead");
		} else {
			skipTo("/tr");
		}
		String descrip = null;
		for (;;) {
			token = getToken();
			if (token != HTMLParser.ELEMENT) {
				continue;
			}
			String tag = parser.getTokenText().toLowerCase();
			if (tag.equals("/table")) {
				break;
			}
			if (!tag.equals("tr")) {
				error(element, prop, "Unexpected tag in table: " + tag);
				break;
			}
			String value = getCell(prop, null, true);
			if (value == null) {
				break;
			}
			SpecChoice choice = new SpecChoice();
			choice.displayName = value;
			value = getCell(prop, null, true);
			if (value == null) {
				break;
			}
			choice.name = value;
			descrip = getCell(prop, descrip, false);
			if (descrip == null) {
				break;
			}
			choice.description = descrip;
			prop.addChoice(choice);
			token = getToken();
			tag = parser.getTokenText().toLowerCase();
			if (!tag.equals("/tr")) {
				error(element, prop, "Unexpected tag in table: " + tag);
				break;
			}
		}
		skipBlock();
	}

	private String getCell(SpecProperty prop, String prev, boolean textOnly) {
		int token = getToken();
		if (token != HTMLParser.ELEMENT) {
			error(element, prop, "Unexpected token in table");
			return null;
		}
		String tag = parser.getTokenText().toLowerCase();
		if (tag.equals("/tr") || tag.equals("/table") || tag.equals("tr")) {
			pushToken(token);
			return prev;
		}
		if (!tag.equals("td")) {
			error(element, prop, "Unexpected tag in table: " + tag);
			return null;
		}
		StringBuilder text = new StringBuilder();
		for (;;) {
			token = getToken();
			if (token == HTMLParser.ELEMENT && parser.getTokenText().equals("p")) {
				continue;
			} else if (token == HTMLParser.ELEMENT && parser.getTokenText().equals("/td")) {
				break;
			} else if (token == HTMLParser.ELEMENT) {
				if (!textOnly) {
					text.append(parser.getFullElement());
				}
			} else if (token == HTMLParser.TEXT) {
				text.append(parser.getTokenText());
			} else {
				error(element, prop, "Unexpected token in table");
				return null;
			}
		}
		return text.toString().trim();
	}

	private void parseMethod(String methodName) {
		SpecMethod method = element.getMethod(methodName);
		if (method == null) {
			method = new SpecMethod();
			method.name = methodName;
			element.addMethod(method);
		}
		int token = getToken();
		if (isPara(token, "MsoBodyText")) //$NON-NLS-1$
		{
			method.summary = getTextTo("/p", true); //$NON-NLS-1$
			token = getToken();
		}
		for (;;) {
			if (!isPara(token, "ManpageTitle")) //$NON-NLS-1$
			{
				pushToken(token);
				return;
			}
			String heading = getTextTo("/p", true); //$NON-NLS-1$
			if (heading.equalsIgnoreCase("Synopsis")) { //$NON-NLS-1$
				skipBlock();
			} else if (heading.equalsIgnoreCase("Summary")) //$NON-NLS-1$
			{
				parseMethodSummaryBlock(method);
			} else if (heading.equalsIgnoreCase("Description")) //$NON-NLS-1$
			{
				method.description = append(method.description, parseTextBlock());
			} else if (heading.equalsIgnoreCase("See Also")) //$NON-NLS-1$
			{
				method.seeAlso = parseTextBlock();
			} else if (heading.equalsIgnoreCase("Returns")) //$NON-NLS-1$
			{
				method.returns = parseTextBlock();
			} else if (heading.equalsIgnoreCase("Arguments")) //$NON-NLS-1$
			{
				String args = parseTextBlock();
				if (!args.equalsIgnoreCase("None")) {
					warning(element, method, "Arguments not yet supported");
				}
			} else {
				warning(element, method, "Unrecognized method header: " + heading);
				method.description = append(method.description,
						"\n\n<h4>" + heading + "</h4>\n\n" + copyBlock() + "\n");
			}
			token = getToken();
		}
	}

	private void parseMethodSummaryBlock(SpecMethod method) {
		int token = getToken();
		while (isPara(token, "MsoBodyText")) //$NON-NLS-1$
		{
			String line = getTextTo("/p", true); //$NON-NLS-1$
			if (startsWith(line, DISPLAY_NAME)) {
				method.displayName = getTail(line, DISPLAY_NAME);
			} else if (startsWith(line, AVAILABILITY)) {
				String tail = getTail(line, AVAILABILITY);
				method.since = getSince(tail);
			} else if (startsWith(line, CONTEXT)) {
				method.context = getTail(line, CONTEXT);
			} else {
				warning(element, method, "Unrecognized method summary header: " + line);
			}
			token = getToken();
		}
		pushToken(token);
	}

	private void parseSlot(String slotName) {
		SpecSlot slot = element.getSlot(slotName);
		if (slot == null) {
			slot = new SpecSlot();
			slot.name = slotName;
			element.addSlot(slot);
		}
		int token = getToken();
		if (isPara(token, "MsoBodyText")) //$NON-NLS-1$
		{
			slot.summary = getTextTo("/p", true); //$NON-NLS-1$
			token = getToken();
		}
		for (;;) {
			if (!isPara(token, "ManpageTitle")) //$NON-NLS-1$
			{
				pushToken(token);
				return;
			}
			String heading = getTextTo("/p", true); //$NON-NLS-1$
			if (heading.equalsIgnoreCase("Summary")) //$NON-NLS-1$
			{
				parseSlotSummaryBlock(slot);
			} else if (heading.equalsIgnoreCase("Description")) //$NON-NLS-1$
			{
				slot.description = append(slot.description, parseTextBlock());
			} else if (heading.equalsIgnoreCase("Contents")) //$NON-NLS-1$
			{
				slot.contents = parseTextBlock();
			} else if (heading.equalsIgnoreCase("XML Summary")) //$NON-NLS-1$
			{
				parseSlotSummaryBlock(slot);
			} else if (heading.equalsIgnoreCase("See Also")) //$NON-NLS-1$
			{
				slot.seeAlso = parseTextBlock();
			} else {
				warning(element, slot, "Unrecognized method header: " + heading);
				slot.description = append(slot.description, "\n\n<h4>" + heading + "</h4>\n\n" + copyBlock() + "\n");
			}
			token = getToken();
		}
	}

	private void parseSlotSummaryBlock(SpecSlot slot) {
		int token = getToken();
		while (isPara(token, "MsoBodyText")) //$NON-NLS-1$
		{
			String line = getTextTo("/p", true); //$NON-NLS-1$
			if (startsWith(line, DISPLAY_NAME)) {
				slot.displayName = getTail(line, DISPLAY_NAME);
			} else if (startsWith(line, AVAILABILITY)) {
				String tail = getTail(line, AVAILABILITY);
				slot.since = getSince(tail);
			} else if (startsWith(line, CARDINALITY)) {
				String tail = getTail(line, CARDINALITY);
				slot.cardinality = tail.equalsIgnoreCase("Multiple") ? SpecSlot.MULTIPLE : SpecSlot.SINGLE;
			} else if (startsWith(line, ELEMENT_NAME)) {
				slot.xmlElement = getTail(line, ELEMENT_NAME);
			} else if (startsWith(line, XML_ELEMENT_NAME)) {
				slot.xmlElement = getTail(line, XML_ELEMENT_NAME);
			} else if (startsWith(line, DEFAULT_STYLE)) {
				slot.styleNames = getTail(line, DEFAULT_STYLE);
			} else {
				warning(element, slot, "Unrecognized method summary header: " + line);
			}
			token = getToken();
		}
		pushToken(token);
	}

}
