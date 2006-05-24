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
package org.eclipse.birt.report.data.oda.xml.util;


/**
 * This is a helper class used by SaxParserConsumer to generate nested xml columns related
 * infomation.
 */
public class SaxParserComplexNestedQueryHelper implements ISaxParserConsumer
{
	//The table name
	private String tableName;
	
	//The RelationInformation instance which defines the table.
	private RelationInformation relationInfo;
	
	//Cache the name of nested columns
	private String[] namesOfNestedColumns;
	
	//The sax parser instance.
	private SaxParser sp;
	private Thread spThread;
	
	private NestedColumnUtil nestedColumnUtil;

	
	
	/**
	 * @param rinfo
	 * @param fileName
	 * @param tName
	 */
	SaxParserComplexNestedQueryHelper( RelationInformation rinfo, XMLDataInputStream xdis, String tName)
	{
		
		tableName = tName;
		relationInfo = rinfo;
		
		namesOfNestedColumns = relationInfo.getTableComplexNestedXMLColumnNames( tableName );
		nestedColumnUtil = new NestedColumnUtil( relationInfo, tableName, false);
		sp = new SaxParser( xdis , this );
		spThread = new Thread( sp );
		spThread.start();
	}
	
	/**
	 * Return whether the SaxParserNestedQueryHelper instance is ready for provide nested
	 * xml columns information.
	 * @return
	 */
	public boolean isPrepared()
	{
		return !spThread.isAlive();
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.birt.report.data.oda.xml.util.ISaxParserConsumer#manipulateData(java.lang.String, java.lang.String)
	 */
	public void manipulateData( String path, String value )
	{
		for( int i = 0; i < this.namesOfNestedColumns.length; i++ )
		{
			this.nestedColumnUtil.update(this.namesOfNestedColumns[i], path, value);
		}
	}

	/**
	 * The method would not be used in this implementation of ISaxParserConsumer.
	 */
	public void detectNewRow( String path )
	{
	}
	

	
	/**
	 * The method would not be used in this implementation of ISaxParserConsumer.
	 */
	public void wakeup( )
	{
		
	}
	
	/**
	 * Return the NestedColumnUtil instance.
	 * @return
	 */
	NestedColumnUtil getNestedColumnUtil()
	{
		return this.nestedColumnUtil;
	}


}


