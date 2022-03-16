/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.ParameterBindingDialog;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ComboAndButtonSection;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.LinkedDataSetAdapter;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;

public class DataSetDescriptorProvider extends AbstractDescriptorProvider {

	@Override
	public String getDisplayName() {
		return Messages.getString("Element.ReportItem.dataSet"); //$NON-NLS-1$
	}

	private static final String NONE = Messages.getString("BindingPage.None"); //$NON-NLS-1$

	@Override
	public Object load() {
		String dataSetName;
		if (getReportItemHandle().getDataSet() == null) {
			dataSetName = NONE;
		} else {
			dataSetName = getReportItemHandle().getDataSet().getQualifiedName();
		}
		if (StringUtil.isBlank(dataSetName)) {
			dataSetName = NONE;
		}
		section.getButtonControl().setEnabled(!dataSetName.equals(NONE));
		return dataSetName;
	}

	public String[] getItems() {
		String[] dataSets = ChoiceSetFactory.getDataSets();
		String[] newList = new String[dataSets.length + 1];
		newList[0] = NONE;
		System.arraycopy(dataSets, 0, newList, 1, dataSets.length);
		return newList;
	}

	public boolean isEnable() {
		if (DEUtil.getInputSize(input) != 1) {
			return false;
		}
		return true;
	}

	@Override
	public void save(Object value) throws SemanticException {
		if (value.equals(NONE)) {
			value = null;
		}

		int ret = 0;

		// If current data set name is None and no column binding
		// existing, pop up dilog doesn't need.
		if (!NONE.equals(load().toString()) || getReportItemHandle().getColumnBindings().iterator().hasNext()) {
			MessageDialog prefDialog = new MessageDialog(UIUtil.getDefaultShell(),
					Messages.getString("dataBinding.title.changeDataSet"), //$NON-NLS-1$
					null, Messages.getString("dataBinding.message.changeDataSet"), //$NON-NLS-1$
					MessageDialog.INFORMATION, new String[] { Messages.getString("AttributeView.dialg.Message.Yes"), //$NON-NLS-1$
							Messages.getString("AttributeView.dialg.Message.No"), //$NON-NLS-1$
							Messages.getString("AttributeView.dialg.Message.Cancel") }, //$NON-NLS-1$
					0);

			ret = prefDialog.open();
		}

		switch (ret) {
		// Clear binding info
		case 0:
			resetDataSetReference(value, true);
			break;
		// Doesn't clear binding info
		case 1:
			resetDataSetReference(value, false);
			break;
		// Cancel.
		case 2:
			section.getComboControl().setStringValue(load() == null ? "" //$NON-NLS-1$
					: load().toString());
		}

	}

	protected Object input;

	@Override
	public void setInput(Object input) {
		this.input = input;

	}

	private ReportItemHandle getReportItemHandle() {
		return (ReportItemHandle) DEUtil.getInputFirstElement(input);
	}

	private ComboAndButtonSection section;

	public void setComboAndButtonSection(ComboAndButtonSection section) {
		this.section = section;
	}

	private void resetDataSetReference(Object value, boolean clearHistory) {
		try {
			startTrans(Messages.getString("DataColumBindingDialog.stackMsg.resetReference")); //$NON-NLS-1$
			DataSetHandle dataSet = null;
			if (value != null) {
				dataSet = SessionHandleAdapter.getInstance().getReportDesignHandle().findDataSet(value.toString());
			}
			boolean isExtendedDataModel = false;
			if (dataSet == null && value != null) {
				getReportItemHandle().setDataSet(null);
				isExtendedDataModel = new LinkedDataSetAdapter().setLinkedDataModel(getReportItemHandle(),
						value.toString());
			} else {
				new LinkedDataSetAdapter().setLinkedDataModel(getReportItemHandle(), null);
				getReportItemHandle().setDataSet(dataSet);
			}
			if (clearHistory) {
				getReportItemHandle().getColumnBindings().clearValue();
				getReportItemHandle().getPropertyHandle(ReportItemHandle.PARAM_BINDINGS_PROP).clearValue();
			}
			if (!isExtendedDataModel) {
				dataSetProvider.generateAllBindingColumns();
			}
			commit();
		} catch (SemanticException e) {
			rollback();
			ExceptionUtil.handle(e);
		}
		load();
	}

	/**
	 * Gets the DE CommandStack instance
	 *
	 * @return CommandStack instance
	 */
	private CommandStack getActionStack() {
		return SessionHandleAdapter.getInstance().getCommandStack();
	}

	private void startTrans(String name) {
		if (isEnableAutoCommit()) {
			getActionStack().startTrans(name);
		}
	}

	private void commit() {
		if (isEnableAutoCommit()) {
			getActionStack().commit();
		}
	}

	private void rollback() {
		if (isEnableAutoCommit()) {
			getActionStack().rollback();
		}
	}

	/**
	 * @return Returns the enableAutoCommit.
	 */
	public boolean isEnableAutoCommit() {
		return enableAutoCommit;
	}

	/**
	 * @param enableAutoCommit The enableAutoCommit to set.
	 */
	public void setEnableAutoCommit(boolean enableAutoCommit) {
		this.enableAutoCommit = enableAutoCommit;
	}

	private transient boolean enableAutoCommit = true;

	DataSetColumnBindingsFormHandleProvider dataSetProvider;

	public void setDependedProvider(DataSetColumnBindingsFormHandleProvider provider) {
		this.dataSetProvider = provider;
	}

	public void bindingDialog() {
		ParameterBindingDialog dialog = new ParameterBindingDialog(UIUtil.getDefaultShell(),
				((DesignElementHandle) DEUtil.getInputFirstElement(input)));
		startTrans(""); //$NON-NLS-1$
		if (dialog.open() == Window.OK) {
			commit();
		} else {
			rollback();
		}
	}

}
