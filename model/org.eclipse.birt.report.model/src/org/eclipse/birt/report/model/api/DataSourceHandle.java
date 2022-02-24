/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IDataSourceModel;

/**
 * This abstract class represents a data source element: a connection to an
 * external data provider such as an SQL database.
 * <p>
 * The application can implement methods to execute code on the two primary data
 * source events: open and close.
 * 
 * @see org.eclipse.birt.report.model.elements.DataSource
 * 
 */

public abstract class DataSourceHandle extends ReportElementHandle implements IDataSourceModel {
	/**
	 * Constructs a handle of DataSource with the given design and element. The
	 * application generally does not create handles directly. Instead, it uses one
	 * of the navigation methods available on other element handles.
	 * 
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public DataSourceHandle(Module module, DesignElement element) {
		super(module, element);
	}

	/**
	 * Returns the code of the beforeOpen method. This is the script called just
	 * before opening the data source.
	 * 
	 * @return the code of the method
	 */

	public String getBeforeOpen() {
		return getStringProperty(IDataSourceModel.BEFORE_OPEN_METHOD);
	}

	/**
	 * Sets the code for the beforeOpen method. This is the script called just
	 * before opening the data source.
	 * 
	 * @param code the code for the method
	 * @throws SemanticException if the method is locked.
	 */

	public void setBeforeOpen(String code) throws SemanticException {
		setProperty(IDataSourceModel.BEFORE_OPEN_METHOD, code);
	}

	/**
	 * Returns the code of the beforeClose method. This is the script called just
	 * before closing the data source.
	 * 
	 * @return the code of the method
	 */

	public String getBeforeClose() {
		return getStringProperty(IDataSourceModel.BEFORE_CLOSE_METHOD);
	}

	/**
	 * Sets the code for the beforeClose method. This is the script called just
	 * before closing the data source.
	 * 
	 * @param code the code for the method
	 * @throws SemanticException if the method is locked.
	 */

	public void setBeforeClose(String code) throws SemanticException {
		setProperty(IDataSourceModel.BEFORE_CLOSE_METHOD, code);
	}

	/**
	 * Returns the code of the afterOpen method. This is the script called just
	 * after opening the data source.
	 * 
	 * @return the code of the method
	 */

	public String getAfterOpen() {
		return getStringProperty(IDataSourceModel.AFTER_OPEN_METHOD);
	}

	/**
	 * Sets the code for the afterOpen method. This is the script called just after
	 * opening the data source.
	 * 
	 * @param code the code for the method
	 * @throws SemanticException if the method is locked.
	 */

	public void setAfterOpen(String code) throws SemanticException {
		setProperty(IDataSourceModel.AFTER_OPEN_METHOD, code);
	}

	/**
	 * Returns the code of the afterClose method. This is the script called just
	 * after closing the data source.
	 * 
	 * @return the code of the method
	 */

	public String getAfterClose() {
		return getStringProperty(IDataSourceModel.AFTER_CLOSE_METHOD);
	}

	/**
	 * Sets the code for the afterClose method. This is the script called just after
	 * closing the data source.
	 * 
	 * @param code the code for the method
	 * @throws SemanticException if the method is locked.
	 */

	public void setAfterClose(String code) throws SemanticException {
		setProperty(IDataSourceModel.AFTER_CLOSE_METHOD, code);
	}

}
