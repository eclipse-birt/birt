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

package org.eclipse.birt.report.model.util;

import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.SessionHandle;

/**
 * The default file search algorithm. It searches for a given file in the 'base'
 * folder of a design. If the 'base' property of the design was not set, then
 * this class looks in folder where the design file is located.
 * <p>
 * The detail search mechanism is:
 * <ul>
 * <li>If the the file to be found is given by an absolute path, returns that
 * path.
 * <li>If it is a relative file path, search the 'base' folder of the design.
 * <li>If the 'base' property of the design is not set, then search the file in
 * the folder where the design file locates.
 * </ul>
 *
 * @see IResourceLocator
 * @see SessionHandle
 */

public class ResourceLocatorImpl extends ResourceLocatorImplBase {

}
