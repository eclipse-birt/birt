/***********************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	protected void initialize() throws BirtException {
		super.initialize();
		repeatHeader();
	}

	protected abstract void repeatHeader() throws BirtException;

}
