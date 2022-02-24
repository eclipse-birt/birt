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

package org.eclipse.birt.report.engine.layout;

/**
 * this event is trigged by the IPagiantionBuilder.
 * 
 */
public interface ILayoutPageHandler {

	/**
	 * a page is created in the layout engine.
	 * 
	 * @param page    the created page number
	 * @param context the create page content.
	 */
	void onPage(long page, Object context);
}
