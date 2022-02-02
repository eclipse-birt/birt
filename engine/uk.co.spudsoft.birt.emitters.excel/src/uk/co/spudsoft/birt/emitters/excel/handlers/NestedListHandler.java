/*************************************************************************************
 * Copyright (c) 2011, 2012, 2013 James Talbut.
 *  jim-emitters@spudsoft.co.uk
 *  
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     James Talbut - Initial implementation.
 ************************************************************************************/

package uk.co.spudsoft.birt.emitters.excel.handlers;

import org.eclipse.birt.report.engine.content.IListContent;

import uk.co.spudsoft.birt.emitters.excel.framework.Logger;

public class NestedListHandler extends TopLevelListHandler {

	public NestedListHandler(Logger log, IHandler parent, IListContent list) {
		super(log, parent, list);
	}

}
