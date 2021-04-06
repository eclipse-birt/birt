/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.ui.odadatasource.wizards;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;

/**
 * @deprecated As of BIRT 2.1, replaced by
 *             {@link org.eclipse.datatools.connectivity.oda.design.ui
 *             org.eclipse.datatools.connectivity.oda.design.ui } .
 */
public abstract class AbstractDataSourceConnectionWizard extends Wizard {

	private static final String CREATE_DATA_SOURCE_TRANS_NAME = Messages
			.getString("wizard.transaction.createDataSource"); //$NON-NLS-1$
	// private transient String finishLabel = IDialogConstants.FINISH_LABEL;

	private transient IConfigurationElement configurationElement = null;

	// The handle of the Node "Data Sources" Node in the Data View
	// to which the newly created Data Source will be added
	private transient DataSourceHandle dataSourceHandle;

	/**
	 * Creates a wizard to create or edit element
	 * 
	 * @param title the wizard title
	 */
	public AbstractDataSourceConnectionWizard(String title) {
		this();
		setWindowTitle(title);
	}

	/**
	 * Creates a wizard to create or edit element
	 * 
	 */
	public AbstractDataSourceConnectionWizard() {
		super();
		setForcePreviousAndNextButtons(true);

	}

	/*
	 * Add Logic of storing teh actual data Source connection Each subclassed wizard
	 * will have to implenent this
	 * 
	 */
	public abstract DataSourceHandle createDataSource(ModuleHandle handle);

	/*
	 * This method checks if a data source has been created If not then it calls the
	 * createDataSource method to create a data source.
	 */
	public DataSourceHandle getDataSource() {
		if (dataSourceHandle == null) {
			// Start the transaction
			getActivityStack().startTrans(CREATE_DATA_SOURCE_TRANS_NAME);

			dataSourceHandle = createDataSource(
					HandleAdapterFactory.getInstance().getReportDesignHandleAdapter().getModuleHandle());
		}
		return dataSourceHandle;
	}

	/*
	 *  
	 */
	public abstract boolean doFinish();

	public abstract boolean doCancel();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	public final boolean performFinish() {
		boolean returnValue = doFinish();
		try {
			if (returnValue) {
				// Add the data source element
				DesignElementHandle parentHandle = HandleAdapterFactory.getInstance().getReportDesignHandleAdapter()
						.getModuleHandle();
				SlotHandle slotHandle = ((ModuleHandle) parentHandle).getDataSources();

				slotHandle.add(dataSourceHandle);

				getActivityStack().commit();
			}

		} catch (Exception e) {
			ExceptionHandler.handle(e);
			getActivityStack().rollback();
		}

		return returnValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	public final boolean performCancel() {
		boolean returnValue = doCancel();
		if (returnValue) {
			getActivityStack().rollback();
		}
		return returnValue;
	}

	/**
	 * Sets finish Label
	 * 
	 * @param newLabel the label to be set
	 */
//	protected void setFinishLabel( String newLabel )
//	{
//		finishLabel = newLabel;
//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.IWizard#isHelpAvailable()
	 */
	public boolean isHelpAvailable() {
		return true;
	}

	/**
	 * Gets the activity stack of the report
	 * 
	 * @return returns the stack
	 */
	public CommandStack getActivityStack() {
		return SessionHandleAdapter.getInstance().getCommandStack();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.IWizard#getNextPage(org.eclipse.jface.wizard.
	 * IWizardPage)
	 */
	public IWizardPage getNextPage(IWizardPage page) {
		// Get the pages and check whether this is the last page in this wizard
		/*
		 * if ( page.getWizard( ) == this && page == getPages( )[getPages( ).length - 1]
		 * ) { //Get the default data set wizard if ( dataSetWizard == null ) {
		 * dataSetWizard = new DefaultDataSetWizard( dataSourceHandle, false ); //Allow
		 * the wizard to create its pages dataSetWizard.addPages( ); } //Return the
		 * starting page of the data set wizard return dataSetWizard.getStartingPage( );
		 * }
		 */
		return super.getNextPage(page);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.wizard.IWizard#createPageControls(org.eclipse.swt.widgets.
	 * Composite)
	 */
	public void createPageControls(Composite pageContainer) {
		// create an empty data source object if one has not been created yet
		getDataSource();

		super.createPageControls(pageContainer);
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
