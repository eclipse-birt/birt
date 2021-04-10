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

package org.eclipse.birt.report.model.api;

/**
 * Implements this interface to provide a custom file search algorithm. This
 * class defines the file search algorithm used to locate files referenced in
 * the design. For example, when parsing a design file and a library is
 * encountered, a file locator will be used to locate the library. This
 * interface is implemented differently for each environment. For example, the
 * GUI might have its own file search requirement, while the deployment
 * environment in application server has another.
 * <p>
 * The default file locator is <code>{@link DefaultResourceLocator}</code>.
 * <p>
 * The customized file search must be installed before opening designs. Set the
 * file locator using
 * {@link org.eclipse.birt.report.model.api.SessionHandle#setResourceLocator(IResourceLocator)}
 * method.
 */

public interface IResourceLocator extends IResourceLocatorBase {

	/**
	 * The location mask which searches all the locations.
	 */
	public int ALL_RESOURCE = RESOURCE_FILEPATH | RESOURCE_BUNDLE | RESOURCE_FOLDER | RESOURCE_DESIGN;
}