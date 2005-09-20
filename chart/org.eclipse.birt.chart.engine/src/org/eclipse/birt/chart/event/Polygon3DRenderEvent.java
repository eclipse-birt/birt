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

package org.eclipse.birt.chart.event;

import java.util.Locale;
import java.util.ResourceBundle;

import org.eclipse.birt.chart.computation.Vector;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Location3D;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Polygon3DRenderEvent
 */
public final class Polygon3DRenderEvent extends PolygonRenderEvent implements
		I3DRenderEvent
{

	private static final long serialVersionUID = -6572679563207168795L;

	private boolean bDoubleSided = false;

	private double dBrightness = 1d;

	private boolean bBehind = false;

	private Location3D[] loa3d;

	private Fill runtimeBackground;

	private Vector center;

	/**
	 * @param oSource
	 */
	public Polygon3DRenderEvent( Object oSource )
	{
		super( oSource );
	}

	/**
	 * Returns true if double sided polygons (not enclosing a volume)
	 * 
	 * @return
	 */
	public boolean isDoubleSided( )
	{
		return bDoubleSided;
	}

	/**
	 * @param value
	 */
	public void setDoubleSided( boolean value )
	{
		this.bDoubleSided = value;
	}

	/**
	 * @return
	 */
	public boolean isBehind( )
	{
		return bBehind;
	}

	/**
	 * @param value
	 */
	public void setBehind( boolean value )
	{
		this.bBehind = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.PolygonRenderEvent#setBackground(org.eclipse.birt.chart.model.attribute.Fill)
	 */
	public void setBackground( Fill ifBackground )
	{
		super.setBackground( ifBackground );

		runtimeBackground = ifBackground;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.PolygonRenderEvent#getBackground()
	 */
	public Fill getBackground( )
	{
		return runtimeBackground;
	}

	/**
	 * returns the normal vector (pointing outside the enclosed volume for
	 * oriented polygons.)
	 * 
	 * @return
	 */
	public Vector getNormal( )
	{
		if ( loa3d == null )
		{
			return null;
		}

		// create vectors with first three points and returns cross products
		Vector v1 = new Vector( loa3d[0], loa3d[1] );
		Vector v2 = new Vector( loa3d[1], loa3d[2] );

		return v1.crossProduct( v2 );
	}

	/**
	 * @param va
	 */
	public void updateCenter( Vector[] va )
	{
		if ( va == null || va.length == 0 )
		{
			return;
		}

		double m = va.length;
		double xs = 0, ys = 0, zs = 0;

		for ( int i = 0; i < m; i++ )
		{
			xs += va[i].get( 0 );
			ys += va[i].get( 1 );
			zs += va[i].get( 2 );
		}

		center = new Vector( xs / m, ys / m, zs / m, true );
	}

	/**
	 * Returns center of gravity of polygon
	 * 
	 * @return
	 */
	public Vector getCenter( )
	{
		if ( center != null )
		{
			return center;
		}

		if ( loa3d == null || loa3d.length == 0 )
		{
			return null;
		}

		double m = loa3d.length;
		double xs = 0, ys = 0, zs = 0;

		for ( int i = 0; i < m; i++ )
		{
			xs += loa3d[i].getX( );
			ys += loa3d[i].getY( );
			zs += loa3d[i].getZ( );
		}

		return new Vector( xs / m, ys / m, zs / m, true );
	}

	/**
	 * @return
	 */
	public double getBrightness( )
	{
		return dBrightness;
	}

	/**
	 * Sets the brightness of this polygon, the value ranges 0.0 - 1.0.
	 */
	public void setBrightness( double value )
	{
		dBrightness = value;

		if ( _ifBackground instanceof ColorDefinition )
		{
			ColorDefinition cdf = (ColorDefinition) EcoreUtil.copy( _ifBackground );

			cdf.set( (int) ( cdf.getRed( ) * dBrightness ),
					(int) ( cdf.getGreen( ) * dBrightness ),
					(int) ( cdf.getBlue( ) * dBrightness ) );

			runtimeBackground = cdf;
		}
	}

	/**
	 * Note that setPoints3D must be called with the points in the right order:
	 * that is needed for the right orientation of the polygon. Points must be
	 * given in anti-clockwise order if looking at the face from outside the
	 * enclosed volume, and so that two adjacent points define a line of the
	 * polygon. A minimum of three points is required, less will throw an
	 * IllegalArgumentException, three consecutive points cannot be aligned.
	 * 
	 * @param la
	 *            Sets the co-ordinates for each point that defines the polygon
	 */
	public final void setPoints3D( Location3D[] la ) throws ChartException
	{
		loa3d = la;

		if ( la.length < 3 )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.RENDERING,
					"exception.3D.points.length.less.than.3", //$NON-NLS-1$
					ResourceBundle.getBundle( Messages.ENGINE,
							Locale.getDefault( ) ) );
		}
	}

	/**
	 * 
	 * @return Returns the co-ordinates for each point in the polygon
	 */
	public final Location3D[] getPoints3D( )
	{
		return loa3d;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.PrimitiveRenderEvent#copy()
	 */
	public PrimitiveRenderEvent copy( )
	{
		final Polygon3DRenderEvent pre = new Polygon3DRenderEvent( source );
		if ( loa3d != null )
		{
			final Location3D[] loa = new Location3D[this.loa3d.length];
			for ( int i = 0; i < loa.length; i++ )
			{
				loa[i] = (Location3D) EcoreUtil.copy( loa3d[i] );
			}
			pre.loa3d = loa;
		}

		if ( _lia != null )
		{
			pre.setOutline( (LineAttributes) EcoreUtil.copy( _lia ) );
		}

		if ( _ifBackground != null )
		{
			pre.setBackground( (Fill) EcoreUtil.copy( _ifBackground ) );
		}

		pre.bDoubleSided = bDoubleSided;
		pre.dBrightness = dBrightness;
		pre.bBehind = bBehind;

		return pre;
	}

}
