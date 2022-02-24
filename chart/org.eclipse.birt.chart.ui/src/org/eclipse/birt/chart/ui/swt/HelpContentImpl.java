/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.chart.ui.swt;

import org.eclipse.birt.chart.ui.swt.interfaces.IHelpContent;

/**
 * @author Actuate Corporation
 */
public class HelpContentImpl implements IHelpContent {

	private String sTitle;

	private String sDescription;

	public HelpContentImpl(String sHelpTitle, String sHelpText) {
		this.sTitle = sHelpTitle;
		this.sDescription = sHelpText;
	}

	public String getTitle() {
		return sTitle;
	}

	public String getDescription() {
		return sDescription;
	}
}
