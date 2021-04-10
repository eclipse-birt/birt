/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.preferences;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.SelectionBorder;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.util.PixelConverter;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

/**
 * GeneralConfigurationBlock
 */
public class GeneralConfigurationBlock extends OptionsConfigurationBlock {

	private final Key PREF_ENABLE_GRADIENT = getReportKey(ReportPlugin.ENABLE_GRADIENT_SELECTION_PREFERENCE);
	private final Key PREF_ENABLE_ANIMATION = getReportKey(ReportPlugin.ENABLE_ANIMATION_SELECTION_PREFERENCE);
	private final Key PREF_LIBRARY_WARNING = getReportKey(ReportPlugin.LIBRARY_WARNING_PREFERENCE);
	private final Key PREF_LIBRARY_DEFAULT_THEME_ENABLE = getReportKey(ReportPlugin.LIBRARY_DEFAULT_THEME_ENABLE);
	private final Key PREF_LIBRARY_DEFAULT_THEME_INCLUDE = getReportKey(ReportPlugin.LIBRARY_DEFAULT_THEME_INCLUDE);
	private final Key PREF_LIBRARY_MOVE_BINDINGS = getReportKey(ReportPlugin.LIBRARY_MOVE_BINDINGS_PREFERENCE);

	private static final String ENABLED = "true"; //$NON-NLS-1$
	private static final String DISABLED = "false"; //$NON-NLS-1$

	private PixelConverter fPixelConverter;

	private Button ckGradient;

	private LibraryHandle defaultLibraryHandle;

	public GeneralConfigurationBlock(IStatusChangeListener context, IProject project) {
		super(context, ReportPlugin.getDefault(), project);
		setKeys(getKeys());
	}

	private Key[] getKeys() {
		Key[] keys = new Key[] { PREF_ENABLE_GRADIENT, PREF_ENABLE_ANIMATION, PREF_LIBRARY_WARNING,
				PREF_LIBRARY_DEFAULT_THEME_ENABLE, PREF_LIBRARY_DEFAULT_THEME_INCLUDE, PREF_LIBRARY_MOVE_BINDINGS };
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

		return mainComp;
	}

	private Composite createBuildPathTabContent(Composite parent) {
		Label description = new Label(parent, SWT.None);
		description.setText(Messages.getString("GeneralConfigurationBlock.message.general.description")); //$NON-NLS-1$

		Composite pageContent = new Composite(parent, SWT.NONE);

		GridData data = new GridData(
				GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL | GridData.VERTICAL_ALIGN_BEGINNING);
		data.grabExcessHorizontalSpace = true;
		pageContent.setLayoutData(data);

		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.numColumns = 1;
		pageContent.setLayout(layout);

		String[] enableDisableValues = new String[] { ENABLED, DISABLED };

		ckGradient = addCheckBox(pageContent,
				Messages.getString("GeneralConfigurationBlock.button.text.enable.gradient"), //$NON-NLS-1$
				PREF_ENABLE_GRADIENT, enableDisableValues, 0);

		addCheckBox(pageContent, Messages.getString("GeneralConfigurationBlock.button.text.enable.animation"), //$NON-NLS-1$
				PREF_ENABLE_ANIMATION, enableDisableValues, 0);

		validateSettings(PREF_ENABLE_ANIMATION, null, getValue(PREF_ENABLE_ANIMATION));

		String[] promptValues = new String[] { MessageDialogWithToggle.PROMPT, MessageDialogWithToggle.NEVER };
		Group group = new Group(pageContent, SWT.NONE);
		group.setText(Messages.getString("GeneralConfigurationBlock.group.label"));
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		group.setLayout(new GridLayout());
		addCheckBox(group, Messages.getString("GeneralConfigurationBlock.button.text.prompt"), //$NON-NLS-1$
				PREF_LIBRARY_WARNING, promptValues, 0);

		if (hasDefaultLibraryHandle()) {
			Group themeGroup = new Group(pageContent, SWT.NONE);
			themeGroup.setText(Messages.getString("GeneralConfigurationBlock.group.defaultThemes"));
			themeGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			themeGroup.setLayout(new GridLayout());

			final Button btnEnable = addCheckBox(themeGroup,
					Messages.getString("GeneralConfigurationBlock.button.text.defaultTheme.enable"), //$NON-NLS-1$
					PREF_LIBRARY_DEFAULT_THEME_ENABLE, enableDisableValues, 0);
			final Button btnInclude = addCheckBox(themeGroup,
					Messages.getString("GeneralConfigurationBlock.button.text.defaultTheme.include"), //$NON-NLS-1$
					PREF_LIBRARY_DEFAULT_THEME_INCLUDE, enableDisableValues, 20);

			btnInclude.setEnabled(btnEnable.getSelection());
			btnEnable.addSelectionListener(new SelectionListener() {

				public void widgetDefaultSelected(SelectionEvent arg0) {

				}

				public void widgetSelected(SelectionEvent arg0) {
					btnInclude.setEnabled(btnEnable.getSelection());
				}

			});

		}
		String[] labels = new String[] { Messages.getString("GeneralConfigurationBlock.move.binding.group"), //$NON-NLS-1$
				Messages.getString("GeneralConfigurationBlock.move.binding.always"), //$NON-NLS-1$
				Messages.getString("GeneralConfigurationBlock.move.binding.never"), //$NON-NLS-1$
				Messages.getString("GeneralConfigurationBlock.move.binding.prompt") //$NON-NLS-1$
		};
		String[] values = new String[] { MessageDialogWithToggle.ALWAYS, MessageDialogWithToggle.NEVER,
				MessageDialogWithToggle.PROMPT };

		addRadioButton(pageContent, labels, PREF_LIBRARY_MOVE_BINDINGS, values, 0);

		return pageContent;
	}

	@Override
	protected void validateSettings(Key changedKey, String oldValue, String newValue) {
		super.validateSettings(changedKey, oldValue, newValue);

		if (changedKey == PREF_ENABLE_ANIMATION) {
			if (ENABLED.equals(newValue)) {
				setValue(PREF_ENABLE_GRADIENT, ENABLED);
				ckGradient.setSelection(true);
				ckGradient.setEnabled(false);
			} else {
				ckGradient.setEnabled(true);
			}
		}
	}

	@Override
	public boolean performApply() {
		boolean rt = super.performApply();

		if (rt) {
			SelectionBorder.enableGradient(ENABLED.equals(getValue(PREF_ENABLE_GRADIENT)));
			SelectionBorder.enableAnimation(ENABLED.equals(getValue(PREF_ENABLE_ANIMATION)));
		}

		return rt;
	}

	@Override
	public void performDefaults() {
		super.performDefaults();

		validateSettings(PREF_ENABLE_ANIMATION, null, getValue(PREF_ENABLE_ANIMATION));
	}

	private boolean hasDefaultLibraryHandle() {
		defaultLibraryHandle = DEUtil.getDefaultLibraryHandle();
		return defaultLibraryHandle != null ? true : false;
	}
}
