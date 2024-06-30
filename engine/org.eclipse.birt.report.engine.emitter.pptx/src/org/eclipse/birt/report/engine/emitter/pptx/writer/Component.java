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

package org.eclipse.birt.report.engine.emitter.pptx.writer;

import java.io.IOException;

import org.eclipse.birt.report.engine.ooxml.IPart;
import org.eclipse.birt.report.engine.ooxml.IPartContainer;
import org.eclipse.birt.report.engine.ooxml.writer.OOXmlWriter;

public abstract class Component {

	protected IPart part;
	protected OOXmlWriter writer;

	protected void initialize(IPartContainer parent, String uri, String type, String relationshipType)
			throws IOException {
		this.initialize(parent, uri, type, relationshipType, true);
	}

	protected void initialize(IPartContainer parent, String uri, String type, String relationshipType,
			boolean needCache) throws IOException {
		this.part = parent.getPart(uri, type, relationshipType);
		this.writer = needCache ? part.getCacheWriter() : part.getWriter();
	}

	protected IPart getPart() {
		return part;
	}

	protected IPart referTo(Component component) {
		return part.createPartReference(component.getPart());
	}
}
