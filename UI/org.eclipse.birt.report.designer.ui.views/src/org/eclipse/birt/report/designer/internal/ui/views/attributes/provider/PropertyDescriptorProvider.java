
package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import java.util.List;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.GroupElementHandle;
import org.eclipse.birt.report.model.api.GroupPropertyHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;

public class PropertyDescriptorProvider implements IDescriptorProvider
{

	private String property;
	private String element;
	protected Object input;

	public PropertyDescriptorProvider( String property, String element )
	{
		this.property = property;
		this.element = element;
	}

	public Object load( )
	{
		String value = null;
		if ( input instanceof GroupElementHandle )
		{
			value = ( (GroupElementHandle) input ).getStringProperty( property );
		}
		else if ( input instanceof List )
		{
			value = DEUtil.getGroupElementHandle( (List) input )
					.getStringProperty( property );
		}
		return value == null ? "" : value; //$NON-NLS-1$
	}

	public void save( Object value ) throws SemanticException
	{
		if ( input instanceof GroupElementHandle )
		{
			( (GroupElementHandle) input ).setProperty( property, value );
		}
		else if ( input instanceof List )
		{
			DEUtil.getGroupElementHandle( (List) input ).setProperty( property,
					value );
		}
	}

	public String getDisplayName( )
	{
		IElementPropertyDefn propertyDefn;
		String name = null;
		if ( input instanceof GroupElementHandle && input != null )
		{
			propertyDefn = ( (GroupElementHandle) input ).getPropertyHandle( property )
					.getPropertyDefn( );
			if ( propertyDefn != null )
			{
				name = propertyDefn.getDisplayName( );
			}
		}
		else
		{
			propertyDefn = DesignEngine.getMetaDataDictionary( )
					.getElement( element )
					.getProperty( property );
			if ( propertyDefn != null )
			{
				name = Messages.getString( propertyDefn.getDisplayNameID( ) );
			}
		}

		if ( name == null )
			return ""; //$NON-NLS-1$
		return name;
	}

	public String getLocalStringValue( )
	{
		if ( input instanceof GroupElementHandle )
		{
			String value = ( (GroupElementHandle) input ).getLocalStringProperty( property );
			if ( value == null )
				return ""; //$NON-NLS-1$
			else
				return value;
		}
		return ""; //$NON-NLS-1$
	}

	public void setInput( Object input )
	{
		this.input = input;
	}

	protected String getElement( )
	{
		return element;
	}

	protected String getProperty( )
	{
		return property;
	}

	public boolean isReadOnly( )
	{
		if ( input instanceof GroupElementHandle )
		{
			GroupPropertyHandle propertyHandle = ( (GroupElementHandle) input ).getPropertyHandle( property );
			if ( propertyHandle != null )
			{
				return propertyHandle.isReadOnly( );
			}
		}
		return false;
	}


}
