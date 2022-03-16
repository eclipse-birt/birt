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

import org.eclipse.birt.report.model.metadata.PropertyType;

public class DocPropertyType {
	PropertyType defn;
	String summary;
	String description;
	String seeAlso;

	public DocPropertyType(PropertyType type) {
		defn = type;
	}

	public void setSummary(String string) {
		summary = string;
	}

	public void setSeeAlso(String string) {
		seeAlso = string;
	}

	public void setDescription(String string) {
		description = string;
	}

	public String getName() {
		return defn.getName();
	}

	public String getSummary() {
		return summary;
	}

	public String getDisplayName() {
		return defn.getDisplayName();
	}

	public String getXmlName() {
		return defn.getName();
	}

	public String getJSDesignType() {
		// TODO: Get this from the model when available.

		return null;
	}

	public String getJSRuntimeType() {
		// TODO: Get this from the model when available.

		return null;
	}

	public String getDescription() {
		return description;
	}

	public String getSeeAlso() {
		return seeAlso;
	}

	public String getSince() {
		// TODO: Get this from the model when it is supported.

		String name = getName();
		if (name.equals("column") || name.equals("variant")) {
			return "reserved";
		}

		return "1.0";
	}
}
