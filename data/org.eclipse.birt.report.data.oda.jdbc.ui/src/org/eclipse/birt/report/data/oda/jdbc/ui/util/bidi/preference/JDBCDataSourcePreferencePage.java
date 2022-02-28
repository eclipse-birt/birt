/***********************************************************************
 * Copyright (c) 2009 IBM Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.data.oda.jdbc.ui.util.bidi.preference;

import org.eclipse.birt.report.data.bidi.utils.core.BidiFormat;
import org.eclipse.birt.report.data.bidi.utils.i18n.Messages;
import org.eclipse.birt.report.data.bidi.utils.ui.BidiGUIUtility;
import org.eclipse.birt.report.data.oda.jdbc.ui.JdbcPlugin;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.IHelpConstants;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.Utility;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * @author bidi_hcg
 *
 */
public class JDBCDataSourcePreferencePage extends PreferencePage implements IWorkbenchPreferencePage {
	public static final String EXTERNAL_BIDI_FORMAT = "report.data.oda.bidi.jdbc.ui.externalbidiformat";
	private Group externalBiDiFormatFrame;
	private String externalBiDiFormatStr;
	Preferences ps = null;

	public JDBCDataSourcePreferencePage() {
		super();
		setDescription(Messages.getString("preference.description"));
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite mainComposite = new Composite(parent, SWT.NONE);
		GridLayout twoColLayout = new GridLayout();
		twoColLayout.numColumns = 2;
		twoColLayout.marginWidth = 10;
		twoColLayout.marginHeight = 10;
		mainComposite.setLayout(twoColLayout);

		externalBiDiFormatFrame = BidiGUIUtility.INSTANCE.addBiDiFormatFrame(mainComposite,
				Messages.getString("preference.bidiframe.title"), new BidiFormat(externalBiDiFormatStr));

		Utility.setSystemHelp(getControl(), IHelpConstants.CONEXT_ID_PREFERENCE_JDBC_BIDI);
		return mainComposite;
	}

	@Override
	public void init(IWorkbench workbench) {
		ps = JdbcPlugin.getDefault().getPluginPreferences();
		externalBiDiFormatStr = ps.getString(EXTERNAL_BIDI_FORMAT);
	}

	@Override
	protected void performDefaults() {
		BidiGUIUtility.INSTANCE.performDefaults();
	}

	@Override
	public boolean performOk() {
		externalBiDiFormatStr = BidiGUIUtility.INSTANCE.getBiDiFormat(externalBiDiFormatFrame).getBiDiFormatString();
		ps.setValue(EXTERNAL_BIDI_FORMAT, externalBiDiFormatStr);
		return super.performOk();
	}

}
