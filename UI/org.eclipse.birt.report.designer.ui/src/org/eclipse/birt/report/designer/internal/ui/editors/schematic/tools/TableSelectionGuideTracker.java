/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools;

import org.eclipse.birt.report.designer.internal.ui.editors.parts.DeferredGraphicalViewer;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.gef.Handle;
import org.eclipse.gef.SharedCursors;
import org.eclipse.gef.tools.DragEditPartsTracker;

/**
 * TableSelectionGuideTracker
 */
public abstract class TableSelectionGuideTracker extends DragEditPartsTracker {

	private int number;

	/**
	 * Constructor
	 * 
	 * @param sourceEditPart
	 */
	public TableSelectionGuideTracker(TableEditPart sourceEditPart, int number) {
		super(sourceEditPart);
		this.number = number;
		setDefaultCursor(SharedCursors.ARROW);
		setUnloadWhenFinished(false);
	}

	protected boolean handleButtonUp(int button) {
		if (stateTransition(STATE_DRAG_IN_PROGRESS, STATE_TERMINAL)) {
			return true;
		}

		return super.handleButtonUp(button);
	}

	protected void performConditionalSelection() {
		super.performConditionalSelection();
		select();
	}

	protected void performSelection() {

	}

	protected boolean handleDragInProgress() {
		if (isDealwithDrag()) {
			selectDrag();
		}

		return true;
	}

	/**
	 * @return number
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * Set number
	 * 
	 * @param number
	 */
	public void setNumber(int number) {
		this.number = number;
	}

	/**
	 * Provides select feature for tracker.
	 */
	public abstract void select();

	public boolean isDealwithDrag() {
		return true;
	}

	public void selectDrag() {

	}

//	protected boolean isSameTable()
//	{
//		if (getHandleUnderMouse() == null)
//		{
//			return false;
//		}
//		Object  obj  = getEditPartUnderMouse().getModel();
//		if (obj instanceof DesignElementHandle)
//		{
//			return false;
//		}
//		DesignElementHandle handle = (DesignElementHandle)obj;
//		DesignElementHandle parent = handle.getContainer();
//		while (parent != null)
//		{
//			if (parent != getSourceEditPart().getModel())
//			{
//				return true;
//			}
//			parent = parent.getContainer();
//		}
//		return false;
//	}

	/**
	 * Updates the target editpart and returns <code>true</code> if the target
	 * changes. The target is updated by using the target conditional and the target
	 * request. If the target has been locked, this method does nothing and returns
	 * <code>false</code>.
	 * 
	 * @return <code>true</code> if the target was changed
	 */
//	protected EditPart getEditPartUnderMouse() {
//		
//			Collection exclude = getExclusionSet();
//			EditPart editPart = getCurrentViewer().findObjectAtExcluding(
//				getLocation(),
//				exclude,
//				getTargetingConditional());
//			if (editPart != null)
//				return editPart.getTargetEditPart(getTargetRequest());
//			return null;
//			
//	}
	protected Handle getHandleUnderMouse() {
		return ((DeferredGraphicalViewer) (getSourceEditPart().getViewer())).findHandleAt(getLocation());
	}
}