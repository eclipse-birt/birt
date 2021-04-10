/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.data.ui.dataset;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.designer.data.ui.util.Utility;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ScriptDataSetHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;

/**
 * TODO: Please document
 * 
 * @version $Revision$ $Date$
 */

public class DefaultDataSetWizard extends Wizard {

	private static final String CREATE_DATA_SET_TRANS_NAME = Messages
			.getString("AbstractDataSetWizard.ModelTrans.Create"); //$NON-NLS-1$

	private transient boolean useTransaction = true;
	private DataSetBasePage dataSetPage;
	private AdvancedColumnDefPage columnDefPage;

	private DataSetHandle dataSetHandle;

	/**
	 *  
	 */
	public DefaultDataSetWizard() {
		this(null, true);

	}

	public DefaultDataSetWizard(DataSourceHandle dataSourceHandle, boolean useTransaction) {
		super();
		this.useTransaction = useTransaction;
		dataSetPage = new DataSetBasePage(useTransaction);
		dataSetPage.setNewDataSource(dataSourceHandle);
		setForcePreviousAndNextButtons(true);
		addPage(dataSetPage);
		columnDefPage = new AdvancedColumnDefPage();
		addPage(columnDefPage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.IWizard#performFinish()
	 */
	public boolean performFinish() {
		if (!canFinish())
			return false;

		if (useTransaction) {
			// Start the transaction
			Utility.getCommandStack().startTrans(CREATE_DATA_SET_TRANS_NAME);
		}
		dataSetHandle = dataSetPage.createSelectedDataSet();

		if (dataSetHandle != null) {
			if (dataSetHandle instanceof ScriptDataSetHandle) {
				columnDefPage.saveResult(dataSetHandle);
			}
			// If we are using transactions
			// commit it
			if (useTransaction) {
				Utility.getCommandStack().commit();
			}
			try {
				createSelectedDataSetTearDown(dataSetHandle);
				DataSetUIUtil.updateColumnCache(dataSetHandle, false);
			} catch (Exception e) {
				if (e instanceof SWTException) {
					SWTException swtException = (SWTException) e;
					if (swtException.code == SWT.ERROR_WIDGET_DISPOSED)
						Utility.log(e);
				}

				Throwable cause = e.getCause();
				if (cause != null && (cause instanceof org.eclipse.birt.data.engine.core.DataException)) {
					Logger logger = Logger.getLogger(DefaultDataSetWizard.class.getName());
					logger.log(Level.WARNING, e.getLocalizedMessage(), e);
				} else {
					ExceptionHandler.handle(e);
				}
			}
		} else {
			// If we are using transactions
			// rollback it
			if (useTransaction) {
				Utility.getCommandStack().rollback();
			}
			return false;
		}
		return true;
	}

	public DataSetHandle getNewCreateDataSetHandle() {
		return this.dataSetHandle;
	}

	/**
	 * Add DataSetHandle to SlotHandle
	 * 
	 * @param dataSetHandle
	 * @throws ContentException
	 * @throws NameException
	 */
	private void createSelectedDataSetTearDown(DataSetHandle dataSetHandle) throws ContentException, NameException {
		DesignElementHandle parentHandle = Utility.getReportModuleHandle();
		SlotHandle slotHandle = ((ModuleHandle) parentHandle).getDataSets();
		slotHandle.add(dataSetHandle);
		if (dataSetHandle instanceof ScriptDataSetHandle) {
			Utility.setScriptActivityEditor();
		}
	}

	public boolean canFinish() {
		dataSetPage.setPageFocus();
		return dataSetPage.canFinish() && columnDefPage.isPageComplete();
	}
}