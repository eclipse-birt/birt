/*
 *************************************************************************
 * Copyright (c) 2004-2005 Actuate Corporation.
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
package org.eclipse.birt.report.data.oda.sampledb.ui;

import java.util.Properties;

import org.eclipse.birt.report.data.oda.jdbc.OdaJdbcDriver;
import org.eclipse.birt.report.data.oda.sampledb.SampleDBConstants;
import org.eclipse.birt.report.data.oda.sampledb.ui.i18n.Messages;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.DataSourceDesign;
import org.eclipse.datatools.connectivity.oda.design.ui.designsession.DesignSessionUtil;
import org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

/**
 * Data Set Editor page for Sample DB Data sets created using builds prior to
 * 2.0M3 It updates the data source to a regular JDBC data source
 */
public class SampleDataSetEditor extends DataSetWizardPage {

	private DataSetDesign datasetDesign;
	private DataSourceDesign dataSourceDesign;
	private Properties props;
	private static final String SAMPLE_DB_SCHEMA = "ClassicModels";

	public SampleDataSetEditor(String pageName) {
		super(pageName);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage#
	 * createPageCustomControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPageCustomControl(Composite parent) {
		initializeControl();
		setControl(createPageControl(parent));
	}

	private void initializeControl() {
		datasetDesign = this.getInitializationDesign();
		dataSourceDesign = datasetDesign.getDataSourceDesign();
	}

	/*
	 * @see org.eclipse.birt.report.designer.ui.dialogs.properties.IPropertyPage#
	 * createPageControl(org.eclipse.swt.widgets.Composite)
	 */
	public Control createPageControl(Composite parent) {
		// create the composite to hold the widgets
		Composite content = new Composite(parent, SWT.NONE);

		GridLayout layout = new GridLayout();
		content.setLayout(layout);
		content.setLayoutData(new GridData(GridData.FILL_BOTH));

		Text txt = new Text(content, SWT.LEFT | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		txt.setEditable(false);
		txt.setText(Messages.formatMessage("datasource.upgrade.msg.firstLine", new Object[] { //$NON-NLS-1$
				dataSourceDesign.getName() }));
		txt.append(Messages.formatMessage("datasource.upgrade.msg.secondLine", new Object[] { //$NON-NLS-1$
				dataSourceDesign.getName() }));
		txt.append(Messages.getMessage("datasource.upgrade.msg.thirdLine"));
		txt.setLayoutData(new GridData(GridData.FILL_BOTH));

		return content;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.design.internal.ui.
	 * DataSetWizardPageCore#collectDataSetDesign(org.eclipse.datatools.connectivity
	 * .oda.design.DataSetDesign)
	 */
	@Override
	protected DataSetDesign collectDataSetDesign(DataSetDesign design) {
		// default implementation does nothing;
		// sub-class to override and update based on the given data set design
		// Update the data source to a regular JDBC data source
		// Note that since the data source extensionID is an intristic property
		// we cannot simply
		// update it; we will instead drop the original data source and
		// re-create one with
		// the same name
		if (props == null) {
			props = new Properties();
		}

		// set custom driver specific properties
		props.setProperty(org.eclipse.birt.report.data.oda.jdbc.Connection.Constants.ODADriverClass,
				SampleDBConstants.DRIVER_CLASS);
		props.setProperty(org.eclipse.birt.report.data.oda.jdbc.Connection.Constants.ODAURL,
				SampleDBConstants.DRIVER_URL);
		props.setProperty(org.eclipse.birt.report.data.oda.jdbc.Connection.Constants.ODAUser, SAMPLE_DB_SCHEMA);
		props.setProperty(org.eclipse.birt.report.data.oda.jdbc.Connection.Constants.ODAPassword, "");

		design.getDataSourceDesign().setOdaExtensionId(OdaJdbcDriver.Constants.DATA_SOURCE_ID);

		try {
			design.getDataSourceDesign().setPublicProperties(
					DesignSessionUtil.createDataSourcePublicProperties(OdaJdbcDriver.Constants.DATA_SOURCE_ID, props));
		} catch (OdaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return design;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage#
	 * canLeave()
	 */
	@Override
	protected boolean canLeave() {
		return true;
	}
}
