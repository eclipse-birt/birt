/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.layout;

import org.eclipse.birt.report.engine.layout.html.HTMLReportLayoutEngine;

public class LayoutEngineFactory {
	public static IReportLayoutEngine createLayoutEngine(String paginationType) {
		/*
		 * if ( ExtensionManager.PAPER_SIZE_PAGINATION.equals( paginationType ) ) { try
		 * { Class clazz = Class .forName(
		 * "org.eclipse.birt.report.engine.layout.pdf.PDFReportLayoutEngine" );
		 * //$NON-NLS-1$ Object engine = clazz.newInstance( ); return
		 * (IReportLayoutEngine) engine; } catch ( Exception ex ) { } return null; }
		 */
		return new HTMLReportLayoutEngine();
	}
}
