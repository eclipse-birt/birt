/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.PropertyDescriptorProvider;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabReportItemConstants;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;

public class RepeatHeaderProvider extends PropertyDescriptorProvider {

	public RepeatHeaderProvider(String property, String element) {
		super(property, element);
	}

	public String getDisplayName() {
		String displayName = super.getDisplayName();
		if (displayName != null && displayName.length() > 0) {
			return displayName;
		}
		if (ICrosstabReportItemConstants.REPEAT_COLUMN_HEADER_PROP.equals(property))
			return Messages.getString("RowPageBreak.RepeatColumnHeader"); //$NON-NLS-1$
		else if (ICrosstabReportItemConstants.REPEAT_ROW_HEADER_PROP.equals(property))
			return Messages.getString("ColumnPageBreak.RepeatRowHeader"); //$NON-NLS-1$
		else
			return "";
	}

}
