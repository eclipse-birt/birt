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

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.util.PixelConverter;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 */
public class LayoutConfigurationBlock extends OptionsConfigurationBlock {

	private final Key PREF_DEFAULT_UNIT = getReportKey(ReportPlugin.DEFAULT_UNIT_PREFERENCE);
	private final Key PREF_DEFAULT_LAYOUT = getReportKey(ReportPlugin.DEFAULT_LAYOUT_PREFERENCE);
	private final Key PREF_DEFAULT_ORIENTATION = getReportKey(ReportPlugin.DEFAULT_ORIENTATION_PREFERENCE);

	private static final String UNIT_AUTO = Messages.getString("LayoutConfigurationBlock.Default.Unit.Auto"); //$NON-NLS-1$

	private static final String DEFAULT_UNIT = Messages.getString("LayoutConfigurationBlock.Default.Unit.Label"); //$NON-NLS-1$
	private static final String DEFAULT_LAYOUT = Messages.getString("LayoutConfigurationBlock.Default.Layout.Label"); //$NON-NLS-1$ ;
	private static final String DEFAULT_ORIENTATION = Messages
			.getString("LayoutConfigurationBlock.Default.Orientation.Label"); //$NON-NLS-1$ ;

	public final int LTR_DIRECTION_INDX = 0;
	public final int RTL_DIRECTION_INDX = 1;

	private PixelConverter fPixelConverter;

	public LayoutConfigurationBlock(IStatusChangeListener context, IProject project) {
		super(context, ReportPlugin.getDefault(), project);
		setKeys(getKeys());
	}

	private Key[] getKeys() {
		Key[] keys = new Key[] { PREF_DEFAULT_UNIT, PREF_DEFAULT_LAYOUT, PREF_DEFAULT_ORIENTATION };
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
		// Set layout
		Composite pageContent = new Composite(parent, SWT.NONE);

		GridData data = new GridData(
				GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL | GridData.VERTICAL_ALIGN_BEGINNING);
		data.grabExcessHorizontalSpace = true;
		pageContent.setLayoutData(data);

		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.numColumns = 2;
		pageContent.setLayout(layout);

		// Add "Default Unit" combo
		IChoiceSet unitChoiceSet = ChoiceSetFactory.getElementChoiceSet(ReportDesignConstants.REPORT_DESIGN_ELEMENT,
				ReportDesignHandle.UNITS_PROP);

		int unitChoiceSetLen = unitChoiceSet.getChoices().length;

		String[] unitValues = new String[unitChoiceSetLen + 1];
		String[] unitNames = new String[unitChoiceSetLen + 1];

		unitNames[0] = UNIT_AUTO;
		unitValues[0] = ReportPlugin.DEFAULT_UNIT_AUTO;

		for (int i = 0; i < unitChoiceSetLen; i++) {
			IChoice ch = unitChoiceSet.getChoices()[i];
			unitValues[i + 1] = ch.getName();
			unitNames[i + 1] = ch.getDisplayName();
		}

		addComboBox(pageContent, DEFAULT_UNIT, PREF_DEFAULT_UNIT, unitValues, unitNames, 0);

		// Add "Default Layout Preference" combo
		IChoiceSet layoutChoiceSet = ChoiceSetFactory.getElementChoiceSet(ReportDesignConstants.REPORT_DESIGN_ELEMENT,
				ReportDesignHandle.LAYOUT_PREFERENCE_PROP);

		int layoutChoiceSetLen = layoutChoiceSet.getChoices().length;

		String[] layoutValues = new String[layoutChoiceSetLen];
		String[] layoutNames = new String[layoutChoiceSetLen];

		for (int i = 0; i < layoutChoiceSetLen; i++) {
			IChoice ch = layoutChoiceSet.getChoices()[i];
			layoutValues[i] = ch.getName();
			layoutNames[i] = ch.getDisplayName();
		}

		addComboBox(pageContent, DEFAULT_LAYOUT, PREF_DEFAULT_LAYOUT, layoutValues, layoutNames, 0);

		// Add "Default Orientation Preference" combo
		IChoiceSet orientationChoiceSet = ChoiceSetFactory
				.getElementChoiceSet(ReportDesignConstants.MASTER_PAGE_ELEMENT, MasterPageHandle.ORIENTATION_PROP);

		int orientationChoiceSetLen = orientationChoiceSet.getChoices().length;

		String[] orientationValues = new String[orientationChoiceSetLen];
		String[] orientationNames = new String[orientationChoiceSetLen];

		for (int i = 0; i < orientationChoiceSetLen; i++) {
			IChoice ch = orientationChoiceSet.getChoices()[i];
			orientationValues[i] = ch.getName();
			orientationNames[i] = ch.getDisplayName();
		}

		addComboBox(pageContent, DEFAULT_ORIENTATION, PREF_DEFAULT_ORIENTATION, orientationValues, orientationNames, 0);

		return pageContent;
	}

	/*
	 * (non-javadoc) Update fields and validate. @param changedKey Key that changed,
	 * or null, if all changed.
	 */

	public void performDefaults() {
		super.performDefaults();
	}

	public void useProjectSpecificSettings(boolean enable) {
		super.useProjectSpecificSettings(enable);
	}
}
