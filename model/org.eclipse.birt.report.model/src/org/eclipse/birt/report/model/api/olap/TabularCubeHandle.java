/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.api.olap;

import java.util.Iterator;

import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DimensionConditionHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.elements.structures.DimensionCondition;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.ITabularCubeModel;

/**
 * Represents a cube.
 * 
 * @see org.eclipse.birt.report.model.elements.olap.Cube
 */

public class TabularCubeHandle extends CubeHandle implements ITabularCubeModel {

	/**
	 * Constructs a handle for the given design and design element. The application
	 * generally does not create handles directly. Instead, it uses one of the
	 * navigation methods available on other element handles.
	 * 
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public TabularCubeHandle(Module module, DesignElement element) {
		super(module, element);
	}

	/**
	 * Returns the data set of this cube.
	 * 
	 * @return the handle to the data set
	 */

	public DataSetHandle getDataSet() {
		return (DataSetHandle) getElementProperty(DATA_SET_PROP);
	}

	/**
	 * Sets the data set of this cube.
	 * 
	 * @param handle the handle of the data set
	 * 
	 * @throws SemanticException if the property is locked, or the data-set is
	 *                           invalid.
	 */

	public void setDataSet(DataSetHandle handle) throws SemanticException {
		if (handle == null)
			setStringProperty(DATA_SET_PROP, null);
		else {
			ModuleHandle moduleHandle = handle.getRoot();
			String valueToSet = handle.getElement().getFullName();
			if (moduleHandle instanceof LibraryHandle) {
				String namespace = ((LibraryHandle) moduleHandle).getNamespace();
				valueToSet = StringUtil.buildQualifiedReference(namespace, valueToSet);
			}
			setStringProperty(DATA_SET_PROP, valueToSet);
		}
	}

	/**
	 * Adds a dimension condition to this cube.
	 * 
	 * @param condition
	 * @return the added dimension condition handle if succeed
	 * @throws SemanticException
	 */

	public DimensionConditionHandle addDimensionCondition(DimensionCondition condition) throws SemanticException {
		PropertyHandle propertyHandle = getPropertyHandle(DIMENSION_CONDITIONS_PROP);
		return (DimensionConditionHandle) propertyHandle.addItem(condition);
	}

	/**
	 * Adds a dimension condition to the specified position.
	 * 
	 * @param condition
	 * @param posn
	 * @return the added dimension condition handle if succeed
	 * @throws SemanticException
	 */
	public DimensionConditionHandle addDimensionCondition(DimensionCondition condition, int posn)
			throws SemanticException {
		PropertyHandle propertyHandle = getPropertyHandle(DIMENSION_CONDITIONS_PROP);
		return (DimensionConditionHandle) propertyHandle.insertItem(condition, posn);
	}

	/**
	 * Gets the iterator of the join conditions. Each one in the iterator is
	 * instance of <code>StructureHandle</code>.
	 * 
	 * @return iterator of the join conditions in this cube
	 */

	public Iterator joinConditionsIterator() {
		PropertyHandle propertyHandle = getPropertyHandle(DIMENSION_CONDITIONS_PROP);
		return propertyHandle.iterator();
	}

	/**
	 * Removes a dimension condition from this cube.
	 * 
	 * @param condition
	 * @throws SemanticException
	 */
	public void removeDimensionCondition(DimensionCondition condition) throws SemanticException {
		PropertyHandle propertyHandle = getPropertyHandle(DIMENSION_CONDITIONS_PROP);
		propertyHandle.removeItem(condition);
	}

	/**
	 * 
	 * @param conditionHandle
	 * @throws SemanticException
	 */
	public void removeDimensionCondition(DimensionConditionHandle conditionHandle) throws SemanticException {
		PropertyHandle propertyHandle = getPropertyHandle(DIMENSION_CONDITIONS_PROP);
		IStructure struct = conditionHandle == null ? null : conditionHandle.getStructure();
		propertyHandle.removeItem(struct);
	}

	/**
	 * Finds the dimension condition defined for the hierarchy element with the
	 * specified name.
	 * 
	 * @param hierarchyName
	 * @return the first dimeneison condition handle if found, otherwise null
	 */
	public DimensionConditionHandle findDimensionCondition(String hierarchyName) {
		Iterator iter = getPropertyHandle(DIMENSION_CONDITIONS_PROP).iterator();
		while (iter.hasNext()) {
			DimensionConditionHandle condition = (DimensionConditionHandle) iter.next();
			String tempHierarchy = condition.getHierarchyName();
			if ((tempHierarchy == null && hierarchyName == null)
					|| (tempHierarchy != null && tempHierarchy.equals(hierarchyName)))
				return condition;
		}
		return null;
	}

	/**
	 * Finds the dimension condition defined for the given hierarchy element.
	 * 
	 * @param hierarchy
	 * @return the first dimeneison condition handle if found, otherwise null
	 */
	public DimensionConditionHandle findDimensionCondition(HierarchyHandle hierarchy) {
		String hierarchyName = hierarchy == null ? null : hierarchy.getQualifiedName();
		return findDimensionCondition(hierarchyName);
	}

	/**
	 * Gets the status whether to generate a primary key for elements that use this
	 * cube so that user no longer need to set the aggregation for measure.
	 * 
	 * @return true if automatically generate the key, otherwise false
	 */
	public boolean autoPrimaryKey() {
		return getBooleanProperty(AUTO_KEY_PROP);
	}

	/**
	 * Sets the status whether to generate a primary key for elements that use this
	 * cube so that user no longer need to set the aggregation for measure.
	 * 
	 * @param autoKey true if automatically generate the key, otherwise false
	 * @throws SemanticException
	 */
	public void setAutoPrimaryKey(boolean autoKey) throws SemanticException {
		setBooleanProperty(AUTO_KEY_PROP, autoKey);
	}
}
