/***********************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/
package org.eclipse.birt.report.engine.nLayout;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.nLayout.area.impl.ContainerArea;

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
