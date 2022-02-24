/*******************************************************************************
 * Copyright (c) 2012 Actuate Corporation.
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

package org.eclipse.birt.report.designer.core.mediator;

import java.util.Map;

/**
 * IMediatorState
 */
public interface IMediatorState {

	/**
	 * Gets the type.
	 */
	String getType();

	/**
	 * Gets the data object.
	 */
	Object getData();

	/**
	 * Gets the source object.
	 */
	Object getSource();

	/**
	 * Returns the extra data.
	 */
	Map<?, ?> getExtras();
}
