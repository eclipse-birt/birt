/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.editors.ReportColorConstants;
import org.eclipse.birt.report.designer.internal.ui.editors.parts.DeferredGraphicalViewer;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.DummyEditpart;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.tools.DragEditPartsTracker;
import org.eclipse.swt.widgets.Display;

/**
 * TableDragGuideTracker
 */
public abstract class TableDragGuideTracker extends DragEditPartsTracker
{
	protected static final DecimalFormat FORMAT = new DecimalFormat("#0.000"); //$NON-NLS-1$
	public static final String PIXELS_LABEL = Messages.getString("TableDragGuideTracker.Pixels"); //$NON-NLS-1$
	private static final int DISTANCE = 30;
	private static final Insets INSETS = new Insets(2,4,2,4);
	private int start;

	private int maxWidth;
	private int end;

	private Figure marqueeRectangleFigure;
	private Label labelFigure;
	IChoiceSet choiceSet = ChoiceSetFactory.getElementChoiceSet(
			ReportDesignConstants.REPORT_DESIGN_ELEMENT,
			ReportDesignHandle.UNITS_PROP );

	/**
	 * Constructor
	 * @param sourceEditPart
	 * @param start
	 * @param end
	 */
	public TableDragGuideTracker( EditPart sourceEditPart, int start, int end )
	{
		super( sourceEditPart );
		this.start = start;
		this.end = end;
	}

	protected boolean handleDragInProgress( )
	{
		return super.handleDragInProgress( );
	}

	protected boolean handleButtonUp( int button )
	{
		boolean bool =  super.handleButtonUp( button );
		eraseSourceFeedback();
		return bool;
	}

	@Override
	protected boolean handleButtonDown( int button )
	{
		
		boolean bool =  super.handleButtonDown( button );
		if (button == 1)
		{
			showSourceFeedback( );
		}
		return bool;
	}
	protected void performSelection( )
	{
	}

	protected void showSourceFeedback( )
	{
		//super.showSourceFeedback();
		Rectangle rect = getMarqueeSelectionRectangle( ).getCopy( );
		getMarqueeFeedbackFigure( ).translateToRelative( rect );
		getMarqueeFeedbackFigure( ).setBounds( rect );
		
		getInfomationLabel( ).setText( getInfomation( ) );

	}

	private Dimension getDistance()
	{
		Point p = getStartLocation( );
		
		FigureCanvas canvas = ((DeferredGraphicalViewer)getSourceEditPart( ).getViewer( )).getFigureCanvas( );
		org.eclipse.swt.graphics.Rectangle rect = canvas.getBounds( );
	
		Dimension retValue = new Dimension(rect.width - p.x, p.y);
		if (canvas.getVerticalBar( ).isVisible( ))
		{
			retValue.width = retValue.width - canvas.getVerticalBar( ).getSize( ).x;
		}
		return retValue;
	}
	
	private void adjustLocation()
	{
		if (labelFigure == null)
		{
			return;
		}
		Rectangle rect = labelFigure.getBounds( );
		Dimension dim = getDistance( );
		Point p = labelFigure.getLocation( ).getCopy( );
		if (dim.width < rect.width)
		{
			p.x = p.x - (rect.width - dim.width);
		}
		if (dim.height < rect.height + DISTANCE)
		{
			p.y = p.y + (rect.height + DISTANCE - dim.height);
		}
		
		labelFigure.setLocation( p );
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.tools.TargetingTool#showTargetFeedback()
	 */
	protected void showTargetFeedback( )
	{
	}

	protected void performDrag( )
	{
		resize( );
		EditPart part = getSourceEditPart( );
		part.getViewer( ).setSelection( part.getViewer( ).getSelection( ) );
	}

	private IFigure getMarqueeFeedbackFigure( )
	{
		if ( marqueeRectangleFigure == null )
		{
			marqueeRectangleFigure = new MarqueeRectangleFigure( );
			addFeedback( marqueeRectangleFigure );
		}
		return marqueeRectangleFigure;
	}
	
	private Label getInfomationLabel()
	{
		if ( labelFigure == null )
		{
			labelFigure = new Label( );
			labelFigure.setBorder( new MarginBorder(new Insets(0,3,0,0)) 
			{
				public void paint(IFigure figure, Graphics graphics, Insets insets) 
				{ 
					tempRect.setBounds(getPaintRectangle(figure, insets));
					if (getWidth() % 2 == 1) {
						tempRect.width--;
						tempRect.height--;
					}
					tempRect.shrink(getWidth() / 2, getWidth() / 2);
					graphics.setLineWidth(getWidth());
					
					graphics.drawRectangle(tempRect);
				}
				
				private int getWidth()
				{
					return 1;
				}

			});
			labelFigure.setLabelAlignment( PositionConstants.LEFT );
			labelFigure.setOpaque( true );

			labelFigure.setBackgroundColor( ReportColorConstants.TableGuideFillColor );
		
			addFeedback( labelFigure );
			Dimension size = FigureUtilities.getTextExtents( getInfomation( ), getInfomationLabel( ).getFont( ) );
			
			
			Dimension newSize  = size.getCopy( ).expand( INSETS.getWidth( ), INSETS.getHeight( ) ) ;
			labelFigure.setSize( newSize );
			
			maxWidth = size.width;
			
			setLabelLocation( );
				
			//Insets insets = getInfomationLabel( ).getInsets( );
			adjustLocation( );
			
		}
		return labelFigure;
	}

	private void setLabelLocation()
	{
		if (labelFigure == null)
		{
			return;
		}
		Point p = getStartLocation( );
		
		labelFigure.translateToRelative( p );
		labelFigure.setLocation( new Point(p.x, p.y - DISTANCE ));
	}
	
	protected void eraseSourceFeedback( )
	{
		super.eraseSourceFeedback( );
		if ( marqueeRectangleFigure != null )
		{
			removeFeedback( marqueeRectangleFigure );
			marqueeRectangleFigure = null;
		}
		
		if ( labelFigure != null )
		{
			removeFeedback( labelFigure );
			labelFigure = null;
		}
	}

	private static class MarqueeRectangleFigure extends Figure
	{

		private int offset = 0;

		private boolean schedulePaint = true;

		private static final int DELAY = 110; //animation delay in millisecond

		/**
		 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
		 */
		protected void paintFigure( Graphics graphics )
		{
			Rectangle bounds = getBounds( ).getCopy( );
			graphics.translate( getLocation( ) );

			graphics.setXORMode( true );
			graphics.setForegroundColor( ColorConstants.white );
			graphics.setBackgroundColor( ColorConstants.black );

			graphics.setLineStyle( Graphics.LINE_DOT );

			int[] points = new int[6];

			points[0] = 0 + offset;
			points[1] = 0;
			points[2] = bounds.width - 1;
			points[3] = 0;
			points[4] = bounds.width - 1;
			points[5] = bounds.height - 1;

			graphics.drawPolyline( points );

			points[0] = 0;
			points[1] = 0 + offset;
			points[2] = 0;
			points[3] = bounds.height - 1;
			points[4] = bounds.width - 1;
			points[5] = bounds.height - 1;

			graphics.drawPolyline( points );

			graphics.translate( getLocation( ).getNegated( ) );

			if ( schedulePaint )
			{
				Display.getCurrent( ).timerExec( DELAY, new Runnable( ) {

					public void run( )
					{
						offset++;
						if ( offset > 5 )
							offset = 0;

						schedulePaint = true;
						repaint( );
					}
				} );
			}

			schedulePaint = false;
		}

	}

	public int getEnd( )
	{
		return end;
	}

	public void setEnd( int end )
	{
		this.end = end;
	}

	protected abstract void resize( );

	protected abstract Rectangle getMarqueeSelectionRectangle( );

	protected abstract Dimension getDragWidth(int start, int end );
	
	protected abstract String getInfomation();
	
	
	/**Update the label.
	 * @param label
	 */
	protected void updateInfomation(String label)
	{
		if (labelFigure == null)
		{
			return;
		}
		labelFigure.setText( label );
		Dimension size = FigureUtilities.getTextExtents(  label, labelFigure.getFont( ) );
		//Insets insets = getInfomationLabel( ).getInsets( );
		Insets insets = INSETS;
		Dimension newSize  = size.getCopy( ).expand( insets.getWidth( ), insets.getHeight( ) ) ;
		if (size.width > maxWidth)
		{
			maxWidth = size.width;
		}
		else
		{
			newSize = new Dimension(maxWidth, size.height).expand( insets.getWidth( ), insets.getHeight( ) );
		}
		labelFigure.setSize( newSize);
		setLabelLocation( );
		adjustLocation( );
	}
	
	public Dimension getDragWidth( )
	{
		return getDragWidth(getStart( ), getEnd() );
	}

	public int getStart( )
	{
		return start;
	}

	public void setStart( int start )
	{
		this.start = start;
	}

	protected int getTrueValue( int value )
	{
		return getTrueValue( value, getStart( ), getEnd() );
	}
	protected int getTrueValue( int value, int start, int end )
	{
		Dimension dimension = getDragWidth(start, end );
		if ( value < dimension.width )
		{
			value = dimension.width;
		}
		else if ( value > dimension.height )
		{
			value = dimension.height;
		}
		return value;
	}
	
	protected List filterEditPart(List list)
	{
		List retValue = new ArrayList();
		for (int i=0; i<list.size( ); i++)
		{
			if (list.get( i ) instanceof DummyEditpart)
			{
				retValue.add( list.get(i) );
			}
		}
		
		return retValue;
	}
	
	/**
	 * @param unit
	 * @return
	 */
	protected String getUnitDisplayName(String unit)
	{
		IChoice choice = choiceSet.findChoice( unit );
		return choice.getDisplayName( );
	}
}