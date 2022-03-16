/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.internal.document;

public interface IPageHintConstant {

	/**
	 * version used before 2.0
	 */
	int VERSION_0 = 0;
	/**
	 * this is the version used before 2.1.3 and 2.2RC0
	 */
	int VERSION_1 = 1;

	/**
	 * this is the version used by 2.1.3 and 2.2RC0 release.
	 */
	int VERSION_2 = 2;

	/**
	 * this is the version used by 2.2RC2 release.
	 */
	int VERSION_3 = 3;
	/**
	 * this is the version used by 2.3M4 release.
	 */
	int VERSION_4 = 4;

	/**
	 * this is the version used by 2.3M6 release.
	 */
	int VERSION_5 = 5;

	/**
	 * this is the version used by fixed layout.
	 */
	int VERSION_FIXED_LAYOUT = 6;

	/**
	 * used after 2.5.0RC1 release, add page variable support
	 */
	int VERSION_6 = 6;
	/**
	 * the current release is version 5.
	 */
	int VERSION = VERSION_6;
}
