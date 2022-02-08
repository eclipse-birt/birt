/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.ui.odadatasource.wizards;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;

/**
 * @deprecated As of BIRT 2.1, replaced by
 *             {@link org.eclipse.datatools.connectivity.oda.design.ui
 *             org.eclipse.datatools.connectivity.oda.design.ui } .
 */
public abstract class AbstractDataSetWizard extends Wizard {

	private static final String CREATE_DATA_SET_TRANS_NAME = Messages
			.getString("AbstractDataSetWizard.ModelTrans.Create"); //$NON-NLS-1$

	private transient DataSetHandle dataSetHandle;

	private transient DataSourceHandle dataSourceHandle = null;

	private transient String dataSetName = null;

	private transient boolean useTransaction = true;

	private transient IConfigurationElement configurationElement = null;

	public abstract boolean doCancel();

	public abstract boolean doFinish();

	public AbstractDataSetWizard(String title) {
		this();
		setWindowTitle(title);
	}

	public AbstractDataSetWizard() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.wizard.IWizard#createPageControls(org.eclipse.swt.widgets.
	 * Composite)
	 */
	public void createPageControls(Composite pageContainer) {
		// Call getDataSet so that it can create the data set if one hasn't been
		// created
		getDataSet();

		try {
			dataSetHandle.setDataSource(getDataSource().getName());
			dataSetHandle.setName(getDataSetName());
		} catch (SemanticException e) {
			ExceptionHandler.handle(e);
		}
		super.createPageControls(pageContainer);
	}

	public final boolean performFinish() {
		boolean returnValue = doFinish();

		// If the return value is true
		// add it to the slot handle
		if (returnValue) {
			// Add the data source element
			DesignElementHandle parentHandle = HandleAdapterFactory.getInstance().getReportDesignHandleAdapter()
					.getModuleHandle();
			SlotHandle slotHandle = ((ModuleHandle) parentHandle).getDataSets();

			try {
				slotHandle.add(getDataSet());

				// If we are using transactions
				// commit it
				if (isUseTransaction()) {
					getActivityStack().commit();
				}
			} catch (ContentException e) {
				getActivityStack().rollback();
				ExceptionHandler.handle(e);
			} catch (NameException e) {
				getActivityStack().rollback();
				ExceptionHandler.handle(e);
			}
		}

		return returnValue;
	}

	/*
	 * Create a DataSet object and store it in the datSet variable
	 */
	public abstract DataSetHandle createDataSet(ModuleHandle handle);

	public final DataSetHandle getDataSet() {
		if (dataSetHandle == null) {
			if (isUseTransaction()) {
				// Start the transaction
				getActivityStack().startTrans(CREATE_DATA_SET_TRANS_NAME);
			}

			// call create data set to create an empty data set object
			dataSetHandle = createDataSet(
					HandleAdapterFactory.getInstance().getReportDesignHandleAdapter().getModuleHandle());
		}
		return dataSetHandle;
	}

	public final void setDataSource(DataSourceHandle dataSourceHandle) {
		this.dataSourceHandle = dataSourceHandle;
	}

	public final DataSourceHandle getDataSource() {
		return dataSourceHandle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performCancel()
	 */
	public final boolean performCancel() {
		boolean returnValue = doCancel();

		if (returnValue) {
			// If we are using transactions
			// Roll back the Transaction
			if (isUseTransaction()) {
				getActivityStack().rollback();
			}
		}
		return returnValue;
	}

	/**
	 * Gets the activity stack of the report
	 * 
	 * @return returns the stack
	 */
	public CommandStack getActivityStack() {
		return SessionHandleAdapter.getInstance().getCommandStack();
	}

	/**
	 * @return Returns the useTransaction.
	 */
	final boolean isUseTransaction() {
		return useTransaction;
	}

	/**
	 * @param useTransaction The useTransaction to set.
	 */
	public final void setUseTransaction(boolean useTransaction) {
		this.useTransaction = useTransaction;
	}

	/**
	 * @return Returns the dataSetName.
	 */
	public final String getDataSetName() {
		return dataSetName;
	}

	/**
	 * @param dataSetName The dataSetName to set.
	 */
	public final void setDataSetName(String dataSetName) {
		this.dataSetName = dataSetName;
	}

	/**
	 * @return Returns the configurationElement.
	 */
	public final IConfigurationElement getConfigurationElement() {
		return configurationElement;
	}

	/**
	 * @param configurationElement The configurationElement to set.
	 */
	public final void setConfigurationElement(IConfigurationElement configurationElement) {
		this.configurationElement = configurationElement;
	}
}
