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

package org.eclipse.birt.report.designer.ui.preferences;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.eclipse.birt.report.designer.internal.ui.util.WidgetUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.ControlEnableState;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PreferencesUtil;

/**
 * Base for project property and preference pages
 */
public abstract class PropertyAndPreferencePage extends PreferencePage
		implements IWorkbenchPreferencePage, IWorkbenchPropertyPage {

	private Control fConfigurationBlockControl;
	private ControlEnableState fBlockEnableState;
	private Link fChangeWorkspaceSettings;
	private SelectionButtonDialogField fUseProjectSettings;

	private IStatus fBlockStatus;

	private Composite fParentComposite;

	private IProject fProject; // project or null
	private Map fData; // page data

	public static final String DATA_NO_LINK = "PropertyAndPreferencePage.nolink"; //$NON-NLS-1$

	public PropertyAndPreferencePage() {

		fBlockStatus = new StatusInfo();

		fBlockEnableState = null;
		fProject = null;
		fData = null;
	}

	public PropertyAndPreferencePage(String title) {
		super(title);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.preference.PreferencePage#PreferencePage(java.lang.
	 * String,org.eclipse.jface.resource.ImageDescriptor)
	 */
	public PropertyAndPreferencePage(String title, ImageDescriptor image) {
		super(title, image);
	}

	protected abstract Control createPreferenceContent(Composite composite);

	protected abstract boolean hasProjectSpecificOptions(IProject project);

	protected abstract String getPreferencePageID();

	protected abstract String getPropertyPageID();

	protected boolean supportsProjectSpecificOptions() {
		return getPropertyPageID() != null;
	}

	protected boolean offerLink() {
		return fData == null || !Boolean.TRUE.equals(fData.get(DATA_NO_LINK));
	}

	@Override
	protected Label createDescriptionLabel(Composite parent) {
		fParentComposite = parent;

		if (enableSetProjectSettings()) {
			if (isProjectPreferencePage()) {
				Composite composite = new Composite(parent, SWT.NONE);
				composite.setFont(parent.getFont());
				GridLayout layout = new GridLayout();
				layout.marginHeight = 0;
				layout.marginWidth = 0;
				layout.numColumns = 2;
				composite.setLayout(layout);
				composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

				IDialogFieldListener listener = new IDialogFieldListener() {

					@Override
					public void dialogFieldChanged(DialogField field) {
						if (field instanceof SelectionButtonDialogField) {
							enableProjectSpecificSettings(((SelectionButtonDialogField) field).isSelected());
						}
					}
				};

				fUseProjectSettings = new SelectionButtonDialogField(SWT.CHECK);
				fUseProjectSettings.setDialogFieldListener(listener);
				fUseProjectSettings
						.setLabelText(Messages.getString("PropertyAndPreferencePage.Text.Enable.SpecialSettings")); //$NON-NLS-1$
				fUseProjectSettings.doFillIntoGrid(composite, 1);
				WidgetUtil.setHorizontalGrabbing(fUseProjectSettings.getSelectionButton(null));

				if (offerLink()) {
					fChangeWorkspaceSettings = createLink(composite,
							Messages.getString("PropertyAndPreferencePage.Text.Configure.Workspace.Settings")); //$NON-NLS-1$
					fChangeWorkspaceSettings.setLayoutData(new GridData(SWT.END, SWT.CENTER, false, false));
				} else {
					WidgetUtil.setHorizontalSpan(fUseProjectSettings.getSelectionButton(null), 2);
				}

				Label horizontalLine = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
				horizontalLine.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false, 2, 1));
				horizontalLine.setFont(composite.getFont());
			} else if (supportsProjectSpecificOptions() && offerLink()) {
				fChangeWorkspaceSettings = createLink(parent,
						Messages.getString("PropertyAndPreferencePage.Text.Configure.Special.Settings")); //$NON-NLS-1$
				fChangeWorkspaceSettings.setLayoutData(new GridData(SWT.END, SWT.CENTER, true, false));
			}
		}

		return super.createDescriptionLabel(parent);
	}

	/*
	 * @see org.eclipse.jface.preference.IPreferencePage#createContents(Composite)
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		composite.setLayout(layout);
		composite.setFont(parent.getFont());

		GridData data = new GridData(GridData.FILL, GridData.FILL, true, true);

		fConfigurationBlockControl = createPreferenceContent(composite);
		fConfigurationBlockControl.setLayoutData(data);

		if (isProjectPreferencePage()) {
			boolean useProjectSettings = hasProjectSpecificOptions(getProject());
			enableProjectSpecificSettings(useProjectSettings);
		}

		Dialog.applyDialogFont(composite);
		return composite;
	}

	private Link createLink(Composite composite, String text) {
		Link link = new Link(composite, SWT.NONE);
		link.setFont(composite.getFont());
		link.setText("<A>" + text + "</A>"); //$NON-NLS-1$//$NON-NLS-2$
		link.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				doLinkActivated((Link) e.widget);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				doLinkActivated((Link) e.widget);
			}
		});
		return link;
	}

	protected boolean useProjectSettings() {
		return isProjectPreferencePage() && fUseProjectSettings != null && fUseProjectSettings.isSelected();
	}

	protected boolean isProjectPreferencePage() {
		return fProject != null;
	}

	protected IProject getProject() {
		return fProject;
	}

	final void doLinkActivated(Link link) {
		Map data = new HashMap();
		data.put(DATA_NO_LINK, Boolean.TRUE);

		if (isProjectPreferencePage()) {
			openWorkspacePreferences(data);
		} else {
			HashSet projectsWithSpecifics = new HashSet();

			IProject[] projects = ReportPlugin.getWorkspace().getRoot().getProjects();
			for (int i = 0; i < projects.length; i++) {
				if (hasProjectSpecificOptions(projects[i])) {
					projectsWithSpecifics.add(projects[i]);
				}
			}

			ProjectSelectionDialog dialog = new ProjectSelectionDialog(getShell(), projectsWithSpecifics);
			if (dialog.open() == Window.OK) {

				IProject res = (IProject) dialog.getFirstResult();
				openProjectProperties(res, data);

			}

		}
	}

	protected final void openWorkspacePreferences(Object data) {
		String id = getPreferencePageID();
		PreferencesUtil.createPreferenceDialogOn(getShell(), id, new String[] { id }, data).open();
	}

	protected final void openProjectProperties(IProject project, Object data) {
		String id = getPropertyPageID();
		if (id != null) {
			PreferencesUtil.createPropertyDialogOn(getShell(), project, id, new String[] { id }, data).open();
		}
	}

	protected void enableProjectSpecificSettings(boolean useProjectSpecificSettings) {
		if (fUseProjectSettings != null) {
			fUseProjectSettings.setSelection(useProjectSpecificSettings);
			enablePreferenceContent(useProjectSpecificSettings);
			updateLinkVisibility();

			doStatusChanged();
		}

	}

	private void updateLinkVisibility() {
		if (fChangeWorkspaceSettings == null || fChangeWorkspaceSettings.isDisposed()) {
			return;
		}

		if (isProjectPreferencePage()) {
			fChangeWorkspaceSettings.setEnabled(!useProjectSettings());
		}
	}

	protected void setPreferenceContentStatus(IStatus status) {
		fBlockStatus = status;
		doStatusChanged();
	}

	/**
	 * Returns a new status change listener that calls
	 * {@link #setPreferenceContentStatus(IStatus)} when the status has changed
	 *
	 * @return The new listener
	 */

	protected IStatusChangeListener getNewStatusChangedListener() {
		return new IStatusChangeListener() {

			@Override
			public void statusChanged(IStatus status) {
				setPreferenceContentStatus(status);
			}
		};
	}

	protected IStatus getPreferenceContentStatus() {
		return fBlockStatus;
	}

	protected void doStatusChanged() {
		if (!isProjectPreferencePage() || useProjectSettings()) {
			updateStatus(fBlockStatus);
		} else {
			updateStatus(new StatusInfo());
		}
	}

	protected void enablePreferenceContent(boolean enable) {
		if (enable) {
			if (fBlockEnableState != null) {
				fBlockEnableState.restore();
				fBlockEnableState = null;
			}
		} else if (fBlockEnableState == null) {
			fBlockEnableState = ControlEnableState.disable(fConfigurationBlockControl);
		}
	}

	/*
	 * @see org.eclipse.jface.preference.IPreferencePage#performDefaults()
	 */
	@Override
	protected void performDefaults() {
		if (useProjectSettings()) {
			enableProjectSpecificSettings(false);
		}
		super.performDefaults();
	}

	private void updateStatus(IStatus status) {
		setValid(status == null ? true : !status.matches(IStatus.ERROR));
		StatusUtil.applyToStatusLine(this, status);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override
	public void init(IWorkbench workbench) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.IWorkbenchPropertyPage#getElement()
	 */
	@Override
	public IAdaptable getElement() {
		return fProject;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.ui.IWorkbenchPropertyPage#setElement(org.eclipse.core.runtime
	 * .IAdaptable)
	 */
	@Override
	public void setElement(IAdaptable element) {
		fProject = (IProject) element.getAdapter(IResource.class);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.preference.PreferencePage#applyData(java.lang.Object)
	 */
	@Override
	public void applyData(Object data) {
		if (data instanceof Map) {
			fData = (Map) data;
		}
		if (fChangeWorkspaceSettings != null) {
			if (!offerLink()) {
				fChangeWorkspaceSettings.dispose();
				fParentComposite.layout(true, true);
			}
		}
	}

	protected Map getData() {
		return fData;
	}

	protected boolean enableSetProjectSettings() {
		if ((IReportPreferenceFactory) ElementAdapterManager.getAdapter(ReportPlugin.getDefault(),
				IReportPreferenceFactory.class) != null) {
			return true;
		}
		return false;
	}
}
