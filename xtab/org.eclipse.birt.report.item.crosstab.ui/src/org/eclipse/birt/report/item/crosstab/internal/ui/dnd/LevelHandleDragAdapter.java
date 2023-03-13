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

package org.eclipse.birt.report.item.crosstab.internal.ui.dnd;

import org.eclipse.birt.report.designer.internal.ui.dnd.DNDService;
import org.eclipse.birt.report.designer.internal.ui.dnd.IDragAdapter;
import org.eclipse.birt.report.model.api.olap.LevelHandle;

/**
 * Support drag the level handle to the cross tab.
 */

public class LevelHandleDragAdapter implements IDragAdapter {

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.dnd.IDragAdapter#canDrag(java.
	 * lang.Object)
	 */
	@Override
	public int canDrag(Object object) {
		if (object instanceof LevelHandle) {
			return DNDService.LOGIC_TRUE;
		}
		return DNDService.LOGIC_UNKNOW;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.dnd.IDragAdapter#getDragTransfer
	 * (java.lang.Object)
	 */
	@Override
	public Object getDragTransfer(Object transfer) {
		if (transfer instanceof LevelHandle) {
			return transfer;
		}
		return null;
	}
}
