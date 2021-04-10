/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.preferences;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.preferences.DialogField;
import org.eclipse.birt.report.designer.ui.preferences.IDialogFieldListener;
import org.eclipse.birt.report.designer.ui.preferences.IStatusChangeListener;
import org.eclipse.birt.report.designer.ui.preferences.OptionsConfigurationBlock;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.layout.PixelConverter;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * 
 */

public class ClassPathBlock extends OptionsConfigurationBlock {

	private static final String[] ALL_ARCHIVES_FILTER_EXTENSIONS = new String[] { "*.jar;*.zip", "*.*" }; //$NON-NLS-1$ //$NON-NLS-2$
	public static final String[] JAR_ZIP_FILTER_EXTENSIONS = new String[] { "*.jar;*.zip" }; //$NON-NLS-1$
	private static Key PREF_CLASSPATH;
	private final TreeListDialogField fLibrariesList;

	private static final int IDX_ADDEXT = 0;

	private static final int IDX_ADDEXTFOL = 1;

	private static final int IDX_UP = 3;
	private static final int IDX_DOWN = 4;
	private static final int IDX_EDIT = 5;
	private static final int IDX_REMOVE = 6;

	private static final String ENTRY_SEPARATOR = "|"; //$NON-NLS-1$
	private static String lastUsedPath = ""; //$NON-NLS-1$

	public ClassPathBlock(IStatusChangeListener context, IProject project) {
		super(context, ReportPlugin.getDefault(), project);
		PREF_CLASSPATH = getReportKey(ReportPlugin.CLASSPATH_PREFERENCE);
		String[] buttonLabels = new String[] { Messages.getString("ClassPathBlock_button.addExtJars"), //$NON-NLS-1$
				Messages.getString("ClassPathBlock_button.addExtFolder"), //$NON-NLS-1$

				/* */null, Messages.getString("ClassPathBlock_button.up"), //$NON-NLS-1$
				Messages.getString("ClassPathBlock_button.down"), //$NON-NLS-1$
				Messages.getString("ClassPathBlock_button.edit"), //$NON-NLS-1$
				Messages.getString("ClassPathBlock_button.remove"), //$NON-NLS-1$
		};

		LibrariesAdapter adapter = new LibrariesAdapter();

		fLibrariesList = new TreeListDialogField(adapter, buttonLabels, new CPListLabelProvider());
		fLibrariesList.setDialogFieldListener(adapter);
		fLibrariesList.setLabelText(Messages.getString("ClassPathBlock_label.LibrariesList")); //$NON-NLS-1$

		fLibrariesList.enableButton(IDX_UP, false);
		fLibrariesList.enableButton(IDX_DOWN, false);
		fLibrariesList.enableButton(IDX_REMOVE, false);
		fLibrariesList.enableButton(IDX_EDIT, false);
		setKeys(getKeys());

		fLibrariesList.setElements(readClassPathEntry(getValue(PREF_CLASSPATH)));

	}

	private Key[] getKeys() {
		Key[] keys = new Key[] { PREF_CLASSPATH };
		return keys;
	}

	// -------- UI creation
	public Control createContents(Composite parent) {
		setShell(parent.getShell());
		PixelConverter converter = new PixelConverter(parent);

		Composite composite = new Composite(parent, SWT.NONE);

		LayoutUtil.doDefaultLayout(composite, new DialogField[] { fLibrariesList }, true, SWT.DEFAULT, SWT.DEFAULT);
		LayoutUtil.setHorizontalGrabbing(fLibrariesList.getTreeControl(null));

		int buttonBarWidth = converter.convertWidthInCharsToPixels(24);
		fLibrariesList.setButtonsMinWidth(buttonBarWidth);

		return composite;
	}

	private class LibrariesAdapter implements IDialogFieldListener, ITreeListAdapter {

		private final Object[] EMPTY_ARR = new Object[0];

		// -------- IListAdapter --------
		public void customButtonPressed(TreeListDialogField field, int index) {
			libaryPageCustomButtonPressed(field, index);
		}

		public void selectionChanged(TreeListDialogField field) {
			libaryPageSelectionChanged(field);
		}

		public void doubleClicked(TreeListDialogField field) {
			libaryPageDoubleClicked(field);
		}

		public void keyPressed(TreeListDialogField field, KeyEvent event) {
			libaryPageKeyPressed(field, event);
		}

		public Object[] getChildren(TreeListDialogField field, Object element) {
			if (element instanceof CPListElement) {
				return ((CPListElement) element).getChildren(false);
			}
			return EMPTY_ARR;
		}

		public Object getParent(TreeListDialogField field, Object element) {
			return null;
		}

		public boolean hasChildren(TreeListDialogField field, Object element) {
			return getChildren(field, element).length > 0;
		}

		// ---------- IDialogFieldListener --------

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
		CPListElement[] libentries = null;
		switch (index) {
		case IDX_ADDEXT: /* add external jar */
			libentries = openExtJarFileDialog(null);
			break;

		case IDX_ADDEXTFOL: /* add external folder */
			libentries = openExternalClassFolderDialog(null);
			break;

		case IDX_UP: /* add external folder */
		{
			libentries = (CPListElement[]) getSelection().toArray(new CPListElement[getSelection().size()]);
			fLibrariesList.up();
			break;
		}
		case IDX_DOWN: /* add external folder */
		{
			libentries = (CPListElement[]) getSelection().toArray(new CPListElement[getSelection().size()]);
			fLibrariesList.down();
			break;
		}
		case IDX_EDIT: /* edit */
		{
			editEntry();
			return;
		}
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
				CPListElement curr = libentries[i];
				if (!cplist.contains(curr) && !elementsToAdd.contains(curr)) {
					elementsToAdd.add(curr);
				}
			}

			fLibrariesList.addElements(elementsToAdd);

			fLibrariesList.postSetSelection(new StructuredSelection(libentries));
		}
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
			editElementEntry((CPListElement) elem);
		}
	}

	private void editElementEntry(CPListElement elem) {
		CPListElement[] res = null;
		IPath path = elem.getPath();

		File file = path.toFile();
		if (file.isDirectory()) {
			res = openExternalClassFolderDialog(elem);
		} else {
			res = openExtJarFileDialog(elem);
		}

		if (res != null && res.length > 0) {
			CPListElement curr = res[0];
			curr.setExported(elem.isExported());
			fLibrariesList.replaceElement(elem, curr);
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

		fLibrariesList.enableButton(IDX_ADDEXTFOL, noAttributes);

		fLibrariesList.enableButton(IDX_UP, fLibrariesList.canMoveUp(selElements));
		fLibrariesList.enableButton(IDX_DOWN, fLibrariesList.canMoveDown(selElements));
	}

	private boolean canEdit(List selElements) {
		if (selElements.size() != 1) {
			return false;
		}
		Object elem = selElements.get(0);
		if (elem instanceof CPListElement) {
			return true;
		}

		return false;
	}

	/**
	 * @param field the dialog field
	 */
	private void libaryPageDialogFieldChanged(DialogField field) {
		// donothing now
	}

	private CPListElement[] openExtJarFileDialog(CPListElement existing) {
		if (existing == null) {
			IPath[] selected = chooseExternalJAREntries(getShell());
			if (selected != null) {
				ArrayList res = new ArrayList();
				for (int i = 0; i < selected.length; i++) {
					res.add(new CPListElement(selected[i]));
				}
				return (CPListElement[]) res.toArray(new CPListElement[res.size()]);
			}
		} else {
			IPath path = existing.getPath();
			IPath configured = configureExternalJAREntry(getShell(), path);
			if (configured != null) {
				return new CPListElement[] { new CPListElement(configured) };
			}
		}
		return null;
	}

	private CPListElement[] openExternalClassFolderDialog(CPListElement existing) {
		if (existing == null) {
			IPath[] selected = chooseExternalClassFolderEntries(getShell());
			if (selected != null) {
				ArrayList res = new ArrayList();
				for (int i = 0; i < selected.length; i++) {
					res.add(new CPListElement(selected[i]));
				}
				return (CPListElement[]) res.toArray(new CPListElement[res.size()]);
			}
		} else {
			IPath configured = configureExternalClassFolderEntries(getShell(), existing.getPath());
			if (configured != null) {
				return new CPListElement[] { new CPListElement(configured) };
			}
		}
		return null;
	}

	/*
	 * @see BuildPathBasePage#getSelection
	 */
	public List getSelection() {
		return fLibrariesList.getSelectedElements();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setFocus() {
		fLibrariesList.setFocus();
	}

	protected boolean containsOnlyTopLevelEntries(List selElements) {
		if (selElements.size() == 0) {
			return true;
		}
		for (int i = 0; i < selElements.size(); i++) {
			Object elem = selElements.get(i);
			if (elem instanceof CPListElement) {
				continue;
			} else {
				return false;
			}
		}
		return true;
	}

	public static List<String> getEntries(String value) {
		List elements = readClassPathEntry(value);
		List<String> retValue = new ArrayList<String>();
		for (int i = 0; i < elements.size(); i++) {
			retValue.add(((CPListElement) elements.get(i)).getPath().toFile().getAbsolutePath());
		}

		return retValue;
	}

	private static List readClassPathEntry(String value) {
		// IWorkspaceRoot root = ResourcesPlugin.getWorkspace( ).getRoot( );
		List retValue = new ArrayList();

		if (value == null || value.length() == 0) {
			return retValue;
		}
		StringTokenizer tokenizer = new StringTokenizer(value, ENTRY_SEPARATOR);

		while (tokenizer.hasMoreTokens()) {
			String entry = tokenizer.nextToken();
			if (entry == null || entry.length() == 0) {
				continue;
			}

			int init = 0;
			Path path = new Path(entry);

			retValue.add(new CPListElement(path));
		}

		return retValue;
	}

	@Override
	public boolean performApply() {
		// String value = ""; //$NON-NLS-1$
		StringBuffer buffer = new StringBuffer();
		List list = fLibrariesList.getElements();

		for (int i = 0; i < list.size(); i++) {
			StringBuffer entryScript = new StringBuffer();
			CPListElement element = (CPListElement) list.get(i);

			entryScript.append(element.getPath().toFile().getAbsolutePath());
			if (i != list.size() - 1) {
				entryScript.append(ENTRY_SEPARATOR);
			}
			buffer.append(entryScript.toString());
		}
		String value = buffer.toString();
		setValue(PREF_CLASSPATH, value);
		return super.performApply();
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

	public static IPath[] chooseExternalJAREntries(Shell shell) {
		if (lastUsedPath == null) {
			lastUsedPath = ""; //$NON-NLS-1$
		}
		FileDialog dialog = new FileDialog(shell, SWT.MULTI);
		dialog.setText(Messages.getString("ClassPathBlock_FileDialog.jar.text")); //$NON-NLS-1$
		dialog.setFilterExtensions(ALL_ARCHIVES_FILTER_EXTENSIONS);
		dialog.setFilterPath(lastUsedPath);

		String res = dialog.open();
		if (res == null) {
			return null;
		}
		String[] fileNames = dialog.getFileNames();
		int nChosen = fileNames.length;

		IPath filterPath = Path.fromOSString(dialog.getFilterPath());
		IPath[] elems = new IPath[nChosen];
		for (int i = 0; i < nChosen; i++) {
			elems[i] = filterPath.append(fileNames[i]).makeAbsolute();
		}
		lastUsedPath = dialog.getFilterPath();

		return elems;
	}

	public static IPath configureExternalJAREntry(Shell shell, IPath initialEntry) {
		if (initialEntry == null) {
			throw new IllegalArgumentException();
		}

		String lastUsedPath = initialEntry.removeLastSegments(1).toOSString();

		FileDialog dialog = new FileDialog(shell, SWT.SINGLE);
		dialog.setText(Messages.getString("ClassPathBlock_FileDialog.edit.text")); //$NON-NLS-1$
		dialog.setFilterExtensions(JAR_ZIP_FILTER_EXTENSIONS);
		dialog.setFilterPath(lastUsedPath);
		dialog.setFileName(initialEntry.lastSegment());

		String res = dialog.open();
		if (res == null) {
			return null;
		}
		// lastUsedPath = dialog.getFilterPath( );

		return Path.fromOSString(res).makeAbsolute();
	}

	public static IPath[] chooseExternalClassFolderEntries(Shell shell) {
		if (lastUsedPath == null) {
			lastUsedPath = ""; //$NON-NLS-1$
		}
		DirectoryDialog dialog = new DirectoryDialog(shell, SWT.MULTI);
		dialog.setText(Messages.getString("ClassPathBlock_FolderDialog.text")); //$NON-NLS-1$
		dialog.setMessage(Messages.getString("ClassPathBlock_FolderDialog.message")); //$NON-NLS-1$
		dialog.setFilterPath(lastUsedPath);

		String res = dialog.open();
		if (res == null) {
			return null;
		}

		File file = new File(res);
		if (file.isDirectory())
			return new IPath[] { new Path(file.getAbsolutePath()) };

		return null;
	}

	public static IPath configureExternalClassFolderEntries(Shell shell, IPath initialEntry) {
		DirectoryDialog dialog = new DirectoryDialog(shell, SWT.SINGLE);
		dialog.setText(Messages.getString("ClassPathBlock_FolderDialog.edit.text")); //$NON-NLS-1$
		dialog.setMessage(Messages.getString("ClassPathBlock_FolderDialog.edit.message")); //$NON-NLS-1$
		dialog.setFilterPath(initialEntry.toString());

		String res = dialog.open();
		if (res == null) {
			return null;
		}

		File file = new File(res);
		if (file.isDirectory())
			return new Path(file.getAbsolutePath());

		return null;
	}
}
