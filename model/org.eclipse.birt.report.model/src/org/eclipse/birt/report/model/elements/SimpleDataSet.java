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

package org.eclipse.birt.report.model.elements;

import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.validators.ElementReferenceValidator;
import org.eclipse.birt.report.model.api.validators.ValueRequiredValidator;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.ISimpleDataSetModel;

/**
 * This class represents a data set: a query, stored procedure, or other source
 * of data. A data set is a named object that provides a result set defined by a
 * sequence of data rows. Report elements use data sets to retrieve data for
 * <ul>
 * <li>Data access: Instructions for retrieving data from an external data
 * source. For example, and SQL query, a stored procedure definition, and so on.
 * <li>Report-specific properties: Properties for how the data is to be used in
 * the report such as rules for searching, data export and so on.
 * <li>Data transforms: Rules for processing the data for use by the report.
 * Data transforms are most frequently defined by report items that use the data
 * set, and are applied to the result set by BIRT.
 * </ul>
 *
 *
 */

public abstract class SimpleDataSet extends DataSet implements ISimpleDataSetModel {

	/**
	 * Default constructor.
	 */

	public SimpleDataSet() {
	}

	/**
	 * Constructs the data set with a required name.
	 *
	 * @param theName the required name
	 */

	public SimpleDataSet(String theName) {
		super(theName);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.DesignElement#validate(org.eclipse
	 * .birt.report.model.elements.ReportDesign)
	 */

	@Override
	public List<SemanticException> validate(Module module) {
		List<SemanticException> list = super.validate(module);

		// Check the data source value is required

		list.addAll(ValueRequiredValidator.getInstance().validate(module, this, DATA_SOURCE_PROP));

		// Check the element reference of dataSource property

		list.addAll(ElementReferenceValidator.getInstance().validate(module, this, DATA_SOURCE_PROP));

		// Check input parameter structure list

		list.addAll(validateStructureList(module, PARAMETERS_PROP));
		list.addAll(validateStructureList(module, PARAM_BINDINGS_PROP));
		list.addAll(validateStructureList(module, COMPUTED_COLUMNS_PROP));
		list.addAll(validateStructureList(module, COLUMN_HINTS_PROP));
		list.addAll(validateStructureList(module, FILTER_PROP));

		return list;
	}

}
