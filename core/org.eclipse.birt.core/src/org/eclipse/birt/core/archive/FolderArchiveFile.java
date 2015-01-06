package org.eclipse.birt.core.archive;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.archive.compound.ArchiveEntry;
import org.eclipse.birt.core.archive.compound.ArchiveFileV3;
import org.eclipse.birt.core.archive.compound.IArchiveFile;
import org.eclipse.birt.core.i18n.CoreMessages;
import org.eclipse.birt.core.i18n.ResourceConstants;
import org.eclipse.birt.core.util.IOUtil;



public class FolderArchiveFile implements IArchiveFile
{
	
	public static final String METEDATA = ".metedata";
	static Logger logger = Logger.getLogger( FolderArchiveFile.class
			.getName( ) );
	protected String folderName;
	protected String systemId;
	protected String dependId;
	private HashSet<RAFolderInputStream> inputStreams = new HashSet<RAFolderInputStream>( );
	private HashSet<RAFolderOutputStream> outputStreams = new HashSet<RAFolderOutputStream>( );
	protected String fileName;
	
	protected Map properties = new HashMap();
	
	public FolderArchiveFile (String name)  throws IOException
	{
		if ( name == null || name.length( ) == 0 )
			throw new IOException(
					CoreMessages.getString( ResourceConstants.FOLDER_NAME_IS_NULL ) );

		this.fileName = new File( name ).getCanonicalPath( );
		this.folderName = fileName;
	}
	public String getName( )
	{
		return folderName;
	}

	public void close( ) throws IOException
	{
		RAFolderOutputStream outputStream = null;
		DataOutputStream data = null;
		try
		{
			// serialize meta data into .metedata file
			String meta = ArchiveUtil.generateFullContentPath( folderName, METEDATA );
			File file = new File( meta );

			outputStream = new RAFolderOutputStream( null, file );
			data = new DataOutputStream( outputStream );
			IOUtil.writeMap( data, this.properties );
		}
		finally
		{
			if ( data != null )
			{
				data.close( );
			}
			if ( outputStream != null )
			{
				outputStream.close( );
			}
		}
		
		
		
		IOException exception = null;
		synchronized ( outputStreams )
		{
			ArrayList<RAFolderOutputStream> outputs = new ArrayList<RAFolderOutputStream>(
					outputStreams );
			for ( RAFolderOutputStream output : outputs )
			{
				try
				{
					output.close( );
				}
				catch ( IOException ex )
				{
					logger.log(Level.SEVERE, ex.getMessage( ), ex);
					if ( exception != null )
					{
						exception = ex;
					}
				}
			}
			outputStreams.clear( );
		}
		synchronized ( inputStreams )
		{
			ArrayList<RAFolderInputStream> inputs = new ArrayList<RAFolderInputStream>(
					inputStreams );
			for ( RAFolderInputStream input : inputs )
			{
				try
				{
					input.close( );
				}
				catch ( IOException ex )
				{
					logger.log( Level.SEVERE, ex.getMessage( ), ex );
					if ( exception != null )
					{
						exception = ex;
					}
				}
			}
			inputStreams.clear( );
		}
		if ( exception != null )
		{
			throw exception;
		}
		//ArchiveUtil.archive( folderName, null, fileName );
		
	}

	public void flush( ) throws IOException
	{
		IOException ioex = null;
		synchronized ( outputStreams )
		{
			
			for ( RAOutputStream output : outputStreams )
			{
				try
				{
					output.flush( );
				}
				catch ( IOException ex )
				{
					logger.log(Level.SEVERE, ex.getMessage( ), ex);
					if ( ioex != null )
					{
						ioex = ex;
					}
				}
				
			}
		}
		if ( ioex != null )
		{
			throw ioex;
		}
		
	}

	public void refresh( ) throws IOException
	{
		// TODO Auto-generated method stub
		
	}

	public boolean exists( String name )
	{
		String path = ArchiveUtil.generateFullContentPath( folderName, name );
		File fd = new File( path );
		return fd.exists( );
	}

	public void setCacheSize( long cacheSize )
	{
		// TODO Auto-generated method stub
		
	}

	public long getUsedCache( )
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public ArchiveEntry openEntry( String name ) throws IOException
	{
		String fullPath = ArchiveUtil.generateFullContentPath( folderName,
				name );
		File fd = new File( fullPath );
		if(fd.exists( ))
		{
			return new FolderArchiveEntry( fullPath, fd, inputStreams, outputStreams );
		}
		throw new FileNotFoundException( fullPath );
	}

	public List<String> listEntries( String namePattern )
	{
		ArrayList<String> streamList = new ArrayList<String>( );
		String storagePath = ArchiveUtil.generateFullPath( folderName,
				namePattern );
		File dir = new File( storagePath );

		if ( dir.exists( ) && dir.isDirectory( ) )
		{
			File[] files = dir.listFiles( );
			if ( files != null )
			{
				for ( int i = 0; i < files.length; i++ )
				{
					File file = files[i];
					if ( file.isFile( ) )
					{
						String relativePath = ArchiveUtil.generateRelativeContentPath(
								folderName, file.getPath( ) );
						if ( !ArchiveUtil.needSkip( relativePath ) )
						{
							streamList.add( relativePath );
						}
					}
				}
			}
		}

		return streamList;
	}

	public ArchiveEntry createEntry( String name ) throws IOException
	{
		String path = ArchiveUtil.generateFullContentPath( folderName, name );
		File fd = new File( path );

		ArchiveUtil.createParentFolder( fd );

		FolderArchiveEntry out = new FolderArchiveEntry( path, fd, inputStreams, outputStreams  );
		return out;
	}

	public boolean removeEntry( String name ) throws IOException
	{
		String path = ArchiveUtil.generateFullContentPath( folderName, name );
		try
		{
			File fd = new File( path );
			return ArchiveUtil.removeFileAndFolder( fd );
		}
		finally
		{
			synchronized ( outputStreams )
			{
				ArrayList<RAFolderOutputStream> outputs = new ArrayList<RAFolderOutputStream>(
						outputStreams );
				for ( RAFolderOutputStream output : outputs )
				{
					try
					{
						if(path.equals( output.getName( ) ))
						{
							output.close( );
						}
					}
					catch ( IOException ex )
					{
						logger.log(Level.SEVERE, ex.getMessage( ), ex);
						throw ex;
					}
				}
			}
		}
		
	}

	public Object lockEntry( String entry ) throws IOException
	{
		String path = ArchiveUtil.generateFullContentPath( folderName, entry)  + ".lck";
		IArchiveLockManager lockManager = ArchiveLockManager.getInstance( );
		return lockManager.lock( path );
	}

	public void unlockEntry( Object locker ) throws IOException
	{
		IArchiveLockManager lockManager = ArchiveLockManager.getInstance( );
		lockManager.unlock( locker );
		
	}

	public String getSystemId( )
	{
		if ( properties.containsKey( ArchiveFileV3.PROPERTY_SYSTEM_ID ) )
		{
			return properties.get( ArchiveFileV3.PROPERTY_SYSTEM_ID )
					.toString( );
		}
		return null;
	}

	public String getDependId( )
	{
		if ( properties.containsKey( ArchiveFileV3.PROPERTY_DEPEND_ID ) )
		{
			return properties.get( ArchiveFileV3.PROPERTY_DEPEND_ID )
					.toString( );
		}
		return null;
	}
	
	public void setSystemId(String systemId)
	{
		if(systemId!=null)
		{
			this.properties.put( ArchiveFileV3.PROPERTY_SYSTEM_ID, systemId );
		}
	}
	
	public void setDependId(String dependId)
	{
		if(dependId!=null)
		{
			this.properties.put( ArchiveFileV3.PROPERTY_DEPEND_ID, dependId );
		}
	}
	
	public void save( ) throws IOException
	{
		flush();
	}

	public long getSize( )
	{
		long result = 0;
		List<String> entries = listEntries( null );
		for( String entry : entries )
		{
			try
			{
				result += openEntry( entry ).getLength( );
			}
			catch ( IOException e )
			{
				e.printStackTrace();
			}
		}
		return result;
	}
}
