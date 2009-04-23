/*******************************************************************************
 * Copyright (c) 2004 ,2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.olap.cursor;

import java.io.IOException;
import java.util.Date;

import javax.olap.OLAPException;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.impl.DataEngineImpl;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.olap.data.api.cube.DocManagerMap;
import org.eclipse.birt.data.engine.olap.data.api.cube.DocManagerReleaser;
import org.eclipse.birt.data.engine.olap.data.api.cube.IDatasetIterator;
import org.eclipse.birt.data.engine.olap.data.api.cube.IDimension;
import org.eclipse.birt.data.engine.olap.data.api.cube.IHierarchy;
import org.eclipse.birt.data.engine.olap.data.api.cube.ILevelDefn;
import org.eclipse.birt.data.engine.olap.data.document.DocumentManagerFactory;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentManager;
import org.eclipse.birt.data.engine.olap.data.impl.Cube;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Dimension;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionFactory;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionForTest;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.LevelDefinition;
import org.eclipse.birt.data.engine.olap.data.util.DataType;
import org.eclipse.birt.data.engine.olap.data.util.IDiskArray;

class DateCube
{
	public static final String cubeName ="DateCube";

	void createCube( DataEngineImpl engine ) throws IOException,
			BirtException, OLAPException
	{
		IDocumentManager documentManager = DocumentManagerFactory.createFileDocumentManager( engine.getSession( )
				.getTempDir( ),
				cubeName );
		DocManagerMap.getDocManagerMap( )
				.set( String.valueOf( engine.hashCode( ) ),
						engine.getSession( ).getTempDir( ) + cubeName,
						documentManager );
		engine.addShutdownListener( new DocManagerReleaser( engine ) );
		Dimension[] dimensions = new Dimension[2];

		// dimension0
		String[] levelNames = new String[7];
		levelNames[0] = "level11";

		// dimension1
		levelNames[1] = "level12";
		levelNames[2] = "year/DateTime";

		levelNames[3] = "level13";
		levelNames[4] = "quarter/DateTime";

		levelNames[5] = "level14";
		levelNames[6] = "month/DateTime";

		DimensionForTest iterator = new DimensionForTest( levelNames );

		iterator.setLevelMember( 0, DateFactTable.DIM0_L1Col );
		iterator.setLevelMember( 1, DateFactTable.DIM1_YEAR_Col );
		iterator.setLevelMember( 2, DateFactTable.ATTRIBUTE_Col );
		iterator.setLevelMember( 3, DateFactTable.DIM1_QUARTER_Col );
		iterator.setLevelMember( 4, DateFactTable.ATTRIBUTE_Col );
		iterator.setLevelMember( 5, DateFactTable.DIM1_MONTH_Col );
		iterator.setLevelMember( 6, DateFactTable.ATTRIBUTE_Col );

		ILevelDefn[] levelDefs = new ILevelDefn[4];
		levelDefs[0] = new LevelDefinition( "level11", new String[]{
			"level11"
		}, null );

		levelDefs[1] = new LevelDefinition( "level12", new String[]{
			"level12"
		}, new String[]{
			"year/DateTime"
		} );
		levelDefs[2] = new LevelDefinition( "level13", new String[]{
			"level13"
		}, new String[]{
			"quarter/DateTime"
		} );
		levelDefs[3] = new LevelDefinition( "level14", new String[]{
			"level14"
		}, new String[]{
			"month/DateTime"
		} );
		dimensions[0] = (Dimension) DimensionFactory.createDimension( "dimension1",
				documentManager,
				iterator,
				levelDefs,
				false,
				new StopSign( ) );
		IHierarchy hierarchy = dimensions[0].getHierarchy( );
		IDiskArray allRow = dimensions[0].getAllRows( new StopSign( ) );

		// dimension3
		levelNames = new String[1];
		levelNames[0] = "level21";
		iterator = new DimensionForTest( levelNames );
		iterator.setLevelMember( 0, DateFactTable.DIM2_L2Col );

		levelDefs = new ILevelDefn[1];
		levelDefs[0] = new LevelDefinition( "level21", new String[]{
			"level21"
		}, null );
		dimensions[1] = (Dimension) DimensionFactory.createDimension( "dimension2",
				documentManager,
				iterator,
				levelDefs,
				false,
				new StopSign( ) );
		hierarchy = dimensions[1].getHierarchy( );
		allRow = dimensions[1].getAllRows( new StopSign( ) );

		DateFactTable factTable2 = new DateFactTable( );
		String[] measureColumnName = new String[1];
		measureColumnName[0] = "measure1";
		Cube cube = new Cube( cubeName, documentManager );

		cube.create( getKeyColNames( dimensions ),
				dimensions,
				factTable2,
				measureColumnName,
				new StopSign( ) );
		cube.close( );
		documentManager.flush( );
	}
	
    /**
	 * 
	 * @param dimensions
	 * @return
	 */
	public static String[][] getKeyColNames( IDimension[] dimensions )
	{
		String[][] keyColumnName = new String[2][];
		
		keyColumnName[0] = new String[]{
				"level11",
				"level12",
				"level13",
				"level14"
		};
		keyColumnName[1] = new String[] {"level21"};
		
		return keyColumnName;
	}
}

class DateFactTable implements IDatasetIterator
{

	int ptr = -1;
	static String[] DIM0_L1Col = {
		"CN","CN",
		"US","US",
		"UN","UN",
		"JP","JP"
   };
  static Integer[] DIM1_YEAR_Col = {
		1998,1999, 
		1999,1999,
		1999,2000,
		1998,2000 
   };

  static Integer[] DIM1_QUARTER_Col = {
	     1,2,
	     2,3,
	     3,4,
	     1,4
  };
  
  static Integer[] DIM1_MONTH_Col = {
	     1,4,
	     5,7,
	     8,10,
	     2,11
  };
  
  static String[] DIM2_L2Col = {
	    "PP1","PP2",
        "PP1","PP2",
        "PP1","PP2",
        "PP1","PP2"
  };
  static int[] MEASURE_Col = {
	  	1,2,
	  	11,16,
	  	23,36,
	  	38,39,
  };

  static Date[] ATTRIBUTE_Col = {
	  	new Date( 98, 0, 1),new Date( 98, 4, 1),
	  	new Date( 99, 0, 1),new Date( 99, 4, 1),
	  	new Date( 99, 0, 1),new Date( 99, 4, 1),
	  	new Date( 98, 0, 1),new Date( 99, 4, 1)
};

	public void close( ) throws BirtException
	{
	}

	public Boolean getBoolean( int fieldIndex ) throws BirtException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Date getDate( int fieldIndex ) throws BirtException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Double getDouble( int fieldIndex ) throws BirtException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public int getFieldIndex( String name ) throws BirtException
	{
		if ( name.equals( "level11" ) )
		{
			return 0;
		}
		else if ( name.equals( "level12" ) )
		{
			return 1;
		}
		else if ( name.equals( "level13" ) )
		{
			return 2;
		}
		else if ( name.equals( "level14" ) )
		{
			return 3;
		}
		else if ( name.equals( "level21" ) )
		{
			return 4;
		}
		else if ( name.equals( "measure1" ) )
		{
			return 5;
		}
		return -1;
	}

	public int getFieldType( String name ) throws BirtException
	{
		if ( name.equals( "level11" ) )
		{
			return DataType.STRING_TYPE;
		}
		else if ( name.equals( "level12" ) )
		{
			return DataType.INTEGER_TYPE;
		}
		else if ( name.equals( "level13" ) )
		{
			return DataType.INTEGER_TYPE;
		}
		else if ( name.equals( "level14" ) )
		{
			return DataType.INTEGER_TYPE;
		}
		else if ( name.equals( "level21" ) )
		{
			return DataType.STRING_TYPE;
		}
		else if ( name.equals( "measure1" ) )
		{
			return DataType.INTEGER_TYPE;
		}
		return -1;
	}

	public Integer getInteger( int fieldIndex ) throws BirtException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String getString( int fieldIndex ) throws BirtException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Object getValue( int fieldIndex ) throws BirtException
	{
		if ( fieldIndex == 0 )
		{
			return DIM0_L1Col[ptr];
		}
		else if ( fieldIndex == 1 )
		{
			return DIM1_YEAR_Col[ptr];
		}
		else if ( fieldIndex == 2 )
		{
			return DIM1_QUARTER_Col[ptr];
		}
		else if ( fieldIndex == 3 )
		{
			return DIM1_MONTH_Col[ptr];
		}
		else if ( fieldIndex == 4 )
		{
			return DIM2_L2Col[ptr];
		}
		else if ( fieldIndex == 5 )
		{
			return new Integer( MEASURE_Col[ptr] );
		}
		return null;
	}

	public boolean next( ) throws BirtException
	{
		ptr++;
		if ( ptr >= MEASURE_Col.length )
		{
			return false;
		}
		return true;
	}
}

