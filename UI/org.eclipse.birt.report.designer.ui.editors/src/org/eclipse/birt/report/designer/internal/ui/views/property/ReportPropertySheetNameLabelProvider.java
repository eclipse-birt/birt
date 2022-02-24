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

package org.eclipse.birt.report.designer.internal.ui.views.property;

import java.util.List;

import org.eclipse.birt.report.designer.core.model.views.property.GroupPropertyHandleWrapper;
import org.eclipse.birt.report.designer.core.model.views.property.PropertySheetRootElement;
import org.eclipse.birt.report.model.api.GroupPropertyHandle;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;

public class ReportPropertySheetNameLabelProvider extends ColumnLabelProvider implements IStyledLabelProvider {

	public String getText(Object element) {
		String text = getStyledText(element).toString();
		System.out.println(text);
		return text;
	}

	public StyledString getStyledText(Object element) {
		String value = null;
		if (element instanceof List) {
			GroupPropertyHandle property = ((GroupPropertyHandleWrapper) (((List) element).get(0))).getModel();
			value = property.getPropertyDefn().getGroupName();
		} else if (element instanceof PropertySheetRootElement) {
			value = ((PropertySheetRootElement) element).getDisplayName();
		} else {
			GroupPropertyHandle property = ((GroupPropertyHandleWrapper) element).getModel();
			value = property.getPropertyDefn().getDisplayName();
		}
		if (value == null)
			value = ""; //$NON-NLS-1$
		StyledString styledString = new StyledString();
		styledString.append(value);
		return styledString;
	}

}
