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

package org.eclipse.birt.doc.romdoc;

import java.io.FileNotFoundException;

import org.eclipse.birt.doc.util.HTMLParser;
import org.eclipse.birt.doc.util.HtmlDocReader;

public class DocParser extends HtmlDocReader {

	Generator generator;
	DocComposite docObj;

	public DocParser(Generator gen) {
		generator = gen;
	}

	public DocElement getElement() {
		return (DocElement) docObj;
	}

	public void parse(DocComposite e) throws ParseException {
		docObj = e;
		String dir = e.isElement() ? "element" : "struct"; //$NON-NLS-1$//$NON-NLS-2$
		String templateDir = generator.templateDir;
		String fileName = templateDir + "/" + dir + "s/" + docObj.getName() + ".html";

		try {
			parser.open(fileName);
		} catch (FileNotFoundException e1) {
			System.out.println("No documentation file for " + dir + " " + docObj.getName());
			return;
		}

		parseElement();
		parseMembers();
	}

	/**
	 * @throws ParseException
	 * 
	 */
	private void parseElement() throws ParseException {
		skipTo("/h1");

		// Summary is first block of text after h1.

		docObj.setSummary(stripPara(copySection()));

		// Parse sections for this element. Sections are given by
		// h3 headings. The next h2 indicates the start of a member.

		for (;;) {
			int token = getToken();
			if (token == HTMLParser.EOF)
				return;
			if (isElement(token, "h2") || isElement(token, "/body") || isElement(token, "/html")) {
				pushToken(token);
				return;
			}
			assert (isElement(token, "h3"));
			token = getToken();
			if (isElement(token, "/h3")) {
				warning("Blank section header");
			} else if (token != HTMLParser.TEXT) {
				String msg = "Unexpected element inside section header";
				error(msg);
				throw new ParseException(msg);
			}

			String header = parser.getTokenText();
			token = getToken();
			if (!isElement(token, "/h3"))
				pushToken(token);
			if (header.equalsIgnoreCase("Description")) {
				docObj.setDescription(copySection());
			} else if (header.equalsIgnoreCase("XML Summary")) {
				if (docObj.isElement())
					getElement().setXmlSummary(copySection());
			} else if (header.equalsIgnoreCase("See Also")) {
				docObj.setSeeAlso(copySection());
			} else if (header.equalsIgnoreCase("Inherited Properties")) {
				if (docObj.isElement())
					parseInheritedProperties();
				else
					copySection();
			} else if (header.equalsIgnoreCase("Issues")) {
				// Issues are private to the implementation. Ignore them.

				copySection();
			} else {
				warning("Unexpected Element header: " + header);
				copySection();
			}
		}
	}

	private void parseInheritedProperties() throws ParseException {
		// Skip over the start if the list, if any.

		int token;
		for (;;) {
			token = getToken();
			if (isBlockEnd(token)) {
				pushToken(token);
				return;
			}
			if (isElement(token, "dl"))
				break;
		}

		// Read list contents.

		for (;;) {
			token = getToken();
			if (isBlockEnd(token) || isElement(token, "/dl")) {
				pushToken(token);
				break;
			}

			if (!isElement(token, "dt")) {
				String msg = "Unexpected element in inherited properties list";
				error(msg);
				throw new ParseException(msg);
			}

			// Get the property name.

			StringBuffer text = new StringBuffer();
			for (;;) {
				token = getToken();
				if (isBlockEnd(token) || isElement(token, "/dl") || isElement(token, "dd") || isElement(token, "dt")) {
					pushToken(token);
					break;
				}
				if (isElement(token, "/dt"))
					break;
				if (token == HTMLParser.TEXT)
					text.append(parser.getTokenText());
				else if (token == HTMLParser.ELEMENT)
					text.append(parser.getFullElement());
			}

			// Add the property.

			String propName = text.toString();
			DocInheritedProperty prop = new DocInheritedProperty();
			prop.setName(stripPara(propName));
			if (prop.isDefined(getElement()))
				getElement().addInheritedPropertyNote(prop);
			else {
				error("Inherited property " + prop.getName() + " is not defined in rom.def");
			}

			// Get the property description.

			token = getToken();
			if (!isElement(token, "dd")) {
				pushToken(token);
				continue;
			}

			text = new StringBuffer();
			for (;;) {
				token = getToken();
				if (isBlockEnd(token) || isElement(token, "/dl") || isElement(token, "dt")) {
					pushToken(token);
					break;
				}
				if (isElement(token, "/dd"))
					break;
				if (token == HTMLParser.TEXT)
					text.append(parser.getTokenText());
				else if (token == HTMLParser.ELEMENT)
					text.append(parser.getFullElement());
			}
			prop.setDescription(stripPara(text.toString()));
		}

		// Skip list end.

		token = getToken();
		if (!isElement(token, "/dl"))
			pushToken(token);
	}

	// Copy the contents of a section up to the next section heading at
	// the H3 level or higher. Also stop at end of body, html or file.

	public String getTail(String header, String suffix) {
		return header.substring(0, header.length() - suffix.length()).trim();
	}

	/**
	 * @throws ParseException
	 * 
	 */
	private void parseMembers() throws ParseException {
		for (;;) {
			int token = getToken();
			if (isElement(token, "/body") || isElement(token, "/html")) {
				pushToken(token);
				return;
			}
			if (token != HTMLParser.ELEMENT || !isElement(token, "h2")) {
				String msg = "Unexpected token while reading members.";
				error(msg);
				throw new ParseException(msg);
			}
			String type = parser.getAttrib("class");

			token = getToken();
			if (token != HTMLParser.TEXT) {
				String msg = "Missing member header.";
				error(msg);
				throw new ParseException(msg);
			}
			String header = parser.getTokenText();
			if (type == null) {
				String msg = "Missing class to identify type: " + header;
				error(msg);
				throw new ParseException(msg);
			}
			token = getToken();
			if (!isElement(token, "/h2"))
				pushToken(token);
			if (type.equals("property")) {
				parseProperty(header);
			} else if (docObj.isElement() && type.equals("method")) {
				parseMethod(header);
			} else if (docObj.isElement() && type.equals("slot")) {
				parseSlot(header);
			} else {
				String msg = "Unrecognized member class: " + header;
				error(msg);
				throw new ParseException(msg);
			}
		}
	}

	private String parseHeader() throws ParseException {
		for (;;) {
			int token = getToken();
			if (token == HTMLParser.EOF)
				return null;
			if (isElement(token, "h2") || isElement(token, "/body") || isElement(token, "/html")) {
				pushToken(token);
				return null;
			}
			assert (isElement(token, "h3"));
			token = getToken();
			if (isElement(token, "/h3")) {
				warning("Blank section header");
				pushToken(token);
			} else if (token != HTMLParser.TEXT) {
				String msg = "Unexpected element inside section header";
				error(msg);
				throw new ParseException(msg);
			}

			String header = parser.getTokenText();
			token = getToken();
			if (!isElement(token, "/h3"))
				pushToken(token);
			if (header.equalsIgnoreCase("Issues")) {
				// Issues are private to the implementation. Ignore them.

				copySection();
			} else if (header.equalsIgnoreCase("Summary")) {
				// Summary section is temporary; used only during
				// conversion. Ignore it.

				copySection();
			} else
				return header;
		}
	}

	private void parseProperty(String name) throws ParseException {
		DocProperty prop = docObj.getProperty(name);
		if (prop == null) {
			error("Property " + name + " is not defined in rom.def.");
		}

		// Summary is first block of text after h1.

		String value = copySection();
		if (prop != null)
			prop.setSummary(stripPara(value));

		for (;;) {
			String header = parseHeader();
			if (header == null)
				break;

			if (header.equalsIgnoreCase("Description")) {
				value = copySection();
				if (prop != null)
					prop.setDescription(value);
			} else if (header.equalsIgnoreCase("Notes")) {
				parseNotes(prop);
			} else if (header.equalsIgnoreCase("Choices")) {
				parseChoices(prop);
			} else if (header.equalsIgnoreCase("See Also")) {
				value = copySection();
				if (prop != null)
					prop.setSeeAlso(value);
			} else {
				warning("Unexpected Property header: " + header);
				copySection();
			}
		}
	}

	private void parseNotes(DocProperty prop) {
		for (;;) {
			int token = getToken();
			if (isBlockEnd(token)) {
				pushToken(token);
				return;
			}
			if (!isElement(token, "p"))
				continue;
			String note = getTextTo("/p");
			int posn = note.indexOf(':');
			if (posn == -1)
				continue;
			String key = note.substring(0, posn);
			note = note.substring(posn + 1).trim();
			prop.addNote(key, note);
		}
	}

	private void parseChoices(DocProperty prop) throws ParseException {
		int token = getToken();
		if (isBlockEnd(token)) {
			pushToken(token);
			return;
		}
		if (!isElement(token, "ul")) {
			String msg = "Expected <ul> within Choices header.";
			error(msg);
			throw new ParseException(msg);
		}
		for (;;) {
			token = getToken();
			if (isElement(token, "/ul"))
				break;
			if (isBlockEnd(token)) {
				error("</ul> missing from Choices section");
				pushToken(token);
				break;
			}
			if (!isElement(token, "li")) {
				String msg = "Expecting <li> within <ul> in Choices section";
				error(msg);
				throw new ParseException(msg);
			}
			String line = getTextTo("/li");
			int posn = line.indexOf(':');
			if (posn == -1)
				continue;
			String name = line.substring(0, posn);
			String descrip = line.substring(posn + 1).trim();
			DocChoice choice = prop.findChoice(name);
			if (choice == null) {
				error("Choice " + name + " is not defined in rom.def");
			} else {
				choice.setDescription(descrip);
			}
		}
	}

	private void parseMethod(String name) throws ParseException {
		DocMethod method = getElement().getMethod(name);
		if (method == null) {
			error("Property " + name + " is not defined in rom.def.");
		}

		// Summary is first block of text after h1.

		String value = copySection();
		if (method != null)
			method.setSummary(stripPara(value));

		for (;;) {
			String header = parseHeader();
			if (header == null)
				break;

			if (header.equalsIgnoreCase("Description")) {
				value = copySection();
				if (method != null)
					method.setDescription(value);
			} else if (header.equalsIgnoreCase("Return")) {
				value = copySection();
				if (method != null)
					method.setReturnText(value);
			} else if (header.equalsIgnoreCase("See Also")) {
				value = copySection();
				if (method != null)
					method.setSeeAlso(value);
			} else {
				warning("Unexpected Method header: " + header);
				copySection();
			}
		}
	}

	private void parseSlot(String name) throws ParseException {
		DocSlot slot = getElement().getSlot(name);
		if (slot == null) {
			error("Slot " + name + " is not defined in rom.def.");
		}

		// Summary is first block of text after h1.

		String value = copySection();
		if (slot != null)
			slot.setSummary(stripPara(value));

		for (;;) {
			String header = parseHeader();
			if (header == null)
				break;

			if (header.equalsIgnoreCase("Description")) {
				value = copySection();
				if (slot != null)
					slot.setDescription(value);
			} else if (header.equalsIgnoreCase("See Also")) {
				value = copySection();
				if (slot != null)
					slot.setSeeAlso(value);
			} else if (header.equalsIgnoreCase("Contents")) {
				value = copySection();
				if (slot != null)
					slot.setContentInfo(value);
			} else {
				warning("Unexpected Slot header: " + header);
				copySection();
			}
		}
	}

	private String label() {
		return "[" + Integer.toString(parser.getLineNo()) + "] Element " + docObj.getName() + ": ";
	}

	private void warning(String msg) {
		System.out.print(label());
		System.out.println(msg);
	}

	private void error(String msg) {
		System.err.print(label());
		System.err.println(msg);
	}

	static class ParseException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ParseException(String msg) {
			super(msg);
		}
	}

}
