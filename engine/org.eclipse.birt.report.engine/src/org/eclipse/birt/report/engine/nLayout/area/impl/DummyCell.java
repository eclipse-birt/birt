
/***********************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/
/***********************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.nLayout.area.impl;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.nLayout.area.style.BoxStyle;

public class DummyCell extends CellArea {
	protected CellArea cell;

	/**
	 * For the first dummy cell, delta = 0 + lastRowHeight For the subsequent dummy
	 * cell, delta = upperDummyCellDelta + lastRowHeight
	 */
	protected int delta;

	public DummyCell(CellArea cell) {
		this.cell = cell;
	}

	@Override
	public BoxStyle getBoxStyle() {
		return cell.getBoxStyle();
	}

	@Override
	public IContent getContent() {
		return cell.getContent();
	}

	public CellArea getCell() {
		return cell;
	}

	@Override
	public int getColumnID() {
		return cell.getColumnID();
	}

	public int getDelta() {
		return delta;
	}

	public void setDelta(int delta) {
		this.delta = delta;
	}

	@Override
	public CellArea cloneArea() {
		CellArea cloneCell = cell.cloneArea();
		return cloneCell;
	}

	@Override
	public SplitResult split(int height, boolean force) throws BirtException {
		SplitResult result = cell.split(height + delta, force);
		return result;
	}

}
