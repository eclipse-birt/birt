/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.preferences;

import org.eclipse.birt.report.designer.internal.ui.preferences.ClassPathBlock;
import org.eclipse.birt.report.designer.internal.ui.preferences.IClassPathHelperProvider;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * Class path Page
 */

public class ClassPathPreferencesPage extends PropertyAndPreferencePage {

	/**
	 * ID
	 */
	public static final String PREF_ID = "org.eclipse.birt.report.designer.ui.preferences.ClassPathpreferencesPage"; //$NON-NLS-1$
	private OptionsConfigurationBlock block;

	@Override
	protected Control createPreferenceContent(Composite composite) {
		block.createContents(composite);
		return composite;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#createControl(org.eclipse
	 * .swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		// IDE support the provider
		IClassPathHelperProvider provider = (IClassPathHelperProvider) ElementAdapterManager.getAdapter(this,
				IClassPathHelperProvider.class);
		if (provider != null) {
			block = provider.createBlock(getNewStatusChangedListener(), getProject());
		}
		if (block == null) {
			// RCP
			block = new ClassPathBlock(getNewStatusChangedListener(), getProject());
		}
		super.createControl(parent);

		UIUtil.bindHelp(parent, IHelpContextIds.PREF_PAGE_CLASSPATH);
	}

	@Override
	protected String getPreferencePageID() {
		return PREF_ID;
	}

	@Override
	protected String getPropertyPageID() {
		return PREF_ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.ui.preferences.PropertyAndPreferencePage
	 * #hasProjectSpecificOptions(org.eclipse.core.resources.IProject)
	 */
	protected boolean hasProjectSpecificOptions(IProject project) {
		return block.hasProjectSpecificOptions(project);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#performApply()
	 */
	public void performApply() {
		if (block != null) {
			block.performApply();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#performOk()
	 */
	public boolean performOk() {
		if (block != null && !block.performOk()) {
			return false;
		}

		return super.performOk();
	}

	@Override
	public void setElement(IAdaptable element) {
		super.setElement(element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.ui.preferences.PropertyAndPreferencePage
	 * #enableProjectSpecificSettings(boolean)
	 */
	protected void enableProjectSpecificSettings(boolean useProjectSpecificSettings) {
		super.enableProjectSpecificSettings(useProjectSpecificSettings);
		if (block != null) {
			block.useProjectSpecificSettings(useProjectSpecificSettings);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.ui.preferences.PropertyAndPreferencePage
	 * #performDefaults()
	 */
	protected void performDefaults() {
		if (block != null) {
			block.performDefaults();
		}
		super.performDefaults();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.DialogPage#dispose()
	 */
	public void dispose() {
		if (block != null) {
			block.dispose();
		}
		super.dispose();
	}
}
