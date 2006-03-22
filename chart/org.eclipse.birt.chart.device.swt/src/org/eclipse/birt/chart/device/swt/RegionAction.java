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

package org.eclipse.birt.chart.device.swt;

import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.data.Action;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.widgets.Display;

/**
 * This class provides a region definition and an associated action that is
 * invoked when interaction occurs with a chart rendered on a SWT device.
 */
public final class RegionAction
{

	private final Object _oSource;

	private final Region _sh;

	private final Path _ph;

	private final Action _ac;

	private RegionAction( Object source, Region rg, Path ph, Action ac )
	{
		this._oSource = source;
		this._sh = rg;
		this._ac = ac;
		this._ph = ph;
	}

	/**
	 * This constructor supports polygon shapes Future shapes (and corresponding
	 * constructors) will be added later
	 * 
	 * @param loa
	 * @param ac
	 */
	RegionAction( Object oSource, Location[] loa, Action ac,
			double dTranslateX, double dTranslateY, double dScale,
			Region clipping )
	{
		_oSource = oSource;
		final int[] i2a = SwtRendererImpl.getCoordinatesAsInts( loa,
				SwtRendererImpl.TRUNCATE,
				dTranslateX,
				dTranslateY,
				dScale );
		_sh = new Region( );
		_sh.add( i2a );
		if ( clipping != null )
		{
			_sh.intersect( clipping );
		}
		_ac = ac;
		_ph = null;
	}

	/**
	 * This constructor supports shape definition via a rectangle.
	 * 
	 * @param oSource
	 * @param boEllipse
	 * @param ac
	 */
	RegionAction( Object oSource, Bounds bo, Action ac, double dTranslateX,
			double dTranslateY, double dScale, Region clipping )
	{
		_oSource = oSource;

		bo = BoundsImpl.copyInstance( bo );
		bo.translate( dTranslateX, dTranslateY );
		bo.scale( dScale );

		Rectangle rect = new Rectangle( (int) bo.getLeft( ),
				(int) bo.getTop( ),
				(int) bo.getWidth( ),
				(int) bo.getHeight( ) );

		_sh = new Region( );
		_sh.add( rect );
		if ( clipping != null )
		{
			_sh.intersect( clipping );
		}
		_ac = ac;
		_ph = null;
	}

	/**
	 * This constructor supports shape definition via an elliptical arc
	 * 
	 * @param oSource
	 * @param boEllipse
	 * @param dStart
	 * @param dExtent
	 * @param iArcType
	 * @param ac
	 */
	RegionAction( Object oSource, Bounds boEllipse, double dStart,
			double dExtent, boolean bSector, Action ac, double dTranslateX,
			double dTranslateY, double dScale, Region clipping )
	{
		_oSource = oSource;

		boEllipse = BoundsImpl.copyInstance( boEllipse );
		boEllipse.translate( dTranslateX, dTranslateY );
		boEllipse.scale( dScale );

		double x = boEllipse.getLeft( );
		double y = boEllipse.getTop( );
		double width = boEllipse.getWidth( );
		double height = boEllipse.getHeight( );

		Path ph = new Path( Display.getDefault( ) );
		ph.addArc( (float) x,
				(float) y,
				(float) width,
				(float) height,
				(float) dStart,
				(float) dExtent );

		if ( bSector )
		{
			ph.lineTo( (float) ( x + width / 2 ), (float) ( y + height / 2 ) );
		}

		ph.close( );

		if ( clipping != null )
		{
			// TODO intersect with clipping
		}

		_ph = ph;
		_ac = ac;
		_sh = null;
	}

	/**
	 * This constructor supports polygon shapes Future shapes (and corresponding
	 * constructors) will be added later
	 * 
	 * @param loa
	 * @param ac
	 */
	RegionAction( Object oSource, Location[] loa, Action ac,
			double dTranslateX, double dTranslateY, double dScale )
	{
		this( oSource, loa, ac, dTranslateX, dTranslateY, dScale, null );
	}

	/**
	 * This constructor supports shape definition via a rectangle.
	 * 
	 * @param oSource
	 * @param boEllipse
	 * @param ac
	 */
	RegionAction( Object oSource, Bounds bo, Action ac, double dTranslateX,
			double dTranslateY, double dScale )
	{
		this( oSource, bo, ac, dTranslateX, dTranslateY, dScale, null );
	}

	/**
	 * This constructor supports shape definition via an elliptical arc
	 * 
	 * @param oSource
	 * @param boEllipse
	 * @param dStart
	 * @param dExtent
	 * @param iArcType
	 * @param ac
	 */
	RegionAction( Object oSource, Bounds boEllipse, double dStart,
			double dExtent, boolean bSector, Action ac, double dTranslateX,
			double dTranslateY, double dScale )
	{
		this( oSource,
				boEllipse,
				dStart,
				dExtent,
				bSector,
				ac,
				dTranslateX,
				dTranslateY,
				dScale,
				null );
	}

	/**
	 * Returns the action associated with current ShapedAction.
	 * 
	 * @return
	 */
	public final Action getAction( )
	{
		return _ac;
	}

	/**
	 * Returns the source object associated with current ShapedAction.
	 * 
	 * @return
	 */
	public final Object getSource( )
	{
		return _oSource;
	}

	/**
	 * Returns a copy of current action. Note the Region object is value copied,
	 * others are just reference copy. <b>The invoker must call
	 * <code>dispose()</code> explicitly when this is not used anymore</b>.
	 * 
	 * @return
	 */
	public RegionAction copy( )
	{
		Region nrg = null;
		Path nph = null;

		if ( _sh != null )
		{
			nrg = new Region( );
			nrg.add( _sh );
		}

		if ( _ph != null )
		{
			nph = new Path( Display.getDefault( ) );
			nph.addPath( _ph );
		}

		return new RegionAction( _oSource, nrg, nph, _ac );
	}

	/**
	 * Returns if the current region contains given point.
	 * 
	 * @param p
	 * @param gc
	 * @return
	 */
	public boolean contains( Point p, GC gc )
	{
		if ( _sh != null && !_sh.isDisposed( ) )
		{
			return _sh.contains( p );
		}

		if ( _ph != null && !_ph.isDisposed( ) )
		{
			return _ph.contains( p.x, p.y, gc, false );
		}

		return false;
	}

	/**
	 * Returns if the current region contains given x,y.
	 * 
	 * @param x
	 * @param y
	 * @param gc
	 * @return
	 */
	public boolean contains( double x, double y, GC gc )
	{
		if ( _sh != null && !_sh.isDisposed( ) )
		{
			return _sh.contains( (int) x, (int) y );
		}

		if ( _ph != null && !_ph.isDisposed( ) )
		{
			return _ph.contains( (float) x, (float) y, gc, false );
		}

		return false;
	}

	/**
	 * Dispose the resources.
	 */
	public void dispose( )
	{
		if ( _sh != null && !_sh.isDisposed( ) )
		{
			_sh.dispose( );
		}

		if ( _ph != null && !_ph.isDisposed( ) )
		{
			_ph.dispose( );
		}
	}

	/**
	 * Returns if current region is empty.
	 * 
	 * @return
	 */
	public boolean isEmpty( )
	{
		if ( _sh != null )
		{
			return _sh.isDisposed( ) || _sh.isEmpty( );
		}

		if ( _ph != null )
		{
			return _ph.isDisposed( );
		}

		return true;
	}
}
