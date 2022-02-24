/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.model.parser;

import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.XMLParserHandler;

/**
 * 
 */

public class SlotState extends AbstractParseState {

	/**
	 * Pointer to the design file parser handler.
	 */

	protected ModuleParserHandler handler = null;

	/**
	 * 
	 */

	protected int slotID;

	/**
	 * 
	 */

	protected DesignElement container;

	/**
	 * Constructor.
	 * 
	 * @param handler   the parser handler
	 * @param container the container element
	 * @param slot      the container slot number
	 */

	protected SlotState(ModuleParserHandler handler, DesignElement container, int slot) {
		this.handler = handler;
		this.container = container;
		slotID = slot;

		initLineNumber();
	}

	/**
	 * 
	 */

	private void initLineNumber() {
		ModuleParserHandler handler = (ModuleParserHandler) getHandler();
		if (handler.markLineNumber) {
			ContainerContext context = new ContainerContext(container, slotID);
			handler.tempLineNumbers.put(context, Integer.valueOf(handler.getCurrentLineNo()));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#getHandler()
	 */

	public XMLParserHandler getHandler() {
		return handler;
	}

}
