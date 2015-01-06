/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.archive;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.eclipse.birt.core.archive.compound.ArchiveFileFactory;
import org.eclipse.birt.core.archive.compound.ArchiveFileV3;
import org.eclipse.birt.core.archive.compound.ArchiveReader;
import org.eclipse.birt.core.archive.compound.ArchiveWriter;
import org.eclipse.birt.core.archive.compound.IArchiveFile;
import org.eclipse.birt.core.archive.compound.IArchiveFileFactory;
import org.eclipse.birt.core.i18n.CoreMessages;
import org.eclipse.birt.core.i18n.ResourceConstants;
import org.eclipse.birt.core.util.IOUtil;

public class ArchiveUtil
{

	protected static Logger logger = Logger.getLogger( ArchiveUtil.class.getName( ) );
	
	// We need this because the report document should be platform neutual. Here
	// we define the neutual is the unix seperator.
	public static String UNIX_SEPERATOR = "/";
	
	public final static String CONTNET_SUFFIX = ".content";

	/**
	 * @param rootPath -
	 *            the absolute path of the root folder. The path is seperated by
	 *            system's File seperator.
	 * @param relativePath -
	 *            the relative path. The path is either seperated by system's
	 *            File seperator or seperated by Unix seperator "/".
	 * @return the absolute path which concats rootPath and relativePath. The
	 *         full path is seperated by system's File seperator. The returned
	 *         absolute path can be used directly to locate the file.
	 */
	public static String generateFullPath( String rootPath, String relativePath )
	{
		relativePath = convertToSystemString( relativePath );

		if ( rootPath != null )
		{
			if ( !rootPath.endsWith( File.separator ) )
				rootPath += File.separator;

			if ( relativePath.startsWith( File.separator ) )
				relativePath = relativePath.substring( 1 );

			return rootPath + relativePath;
		}

		return relativePath;
	}
	
	public static String generateFullContentPath( String rootPath, String relativePath )
	{
		return generateFullPath( rootPath, relativePath + CONTNET_SUFFIX );
	}

	/**
	 * @param rootPath -
	 *            the absolute path of the root folder. The path is seperated by
	 *            system's File seperator.
	 * @param fullString -
	 *            the absolute path of the stream. The path is seperated by
	 *            system's File seperator.
	 * @return the relative path string. The path is based on Unix syntax and
	 *         starts with "/".
	 */
	public static String generateRelativePath( String rootPath, String fullPath )
	{
		String relativePath = null;

		if ( ( rootPath != null ) && fullPath.startsWith( rootPath ) )
		{
			relativePath = fullPath.substring( rootPath.length( ) );
		}
		else
			relativePath = fullPath;

		relativePath = convertToUnixString( relativePath );

		if ( !relativePath.startsWith( UNIX_SEPERATOR ) )
			relativePath = UNIX_SEPERATOR + relativePath;

		return relativePath;
	}
	
	public static String generateRelativeContentPath( String rootPath, String fullPath )
	{
		String path = generateRelativePath(rootPath, fullPath);
		if(path.endsWith( CONTNET_SUFFIX ))
		{
			return path.substring( 0, path.length( ) - CONTNET_SUFFIX.length( ) );
		}
		return path;
	}

	/**
	 * @param path -
	 *            the path that could be in system format (seperated by
	 *            File.seperator) or Unix format (seperated by "/").
	 * @return the path that is in Unix format.
	 */
	private static String convertToUnixString( String path )
	{
		if ( path == null )
			return null;

		return path.replace( File.separator.charAt( 0 ), UNIX_SEPERATOR
				.charAt( 0 ) );
	}

	/**
	 * @param path -
	 *            the path that could be in system format (seperated by
	 *            File.seperator) or Unix format (seperated by "/").
	 * @return the path that is in the system format.
	 */
	private static String convertToSystemString( String path )
	{
		if ( path == null )
			return null;

		return path.replace( UNIX_SEPERATOR.charAt( 0 ), File.separator
				.charAt( 0 ) );
	}

	/**
	 * Generate a unique file or folder name which is in the same folder as the
	 * originalName
	 * 
	 * @param originalName -
	 *            the original Name. For example, it could be the name of the
	 *            file archive
	 * @return a unique file or folder name which is in the same folder as the
	 *         originalName
	 */
	synchronized public static String generateUniqueFileFolderName(
			String originalName )
	{
		SimpleDateFormat df = new SimpleDateFormat( "yyyy_MM_dd_HH_mm_ss" ); //$NON-NLS-1$
		String dateTimeString = df.format( new Date( ) );
		
		StringBuffer folderName = new StringBuffer( originalName );
		folderName.append( '_' );
		folderName.append( dateTimeString );

		Random generator = new Random( );
		File folder = new File( folderName.toString( ) );
		while ( folder.exists( ) )
		{
			folderName.append( generator.nextInt( ) );
			folder = new File( folderName.toString( ) );
		}

		return folderName.toString( );

	}

	/**
	 * If the parent folder of the file doesn't exsit, create the parent folder.
	 */
	public static void createParentFolder( File fd )
	{
		if ( fd != null && fd.getParentFile( ) != null
				&& fd.getParentFile( ).exists( ) == false )
		{
			fd.getParentFile( ).mkdirs( );
		}
	}

	/**
	 * Recursively delete all the files and folders under dirOrFile
	 * 
	 * @param dirOrFile -
	 *            the File object which could be either a folder or a file.
	 */
	public static void deleteAllFiles( File dirOrFile )
	{
		if ( !dirOrFile.exists( ) )
			return;

		if ( dirOrFile.isFile( ) )
		{
			dirOrFile.delete( );
		}
		else
		// dirOrFile is directory
		{
			if ( ( dirOrFile.listFiles( ) != null )
					&& ( dirOrFile.listFiles( ).length > 0 ) )
			{
				File[] fileList = dirOrFile.listFiles( );
				for ( int i = 0; i < fileList.length; i++ )
					deleteAllFiles( fileList[i] );
			}

			// Directory can only be deleted when it is empty.
			dirOrFile.delete( );
		}
	}

	public static void zipFolderToStream( String tempFolderPath,
			OutputStream ostream )
	{
		ZipOutputStream zipOutput = new ZipOutputStream( ostream );
		File rootDir = new File( tempFolderPath );
		File[] files = rootDir.listFiles( );

		try
		{
			zipFiles( zipOutput, files, tempFolderPath );
			zipOutput.close( );
		}
		catch ( FileNotFoundException e )
		{
			logger.log( Level.WARNING, e.getMessage( ) );
		}
		catch ( IOException e )
		{
			logger.log( Level.WARNING, e.getMessage( ) );
		}
	}

	/**
	 * Utility funtion to write files/directories to a ZipOutputStream. For
	 * directories, all the files and subfolders are written recursively.
	 */
	private static void zipFiles( ZipOutputStream zipOut, File[] files,
			String tempFolderPath ) throws FileNotFoundException, IOException
	{
		if ( files == null )
			return;

		for ( int i = 0; i < files.length; i++ )
		{
			File file = files[i];
			if ( file.isDirectory( ) )
			{ // if file is a directory, get child files and recursively call
				// this method
				File[] dirFiles = file.listFiles( );
				zipFiles( zipOut, dirFiles, tempFolderPath );
			}
			else
			{ // if file is a file, create a new ZipEntry and write out the
				// file.
				BufferedInputStream in 
					= new BufferedInputStream( new FileInputStream( file ) );
				try
				{
					String relativePath = generateRelativePath( tempFolderPath,
							file.getPath( ) );
					ZipEntry entry = new ZipEntry( relativePath );
					try
					{
						entry.setTime( file.lastModified( ) );
						zipOut.putNextEntry( entry ); // Create a new zipEntry

						int len;
						byte[] buf = new byte[1024 * 5];
						while ( ( len = in.read( buf ) ) > 0 )
						{
							zipOut.write( buf, 0, len );
						}
					}
					finally
					{
						zipOut.closeEntry( );
					}
				}
				finally
				{
						in.close( );
				}
			}
		} // end of for ( int i = 0; i < files.length; i++ )
	}

	public static void unzipArchive( File zipArchive, String tempFolderPath )
	{
		try
		{
			ZipFile zipFile = new ZipFile( zipArchive );

			Enumeration<? extends ZipEntry> entries = zipFile.entries( );
			while ( entries.hasMoreElements( ) )
			{
				ZipEntry entry = (ZipEntry) entries.nextElement( );
				if ( entry.isDirectory( ) )
				{ // Assume directories are stored parents first then
					// children.
					String dirName = generateFullPath( tempFolderPath, entry
							.getName( ) );
					// TODO: handle the error case where the folder can not be
					// created!
					File dir = new File( dirName );
					dir.mkdirs( );
				}
				else
				{
					InputStream in = null;
					try
					{
						in = zipFile.getInputStream( entry );
						File file = new File( generateFullPath( tempFolderPath,
								entry.getName( ) ) );

						File dir = new File( file.getParent( ) );
						if ( dir.exists( ) )
						{
							assert ( dir.isDirectory( ) );
						}
						else
						{
							dir.mkdirs( );
						}

						BufferedOutputStream out = new BufferedOutputStream(
								new FileOutputStream( file ) );
						int len;
						byte[] buf = new byte[1024 * 5];
						try
						{
							while ( ( len = in.read( buf ) ) > 0 )
							{
								out.write( buf, 0, len );
							}
						}
						finally
						{
							out.close( );
						}
					}
					finally
					{
						if ( in != null )
						{
							in.close( );
						}
					}
				}
			}
			zipFile.close( );

		}
		catch ( ZipException e )
		{
			logger.log( Level.WARNING, e.getMessage( ) );
		}
		catch ( IOException e )
		{
			logger.log( Level.WARNING, e.getMessage( ) );
		}
	}

	public static void copy( IArchiveFile inArchive, IArchiveFile outArchive )
			throws IOException
	{
		if ( inArchive == null || outArchive == null )
		{
			throw new IOException(
					CoreMessages.getString( ResourceConstants.NULL_SOURCE ) );
		}

		copy( new ArchiveReader( inArchive ), new ArchiveWriter( outArchive ) );
	}

	static public void copy( IDocArchiveReader reader, IDocArchiveWriter writer )
			throws IOException
	{
		List<String> streamList = reader.listAllStreams( );
		for ( int i = 0; i < streamList.size( ); i++ )
		{
			String streamPath = streamList.get( i );
			RAInputStream in = reader.getStream( streamPath );
			try
			{
				RAOutputStream out = writer
						.createRandomAccessStream( streamPath );
				try
				{
					copyStream( in, out );
				}
				finally
				{
					out.close( );
				}
			}
			finally
			{
				in.close( );
			}
		}
	}

	static private void copyStream( RAInputStream in, RAOutputStream out )
			throws IOException
	{
		byte[] buf = new byte[4096];
		int readSize = in.read( buf );
		while ( readSize != -1 )
		{
			out.write( buf, 0, readSize );
			readSize = in.read( buf );
		}
	}

	static public void archive( String folder, String file ) throws IOException
	{
		archive( folder, null, file );
	}
	
	static public void convertFolderArchive(String folder, String file)  throws IOException
	{
		FolderArchiveReader reader = null;
		InputStream inputStream = null;
		DataInputStream dataInput = null;
		try
		{
			archive( folder, null, file, true );
			String folderName = new File( folder ).getCanonicalPath( );
			reader = new FolderArchiveReader( folderName, true );
			if ( reader.exists( FolderArchiveFile.METEDATA ) )
			{
				inputStream = reader.getInputStream( FolderArchiveFile.METEDATA );
				dataInput = new DataInputStream( inputStream );
				Map properties = IOUtil.readMap( dataInput );
				IArchiveFileFactory factory = new ArchiveFileFactory( );
				ArchiveFileV3 archive = new ArchiveFileV3( file, "rw+" );
				if ( properties.containsKey( ArchiveFileV3.PROPERTY_DEPEND_ID ) )
				{
					archive.setDependId( properties.get(
							ArchiveFileV3.PROPERTY_DEPEND_ID ).toString( ) );
				}
				if ( properties.containsKey( ArchiveFileV3.PROPERTY_SYSTEM_ID ) )
				{
					archive.setDependId( properties.get(
							ArchiveFileV3.PROPERTY_SYSTEM_ID ).toString( ) );
				}
				archive.removeEntry( FolderArchiveFile.METEDATA );
				archive.close( );
			}
		}
		finally
		{
			if(reader!=null)
			{
				reader.close( );
			}
			if(inputStream!=null)
			{
				inputStream.close( );
			}
			if(dataInput!=null)
			{
				dataInput.close( );
			}
		}
		
		
	}

	/**
	 * Compound File Format: <br>
	 * 1long(stream section position) + 1long(entry number in lookup map) +
	 * lookup map section + stream data section <br>
	 * The Lookup map is a hash map. The key is the relative path of the stram.
	 * The entry contains two long number. The first long is the start postion.
	 * The second long is the length of the stream. <br>
	 * 
	 * @param tempFolder
	 * @param fileArchiveName -
	 *            the file archive name
	 * @return Whether the compound file was created successfully.
	 */
	static public void archive( String folderName, IStreamSorter sorter,
			String fileName ) throws IOException
	{
		// Delete existing file or folder that has the same
		// name of the file archive.
		folderName = new File( folderName ).getCanonicalPath( );
		FolderArchiveReader reader = new FolderArchiveReader( folderName );
		try
		{
			reader.open( );
			File file = new File( fileName );
			if ( file.exists( ) )
			{
				if ( file.isFile( ) )
				{
					file.delete( );
				}
			}
			FileArchiveWriter writer = new FileArchiveWriter( fileName );
			try
			{
				writer.initialize( );
				copy( reader, writer );
			}
			finally
			{
				writer.finish( );
			}
		}
		finally
		{
			reader.close( );
		}
	}
	
	
	/**
	 * Compound File Format: <br>
	 * 1long(stream section position) + 1long(entry number in lookup map) +
	 * lookup map section + stream data section <br>
	 * The Lookup map is a hash map. The key is the relative path of the stram.
	 * The entry contains two long number. The first long is the start postion.
	 * The second long is the length of the stream. <br>
	 * 
	 * @param tempFolder
	 * @param fileArchiveName -
	 *            the file archive name
	 * @return Whether the compound file was created successfully.
	 */
	static public void archive( String folderName, IStreamSorter sorter,
			String fileName, boolean contentEscape ) throws IOException
	{
		// Delete existing file or folder that has the same
		// name of the file archive.
		folderName = new File( folderName ).getCanonicalPath( );
		FolderArchiveReader reader = new FolderArchiveReader( folderName, contentEscape );
		try
		{
			reader.open( );
			File file = new File( fileName );
			if ( file.exists( ) )
			{
				if ( file.isFile( ) )
				{
					file.delete( );
				}
			}
			FileArchiveWriter writer = new FileArchiveWriter( fileName );
			try
			{
				writer.initialize( );
				copy( reader, writer );
			}
			finally
			{
				writer.finish( );
			}
		}
		finally
		{
			reader.close( );
		}
	}

	/**
	 * files used to record the reader count reference.
	 */
	static final String READER_COUNT_FILE_NAME = "/.reader.count";
	/**
	 * files which should not be copy into the archives
	 */
	static final String[] SKIP_FILES = new String[]{READER_COUNT_FILE_NAME};

	static boolean needSkip( String file )
	{
		for ( int i = 0; i < SKIP_FILES.length; i++ )
		{
			if ( SKIP_FILES[i].equals( file ) )
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Get all the files under the specified folder (including all the files
	 * under sub-folders)
	 * 
	 * @param dir -
	 *            the folder to look into
	 * @param fileList -
	 *            the fileList to be returned
	 */
	public static void listAllFiles( File dir, ArrayList<? super File> fileList )
	{
		if ( dir.exists( ) && dir.isDirectory( ) )
		{
			File[] files = dir.listFiles( );
			if ( files == null )
				return;

			for ( int i = 0; i < files.length; i++ )
			{
				File file = files[i];
				if ( file.isFile( ) )
				{
					fileList.add( file );
				}
				else if ( file.isDirectory( ) )
				{
					listAllFiles( file, fileList );
				}
			}
		}
	}

	static public void expand( String file, String folder ) throws IOException
	{
		FileArchiveReader reader = new FileArchiveReader( file );
		try
		{
			reader.open( );
			reader.expandFileArchive( folder );
		}
		finally
		{
			reader.close( );
		}
	}

	/**
	 * Assemble four bytes to an int value, make sure that the passed bytes
	 * length is larger than 4.
	 * 
	 * @param bytes
	 * @return int value of bytes
	 */
	public final static int bytesToInteger( byte[] b )
	{
		assert b.length >= 4;
		return ( ( b[0] & 0xFF ) << 24 ) + ( ( b[1] & 0xFF ) << 16 )
				+ ( ( b[2] & 0xFF ) << 8 ) + ( ( b[3] & 0xFF ) << 0 );
	}

	public final static int bytesToInteger( byte[] b, int off )
	{
		assert b.length - off >= 4;
		return ( ( b[off++] & 0xFF ) << 24 ) + ( ( b[off++] & 0xFF ) << 16 )
				+ ( ( b[off++] & 0xFF ) << 8 ) + ( ( b[off] & 0xFF ) << 0 );
	}

	/**
	 * Assemble eight bytes to an long value, make sure that the passed bytes
	 * length larger than 8.
	 * 
	 * @param bytes
	 * @return int value of bytes
	 */
	public final static long bytesToLong( byte[] b )
	{
		assert b.length >= 8;
		return ( ( b[0] & 0xFFL ) << 56 ) + ( ( b[1] & 0xFFL ) << 48 )
				+ ( ( b[2] & 0xFFL ) << 40 ) + ( ( b[3] & 0xFFL ) << 32 )
				+ ( ( b[4] & 0xFFL ) << 24 ) + ( ( b[5] & 0xFFL ) << 16 )
				+ ( ( b[6] & 0xFFL ) << 8 ) + ( ( b[7] & 0xFFL ) << 0 );

	}

	public final static long bytesToLong( byte[] b, int off )
	{
		assert b.length - off >= 8;
		return ( ( b[off++] & 0xFFL ) << 56 ) + ( ( b[off++] & 0xFFL ) << 48 )
				+ ( ( b[off++] & 0xFFL ) << 40 )
				+ ( ( b[off++] & 0xFFL ) << 32 )
				+ ( ( b[off++] & 0xFFL ) << 24 )
				+ ( ( b[off++] & 0xFFL ) << 16 ) + ( ( b[off++] & 0xFFL ) << 8 )
				+ ( ( b[off] & 0xFFL ) << 0 );
	}
	
	public final static void integerToBytes( int v, byte[] b )
	{
		assert b.length >= 4;
		b[0] = (byte) ( ( v >>> 24 ) & 0xFF );
		b[1] = (byte) ( ( v >>> 16 ) & 0xFF );
		b[2] = (byte) ( ( v >>> 8 ) & 0xFF );
		b[3] = (byte) ( ( v >>> 0 ) & 0xFF );
	}

	public final static void integerToBytes( int v, byte[] b, int off )
	{
		assert b.length - off >= 4;
		b[off++] = (byte) ( ( v >>> 24 ) & 0xFF );
		b[off++] = (byte) ( ( v >>> 16 ) & 0xFF );
		b[off++] = (byte) ( ( v >>> 8 ) & 0xFF );
		b[off] = (byte) ( ( v >>> 0 ) & 0xFF );
	}
	
	public final static void longToBytes( long v, byte[] b )
	{
		assert b.length >= 8;
		b[0] = (byte) ( ( v >>> 56 ) & 0xFF );
		b[1] = (byte) ( ( v >>> 48 ) & 0xFF );
		b[2] = (byte) ( ( v >>> 40 ) & 0xFF );
		b[3] = (byte) ( ( v >>> 32 ) & 0xFF );
		b[4] = (byte) ( ( v >>> 24 ) & 0xFF );
		b[5] = (byte) ( ( v >>> 16 ) & 0xFF );
		b[6] = (byte) ( ( v >>> 8 ) & 0xFF );
		b[7] = (byte) ( ( v >>> 0 ) & 0xFF );
	}
	
	public final static void longToBytes( long v, byte[] b, int off )
	{
		assert b.length - off >= 8;
		b[off++] = (byte) ( ( v >>> 56 ) & 0xFF );
		b[off++] = (byte) ( ( v >>> 48 ) & 0xFF );
		b[off++] = (byte) ( ( v >>> 40 ) & 0xFF );
		b[off++] = (byte) ( ( v >>> 32 ) & 0xFF );
		b[off++] = (byte) ( ( v >>> 24 ) & 0xFF );
		b[off++] = (byte) ( ( v >>> 16 ) & 0xFF );
		b[off++] = (byte) ( ( v >>> 8 ) & 0xFF );
		b[off] = (byte) ( ( v >>> 0 ) & 0xFF );
	}
	
	public static boolean removeFileAndFolder( File file )
	{
		assert ( file != null );
		if ( file.isDirectory( ) )
		{
			File[] children = file.listFiles( );
			if ( children != null )
			{
				for ( int i = 0; i < children.length; i++ )
				{
					removeFileAndFolder( children[i] );
				}
			}
		}
		if ( file.exists( ) )
		{
			return file.delete( );
		}
		return true;
	}

	public final static IDocArchiveReader createReader(
			final IDocArchiveWriter writer )
	{
		return new IDocArchiveReader( ) {

			@Override
			public String getName( )
			{
				return writer.getName( );
			}

			@Override
			public void open( ) throws IOException
			{
			}

			@Override
			public RAInputStream getStream( String relativePath )
					throws IOException
			{
				return writer.getInputStream( relativePath );
			}

			@Override
			public RAInputStream getInputStream( String relativePath )
					throws IOException
			{
				return writer.getInputStream( relativePath );
			}

			@Override
			public boolean exists( String relativePath )
			{
				return writer.exists( relativePath );
			}

			@Override
			public List<String> listStreams( String relativeStoragePath )
					throws IOException
			{
				return writer.listStreams( relativeStoragePath );
			}

			@Override
			public List<String> listAllStreams( ) throws IOException
			{
				return writer.listAllStreams( );
			}

			@Override
			public void close( ) throws IOException
			{
			}

			@Override
			public Object lock( String stream ) throws IOException
			{
				return writer.lock( stream );
			}

			@Override
			public void unlock( Object locker )
			{
				writer.unlock( locker );
			}
		};
	}
	
}