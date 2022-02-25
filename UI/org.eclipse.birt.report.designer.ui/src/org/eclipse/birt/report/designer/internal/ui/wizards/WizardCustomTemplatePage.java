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

package org.eclipse.birt.report.designer.internal.ui.wizards;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.views.ResourceSorter;
import org.eclipse.birt.report.designer.internal.ui.views.WorkbenchContentProvider;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.model.WorkbenchLabelProvider;

/**
 * A wizard page to select a custom template.
 */
public class WizardCustomTemplatePage extends WizardPage {

	private static final String MESSAGE_SHOW_CHEATSHEET = Messages
			.getString("WizardTemplateChoicePage.label.ShowCheatSheets"); //$NON-NLS-1$ )
	private static final String MESSAGE_BROWSE = Messages.getString("WizardCustomTemplatePage.button.Browse"); //$NON-NLS-1$
	private static final String MESSAGE_LABEL = Messages.getString("WizardCustomTemplatePage.label.FileName"); //$NON-NLS-1$
	private static final String MESSAGE_FROM_LOCAL = Messages.getString("WizardCustomTemplatePage.label.FromLocal"); //$NON-NLS-1$
	private static final String MESSAGE_FROM_PROJECT_TITLE = Messages
			.getString("WizardCustomTemplatePage.label.FromProjectTitle"); //$NON-NLS-1$
	private static final String MESSAGE_FROM_PROJECT_DESCRIPTION = Messages
			.getString("WizardCustomTemplatePage.label.FromProjectDescription"); //$NON-NLS-1$

	private Text inputText;
	private Button browse;
	private Button chkBoxBrowseFrom;
	private Button chkBoxCheetSheet;

	/**
	 * The constructor.
	 *
	 * @param pageName
	 */
	public WizardCustomTemplatePage(String pageName) {
		super(pageName);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.
	 * Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.marginHeight = 10;
		gridLayout.marginWidth = 10;
		gridLayout.horizontalSpacing = 10;
		gridLayout.verticalSpacing = 10;
		composite.setLayout(gridLayout);

		Label lb = new Label(composite, SWT.NONE);
		lb.setText(MESSAGE_LABEL);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.grabExcessHorizontalSpace = true;
		lb.setLayoutData(data);

		Composite space = new Composite(composite, SWT.NONE);
		data = new GridData();
		data.heightHint = 20;
		data.widthHint = 20;
		space.setLayoutData(data);

		inputText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		data = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL);
		data.grabExcessHorizontalSpace = true;
		inputText.setLayoutData(data);

		inputText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				setPageComplete(validatePage());
				updateChkBox();
			}
		});

		browse = new Button(composite, SWT.PUSH);
		browse.setText(MESSAGE_BROWSE);
		data = new GridData(GridData.HORIZONTAL_ALIGN_END);
		browse.setLayoutData(data);

		browse.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (chkBoxBrowseFrom.getSelection()) {
					FileDialog dialog = new FileDialog(getShell());
					dialog.setFilterPath(ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString());
					List extensionNameList = ReportPlugin.getDefault().getReportExtensionNameList();
					String[] extensionNames = new String[extensionNameList.size()];
					for (int i = 0; i < extensionNames.length; i++) {
						extensionNames[i] = "*." + extensionNameList.get(i); //$NON-NLS-1$
					}
					dialog.setFilterExtensions(extensionNames);
					if (dialog.open() != null) {
						inputText.setText(dialog.getFilterPath() + File.separator + dialog.getFileName());
					}
				} else {
					ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(getShell(),
							new WorkbenchLabelProvider(), new WorkbenchContentProvider());
					dialog.setAllowMultiple(false);
					dialog.setTitle(MESSAGE_FROM_PROJECT_TITLE);
					dialog.setMessage(MESSAGE_FROM_PROJECT_DESCRIPTION);
					dialog.setValidator(new ISelectionStatusValidator() {

						@Override
						public IStatus validate(Object[] selection) {
							if (selection == null || selection.length < 1 || selection[0] instanceof IProject
									|| selection[0] instanceof IFolder) {
								return new Status(IStatus.ERROR, ReportPlugin.REPORT_UI, IStatus.ERROR, "", null); //$NON-NLS-1$
							}

							return new Status(IStatus.OK, ReportPlugin.REPORT_UI, IStatus.OK, "", null); //$NON-NLS-1$
						}
					});
					dialog.addFilter(new ViewerFilter() {

						@Override
						public boolean select(Viewer viewer, Object parentElement, Object element) {
							if (element instanceof IProject || element instanceof IFolder) {
								return ((IResource) element).isAccessible();
							}
							if (element instanceof IFile) {
								return ((IResource) element).isAccessible()
										&& ReportPlugin.getDefault().getReportExtensionNameList()
												.contains(((IResource) element).getFileExtension());
							}
							return false;
						}
					});
					dialog.setInput(ResourcesPlugin.getWorkspace().getRoot());
					dialog.setSorter(new ResourceSorter(ResourceSorter.NAME));

					if (dialog.open() == Window.OK) {
						IResource res = (IResource) dialog.getFirstResult();
						inputText.setText(res.getLocation().toOSString());
					}
				}
			}
		});

		chkBoxBrowseFrom = new Button(composite, SWT.CHECK);
		chkBoxBrowseFrom.setText(MESSAGE_FROM_LOCAL);
		chkBoxBrowseFrom.setSelection(false);
		data = new GridData();
		data.horizontalSpan = 2;
		chkBoxBrowseFrom.setLayoutData(data);

		chkBoxCheetSheet = new Button(composite, SWT.CHECK);
		chkBoxCheetSheet.setText(MESSAGE_SHOW_CHEATSHEET);
		chkBoxCheetSheet.setSelection(ReportPlugin.readCheatSheetPreference());
		chkBoxCheetSheet.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				ReportPlugin.writeCheatSheetPreference(chkBoxCheetSheet.getSelection());
			}
		});

		// until Eclipse OpenSheetCheatAction bug is fixed
		// https://bugs.eclipse.org/bugs/show_bug.cgi?id=88481
		chkBoxCheetSheet.setVisible(false);

		setPageComplete(false);
		setControl(composite);
	}

	/**
	 * Updates the cheatSheet checkBox state.
	 */
	protected void updateChkBox() {
		for (Iterator iter = ReportPlugin.getDefault().getReportExtensionNameList().iterator(); iter.hasNext();) {
			String extensionName = "." + iter.next(); //$NON-NLS-1$
			if (getReportPath().endsWith(extensionName)) {
				String xmlPath = getReportPath().substring(0, getReportPath().length() - extensionName.length())
						+ ".xml"; //$NON-NLS-1$
				File f = new File(xmlPath);
				chkBoxCheetSheet.setEnabled(f.exists());
				return;
			}
		}
	}

	/**
	 * @return Returns the report file path.
	 */
	public String getReportPath() {
		return inputText.getText();
	}

	/**
	 * Checks if the file exists and is a file
	 */
	protected boolean validatePage() {
		if (inputText.getText().length() > 0) {
			File f = new File(getReportPath());
			return f.exists() && f.isFile();
		}

		return false;
	}

	/**
	 * @return true if show CheatSheet is checked
	 */
	public boolean getShowCheatSheet() {
		return chkBoxCheetSheet.getSelection();
	}
}
