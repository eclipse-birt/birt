/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
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

package org.eclipse.birt.chart.ui.swt.interfaces;

import org.eclipse.birt.chart.model.data.DataElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Listener;

/**
 * Composite for inputing a DataElement
 */

public interface IDataElementComposite {

	/**
	 * Event type of modifying DateElement
	 */
	int DATA_MODIFIED = 0;

	int FRACTION_CONVERTED = 1;

	/**
	 * Gets data
	 *
	 * @return data
	 */
	DataElement getDataElement();

	/**
	 * Sets data
	 *
	 * @param data data
	 */
	void setDataElement(DataElement data);

	void setEnabled(boolean enabled);

	void setLayoutData(Object layoutData);

	void addListener(Listener listener);

	void setEObjectParent(EObject eParent);
}
