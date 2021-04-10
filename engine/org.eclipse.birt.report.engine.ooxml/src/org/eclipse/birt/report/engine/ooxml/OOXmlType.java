/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.ooxml;

import org.eclipse.birt.report.engine.ooxml.writer.OOXmlWriter;

public class OOXmlType implements ContentType {

	private IPart part;

	private String type;

	public OOXmlType(String type) {
		this.type = type;
	}

	public void setPart(IPart part) {
		this.part = part;
	}

	public void write(OOXmlWriter writer) {
		writer.openTag("Override");
		writer.attribute("PartName", part.getAbsoluteUri());
		writer.attribute("ContentType", type);
		writer.closeTag("Overrrid");
	}

	public String toString() {
		return type;
	}
}
