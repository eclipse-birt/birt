/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.chart.reportitem.ui.views.attributes.provider;

import org.eclipse.birt.chart.reportitem.ChartReportItemUtil;
import org.eclipse.birt.chart.reportitem.ui.ChartFilterFactory;
import org.eclipse.birt.chart.reportitem.ui.ChartReportItemUIUtil;
import org.eclipse.birt.chart.reportitem.ui.dialogs.ChartCubeFilterConditionBuilder;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizard;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.AbstractFilterHandleProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IFormProvider;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.FilterConditionBuilder;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.FilterConditionElementHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.PropertyEvent;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.jface.dialogs.Dialog;

/**
 * The filter provider is used for cube set, it works against setting filters in
 * property page.
 * 
 * @since 2.3
 */
public class ChartCubeFilterHandleProvider extends ChartFilterProviderDelegate {
	private ChartWizardContext context = null;

	public void setContext(ChartWizardContext context) {
		this.context = context;
	}

	public ChartCubeFilterHandleProvider(AbstractFilterHandleProvider baseProvider) {
		super(baseProvider);
		setModelAdapter(new ChartCubeFilterModelProvider());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * IFormHandleProvider#doAddItem(int)
	 */
	public boolean doAddItem(int pos) throws SemanticException {
		// return modelAdapter.doAddItem( input.get( 0 ), pos );
		Object item = getContentInput().get(0);
		if (item instanceof DesignElementHandle) {
			// Create proper chart filter factory which is responsible to create concrete
			// filter handles.
			ChartFilterFactory cff = ChartReportItemUIUtil.createChartFilterFactory(item);
			ChartCubeFilterConditionBuilder dialog = cff.createCubeFilterConditionBuilder(UIUtil.getDefaultShell(),
					FilterConditionBuilder.DLG_TITLE_NEW, FilterConditionBuilder.DLG_MESSAGE_NEW);
			dialog.setDesignHandle((DesignElementHandle) item, context);
			dialog.setInput(null);
			dialog.setBindingParams(getBindingParams());
			if (item instanceof ReportItemHandle) {
				dialog.setReportElement((ReportItemHandle) item);
			} else if (item instanceof GroupHandle) {
				dialog.setReportElement((ReportItemHandle) ((GroupHandle) item).getContainer());
			}
			if (dialog.open() == Dialog.CANCEL) {
				return false;
			}

		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * IFormHandleProvider#doEditItem(int)
	 */
	public boolean doEditItem(int pos) {

		Object item = getContentInput().get(0);
		if (item instanceof DesignElementHandle) {
			DesignElementHandle element = (DesignElementHandle) item;
			PropertyHandle propertyHandle = element.getPropertyHandle(ChartReportItemUtil.PROPERTY_CUBE_FILTER);
			FilterConditionElementHandle filterHandle = (FilterConditionElementHandle) (propertyHandle.getListValue()
					.get(pos));
			if (filterHandle == null) {
				return false;
			}
			ChartFilterFactory cff;
			try {
				cff = ChartReportItemUIUtil.createChartFilterFactory(item);
			} catch (ExtendedElementException e) {
				ChartWizard.displayException(e);
				return false;
			}
			ChartCubeFilterConditionBuilder dialog = cff.createCubeFilterConditionBuilder(UIUtil.getDefaultShell(),
					FilterConditionBuilder.DLG_TITLE_EDIT, FilterConditionBuilder.DLG_MESSAGE_EDIT);
			dialog.setDesignHandle((DesignElementHandle) item, context);
			dialog.setInput(filterHandle);
			dialog.setBindingParams(getBindingParams());
			if (item instanceof ReportItemHandle) {
				dialog.setReportElement((ReportItemHandle) item);
			} else if (item instanceof GroupHandle) {
				dialog.setReportElement((ReportItemHandle) ((GroupHandle) item).getContainer());
			}
			if (dialog.open() == Dialog.CANCEL) {
				return false;
			}

		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.
	 * FilterHandleProvider#isEditable()
	 */
	public boolean isEditable() {
		if (((ReportItemHandle) DEUtil.getInputFirstElement(getInput())).getCube() != null) {
			return true;
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider.
	 * CrosstabFilterHandleProvider#getConcreteFilterProvider()
	 */
	public IFormProvider getConcreteFilterProvider() {
		if (input == null) {
			return this;
		}

		return ChartFilterProviderDelegate.createFilterProvider(input, getInput());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.
	 * FilterHandleProvider#needRefreshed(org.eclipse.birt.report.model.api.activity
	 * .NotificationEvent)
	 */
	public boolean needRefreshed(NotificationEvent event) {
		if (event instanceof PropertyEvent) {
			String propertyName = ((PropertyEvent) event).getPropertyName();
			if (ChartReportItemUtil.PROPERTY_CUBE_FILTER.equals(propertyName)) {
				return true;
			}
		}

		return super.needRefreshed(event);
	}

	@Override
	public void add(int pos) throws Exception {
		boolean sucess = false;
		CommandStack stack = getActionStack();
		stack.startTrans(Messages.getString("FormPage.Menu.ModifyProperty")); //$NON-NLS-1$
		try {
			sucess = doAddItem(pos);
		} catch (Exception e) {
			stack.rollback();
			throw new Exception(e);
		}
		if (sucess) {
			stack.commit();
		} else {
			stack.rollback();
		}
	}

	@Override
	public boolean edit(int pos) {
		CommandStack stack = getActionStack();
		stack.startTrans(Messages.getString("FormPage.Menu.ModifyProperty")); //$NON-NLS-1$
		if (!doEditItem(pos)) {
			stack.rollback();
			return false;
		}
		stack.commit();
		return true;
	}
}
