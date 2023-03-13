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

package org.eclipse.birt.report.designer.internal.ui.editors;

import java.io.File;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.IElementFactory;
import org.eclipse.ui.IMemento;

/**
 *
 */

public class ReportEditorInputFactory implements IElementFactory {

	public static final String ID = "org.eclipse.birt.report.designer.ui.ReportEditorInputFactory"; //$NON-NLS-1$

	private static final String TAG_PATH = "path"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.IElementFactory#createElement(org.eclipse.ui.IMemento)
	 */
	@Override
	public IAdaptable createElement(IMemento memento) {
		String fileName = memento.getString(TAG_PATH);
		if (fileName == null) {
			return null;
		}

		File file = new File(fileName);
		if (file != null) {
			return new ReportEditorInput(file);
		} else {
			return null;
		}
	}

	/**
	 * Saves the state of the given file editor input into the given memento.
	 *
	 * @param memento the storage area for element state
	 * @param input   the file editor input
	 */
	public static void saveState(IMemento memento, ReportEditorInput input) {
		File file = input.getFile();
		memento.putString(TAG_PATH, file.getAbsolutePath());
	}
}
