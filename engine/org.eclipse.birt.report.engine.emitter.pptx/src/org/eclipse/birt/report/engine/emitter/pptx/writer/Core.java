/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.pptx.writer;

import java.io.IOException;

import org.eclipse.birt.report.engine.ooxml.Package;
import org.eclipse.birt.report.engine.ooxml.constants.ContentTypes;
import org.eclipse.birt.report.engine.ooxml.constants.NameSpaces;
import org.eclipse.birt.report.engine.ooxml.constants.RelationshipTypes;

public class Core extends Component {

	private static final String TAG_DESCRIPTION = "dc:description";
	private static final String TAG_TITLE = "dc:title";
	private static final String TAG_CREATOR = "dc:creator";
	private static final String TAG_SUBJECT = "dc:subject";
	private static final String TAG_CORE_PROPERTIES = "cp:coreProperties";

	public Core(Presentation presentation, String author, String title, String description, String subject)
			throws IOException {
		String uri = "docProps/core.xml";
		Package pkg = presentation.getPackage();
		super.initialize(pkg, uri, ContentTypes.CORE, RelationshipTypes.CORE, false);
		start();
		writeCoreProperty(author, title, description, subject);
		close();
	}

	private void start() {

		writer.startWriter();
		writer.openTag(TAG_CORE_PROPERTIES);
		writer.nameSpace("cp", NameSpaces.CORE);
		writer.nameSpace("dc", NameSpaces.DC);
		writer.nameSpace("dcterms", NameSpaces.DC_TERMS);
		writer.nameSpace("dcmitype", NameSpaces.DC_MITYPE);
		writer.nameSpace("xsi", NameSpaces.XSI);

	}

	private void close() {
		writer.closeTag(TAG_CORE_PROPERTIES);
		writer.close();
		this.writer = null;
	}

	private void writeCoreProperty(String creator, String title, String description, String subject) {
		writeProperty(TAG_CREATOR, creator);
		writeProperty(TAG_TITLE, title);
		writeProperty(TAG_DESCRIPTION, description);
		writeProperty(TAG_SUBJECT, subject);
	}

	private void writeProperty(String tag, String property) {
		if (property != null) {
			writer.openTag(tag);
			writer.text(property);
			writer.closeTag(tag);
		}
	}
}
