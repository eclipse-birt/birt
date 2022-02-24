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
import java.util.Arrays;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.dialogs.resource.AddResourceFileFolderSelectionDialog;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.ScriptLibHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.PropertyEvent;
import org.eclipse.birt.report.model.api.elements.structures.ScriptLib;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Table;

/**
 * 
 */
public class JarFileFormProvider extends AbstractFormHandleProvider {

	/**
	 * 
	 */
	public JarFileFormProvider() {
		// TODO Auto-generated constructor stub
	}

	private static final int[] COLUMN_WIDTHS = new int[] { 300 };
	private static final String[] COLUMNS = new String[] { Messages.getString("JarFileFormProvider.Column.Name"), //$NON-NLS-1$
	};
	private static final String TITLE = Messages.getString("ReportPageGenerator.List.Resources.JarFile"); //$NON-NLS-1$
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
		inputElement.shiftScriptLibs(oldPos, newPos);
		return true;
	}

	public boolean doDeleteItem(int pos) throws Exception {
		if (getElements(inputElement).length <= 0) {
			return false;
		}
		ScriptLibHandle scriptLibHandle = ((ScriptLibHandle) getElements(inputElement)[pos]);
		if ((scriptLibHandle != null) && (scriptLibHandle.getStructure() != null)
				&& (scriptLibHandle.getStructure() instanceof ScriptLib)) {
			ScriptLib scriptLib = (ScriptLib) (scriptLibHandle.getStructure());
			inputElement.dropScriptLib(scriptLib);
		}

		return true;
	}

	public boolean doAddItem(int pos) throws Exception {

		AddResourceFileFolderSelectionDialog dialog = new AddResourceFileFolderSelectionDialog(new String[] { "*.jar" }, //$NON-NLS-1$
				new String[] { ".jar" }); //$NON-NLS-1$
		dialog.setHelpDialogId(IHelpContextIds.ADD_JAR_FILES_DIALOG_ID);
		dialog.setAllowImportFile(true);
		dialog.setExistFiles(getElmentNames(inputElement));

		if (dialog.open() != Window.OK) {
			return false;
		}
		Object[] selection = dialog.getResult();
		int length = selection.length;
		for (int i = 0; i < length; i++) {
			String fileName = dialog.getPath(i);
			ScriptLib lib = StructureFactory.createScriptLib();
			lib.setName(fileName);
			inputElement.addScriptLib(lib);
		}
		return true;
	}

	public boolean doEditItem(int pos) {
		return false;
	}

	public boolean isDeleteEnable(Object selectedObject) {
		if (selectedObject instanceof StructuredSelection && !((StructuredSelection) selectedObject).isEmpty()) {
			ScriptLibHandle ScriptLibHandle = (ScriptLibHandle) ((StructuredSelection) selectedObject)
					.getFirstElement();
			if (ScriptLibHandle.getElementHandle() != inputElement) {
				return false;
			}
		}
		return true;
	}

	public boolean isUpEnable(Object selectedObject) {
		if (selectedObject instanceof StructuredSelection && !((StructuredSelection) selectedObject).isEmpty()) {
			ScriptLibHandle ScriptLibHandle = (ScriptLibHandle) ((StructuredSelection) selectedObject)
					.getFirstElement();
			if (ScriptLibHandle.getElementHandle() != inputElement) {
				return false;
			} else {
				List handles = Arrays.asList(getElements(inputElement));
				int index = -1;
				for (int i = 0; i < handles.size(); i++) {
					ScriptLibHandle handle = (ScriptLibHandle) handles.get(i);
					if (handle.getName().equals(ScriptLibHandle.getName())) {
						index = i;
						break;
					}
				}
				if (index > 0) {
					ScriptLibHandle nextHandle = (ScriptLibHandle) handles.get(index - 1);
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
			ScriptLibHandle ScriptLibHandle = (ScriptLibHandle) ((StructuredSelection) selectedObject)
					.getFirstElement();
			if (ScriptLibHandle.getElementHandle() != inputElement) {
				return false;
			} else {
				List handles = Arrays.asList(getElements(inputElement));
				int index = -1;
				for (int i = 0; i < handles.size(); i++) {
					ScriptLibHandle handle = (ScriptLibHandle) handles.get(i);
					if (handle.getName().equals(ScriptLibHandle.getName())) {
						index = i;
						break;
					}
				}
				if (handles.size() > index + 1) {
					ScriptLibHandle nextHandle = (ScriptLibHandle) handles.get(index + 1);
					if (nextHandle.getElementHandle() != inputElement) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof ScriptLibHandle) {
			ScriptLibHandle srcriptLibHandle = (ScriptLibHandle) element;
			if (columnIndex == 0) {
				return srcriptLibHandle.getName();
			}
		}
		return EMPTY_STRING;
	}

	public Image getImage(Object element, int columnIndex) {
		return null;
	}

	public Object[] getElements(Object inputElement) {
		List list = new ArrayList();
		if (inputElement instanceof List) {
			inputElement = ((List) inputElement).get(0);
		}
		if (inputElement instanceof ModuleHandle) {
			this.inputElement = (ModuleHandle) inputElement;
			list = ((ModuleHandle) inputElement).getAllScriptLibs();
			if (list == null || list.size() == 0) {
				return new ScriptLibHandle[0];
			}
		}

		return list.toArray();
	}

	private String[] getElmentNames(Object inputElement) {
		Object[] obj = getElements(inputElement);
		String[] names = new String[obj.length];
		for (int i = 0; i < names.length; i++) {
			names[i] = ((ScriptLibHandle) (obj[i])).getName();
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
		if (propertyEvent.getPropertyName().equals(ModuleHandle.SCRIPTLIBS_PROP)) {
			return true;
		}
		return false;
	}

}
