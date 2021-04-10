/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
