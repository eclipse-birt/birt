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

package org.eclipse.birt.report.model.api.elements.structures;

import org.eclipse.birt.report.model.api.DimensionConditionHandle;
import org.eclipse.birt.report.model.api.SimpleValueHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.core.PropertyStructure;

/**
 * The DimensionCondition structure defines a list of join conditions between
 * cube and hierarchy.
 */

public class DimensionCondition extends PropertyStructure {

	/**
	 * Name of this structure. Matches the definition in the meta-data dictionary.
	 */

	public final static String DIMENSION_CONDITION_STRUCT = "DimensionCondition"; //$NON-NLS-1$

	/**
	 * The property name of the data set parameters definitions.
	 */

	public static final String JOIN_CONDITIONS_MEMBER = "joinConditions"; //$NON-NLS-1$

	/**
	 * Member name of the cached result set(output columns).
	 */

	public final static String HIERARCHY_MEMBER = "hierarchy"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.Structure#handle(org.eclipse.birt.report.
	 * model.api.SimpleValueHandle, int)
	 */
	protected StructureHandle handle(SimpleValueHandle valueHandle, int index) {
		return new DimensionConditionHandle(valueHandle, index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.core.IStructure#getStructName()
	 */
	public String getStructName() {
		return DIMENSION_CONDITION_STRUCT;
	}
}
