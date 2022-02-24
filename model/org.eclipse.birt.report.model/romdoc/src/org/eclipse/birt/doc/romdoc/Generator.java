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

package org.eclipse.birt.doc.romdoc;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.eclipse.birt.doc.romdoc.DocParser.ParseException;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.MetaDataParserException;
import org.eclipse.birt.report.model.metadata.MetaDataReader;
import org.eclipse.birt.report.model.metadata.PredefinedStyle;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.birt.report.model.metadata.StructureDefn;

public class Generator {

	ArrayList elements = new ArrayList();
	ArrayList structs = new ArrayList();
	PrintStream writer;
	PrintStream index;
	private static final String PROPERTY_NAME = "Property"; //$NON-NLS-1$
	private static final String METHOD_NAME = "Method"; //$NON-NLS-1$
	private static final String SLOT_NAME = "Slot"; //$NON-NLS-1$
	private String typeHeader;
	ArrayList propertyTypes = new ArrayList();

	String outputDir = "romdoc/gen"; //$NON-NLS-1$
	String templateDir = "romdoc/docs"; //$NON-NLS-1$

	public void generate() throws Exception {
		loadModel();
		createDocObjects();
		loadElementDocs();
		loadStructureDocs();
		loadTypeDoc();
		semanticCheck();
		writeDocs();
	}

	private void loadModel() throws MetaDataParserException {
		try {
			MetaDataReader.read(ReportDesign.class.getResourceAsStream("rom.def")); //$NON-NLS-1$
		} catch (MetaDataParserException e) {
			System.out.println("rom.def load failed."); //$NON-NLS-1$
			throw e;
		}
	}

	/**
	 * Sets the output folder for the generated rom documents. If not specified, rom
	 * documents will be generated under "gen" folder in the current classpath.
	 * 
	 * @param dir output foloder for the generated rom documents.
	 */

	public void setOutputDir(String dir) {
		this.outputDir = dir;
	}

	/**
	 * Sets the path of the folder that stores the template document. If not
	 * specified, a relative path "/romdoc/docs" will be used instead.
	 * 
	 * @param dir folder that stores the template document
	 */

	public void setTemplateDir(String dir) {
		this.templateDir = dir;
	}

	private void createDocObjects() {
		createElements();
		createStructures();
		createTypes();
	}

	private void createElements() {
		MetaDataDictionary dict = MetaDataDictionary.getInstance();
		Iterator iter = dict.getElements().iterator();
		while (iter.hasNext()) {
			ElementDefn defn = (ElementDefn) iter.next();
			DocElement element = new DocElement(defn);
			elements.add(element);
		}
		Collections.sort(elements, new DocComparator());
	}

	private void createStructures() {
		MetaDataDictionary dict = MetaDataDictionary.getInstance();
		Iterator iter = dict.getStructures().iterator();
		while (iter.hasNext()) {
			StructureDefn defn = (StructureDefn) iter.next();
			DocStructure struct = new DocStructure(defn);
			structs.add(struct);
		}
		Collections.sort(structs, new DocComparator());
	}

	private void createTypes() {
		MetaDataDictionary dict = MetaDataDictionary.getInstance();
		List list = dict.getPropertyTypes();
		Collections.sort(list, new TypeComparator());
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			PropertyType propType = (PropertyType) iter.next();
			DocPropertyType type = new DocPropertyType(propType);
			propertyTypes.add(type);
		}
	}

	private void loadElementDocs() {
		Iterator iter = elements.iterator();
		while (iter.hasNext()) {
			DocElement element = (DocElement) iter.next();
			DocParser parser = new DocParser(this);
			try {
				parser.parse(element);
			} catch (ParseException e) {
				System.err.println("Parse of document file for element " + //$NON-NLS-1$
						element.getName() + " failed."); //$NON-NLS-1$
			}
		}
	}

	private void loadStructureDocs() {
		Iterator iter = structs.iterator();
		while (iter.hasNext()) {
			DocStructure struct = (DocStructure) iter.next();
			DocParser parser = new DocParser(this);
			try {
				parser.parse(struct);
			} catch (ParseException e) {
				System.err.println("Parse of document file for element " + struct.getName() + " failed.");
			}
		}
	}

	private void loadTypeDoc() {
		DataTypeParser parser = new DataTypeParser(this);
		try {
			parser.parse();
		} catch (ParseException e) {
			System.err.println("Parse of property types file failed.");
		}
	}

	private void semanticCheck() {
		// TODO Auto-generated method stub

	}

	private void writeDocs() throws IOException {
		startIndex();
		writeElements();
		writeStructures();
		write(index, "<h1>Supporting Indexes</h1>\n<table class=\"summary-table\">\n");
		writeTypes();
		writeStyles();
		writePropertyIndex();
		writeInheritanceTable();
		write(index, "</table><br>\n\n");
		endIndex();
	}

	private void writeElements() throws IOException {
		write(index, "<h1>Elements</h1>\n<table class=\"summary-table\">\n");
		Iterator iter = elements.iterator();
		while (iter.hasNext()) {
			DocElement element = (DocElement) iter.next();
			try {
				write(element);
			} catch (IOException e) {
				System.out.println("Failed to write file for Element " + element.getName());
				throw e;
			}
		}
		write(index, "</table>\n\n");
	}

	private void write(String s) {
		write(writer, s);
	}

	private void write(PrintStream out, String s) {
		if (s == null)
			return;
		if (s.indexOf('\n') == -1) {
			out.print(s);
			return;
		}
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == '\n')
				out.println();
			else
				out.print(c);
		}
	}

	private void writeln(String s) {
		write(s);
		writer.println();
	}

	private boolean isBlank(String s) {
		return s == null || s.trim().length() == 0;
	}

	private void startIndex() throws IOException {
		File output = this.makeFile(null, "index.html");

		try {
			index = new PrintStream(new FileOutputStream(output));
		} catch (FileNotFoundException e) {
			System.err.println("Could not open the index.html file.");
			throw e;
		}

		write(index, "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.0 transitional//EN\">\n");
		write(index, "<html>\n<head>\n<title>BIRT ROM Documentation</title>\n");
		write(index, "<link rel=\"stylesheet\" href=\"style/style.css\" type=\"text/css\"/>\n");
		write(index, "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">\n");
		write(index, "</head>\n<body>\n");
		write(index, "<p class=\"title\">Eclipse BIRT Report Object Model (ROM)</p>\n");
		write(index, "<p class=\"subtitle\">Table of Contents</p>\n");
	}

	private void endIndex() {
		write(index, "</body>\n</html>\n");
		index.close();
	}

	/**
	 * Make a file under output folder.
	 * 
	 * @param relativeDir relative to the output folder. For example, if current
	 *                    output folder is "d:\romdoc",
	 *                    <code>makeFile( "structs", "action.html")</code> will
	 *                    return a File instance to "d:\romdoc\structs\action.html".
	 * @param fileName    name of the file
	 * @return File instance to the file.
	 * @throws IOException
	 */

	private File makeFile(String relativeDir, String fileName) throws IOException {
		if (relativeDir == null)
			relativeDir = ""; //$NON-NLS-1$

		File dir = new File(this.outputDir + "/" + relativeDir); //$NON-NLS-1$
		if (!dir.exists()) {
			dir.mkdir();
		}

		File output = new File(dir, fileName);
		if (!output.exists()) {
			output.createNewFile();
		}

		return output;
	}

	private void write(DocElement element) throws IOException {
		File output = makeFile("elements", element.getName() + ".html"); //$NON-NLS-1$ //$NON-NLS-2$

		writer = new PrintStream(new FileOutputStream(output));
		writeIndexEntry(element);
		writeHeader(element);
		writeElement(element);
		writeProperties(element);
		writeMethods(element);
		writeSlots(element);
		writeFooter();
		writer.close();
	}

	/**
	 * @param obj
	 */
	private void writeHeader(DocComposite obj) {
		writeln("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.0 transitional//EN\">");
		write("<html>\n<head>\n<title>");

		// Display name is encoded as the title.

		String title = obj.getDisplayName();
		if (isBlank(obj.getDisplayName()))
			title = obj.getName();
		write(title);
		writeln(" Element (Eclipse BIRT ROM Documentation)</title>");
		writeln("<link rel=\"stylesheet\" href=\"../style/style.css\" type=\"text/css\"/>");
		writeln("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">");
		writeln("</head>\n<body>");
		writeln("<p class=\"title\">Eclipse BIRT Report Object Model (ROM)</p>");
		write("<p class=\"subtitle\">");
		write(obj.getName());
		if (obj.isElement())
			write(" Element");
		else
			write(" Structure");
		writeln("</p>");
	}

	private void writeIndexEntry(DocComposite obj) {
		write(index, "<tr><td><a href=\"");
		if (obj.isElement())
			write(index, "elements/");
		else
			write(index, "structs/");
		write(index, obj.getName());
		write(index, ".html\">");
		write(index, obj.getName());
		write(index, "</a></td>\n<td>");
		write(index, obj.getSummary());
		write(index, "</td></tr>\n");
	}

	private void writeElement(DocElement element) {
		// Element name appears as H1 element

		write("<h1>Element Overview</h1>\n");
		writeSectionBody(element.getSummary());

		writeDetails(element);
		writePropertySummary(element);
		writeMethodSummary(element);
		writeSlotSummary(element);
		writeInheritedProperties(element);
		writeStyleProperties(element);
		writeInheritedMethods(element);
		writeInheritedSlots(element);

		writeSection("Description", element.getDescription());
		writeSection("XML Summary", element.getXmlSummary());
		writeSection("See Also", element.getSeeAlso());
	}

	private void writeSection(String title, String body) {
		if (isBlank(body))
			return;
		writeSectionHeader(title);
		writeSectionBody(body);
	}

	private void writeSectionBody(String body) {
		if (isBlank(body))
			return;
		writeln("<div class=\"section-text\">");
		write(body);
		writeln("</div>");
	}

	private void writeSectionHeader(String title) {
		write("<h3>");
		write(title);
		writeln("</h3>\n");
	}

	private void writeDetails(DocElement element) {
		writeSectionHeader("Details");
		startDetailsTable();
		detailRow("Display Name", element.getDisplayName());
		detailRow("Since", element.getSince());
		detailRow("XML Element", "<code>" + element.getXmlElement() + "</code>");
		detailRow("Extends", element.getExtends());
		detailRow("Extendable", element.getExtendable());
		detailRow("Abstract", element.getAbstract());
		detailRow("Name Space", element.getNameSpace());
		detailRow("Name Requirement", element.getNameRequirement());
		detailRow("Allows User Properties", element.getUserProperties());
		detailRow("Has Style", element.getHasStyle());
		if (element.hasStyle()) {
			detailRow("Default Style", element.getStyle());
		}
		endDetailsTable();
	}

	private void startSummaryTable(String title) {
		// writeln( "<table class=\"summary-table\">" );
		// write( "<thead><tr><td colspan=2>" );
		// write( title );
		// writeln( " Summary</td></tr></thead>" );
		writeSectionHeader(title + " Summary");
		writeln("<dl class=\"section-text\">");
	}

	private void summaryTableRow(String type, String name, String summary) {
		// write( "<tr><td><a href=\"#" );
		// write( getTagName( type, name ) );
		// write( "\">" );
		// write( name );
		// writeln( "</a></td><td>" );
		// write( summary );
		// writeln( "\n</td></tr>" );
		write("<dt><a href=\"#");
		write(getTagName(type, name));
		write("\">");
		write(name);
		write("</a></dt>\n<dd>");
		write(summary);
		writeln("</dd>");
	}

	private void finishSummaryTable() {
		// writeln( "</table>\n<br>" );
		writeln("</dl>\n");
	}

	private void writePropertySummary(DocComposite obj) {
		if (!obj.hasProperties())
			return;

		startSummaryTable(PROPERTY_NAME);
		Iterator iter = obj.getProperties().iterator();
		while (iter.hasNext()) {
			DocProperty prop = (DocProperty) iter.next();
			summaryTableRow(PROPERTY_NAME, prop.getName(), prop.getSummary());
		}
		finishSummaryTable();
	}

	private void writeMethodSummary(DocElement element) {
		if (!element.hasMethods())
			return;

		startSummaryTable(METHOD_NAME);
		Iterator iter = element.getMethods().iterator();
		while (iter.hasNext()) {
			DocMethod method = (DocMethod) iter.next();
			summaryTableRow(METHOD_NAME, method.getName(), method.getSummary());
		}
		finishSummaryTable();
	}

	private void writeSlotSummary(DocElement element) {
		if (!element.hasSlots())
			return;

		startSummaryTable(SLOT_NAME);
		Iterator iter = element.getSlots().iterator();
		while (iter.hasNext()) {
			DocSlot slot = (DocSlot) iter.next();
			summaryTableRow(SLOT_NAME, slot.getName(), slot.getSummary());
		}
		finishSummaryTable();
	}

	private String makeMemberReference(String dir, String objName, String prefix, String name) {
		StringBuffer link = new StringBuffer();
		link.append("<a href=\"");
		if (dir != null) {
			link.append(dir);
			link.append("/");
		}
		link.append(objName);
		link.append(".html#");
		link.append(getTagName(prefix, name));
		link.append("\">");
		link.append(name);
		link.append("</a>");
		return link.toString();
	}

	public static String getTagName(String prefix, String name) {
		return prefix + "-" + name;
	}

	private void writeInheritedProperties(DocElement element) {
		List props = element.getInheritedProperties();
		if (props.isEmpty())
			return;

		writeSectionHeader("Inherited Properties");
		writeln("<p class=\"section-text\">");
		Iterator iter = props.iterator();
		while (iter.hasNext()) {
			PropertyDefn prop = (PropertyDefn) iter.next();
			write(makeMemberReference(null, prop.definedBy().getName(), PROPERTY_NAME, prop.getName()));
			if (iter.hasNext())
				writeln(", ");
		}
		writeln("\n</p>");

		props = element.getInheritedPropertyNotes();
		if (props.isEmpty())
			return;

		writeSectionHeader("Inherited Property Notes");
		writeln("<dl class=\"section-text\">");
		iter = props.iterator();
		while (iter.hasNext()) {
			DocInheritedProperty prop = (DocInheritedProperty) iter.next();
			if (prop.isReserved(element))
				continue;
			String baseElement = element.getDefiningElement(prop.getName());
			write("<dt>");
			write(makeMemberReference(null, baseElement, PROPERTY_NAME, prop.getName()));
			write("</dt>\n<dd>");
			write(prop.getDescription());
			writeln("</dd>");
		}
		finishSummaryTable();
	}

	private void writeStyleProperties(DocElement element) {
		List props = element.getStyleProperties();
		if (props.isEmpty())
			return;

		writeSectionHeader("Style Properties");
		writeln("<p class=\"section-text\">");
		Iterator iter = props.iterator();
		while (iter.hasNext()) {
			PropertyDefn prop = (PropertyDefn) iter.next();
			write(makeMemberReference(null, prop.definedBy().getName(), PROPERTY_NAME, prop.getName()));
			if (iter.hasNext())
				writeln(", ");
		}
		writeln("\n</p>");
	}

	private void writeInheritedMethods(DocElement element) {
		List props = element.getInheritedMethods();
		if (props.isEmpty())
			return;

		writeSectionHeader("Inherited Methods");
		writeln("<p class=\"section-text\">");
		Iterator iter = props.iterator();
		while (iter.hasNext()) {
			PropertyDefn method = (PropertyDefn) iter.next();
			write(makeMemberReference(null, method.definedBy().getName(), METHOD_NAME, method.getName()));
			if (iter.hasNext())
				writeln(", ");
		}
		writeln("</p>");
	}

	private void writeInheritedSlots(DocElement element) {
		// Not supported in Release 1.0
	}

	/**
	 * @param obj
	 */
	private void writeProperties(DocComposite obj) {
		if (!obj.hasProperties())
			return;

		writeln("<h1>Property Detail</h1>\n");
		Iterator iter = obj.getProperties().iterator();
		while (iter.hasNext()) {
			DocProperty prop = (DocProperty) iter.next();
			writePropertyDetail(obj, prop);
			if (iter.hasNext())
				writeln("\n<hr>");
		}
	}

	private void startDetail(String type, String name, String summary) {
		write("<h2><a name=\"");
		write(getTagName(type, name));
		write("\">");
		write(name);
		write(" ");
		write(type);
		writeln("</a></h2>\n");
		write("<p class=\"section-text\">");
		write(summary);
		writeln("</p>");
	}

	private void startDetailsTable() {
		writeln("<table class=\"detail-table\">");
	}

	private void detailRow(String label, String value) {
		write("<tr><td>");
		write(label);
		writeln(":</td>");
		write("<td>");
		write(value);
		writeln("</td></tr>");
	}

	private void endDetailsTable() {
		writeln("</table>\n");
	}

	/**
	 * @param prop
	 */
	private void writePropertyDetail(DocComposite obj, DocProperty prop) {
		startDetail(PROPERTY_NAME, prop.getName(), prop.getSummary());

		writeln("<h3>Details</h3>\n");

		startDetailsTable();
		detailRow("Type", prop.getType());
		if (prop.isExpression()) {
			detailRow("Context", prop.getContext());
			detailRow("Expression Type", prop.getReturnType());
		}
		detailRow("Since", prop.getSince());
		detailRow("Required", prop.getRequired());
		detailRow("Display Name", prop.getDisplayName());
		detailRow("JavaScript Type", prop.getJSType());
		detailRow("Default Value", prop.getDefaultValue());
		if (obj.isElement()) {
			detailRow("Inherited", prop.getInherited());
		}
		detailRow("Runtime Settable", prop.getRuntimeSettable());
		if (obj.isElement()) {
			DocElement element = (DocElement) obj;
			detailRow("Property Sheet Visibility", prop.getVisibility(element));
			detailRow("Property Sheet Group", prop.getGroup());
		}
		endDetailsTable();

		writeChoices(prop);
		writeSection("Description", prop.getDescription());
		writeSection("See Also", prop.getSeeAlso());
	}

	private void writeChoices(DocProperty prop) {
		if (!prop.hasChoices())
			return;

		writeln("<h3>Choices</h3>\n");
		writeln("<table class=\"section-table\">");
		writeln("<thead><tr><td>Name</td><td>Display Name</td>");
		writeln("<td>Value</td><td>Description</td></tr></thead>");
		writeln("<tbody>");
		Iterator iter = prop.getChoices().iterator();
		while (iter.hasNext()) {
			DocChoice choice = (DocChoice) iter.next();
			write("<tr><td>");
			write(choice.getName());
			write("</td>\n<td>");
			write(choice.getDisplayName());
			write("</td>\n<td>");
			write(choice.getValue());
			write("</td>\n<td>");
			write(choice.getDescription());
			writeln("</td></tr>");
		}
		writeln("</tbody></table><br>");
	}

	/**
	 * @param element
	 */
	private void writeMethods(DocElement element) {
		if (!element.hasMethods())
			return;

		writeln("<h1>Method Detail</h1>\n");
		Iterator iter = element.getMethods().iterator();
		while (iter.hasNext()) {
			DocMethod method = (DocMethod) iter.next();
			writeMethodDetail(method);
			if (iter.hasNext())
				writeln("\n<hr>");
		}
	}

	/**
	 * @param method
	 */
	private void writeMethodDetail(DocMethod method) {
		startDetail(METHOD_NAME, method.getName(), method.getSummary());

		writeSynopsis(method);
		writeSectionHeader("Details\n");
		startDetailsTable();
		detailRow("Since", method.getSince());
		detailRow("Context", method.getContext());
		detailRow("Arguments", "None"); // In R1, no element methods have
		// arguments.
		detailRow("Return Type", method.getReturnType());
		endDetailsTable();

		// writeSectionHeader( "Arguments" );
		// writePara( "None" ); // In R1, no element methods have arguments.
		writeSection("Return", method.getReturnText());
		writeSection("Description", method.getDescription());
		writeSection("See Also", method.getSeeAlso());
	}

	private void writeSynopsis(DocMethod method) {
		writeSectionHeader("Synopsis\n");
		write("<p class=\"section-text\"><code>");
		if (method.getReturnType() != null) {
			write(method.getReturnType());
			write("&nbsp;");
		}
		write("obj.");
		write(method.getName());

		// Add aguments if/when any methods support them.

		writeln("(&nbsp;)</code></p>");
	}

	private void writeSlots(DocElement element) {
		if (!element.hasSlots())
			return;

		writeln("<h1>Slot Detail</h1>\n");
		Iterator iter = element.getSlots().iterator();
		while (iter.hasNext()) {
			DocSlot slot = (DocSlot) iter.next();
			writeSlotDetail(element, slot);
			if (iter.hasNext())
				writeln("\n<hr>");
		}
	}

	/**
	 * @param slot
	 */
	private void writeSlotDetail(DocElement element, DocSlot slot) {
		startDetail(SLOT_NAME, slot.getName(), slot.getSummary());

		writeln("<h3>Details</h3>\n");

		startDetailsTable();
		// detailRow( "Cardinality", slot.getCardinality( ) );
		detailRow("Display Name", slot.getDisplayName());
		detailRow("Since", slot.getSince());
		String name = slot.getXmlName();
		if (isBlank(name))
			name = "None. (The contents appear directly within the container element.)";
		else
			name = "<code>" + name + "</code>";
		detailRow("XML Element", name);
		detailRow("Contents", slot.getContents());
		if (element.hasStyle() || slot.hasStyle())
			detailRow("Default Style", slot.getStyle());
		endDetailsTable();

		writeSection("Description", slot.getDescription());
		writeSection("See Also", slot.getSeeAlso());
	}

	private void writeStructures() throws IOException {
		write(index, "<h1>Structures</h1>\n<table class=\"summary-table\">\n");
		Iterator iter = structs.iterator();
		while (iter.hasNext()) {
			DocStructure struct = (DocStructure) iter.next();
			try {
				write(struct);
			} catch (IOException e) {
				System.out.println("Failed to write file for Structure " + struct.getName());
				throw e;
			}
		}
		write(index, "</table>\n\n");
	}

	private void write(DocStructure struct) throws IOException {
		File output = makeFile("structs", struct.getName() + ".html"); //$NON-NLS-1$ //$NON-NLS-2$
		writer = new PrintStream(new FileOutputStream(output));
		writeIndexEntry(struct);
		writeHeader(struct);
		writeStructure(struct);
		writeProperties(struct);
		writeFooter();
		writer.close();
	}

	private void writeIndexEntry(String title, String file, String descrip) {
		write(index, "<tr><td><a href=\"");
		write(index, file);
		write(index, ".html\">");
		write(index, title);
		write(index, "</a></td>\n<td>");
		write(index, descrip);
		write(index, "</td></tr>\n");
	}

	private void writeStructure(DocStructure struct) {
		// Element name appears as H1 element

		write("<h1>Structure Overview</h1>\n");
		writeSectionBody(struct.getSummary());

		writeDetails(struct);
		writePropertySummary(struct);

		writeSection("Description", struct.getDescription());
		writeSection("XML Summary", struct.getXmlSummary());
		writeSection("See Also", struct.getSeeAlso());
	}

	private void writeDetails(DocStructure struct) {
		writeSectionHeader("Details");
		startDetailsTable();
		detailRow("Display Name", struct.getDisplayName());
		detailRow("Since", struct.getSince());
		endDetailsTable();
	}

	/**
	 * @throws IOException
	 * 
	 */

	private void writeTypes() throws IOException {
		writeIndexEntry("Property Types", "types", "The set of types used to define ROM properties.");

		File output = makeFile(null, "types.html"); //$NON-NLS-1$ //$NON-NLS-2$
		writer = new PrintStream(new FileOutputStream(output));

		writeln("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.0 transitional//EN\">");
		write("<html>\n<head>\n<title>");
		writeln(" Element (Eclipse BIRT ROM Documentation)</title>");
		writeln("<link rel=\"stylesheet\" href=\"style/style.css\" type=\"text/css\"/>");
		writeln("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">");
		writeln("</head>\n<body>");
		writeln("<p class=\"title\">Eclipse BIRT Report Object Model (ROM)</p>");
		writeln("<p class=\"subtitle\">Property Types</p>");
		writeln("<h1>Property Types</h1>\n<table class=\"summary-table\">");

		writeSectionBody(typeHeader);
		writeln("<hr>");

		Iterator iter = propertyTypes.iterator();
		while (iter.hasNext()) {
			DocPropertyType type = (DocPropertyType) iter.next();
			write("<h2><a name=\"");
			write(type.getName());
			write("\">");
			write(type.getName());
			writeln("</a></h2>");

			writeSectionBody(type.getSummary());

			writeSectionHeader("Details");
			startDetailsTable();
			detailRow("Display Name", type.getDisplayName());
			detailRow("Since", type.getSince());
			detailRow("XML Name", "<code>" + type.getXmlName() + "</code>");
			detailRow("JavaScript Design Type", type.getJSDesignType());
			detailRow("JavaScript Runtime Type", type.getJSRuntimeType());
			endDetailsTable();

			writeSection("Description", type.getDescription());
			if (type.getSeeAlso() != null) {
				writeSection("See Also", type.getSeeAlso());
			}
			// if ( iter.hasNext( ) )
			// ;
			writeln("<hr>");
		}
		finishSummaryTable();
		writeFooter();
		writer.close();
	}

	static class TypeComparator implements Comparator {

		public int compare(Object arg0, Object arg1) {
			PropertyType s1 = (PropertyType) arg0;
			PropertyType s2 = (PropertyType) arg1;
			return s1.getName().compareTo(s2.getName());
		}
	}

	/**
	 * @throws IOException
	 * 
	 */

	private void writeStyles() throws IOException {
		writeIndexEntry("Predefined Styles", "styles",
				"Styles defined by BIRT, usually as a \"default style\" for an element or slot.");

		File output = makeFile(null, "styles.html");
		writer = new PrintStream(new FileOutputStream(output));

		Properties selectors = new Properties();
		try {
			InputStream is = new BufferedInputStream(new FileInputStream(templateDir + "/style/selectors.properties"));
			selectors.load(is);
		} catch (IOException ex) {
			System.out.println("Can not read in \"selectors.properties\". ");
		}

		writeln("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.0 transitional//EN\">");
		write("<html>\n<head>\n<title>");
		writeln(" Element (Eclipse BIRT ROM Documentation)</title>");
		writeln("<link rel=\"stylesheet\" href=\"style/style.css\" type=\"text/css\"/>");
		writeln("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">");
		writeln("</head>\n<body>");
		writeln("<p class=\"title\">Eclipse BIRT Report Object Model (ROM)</p>");
		writeln("<p class=\"subtitle\">Predefined Styles</p>");
		writeln("<h1>Predefined Styles</h1>\n<table class=\"summary-table\">");

		MetaDataDictionary dict = MetaDataDictionary.getInstance();
		ArrayList list = new ArrayList();
		list.addAll(dict.getPredefinedStyles());
		Collections.sort(list, new StyleComparator());
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			PredefinedStyle style = (PredefinedStyle) iter.next();
			write("<tr><td><a name=\"");
			write(style.getName());
			write("\">");
			write(style.getName());
			write("</a></td>\n<td>");

			String description = selectors.getProperty(style.getName());
			if (description == null) {
				System.err.println("Missing selector description for " + style.getName());
			}

			write(description);
			writeln("</td></tr>");
		}
		finishSummaryTable();
		writeFooter();
		writer.close();
	}

	static class StyleComparator implements Comparator {

		public int compare(Object arg0, Object arg1) {
			PredefinedStyle s1 = (PredefinedStyle) arg0;
			PredefinedStyle s2 = (PredefinedStyle) arg1;
			return s1.getName().compareTo(s2.getName());
		}

	}

	/**
	 * @throws IOException
	 * 
	 */
	private void writePropertyIndex() throws IOException {
		writeIndexEntry("Property Index", "prop-index", "Index of properties with a link to their definition.");

		HashMap props = new HashMap();
		buildIndex(props, elements);
		buildIndex(props, structs);
		ArrayList list = new ArrayList();
		list.addAll(props.values());
		Collections.sort(list, new PropInfoComparator());

		File output = makeFile(null, "prop-index.html");
		writer = new PrintStream(new FileOutputStream(output));

		writeln("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.0 transitional//EN\">");
		write("<html>\n<head>\n<title>");
		writeln(" Element (Eclipse BIRT ROM Documentation)</title>");
		writeln("<link rel=\"stylesheet\" href=\"style/style.css\" type=\"text/css\"/>");
		writeln("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">");
		writeln("</head>\n<body>");
		writeln("<p class=\"title\">Eclipse BIRT Report Object Model (ROM)</p>");
		writeln("<p class=\"subtitle\">Property Index</p>");
		writeln("<h1>Property Index</h1>\n<table class=\"summary-table\">");

		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			PropInfo prop = (PropInfo) iter.next();
			write("<tr><td>");
			write(prop.name);
			write("</td>\n<td>");
			Collections.sort(prop.uses, new DocComparator());
			Iterator i2 = prop.uses.iterator();
			while (i2.hasNext()) {
				DocComposite obj = (DocComposite) i2.next();
				write("<a href=\"");
				if (obj.isElement())
					write("elements");
				else
					write("structs");
				write("/");
				write(obj.getName());
				write(".html\">");
				write(obj.getName());
				write("</a> ");
				if (obj.isElement())
					write("Element");
				else
					write("Structure");
				if (i2.hasNext())
					write(", ");
				writeln("");
			}
			writeln("</td>\n");
		}
		finishSummaryTable();
		writeFooter();
		writer.close();
	}

	private void buildIndex(HashMap props, List objs) {
		Iterator iter = objs.iterator();
		while (iter.hasNext()) {
			DocComposite obj = (DocComposite) iter.next();
			Iterator i2 = obj.getProperties().iterator();
			while (i2.hasNext()) {
				DocProperty prop = (DocProperty) i2.next();
				PropInfo info = (PropInfo) props.get(prop.getName());
				if (info == null) {
					info = new PropInfo();
					info.name = prop.getName();
					props.put(info.name, info);
				}
				info.uses.add(obj);
			}
		}
	}

	static class PropInfo {

		String name;
		ArrayList uses = new ArrayList();
	}

	static class PropInfoComparator implements Comparator {

		public int compare(Object arg0, Object arg1) {
			PropInfo s1 = (PropInfo) arg0;
			PropInfo s2 = (PropInfo) arg1;
			return s1.name.compareTo(s2.name);
		}

	}

	/**
	 * 
	 */
	private void writeInheritanceTable() {
		// writeIndexEntry( "Element Inheritance", "inheritance",
		// "Inheritance chart for all ROM elements." );
		// TODO Auto-generated method stub

	}

	private void writeFooter() {
		writeln("</body>\n</html>");
	}

	void setTypeHeader(String text) {
		typeHeader = text;
	}

	DocPropertyType findType(String name) {
		Iterator iter = propertyTypes.iterator();
		while (iter.hasNext()) {
			DocPropertyType type = (DocPropertyType) iter.next();
			if (type.getName().equals(name))
				return type;
		}
		return null;
	}

}
