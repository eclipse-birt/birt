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

package org.eclipse.birt.report.designer.data.ui.dataset;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.impl.DataSetMetaDataHelper;
import org.eclipse.birt.report.designer.data.ui.dataset.DataSetPreviewer.PreviewType;
import org.eclipse.birt.report.designer.data.ui.util.DTPUtil;
import org.eclipse.birt.report.designer.data.ui.util.DataSetProvider;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.properties.AbstractPropertyPage;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.api.elements.structures.DataSetParameter;
import org.eclipse.datatools.connectivity.oda.util.ResourceIdentifiers;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.PlatformUI;

/**
 * Property page to preview the output parameters.
 *
 */

public class OutputParameterPreviewPage extends AbstractPropertyPage implements Listener {
	private Table outputParameterTable = null;
	private boolean modelChanged = true;

	/**
	 * The constructor.
	 */
	public OutputParameterPreviewPage() {
		super();
	}

	/*
	 * @see org.eclipse.birt.report.designer.ui.IPropertyPage#createPageControl(org.
	 * eclipse.swt.widgets.Composite)
	 */
	@Override
	public Control createPageControl(Composite parent) {
		outputParameterTable = new Table(parent, SWT.FULL_SELECTION | SWT.MULTI);
		outputParameterTable.setHeaderVisible(true);
		outputParameterTable.setLinesVisible(true);
		((DataSetHandle) getContainer().getModel()).addListener(this);

		outputParameterTable.addMouseListener(new MouseAdapter() {

			/*
			 * (non-Javadoc)
			 *
			 * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.
			 * MouseEvent)
			 */
			@Override
			public void mouseUp(MouseEvent e) {
				// if not mouse left button
				if (e.button != 1) {
					MenuManager menuManager = new MenuManager();

					ResultSetTableAction copyAction = ResultSetTableActionFactory
							.createResultSetTableAction(outputParameterTable, ResultSetTableActionFactory.COPY_ACTION);
					ResultSetTableAction selectAllAction = ResultSetTableActionFactory.createResultSetTableAction(
							outputParameterTable, ResultSetTableActionFactory.SELECTALL_ACTION);
					menuManager.add(copyAction);
					menuManager.add(selectAllAction);

					menuManager.update();

					copyAction.update();
					selectAllAction.update();

					Menu contextMenu = menuManager.createContextMenu(outputParameterTable);

					contextMenu.setEnabled(true);
					contextMenu.setVisible(true);
				}
			}
		});

		return outputParameterTable;
	}

	/*
	 * @see
	 * org.eclipse.birt.model.core.Listener#elementChanged(org.eclipse.birt.model.
	 * api.DesignElementHandle, org.eclipse.birt.model.activity.NotificationEvent)
	 */
	@Override
	public void elementChanged(DesignElementHandle focus, NotificationEvent ev) {
		if (focus.equals(getContainer().getModel())) {
			modelChanged = true;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.ui.IPropertyPage#pageActivated()
	 */
	@Override
	public void pageActivated() {
		getContainer().setMessage(Messages.getString("dataset.editor.outputparameters"), //$NON-NLS-1$
				IMessageProvider.NONE);

		if (modelChanged || ((DataSetEditor) this.getContainer()).modelChanged()) {
			modelChanged = false;
			runUpdateResults();
		}
	}

	/**
	 * Update table result
	 */
	private void runUpdateResults() {
		if (outputParameterTable != null && !outputParameterTable.isDisposed()) {
			clearResultSetTable();
			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
				/*
				 * (non-Javadoc)
				 *
				 * @see java.lang.Runnable#run()
				 */
				@Override
				public void run() {
					if (outputParameterTable != null && !outputParameterTable.isDisposed()) {
						updateResults();
					}
				}
			});
		}
	}

	/**
	 * Clear table result and release its UI resource
	 */
	private void clearResultSetTable() {
		// Clear the columns
		TableColumn[] columns = outputParameterTable.getColumns();
		for (int n = 0; n < columns.length; n++) {
			columns[n].dispose();
		}
		// clear everything else
		outputParameterTable.removeAll();
	}

	/**
	 * update output parameter table result
	 */
	private void updateResults() {
		int outputParamsSize = outputParametersSize();
		if (outputParamsSize == 0) {
			return;
		}

		DataRequestSession session = null;

		ModuleHandle handle;
		DataSetHandle dsHandle = ((DataSetEditor) getContainer()).getHandle();
		handle = dsHandle.getModuleHandle();
		DataSetPreviewer previewer = new DataSetPreviewer(dsHandle, 1, PreviewType.OUTPUTPARAM);

		Map appContext = new HashMap();
		Map dataSetBindingMap = new HashMap();
		Map dataSourceBindingMap = new HashMap();

		TableLayout layout = new TableLayout();
		TableColumn column = null;
		TableItem tableItem = null;
		try {
			ResourceIdentifiers identifiers = new ResourceIdentifiers();
			String resouceIDs = ResourceIdentifiers.ODA_APP_CONTEXT_KEY_CONSUMER_RESOURCE_IDS;
			identifiers.setApplResourceBaseURI(DTPUtil.getInstance().getBIRTResourcePath());
			identifiers.setDesignResourceBaseURI(DTPUtil.getInstance().getReportDesignPath());
			appContext.put(resouceIDs, identifiers);
			clearProperyBindingMap(dataSetBindingMap, dataSourceBindingMap);

			AppContextPopulator.populateApplicationContext(dsHandle, appContext);
			previewer.open(appContext, getEngineConfig(handle));
			IResultIterator iter = previewer.preview();
			iter.next();

			IResultMetaData meta = iter.getResultMetaData();
			String[] record = new String[meta.getColumnCount()];
			for (int n = 0; n < record.length; n++) {
				column = new TableColumn(outputParameterTable, SWT.LEFT);
				column.setText(meta.getColumnName(n + 1));
				column.setResizable(true);
				layout.addColumnData(new ColumnPixelData(120, true));
				record[n] = iter.getString(meta.getColumnName(n + 1));
			}
			outputParameterTable.setLayout(layout);
			outputParameterTable.layout(true);

			tableItem = new TableItem(outputParameterTable, SWT.NONE);
			tableItem.setText(record);

			iter.close();
		} catch (BirtException e) {
			ExceptionHandler.handle(e);
		} finally {
			try {
				AppContextResourceReleaser.release(appContext);
				previewer.close();
			} catch (BirtException e) {
				e.printStackTrace();
			}
			resetPropertyBinding(dataSetBindingMap, dataSourceBindingMap);
		}
	}

	private EngineConfig getEngineConfig(ModuleHandle handle) {
		EngineConfig ec = new EngineConfig();
		ClassLoader parent = Thread.currentThread().getContextClassLoader();
		if (parent == null) {
			parent = this.getClass().getClassLoader();
		}
		ClassLoader customClassLoader = DataSetProvider.getCustomScriptClassLoader(parent, handle);
		ec.getAppContext().put(EngineConstants.APPCONTEXT_CLASSLOADER_KEY, customClassLoader);
		return ec;
	}

	private void resetPropertyBinding(final Map dataSetBindingMap, final Map dataSourceBindingMap) {
		Display.getDefault().syncExec(new Runnable() {

			@Override
			public void run() {
				try {
					DataSetHandle dsHandle = ((DataSetEditor) getContainer()).getHandle();
					DataSetMetaDataHelper.resetPropertyBinding(dsHandle, dataSetBindingMap, dataSourceBindingMap);
				} catch (SemanticException e) {
					ExceptionHandler.handle(e);
				}
			}
		});
	}

	private void clearProperyBindingMap(final Map dataSetBindingMap, final Map dataSourceBindingMap) {
		Display.getDefault().syncExec(new Runnable() {

			@Override
			public void run() {
				DataSetHandle dsHandle = ((DataSetEditor) getContainer()).getHandle();
				try {
					DataSetMetaDataHelper.clearPropertyBindingMap(dsHandle, dataSetBindingMap, dataSourceBindingMap);
				} catch (SemanticException e) {
					ExceptionHandler.handle(e);
				}
			}
		});
	}

	/**
	 * @return the count of output parameters
	 */
	private int outputParametersSize() {
		// first check whether parameter list is null
		PropertyHandle propertyHandle = ((DataSetEditor) getContainer()).getHandle()
				.getPropertyHandle(DataSetHandle.PARAMETERS_PROP);
		List paramList = propertyHandle.getListValue();
		if (paramList == null || paramList.size() == 0) {
			return 0;
		}

		// second check whether there is output parameter
		int size = 0;
		int paramSize = paramList.size();
		for (int i = 0; i < paramSize; i++) {
			DataSetParameter parameter = (DataSetParameter) paramList.get(i);
			if (parameter.isOutput()) {
				size++;
			}
		}

		return size;
	}

	/*
	 * @see org.eclipse.birt.report.designer.ui.dialogs.properties.IPropertyPage#
	 * performOk()
	 */
	@Override
	public boolean performOk() {
		((DataSetHandle) getContainer().getModel()).removeListener(this);
		return super.performOk();
	}

	/*
	 * @see org.eclipse.birt.report.designer.ui.dialogs.properties.IPropertyPage#
	 * performCancel()
	 */
	@Override
	public boolean performCancel() {
		((DataSetHandle) getContainer().getModel()).removeListener(this);
		return super.performCancel();
	}

	/*
	 * @see org.eclipse.birt.report.designer.ui.dialogs.properties.IPropertyPage#
	 * getToolTip()
	 */
	@Override
	public String getToolTip() {
		return Messages.getString("dataset.outputparameters.preview.tooltip"); //$NON-NLS-1$
	}

}
