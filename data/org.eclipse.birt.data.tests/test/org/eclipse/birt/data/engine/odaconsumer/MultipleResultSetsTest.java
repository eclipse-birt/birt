/*
 *************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.data.engine.odaconsumer;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odaconsumer.testdriver.TestAdvQueryImpl;
import org.eclipse.birt.data.engine.odaconsumer.testutil.OdaTestDriverCase;
import org.eclipse.birt.data.engine.odi.IResultClass;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

/**
 *  Test cases for handling multiple result sets.
 */
@Ignore("Ignore tests that require manual setup")
@SuppressWarnings("nls")
public class MultipleResultSetsTest extends OdaTestDriverCase
{
    /*
     * Setup PreparedStatement capable of handling sequential result sets
     */
    private PreparedStatement getSequentialRSPreparedStatement()
            throws DataException
    {
        // setup; uses default dataSetType in plugin.xml
        PreparedStatement hostStmt = getOpenedConnection().prepareStatement( null,
                TestAdvQueryImpl.TEST_CASE_SEQ_RESULT_SETS ); 
        assertTrue( hostStmt != null );
        return hostStmt;
    }

    // Positive test cases for sequential result set w/o projected columns
    
    /**
     * Get the first result set's metadata, then get the result set.
     */
	@Test
    public void testGetSequentialResultSetMetaData() throws Exception
    {
        try
        {
            PreparedStatement hostStmt = getSequentialRSPreparedStatement();
            hostStmt.execute();
            getSequentialResultSetMetaData( hostStmt, 1 );
        }
        catch( DataException e1 )
        {
            fail( "testGetSequentialResultSetMetaData failed: " + e1.toString() ); //$NON-NLS-1$
        }
    }
	@Test
    public void testGetMoreResultSetMetaData() throws Exception
    {
        try
        {
            PreparedStatement hostStmt = getSequentialRSPreparedStatement();            
            hostStmt.execute();
            getSequentialResultSetMetaData( hostStmt, TestAdvQueryImpl.MAX_RESULT_SETS );
        }
        catch( DataException e1 )
        {
            fail( "testGetMoreResultSetMetaData failed: " + e1.toString() ); //$NON-NLS-1$
        }
    }

    private void getSequentialResultSetMetaData( PreparedStatement hostStmt, int resultSetNum ) throws Exception
    {       
        // first get the metadata on specified index
        IResultClass resultClass1 = hostStmt.getMetaData( resultSetNum );
        assertNotNull( resultClass1 );
 
         // repeat the call; expects to get the same instance
        IResultClass resultClass2 = hostStmt.getMetaData( resultSetNum );
        assertEquals( resultClass1, resultClass2 );
        
        // now get the result set itself on same index
        ResultSet rs = hostStmt.getResultSet( resultSetNum );
        resultClass2 = rs.getMetaData();
        assertEquals( resultClass1, resultClass2 );
    }
    
    /**
     * First get result set, then get its metadata.
     */
	@Test
    public void testGetSequentialResultSet() throws Exception
    {       
        try
        {
            PreparedStatement hostStmt = getSequentialRSPreparedStatement();            
            hostStmt.execute();
            getSequentialResultSet( hostStmt, 1 );
        }
        catch( DataException e1 )
        {
            fail( "testGetSequentialResultSet failed: " + e1.toString() ); //$NON-NLS-1$
        }
    }
	@Test
    public void testGetMoreResultSet() throws Exception
    {       
        try
        {
            PreparedStatement hostStmt = getSequentialRSPreparedStatement();            
            hostStmt.execute();
            getSequentialResultSet( hostStmt, TestAdvQueryImpl.MAX_RESULT_SETS );
        }
        catch( DataException e1 )
        {
            fail( "testGetMoreResultSet failed: " + e1.toString() ); //$NON-NLS-1$
        }
    }

    private ResultSet getSequentialResultSet( PreparedStatement hostStmt, int resultSetNum ) throws Exception
    {
        // first get result set at specified index
        ResultSet rs1 = hostStmt.getResultSet( resultSetNum );
        assertNotNull( rs1 );
 
        // repeat the call; should get the same instance
        ResultSet rs2 = hostStmt.getResultSet( resultSetNum );
        assertEquals( rs1, rs2 );
       
        // now get the result set metdata on same index
        IResultClass resultClass2 = hostStmt.getMetaData( resultSetNum );
        IResultClass resultClass1 = rs1.getMetaData();
        assertEquals( resultClass1, resultClass2 );
        return rs1;
    }
    
    private boolean hasCustomColumn( ResultSet rs, String customColumnName ) throws Exception
    {
        IResultClass customMd = rs.getMetaData();
        return ( customMd.getFieldIndex( customColumnName ) > 0 );
    }
    
    /**
     * Get multiple result sets in sequential order.
     */
	@Test
    public void testGetSequentialResultSets() throws Exception
    {       
        try
        {
            PreparedStatement hostStmt = getSequentialRSPreparedStatement();            
            hostStmt.execute();
            getSequentialResultSet( hostStmt, 2 );
            getSequentialResultSet( hostStmt, TestAdvQueryImpl.MAX_RESULT_SETS );
        }
        catch( DataException e1 )
        {
            fail( "testGetSequentialResultSets failed: " + e1.toString() ); //$NON-NLS-1$
        }
    }
	@Test
    public void testGetFirstResultSetWithCustomColumn() throws Exception
    {       
        try
        {
            PreparedStatement hostStmt = getSequentialRSPreparedStatement();  
            int resultSetNum = 1;
            String customColumnName = "Custom1";

            hostStmt.declareCustomColumn( resultSetNum, customColumnName, String.class );
            hostStmt.execute();            

            IResultClass metadata = hostStmt.getMetaData( resultSetNum );
            assertTrue( metadata.getFieldIndex( customColumnName ) > 0 );

            ResultSet customRs = getSequentialResultSet( hostStmt, resultSetNum );            
            assertTrue( hasCustomColumn( customRs, customColumnName ) );

            customRs = getSequentialResultSet( hostStmt, TestAdvQueryImpl.MAX_RESULT_SETS );
            assertFalse( hasCustomColumn( customRs, customColumnName ) );
        }
        catch( DataException e1 )
        {
            fail( "testGetFirstResultSetWithCustomColumn() failed: " + e1.toString() ); //$NON-NLS-1$
        }
    }
	@Test
    public void testGetSequentialResultSetsWithCustomColumn() throws Exception
    {       
        try
        {
            PreparedStatement hostStmt = getSequentialRSPreparedStatement();  
            int resultSetNum = 2;   // beyong first set to trigger getMoreResults()
            String customColumnName = "Custom1";

            // declareCustomColumn should be called before execute; it is done after execute
            // here purely to simulate the behavior of an oda driver that allows getResultSet before execute is called
            hostStmt.execute();
            hostStmt.declareCustomColumn( resultSetNum, customColumnName, String.class );

            IResultClass metadata = hostStmt.getMetaData( resultSetNum );
            assertTrue( metadata.getFieldIndex( customColumnName ) > 0 );

            ResultSet customRs = getSequentialResultSet( hostStmt, resultSetNum );            
            assertTrue( hasCustomColumn( customRs, customColumnName ) );

            customRs = getSequentialResultSet( hostStmt, TestAdvQueryImpl.MAX_RESULT_SETS );
            assertFalse( hasCustomColumn( customRs, customColumnName ) );
        }
        catch( DataException e1 )
        {
            fail( "testGetSequentialResultSetsWithCustomColumn() failed: " + e1.toString() ); //$NON-NLS-1$
        }
    }
    
    // Negative test cases for sequential result set w/o projected columns
	@Test
    public void testGetSequentialResultSetBeforeExecute() throws Exception
    {       
        boolean hasExpectedException = false;
        try
        {
            PreparedStatement hostStmt = getSequentialRSPreparedStatement();            
            // do not call execute first
            ResultSet rs1 = hostStmt.getResultSet( 2 );
            assertNull( rs1 );
            hasExpectedException = true;    // oda driver returns null instead of throws exception
        }
        catch( DataException e1 )
        {
            hasExpectedException = true;
        }
        assert( hasExpectedException );
    }
	@Test
    public void testGetOutOfRangeResultSet() throws Exception
    {       
        final String methodName = "testGetOutOfRangeResultSet"; //$NON-NLS-1$
        PreparedStatement hostStmt = getSequentialRSPreparedStatement();            
        hostStmt.execute();
        try
        {
            getSequentialResultSet( hostStmt, 2 );
        }
        catch( DataException e1 )
        {
            fail( methodName + " failed: " + e1.toString() ); //$NON-NLS-1$
        }
        
        // specify more result set than available, which is not allowed
        boolean hasExpectedException = false;
        try
        {
            getSequentialResultSet( hostStmt, TestAdvQueryImpl.MAX_RESULT_SETS + 1 );
        }
        catch( DataException ex )
        {
            hasExpectedException = true;
        }
        assert( hasExpectedException );
    }
	@Test
    public void testGetReverseSequenceResultSets() throws Exception
    {       
        final String methodName = "testGetReverseSequenceResultSets"; //$NON-NLS-1$
        PreparedStatement hostStmt = getSequentialRSPreparedStatement();            
        hostStmt.execute();
        try
        {
            getSequentialResultSet( hostStmt, TestAdvQueryImpl.MAX_RESULT_SETS );
        }
        catch( DataException e1 )
        {
            fail( methodName + " failed: " + e1.toString() ); //$NON-NLS-1$
        }
        
        // reverse the sequence of index, which is not allowed
        boolean hasExpectedException = false;
        try
        {
            getSequentialResultSet( hostStmt, 1 );
        }
        catch( DataException ex )
        {
            hasExpectedException = true;
        }
        assert( hasExpectedException );
    }

    //-----------------------------------------------------------------------------------------------------
    // Positive test cases for sequential result set w/ projected columns
    
    /**
     * Test setting projected columns before getting result set and metadata
     */
	@Test
    public void testProjectedColumnsResultSet( ) throws Exception
    {
        PreparedStatement hostStmt = getSequentialRSPreparedStatement();            
        hostStmt.execute();
        getProjectedColumnsResultSet( hostStmt, 2, false );
    }
	@Test
    public void testSetProjectedColumnsOnResultSet1BeforeExecute( ) throws Exception
    {
        PreparedStatement hostStmt = getSequentialRSPreparedStatement();            
        // do not call execute first, which is ok for getting the first result set
        getProjectedColumnsResultSet( hostStmt, 1, true );
    }
	@Test
    public void testSetProjectedColumnsOnMoreResultSetBeforeExecute( ) throws Exception
    {
        PreparedStatement hostStmt = getSequentialRSPreparedStatement();            
        // do not call execute first, which is ok if execute is called 
        // before getting result set and metadata
        getProjectedColumnsResultSet( hostStmt, 3, true );
    }
	@Test
    public void testSetProjectedCustomColumnsOnMoreResultSetBeforeExecute( ) throws Exception
    {
        PreparedStatement hostStmt = getSequentialRSPreparedStatement();            
        // do not call execute first, which is ok if execute is called 
        // before getting result set and metadata
        int resultSetNum = 3;
        hostStmt.declareCustomColumn( resultSetNum, "Custom1", String.class );

        ColumnHint columnHint = new ColumnHint( "Custom1" );
        columnHint.setAlias( "CustomAlias1" );
        hostStmt.addColumnHint( resultSetNum, columnHint );

        hostStmt.setColumnsProjection( resultSetNum, new String[]{
                "StringCol", "CustomAlias1", "IntCol"  } );  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        
        hostStmt.execute();

        IResultClass metadata1 = hostStmt.getMetaData( resultSetNum );
        assertNotNull( metadata1 );
        
        ResultSet rs = hostStmt.getResultSet( resultSetNum );
        IResultClass metadata2 = rs.getMetaData();
        assertEquals( metadata1, metadata2 );
    }

    // Negative test cases for sequential result set w/ projected columns
	@Test
    public void testProjectedMoreColumnsResultSetBeforeExecute( ) throws Exception
    {
        PreparedStatement hostStmt = getSequentialRSPreparedStatement();            
        // do not call execute, expects failure to get second result set
        boolean hasExpectedException = false;
        try
        {
            getProjectedColumnsResultSet( hostStmt, 2, false );
        }
        catch( DataException ex )
        {
            hasExpectedException = true;
        }
        assert( hasExpectedException );
    }

    private void getProjectedColumnsResultSet( PreparedStatement hostStmt, int resultSetNum,
            boolean executeAfterSetProjections ) throws Exception
    {
        hostStmt.setColumnsProjection( resultSetNum, getTestProjectedColumns() );
        
        // flag to execute before getting result set and metadata
        if( executeAfterSetProjections )
            hostStmt.execute();

        IResultClass metadata1 = hostStmt.getMetaData( resultSetNum );
        assertNotNull( metadata1 );
        ResultSet rs = hostStmt.getResultSet( resultSetNum );
        IResultClass metadata2 = rs.getMetaData();
        assertEquals( metadata1, metadata2 );
    }
    
    private String[] getTestProjectedColumns()
    {
        return new String[]{
                "StringCol", "IntCol" //$NON-NLS-1$ //$NON-NLS-2$
        };
    }
    
}
