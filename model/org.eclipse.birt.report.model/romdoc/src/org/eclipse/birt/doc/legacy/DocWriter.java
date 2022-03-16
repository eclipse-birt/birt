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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;

public class DocWriter {
	SpecElement element;
	PrintStream writer;
	PrintStream index;

	public DocWriter() {
	}

	public void startIndex() throws IOException {
		String fileName = "docs/index.html";
		index = new PrintStream(new FileOutputStream(fileName));

		write(index, "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.0 transitional//EN\">\n");
		write(index, "<html>\n<head>\n<title>Element Index</title>\n");
		write(index, "<link rel=\"stylesheet\" href=\"../style/style.css\" type=\"text/css\"/>\n");
		write(index, "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">\n");
		write(index, "</head>\n<body>\n");
	}

	public void startElementIndex() {
		write(index, "<h1>Elements</h1>\n<ul>\n");
	}

	public void startStructIndex() {
		write(index, "</ul>\n\n<h1>Structures</h1>\n<ul>\n");
	}

	public void endIndex() throws IOException {
		write(index, "</ul>\n</body>\n</html>\n");
		index.close();
	}

	public String getDir() {
		if (element.type == SpecElement.ELEMENT) {
			return "elements";
		}
		return "structs";
	}

	public void write(SpecElement el) throws DocException, IOException {
		element = el;
		if (element.name == null || element.name.length() == 0) {
			throw new DocException("Missing file name"); //$NON-NLS-1$
		}
		String fileName = "docs/" + getDir() + "/" + element.name + ".html";
		writer = new PrintStream(new FileOutputStream(fileName));
		writeHeader();
		writeElement();
		writeProperties();
		writeMethods();
		writeSlots();
		writeFooter();
		writer.close();
	}

	private void write(String s) throws IOException {
		write(writer, s);
	}

	private void write(PrintStream out, String s) {
		if (isBlank(s)) {
			return;
		}
		if (s.indexOf('\n') == 0) {
			out.print(s);
			return;
		}
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == '\n') {
				out.println("");
			} else {
				out.print(c);
			}
		}
	}

	private void writeln(String s) throws IOException {
		if (isBlank(s)) {
			return;
		}

		write(s);
		writer.println("");
	}

	private boolean isBlank(String s) {
		return s == null || s.trim().length() == 0;
	}

	private void writePara(String s) throws IOException {
		if (isBlank(s)) {
			return;
		}
		write("<p>");
		write(s);
		writeln("</p>\n");
	}

	private void writeHeader() throws IOException {
		writeln("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.0 transitional//EN\">");
		write("<html>\n<head>\n<title>");

		// Display name is encoded as the title.

		String title = element.displayName;
		if (isBlank(element.displayName)) {
			title = element.name;
		}
		write(title);
		writeln("</title>");
		writeln("<link rel=\"stylesheet\" href=\"../style/style.css\" type=\"text/css\"/>");
		writeln("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">");
		writeln("</head>\n<body>");

		// Add index entry.

		write(index, "<li><a href=\"" + getDir() + "/");
		write(index, element.name);
		write(index, ".html\">");
		write(index, title);
		write(index, "</a></li>\n");
	}

	private void writeElement() throws IOException {
		// Element name appears as H1 element

		write("<h1>");
		write(element.name);
		writeln("</h1>\n");

		// Summary appears as text after H1 before next Hn

		writePara(element.summary);

		// Description appears as h3 block with text Description

		writeln("<h3>Description</h3>\n");
		writePara(element.description);

		if (element.type == SpecElement.ELEMENT) {
			write("<h3>XML Summary</h3>\n");
			writePara(element.xmlSummary);
			writeInheritedProperties();
		}

		writeIssues(element.issues);

		writeln("<h3>See Also</h3>\n");
		writePara(element.seeAlso);
	}

	public void writeIssues(String issues) throws IOException {
		if (issues == null) {
			return;
		}
		write("<h3>Issues</h3>\n");
		write(issues);
	}

	private void writeInheritedProperties() throws IOException {
		if (element.inheritedProperties.isEmpty()) {
			return;
		}

		writeln("\n<h3>Inherited Properties</h3>\n\n<dl>");
		for (int i = 0; i < element.inheritedProperties.size(); i++) {
			writeInheritedProperty((SpecInheritedProperty) element.inheritedProperties.get(i));
		}
		write("</dl>\n\n");
	}

	private void writeInheritedProperty(SpecInheritedProperty prop) throws IOException {
		write("<dt class=\"inherited-property\">");
		write(prop.name);
		write("</dt>\n<dd>");
		write(prop.description);
		writeln("</dd>");
	}

	private void writeProperties() throws IOException {
		for (int i = 0; i < element.properties.size(); i++) {
			writeProperty((SpecProperty) element.properties.get(i));
		}
	}

	private void writeProperty(SpecProperty prop) throws IOException {
		write("<h2 class=\"property\">");
		write(prop.name);
		writeln("</h2>\n");
		writePara(prop.summary);

		writeln("<h3>Summary</h3>");
		writePara(prop.shortDescrip);

		writeChoices(prop);

		writeln("<h3>Description</h3>");
		writePara(prop.description);

		writeIssues(prop.issues);

		write("<h3>See Also</h3>\n");
		writePara(prop.seeAlso);
	}

	private void writeChoices(SpecProperty prop) throws IOException {
		if (prop.choices.isEmpty()) {
			return;
		}
		writeln("<h3>Choices</h3>\n<ul>");
		Iterator iter = prop.choices.iterator();
		while (iter.hasNext()) {
			SpecChoice choice = (SpecChoice) iter.next();
			write("<li>");
			write(choice.name);
			write(": ");
			write(choice.description);
			writeln("</li>\n");
		}
		writeln("</ul>");
	}

	private void writeMethods() throws IOException {
		for (int i = 0; i < element.methods.size(); i++) {
			writeMethod((SpecMethod) element.methods.get(i));
		}
	}

	private void writeMethod(SpecMethod method) throws IOException {
		write("<h2 class=\"method\">");
		write(method.name);
		writeln("</h2>\n");
		writePara(method.summary);

		writeln("<h3>Summary</h3>");
		writePara(method.shortDescrip);

		writeln("<h3>Description</h3>");
		writePara(method.description);

		writeIssues(method.issues);

		write("<h3>See Also</h3>\n");
		writePara(method.seeAlso);
	}

	private void writeSlots() throws IOException {
		for (int i = 0; i < element.slots.size(); i++) {
			writeSlot((SpecSlot) element.slots.get(i));
		}
	}

	private void writeSlot(SpecSlot slot) throws IOException {
		write("<h2 class=\"slot\">");
		write(slot.name);
		writeln("</h2>\n");
		writePara(slot.summary);

		writeln("<h3>Summary</h3>");
		writePara(slot.shortDescrip);

		writeln("<h3>Description</h3>");
		writePara(slot.description);

		writeln("<h3>Contents</h3>");
		writePara(slot.contents);

		writeIssues(slot.issues);

		write("<h3>See Also</h3>\n");
		writeln(slot.seeAlso);
	}

	private void writeFooter() throws IOException {
		writeln("</body>\n</html>");
	}

	static class DocException extends Exception {
		/**
		 *
		 */
		private static final long serialVersionUID = -1769976375996415539L;

		DocException(String msg) {
			super(msg);
		}
	}
}
