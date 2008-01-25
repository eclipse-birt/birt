
package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.FormatChangeEvent;
import org.eclipse.birt.report.designer.internal.ui.dialogs.IFormatChangeListener;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.StringFormatValue;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;

public class FormatStringDescriptorProvider implements IDescriptorProvider
{

	private static String[][] choiceArray = null;
	private static String[] formatTypes = null;
	/**
	 * Listener, or <code>null</code> if none
	 */
	private java.util.List listeners = new ArrayList( );

	public String getDisplayName( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Object load( )
	{
		if ( DEUtil.getInputElements( input ).isEmpty( ) )
		{
			return null;
		}
		String baseCategory = ( (DesignElementHandle) DEUtil.getInputFirstElement( input ) ).getPrivateStyle( )
				.getStringFormatCategory( );
		String basePattern = ( (DesignElementHandle) (DesignElementHandle) DEUtil.getInputFirstElement( input ) ).getPrivateStyle( )
				.getStringFormat( );

		for ( Iterator iter = DEUtil.getInputElements( input ).iterator( ); iter.hasNext( ); )
		{
			DesignElementHandle handle = (DesignElementHandle) iter.next( );
			String category = handle.getPrivateStyle( )
					.getStringFormatCategory( );
			String pattern = handle.getPrivateStyle( ).getStringFormat( );

			if ( ( ( baseCategory == null && category == null ) || ( baseCategory != null && baseCategory.equals( category ) ) )
					&& ( ( basePattern == null && pattern == null ) || ( basePattern != null && basePattern.equals( pattern ) ) ) )
			{
				continue;
			}
			return null;
		}
		return new String[]{
				baseCategory, basePattern
		};
	}

	public void save( Object value ) throws SemanticException
	{
		String[] values = (String[]) value;
		if ( values.length != 2 )
			return;
		CommandStack stack = SessionHandleAdapter.getInstance( )
				.getCommandStack( );
		stack.startTrans( Messages.getString( "FormatStringAttributePage.Trans.SetStringFormat" ) ); //$NON-NLS-1$

		for ( Iterator iter = DEUtil.getInputElements( input ).iterator( ); iter.hasNext( ); )
		{
			DesignElementHandle element = (DesignElementHandle) iter.next( );
			try
			{
				if ( values[0] == null && values[1] == null )
				{
					element.setProperty( IStyleModel.STRING_FORMAT_PROP, null );
				}
				else
				{
					element.getPrivateStyle( )
							.setStringFormatCategory( values[0] );
					element.getPrivateStyle( ).setStringFormat( values[1] );
				}
			}
			catch ( SemanticException e )
			{
				stack.rollbackAll( );
				ExceptionHandler.handle( e );
			}
		}
		stack.commit( );

	}

	private Object input;

	public void setInput( Object input )
	{
		this.input = input;
	}

	public String[][] initChoiceArray( )
	{
		if ( choiceArray == null )
		{
			IChoiceSet set = ChoiceSetFactory.getStructChoiceSet( StringFormatValue.FORMAT_VALUE_STRUCT,
					StringFormatValue.CATEGORY_MEMBER );
			IChoice[] choices = set.getChoices( );
			if ( choices.length > 0 )
			{
				choiceArray = new String[4][2];
				for ( int i = 0, j = 0; i < choices.length; i++ )
				{
					if ( choices[i].getName( )
							.equals( DesignChoiceConstants.STRING_FORMAT_TYPE_UNFORMATTED )
							|| choices[i].getName( )
									.equals( DesignChoiceConstants.STRING_FORMAT_TYPE_UPPERCASE )
							|| choices[i].getName( )
									.equals( DesignChoiceConstants.STRING_FORMAT_TYPE_LOWERCASE )
							|| choices[i].getName( )
									.equals( DesignChoiceConstants.STRING_FORMAT_TYPE_CUSTOM ) )
					{
						choiceArray[j][0] = choices[i].getDisplayName( );
						choiceArray[j][1] = choices[i].getName( );
						j++;
					}
				}
			}
			else
			{
				choiceArray = new String[0][0];
			}
		}
		return choiceArray;
	}

	public String[] getFormatTypes( )
	{
		if ( initChoiceArray( ) != null )
		{
			formatTypes = new String[choiceArray.length];
			for ( int i = 0; i < choiceArray.length; i++ )
			{
				formatTypes[i] = choiceArray[i][0];
			}
		}
		else
		{
			formatTypes = new String[0];
		}
		return formatTypes;
	}

	public int getIndexOfCategory( String name )
	{
		if ( choiceArray != null )
		{
			for ( int i = 0; i < choiceArray.length; i++ )
			{
				if ( choiceArray[i][1].equals( name ) )
				{
					return i;
				}
			}
		}
		return 0;
	}

	/**
	 * Gets the corresponding category for given display name.
	 */

	public String getCategory4DisplayName( String displayName )
	{
		if ( choiceArray != null )
		{
			for ( int i = 0; i < choiceArray.length; i++ )
			{
				if ( choiceArray[i][0].equals( displayName ) )
				{
					return choiceArray[i][1];
				}
			}
		}
		return displayName;
	}

	public String getDisplayName4Category( String category )
	{
		return ChoiceSetFactory.getStructDisplayName( StringFormatValue.FORMAT_VALUE_STRUCT,
				StringFormatValue.CATEGORY_MEMBER,
				category );
	}

	public void fireFormatChanged( String newCategory, String newPattern )
	{
		if ( listeners.isEmpty( ) )
		{
			return;
		}
		FormatChangeEvent event = new FormatChangeEvent( this,
				StyleHandle.STRING_FORMAT_PROP,
				newCategory,
				newPattern );
		for ( Iterator iter = listeners.iterator( ); iter.hasNext( ); )
		{
			Object listener = iter.next( );
			if ( listener instanceof IFormatChangeListener )
			{
				( (IFormatChangeListener) listener ).formatChange( event );
			}
		}
	}

	public void addFormatChangeListener( IFormatChangeListener listener )
	{
		if ( !listeners.contains( listener ) )
		{
			listeners.add( listener );
		}
	}

	public boolean isBlank( String fmtStr )
	{
		return StringUtil.isBlank( fmtStr );
	}

	public String STRING_FORMAT_TYPE_UNFORMATTED = DesignChoiceConstants.STRING_FORMAT_TYPE_UNFORMATTED;

	public String STRING_FORMAT_TYPE_UPPERCASE = DesignChoiceConstants.STRING_FORMAT_TYPE_UPPERCASE;

	public String STRING_FORMAT_TYPE_LOWERCASE = DesignChoiceConstants.STRING_FORMAT_TYPE_LOWERCASE;

	public String STRING_FORMAT_TYPE_CUSTOM = DesignChoiceConstants.STRING_FORMAT_TYPE_CUSTOM;

	public String STRING_FORMAT_TYPE_ZIP_CODE = DesignChoiceConstants.STRING_FORMAT_TYPE_ZIP_CODE;

	public String STRING_FORMAT_TYPE_ZIP_CODE_4 = DesignChoiceConstants.STRING_FORMAT_TYPE_ZIP_CODE_4;

	public String STRING_FORMAT_TYPE_PHONE_NUMBER = DesignChoiceConstants.STRING_FORMAT_TYPE_PHONE_NUMBER;

	public String STRING_FORMAT_TYPE_SOCIAL_SECURITY_NUMBER = DesignChoiceConstants.STRING_FORMAT_TYPE_SOCIAL_SECURITY_NUMBER;

	public String FORMAT_VALUE_STRUCT = StringFormatValue.FORMAT_VALUE_STRUCT;

	public String CATEGORY_MEMBER = StringFormatValue.CATEGORY_MEMBER;

}
