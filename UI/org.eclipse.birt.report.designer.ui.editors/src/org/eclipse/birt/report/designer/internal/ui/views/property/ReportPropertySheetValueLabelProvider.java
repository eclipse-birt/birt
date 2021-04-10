/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.property;

import org.eclipse.birt.report.designer.core.model.views.property.GroupPropertyHandleWrapper;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.GroupPropertyHandle;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;

/**
 * Lable provide for property sheet tree view.
 */
public class ReportPropertySheetValueLabelProvider extends ColumnLabelProvider implements IStyledLabelProvider {

	private static final String PASSWORD_REPLACEMENT = "********";//$NON-NLS-1$

	public String getText(Object element) {
		String text = getStyledText(element).toString();
		System.out.println(text);
		return text;
	}

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
		if (value == null)
			value = ""; //$NON-NLS-1$
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