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

import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;

import uk.co.spudsoft.birt.emitters.excel.framework.Logger;

public class TopLevelTableCellHandler extends AbstractRealTableCellHandler {

	public TopLevelTableCellHandler(IContentEmitter emitter, Logger log, IHandler parent, ICellContent cell) {
		super(emitter, log, parent, cell);
	}

}
