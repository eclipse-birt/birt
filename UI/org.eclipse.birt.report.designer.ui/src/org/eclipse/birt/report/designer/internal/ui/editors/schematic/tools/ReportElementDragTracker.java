/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools;

import java.util.ArrayList;

import org.eclipse.birt.report.designer.internal.ui.editors.parts.DeferredGraphicalViewer;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.IDelaySelectionDragTracker;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.DragEditPartsTracker;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Display;

/**
 * 
 */
public class ReportElementDragTracker extends DragEditPartsTracker
{

	private static final int FLAG_DELAY_SELECTION = DragEditPartsTracker.MAX_FLAG << 1;
	protected static final int MAX_FLAG = FLAG_DELAY_SELECTION;
	private DelaySelectionHelper activeHelper = null;
	private static final int DELAY_TIME = 1700;
	private IDelaySelectionDragTracker proxy;

	public ReportElementDragTracker( EditPart sourceEditPart )
	{
		super( sourceEditPart );

	}

	protected boolean handleButtonDown( int button )
	{
		if ( getCurrentViewer( ) instanceof DeferredGraphicalViewer )
		{
			( (DeferredGraphicalViewer) getCurrentViewer( ) ).initStepDat( );
		}
		boolean bool = super.handleButtonDown( button );

		if ( !getFlag( FLAG_DELAY_SELECTION )
				&& getCurrentInput( ).isMouseButtonDown( 1 ) )
		{
			new DelaySelectionHelper( );
			setFlag( FLAG_DELAY_SELECTION, true );
		}

		return bool;
	}

	@Override
	public void mouseUp( MouseEvent me, EditPartViewer viewer )
	{
		if ( proxy != null )
		{
			eraseSourceFeedback( );
			eraseTargetFeedback( );

			proxy.mouseUp( me, viewer );
			proxy = null;
			return;
		}
		activeHelper = null;
		super.mouseUp( me, viewer );
	}

	@Override
	protected boolean handleDragInProgress( )
	{
		if ( proxy != null )
		{
			eraseSourceFeedback( );
			eraseTargetFeedback( );
			proxy.setLocation( getLocation( ) );
			proxy.setViewer( getCurrentViewer( ) );
			return proxy.handleDragInProgress( );
		}

		activeHelper = null;
		return super.handleDragInProgress( );
	}

	@Override
	protected void resetFlags( )
	{
		super.resetFlags( );
		setFlag( FLAG_DELAY_SELECTION, false );
	}

	private EditPart getEditPartUnderMouse( )
	{
		EditPart editPart = getCurrentViewer( ).findObjectAtExcluding( getLocation( ),
				new ArrayList( ) );

		return editPart;
	}

	class DelaySelectionHelper implements Runnable
	{

		public DelaySelectionHelper( )
		{			
			activeHelper = this;
			Display.getCurrent( ).timerExec( DELAY_TIME, this );	
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run( )
		{
			if (!getSourceEditPart( ).isActive( ))
			{
				return;
			}
			EditPartViewer viewer = getSourceEditPart( ).getViewer( );
			EditPart parent = getSourceEditPart( ).getParent( );
			if ( activeHelper == this
					&& getSourceEditPart( ).isActive( )
					&& viewer.getControl( ) != null
					&& !viewer.getControl( ).isDisposed( ) )
			{
				if ( viewer.getSelectedEditParts( ).size( ) == 1 )
				{
					if ( parent.getAdapter( IDelaySelectionDragTracker.class ) != null )
					{
						proxy = (IDelaySelectionDragTracker) parent.getAdapter( IDelaySelectionDragTracker.class );
						if ( viewer instanceof DeferredGraphicalViewer )
							( (DeferredGraphicalViewer) viewer ).setSelection( new StructuredSelection( proxy.getSourceEditPart( ) ),
									true );

						if ( getSourceEditPart( ) != getEditPartUnderMouse( ) )
						{
							IFigure figure = ( (GraphicalEditPart) getSourceEditPart( ) ).getFigure( );
							Rectangle center = figure.getBounds( ).getCopy( );
							figure.translateToAbsolute( center );
							proxy.setStartLocation( center.getCenter( ) );
						}
						else
						{
							proxy.setStartLocation( getStartLocation( ) );
						}
						//
						proxy.setState( STATE_DRAG_IN_PROGRESS );
					}
					setFlag( FLAG_DELAY_SELECTION, true );
				}
				else
				{
					setFlag( FLAG_DELAY_SELECTION, false );
				}
			}
			else
			{
				setFlag( FLAG_DELAY_SELECTION, false );
			}
			activeHelper = null;

		}
	}
}
