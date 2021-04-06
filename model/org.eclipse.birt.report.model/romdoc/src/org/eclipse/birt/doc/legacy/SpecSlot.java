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

public class SpecSlot extends SpecObject {
	public static final int UNKNOWN = 0;
	public static final int SINGLE = 1;
	public static final int MULTIPLE = 2;

	public String shortDescrip; // Doc
	public String contents; // Doc
	public String xmlElement; // rom.def
	public int cardinality = UNKNOWN; // rom.def
	public String styleNames; // rom.def
}
