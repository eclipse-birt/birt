package org.eclipse.birt.report.tests.model.api;

import java.net.URL;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.model.api.DefaultResourceLocator;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.tests.model.BaseTestCase;

public class InputStreamURITest extends BaseTestCase
{
	private final String fileName = "inputStream_uri_Test.xml"; 
	private DefaultResourceLocator rl;
	
    public InputStreamURITest(String name) 
	{	
		super(name);
	}
    public static Test suite()
    {
		
		return new TestSuite(InputStreamURITest.class);
	}
	protected void setUp( ) throws Exception
	{
		super.setUp( );
		openDesign( fileName );
		rl = new DefaultResourceLocator( );
	}
	public void testImportLibrary( ) throws Exception
	{
		
		
		
		URL url = rl.findResource( designHandle, "1.xml", IResourceLocator.IMAGE );
		assertNull(url);
		
		url = rl.findResource( designHandle, "1.xml", IResourceLocator.LIBRARY );
		assertNull(url);
		
		url = rl.findResource( designHandle, "Library_Import_Test.xml", IResourceLocator.LIBRARY );
		assertNotNull(url);
		
		designHandle.setFileName( getClassFolder( ) +"/golden/" );
		url = rl.findResource( designHandle, "1_golden.xml", IResourceLocator.IMAGE );
		assertNull( url );
		
		designHandle.setFileName( getClassFolder( ) +"/golden/" ); 
		url = rl.findResource( designHandle, "LibraryCreatLib.xml", IResourceLocator.IMAGE ); 
		assertNull( url );
		
		url = rl.findResource( designHandle, "http://www.actuate.com/logo.gif", IResourceLocator.IMAGE );
		assertNotNull( url );
		
	}
}
