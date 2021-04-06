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

package org.eclipse.birt.report.designer.internal.ui.views.data.dnd;

import org.eclipse.birt.report.designer.internal.ui.views.outline.dnd.DesignerDropListener;
import org.eclipse.birt.report.model.api.CascadingParameterGroupHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.jface.viewers.TreeViewer;

/**
 * 
 */

public class ParameterDropListener extends DesignerDropListener {

	public ParameterDropListener(TreeViewer viewer) {
		super(viewer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.outline.dnd.
	 * DesignerDropListener#validateTarget(java.lang.Object, java.lang.Object)
	 */
	protected boolean validateTarget(Object target, Object transfer) {
		if (target instanceof DataSetHandle || target instanceof CascadingParameterGroupHandle
				|| (target instanceof ScalarParameterHandle
						&& ((ScalarParameterHandle) target).getContainer() instanceof CascadingParameterGroupHandle)) {
			return false;
		}
//		if ( target instanceof ReportElementModel )
//		{
//			ReportElementModel model = (ReportElementModel) target;
//			if ( model.getSlotId( ) == ModuleHandle.DATA_SET_SLOT )
//			{
//				return false;
//			}
//			return true;
//		}

		if (target instanceof SlotHandle) {
			SlotHandle model = (SlotHandle) target;
			if (model.getSlotID() == ModuleHandle.DATA_SET_SLOT) {
				return false;
			}
			return true;
		}

		return super.validateTarget(target, transfer);
	}

}
