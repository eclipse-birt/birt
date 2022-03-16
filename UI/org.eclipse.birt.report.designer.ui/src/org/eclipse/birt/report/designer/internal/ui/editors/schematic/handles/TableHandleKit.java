/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.handles;

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableCellEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.Handle;
import org.eclipse.gef.SharedCursors;

/**
 * Provides handle caretion and adding support.
 */
public class TableHandleKit {

	/**
	 * Adds handle to table cell.
	 *
	 * @param part
	 * @param handles
	 */
	public static void addHandles(TableCellEditPart part, List handles) {
		List list = part.getViewer().getSelectedEditParts();
		if (hasRemoveEditPart(list)) {
			return;
		}
		handles.add(createHandle(part));
	}

	/**
	 * Adds handle to table
	 *
	 * @param part
	 * @param handles
	 */
	public static void addHandles(TableEditPart part, List handles) {
		handles.add(createColumnHandle(part));
		handles.add(createRowHandle(part));
	}

	private static boolean hasRemoveEditPart(List parts) {
		for (int i = 0; i < parts.size(); i++) {
			Object obj = parts.get(i);
			if (obj instanceof ReportElementEditPart && ((ReportElementEditPart) obj).isDelete()) {
				return true;
			}
		}
		return false;
	}

	static Handle createHandle(TableCellEditPart owner) {

		TableEditPart part = (TableEditPart) owner.getParent();
		Rectangle rect = part.getSelectBounds();

		TableSelectionHandle handle = new TableSelectionHandle(owner, rect);
		handle.setCursor(SharedCursors.SIZEALL);

		return handle;
	}

	static Handle createColumnHandle(TableEditPart owner) {
		TableColumnHandle handle = new TableColumnHandle(owner);
		return handle;

	}

	static Handle createRowHandle(TableEditPart owner) {
		TableRowHandle handle = new TableRowHandle(owner);
		return handle;

	}

}
