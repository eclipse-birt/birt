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
