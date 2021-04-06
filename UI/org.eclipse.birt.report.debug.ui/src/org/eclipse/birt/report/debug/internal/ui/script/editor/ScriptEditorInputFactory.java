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

package org.eclipse.birt.report.debug.internal.ui.script.editor;

import java.io.File;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.IElementFactory;
import org.eclipse.ui.IMemento;

/**
 * ScriptEditorInputFactory
 */
public class ScriptEditorInputFactory implements IElementFactory {

	public static final String ID = "org.eclipse.birt.report.debug.ui.script.ScriptEditorInputFactory"; //$NON-NLS-1$

	private static final String TAG_PATH = "path"; //$NON-NLS-1$
	private static final String TAG_ID = "id"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IElementFactory#createElement(org.eclipse.ui.IMemento)
	 */
	public IAdaptable createElement(IMemento memento) {
		String fileName = memento.getString(TAG_PATH);
		if (fileName == null) {
			return null;
		}

		String id = memento.getString(TAG_ID);
		if (id == null) {
			return null;
		}

		File file = new File(fileName);
		if (file != null) {
			return new DebugJsInput(file, id);
		} else {
			return null;
		}
	}

	/**
	 * Save the memento.
	 * 
	 * @param memento
	 * @param input
	 */
	public static void saveState(IMemento memento, DebugJsInput input) {
		File file = input.getFile();
		memento.putString(TAG_PATH, file.getAbsolutePath());

		memento.putString(TAG_ID, input.getId());
	}
}
