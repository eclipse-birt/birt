
package org.eclipse.birt.report.designer.internal.ui.swt.custom;

import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.SortMap;
import org.eclipse.birt.report.designer.util.ColorManager;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class BorderCanvas extends Canvas
{

	private boolean mouseIn = false;
	private int mouseInArea = SWT.NONE;

	public BorderCanvas( Composite parent, int style )
	{
		super( parent, SWT.NONE );

		addPaintListener( new PaintListener( ) {

			public void paintControl( PaintEvent e )
			{
				BorderCanvas.this.paintControl( e );
			}
		} );

		addMouseListener( new MouseAdapter( ) {

			public void mouseUp( MouseEvent e )
			{
				int width = getSize( ).x;
				int height = getSize( ).y;
				int x = ( width - 100 ) / 2;
				int y = ( height - 100 ) / 2;
				Region top = new Region( );
				Region bottom = new Region( );
				Region left = new Region( );
				Region right = new Region( );

				top.add( new Rectangle( x, y - 11, 100, 11 ) );
				bottom.add( new Rectangle( x, y + 100, 100, 11 ) );
				left.add( new Rectangle( x - 11, y, 11, 100 ) );
				right.add( new Rectangle( x + 100, y, 11, 100 ) );

				top.add( new int[]{
						x, y, x + 100, y, x + 50, y + 50
				} );
				bottom.add( new int[]{
						x, y + 100, x + 100, y + 100, x + 50, y + 50
				} );
				left.add( new int[]{
						x, y, x, y + 100, x + 50, y + 50
				} );
				right.add( new int[]{
						x + 100, y, x + 100, y + 100, x + 50, y + 50
				} );

				if ( top.contains( e.x, e.y ) )
				{
					mouseIn = true;
					mouseInArea = SWT.TOP;
				}
				else if ( bottom.contains( e.x, e.y ) )
				{
					mouseIn = true;
					mouseInArea = SWT.BOTTOM;
				}
				else if ( left.contains( e.x, e.y ) )
				{
					mouseIn = true;
					mouseInArea = SWT.LEFT;
				}
				else if ( right.contains( e.x, e.y ) )
				{
					mouseIn = true;
					mouseInArea = SWT.RIGHT;
				}
				else
				{
					mouseIn = false;
					mouseInArea = SWT.NONE;
				}
				
				// dispose resources
				top.dispose( );
				bottom.dispose( );
				right.dispose( );
				left.dispose( );
				
				if ( mouseIn )
				{
					if ( listener != null )
					{
						Event event = new Event( );
						event.detail = mouseInArea;
						listener.handleEvent( event );
					}
				}
			}

		} );

	}
	private Listener listener;

	private SortMap borderInfoMap = new SortMap( );

	public void setBorderInfomation( BorderInfomation info )
	{
		borderInfoMap.put( info.getPosition( ), info );
	}

	protected void paintControl( PaintEvent e )
	{
		GC gc = e.gc;
		gc.setLineStyle( SWT.LINE_DOT );
		gc.setLineWidth( 1 );
		int width = getSize( ).x;
		int height = getSize( ).y;
		int x = ( width - 100 ) / 2;
		int y = ( height - 100 ) / 2;
		gc.drawLine( x - 10 - 1, y - 1, x - 1, y - 1 );
		gc.drawLine( x + 100 + 1, y - 1, x + 100 + 10 + 1, y - 1 );
		gc.drawLine( x - 10 - 1, y + 100 + 1, x - 1, y + 100 + 1 );
		gc.drawLine( x + 100 + 1, y + 100 + 1, x + 100 + 10 + 1, y + 100 + 1 );

		gc.drawLine( x - 1, y - 10 - 1, x - 1, y - 1 );
		gc.drawLine( x - 1, y + 100 + 1, x - 1, y + 100 + 10 + 1 );
		gc.drawLine( x + 100 + 1, y - 10 - 1, x + 100 + 1, y - 1 );
		gc.drawLine( x + 100 + 1, y + 100 + 1, x + 100 + 1, y + 100 + 10 + 1 );
		for ( int i = 0; i < borderInfoMap.size( ); i++ )
		{
			BorderInfomation info = (BorderInfomation) borderInfoMap.getValue( i );

			if ( info.getStyle( ) == null || info.getStyle( ).equals( "" ) ) //$NON-NLS-1$
				continue;

			if ( info.getColor( ) == null )
				gc.setForeground( getDisplay( ).getSystemColor( SWT.COLOR_BLACK ) );
			else
				gc.setForeground( ColorManager.getColor( info.getColor( ) ) );

			if ( !( info.getStyle( ).equals( DesignChoiceConstants.LINE_STYLE_DOUBLE ) ) )
			{
				if ( DesignChoiceConstants.LINE_STYLE_DOTTED.equals( info.style ) )
					gc.setLineStyle( SWT.LINE_DOT );
				else if ( DesignChoiceConstants.LINE_STYLE_DASHED.equals( info.style ) )
					gc.setLineStyle( SWT.LINE_DASH );
				else if ( DesignChoiceConstants.LINE_STYLE_SOLID.equals( info.style )
						|| DesignChoiceConstants.LINE_STYLE_DOUBLE.equals( info.style ) )
					gc.setLineStyle( SWT.LINE_SOLID );

				drawLine( gc, width, height, info );
			}
			else
			{
				gc.setLineStyle( SWT.LINE_SOLID );
				int gcWidth = 1;
				int gcSeperator = 1;
				int gcInnerWidth = 1;
				int customWidth = -1;
				if ( !DesignChoiceConstants.LINE_WIDTH_THIN.equals( info.width )
						&& !DesignChoiceConstants.LINE_WIDTH_MEDIUM.equals( info.width )
						&& !DesignChoiceConstants.LINE_WIDTH_THICK.equals( info.width )
						&& info.width != null
						&& !info.width.equals( "" ) ) //$NON-NLS-1$
				{
					try
					{
						customWidth = (int) DimensionValue.parse( info.width )
								.getMeasure( );
						if ( DimensionValue.parse( info.width )
								.getUnits( )
								.equals( DesignChoiceConstants.UNITS_PX ) )
						{
							if ( customWidth % 3 == 0 )
							{
								gcWidth = gcSeperator = gcInnerWidth = customWidth / 3;
							}
							else if ( customWidth % 3 == 1 )
							{
								gcWidth = customWidth / 3 + 1;
								gcSeperator = gcInnerWidth = customWidth / 3;
							}
							else
							{
								gcWidth = gcSeperator = customWidth / 3 + 1;
								gcInnerWidth = customWidth / 3;
							}
						}

					}
					catch ( Exception e1 )
					{
						ExceptionHandler.handle( e1 );
					}
				}

				if ( customWidth > 3 )
				{
					gc.setLineWidth( gcWidth );
					if ( info.position.equals( BorderInfomation.BORDER_LEFT ) )
					{
						gc.drawLine( ( width - 100 )
								/ 2
								+ gc.getLineWidth( )
								/ 2, ( height - 100 ) / 2, ( width - 100 )
								/ 2
								+ gc.getLineWidth( )
								/ 2, ( height - 100 ) / 2 + 100 + 1 );

					}
					else if ( info.position.equals( BorderInfomation.BORDER_TOP ) )
					{
						gc.drawLine( ( width - 100 ) / 2,
								( height - 100 ) / 2 + gc.getLineWidth( ) / 2,
								( width - 100 ) / 2 + 100 + 1,
								( height - 100 ) / 2 + gc.getLineWidth( ) / 2 );

					}
					else if ( info.position.equals( BorderInfomation.BORDER_RIGHT ) )
					{
						gc.drawLine( ( width - 100 )
								/ 2
								+ 100
								- gc.getLineWidth( )
								/ 2
								+ 1, ( height - 100 ) / 2, ( width - 100 )
								/ 2
								+ 100
								- gc.getLineWidth( )
								/ 2
								+ 1, ( height - 100 ) / 2 + 100 + 1 );

					}
					else if ( info.position.equals( BorderInfomation.BORDER_BOTTOM ) )
					{
						gc.drawLine( ( width - 100 ) / 2,
								( height - 100 )
										/ 2
										+ 100
										- gc.getLineWidth( )
										/ 2
										+ 1,
								( width - 100 ) / 2 + 100 + 1,
								( height - 100 )
										/ 2
										+ 100
										- gc.getLineWidth( )
										/ 2
										+ 1 );

					}

					gc.setLineWidth( gcInnerWidth );
					if ( info.position.equals( BorderInfomation.BORDER_LEFT ) )
					{
						gc.drawLine( ( width - 100 )
								/ 2
								+ ( gcWidth + gcSeperator )
								+ gc.getLineWidth( )
								/ 2, ( height - 100 )
								/ 2
								+ ( gcWidth + gcSeperator ), ( width - 100 )
								/ 2
								+ ( gcWidth + gcSeperator )
								+ gc.getLineWidth( )
								/ 2, ( height - 100 )
								/ 2
								+ 100
								- ( gcWidth + gcSeperator ) );

					}
					else if ( info.position.equals( BorderInfomation.BORDER_TOP ) )
					{
						gc.drawLine( ( width - 100 )
								/ 2
								+ ( gcWidth + gcSeperator ), ( height - 100 )
								/ 2
								+ ( gcWidth + gcSeperator )
								+ gc.getLineWidth( )
								/ 2, ( width - 100 )
								/ 2
								+ 100
								- ( gcWidth + gcSeperator ), ( height - 100 )
								/ 2
								+ ( gcWidth + gcSeperator )
								+ gc.getLineWidth( )
								/ 2 );

					}
					else if ( info.position.equals( BorderInfomation.BORDER_RIGHT ) )
					{
						gc.drawLine( ( width - 100 )
								/ 2
								+ 100
								- ( gcWidth + gcSeperator )
								- gc.getLineWidth( )
								/ 2, ( height - 100 )
								/ 2
								+ ( gcWidth + gcSeperator ), ( width - 100 )
								/ 2
								+ 100
								- ( gcWidth + gcSeperator )
								- gc.getLineWidth( )
								/ 2, ( height - 100 )
								/ 2
								+ 100
								- ( gcWidth + gcSeperator ) );

					}
					else if ( info.position.equals( BorderInfomation.BORDER_BOTTOM ) )
					{
						gc.drawLine( ( width - 100 )
								/ 2
								+ ( gcWidth + gcSeperator ), ( height - 100 )
								/ 2
								+ 100
								- ( gcWidth + gcSeperator )
								- gc.getLineWidth( )
								/ 2, ( width - 100 )
								/ 2
								+ 100
								- ( gcWidth + gcSeperator ), ( height - 100 )
								/ 2
								+ 100
								- ( gcWidth + gcSeperator )
								- gc.getLineWidth( )
								/ 2 );

					}

				}
				else if ( customWidth == 3
						|| DesignChoiceConstants.LINE_WIDTH_THICK.equals( info.width ) )
				{
					gc.setLineWidth( 1 );

					if ( info.position.equals( BorderInfomation.BORDER_LEFT ) )
					{
						gc.drawLine( ( width - 100 ) / 2,
								( height - 100 ) / 2,
								( width - 100 ) / 2,
								( height - 100 ) / 2 + 100 );

						gc.drawLine( ( width - 100 ) / 2 + 2,
								( height - 100 ) / 2 + 2,
								( width - 100 ) / 2 + 2,
								( height - 100 ) / 2 + 100 - 2 );

					}
					else if ( info.position.equals( BorderInfomation.BORDER_TOP ) )
					{
						gc.drawLine( ( width - 100 ) / 2,
								( height - 100 ) / 2,
								( width - 100 ) / 2 + 100,
								( height - 100 ) / 2 );

						gc.drawLine( ( width - 100 ) / 2 + 2,
								( height - 100 ) / 2 + 2,
								( width - 100 ) / 2 + 100 - 2,
								( height - 100 ) / 2 + 2 );

					}
					else if ( info.position.equals( BorderInfomation.BORDER_RIGHT ) )
					{
						gc.drawLine( ( width - 100 ) / 2 + 100,
								( height - 100 ) / 2,
								( width - 100 ) / 2 + 100,
								( height - 100 ) / 2 + 100 );

						gc.drawLine( ( width - 100 ) / 2 + 100 - 2,
								( height - 100 ) / 2 + 2,
								( width - 100 ) / 2 + 100 - 2,
								( height - 100 ) / 2 + 100 - 2 );

					}
					else if ( info.position.equals( BorderInfomation.BORDER_BOTTOM ) )
					{
						gc.drawLine( ( width - 100 ) / 2,
								( height - 100 ) / 2 + 100,
								( width - 100 ) / 2 + 100,
								( height - 100 ) / 2 + 100 );

						gc.drawLine( ( width - 100 ) / 2 + 2,
								( height - 100 ) / 2 + 100 - 2,
								( width - 100 ) / 2 + 100 - 2,
								( height - 100 ) / 2 + 100 - 2 );

					}
				}
				else
				{
					gc.setLineStyle( SWT.LINE_SOLID );
					gc.setLineWidth( 1 );
					drawLine( gc, width, height, info );
				}
			}
		}
	}

	private void drawLine( GC gc, int width, int height, BorderInfomation info )
	{
		if ( DesignChoiceConstants.LINE_WIDTH_THIN.equals( info.width ) )
			gc.setLineWidth( 1 );
		else if ( DesignChoiceConstants.LINE_WIDTH_MEDIUM.equals( info.width ) )
			gc.setLineWidth( 2 );
		else if ( DesignChoiceConstants.LINE_WIDTH_THICK.equals( info.width ) )
			gc.setLineWidth( 3 );
		else
		{
			try
			{
				if ( info.width != null && !info.width.equals( "" ) ) //$NON-NLS-1$
				{
					int customWidth = (int) DimensionValue.parse( info.width )
							.getMeasure( );
					if ( DimensionValue.parse( info.width )
							.getUnits( )
							.equals( DesignChoiceConstants.UNITS_PX ) )
						gc.setLineWidth( customWidth );
				}
			}
			catch ( Exception e1 )
			{
				ExceptionHandler.handle( e1 );
			}
		}

		if ( info.position.equals( BorderInfomation.BORDER_LEFT ) )
		{
			gc.drawLine( ( width - 100 ) / 2 + gc.getLineWidth( ) / 2,
					( height - 100 ) / 2,
					( width - 100 ) / 2 + gc.getLineWidth( ) / 2,
					( height - 100 ) / 2 + 100 );
		}
		else if ( info.position.equals( BorderInfomation.BORDER_TOP ) )
		{
			gc.drawLine( ( width - 100 ) / 2, ( height - 100 )
					/ 2
					+ gc.getLineWidth( )
					/ 2, ( width - 100 ) / 2 + 100, ( height - 100 )
					/ 2
					+ gc.getLineWidth( )
					/ 2 );
		}
		else if ( info.position.equals( BorderInfomation.BORDER_RIGHT ) )
		{
			gc.drawLine( ( width - 100 ) / 2 + 100 - gc.getLineWidth( ) / 2,
					( height - 100 ) / 2,
					( width - 100 ) / 2 + 100 - gc.getLineWidth( ) / 2,
					( height - 100 ) / 2 + 100 );
		}
		else if ( info.position.equals( BorderInfomation.BORDER_BOTTOM ) )
		{
			gc.drawLine( ( width - 100 ) / 2, ( height - 100 )
					/ 2
					+ 100
					- gc.getLineWidth( )
					/ 2, ( width - 100 ) / 2 + 100, ( height - 100 )
					/ 2
					+ 100
					- gc.getLineWidth( )
					/ 2 );
		}
	}

	public void removeBorderInfomation( String position )
	{
		borderInfoMap.remove( position );
	}

	public Listener getListener( )
	{
		return listener;
	}

	public void setListener( Listener listener )
	{
		this.listener = listener;
	}
}
