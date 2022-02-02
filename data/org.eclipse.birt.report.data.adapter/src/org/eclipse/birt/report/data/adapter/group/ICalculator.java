/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

package org.eclipse.birt.report.data.adapter.group;

import org.eclipse.birt.core.exception.BirtException;

/**
 * Use ICalculator interface whenever you wish to calculator a value.
 */
public interface ICalculator {

	/**
	 * 
	 * @param value
	 * @return
	 */
	Object calculate(Object value) throws BirtException;
}
