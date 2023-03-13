/*******************************************************************************
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
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.internal.ui.dnd;

import org.eclipse.birt.report.designer.internal.ui.dnd.DNDService;
import org.eclipse.birt.report.designer.internal.ui.dnd.IDragAdapter;
import org.eclipse.birt.report.model.api.LevelAttributeHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.core.runtime.IAdaptable;

/**
 *
 */

public class LevelAttributeHandleDragAdapter implements IDragAdapter {

	@Override
	public int canDrag(Object object) {
		if (object instanceof IAdaptable) {
			if (((IAdaptable) object).getAdapter(StructureHandle.class) instanceof LevelAttributeHandle) {
				object = ((IAdaptable) object).getAdapter(StructureHandle.class);
			}
		}
		if (object instanceof LevelAttributeHandle) {
			return DNDService.LOGIC_TRUE;
		}
		return DNDService.LOGIC_UNKNOW;
	}

	@Override
	public Object getDragTransfer(Object transfer) {
		if (transfer instanceof LevelAttributeHandle) {
			return transfer;
		}
		if (transfer instanceof IAdaptable) {
			if (((IAdaptable) transfer).getAdapter(StructureHandle.class) instanceof LevelAttributeHandle) {
				return ((IAdaptable) transfer).getAdapter(StructureHandle.class);
			}
		}
		return null;
	}

}
