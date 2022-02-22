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

package org.eclipse.birt.report.designer.internal.lib.editparts;

import org.eclipse.birt.report.designer.core.model.LibRootModel;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.DummyEditpart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.GraphicalPartFactory;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.MasterPageEditPart;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.gef.EditPart;

/**
 *
 */

public class LibraryMasterPageGraphicalPartFactory extends GraphicalPartFactory {
	@Override
	public EditPart createEditPart(EditPart context, Object model) {
		// default edit part

		EditPart editPart = null;
		if (model instanceof LibRootModel) {
			model = ((LibRootModel) model).getModel();
		} else if (model instanceof LibraryHandle) {
			// return new LibraryReportDesignEditPart( model );
			editPart = new EmptyEditPart(model) {
				@Override
				public void activate() {
//					do nothing
				}

				@Override
				public void deactivate() {
					// do nothing
				}
			};
		} else if (model instanceof MasterPageHandle) {
			return new MasterPageEditPart(model);
		}

//		if ( ignoreModel( model ) )
//		{
//			editPart = new EmptyEditPart( model );
//		}
		if (editPart != null) {
			return editPart;
		}
		editPart = super.createEditPart(context, model);
		if (editPart == null || editPart instanceof DummyEditpart) {
			editPart = new EmptyEditPart(model);
		}
		return editPart;
	}

//	private boolean ignoreModel( Object model )
//	{
//		if ( model instanceof ReportElementModel
//				&& ( ( (ReportElementModel) model ).getElementHandle( ) instanceof SimpleMasterPageHandle ) )
//		{
//			return true;
//		}
//		return model instanceof MasterPageHandle;
//	}
}
