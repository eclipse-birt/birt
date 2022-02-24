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
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.dialogs.resource.NewResourceFileDialog;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.PropertyEvent;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Table;

/**
 * 
 */
public class PropertiesFileFormProvider extends AbstractFormHandleProvider {

	/**
	 * 
	 */
	public PropertiesFileFormProvider() {
		// TODO Auto-generated constructor stub
	}

	private static final int[] COLUMN_WIDTHS = new int[] { 300 };
	private static final String[] COLUMNS = new String[] { Messages.getString("PropertiesFileFormProvider.Column.Name"), //$NON-NLS-1$
	};
	private static final String TITLE = Messages.getString("ReportPageGenerator.List.Resources.PropertiesFile"); //$NON-NLS-1$
	private static final String EMPTY_STRING = ""; //$NON-NLS-1$

	private ModuleHandle inputElement;

	public String[] getColumnNames() {
		return COLUMNS;
	}

	public int[] getColumnWidths() {
		return COLUMN_WIDTHS;
	}

	public String getDisplayName() {
		return TITLE;
	}

	public CellEditor[] getEditors(Table table) {
		return null;
	}

	public boolean doMoveItem(int oldPos, int newPos) throws Exception {
		List<String> resources = inputElement.getIncludeResources();
		String resource = resources.get(oldPos);
		resources.remove(oldPos);
		resources.add(newPos, resource);
		inputElement.setIncludeResources(resources);
		return true;
	}

	public boolean doDeleteItem(int pos) throws Exception {
		if (getElements(inputElement).length <= 0) {
			return false;
		}
		List<String> resources = inputElement.getIncludeResources();
		if (resources != null && resources.size() > pos) {
			resources.remove(pos);
			inputElement.setIncludeResources(resources);
			return true;
		}
		return false;
	}

	public boolean doAddItem(int pos) throws Exception {

		NewResourceFileDialog dialog = new NewResourceFileDialog();
		dialog.setAllowImportFile(true);

		if (dialog.open() != Window.OK) {
			return false;
		}
		Object[] selection = dialog.getResult();
		int length = selection.length;
		for (int i = 0; i < length; i++) {
			String path = dialog.getPath();
			if (path.lastIndexOf(".") > 0) //$NON-NLS-1$
			{
				path = path.substring(0, path.lastIndexOf(".")); //$NON-NLS-1$
			}
			if (inputElement.getIncludeResources() != null) {
				List resources = inputElement.getIncludeResources();
				if (!resources.contains(path)) {
					resources.add(path);
					inputElement.setIncludeResources(resources);
				} else {
					return false;
				}
			} else {
				List<String> resources = new ArrayList<String>();
				resources.add(path);
				inputElement.setIncludeResources(resources);
			}
		}
		return true;
	}

	public boolean doEditItem(int pos) {
		return false;
	}

	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof String) {
			if (columnIndex == 0) {
				return (String) element;
			}
		}
		return EMPTY_STRING;
	}

	public Image getImage(Object element, int columnIndex) {
		return null;
	}

	public Object[] getElements(Object inputElement) {
		ArrayList list = new ArrayList();
		if (inputElement instanceof List) {
			inputElement = ((List) inputElement).get(0);
		}
		if (inputElement instanceof ModuleHandle) {
			this.inputElement = (ModuleHandle) inputElement;
			list = (ArrayList) ((ModuleHandle) inputElement).getIncludeResources();
			if (list == null || list.size() == 0) {
				return new String[0];
			}
		}

		return list.toArray();
	}

	public boolean canModify(Object element, String property) {
		return false;
	}

	public Object getValue(Object element, String property) {
		return null;
	}

	public boolean modify(Object data, String property, Object value) throws Exception {
		return false;
	}

	public boolean needRefreshed(NotificationEvent event) {
		if (!(event instanceof PropertyEvent)) {
			return false;
		}
		PropertyEvent propertyEvent = (PropertyEvent) event;
		if (propertyEvent.getPropertyName().equals(ModuleHandle.INCLUDE_RESOURCE_PROP)) {
			return true;
		}
		return false;
	}

}
