
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
import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.cube.IDatasetIterator;
import org.eclipse.birt.data.engine.olap.api.cube.IHierarchy;
import org.eclipse.birt.data.engine.olap.api.cube.ILevelDefn;
import org.eclipse.birt.data.engine.olap.data.api.ILevel;
import org.eclipse.birt.data.engine.olap.data.document.DocumentObjectUtil;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentManager;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentObject;
import org.eclipse.birt.data.engine.olap.data.impl.Constants;
import org.eclipse.birt.data.engine.olap.data.impl.NamingUtil;
import org.eclipse.birt.data.engine.olap.data.util.BufferedStructureArray;
import org.eclipse.birt.data.engine.olap.data.util.DiskIndex;
import org.eclipse.birt.data.engine.olap.data.util.DiskSortedStack;
import org.eclipse.birt.data.engine.olap.data.util.IDiskArray;
import org.eclipse.birt.data.engine.olap.data.util.IndexKey;

/**
 * Describes a hierarchy. A hierarchy is composed of multi-levels.
 */

public class Hierarchy implements IHierarchy
{
	private IDocumentManager documentManager = null;
	private IDocumentObject documentObj = null;
	private IDocumentObject offsetDocObj = null;
	private Level[] levels = null;
	private String name = null;
	private Map levelMap = new HashMap( );
	
	public Hierarchy( IDocumentManager documentManager, String name )
	{
		this.documentManager = documentManager;
		this.name = name;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.IHierarchy#getLevels()
	 */
	public ILevel[] getLevels( )
	{
		return levels;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.IHierarchy#getName()
	 */
	public String getName( )
	{
		return name;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.IHierarchy#close()
	 */
	public void close( ) throws IOException
	{
		for ( int i = 0; i < levels.length; i++ )
		{
			levels[i].close( );
		}
		documentObj.close( );
		offsetDocObj.close( );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.api.IHierarchy#size()
	 */
	public int size( )
	{
		return levels[levels.length - 1].size( );
	}
	
	/**
	 * 
	 * @param datasetIterator
	 * @param levelDefs
	 * @throws IOException
	 * @throws BirtException
	 */
	public void createAndSaveHierarchy( IDatasetIterator datasetIterator,
			ILevelDefn[] levelDefs ) throws IOException, BirtException
	{
		documentObj = createHierarchyDocumentObject( );
		offsetDocObj = createLevelOffsetDocumentObject( );
		
		DiskSortedStack sortedDimensionSet = getSortedDimRows( datasetIterator,
				levelDefs );
		
		documentObj.seek( 4 );
		saveHierarchyMetadata( datasetIterator,
				levelDefs );
		
		int[][] keyDataType = new int[levelDefs.length][];
		int[][] attributesDataType = new int[levelDefs.length][];
		for ( int i = 0; i < levelDefs.length; i++ )
		{
			keyDataType[i] = new int[levelDefs[i].getKeyColumns( ).length];
			for ( int j = 0; j < levelDefs[i].getKeyColumns( ).length; j++ )
			{
				keyDataType[i][j] = datasetIterator.getFieldType( levelDefs[i].getKeyColumns( )[j] );
			}
			if ( levelDefs[i].getAttributeColumns( ) != null )
			{
				attributesDataType[i] = new int[levelDefs[i].getAttributeColumns( ).length];
				for ( int j = 0; j < levelDefs[i].getAttributeColumns( ).length; j++ )
				{
					attributesDataType[i][j] = datasetIterator.getFieldType( levelDefs[i].getAttributeColumns( )[j] );
				}
			}
		}
		
		int size = saveHierarchyRows( levelDefs,
				keyDataType,
				attributesDataType,
				sortedDimensionSet );
		// save dimension member size
		int savedPointer = (int) documentObj.getFilePointer( );
		documentObj.seek( 0 );
		documentObj.writeInt( size );
		documentObj.seek( savedPointer );

		levels = new Level[levelDefs.length];
		for ( int i = 0; i < levels.length; i++ )
		{
			levels[i] = new Level( documentManager,
					levelDefs[i],
					keyDataType[i],
					attributesDataType[i],
					size );
		}
		for ( int i = 0; i < levels.length; i++ )
		{
			this.levelMap.put( levels[i].name, levels[i] );
		}
		documentObj.flush( );
	}

	/**
	 * 
	 * @throws IOException
	 * @throws DataException
	 */
	public void loadFromDisk( ) throws IOException, DataException
	{
		documentObj = documentManager.openDocumentObject( NamingUtil.getHierarchyDocName( name ) );
		offsetDocObj = documentManager.openDocumentObject( NamingUtil.getHierarchyOffsetDocName( name ) );
		int size = documentObj.readInt( );
		levels = new Level[documentObj.readInt( )];
		for ( int i = 0; i < levels.length; i++ )
		{
			String levelName = documentObj.readString( );
			String[] keyColName = new String[documentObj.readInt( )];
			int[] keyDataType = new int[keyColName.length];
			for ( int j = 0; j < keyColName.length; j++ )
			{
				keyColName[j] = documentObj.readString( );
				keyDataType[j] = documentObj.readInt( );
			}
			int attributeNumber = documentObj.readInt( );
			String[] attributeColNames = null;
			int[] attributeDataTypes = null;
			if ( attributeNumber > 0 )
			{
				attributeColNames = new String[attributeNumber];
				attributeDataTypes = new int[attributeNumber];
				for ( int j = 0; j < attributeNumber; j++ )
				{
					attributeColNames[j] = documentObj.readString( );
					attributeDataTypes[j] = documentObj.readInt( );
				}
			}
			
			levels[i] = new Level( documentManager,
					new LevelDefinition( levelName,
							keyColName,
							attributeColNames ),
					keyDataType,
					attributeDataTypes,
					size );
		}
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	private IDocumentObject createHierarchyDocumentObject( )
			throws IOException
	{	
		documentManager.createDocumentObject( NamingUtil.getHierarchyDocName( name) );
		return documentManager.openDocumentObject( NamingUtil.getHierarchyDocName( name ) );
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	private IDocumentObject createLevelOffsetDocumentObject( )
			throws IOException
	{
		documentManager.createDocumentObject( NamingUtil.getHierarchyOffsetDocName( name ) );
		return documentManager.openDocumentObject( NamingUtil.getHierarchyOffsetDocName( name ) );
	}

	/**
	 * 
	 * @param iterator
	 * @param levelDefs
	 * @throws IOException
	 * @throws BirtException
	 */
	private void saveHierarchyMetadata( IDatasetIterator iterator, ILevelDefn[] levelDefs )
			throws IOException, BirtException
	{
		documentObj.writeInt( levelDefs.length );
		for ( int i = 0; i < levelDefs.length; i++ )
		{
			saveLevelMetadata( iterator, levelDefs[i] );
		}
	}
	
	/**
	 * 
	 * @param iterator
	 * @param levelDef
	 * @throws IOException
	 * @throws BirtException
	 */
	private void saveLevelMetadata( IDatasetIterator iterator, ILevelDefn levelDef ) throws IOException, BirtException
	{
		documentObj.writeString( levelDef.getLevelName( ) );
		documentObj.writeInt( levelDef.getKeyColumns( ).length );
		for ( int i = 0; i < levelDef.getKeyColumns( ).length; i++ )
		{
			documentObj.writeString( levelDef.getKeyColumns( )[i] );
			documentObj.writeInt( iterator.getFieldType( levelDef.getKeyColumns( )[i] ) );
		}
		String[] attributes = levelDef.getAttributeColumns( );
		if ( attributes != null )
		{
			documentObj.writeInt( attributes.length );
			for ( int j = 0; j < attributes.length; j++ )
			{
				documentObj.writeString( attributes[j] );
				documentObj.writeInt( iterator.getFieldType( attributes[j] ) );
			}
		}
		else
		{
			documentObj.writeInt( 0 );
		}
	}
	
	/**
	 * 
	 * @param levelDefs
	 * @param keyDataType
	 * @param attributesDataType
	 * @param sortedDimensionSet
	 * @throws IOException
	 * @throws BirtException
	 */
	private int saveHierarchyRows(
			ILevelDefn[] levelDefs,
			int[][] keyDataType,
			int[][] attributesDataType, DiskSortedStack sortedDimensionSet )
			throws IOException, BirtException
	{
		IDiskArray[] indexKeyLists = new IDiskArray[keyDataType.length];
		for( int i=0;i<indexKeyLists.length;i++)
		{
			indexKeyLists[i] = new BufferedStructureArray( IndexKey.getCreator( ),
				Constants.LIST_BUFFER_SIZE );
		}

		Object obj = sortedDimensionSet.pop( );
		int currentIndex = 0;
		IndexKey indexKey = null;
		while ( obj != null )
		{
			DimensionRow dimRows = (DimensionRow) obj;
			Member[] levelMembers = dimRows.members;
			for ( int i = 0; i < indexKeyLists.length; i++ )
			{
				indexKey = new IndexKey( );
				indexKey.key = levelMembers[i].keyValues;
				indexKey.offset = (int) documentObj.getFilePointer( );
				indexKey.dimensionPos = currentIndex;
				indexKeyLists[i].add( indexKey );
			}
			// write row offset
			offsetDocObj.writeInt( (int) documentObj.getFilePointer( ) );
			// write hierarchy rows
			writeDimensionRow( dimRows,
					keyDataType,
					attributesDataType );

			obj = sortedDimensionSet.pop( );
			currentIndex++;
		}
		for ( int i = 0; i < indexKeyLists.length; i++ )
		{
			// create index for this level
			DiskIndex.createIndex( documentManager,
					NamingUtil.getLevelIndexDocName( levelDefs[i].getLevelName( ) ),
					indexKeyLists[i],
					false );
		}
		offsetDocObj.flush( );
		return currentIndex;
	}
	
	/**
	 * 
	 * @param dimensionMember
	 * @param keyDataType
	 * @param attributesDataType
	 * @throws IOException
	 */
	private void writeDimensionRow( DimensionRow dimensionMember, int[][] keyDataType, int[][] attributesDataType )
			throws IOException
	{
		Member[] levelMembers = dimensionMember.members;
		for ( int i = 0; i < levelMembers.length; i++ )
		{
			writeLevelMember( levelMembers[i], keyDataType[i], attributesDataType[i] );
		}
	}
	
	/**
	 * 
	 * @param levelMember
	 * @param keyDataType
	 * @param attributesDataType
	 * @throws IOException
	 */
	private void writeLevelMember( Member levelMember, int keyDataType[], int[] attributesDataType )
			throws IOException
	{
		for ( int i = 0; i < levelMember.keyValues.length; i++ )
		{
			DocumentObjectUtil.writeValue( documentObj,
					keyDataType[i],
					levelMember.keyValues[i] );
		}
		if ( levelMember.attributes != null )
		{
			for ( int i = 0; i < levelMember.attributes.length; i++ )
			{
				DocumentObjectUtil.writeValue( documentObj,
						attributesDataType[i],
						levelMember.attributes[i] );
			}
		}
	}
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	private DimensionRow readDimensionRow( )
			throws IOException
	{
		Member[] levelMembers = new Member[levels.length];
		for ( int i = 0; i < levelMembers.length; i++ )
		{
			levelMembers[i] = readLevelMember( levels[i] );
		}
		return new DimensionRow( levelMembers );
	}
	
	/**
	 * 
	 * @param level
	 * @return
	 * @throws IOException
	 */
	private Member readLevelMember( Level level )
			throws IOException
	{
		Member levelMember = new Member( );
		levelMember.keyValues = new Object[level.keyColNames.length];
		for ( int i = 0; i < level.keyColNames.length; i++ )
		{
			levelMember.keyValues[i] = DocumentObjectUtil.readValue( documentObj,
					level.keyDataType[i] );
		}
		if ( level.attributeDataTypes != null && level.attributeDataTypes.length>0)
		{
			levelMember.attributes = new Object[level.attributeDataTypes.length];
			for ( int i = 0; i < level.attributeDataTypes.length; i++ )
			{
				levelMember.attributes[i] = DocumentObjectUtil.readValue( documentObj,
						level.attributeDataTypes[i]);
			}
		}
		return levelMember;
	}
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public IDiskArray readAllRows( ) throws IOException
	{
		BufferedStructureArray resultArray = new BufferedStructureArray( DimensionRow.getCreator( ),
				Constants.LIST_BUFFER_SIZE );
		documentObj.seek( 0 );
		int size = documentObj.readInt( );
		offsetDocObj.seek( 0 );
		documentObj.seek( offsetDocObj.readInt( ) );
		for ( int i = 0; i < size; i++ )
		{
			resultArray.add( readDimensionRow( ) );
		}
		
		return resultArray;
	}
	
	/**
	 * 
	 * @param dimPosition
	 * @return
	 * @throws IOException
	 */
	public DimensionRow readRowByPosition( int dimPosition ) throws IOException
	{
		offsetDocObj.seek( dimPosition * 4 );
		
		return readRowByOffset( offsetDocObj.readInt( ) );
	}
	
	/**
	 * 
	 * @param offset
	 * @return
	 * @throws IOException
	 */
	public DimensionRow readRowByOffset(
			int offset )
			throws IOException
	{
		documentObj.seek( offset );

		return readDimensionRow( );
	}

	/**
	 * 
	 * @param iterator
	 * @param levelDefs
	 * @return
	 * @throws BirtException
	 * @throws IOException
	 */
	private static DiskSortedStack getSortedDimRows( IDatasetIterator iterator,
			ILevelDefn[] levelDefs ) throws BirtException, IOException
	{
		DiskSortedStack result = new DiskSortedStack( Constants.LIST_BUFFER_SIZE,
				true,
				true,
				DimensionRow.getCreator( ) );

		int[][] levelKeyColumnIndex = new int[levelDefs.length][];
		int[][] levelAttributesIndex = new int[levelDefs.length][];
		for ( int i = 0; i < levelDefs.length; i++ )
		{
			levelKeyColumnIndex[i] = new int[levelDefs[i].getKeyColumns( ).length];
			for ( int j = 0; j < levelDefs[i].getKeyColumns( ).length; j++ )
			{
				levelKeyColumnIndex[i][j] = iterator.getFieldIndex( levelDefs[i].getKeyColumns( )[j] );
			}
			String[] attributeColumns = levelDefs[i].getAttributeColumns( );
			if ( attributeColumns != null )
			{
				levelAttributesIndex[i] = new int[attributeColumns.length];
				for ( int j = 0; j < attributeColumns.length; j++ )
				{
					levelAttributesIndex[i][j] = iterator.getFieldIndex( attributeColumns[j] );
				}
			}
		}
		Member[] levelMembers = null;
		while ( iterator.next( ) )
		{
			levelMembers = new Member[levelDefs.length];
			for ( int i = 0; i < levelDefs.length; i++ )
			{
				levelMembers[i] = getLevelMember( iterator,
						levelKeyColumnIndex[i],
						levelAttributesIndex[i] );
			}
			result.push( new DimensionRow( levelMembers ) );
		}
		return result;
	}

	/**
	 * 
	 * @param iterator
	 * @param IDColumn
	 * @param attributeCols
	 * @return
	 * @throws BirtException
	 */
	private static Member getLevelMember( IDatasetIterator iterator,
			int[] keyCols, int[] attributeCols ) throws BirtException
	{
		Member levelMember = new Member( );
		levelMember.keyValues = new Object[keyCols.length];
		for ( int i = 0; i < keyCols.length; i++ )
		{
			levelMember.keyValues[i] = iterator.getValue( keyCols[i] );
		}
		if ( attributeCols != null )
		{
			levelMember.attributes = new Object[attributeCols.length];
			for ( int i = 0; i < attributeCols.length; i++ )
			{
				levelMember.attributes[i] = iterator.getValue( attributeCols[i] );
			}
		}
		return levelMember;
	}

}
