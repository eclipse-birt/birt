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

package org.eclipse.birt.report.designer.core.model.schematic;

import java.util.List;

import org.eclipse.birt.report.designer.core.model.DesignElementHandleAdapter;
import org.eclipse.birt.report.designer.core.model.IModelAdapterHelper;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Adapter class to adapt model handle. This adapter provides convenience.
 * methods to GUI requirement CellHandleAdapter responds to model CellHandle
 * 
 */

public class CellHandleAdapter extends DesignElementHandleAdapter {

	/**
	 * Constructor
	 * 
	 * @param cellHandle The cell handle.
	 * @param mark
	 */
	public CellHandleAdapter(ReportElementHandle cellHandle, IModelAdapterHelper mark) {
		super(cellHandle, mark);
	}

	/**
	 * Gets the Children iterator. This children relationship is determined by GUI
	 * requirement. This is not the model children relationship.
	 * 
	 * @return Children iterator
	 */

	public List getChildren() {
		return getCellHandle().getContent().getContents();
	}

	/**
	 * Gets the row number.
	 * 
	 * @return The row number.
	 */
	public int getRowNumber() {
		assert getCellHandle().getContainer() instanceof RowHandle;
		return HandleAdapterFactory.getInstance().getRowHandleAdapter(getCellHandle().getContainer()).getRowNumber();
	}

	/**
	 * Gets the column number
	 * 
	 * @return The column number.
	 */
	public int getColumnNumber() {
		assert getCellHandle().getContainer() instanceof RowHandle;

		if (getCellHandle().getColumn() == 0) {
			TableHandleAdapter adapt = HandleAdapterFactory.getInstance().getTableHandleAdapter(getTableParent());
			TableHandleAdapter.RowUIInfomation info = adapt.getRowInfo(getHandle().getContainer());
			if (info == null) {
				adapt.reload();
				info = adapt.getRowInfo(getHandle().getContainer());
			}
			return info.getAllChildren().indexOf(getHandle()) + 1;
		}

		return getCellHandle().getColumn();
	}

	/**
	 * Gets the row span.
	 * 
	 * @return The row span.
	 */
	public int getRowSpan() {
		return getCellHandle().getRowSpan();
	}

	/**
	 * Gets the column span
	 * 
	 * @return the column span.
	 */
	public int getColumnSpan() {
		return getCellHandle().getColumnSpan();
	}

	/**
	 * Gets the location.
	 * 
	 * @return The location.
	 */
	public Point getLocation() {
		return new Point(1, 1);
	}

	/**
	 * Gets the size.
	 * 
	 * @return The size
	 */
	public Dimension getSize() {
		return new Dimension(60, 40);
	}

	/**
	 * Gets the bounds.
	 * 
	 * @return The bounds
	 */

	public Rectangle getBounds() {
		return new Rectangle(getLocation().x, getLocation().y, getSize().width, getSize().height);
	}

	private CellHandle getCellHandle() {
		return (CellHandle) getHandle();
	}

	/**
	 * Set column span.
	 * 
	 * @param colSpan The new column span.
	 * @throws SemanticException
	 */
	public void setColumnSpan(int colSpan) throws SemanticException {
		this.getCellHandle().setProperty(CellHandle.COL_SPAN_PROP, Integer.valueOf(colSpan));
	}

	/**
	 * Set row span.
	 * 
	 * @param rowSpan The new row span.
	 * @throws SemanticException
	 */
	public void setRowSpan(int rowSpan) throws SemanticException {
		this.getCellHandle().setProperty(CellHandle.ROW_SPAN_PROP, Integer.valueOf(rowSpan));

	}

	public Object getTableParent() {
		DesignElementHandle handle = getCellHandle();

		while (handle != null) {
			if (handle instanceof TableHandle) {
				return handle;
			}
			if (handle instanceof GridHandle) {
				return handle;
			}
			handle = handle.getContainer();
		}
		return null;
	}
}
