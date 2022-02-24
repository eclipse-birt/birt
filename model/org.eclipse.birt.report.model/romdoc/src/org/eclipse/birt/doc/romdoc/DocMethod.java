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

import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;

public class DocMethod extends DocProperty {
	String returnText;

	public DocMethod(ElementPropertyDefn propDefn) {
		super(propDefn);
	}

	public void setReturnText(String value) {
		returnText = value;
	}

	public String getReturnText() {
		return returnText;
	}
}
