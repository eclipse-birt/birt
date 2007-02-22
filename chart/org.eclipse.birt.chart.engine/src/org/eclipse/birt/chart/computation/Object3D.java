
package org.eclipse.birt.chart.computation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.chart.internal.computations.Matrix;
import org.eclipse.birt.chart.internal.computations.Polygon;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Location3D;
import org.eclipse.birt.chart.model.attribute.impl.Location3DImpl;
import org.eclipse.birt.chart.model.attribute.impl.LocationImpl;
import org.eclipse.birt.chart.util.ChartUtil;

/**
 * This class represents a 3D object and is used to perform all 3d operations.
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

	private int iZmin;

	private int iZmax;

	/**
	 * Construction by an empty array of coordinates
	 */
	public Object3D( int points )
	{
		va = new Vector[points];
	}

	/**
	 * Construction by one 3d coordinate
	 */
	public Object3D( Location3D la )
	{
		this( new Location3D[]{
			la
		} );
	}

	/**
	 * Construction by an array of 3d coordinates
	 * @param the points that constitue the 3D object. If there are more than 2 points
	 * the order of points decides the orientation of the surface.
	 * Only the outside face is painted, unless the object is double-sided. Note that
	 * polygons making a volume should not be double-sided.
	 */
	public Object3D( Location3D[] loa )
	{
		this( loa, false );
	}

	/**
	 * Construction by an array of 3d coordinates
	 * @param inverted: invert the order of points to change the surface orientation
	 */
	public Object3D( Location3D[] loa, boolean inverted )
	{

		va = new Vector[loa.length];
		for ( int i = 0; i < va.length; i++ )
		{
			if ( !inverted )
			{
				va[i] = new Vector( loa[i] );
				loa[i].linkToVector( va[i] );
			}
			else
			{
				va[ va.length - 1 - i ] = new Vector( loa[i] );
				loa[i].linkToVector( va[ va.length - 1 -i ] );
			}
		}
		
	}
	/**
	 * Construction by another Object3D object
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
		iZmin = 0;
		iZmax = 0;
	}

	/**
	 * Returns the 3d coordinates for this object.
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
			v2.sub( va[0] );

			normal = v1.crossProduct( v2 );
		}

		return normal;
	}

	/**
	 * Returns center of gravity of polygon
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
		this.iZmin = 0;
		this.iZmax = 0;
	}

	/*
	 * Returns the point with the fursthest z
	 */
	public Vector getZMaxPoint( )
	{
		return va[iZmax];
	}
	/*
	 * Returns the point with the nearest z
	 */
	
	public Vector getZMinPoint( )
	{
		return va[iZmin];
	}
	
	/**
	 * Returns maximum X value for this object
	 */
	public double getXMax( )
	{
		return xMax;
	}

	/**
	 * Returns minimum X value for this object
	 */
	public double getXMin( )
	{
		return xMin;
	}

	/**
	 * Returns maximum Y value for this object
	 */
	public double getYMax( )
	{
		return yMax;
	}

	/**
	 * Returns minimum Y value for this object
	 */
	public double getYMin( )
	{
		return yMin;
	}

	/**
	 * Returns maximum Z value for this object
	 */
	public double getZMax( )
	{
		return zMax;
	}

	/**
	 * Returns minimum Z value for this object
	 */
	public double getZMin( )
	{
		return zMin;
	}

	/**
	 * Performs transformation by given matrix
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

			
			if ( zMin > va[i].get( 2 ) )
			{
				zMin = va[i].get( 2 );
				iZmin = i;
			}
			if ( zMax < va[i].get( 2 ) )
			{
				zMax = va[i].get( 2 );
				iZmax = i;
			}
			
			
		}
	}

	/**
	 * Check and clip vectors by given engine.
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
				boolean endClipped = false;

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
						if ( i == 0
								|| ( retval & Engine3D.OUT_OF_RANGE_START ) != 0
								|| endClipped )
						{
							lst.add( start );
						}

						endClipped = false;

						if ( ( retval & Engine3D.OUT_OF_RANGE_END ) != 0 )
						{
							endClipped = true;
						}

						if ( i != va.length - 1 || endClipped )
						{
							lst.add( end );
						}

					}
				}
			}
				break;
		}
		va = (Vector[]) lst.toArray( new Vector[lst.size( )] );
	}

	/**
	 * Prepars for Z-sorting
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
	 * Returns vectors in model frame for this object
	 */
	public Vector[] getVectors( )
	{
		return va;
	}

	/**
	 * Returns vectors in viewer frame for this object
	 */
	public Vector[] getViewerVectors( )
	{
		return viewVa;
	}

	/**
	 * Returns the projected 2D coordinates for this object
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
	 * Returns if current object is totally aside the given object. "outside" is
	 * along the direction of the viewer vector.
	 */
	protected boolean testAside( Object3D comparedObj, boolean outside, Engine3D engine )
	{
		int thisPointsNumber = viewVa.length;
		int comparedPointsNumber = comparedObj.getViewerVectors( ).length ;
		Vector[] thisPoints = viewVa;
		Vector[] comparedPoints = comparedObj.getViewerVectors( );
		
		if ( thisPointsNumber == 0 || comparedPointsNumber == 0 )
		{
			// skip empty object.
			return true;
		}

		if ( thisPointsNumber < 3 && comparedPointsNumber < 3 )
		{
			//  test two lines or point in a line.
			return true;
		}

		
		Vector normal = null;
		Vector ov = viewVa[0];
		
		
		double d = 0;
		if ( thisPointsNumber < 3 || comparedPointsNumber < 3 )
		{
			return true;
			/*
			if ( thisPointsNumber >=3 && comparedPointsNumber == 2 )
			{
				return false;// this=line other=polygon. Return false to trigger next test which is enough.
			}
			
			if ( thisPointsNumber == 2 && comparedPointsNumber >= 3)
			{
			
				// Case: Line could obscure polygon or not
				
				Vector lineDirection = new Vector( thisPoints[1] );
				lineDirection.sub(  thisPoints[0] );
				//lineDirection.normalize();
				Vector viewDirection = this.getCenter( );
				
				Vector nearPoint = thisPoints[0];
				Vector farPoint = thisPoints[1];
				if ( farPoint.get( 2 ) < nearPoint.get( 2 ) )
				{
					lineDirection.inverse( );
					farPoint = thisPoints[0];
					nearPoint = thisPoints[1];
				}
				// Case line parallel to plan, easy case, use the parallel plane
				if ( ChartUtil.mathEqual( lineDirection.scalarProduct( comparedObj.getNormal( ) ), 0 ) )
				{

					normal = new Vector( comparedObj.getNormal() );
					d = -normal.scalarProduct( ov );
					
					return testPolygon( normal, d, comparedObj, outside );
				}
				else
				{
					// look for intersection point
					
					Vector N = comparedObj.getNormal( );
					
					if ( N.scalarProduct( viewDirection  ) < 0 )
						N.inverse( );
					Vector planPoint = new Vector( comparedPoints[0] );
					
					// compute u = N dot(planPoint-nearPoint)/ N dot( farPoint-nearPoint)
					// that gives P = nearPoint + u  (farPoint-nearPoint) - intersection line plan.
					
					
					double u = N.scalarProduct( planPoint.getSub( nearPoint ) ) / N.scalarProduct( farPoint.getSub( nearPoint ) );
					Vector intersectionPoint = new Vector( farPoint );
					intersectionPoint.sub( nearPoint );
					intersectionPoint.scale( u );
					intersectionPoint.add( nearPoint );
					
					// u tells where is the intersection point in the line.
					// if it's outside the segment, near the viewer : <0
					// if it's in the segment: 0<=u<=1)
					// if it's outside the segment, far from the viewer: >1
					
						if ( u > 1 )
							return !outside;
						else if ( u < 0 )
							return outside;
						else 
						{
							Object3D nearSegment = new Object3D( 2 );
							nearSegment.getVectors( )[0] = nearPoint;
							nearSegment.getVectors( )[1] = intersectionPoint;
							nearSegment.prepareZSort( );
							//boolean intersect = ( nearSegment.testXOverlap( comparedObj ) && nearSegment.testYOverlap( comparedObj ));
							boolean intersect = nearSegment.testIntersect(comparedObj, engine );
							if ( intersect )
								return !outside;
							else
								return outside;
							
						}
					
					
						
				}
					
			}
			else
			{
				// line/line
				return true;
			}
			*/
			
		}
		else
		{
			// case: polygon obscures polygon
		
			normal = new Vector( getNormal( ) );
			// 	necessary for the plan equation ax+by+cz+d=0	
			d = -normal.scalarProduct( ov );
			
			return testPolygon( normal, d, comparedObj, outside );
		}
	
	}
		
	protected boolean testPolygon( Vector normal, double d, Object3D obj,
			boolean outside )
	{
		// Tests if a polygon is inside or outside another one, based on the
		// viewing direction

		Vector[] tva = obj.getViewerVectors( );
		Vector viewDirection = obj.getCenter( ).getNormalized( );
		// check if the normal vector of face points to the same direction
		// of the viewing direction

		if ( normal.getNormalized( ).scalarProduct( viewDirection ) < 0 )
		{
			normal.inverse( );
			d = -d;
		}
		boolean sameSide = false;
		if (ChartUtil.mathEqual( normal.scalarProduct( viewDirection ), 0 ) )
			sameSide = true; // as long as all the points are all on the same side, it's good. there is no outside/inside concept
		
		double oldP = 0;
		for ( int i = 0; i < tva.length; i++ )
		{
			double p = tva[i].scalarProduct( normal ) + d;
			if ( sameSide )
			{
				if (oldP * p < 0 )
					return false;
				oldP = p;
			}
			else
			if ( !outside )
			{
				if ( ChartUtil.mathLT( p, 0 ) )
				{
					return false;
				}
			}
			else
			{
				if ( ChartUtil.mathGT( p, 0 ) )
				{
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Tests if two objects intersects.
	 */
	protected boolean testIntersect( Object3D near, Engine3D engine )
	{
		Vector[] va1 = getViewerVectors( );
		Vector[] va2 = near.getViewerVectors( );

		Vector v;
		Polygon p1 = new Polygon( );
		for ( int i = 0; i < va1.length; i++ )
		{
			v = engine.perspective( va1[i] );
			v = engine.view2Canvas( v );
			p1.add( v.get( 0 ), v.get( 1 ) );
		}

		Polygon p2 = new Polygon( );
		for ( int i = 0; i < va2.length; i++ )
		{
			v = engine.perspective( va2[i] );
			v = engine.view2Canvas( v );
			p2.add( v.get( 0 ), v.get( 1 ) );
		}

		return p1.intersects( p2 );
	}

	/**
	 * Tests if two objects overlap in X diretion.
	 */
	protected boolean testXOverlap( Object3D near )
	{
		return ( ( this.getXMax( ) > near.getXMax( ) && this.getXMin( ) < near.getXMax( ) ) || ( this.getXMax( ) < near.getXMax( ) && this.getXMax( ) > near.getXMin( ) ) );
	}

	/**
	 * Tests if two objects overlap in Y diretion.
	 */
	protected boolean testYOverlap( Object3D near )
	{
		return ( ( this.getYMax( ) > near.getYMax( ) && this.getYMin( ) < near.getYMax( ) ) || ( this.getYMax( ) < near.getYMax( ) && this.getYMax( ) > near.getYMin( ) ) );
	}

	/**
	 * Tests if two objects need swapping.
	 */
	public boolean testSwap( Object3D near, Engine3D engine )
	{
		Object3D far = this;
		/*if ( this.getZMax( ) < near.getZMax( ) )
		{
			far = near;
			near = this;
		}*/
		boolean swap = false;
		if ( far.testXOverlap( near ) && far.testYOverlap( near ) )
		{
			if ( !( far.testAside( near, true, engine ) ) )
			{
				if ( !( near.testAside( far, false, engine ) ) )
				{
					if ( far.testIntersect( near, engine ) )
					{
						swap = true;
					}
				}
			}
		}
		return swap;
	}

	/**
	 * Tests if two objects overlap in Z diretion.
	 */
	public boolean testZOverlap( Object3D near )
	{
		return ( ( this.getZMax( ) >= near.getZMax( ) && this.getZMin( ) < near.getZMax( ) ) || ( this.getZMax( ) < near.getZMax( ) && this.getZMax( ) > near.getZMin( ) ) );
	}
}
