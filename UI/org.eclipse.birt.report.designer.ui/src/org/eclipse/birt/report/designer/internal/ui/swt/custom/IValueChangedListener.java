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

package org.eclipse.birt.report.designer.internal.ui.swt.custom;

/**
 * After value in Spinner has changed,the Spinner control will inform
 * IValueChangedListener about this changing.
 */
public interface IValueChangedListener {

	/**
	 * Informs the value-changed event.
	 *
	 * @param newValue new Spinner value.
	 */
	void valueChanged(double newValue);
}
