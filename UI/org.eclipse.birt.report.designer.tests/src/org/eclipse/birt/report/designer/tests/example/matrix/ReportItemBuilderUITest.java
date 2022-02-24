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

package org.eclipse.birt.report.designer.tests.example.matrix;

import org.eclipse.birt.report.designer.ui.extensions.IReportItemBuilderUI;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.jface.window.Window;

public class ReportItemBuilderUITest implements IReportItemBuilderUI {

	public int open(ExtendedItemHandle handle) {
		if (handle == null) {
			return Window.CANCEL;
		}
		try {
			handle.loadExtendedElement();
		} catch (ExtendedElementException e) {
			return Window.CANCEL;
		}
		if (handle.getProperty(TestingMatrixUI.TEST_PROPERTY[1]) == null) {
			System.out.println("Created OK"); //$NON-NLS-1$
			try {
				handle.setProperty(TestingMatrixUI.TEST_PROPERTY[1], TestingMatrixUI.TEST_ELEMENT_CONTENT[1]);
			} catch (SemanticException e1) {
				return Window.CANCEL;
			}

			return Window.OK;
		}
		try {
			int value = ((Integer) handle.getProperty(TestingMatrixUI.TEST_PROPERTY[1])).intValue() + 1;
			handle.setProperty(TestingMatrixUI.TEST_PROPERTY[0], TestingMatrixUI.TEST_ELEMENT_CONTENT_EDITED[0]);
			handle.setProperty(TestingMatrixUI.TEST_PROPERTY[1], new Integer(value));
		} catch (SemanticException e1) {
			return Window.CANCEL;
		}

		System.out.println("Edit OK"); //$NON-NLS-1$
		return Window.OK;

	}

};
