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

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.util.PixelConverter;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 */
public class BidiPropertiesConfigurationBlock extends OptionsConfigurationBlock {

	private final Key PREF_LTR_BIDI_DIRECTION = getReportKey(ReportPlugin.LTR_BIDI_DIRECTION);

	private final static String RTL_DIRECTION = Messages
			.getString("report.designer.ui.preferences.bidiproperties.rtldirection");
	private final static String LTR_DIRECTION = Messages
			.getString("report.designer.ui.preferences.bidiproperties.ltrdirection");
	private final static String DEFAULT_DIRECTION = Messages
			.getString("report.designer.ui.preferences.bidiproperties.defaultdirection");

	public final int LTR_DIRECTION_INDX = 0;
	public final int RTL_DIRECTION_INDX = 1;

	private PixelConverter fPixelConverter;

	public BidiPropertiesConfigurationBlock(IStatusChangeListener context, IProject project) {
		super(context, ReportPlugin.getDefault(), project);
		setKeys(getKeys());
	}

	private Key[] getKeys() {
		Key[] keys = new Key[] { PREF_LTR_BIDI_DIRECTION };
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

	private static final String TRUE = "true"; //$NON-NLS-1$
	private static final String FALSE = "false"; //$NON-NLS-1$

	private Composite createBuildPathTabContent(Composite parent) {
		String[] bidiValues = new String[] { TRUE, FALSE };

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

		addComboBox(pageContent, DEFAULT_DIRECTION, PREF_LTR_BIDI_DIRECTION, bidiValues,
				new String[] { LTR_DIRECTION, RTL_DIRECTION }, 0);

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
