/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.toc;

import org.eclipse.birt.report.engine.api.impl.ReportDocumentConstants;

public interface ITOCConstants {

	String TOC_STREAM = ReportDocumentConstants.TOC_STREAM;

	String VERSION_PREFIX = "__Version : ";
	String VERSION_V0 = VERSION_PREFIX + "0.0";
	String VERSION_V1 = VERSION_PREFIX + "1.0";
	String VERSION_V2 = VERSION_PREFIX + "2.0";
	String VERSION_V3 = VERSION_PREFIX + "3.0";

	String TOC_PREFIX = "__TOC_";//

}
