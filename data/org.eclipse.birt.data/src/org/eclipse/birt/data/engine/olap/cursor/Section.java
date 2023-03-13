/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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
