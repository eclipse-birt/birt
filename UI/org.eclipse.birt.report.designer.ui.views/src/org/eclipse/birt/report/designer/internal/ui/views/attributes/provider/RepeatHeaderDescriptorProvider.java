
package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

public class RepeatHeaderDescriptorProvider implements IDescriptorProvider
{

	public String getDisplayName( )
	{
		return Messages.getString( "ListingSectionPage.RepeatHeader" );
	}

	public Object load( )
	{
		if ( DEUtil.getInputSize( input ) == 1
				&& DEUtil.getInputFirstElement( input ) instanceof ListingHandle )
		{
			ListingHandle listingHandle = (ListingHandle) DEUtil.getInputFirstElement( input );

			return new Boolean( listingHandle.repeatHeader( ) ).toString( );

		}
		return "false";
	}

	public void save( Object value ) throws SemanticException
	{
		if ( DEUtil.getInputSize( input ) == 1
				&& DEUtil.getInputFirstElement( input ) instanceof ListingHandle )
		{
			ListingHandle listingHandle = (ListingHandle) DEUtil.getInputFirstElement( input );
			try
			{
				listingHandle.setRepeatHeader( Boolean.valueOf( value.toString( ) ).booleanValue( ) );
			}
			catch ( SemanticException e )
			{
			}
			

		}
	}

	private Object input;

	public void setInput( Object input )
	{
		this.input = input;

	}
	


}
