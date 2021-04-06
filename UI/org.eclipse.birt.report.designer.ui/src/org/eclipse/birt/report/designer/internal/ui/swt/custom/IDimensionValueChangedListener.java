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

package org.eclipse.birt.report.designer.internal.ui.swt.custom;

/**
 * After value in Spinner has changed,the Spinner control will inform
 * IValueChangedListener about this changing.
 */
public interface IDimensionValueChangedListener {

	/**
	 * Informs the value-changed event.
	 * 
	 * @param newValue new Spinner value.
	 */
	void valueChanged(String newValue, String unit);
}