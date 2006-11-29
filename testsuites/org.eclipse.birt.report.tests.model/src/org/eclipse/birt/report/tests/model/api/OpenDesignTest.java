package org.eclipse.birt.report.tests.model.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

public class OpenDesignTest extends BaseTestCase
{
	String fileName = "BlankReport.xml"; //$NON-NLS-1$

	private String nofileName = "NoExisting.xml"; //$NON-NLS-1$
	
	String noexistingFileName= this.getFullQualifiedClassName( ) + "/" + INPUT_FOLDER + "/" + nofileName;

	
	public OpenDesignTest(String name) 
	{	
		super(name);
	}
    public static Test suite()
    {
		
		return new TestSuite(OpenDesignTest.class);
	}
	protected void setUp( ) throws Exception
	{
		super.setUp( );
		removeResource( );
		copyResource_INPUT( fileName, fileName );
	}
	public void tearDown( )
	{
		removeResource( );
	}
	
//	Open a design with absolute path string filename and inputstream
	public void testOpendesign1( ) throws Exception 
	{
		openDesign( fileName );
		File file = new File(this.getFullQualifiedClassName( ) + "/" + INPUT_FOLDER + "/" + fileName );
		
		InputStream is = new FileInputStream( file );
		assertTrue( is != null );
		
		String s1 = this.getFullQualifiedClassName( ) + "/" + INPUT_FOLDER + "/" + fileName;
		sessionHandle.openDesign( s1, is );
	}
	
	
	
	
	
//	Open a design with relative path string filename and inputstream
	public void testOpendesign2( ) throws Exception 
	{
		openDesign( fileName );
		File file = new File( this.getFullQualifiedClassName( ) + "/" + INPUT_FOLDER + "/" + fileName );
		
		InputStream is = new FileInputStream( file );
		assertTrue( is != null );
		String s2 = "BlankReport.xml"; //$NON-NLS-1$
		openDesign( s2 , is );
	}
	
	
	
	
	
//	Open a design with URI and inputstream
	public void testOpendesign3( ) throws Exception 
	{
		openDesign( fileName );
		File file = new File( this.getFullQualifiedClassName( ) + "/" + INPUT_FOLDER + "/" + fileName );
		
		InputStream is = new FileInputStream( file );
		assertTrue( is != null );
		String s3 = this.getFullQualifiedClassName( ) + "/" + INPUT_FOLDER + "/" + fileName; //$NON-NLS-1$
		openDesign( s3 , is );
	}
	
	
	
	
	
	
//	Open a no-existing design with absolute path string filename and inputstream
	public void testOpendesign4( ) throws Exception 
	{
		openDesign( fileName );
		File file = new File( this.getFullQualifiedClassName( ) + "/" + INPUT_FOLDER + "/" + fileName );
		
		InputStream is = new FileInputStream( file );
		assertTrue( is != null );
		
		String s4 = this.getFullQualifiedClassName( ) + "/" + INPUT_FOLDER + "/" + noexistingFileName;
		try
		{
			openDesign( s4 , is );
		
		}
		catch( DesignFileException e )
		{
			assertEquals( DesignFileException.DESIGN_EXCEPTION_INVALID_XML , e.getErrorCode() );
		}
	}
	
	
	
//	Open a no-existing design with relative path string filename and inputstream
	public void testOpendesign5( ) throws Exception 
	{
		openDesign( fileName );
		File file = new File( this.getFullQualifiedClassName( ) + "/" + INPUT_FOLDER + "/" + fileName );
		
		InputStream is = new FileInputStream( file );
		assertTrue( is != null );
		
		String s5 = "noput"; //$NON-NLS-1$
		
		try
		{
			openDesign( s5 , is );
		
		}
		catch( DesignFileException e )
		{
			assertEquals( DesignFileException.DESIGN_EXCEPTION_INVALID_XML , e.getErrorCode() );
		}
	}
	
	
	
	
	
	
//	Open a no-existing design with URI and inputstream
	public void testOpendesign6( ) throws Exception 
	{
		openDesign( fileName );
		File file = new File( this.getFullQualifiedClassName( ) + "/" + INPUT_FOLDER + "/" + fileName );
		
		InputStream is = new FileInputStream( file );
		assertTrue( is != null );
		
		String s6 = "file:///" + this.getFullQualifiedClassName( ) + "/" + INPUT_FOLDER + "/" + noexistingFileName; //$NON-NLS-1$
		try
		{
			openDesign( s6 , is );
			
		}
		catch( DesignFileException e )
		{
			assertEquals( DesignFileException.DESIGN_EXCEPTION_INVALID_XML, e.getErrorCode( ) );
		}
	}
	
	
	
	
	
	
//	Open a design with absolute path folder and inputstream
	public void testOpendesign7() throws Exception 
	{
		openDesign( fileName );
		File file = new File ( this.getFullQualifiedClassName( ) + "/" + INPUT_FOLDER + "/" + fileName );
		
		InputStream is = new FileInputStream( file );
		assertTrue( is != null );
		
		String s7 = this.getFullQualifiedClassName( ) + "/" + INPUT_FOLDER;
		openDesign( s7 , is );
	}
	
	
	
	
	
	
//	Open a design with URI folder and inputstream
	public void testOpendesign8() throws Exception 
	{
		openDesign( fileName );
		File file = new File ( this.getFullQualifiedClassName( ) + "/" + INPUT_FOLDER + "/" + fileName );
		
		InputStream is = new FileInputStream( file );
		assertTrue( is != null );
		
		String s8 = "file:///" + this.getFullQualifiedClassName( ) + "/" + INPUT_FOLDER + "/" + INPUT_FOLDER; //$NON-NLS-1$
		openDesign( s8 , is );
	}
	
	
	
	
	
	
//	Open a design with relative path folder and inputstream
	public void testOpendesign9() throws Exception 
	{
		openDesign( fileName );
		File file = new File ( this.getFullQualifiedClassName( ) + "/" + INPUT_FOLDER + "/" + fileName );
		
		InputStream is = new FileInputStream( file );
		assertTrue( is != null );
		
		String s9 = "input"; //$NON-NLS-1$
		openDesign( s9 , is );
	}
}
