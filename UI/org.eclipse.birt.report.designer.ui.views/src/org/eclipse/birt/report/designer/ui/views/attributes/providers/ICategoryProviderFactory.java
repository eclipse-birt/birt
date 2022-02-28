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

package org.eclipse.birt.report.designer.ui.views.attributes.providers;

/**
 * The interface for a category provider factory
 */

public interface ICategoryProviderFactory {

	/**
	 * Returns the category provider for the given model
	 *
	 * @param model the given model
	 * @return the category provider for the model, or null if it doesn't exist
	 */
	ICategoryProvider getCategoryProvider(Object model);

}
