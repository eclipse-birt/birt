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

package org.eclipse.birt.report.designer.internal.ui.editors;

import org.eclipse.ui.IEditorPart;

/**
 * Client implements this interface to provide some report editor's information.
 */

public interface IReportEditor {

	/**
	 * Get the report editor's editor part.
	 * 
	 * @return
	 */
	IEditorPart getEditorPart();
}
