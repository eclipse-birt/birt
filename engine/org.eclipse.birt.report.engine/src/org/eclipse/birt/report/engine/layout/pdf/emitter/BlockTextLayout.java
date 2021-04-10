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
import org.eclipse.birt.report.engine.content.IContent;

public class BlockTextLayout extends BlockStackingLayout {

	public BlockTextLayout(LayoutEngineContext context, ContainerLayout parent, IContent content) {
		super(context, parent, content);
	}

	public void layout() throws BirtException {
		LineLayout line = new LineLayout(context, this);
		line.initialize();
		TextAreaLayout blockText = new TextAreaLayout(context, line, content);
		blockText.initialize();
		blockText.layout();
		blockText.closeLayout();
		line.closeLayout();
	}

}
