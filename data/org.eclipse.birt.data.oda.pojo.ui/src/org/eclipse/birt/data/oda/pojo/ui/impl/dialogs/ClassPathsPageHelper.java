/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
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
 *******************************************************************************/
package org.eclipse.birt.data.oda.pojo.ui.impl.dialogs;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.eclipse.birt.data.oda.pojo.api.Constants;
import org.eclipse.birt.data.oda.pojo.ui.i18n.Messages;
import org.eclipse.birt.data.oda.pojo.ui.impl.contols.POJOClassTabFolderPage;
import org.eclipse.birt.data.oda.pojo.ui.util.HelpUtil;
import org.eclipse.birt.data.oda.pojo.ui.util.Utils;
import org.eclipse.birt.data.oda.pojo.util.URLParser;
import org.eclipse.datatools.connectivity.IConnection;
import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.design.ResourceIdentifiers;
import org.eclipse.datatools.connectivity.ui.PingJob;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

/**
 * 
 */

public class ClassPathsPageHelper {
	public static final String DEFAULT_MSG = Messages.getString("DataSource.PageMessage"); //$NON-NLS-1$
	private WizardPage wizardPage;

	private ResourceIdentifiers ri;
	private Properties props;

	private Composite parent;
	private TabFolder tabFolder;
	private Button synchronizeCheckbox;

	private POJOClassTabFolderPage runtimePage;
	private POJOClassTabFolderPage designtimePage;

	private boolean needsRefresh, synchronizeClassPath;

	public ClassPathsPageHelper(ResourceIdentifiers ri) {
		this.ri = ri;
	}

	public void setWizardPage(WizardPage page) {
		wizardPage = page;
	}

	public void setResourceIdentifiers(ResourceIdentifiers ri) {
		this.ri = ri;
	}

	public Properties collectCustomProperties(Properties properties) {
		if (properties == null)
			return properties;

		properties.put(Constants.POJO_DATA_SET_CLASS_PATH, runtimePage.getClassPathString());
		properties.put(Constants.SYNCHRONIZE_CLASS_PATH, String.valueOf(synchronizeClassPath));

		if (synchronizeClassPath) {
			properties.put(Constants.POJO_CLASS_PATH, runtimePage.getClassPathString());
		} else {
			properties.put(Constants.POJO_CLASS_PATH, designtimePage.getClassPathString());
		}
		return properties;
	}

	public void createPageCustomControl(Composite parent) {
		this.parent = parent;
		ScrolledComposite sComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		sComposite.setLayout(new GridLayout());
		sComposite.setMinWidth(560);
		sComposite.setExpandHorizontal(true);
		sComposite.setMinHeight(400);
		sComposite.setExpandVertical(true);

		Composite composite = new Composite(sComposite, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
		layout.horizontalSpacing = 10;
		composite.setLayout(layout);

		createTabFolderArea(composite);

		createCheckboxArea(composite);

		Point size = composite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		composite.setSize(size.x, size.y);

		sComposite.setContent(composite);

		HelpUtil.setSystemHelp(parent, HelpUtil.CONEXT_ID_DATASOURCE_POJO);

	}

	protected boolean isPageInitialized() {
		return tabFolder != null && !tabFolder.isDisposed();
	}

	private void createCheckboxArea(Composite composite) {
		Composite bottom = new Composite(composite, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginLeft = 5;
		bottom.setLayout(layout);
		bottom.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		synchronizeCheckbox = new Button(bottom, SWT.CHECK);
		synchronizeCheckbox.setLayoutData(new GridData());
		synchronizeCheckbox.setText(Messages.getString("DataSource.ClassPathPage.synchronize.checkbox.message")); //$NON-NLS-1$
		synchronizeCheckbox.setToolTipText(Messages.getString("DataSource.ClassPathPage.synchronize.checkbox.tooltip")); //$NON-NLS-1$

		synchronizeCheckbox.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				synchronizeClassPath = synchronizeCheckbox.getSelection();
				handleSynchronizeCheckboxSelection();
			}

			public void widgetDefaultSelected(SelectionEvent e) {

			}

		});

		Label label = new Label(bottom, SWT.NONE);
		label.setText(Messages.getString("DataSource.ClassPathPage.synchronize.PrompMessage")); //$NON-NLS-1$
		GridData gd = new GridData();
		gd.horizontalAlignment = 30;
		label.setLayoutData(gd);

		synchronizeCheckbox.setSelection(synchronizeClassPath);
		handleSynchronizeCheckboxSelection();
	}

	private void updateDesigntimePageStatus(boolean enabled) {
		designtimePage.setEnabled(enabled);
	}

	private void createTabFolderArea(Composite composite) {
		Composite tabArea = new Composite(composite, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
		layout.marginWidth = 10;
		tabArea.setLayout(layout);
		GridData gd = new GridData(GridData.FILL_BOTH);
		tabArea.setLayoutData(gd);

		tabFolder = new TabFolder(tabArea, SWT.TOP);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));

		runtimePage = new POJOClassTabFolderPage(this, getApplResourceDir());
		runtimePage.setPrompMessage(Messages.getString("DataSource.POJOClassTabFolderPage.promptLabel.runtime")); //$NON-NLS-1$
		TabItem runtimeTab = runtimePage.createContents(tabFolder);
		runtimeTab.setText(Messages.getString("DataSource.POJOClasses.tab.runtime")); //$NON-NLS-1$

		designtimePage = new POJOClassTabFolderPage(this, getApplResourceDir());
		designtimePage.setPrompMessage(Messages.getString("DataSource.POJOClassTabFolderPage.promptLabel.designtime")); //$NON-NLS-1$
		TabItem designTimeTab = designtimePage.createContents(tabFolder);
		designTimeTab.setText(Messages.getString("DataSource.POJOClasses.tab.designTime")); //$NON-NLS-1$

		runtimePage.setFriendPage(designtimePage);
		designtimePage.setFriendPage(runtimePage);

		initControlValues();
	}

	protected void setInitialProperties(Properties dataSourceProps) {
		if (dataSourceProps != null) {
			props = dataSourceProps;
			needsRefresh = true;
		} else {
			props = new Properties();
		}
	}

	public void refresh() {
		if (needsRefresh && runtimePage != null && designtimePage != null) {
			String dataSetClassPath = props.getProperty(Constants.POJO_DATA_SET_CLASS_PATH);
			String pojoClassPath = props.getProperty(Constants.POJO_CLASS_PATH);

			// UI controls are already created
			runtimePage.setClassPath(dataSetClassPath);
			designtimePage.setClassPath(pojoClassPath);

			runtimePage.refresh();
			designtimePage.refresh();

		}
		needsRefresh = false;
	}

	private void initControlValues() {
		if (runtimePage != null && props != null) {
			String dataSetClassPath = props.getProperty(Constants.POJO_DATA_SET_CLASS_PATH);
			String pojoClassPath = props.getProperty(Constants.POJO_CLASS_PATH);
			String value = props.getProperty(Constants.SYNCHRONIZE_CLASS_PATH);
			if (value != null) {
				synchronizeClassPath = Boolean.valueOf(value);
			} else {
				synchronizeClassPath = false;
			}

			// UI controls are already created
			runtimePage.setClassPath(dataSetClassPath);
			designtimePage.setClassPath(pojoClassPath);
		}
		runtimePage.initClassPathElements();
		designtimePage.initClassPathElements();
	}

	private File getApplResourceDir() {
		if (ri != null) {
			if (ri.getApplResourceBaseURI() != null) {
				return new File(ri.getApplResourceBaseURI());
			}
		}
		return null;
	}

	public void updatePageStatus() {
		if (wizardPage != null)
			wizardPage.setPageComplete(runtimePage.canFinish() && designtimePage.canFinish());
	}

	protected Runnable createTestConnectionRunnable(final IConnectionProfile profile) {
		return new Runnable() {
			public void run() {
				IConnection conn = PingJob.createTestConnection(profile);

				Throwable exception = PingJob.getTestConnectionException(conn);

				if (exception == null) // succeed in creating connection
				{
					exception = testConnection();
				}
				PingJob.PingUIJob.showTestConnectionMessage(parent.getShell(), exception);
				if (conn != null) {
					conn.close();
				}
			}

			private Throwable testConnection() {
				Throwable exception = null;
				if (runtimePage.getClassPathString().length() == 0) {
					exception = new OdaException(Messages.getString("DataSource.MissDataSetPojoClassPath.runtime")); //$NON-NLS-1$
				} else if (designtimePage.getClassPathString().length() == 0) {
					exception = new OdaException(Messages.getString("DataSource.MissDataSetPojoClassPath.designtime")); //$NON-NLS-1$
				} else {
					exception = validateAllJars(exception);
				}
				return exception;
			}

			private Throwable validateAllJars(Throwable exception) {
				URLParser up = Utils.createURLParser(ri);
				try {
					URL[] urls = up.parse(runtimePage.getClassPathString());
					for (URL url : urls) {
						try {
							// check if url exists
							url.openStream().close();
						} catch (IOException e) {
							throw new OdaException(Messages.getFormattedString(
									"DataSource.ClassPathPage.testConnection.failed.runtime", //$NON-NLS-1$
									new Object[] { url.getFile() }));
						}
					}
				} catch (OdaException e1) {
					exception = e1;
				}

				if (exception != null)
					return exception;

				try {
					URL[] urls = up.parse(designtimePage.getClassPathString());
					for (URL url : urls) {
						try {
							// check if url exists
							url.openStream().close();
						} catch (IOException e) {
							throw new OdaException(Messages.getFormattedString(
									"DataSource.ClassPathPage.testConnection.failed.designtime", //$NON-NLS-1$
									new Object[] { url.getFile() }));
						}
					}
				} catch (OdaException e1) {
					exception = e1;
				}
				return exception;
			}
		};
	}

	private void handleSynchronizeCheckboxSelection() {
		updateDesigntimePageStatus(!synchronizeClassPath);
		if (synchronizeClassPath) {
			designtimePage.resetJarElements(runtimePage.getJarElements());
		}
		updatePageStatus();
	}

}
