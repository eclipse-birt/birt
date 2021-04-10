/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class ExpressionBuilderPreferencePage extends PropertyAndPreferencePage {

	public static final String PREF_ID = "org.eclipse.birt.report.designer.ui.preferences.ExpressionBuilderPreferencePage"; //$NON-NLS-1$

	private ExpressionBuilderConfigurationBlock fConfigurationBlock;

	public ExpressionBuilderPreferencePage() {
		super();
	}

	public ExpressionBuilderPreferencePage(String title) {
		super(title);
	}

	public ExpressionBuilderPreferencePage(String title, ImageDescriptor image) {
		super(title, image);
	}

	public void createControl(Composite parent) {
		fConfigurationBlock = new ExpressionBuilderConfigurationBlock(getNewStatusChangedListener(), getProject());
		super.createControl(parent);

		UIUtil.bindHelp(getControl(), IHelpContextIds.PREFERENCE_BIRT_EXPRESSION_SYNTAX_ID);
	}

	protected Control createPreferenceContent(Composite composite) {
		return fConfigurationBlock.createContents(composite);
	}

	protected boolean hasProjectSpecificOptions(IProject project) {
		return fConfigurationBlock.hasProjectSpecificOptions(project);
	}

	protected String getPreferencePageID() {
		return PREF_ID;
	}

	protected String getPropertyPageID() {
		return PREF_ID;
	}

	public void dispose() {
		if (fConfigurationBlock != null) {
			fConfigurationBlock.dispose();
		}
		super.dispose();
	}

	protected void enableProjectSpecificSettings(boolean useProjectSpecificSettings) {
		super.enableProjectSpecificSettings(useProjectSpecificSettings);
		if (fConfigurationBlock != null) {
			fConfigurationBlock.useProjectSpecificSettings(useProjectSpecificSettings);
		}
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

	public void setElement(IAdaptable element) {
		super.setElement(element);
		setDescription(null); // no description for property page
	}

}
