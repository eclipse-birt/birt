/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.elements;

import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.validators.CellOverlappingValidator;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.StyledElement;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.elements.interfaces.ITableRowModel;
import org.eclipse.birt.report.model.elements.strategy.TableRowPropSearchStrategy;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;

/**
 * This class represents a row in a Grid or a table.
 * 
 */

public class TableRow extends StyledElement implements ITableRowModel {

	/**
	 * Default constructor.
	 */

	public TableRow() {
		super();
		initSlots();
		cachedPropStrategy = TableRowPropSearchStrategy.getInstance();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getSlot(int)
	 */

	public ContainerSlot getSlot(int slot) {
		assert (slot == CONTENT_SLOT);
		return slots[CONTENT_SLOT];
	}

	/**
	 * Gets the contents of of the Contents slot. DO NOT change the returned list,
	 * use the handle class to make changes.
	 * 
	 * @return the contents as an array
	 */

	public List<DesignElement> getContentsSlot() {
		return slots[CONTENT_SLOT].getContents();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt
	 * .report.model.elements.ElementVisitor)
	 */

	public void apply(ElementVisitor visitor) {
		visitor.visitRow(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	public String getElementName() {
		return ReportDesignConstants.ROW_ELEMENT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getHandle(org.eclipse
	 * .birt.report.model.element.ReportDesign)
	 */

	public DesignElementHandle getHandle(Module module) {
		return handle(module);
	}

	/**
	 * Returns an API handle for this element.
	 * 
	 * @param module the report design of the row
	 * 
	 * @return an API handle for this element
	 */

	public RowHandle handle(Module module) {
		if (handle == null) {
			handle = new RowHandle(module, this);
		}
		return (RowHandle) handle;
	}

	/**
	 * Computes the number of columns defined by this row.
	 * 
	 * @param module the report design
	 * @return the number of columns defined in this row
	 */

	public int getColumnCount(Module module) {
		int colCount = 0;
		int cellCount = slots[CONTENT_SLOT].getCount();
		for (int i = 0; i < cellCount; i++) {
			Cell cell = (Cell) slots[CONTENT_SLOT].getContent(i);
			int posn = cell.getColumn(module);
			int span = cell.getColSpan(module);

			// One-based indexing. Position is optional.

			if (posn > 0) {
				int end = posn + span - 1;
				if (end > colCount)
					colCount = end;
			} else
				colCount += span;
		}
		return colCount;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#validate(org.eclipse
	 * .birt.report.model.elements.ReportDesign)
	 */

	public List<SemanticException> validate(Module module) {
		List<SemanticException> list = super.validate(module);

		list.addAll(CellOverlappingValidator.getInstance().validate(module, this));

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getProperty(org.eclipse
	 * .birt.report.model.core.Module,
	 * org.eclipse.birt.report.model.metadata.ElementPropertyDefn)
	 */
	public Object getProperty(Module module, ElementPropertyDefn prop) {

		String propName = prop.getName();
		if (IStyleModel.PAGE_BREAK_INSIDE_PROP.equals(propName)) {
			// get default in different cases
			DesignElement container = getContainer();
			if (container instanceof TableItem || container instanceof TableGroup) {
				Object value = cachedPropStrategy.getPropertyFromElement(module, this, prop);
				if (value != null)
					return value;

				// row in table or table group: default is avoid
				return DesignChoiceConstants.PAGE_BREAK_INSIDE_AVOID;
			}
		}

		return super.getProperty(module, prop);
	}
}