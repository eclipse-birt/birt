/*******************************************************************************
 * Copyright (c) 2021 Solme AB and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Claes Rosell  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.designer.ui.ide.explorer;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 *
 * Abstract handler used for all handlers in the explorer
 *
 */
class ViewHandlerUtil {

	public static IFile getSelectedFile(IStructuredSelection selection) {
		IFile selectedFile = null;
		if (selection.size() == 1 && selection.getFirstElement() instanceof IFile) {
			selectedFile = (IFile) selection.getFirstElement();
		}

		return selectedFile;
	}
}
