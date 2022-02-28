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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.dialogs.UserPropertyBuilder;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.UserPropertyEvent;
import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Table;

/**
 *
 */

public class NamedExpressionsHandleProvier extends AbstractFormHandleProvider {

	private static final int[] COLUMN_WIDTHS = { 250, 300 };
	private static final String[] COLUMNS = { Messages.getString("NamedExpressionsHandleProvier.Column.Name"), //$NON-NLS-1$
			Messages.getString("NamedExpressionsHandleProvier.Column.DefaultValue") //$NON-NLS-1$
	};
	private static final String TITLE = Messages.getString("ReportPageGenerator.List.NamedExpressions"); //$NON-NLS-1$

	private DesignElementHandle inputElement;

	@Override
	public String[] getColumnNames() {
		return COLUMNS;
	}

	@Override
	public int[] getColumnWidths() {
		return COLUMN_WIDTHS;
	}

	@Override
	public String getDisplayName() {
		return TITLE;
	}

	@Override
	public CellEditor[] getEditors(Table table) {
		return null;
	}

	@Override
	public boolean doMoveItem(int oldPos, int newPos) throws Exception {
		return false;
	}

	@Override
	public boolean doDeleteItem(int pos) throws Exception {
		inputElement.dropUserPropertyDefn(((UserPropertyDefn) getElements(inputElement)[pos]).getName());
		return true;
	}

	@Override
	public boolean doAddItem(int pos) throws Exception {
		UserPropertyBuilder builder = new UserPropertyBuilder(UserPropertyBuilder.NAMED_EXPRESSION);
		builder.setInput(inputElement);
		if (builder.open() == Dialog.OK) {
			inputElement.addUserPropertyDefn((UserPropertyDefn) builder.getResult());
			return true;
		}
		return false;
	}

	@Override
	public boolean doEditItem(int pos) {
		return false;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		UserPropertyDefn def = (UserPropertyDefn) element;
		if (columnIndex == 0) {
			return def.getName();
		}
		Object defaultValue = def.getDefault();
		if (defaultValue instanceof Expression) {
			return ((Expression) defaultValue).getStringExpression();
		} else if (defaultValue instanceof String) {
			return (String) defaultValue;
		}
		return null;
	}

	@Override
	public Image getImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof List) {
			inputElement = ((List) inputElement).get(0);
		}
		if (inputElement instanceof DesignElementHandle) {
			this.inputElement = (DesignElementHandle) inputElement;
			ArrayList list = new ArrayList();
			for (Iterator iter = ((DesignElementHandle) inputElement).getUserProperties().iterator(); iter.hasNext();) {
				UserPropertyDefn def = (UserPropertyDefn) iter.next();
				if (def.getType().getName().equals(PropertyType.EXPRESSION_TYPE_NAME)) {
					list.add(def);
				}
			}
			return list.toArray();
		}
		return null;
	}

	@Override
	public boolean canModify(Object element, String property) {
		return true;
	}

	@Override
	public Object getValue(Object element, String property) {
		return null;
	}

	@Override
	public boolean modify(Object data, String property, Object value) throws Exception {
		return false;
	}

	@Override
	public boolean needRefreshed(NotificationEvent event) {
		if (event instanceof UserPropertyEvent) {
			return true;
		}
		return false;
	}

}
