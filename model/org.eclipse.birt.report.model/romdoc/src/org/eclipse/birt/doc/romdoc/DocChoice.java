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

import org.eclipse.birt.report.model.api.metadata.IChoice;

public class DocChoice {
	IChoice choice;
	String description;

	public DocChoice(IChoice item) {
		choice = item;
	}

	public String getName() {
		return choice.getName();
	}

	public String getDisplayName() {
		return choice.getDisplayName();
	}

	public String getValue() {
		if (choice.getValue() == null)
			return null;
		return choice.getValue().toString();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String descrip) {
		description = descrip;
	}
}
