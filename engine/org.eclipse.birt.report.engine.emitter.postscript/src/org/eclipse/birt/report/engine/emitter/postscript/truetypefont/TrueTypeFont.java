/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.postscript.truetypefont;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.lowagie.text.DocumentException;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.pdf.IntHashtable;
import com.lowagie.text.pdf.RandomAccessFileOrArray;

/**
 * Reads a Truetype font
 * 
 * @author Paulo Soares (psoares@consiste.pt)
 */
public class TrueTypeFont
{

	private static final int MAX_STRING_LENGTH = 16382;

	private static final int HEAD_LOCA_FORMAT_OFFSET = 51;

	/** A possible encoding. */
	private static final String WINANSI = "Cp1252";

	/** true if the font is to be embedded in the PDF */
	private boolean embedded;

	/**
	 * true if the font must use it's built in encoding. In that case the <CODE>encoding</CODE>
	 * is only used to map a char to the position inside the font, not to the
	 * expected char name.
	 */
	private boolean fontSpecific = true;

	private boolean justNames = false;
	/**
	 * Contains the location of the several tables. The key is the name of the
	 * table and the value is an <CODE>int[2]</CODE> where position 0 is the
	 * offset from the start of the file and position 1 is the length of the
	 * table.
	 */
	private HashMap positionTables;
	/**
	 * The file in use.
	 */
	private RandomAccessFileOrArray rf;
	/**
	 * The file name.
	 */
	private String fileName;

	/**
	 * The offset from the start of the file to the table directory. It is 0 for
	 * TTF and may vary for TTC depending on the chosen font.
	 */
	private int directoryOffset;
	/**
	 * The index for the TTC font. It is an empty <CODE>String</CODE> for a
	 * TTF file.
	 */
	private String ttcIndex;
	/** The style modifier */
	private String style = "";
	/**
	 * The content of table 'head'.
	 */
	private FontHeader head = new FontHeader( );
	/**
	 * The content of table 'hhea'.
	 */
	private HorizontalHeader hhea = new HorizontalHeader( );
	/**
	 * The content of table 'OS/2'.
	 */
	private WindowsMetrics os_2 = new WindowsMetrics( );
	/**
	 * The width of the glyphs. This is essentially the content of table 'hmtx'
	 * normalized to 1000 units.
	 */
	private int GlyphWidths[];

	private int bboxes[][];
	/**
	 * The map containing the code information for the table 'cmap', encoding
	 * 1.0. The key is the code and the value is an <CODE>int[2]</CODE> where
	 * position 0 is the glyph number and position 1 is the glyph width
	 * normalized to 1000 units.
	 */
	private HashMap cmap10;
	/**
	 * The map containing the code information for the table 'cmap', encoding
	 * 3.1 in Unicode.
	 * <P>
	 * The key is the code and the value is an <CODE>int</CODE>[2] where
	 * position 0 is the glyph number and position 1 is the glyph width
	 * normalized to 1000 units.
	 */
	private HashMap cmap31;
	/**
	 * The map containing the kerning information. It represents the content of
	 * table 'kern'. The key is an <CODE>Integer</CODE> where the top 16 bits
	 * are the glyph number for the first character and the lower 16 bits are
	 * the glyph number for the second character. The value is the amount of
	 * kerning in normalized 1000 units as an <CODE>Integer</CODE>. This
	 * value is usually negative.
	 */
	private IntHashtable kerning = new IntHashtable( );
	/**
	 * The font name. This name is usually extracted from the table 'name' with
	 * the 'Name ID' 6.
	 */
	private String fontName;

	/**
	 * The full name of the font
	 */
	private String fullName[][];

	/**
	 * The family name of the font
	 */
	private String familyName[][];
	/**
	 * The italic angle. It is usually extracted from the 'post' table or in
	 * it's absence with the code:
	 * <P>
	 * 
	 * <PRE>
	 * 
	 * -Math.atan2(hhea.caretSlopeRun, hhea.caretSlopeRise) * 180 / Math.PI
	 * 
	 * </PRE>
	 */
	private double italicAngle;

	private double underlineThickness;

	private double underlinePosition;
	/**
	 * <CODE>true</CODE> if all the glyphs have the same width.
	 */
	private boolean isFixedPitch = false;

	/**
	 * The components of table 'head'.
	 */
	protected static class FontHeader
	{

		/** A variable. */
		int flags;
		/** A variable. */
		int unitsPerEm;
		/** A variable. */
		short xMin;
		/** A variable. */
		short yMin;
		/** A variable. */
		short xMax;
		/** A variable. */
		short yMax;
		/** A variable. */
		int macStyle;
		int locaBytesPerEntry;
	}

	/**
	 * The components of table 'hhea'.
	 */
	protected static class HorizontalHeader
	{

		/** A variable. */
		short Ascender;
		/** A variable. */
		short Descender;
		/** A variable. */
		short LineGap;
		/** A variable. */
		int advanceWidthMax;
		/** A variable. */
		short minLeftSideBearing;
		/** A variable. */
		short minRightSideBearing;
		/** A variable. */
		short xMaxExtent;
		/** A variable. */
		short caretSlopeRise;
		/** A variable. */
		short caretSlopeRun;
		/** A variable. */
		int numberOfHMetrics;
	}

	/**
	 * The components of table 'OS/2'.
	 */
	protected static class WindowsMetrics
	{

		/** A variable. */
		short xAvgCharWidth;
		/** A variable. */
		int usWeightClass;
		/** A variable. */
		int usWidthClass;
		/** A variable. */
		short fsType;
		/** A variable. */
		short ySubscriptXSize;
		/** A variable. */
		short ySubscriptYSize;
		/** A variable. */
		short ySubscriptXOffset;
		/** A variable. */
		short ySubscriptYOffset;
		/** A variable. */
		short ySuperscriptXSize;
		/** A variable. */
		short ySuperscriptYSize;
		/** A variable. */
		short ySuperscriptXOffset;
		/** A variable. */
		short ySuperscriptYOffset;
		/** A variable. */
		short yStrikeoutSize;
		/** A variable. */
		short yStrikeoutPosition;
		/** A variable. */
		short sFamilyClass;
		/** A variable. */
		byte panose[] = new byte[10];
		/** A variable. */
		byte achVendID[] = new byte[4];
		/** A variable. */
		int fsSelection;
		/** A variable. */
		int usFirstCharIndex;
		/** A variable. */
		int usLastCharIndex;
		/** A variable. */
		short sTypoAscender;
		/** A variable. */
		short sTypoDescender;
		/** A variable. */
		short sTypoLineGap;
		/** A variable. */
		int usWinAscent;
		/** A variable. */
		int usWinDescent;
		/** A variable. */
		int ulCodePageRange1;
		/** A variable. */
		int ulCodePageRange2;
		/** A variable. */
		int sCapHeight;
	}

	/**
	 * Raw data of directory.
	 */
	private byte[] directoryRawData = new byte[12];

	/**
	 * Raw data of metadata of each tables.
	 */
	private HashMap metadataTables;

	private String[][] notice;

	private String[][] version;

	private static Map fonts = new HashMap( );

	/**
	 * This constructor is present to allow extending the class.
	 */
	protected TrueTypeFont( )
	{
	}

	TrueTypeFont( String ttFile ) throws DocumentException, IOException
	{
		this( ttFile, false );
	}

	/**
	 * Creates a new TrueType font.
	 * 
	 * @param ttFile
	 *            the location of the font on file. The file must end in '.ttf'
	 *            or '.ttc' but can have modifiers after the name
	 * @param enc
	 *            the encoding to be applied to this font
	 * @param emb
	 *            true if the font is to be embedded in the PDF
	 * @param ttfAfm
	 *            the font as a <CODE>byte</CODE> array
	 * @throws DocumentException
	 *             the font is invalid
	 * @throws IOException
	 *             the font file could not be read
	 */
	TrueTypeFont( String ttFile, boolean justNames ) throws DocumentException,
			IOException
	{
		this.justNames = justNames;
		String nameBase = getBaseName( ttFile );
		String ttcName = getTTCName( nameBase );
		if ( nameBase.length( ) < ttFile.length( ) )
		{
			style = ttFile.substring( nameBase.length( ) );
		}
		embedded = false;
		fileName = ttcName;
		ttcIndex = "";
		if ( ttcName.length( ) < nameBase.length( ) )
			ttcIndex = nameBase.substring( ttcName.length( ) + 1 );
		if ( fileName.toLowerCase( ).endsWith( ".ttf" )
				|| fileName.toLowerCase( ).endsWith( ".otf" )
				|| fileName.toLowerCase( ).endsWith( ".ttc" ) )
		{
			process( );
		}
		else
			throw new DocumentException( fileName + style
					+ " is not a TTF, OTF or TTC font file." );
	}

	public static TrueTypeFont getInstance( String fileName )
			throws DocumentException, IOException
	{
		File file = new File( fileName );
		if ( fonts.containsKey( file ) )
		{
			return (TrueTypeFont) fonts.get( file );
		}
		synchronized ( fonts )
		{
			if ( fonts.containsKey( file ) )
			{
				return (TrueTypeFont) fonts.get( file );
			}
			TrueTypeFont font = new TrueTypeFont( fileName );
			fonts.put( new File( fileName ), font );
			return font;
		}
	}

	/**
	 * Gets the name without the modifiers Bold, Italic or BoldItalic.
	 * 
	 * @param name
	 *            the full name of the font
	 * @return the name without the modifiers Bold, Italic or BoldItalic
	 */
	protected static String getBaseName( String name )
	{
		if ( name.endsWith( ",Bold" ) )
			return name.substring( 0, name.length( ) - 5 );
		else if ( name.endsWith( ",Italic" ) )
			return name.substring( 0, name.length( ) - 7 );
		else if ( name.endsWith( ",BoldItalic" ) )
			return name.substring( 0, name.length( ) - 11 );
		else
			return name;
	}

	/**
	 * Gets the name from a composed TTC file name. If I have for input
	 * "myfont.ttc,2" the return will be "myfont.ttc".
	 * 
	 * @param name
	 *            the full name
	 * @return the simple file name
	 */
	protected static String getTTCName( String name )
	{
		int idx = name.toLowerCase( ).indexOf( ".ttc," );
		if ( idx < 0 )
			return name;
		else
			return name.substring( 0, idx + 4 );
	}

	/**
	 * Reads the tables 'head', 'hhea', 'OS/2' and 'post' filling several
	 * variables.
	 * 
	 * @throws DocumentException
	 *             the font is invalid
	 * @throws IOException
	 *             the font file could not be read
	 */
	void fillTables( ) throws DocumentException, IOException
	{
		fillHead( );
		fillHHea( );
		fillOS( );
		processPost( );
	}

	private void processPost( ) throws DocumentException, IOException
	{
		int[] tableLocation = getTableLocation( "post" );
		if ( tableLocation == null )
		{
			italicAngle = -Math.atan2( hhea.caretSlopeRun, hhea.caretSlopeRise )
					* 180 / Math.PI;
			return;
		}
		rf.seek( tableLocation[0] + 4 );
		short mantissa = rf.readShort( );
		int fraction = rf.readUnsignedShort( );
		italicAngle = (double) mantissa + (double) fraction / 16384.0;
		underlinePosition = (double) rf.readShort( ) / head.unitsPerEm;
		underlineThickness = (double) rf.readShort( ) / head.unitsPerEm;
		isFixedPitch = rf.readInt( ) != 0;
	}

	private void fillOS( ) throws DocumentException, IOException
	{
		int[] tableLocation = getTableLocation( "OS/2" );
		rf.seek( tableLocation[0] );
		int version = rf.readUnsignedShort( );
		os_2.xAvgCharWidth = rf.readShort( );
		os_2.usWeightClass = rf.readUnsignedShort( );
		os_2.usWidthClass = rf.readUnsignedShort( );
		os_2.fsType = rf.readShort( );
		os_2.ySubscriptXSize = rf.readShort( );
		os_2.ySubscriptYSize = rf.readShort( );
		os_2.ySubscriptXOffset = rf.readShort( );
		os_2.ySubscriptYOffset = rf.readShort( );
		os_2.ySuperscriptXSize = rf.readShort( );
		os_2.ySuperscriptYSize = rf.readShort( );
		os_2.ySuperscriptXOffset = rf.readShort( );
		os_2.ySuperscriptYOffset = rf.readShort( );
		os_2.yStrikeoutSize = rf.readShort( );
		os_2.yStrikeoutPosition = rf.readShort( );
		os_2.sFamilyClass = rf.readShort( );
		rf.readFully( os_2.panose );
		rf.skipBytes( 16 );
		rf.readFully( os_2.achVendID );
		os_2.fsSelection = rf.readUnsignedShort( );
		os_2.usFirstCharIndex = rf.readUnsignedShort( );
		os_2.usLastCharIndex = rf.readUnsignedShort( );
		os_2.sTypoAscender = rf.readShort( );
		os_2.sTypoDescender = rf.readShort( );
		if ( os_2.sTypoDescender > 0 )
			os_2.sTypoDescender = (short) ( -os_2.sTypoDescender );
		os_2.sTypoLineGap = rf.readShort( );
		os_2.usWinAscent = rf.readUnsignedShort( );
		os_2.usWinDescent = rf.readUnsignedShort( );
		os_2.ulCodePageRange1 = 0;
		os_2.ulCodePageRange2 = 0;
		if ( version > 0 )
		{
			os_2.ulCodePageRange1 = rf.readInt( );
			os_2.ulCodePageRange2 = rf.readInt( );
		}
		if ( version > 1 )
		{
			rf.skipBytes( 2 );
			os_2.sCapHeight = rf.readShort( );
		}
		else
			os_2.sCapHeight = (int) ( 0.7 * head.unitsPerEm );
	}

	private void fillHHea( ) throws DocumentException, IOException
	{
		int[] tableLocation = getTableLocation( "hhea" );
		rf.seek( tableLocation[0] + 4 );
		hhea.Ascender = rf.readShort( );
		hhea.Descender = rf.readShort( );
		hhea.LineGap = rf.readShort( );
		hhea.advanceWidthMax = rf.readUnsignedShort( );
		hhea.minLeftSideBearing = rf.readShort( );
		hhea.minRightSideBearing = rf.readShort( );
		hhea.xMaxExtent = rf.readShort( );
		hhea.caretSlopeRise = rf.readShort( );
		hhea.caretSlopeRun = rf.readShort( );
		rf.skipBytes( 12 );
		hhea.numberOfHMetrics = rf.readUnsignedShort( );
	}

	private void fillHead( ) throws DocumentException, IOException
	{
		int tableLocation[] = getTableLocation( "head" );
		rf.seek( tableLocation[0] + 16 );
		head.flags = rf.readUnsignedShort( );
		head.unitsPerEm = rf.readUnsignedShort( );
		rf.skipBytes( 16 );
		head.xMin = rf.readShort( );
		head.yMin = rf.readShort( );
		head.xMax = rf.readShort( );
		head.yMax = rf.readShort( );
		head.macStyle = rf.readUnsignedShort( );
		rf.skip( 4 );
		int indexToLocFormat = rf.readUnsignedShort( );
		head.locaBytesPerEntry = indexToLocFormat == 0 ? 2 : 4;
	}

	private int[] getTableLocation( String name ) throws DocumentException
	{
		int[] table_location;
		table_location = (int[]) positionTables.get( name );
		if ( table_location == null )
			throw new DocumentException( "Table 'head' does not exist in "
					+ fileName + style );
		return table_location;
	}

	/**
	 * Gets the Postscript font name.
	 * 
	 * @throws DocumentException
	 *             the font is invalid
	 * @throws IOException
	 *             the font file could not be read
	 * @return the Postscript font name
	 */
	String getBaseFont( ) throws DocumentException, IOException
	{
		int table_location[];
		table_location = (int[]) positionTables.get( "name" );
		if ( table_location == null )
			throw new DocumentException( "Table 'name' does not exist in "
					+ fileName + style );
		rf.seek( table_location[0] + 2 );
		int numRecords = rf.readUnsignedShort( );
		int startOfStorage = rf.readUnsignedShort( );
		for ( int k = 0; k < numRecords; ++k )
		{
			int platformID = rf.readUnsignedShort( );
			int nameID = rf.readUnsignedShort( );
			int length = rf.readUnsignedShort( );
			int offset = rf.readUnsignedShort( );
			if ( nameID == 6 )
			{
				rf.seek( table_location[0] + startOfStorage + offset );
				if ( platformID != 0 && platformID != 3 )
				{
					String name = readStandardString( length );
					name = name.replace( ' ', '_' );
					return name.replace( (char)0, '_' );
				}
			}
		}
		File file = new File( fileName );
		return file.getName( ).replace( ' ', '_' );
	}

	/**
	 * Extracts the names of the font in all the languages available.
	 * 
	 * @param id
	 *            the name id to retrieve
	 * @throws DocumentException
	 *             on error
	 * @throws IOException
	 *             on error
	 */
	String[][] getNames( int id ) throws DocumentException, IOException
	{
		int table_location[];
		table_location = (int[]) positionTables.get( "name" );
		if ( table_location == null )
			throw new DocumentException( "Table 'name' does not exist in "
					+ fileName + style );
		rf.seek( table_location[0] + 2 );
		int numRecords = rf.readUnsignedShort( );
		int startOfStorage = rf.readUnsignedShort( );
		ArrayList names = new ArrayList( );
		for ( int k = 0; k < numRecords; ++k )
		{
			int platformID = rf.readUnsignedShort( );
			int platformEncodingID = rf.readUnsignedShort( );
			int languageID = rf.readUnsignedShort( );
			int nameID = rf.readUnsignedShort( );
			int length = rf.readUnsignedShort( );
			int offset = rf.readUnsignedShort( );
			if ( nameID == id )
			{
				int pos = rf.getFilePointer( );
				rf.seek( table_location[0] + startOfStorage + offset );
				String name;
				if ( platformID == 0 || platformID == 3
						|| ( platformID == 2 && platformEncodingID == 1 ) )
				{
					name = readUnicodeString( length );
				}
				else
				{
					name = readStandardString( length );
				}
				names.add( new String[]{String.valueOf( platformID ),
						String.valueOf( platformEncodingID ),
						String.valueOf( languageID ), name} );
				rf.seek( pos );
			}
		}
		String thisName[][] = new String[names.size( )][];
		for ( int k = 0; k < names.size( ); ++k )
			thisName[k] = (String[]) names.get( k );
		return thisName;
	}

	/**
	 * Reads the font data.
	 * 
	 * @param ttfAfm
	 *            the font as a <CODE>byte</CODE> array, possibly <CODE>null</CODE>
	 * @throws DocumentException
	 *             the font is invalid
	 * @throws IOException
	 *             the font file could not be read
	 */
	void process( ) throws DocumentException, IOException
	{
		positionTables = new HashMap( );
		metadataTables = new HashMap( );

		try
		{
			rf = new RandomAccessFileOrArray( fileName );
			if ( ttcIndex.length( ) > 0 )
			{
				int dirIdx = Integer.parseInt( ttcIndex );
				if ( dirIdx < 0 )
					throw new DocumentException( "The font index for "
							+ fileName + " must be positive." );
				String mainTag = readStandardString( 4 );
				if ( !mainTag.equals( "ttcf" ) )
					throw new DocumentException( fileName
							+ " is not a valid TTC file." );
				rf.skipBytes( 4 );
				int dirCount = rf.readInt( );
				if ( dirIdx >= dirCount )
					throw new DocumentException( "The font index for "
							+ fileName + " must be between 0 and "
							+ ( dirCount - 1 ) + ". It was " + dirIdx + "." );
				rf.skipBytes( dirIdx * 4 );
				directoryOffset = rf.readInt( );
			}
			rf.seek( directoryOffset );
			rf.readFully( directoryRawData );
			int ttId = Util.getInt( directoryRawData, 0 );
			if ( ttId != 0x00010000 && ttId != 0x4F54544F )
				throw new DocumentException( fileName
						+ " is not a valid TTF or OTF file." );
			int num_tables = Util.getUnsignedShort( directoryRawData, 4 );
			for ( int k = 0; k < num_tables; ++k )
			{
				byte[] rawData = new byte[16];
				rf.readFully( rawData );
				String tag = getStandardString( rawData, 0, 4 );
				int table_location[] = new int[2];
				table_location[0] = Util.getInt( rawData, 8 );
				table_location[1] = Util.getInt( rawData, 12 );
				positionTables.put( tag, table_location );
				metadataTables.put( tag, rawData );
			}
			fontName = getBaseFont( );
			fullName = getNames( 4 ); // full name
			familyName = getNames( 1 ); // family name
			notice = getNames( 0 );
			version = getNames( 5 );

			if ( !justNames )
			{
				fillTables( );
				readGlyphWidths( );
				readCMaps( );
				readKerning( );
				readBbox( );
				GlyphWidths = null;
			}
		}
		finally
		{
			if ( rf != null )
			{
				rf.close( );
				if ( !embedded )
					rf = null;
			}
		}
	}

	public ITrueTypeWriter getTrueTypeWriter( PrintStream out )
			throws IOException
	{
		return new TrueTypeWriter( out );
	}

	/**
	 * Reads a <CODE>String</CODE> from the font file as bytes using the
	 * Cp1252 encoding.
	 * 
	 * @param length
	 *            the length of bytes to read
	 * @return the <CODE>String</CODE> read
	 * @throws IOException
	 *             the font file could not be read
	 */
	protected String readStandardString( int length ) throws IOException
	{
		byte buf[] = new byte[length];
		rf.readFully( buf );
		try
		{
			return new String( buf, WINANSI );
		}
		catch ( Exception e )
		{
			throw new ExceptionConverter( e );
		}
	}

	/**
	 * Reads a Unicode <CODE>String</CODE> from the font file. Each character
	 * is represented by two bytes.
	 * 
	 * @param length
	 *            the length of bytes to read. The <CODE>String</CODE> will
	 *            have <CODE>length</CODE>/2 characters
	 * @return the <CODE>String</CODE> read
	 * @throws IOException
	 *             the font file could not be read
	 */
	protected String readUnicodeString( int length ) throws IOException
	{
		StringBuffer buf = new StringBuffer( );
		length /= 2;
		for ( int k = 0; k < length; ++k )
		{
			buf.append( rf.readChar( ) );
		}
		return buf.toString( );
	}

	/**
	 * Reads the glyphs widths. The widths are extracted from the table 'hmtx'.
	 * The glyphs are normalized to 1000 units.
	 * 
	 * @throws DocumentException
	 *             the font is invalid
	 * @throws IOException
	 *             the font file could not be read
	 */
	protected void readGlyphWidths( ) throws DocumentException, IOException
	{
		int table_location[];
		table_location = (int[]) positionTables.get( "hmtx" );
		if ( table_location == null )
			throw new DocumentException( "Table 'hmtx' does not exist in "
					+ fileName + style );
		rf.seek( table_location[0] );
		GlyphWidths = new int[hhea.numberOfHMetrics];
		for ( int k = 0; k < hhea.numberOfHMetrics; ++k )
		{
			GlyphWidths[k] = ( rf.readUnsignedShort( ) * 1000 )
					/ head.unitsPerEm;
			rf.readUnsignedShort( );
		}
	}

	/**
	 * Gets a glyph width.
	 * 
	 * @param glyph
	 *            the glyph to get the width of
	 * @return the width of the glyph in normalized 1000 units
	 */
	protected int getGlyphWidth( int glyph )
	{
		if ( glyph >= GlyphWidths.length )
			glyph = GlyphWidths.length - 1;
		return GlyphWidths[glyph];
	}

	private void readBbox( ) throws DocumentException, IOException
	{
		int tableLocation[];
		tableLocation = (int[]) positionTables.get( "head" );
		if ( tableLocation == null )
			throw new DocumentException( "Table 'head' does not exist in "
					+ fileName + style );
		rf.seek( tableLocation[0] + HEAD_LOCA_FORMAT_OFFSET );
		boolean locaShortTable = ( rf.readUnsignedShort( ) == 0 );
		tableLocation = (int[]) positionTables.get( "loca" );
		if ( tableLocation == null )
			return;
		rf.seek( tableLocation[0] );
		int locaTable[];
		if ( locaShortTable )
		{
			int entries = tableLocation[1] / 2;
			locaTable = new int[entries];
			for ( int k = 0; k < entries; ++k )
				locaTable[k] = rf.readUnsignedShort( ) * 2;
		}
		else
		{
			int entries = tableLocation[1] / 4;
			locaTable = new int[entries];
			for ( int k = 0; k < entries; ++k )
				locaTable[k] = rf.readInt( );
		}
		tableLocation = (int[]) positionTables.get( "glyf" );
		if ( tableLocation == null )
			throw new DocumentException( "Table 'glyf' does not exist in "
					+ fileName + style );
		int tableGlyphOffset = tableLocation[0];
		bboxes = new int[locaTable.length - 1][];
		for ( int glyph = 0; glyph < locaTable.length - 1; ++glyph )
		{
			int start = locaTable[glyph];
			if ( start != locaTable[glyph + 1] )
			{
				rf.seek( tableGlyphOffset + start + 2 );
				bboxes[glyph] = new int[]{
						( rf.readShort( ) * 1000 ) / head.unitsPerEm,
						( rf.readShort( ) * 1000 ) / head.unitsPerEm,
						( rf.readShort( ) * 1000 ) / head.unitsPerEm,
						( rf.readShort( ) * 1000 ) / head.unitsPerEm};
			}
		}
	}

	/**
	 * Reads the several maps from the table 'cmap'. The maps of interest are
	 * 1.0 for symbolic fonts and 3.1 for all others. A symbolic font is defined
	 * as having the map 3.0.
	 * 
	 * @throws DocumentException
	 *             the font is invalid
	 * @throws IOException
	 *             the font file could not be read
	 */
	public void readCMaps( ) throws DocumentException, IOException
	{
		int table_location[];
		table_location = (int[]) positionTables.get( "cmap" );
		if ( table_location == null )
			throw new DocumentException( "Table 'cmap' does not exist in "
					+ fileName + style );
		rf.seek( table_location[0] );
		rf.skipBytes( 2 );
		int num_tables = rf.readUnsignedShort( );
		fontSpecific = false;
		int map10 = 0;
		int map31 = 0;
		int map30 = 0;
		for ( int k = 0; k < num_tables; ++k )
		{
			int platId = rf.readUnsignedShort( );
			int platSpecId = rf.readUnsignedShort( );
			int offset = rf.readInt( );
			if ( platId == 3 && platSpecId == 0 )
			{
				fontSpecific = true;
				map30 = offset;
			}
			else if ( platId == 3 && platSpecId == 1 )
			{
				map31 = offset;
			}
			if ( platId == 1 && platSpecId == 0 )
			{
				map10 = offset;
			}
		}
		if ( map10 > 0 )
		{
			rf.seek( table_location[0] + map10 );
			int format = rf.readUnsignedShort( );
			switch ( format )
			{
				case 0 :
					cmap10 = readFormat0( );
					break;
				case 4 :
					cmap10 = readFormat4( );
					break;
				case 6 :
					cmap10 = readFormat6( );
					break;
			}
		}
		if ( map31 > 0 )
		{
			rf.seek( table_location[0] + map31 );
			int format = rf.readUnsignedShort( );
			if ( format == 4 )
			{
				cmap31 = readFormat4( );
			}
		}
		if ( map30 > 0 )
		{
			rf.seek( table_location[0] + map30 );
			int format = rf.readUnsignedShort( );
			if ( format == 4 )
			{
				cmap10 = readFormat4( );
			}
		}
	}

	/**
	 * The information in the maps of the table 'cmap' is coded in several
	 * formats. Format 0 is the Apple standard character to glyph index mapping
	 * table.
	 * 
	 * @return a <CODE>HashMap</CODE> representing this map
	 * @throws IOException
	 *             the font file could not be read
	 */
	HashMap readFormat0( ) throws IOException
	{
		HashMap h = new HashMap( );
		rf.skipBytes( 4 );
		for ( int k = 0; k < 256; ++k )
		{
			int r[] = new int[2];
			r[0] = rf.readUnsignedByte( );
			r[1] = getGlyphWidth( r[0] );
			h.put( new Integer( k ), r );
		}
		return h;
	}

	/**
	 * The information in the maps of the table 'cmap' is coded in several
	 * formats. Format 4 is the Microsoft standard character to glyph index
	 * mapping table.
	 * 
	 * @return a <CODE>HashMap</CODE> representing this map
	 * @throws IOException
	 *             the font file could not be read
	 */
	HashMap readFormat4( ) throws IOException
	{
		HashMap h = new HashMap( );
		int table_lenght = rf.readUnsignedShort( );
		rf.skipBytes( 2 );
		int segCount = rf.readUnsignedShort( ) / 2;
		rf.skipBytes( 6 );
		int endCount[] = new int[segCount];
		for ( int k = 0; k < segCount; ++k )
		{
			endCount[k] = rf.readUnsignedShort( );
		}
		rf.skipBytes( 2 );
		int startCount[] = new int[segCount];
		for ( int k = 0; k < segCount; ++k )
		{
			startCount[k] = rf.readUnsignedShort( );
		}
		int idDelta[] = new int[segCount];
		for ( int k = 0; k < segCount; ++k )
		{
			idDelta[k] = rf.readUnsignedShort( );
		}
		int idRO[] = new int[segCount];
		for ( int k = 0; k < segCount; ++k )
		{
			idRO[k] = rf.readUnsignedShort( );
		}
		int glyphId[] = new int[table_lenght / 2 - 8 - segCount * 4];
		for ( int k = 0; k < glyphId.length; ++k )
		{
			glyphId[k] = rf.readUnsignedShort( );
		}
		for ( int k = 0; k < segCount; ++k )
		{
			int glyph;
			for ( int j = startCount[k]; j <= endCount[k] && j != 0xFFFF; ++j )
			{
				if ( idRO[k] == 0 )
				{
					glyph = ( j + idDelta[k] ) & 0xFFFF;
				}
				else
				{
					int idx = k + idRO[k] / 2 - segCount + j - startCount[k];
					if ( idx >= glyphId.length )
						continue;
					glyph = ( glyphId[idx] + idDelta[k] ) & 0xFFFF;
				}
				int r[] = new int[2];
				r[0] = glyph;
				r[1] = getGlyphWidth( r[0] );
				h.put( new Integer( fontSpecific ? ( ( j & 0xff00 ) == 0xf000
						? j & 0xff
						: j ) : j ), r );
			}
		}
		return h;
	}

	/**
	 * The information in the maps of the table 'cmap' is coded in several
	 * formats. Format 6 is a trimmed table mapping. It is similar to format 0
	 * but can have less than 256 entries.
	 * 
	 * @return a <CODE>HashMap</CODE> representing this map
	 * @throws IOException
	 *             the font file could not be read
	 */
	HashMap readFormat6( ) throws IOException
	{
		HashMap h = new HashMap( );
		rf.skipBytes( 4 );
		int start_code = rf.readUnsignedShort( );
		int code_count = rf.readUnsignedShort( );
		for ( int k = 0; k < code_count; ++k )
		{
			int r[] = new int[2];
			r[0] = rf.readUnsignedShort( );
			r[1] = getGlyphWidth( r[0] );
			h.put( new Integer( k + start_code ), r );
		}
		return h;
	}

	/**
	 * Reads the kerning information from the 'kern' table.
	 * 
	 * @throws IOException
	 *             the font file could not be read
	 */
	void readKerning( ) throws IOException
	{
		int table_location[];
		table_location = (int[]) positionTables.get( "kern" );
		if ( table_location == null )
			return;
		rf.seek( table_location[0] + 2 );
		int nTables = rf.readUnsignedShort( );
		int checkpoint = table_location[0] + 4;
		int length = 0;
		for ( int k = 0; k < nTables; ++k )
		{
			checkpoint += length;
			rf.seek( checkpoint );
			rf.skipBytes( 2 );
			length = rf.readUnsignedShort( );
			int coverage = rf.readUnsignedShort( );
			if ( ( coverage & 0xfff7 ) == 0x0001 )
			{
				int nPairs = rf.readUnsignedShort( );
				rf.skipBytes( 6 );
				for ( int j = 0; j < nPairs; ++j )
				{
					int pair = rf.readInt( );
					int value = ( (int) rf.readShort( ) * 1000 )
							/ head.unitsPerEm;
					kerning.put( pair, value );
				}
			}
		}
	}

	/**
	 * Gets the glyph index and metrics for a character.
	 * 
	 * @param c
	 *            the character
	 * @return an <CODE>int</CODE> array with {glyph index, width}
	 */
	public int[] getMetricsTT( int c )
	{
		if ( !fontSpecific && cmap31 != null )
			return (int[]) cmap31.get( new Integer( c ) );
		if ( fontSpecific && cmap10 != null )
			return (int[]) cmap10.get( new Integer( c ) );
		if ( cmap31 != null )
			return (int[]) cmap31.get( new Integer( c ) );
		if ( cmap10 != null )
			return (int[]) cmap10.get( new Integer( c ) );
		return null;
	}

	/**
	 * Gets the postscript font name.
	 * 
	 * @return the postscript font name
	 */
	public String getPostscriptFontName( )
	{
		return fontName;
	}

	/**
	 * Gets the full name of the font. If it is a True Type font each array
	 * element will have {Platform ID, Platform Encoding ID, Language ID, font
	 * name}. The interpretation of this values can be found in the Open Type
	 * specification, chapter 2, in the 'name' table.<br>
	 * For the other fonts the array has a single element with {"", "", "", font
	 * name}.
	 * 
	 * @return the full name of the font
	 */
	public String[][] getFullFontName( )
	{
		return fullName;
	}

	/**
	 * Gets the family name of the font. If it is a True Type font each array
	 * element will have {Platform ID, Platform Encoding ID, Language ID, font
	 * name}. The interpretation of this values can be found in the Open Type
	 * specification, chapter 2, in the 'name' table.<br>
	 * For the other fonts the array has a single element with {"", "", "", font
	 * name}.
	 * 
	 * @return the family name of the font
	 */
	public String[][] getFamilyFontName( )
	{
		return familyName;
	}

	/**
	 * Sets the font name that will appear in the pdf font dictionary. Use with
	 * care as it can easily make a font unreadable if not embedded.
	 * 
	 * @param name
	 *            the new font name
	 */
	public void setPostscriptFontName( String name )
	{
		fontName = name;
	}

	public HashMap getCMap( )
	{
		if ( !fontSpecific && cmap31 != null )
			return cmap31;
		if ( fontSpecific && cmap10 != null )
			return cmap10;
		if ( cmap31 != null )
			return cmap31;
		if ( cmap10 != null )
			return cmap10;
		return null;
	}

	private String getStandardString( byte[] source, int index, int length )
	{
		assert ( source.length >= index + length );
		try
		{
			return new String( source, index, length, WINANSI );
		}
		catch ( UnsupportedEncodingException e )
		{
			throw new ExceptionConverter( e );
		}
	}

	private String getName( String[][] names )
	{
		if ( names.length > 0 && names[0].length > 0 )
		{
			return toPSString( names[0][names[0].length - 1] );
		}
		return null;
	}

	private String toPSString( String data )
	{
		return "(" + data + ")";
	}

	class TrueTypeWriter implements ITrueTypeWriter
	{

		private PrintStream out;

		private RandomAccessFileOrArray rf;

		private Set glyphDefined = new HashSet( );

		public TrueTypeWriter( PrintStream out ) throws IOException
		{
			this.out = out;
		}

		public void initialize( ) throws IOException
		{
			rf = new RandomAccessFileOrArray( fileName );
			out.println( "mark" );
			out.println( "/FontMatrix matrix" );
			out.println( "/FontBBox " + "["
					+ Util.div( head.xMin, head.unitsPerEm ) + " "
					+ Util.div( head.yMin, head.unitsPerEm ) + " "
					+ Util.div( head.xMax, head.unitsPerEm ) + " "
					+ Util.div( head.yMax, head.unitsPerEm ) + "]" );
			out.println( "nextxuid" );
			String psFontName = toPSString( fontName );
			out.println( "/FontName " + psFontName );
			out
					.println( "/Encoding 256 array  0 1 255 {1 index exch /.notdef put} for" );
			out.println( "/GlyphDirectory 16 dict" );
			out.println( "/FontInfo mark" );
			if ( hasTable( "name" ) )
			{
				output( out, "/Notice", getName( notice ) );
				output( out, "/FamilyName", getName( familyName ) );
				output( out, "/FullName", getName( fullName ) );
				output( out, "/Version", getName( version ) );
			}
			if ( hasTable( "post" ) )
			{
				out.println( "/ItalicAngle " + italicAngle );
				out.println( "/isFixedPitch " + isFixedPitch );
				out.println( "/UnderlinePosition " + underlinePosition );
				out.println( "/UnderlineThickness " + underlineThickness );
			}
			out.println( ">>" );
			out.println( "/XUID [107 42 curxuid]" );
			outputSfnts( out );
			out.println( "/CIDFontName " + psFontName );
			out.println( "/CIDFontType 2" );
			out.println( "/CIDSystemInfo mark" );
			out.println( "  /Registry (Actuate)" );
			out.println( "  /Ordering (China)" );
			out.println( "  /Supplement 0" );
			out.println( ">>" );
			out.println( "/CharStrings mark /.notdef 0 >>" );
			int[] tableLocation = (int[]) positionTables.get( "loca" );
			int locaLength = tableLocation[1];
			int glyphCount = locaLength / head.locaBytesPerEntry + 1;
			out.println( "/CIDCount " + glyphCount );
			out.println( "/CIDMap " + glyphCount + " dict" );
			out.println( "/PaintType 0" );
			out.println( "/FontType 42" );
			out.println( "/GDBytes 2" );
			out.println( ">>" );
			out
					.println( "dup /CIDFontName get cvn exch /CIDFont defineresource pop" );
		}

		public void useDisplayName(String displayName )
		{
			out.println( "/" + displayName + " /PeerCMap [/"+ fontName + "] composefont pop");
		}
		
		public void ensureGlyphAvailable( char c ) throws IOException
		{
			Character character = new Character( c );
			if ( !glyphDefined.contains( character ) )
			{
				glyphDefined.add( character );
				out.print( ( (int) c ) + " " );// CID
				out.print( getGlyphIndex( c ) );// glyph index.
				outputAsPsString( out, getGlyphData( c ) );
				// output glyph data
				out.println( " /" + fontName + " AddT42Char" );
			}
		}

		private byte[] getGlyphData( char c ) throws IOException
		{
			int[] tableLocation = (int[]) positionTables.get( "loca" );
			int glyphIndex = getGlyphIndex( c );
			int offset = tableLocation[0] + head.locaBytesPerEntry * glyphIndex;
			rf.seek( offset );
			int dataOffsetRelativeToGlyfTable, dataLength;
			if ( head.locaBytesPerEntry == 4 )
			{
				dataOffsetRelativeToGlyfTable = rf.readInt( );
				dataLength = rf.readInt( ) - dataOffsetRelativeToGlyfTable;
			}
			else
			{
				dataOffsetRelativeToGlyfTable = rf.readUnsignedShort( ) * 2;
				dataLength = rf.readUnsignedShort( ) * 2
						- dataOffsetRelativeToGlyfTable;
			}
			int[] glyphLocation = (int[]) positionTables.get( "glyf" );
			int dataOffset = dataOffsetRelativeToGlyfTable + glyphLocation[0];
			byte[] result = new byte[dataLength];
			rf.seek( dataOffset );
			rf.readFully( result );
			return result;
		}

		public void ensureGlyphsAvailable( String string ) throws IOException
		{
			for ( int i = 0; i < string.length( ); i++ )
			{
				ensureGlyphAvailable( string.charAt( i ) );
			}
		}

		public void close( ) throws IOException
		{
			rf.close( );
		}

		private void output( PrintStream out, String key, String value )
		{
			if ( value != null )
			{
				out.println( key + " " + value );
			}
		}

		private int getGlyphIndex( char c )
		{
			int[] glyphIndexs = (int[]) getCMap( ).get( new Integer( c ) );
			return glyphIndexs[0];
		}

		private String toPSDataString( String data )
		{
			return "<" + data + ">";
		}

		private boolean isBigTable( String name, String[] bigTables )
		{
			for ( int i = 0; i < bigTables.length; i++ )
			{
				if ( name.equals( bigTables[i] ) )
				{
					return true;
				}
			}
			return false;
		}

		private void addExistedTables( List tablesToAdd, String[] tablesDesired )
		{
			for ( int i = 0; i < tablesDesired.length; i++ )
			{
				String name = tablesDesired[i];
				int[] position = (int[]) positionTables.get( name );
				// add table if it exists.
				if ( position != null )
				{
					addTableNameInOrder( name, tablesToAdd, position );
				}
			}
		}

		private void addTableNameInOrder( String name, List arrayList,
				int[] position )
		{
			for ( int j = 0; j < arrayList.size( ); j++ )
			{
				String tableName = (String) arrayList.get( j );
				int[] position1 = (int[]) positionTables.get( tableName );
				if ( position[0] < position1[0] )
				{
					arrayList.add( j, name );
					return;
				}
			}
			arrayList.add( name );
		}

		private void outputGdirTable( PrintStream out )
		{
			byte[] gdirMetadata = new byte[16];
			gdirMetadata[0] = 'g';
			gdirMetadata[2] = 'd';
			gdirMetadata[4] = 'i';
			gdirMetadata[6] = 'r';
			out.print( Util.toHexString( gdirMetadata ) );
		}

		private int addTable( PrintStream out, int offset, String name,
				StringBuffer tableContent )
		{
			int result = 0;
			try
			{
				int[] tableLocation = getTableLocation( name );
				if ( tableLocation != null )
				{
					byte[] tableMetadata = (byte[]) metadataTables.get( name );
					Util.putInt32( tableMetadata, 8, offset );
					result = offset + getEvenLength( tableLocation[1] );
					out.println( Util.toHexString( tableMetadata ) );
					byte[] data = readTable( name );
					tableContent.append( "\n"
							+ toPSDataString( Util.toHexString( data ) ) );
				}
			}
			catch ( Exception e )
			{
				e.printStackTrace( );
			}
			return result;
		}

		private int getEvenLength( int length )
		{
			return ( length & 1 ) == 0 ? length : length + 1;
		}

		private int addBigTable( PrintStream out, int offset, String name,
				StringBuffer tableContent )
		{
			int result = 0;
			try
			{
				int[] tableLocation = getTableLocation( name );
				if ( tableLocation != null )
				{
					byte[] tableMetadata = (byte[]) metadataTables.get( name );
					// For incremental defination, 'loca' table and 'glyf' table
					// don't need to be provided.
					if ( "loca".equals( name ) || "glyf".equals( name ) )
					{
						tableMetadata[3] = 'x';
						Util.putInt32( tableMetadata, 8, 0 );
						Util.putInt32( tableMetadata, 12, 0 );
						result = offset;
						out.println( Util.toHexString( tableMetadata ) );
					}
					else
					{
						Util.putInt32( tableMetadata, 8, offset );
						out.println( Util.toHexString( tableMetadata ) );
						result = offset + getEvenLength( tableLocation[1] );
						List datas = readBigTable( name );
						for ( int i = 0; i < datas.size( ); i++ )
						{
							tableContent.append( "\n"
									+ toPSDataString( Util
											.toHexString( (byte[]) datas
													.get( i ) ) ) );
						}
					}
				}
			}
			catch ( Exception e )
			{
				e.printStackTrace( );
			}
			return result;
		}

		private void outputAsPsString( PrintStream out, byte[] data )
		{
			out.println( toPSDataString( Util.toHexString( data ) ) );
		}

		private byte[] readDataWithPadding( int length ) throws IOException
		{
			byte[] value = null;
			if ( ( length & 1 ) != 0 )
			{
				value = new byte[length + 1];
			}
			else
			{
				value = new byte[length];
			}
			rf.readFully( value, 0, length );
			return value;
		}

		private boolean hasTable( String tableName )
		{
			return positionTables.get( tableName ) != null;
		}

		private void outputSfnts( PrintStream out )
		{
			out.println( "/sfnts [" );
			String[] tablesDesired = {"cmap", "head", "hhea", "maxp", "name",
					"OS/2", "cvt ", "fpgm", "prep"};
			String[] bigTablesDesired = {"post", "hmtx"};
			List tablesToAdd = new ArrayList( );
			addExistedTables( tablesToAdd, tablesDesired );
			addExistedTables( tablesToAdd, bigTablesDesired );
			Util.putInt16( directoryRawData, 4, tablesToAdd.size( ) + 1 );
			out.print( "<" );
			out.println( Util.toHexString( directoryRawData ) );
			// Output metadata of each table and adjust the offset in the
			// metadata.
			// The offset need to be adjusted because some tables involved in
			// true
			// type font file are discarded in the following font definition,
			// e.g., "EBTD" table.
			int offset = 12 + ( tablesToAdd.size( ) + 1 ) * 16;
			StringBuffer tableContent = new StringBuffer( );
			for ( int i = 0; i < tablesToAdd.size( ); i++ )
			{
				String name = (String) tablesToAdd.get( i );
				if ( isBigTable( name, bigTablesDesired ) )
				{
					offset = addBigTable( out, offset, name, tableContent );
				}
				else
				{
					offset = addTable( out, offset, name, tableContent );
				}
			}
			// To define a type 2 CID font incrementally, a 'gdir' table must be
			// defined even without any valid data.
			outputGdirTable( out );
			out.print( ">" );
			out.println( tableContent.toString( ) );
			out.println( "]" );
		}

		private byte[] readTable( String name ) throws DocumentException,
				IOException
		{
			int[] tableLocation;
			tableLocation = getTableLocation( name );
			if ( tableLocation == null )
			{
				return null;
			}
			rf.seek( tableLocation[0] );
			byte[] data = readDataWithPadding( tableLocation[1] );
			return data;
		}

		private List readBigTable( String name ) throws DocumentException,
				IOException
		{
			ArrayList result = new ArrayList( );
			int[] tableLocation;
			tableLocation = getTableLocation( name );
			if ( tableLocation == null )
			{
				return null;
			}
			int limitation = 32700;
			int maxString = MAX_STRING_LENGTH;
			if ( tableLocation[1] < limitation )
			{
				rf.seek( tableLocation[0] );
				byte[] value = readDataWithPadding( tableLocation[1] );
				result.add( value );
			}
			else
			{
				int length = tableLocation[1];
				rf.seek( tableLocation[0] );
				while ( length > maxString )
				{
					length -= maxString;
					result.add( readDataWithPadding( maxString ) );
				}
				result.add( readDataWithPadding( length ) );
			}
			return result;
		}
	}
}