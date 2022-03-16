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

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.model;

import java.util.List;

import org.eclipse.birt.report.designer.util.IVirtualValidator;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.VariableElementHandle;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;

/**
 * Ceosstab cell adapter
 */

public abstract class CrosstabCellAdapter extends BaseCrosstabAdapter implements IVirtualValidator {

	int rowNumber;
	int columnNumber;
	int rowSpan = 1;
	int columnSpan = 1;
	private String positionType = ""; //$NON-NLS-1$

	/**
	 * Constructor
	 *
	 * @param handle
	 */
	public CrosstabCellAdapter(CrosstabCellHandle handle) {
		super(handle);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.
	 * BaseCrosstabAdapter#getModelList()
	 */
	@Override
	public List getModelList() {
		return getCrosstabCellHandle().getContents();
	}

	/**
	 * Gets the column number.
	 *
	 * @return
	 */
	public int getColumnNumber() {
		return columnNumber;
	}

	/**
	 * Sets the column number
	 *
	 * @param columnNumber
	 */
	public void setColumnNumber(int columnNumber) {
		this.columnNumber = columnNumber;
	}

	/**
	 * Gets the column span
	 *
	 * @return
	 */
	public int getColumnSpan() {
		return columnSpan;
	}

	/**
	 * Sets the column span
	 *
	 * @param columnSpan
	 */
	public void setColumnSpan(int columnSpan) {
		this.columnSpan = columnSpan;
	}

	/**
	 * Gets the row number
	 *
	 * @return
	 */
	public int getRowNumber() {
		return rowNumber;
	}

	/**
	 * Sets the row number
	 *
	 * @param rowNumber
	 */
	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

	/**
	 * Gets the row span
	 *
	 * @return
	 */
	public int getRowSpan() {
		return rowSpan;
	}

	/**
	 * Sets the row span
	 *
	 * @param rowSpan
	 */
	public void setRowSpan(int rowSpan) {
		this.rowSpan = rowSpan;
	}

	/**
	 * Gets the crosstab cell handle
	 *
	 * @return
	 */
	public CrosstabCellHandle getCrosstabCellHandle() {
		return (CrosstabCellHandle) getCrosstabItemHandle();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "rownumber == " //$NON-NLS-1$
				+ getRowNumber() + " rowspan == " //$NON-NLS-1$
				+ getRowSpan() + " columnnumber=" //$NON-NLS-1$
				+ getColumnNumber() + " columnspan ==" //$NON-NLS-1$
				+ getColumnSpan();
	}

	/**
	 * Gets the position type
	 *
	 * @return
	 */
	public String getPositionType() {
		return positionType;
	}

	/**
	 * Sets the position type
	 *
	 * @param positionType
	 */
	public void setPositionType(String positionType) {
		this.positionType = positionType;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.
	 * BaseCrosstabAdapter#copyToTarget(org.eclipse.birt.report.item.crosstab.
	 * internal.ui.editors.model.BaseCrosstabAdapter)
	 */
	@Override
	public BaseCrosstabAdapter copyToTarget(BaseCrosstabAdapter crossAdapt) {
		if (crossAdapt instanceof CrosstabCellAdapter) {
			CrosstabCellAdapter copy = (CrosstabCellAdapter) crossAdapt;
			copy.setColumnNumber(getColumnNumber());
			copy.setRowNumber(getRowNumber());
			copy.setColumnSpan(getColumnSpan());
			copy.setRowSpan(getRowSpan());
			copy.setPositionType(getPositionType());
		}
		return super.copyToTarget(crossAdapt);
	}

	/**
	 * @return
	 */
	public DataItemHandle getFirstDataItem() {
		List list = getCrosstabCellHandle().getContents();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) instanceof DataItemHandle) {
				return (DataItemHandle) list.get(i);
			}
		}
		return null;
	}

	/**
	 * @return
	 */
	public Object getFirstElement() {
		List list = getCrosstabCellHandle().getContents();
		if (!list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	public PropertyHandle getPropertyHandle() {
		if (getCrosstabCellHandle() != null) {
			DesignElementHandle handle = getCrosstabCellHandle().getModelHandle();
			return handle.getPropertyHandle(getDefaultContentName(handle));
		}
		return null;
	}

	public String getDefaultContentName(DesignElementHandle parent) {

		List propDefns = (parent).getDefn().getContents();
		if (!propDefns.isEmpty()) {
			return ((IPropertyDefn) propDefns.get(0)).getName();
		}

		return ""; //$NON-NLS-1$
	}

	@Override
	public boolean handleValidate(Object obj) {
		if (obj instanceof Object[]) {
			Object[] objects = (Object[]) obj;
			int len = objects.length;
			if (len == 0) {
				return false;
			}
			if (len == 1) {
				return handleValidate(objects[0]);
			} else if (isAllParameter(objects)) {
				return true;
			}

		}

		if (obj instanceof ScalarParameterHandle || obj instanceof VariableElementHandle) {
			return true;
		}
		return false;
	}

	private boolean isAllParameter(Object[] objs) {
		for (int i = 0; i < objs.length; i++) {
			if (!(objs[i] instanceof ScalarParameterHandle)) {
				return false;
			}
		}

		return true;
	}
}
