/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
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

	@Override
	public void write(OOXmlWriter writer) {
		writer.openTag("Override");
		writer.attribute("PartName", part.getAbsoluteUri());
		writer.attribute("ContentType", type);
		writer.closeTag("Overrrid");
	}

	@Override
	public String toString() {
		return type;
	}
}
