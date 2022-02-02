/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.build.framework;

import org.w3c.dom.Element;

public class ExtensionPoint {
	Bundle bundle;
	Element element;

	ExtensionPoint(Bundle bundle, Element element) {
		this.bundle = bundle;
		this.element = element;
	}

	public String getSchema() {
		return element.getAttribute("schema");
	}

	public void setSchema(String schema) {
		element.setAttribute("schema", schema);
	}

	public String getId() {
		return element.getAttribute("id");
	}

	public void setId(String id) {
		element.setAttribute("id", id);
	}

	public String getName() {
		return element.getAttribute("name");
	}

	public void setName(String name) {
		element.setAttribute("name", name);
	}

	public Element getElement() {
		return element;
	}

}
