
package org.eclipse.birt.data.engine.impl;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.ExprManagerUtil.Node;

public class ExprManagerUtilTest extends TestCase
{

	Node[] nodes = new Node[5];

	protected void setUp( ) throws Exception
	{
		super.setUp( );
		nodes[0] = new Node( "COL0" );
		nodes[1] = new Node( "COL1" );
		nodes[2] = new Node( "COL2" );
		nodes[3] = new Node( "COL3" );
		nodes[4] = new Node( "COL4" );
	}

	protected void tearDown( ) throws Exception
	{
		super.tearDown( );
	}

	/*
	 * Test method for
	 * 'org.eclipse.birt.data.engine.impl.ExprManagerUtil.validateNodes(Node[])'
	 */
	public void testValidateNodes1( ) throws DataException
	{

		ExprManager em = new ExprManager( );
		Map m = new HashMap( );
		m.put( "COL0",
				new ScriptExpression( "row[\"COL1\"]+row[\"COL2\"]+row[\"COL3\"]" ) );
		m.put( "COL2", new ScriptExpression( "dataSetRow[\"COL2\"]" ) );
		m.put( "COL1",
				new ScriptExpression( "row[\"COL2\"]+row[\"COL3\"]+dataSetRow[\"COL3\"]" ) );
		m.put( "COL3", new ScriptExpression( "row[\"COL2\"]+row[\"COL4\"]" ) );
		m.put( "COL4", new ScriptExpression( "row[\"COL2\"]" ) );

		em.addBindingExpr( null, m, 0 );
		try
		{
			ExprManagerUtil.validateColumnBinding( em );
		}
		catch ( DataException e )
		{
			fail( "Should not arrive here" );
		}
	}

	// nested, directly
	public void testValidateNodes2( ) throws DataException
	{

		ExprManager em = new ExprManager( );
		Map m = new HashMap( );
		m.put( "COL0",
				new ScriptExpression( "row[\"COL1\"]+row[\"COL2\"]+row[\"COL3\"]" ) );
		m.put( "COL2", new ScriptExpression( "dataSetRow[\"COL2\"]" ) );
		m.put( "COL1",
				new ScriptExpression( "row[\"COL0\"]+row[\"COL3\"]+dataSetRow[\"COL3\"]" ) );
		m.put( "COL3", new ScriptExpression( "row[\"COL2\"]+row[\"COL4\"]" ) );
		m.put( "COL4", new ScriptExpression( "row[\"COL2\"]" ) );

		em.addBindingExpr( null, m, 0 );
		
		try
		{
			ExprManagerUtil.validateColumnBinding( em );
			fail( "Should not arrive here" );
		}
		catch ( DataException e )
		{
		}
	}

	// nested, indirectly
	public void testValidateNodes3( ) throws DataException
	{

		ExprManager em = new ExprManager( );
		Map m = new HashMap( );
		m.put( "COL0",
				new ScriptExpression( "row[\"COL1\"]+row[\"COL2\"]+row[\"COL3\"]" ) );
		m.put( "COL2", new ScriptExpression( "dataSetRow[\"COL2\"]" ) );
		m.put( "COL1",
				new ScriptExpression( "row[\"COL2\"]+row[\"COL3\"]+dataSetRow[\"COL3\"]" ) );
		m.put( "COL3", new ScriptExpression( "row[\"COL2\"]+row[\"COL4\"]" ) );
		m.put( "COL4", new ScriptExpression( "row[\"COL0\"]" ) );

		em.addBindingExpr( null, m, 0 );
		try
		{
			ExprManagerUtil.validateColumnBinding( em );
			fail( "Should not arrive here" );
		}
		catch ( DataException e )
		{
		}
	}

	// nested, self
	public void testValidateNodes4( ) throws DataException
	{

		ExprManager em = new ExprManager( );
		Map m = new HashMap( );
		m.put( "COL0",
				new ScriptExpression( "row[\"COL1\"]+row[\"COL2\"]+row[\"COL3\"]" ) );
		m.put( "COL2", new ScriptExpression( "dataSetRow[\"COL2\"]" ) );
		m.put( "COL1",
				new ScriptExpression( "row[\"COL1\"]+row[\"COL3\"]+dataSetRow[\"COL3\"]" ) );
		m.put( "COL3", new ScriptExpression( "row[\"COL2\"]+row[\"COL4\"]" ) );
		m.put( "COL4", new ScriptExpression( "row[\"COL0\"]" ) );

		em.addBindingExpr( null, m, 0 );
		try
		{
			ExprManagerUtil.validateColumnBinding( em );
			fail( "Should not arrive here" );
		}
		catch ( DataException e )
		{
		}
	}

	/**
	 * Test valid group keys
	 * @throws DataException
	 */
	public void testValidateNodes5( ) throws DataException
	{
		ExprManager em = new ExprManager( );
		Map m = new HashMap( );
		m.put( "COL0",
				new ScriptExpression( "row[\"COL1\"]+row[\"COL2\"]+row[\"COL3\"]" ) );
		m.put( "COL2", new ScriptExpression( "dataSetRow[\"COL2\"]" ) );
		m.put( "COL1",
				new ScriptExpression( "row[\"COL2\"]+row[\"COL3\"]+dataSetRow[\"COL3\"]" ) );
		m.put( "COL3", new ScriptExpression( "row[\"COL2\"]+row[\"COL4\"]" ) );
		m.put( "COL4", new ScriptExpression( "row[\"COL2\"]" ) );

		em.addBindingExpr( "COL0", m, 1 );
		
		m = new HashMap( );
		m.put( "COL5",
				new ScriptExpression( "row[\"COL1\"]+row[\"COL2\"]+row[\"COL3\"]" ) );
		m.put( "COL6", new ScriptExpression( "dataSetRow[\"COL2\"]" ) );
		m.put( "COL7",
				new ScriptExpression( "row[\"COL2\"]+row[\"COL3\"]+dataSetRow[\"COL3\"]" ) );
		m.put( "COL8", new ScriptExpression( "row[\"COL2\"]+row[\"COL4\"]" ) );
		m.put( "COL9", new ScriptExpression( "row[\"COL8\"]" ) );
		
		em.addBindingExpr( "COL2", m, 2 );
		
		m = new HashMap( );
		m.put( "COL10",
				new ScriptExpression( "row[\"COL1\"]+row[\"COL2\"]+row[\"COL3\"]" ) );
		m.put( "COL11", new ScriptExpression( "dataSetRow[\"COL2\"]" ) );
		m.put( "COL12",
				new ScriptExpression( "row[\"COL2\"]+row[\"COL3\"]+dataSetRow[\"COL3\"]" ) );
		m.put( "COL13", new ScriptExpression( "row[\"COL2\"]+row[\"COL4\"]" ) );
		m.put( "COL14", new ScriptExpression( "row[\"COL8\"]" ) );
		
		em.addBindingExpr( "COL10", m, 3 );
		
		try
		{
			ExprManagerUtil.validateColumnBinding( em );
		}
		catch ( DataException e )
		{
			fail( "Should not arrive here" );
		}
	}
	
	/**
	 * Test invalid group keys. The group key of group 2 directly uses the column binding
	 * defined in group 3.
	 * @throws DataException
	 */
	public void testValidateNodes6( ) throws DataException
	{
		ExprManager em = new ExprManager( );
		Map m = new HashMap( );
		m.put( "COL0",
				new ScriptExpression( "row[\"COL1\"]+row[\"COL2\"]+row[\"COL3\"]" ) );
		m.put( "COL2", new ScriptExpression( "dataSetRow[\"COL2\"]" ) );
		m.put( "COL1",
				new ScriptExpression( "row[\"COL2\"]+row[\"COL3\"]+dataSetRow[\"COL3\"]" ) );
		m.put( "COL3", new ScriptExpression( "row[\"COL2\"]+row[\"COL4\"]" ) );
		m.put( "COL4", new ScriptExpression( "row[\"COL2\"]" ) );

		em.addBindingExpr( "COL0", m, 1 );
		
		m = new HashMap( );
		m.put( "COL5",
				new ScriptExpression( "row[\"COL1\"]+row[\"COL2\"]+row[\"COL3\"]" ) );
		m.put( "COL6", new ScriptExpression( "dataSetRow[\"COL2\"]" ) );
		m.put( "COL7",
				new ScriptExpression( "row[\"COL2\"]+row[\"COL3\"]+dataSetRow[\"COL3\"]" ) );
		m.put( "COL8", new ScriptExpression( "row[\"COL2\"]+row[\"COL4\"]" ) );
		m.put( "COL9", new ScriptExpression( "row[\"COL8\"]" ) );
		
		em.addBindingExpr( "COL10", m, 2 );
		
		m = new HashMap( );
		m.put( "COL10",
				new ScriptExpression( "row[\"COL1\"]+row[\"COL2\"]+row[\"COL3\"]" ) );
		m.put( "COL11", new ScriptExpression( "dataSetRow[\"COL2\"]" ) );
		m.put( "COL12",
				new ScriptExpression( "row[\"COL2\"]+row[\"COL3\"]+dataSetRow[\"COL3\"]" ) );
		m.put( "COL13", new ScriptExpression( "row[\"COL2\"]+row[\"COL4\"]" ) );
		m.put( "COL14", new ScriptExpression( "row[\"COL8\"]" ) );
		
		em.addBindingExpr( "COL10", m, 3 );
		
		try
		{
			ExprManagerUtil.validateColumnBinding( em );
			fail( "Should not arrive here" );
		}
		catch ( DataException e )
		{
		}
	}

	/**
	 * Test valid group column bindings. One non-key column binding  of group 2 directly uses the column binding
	 * defined in group 3.
	 * @throws DataException
	 */
	public void testValidateNodes7( ) throws DataException
	{
		ExprManager em = new ExprManager( );
		Map m = new HashMap( );
		m.put( "COL0",
				new ScriptExpression( "row[\"COL1\"]+row[\"COL2\"]+row[\"COL3\"]" ) );
		m.put( "COL2", new ScriptExpression( "dataSetRow[\"COL2\"]" ) );
		m.put( "COL1",
				new ScriptExpression( "row[\"COL2\"]+row[\"COL3\"]+dataSetRow[\"COL3\"]" ) );
		m.put( "COL3", new ScriptExpression( "row[\"COL2\"]+row[\"COL4\"]" ) );
		m.put( "COL4", new ScriptExpression( "row[\"COL2\"]" ) );

		em.addBindingExpr( "COL0", m, 1 );
		
		m = new HashMap( );
		m.put( "COL5",
				new ScriptExpression( "row[\"COL1\"]+row[\"COL2\"]+row[\"COL3\"]" ) );
		m.put( "COL6", new ScriptExpression( "dataSetRow[\"COL2\"]" ) );
		m.put( "COL7",
				new ScriptExpression( "row[\"COL2\"]+row[\"COL3\"]+dataSetRow[\"COL3\"]" ) );
		m.put( "COL8", new ScriptExpression( "row[\"COL2\"]+row[\"COL4\"]" ) );
		m.put( "COL9", new ScriptExpression( "row[\"COL10\"]" ) );
		
		em.addBindingExpr( "COL5", m, 2 );
		
		m = new HashMap( );
		m.put( "COL10",
				new ScriptExpression( "row[\"COL1\"]+row[\"COL2\"]+row[\"COL3\"]" ) );
		m.put( "COL11", new ScriptExpression( "dataSetRow[\"COL2\"]" ) );
		m.put( "COL12",
				new ScriptExpression( "row[\"COL2\"]+row[\"COL3\"]+dataSetRow[\"COL3\"]" ) );
		m.put( "COL13", new ScriptExpression( "row[\"COL2\"]+row[\"COL4\"]" ) );
		m.put( "COL14", new ScriptExpression( "row[\"COL8\"]" ) );
		
		em.addBindingExpr( "COL10", m, 3 );
		
		try
		{
			ExprManagerUtil.validateColumnBinding( em );
		}
		catch ( DataException e )
		{
			fail( "Should not arrive here" );
		}
	}
	
	/**
	 * Test invalid group key. The key of group 2 directly uses the column binding
	 * defined in group 3.
	 * @throws DataException
	 */
	public void testValidateNodes8( ) throws DataException
	{
		ExprManager em = new ExprManager( );
		Map m = new HashMap( );
		m.put( "COL0",
				new ScriptExpression( "row[\"COL1\"]+row[\"COL2\"]+row[\"COL3\"]" ) );
		m.put( "COL2", new ScriptExpression( "dataSetRow[\"COL2\"]" ) );
		m.put( "COL1",
				new ScriptExpression( "row[\"COL2\"]+row[\"COL3\"]+dataSetRow[\"COL3\"]" ) );
		m.put( "COL3", new ScriptExpression( "row[\"COL2\"]+row[\"COL4\"]" ) );
		m.put( "COL4", new ScriptExpression( "row[\"COL2\"]" ) );

		em.addBindingExpr( "COL0", m, 1 );
		
		m = new HashMap( );
		m.put( "COL5",
				new ScriptExpression( "row[\"COL1\"]+row[\"COL11\"]+row[\"COL3\"]" ) );
		m.put( "COL6", new ScriptExpression( "dataSetRow[\"COL2\"]" ) );
		m.put( "COL7",
				new ScriptExpression( "row[\"COL2\"]+row[\"COL3\"]+dataSetRow[\"COL3\"]" ) );
		m.put( "COL8", new ScriptExpression( "row[\"COL2\"]+row[\"COL4\"]" ) );
		m.put( "COL9", new ScriptExpression( "row[\"COL10\"]" ) );
		
		em.addBindingExpr( "COL5", m, 2 );
		
		m = new HashMap( );
		m.put( "COL10",
				new ScriptExpression( "row[\"COL1\"]+row[\"COL2\"]+row[\"COL3\"]" ) );
		m.put( "COL11", new ScriptExpression( "dataSetRow[\"COL2\"]" ) );
		m.put( "COL12",
				new ScriptExpression( "row[\"COL2\"]+row[\"COL3\"]+dataSetRow[\"COL3\"]" ) );
		m.put( "COL13", new ScriptExpression( "row[\"COL2\"]+row[\"COL4\"]" ) );
		m.put( "COL14", new ScriptExpression( "row[\"COL8\"]" ) );
		
		em.addBindingExpr( "COL10", m, 3 );
		
		try
		{
			ExprManagerUtil.validateColumnBinding( em );
			fail( "Should not arrive here" );
		}
		catch ( DataException e )
		{
		}
	}

	/**
	 * Test invalid group key. The key of group 1 directly uses the column binding
	 * defined in group 3.
	 * @throws DataException
	 */
	public void testValidateNodes9( ) throws DataException
	{
		ExprManager em = new ExprManager( );
		Map m = new HashMap( );
		m.put( "COL0",
				new ScriptExpression( "row[\"COL1\"]+row[\"COL11\"]+row[\"COL3\"]" ) );
		m.put( "COL2", new ScriptExpression( "dataSetRow[\"COL2\"]" ) );
		m.put( "COL1",
				new ScriptExpression( "row[\"COL2\"]+row[\"COL3\"]+dataSetRow[\"COL3\"]" ) );
		m.put( "COL3", new ScriptExpression( "row[\"COL2\"]+row[\"COL4\"]" ) );
		m.put( "COL4", new ScriptExpression( "row[\"COL11\"]" ) );

		em.addBindingExpr( "COL0", m, 1 );
		
		m = new HashMap( );
		m.put( "COL5",
				new ScriptExpression( "row[\"COL1\"]+row[\"COL11\"]+row[\"COL3\"]" ) );
		m.put( "COL6", new ScriptExpression( "dataSetRow[\"COL2\"]" ) );
		m.put( "COL7",
				new ScriptExpression( "row[\"COL2\"]+row[\"COL3\"]+dataSetRow[\"COL3\"]" ) );
		m.put( "COL8", new ScriptExpression( "row[\"COL2\"]+row[\"COL4\"]" ) );
		m.put( "COL9", new ScriptExpression( "row[\"COL10\"]" ) );
		
		em.addBindingExpr( "COL5", m, 2 );
		
		m = new HashMap( );
		m.put( "COL10",
				new ScriptExpression( "row[\"COL1\"]+row[\"COL2\"]+row[\"COL3\"]" ) );
		m.put( "COL11", new ScriptExpression( "dataSetRow[\"COL2\"]" ) );
		m.put( "COL12",
				new ScriptExpression( "row[\"COL2\"]+row[\"COL3\"]+dataSetRow[\"COL3\"]" ) );
		m.put( "COL13", new ScriptExpression( "row[\"COL2\"]+row[\"COL4\"]" ) );
		m.put( "COL14", new ScriptExpression( "row[\"COL8\"]" ) );
		
		em.addBindingExpr( "COL10", m, 3 );
		
		try
		{
			ExprManagerUtil.validateColumnBinding( em );
			fail( "Should not arrive here" );
		}
		catch ( DataException e )
		{
		}
	}
	
	/*
	 * Test method for
	 * 'org.eclipse.birt.data.engine.impl.ExprManagerUtil.validateNodes(Node[])'
	 */
	public void testValidateNodes10( ) throws DataException
	{

		ExprManager em = new ExprManager( );
		Map m = new HashMap( );
		m.put( "COL0",
				new ScriptExpression( "row[\"COL1\"]+row[\"COL2\"]+row[\"COL3\"]" ) );
		m.put( "COL2", new ScriptExpression( "dataSetRow[\"COL2\"]" ) );
		m.put( "COL1",
				new ScriptExpression( "row[\"COL2\"]+row[\"COL3\"]+dataSetRow[\"COL3\"]" ) );
		m.put( "COL3", new ScriptExpression( "row[\"COL2\"]+row[\"COL4\"]" ) );
		m.put( "COL4", new ScriptExpression( "row[\"COL2\"]" ) );
		m.put( "COL5", new ConditionalExpression( "row[\"COL2\"]", IConditionalExpression.OP_EQ,"2" ));

		em.addBindingExpr( null, m, 0 );
		try
		{
			ExprManagerUtil.validateColumnBinding( em );
		}
		catch ( DataException e )
		{
			fail( "Should not arrive here" );
		}
	}
	
	/*
	 * Test method for
	 * 'org.eclipse.birt.data.engine.impl.ExprManagerUtil.validateNodes(Node[])'
	 */
	public void testValidateNodes11( ) throws DataException
	{

		ExprManager em = new ExprManager( );
		Map m = new HashMap( );
		m.put( "COL0",
				new ScriptExpression( "row[\"COL1\"]+row[\"COL2\"]+row[\"COL3\"]" ) );
		m.put( "COL2", new ScriptExpression( "dataSetRow[\"COL2\"]" ) );
		m.put( "COL1",
				new ScriptExpression( "row[\"COL2\"]+row[\"COL3\"]+dataSetRow[\"COL3\"]" ) );
		m.put( "COL3", new ScriptExpression( "row[\"COL2\"]+row[\"COL4\"]" ) );
		m.put( "COL4", new ScriptExpression( "row[\"COL2\"]" ) );
		m.put( "COL5", new ConditionalExpression( "row[\"COL5\"]", IConditionalExpression.OP_EQ,"2" ));

		em.addBindingExpr( null, m, 0 );
		try
		{
			ExprManagerUtil.validateColumnBinding( em );
			fail( "Should not arrive here" );
		}
		catch ( DataException e )
		{
			
		}
	}
	
	/*
	 * Test method for
	 * 'org.eclipse.birt.data.engine.impl.ExprManagerUtil.validateNodes(Node[])'
	 */
	public void testValidateNodes12( ) throws DataException
	{

		ExprManager em = new ExprManager( );
		Map m = new HashMap( );
		m.put( "COL0",
				new ScriptExpression( "11111row[\"COL1\"]+row[\"COL2\"]+row[\"COL3\"]" ) );
		m.put( "COL2", new ScriptExpression( "dataSetRow[\"COL2\"]" ) );
		m.put( "COL1",
				new ScriptExpression( "row[\"COL2\"]+row[\"COL3\"]+dataSetRow[\"COL3\"]" ) );
		m.put( "COL3", new ScriptExpression( "row[\"COL2\"]+row[\"COL4\"]" ) );
		m.put( "COL4", new ScriptExpression( "row[\"COL2\"]" ) );

		em.addBindingExpr( null, m, 0 );
		try
		{
			ExprManagerUtil.validateColumnBinding( em );
		}
		catch ( DataException e )
		{
			fail( "Should not arrive here" );
		}
	}
	
	/**
	 * Test reference to not exist column binding in an expression.
	 * 
	 * @throws DataException
	 */
	public void testValidateNodes13( ) throws DataException
	{

		ExprManager em = new ExprManager( );
		Map m = new HashMap( );
		m.put( "COL0", new ScriptExpression( "row[\"COL1\"]" ) );
		m.put( "COL1", new ScriptExpression( "row[\"COL2\"]" ) );
		m.put( "COL2", new ScriptExpression( "row[\"COL3\"]" ) );
		m.put( "COL3", new ScriptExpression( "row[\"COL4\"]" ) );
		m.put( "COL4", new ScriptExpression( "row[\"COL\"]" ) );

		em.addBindingExpr( null, m, 0 );
		try
		{
			ExprManagerUtil.validateColumnBinding( em );
			fail( "Should not arrive here" );
		}
		catch ( DataException e )
		{
			e.printStackTrace( );
		}
	}
}
