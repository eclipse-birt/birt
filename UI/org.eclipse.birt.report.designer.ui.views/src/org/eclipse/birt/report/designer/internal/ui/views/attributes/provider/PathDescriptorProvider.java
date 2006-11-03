
package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

public class PathDescriptorProvider implements ITextDescriptorProvider
{

	private Object input;

	public boolean isReadOnly( )
	{
		return true;
	}

	public String getDisplayName( )
	{
		return Messages.getString( "ModulePage.text.Path" );
	}

	public Object load( )
	{
		if ( input == null )
			return "";
		DesignElementHandle handle = (DesignElementHandle) DEUtil.getInputFirstElement( input );
		if ( handle != null )
			return ( (ModuleHandle) handle ).getFileName( );
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
