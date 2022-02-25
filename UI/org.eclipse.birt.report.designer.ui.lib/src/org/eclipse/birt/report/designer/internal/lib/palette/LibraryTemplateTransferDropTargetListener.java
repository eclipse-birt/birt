/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation .
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

package org.eclipse.birt.report.designer.internal.lib.palette;

import org.eclipse.birt.report.designer.internal.ui.palette.ReportTemplateTransferDropTargetListener;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;

/**
 * Drag&Drop listener for the library editor.
 *
 */
public class LibraryTemplateTransferDropTargetListener extends ReportTemplateTransferDropTargetListener {

	/**
	 * @param viewer
	 */
	public LibraryTemplateTransferDropTargetListener(EditPartViewer viewer) {
		super(viewer);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.gef.dnd.AbstractTransferDropTargetListener#dragOver(org.eclipse.
	 * swt.dnd.DropTargetEvent)
	 */
	@Override
	public void dragOver(DropTargetEvent event) {
		super.dragOver(event);
		if (getTargetEditPart() == null) {
			event.detail = DND.DROP_NONE;
		}

//		if ( getTargetEditPart( ).getModel( ) instanceof LibraryHandle )
//		{
//			List list = getTargetEditPart( ).getChildren( );
//			if ( list.size( ) > 0 && !( list.get( 0 ) instanceof EmptyEditPart ) )
//			{
//				event.detail = DND.DROP_NONE;
//			}
//		}

	}
}
