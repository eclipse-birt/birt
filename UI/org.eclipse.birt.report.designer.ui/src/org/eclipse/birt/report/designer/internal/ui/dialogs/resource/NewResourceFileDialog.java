/*******************************************************************************
 * Copyright (c) 2004, 2014 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.dialogs.resource;

import java.io.File;
import java.io.IOException;

import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.ReportResourceChangeEvent;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.views.IReportResourceChangeEvent;
import org.eclipse.birt.report.designer.ui.views.IReportResourceSynchronizer;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
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
 * A dialog which can browser all properties in BIRT resource folder. User can
 * select a properties or enter new file name to creat a new one.
 *
 */

public class NewResourceFileDialog extends ResourceFileFolderSelectionDialog {

	private Text text;
	private String ext = ".properties"; //$NON-NLS-1$

	protected String newFileName = ""; //$NON-NLS-1$

	private Status OKStatus = new Status(IStatus.OK, ReportPlugin.REPORT_UI, IStatus.OK, "", null); //$NON-NLS-1$
	private Status ErrorStatus = new Status(IStatus.ERROR, ReportPlugin.REPORT_UI, IStatus.ERROR,
			Messages.getString("NewResourceFileDialog.ErrorMessage"), //$NON-NLS-1$
			null);
	private Status ErrorStatusNoSelection = new Status(IStatus.ERROR, ReportPlugin.REPORT_UI, IStatus.ERROR,
			Messages.getString(""), //$NON-NLS-1$
			null);
	private Status ErrorStatusInvalid = new Status(IStatus.ERROR, ReportPlugin.REPORT_UI, IStatus.ERROR,
			Messages.getString("NewResourceFileDialog.ErrorMessageInvalid"), //$NON-NLS-1$
			null);

	private class Validator implements ISelectionStatusValidator {

		@Override
		public IStatus validate(Object[] selection) {
			int nSelected = selection.length;
			if (nSelected == 0) {
				return ErrorStatusNoSelection;
			} else if (nSelected > 1) {
				return ErrorStatus;
			} else if (selection[0] instanceof ResourceEntry && ((ResourceEntry) selection[0]).isFile()) {
				return OKStatus;
			} else if (newFileName == null || !newFileName.toLowerCase().endsWith(ext.toLowerCase())) {
				return ErrorStatus;
			} else if (newFileName == null || newFileName.toLowerCase().equals(ext.toLowerCase())) {
				return ErrorStatusInvalid;
			}
			return OKStatus;
		}
	}

	public NewResourceFileDialog() {
		super(true, new String[] { "*.properties" //$NON-NLS-1$
		});
		setDoubleClickSelects(true);
		setValidator(new Validator());
		setAllowMultiple(false);
		// setInput( getResourceRootFile( ) );
		setTitle(Messages.getString("ModulePage.Resourcefile.Dialog.Title")); //$NON-NLS-1$
		setMessage(Messages.getString("ModulePage.Resourcefile.Dialog.Message")); //$NON-NLS-1$
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
		rt.setLayoutData(new GridData(GridData.FILL_BOTH));

		Composite pane = new Composite(rt, 0);
		pane.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		pane.setLayout(new GridLayout(2, false));
		pane.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label lb = new Label(pane, 0);
		lb.setText(Messages.getString("NewResourceFileDialog.label.NewFile"));//$NON-NLS-1$

		text = new Text(pane, SWT.BORDER);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				newFileName = text.getText();
				updateOKStatus();
			}
		});

		configViewer();
		UIUtil.bindHelp(parent, IHelpContextIds.ADD_PROPERTIES_FILES_DIALOG_ID);
		return rt;
	}

	private void configViewer() {

		getTreeViewer().addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				Object object = ((StructuredSelection) event.getSelection()).getFirstElement();
				if (object instanceof ResourceEntry) {
					ResourceEntry entry = (ResourceEntry) object;
					if (entry.getURL() != null && entry.getURL().getProtocol().equals("file")) //$NON-NLS-1$
					{
						File file = new File(entry.getURL().getPath());
						text.setEnabled(file.isDirectory());
					} else {
						text.setText(""); //$NON-NLS-1$
						text.setEnabled(false);
					}
				} else {
					text.setText(""); //$NON-NLS-1$
					text.setEnabled(false);
				}
			}
		});

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.dialogs.SelectionStatusDialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		super.okPressed();
		Object[] selected = getResult();
		if (selected.length > 0 && !newFileName.equals("")) //$NON-NLS-1$
		{
			ResourceEntry entry = (ResourceEntry) selected[0];
			File file = new File(entry.getURL().getPath());
			if (file == null || file.isFile()) {
				return;
			}
			try {
				File newFile = new File(file, newFileName);

				newFile.createNewFile();

				IReportResourceSynchronizer synchronizer = ReportPlugin.getDefault().getResourceSynchronizerService();

				if (synchronizer != null) {
					synchronizer.notifyResourceChanged(new ReportResourceChangeEvent(this,
							Path.fromOSString(newFile.getAbsolutePath()), IReportResourceChangeEvent.NewResource));
				}
			} catch (IOException e) {
				ExceptionHandler.handle(e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.designer.ui.dialogs.resource.
	 * FileFolderSelectionDialog#getPath()
	 */
	@Override
	public String getPath() {
		Object[] selected = getResult();
		if (selected.length > 0 && !newFileName.equals("")) //$NON-NLS-1$
		{
			String path = super.getPath();
			ResourceEntry entry = (ResourceEntry) selected[0];
			File file = new File(entry.getURL().getPath());
			if (file == null || file.isFile()) {
				return path;
			}
			return path + ((path.equals("") || path.endsWith("/")) ? "" : "/") //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$
					+ newFileName;
		} else {
			return super.getPath();
		}
	}
}
