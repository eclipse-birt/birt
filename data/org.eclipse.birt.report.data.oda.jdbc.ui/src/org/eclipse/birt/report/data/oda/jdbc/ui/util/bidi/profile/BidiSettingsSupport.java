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

package org.eclipse.birt.report.data.oda.jdbc.ui.util.bidi.profile;

import java.util.Properties;

import org.eclipse.birt.report.data.bidi.utils.core.BidiConstants;
import org.eclipse.birt.report.data.bidi.utils.core.BidiFormat;
import org.eclipse.birt.report.data.oda.jdbc.ui.JdbcPlugin;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.bidi.preference.JDBCDataSourcePreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * @author bidi_hcg
 *
 */
public class BidiSettingsSupport {
	private Button bidiButton;
	private BidiFormat metadataBidiFormat = null;
	private BidiFormat contentBidiFormat = null;
	private BidiFormat disabledMetadataBidiFormat = null;
	private BidiFormat disabledContentBidiFormat = null;

	public BidiSettingsSupport() {
		String bidiFormatString = JdbcPlugin.getDefault().getPluginPreferences()
				.getString(JDBCDataSourcePreferencePage.EXTERNAL_BIDI_FORMAT);
		contentBidiFormat = metadataBidiFormat = new BidiFormat(bidiFormatString);

	}

	public void drawBidiSettingsButton(Composite parent, Properties props) {
		initBidiFormats(props);

		bidiButton = new Button(parent, SWT.PUSH);
		bidiButton.setText(JdbcPlugin.getResourceString("wizard.label.bidiSettings"));//$NON-NLS-1$
		bidiButton.setLayoutData(new GridData(GridData.END));
		bidiButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				doSetAdvancedBidiSettings();
			}
		});

	}

	private void doSetAdvancedBidiSettings() {
		AdvancedBidiDialog dlg = new AdvancedBidiDialog(this);
		dlg.open();
	}

	public BidiFormat getContentBidiFormat() {
		return contentBidiFormat;
	}

	public BidiFormat getMetadataBidiFormat() {
		return metadataBidiFormat;
	}

	public BidiFormat getDdisabledContentBidiFormat() {
		return disabledContentBidiFormat;
	}

	public BidiFormat getDdisabledMetadataBidiFormat() {
		return disabledMetadataBidiFormat;
	}

	public void setBidiFormats(BidiFormat metadataBidiFormat, BidiFormat contentBidiFormat,
			BidiFormat disabledMetadataBidiFormat, BidiFormat disabledContentBidiFormat) {
		this.contentBidiFormat = contentBidiFormat;
		this.metadataBidiFormat = metadataBidiFormat;
		this.disabledContentBidiFormat = disabledContentBidiFormat;
		this.disabledMetadataBidiFormat = disabledMetadataBidiFormat;
	}

	public Properties getBidiFormats() {
		Properties p = new Properties();
		BidiFormat externalDefaultBDiFormat = new BidiFormat(JdbcPlugin.getDefault().getPluginPreferences()
				.getString(JDBCDataSourcePreferencePage.EXTERNAL_BIDI_FORMAT));
		if (contentBidiFormat != null) {
			p.setProperty(BidiConstants.CONTENT_FORMAT_PROP_NAME, contentBidiFormat.getBiDiFormatString());
		} else {
			p.setProperty(BidiConstants.CONTENT_FORMAT_PROP_NAME, externalDefaultBDiFormat.getBiDiFormatString());
		}
		if (metadataBidiFormat != null) {
			p.setProperty(BidiConstants.METADATA_FORMAT_PROP_NAME, metadataBidiFormat.getBiDiFormatString());
		} else {
			p.setProperty(BidiConstants.METADATA_FORMAT_PROP_NAME, externalDefaultBDiFormat.getBiDiFormatString());
		}
		return p;
	}

	private void initBidiFormats(Properties props) {
		BidiFormat contentFormat = null;
		BidiFormat metadataFormat = null;
		if (props == null) {
			return;
		}
		String str = props.getProperty(BidiConstants.CONTENT_FORMAT_PROP_NAME);
		if (str != null && !str.equals("")) {
			contentFormat = new BidiFormat(str);
		} else {
			contentFormat = new BidiFormat(BidiConstants.DEFAULT_BIDI_FORMAT_STR);
		}
		str = props.getProperty(BidiConstants.METADATA_FORMAT_PROP_NAME);
		if (str != null && !str.equals("")) {
			metadataFormat = new BidiFormat(str);
		} else {
			metadataFormat = new BidiFormat(BidiConstants.DEFAULT_BIDI_FORMAT_STR);
		}
		str = props.getProperty(BidiConstants.DISABLED_CONTENT_FORMAT_PROP_NAME);
		if (str != null && !str.equals("")) {
			disabledContentBidiFormat = new BidiFormat(str);
		} else {
			disabledContentBidiFormat = null;
		}
		str = props.getProperty(BidiConstants.DISABLED_METADATA_FORMAT_PROP_NAME);
		if (str != null && !str.equals("")) {
			disabledMetadataBidiFormat = new BidiFormat(str);
		} else {
			disabledMetadataBidiFormat = null;
		}

		setBidiFormats(metadataFormat, contentFormat, disabledMetadataBidiFormat, disabledContentBidiFormat);
	}

	public Properties addBidiProperties(Properties props) {
		if (props != null) {
			if (contentBidiFormat != null) {
				props.setProperty(BidiConstants.CONTENT_FORMAT_PROP_NAME, contentBidiFormat.toString());
			}
			if (metadataBidiFormat != null) {
				props.setProperty(BidiConstants.METADATA_FORMAT_PROP_NAME, metadataBidiFormat.toString());
			}
			if (disabledContentBidiFormat != null) {
				props.setProperty(BidiConstants.DISABLED_CONTENT_FORMAT_PROP_NAME,
						disabledContentBidiFormat.toString());
			} else {
				props.setProperty(BidiConstants.DISABLED_CONTENT_FORMAT_PROP_NAME, BidiConstants.EMPTY_STR);
			}
			if (disabledMetadataBidiFormat != null) {
				props.setProperty(BidiConstants.DISABLED_METADATA_FORMAT_PROP_NAME,
						disabledMetadataBidiFormat.toString());
			} else {
				props.setProperty(BidiConstants.DISABLED_METADATA_FORMAT_PROP_NAME, BidiConstants.EMPTY_STR);
			}
		}
		return props;
	}

}
