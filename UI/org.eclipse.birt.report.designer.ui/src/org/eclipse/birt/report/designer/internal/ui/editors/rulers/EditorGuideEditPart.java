/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation .
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.rulers;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.editpolicies.SelectionEditPolicy;
import org.eclipse.gef.internal.ui.rulers.GuideEditPart;

/**
 * add comment here
 *  
 */
public class EditorGuideEditPart extends GuideEditPart
{
	private GuideLineFigure guideLineFig;
	/**
	 * @param model
	 */
	public EditorGuideEditPart( Object model )
	{
		super( model );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.internal.ui.rulers.GuideEditPart#getDragTracker(org.eclipse.gef.Request)
	 */
	public DragTracker getDragTracker( Request request )
	{
		return new EditorGuideDragTracker(this);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure( )
	{
		guideLineFig = createGuideLineFigure();
		//getGuideLayer().add(getGuideLineFigure());
		//getGuideLayer().setConstraint(getGuideLineFigure(), new Boolean(isHorizontal()));
		return new EditorGuideFigure( isHorizontal( ) );	
	}
	
	
	public IFigure getGuideLineFigure() {
		return guideLineFig;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new GuideSelectionPolicy());
		installEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE,	new EditorDragGuidePolicy());
	}
	
	public static class GuideSelectionPolicy extends SelectionEditPolicy {
		protected void hideFocus() {
			//((GuideFigure)getHostFigure()).setDrawFocus(false);
		}
		protected void hideSelection() {
			//((GuideFigure)getHostFigure()).setDrawFocus(false);
		}
		protected void showFocus() {
			//((GuideFigure)getHostFigure()).setDrawFocus(true);
		}
		protected void showSelection() {
			//((GuideFigure)getHostFigure()).setDrawFocus(true);
		}
	}
	
	
}