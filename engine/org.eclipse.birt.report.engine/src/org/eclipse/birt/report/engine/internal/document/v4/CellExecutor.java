/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.engine.internal.document.v4;

import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.ir.CellDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;

public class CellExecutor extends ContainerExecutor {

	private int nextItem;

	protected CellExecutor(ExecutorManager manager) {
		super(manager, ExecutorManager.CELLITEM);
		nextItem = 0;
	}

	@Override
	protected IContent doCreateContent() {
		return report.createCellContent();
	}

	@Override
	protected void doExecute() throws Exception {
		executeQuery();
	}

	@Override
	public void close() {
		nextItem = 0;
		closeQuery();
		super.close();
	}

	@Override
	protected ReportItemExecutor doCreateExecutor(long offset) throws Exception {
		CellDesign cellDesign = (CellDesign) design;
		if (nextItem < cellDesign.getContentCount()) {
			ReportItemDesign design = cellDesign.getContent(nextItem);
			nextItem++;
			return manager.createExecutor(this, design, nextOffset);
		}
		return null;
	}

	@Override
	protected void doSkipToExecutor(InstanceID id, long offset) throws Exception {
		CellDesign cellDesign = (CellDesign) design;
		int itemCount = cellDesign.getContentCount();
		long designId = id.getComponentID();
		for (int i = 0; i < itemCount; i++) {
			ReportItemDesign childDesign = cellDesign.getContent(i);
			if (designId == childDesign.getID()) {
				// this one is the first executed element.
				nextItem = i;
				return;
			}
		}
		nextItem = itemCount;
	}
}
