/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.oda.pojo.impl;

import java.io.File;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.eclipse.birt.data.oda.pojo.api.Constants;
import org.eclipse.birt.data.oda.pojo.impl.ResultSet;
import org.eclipse.birt.data.oda.pojo.input.pojos.Course;
import org.eclipse.birt.data.oda.pojo.input.pojos.CustomTeacherDataSet;
import org.eclipse.birt.data.oda.pojo.input.pojos.Student;
import org.eclipse.birt.data.oda.pojo.input.pojos.Teacher;
import org.eclipse.birt.data.oda.pojo.input.pojos.TeacherDataSet;
import org.eclipse.birt.data.oda.pojo.input.pojos.TeacherStudent;
import org.eclipse.birt.data.oda.pojo.input.pojos.TeacherStudentCourse;
import org.eclipse.birt.data.oda.pojo.querymodel.ClassColumnMappings;
import org.eclipse.birt.data.oda.pojo.querymodel.Column;
import org.eclipse.birt.data.oda.pojo.querymodel.MethodSource;
import org.eclipse.birt.data.oda.pojo.querymodel.OneColumnMapping;
import org.eclipse.birt.data.oda.pojo.querymodel.PojoQuery;
import org.eclipse.birt.data.oda.pojo.testutil.PojoInstancesUtil;
import org.eclipse.birt.data.oda.pojo.testutil.PojoQueryCreator;
import org.eclipse.birt.data.oda.pojo.util.PojoQueryWriter;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.IResultSet;
import org.eclipse.datatools.connectivity.oda.OdaException;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

/**
 * 
 */

public class ResultSetTest {

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
@SuppressWarnings("unchecked")
	@Test
    public void testPojosFromAppContext( ) throws OdaException
	{
		PojoQuery query = PojoQueryCreator.createWithA1tonMap( );
		List<Teacher> ts = PojoInstancesUtil.createTeachers( );
		Map appContext = new HashMap( );
		appContext.put( query.getAppContextKey( ), ts );
		Connection conn = new Connection();	
		conn.open(null);
		IQuery q = conn.newQuery(null);
		q.setAppContext( appContext );
		q.prepare( PojoQueryWriter.write( query ) );
		ResultSet rs = (ResultSet)q.executeQuery( );

		List<TeacherStudent> studentCompound = PojoInstancesUtil.getTeacherStudentCompound( ts );
		verifyResultSetTeacherStudent( rs, studentCompound );
		rs.close( );
	}
	@Test
    public void testPojosFromPojoDataSetClass( ) throws OdaException, URISyntaxException
	{
		PojoQuery query = PojoQueryCreator.createWithA1tonMap( );
		query.setDataSetClass( TeacherDataSet.class.getName( ) );
		List<Teacher> ts = new TeacherDataSet( ).getTeachers( );
		Properties prop = new Properties();
		prop.setProperty(Constants.POJO_DATA_SET_CLASS_PATH, getAbsolutePath( ));
		Connection conn = new Connection();
		conn.open(prop);
		IQuery q = conn.newQuery(null);
		q.prepare( PojoQueryWriter.write( query ) );
		ResultSet rs = (ResultSet)q.executeQuery( );

		List<TeacherStudent> studentCompound = PojoInstancesUtil.getTeacherStudentCompound( ts );
		verifyResultSetTeacherStudent( rs, studentCompound );
		rs.close( );
	}
	
	@SuppressWarnings("nls")
	@Test
    public void testQueryWithParameters( ) throws OdaException, URISyntaxException
	{
		PojoQuery query = PojoQueryCreator.createWithParameters( );
		query.setDataSetClass( TeacherDataSet.class.getName( ) );
		Properties prop = new Properties();
		prop.setProperty(Constants.POJO_DATA_SET_CLASS_PATH, getAbsolutePath( ));
		Connection conn = new Connection();
		conn.open(prop);
		IQuery q = conn.newQuery(null);
		q.prepare( PojoQueryWriter.write( query ) );
		q.setBoolean( "sex", false );
		q.setInt( "id", 1 );
		IResultSet rs = q.executeQuery( );
		List<Teacher> ts = new TeacherDataSet( ).getTeachers( );
		for ( Teacher t : ts )
		{
			Student[] ss = t.getStudents( 18, false, null );
			Student s_1 = t.getStudentById( 1 );
			if ( ss == null || ss.length == 0 )
			{
				rs.next( );
				if ( s_1 != null )
				{
					assertEquals( s_1.getId( ), rs.getInt( "StudentId_Param" ) );
				}
			}
			else
			{
				for ( Student s : ss )
				{
					rs.next( );
					assertEquals( s.getId( ), rs.getInt( "StudentId" ) );
					assertEquals( s.getName( ), rs.getString( "StudentName" ) );
					if ( s_1 != null )
					{
						assertEquals( s_1.getId( ), rs.getInt( "StudentId_Param" ) );
					}
				}
			}
		}
		assertFalse( rs.next( ) );
		rs.close( );
		q.close( );
	}
	@Test
    public void testPojosFromCustomDataSetClass( ) throws OdaException
	{
		PojoQuery query = PojoQueryCreator.createWithA1tonMap( );
		query.setDataSetClass( CustomTeacherDataSet.class.getName( ) );
		List<Teacher> ts = new TeacherDataSet( ).getTeachers( );
		Properties prop = new Properties();
		prop.setProperty(Constants.POJO_DATA_SET_CLASS_PATH, getAbsolutePath( ));
		Connection conn = new Connection();
		conn.open(prop);
		IQuery q = conn.newQuery(null);
		q.prepare( PojoQueryWriter.write( query ) );
		ResultSet rs = (ResultSet)q.executeQuery( );

		List<TeacherStudent> studentCompound = PojoInstancesUtil.getTeacherStudentCompound( ts );
		verifyResultSetTeacherStudent( rs, studentCompound );
		rs.close( );
	}
	@Test
    public void testCheckNoPojoProvider( ) throws OdaException
	{
		PojoQuery query = PojoQueryCreator.createWithA1tonMap( );

		ResultSet rs = null;
		try
		{
			Connection conn = new Connection();	
			conn.open(null);
			IQuery q = conn.newQuery(null);
			q.prepare( PojoQueryWriter.write( query ) );
			rs = (ResultSet)q.executeQuery( );
			rs.next( );
			assertTrue( false );
		}
		catch ( OdaException e )
		{
			assertTrue( true );
		}
		finally
		{
			if ( rs != null )
			{
				rs.close( );
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Test
    public void testMulti1TonMaps( ) throws OdaException
	{
		PojoQuery query = PojoQueryCreator.createWithMulti1tonMaps( );
		List<Teacher> ts = PojoInstancesUtil.createTeachers( );

		// add courses for each
		Map appContext = new HashMap( );
		appContext.put( query.getAppContextKey( ), ts );
		
		Connection conn = new Connection();
		conn.open(null);
		IQuery q = conn.newQuery(null);
		q.setAppContext( appContext );
		q.prepare( PojoQueryWriter.write( query ) );
		ResultSet rs = (ResultSet)q.executeQuery( );

		List<TeacherStudentCourse> compounds = PojoInstancesUtil.getTeacherStudentCourseCompound( ts );
		verifyResultSetTeacherStudentCourse( rs, compounds );
		rs.close( );
	}

	@SuppressWarnings({
			"unchecked", "nls"
	})
	@Test
    public void testCheckInvalidMulti1TonMaps( ) throws OdaException
	{
		PojoQuery query = PojoQueryCreator.createWithA1tonMap( );

		// Add 1 teacher to n courses map
		ClassColumnMappings ccm = new ClassColumnMappings( new MethodSource( "getCourses", null ) );
		ccm.addColumnsMapping( new OneColumnMapping( new MethodSource( "getId", null ),
				new Column( "CourseId", "Integer", 7 ) ) );
		query.addColumnsMapping( ccm );

		List<Teacher> ts = PojoInstancesUtil.createTeachers( );
		Map appContext = new HashMap( );
		appContext.put( query.getAppContextKey( ), ts );

		// All Teachers currently has no course, so in fact, no 2 1-to-maps are
		// checked during runtime.
		// ResultSet should works
		Connection conn = new Connection();
		conn.open(null);
		IQuery q = conn.newQuery(null);
		q.setAppContext( appContext );
		q.prepare( PojoQueryWriter.write( query ) );
		ResultSet rs = (ResultSet)q.executeQuery( );
		List<TeacherStudent> studentCompound = PojoInstancesUtil.getTeacherStudentCompound( ts );
		verifyResultSetTeacherStudent( rs, studentCompound );
		rs.close( );

		// Add one course for the 2rd teacher
		ts.get( 1 ).addCourse( new Course( 1, "c1" ) );
		try
		{
			q = conn.newQuery(null);
			q.setAppContext( appContext );
			q.prepare( PojoQueryWriter.write( query ) );
			rs = (ResultSet)q.executeQuery( );


			while ( rs.next( ) )
			{
			}
			// Two not transitional 1-to-n maps will eventually checked during
			// runtime
			assertTrue( false );
		}
		catch ( OdaException e )
		{
			// Invalid Multiple 1-to-n Maps Exception should be caught
			assertTrue( true );
		}
		rs.close( );
	}

	private void verifyResultSetTeacherStudent( ResultSet rs,
			List<TeacherStudent> compounds ) throws OdaException
	{
		int expectedRowCount = compounds.size( );

		int rowCount = 0;
		while ( rs.next( ) )
		{
			TeacherStudent ts = compounds.get( rowCount );
			Integer teacherId = (Integer) rs.getColumnValue( 1 );
			String teacherName = (String) rs.getColumnValue( 2 );
			Integer teacherAge = (Integer) rs.getColumnValue( 3 );
			assertEquals( ts.teacher.getId( ), teacherId.intValue( ) );
			assertEquals( ts.teacher.getName( ), teacherName );
			assertEquals( ts.teacher.age, teacherAge.intValue( ) );

			if ( ts.teacher.getDean( ) == null )
			{
				Integer deanId = (Integer) rs.getColumnValue( 4 );
				assertEquals( null, deanId );
				assertTrue( rs.wasNull( ) );

				String deanName = (String) rs.getColumnValue( 5 );
				assertEquals( null, deanName );
				assertTrue( rs.wasNull( ) );
			}
			else
			{
				Integer deanId = (Integer) rs.getColumnValue( 4 );
				String deanName = (String) rs.getColumnValue( 5 );
				assertEquals( ts.teacher.getDean( ).getId( ), deanId.intValue( ) );
				assertEquals( ts.teacher.getDean( ).getName( ), deanName );
			}

			if ( ts.student == null )
			{
				Integer studentId = (Integer) rs.getColumnValue( 6 );
				assertEquals( null, studentId );
				assertTrue( rs.wasNull( ) );

				String studentName = (String) rs.getColumnValue( 7 );
				assertEquals( null, studentName );
				assertTrue( rs.wasNull( ) );
			}
			else
			{
				Integer studentId = (Integer) rs.getColumnValue( 6 );
				String studentName = (String) rs.getColumnValue( 7 );
				assertEquals( ts.student.getId( ), studentId.intValue( ) );
				assertEquals( ts.student.getName( ), studentName );
			}

			rowCount++;
		}
		assertEquals( expectedRowCount, rowCount );
	}

	private void verifyResultSetTeacherStudentCourse( ResultSet rs,
			List<TeacherStudentCourse> compounds ) throws OdaException
	{
		int expectedRowCount = compounds.size( );

		int rowCount = 0;
		while ( rs.next( ) )
		{
			TeacherStudentCourse tsc = compounds.get( rowCount );
			Integer teacherId = (Integer) rs.getColumnValue( 1 );
			String teacherName = (String) rs.getColumnValue( 2 );
			Integer teacherAge = (Integer) rs.getColumnValue( 3 );
			assertEquals( tsc.teacher.getId( ), teacherId.intValue( ) );
			assertEquals( tsc.teacher.getName( ), teacherName );
			assertEquals( tsc.teacher.age, teacherAge.intValue( ) );

			if ( tsc.teacher.getDean( ) == null )
			{
				Integer deanId = (Integer) rs.getColumnValue( 4 );
				assertEquals( null, deanId );
				assertTrue( rs.wasNull( ) );

				String deanName = (String) rs.getColumnValue( 5 );
				assertEquals( null, deanName );
				assertTrue( rs.wasNull( ) );
			}
			else
			{
				Integer deanId = (Integer) rs.getColumnValue( 4 );
				String deanName = (String) rs.getColumnValue( 5 );
				assertEquals( tsc.teacher.getDean( ).getId( ),
						deanId.intValue( ) );
				assertEquals( tsc.teacher.getDean( ).getName( ), deanName );
			}

			if ( tsc.student == null )
			{
				Integer studentId = (Integer) rs.getColumnValue( 6 );
				assertEquals( null, studentId );
				assertTrue( rs.wasNull( ) );

				String studentName = (String) rs.getColumnValue( 7 );
				assertEquals( null, studentName );
				assertTrue( rs.wasNull( ) );
			}
			else
			{
				Integer studentId = (Integer) rs.getColumnValue( 6 );
				String studentName = (String) rs.getColumnValue( 7 );
				assertEquals( tsc.student.getId( ), studentId.intValue( ) );
				assertEquals( tsc.student.getName( ), studentName );
			}

			if ( tsc.course == null )
			{
				Integer courseId = (Integer) rs.getColumnValue( 8 );
				assertEquals( null, courseId );
				assertTrue( rs.wasNull( ) );

				String courseName = (String) rs.getColumnValue( 9 );
				assertEquals( null, courseName );
				assertTrue( rs.wasNull( ) );
			}
			else
			{
				Integer courseId = (Integer) rs.getColumnValue( 8 );
				String courseName = (String) rs.getColumnValue( 9 );
				assertEquals( tsc.course.getId( ), courseId.intValue( ) );
				assertEquals( tsc.course.getName( ), courseName );
			}

			rowCount++;
		}
		assertEquals( expectedRowCount, rowCount );
	}

	@SuppressWarnings("unchecked")
	@Test
    public void testMaxRows( ) throws OdaException
	{
		PojoQuery query = PojoQueryCreator.createWithA1tonMap( );
		List<Teacher> ts = PojoInstancesUtil.createTeachers( );
		Map appContext = new HashMap( );
		appContext.put( query.getAppContextKey( ), ts );

		Connection conn = new Connection();	
		conn.open(null);
		IQuery q = conn.newQuery(null);
		q.setAppContext( appContext );
		q.prepare( PojoQueryWriter.write( query ) );
		ResultSet rs = (ResultSet)q.executeQuery( );

		List<TeacherStudent> studentCompound = PojoInstancesUtil.getTeacherStudentCompound( ts );
		int maxRows = studentCompound.size( ) - 2;
		rs.setMaxRows( maxRows );

		int rowCount = 0;
		while ( rs.next( ) )
		{
			rowCount++;
		}
		assertEquals( maxRows, rowCount );
		rs.close( );
	}

	@SuppressWarnings("nls")
	private String getAbsolutePath( )
	{
		java.security.ProtectionDomain pd = TeacherDataSet.class.getProtectionDomain( );
		if ( pd == null )
			return null;
		java.security.CodeSource cs = pd.getCodeSource( );
		if ( cs == null )
			return null;
		java.net.URL url = cs.getLocation( );
		if ( url == null )
			return null;
		java.io.File f = new File( url.getFile( ) );
		if ( f == null )
			return null;
		String result = f.getAbsolutePath( );
		
		//when run in "JUnit Plugin Test", not locate to the "bin" dir
		if ( !result.endsWith( "bin" ) )
		{
			result += "\\bin";
		}
		return result;
	}
}
