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
