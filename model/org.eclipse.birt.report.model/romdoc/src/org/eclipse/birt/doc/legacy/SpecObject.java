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
		if (issues == null) {
			issues = "";
		}
		issues += "<p>" + issue + "</p>\n";
	}
}
