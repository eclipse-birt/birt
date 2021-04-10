
/***********************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/
/***********************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	public BoxStyle getBoxStyle() {
		return cell.getBoxStyle();
	}

	public IContent getContent() {
		return cell.getContent();
	}

	public CellArea getCell() {
		return cell;
	}

	public int getColumnID() {
		return cell.getColumnID();
	}

	public int getDelta() {
		return delta;
	}

	public void setDelta(int delta) {
		this.delta = delta;
	}

	public CellArea cloneArea() {
		CellArea cloneCell = cell.cloneArea();
		return cloneCell;
	}

	public SplitResult split(int height, boolean force) throws BirtException {
		SplitResult result = cell.split(height + delta, force);
		return result;
	}

}
