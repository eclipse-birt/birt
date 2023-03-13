/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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
package org.eclipse.birt.data.engine.olap.query.view;

import java.util.List;

/**
 * The RelationShip class keeps the row and column information with certain
 * measure.
 *
 */
public class Relationship {

	private List rowLevel, columnLevel, pageLevel;

	/**
	 *
	 * @param rowLevel
	 * @param columnLevel
	 */
	public Relationship(List rowLevel, List columnLevel, List pageLevel) {
		this.rowLevel = rowLevel;
		this.columnLevel = columnLevel;
		this.pageLevel = pageLevel;
	}

	/**
	 *
	 * @return
	 */
	public List getLevelListOnRow() {
		return this.rowLevel;
	}

	/**
	 *
	 * @return
	 */
	public List getLevelListOnColumn() {
		return this.columnLevel;
	}

	/**
	 *
	 * @return
	 */
	public List getLevelListOnPage() {
		return this.pageLevel;
	}
}
