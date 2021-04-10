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

package org.eclipse.birt.report.designer.internal.ui.command;

import org.eclipse.birt.report.designer.internal.ui.dnd.InsertInLayoutUtil;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableUtil;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.StructuredSelection;

/**
 * 
 */

public class ResetImageSizeHandler extends SelectionHandler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.
	 * ExecutionEvent)
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		super.execute(event);
		Object selected = ((StructuredSelection) InsertInLayoutUtil
				.editPart2Model(TableUtil.filletCellInSelectionEditorpart(getSelection()))).getFirstElement();
		if (selected == null || !(selected instanceof ImageHandle))
			return Boolean.FALSE;
		ImageHandle image = (ImageHandle) selected;
		try {
			image.setWidth(null);
			image.setHeight(null);
		} catch (SemanticException e) {
		}
		return Boolean.TRUE;
	}

}
