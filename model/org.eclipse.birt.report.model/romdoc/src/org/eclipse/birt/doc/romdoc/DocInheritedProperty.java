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

import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyDefn;

public class DocInheritedProperty extends DocObject {

	private String name;

	public void setName(String propName) {
		name = propName;
	}

	@Override
	public void setDescription(String string) {
		description = string;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	public boolean isDefined(DocElement element) {
		return element.getDefn().findProperty(name) != null;
	}

	public boolean isReserved(DocElement element) {
		IPropertyDefn prop = element.getDefn().findProperty(name);
		if (prop == null) {
			return true;
		}
		String since = ((PropertyDefn) prop).getSince();
		if (since == null) {
			return false;
		}
		return since.equals("reserved");
	}

}
