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

package org.eclipse.birt.report.designer.internal.ui.dnd;

import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.gef.dnd.TemplateTransfer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;

/**
 * Supports dropping elements in outline view.
 */

public abstract class DesignElementDropAdapter extends ViewerDropAdapter {

	/**
	 * Constructor
	 * 
	 * @param viewer
	 */
	public DesignElementDropAdapter(TreeViewer viewer) {
		super(viewer);
	}

	/**
	 * @see ViewerDropAdapter#dragOver(DropTargetEvent)
	 * 
	 */
	public void dragOver(DropTargetEvent event) {
		super.dragOver(event);
		if (event.detail == DND.DROP_NONE)
			return;
		if (!validateTarget(getCurrentTarget())
				|| !validateTarget(getCurrentTarget(), TemplateTransfer.getInstance().getTemplate())) {
			event.detail = DND.DROP_NONE;
			if (Policy.TRACING_DND_DRAG) {
				System.out.println("DND >> Drag over " + event.getSource()); //$NON-NLS-1$
			}
		}
	}

	/**
	 * @see ViewerDropAdapter#performDrop(Object)
	 */
	public boolean performDrop(Object data) {

		if (DNDService.getInstance().performDrop(data, getCurrentTarget(), getCurrentOperation(),
				new DNDLocation(getCurrentLocation()))) {
			return true;
		}

		if (data instanceof Object[] && ((Object[]) data)[0] instanceof ThemeHandle) {
			if (getCurrentTarget() instanceof ModuleHandle) {
				if (Policy.TRACING_DND_DRAG) {
					System.out.println("DND >> Dropped. Operation: Apply Theme, Target: " //$NON-NLS-1$
							+ getCurrentTarget());
				}
				return applyTheme((ThemeHandle) ((Object[]) data)[0], (ModuleHandle) getCurrentTarget());
			} else {
				return false;
			}
		}

		if (getCurrentOperation() == DND.DROP_MOVE) {
			if (Policy.TRACING_DND_DRAG) {
				System.out.println("DND >> Dropped. Operation: Copy, Target: " //$NON-NLS-1$
						+ getCurrentTarget());
			}
			return moveData(data, getCurrentTarget());
		} else if (getCurrentOperation() == DND.DROP_COPY || getCurrentOperation() == DND.DROP_LINK) {
			if (Policy.TRACING_DND_DRAG) {
				System.out.println("DND >> Dropped. Operation: Move, Target: " //$NON-NLS-1$
						+ getCurrentTarget());
			}
			return copyData(data, getCurrentTarget());
		}
		return false;
	}

	/**
	 * @see ViewerDropAdapter#validateDrop(Object, int, TransferData)
	 */
	public boolean validateDrop(Object target, int op, TransferData type) {

		// if(target!=null){
		// Object adapter = ElementAdapterManager.getAdatper( target,
		// IElementDropAdapter.class );
		// if(adapter!=null){
		// IElementDropAdapter dropAdapter = (IElementDropAdapter)adapter;
		// return dropAdapter.validateDrop( target, getCurrentOperation( ),
		// getCurrentLocation( ), null, type );
		// }
		// }

		return TemplateTransfer.getInstance().isSupportedType(type);
	}

	/**
	 * Validates target elements can be dropped
	 * 
	 * @param target target elements
	 * @return if target elements can be dropped
	 */
	protected abstract boolean validateTarget(Object target);

	/**
	 * Validates target elements can contain transfer data data
	 * 
	 * @param target   target elements
	 * @param transfer transfer data
	 * @return if target elements can be dropped
	 */
	protected abstract boolean validateTarget(Object target, Object transfer);

	/**
	 * Moves elements
	 * 
	 * @param transfer transfer elements.
	 * @param target   target elements
	 * @return if succeeding in moving data
	 */
	protected abstract boolean moveData(Object transfer, Object target);

	protected abstract boolean applyTheme(ThemeHandle themeHandle, ModuleHandle moudelHandle);

	/**
	 * Copys elements
	 * 
	 * @param transfer transfer elements.
	 * @param target   target elements
	 * @return if succeeding in copying data
	 */
	protected abstract boolean copyData(Object transfer, Object target);
}
