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
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.editpolicies.SelectionEditPolicy;
import org.eclipse.gef.internal.ui.rulers.RulerEditPart;
import org.eclipse.gef.internal.ui.rulers.RulerFigure;
import org.eclipse.gef.rulers.RulerProvider;


/**
 * add comment here
 * 
 */
public class EditorRulerEditPart extends RulerEditPart
{

//	private EditorRulerChangeListener listener = new EditorRulerChangeListener.Stub() 
//	{
//		
//		/* (non-Javadoc)
//		 * @see org.eclipse.birt.report.designer.internal.ui.editors.rulers.EditorRulerChangeListener.Stub#notifyMarginChanged(int)
//		 */
//		public void notifyMarginChanged( int newUnit )
//		{
//			super.notifyMarginChanged( newUnit );
//		}
//	};
	/**
	 * @param model
	 */
	public EditorRulerEditPart( Object model )
	{
		super( model );
	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.internal.ui.rulers.RulerEditPart#activate()
	 */
	public void activate( )
	{
		//getRulerProvider().addRulerChangeListener(listener);
		super.activate( );
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.internal.ui.rulers.RulerEditPart#deactivate()
	 */
	public void deactivate( )
	{
		//getRulerProvider().removeRulerChangeListener(listener);
		super.deactivate( );
		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.internal.ui.rulers.RulerEditPart#getDragTracker(org.eclipse.gef.Request)
	 */
	public DragTracker getDragTracker( Request request )
	{
		return null;
	}
	
	/* (non-Javadoc)
	* @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	*/
	protected void createEditPolicies()
	{
		super.createEditPolicies();
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new EditRulerSelectionPolicy());
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.internal.ui.rulers.RulerEditPart#createFigure()
	 */
	protected IFigure createFigure( )
	{
		EditorRulerFigure ruler =  new EditorRulerFigure(isHorizontal(), getRulerProvider().getUnit());
		if (ruler.getUnit() == RulerProvider.UNIT_PIXELS)
			ruler.setInterval(100, 2);
		return ruler;
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.internal.ui.rulers.RulerEditPart#getDiagramViewer()
	 */
	protected GraphicalViewer getDiagramViewer( )
	{
		// TODO Auto-generated method stub
		return super.getDiagramViewer( );
	}
	public static class EditRulerSelectionPolicy extends SelectionEditPolicy {

		
		protected void hideFocus() {
			((RulerFigure)getHostFigure()).setDrawFocus(false);
		}
		protected void hideSelection() {
			((RulerFigure)getHostFigure()).setDrawFocus(false);
		}
		protected void showSelection( )
		{	
		}
	}
}
