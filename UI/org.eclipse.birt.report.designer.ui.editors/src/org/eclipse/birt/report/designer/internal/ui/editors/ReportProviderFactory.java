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

import org.eclipse.birt.report.designer.ui.editors.IReportProvider;
import org.eclipse.birt.report.designer.ui.editors.IReportProviderFactory;
import org.eclipse.ui.IEditorInput;

/**
 * ReportProviderFactory
 */
public class ReportProviderFactory implements IReportProviderFactory {

	public IReportProvider getProvider(IEditorInput input) {
		return new FileReportProvider();
	}

}
