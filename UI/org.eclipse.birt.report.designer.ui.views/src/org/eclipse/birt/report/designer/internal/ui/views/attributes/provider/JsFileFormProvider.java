/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.dialogs.resource.AddResourceFileFolderSelectionDialog;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.IncludeScriptHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.PropertyEvent;
import org.eclipse.birt.report.model.api.elements.structures.IncludeScript;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Table;

/**
 * 
 */
public class JsFileFormProvider extends AbstractFormHandleProvider {

	/**
	 * 
	 */
	public JsFileFormProvider() {
		// TODO Auto-generated constructor stub
	}

	private static final int[] COLUMN_WIDTHS = new int[] { 300 };
	private static final String[] COLUMNS = new String[] { Messages.getString("JsFileFormProvider.Column.Name"), //$NON-NLS-1$
	};
	private static final String TITLE = Messages.getString("ReportPageGenerator.List.Resources.JsFile"); //$NON-NLS-1$
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
		inputElement.shifIncludeScripts(oldPos, newPos);
		return true;
	}

	public boolean doDeleteItem(int pos) throws Exception {
		if (getElements(inputElement).length <= 0) {
			return false;
		}
		IncludeScriptHandle includeScriptHandle = ((IncludeScriptHandle) getElements(inputElement)[pos]);
		if ((includeScriptHandle != null) && (includeScriptHandle.getStructure() != null)
				&& (includeScriptHandle.getStructure() instanceof IncludeScript)) {
			IncludeScript includeSript = (IncludeScript) (includeScriptHandle.getStructure());
			inputElement.dropIncludeScript(includeSript);
		}

		return true;
	}

	public boolean isDeleteEnable(Object selectedObject) {
		if (selectedObject instanceof StructuredSelection && !((StructuredSelection) selectedObject).isEmpty()) {
			IncludeScriptHandle includeScriptHandle = (IncludeScriptHandle) ((StructuredSelection) selectedObject)
					.getFirstElement();
			if (includeScriptHandle.getElementHandle() != inputElement) {
				return false;
			}
		}
		return true;
	}

	public boolean isUpEnable(Object selectedObject) {
		if (selectedObject instanceof StructuredSelection && !((StructuredSelection) selectedObject).isEmpty()) {
			IncludeScriptHandle includeScriptHandle = (IncludeScriptHandle) ((StructuredSelection) selectedObject)
					.getFirstElement();
			if (includeScriptHandle.getElementHandle() != inputElement) {
				return false;
			} else {
				List handles = Arrays.asList(getElements(inputElement));
				int index = -1;
				for (int i = 0; i < handles.size(); i++) {
					IncludeScriptHandle handle = (IncludeScriptHandle) handles.get(i);
					if (handle.getFileName().equals(includeScriptHandle.getFileName())) {
						index = i;
						break;
					}
				}
				if (index > 0) {
					IncludeScriptHandle nextHandle = (IncludeScriptHandle) handles.get(index - 1);
					if (nextHandle.getElementHandle() != inputElement) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public boolean isDownEnable(Object selectedObject) {
		if (selectedObject instanceof StructuredSelection && !((StructuredSelection) selectedObject).isEmpty()) {
			IncludeScriptHandle includeScriptHandle = (IncludeScriptHandle) ((StructuredSelection) selectedObject)
					.getFirstElement();
			if (includeScriptHandle.getElementHandle() != inputElement) {
				return false;
			} else {
				List handles = Arrays.asList(getElements(inputElement));
				int index = -1;
				for (int i = 0; i < handles.size(); i++) {
					IncludeScriptHandle handle = (IncludeScriptHandle) handles.get(i);
					if (handle.getFileName().equals(includeScriptHandle.getFileName())) {
						index = i;
						break;
					}
				}
				if (handles.size() > index + 1) {
					IncludeScriptHandle nextHandle = (IncludeScriptHandle) handles.get(index + 1);
					if (nextHandle.getElementHandle() != inputElement) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public boolean doAddItem(int pos) throws Exception {

		AddResourceFileFolderSelectionDialog dialog = new AddResourceFileFolderSelectionDialog(new String[] { "*.js" },
				new String[] { ".js" });
		dialog.setHelpDialogId(IHelpContextIds.ADD_JS_FILES_DIALOG_ID);
		dialog.setAllowImportFile(true);
		dialog.setExistFiles(getElmentNames(inputElement));

		if (dialog.open() != Window.OK) {
			return false;
		}
		Object[] selection = dialog.getResult();
		int length = selection.length;
		for (int i = 0; i < length; i++) {
			String fileName = dialog.getPath(i);
			IncludeScript script = StructureFactory.createIncludeScript();
			script.setFileName(fileName);
			inputElement.addIncludeScript(script);
		}
		return true;
	}

	public boolean doEditItem(int pos) {
		return false;
	}

	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof IncludeScriptHandle) {
			IncludeScriptHandle srcriptHandle = (IncludeScriptHandle) element;
			if (columnIndex == 0) {
				return srcriptHandle.getFileName();
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
			list = (ArrayList) ((ModuleHandle) inputElement).getAllIncludeScripts();
			if (list == null || list.size() == 0) {
				return new IncludeScriptHandle[0];
			}
		}

		return list.toArray();
	}

	private String[] getElmentNames(Object inputElement) {
		Object[] obj = getElements(inputElement);
		String[] names = new String[obj.length];
		for (int i = 0; i < names.length; i++) {
			names[i] = ((IncludeScriptHandle) (obj[i])).getFileName();
		}
		return names;
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
		if (propertyEvent.getPropertyName().equals(ModuleHandle.INCLUDE_SCRIPTS_PROP)) {
			return true;
		}
		return false;
	}

}
