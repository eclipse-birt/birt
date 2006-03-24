/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.impl.jointdataset;

import org.eclipse.birt.data.engine.api.IJointDataSetDesign;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.ResultObject;
import org.eclipse.birt.data.engine.odi.IDataSetPopulator;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.eclipse.birt.data.engine.odi.IResultObject;


/**
 * An implementation of IJointDataSetPopulator. It encapsulates the general algorithm
 * to deal with inner, left outer, and right outer joins.The right outer join is actually
 * treated as left out join in this class, except that when dealing with right out join 
 * the primaryIterator would be set to the right Iterator rather than left one.
 */
public class BaseJointDataSetPopulator implements IDataSetPopulator
{
	//
	private JointResultMetadata meta;
	
	private IResultIterator primaryIterator;
	private IResultIterator secondaryIterator;
	private int joinType;
	private int lastPopulatePrimaryIndex;
	private IMatchResultObjectSeeker seeker;
	
	/**
	 * Constructor.
	 * 
	 * @param left
	 * @param right
	 * @param meta
	 * @param jcm
	 * @param joinType
	 * @param seeker
	 * @throws DataException
	 */
	public BaseJointDataSetPopulator ( IResultIterator left, IResultIterator right, JointResultMetadata meta, IJoinConditionMatcher jcm , int joinType, IMatchResultObjectSeeker seeker) throws DataException
	{
		this.meta = meta;
		this.joinType = joinType;
		this.lastPopulatePrimaryIndex = -1;
		
		if( this.joinType != IJointDataSetDesign.RIGHT_OUTER_JOIN )
		{
			this.primaryIterator = left;
			this.secondaryIterator = right;
		}else
		{
			this.primaryIterator = right;
			this.secondaryIterator = left;
		}
		
		this.seeker = seeker;
		
		this.seeker.setResultIterator( this.secondaryIterator );
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.impl.jointdataset.IJointDataSetPopulator#next()
	 */
	public IResultObject next( ) throws DataException
	{
		do
		{
			IResultObject primary = primaryIterator.getCurrentResult( );
			if( primary == null )
				break;
			
			IResultObject secondary = seeker.getNextMatchedResultObject( primaryIterator.getCurrentResultIndex( ) );
			if( secondary == null )
			{
				secondaryIterator.first( 0 );
				if ( (joinType != IJointDataSetDesign.INNER_JOIN )&& primaryIterator.getCurrentResultIndex( ) != lastPopulatePrimaryIndex )
				{
					IResultObject result = createResultObject( primary, secondary );
					primaryIterator.next( );
					return result;
				}
			}
			else
			{
				lastPopulatePrimaryIndex = primaryIterator.getCurrentResultIndex( );
				return createResultObject( primary, secondary );
			}	
		} while ( primaryIterator.next( ) );
		
		//Return null means there is no more rows.
		return null;
	}
	
	/**
	 * Create an instance of IResultObject.
	 * 
	 * @param primary
	 * @param secondary
	 * @return
	 * @throws DataException
	 */
	private IResultObject createResultObject( IResultObject primary, IResultObject secondary ) throws DataException
	{
		Object[] fields = new Object[meta.getResultClass( ).getFieldCount( )];
		for( int i = 1; i <= fields.length; i++ )
		{
			IResultObject ri = null;
			
			if( meta.getColumnSource( i ) == JointResultMetadata.COLUMN_TYPE_LEFT)
			{
				if( joinType!=IJointDataSetDesign.RIGHT_OUTER_JOIN)
					ri = primary;
				else
					ri = secondary;
			}else if( meta.getColumnSource( i ) == JointResultMetadata.COLUMN_TYPE_RIGHT)
			{
				if( joinType!=IJointDataSetDesign.RIGHT_OUTER_JOIN)
					ri = secondary;
				else
					ri = primary;
			}
			
			fields[i-1] = ri== null?null:ri.getFieldValue( meta.getSourceIndex( i ) );
		}
		return new ResultObject( meta.getResultClass( ), fields);
	}
		
}
