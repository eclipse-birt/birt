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

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.datatools.connectivity.internal.ui.dialogs.ExceptionHandler;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.DataSourceDesign;
import org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import org.eclipse.birt.data.oda.pojo.api.Constants;
import org.eclipse.birt.data.oda.pojo.querymodel.PojoQuery;
import org.eclipse.birt.data.oda.pojo.ui.Activator;
import org.eclipse.birt.data.oda.pojo.ui.i18n.Messages;
import org.eclipse.birt.data.oda.pojo.ui.util.HelpUtil;
import org.eclipse.birt.data.oda.pojo.ui.util.Utils;
import org.eclipse.birt.data.oda.pojo.util.ClassLister;
import org.eclipse.birt.data.oda.pojo.util.PojoQueryParser;

/**
 * 
 */

public class DataSetPropertiesWizardPage extends DataSetWizardPage {
	private static final String APP_CONTEXT_KEY_PREFIX = "APP_CONTEXT_KEY_"; //$NON-NLS-1$
	private static Logger logger = Logger.getLogger(DataSetPropertiesWizardPage.class.getName());
	private Text txtPojoDataSetClass;
	private Text txtAppContextKey;

	private DataSetDesign design;

	private URLClassLoader classLoader;
	private String[] filteredClassNames;

	private URL[] getPojoDataSetClassPath() {
		if (design == null) {
			return null;
		}

		DataSourceDesign dataSource = design.getDataSourceDesign();
		String classPath = Utils.getPublicProperty(dataSource, Constants.POJO_DATA_SET_CLASS_PATH);
		try {
			return Utils.createURLParser(this.getHostResourceIdentifiers()).parse(classPath);
		} catch (OdaException e) {
			logger.log(Level.WARNING, "Failed to parse POJO Class Path", e); //$NON-NLS-1$
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage#
	 * collectDataSetDesign(org.eclipse.datatools.connectivity.oda.design.
	 * DataSetDesign)
	 */
	@Override
	protected DataSetDesign collectDataSetDesign(DataSetDesign design) {
		if (this.getControl() == null) {
			return super.collectDataSetDesign(design);
		}

		PojoQuery pq = null;
		String query = design.getQueryText();
		if (query != null && query.length() > 0) {
			try {
				pq = PojoQueryParser.parse(query);
			} catch (OdaException e) {
				logger.log(Level.WARNING, "Failed to parse original query text:" + query, e); //$NON-NLS-1$
			}
		}
		if (pq == null) {
			pq = new PojoQuery(Constants.DEFAULT_VERSION, txtPojoDataSetClass.getText().trim(),
					txtAppContextKey.getText().trim());
			Utils.savePojoQuery(pq, design, getControl().getShell());
		} else {
			if (!txtAppContextKey.getText().trim().equals(pq.getAppContextKey())
					|| !txtPojoDataSetClass.getText().trim().equals(pq.getDataSetClass())) {
				pq.setAppContextKey(txtAppContextKey.getText().trim());
				pq.setDataSetClass(txtPojoDataSetClass.getText().trim());
				Utils.savePojoQuery(pq, design, getControl().getShell());
			}

		}

		return design;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage#
	 * refresh(org.eclipse.datatools.connectivity.oda.design.DataSetDesign)
	 */
	@Override
	protected void refresh(DataSetDesign dataSetDesign) {
		super.refresh(dataSetDesign);
		this.design = dataSetDesign;
		txtAppContextKey.setText(""); //$NON-NLS-1$
		txtPojoDataSetClass.setText(""); //$NON-NLS-1$
		String query = dataSetDesign.getQueryText();
		PojoQuery pq = null;
		if (query != null && query.length() > 0) {
			try {
				pq = PojoQueryParser.parse(query);
			} catch (OdaException e) {
				ExceptionHandler.showException(getControl().getShell(), Messages.getString("FailedParseQueryTitle"), //$NON-NLS-1$
						Messages.getFormattedString("FailedParseQueryMsg", new String[] { query }), e); //$NON-NLS-1$
				setMessage(Messages.getString("FailedParseQueryTitle"), IMessageProvider.ERROR); //$NON-NLS-1$
			}
		}
		if (pq != null) {
			if (pq.getAppContextKey() != null) {
				txtAppContextKey.setText(pq.getAppContextKey().trim());
			}
			if (pq.getDataSetClass() != null) {
				txtPojoDataSetClass.setText(pq.getDataSetClass().trim());
			}
		}

		if (classLoader == null)
			initPageInfos();
	}

	public DataSetPropertiesWizardPage(String pageName) {
		super(pageName);
		this.setMessage(Messages.getString("DataSet.PropertyPageMsg")); //$NON-NLS-1$
		// disable "Finish" button
		this.setPageComplete(false);
	}

	public DataSetPropertiesWizardPage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
		this.setMessage(Messages.getString("DataSet.PropertyPageMsg")); //$NON-NLS-1$
		// disable "Finish" button
		this.setPageComplete(false);

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
		Composite topComposite = new Composite(parent, SWT.NONE);
		topComposite.setLayout(new FillLayout(SWT.VERTICAL));

		Group group = new Group(topComposite, SWT.NONE);
		group.setText(Messages.getString("DataSource.RuntimeProperty")); //$NON-NLS-1$
		group.setLayout(new GridLayout(2, false));
		Label label = new Label(group, SWT.NONE);
		label.setText(Messages.getString("DataSet.PojoDataSetClass")); //$NON-NLS-1$
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		label.setLayoutData(gd);
		txtPojoDataSetClass = new Text(group, SWT.BORDER);
		txtPojoDataSetClass.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		setPageComplete(false);
		txtPojoDataSetClass.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				virifyClassName();
			}

			private void virifyClassName() {
				String name = txtPojoDataSetClass.getText().trim();
				setMessage(Messages.getString("DataSet.PropertyPageMsg")); //$NON-NLS-1$
				if (name.trim().length() == 0) {
					setMessage(Messages.getString("error.PojoDataSet.emptyClassName"), ERROR);
					setPageComplete(false);
				}
				setPageComplete(true);
			}
		});
		Button browseButton = new Button(group, SWT.NONE);
		browseButton.setText(Messages.getString("DataSet.Browse")); //$NON-NLS-1$
		browseButton.setToolTipText(Messages.getString("DataSet.Browse.tooltip")); //$NON-NLS-1$
		browseButton.addSelectionListener(new SelectionAdapter() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events
			 * .SelectionEvent)
			 */
			public void widgetSelected(SelectionEvent e) {
				if (classLoader == null || filteredClassNames == null)
					initPageInfos();

				ClassInputDialog cid = new ClassInputDialog(getControl().getShell(), filteredClassNames,
						txtPojoDataSetClass.getText().trim());
				cid.open();
				if (cid.getInput() != null) {
					txtPojoDataSetClass.setText(cid.getInput().trim());
				}
			}
		});

		label = new Label(group, SWT.NONE);
		label.setText(Messages.getString("DataSet.AppContextKey")); //$NON-NLS-1$
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		label.setLayoutData(gd);
		txtAppContextKey = new Text(group, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		txtAppContextKey.setLayoutData(gd);
		txtAppContextKey.setText(APP_CONTEXT_KEY_PREFIX + this.getInitializationDesign().getName().toUpperCase());

		this.setControl(topComposite);
		this.design = this.getInitializationDesign();
		HelpUtil.setSystemHelp(topComposite, HelpUtil.CONEXT_ID_DATASET_POJO_PROPS);
	}

	private void initPageInfos() {
		URL[] allClassPaths = getPojoDataSetClassPath();
		classLoader = new URLClassLoader(allClassPaths, Activator.class.getClassLoader());
		filteredClassNames = getFilteredClassNames(ClassLister.listClasses(allClassPaths));
	}

	private String[] getFilteredClassNames(String[] allClassNames) {
		if (allClassNames == null || allClassNames.length == 0)
			return new String[0];

		List<String> filteredClassList = new ArrayList<String>();
		for (int i = 0; i < allClassNames.length; i++) {
			try {
				if (org.eclipse.birt.data.oda.pojo.util.Utils
						.isPojoDataSetClass(classLoader.loadClass(allClassNames[i]))) {
					filteredClassList.add(allClassNames[i]);
				}
			} catch (Throwable e) {
				continue;
			}
		}

		return filteredClassList.toArray(new String[0]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.WizardPage#getNextPage()
	 */
	@Override
	public IWizardPage getNextPage() {
		String pojoDataSetClass = txtPojoDataSetClass.getText().trim();
		IWizardPage page = super.getNextPage();
		if (page instanceof ColumnMappingWizardPage) {
			ColumnMappingWizardPage cmw = (ColumnMappingWizardPage) page;
			if (cmw.getPojoDataSetClass() == null || !cmw.getPojoDataSetClass().trim().equals(pojoDataSetClass)) {
				cmw.setPojoDataSetClass(pojoDataSetClass);
				try {
					cmw.initClassStructure(this.getInitializationDesign());
				} catch (Throwable t) {
				}
			}
		}
		this.setPageComplete(true);
		return page;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.WizardPage#canFlipToNextPage()
	 */
	@Override
	public boolean canFlipToNextPage() {
		// "Next" button is enabled only when no error message
		if (isPageComplete() && getMessageType() != ERROR) {
			return true;
		} else {
			return false;
		}
	}

}
