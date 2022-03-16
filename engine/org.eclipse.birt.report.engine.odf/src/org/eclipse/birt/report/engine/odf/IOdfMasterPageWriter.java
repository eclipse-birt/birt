/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.engine.odf;

import org.eclipse.birt.report.engine.odf.style.StyleEntry;

public interface IOdfMasterPageWriter extends IOdfWriter {

	void start();

	void end();

	void startMasterPage(StyleEntry pageLayout, String masterPageName, String displayName);

	void endMasterPage();

	void startHeader();

	void endHeader();

	void startFooter();

	void endFooter();

	@Override
	void writeString(String s);

}
