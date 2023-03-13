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

import java.util.ArrayList;

public class SpecProperty extends SpecObject {
	public String shortDescrip; // Doc
	public String jsType; // rom.def
	public String defaultValue; // rom.def
	public int runtimeSettable; // rom.def
	public int isArray; // rom.def
	public int hidden; // rom.def
	public String romType; // rom.def
	public int inherited; // rom.def
	public String exprType; // rom.def
	public String exprContext; // rom.def
	public ArrayList choices = new ArrayList();
	public int required; // rom.def

	/**
	 * @param choice
	 */
	public void addChoice(SpecChoice choice) {
		choices.add(choice);
	}
}
