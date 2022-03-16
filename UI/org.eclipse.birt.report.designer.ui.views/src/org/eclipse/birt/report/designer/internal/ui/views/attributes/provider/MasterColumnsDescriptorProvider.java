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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ComplexUnitSection;

/**
 *
 */

public class MasterColumnsDescriptorProvider extends PropertyDescriptorProvider {

	public static final String THREE_COLUMNS = "3"; //$NON-NLS-1$
	public static final String TWO_COLUMNS = "2"; //$NON-NLS-1$
	public static final String ONE_COLUMN = "1"; //$NON-NLS-1$

	public MasterColumnsDescriptorProvider(String property, String element) {
		super(property, element);
	}

	private ComplexUnitSection columnSpaceSection;

	public void setColumnSpaceSection(ComplexUnitSection columnSpaceSection) {
		this.columnSpaceSection = columnSpaceSection;
	}

	@Override
	public Object load() {
		Object value = super.load();
		if (columnSpaceSection != null) {
			if (value == null || value.toString().equals(ONE_COLUMN)) // $NON-NLS-1$
			{
				columnSpaceSection.getUnitComboControl().setReadOnly(true);
			} else { // $NON-NLS-1$
				columnSpaceSection.getUnitComboControl().setReadOnly(false);
			}
		}
		return value;

	}
}
