/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
