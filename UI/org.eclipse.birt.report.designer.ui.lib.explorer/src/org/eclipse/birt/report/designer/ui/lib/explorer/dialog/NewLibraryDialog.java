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

package org.eclipse.birt.report.designer.ui.lib.explorer.dialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.dialogs.resource.IResourceContentProvider;
import org.eclipse.birt.report.designer.internal.ui.dialogs.resource.ResourceFileFolderSelectionDialog;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.lib.explorer.action.ResourceAction;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;

/**
 * A dialog for moving reource in resource explorer. User can select a folder to
 * move reource.
 */
public class NewLibraryDialog extends ResourceFileFolderSelectionDialog {

	private final File defaultLibrary;

	private Text text;

	private String ext = Messages.getString("NewLibraryWizard.displayName.NewReportFileExtension").toLowerCase(); //$NON-NLS-1$

	private Status OKStatus = new Status(IStatus.OK, ReportPlugin.REPORT_UI, IStatus.OK, "", //$NON-NLS-1$
			null);

	private Status ErrorStatus = new Status(IStatus.ERROR, ReportPlugin.REPORT_UI, IStatus.ERROR,
			Messages.getString("NewResourceFileDialog.ErrorMessage"), //$NON-NLS-1$
			null);

	private Status ErrorStatusNoSelection = new Status(IStatus.ERROR, ReportPlugin.REPORT_UI, IStatus.ERROR,
			Messages.getString(""), //$NON-NLS-1$
			null);

	private Status ErrorStatusInvalid = new Status(IStatus.ERROR, ReportPlugin.REPORT_UI, IStatus.ERROR,
			Messages.getString("NewResourceFileDialog.ErrorMessageInvalid"), //$NON-NLS-1$
			null);

	private String filename;

	private class Validator implements ISelectionStatusValidator {

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.ui.dialogs.ISelectionStatusValidator#validate(java.lang
		 * .Object[])
		 */
		@Override
		public IStatus validate(Object[] selection) {
			int nSelected = selection.length;

			if (nSelected != 1) {
				return ErrorStatusNoSelection;
			}

			String filename = getFileName();

			if (filename == null || filename.equals(ext)) {
				return ErrorStatusInvalid;
			}

			if (!filename.endsWith(ext)) {
				return ErrorStatus;
			}

			return OKStatus;
		}
	}

	/**
	 * Constructs a dialog for moving resource.
	 */
	public NewLibraryDialog() {
		this(null);
	}

	public String getFileName() {
		return filename;
		// return text.getText( ).trim( ).toLowerCase( );
	}

	public NewLibraryDialog(File defaultLibrary) {
		super(false, false, null);
		this.defaultLibrary = defaultLibrary;
		setTitle(Messages.getString("NewLibraryDialog.Title")); //$NON-NLS-1$
		setMessage(Messages.getString("NewLibraryDialog.Message")); //$NON-NLS-1$
		setDoubleClickSelects(true);
		setAllowMultiple(false);
		setValidator(new Validator());
		setEmptyFolderShowStatus(IResourceContentProvider.ALWAYS_SHOW_EMPTYFOLDER);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.dialogs.ElementTreeSelectionDialog#createDialogArea(org
	 * .eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite rt = (Composite) super.createDialogArea(parent);
		Composite pane = new Composite(rt, 0);
		Label lb = new Label(pane, 0);

		text = new Text(pane, SWT.BORDER);
		lb.setText(Messages.getString("NewLibraryDialog.label.NewLibrary")); //$NON-NLS-1$
		rt.setLayoutData(new GridData(GridData.FILL_BOTH));
		pane.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		pane.setLayout(new GridLayout(2, false));
		pane.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		text.addModifyListener(new ModifyListener() {

			/*
			 * (non-Javadoc)
			 *
			 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.
			 * swt.events.ModifyEvent)
			 */
			@Override
			public void modifyText(ModifyEvent e) {
				filename = text.getText().trim().toLowerCase();
				updateOKStatus();
			}
		});

		if (defaultLibrary != null) {
			selectPath(defaultLibrary.getParent());
			text.setText(defaultLibrary.getName());
		}
		text.selectAll();
		text.setFocus();
		UIUtil.bindHelp(parent, IHelpContextIds.NEW_LIBRARY_WIZARD_ID);
		return rt;
	}

	/**
	 * Selects the specified path in the tree viewer.
	 *
	 * @param path the path to select.
	 */
	private void selectPath(String path) {
		String parent = new File(path).getParent();
		List<String> folders = new ArrayList<>();

		while (parent != null) {
			folders.add(parent);
			parent = new File(parent).getParent();
		}

		for (int i = folders.size() - 1; i >= 0; i--) {
			getTreeViewer().expandToLevel(folders.get(i), 1);
		}
		setInitialSelection(path);
	}

	@Override
	public String getPath() {
		Object[] selected = getResult();

		if (selected != null && selected.length > 0 && selected[0] instanceof ResourceEntry) {
			ResourceEntry entry = (ResourceEntry) selected[0];

			if (entry == null) {
				return null;
			}

			try {
				File path = ResourceAction.convertToFile(entry.getURL());
				String filename = getFileName();
				File file = new Path(path.getAbsolutePath()).append(filename).toFile();

				return file.getAbsolutePath();
			} catch (IOException e) {
				ExceptionUtil.handle(e);
			}
		}
		return null;
	}

	@Override
	protected void okPressed() {
		String path = getPath();

		if (path != null) {
			File file = new File(path);
			if (file.exists() && !MessageDialog.openConfirm(UIUtil.getDefaultShell(), "Question",
					Messages.getFormattedString("NewResourceFileDialog.FileExists", new String[] { file.getName() }))) {
				return;
			}
		}
		super.okPressed();
	}

}
