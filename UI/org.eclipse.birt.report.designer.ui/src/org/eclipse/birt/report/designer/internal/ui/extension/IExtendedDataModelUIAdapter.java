/*******************************************************************************
 * Copyright (c) 2004, 2012 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.extension;

import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.designer.ui.preferences.IStatusChangeListener;
import org.eclipse.birt.report.designer.ui.preferences.OptionsConfigurationBlock;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ElementDetailHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.core.resources.IProject;

/**
 * The extended data adapter for extended report item
 */
public interface IExtendedDataModelUIAdapter {
	/**
	 * Checks whether the data item is a child of the extended data.
	 * 
	 * @param parent the extended data
	 * @param child  the extended data item
	 * @return true if contains, false if not
	 */
	public boolean contains(ReportElementHandle parent, ReportElementHandle child);

	/**
	 * Gets the bound extended data from the report item.
	 * 
	 * @param element the report item
	 * @return the bound extended data, or null if not found
	 */
	public ReportElementHandle getBoundExtendedData(ReportItemHandle element);

	/**
	 * Gets the name of the extended data from the report item.
	 * 
	 * @param element the report item
	 * @return the name of the extended data
	 */
	public String getExtendedDataName(ReportItemHandle element);

	/**
	 * Gets instances of supported data types of the extended data.
	 * 
	 * @param element the data item
	 * @param parent  the instance of the given parent type
	 * @return instances of the supported data types
	 */
	public ReportElementHandle[] getSupportedTypes(ReportElementHandle element, ReportElementHandle parent);

	/**
	 * Checks whether this element needs a data source or not.
	 * 
	 * @param element the element to be checked
	 * @return true if needs, or false if not
	 */
	public boolean needsDataSource(ReportElementHandle element);

	/**
	 * Checks whether this is an extended data item.
	 * 
	 * @param element the element to be checked
	 * @return true if it is, or false if not
	 */
	public boolean isExtendedDataItem(ReportElementHandle element);

	/**
	 * Resolves and returns the extended data by the given element if possible.
	 * 
	 * @param element the element to be resolved
	 * @return the extended data
	 */
	public ReportElementHandle resolveExtendedData(DesignElementHandle element);

	/**
	 * Sets the extended data to the report item.
	 * 
	 * @param target  the report item
	 * @param element the extended data
	 * @return true if succeeded, of false if not
	 */
	public boolean setExtendedData(ReportItemHandle target, ReportElementHandle element);

	/**
	 * Finds the extended data by the given name.
	 * 
	 * @param name the name
	 * @return the extended data if found, or null if not
	 */
	public ReportElementHandle findExtendedDataByName(String name);

	/**
	 * Gets the data set element of the column.
	 * 
	 * @param column the result set column
	 * @return the data set handle resolved by the result set column, or null if
	 *         cannot
	 */
	public DataSetHandle getDataSet(ResultSetColumnHandle column);

	/**
	 * Gets the data set instance of the extended data.
	 * 
	 * @param element the extended data
	 * @return the data set handle returned by the extended data, or null if it
	 *         cannot be a DataSetHandle instance
	 */
	public DataSetHandle getDataSet(ReportElementHandle element);

	/**
	 * Gets the result set column instance of the extended data item.
	 * 
	 * @param element the extended data item
	 * @return the result set column handle returned by the extended data model
	 *         item, or null if it cannot be a ResultSetColumnHandle instance
	 */
	public ResultSetColumnHandle getResultSetColumn(ReportElementHandle element);

	/**
	 * Creates an expression by the given element.
	 * 
	 * @param element the report element
	 * @return the expression
	 */
	public String createExtendedDataItemExpression(DesignElementHandle element);

	/**
	 * Creates the binding name b the given element.
	 * 
	 * @param element the report element
	 * @return the binding name
	 */
	public String createBindingName(DesignElementHandle element);

	/**
	 * Gets the detail handle about this extended data model
	 * 
	 * @param module the module handle, might be a report design handle or a library
	 *               handle
	 * @return the detail handle, might be a slot handle, a property handle or a
	 *         structure handle
	 */
	public ElementDetailHandle getDetailHandle(ModuleHandle module);

	/**
	 * Gets the available binding reference list of the extended data model.
	 * 
	 * @param handle the report item handle
	 * @return the available binding reference list
	 */
	public List getAvailableBindingReferenceList(ReportItemHandle handle);

	/**
	 * Gets the binding expression provider for the extended data model
	 * 
	 * @param handle               the binding holder
	 * @param computedColumnHandle the binding item
	 * @return the expression provider
	 */
	public ExpressionProvider getBindingExpressionProvider(DesignElementHandle handle,
			ComputedColumnHandle computedColumnHandle);

	/**
	 * Gets the level hierarchy hints
	 * 
	 * @param dimension the dimension
	 * @return the levels
	 */
	public List<LevelHandle> getLevelHints(DimensionHandle dimension);

	/**
	 * Gets the display block for data model in the preference page.
	 * 
	 * @param listener The StatusChangeListener
	 * @param project  The project
	 * @return The ConfigurationBlock
	 */
	public OptionsConfigurationBlock getDataModelConfigurationBlock(IStatusChangeListener listener, IProject project);

	/**
	 * Gets application context settings for the engine tasks.
	 */
	public Map getAppContext();
}
