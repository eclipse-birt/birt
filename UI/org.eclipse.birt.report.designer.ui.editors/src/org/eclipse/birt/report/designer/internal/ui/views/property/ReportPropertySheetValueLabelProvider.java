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

package org.eclipse.birt.report.designer.internal.ui.views.property;

import org.eclipse.birt.report.designer.core.model.views.property.GroupPropertyHandleWrapper;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.GroupPropertyHandle;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.StyledString;

/**
 * Lable provide for property sheet tree view.
 */
public class ReportPropertySheetValueLabelProvider extends ColumnLabelProvider implements IStyledLabelProvider {

	private static final String PASSWORD_REPLACEMENT = "********";//$NON-NLS-1$

	@Override
	public String getText(Object element) {
		String text = getStyledText(element).toString();
		System.out.println(text);
		return text;
	}

	@Override
	public StyledString getStyledText(Object element) {
		String value = null;
		GroupPropertyHandle propertyHandle = null;
		if (element instanceof GroupPropertyHandleWrapper) {
			propertyHandle = ((GroupPropertyHandleWrapper) element).getModel();

			if (propertyHandle != null) {
				if (propertyHandle.getStringValue() != null) {
					if (propertyHandle.getPropertyDefn().isEncryptable()) {
						value = PASSWORD_REPLACEMENT;
					} else {
						value = propertyHandle.getDisplayValue();
					}
				}
			}
		}
		if (value == null) {
			value = ""; //$NON-NLS-1$
		}
		StyledString styledString = new StyledString();
		styledString.append(value);
		if (propertyHandle != null && propertyHandle.getDisplayValue() != null
				&& propertyHandle.getLocalStringValue() == null) {
			styledString.append(" : " + Messages.getString("ReportPropertySheetPage.Value.Inherited"),
					StyledString.DECORATIONS_STYLER);
		}
		return styledString;
	}

}
