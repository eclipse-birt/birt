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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.command.ComplexPropertyCommand;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.StructureContext;
import org.eclipse.birt.report.model.elements.DataSet;
import org.eclipse.birt.report.model.elements.JointDataSet;
import org.eclipse.birt.report.model.elements.interfaces.IJointDataSetModel;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;

/**
 * Provides API to operate a joint data set.
 * 
 * @see org.eclipse.birt.report.model.elements.JointDataSet
 */

public class JointDataSetHandle extends DataSetHandle implements IJointDataSetModel {

	/**
	 * Constructs a handle of the joint data set with the given design and a joint
	 * data set. The application generally does not create handles directly.
	 * Instead, it uses one of the navigation methods available on other element
	 * handles.
	 * 
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public JointDataSetHandle(Module module, JointDataSet element) {
		super(module, element);
	}

	/**
	 * Gets the names of the data sets in this joint data set.
	 * 
	 * @return a list of names of data sets in this joint data set.
	 */

	public List getDataSetNames() {
		return ((JointDataSet) getElement()).getDataSetNames(module);
	}

	/**
	 * Gets data sets in this joint data set. Each item in the list is instance of
	 * <code>DataSetHandle</code>.
	 * 
	 * @return a list of data sets in this joint data set.
	 */

	public Iterator dataSetsIterator() {
		List dataSetRefs = (List) getElement().getProperty(getModule(), DATA_SETS_PROP);
		if (dataSetRefs == null)
			return Collections.EMPTY_LIST.iterator();

		List rtnList = new ArrayList();

		for (int i = 0; i < dataSetRefs.size(); i++) {
			ElementRefValue dataSetRef = (ElementRefValue) dataSetRefs.get(i);
			if (dataSetRef != null) {
				DataSet ds = (DataSet) dataSetRef.getElement();
				if (ds == null)
					continue;
				rtnList.add(ds.getHandle(ds.getRoot()));
			}
		}

		return rtnList.iterator();
	}

	/**
	 * Adds a data set into this joint data set by name.
	 * 
	 * @param dataSetName the name of the data set to be added in.
	 * @throws SemanticException if the the value of the item is incorrect.
	 */

	public void addDataSet(String dataSetName) throws SemanticException {
		ComplexPropertyCommand command = new ComplexPropertyCommand(module, getElement());
		command.addItem(new StructureContext(getElement(), (ElementPropertyDefn) getPropertyDefn(DATA_SETS_PROP), null),
				dataSetName);
	}

	/**
	 * Removes a data set from this joint data set by name.
	 * 
	 * @param dataSetName the name of the data set to be removed.
	 * @throws SemanticException if the the value of the item is incorrect.
	 * 
	 */

	public void removeDataSet(String dataSetName) throws SemanticException {
		PropertyHandle propertyHandle = getPropertyHandle(DATA_SETS_PROP);
		propertyHandle.removeItem(dataSetName);
	}

	/**
	 * Returns the iterator of join conditions. The element in the iterator is the
	 * corresponding <code>JoinConditionHandle</code> that deal with a
	 * <code>JoinCondition</code>.
	 * 
	 * @return the iterator of join condition structure list
	 */

	public Iterator joinConditionsIterator() {
		PropertyHandle propHandle = getPropertyHandle(IJointDataSetModel.JOIN_CONDITONS_PROP);

		assert propHandle != null;

		return propHandle.iterator();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.DataSetHandle#paramBindingsIterator()
	 */
	public Iterator paramBindingsIterator() {
		return Collections.EMPTY_LIST.iterator();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.DesignElementHandle#setProperty(java
	 * .lang.String, java.lang.Object)
	 */

	public void setProperty(String propName, Object value) throws SemanticException {
		if (!(PARAM_BINDINGS_PROP.equalsIgnoreCase(propName) || CACHED_ROW_COUNT_PROP.equalsIgnoreCase(propName)
				|| AFTER_CLOSE_METHOD.equalsIgnoreCase(propName)) || DATA_SET_ROW_LIMIT.equalsIgnoreCase(propName)
				|| AFTER_OPEN_METHOD.equalsIgnoreCase(propName) || BEFORE_CLOSE_METHOD.equalsIgnoreCase(propName)
				|| BEFORE_OPEN_METHOD.equalsIgnoreCase(propName) || DATA_SOURCE_PROP.equalsIgnoreCase(propName)
				|| ON_FETCH_METHOD.equalsIgnoreCase(propName))
			super.setProperty(propName, value);
	}
}
