
package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.format.DateFormatter;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.FormatChangeEvent;
import org.eclipse.birt.report.designer.internal.ui.dialogs.IFormatChangeListener;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.designer.util.FormatDateTimePattern;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.DateTimeFormatValue;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;

public class FormatDataTimeDescriptorProvider implements IDescriptorProvider
{

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
				.getDateTimeFormatCategory( );
		String basePattern = ( (DesignElementHandle) (DesignElementHandle) DEUtil.getInputFirstElement( input ) ).getPrivateStyle( )
				.getDateTimeFormat( );

		for ( Iterator iter = DEUtil.getInputElements( input ).iterator( ); iter.hasNext( ); )
		{
			DesignElementHandle handle = (DesignElementHandle) iter.next( );
			String category = handle.getPrivateStyle( )
					.getDateTimeFormatCategory( );
			String pattern = handle.getPrivateStyle( ).getDateTimeFormat( );

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
		String[] result = (String[]) value;
		if ( result.length == 2 )
		{
			SessionHandleAdapter.getInstance( )
					.getCommandStack( )
					.startTrans( Messages.getString( "FormatDateTimeAttributePage.Trans.SetDateTimeFormat" ) ); //$NON-NLS-1$

			for ( Iterator iter = DEUtil.getInputElements( input ).iterator( ); iter.hasNext( ); )
			{
				DesignElementHandle element = (DesignElementHandle) iter.next( );
				try
				{
					if ( result[0] == null && result[1] == null )
					{
						element.setProperty( IStyleModel.DATE_TIME_FORMAT_PROP,
								null );
					}
					else
					{

						element.getPrivateStyle( )
								.setDateTimeFormatCategory( result[0] );
						element.getPrivateStyle( )
								.setDateTimeFormat( result[1] );
					}
				}
				catch ( SemanticException e )
				{
					ExceptionHandler.handle( e );
					SessionHandleAdapter.getInstance( )
							.getCommandStack( )
							.rollbackAll( );
					return;
				}
			}
			SessionHandleAdapter.getInstance( ).getCommandStack( ).commit( );

		}

	}

	private Object input;

	public void setInput( Object input )
	{
		this.input = input;

	}

	private Date defaultDate;

	public FormatDataTimeDescriptorProvider( )
	{
		defaultDate = new Date( );
	}

	private String getDisplayName4Category( String category )
	{
		return ChoiceSetFactory.getStructDisplayName( DateTimeFormatValue.FORMAT_VALUE_STRUCT,
				DateTimeFormatValue.CATEGORY_MEMBER,
				category );
	}

	public String[][] getTableItems( )
	{
		List itemList = new ArrayList( );
		String[][] items = new String[][]{
				new String[]{
						getDisplayName4Category( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_GENERAL_DATE ),
						new DateFormatter( FormatDateTimePattern.getPatternForCategory( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_GENERAL_DATE ) ).format( defaultDate ),
						new DateFormatter( FormatDateTimePattern.getPatternForCategory( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_GENERAL_DATE ) ).getFormatCode( )
				},
				new String[]{
						getDisplayName4Category( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_LONG_DATE ),
						new DateFormatter( FormatDateTimePattern.getPatternForCategory( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_LONG_DATE ) ).format( defaultDate ),
						new DateFormatter( FormatDateTimePattern.getPatternForCategory( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_LONG_DATE ) ).getFormatCode( )
				},
				new String[]{
						getDisplayName4Category( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_MUDIUM_DATE ),
						new DateFormatter( FormatDateTimePattern.getPatternForCategory( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_MUDIUM_DATE ) ).format( defaultDate ),
						new DateFormatter( FormatDateTimePattern.getPatternForCategory( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_MUDIUM_DATE ) ).getFormatCode( )
				},
				new String[]{
						getDisplayName4Category( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_SHORT_DATE ),
						new DateFormatter( FormatDateTimePattern.getPatternForCategory( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_SHORT_DATE ) ).format( defaultDate ),
						new DateFormatter( FormatDateTimePattern.getPatternForCategory( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_SHORT_DATE ) ).getFormatCode( )
				},
				new String[]{
						getDisplayName4Category( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_LONG_TIME ),
						new DateFormatter( FormatDateTimePattern.getPatternForCategory( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_LONG_TIME ) ).format( defaultDate ),
						new DateFormatter( FormatDateTimePattern.getPatternForCategory( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_LONG_TIME ) ).getFormatCode( )
				},
				new String[]{
						getDisplayName4Category( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_MEDIUM_TIME ),
						new DateFormatter( FormatDateTimePattern.getPatternForCategory( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_MEDIUM_TIME ) ).format( defaultDate ),
						new DateFormatter( FormatDateTimePattern.getPatternForCategory( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_MEDIUM_TIME ) ).getFormatCode( )
				},
				new String[]{
						getDisplayName4Category( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_SHORT_TIME ),
						new DateFormatter( FormatDateTimePattern.getPatternForCategory( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_SHORT_TIME ) ).format( defaultDate ),
						new DateFormatter( FormatDateTimePattern.getPatternForCategory( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_SHORT_TIME ) ).getFormatCode( )
				}
		};
		itemList.addAll( Arrays.asList( items ) );
		String[] customPatterns = FormatDateTimePattern.getCustormPatternCategorys( );
		for ( int i = 0; i < customPatterns.length; i++ )
		{
			itemList.add( new String[]{
					FormatDateTimePattern.getDisplayName4CustomCategory( customPatterns[i] ),
					new DateFormatter( FormatDateTimePattern.getCustormFormatPattern( customPatterns[i] ) ).format( defaultDate ),
					FormatDateTimePattern.getCustormFormatPattern( customPatterns[i] )
			} );
		}
		return (String[][]) itemList.toArray( new String[0][3] );
	}

	public String getCategory4UIDisplayName( String displayName )
	{
		if ( initChoiceArray( ) != null )
		{
			for ( int i = 0; i < choiceArray.length; i++ )
			{
				if ( formatTypes[i].equals( displayName ) )
				{
					return choiceArray[i][1];
				}
			}
		}
		return displayName;
	}

	private String[][] choiceArray = null;

	public String[][] initChoiceArray( )
	{
		if ( choiceArray == null )
		{
			IChoiceSet set = ChoiceSetFactory.getStructChoiceSet( DateTimeFormatValue.FORMAT_VALUE_STRUCT,
					DateTimeFormatValue.CATEGORY_MEMBER );
			IChoice[] choices = set.getChoices( );
			if ( choices.length > 0 )
			{
				choiceArray = new String[choices.length][2];
				for ( int i = 0, j = 0; i < choices.length; i++ )
				{
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

	/**
	 * Gets the format types for display names.
	 */

	private String[] formatTypes = null;

	public String[] getFormatTypes( )
	{
		if ( initChoiceArray( ) != null )
		{
			formatTypes = new String[choiceArray.length];
			for ( int i = 0; i < choiceArray.length; i++ )
			{
				String fmtStr = ""; //$NON-NLS-1$
				String category = choiceArray[i][1];
				if ( category.equals( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_CUSTOM )
						|| category.equals( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_UNFORMATTED ) )
				{
					fmtStr = choiceArray[i][0];
				}
				else
				{
					// uses UI specified display names.
					String pattern = FormatDateTimePattern.getPatternForCategory( category );
					fmtStr = new DateFormatter( pattern ).format( defaultDate );
				}
				formatTypes[i] = fmtStr;
			}
		}
		else
		{
			formatTypes = new String[0];
		}
		return formatTypes;
	}

	/**
	 * Gets the index of given category.
	 */

	public int getIndexOfCategory( String category )
	{
		if ( initChoiceArray( ) != null )
		{
			for ( int i = 0; i < choiceArray.length; i++ )
			{
				if ( choiceArray[i][1].equals( category ) )
				{
					return i;
				}
			}
		}
		return 0;
	}

	private java.util.List listeners = new ArrayList( );

	public void fireFormatChanged( String newCategory, String newPattern )
	{
		if ( listeners.isEmpty( ) )
		{
			return;
		}
		FormatChangeEvent event = new FormatChangeEvent( this,
				StyleHandle.DATE_TIME_FORMAT_PROP,
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

	public String getPattern( String displayName )
	{
		String category = ChoiceSetFactory.getStructPropValue( DateTimeFormatValue.FORMAT_VALUE_STRUCT,
				DateTimeFormatValue.CATEGORY_MEMBER,
				displayName );
		return FormatDateTimePattern.getPatternForCategory( category );
	}

	public String DATETIEM_FORMAT_TYPE_UNFORMATTED = DesignChoiceConstants.DATETIEM_FORMAT_TYPE_UNFORMATTED;
	public String DATETIEM_FORMAT_TYPE_CUSTOM = DesignChoiceConstants.DATETIEM_FORMAT_TYPE_CUSTOM;
	public String NUMBER_FORMAT_TYPE_CUSTOM = DesignChoiceConstants.NUMBER_FORMAT_TYPE_CUSTOM;
	public String DATETIEM_FORMAT_TYPE_GENERAL_DATE = DesignChoiceConstants.DATETIEM_FORMAT_TYPE_GENERAL_DATE;
	public String DATETIEM_FORMAT_TYPE_LONG_DATE = DesignChoiceConstants.DATETIEM_FORMAT_TYPE_LONG_DATE;
	public String DATETIEM_FORMAT_TYPE_MUDIUM_DATE = DesignChoiceConstants.DATETIEM_FORMAT_TYPE_MUDIUM_DATE;
	public String DATETIEM_FORMAT_TYPE_SHORT_DATE = DesignChoiceConstants.DATETIEM_FORMAT_TYPE_SHORT_DATE;
	public String DATETIEM_FORMAT_TYPE_LONG_TIME = DesignChoiceConstants.DATETIEM_FORMAT_TYPE_LONG_TIME;
	public String DATETIEM_FORMAT_TYPE_MEDIUM_TIME = DesignChoiceConstants.DATETIEM_FORMAT_TYPE_MEDIUM_TIME;
	public String DATETIEM_FORMAT_TYPE_SHORT_TIME = DesignChoiceConstants.DATETIEM_FORMAT_TYPE_SHORT_TIME;

}
