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
import org.eclipse.birt.report.model.api.elements.structures.SelectionChoice;

/**
 * Represents the handle of selection choice. The selection choice is the value
 * and label pair for parameter.
 *
 * <dl>
 * <dt><strong>Value </strong></dt>
 * <dd>the data value for this choice. The value string is interpreted base on
 * the parameter data type.
 *
 * <dt><strong>Label </strong></dt>
 * <dd>a optional label to display for this value.
 *
 * <dt><strong>Label Resource Key </strong></dt>
 * <dd>a optional label resource key when localiztion is needed.
 * </dl>
 */

public class SelectionChoiceHandle extends StructureHandle {

	/**
	 * Constructs the handle of selection choice.
	 *
	 * @param valueHandle the value handle for selection choice list of one property
	 * @param index       the position of this selection choice in the list
	 */

	public SelectionChoiceHandle(SimpleValueHandle valueHandle, int index) {
		super(valueHandle, index);
	}

	/**
	 * Returns the label.
	 *
	 * @return the label
	 */

	public String getLabel() {
		return getStringProperty(SelectionChoice.LABEL_MEMBER);
	}

	/**
	 * Sets the label.
	 *
	 * @param label the label to set
	 */

	public void setLabel(String label) {
		setPropertySilently(SelectionChoice.LABEL_MEMBER, label);
	}

	/**
	 * Returns the resource key if label needs localization.
	 *
	 * @return the resource key of label.
	 */

	public String getLabelKey() {
		return getStringProperty(SelectionChoice.LABEL_RESOURCE_KEY_MEMBER);
	}

	/**
	 * Sets the resource key if label needs localization.
	 *
	 * @param labelResourceKey the resource key to set
	 */

	public void setLabelKey(String labelResourceKey) {
		setPropertySilently(SelectionChoice.LABEL_RESOURCE_KEY_MEMBER, labelResourceKey);
	}

	/**
	 * Returns the data value for this choice.
	 *
	 * @return the data value for this choice
	 */

	public String getValue() {
		return getStringProperty(SelectionChoice.VALUE_MEMBER);
	}

	/**
	 * Sets the data value for this choice.
	 *
	 * @param value the value to set
	 * @throws SemanticException value required exception
	 */

	public void setValue(String value) throws SemanticException {
		setProperty(SelectionChoice.VALUE_MEMBER, value);
	}
}
