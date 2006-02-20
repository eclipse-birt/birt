/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.examples.api.viewer;

import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.JavaNumberFormatSpecifierImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.core.exception.BirtException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * The selector of charts in SWT.
 * 
 */
public final class SwtChartViewerSelector implements
		PaintListener,
		SelectionListener
{

	private IDeviceRenderer idr = null;

	private Chart cm = null;

	private Combo cb = null;

	private Combo cbDimension = null;

	private Canvas ca = null;

	private Button cbPercent, cbLogarithmic, cbTransposed;

	/**
	 * main() method for constructing the selector layout.
	 * 
	 * @param args
	 */
	public static void main( String[] args )
	{
		SwtChartViewerSelector scv = new SwtChartViewerSelector( );

		GridLayout gl = new GridLayout( );
		// gl.numColumns = 1;
		Display d = Display.getDefault( );
		Shell sh = new Shell( d );
		sh.setSize( 800, 600 );
		sh.setLayout( gl );
		sh.setText( scv.getClass( ).getName( ) + " [device="//$NON-NLS-1$
				+ scv.idr.getClass( ).getName( ) + "]" );//$NON-NLS-1$

		GridData gd = new GridData( GridData.FILL_BOTH );
		Canvas cCenter = new Canvas( sh, SWT.NONE );
		cCenter.setLayoutData( gd );
		cCenter.addPaintListener( scv );

		Composite cBottom = new Composite( sh, SWT.NONE );
		gd = new GridData( GridData.FILL_HORIZONTAL );
		cBottom.setLayoutData( gd );
		cBottom.setLayout( new RowLayout( ) );

		Label la = new Label( cBottom, SWT.NONE );

		la.setText( "Choose: " );//$NON-NLS-1$
		Combo cbType = new Combo( cBottom, SWT.DROP_DOWN | SWT.READ_ONLY );
		cbType.add( "Bar Chart" );//$NON-NLS-1$
		cbType.add( "Bar Chart(2 Series)" );//$NON-NLS-1$
		cbType.add( "Pie Chart" );//$NON-NLS-1$
		cbType.add( "Pie Chart(4 Series)" );//$NON-NLS-1$
		cbType.add( "Line Chart" );//$NON-NLS-1$
		cbType.add( "Bar/Line Stacked Chart" );//$NON-NLS-1$
		cbType.add( "Scatter Chart" );//$NON-NLS-1$
		cbType.add( "Stock Chart" );//$NON-NLS-1$
		cbType.add( "Area Chart" );//$NON-NLS-1$

		cbType.select( 0 );

		Combo cbDimension = new Combo( cBottom, SWT.DROP_DOWN | SWT.READ_ONLY );
		cbDimension.add( "2D" );//$NON-NLS-1$
		cbDimension.add( "2D with Depth" );//$NON-NLS-1$
		cbDimension.select( 0 );

		Button cbTransposed = new Button( cBottom, SWT.CHECK );
		cbTransposed.setText( "Transposed" );//$NON-NLS-1$

		Button cbPercent = new Button( cBottom, SWT.CHECK );
		cbPercent.setText( "Percent" );//$NON-NLS-1$

		Button cbLogarithmic = new Button( cBottom, SWT.CHECK );
		cbLogarithmic.setText( "Logarithmic" );//$NON-NLS-1$

		Button btn = new Button( cBottom, SWT.NONE );
		btn.setText( "Update" );//$NON-NLS-1$
		btn.addSelectionListener( scv );

		scv.cb = cbType;
		scv.ca = cCenter;

		scv.cbDimension = cbDimension;
		scv.cbTransposed = cbTransposed;
		scv.cbPercent = cbPercent;
		scv.cbLogarithmic = cbLogarithmic;

		sh.open( );

		while ( !sh.isDisposed( ) )
		{
			if ( !d.readAndDispatch( ) )
			{
				d.sleep( );
			}
		}
	}

	/**
	 * Get the connection with SWT device to render the graphics.
	 */
	SwtChartViewerSelector( )
	{
		final PluginSettings ps = PluginSettings.instance( );
		try
		{
			idr = ps.getDevice( "dv.SWT" );//$NON-NLS-1$
		}
		catch ( ChartException ex )
		{
			ex.printStackTrace( );
		}
		cm = PrimitiveCharts.createBarChart( );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.PaintListener#paintControl(org.eclipse.swt.events.PaintEvent)
	 */
	public final void paintControl( PaintEvent pe )
	{
		idr.setProperty( IDeviceRenderer.GRAPHICS_CONTEXT, pe.gc );

		Composite co = (Composite) pe.getSource( );
		Rectangle re = co.getClientArea( );
		Bounds bo = BoundsImpl.create( re.x, re.y, re.width, re.height );
		bo.scale( 72d / idr.getDisplayServer( ).getDpiResolution( ) );

		Generator gr = Generator.instance( );
		try
		{
			gr.render( idr, gr.build( idr.getDisplayServer( ),
					cm,
					bo,
					null,
					null,
					null ) );
		}
		catch ( ChartException ex )
		{
			showException( pe.gc, ex );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetSelected( SelectionEvent e )
	{
		int iSelection = cb.getSelectionIndex( );
		switch ( iSelection )
		{
			case 0 :
				cm = PrimitiveCharts.createBarChart( );
				break;
			case 1 :
				cm = PrimitiveCharts.createMultiBarChart( );
				break;
			case 2 :
				cm = PrimitiveCharts.createPieChart( );
				break;
			case 3 :
				cm = PrimitiveCharts.createMultiPieChart( );
				break;
			case 4 :
				cm = PrimitiveCharts.createLineChart( );
				break;
			case 5 :
				cm = PrimitiveCharts.createStackedChart( );
				break;
			case 6 :
				cm = PrimitiveCharts.createScatterChart( );
				break;
			case 7 :
				cm = PrimitiveCharts.createStockChart( );
				break;
			case 8 :
				cm = PrimitiveCharts.createAreaChart( );
				break;
		}

		if ( cm instanceof ChartWithAxes )
		{

			cbTransposed.setEnabled( true );
			cbLogarithmic.setEnabled( true );
			cbPercent.setEnabled( true );

			ChartWithAxes cwa = ( (ChartWithAxes) cm );
			cwa.setTransposed( cbTransposed.getSelection( ) );
			Axis ax = cwa.getPrimaryOrthogonalAxis( cwa.getPrimaryBaseAxes( )[0] );

			if ( cbLogarithmic.getSelection( ) )
			{
				if ( ax.getType( ) == AxisType.LINEAR_LITERAL )
				{
					ax.setType( AxisType.LOGARITHMIC_LITERAL );
				}
			}
			else
			{
				if ( ax.getType( ) == AxisType.LOGARITHMIC_LITERAL )
				{
					ax.setType( AxisType.LINEAR_LITERAL );
				}
			}

			if ( cbPercent.getSelection( ) == true )
			{
				ax.setFormatSpecifier( JavaNumberFormatSpecifierImpl.create( "0'%'" ) );//$NON-NLS-1$
			}
			else
			{
				ax.setFormatSpecifier( null );
			}

		}
		else if ( cm instanceof ChartWithoutAxes )
		{
			cbTransposed.setEnabled( false );
			cbLogarithmic.setEnabled( false );
			cbPercent.setEnabled( false );
		}

		if ( cb.getSelectionIndex( ) == 7 )
		{
			cm.setDimension( ChartDimension.TWO_DIMENSIONAL_LITERAL );
		}
		else
		{
			switch ( cbDimension.getSelectionIndex( ) )
			{

				case 0 :
					cm.setDimension( ChartDimension.TWO_DIMENSIONAL_LITERAL );
					break;
				case 1 :
					cm.setDimension( ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL );
					break;
			}
		}

		ca.redraw( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetDefaultSelected( SelectionEvent e )
	{
		// TODO Auto-generated method stub

	}

	private final void showException( GC g2d, Exception ex )
	{
		String sWrappedException = ex.getClass( ).getName( );
		Throwable th = ex;
		while ( ex.getCause( ) != null )
		{
			ex = (Exception) ex.getCause( );
		}
		String sException = ex.getClass( ).getName( );
		if ( sWrappedException.equals( sException ) )
		{
			sWrappedException = null;
		}

		String sMessage = null;
		if ( th instanceof BirtException )
		{
			sMessage = ( (BirtException) th ).getLocalizedMessage( );
		}
		else
		{
			sMessage = ex.getMessage( );
		}

		if ( sMessage == null )
		{
			sMessage = "<null>";//$NON-NLS-1$
		}
		StackTraceElement[] stea = ex.getStackTrace( );
		Point d = ca.getSize( );

		Device dv = Display.getCurrent( );
		Font fo = new Font( dv, "Courier", SWT.BOLD, 16 );//$NON-NLS-1$
		g2d.setFont( fo );
		FontMetrics fm = g2d.getFontMetrics( );
		g2d.setBackground( dv.getSystemColor( SWT.COLOR_WHITE ) );
		g2d.fillRectangle( 20, 20, d.x - 40, d.y - 40 );
		g2d.setForeground( dv.getSystemColor( SWT.COLOR_BLACK ) );
		g2d.drawRectangle( 20, 20, d.x - 40, d.y - 40 );
		g2d.setClipping( 20, 20, d.x - 40, d.y - 40 );
		int x = 25, y = 20 + fm.getHeight( );
		g2d.drawString( "Exception:", x, y );//$NON-NLS-1$
		x += g2d.textExtent( "Exception:" ).x + 5;//$NON-NLS-1$
		g2d.setForeground( dv.getSystemColor( SWT.COLOR_RED ) );
		g2d.drawString( sException, x, y );
		x = 25;
		y += fm.getHeight( );
		if ( sWrappedException != null )
		{
			g2d.setForeground( dv.getSystemColor( SWT.COLOR_BLACK ) );
			g2d.drawString( "Wrapped In:", x, y );//$NON-NLS-1$
			x += g2d.textExtent( "Wrapped In:" ).x + 5;//$NON-NLS-1$
			g2d.setForeground( dv.getSystemColor( SWT.COLOR_RED ) );
			g2d.drawString( sWrappedException, x, y );
			x = 25;
			y += fm.getHeight( );
		}
		g2d.setForeground( dv.getSystemColor( SWT.COLOR_BLACK ) );
		y += 10;
		g2d.drawString( "Message:", x, y );//$NON-NLS-1$
		x += g2d.textExtent( "Message:" ).x + 5;//$NON-NLS-1$
		g2d.setForeground( dv.getSystemColor( SWT.COLOR_BLUE ) );
		g2d.drawString( sMessage, x, y );
		x = 25;
		y += fm.getHeight( );
		g2d.setForeground( dv.getSystemColor( SWT.COLOR_BLACK ) );
		y += 10;
		g2d.drawString( "Trace:", x, y );//$NON-NLS-1$
		x = 40;
		y += fm.getHeight( );
		g2d.setForeground( dv.getSystemColor( SWT.COLOR_DARK_GREEN ) );
		for ( int i = 0; i < stea.length; i++ )
		{
			g2d.drawString( stea[i].getClassName( ) + ":"//$NON-NLS-1$
					+ stea[i].getMethodName( ) + "(...):"//$NON-NLS-1$
					+ stea[i].getLineNumber( ), x, y );
			x = 40;
			y += fm.getHeight( );
		}
		fo.dispose( );
	}
}