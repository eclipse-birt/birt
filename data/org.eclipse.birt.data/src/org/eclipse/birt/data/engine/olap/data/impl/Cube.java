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

package org.eclipse.birt.data.engine.olap.data.impl;

import java.io.IOException;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.cube.ICube;
import org.eclipse.birt.data.engine.olap.api.cube.IDatasetIterator;
import org.eclipse.birt.data.engine.olap.api.cube.IDimension;
import org.eclipse.birt.data.engine.olap.api.cube.StopSign;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentManager;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentObject;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Dimension;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionFactory;
import org.eclipse.birt.data.engine.olap.data.impl.facttable.FactTable;
import org.eclipse.birt.data.engine.olap.data.impl.facttable.FactTableAccessor;

/**
 * Default implements of ICube interface.
 */

public class Cube implements ICube
{

	private String name;
	private IDocumentManager documentManager;
	private IDocumentObject documentObject;
	private IDimension[] dimension;
	private FactTable factTable;

	/**
	 * 
	 * @param name
	 * @param documentManager
	 */
	public Cube( String name, IDocumentManager documentManager )
	{
		this.name = name;
		this.documentManager = documentManager;
	}

	/**
	 * 
	 * @param dimension
	 * @param iterator
	 * @param measureColumnName
	 * @param stopSign
	 * @throws IOException
	 * @throws BirtException
	 */
	public void create( IDimension[] dimension, IDatasetIterator iterator,
			String[] measureColumnName, StopSign stopSign ) throws IOException,
			BirtException
	{
		documentManager.createDocumentObject( NamingUtil.getCubeDocName( name ) );
		documentObject = documentManager.openDocumentObject( NamingUtil.getCubeDocName( name ) );
		documentObject.writeString( name );
		documentObject.writeInt( dimension.length );
		for ( int i = 0; i < dimension.length; i++ )
		{
			documentObject.writeString( dimension[i].getName( ) );
		}
		this.dimension = dimension;
		Dimension[] pDimensions = new Dimension[dimension.length];
		for ( int i = 0; i < pDimensions.length; i++ )
		{
			pDimensions[i] = (Dimension) dimension[i];
		}
		FactTableAccessor factTableConstructor = new FactTableAccessor( documentManager );
		this.factTable = factTableConstructor.saveFactTable( NamingUtil.getFactTableName( name ),
				iterator,
				pDimensions,
				measureColumnName,
				stopSign );

	}

	/**
	 * 
	 * @param stopSign
	 * @throws IOException
	 * @throws BirtException
	 */
	public void load( StopSign stopSign ) throws IOException, DataException
	{
		documentObject = documentManager.openDocumentObject( NamingUtil.getCubeDocName( name ) );
		name = documentObject.readString( );
		dimension = new IDimension[documentObject.readInt( )];

		for ( int i = 0; i < dimension.length; i++ )
		{
			dimension[i] = DimensionFactory.loadDimension( documentObject.readString( ),
					documentManager );
		}
		FactTableAccessor factTableConstructor = new FactTableAccessor( documentManager );
		this.factTable = factTableConstructor.load( NamingUtil.getFactTableName( name ),
				stopSign );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.data.api.ICube#getDimesions()
	 */
	public IDimension[] getDimesions( )
	{
		return this.dimension;
	}

	/**
	 * 
	 * @return
	 */
	public FactTable getFactTable( )
	{
		return factTable;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.data.api.ICube#close()
	 */
	public void close( ) throws IOException
	{
		documentObject.close( );
		for(int i=0;i<dimension.length;i++)
		{
			dimension[i].close( );
		}
	}

}
