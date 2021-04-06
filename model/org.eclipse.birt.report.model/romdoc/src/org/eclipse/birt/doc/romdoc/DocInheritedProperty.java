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

import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyDefn;

public class DocInheritedProperty extends DocObject {

	private String name;

	public void setName(String propName) {
		name = propName;
	}

	public void setDescription(String string) {
		description = string;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public boolean isDefined(DocElement element) {
		return element.getDefn().findProperty(name) != null;
	}

	public boolean isReserved(DocElement element) {
		IPropertyDefn prop = element.getDefn().findProperty(name);
		if (prop == null)
			return true;
		String since = ((PropertyDefn) prop).getSince();
		if (since == null)
			return false;
		return since.equals("reserved");
	}

}
