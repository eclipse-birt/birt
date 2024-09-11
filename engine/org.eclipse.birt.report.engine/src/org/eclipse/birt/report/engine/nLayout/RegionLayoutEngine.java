/***********************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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
package org.eclipse.birt.report.engine.nLayout;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.nLayout.area.impl.ContainerArea;

/**
 *
 * This is used only in HTMLLeafItemLM.splitText and in PageArea.layoutFooter
 * and layoutHeader.
 *
 * Originally undocumented, so we don't more about the exact purpose ATM.
 *
 */
public class RegionLayoutEngine extends LayoutEngine implements IContentEmitter {

	public RegionLayoutEngine(ContainerArea container, LayoutContext context) {
		super(context);
		current = container;
		current.setMaxAvaWidth(current.getWidth());
	}

	public void layout(IContent content) throws BirtException {
		current.initialize();
		visitChildren(content, this);
		current.close();
	}

}
