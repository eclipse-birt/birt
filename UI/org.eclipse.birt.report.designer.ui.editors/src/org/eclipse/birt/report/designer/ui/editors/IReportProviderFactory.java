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

package org.eclipse.birt.report.designer.ui.editors;

import org.eclipse.ui.IEditorInput;

/**
 * ReportProvider factory.
 * 
 * Client implements this interface to provide factory to create IReportProvider
 * for editor input.
 */
public interface IReportProviderFactory {

	/**
	 * Get the IReportProvider for editor input.
	 * 
	 * @param input
	 * @return
	 */
	IReportProvider getProvider(IEditorInput input);
}
