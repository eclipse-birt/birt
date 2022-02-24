/*
 *************************************************************************
 * Copyright (c) 2005, 2007 Actuate Corporation.
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
 *  
 *************************************************************************
 */

package org.eclipse.birt.report.data.oda.sampledb.ui.profile;

import java.util.Properties;

import org.eclipse.birt.report.data.bidi.utils.core.BidiConstants;
import org.eclipse.birt.report.data.bidi.utils.core.BidiFormat;
import org.eclipse.birt.report.data.oda.sampledb.SampleDBConstants;
import org.eclipse.birt.report.data.oda.sampledb.SampleDBJDBCConnectionFactory;
import org.eclipse.birt.report.data.oda.sampledb.ui.i18n.Messages;
import org.eclipse.birt.report.data.oda.sampledb.ui.plugin.SampledbUIPlugin;
import org.eclipse.datatools.connectivity.oda.util.manifest.ConnectionProfileProperty;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;

/**
 * Helper class to create sampleDb selection page and property page
 * 
 */
public class SampleDbSelectionPageHelper {
	private WizardPage m_wizardPage;
	private PreferencePage m_propertyPage;

	private static final String SAMPLE_DB_SCHEMA = "ClassicModels";
	private final String CONEXT_ID_DATASOURCE_SAMPLEDB = "org.eclipse.birt.cshelp.Wizard_DatasourceProfile_ID";//$NON-NLS-1$
	private Label m_driverClass, m_driverURL, m_sampleUser;

	static final String DEFAULT_MESSAGE = Messages.getMessage("datasource.page.title"); //$NON-NLS-1$

	private static final String EMPTY_STRING = "";
	private static final String EXTERNAL_BIDI_FORMAT = "report.data.oda.bidi.jdbc.ui.externalbidiformat";

	SampleDbSelectionPageHelper(WizardPage page) {
		m_wizardPage = page;
	}

	SampleDbSelectionPageHelper(PreferencePage page) {
		m_propertyPage = page;
	}

	void createCustomControl(Composite parent) {
		// create the composite to hold the widgets
		Composite content = new Composite(parent, SWT.NONE);

		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		content.setLayout(layout);

		new Label(content, SWT.LEFT).setText(Messages.getMessage("datasource.page.driver.class")); //$NON-NLS-1$
		m_driverClass = new Label(content, SWT.LEFT);
		m_driverClass.setText(SampleDBConstants.DRIVER_CLASS); // $NON-NLS-1$
		new Label(content, SWT.LEFT).setText(Messages.getMessage("datasource.page.url")); //$NON-NLS-1$
		m_driverURL = new Label(content, SWT.LEFT);
		m_driverURL.setText(SampleDBConstants.DRIVER_URL);// $NON-NLS-1$
		new Label(content, SWT.LEFT).setText(Messages.getMessage("datasource.page.user")); //$NON-NLS-1$
		m_sampleUser = new Label(content, SWT.LEFT);
		m_sampleUser.setText(SampleDBJDBCConnectionFactory.getDbUser());// $NON-NLS-1$

		setMessage(DEFAULT_MESSAGE, IMessageProvider.NONE);
		if (this.m_wizardPage != null) {
			if (this.m_wizardPage.getPreviousPage().getClass().toString().contains("DataSourceSelectionPage")) {
				setMessage(DEFAULT_MESSAGE, IMessageProvider.NONE);
			} else {
				setMessage(Messages.getMessage("datasource.page.warning"), IMessageProvider.WARNING);
			}
		}

		PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), CONEXT_ID_DATASOURCE_SAMPLEDB);
	}

	/**
	 * collect custom properties
	 * 
	 * @param props
	 * @return
	 */
	Properties collectCustomProperties(Properties props) {
		if (props == null)
			props = new Properties();

		// set custom driver specific properties
		props.setProperty(org.eclipse.birt.report.data.oda.jdbc.Connection.Constants.ODADriverClass, getDriverClass());
		props.setProperty(org.eclipse.birt.report.data.oda.jdbc.Connection.Constants.ODAURL, getDriverUrl());
		props.setProperty(org.eclipse.birt.report.data.oda.jdbc.Connection.Constants.ODAUser, SAMPLE_DB_SCHEMA);
		props.setProperty(org.eclipse.birt.report.data.oda.jdbc.Connection.Constants.ODAPassword, "");

		// sampledb uses predined connection property values, and does not support
		// external reference to a connection profile
		props.setProperty(ConnectionProfileProperty.PROFILE_STORE_FILE_PATH_PROP_KEY, "");

		BidiFormat externalDefaultBDiFormat = new BidiFormat(
				SampledbUIPlugin.getDefault().getPluginPreferences().getString(EXTERNAL_BIDI_FORMAT));
		if (externalDefaultBDiFormat != null) {
			props.setProperty(BidiConstants.CONTENT_FORMAT_PROP_NAME, externalDefaultBDiFormat.getBiDiFormatString());
			props.setProperty(BidiConstants.METADATA_FORMAT_PROP_NAME, externalDefaultBDiFormat.getBiDiFormatString());
		}

		return props;
	}

	/**
	 * get driver class
	 * 
	 * @return
	 */
	private String getDriverClass() {

		return SampleDBConstants.DRIVER_CLASS;
	}

	/**
	 * get driver url
	 * 
	 * @return
	 */
	private String getDriverUrl() {
		// TODO Auto-generated method stub
		return SampleDBConstants.DRIVER_URL;
	}

	/**
	 * populate initial properties
	 * 
	 * @param profileProps
	 */
	void initCustomControl(Properties profileProps) {
		if (profileProps == null || profileProps.isEmpty())
			return; // nothing to initialize

		String driverClass = profileProps
				.getProperty(org.eclipse.birt.report.data.oda.jdbc.Connection.Constants.ODADriverClass);
		if (driverClass == null)
			driverClass = EMPTY_STRING;
		m_driverClass.setText(driverClass);

		String driverUrl = profileProps.getProperty(org.eclipse.birt.report.data.oda.jdbc.Connection.Constants.ODAURL);
		if (driverUrl == null)
			driverUrl = EMPTY_STRING;
		m_driverURL.setText(driverUrl);

		String user = profileProps.getProperty(org.eclipse.birt.report.data.oda.jdbc.Connection.Constants.ODAUser);
		if (user == null)
			user = EMPTY_STRING;
		m_sampleUser.setText(user);
	}

	/**
	 * set message
	 * 
	 * @param message
	 */
	private void setMessage(String message, int type) {
		if (m_wizardPage != null)
			m_wizardPage.setMessage(message, type);
		else if (m_propertyPage != null)
			m_propertyPage.setMessage(message, type);
	}

	private Control getControl() {
		if (m_wizardPage != null)
			return m_wizardPage.getControl();
		assert (m_propertyPage != null);
		return m_propertyPage.getControl();
	}

}
