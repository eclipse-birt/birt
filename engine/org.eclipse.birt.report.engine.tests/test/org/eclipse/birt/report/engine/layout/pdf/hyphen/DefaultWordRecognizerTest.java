package org.eclipse.birt.report.engine.layout.pdf.hyphen;

import junit.framework.TestCase;


public class DefaultWordRecognizerTest extends TestCase
{

	protected void setUp( ) throws Exception
	{
		super.setUp( );
	}

	protected void tearDown( ) throws Exception
	{
		super.tearDown( );
	}


	/*
	 * Test method for 'org.eclipse.birt.report.engine.layout.hyphen.DefaultWordRecognizer.getLastWordEnd()'
	 * Test method for 'org.eclipse.birt.report.engine.layout.hyphen.DefaultWordRecognizer.getNextWord()'
	 */
	public void testWordRecognize( )
	{
		String str = " simple \n test ";
		DefaultWordRecognizer wr = new DefaultWordRecognizer(str);
		Word word = wr.getNextWord( );
		assertTrue(" ".equals( word.getValue( ) ));
		word = wr.getNextWord( );
		assertTrue("simple ".equals( word.getValue( ) ));
		word = wr.getNextWord( );
		assertTrue("\n".equals( word.getValue( ) ));
		word = wr.getNextWord( );
		assertTrue(" ".equals( word.getValue( ) ));
		word = wr.getNextWord( );
		assertTrue("test ".equals( word.getValue( ) ));

		str = "simple\ntest";
		wr = new DefaultWordRecognizer(str);
		word = wr.getNextWord( );
		assertTrue("simple".equals( word.getValue( ) ));
		word = wr.getNextWord( );
		assertTrue("\n".equals( word.getValue( ) ));
		word = wr.getNextWord( );
		assertTrue("test".equals( word.getValue( ) ));
		
		str = "simple\n\n\n";
		wr = new DefaultWordRecognizer(str);
		word = wr.getNextWord( );
		assertTrue("simple".equals( word.getValue( ) ));
		word = wr.getNextWord( );
		assertTrue("\n".equals( word.getValue( ) ));
		word = wr.getNextWord( );
		assertTrue("\n".equals( word.getValue( ) ));
		word = wr.getNextWord( );
		assertTrue("\n".equals( word.getValue( ) ));

	}

}
