/*
 * Created on May 18, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.eclipse.birt.doc.romdoc;

import java.io.FileNotFoundException;

import org.eclipse.birt.doc.romdoc.DocParser.ParseException;
import org.eclipse.birt.doc.util.HTMLParser;
import org.eclipse.birt.doc.util.HtmlDocReader;

/**
 * @author Paul Rogers
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DataTypeParser extends HtmlDocReader
{
	Generator generator;
	
	public DataTypeParser( Generator gen )
	{
		generator = gen;
	}
	
	public void parse( ) throws ParseException
	{
		String fileName = "docs/data-types.html";
		try
		{
			parser.open( fileName );
		}
		catch ( FileNotFoundException e1 )
		{
			System.out.println( "No documentation file for " + fileName );
			return;
		}
		
		parseHeader( );
		try
		{
			parseTypes( );
		}
		catch ( ParserException e )
		{
			// Ignore
		}
		parser.close( );
	}

	private void parseHeader( )
	{
		skipTo( "/h1" );
		generator.setTypeHeader( getTextTo( "h2" ) );
		pushToken( HTMLParser.ELEMENT );
	}

	private void parseTypes( ) throws ParserException
	{
		for ( ; ; )
		{
			int token = getToken( );
			if ( token == HTMLParser.EOF )
				return;
			if ( isElement( token, "h1" )  ||
				 isElement( token, "/body" )  ||
				 isElement( token, "/html" ) )
			{
				pushToken( token );
				return;
			}
			
			parseType( );		
		}
	}
	
	private void parseType( ) throws ParserException
	{
		int token = getToken( );
		if ( token != HTMLParser.TEXT )
		{
			String msg = "Type name missing from h2 block.";
			System.out.println( msg );
			throw new ParserException( msg );
		}
		
		String name = parser.getTokenText( );
		DocPropertyType type = generator.findType( name );
		if ( type == null )
		{
			System.err.println( "Property type " + name + " is not defined in rom.def!" );
			
			// Type not found. Create, then discard, a dummy type object.
			
			type = new DocPropertyType( null );
		}
		token = getToken( );
		if ( ! isElement( token, "/h2" ) )
		{
			String msg = "Missing /h2 element.";
			System.out.println( msg );
			throw new ParserException( msg );
		}
		
		// Summary is first block of text after h2.
		
		type.setSummary( stripPara( copySection( ) ) );
		
		// Parse sections for this element. Sections are given by
		// h3 headings. The next h2 indicates the start of a member.
		
		for ( ; ; )
		{
			token = getToken( );
			if ( token == HTMLParser.EOF )
				return;
			if ( isElement( token, "h2" )  ||
				 isElement( token, "/body" )  ||
				 isElement( token, "/html" ) )
			{
				pushToken( token );
				return;
			}
			assert( isElement( token, "h3" ) );
			token = getToken( );
			if ( isElement( token, "/h3" ) )
			{
				System.out.println( "Blank section header" );
			}
			else if ( token != HTMLParser.TEXT )
			{
				String msg = "Unexpected element inside section header";
				System.out.println( msg );
				throw new ParserException( msg );
			}
			
			String header = parser.getTokenText( );
			token = getToken( );
			if ( ! isElement( token, "/h3" ) )
				pushToken( token );
			if ( header.equalsIgnoreCase( "Description" ) )
			{
				type.setDescription( copySection( ) );
			}
			else if ( header.equalsIgnoreCase( "See Also" ) )
			{
				type.setSeeAlso( copySection( ) );
			}
			else if ( header.equalsIgnoreCase( "Issues" ) )
			{
				// Issues are private to the implementation. Ignore them.
				
				copySection( );
			}
			else
			{
				System.out.println( "Unexpected Element header: " + header );
				copySection( );
			}
		}
	}

	static class ParserException extends Exception
	{
		public ParserException( String msg )
		{
			super( msg );
		}
	}

}
