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

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.layout.area.impl.AbstractArea;

public class TableBandLayout extends BlockStackingLayout {

	public TableBandLayout(LayoutEngineContext context, ContainerLayout parent, IContent content) {
		super(context, parent, content);

	}

	// need not clip the row area(span cells)
	@Override
	protected void addToRoot(AbstractArea area) {
		addToRoot(area, false);
	}

}
