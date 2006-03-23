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

package org.eclipse.birt.data.engine.api.querydefn;

import java.util.List;

import org.eclipse.birt.data.engine.api.IJointDataSetDesign;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;

/**
 * This is an implementation of IJointDataSetDesign
 */
public class JointDataSetDesign extends BaseDataSetDesign
		implements
			IJointDataSetDesign
{

	//
	private String left;
	private String right;
	
	/**
	 * The join type of this JointDataSet. May one of inner join, left outer join
	 * and right outer join.
	 */
	private int joinType;
	
	/**
	 * A list of join conditions.
	 */
	private List joinConditions;

	/**
	 * Constructor.
	 * @throws DataException 
	 */
	public JointDataSetDesign( String name, String left,
			String right, int joinType, List joinConditions )
			throws DataException
	{
		super( name );
		validateJoinType( joinType );
		
		this.right = right;
		this.left = left;
		this.joinType = joinType;
		
		this.joinConditions = joinConditions;

	}

	/**
	 * @param joinType
	 * @throws DataException
	 */
	private void validateJoinType( int joinType ) throws DataException
	{
		if( !((joinType == IJointDataSetDesign.INNER_JOIN)||
			  (joinType == IJointDataSetDesign.LEFT_OUTER_JOIN)||
			  (joinType == IJointDataSetDesign.RIGHT_OUTER_JOIN)))
			throw new DataException( ResourceConstants.INVALID_JOIN_TYPE);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IJointDataSetDesign#getLeftDataSetDesign()
	 */
	public String getLeftDataSetDesignName( )
	{
		return left;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IJointDataSetDesign#getRightDataSetDesign()
	 */
	public String getRightDataSetDesignName( )
	{
		return right;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IJointDataSetDesign#getJoinType()
	 */
	public int getJoinType( )
	{
		return joinType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IJointDataSetDesign#getJoinConditions()
	 */
	public List getJoinConditions( )
	{
		return joinConditions;
	}

}
