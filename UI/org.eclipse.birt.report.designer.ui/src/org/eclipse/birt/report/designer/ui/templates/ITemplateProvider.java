/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

package org.eclipse.birt.report.designer.ui.templates;

/**
 * ITemplateProvider
 */
public interface ITemplateProvider {

	/**
	 * Gets the provider ID.
	 *
	 * @return
	 */
	String getParentBaseName();

	/**
	 * Gets the entries.
	 *
	 * @return
	 */
	ITemplateEntry[] getTemplates();

	/**
	 * Release the resources allocated if applicable.
	 */
	void release();

}
