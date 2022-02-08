/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.designer.ui.editors;

import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.IEditorInput;

/**
 * This interface defines a factory for editor input based on the local file
 * system path.
 */
public interface IPathEditorInputFactory {

	/**
	 * Creates and returns an instance of <code>IEditorInput</code> with the
	 * specified local file system path.
	 * 
	 * @param path the local file system path
	 * @return an editor input based on the local file system path.
	 */
	public IEditorInput create(IPath path);
}
