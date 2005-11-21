
package org.eclipse.birt.chart.computation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Location3D;
import org.eclipse.birt.chart.model.attribute.impl.Location3DImpl;
import org.eclipse.birt.chart.model.attribute.impl.LocationImpl;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.chart.util.Matrix;

/**
 * Object3D
 */
public class Object3D
{

	private Vector[] va;

	private Vector[] viewVa;

	private Vector center;

	private Vector normal;

	private double xMax, xMin;

	private double yMax, yMin;

	private double zMax, zMin;

	/**
	 * @param points
	 */
	public Object3D( int points )
	{
		va = new Vector[points];
	}

	/**
	 * @param la
	 */
	public Object3D( Location3D la )
	{
		this( new Location3D[]{
			la
		} );
	}

	/**
	 * @param loa
	 */
	public Object3D( Location3D[] loa )
	{
		va = new Vector[loa.length];
		for ( int i = 0; i < va.length; i++ )
		{
			va[i] = new Vector( loa[i] );
			loa[i].linkToVector( va[i] );
		}
	}

	/**
	 * @param original
	 */
	public Object3D( Object3D original )
	{
		if ( original == null )
		{
			return;
		}

		this.va = new Vector[original.va.length];
		for ( int i = 0; i < original.va.length; i++ )
		{
			this.va[i] = new Vector( original.va[i] );
		}
		center = original.center;
		normal = original.normal;
		zMax = original.zMax;
		zMin = original.zMin;
		yMax = original.yMax;
		yMin = original.yMin;
		xMax = original.xMax;
		xMin = original.xMin;
	}

	/**
	 * @return
	 */
	public Location3D[] getLocation3D( )
	{
		Location3D[] loa3d = new Location3D[va.length];
		for ( int i = 0; i < va.length; i++ )
		{
			loa3d[i] = Location3DImpl.create( va[i].get( 0 ),
					va[i].get( 1 ),
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
		if ( normal == null )
		{
			if ( va == null || va.length < 3 )
			{
				return null;
			}

			// create vectors with first three points and returns cross products
			Vector v1 = new Vector( va[1] );
			v1.sub( va[0] );
			Vector v2 = new Vector( va[2] );
			v2.sub( va[1] );

			normal = v1.crossProduct( v2 );
		}

		return normal;
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

			center = new Vector( );

			for ( int i = 0; i < m; i++ )
			{
				center.add( va[i] );
			}
			center.scale( 1d / m );
		}
		return center;
	}

	/**
	 * Resets all values to defaults.
	 */
	public void reset( )
	{
		this.center = null;
		this.normal = null;
		this.va = null;
		this.viewVa = null;
		this.zMax = 0;
		this.zMin = 0;
		this.yMax = 0;
		this.yMin = 0;
		this.xMax = 0;
		this.xMin = 0;
	}

	/**
	 * @return
	 */
	public double getXMax( )
	{
		return xMax;
	}

	/**
	 * @return
	 */
	public double getXMin( )
	{
		return xMin;
	}

	/**
	 * @return
	 */
	public double getYMax( )
	{
		return yMax;
	}

	/**
	 * @return
	 */
	public double getYMin( )
	{
		return yMin;
	}

	/**
	 * @return
	 */
	public double getZMax( )
	{
		return zMax;
	}

	/**
	 * @return
	 */
	public double getZMin( )
	{
		return zMin;
	}

	/**
	 * @param m
	 */
	public void transform( Matrix m )
	{
		for ( int i = 0; i < va.length; i++ )
		{
			va[i].multiply( m );
		}
		if ( center != null )
		{
			// center.multiply( m );
		}
		if ( normal != null )
		{
			// normal.multiply( m );
		}
	}

	/**
	 * 
	 */
	private void computeExtremums( )
	{
		xMin = Double.MAX_VALUE;
		xMax = -Double.MAX_VALUE;

		yMin = Double.MAX_VALUE;
		yMax = -Double.MAX_VALUE;

		zMin = Double.MAX_VALUE;
		zMax = -Double.MAX_VALUE;

		for ( int i = 0; i < va.length; i++ )
		{
			xMin = Math.min( xMin, va[i].get( 0 ) );
			xMax = Math.max( xMax, va[i].get( 0 ) );

			yMin = Math.min( yMin, va[i].get( 1 ) );
			yMax = Math.max( yMax, va[i].get( 1 ) );

			zMin = Math.min( zMin, va[i].get( 2 ) );
			zMax = Math.max( zMax, va[i].get( 2 ) );
		}
	}

	/**
	 * @param engine
	 */
	public void clip( Engine3D engine )
	{
		byte retval;

		List lst = new ArrayList( );

		switch ( va.length )
		{
			case 0 :
				break;
			case 1 :
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
			case 2 :
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

			default :
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
	 * 
	 */
	public void prepareZSort( )
	{
		computeExtremums( );
		getNormal( );
		getCenter( );

		viewVa = new Vector[va.length];
		for ( int i = 0; i < va.length; i++ )
		{
			viewVa[i] = new Vector( va[i] );
		}

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
		{
			// center.perspective( distance );
		}
		// computeExtremums( );
	}

	/**
	 * @return
	 */
	public Vector[] getVectors( )
	{
		return va;
	}

	/**
	 * @return
	 */
	public Vector[] getViewerVectors( )
	{
		return viewVa;
	}

	/**
	 * @param xOffset
	 * @param yOffset
	 * @return
	 */
	public Location[] getPoints2D( double xOffset, double yOffset )
	{
		Location[] locations = new Location[va.length];
		for ( int i = 0; i < va.length; i++ )
		{
			locations[i] = LocationImpl.create( va[i].get( 0 ) + xOffset,
					va[i].get( 1 ) + yOffset );
		}
		return locations;
	}

	/**
	 * @param obj
	 * @return
	 */
	protected boolean testAside( Object3D obj, boolean outside )
	{
		if ( viewVa.length < 3 && obj.getViewerVectors( ).length < 3 )
		{
			return true;
		}

		Vector normal = null;
		Vector ov = viewVa[0];
		Vector[] tva = obj.getViewerVectors( );
		Vector viewDirection = new Vector( 0, 0, 1 );

		if ( viewVa.length < 3 )
		{
			normal = new Vector( obj.getNormal( ) );
			outside = !outside;
			ov = tva[0];
			tva = getViewerVectors( );
			// viewDirection = obj.getCenter( );
		}
		else
		{
			normal = new Vector( getNormal( ) );
		}

		// check if the normal vector of face points to the same direction
		// of the viewing direction
		if ( normal.scalarProduct( viewDirection ) <= 0 )
		{
			normal.inverse( );
		}

		double d = -normal.get( 0 )
				* ov.get( 0 )
				- normal.get( 1 )
				* ov.get( 1 )
				- normal.get( 2 )
				* ov.get( 2 );

		for ( int i = 0; i < tva.length; i++ )
		{
			if ( outside )
			{
				double p = tva[i].get( 0 )
						* normal.get( 0 )
						+ tva[i].get( 1 )
						* normal.get( 1 )
						+ tva[i].get( 2 )
						* normal.get( 2 )
						+ d;
				if ( ChartUtil.mathLT( p, 0 ) )
				{
					return false;
				}
			}
			else
			{
				double p = tva[i].get( 0 )
						* normal.get( 0 )
						+ tva[i].get( 1 )
						* normal.get( 1 )
						+ tva[i].get( 2 )
						* normal.get( 2 )
						+ d;
				if ( ChartUtil.mathGT( p, 0 ) )
				{
					return false;
				}
			}
		}

		return true;
	}

	Location[] vectors2locations( Vector[] va )
	{
		if ( va != null )
		{
			Location[] loa = new Location[va.length];

			for ( int i = 0; i < va.length; i++ )
			{
				loa[i] = LocationImpl.create( va[i].get( 0 ), va[i].get( 1 ) );
			}

			return loa;
		}
		return null;
	}

	/**
	 * @param near
	 * @return
	 */
	protected boolean testIntersect( Object3D near )
	{
		// Vector[] va1 = getViewerVectors( );
		// Vector[] va2 = near.getViewerVectors( );
		//
		// Polygon p1 = PolygonImpl.create( vectors2locations( va1 ) );
		// Polygon p2 = PolygonImpl.create( vectors2locations( va2 ) );

		// return p1.intersects( p2 );

		// Temporarily disable this check, because it may cause an infinite loop
		// occasionally.
		return false;
	}

	/**
	 * @param near
	 * @return
	 */
	protected boolean testXOverlap( Object3D near )
	{
		return ( ( this.getXMax( ) >= near.getXMax( ) && this.getXMin( ) < near.getXMax( ) ) || ( this.getXMax( ) < near.getXMax( ) && this.getXMax( ) > near.getXMin( ) ) );
	}

	/**
	 * @param near
	 * @return
	 */
	protected boolean testYOverlap( Object3D near )
	{
		return ( ( this.getYMax( ) >= near.getYMax( ) && this.getYMin( ) < near.getYMax( ) ) || ( this.getYMax( ) < near.getYMax( ) && this.getYMax( ) > near.getYMin( ) ) );
	}

	/**
	 * @param near
	 * @return
	 */
	public boolean testSwap( Object3D near )
	{
		Object3D far = this;
		boolean swap = false;
		if ( far.testXOverlap( near ) && far.testYOverlap( near ) )
		{
			if ( !( near.testAside( far, true ) ) )
			{
				if ( !( far.testAside( near, false ) ) )
				{
					if ( far.testIntersect( near ) )
					{
						swap = true;
					}
				}
			}
		}
		return swap;
	}

	/**
	 * @param near
	 * @return
	 */
	public boolean testZOverlap( Object3D near )
	{
		return ( near.getZMax( ) >= this.getZMin( ) );
	}
}
