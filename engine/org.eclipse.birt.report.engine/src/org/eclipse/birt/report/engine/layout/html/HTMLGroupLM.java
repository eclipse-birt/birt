/***********************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.html;

import org.eclipse.birt.report.engine.content.IBandContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IGroupContent;

public class HTMLGroupLM extends HTMLRepeatHeaderLM {

	public HTMLGroupLM(HTMLLayoutManagerFactory factory) {
		super(factory);
	}

	@Override
	public int getType() {
		return LAYOUT_MANAGER_GROUP;
	}

	protected boolean isHeaderBand() {
		if (childLayout != null) {
			IContent band = ((HTMLAbstractLM) childLayout).getContent();
			if (band instanceof IBandContent) {
				return ((IBandContent) band).getBandType() == IBandContent.BAND_GROUP_HEADER;
			}
		}
		return false;
	}

	@Override
	protected boolean shouldRepeatHeader() {
		return ((IGroupContent) content).isHeaderRepeat() && getHeader() != null && !isHeaderBand();
	}

	@Override
	protected IBandContent getHeader() {
		return ((IGroupContent) content).getHeader();
	}
}
