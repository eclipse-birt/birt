/*******************************************************************************
 * Copyright (c) 2004,2005 Actuate Corporation.
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

package org.eclipse.birt.report.designer.ui.preferences;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TableItem;

/**
 * This class implements an ICellModifier An ICellModifier is called when the
 * user modifes a cell in the tableViewer
 */

public class ElementNamesCellModifier implements ICellModifier {

	private ElementNamesConfigurationBlock elementPreferPage;
	private static final String columnModifyEnabled[] = {
			Messages.getString("designer.preview.preference.elementname.defaultname.Enabled").trim(), //$NON-NLS-1$
			Messages.getString("designer.preview.preference.elementname.customname.Enabled").trim(), //$NON-NLS-1$
			Messages.getString("designer.preview.preference.elementname.description.Enabled").trim() //$NON-NLS-1$

	};
	private static final String enabledFlag = Messages.getString("designer.preview.preference.elementname.Enabled") //$NON-NLS-1$
			.trim();

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.ICellModifier#canModify(java.lang.Object,
	 * java.lang.String)
	 */

	public ElementNamesCellModifier(ElementNamesConfigurationBlock elementPreferPage) {
		super();
		this.elementPreferPage = elementPreferPage;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.ICellModifier#canModify(java.lang.Object,
	 * java.lang.String)
	 */
	@Override
	public boolean canModify(Object element, String property) {
		// TODO Auto-generated method stub
		int columnIndex = elementPreferPage.getElementNames().indexOf(property);

		if ((columnIndex < 0) && (columnIndex >= columnModifyEnabled.length)) {
			return false;
		}
		if (columnModifyEnabled[columnIndex].equalsIgnoreCase(enabledFlag)) {
			return true;
		}
		return false;

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.ICellModifier#getValue(java.lang.Object,
	 * java.lang.String)
	 */
	@Override
	public Object getValue(Object element, String property) {
		// Find the index of the column
		int columnIndex = elementPreferPage.getElementNames().indexOf(property);

		Object result = null;
		ItemContent content = (ItemContent) element;

		switch (columnIndex) {
		case 0: // Element name column
			result = content.getDefaultName();
			break;
		case 1: // Default name column
			result = content.getCustomName();
			break;
		case 2: // Description column
			result = content.getDescription();
			break;
		default:
			result = ""; //$NON-NLS-1$
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object,
	 * java.lang.String, java.lang.Object)
	 */
	@Override
	public void modify(Object element, String property, Object value) {
		// Find the index of the column
		int columnIndex = elementPreferPage.getElementNames().indexOf(property);

		TableItem item = (TableItem) element;
		ItemContent content = (ItemContent) item.getData();

		String valueString;
		valueString = ((String) value).trim();

		switch (columnIndex) {
		case 0: // Element Name column
			content.setDefaultName(valueString);
			break;
		case 1: // Default Name column
			content.setCustomName(valueString);
			break;
		case 2: // Description column
			content.setDescription(valueString);

		default:
		}
		elementPreferPage.getContentList().contentChanged(content);

	}

}
