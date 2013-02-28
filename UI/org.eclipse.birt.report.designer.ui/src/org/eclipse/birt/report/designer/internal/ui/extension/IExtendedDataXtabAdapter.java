/*******************************************************************************
 * Copyright (c) 2004, 2012 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.extension;

import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;

/**
 * The extended data adapter for extended report item
 */
public interface IExtendedDataXtabAdapter
{
	/**
	 * Checks whether the data item is a child of the extended data.
	 * @param parent the extended data
	 * @param child the extended data item
	 * @return true if contains, false if not
	 */
	public boolean contains(ReportElementHandle parent, ReportElementHandle child);
	
	/**
	 * Gets the extended data from the report item.
	 * @param element the report item
	 * @return the extended data
	 */
	public ReportElementHandle getExtendedData(ReportItemHandle element);
	
	/**
	 * Gets the name of the extended data from the report item.
	 * @param element the report item
	 * @return the name of the extended data
	 */
	public String getExtendedDataName(ReportItemHandle element);
	
	/**
	 * Gets instances of supported data types of the extended data. 
	 * @param element the data item
	 * @param parent the instance of the given parent type 
	 * @return instances of the supported data types
	 */
	public ReportElementHandle[] getSupportedTypes(ReportElementHandle element, ReportElementHandle parent);
	
	/**
	 * Checks whether this element needs a data source or not.
	 * @param element the element to be checked
	 * @return true if needs, or false if not
	 */
	public boolean needsDataSource(ReportElementHandle element);
	
	/**
	 * Checks whether this is an extended data item.
	 * @param element the element to be checked
	 * @return true if it is, or false if not
	 */
	public boolean isExtendedDataItem(ReportElementHandle element);
	
	/**
	 * Resolves and returns the extended data by the given element if possible.
	 * @param element the element to be resolved
	 * @return the extended data
	 */
	public ReportElementHandle resolveExtendedData(ReportElementHandle element);
	
	/**
	 * Sets the extended data to the report item.
	 * @param target the report item
	 * @param element the extended data
	 * @return true if succeeded, of false if not
	 */
	public boolean setExtendedData(ReportItemHandle target, ReportElementHandle element);

}
