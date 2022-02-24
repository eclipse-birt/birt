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

package org.eclipse.birt.report.model.elements;

import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IDataItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;

/**
 * This class represents a data item element: one that displays the value of an
 * expression.
 *
 */

public class DataItem extends ReportItem implements IDataItemModel {

	/**
	 * Default constructor.
	 */

	public DataItem() {
	}

	/**
	 * Constructs the data item with an optional name.
	 *
	 * @param theName optional item name
	 */

	public DataItem(String theName) {
		super(theName);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt.
	 * report.model.elements.ElementVisitor)
	 */

	@Override
	public void apply(ElementVisitor visitor) {
		visitor.visitDataItem(this);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	@Override
	public String getElementName() {
		return ReportDesignConstants.DATA_ITEM;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.core.DesignElement#getHandle(org.eclipse.birt.
	 * report.model.elements.ReportDesign)
	 */

	@Override
	public DesignElementHandle getHandle(Module module) {
		return handle(module);
	}

	/**
	 * Returns an API handle for this element.
	 *
	 * @param module the report design
	 * @return an API handle for this element.
	 */

	public DataItemHandle handle(Module module) {
		if (handle == null) {
			handle = new DataItemHandle(module, this);
		}
		return (DataItemHandle) handle;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.core.DesignElement#getDisplayLabel(org.eclipse.
	 * birt.report.model.elements.ReportDesign, int)
	 */

	@Override
	public String getDisplayLabel(Module module, int level) {
		String displayLabel = super.getDisplayLabel(module, level);
		if (level == IDesignElementModel.FULL_LABEL) {
			String valueExpr = handle(module).getResultSetExpression();
			if (!StringUtil.isBlank(valueExpr)) {
				valueExpr = limitStringLength(valueExpr);
				displayLabel += "(" + valueExpr + ")"; //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		return displayLabel;
	}
}
