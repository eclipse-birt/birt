/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.layout;

import java.util.ArrayList;

/**
 * 
 */

public class CompositeLayoutPageHandler implements ILayoutPageHandler {

	ArrayList eventHandlers = new ArrayList();

	public void onPage(long page, Object context) {
		for (int i = 0; i < eventHandlers.size(); i++) {
			ILayoutPageHandler eventHandler = (ILayoutPageHandler) eventHandlers.get(i);
			if (eventHandler != null) {
				eventHandler.onPage(page, context);
			}
		}
	}

	public void addPageHandler(ILayoutPageHandler layoutPageHandler) {
		eventHandlers.add(layoutPageHandler);
	}

}
