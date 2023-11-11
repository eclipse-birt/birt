/***********************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/
package org.eclipse.birt.report.engine.nLayout.area;

import org.eclipse.birt.core.exception.BirtException;

/**
 * Interface of the layout
 *
 * @since 3.3
 *
 */
public interface ILayout {

	/**
	 * The layout
	 *
	 * @throws BirtException
	 */
	void layout() throws BirtException;
}
