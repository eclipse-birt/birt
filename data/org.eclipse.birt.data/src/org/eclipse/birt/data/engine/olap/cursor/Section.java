/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 *
 */
class Section {
	private List[] relation;
	private int baseStart, baseEnd;

	Section(int customSize, int baseStart, int baseEnd) {
		this.baseStart = baseStart;
		this.baseEnd = baseEnd;
		relation = new List[customSize];
		for (int i = 0; i < customSize; i++) {
			relation[i] = new ArrayList();
		}
	}

	List[] getRelation() {
		return this.relation;
	}

	int getBaseStart() {
		return this.baseStart;
	}

	int getBaseEnd() {
		return this.baseEnd;
	}

	void setBaseStart(int baseStart) {
		this.baseStart = baseStart;
	}

	void setBaseEnd(int baseEnd) {
		this.baseEnd = baseEnd;
	}
}
