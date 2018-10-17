/*************************************************************************************
 * Copyright (c) 2011, 2012, 2013 James Talbut.
 *  jim-emitters@spudsoft.co.uk
 *  
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     James Talbut - Initial implementation.
 ************************************************************************************/

package uk.co.spudsoft.birt.emitters.excel.handlers;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IListContent;

import uk.co.spudsoft.birt.emitters.excel.HandlerState;
import uk.co.spudsoft.birt.emitters.excel.framework.Logger;

public class NestedListHandler extends TopLevelListHandler {
	
	public NestedListHandler(Logger log, IHandler parent, IListContent list) {
		super(log, parent, list);
	}

	@Override
	public void startList(HandlerState state, IListContent list) throws BirtException {
		String name = list.getName();
		if( ( name != null ) && ! name.isEmpty() ) {
			state.sheetName = name;
		}
		super.startList(state, list);
		
	}
	
	

}
