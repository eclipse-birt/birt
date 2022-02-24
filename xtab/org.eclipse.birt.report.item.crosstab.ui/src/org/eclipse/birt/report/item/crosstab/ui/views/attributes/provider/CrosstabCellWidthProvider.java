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

package org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.UnitPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;

public class CrosstabCellWidthProvider extends UnitPropertyDescriptorProvider {

	public CrosstabCellWidthProvider(String property, String element) {
		super(property, element);
	}

	public Object load() {
		String text = null;
		try {
			ExtendedItemHandle handle = (ExtendedItemHandle) DEUtil.getInputFirstElement(input);
			if (handle.getReportItem() instanceof CrosstabCellHandle) {
				CrosstabCellHandle cell = (CrosstabCellHandle) handle.getReportItem();
				text = cell.getCrosstab().getColumnWidth(cell).getStringValue();
			}
		} catch (Exception e) {
			ExceptionUtil.handle(e);
		}
		if (text == null)
			return ""; //$NON-NLS-1$
		return text;
	}

	public void save(Object value) throws SemanticException {
		DimensionValue dimensionValue = null;
		if (value != null)
			dimensionValue = DimensionValue.parse(value.toString());
		try {
			ExtendedItemHandle handle = (ExtendedItemHandle) DEUtil.getInputFirstElement(input);
			if (handle.getReportItem() instanceof CrosstabCellHandle) {
				CrosstabCellHandle cell = (CrosstabCellHandle) handle.getReportItem();
				cell.getCrosstab().setColumnWidth(cell, dimensionValue);
			}
		} catch (Exception e) {
			ExceptionUtil.handle(e);
		}
	}
}
