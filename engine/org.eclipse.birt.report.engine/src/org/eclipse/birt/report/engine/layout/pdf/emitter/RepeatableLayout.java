/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.pdf.emitter;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IBandContent;
import org.eclipse.birt.report.engine.content.IContent;

public abstract class RepeatableLayout extends BlockStackingLayout {
	protected int repeatCount = 0;
	protected int bandStatus = IBandContent.BAND_HEADER;

	public RepeatableLayout(LayoutEngineContext context, ContainerLayout parent, IContent content) {
		super(context, parent, content);
	}

	@Override
	protected void initialize() throws BirtException {
		super.initialize();
		repeatHeader();
	}

	protected abstract void repeatHeader() throws BirtException;

}
