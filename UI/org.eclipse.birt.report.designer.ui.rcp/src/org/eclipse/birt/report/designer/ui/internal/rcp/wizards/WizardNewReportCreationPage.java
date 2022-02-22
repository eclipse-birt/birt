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

package org.eclipse.birt.report.designer.ui.internal.rcp.wizards;

import org.eclipse.birt.report.designer.core.IReportElementConstants;
import org.eclipse.birt.report.designer.internal.ui.editors.wizards.NewReportPageSupport;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.editors.IReportEditorContants;
import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * Creation page for Report Wizard without Advanced control
 *
 */
public class WizardNewReportCreationPage extends WizardPage {

	private static final String MSG_DUPLICATE_FILE_NAME = Messages
			.getString("WizardNewReportCreationPage.msg.duplicate.fileName"); //$NON-NLS-1$

	private static final String MSG_EMPTY_FILE_LOCATION_DIRECTORY = Messages
			.getString("WizardNewReportCreationPage.msg.empty.file.locationDirectory"); //$NON-NLS-1$

	private static final String MSG_EMPTY_FILE_NAME = Messages
			.getString("WizardNewReportCreationPage.msg.empty.file.name"); //$NON-NLS-1$

	private Listener locationModifyListener = new Listener() {

		@Override
		public void handleEvent(Event e) {
			setPageComplete(validatePage());
		}
	};

	NewReportPageSupport pageSupport = null;

	/**
	 * The Constructor.
	 *
	 * @param pageName
	 */
	public WizardNewReportCreationPage(String pageName) {
		super(pageName);
		pageSupport = new NewReportPageSupport();
	}

	/**
	 * Sets the initial file name that this page will use when created. The name is
	 * ignored if the createControl(Composite) method has already been called.
	 * Leading and trailing spaces in the name are ignored.
	 *
	 * @param name initial file name for this page
	 */
	public void setInitialFileName(String name) {
		pageSupport.setInitialFileName(name);
	}

	public void setInitialFileLocation(String path) {
		pageSupport.setInitialFileLocation(path);
	}

	@Override
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		setControl(pageSupport.createComposite(parent));

		pageSupport.getFileNameField().addListener(SWT.Modify, locationModifyListener);
		pageSupport.getLocationPathField().addListener(SWT.Modify, locationModifyListener);

		setPageComplete(validatePage());
		setErrorMessage(null);
		setMessage(null);

		if (getFileName().endsWith(IReportElementConstants.TEMPLATE_FILE_EXTENSION)) {
			UIUtil.bindHelp(getControl(), IHelpContextIds.NEW_TEMPLATE_WIZARD_ID);
		} else if (getFileName().endsWith(IReportElementConstants.DESIGN_FILE_EXTENSION)) {
			UIUtil.bindHelp(getControl(), IHelpContextIds.NEW_REPORT_WIZARD_ID);
		}
	}

	public String getFileName() {
		return pageSupport.getFileName();
	}

	public IPath getFileLocationFullPath() {
		return pageSupport.getFileLocationFullPath();
	}

	@Override
	public void setVisible(boolean visible) {
		getControl().setVisible(visible);
		if (visible) {
			pageSupport.getFileNameField().setFocus();
		}
	}

	public boolean validatePage() {
		return validatePage(IReportEditorContants.DESIGN_FILE_EXTENTION);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.dialogs.WizardNewFileCreationPage#validatePage()
	 */
	public boolean validatePage(String suffix) {
		if (getFileName().equals(""))//$NON-NLS-1$
		{
			setErrorMessage(null);
			setMessage(MSG_EMPTY_FILE_NAME);
			return false;
		}

		String location = getFileLocationFullPath().toOSString();

		if (location.equals("")) //$NON-NLS-1$
		{
			setErrorMessage(null);
			setMessage(MSG_EMPTY_FILE_LOCATION_DIRECTORY);
			return false;
		}

		IPath path;

		if (!Platform.getOS().equals(Platform.OS_WIN32)) {
			if (!getFileName().endsWith(suffix)) {
				path = getFileLocationFullPath().append(getFileName() + suffix);
			} else {
				path = getFileLocationFullPath().append(getFileName());
			}
		} else if (!getFileName().toLowerCase().endsWith(suffix.toLowerCase())) {
			path = getFileLocationFullPath().append(getFileName() + suffix);
		} else {
			path = getFileLocationFullPath().append(getFileName());
		}

		if (path.lastSegment().equals(suffix)) {
			setErrorMessage(Messages.getString("WizardNewReportCreationPage.Errors.nameEmpty")); //$NON-NLS-1$
			return false;
		}

		if (path.toFile().exists()) {
			setErrorMessage(MSG_DUPLICATE_FILE_NAME);
			return false;
		}

		setErrorMessage(null);
		setMessage(null);
		return true;
	}
}
