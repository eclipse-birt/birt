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

package org.eclipse.birt.data.engine.olap.data.impl.dimension;

import java.io.IOException;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.cube.IDimension;
import org.eclipse.birt.data.engine.olap.api.cube.IHierarchy;
import org.eclipse.birt.data.engine.olap.data.api.ISelection;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentManager;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentObject;
import org.eclipse.birt.data.engine.olap.data.impl.Constants;
import org.eclipse.birt.data.engine.olap.data.impl.NamingUtil;
import org.eclipse.birt.data.engine.olap.data.util.BufferedPrimitiveDiskArray;
import org.eclipse.birt.data.engine.olap.data.util.BufferedStructureArray;
import org.eclipse.birt.data.engine.olap.data.util.IDiskArray;
import org.eclipse.birt.data.engine.olap.data.util.IndexKey;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;

/**
 * Describes a dimension. In current implement a dimension only contains one hierarchy.
 */

public class Dimension implements IDimension
{
	
	private String name = null;
	private IDocumentManager documentManager = null;
	private IDocumentObject documentObj = null;
	private Hierarchy hierarchy = null;
	private int length = 0;
	private boolean isTime;

	/**
	 * 
	 * @param name
	 * @param documentManager
	 * @param hierarchy
	 * @param isTime
	 * @throws BirtException
	 * @throws IOException
	 */
	public Dimension( String name, IDocumentManager documentManager,
			 Hierarchy hierarchy, boolean isTime )
			throws DataException, IOException
	{
		this.name = name;
		this.documentManager = documentManager;
		this.isTime = isTime;
		documentManager.createDocumentObject( NamingUtil.getDimensionDocName( name ) );
		documentObj = documentManager.openDocumentObject( NamingUtil.getDimensionDocName( name ) );
		documentObj.writeBoolean( isTime );
		
		this.hierarchy =  (Hierarchy)hierarchy;
		length = hierarchy.size( );
		// close document object
		documentObj.close( );
		documentObj = null;
	}

	Dimension( String name, IDocumentManager documentManager )
			throws IOException, DataException
	{
		this.name = name;
		this.documentManager = documentManager;
		loadFromDisk( );
	}

	private void loadFromDisk( ) throws IOException, DataException
	{
		documentObj = documentManager.openDocumentObject( NamingUtil.getDimensionDocName( name ) );
		if ( documentObj == null )
		{
			throw new DataException( ResourceConstants.DIMENSION_NOT_EXIST,
					name );
		}
		isTime = documentObj.readBoolean( );
		
		hierarchy = new Hierarchy( documentManager, name );
		hierarchy.loadFromDisk( );
		length = hierarchy.size( );
		documentObj.close( );
		documentObj = null;
	}
	
	public IDiskArray getAllRows( ) throws IOException
	{
		return hierarchy.readAllRows( );
	}

	public DimensionRow getRowByPosition( int position )
			throws IOException
	{
		return hierarchy.readRowByPosition( position );
	}
	
	public IDiskArray getDimensionRowByPositions(
			IDiskArray positionArray ) throws IOException
	{
		BufferedStructureArray resultArray = new BufferedStructureArray( DimensionRow.getCreator( ),
				Constants.LIST_BUFFER_SIZE );

		for ( int i = 0; i < positionArray.size( ); i++ )
		{
			int pos = ( (Integer) positionArray.get( i ) ).intValue( );
			resultArray.add( hierarchy.readRowByPosition( pos ) );
		}
		return resultArray;
	}
	
	public DimensionRow getDimensionRowByOffset( int offset )
			throws IOException
	{
		return hierarchy.readRowByOffset( offset );
	}


	public IDiskArray find( Level level, Object[] keyValue ) throws IOException, DataException
	{
		return level.diskIndex.find( keyValue );
	}
	
	public IndexKey findFirst( Level level, Object[] keyValue ) throws IOException, DataException
	{
		return level.diskIndex.findFirst( keyValue );
	}
	

	/**
	 * 
	 * @param level
	 * @param selections
	 * @return Dimension index array.
	 * @throws IOException 
	 * @throws DataException 
	 */
	public IDiskArray find( Level[] levels, ISelection[][] filters ) throws IOException, DataException
	{
		return DimensionFilterHelper.find( levels, filters );
	}
	
	public Level getDetailLevel( )
	{
		return (Level)(hierarchy.getLevels( )[hierarchy.getLevels( ).length - 1]);
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.IDimension#findAll()
	 */
	public IDiskArray findAll( ) throws IOException
	{
		IDiskArray result = new BufferedPrimitiveDiskArray( Constants.LIST_BUFFER_SIZE );
		int lastPos = length( ) - 1;
		for ( int i = 0; i <= lastPos; i++ )
		{
			result.add( new Integer(i) );
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.IDimension#getName()
	 */
	public String getName()
	{
		return name;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.IDimension#getHierarchy()
	 */
	public IHierarchy getHierarchy( )
	{
		return hierarchy;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.IDimension#isTime()
	 */
	public boolean isTime( )
	{
		return isTime;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.IDimension#length()
	 */
	public int length( )
	{
		return length;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.IDimension#close()
	 */
	public void close( ) throws IOException
	{
		hierarchy.close( );
	}

}
