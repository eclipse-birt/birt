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

import java.io.File;

import org.eclipse.birt.report.designer.internal.ui.dialogs.helper.IDialogHelper;
import org.eclipse.birt.report.designer.internal.ui.dialogs.helper.IDialogHelperProvider;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.util.PixelConverter;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.variables.IStringVariableManager;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

/**
 */
public class ResourceConfigurationBlock extends OptionsConfigurationBlock {

	public ResourceConfigurationBlock(IStatusChangeListener context, IProject project) {
		super(context, ReportPlugin.getDefault(), project);
		setKeys(getKeys());
	}

	private Key[] getKeys() {
		Key[] keys = { PREF_RESOURCE };
		return keys;
	}

	/*
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(Composite)
	 */
	@Override
	protected Control createContents(Composite parent) {
		fPixelConverter = new PixelConverter(parent);
		setShell(parent.getShell());

		Composite mainComp = new Composite(parent, SWT.NONE);
		mainComp.setFont(parent.getFont());
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		mainComp.setLayout(layout);

		Composite othersComposite = createBuildPathTabContent(mainComp);
		GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
		gridData.heightHint = fPixelConverter.convertHeightInCharsToPixels(20);
		othersComposite.setLayoutData(gridData);

		validateSettings(null, null, null);

		return mainComp;
	}

	public static final String TITLE_LABEL = Messages.getString("ResourecePreferencePage.title"); //$NON-NLS-1$
	public static final String FOLDER_LABEL = Messages.getString("ResourecePreferencePage.folder"); //$NON-NLS-1$
	public static final String BROWSER_BUTTON = Messages.getString("ResourecePreferencePage.select"); //$NON-NLS-1$
	public static final String OPEN_DIALOG_TITLE = Messages.getString("ResourecePreferencePage.openDialogTitle"); //$NON-NLS-1$
	public static final String OPEN_DILAOG_MESSAGE = Messages.getString("ResourecePreferencePage.openDialogMessage"); //$NON-NLS-1$
	public static final String DIRCTORY = "resource"; //$NON-NLS-1$
	public static final String DEFAULT_RESOURCE_FOLDER_DISPLAY = Messages
			.getString("ResourecePreferencePage.defaultResourceFolder.dispaly"); //$NON-NLS-1$
	private Text resourceText;

	private final Key PREF_RESOURCE = getReportKey(ReportPlugin.RESOURCE_PREFERENCE);
	public static final String BUTTON_KEY = "buttons";//$NON-NLS-1$
	private PixelConverter fPixelConverter;

	private Composite createBuildPathTabContent(Composite parent) {

		Label title = new Label(parent, SWT.NULL);
		title.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		title.setText(TITLE_LABEL);
		new Label(parent, SWT.NONE);

		Composite pageContent = new Composite(parent, SWT.NONE);

		GridData data = new GridData(
				GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL | GridData.VERTICAL_ALIGN_BEGINNING);
		data.grabExcessHorizontalSpace = true;
		pageContent.setLayoutData(data);

		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.numColumns = 3;
		pageContent.setLayout(layout);

		resourceText = addTextField(pageContent, Messages.getString("ResourecePreferencePage.folder"), //$NON-NLS-1$
				PREF_RESOURCE, 0, 0);
		if (resourceText.getText().trim().equals(ReportPlugin.getDefault().getDefaultResourcePreference())) {
			resourceText.setText(DEFAULT_RESOURCE_FOLDER_DISPLAY);
		} else {
			String str = resourceText.getText().trim();
			try {
				IStringVariableManager mgr = VariablesPlugin.getDefault().getStringVariableManager();
				str = mgr.performStringSubstitution(str);
			} catch (CoreException e) {
				str = resourceText.getText().trim();
			}

			File file = new File(str);
			if (!file.isAbsolute()) {
				resourceText.setText(processString(str));
			}
		}

		IDialogHelperProvider helperProvider = (IDialogHelperProvider) ElementAdapterManager.getAdapter(this,
				IDialogHelperProvider.class);

		IDialogHelper controlTypeHelper = null;
		if (helperProvider != null) {
			controlTypeHelper = helperProvider.createHelper(this, BUTTON_KEY);
		}

		if (controlTypeHelper != null) {
			controlTypeHelper.setContainer(this);
			controlTypeHelper.createContent(pageContent);

			controlTypeHelper.addListener(SWT.Selection, new Listener() {

				@Override
				public void handleEvent(Event event) {
					resourceText.setText(event.text);
				}
			});
		} else {
			new Label(pageContent, SWT.NONE);
			Button browser = new Button(pageContent, SWT.PUSH);
			browser.setText(BROWSER_BUTTON);
			data = new GridData();
			browser.setLayoutData(data);
			browser.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent event) {
					DirectoryDialog dialog = new DirectoryDialog(
							PlatformUI.getWorkbench().getDisplay().getActiveShell());

					dialog.setText(OPEN_DIALOG_TITLE);
					dialog.setMessage(OPEN_DILAOG_MESSAGE);
					String folderName = dialog.open();
					if (folderName == null) {
						return;
					}
					folderName = folderName.replace('\\', '/'); // $NON-NLS-1$
					if (!folderName.endsWith("/")) //$NON-NLS-1$
					{
						folderName = folderName + "/"; //$NON-NLS-1$
					}
					resourceText.setText(folderName);
				}
			});
		}

		return pageContent;
	}

	private String processString(String str) {
		// This is a variable.
		if (str.startsWith("${") && str.endsWith("}")) //$NON-NLS-1$ //$NON-NLS-2$
		{
			return str;
		}
		File file = new File(str);
		String temp = file.getAbsolutePath();
		int index = temp.indexOf(str);
		if (index > 0) {
			str = temp.substring(index);
			if (!str.startsWith(File.separator)) {
				str = File.separator + str;
			}
		}

		return DEFAULT_RESOURCE_FOLDER_DISPLAY + str;
	}

	@Override
	protected void textChanged(Text textControl) {
		Key key = (Key) textControl.getData();
		String path = textControl.getText();

		if (path != null && textControl == resourceText) {
			if (path.startsWith(DEFAULT_RESOURCE_FOLDER_DISPLAY)) {
				path = path.replaceFirst(DEFAULT_RESOURCE_FOLDER_DISPLAY, PREF_RESOURCE.getDefaultValue(fPref));
			}
		}

		String oldValue = setValue(key, path);

		validateSettings(key, oldValue, path);
	}

	@Override
	protected void validateSettings(Key changedKey, String oldValue, String newValue) {
		if (newValue == null) {
			return;
		}

		String[] segments = new Path(newValue).segments();

		for (String segment : segments) {
			IStatus status = ResourcesPlugin.getWorkspace().validateName(segment, IResource.FOLDER);

			if (!status.isOK()) {
				fContext.statusChanged(status);
				return;
			}
		}
		fContext.statusChanged(null);
	}

	@Override
	protected void updateText(Text curr) {
		Key key = (Key) curr.getData();

		String currValue = getValue(key);
		curr.setText(currValue);
		if (currValue != null) {
			if (curr == resourceText) {
				String text = curr.getText().trim();
				if (text.equals(ReportPlugin.getDefault().getDefaultResourcePreference())) {
					curr.setText(DEFAULT_RESOURCE_FOLDER_DISPLAY);
				} else {
					File file = new File(text);
					if (!file.isAbsolute()) {
						curr.setText(processString(text));
					}
				}
			}

		}
	}
}
