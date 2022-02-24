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

package org.eclipse.birt.report.item.crosstab.ui.preference;

import org.eclipse.birt.report.designer.ui.preferences.IStatusChangeListener;
import org.eclipse.birt.report.designer.ui.preferences.OptionsConfigurationBlock;
import org.eclipse.birt.report.designer.ui.preferences.StatusInfo;
import org.eclipse.birt.report.designer.ui.util.PixelConverter;
import org.eclipse.birt.report.item.crosstab.plugin.CrosstabPlugin;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;

/**
 */
public class CrosstabConfigurationBlock extends OptionsConfigurationBlock {

	private final Key PREF_FILTER_LIMIT = getKey(CrosstabPlugin.ID, CrosstabPlugin.PREFERENCE_FILTER_LIMIT);
	private final Key PREF_CUBE_BUILDER_WARNING = getKey(CrosstabPlugin.ID,
			CrosstabPlugin.CUBE_BUILDER_WARNING_PREFERENCE);
	private final Key PREF_AUTO_DEL_BINDINGS = getKey(CrosstabPlugin.ID, CrosstabPlugin.PREFERENCE_AUTO_DEL_BINDINGS);
	private static final String ENABLED = MessageDialogWithToggle.PROMPT;
	private static final String DISABLED = MessageDialogWithToggle.NEVER;
	private static final int MAX_FILTER_LIMIT = 10000;
	private PixelConverter fPixelConverter;

	public CrosstabConfigurationBlock(IStatusChangeListener context, IProject project) {
		super(context, CrosstabPlugin.getDefault(), project);
		setKeys(getKeys());
	}

	private Key[] getKeys() {
		Key[] keys = null;
		if (fProject == null) {
			keys = new Key[] { PREF_FILTER_LIMIT, PREF_AUTO_DEL_BINDINGS, PREF_CUBE_BUILDER_WARNING };
		} else
			keys = new Key[] { PREF_FILTER_LIMIT };
		return keys;
	}

	/*
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(Composite)
	 */
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

	private Composite createBuildPathTabContent(Composite parent) {

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

		Group group = new Group(pageContent, SWT.NONE);
		group.setText(Messages.getString("CrosstabPreferencePage.filterLimit")); //$NON-NLS-1$
		group.setLayout(new GridLayout(3, false));
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		group.setLayoutData(gd);

		addTextField(group, Messages.getString("CrosstabPreferencePage.filterLimit.prompt"), //$NON-NLS-1$
				PREF_FILTER_LIMIT, 0, 0);

		if (fProject == null) {
			Group promptGroup = new Group(pageContent, SWT.NONE);
			promptGroup.setText(Messages.getString("CrosstabPreferencePage.promptGroup")); //$NON-NLS-1$
			promptGroup.setLayout(new GridLayout(3, false));
			gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalSpan = 3;
			promptGroup.setLayoutData(gd);

			String[] enableDisableValues = new String[] { ENABLED, DISABLED };

			String[] RadioValues = new String[] { MessageDialogWithToggle.ALWAYS, MessageDialogWithToggle.NEVER,
					MessageDialogWithToggle.PROMPT };

			String[] labels = new String[] { Messages.getString("CrosstabPreferencePage.autoDelBindings.Text"), //$NON-NLS-1$
					Messages.getString("CrosstabPreferencePage.autoDelBindings.Text.Always"), //$NON-NLS-1$
					Messages.getString("CrosstabPreferencePage.autoDelBindings.Text.Never"), //$NON-NLS-1$
					Messages.getString("CrosstabPreferencePage.autoDelBindings.Text.Prompt"), //$NON-NLS-1$
			};

			addCheckBox(promptGroup, Messages.getString("CrosstabPreferencePage.cubePopup.Text"), //$NON-NLS-1$
					PREF_CUBE_BUILDER_WARNING, enableDisableValues, 0);

			addRadioButton(pageContent, labels, PREF_AUTO_DEL_BINDINGS, RadioValues, 0);

		}
		return pageContent;
	}

	protected void validateSettings(Key changedKey, String oldValue, String newValue) {
		fContext.statusChanged(validatePositiveNumber(getValue(PREF_FILTER_LIMIT)));
	}

	protected IStatus validatePositiveNumber(final String number) {

		final StatusInfo status = new StatusInfo();
		String errorMessage = Messages.getString("CrosstabPreferencePage.Error.MaxRowInvalid", //$NON-NLS-1$
				new Object[] { Integer.valueOf(MAX_FILTER_LIMIT) });
		if (number.length() == 0) {
			status.setError(errorMessage);
		} else {
			try {
				final int value = Integer.parseInt(number);
				if (value < 1 || value > MAX_FILTER_LIMIT) {
					status.setError(errorMessage);
				}
			} catch (NumberFormatException exception) {
				status.setError(errorMessage);
			}
		}
		return status;
	}
}
