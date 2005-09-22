package org.eclipse.birt.chart.computation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Location3D;
import org.eclipse.birt.chart.model.attribute.impl.Location3DImpl;
import org.eclipse.birt.chart.model.attribute.impl.LocationImpl;
import org.eclipse.birt.chart.util.Matrix;

public class Object3D
{

	private Vector[] va;

	private Vector center;

	private Vector normal;

	private double zMax;

	private double zMin;

	public Object3D( int points )
	{	
		va = new Vector[ points ];
		
		                  
	}

	public Object3D( Location3D la )
	{
		this( new Location3D[] { la } );
	}
	public Object3D( Location3D[] loa )
	{
		va = new Vector[loa.length];
		for ( int i = 0; i < va.length; i++ )
		{
			va[i] = new Vector( loa[i] );
			//loa[i].linkToVector( va[i] );
		}
	}

	public Object3D( Object3D original )
	{
		if ( original == null )
			return;
		this.va = new Vector[ original.va.length ];
		for ( int i = 0; i < original.va.length; i++ )
		{
			this.va[i] = new Vector( original.va[i] );
		}
		center = original.center;
		normal = original.normal;
		zMax = original.zMax;
		zMin = original.zMin;
	}

	public Location3D[] getLocation3D( )
	{
		Location3D[] loa3d = new Location3D[va.length];
		for ( int i = 0; i < va.length; i++ )
		{
			loa3d[i] = Location3DImpl.create( va[i].get( 0 ), va[i].get( 1 ),
					va[i].get( 2 ) );
		}
		return loa3d;
	}

	/**
	 * returns the normal vector (pointing outside the enclosed volume for
	 * oriented polygons.)
	 * 
	 * @return
	 */
	public Vector getNormal( )
	{
		if ( normal != null )
		{
			return normal;
		}

		if ( va == null )
		{
			return null;
		}

		// create vectors with first three points and returns cross products
		Vector v1 = new Vector( va[1] );
		v1.sub( va[0] );
		Vector v2 = new Vector( va[2] );
		v2.sub( va[1] );

		return v1.crossProduct( v2 );
	}

	/**
	 * Returns center of gravity of polygon
	 * 
	 * @return
	 */
	public Vector getCenter( )
	{
		if ( center == null )
		{
			if ( va == null || va.length == 0 )
			{
				return null;
			}

			double m = va.length;

			center = new Vector();
			
			for ( int i = 0; i < m; i++ )
			{
				center.add( va[i] );
			}
			center.scale( 1d / m );
		}
		return center;
	}

	public void reset( )
	{
		this.center = null;
		this.normal = null;
		this.va = null;
		this.zMax = 0;
		this.zMin = 0;

	}

	public double getZMax( )
	{

		return zMax;
	}

	public double getZMin( )
	{

		return zMin;
	}

	public void transform( Matrix m )
	{
		for ( int i = 0; i < va.length; i++ )
		{
			va[i].multiply( m );
		}
		if ( center != null )
			center.multiply( m );
		if ( normal != null )
			normal.multiply( m );
	}

	private void computeZExtremums( )
	{
		zMin = Double.MAX_VALUE;
		zMax = Double.MIN_VALUE;

		for ( int i = 0; i < va.length; i++ )
		{
			zMin = Math.min( zMin, va[i].get( 2 ) );
			zMax = Math.max( zMax, va[i].get( 2 ) );
		}
	}

	public void clip( Engine3D engine )
	{
		byte retval;

		List lst = new ArrayList();

		switch ( va.length )
		{
		case 0:
			break;
		case 1:
		{
			Vector start = new Vector( va[0] );
			Vector end = new Vector( va[0] );

			retval = engine.checkClipping( start, end );

			if ( retval != Engine3D.OUT_OF_RANGE_BOTH )
			{
				lst.add( start );
			}
		}
			break;
		case 2:
		{
			Vector start = new Vector( va[0] );
			Vector end = new Vector( va[1] );

			retval = engine.checkClipping( start, end );

			if ( retval != Engine3D.OUT_OF_RANGE_BOTH )
			{
				lst.add( start );
				lst.add( end );
			}
		}
			break;

		default:
		{
			for ( int i = 0; i < va.length; i++ )
			{
				Vector start = null;
				Vector end = null;

				if ( i == va.length - 1 )
				{
					start = new Vector( va[i] );
					end = new Vector( va[0] );
				}
				else
				{
					start = new Vector( va[i] );
					end = new Vector( va[i + 1] );
				}

				retval = engine.checkClipping( start, end );

				if ( retval != Engine3D.OUT_OF_RANGE_BOTH )
				{
					lst.add( start );
					lst.add( end );
				}
			}
		}
			break;
		}
		va = (Vector[]) lst.toArray( new Vector[0] );
	}

	/**
	 * Perspective transformation of the vectors.
	 * 
	 * @param distance
	 */

	public void perspective( double distance )
	{
		for ( int i = 0; i < va.length; i++ )
		{
			va[i].perspective( distance );
		}
		if ( center != null )
			center.perspective( distance );
		computeZExtremums();
	}



	public Vector[] getVectors( )
	{
		return va;
	}

	public Location[] getPoints2D( double xOffset, double yOffset )
	{
		Location[] locations = new Location[ va.length ];
		for ( int i = 0; i < va.length; i++ )
		{
			locations[ i ] = LocationImpl.create( va[i].get( 0 ) + xOffset , va[i].get( 1 ) + yOffset );
		}
		return locations;
			
	}



}
