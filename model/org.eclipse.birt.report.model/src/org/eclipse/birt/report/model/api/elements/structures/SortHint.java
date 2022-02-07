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

import java.util.List;

import org.eclipse.birt.report.model.api.SimpleValueHandle;
import org.eclipse.birt.report.model.api.SortHintHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.metadata.IStructureDefn;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.PropertyStructure;
import org.eclipse.birt.report.model.metadata.PropertyDefn;

/**
 * This class represents a sort hint.
 * 
 */
public class SortHint extends PropertyStructure {

	/**
	 * Name of this structure. Matches the definition in the meta-data dictionary.
	 */

	public static final String SORT_HINT_STRUCT = "SortHint"; //$NON-NLS-1$

	/**
	 * Name of the column name member. This member keys the column hint to a column
	 * within the result set.
	 */

	public static final String COLUMN_NAME_MEMBER = "columnName"; //$NON-NLS-1$

	/**
	 * Name of the column position member. It is 1-based index position
	 * (left-to-right order) of a result set column.
	 */
	public static final String POSITION_MEMBER = "position"; //$NON-NLS-1$

	/**
	 * Name of the direction member. It is sort direction of the result set column.
	 */
	public static final String DIRECTION_MEMBER = "direction"; //$NON-NLS-1$

	/**
	 * Name of the nullValueOrdering member. The ordering of null vs. non-null
	 * values in the sort order.
	 */
	public static final String NULL_VALUE_ORDERING_MEMBER = "nullValueOrdering"; //$NON-NLS-1$

	/**
	 * Name of the isOptional member. It indicates whether this sort key can be
	 * excluded at runtime.
	 */
	public static final String IS_OPTIONAL_MEMBER = "isOptional"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#handle(org.eclipse.birt.
	 * report.model.api.SimpleValueHandle, int)
	 */
	protected StructureHandle handle(SimpleValueHandle valueHandle, int index) {
		return new SortHintHandle(valueHandle, index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.core.IStructure#getStructName()
	 */
	public String getStructName() {
		return SORT_HINT_STRUCT;
	}

	/**
	 * Validates this structure. The following are the rules:
	 * <ul>
	 * <li>If a column can only be identified by position, this name may be empty.
	 * </ul>
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#validate(Module,
	 *      org.eclipse.birt.report.model.core.DesignElement)
	 */

	public List<SemanticException> validate(Module module, DesignElement element) {
		List<SemanticException> list = super.validate(module, element);

		IStructureDefn structureDefn = getDefn();
		PropertyDefn propDefn = (PropertyDefn) structureDefn.getMember(COLUMN_NAME_MEMBER);
		String columnName = (String) getProperty(module, propDefn);
		if (StringUtil.isBlank(columnName)) {
			propDefn = (PropertyDefn) structureDefn.getMember(POSITION_MEMBER);

			// if the column name is empty, the column can be identified by
			// position,otherwise exception will be recorded.

			Object pos = getProperty(module, propDefn);

			if (pos == null) {
				list.add(new PropertyValueException(element, propDefn, columnName,
						PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED));
			}
		}

		return list;
	}

}
