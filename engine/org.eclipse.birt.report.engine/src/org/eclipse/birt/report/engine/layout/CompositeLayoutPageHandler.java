/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
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

	@Override
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
