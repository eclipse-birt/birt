/*
 *************************************************************************
 * Copyright (c) 2005, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.report.data.oda.jdbc.ui.profile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.eclipse.birt.report.data.oda.jdbc.ui.JdbcPlugin;
import org.eclipse.birt.report.data.oda.jdbc.ui.dialogs.JdbcDriverManagerDialog;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.Constants;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.DriverLoader;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.ExceptionHandler;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.IHelpConstants;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.JdbcToolKit;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.Utility;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.bidi.profile.BidiSettingsSupport;
import org.eclipse.birt.report.data.oda.jdbc.utils.DriverInfoConstants;
import org.eclipse.birt.report.data.oda.jdbc.utils.JDBCDriverInfoManager;
import org.eclipse.birt.report.data.oda.jdbc.utils.JDBCDriverInformation;
import org.eclipse.birt.report.data.oda.jdbc.utils.PropertyElement;
import org.eclipse.birt.report.data.oda.jdbc.utils.PropertyGroup;
import org.eclipse.birt.report.data.oda.jdbc.utils.ResourceLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.design.ResourceIdentifiers;
import org.eclipse.datatools.connectivity.oda.design.ui.designsession.DesignSessionUtil;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Helper class for jdbc selection page and property page
 * 
 */
abstract class GetSyncResultRunnable<T> implements Runnable {
	T result;
}

public class JDBCSelectionPageHelper {

	private class TestInProcessDialog extends MessageDialog {
		private TestConnectionJob testJob;
		private boolean testCancelled;

		public TestInProcessDialog(Shell parentShell) {
			super(parentShell, "", //$NON-NLS-1$
					null, JdbcPlugin.getResourceString("testInProcessDialog.text"), //$NON-NLS-1$
					MessageDialog.INFORMATION, new String[] {}, 0);
			testCancelled = false;
			// Test connection might be a long task, use a separate thread
			testJob = new TestConnectionJob();
			// Start the Job
			testJob.schedule();
		}

		@Override
		protected Control createDialogArea(Composite parent) {
			Composite container = (Composite) super.createDialogArea(parent);
			Button cancelTestButton = new Button(container, SWT.PUSH);
			cancelTestButton.setLayoutData(new GridData(SWT.RIGHT, SWT.RIGHT, true, true, 1, 1));
			cancelTestButton.setText(JdbcPlugin.getResourceString("testInProcessDialog.cancelButton.label")); //$NON-NLS-1$
			cancelTestButton.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					testCancelled = true;
					if (testJob != null) {
						testJob.cancel();
					}
					testButton.setEnabled(true);
					TestInProcessDialog.this.close();
				}
			});
			return container;
		}

		protected boolean isResizable() {
			return true;
		}

		// overriding this methods allows you to set the
		// title of the custom dialog
		@Override
		protected void configureShell(Shell newShell) {
			super.configureShell(newShell);
			newShell.setText(JdbcPlugin.getResourceString("testInProcessDialog.title")); //$NON-NLS-1$
		}

		public boolean isTestCancelled() {
			return testCancelled;
		}

		private class TestConnectionJob extends Job {
			private boolean isConnected;

			public TestConnectionJob() {
				super("Test connection"); //$NON-NLS-1$
			}

			@Override
			protected IStatus run(IProgressMonitor monitor) {

				try {
					isConnected = testConnection();
				} catch (OdaException e1) {
					isConnected = false;
				}

				// Eclipse UI can only be changed by UI thread
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						// do something in the user interface
						// e.g. set a text field
						if (!isTestCancelled()) {
							testButton.setEnabled(true);
							if (isConnected) {
								// Test connection is usually really quick
								// To avoid rapid dialog toggling in UI, sleep for 0.3s
								try {
									Thread.sleep(300);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								TestInProcessDialog.this.close();

								MessageDialog.openInformation(JDBCSelectionPageHelper.this.getShell(),
										JdbcPlugin.getResourceString("connection.test"), //$NON-NLS-1$
										JdbcPlugin.getResourceString("connection.success"));//$NON-NLS-1$
							} else {
								TestInProcessDialog.this.close();

								OdaException ex = new OdaException(JdbcPlugin.getResourceString("connection.failed")); //$NON-NLS-1$
								ExceptionHandler.showException(JDBCSelectionPageHelper.this.getShell(),
										JdbcPlugin.getResourceString("connection.test"), //$NON-NLS-1$
										JdbcPlugin.getResourceString("connection.failed"), //$NON-NLS-1$
										ex);
							}
						}
					}
				});

				return Status.OK_STATUS;
			}
		}
	}

	private WizardPage m_wizardPage;
	private PreferencePage m_propertyPage;
	// bidi_hcg: Bidi Object containing Bidi formats definitions
	private BidiSettingsSupport bidiSupportObj;

	private static final String EMPTY_STRING = ""; //$NON-NLS-1$

	// combo viewer to show candidate driver class
	private ComboViewer driverChooserCombo;

	// Text of url, name and password
	private Text jdbcUrl, userName, password, jndiName;

	private Composite porpertyGroupComposite;
	// Button of manage driver and test connection
	private Button manageButton, testButton;

	private String DEFAULT_MESSAGE;

	private Map<String, String> databaseProperties = new HashMap<String, String>();

	private Properties profileProperties = null;
	// constant string
	final private static String EMPTY_DRIVER_CLASS_OR_URL = JdbcPlugin.getResourceString("error.emptyDriverclassOrURL");//$NON-NLS-1$

	private final String ENCRYTPION_METHOD_DEFAULT_VALUE = "noEncryption"; //$NON-NLS-1$

	private final String JDBC_EXTENSION_ID = "org.eclipse.birt.report.data.oda.jdbc"; //$NON-NLS-1$
	private org.eclipse.datatools.connectivity.oda.util.ResourceIdentifiers resourceIdentifier;

	JDBCSelectionPageHelper(WizardPage page) {
		DEFAULT_MESSAGE = JdbcPlugin.getResourceString("wizard.message.createDataSource"); //$NON-NLS-1$
		m_wizardPage = page;
		if (page instanceof JDBCSelectionWizardPage) // bidi_hcg
			bidiSupportObj = ((JDBCSelectionWizardPage) page).getBidiSupport();
	}

	JDBCSelectionPageHelper(PreferencePage page) {
		DEFAULT_MESSAGE = JdbcPlugin.getResourceString("wizard.message.editDataSource"); //$NON-NLS-1$
		m_propertyPage = page;
		if (page instanceof JDBCPropertyPage) // bidi_hcg
			bidiSupportObj = ((JDBCPropertyPage) page).getBidiSupport();
	}

	Composite createCustomControl(Composite parent) {
		ScrolledComposite scrollContent = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);

		scrollContent.setAlwaysShowScrollBars(false);
		scrollContent.setExpandHorizontal(true);

		scrollContent.setLayout(new FillLayout());

		// create the composite to hold the widgets
		Composite content = new Composite(scrollContent, SWT.NONE);

		GridLayout layout = new GridLayout();
		layout.numColumns = 4;
		layout.marginBottom = 300;
		content.setLayout(layout);

		GridData gridData;

		// List if all supported data bases
		new Label(content, SWT.RIGHT).setText(JdbcPlugin.getResourceString("wizard.label.driverClass"));//$NON-NLS-1$
		driverChooserCombo = new ComboViewer(content, SWT.DROP_DOWN);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 3; // bidi_hcg
		driverChooserCombo.getControl().setLayoutData(gridData);

		List driverListTmp1 = JdbcToolKit.getJdbcDriversFromODADir(JDBC_EXTENSION_ID);
		JDBCDriverInformation[] driverListTmp2 = JDBCDriverInfoManager.getInstance().getDriversInfo();
		List driverList = new ArrayList();
		for (Object driverInfo : driverListTmp1) {
			if (needCheckHide(driverListTmp2, (JDBCDriverInformation) driverInfo)) {
				if (!((JDBCDriverInformation) driverInfo).getHide()) {
					driverList.add(driverInfo);
				}
			} else {
				driverList.add(driverInfo);
			}
		}
		driverChooserCombo.setContentProvider(new IStructuredContentProvider() {

			public Object[] getElements(Object inputElement) {
				if (inputElement != null) {
					return ((ArrayList) inputElement).toArray();
				}
				return new JDBCDriverInformation[] {};
			}

			public void dispose() {
				// TODO Auto-generated method stub

			}

			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				// TODO Auto-generated method stub

			}

		});

		driverChooserCombo.setLabelProvider(new LabelProvider() {

			public String getText(Object inputElement) {
				JDBCDriverInformation info = (JDBCDriverInformation) inputElement;
				return info.getDisplayString();
			}

		});

		driverChooserCombo.setInput(sortDriverList(driverList));

		driverChooserCombo.addSelectionChangedListener(new ISelectionChangedListener() {

			// store latest driver class name
			private String driverClassName;

			public void selectionChanged(SelectionChangedEvent event) {
				StructuredSelection selection = (StructuredSelection) event.getSelection();
				final JDBCDriverInformation info = (JDBCDriverInformation) selection.getFirstElement();

				String className = (info != null) ? info.getDriverClassName() : EMPTY_STRING;
				if (className.equalsIgnoreCase(driverClassName) == true)
					return;
				driverClassName = className;

				if (info != null) {
					// do nothing
					if (info.getUrlFormat() != null) {
						jdbcUrl.setText(info.getUrlFormat());
					} else {
						jdbcUrl.setText(EMPTY_STRING);
					}
					((GridData) porpertyGroupComposite.getLayoutData()).exclude = true;
					porpertyGroupComposite.setVisible(false);
					porpertyGroupComposite.getParent().layout();

					Control[] children = porpertyGroupComposite.getChildren();
					for (int i = 0; i < children.length; i++) {
						children[i].dispose();
					}
					if (info.hasProperty()) {
						drawPropertyGroups(info);
					}
					porpertyGroupComposite.getParent().layout();
				}
				// TODO - enhance driverinfo extension point and UI to include
				// driver-specific JNDI URL template and jndi properties file
				// name
				jndiName.setText(EMPTY_STRING);

				// Clear off the user name and passwords
				userName.setText(EMPTY_STRING);
				password.setText(EMPTY_STRING);
				updateTestButton();
			}

			private void drawPropertyGroups(final JDBCDriverInformation info) {
				((GridData) porpertyGroupComposite.getLayoutData()).exclude = false;
				porpertyGroupComposite.setVisible(true);
				((GridData) porpertyGroupComposite.getLayoutData()).heightHint = SWT.DEFAULT;
				databaseProperties.clear();
				List<PropertyGroup> propertyGroups = info.getPropertyGroup();
				for (Iterator it = propertyGroups.iterator(); it.hasNext();) {
					PropertyGroup group = (PropertyGroup) (it.next());
					String propertyGroupName = group.getName();
					List<PropertyElement> propertyList = group.getProperties();
					Group propertyGroup = drawPropertyGroup(
							propertyGroupName == null ? EMPTY_STRING : propertyGroupName);
					for (int i = 0; i < propertyList.size(); i++) {
						final String propertyName = propertyList.get(i)
								.getAttribute(DriverInfoConstants.DRIVER_INFO_PROPERTY_NAME);
						Label propertyParam = new Label(propertyGroup, SWT.NONE);
						String propertyParamDisplayName = propertyList.get(i)
								.getAttribute(DriverInfoConstants.DRIVER_INFO_PROPERTY_DISPLAYNAME);
						if (propertyParamDisplayName == null) {
							propertyParamDisplayName = propertyName;
						}
						propertyParam.setText(propertyParamDisplayName);
						propertyParam.setToolTipText(
								propertyList.get(i).getAttribute(DriverInfoConstants.DRIVER_INFO_PROPERTY_DEC));
						GridData gd = new GridData();
						gd.horizontalSpan = 2; // bidi_hcg
						propertyParam.setLayoutData(gd);

						String propertyContent = null;
						if (profileProperties != null && !profileProperties.isEmpty()) {
							propertyContent = getProfileproperty(propertyName);
						}

						if (DriverInfoConstants.DRIVER_INFO_PROPERTY_TYPE_BOOLEN.equalsIgnoreCase(
								propertyList.get(i).getAttribute(DriverInfoConstants.DRIVER_INFO_PROPERTY_TYPE))) {
							drawPropertyCombo(propertyGroup, propertyName, propertyContent);
						} else {
							if (Boolean.valueOf(propertyList.get(i)
									.getAttribute(DriverInfoConstants.DRIVER_INFO_PROPERTY_ENCRYPT))) {
								drawPropertyText(propertyGroup, propertyName, propertyContent, true);
							} else
								drawPropertyText(propertyGroup, propertyName, propertyContent, false);
						}
					}
					propertyGroup.getParent().layout();
				}
			}

			private void drawPropertyText(Group propertyGroup, final String propertyName, String propertyContent,
					boolean encrypt) {
				GridData gd;
				final Text propertyText;
				if (encrypt) {
					propertyText = new Text(propertyGroup, SWT.BORDER | SWT.PASSWORD);
				} else {
					propertyText = new Text(propertyGroup, SWT.BORDER);
				}

				boolean isEncryptionMethod = DriverInfoConstants.DRIVER_INFO_PROPERTY_ENCRYPTION_METHOD
						.equals(propertyName);
				if (propertyContent != null) {
					propertyText.setText(propertyContent);
					databaseProperties.put(propertyName, propertyContent);
				} else if (isEncryptionMethod) {
					propertyText.setText(ENCRYTPION_METHOD_DEFAULT_VALUE);
				}

				gd = new GridData(GridData.FILL_HORIZONTAL);
				gd.horizontalSpan = 3; // bidi_hcg
				propertyText.setLayoutData(gd);

				if (isEncryptionMethod) {
					Label blankLabel = new Label(propertyGroup, SWT.NONE);
					GridData blankLabelGd = new GridData();
					blankLabelGd.horizontalSpan = 2;
					blankLabel.setLayoutData(blankLabelGd);

					Label prompLabel = new Label(propertyGroup, SWT.NONE);
					prompLabel.setText(JdbcPlugin.getResourceString("wizard.label.SSL.EncryptionMethod"));

					GridData labelGd = new GridData(GridData.FILL_HORIZONTAL);
					labelGd.horizontalSpan = 3;
					prompLabel.setLayoutData(labelGd);
				}

				propertyText.addModifyListener(new ModifyListener() {

					public void modifyText(ModifyEvent e) {
						databaseProperties.put(propertyName, propertyText.getText());
					}
				});
				propertyText.getParent().layout();
			}

			private void drawPropertyCombo(Group propertyGroup, final String propertyName, String propertyContent) {
				GridData gd;
				final Combo propertyField = new Combo(propertyGroup, SWT.BORDER | SWT.READ_ONLY);
				propertyField.setItems(new String[] { EMPTY_STRING, "True", "False" });
				if (propertyContent != null) {
					propertyField.setText(propertyContent);
					databaseProperties.put(propertyName, propertyContent);
				} else
					propertyField.setText(EMPTY_STRING);

				propertyField.addSelectionListener(new SelectionListener() {
					public void widgetSelected(SelectionEvent arg0) {
						if (propertyField.getSelectionIndex() == 1) {
							databaseProperties.put(propertyName, "True");
						} else if (propertyField.getSelectionIndex() == 2) {
							databaseProperties.put(propertyName, "False");
						} else {
							databaseProperties.put(propertyName, EMPTY_STRING);
						}
					}

					public void widgetDefaultSelected(SelectionEvent arg0) {
						databaseProperties.put(propertyName, EMPTY_STRING);
					}
				});
				gd = new GridData(GridData.FILL_HORIZONTAL);
				gd.horizontalSpan = 3; // bidi_hcg
				gd.horizontalAlignment = SWT.FILL;
				propertyField.setLayoutData(gd);
				propertyField.getParent().layout();
			}

			private Group drawPropertyGroup(String propertyGroupName) {
				GridData gridData;

				Group propertyGroup = new Group(porpertyGroupComposite, SWT.NONE);

				gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
				gridData.horizontalSpan = 4;
				gridData.horizontalAlignment = SWT.FILL;
				propertyGroup.setText(propertyGroupName);
				propertyGroup.setLayoutData(gridData);
				GridLayout layout = new GridLayout();
				// layout.horizontalSpacing = layout.verticalSpacing = 0;
				layout.marginWidth = layout.marginHeight = 0;
				layout.numColumns = 5;

				Layout parentLayout = porpertyGroupComposite.getParent().getLayout();
				if (parentLayout instanceof GridLayout)
					layout.horizontalSpacing = ((GridLayout) parentLayout).horizontalSpacing;
				propertyGroup.setLayout(layout);
				return propertyGroup;
			}

			private String getProfileproperty(String propertyName) {
				return profileProperties.getProperty(propertyName);
			}
		});

		// initialize Database URL editor
		new Label(content, SWT.RIGHT).setText(JdbcPlugin.getResourceString("wizard.label.url"));//$NON-NLS-1$

		jdbcUrl = new Text(content, SWT.BORDER);
		gridData = new GridData();
		gridData.horizontalSpan = 3; // bidi_hcg
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		jdbcUrl.setLayoutData(gridData);

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

		// JNDI Data Source URL
		String jndiLabel = JdbcPlugin.getResourceString("wizard.label.jndiname"); //$NON-NLS-1$
		new Label(content, SWT.RIGHT).setText(jndiLabel);
		jndiName = new Text(content, SWT.BORDER);
		gridData = new GridData();
		gridData.horizontalSpan = 3; // bidi_hcg
		gridData.horizontalAlignment = SWT.FILL;
		jndiName.setLayoutData(gridData);

		createPropertiesComposite(content);

		manageButton = new Button(content, SWT.PUSH);
		manageButton.setText(JdbcPlugin.getResourceString("wizard.label.manageDriver"));//$NON-NLS-1$

		testButton = new Button(content, SWT.PUSH);
		testButton.setText(JdbcPlugin.getResourceString("wizard.label.testConnection"));//$NON-NLS-1$
		testButton.setLayoutData(new GridData(GridData.CENTER));

		Point size = content.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		content.setSize(size.x, size.y);

		scrollContent.setExpandHorizontal(true);
		scrollContent.setMinWidth(size.x + 20);
		scrollContent.setExpandVertical(true);
		scrollContent.setMinHeight(size.y + 20);

		scrollContent.setContent(content);

		addControlListeners();
		updateTestButton();
		verifyJDBCProperties();

		Utility.setSystemHelp(getControl(), IHelpConstants.CONEXT_ID_DATASOURCE_JDBC);

		return content;
	}

	private void createPropertiesComposite(Composite content) {
		GridData gridData;

		porpertyGroupComposite = new Composite(content, SWT.NONE);

		gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		gridData.horizontalSpan = 4;
		gridData.horizontalAlignment = SWT.FILL;
		gridData.exclude = true;
		porpertyGroupComposite.setLayoutData(gridData);
		GridLayout layout = new GridLayout();
		// layout.horizontalSpacing = layout.verticalSpacing = 0;
		layout.marginWidth = layout.marginHeight = 0;
		layout.numColumns = 5;
		Layout parentLayout = porpertyGroupComposite.getParent().getLayout();
		if (parentLayout instanceof GridLayout)
			layout.horizontalSpacing = ((GridLayout) parentLayout).horizontalSpacing;
		porpertyGroupComposite.setLayout(layout);
	}

	/**
	 * populate properties
	 * 
	 * @param profileProps
	 */
	void initCustomControl(Properties profileProps) {
		if (profileProps == null || profileProps.isEmpty())
			return; // nothing to initialize

		profileProperties = profileProps;
		String driverClass = profileProps.getProperty(Constants.ODADriverClass);
		if (driverClass == null)
			driverClass = EMPTY_STRING;
		setDriverSelection(driverClass);

		String odaUrl = profileProps.getProperty(Constants.ODAURL);
		if (odaUrl == null)
			odaUrl = EMPTY_STRING;
		jdbcUrl.setText(odaUrl);

		String odaUser = profileProps.getProperty(Constants.ODAUser);
		if (odaUser == null)
			odaUser = EMPTY_STRING;
		userName.setText(odaUser);

		String odaPassword = profileProps.getProperty(Constants.ODAPassword);
		if (odaPassword == null)
			odaPassword = EMPTY_STRING;
		password.setText(odaPassword);

		String odaJndiName = profileProps.getProperty(Constants.ODAJndiName);
		if (odaJndiName == null)
			odaJndiName = EMPTY_STRING;
		jndiName.setText(odaJndiName);

		updateTestButton();
		verifyJDBCProperties();
	}

	private boolean needCheckHide(JDBCDriverInformation[] driverInfos, JDBCDriverInformation info) {
		for (JDBCDriverInformation driverInfo : driverInfos) {
			if (driverInfo.getDriverClassName().equals(info.getDriverClassName())) {
				info.setHide(driverInfo.getHide());
				return true;
			}
		}
		return false;
	}

	/**
	 * give a certain class name , set the combo selection.
	 * 
	 * @param originalDriverClassName
	 */
	private void setDriverSelection(String originalDriverClassName) {
		StructuredSelection selection = null;
		JDBCDriverInformation jdbcDriverInfo = findJdbcDriverInfo(driverChooserCombo, originalDriverClassName);
		if (jdbcDriverInfo != null) {
			selection = new StructuredSelection(jdbcDriverInfo);
		} else if (originalDriverClassName.trim().length() == 0) {
			return;
		} else {
			JDBCDriverInformation driverInfo = JDBCDriverInformation.newInstance(originalDriverClassName);
			List driverList = sortDriverList(JdbcToolKit.getJdbcDriversFromODADir(JDBC_EXTENSION_ID));

			driverList.add(0, driverInfo);
			driverChooserCombo.setInput(driverList);
			selection = new StructuredSelection(driverInfo);
		}
		driverChooserCombo.setSelection(selection);
	}

	/**
	 * collection all custom properties
	 * 
	 * @param props
	 * @return
	 */
	Properties collectCustomProperties(Properties props) {
		if (props == null)
			props = new Properties();

		// set custom driver specific properties
		props.setProperty(Constants.ODADriverClass, getDriverClass());
		props.setProperty(Constants.ODAURL, getDriverURL());
		props.setProperty(Constants.ODAUser, getODAUser());
		props.setProperty(Constants.ODAPassword, getODAPassword());
		props.setProperty(Constants.ODAJndiName, getODAJndiName());
		props.putAll(databaseProperties);

		// bidi_hcg: add Bidi formats settings to props
		props = bidiSupportObj.addBidiProperties(props);
		return props;
	}

	/**
	 * get user name
	 * 
	 * @return
	 */
	private String getODAUser() {
		if (userName == null)
			return EMPTY_STRING;
		return getTrimedString(userName.getText());
	}

	/**
	 * get password
	 * 
	 * @return
	 */
	private String getODAPassword() {
		if (password == null)
			return EMPTY_STRING;
		return getTrimedString(password.getText());
	}

	private String getODAJndiName() {
		if (jndiName == null)
			return EMPTY_STRING;
		return getTrimedString(jndiName.getText());
	}

	/**
	 * get driver url
	 * 
	 * @return
	 */
	private String getDriverURL() {
		if (jdbcUrl == null)
			return EMPTY_STRING;
		return getTrimedString(jdbcUrl.getText());
	}

	/**
	 * get driver class
	 * 
	 * @return
	 */
	private String getDriverClass() {
		if (driverChooserCombo == null)
			return EMPTY_STRING;
		return getTrimedString(getSelectedDriverClassName());
	}

	/**
	 * 
	 * @param tobeTrimed
	 * @return
	 */
	private String getTrimedString(String tobeTrimed) {
		if (tobeTrimed != null)
			tobeTrimed = tobeTrimed.trim();
		return tobeTrimed;
	}

	/**
	 * sort the driver list with ascending order
	 * 
	 * @param driverObj
	 * @return
	 */
	private List sortDriverList(List driverObjList) {
		Object[] driverObj = driverObjList.toArray();
		Arrays.sort(driverObj, new Comparator() {

			public int compare(Object o1, Object o2) {
				JDBCDriverInformation it1 = (JDBCDriverInformation) o1;
				JDBCDriverInformation it2 = (JDBCDriverInformation) o2;
				int result = 0;
				result = it1.getDriverClassName().compareTo(it2.getDriverClassName());
				return result;
			}
		});
		List driverList = new ArrayList();
		for (int i = 0; i < driverObj.length; i++) {
			driverList.add(driverObj[i]);
		}
		return driverList;
	}

	/**
	 * Set selected driver in driverChooserViewer combo box
	 * 
	 * @param driverChooserViewer
	 * @param driverList
	 */
	private void setDriverSelection(String originalDriverClassName, ComboViewer driverChooserViewer, List driverList) {
		// there may exist logic error
		if (driverList == null || driverList.size() == 0) {
			return;
		}

		StructuredSelection selection = null;
		JDBCDriverInformation jdbcDriverInfo = findJdbcDriverInfo(driverChooserViewer, originalDriverClassName);
		if (jdbcDriverInfo != null) {
			selection = new StructuredSelection(jdbcDriverInfo);
		} else if (originalDriverClassName.trim().length() == 0) {
			return;
		} else {
			JDBCDriverInformation driverInfo = JDBCDriverInformation.newInstance(originalDriverClassName);
			driverList.add(0, driverInfo);
			driverChooserViewer.setInput(driverList);
			selection = new StructuredSelection(driverInfo);
		}

		driverChooserViewer.setSelection(selection);
	}

	/**
	 * Find specified driver name in driverChooserViewer ComboViewer
	 * 
	 * @param driverChooserViewer
	 * @param driverName
	 * @return
	 */
	private JDBCDriverInformation findJdbcDriverInfo(ComboViewer driverChooserViewer, String driverName) {
		JDBCDriverInformation info = null;

		ArrayList infoList = (ArrayList) driverChooserViewer.getInput();
		// The retrieved name is of the format DriverName (version)
		if (infoList != null) {
			for (int i = 0; i < infoList.size(); i++) {
				JDBCDriverInformation jdbcDriverInfo = (JDBCDriverInformation) infoList.get(i);
				if (jdbcDriverInfo.getDriverClassName().equals(driverName)) {
					info = jdbcDriverInfo;
					break;
				}
			}
		}

		return info;
	}

	/**
	 * Adds event listeners
	 */
	private void addControlListeners() {
		driverChooserCombo.getCombo().addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				if (!driverChooserCombo.getCombo().isFocusControl()
						&& driverChooserCombo.getCombo().getText().trim().length() == 0) {
					return;
				}
				verifyJDBCProperties();
				updateTestButton();
			}
		});

		jdbcUrl.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				if (!jdbcUrl.isFocusControl() && jdbcUrl.getText().trim().length() == 0) {
					return;
				}
				verifyJDBCProperties();
				updateTestButton();
			}
		});

		jndiName.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				if (!jndiName.isFocusControl() && jndiName.getText().trim().length() == 0) {
					return;
				}
				verifyJDBCProperties();
				updateTestButton();
			}
		});

		testButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				testButton.setEnabled(false);
				TestInProcessDialog testDialog = new TestInProcessDialog(getShell());
				testDialog.open();
			}

		});

		manageButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				JdbcDriverManagerDialog dlg = new JdbcDriverManagerDialog(getShell());

				manageButton.setEnabled(false);
				testButton.setEnabled(false);

				if (dlg.open() == Window.OK) {
					BusyIndicator.showWhile(getShell() == null ? null : getShell().getDisplay(), new Runnable() {

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

	/**
	 * processes after pressing ok button
	 * 
	 */
	private void okPressedProcess() {
		String driverClassName = getSelectedDriverClassName();
		List lst = JdbcToolKit.getJdbcDriversFromODADir(JDBC_EXTENSION_ID);
		driverChooserCombo.setInput(sortDriverList(lst));
		setDriverSelection(driverClassName, driverChooserCombo, lst);
	}

	/**
	 * Attempts to connect to the Jdbc Data Source using the properties ( username,
	 * password, driver class ) specified.
	 * 
	 * @param: showErrorMessage is set to true , and error dialog box will be
	 *                          displayed if the connection fails.
	 * 
	 * @return Returns true if the connection is OK,and false otherwise
	 * @throws OdaException
	 */
	private boolean testConnection() throws OdaException {
		if (!isValidDataSource()) {
			return false;
		}

		String url = getJDBCUrl().trim();
		String userid = getUserID().trim();
		String passwd = getPassWD();
		String driverName = getDriverName();
		String jndiNameValue = getJNDIName();

		if (jndiNameValue.length() == 0)
			jndiNameValue = null;

		// bidi_hcg: if we are running with Bidi settings, then testConnection
		// method should perform required Bidi treatment before actually trying
		// to connect
		if (bidiSupportObj == null) {
			if (m_wizardPage instanceof JDBCSelectionWizardPage) {
				bidiSupportObj = ((JDBCSelectionWizardPage) m_wizardPage).getBidiSupport();
			} else if (m_propertyPage instanceof JDBCPropertyPage) {
				bidiSupportObj = ((JDBCPropertyPage) m_propertyPage).getBidiSupport();
			}
		}

		Properties privateProperties = collectSpecifiedProperties();
		Map appContext = new HashMap();
		appContext.put(
				org.eclipse.datatools.connectivity.oda.util.ResourceIdentifiers.ODA_APP_CONTEXT_KEY_CONSUMER_RESOURCE_IDS,
				this.resourceIdentifier);
		ResourceLocator.resolveConnectionProperties(privateProperties, driverName, appContext);

		if (bidiSupportObj != null) {
			return DriverLoader.testConnection(driverName, url, jndiNameValue, userid, passwd,
					bidiSupportObj.getMetadataBidiFormat().toString(), privateProperties);
		}

		return DriverLoader.testConnection(driverName, url, jndiNameValue, userid, passwd, privateProperties);
	}

	private Properties collectSpecifiedProperties() {
		Properties props = new Properties();

		for (String o : databaseProperties.keySet()) {
			if (databaseProperties.get(o) != null && databaseProperties.get(o).trim().length() > 0) {
				props.setProperty(o, databaseProperties.get(o));
			}
		}
		return props;
	}

	/**
	 * Return selected driver class name of DriverChooserCombo, the info of version
	 * and vendor is trimmed.
	 * 
	 * @return selected driver class name
	 */
	private String getSelectedDriverClassName() {
		IStructuredSelection selection = (IStructuredSelection) driverChooserCombo.getSelection();
		if (selection != null && selection.getFirstElement() != null) {
			return ((JDBCDriverInformation) selection.getFirstElement()).getDriverClassName();
		}

		// In case the driver name has been typed in, select this name
		String driverName = driverChooserCombo.getCombo().getText();

		// If the typed in driver name existed in selection list
		if (driverName != null) {
			int count = driverChooserCombo.getCombo().getItemCount();
			for (int i = 0; i < count; i++) {
				if (driverName.equalsIgnoreCase(driverChooserCombo.getCombo().getItem(i))) {
					return ((JDBCDriverInformation) driverChooserCombo.getElementAt(i)).getDriverClassName();
				}
			}
		}

		return driverName;
	}

	// Used inside TestConnection, which is executed by a separate thread
	// To avoid invalid thread access, get value with UI thread
	private String getJDBCUrl() {
		GetSyncResultRunnable<String> getJDBCUrl = new GetSyncResultRunnable<String>() {
			public void run() {
				result = jdbcUrl == null ? null : jdbcUrl.getText();
			}
		};
		Display.getDefault().syncExec(getJDBCUrl);
		return getJDBCUrl.result;
	}

	private String getUserID() {
		GetSyncResultRunnable<String> getUserName = new GetSyncResultRunnable<String>() {
			public void run() {
				result = userName == null ? null : userName.getText();
			}
		};
		Display.getDefault().syncExec(getUserName);
		return getUserName.result;
	}

	private String getPassWD() {
		GetSyncResultRunnable<String> getPassWD = new GetSyncResultRunnable<String>() {
			public void run() {
				result = password == null ? null : password.getText();
			}
		};
		Display.getDefault().syncExec(getPassWD);
		return getPassWD.result;
	}

	private String getDriverName() {
		GetSyncResultRunnable<String> getDriverName = new GetSyncResultRunnable<String>() {
			public void run() {
				result = getSelectedDriverClassName();
			}
		};
		Display.getDefault().syncExec(getDriverName);
		return getDriverName.result;
	}

	private String getJNDIName() {
		GetSyncResultRunnable<String> getJNDINameValue = new GetSyncResultRunnable<String>() {
			public void run() {
				result = getODAJndiName();
			}
		};
		Display.getDefault().syncExec(getJNDINameValue);
		return getJNDINameValue.result;
	}

	/**
	 * Validates the data source and updates the window message accordingly
	 * 
	 * @return
	 */
	private boolean isValidDataSource() {
		return !isURLBlank() || !isJNDIBlank();
	}

	/**
	 * Test if the input URL is blank
	 * 
	 * @return true url is blank
	 */
	private boolean isURLBlank() {
		return jdbcUrl == null || getJDBCUrl().trim().length() == 0;
	}

	/**
	 * Test if the input JNDI is blank
	 * 
	 * @return true JNDI is blank
	 */
	private boolean isJNDIBlank() {
		return jndiName == null || getJNDIName().trim().length() == 0;
	}

	/**
	 * Check if the driver class is blank
	 * 
	 * @return true driver class is blank
	 */
	private boolean isDriverClassBlank() {
		return getSelectedDriverClassName() == null || getSelectedDriverClassName().trim().length() == 0;
	}

	/**
	 * This method should be called in the following occations: 1. The value of
	 * selected driver is changed 2. The value of inputed URL is changed 3. When the
	 * control is created 4.
	 */
	private void updateTestButton() {
		// Jdbc Url cannot be blank
		if (isDriverClassBlank() || isURLBlank()) {
			if (isJNDIBlank()) {
				setMessage(EMPTY_DRIVER_CLASS_OR_URL, IMessageProvider.ERROR);
				testButton.setEnabled(false);
			} else {
				setMessage(DEFAULT_MESSAGE);
				if (!testButton.isEnabled())
					testButton.setEnabled(true);
			}
		} else {
			setMessage(DEFAULT_MESSAGE);
			if (!testButton.isEnabled())
				testButton.setEnabled(true);
		}
	}

	/**
	 * Reset the testButton and manageButton to "enabled" status
	 */
	protected void resetTestAndMngButton() {
		updateTestButton();
		manageButton.setEnabled(true);
		enableParent(manageButton);
	}

	private void verifyJDBCProperties() {
		if (isDriverClassBlank() || isURLBlank()) {
			if (!isJNDIBlank()) {
				setPageComplete(true);
			} else {
				setPageComplete(false);
			}
		} else {
			setPageComplete(true);
		}
	}

	/**
	 * Enable the specific composite
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

	/**
	 * get the Shell from DialogPage
	 * 
	 * @return
	 */
	private Shell getShell() {
		if (m_wizardPage != null)
			return m_wizardPage.getShell();
		else if (m_propertyPage != null)
			return m_propertyPage.getShell();
		else
			return null;
	}

	/**
	 * set page complete
	 * 
	 * @param complete
	 */
	private void setPageComplete(boolean complete) {
		if (m_wizardPage != null)
			m_wizardPage.setPageComplete(complete);
		else if (m_propertyPage != null)
			m_propertyPage.setValid(complete);
	}

	/**
	 * set message
	 * 
	 * @param message
	 */
	private void setMessage(String message) {
		if (m_wizardPage != null)
			m_wizardPage.setMessage(message);
		else if (m_propertyPage != null)
			m_propertyPage.setMessage(message);
	}

	/**
	 * set message
	 * 
	 * @param message
	 * @param type
	 */
	private void setMessage(String message, int type) {
		if (m_wizardPage != null)
			m_wizardPage.setMessage(message, type);
		else if (m_propertyPage != null)
			m_propertyPage.setMessage(message, type);
	}

	public void setDefaultMessage(String message) {
		this.DEFAULT_MESSAGE = message;
	}

	private Control getControl() {
		if (m_wizardPage != null)
			return m_wizardPage.getControl();
		assert (m_propertyPage != null);
		return m_propertyPage.getControl();
	}

	// bidi_hcg
	public void addBidiSettingsButton(Composite parent, Properties props) {
		bidiSupportObj.drawBidiSettingsButton(parent, props);
	}

	public void setResourceIdentifier(ResourceIdentifiers identifiers) {
		if (identifiers != null) {
			this.resourceIdentifier = DesignSessionUtil.createRuntimeResourceIdentifiers(identifiers);
		}
	}
}
