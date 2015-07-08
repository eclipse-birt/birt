package org.eclipse.birt.core.archive;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

import org.eclipse.birt.core.archive.compound.ArchiveEntry;



public class FolderArchiveEntry extends ArchiveEntry
{
	protected File file;
	RAInputStream in;
	RAOutputStream out;
	public FolderArchiveEntry(String name, File file, HashSet<RAFolderInputStream> inputs, HashSet<RAFolderOutputStream> outputs) throws IOException
	{
		super(name);
		this.file = file;
		this.out = new RAFolderOutputStream( outputs, file, true );
		this.in = new RAFolderInputStream( inputs, file );
		
		
	}
	public long getLength( ) throws IOException
	{
		return file.length( );
	}

	public void setLength( long length ) throws IOException
	{
		
	}

	public int read( long pos, byte[] b, int off, int len ) throws IOException
	{
		in.seek( pos );
		return in.read( b, off, len );
	}

	public void write( long pos, byte[] b, int off, int len )
			throws IOException
	{
		out.seek( pos );
		out.write( b, off, len );
		out.flush( );
	}

	public void close( ) throws IOException
	{
		if ( in != null )
		{
			in.close( );
			in = null;
		}
		if ( out != null )
		{
			out.close( );
			out = null;
		}
	}

}
