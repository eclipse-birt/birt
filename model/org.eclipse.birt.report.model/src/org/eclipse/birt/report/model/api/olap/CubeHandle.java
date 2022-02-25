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

package org.eclipse.birt.report.model.api.olap;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.ExpressionHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.BackRef;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.ICubeModel;
import org.eclipse.birt.report.model.elements.olap.Cube;
import org.eclipse.birt.report.model.elements.olap.Dimension;
import org.eclipse.birt.report.model.elements.olap.Measure;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;

/**
 * Represents a cube.
 *
 * @see org.eclipse.birt.report.model.elements.olap.Cube
 */

public abstract class CubeHandle extends ReportElementHandle implements ICubeModel {

	/**
	 * Constructs a handle for the given design and design element. The application
	 * generally does not create handles directly. Instead, it uses one of the
	 * navigation methods available on other element handles.
	 *
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public CubeHandle(Module module, DesignElement element) {
		super(module, element);
	}

	public DimensionHandle getDimension(String dimensionName, boolean needLevelForTimeDimension) {
		return getDimension(dimensionName);
	}

	/**
	 * Gets the dimension with the specified name within this cube.
	 *
	 * @param dimensionName name of the dimension to find
	 * @return dimension within the cube if found, otherwise <code>null</code>
	 */
	public DimensionHandle getDimension(String dimensionName) {
		if (StringUtil.isBlank(dimensionName)) {
			return null;
		}
		if (!getElement().canDynamicExtends()) {
			Dimension dimension = module.findDimension(dimensionName);
			if (dimension == null) {
				return null;
			}
			if (dimension.isContentOf(getElement())) {
				return (DimensionHandle) dimension.getHandle(module);
			} else {
				// check the client to find the children of the cube
				List<BackRef> clients = dimension.getClientList();
				if (clients != null) {
					for (BackRef ref : clients) {
						DesignElement client = ref.getElement();
						if (client.isContentOf(getElement())) {
							return (DimensionHandle) client.getHandle(module);
						}
					}
				}
			}
		} else if (getElement().getDynamicExtendsElement(getModule()) != null) {
			Cube cube = (Cube) getElement();
			DesignElement element = cube.findLocalElement(dimensionName,
					MetaDataDictionary.getInstance().getElement(ReportDesignConstants.DIMENSION_ELEMENT));
			return (DimensionHandle) (element == null ? null : element.getHandle(module));
		}

		return null;
	}

	/**
	 * Gets the dimension with the specified name within this cube. If dimension
	 * defined with the given name doesn't exist, it returns the local corresponding
	 * one mapped to the parent dimension that matches the given name.
	 *
	 * @param dimensionName name of the dimension to find
	 * @return dimension within the cube if found, otherwise <code>null</code>
	 */

	public DimensionHandle getLocalDimension(String dimensionName) {
		DesignElement dimension = module.findDimension(dimensionName);
		if (dimension != null && dimension.isContentOf(getElement())) {
			return (DimensionHandle) dimension.getHandle(module);
		}

		// find the dimension according to the name in the parent cube.

		CubeHandle parent = (CubeHandle) getExtends();
		if (parent == null) {
			return null;
		}

		dimension = doGetLocalDimension(dimensionName, (Cube) parent.element, parent.module);

		if (dimension == null) {
			return null;
		}

		return (DimensionHandle) dimension.getHandle(module);
	}

	/**
	 * Returns the dimension defined on the given cube.
	 *
	 * @param dimensionName
	 * @param parent
	 * @param parentModule
	 * @return
	 */

	protected DesignElement doGetLocalDimension(String dimensionName, Cube parent, Module parentModule) {
		DesignElement dimension = parentModule.findDimension(dimensionName);
		if (dimension == null) {
			return null;
		}

		int index = dimension.getIndex(parentModule);
		assert index != -1;

		List<DesignElement> dims = (List<DesignElement>) getElement().getProperty(module, DIMENSIONS_PROP);

		return dims.get(index);
	}

	/**
	 * Gets the measure with the specified name within this cube.
	 *
	 * @param measureName name of the measure to find
	 * @return measure within the cube if found, otherwise <code>null</code>
	 */
	public MeasureHandle getMeasure(String measureName) {
		if (StringUtil.isBlank(measureName)) {
			return null;
		}
		if (!getElement().canDynamicExtends()) {
			DesignElement measure = module.findOLAPElement(measureName);
			if (measure instanceof Measure && measure.isContentOf(getElement())) {
				return (MeasureHandle) measure.getHandle(module);
			}
		} else if (getElement().getDynamicExtendsElement(getModule()) != null) {
			Cube cube = (Cube) getElement();
			DesignElement element = cube.findLocalElement(measureName,
					MetaDataDictionary.getInstance().getElement(ReportDesignConstants.MEASURE_ELEMENT));
			return (MeasureHandle) (element == null ? null : element.getHandle(module));
		}

		return null;
	}

	/**
	 * Returns an iterator for the filter list defined on this cube. Each object
	 * returned is of type <code>StructureHandle</code>.
	 *
	 * @return the iterator for <code>FilterCond</code> structure list defined on
	 *         this cube.
	 */

	public Iterator filtersIterator() {
		PropertyHandle propHandle = getPropertyHandle(FILTER_PROP);
		assert propHandle != null;
		return propHandle.iterator();
	}

	/**
	 * Gets the default measure group for the cube.
	 *
	 * @return the default measure group
	 *
	 * @deprecated
	 */
	@Deprecated
	public MeasureGroupHandle getDefaultMeasureGroup() {
		return null;
	}

	/**
	 * Sets the default measure group for this cube.
	 *
	 * @param defaultMeasureGroup the default measure group to set
	 * @throws SemanticException
	 * @deprecated
	 */
	@Deprecated
	public void setDefaultMeasureGroup(MeasureGroupHandle defaultMeasureGroup) throws SemanticException {
	}

	/**
	 * Returns an iterator for the access controls. Each object returned is of type
	 * <code>AccessControlHandle</code>.
	 *
	 * @return the iterator for user accesses defined on this cube.
	 */

	public Iterator accessControlsIterator() {
		return Collections.emptyList().iterator();
	}

	/**
	 * Adds the filter condition.
	 *
	 * @param fc the filter condition structure
	 * @throws SemanticException if the expression of filter condition is empty or
	 *                           null
	 */

	public void addFilter(FilterCondition fc) throws SemanticException {
		PropertyHandle propHandle = getPropertyHandle(FILTER_PROP);
		propHandle.addItem(fc);
	}

	/**
	 * Removes the filter condition.
	 *
	 * @param fc the filter condition structure
	 * @throws SemanticException if the given condition doesn't exist in the filters
	 */

	public void removeFilter(FilterCondition fc) throws SemanticException {
		PropertyHandle propHandle = getPropertyHandle(FILTER_PROP);
		propHandle.removeItem(fc);
	}

	/**
	 * Gets the expression handle for the <code>ACLExpression</code> property.
	 *
	 * @return
	 */
	public ExpressionHandle getACLExpression() {
		return getExpressionProperty(ACL_EXPRESSION_PROP);
	}
}
