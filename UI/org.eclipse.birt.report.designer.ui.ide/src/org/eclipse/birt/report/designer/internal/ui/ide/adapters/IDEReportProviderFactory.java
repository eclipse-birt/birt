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

package org.eclipse.birt.report.designer.internal.ui.ide.adapters;

import org.eclipse.birt.report.designer.internal.ui.editors.ReportProviderFactory;
import org.eclipse.birt.report.designer.ui.editors.IReportProvider;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPathEditorInput;

/**
 * ReportProviderFactory use for workspace resources.
 */

public class IDEReportProviderFactory extends ReportProviderFactory {

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.editors.ReportProviderFactory#
	 * getProvider(org.eclipse.ui.IEditorInput)
	 */
	@Override
	public IReportProvider getProvider(IEditorInput input) {
		if (input instanceof IFileEditorInput) {
			return new IDEFileReportProvider();
		} else if (input instanceof IPathEditorInput) {
			return super.getProvider(input);
		}
//		else
//		{
//			return FileReportProvider.getInstance( );
//		}
		return null;
	}

}
