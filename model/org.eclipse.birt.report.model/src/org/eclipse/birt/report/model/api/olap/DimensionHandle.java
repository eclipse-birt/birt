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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExpressionHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IDimensionModel;
import org.eclipse.birt.report.model.elements.olap.Dimension;

/**
 * Represents a dimension element in the cube element.
 *
 * @see org.eclipse.birt.report.model.elements.olap.Dimension
 */

public abstract class DimensionHandle extends ReportElementHandle implements IDimensionModel {

	/**
	 * Constructs a handle for the given design and design element. The application
	 * generally does not create handles directly. Instead, it uses one of the
	 * navigation methods available on other element handles.
	 *
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public DimensionHandle(Module module, DesignElement element) {
		super(module, element);
	}

	/**
	 * Indicates whether this dimension is a special type of Time.
	 *
	 * @return true if this dimension is of Time type, otherwise false
	 */

	public boolean isTimeType() {
		return getBooleanProperty(IS_TIME_TYPE_PROP);
	}

	/**
	 * Sets the status to indicate whether this dimension is a special type of Time.
	 *
	 * @param isTimeType status whether this dimension is of Time type
	 * @throws SemanticException the property is locked
	 */

	public void setTimeType(boolean isTimeType) throws SemanticException {
		setBooleanProperty(IS_TIME_TYPE_PROP, isTimeType);
	}

	/**
	 * Gets the default hierarchy for the dimension.
	 *
	 * @return the default hierarchy for this dimension
	 */
	public HierarchyHandle getDefaultHierarchy() {
		DesignElement hierarchy = ((Dimension) getElement()).getDefaultHierarchy(module);
		return hierarchy == null ? null : (HierarchyHandle) hierarchy.getHandle(module);
	}

	/**
	 * Sets the default hierarchy for this dimension.
	 *
	 * @param defaultHierarchy the default hierarchy to set
	 * @throws SemanticException
	 */
	public void setDefaultHierarchy(HierarchyHandle defaultHierarchy) throws SemanticException {
		setProperty(DEFAULT_HIERARCHY_PROP, defaultHierarchy);
	}

	/**
	 * Gets the expression handle for the <code>ACLExpression</code> property.
	 *
	 * @return
	 */
	public ExpressionHandle getACLExpression() {
		return getExpressionProperty(ACL_EXPRESSION_PROP);
	}

	/**
	 *
	 * @return
	 */
	public List<CubeHandle> getCubeClients() {
		Iterator iter = clientsIterator();
		List<CubeHandle> cubes = new ArrayList<>();

		while (iter.hasNext()) {
			DesignElementHandle client = (DesignElementHandle) iter.next();
			if (client instanceof TabularDimensionHandle) {
				TabularDimensionHandle dimension = (TabularDimensionHandle) client;
				DesignElementHandle container = dimension.getContainer();

				// if already added, do nothing
				if (cubes.contains(container)) {
					continue;
				}

				if (container instanceof CubeHandle) {
					CubeHandle cube = (CubeHandle) container;
					cubes.add(cube);
				}
			}
		}

		return cubes;
	}
}
