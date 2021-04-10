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

package org.eclipse.birt.doc.legacy;

public class SpecObject {
	public static final int TRI_UNKNOWN = 0;
	public static final int TRI_TRUE = 1;
	public static final int TRI_FALSE = 2;

	public String name; // Keys docs, rom.def
	public String displayName; // Doc
	public String description; // Doc
	public String since; // rom.def
	public String seeAlso; // Doc
	public String summary; // Doc
	public String issues; // Doc

	public void addIssue(String issue) {
		if (issues == null)
			issues = "";
		issues += "<p>" + issue + "</p>\n";
	}
}
