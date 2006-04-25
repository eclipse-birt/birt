/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.service.api;

/**
 * Representation of a result set column
 * 
 */
public class ExportedColumn {
	private String name;

	private String label;

	private boolean visibility;

	public ExportedColumn(String name, String label, boolean visibility) {
		this.name = name;
		this.label = label;
		this.visibility = visibility;
	}

	public String getName() {
		return name;
	}

	public String getLabel() {
		return label;
	}

	public boolean getVisibility() {
		return visibility;
	}
}
