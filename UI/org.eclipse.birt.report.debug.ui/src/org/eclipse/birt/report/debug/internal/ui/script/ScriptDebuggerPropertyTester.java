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

package org.eclipse.birt.report.debug.internal.ui.script;

import org.eclipse.birt.report.designer.core.IReportElementConstants;
import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IFile;
import org.eclipse.ui.part.FileEditorInput;

/**
 * 
 */

public class ScriptDebuggerPropertyTester extends PropertyTester {

	/**
	 * 
	 */
	public ScriptDebuggerPropertyTester() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.expressions.IPropertyTester#test(java.lang.Object,
	 * java.lang.String, java.lang.Object[], java.lang.Object)
	 */
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		if (property.equals("isRptdesign"))//$NON-NLS-1$
		{
			IFile file = null;
			if (receiver instanceof FileEditorInput) {
				FileEditorInput input = (FileEditorInput) receiver;
				if (input.getFile() != null
						&& IReportElementConstants.DESIGN_FILE_EXTENSION.equals(input.getFile().getFileExtension())) {
					return true;
				}
			}

		}
		return false;
	}

}
