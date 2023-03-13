/*******************************************************************************
 * Copyright (c) 2005, 2008 Actuate Corporation.
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
 *******************************************************************************/
package org.eclipse.birt.report.designer.data.ui.dataset;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.data.ui.property.AbstractDescriptionPropertyPage;
import org.eclipse.birt.report.designer.data.ui.util.Utility;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

public class DataSetDataSourceSelectionPage extends AbstractDescriptionPropertyPage implements Listener {

	Combo combo;
	int lastSelectedDataSourceIndex = -1;

	public DataSetDataSourceSelectionPage() {
		super();
	}

	@Override
	public Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);

		Label label = new Label(composite, SWT.NONE);
		label.setText(Messages.getString("dataset.editor.label.selectDataSource")); //$NON-NLS-1$

		combo = new Combo(composite, SWT.BORDER | SWT.READ_ONLY);
		combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// populate the combo box with data sources of the same type
		combo.setItems(getSimilarDataSources());
		combo.setVisibleItemCount(30);
		selectCurrentDataSource();

		((DataSetHandle) this.getContainer().getModel()).addListener(this);

		return composite;
	}

	@Override
	public void pageActivated() {
		getContainer().setMessage(Messages.getString("dataset.editor.dataSource"), IMessageProvider.NONE); //$NON-NLS-1$
		lastSelectedDataSourceIndex = combo.getSelectionIndex();
	}

	private void selectCurrentDataSource() {
		String current = getCurrentDataSource().getName();
		String[] items = combo.getItems();
		for (int n = 0; n < items.length; n++) {
			if (items[n].equals(current)) {
				combo.select(n);
				break;
			}
		}
	}

	private DataSourceHandle getCurrentDataSource() {
		return ((DataSetHandle) getContainer().getModel()).getDataSource();
	}

	private String[] getSimilarDataSources() {
		DataSourceHandle currentDataSource = getCurrentDataSource();
		return getSimilarDataSources(currentDataSource);
	}

	private String[] getSimilarDataSources(DataSourceHandle currentDataSource) {
		ArrayList similarDataSources = new ArrayList();
		List dataSources = Utility.getDataSources();
		Class clazz = currentDataSource.getClass();
		if (dataSources != null && !dataSources.isEmpty()) {
			Iterator iter = dataSources.iterator();
			while (iter.hasNext()) {
				DataSourceHandle dataSource = (DataSourceHandle) iter.next();

				if (dataSource.getClass().equals(clazz)) {
					if (clazz.equals(OdaDataSourceHandle.class)) {
						if (((OdaDataSourceHandle) dataSource).getExtensionID()
								.equals(((OdaDataSourceHandle) currentDataSource).getExtensionID())) {
							similarDataSources.add(dataSource.getName());
						}
					} else {
						similarDataSources.add(dataSource.getName());
					}

				}
			}
		}
		return (String[]) similarDataSources.toArray(new String[] {});
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.ui.dialogs.properties.AbstractPropertyPage
	 * #canLeave()
	 */
	@Override
	public boolean canLeave() {
		try {
			if (combo != null && !combo.isDisposed()) {
				if (lastSelectedDataSourceIndex != combo.getSelectionIndex()) {
					DataSetHandle datasetHandle = (DataSetHandle) getContainer().getModel();
					datasetHandle.setDataSource(combo.getItem(combo.getSelectionIndex()));
					((DataSetEditor) (getContainer())).updateDataSetDesign(this);
					if (datasetHandle instanceof OdaDataSetHandle
							&& datasetHandle.getProperty(DataSetHandle.RESULT_SET_PROP) != null) {
						datasetHandle.clearProperty(DataSetHandle.RESULT_SET_PROP);
					}
				}
			}
		} catch (SemanticException e) {
			ExceptionHandler.handle(e);
			return false;
		}
		return super.canLeave();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.ui.dialogs.properties.AbstractPropertyPage
	 * #performOk()
	 */
	@Override
	public boolean performOk() {
		try {
			if (combo != null && !combo.isDisposed() && combo.getSelectionIndex() > -1) {
				if (lastSelectedDataSourceIndex != combo.getSelectionIndex()) {
					DataSetHandle datasetHandle = (DataSetHandle) getContainer().getModel();
					datasetHandle.setDataSource(combo.getItem(combo.getSelectionIndex()));
					((DataSetEditor) (getContainer())).updateDataSetDesign(this);

					if (datasetHandle instanceof OdaDataSetHandle
							&& datasetHandle.getProperty(DataSetHandle.RESULT_SET_PROP) != null) {
						datasetHandle.clearProperty(DataSetHandle.RESULT_SET_PROP);
					}
				}
			}
		} catch (SemanticException e) {
			ExceptionHandler.handle(e);
			return false;
		}

		((DataSetHandle) (getContainer().getModel())).removeListener(this);
		return super.performOk();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.ui.IPropertyPage#performCancel()
	 */
	@Override
	public boolean performCancel() {
		((DataSetHandle) (getContainer().getModel())).removeListener(this);
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.ui.dialogs.properties.IPropertyPage#
	 * getToolTip()
	 */
	@Override
	public String getToolTip() {
		return Messages.getString("dataset.editor.dataSource.Tooltip"); //$NON-NLS-1$
	}

	@Override
	public void elementChanged(DesignElementHandle focus, NotificationEvent ev) {
	}
}
