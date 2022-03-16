/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.model.simpleapi;

import java.io.IOException;

import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.simpleapi.IAction;
import org.eclipse.birt.report.model.api.simpleapi.IDataBinding;
import org.eclipse.birt.report.model.api.simpleapi.IDataItem;
import org.eclipse.birt.report.model.api.simpleapi.IDataSet;
import org.eclipse.birt.report.model.api.simpleapi.IDataSource;
import org.eclipse.birt.report.model.api.simpleapi.IDesignElement;
import org.eclipse.birt.report.model.api.simpleapi.IDynamicText;
import org.eclipse.birt.report.model.api.simpleapi.IFilterCondition;
import org.eclipse.birt.report.model.api.simpleapi.IGrid;
import org.eclipse.birt.report.model.api.simpleapi.IHideRule;
import org.eclipse.birt.report.model.api.simpleapi.IHighlightRule;
import org.eclipse.birt.report.model.api.simpleapi.IImage;
import org.eclipse.birt.report.model.api.simpleapi.ILabel;
import org.eclipse.birt.report.model.api.simpleapi.IList;
import org.eclipse.birt.report.model.api.simpleapi.IMasterPage;
import org.eclipse.birt.report.model.api.simpleapi.IReportDesign;
import org.eclipse.birt.report.model.api.simpleapi.IReportElement;
import org.eclipse.birt.report.model.api.simpleapi.ISortCondition;
import org.eclipse.birt.report.model.api.simpleapi.ITable;
import org.eclipse.birt.report.model.api.simpleapi.ITextItem;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;

/**
 * InternalReportDesign
 */
class InternalReportDesign extends DesignElement implements IReportDesign {

	private ReportDesignHandle report;

	/**
	 * Constructor.
	 *
	 * @param report
	 */

	public InternalReportDesign(ReportDesignHandle report) {
		super(report);
		this.report = report;
	}

	/**
	 * Gets master page script instance.
	 *
	 * @param name
	 * @return master page script instance
	 */

	@Override
	public IMasterPage getMasterPage(String name) {
		MasterPageHandle masterPage = report.findMasterPage(name);
		if (masterPage == null) {
			return null;
		}
		return new MasterPage(masterPage);
	}

	@Override
	public IDataSet getDataSet(String name) {
		DataSetHandle dataSet = report.findDataSet(name);
		if (dataSet == null) {
			return null;
		}
		return new DataSet(dataSet);
	}

	@Override
	public IDataSource getDataSource(String name) {
		DataSourceHandle dataSource = report.findDataSource(name);
		if (dataSource == null) {
			return null;
		}
		return new DataSource(dataSource);
	}

	@Override
	public IReportElement getReportElement(String name) {
		DesignElementHandle element = report.findElement(name);
		IDesignElement elementDesign = ElementUtil.getElement(element);
		return (elementDesign instanceof IReportElement ? (IReportElement) elementDesign : null);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.api.simpleapi.IReportDesign#
	 * getReportElementByID(long)
	 */
	@Override
	public IReportElement getReportElementByID(long id) {
		DesignElementHandle element = report.getElementByID(id);
		IDesignElement elementDesign = ElementUtil.getElement(element);
		return (elementDesign instanceof IReportElement ? (IReportElement) elementDesign : null);
	}

	@Override
	public IDataItem getDataItem(String name) {
		IReportElement element = getReportElement(name);
		if (element instanceof IDataItem) {
			return (IDataItem) element;
		}
		return null;
	}

	@Override
	public IGrid getGrid(String name) {
		IReportElement element = getReportElement(name);
		if (element instanceof IGrid) {
			return (IGrid) element;
		}
		return null;
	}

	@Override
	public IImage getImage(String name) {
		IReportElement element = getReportElement(name);
		if (element instanceof IImage) {
			return (IImage) element;
		}
		return null;
	}

	@Override
	public ILabel getLabel(String name) {
		IReportElement element = getReportElement(name);
		if (element instanceof ILabel) {
			return (ILabel) element;
		}
		return null;
	}

	@Override
	public IList getList(String name) {
		IReportElement element = getReportElement(name);
		if (element instanceof IList) {
			return (IList) element;
		}
		return null;
	}

	@Override
	public ITable getTable(String name) {
		IReportElement element = getReportElement(name);
		if (element instanceof ITable) {
			return (ITable) element;
		}
		return null;
	}

	@Override
	public IDynamicText getDynamicText(String name) {
		IReportElement element = getReportElement(name);
		if (element instanceof IDynamicText) {
			return (IDynamicText) element;
		}
		return null;
	}

	@Override
	public ITextItem getTextItem(String name) {
		IReportElement element = getReportElement(name);
		if (element instanceof ITextItem) {
			return (ITextItem) element;
		}
		return null;
	}

	@Override
	public void setDisplayNameKey(String displayNameKey) throws SemanticException {
		setProperty(IDesignElementModel.DISPLAY_NAME_ID_PROP, displayNameKey);
	}

	@Override
	public String getDisplayNameKey() {
		return report.getDisplayNameKey();
	}

	@Override
	public void setDisplayName(String displayName) throws SemanticException {
		setProperty(IDesignElementModel.DISPLAY_NAME_PROP, displayName);

	}

	@Override
	public String getDisplayName() {
		return report.getDisplayName();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IReportDesign#save()
	 */

	@Override
	public void save() throws IOException {
		report.save();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IReportDesign#saveAs(java
	 * .lang.String)
	 */

	@Override
	public void saveAs(String newName) throws IOException {
		report.saveAs(newName);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IReportDesign#getTheme()
	 */
	@Override
	public String getTheme() {
		return report.getStringProperty(IModuleModel.THEME_PROP);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IReportDesign#setTheme(java
	 * .lang.String)
	 */

	@Override
	public void setTheme(String theme) throws SemanticException {
		setProperty(IModuleModel.THEME_PROP, theme);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.api.simpleapi.IReportDesign#
	 * createFilterCondition()
	 */
	@Override
	public IFilterCondition createFilterCondition() {
		return new FilterConditionImpl();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IReportDesign#createHideRule
	 * ()
	 */
	@Override
	public IHideRule createHideRule() {
		return new HideRuleImpl();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IReportDesign#createHighLightRule
	 * ()
	 */
	@Override
	public IHighlightRule createHighLightRule() {
		return new HighlightRuleImpl();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IReportDesign#createSortCondition
	 * ()
	 */
	@Override
	public ISortCondition createSortCondition() {
		return new SortConditionImpl();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IReportDesign#createAction()
	 */
	@Override
	public IAction createAction() {
		return new ActionImpl();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IReportDesign#createDataBinding
	 * ()
	 */
	@Override
	public IDataBinding createDataBinding() {
		return new DataBindingImpl();
	}
}
