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

package org.eclipse.birt.report.designer.internal.ui.ide.adapters;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.birt.report.designer.internal.ui.preferences.ITreeListAdapter;
import org.eclipse.birt.report.designer.internal.ui.preferences.LayoutUtil;
import org.eclipse.birt.report.designer.internal.ui.preferences.TreeListDialogField;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.preferences.DialogField;
import org.eclipse.birt.report.designer.ui.preferences.IDialogFieldListener;
import org.eclipse.birt.report.designer.ui.preferences.IStatusChangeListener;
import org.eclipse.birt.report.designer.ui.preferences.OptionsConfigurationBlock;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaElementComparator;
import org.eclipse.jdt.ui.wizards.BuildPathDialogAccess;
import org.eclipse.jface.layout.PixelConverter;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.dialogs.ListSelectionDialog;

/**
 *
 */

public class IDEClassPathBlock extends OptionsConfigurationBlock {

	private static Key PREF_CLASSPATH;
	private final TreeListDialogField fLibrariesList;

	// private Control fSWTControl;

	private static final int IDX_ADDJAR = 0;
	private static final int IDX_ADDEXT = 1;
	private static final int IDX_ADDVAR = 2;

	private static final int IDX_ADDFOL = 3;
	private static final int IDX_ADDEXTFOL = 4;
	private static final int IDX_ADDPROJECT = 5;

	private static final int IDX_UP = 7;
	private static final int IDX_DOWN = 8;
	private static final int IDX_EDIT = 9;
	private static final int IDX_REMOVE = 10;

	private static final int UNKNOW_TYPE = -1;
	private static int JAR_TYPE = 0;
	private static final int EXTJAR_TYPE = 1;

	private static final int VAR_TYPE = 2;
	private static final int FOL_TYPE = 3;
	private static final int ADDFOL_TYPE = 4;
	private static final int PROJECT_TYPE = 5;

	private static final String ENTRY_SEPARATOR = "|"; //$NON-NLS-1$
	private static final String TYPE_SEPARATOR = "*"; //$NON-NLS-1$

	public IDEClassPathBlock(IStatusChangeListener context, IProject project) {

		super(context, ReportPlugin.getDefault(), project);
		// fSWTControl = null;
		PREF_CLASSPATH = getReportKey(ReportPlugin.CLASSPATH_PREFERENCE);
		String[] buttonLabels = { Messages.getString("IDEClassPathBlock.button_addJar"), //$NON-NLS-1$
				Messages.getString("IDEClassPathBlock.button_addEXTJar"), //$NON-NLS-1$
				Messages.getString("IDEClassPathBlock.button_addVar"), //$NON-NLS-1$

				Messages.getString("IDEClassPathBlock.button_folder"), //$NON-NLS-1$
				Messages.getString("IDEClassPathBlock.button_addExtFolder"), //$NON-NLS-1$
				Messages.getString("IDEClassPathBlock.button_addProject"), //$NON-NLS-1$
				/* */null, Messages.getString("IDEClassPathBlock.button_up"), //$NON-NLS-1$
				Messages.getString("IDEClassPathBlock.button_down"), //$NON-NLS-1$
				Messages.getString("IDEClassPathBlock.button_edit"), //$NON-NLS-1$
				Messages.getString("IDEClassPathBlock.button_remove"), //$NON-NLS-1$
		};

		LibrariesAdapter adapter = new LibrariesAdapter();

		fLibrariesList = new TreeListDialogField(adapter, buttonLabels, new IDECPListLabelProvider());
		fLibrariesList.setDialogFieldListener(adapter);
		fLibrariesList.setLabelText(Messages.getString("IDEClassPathBlock.fLibrariesList_text")); //$NON-NLS-1$

		fLibrariesList.enableButton(IDX_UP, false);
		fLibrariesList.enableButton(IDX_DOWN, false);
		fLibrariesList.enableButton(IDX_REMOVE, false);
		fLibrariesList.enableButton(IDX_EDIT, false);
		setKeys(getKeys());

		fLibrariesList.setElements(readClassPathEntry(getValue(PREF_CLASSPATH)));

	}

	private Key[] getKeys() {
		Key[] keys = { PREF_CLASSPATH };
		return keys;
	}

	// -------- UI creation

	@Override
	public Control createContents(Composite parent) {
		setShell(parent.getShell());
		PixelConverter converter = new PixelConverter(parent);

		Composite composite = new Composite(parent, SWT.NONE);
		if (getProject() == null) {
			fLibrariesList.removeButton(IDX_ADDPROJECT);
		}
		LayoutUtil.doDefaultLayout(composite, new DialogField[] { fLibrariesList }, true, SWT.DEFAULT, SWT.DEFAULT);
		LayoutUtil.setHorizontalGrabbing(fLibrariesList.getTreeControl(null));

		int buttonBarWidth = converter.convertWidthInCharsToPixels(24);
		fLibrariesList.setButtonsMinWidth(buttonBarWidth);

		// fSWTControl = composite;

		return composite;
	}

	private class LibrariesAdapter implements IDialogFieldListener, ITreeListAdapter {

		private final Object[] EMPTY_ARR = {};

		// -------- IListAdapter --------
		@Override
		public void customButtonPressed(TreeListDialogField field, int index) {
			libaryPageCustomButtonPressed(field, index);
		}

		@Override
		public void selectionChanged(TreeListDialogField field) {
			libaryPageSelectionChanged(field);
		}

		@Override
		public void doubleClicked(TreeListDialogField field) {
			libaryPageDoubleClicked(field);
		}

		@Override
		public void keyPressed(TreeListDialogField field, KeyEvent event) {
			libaryPageKeyPressed(field, event);
		}

		@Override
		public Object[] getChildren(TreeListDialogField field, Object element) {
			if (element instanceof IDECPListElement) {
				return ((IDECPListElement) element).getChildren(false);
			}

			return EMPTY_ARR;
		}

		@Override
		public Object getParent(TreeListDialogField field, Object element) {

			return null;
		}

		@Override
		public boolean hasChildren(TreeListDialogField field, Object element) {
			return getChildren(field, element).length > 0;
		}

		// ---------- IDialogFieldListener --------

		@Override
		public void dialogFieldChanged(DialogField field) {
			libaryPageDialogFieldChanged(field);
		}
	}

	/**
	 * A button has been pressed.
	 *
	 * @param field the dialog field containing the button
	 * @param index the index of the button
	 */
	private void libaryPageCustomButtonPressed(DialogField field, int index) {
		IDECPListElement[] libentries = null;
		switch (index) {
		case IDX_ADDJAR: /* add jar */
			libentries = openJarFileDialog(null);
			break;
		case IDX_ADDEXT: /* add external jar */
			libentries = openExtJarFileDialog(null);
			break;
		case IDX_ADDVAR: /* add variable */
			libentries = openVariableSelectionDialog(null);
			break;
		case IDX_ADDFOL: /* add folder */
			libentries = openClassFolderDialog(null);
			break;
		case IDX_ADDEXTFOL: /* add external folder */
			libentries = openExternalClassFolderDialog(null);
			break;
		case IDX_ADDPROJECT: /* add project */
			libentries = addProjectDialog();
			break;
		case IDX_UP: /* add external folder */
		{
			libentries = (IDECPListElement[]) getSelection().toArray(new IDECPListElement[getSelection().size()]);
			fLibrariesList.up();
			break;
		}
		case IDX_DOWN: /* add external folder */
		{
			libentries = (IDECPListElement[]) getSelection().toArray(new IDECPListElement[getSelection().size()]);
			fLibrariesList.down();
			break;
		}
		case IDX_EDIT: /* edit */
			editEntry();
			return;
		case IDX_REMOVE: /* remove */
			removeEntry();
			return;
		}
		if (libentries != null) {
			int nElementsChosen = libentries.length;
			// remove duplicates
			List cplist = fLibrariesList.getElements();
			List elementsToAdd = new ArrayList(nElementsChosen);

			for (int i = 0; i < nElementsChosen; i++) {
				IDECPListElement curr = libentries[i];
				if (!cplist.contains(curr) && !elementsToAdd.contains(curr)) {
					elementsToAdd.add(curr);
				}
			}

			fLibrariesList.addElements(elementsToAdd);

			fLibrariesList.postSetSelection(new StructuredSelection(libentries));
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.jdt.internal.ui.wizards.buildpaths.BuildPathBasePage#addElement
	 * (org.eclipse.jdt.internal.ui.wizards.buildpaths.CPListElement)
	 */
	public void addElement(IDECPListElement element) {
		fLibrariesList.addElement(element);
		fLibrariesList.postSetSelection(new StructuredSelection(element));
	}

	protected void libaryPageDoubleClicked(TreeListDialogField field) {
		List selection = field.getSelectedElements();
		if (canEdit(selection)) {
			editEntry();
		}
	}

	protected void libaryPageKeyPressed(TreeListDialogField field, KeyEvent event) {
		if (field == fLibrariesList) {
			if (event.character == SWT.DEL && event.stateMask == 0) {
				List selection = field.getSelectedElements();
				if (canRemove(selection)) {
					removeEntry();
				}
			}
		}
	}

	private void removeEntry() {
		List selElements = fLibrariesList.getSelectedElements();
		// HashMap containerEntriesToUpdate = new HashMap( );

		if (selElements.isEmpty()) {
			fLibrariesList.refresh();
		} else {
			fLibrariesList.removeElements(selElements);
		}

	}

	private boolean canRemove(List selElements) {
		if (selElements.size() == 0) {
			return false;
		}
		return true;
	}

	/**
	 * Method editEntry.
	 */
	private void editEntry() {
		List selElements = fLibrariesList.getSelectedElements();
		if (selElements.size() != 1) {
			return;
		}
		Object elem = selElements.get(0);
		if (fLibrariesList.getIndexOfElement(elem) != -1) {
			editElementEntry((IDECPListElement) elem);
		}
	}

	private void editElementEntry(IDECPListElement elem) {
		IDECPListElement[] res = null;

		switch (elem.getEntryKind()) {
		case IClasspathEntry.CPE_LIBRARY:
			IResource resource = elem.getResource();
			if (resource == null) {
				File file = elem.getPath().toFile();
				if (file.isDirectory()) {
					res = openExternalClassFolderDialog(elem);
				} else {
					res = openExtJarFileDialog(elem);
				}
			} else if (resource.getType() == IResource.FILE) {
				res = openJarFileDialog(elem);
			}
			break;
		case IClasspathEntry.CPE_VARIABLE:
			res = openVariableSelectionDialog(elem);
			break;
		}
		if (res != null && res.length > 0) {
			IDECPListElement curr = res[0];
			curr.setExported(elem.isExported());
			// curr.setAttributesFromExisting(elem);
			fLibrariesList.replaceElement(elem, curr);
			if (elem.getEntryKind() == IClasspathEntry.CPE_VARIABLE) {
				fLibrariesList.refresh();
			}
		}

	}

	/**
	 * @param field the dilaog field
	 */
	private void libaryPageSelectionChanged(DialogField field) {
		updateEnabledState();
	}

	private void updateEnabledState() {
		List selElements = fLibrariesList.getSelectedElements();
		fLibrariesList.enableButton(IDX_EDIT, canEdit(selElements));
		fLibrariesList.enableButton(IDX_REMOVE, canRemove(selElements));

		boolean noAttributes = containsOnlyTopLevelEntries(selElements);
		fLibrariesList.enableButton(IDX_ADDEXT, noAttributes);
		fLibrariesList.enableButton(IDX_ADDFOL, noAttributes);
		fLibrariesList.enableButton(IDX_ADDEXTFOL, noAttributes);
		fLibrariesList.enableButton(IDX_ADDJAR, noAttributes);

		fLibrariesList.enableButton(IDX_ADDVAR, noAttributes);
		fLibrariesList.enableButton(IDX_UP, fLibrariesList.canMoveUp(selElements));
		fLibrariesList.enableButton(IDX_DOWN, fLibrariesList.canMoveDown(selElements));
	}

	private boolean canEdit(List selElements) {
		if (selElements.size() != 1) {
			return false;
		}
		Object elem = selElements.get(0);
		if (elem instanceof IDECPListElement) {
			IDECPListElement curr = (IDECPListElement) elem;
			return !(curr.getResource() instanceof IFolder || curr.getResource() instanceof IProject)
					&& curr.getParentContainer() == null;
		}

		return false;
	}

	/**
	 * @param field the dialog field
	 */
	private void libaryPageDialogFieldChanged(DialogField field) {
		// donothing now
	}

	private IDECPListElement[] openClassFolderDialog(IDECPListElement existing) {
		if (existing == null) {
			IPath[] selected = BuildPathDialogAccess.chooseClassFolderEntries(getShell(),
					getProject() == null ? null : getProject().getLocation(), getUsedContainers(existing));
			if (selected != null) {
				IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
				ArrayList res = new ArrayList();
				for (int i = 0; i < selected.length; i++) {
					IPath curr = selected[i];
					IResource resource = root.findMember(curr);
					if (resource instanceof IContainer) {
						res.add(newCPLibraryElement(resource));
					}
				}
				return (IDECPListElement[]) res.toArray(new IDECPListElement[res.size()]);
			}
		} else {
			// disabled
		}
		return null;
	}

	private IDECPListElement[] openJarFileDialog(IDECPListElement existing) {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		if (existing == null) {
			IPath[] selected = BuildPathDialogAccess.chooseJAREntries(getShell(),
					getProject() == null ? null : getProject().getLocation(), getUsedJARFiles(existing));
			if (selected != null) {
				ArrayList res = new ArrayList();

				for (int i = 0; i < selected.length; i++) {
					IPath curr = selected[i];
					IResource resource = root.findMember(curr);
					if (resource instanceof IFile) {
						res.add(newCPLibraryElement(resource));
					}
				}
				return (IDECPListElement[]) res.toArray(new IDECPListElement[res.size()]);
			}
		} else {
			IPath configured = BuildPathDialogAccess.configureJAREntry(getShell(), existing.getPath(),
					getUsedJARFiles(existing));
			if (configured != null) {
				IResource resource = root.findMember(configured);
				if (resource instanceof IFile) {
					return new IDECPListElement[] { newCPLibraryElement(resource) };
				}
			}
		}
		return null;
	}

	private IPath[] getUsedContainers(IDECPListElement existing) {
		ArrayList res = new ArrayList();

		List cplist = fLibrariesList.getElements();
		for (int i = 0; i < cplist.size(); i++) {
			IDECPListElement elem = (IDECPListElement) cplist.get(i);
			if (elem.getEntryKind() == IClasspathEntry.CPE_LIBRARY && (elem != existing)) {
				IResource resource = elem.getResource();
				if (resource instanceof IContainer && !resource.equals(existing)) {
					res.add(resource.getFullPath());
				}
			}
		}
		return (IPath[]) res.toArray(new IPath[res.size()]);
	}

	private IPath[] getUsedJARFiles(IDECPListElement existing) {
		List res = new ArrayList();
		List cplist = fLibrariesList.getElements();
		for (int i = 0; i < cplist.size(); i++) {
			IDECPListElement elem = (IDECPListElement) cplist.get(i);
			if (elem.getEntryKind() == IClasspathEntry.CPE_LIBRARY && (elem != existing)) {
				IResource resource = elem.getResource();
				if (resource instanceof IFile) {
					res.add(resource.getFullPath());
				}
			}
		}
		return (IPath[]) res.toArray(new IPath[res.size()]);
	}

	private static IDECPListElement newCPLibraryElement(IResource res) {
		return new IDECPListElement(IClasspathEntry.CPE_LIBRARY, res.getFullPath(), res);
	}

	private IDECPListElement[] openExtJarFileDialog(IDECPListElement existing) {
		if (existing == null) {
			IPath[] selected = BuildPathDialogAccess.chooseExternalJAREntries(getShell());
			if (selected != null) {
				ArrayList res = new ArrayList();
				for (int i = 0; i < selected.length; i++) {
					res.add(new IDECPListElement(IClasspathEntry.CPE_LIBRARY, selected[i], null));
				}
				return (IDECPListElement[]) res.toArray(new IDECPListElement[res.size()]);
			}
		} else {
			IPath path = existing.getPath();
			IPath configured = BuildPathDialogAccess.configureExternalJAREntry(getShell(), path);
			if (configured != null) {
				return new IDECPListElement[] { new IDECPListElement(IClasspathEntry.CPE_LIBRARY, configured, null) };
			}
		}
		return null;
	}

	private IDECPListElement[] openExternalClassFolderDialog(IDECPListElement existing) {
		if (existing == null) {
			IPath[] selected = BuildPathDialogAccess.chooseExternalClassFolderEntries(getShell());
			if (selected != null) {
				ArrayList res = new ArrayList();
				for (int i = 0; i < selected.length; i++) {
					res.add(new IDECPListElement(IClasspathEntry.CPE_LIBRARY, selected[i], null));
				}
				return (IDECPListElement[]) res.toArray(new IDECPListElement[res.size()]);
			}
		} else {
			IPath configured = BuildPathDialogAccess.configureExternalClassFolderEntries(getShell(),
					existing.getPath());
			if (configured != null) {
				return new IDECPListElement[] { new IDECPListElement(IClasspathEntry.CPE_LIBRARY, configured, null) };
			}
		}
		return null;
	}

	private static IDECPListElement createCPVariableElement(IPath path) {
		IDECPListElement elem = new IDECPListElement(IClasspathEntry.CPE_VARIABLE, path, null);
		IPath resolvedPath = JavaCore.getResolvedVariablePath(path);
		elem.setIsMissing((resolvedPath == null) || !resolvedPath.toFile().exists());
		return elem;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.jdt.internal.ui.wizards.buildpaths.BuildPathBasePage#isEntryKind
	 * (int)
	 */
	public boolean isEntryKind(int kind) {
		return kind == IClasspathEntry.CPE_LIBRARY || kind == IClasspathEntry.CPE_PROJECT
				|| kind == IClasspathEntry.CPE_VARIABLE || kind == IClasspathEntry.CPE_CONTAINER;
	}

	/*
	 * @see BuildPathBasePage#getSelection
	 */
	public List getSelection() {
		return fLibrariesList.getSelectedElements();
	}

	/*
	 * @see BuildPathBasePage#setSelection
	 */
	public void setSelection(List selElements, boolean expand) {
		fLibrariesList.selectElements(new StructuredSelection(selElements));
		if (expand) {
			for (int i = 0; i < selElements.size(); i++) {
				fLibrariesList.expandElement(selElements.get(i), 1);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void setFocus() {
		fLibrariesList.setFocus();
	}

	private IDECPListElement[] openVariableSelectionDialog(IDECPListElement existing) {
		List existingElements = fLibrariesList.getElements();
		ArrayList existingPaths = new ArrayList(existingElements.size());
		for (int i = 0; i < existingElements.size(); i++) {
			IDECPListElement elem = (IDECPListElement) existingElements.get(i);
			if (elem.getEntryKind() == IClasspathEntry.CPE_VARIABLE) {
				existingPaths.add(elem.getPath());
			}
		}
		IPath[] existingPathsArray = (IPath[]) existingPaths.toArray(new IPath[existingPaths.size()]);

		if (existing == null) {
			IPath[] paths = BuildPathDialogAccess.chooseVariableEntries(getShell(), existingPathsArray);
			if (paths != null) {
				ArrayList result = new ArrayList();
				for (int i = 0; i < paths.length; i++) {
					IPath path = paths[i];
					IDECPListElement elem = createCPVariableElement(path);
					if (!existingElements.contains(elem)) {
						result.add(elem);
					}
				}
				return (IDECPListElement[]) result.toArray(new IDECPListElement[result.size()]);
			}
		} else {
			IPath path = BuildPathDialogAccess.configureVariableEntry(getShell(), existing.getPath(),
					existingPathsArray);
			if (path != null) {
				return new IDECPListElement[] { createCPVariableElement(path) };
			}
		}
		return null;
	}

	protected boolean containsOnlyTopLevelEntries(List selElements) {
		if (selElements.size() == 0) {
			return true;
		}
		for (int i = 0; i < selElements.size(); i++) {
			Object elem = selElements.get(i);
			if (elem instanceof IDECPListElement) {
				if (((IDECPListElement) elem).getParentContainer() != null) {
					return false;
				}
			} else {
				return false;
			}
		}
		return true;
	}

	public static List<IClasspathEntry> getEntries(String value) {
		List elements = readClassPathEntry(value);
		List<IClasspathEntry> retValue = new ArrayList<>();
		for (int i = 0; i < elements.size(); i++) {
			retValue.add(((IDECPListElement) elements.get(i)).getClasspathEntry());
		}

		return retValue;
	}

	private static List readClassPathEntry(String value) {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		List retValue = new ArrayList();

		if (value == null || value.length() == 0) {
			return retValue;
		}
		StringTokenizer tokenizer = new StringTokenizer(value, ENTRY_SEPARATOR);

		// String[] entries = value.split( ENTRY_SEPARATOR );
		while (tokenizer.hasMoreTokens()) {
			String entry = tokenizer.nextToken();
			if (entry == null || entry.length() == 0) {
				continue;
			}
			StringTokenizer typeTokenizer = new StringTokenizer(entry, TYPE_SEPARATOR);
			// String[] types = entry.split( TYPE_SEPARATOR );
			if (typeTokenizer.countTokens() != 3) {
				continue;
			}
			int init = 0;
			IPath path = null;
			int type = UNKNOW_TYPE;
			while (typeTokenizer.hasMoreTokens()) {
				String str = typeTokenizer.nextToken();
				if (init == 1) {
					type = Integer.parseInt(str);
				}
				if (init == 2) {
					path = new Path(str);
				}
				init++;
			}
			if (type == JAR_TYPE || type == FOL_TYPE) {
				IResource resource = root.findMember(path);
				if (resource != null) {
					retValue.add(newCPLibraryElement(resource));
				}
			} else if (type == EXTJAR_TYPE || type == ADDFOL_TYPE) {
				retValue.add(new IDECPListElement(IClasspathEntry.CPE_LIBRARY, path, null));
			} else if (type == VAR_TYPE) {
				retValue.add(createCPVariableElement(path));
			} else if (type == PROJECT_TYPE) {
				IResource resource = root.findMember(path);
				if (resource != null) {
					retValue.add(new IDECPListElement(IClasspathEntry.CPE_PROJECT, resource.getFullPath(), resource));
				}
			}
		}

		return retValue;
	}

	@Override
	public boolean performApply() {
		StringBuilder value = new StringBuilder(); // $NON-NLS-1$
		List list = fLibrariesList.getElements();

		for (int i = 0; i < list.size(); i++) {
			StringBuilder entryScript = new StringBuilder();
			IDECPListElement element = (IDECPListElement) list.get(i);
			int type = getType(element);
			if (type == UNKNOW_TYPE) {
				continue;
			}
			IClasspathEntry entry = element.getClasspathEntry();
			if (entry == null) {
				continue;
			}
			entryScript.append(entry.getEntryKind());
			entryScript.append(TYPE_SEPARATOR);
			entryScript.append(type);
			entryScript.append(TYPE_SEPARATOR);
			IResource resource = element.getResource();
			IPath path;
			if (resource == null) {
				path = element.getPath();
			} else {
				path = resource.getFullPath();
			}
			entryScript.append(path);
			if (i != list.size() - 1) {
				entryScript.append(ENTRY_SEPARATOR);
			}
			value.append(entryScript.toString());
		}

		setValue(PREF_CLASSPATH, value.toString());
		return super.performApply();
	}

	private int getType(IDECPListElement element) {
		int kind = element.getEntryKind();
		if (kind == IClasspathEntry.CPE_VARIABLE) {
			return VAR_TYPE;
		} else if (kind == IClasspathEntry.CPE_PROJECT) {
			return PROJECT_TYPE;
		} else if (kind == IClasspathEntry.CPE_LIBRARY) {
			IResource resource = element.getResource();
			if (resource instanceof IFile) {
				return JAR_TYPE;
			}
			if (resource instanceof IContainer) {
				return FOL_TYPE;
			}

			IPath path = element.getPath();

			File file = path.toFile();
			if (file.isFile()) {
				return EXTJAR_TYPE;
			} else {
				return ADDFOL_TYPE;
			}
		}

		return UNKNOW_TYPE;
	}

	private IDECPListElement[] addProjectDialog() {

		try {
			Object[] selectArr = getNotYetRequiredProjects();
			new JavaElementComparator().sort(null, selectArr);

			ListSelectionDialog dialog = new ListSelectionDialog(getShell(), Arrays.asList(selectArr),
					new ArrayContentProvider(), new ProjectLabelProvider(),
					Messages.getString("IDEClassPathBlock.ProjectDialog_message")); //$NON-NLS-1$
			dialog.setTitle(Messages.getString("IDEClassPathBlock.ProjectDialog_title")); //$NON-NLS-1$
			dialog.setHelpAvailable(false);
			if (dialog.open() == Window.OK) {
				Object[] result = dialog.getResult();
				IDECPListElement[] cpElements = new IDECPListElement[result.length];
				for (int i = 0; i < result.length; i++) {
					IJavaProject curr = ((IJavaProject) result[i]);
					cpElements[i] = new IDECPListElement(IClasspathEntry.CPE_PROJECT, curr.getPath(),
							curr.getResource());
				}
				return cpElements;
			}
		} catch (JavaModelException e) {
			return null;
		}
		return null;
	}

	private boolean isJavaProject(IProject project) {
		try {
			return project != null && project.hasNature(JavaCore.NATURE_ID);
		} catch (CoreException e) {
			return false;
		}
	}

	private Object[] getNotYetRequiredProjects() throws JavaModelException {
		ArrayList selectable = new ArrayList();
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject[] projects = root.getProjects();
		for (int i = 0; i < projects.length; i++) {
			if (isJavaProject(projects[i])) {
				selectable.add(JavaCore.create(projects[i]));
			}
		}

		if (isJavaProject(getProject())) {
			selectable.remove(JavaCore.create(getProject()));
		}
		List elements = fLibrariesList.getElements();
		for (int i = 0; i < elements.size(); i++) {
			IDECPListElement curr = (IDECPListElement) elements.get(i);
			if (curr.getEntryKind() != IClasspathEntry.CPE_PROJECT) {
				continue;
			}
			IJavaProject proj = (IJavaProject) JavaCore.create(curr.getResource());
			selectable.remove(proj);
		}
		Object[] selectArr = selectable.toArray();
		return selectArr;
	}

	@Override
	public void performDefaults() {
		fLibrariesList.setElements(new ArrayList());
		super.performDefaults();
	}

	@Override
	public void useProjectSpecificSettings(boolean enable) {
		super.useProjectSpecificSettings(enable);
	}

	public IProject getProject() {
		return fProject;
	}

	public void setProject(IProject fCurrProject) {
		this.fProject = fCurrProject;
	}
}
