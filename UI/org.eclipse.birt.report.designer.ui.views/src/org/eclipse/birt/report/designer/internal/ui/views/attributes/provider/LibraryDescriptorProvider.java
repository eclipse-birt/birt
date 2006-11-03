
package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

public class LibraryDescriptorProvider implements ITextDescriptorProvider
{

	private Object input;

	public boolean isReadOnly( )
	{
		return true;
	}

	public String getDisplayName( )
	{
		return Messages.getString( "GeneralPage.Library.Included" );
	}

	public Object load( )
	{
		if ( input == null )
			return "";
		DesignElementHandle handle = (DesignElementHandle) DEUtil.getInputFirstElement( input );
		if ( handle.getExtends( ) == null )
			return "";
		try
		{
			File libraryFile = new File( new URL( handle.getExtends( )
					.getRoot( )
					.getFileName( ) ).getFile( ) );
			if ( libraryFile.exists( ) )
				return libraryFile.getAbsolutePath( );
		}
		catch ( MalformedURLException e )
		{
			e.printStackTrace( );
		}
		return "";
	}

	public void save( Object value ) throws SemanticException
	{
	}

	public void setInput( Object input )
	{
		this.input = input;
	}

}
