/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.preferences;

import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbenchPropertyPage;

/**
 * GeneralPreferencePage
 */
public class GeneralPreferencePage extends PreferencePage implements IWorkbenchPreferencePage, IWorkbenchPropertyPage {

	public static final String PREF_ID = "org.eclipse.birt.report.designer.ui.preferences"; //$NON-NLS-1$

	private GeneralConfigurationBlock fConfigurationBlock;

	public GeneralPreferencePage() {
		super();
	}

	public GeneralPreferencePage(String title) {
		super(title);
	}

	public GeneralPreferencePage(String title, ImageDescriptor image) {
		super(title, image);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.
	 * widgets.Composite)
	 */
	protected Control createContents(Composite parent) {
		fConfigurationBlock = new GeneralConfigurationBlock(null, null);

		UIUtil.bindHelp(getControl(), IHelpContextIds.PREFERENCE_BIRT_GENERAL_ID);

		return fConfigurationBlock.createContents(parent);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}

	protected Control createPreferenceContent(Composite composite) {
		return fConfigurationBlock.createContents(composite);
	}

	protected String getPreferencePageID() {
		return PREF_ID;
	}

	protected String getPropertyPageID() {
		return PREF_ID;
	}

	protected boolean hasProjectSpecificOptions(IProject project) {
		return false;
	}

	@Override
	public void dispose() {
		if (fConfigurationBlock != null) {
			fConfigurationBlock.dispose();
		}
		super.dispose();
	}

	protected void performDefaults() {
		super.performDefaults();
		if (fConfigurationBlock != null) {
			fConfigurationBlock.performDefaults();
		}
	}

	public boolean performOk() {
		if (fConfigurationBlock != null && !fConfigurationBlock.performOk()) {
			return false;
		}

		return super.performOk();
	}

	public void performApply() {
		if (fConfigurationBlock != null) {
			fConfigurationBlock.performApply();
		}
	}

	public IAdaptable getElement() {
		return null;
	}

	public void setElement(IAdaptable element) {
	}

}
