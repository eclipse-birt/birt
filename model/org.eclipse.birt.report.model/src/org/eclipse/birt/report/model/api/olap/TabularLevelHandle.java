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

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.ITabularLevelModel;

/**
 * Represents a level element.
 *
 * @see org.eclipse.birt.report.model.elements.olap.Level
 */

public class TabularLevelHandle extends LevelHandle implements ITabularLevelModel {

	/**
	 * Constructs a handle for the given design and design element. The application
	 * generally does not create handles directly. Instead, it uses one of the
	 * navigation methods available on other element handles.
	 *
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public TabularLevelHandle(Module module, DesignElement element) {
		super(module, element);
	}

	/**
	 * Gets the column name of this level.
	 *
	 * @return column name of this level
	 */
	public String getColumnName() {
		return getStringProperty(COLUMN_NAME_PROP);
	}

	/**
	 * Sets the column name for this level.
	 *
	 * @param columnName the column name to set
	 * @throws SemanticException property is locked
	 */

	public void setColumnName(String columnName) throws SemanticException {
		setStringProperty(COLUMN_NAME_PROP, columnName);
	}

	/**
	 * Gets the display column name of this level.
	 *
	 * @return display column name of this level
	 */
	public String getDisplayColumnName() {
		return getStringProperty(DISPLAY_COLUMN_NAME_PROP);
	}

	/**
	 * Sets the display column name for this level.
	 *
	 * @param columnName the display column name to set
	 * @throws SemanticException property is locked
	 */

	public void setDisplayColumnName(String columnName) throws SemanticException {
		setStringProperty(DISPLAY_COLUMN_NAME_PROP, columnName);
	}
}
