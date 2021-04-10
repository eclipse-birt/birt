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

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.layout.area.impl.AbstractArea;

public class TableBandLayout extends BlockStackingLayout {

	public TableBandLayout(LayoutEngineContext context, ContainerLayout parent, IContent content) {
		super(context, parent, content);

	}

	// need not clip the row area(span cells)
	protected void addToRoot(AbstractArea area) {
		addToRoot(area, false);
	}

}
