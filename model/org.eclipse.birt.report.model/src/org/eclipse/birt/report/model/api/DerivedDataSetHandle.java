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

package org.eclipse.birt.report.model.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.DerivedDataSet;
import org.eclipse.birt.report.model.elements.interfaces.IDerivedDataSetModel;
import org.eclipse.birt.report.model.elements.interfaces.IDerivedExtendableElementModel;
import org.eclipse.birt.report.model.metadata.ElementRefValue;

/**
 * Provides API to operate a derived data set.
 */

public class DerivedDataSetHandle extends DataSetHandle
		implements IDerivedDataSetModel, IDerivedExtendableElementModel {

	/**
	 * Constructs a handle of the joint data set with the given design and a joint
	 * data set. The application generally does not create handles directly.
	 * Instead, it uses one of the navigation methods available on other element
	 * handles.
	 * 
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public DerivedDataSetHandle(Module module, DerivedDataSet element) {
		super(module, element);
	}

	/**
	 * Sets the query text.
	 * 
	 * @param text the text to set
	 * @throws SemanticException if this property is locked.
	 */

	public void setQueryText(String text) throws SemanticException {
		setStringProperty(QUERY_TEXT_PROP, text);
	}

	/**
	 * Returns the query text.
	 * 
	 * @return the query text.
	 */

	public String getQueryText() {
		return getStringProperty(QUERY_TEXT_PROP);
	}

	/**
	 * Inserts a data set with the specified name into this derived data set.
	 * 
	 * @param dataSetName name of the data set
	 * @throws SemanticException
	 */
	public void addInputDataSets(String dataSetName) throws SemanticException {
		PropertyHandle propHandle = getPropertyHandle(INPUT_DATA_SETS_PROP);
		if (propHandle != null)
			propHandle.addItem(dataSetName);
	}

	/**
	 * Removes a data set with the specified name from this derived data set.
	 * 
	 * @param dataSetName name of the data set to remove
	 * @throws SemanticException
	 */
	public void removeInputDataSet(String dataSetName) throws SemanticException {
		PropertyHandle propHandle = getPropertyHandle(INPUT_DATA_SETS_PROP);
		if (propHandle != null)
			propHandle.removeItem(dataSetName);
	}

	/**
	 * Gets the list of all the input data set handle that this derive data set
	 * includes. If no data set is included or no data set is found for input data
	 * set names, an empty list will be returned.
	 * 
	 * @return list of the input data set handles
	 */
	public List<DataSetHandle> getInputDataSets() {
		List dataSetRefs = getElement().getListProperty(getModule(), INPUT_DATA_SETS_PROP);
		if (dataSetRefs == null || dataSetRefs.isEmpty())
			return Collections.emptyList();

		List<DataSetHandle> dataSets = new ArrayList<DataSetHandle>();
		for (int i = 0; i < dataSetRefs.size(); i++) {
			ElementRefValue refValue = (ElementRefValue) dataSetRefs.get(i);
			if (refValue != null && refValue.getElement() != null) {
				DesignElement dataSet = refValue.getElement();
				dataSets.add((DataSetHandle) dataSet.getHandle(dataSet.getRoot()));
			}
		}

		return dataSets;
	}

	/**
	 * Returns ID of the extension which extends this ODA data set.
	 * 
	 * @return the extension ID
	 */

	public String getExtensionID() {
		return getStringProperty(EXTENSION_ID_PROP);
	}
}
