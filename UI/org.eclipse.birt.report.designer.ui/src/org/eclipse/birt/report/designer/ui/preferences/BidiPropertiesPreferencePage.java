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

import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * @author bidi_hcg
 *
 *         This class represents a preference page that is contributed to the
 *         Preferences dialog. This page is used to modify BiDi-specific
 *         settings - Enable/Disable BiDi support - Set 'Left To Right' or
 *         'Right To Left' default report orientation
 */

public class BidiPropertiesPreferencePage extends PropertyAndPreferencePage {

	public static final String PREF_ID = "org.eclipse.birt.report.designer.ui.preferences.BidiPropertiesPreferencePage"; //$NON-NLS-1$

	private BidiPropertiesConfigurationBlock fConfigurationBlock;

	public BidiPropertiesPreferencePage() {
		super();
	}

	public BidiPropertiesPreferencePage(String title) {
		super(title);
	}

	public BidiPropertiesPreferencePage(String title, ImageDescriptor image) {
		super(title, image);
	}

	@Override
	public void createControl(Composite parent) {
		fConfigurationBlock = new BidiPropertiesConfigurationBlock(getNewStatusChangedListener(), getProject());
		super.createControl(parent);

		UIUtil.bindHelp(getControl(), IHelpContextIds.PREFERENCE_BIRT_BIDI_ID);
	}

	@Override
	protected Control createPreferenceContent(Composite composite) {
		return fConfigurationBlock.createContents(composite);
	}

	@Override
	protected boolean hasProjectSpecificOptions(IProject project) {
		return fConfigurationBlock.hasProjectSpecificOptions(project);
	}

	@Override
	protected String getPreferencePageID() {
		return PREF_ID;
	}

	@Override
	protected String getPropertyPageID() {
		return PREF_ID;
	}

	@Override
	public void dispose() {
		if (fConfigurationBlock != null) {
			fConfigurationBlock.dispose();
		}
		super.dispose();
	}

	@Override
	protected void enableProjectSpecificSettings(boolean useProjectSpecificSettings) {
		super.enableProjectSpecificSettings(useProjectSpecificSettings);
		if (fConfigurationBlock != null) {
			fConfigurationBlock.useProjectSpecificSettings(useProjectSpecificSettings);
		}
	}

	@Override
	protected void performDefaults() {
		super.performDefaults();
		if (fConfigurationBlock != null) {
			fConfigurationBlock.performDefaults();
		}
	}

	@Override
	public boolean performOk() {
		if (fConfigurationBlock != null && !fConfigurationBlock.performOk()) {
			return false;
		}
		return super.performOk();
	}

	@Override
	public void performApply() {
		if (fConfigurationBlock != null) {
			fConfigurationBlock.performApply();
		}
	}

	@Override
	public void setElement(IAdaptable element) {
		super.setElement(element);
		setDescription(null); // no description for property page
	}

}
