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
	public ICategoryProvider getCategoryProvider(Object model);

}