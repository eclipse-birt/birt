
package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import java.net.URL;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.IResourceLocator;

public class ResourceKeyDescriptorProvider extends PropertyDescriptorProvider implements IResourceKeyDescriptorProvider
{

	public ResourceKeyDescriptorProvider( String property, String element )
	{
		super( property, element );
	}

	public String getBaseName( )
	{
		return SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( )
				.getIncludeResource( );
	}

	public URL getResourceURL( )
	{
		return SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( )
				.findResource( getBaseName( ), IResourceLocator.MESSAGE_FILE );
	}

	public String getBrowseText( )
	{
		return "..."; //$NON-NLS-1$
	}

	public String getResetText( )
	{
		return Messages.getString( "ResourceKeyDescriptor.text.Reset" ); //$NON-NLS-1$
	}

	public boolean isEnable( )
	{
		return !( DEUtil.getInputSize( input ) > 1 );
	}

	public String getBrowseTooltipText( )
	{
		return Messages.getString( "ResourceKeyDescriptor.button.browse.tooltip" ); //$NON-NLS-1$
	}

	public String getResetTooltipText( )
	{

		return Messages.getString( "ResourceKeyDescriptor.button.reset.tooltip" ); //$NON-NLS-1$
	}

}
