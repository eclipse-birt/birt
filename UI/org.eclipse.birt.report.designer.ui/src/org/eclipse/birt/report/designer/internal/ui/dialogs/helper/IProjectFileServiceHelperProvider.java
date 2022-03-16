/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.report.designer.internal.ui.dialogs.helper;

/**
 *
 * @author cthronson
 *
 */
public interface IProjectFileServiceHelperProvider {

	/**
	 * Creates a helper for selecting project files.
	 *
	 * @return A helper for selecting project files
	 */
	IProjectFileServiceHelper createHelper();

}
