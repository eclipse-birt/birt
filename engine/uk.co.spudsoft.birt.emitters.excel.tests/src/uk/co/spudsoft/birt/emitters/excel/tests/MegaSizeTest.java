/*************************************************************************************
 * Copyright (c) 2011, 2012, 2013 James Talbut.
 *  jim-emitters@spudsoft.co.uk
 *  
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     James Talbut - Initial implementation.
 ************************************************************************************/

package uk.co.spudsoft.birt.emitters.excel.tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.birt.core.exception.BirtException;
import org.junit.Test;

public class MegaSizeTest extends ReportRunner {
	
	/*
	 * No point in trying to do this warmup until using junit 4.11 which allows the order of tests to be controlled.
	@Test
	public void testFirstWarmup() throws BirtException, IOException {

		debug = false;
		InputStream inputStream = runAndRenderReport("MegaSize.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			
			
		} finally {
			inputStream.close();
		}
	}
	*/
	
	@Test
	public void testMegaXlsx() throws BirtException, IOException {

		debug = false;
		InputStream inputStream = runAndRenderReport("MegaSize.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			
			
		} finally {
			inputStream.close();
		}
	}
	
	@Test
	public void testMegaXlsxExtract() throws BirtException, IOException {

		debug = false;
		extractMode= true;
		try {
			InputStream inputStream = runAndRenderReport("MegaSize.rptdesign", "xlsx");
			assertNotNull(inputStream);
			try {
				
				
			} finally {
				inputStream.close();
			}
		} finally {
			extractMode = false;
		}
	}
	
	@Test
	public void testMegaXlsxExtractNoStyles() throws BirtException, IOException {

		debug = false;
		extractMode= true;
		noStyles = true;
		try {
			InputStream inputStream = runAndRenderReport("MegaSize.rptdesign", "xlsx");
			assertNotNull(inputStream);
			try {
				
				
			} finally {
				inputStream.close();
			}
		} finally {
			extractMode = false;
			noStyles = false;
		}
	}
	
	@Test
	public void testMegaXlsxNoStyles() throws BirtException, IOException {

		debug = false;
		InputStream inputStream = runAndRenderReport("MegaSizeNoStyles.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			
			
		} finally {
			inputStream.close();
		}
	}
	
	@Test
	public void testMegaXlsxTemplateNoStyles() throws BirtException, IOException {

		debug = false;
		String file = deriveFilepath( "MegaSizeTemplate.xlsx" );
		File template = new File( file );
		
		assertTrue( template.exists() );
		templateFile = template.getAbsolutePath();

		InputStream inputStream = runAndRenderReport("MegaSizeNoStyles.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			
			
		} finally {
			inputStream.close();
		}
	}
	
	@Test
	public void testMegaXlsxTemplate() throws BirtException, IOException {

		debug = false;
		String file = deriveFilepath( "MegaSizeTemplate.xlsx" );
		File template = new File( file );
		
		assertTrue( template.exists() );
		templateFile = template.getAbsolutePath();

		InputStream inputStream = runAndRenderReport("MegaSize.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			
			
		} finally {
			inputStream.close();
		}
	}
	
	@Test
	public void testMegaXlsxFixedLayout() throws BirtException, IOException {

		debug = false;
		InputStream inputStream = runAndRenderReport("MegaSizeFixedLayout.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			
			
		} finally {
			inputStream.close();
		}
	}
	
	@Test
	public void testMegaXls() throws BirtException, IOException {

		try {
			runAndRenderReport("MegaSize.rptdesign", "xls");
			fail( "Should have failed!" );
		} catch( Throwable ex ) {
			assertEquals( "Error happened while running the report.", ex.getMessage() );
			ex = ex.getCause();
			assertEquals( "Invalid row number (65536) outside allowable range (0..65535)", ex.getMessage() );
		}
	}
	
	@Test
	public void testMegaXls60000() throws BirtException, IOException {

		InputStream inputStream = runAndRenderReport("MegaSize60000.rptdesign", "xls");
		assertNotNull(inputStream);
		try {
			
		} finally {
			inputStream.close();
		}
	}
	
}
