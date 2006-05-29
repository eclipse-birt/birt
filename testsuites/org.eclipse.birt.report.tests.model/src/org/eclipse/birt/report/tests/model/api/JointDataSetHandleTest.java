package org.eclipse.birt.report.tests.model.api;

import java.util.List;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSetParameterHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.IllegalOperationException;
import org.eclipse.birt.report.model.api.JoinConditionHandle;
import org.eclipse.birt.report.model.api.JointDataSetHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.DataSet;
import org.eclipse.birt.report.model.elements.JointDataSet;
import org.eclipse.birt.report.model.elements.interfaces.IDataSetModel;
import org.eclipse.birt.report.model.elements.interfaces.IJointDataSetModel;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

public class JointDataSetHandleTest extends BaseTestCase{

	private String filename= "JointDataSetHandleTest.xml";
	private String filename1= "JointDataSetHandleTest_1.xml";
	private String filename2= "JointDataSetHandleTest_2.xml";

	
	public JointDataSetHandleTest(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public static Test suite()
    {
		
		return new TestSuite(JointDataSetHandleTest.class);
	}
	protected void setUp( ) throws Exception
	{
		super.setUp( );
	}
	public void testAddDataSet( ) throws Exception
	{
	    openDesign(filename);
		JointDataSetHandle jointds = designHandle.findJointDataSet("JointDataSet");
		//add data set to jointdataset
		jointds.addDataSet("ds1");
		jointds.addDataSet("ds2");
		assertEquals(2,jointds.getListProperty(JointDataSet.DATA_SETS_PROP).size());
		
		//remove data set from jointdataset
		jointds.removeDataSet("ds2");
		assertEquals(1,jointds.getListProperty(JointDataSet.DATA_SETS_PROP).size());
	}
	public void testJointDataSetType( ) throws Exception
	{
		openDesign(filename1);
		JointDataSetHandle jointds1 = designHandle.findJointDataSet("jointds1");
		
		//heterogeneous data sets
		jointds1.addDataSet("ds1");
		jointds1.addDataSet("ds2");
        assertEquals(2,jointds1.getListProperty(JointDataSet.DATA_SETS_PROP).size());	
		
		//homogeneous data sets
		JointDataSetHandle jointds2 = designHandle.findJointDataSet("jointds2");
		jointds2.addDataSet("ds1");
		jointds2.addDataSet("ds2");
		assertEquals(2,jointds2.getListProperty(JointDataSet.DATA_SETS_PROP).size());
		
		//self-join
		JointDataSetHandle jointds3 = designHandle.findJointDataSet("jointds3");
		jointds3.addDataSet("ds1");
		assertEquals(1,jointds3.getListProperty(JointDataSet.DATA_SETS_PROP).size());
		Iterator joinConditionsIterator = jointds3.joinConditionsIterator( );
		JoinConditionHandle joinConditionHandle = (JoinConditionHandle) joinConditionsIterator
				.next( );
		joinConditionHandle.setLeftDataSet("ds1");
		joinConditionHandle.setLeftDataSet("ds2");
	}
	public void testJointCondition( ) throws SemanticException, DesignFileException
	{
		openDesign(filename);
		JointDataSetHandle jointds = designHandle.findJointDataSet("JointDataSet");
		Iterator joinConditionsIterator = jointds.joinConditionsIterator( );
		JoinConditionHandle joinConditionHandle = (JoinConditionHandle) joinConditionsIterator
				.next( );
		joinConditionHandle.setJoinType( DesignChoiceConstants.JOIN_TYPE_LEFT_OUT );
        assertEquals( DesignChoiceConstants.JOIN_TYPE_LEFT_OUT,
		joinConditionHandle.getJoinType( ) );

       String operator = DesignChoiceConstants.JOIN_OPERATOR_EQALS;
       joinConditionHandle.setOperator( operator );
       assertEquals( operator, joinConditionHandle.getOperator( ) );

       String leftDataSet = "ds1"; //$NON-NLS-1$
       joinConditionHandle.setLeftDataSet( leftDataSet );
       assertEquals( leftDataSet, joinConditionHandle.getLeftDataSet( ) );

       String rightDataSet = "ds2"; //$NON-NLS-1$
       joinConditionHandle.setRightDataSet( rightDataSet );
       assertEquals( rightDataSet, joinConditionHandle.getRightDataSet( ) );

       String leftExpression = "leftExpression"; //$NON-NLS-1$
       joinConditionHandle.setLeftExpression( leftExpression );
       assertEquals( leftExpression, joinConditionHandle.getLeftExpression( ) );

       String rightExpression = "rightExpression"; //$NON-NLS-1$
       joinConditionHandle.setRightExpression( rightExpression );
       assertEquals( rightExpression, joinConditionHandle
		.getRightExpression( ) );
		
	}
	public void testParameter( ) throws Exception
	{
		openDesign(filename2);
		JointDataSetHandle jointds = designHandle.findJointDataSet("jointds");
        DataSetHandle ds1 = designHandle.findDataSet("ds1");
        DataSetHandle ds2 = designHandle.findDataSet("ds2");
	    DataSetParameterHandle param1 = (DataSetParameterHandle)ds1.parametersIterator().next();
	    DataSetParameterHandle param2 = (DataSetParameterHandle)ds2.parametersIterator().next();
	    
		jointds.addDataSet("ds1");
		jointds.addDataSet("ds2");
		assertFalse( jointds.paramBindingsIterator( ).hasNext( ) ); //$NON-NLS-1$
		
		List list = jointds.getListProperty(JointDataSet.DATA_SETS_PROP);
		
	    assertSame(design.findDataSet("ds1"),((ElementRefValue)list.get(0)).getElement());
	    assertSame(design.findDataSet("ds2"),((ElementRefValue)list.get(1)).getElement());
	    
	    assertEquals("param1",param1.getName());
		assertEquals("param1",param2.getName());
	}
	public void testFilter( ) throws Exception
	{
		SessionHandle session = DesignEngine.newSession( ULocale.ENGLISH );
		designHandle = session.createDesign();
		ElementFactory factory = new ElementFactory(designHandle.getModule());
		StructureFactory structFactory = new StructureFactory( );
		
		JointDataSetHandle jointds = factory.newJointDataSet("jointds");
		FilterCondition filter = structFactory.createFilterCond( );
				
		filter.setExpr("row[\"abc\"]");
		filter.setOperator(DesignChoiceConstants.FILTER_OPERATOR_BETWEEN);
		filter.setValue1("a");
		filter.setValue2("b");
		
		assertEquals("row[\"abc\"]",filter.getExpr());
		assertEquals(DesignChoiceConstants.FILTER_OPERATOR_BETWEEN,filter.getOperator());
		assertEquals("a",filter.getValue1());
		assertEquals("b",filter.getValue2());
		
		PropertyHandle prophandle = (PropertyHandle)jointds.getPropertyHandle( JointDataSetHandle.FILTER_PROP );
		prophandle.addItem( filter );
	}
	
}
