/*
 *************************************************************************
 * Copyright (c) 2005, 2012 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *
 *************************************************************************
 */

package org.eclipse.birt.report.data.oda.hive.ui.profile;

import java.util.Properties;

import org.eclipse.birt.report.data.oda.hive.HiveConstants;
import org.eclipse.birt.report.data.oda.hive.ui.i18n.Messages;
import org.eclipse.birt.report.data.oda.jdbc.ui.JdbcPlugin;
import org.eclipse.birt.report.data.oda.jdbc.ui.dialogs.JdbcDriverManagerDialog;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.DriverLoader;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.ExceptionHandler;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.variables.IStringVariableManager;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

/**
 * Helper class for Hive data source wizard page and property page
 */
public class HiveSelectionPageHelper {
	private WizardPage m_wizardPage;
	private PreferencePage m_propertyPage;
	private Button manageButton, testButton;
	final private static String EMPTY_URL = JdbcPlugin.getResourceString("error.emptyDatabaseUrl");
	private final String CONEXT_ID_DATASOURCE_HIVE = "org.eclipse.birt.cshelp.Wizard_DatasourceProfile_ID";//$NON-NLS-1$
	private Label m_driverClass;
	// Text of url, name and password
	private Text jdbcUrl, userName, password, addfile;
	private String DEFAULT_MESSAGE = "";

	private static final String EMPTY_STRING = ""; //$NON-NLS-1$
//	private static final String EXTERNAL_BIDI_FORMAT = "report.data.oda.bidi.jdbc.ui.externalbidiformat";

	public HiveSelectionPageHelper(WizardPage page, String odaDesignerID) {
		m_wizardPage = page;
		setDefaultMessage(odaDesignerID);
	}

	public HiveSelectionPageHelper(PreferencePage page, String odaDesignerID) {
		m_propertyPage = page;
		setDefaultMessage(odaDesignerID);
	}

	private void setDefaultMessage(String odaDesignerID) {
		String msgExpr = Messages.getMessage("datasource.page.title");
		// "Define ${odadesignerid.ds.displayname} Data Source";
		String dsMsgExpr = msgExpr.replace("odadesignerid", odaDesignerID); //$NON-NLS-1$

		IStringVariableManager varMgr = org.eclipse.core.variables.VariablesPlugin.getDefault()
				.getStringVariableManager();
		try {
			DEFAULT_MESSAGE = varMgr.performStringSubstitution(dsMsgExpr, false);
		} catch (CoreException ex) {
			// TODO Auto-generated catch block
		}

	}

	void createCustomControl(Composite parent) {

		ScrolledComposite scrollContent = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);

		scrollContent.setAlwaysShowScrollBars(false);
		scrollContent.setExpandHorizontal(true);

		scrollContent.setLayout(new FillLayout());

		// create the composite to hold the widgets
		Composite content = new Composite(scrollContent, SWT.NONE);

		GridLayout layout = new GridLayout();
		layout.numColumns = 4;
		layout.verticalSpacing = 10;
		layout.marginBottom = 10;
		content.setLayout(layout);

		GridData gridData;

		new Label(content, SWT.LEFT).setText(Messages.getMessage("datasource.page.driver.class")); //$NON-NLS-1$
		m_driverClass = new Label(content, SWT.LEFT);
		m_driverClass.setText(HiveConstants.HS2_JDBC_DRIVER_CLASS);
		gridData = new GridData();
		gridData.horizontalSpan = 3; // bidi_hcg
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		m_driverClass.setLayoutData(gridData);

		/*
		 * new Label( content, SWT.LEFT ).setText( Messages.getMessage(
		 * "datasource.page.url" ) ); //$NON-NLS-1$ m_driverURL = new Label( content,
		 * SWT.LEFT ); m_driverURL.setText( HiveConstants.DRIVER_URL );//$NON-NLS-1$ new
		 * Label( content, SWT.LEFT ).setText( Messages.getMessage(
		 * "datasource.page.user" ) ); //$NON-NLS-1$ m_User = new Label( content,
		 * SWT.LEFT ); //m_User.setText( HiveJDBCConnectionFactory.getDbUser( )
		 * );//$NON-NLS-1$ setMessage( DEFAULT_MESSAGE );
		 */
		new Label(content, SWT.RIGHT).setText(JdbcPlugin.getResourceString("wizard.label.url"));//$NON-NLS-1$

		jdbcUrl = new Text(content, SWT.BORDER);
		gridData = new GridData();
		gridData.horizontalSpan = 3; // bidi_hcg
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		jdbcUrl.setLayoutData(gridData);
		jdbcUrl.setText(HiveConstants.HS2_DEFAULT_URL);

		// User Name
		new Label(content, SWT.RIGHT).setText(JdbcPlugin.getResourceString("wizard.label.username"));//$NON-NLS-1$
		userName = new Text(content, SWT.BORDER);
		gridData = new GridData();
		gridData.horizontalSpan = 3; // bidi_hcg
		gridData.horizontalAlignment = SWT.FILL;
		userName.setLayoutData(gridData);

		// Password
		new Label(content, SWT.RIGHT).setText(JdbcPlugin.getResourceString("wizard.label.password"));//$NON-NLS-1$
		password = new Text(content, SWT.BORDER | SWT.PASSWORD);
		gridData = new GridData();
		gridData.horizontalSpan = 3; // bidi_hcg
		gridData.horizontalAlignment = SWT.FILL;
		password.setLayoutData(gridData);

		// Add File
		new Label(content, SWT.RIGHT).setText(Messages.getMessage("datasource.addfile"));//$NON-NLS-1$
		addfile = new Text(content, SWT.BORDER);
		gridData = new GridData();
		gridData.horizontalSpan = 3; // bidi_hcg
		gridData.horizontalAlignment = SWT.FILL;
		addfile.setLayoutData(gridData);

		manageButton = new Button(content, SWT.PUSH);
		manageButton.setText(JdbcPlugin.getResourceString("wizard.label.manageDriver"));

		testButton = new Button(content, SWT.PUSH);
		testButton.setText(JdbcPlugin.getResourceString("wizard.label.testConnection"));//$NON-NLS-1$
		testButton.setLayoutData(new GridData(GridData.CENTER));

		Point size = content.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		content.setSize(size.x, size.y);

		scrollContent.setMinWidth(size.x + 10);

		scrollContent.setContent(content);
		addControlListeners();
		updateTestButton();
		PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), CONEXT_ID_DATASOURCE_HIVE);
	}

	/**
	 * collect custom properties
	 *
	 * @param props
	 * @return
	 */
	Properties collectCustomProperties(Properties props) {
		if (props == null) {
			props = new Properties();
		}

		// set custom driver specific properties

		props.setProperty(org.eclipse.birt.report.data.oda.jdbc.Connection.Constants.ODADriverClass, getDriverClass());
		props.setProperty(org.eclipse.birt.report.data.oda.jdbc.Connection.Constants.ODAURL, getDriverURL());
		props.setProperty(org.eclipse.birt.report.data.oda.jdbc.Connection.Constants.ODAUser, getODAUser());
		props.setProperty(org.eclipse.birt.report.data.oda.jdbc.Connection.Constants.ODAPassword, getODAPassword());
		props.setProperty(HiveConstants.HIVE_ADD_FILE_PROPERTY, getAddFile());

		return props;
	}

	private String getODAUser() {
		if (userName == null) {
			return EMPTY_STRING;
		}
		return getTrimedString(userName.getText());
	}

	/**
	 * get password
	 *
	 * @return
	 */
	private String getODAPassword() {
		if (password == null) {
			return EMPTY_STRING;
		}
		return getTrimedString(password.getText());
	}

	private String getDriverURL() {
		if (jdbcUrl == null) {
			return EMPTY_STRING;
		}
		return getTrimedString(jdbcUrl.getText());
	}

	private String getAddFile() {
		if (addfile == null) {
			return EMPTY_STRING;
		}
		return getTrimedString(addfile.getText());
	}

	private String getTrimedString(String tobeTrimed) {
		if (tobeTrimed != null) {
			tobeTrimed = tobeTrimed.trim();
		}
		return tobeTrimed;
	}

	/**
	 * get driver class
	 *
	 * @return
	 */
	private String getDriverClass() {

		return HiveConstants.HS2_JDBC_DRIVER_CLASS;
	}

	/**
	 * populate initial properties
	 *
	 * @param profileProps
	 */
	void initCustomControl(Properties profileProps) {
		if (profileProps == null || profileProps.isEmpty()) {
			return; // nothing to initialize
		}

		String driverClass = profileProps
				.getProperty(org.eclipse.birt.report.data.oda.jdbc.Connection.Constants.ODADriverClass);
		if (driverClass == null) {
			driverClass = EMPTY_STRING;
		}
		m_driverClass.setText(driverClass);

		String driverUrl = profileProps.getProperty(org.eclipse.birt.report.data.oda.jdbc.Connection.Constants.ODAURL);
		if (driverUrl == null) {
			driverUrl = EMPTY_STRING;
		}
		jdbcUrl.setText(driverUrl);

		String user = profileProps.getProperty(org.eclipse.birt.report.data.oda.jdbc.Connection.Constants.ODAUser);
		if (user == null) {
			user = EMPTY_STRING;
		}
		userName.setText(user);

		String odaPassword = profileProps
				.getProperty(org.eclipse.birt.report.data.oda.jdbc.Connection.Constants.ODAPassword);
		if (odaPassword == null) {
			odaPassword = EMPTY_STRING;
		}
		password.setText(odaPassword);

		String addFile = profileProps.getProperty(HiveConstants.HIVE_ADD_FILE_PROPERTY);
		if (addFile == null) {
			addFile = EMPTY_STRING;
		}
		addfile.setText(addFile);
	}

	/**
	 * set message
	 *
	 * @param message
	 */
	private void setMessage(String message) {
		if (m_wizardPage != null) {
			m_wizardPage.setMessage(message);
		} else if (m_propertyPage != null) {
			m_propertyPage.setMessage(message);
		}
	}

	private Control getControl() {
		if (m_wizardPage != null) {
			return m_wizardPage.getControl();
		}
		assert (m_propertyPage != null);
		return m_propertyPage.getControl();
	}

	private void addControlListeners() {
		jdbcUrl.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				if (!jdbcUrl.isFocusControl() && jdbcUrl.getText().trim().length() == 0) {
					return;
				}
				verifyJDBCProperties();
				updateTestButton();
			}
		});
		testButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				testButton.setEnabled(false);
				try {
					if (testConnection()) {
						MessageDialog.openInformation(getShell(), JdbcPlugin.getResourceString("connection.test"), //$NON-NLS-1$
								JdbcPlugin.getResourceString("connection.success"));//$NON-NLS-1$
					} else {
						OdaException ex = new OdaException(JdbcPlugin.getResourceString("connection.failed"));
						ExceptionHandler.showException(getShell(), JdbcPlugin.getResourceString("connection.test"), //$NON-NLS-1$
								JdbcPlugin.getResourceString("connection.failed"), ex);
					}
				} catch (OdaException e1) {
					ExceptionHandler.showException(getShell(), JdbcPlugin.getResourceString("connection.test"), //$NON-NLS-1$
							JdbcPlugin.getResourceString(e1.getLocalizedMessage()), e1);
				}
				testButton.setEnabled(true);
			}

		});

		manageButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				JdbcDriverManagerDialog dlg = new JdbcDriverManagerDialog(getShell());

				manageButton.setEnabled(false);
				testButton.setEnabled(false);

				if (dlg.open() == Window.OK) {
					BusyIndicator.showWhile(getShell() == null ? null : getShell().getDisplay(), new Runnable() {

						@Override
						public void run() {
							okPressedProcess();
						}
					});
				}

				updateTestButton();
				manageButton.setEnabled(true);
			}
		});

	}

	private void okPressedProcess() {
	}

	private boolean testConnection() throws OdaException {
		if (!isValidDataSource()) {
			return false;
		}

		String url = jdbcUrl.getText().trim();
		String userid = userName.getText().trim();
		String passwd = password.getText();
		String driverName = HiveConstants.HS2_JDBC_DRIVER_CLASS;

		return DriverLoader.testConnection(driverName, url, null, userid, passwd);
	}

	private boolean isValidDataSource() {
		return !isURLBlank();
	}

	private boolean isURLBlank() {
		return jdbcUrl == null || jdbcUrl.getText().trim().length() == 0;
	}

	private Shell getShell() {
		if (m_wizardPage != null) {
			return m_wizardPage.getShell();
		} else if (m_propertyPage != null) {
			return m_propertyPage.getShell();
		} else {
			return null;
		}
	}

	private void updateTestButton() {
		if (isURLBlank()) {
			// Jdbc Url cannot be blank
			setMessage(EMPTY_URL, IMessageProvider.ERROR);
			testButton.setEnabled(false);
		} else {
			setMessage(DEFAULT_MESSAGE);
			if (!testButton.isEnabled()) {
				testButton.setEnabled(true);
			}
		}
	}

	/**
	 * Reset the testButton to "enabled" state, as appropriate.
	 */
	void resetTestButton() {
		updateTestButton();
		enableParent(testButton);
	}

	/**
	 * Enable the specified composite.
	 */
	private void enableParent(Control control) {
		Composite parent = control.getParent();
		if (parent == null || parent instanceof Shell) {
			return;
		}
		if (!parent.isEnabled()) {
			parent.setEnabled(true);
		}
		enableParent(parent);
	}

	private void setMessage(String message, int type) {
		if (m_wizardPage != null) {
			m_wizardPage.setMessage(message, type);
		} else if (m_propertyPage != null) {
			m_propertyPage.setMessage(message, type);
		}
	}

	private void verifyJDBCProperties() {
		if (!isURLBlank()) {
			setPageComplete(true);
		} else {
			setPageComplete(false);
		}
	}

	private void setPageComplete(boolean complete) {
		if (m_wizardPage != null) {
			m_wizardPage.setPageComplete(complete);
		} else if (m_propertyPage != null) {
			m_propertyPage.setValid(complete);
		}
	}

}
