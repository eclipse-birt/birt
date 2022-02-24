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

import org.eclipse.birt.report.designer.internal.ui.dialogs.resource.AddImageResourceForNewTemplateWizard;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * 
 */

public class WizardReportSettingPage extends WizardPage {

	private IPath containerFullPath;
	private static final String LABEL_DISPLAY_NAME = Messages
			.getString("PublishTemplateAction.wizard.page.label.displayName"); //$NON-NLS-1$
	private static final String LABEL_DESCRIPTION = Messages
			.getString("PublishTemplateAction.wizard.page.label.description"); //$NON-NLS-1$
	private static final String LABEL_IMAGE = Messages.getString("PublishTemplateAction.wizard.page.label.image"); //$NON-NLS-1$
	private static final String BTN_CHOOSE = Messages.getString("PublishTemplateAction.wizard.page.btn.browse"); //$NON-NLS-1$
	private static final String BROWSE_TITLE = Messages.getString("PublishTemplateAction.wizard.page.browse.title"); //$NON-NLS-1$
	// private static final String IMAGE_ERROR =
	// "PublishTemplateAction.wizard.page.imageError";

	// private static final String PAGE_DESC = Messages.getString(
	// "PublishTemplateAction.wizard.page.desc" ); //$NON-NLS-1$
	private static final String PLUGIN_ID = "org.eclipse.birt.report.designer.ui.actions.PublishTemplateWizard"; //$NON-NLS-1$

	private static final String STR_EMPTY = ""; //$NON-NLS-1$

	private String pageDesc = STR_EMPTY;
	private ReportDesignHandle module;
	private Text previewImageText;
	private Text descText;
	private Text nameText;

	private Status nameStatus;

	private Status previewImageStatus;

	private static final String[] IMAGE_TYPES = new String[] { ".bmp", //$NON-NLS-1$
			".jpg", //$NON-NLS-1$
			".jpeg", //$NON-NLS-1$
			".jpe", //$NON-NLS-1$
			".jfif", //$NON-NLS-1$
			".gif", //$NON-NLS-1$
			".png", //$NON-NLS-1$
			".tif", //$NON-NLS-1$
			".tiff", //$NON-NLS-1$
			".ico", //$NON-NLS-1$
			".svg" //$NON-NLS-1$
	};

	private static final String[] IMAGE_FILEFILTERS = new String[] {
			"*.bmp;*.jpg;*.jpeg;*.jpe;*.jfif;*.gif;*.png;*.tif;*.tiff;*.ico;*.svg" //$NON-NLS-1$
	};

	private String orientation = null; // bidi_hcg
	protected String displayName;
	protected String description;
	protected String previewImageFile;

	public void setContainerFullPath(IPath path) {
		this.containerFullPath = path;
	}

	public WizardReportSettingPage(ReportDesignHandle handle) {
		super(""); //$NON-NLS-1$
		module = handle;
		pageDesc = null;
	}

	public void setPageDesc(String pageDesc) {
		this.pageDesc = pageDesc;
		setMessage(pageDesc);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	public void createControl(Composite parent) {

		Composite container = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		container.setLayout(gridLayout);

		new Label(container, SWT.NONE).setText(LABEL_DISPLAY_NAME);
		nameText = createText(container, 2, 1);
		if (module != null && module.getProperty(ModuleHandle.DISPLAY_NAME_PROP) != null)
			nameText.setText(module.getDisplayName());
		nameText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				checkStatus();
				// Show the most serious error
				applyToStatusLine(findMostSevere());
				getWizard().getContainer().updateButtons();
				displayName = nameText.getText();
			}
		});

		new Label(container, SWT.NONE).setText(LABEL_DESCRIPTION);
		descText = createText(container, 2, 5);
		if (module != null && module.getProperty(ModuleHandle.DESCRIPTION_PROP) != null) {
			String descProp = (String) module.getProperty(ModuleHandle.DESCRIPTION_PROP);
			descText.setText(descProp);
			description = descProp;
		}
		descText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				checkStatus();
				// Show the most serious error
				applyToStatusLine(findMostSevere());
				getWizard().getContainer().updateButtons();
				description = descText.getText();
			}
		});

		new Label(container, SWT.NONE).setText(LABEL_IMAGE);
		previewImageText = createText(container, 1, 1, SWT.BORDER | SWT.READ_ONLY);
		if (module != null && module.getIconFile() != null)
			previewImageText.setText(module.getIconFile());
		previewImageText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				checkStatus();
				// Show the most serious error
				applyToStatusLine(findMostSevere());
				getWizard().getContainer().updateButtons();
				validate();
			}
		});

		Button chooseBtn = new Button(container, SWT.NONE);
		chooseBtn.setText(BTN_CHOOSE);
		chooseBtn.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {

				String fileName = null;
				AddImageResourceForNewTemplateWizard dlg = new AddImageResourceForNewTemplateWizard();
				dlg.setContainerFullPath(containerFullPath);
				if (dlg.open() == Window.OK) {
					fileName = dlg.getPath();
					previewImageText.setText(fileName);
					previewImageFile = fileName;
				}

			}

			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		nameText.forceFocus();
		setControl(container);

		UIUtil.bindHelp(getControl(), IHelpContextIds.PUBLISH_TEMPLATE_WIZARD_ID);
	}

	public String getDisplayName() {
		return displayName == null ? STR_EMPTY : displayName.trim();
	}

	public String getDescription() {
		return description == null ? STR_EMPTY : description.trim();
	}

	public String getPreviewImagePath() {
		return previewImageFile == null ? STR_EMPTY : previewImageFile.trim();
	}

	private Text createText(Composite container, int column, int row) {
		return createText(container, column, row, SWT.BORDER);
	}

	private Text createText(Composite container, int column, int row, int style) {
		Text text;
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		gridData.horizontalSpan = column;

		if (row > 1) {
			text = new Text(container, style | SWT.MULTI | SWT.WRAP);
			gridData.heightHint = row * 20;
		} else
			text = new Text(container, style | SWT.SINGLE);
		text.setLayoutData(gridData);
		return text;
	}

	private void validate() {
		if (previewImageStatus.getSeverity() != IStatus.OK) {
			setErrorMessage(previewImageStatus.getMessage());
		}

		setPageComplete(previewImageStatus.getSeverity() != IStatus.ERROR);
	}

	public void checkStatus() {
		String imageFileName = previewImageText.getText().trim();
		// Initialize a variable with the no error status
		Status status = new Status(IStatus.OK, PLUGIN_ID, 0, pageDesc, null);
		nameStatus = status;
		previewImageStatus = status;

		if (isTextEmpty(nameText)) {
			status = new Status(IStatus.ERROR, PLUGIN_ID, 0,
					Messages.getString("PublishTemplateAction.wizard.page.nameInfo"), //$NON-NLS-1$
					null);
			nameStatus = status;
		} else
		// If preview image file is not empty, then need to check whether it
		// exist or it's an iamge.
		if (!isTextEmpty(previewImageText) && (module != null && module.getIconFile() == null)) {
			if (imageFileName.equals(Messages.getString("ThumbnailBuilder.Image.DefaultName"))) //$NON-NLS-1$
			{
				status = new Status(IStatus.ERROR, PLUGIN_ID, 0,
						Messages.getString("PublishTemplateAction.wizard.message.ThumbnailImageNotExist"), //$NON-NLS-1$
						null);
				previewImageStatus = status;
			} else if ((!new File(imageFileName).exists())
					&& (!new File(ReportPlugin.getDefault().getResourceFolder(), imageFileName).exists())
					&& (!new File(UIUtil.getFragmentDirectory(), imageFileName).exists())) {
				status = new Status(IStatus.ERROR, PLUGIN_ID, 0,
						Messages.getString("PublishTemplateAction.wizard.message.PreviewImageNotExist"), //$NON-NLS-1$
						null);
				previewImageStatus = status;
			} else if (!checkExtensions(imageFileName)) {
				status = new Status(IStatus.ERROR, PLUGIN_ID, 0,
						Messages.getString("PublishTemplateAction.wizard.message.PreviewImageNotValid"), //$NON-NLS-1$
						null);
				previewImageStatus = status;
			}

		}
	}

	private static boolean isTextEmpty(Text text) {
		String s = text.getText();
		if ((s != null) && (s.trim().length() > 0))
			return false;
		return true;
	}

	private IStatus findMostSevere() {
		if (nameStatus.getSeverity() == IStatus.ERROR) {
			return nameStatus;
		} else if (previewImageStatus.getSeverity() == IStatus.ERROR) {
			return previewImageStatus;
		} else if (nameStatus.getSeverity() == IStatus.WARNING) {
			return nameStatus;
		} else {
			return previewImageStatus;
		}

	}

	/**
	 * Applies the status to the status line of a dialog page.
	 */
	private void applyToStatusLine(IStatus status) {
		String message = status.getMessage();
		if (message.length() == 0)
			message = pageDesc;
		switch (status.getSeverity()) {
		case IStatus.OK:
			setErrorMessage(null);
			setMessage(message);
			break;
		case IStatus.ERROR:
			setErrorMessage(message);
			setMessage(message, WizardPage.ERROR);
			break;
		case IStatus.WARNING:
			setErrorMessage(null);
			setMessage(message, WizardPage.WARNING);
			break;
		case IStatus.INFO:
			setErrorMessage(null);
			setMessage(message, WizardPage.INFORMATION);
			break;
		default:
			setErrorMessage(message);
			setMessage(null);
			break;
		}
	}

	/**
	 * @see IWizardPage#canFinish()
	 */
	public boolean canFinish() {
		checkStatus();
		if ((nameStatus != null && nameStatus.getSeverity() == IStatus.ERROR)
				|| (previewImageStatus != null && previewImageStatus.getSeverity() == IStatus.ERROR)) {
			return false;
		}
		return true;
	}

	private boolean checkExtensions(String fileName) {
		for (int i = 0; i < IMAGE_TYPES.length; i++) {
			if (fileName.toLowerCase().endsWith(IMAGE_TYPES[i])) {
				return true;
			}
		}
		return false;
	}

	/*
	 * @see DialogPage.setVisible(boolean)
	 */
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			nameText.forceFocus();
			// getControl( ).setFocus( );
		}
	}

	/**
	 * @return the orientation
	 */
	public String getOrientation() {
		return orientation;
	}

	/**
	 * @param orientation the orientation to set
	 */
	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

}
