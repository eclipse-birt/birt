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

	public abstract void start();

	public abstract void end();

	public abstract void startMasterPage(StyleEntry pageLayout, String masterPageName, String displayName);

	public abstract void endMasterPage();

	public abstract void startHeader();

	public abstract void endHeader();

	public abstract void startFooter();

	public abstract void endFooter();

	public abstract void writeString(String s);

}
