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

package org.eclipse.birt.report.designer.internal.lib.editparts;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.AreaEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.DestroyEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.DummyEditpart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.GraphicalPartFactory;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.gef.EditPart;

/**
 * Factory to populate the edit part for given model type
 *
 */
public class LibraryGraphicalPartFactory extends GraphicalPartFactory {

	@Override
	public EditPart createEditPart(EditPart context, Object model) {
		// default edit part
		EditPart editPart = null;
		if (model instanceof LibraryHandle) {
			return new LibraryReportDesignEditPart(model);
		}

		if (ignoreModel(model)) {
			editPart = new EmptyEditPart(model);
		}
		if (editPart != null) {
			return editPart;
		}
		editPart = super.createEditPart(context, model);
		if (!(editPart instanceof DestroyEditPart) && (editPart == null || editPart instanceof DummyEditpart)) {
			editPart = new EmptyEditPart(model);
		}
		// fix bug235551
		if (editPart instanceof AreaEditPart) {
			editPart = new EmptyEditPart(model);
		}
		return editPart;
	}

	private boolean ignoreModel(Object model) {
//		if ( model instanceof ReportElementModel
//				&& ( ( (ReportElementModel) model ).getElementHandle( ) instanceof SimpleMasterPageHandle ) )
//		{
//			return true;
//		}
		if (model instanceof SimpleMasterPageHandle) {
			return true;
		}
		return model instanceof MasterPageHandle;
	}
}
