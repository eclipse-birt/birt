
package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import java.util.Iterator;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.ReportEngine;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.HideRuleHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.HideRule;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;

public class OutputPropertyDescriptorProvider extends AbstractDescriptorProvider
{

	private boolean updateHideRule( DesignElementHandle element, String format,
			boolean checked, String expression ) throws Exception
	{
		// save the output type
		if ( checked )
		{
			HideRuleHandle hideHandle = getHideRuleHandle( element, format );
			if ( hideHandle == null )
			{
				try
				{
					hideHandle = createHideRuleHandle( element,
							format,
							expression );
				}
				catch ( SemanticException e )
				{
					throw e;
				}
			}
			else
			{
				if ( !expression.equals( hideHandle.getExpression( ) )
						&& ( !expression.equals( "" ) || hideHandle.getExpression( ) != null ) ) //$NON-NLS-1$
				{
					hideHandle.setExpression( expression );
				}
			}
		}
		else
		{
			// remove the given output format
			Iterator visibilities = visibilityRulesIterator( element );
			if ( visibilities == null )
			{
				return true;
			}
			while ( visibilities.hasNext( ) )
			{
				HideRuleHandle handle = (HideRuleHandle) visibilities.next( );
				if ( format.equalsIgnoreCase( handle.getFormat( ) ) )
				{
					try
					{
						getVisibilityPropertyHandle( element ).removeItem( handle.getStructure( ) );
					}
					catch ( PropertyValueException e )
					{
						throw e;
					}
					return true;
				}
			}
		}
		return true;
	}

	/**
	 * Gets a given hide-output Handle.
	 * 
	 * @param handle
	 *            The ReportItemHandle.
	 * @param format
	 *            hide-output format.
	 * @return hide-output Handle.
	 */
	private HideRuleHandle getHideRuleHandle( DesignElementHandle handle,
			String format )
	{
		Iterator visibilities = visibilityRulesIterator( handle );
		if ( visibilities == null )
		{
			return null;
		}

		while ( visibilities.hasNext( ) )
		{
			HideRuleHandle hideHandle = (HideRuleHandle) visibilities.next( );
			if ( format.equalsIgnoreCase( hideHandle.getFormat( ) ) )
			{
				return hideHandle;
			}
		}
		return null;
	}

	/**
	 * Creates a new hide-output Handle.
	 * 
	 * @param format
	 *            hide-output format.
	 * @return hide-output Handle.
	 * @throws SemanticException
	 */
	private HideRuleHandle createHideRuleHandle( DesignElementHandle element,
			String format, String expression ) throws SemanticException
	{
		PropertyHandle propertyHandle = getVisibilityPropertyHandle( element );
		HideRule hide = StructureFactory.createHideRule( );

		hide.setFormat( format );
		hide.setExpression( expression );

		propertyHandle.addItem( hide );

		return (HideRuleHandle) hide.getHandle( propertyHandle );

	}

	/**
	 * Clears the VISIBILITY_PROP property value.
	 * 
	 * @return True if operation successes, false if fails.
	 */
	private boolean clearProperty( DesignElementHandle handle )
			throws Exception
	{
		if ( visibilityRulesIterator( handle ) != null )
		{
			try
			{
				if ( handle instanceof ReportItemHandle )
				{
					handle.clearProperty( ReportItemHandle.VISIBILITY_PROP );
				}
				else if ( handle instanceof RowHandle )
				{
					handle.clearProperty( RowHandle.VISIBILITY_PROP );
				}
				else if ( handle instanceof ColumnHandle )
				{
					handle.clearProperty( ColumnHandle.VISIBILITY_PROP );
				}

			}
			catch ( SemanticException e )
			{
				throw e;
			}
		}
		return true;
	}

	public void clearProperty( ) throws Exception
	{
		for ( Iterator iter = DEUtil.getInputElements( input ).iterator( ); iter.hasNext( ); )
		{
			DesignElementHandle handle = (DesignElementHandle) iter.next( );
			clearProperty( handle );
		}
	}

	/**
	 * Gets the PropertyHandle of VISIBILITY_PROP property.
	 * 
	 * @return PropertyHandle
	 */
	private PropertyHandle getVisibilityPropertyHandle(
			DesignElementHandle handle )
	{
		if ( handle == null )
		{
			return null;
		}
		if ( handle instanceof ReportItemHandle )
		{
			return handle.getPropertyHandle( ReportItemHandle.VISIBILITY_PROP );
		}
		else if ( handle instanceof RowHandle )
		{
			return handle.getPropertyHandle( RowHandle.VISIBILITY_PROP );
		}
		else if ( handle instanceof ColumnHandle )
		{
			return handle.getPropertyHandle( ColumnHandle.VISIBILITY_PROP );
		}
		else
		{
			return null;
		}

	}

	/**
	 * Gets the DE CommandStack instance
	 * 
	 * @return CommandStack instance
	 */
	private CommandStack getActionStack( )
	{
		return SessionHandleAdapter.getInstance( ).getCommandStack( );
	}

	public Iterator visibilityRulesIterator( DesignElementHandle handle )
	{
		Iterator visibilities = null;
		if ( handle instanceof ReportItemHandle )
		{
			visibilities = ( (ReportItemHandle) handle ).visibilityRulesIterator( );
		}
		else if ( handle instanceof RowHandle )
		{
			visibilities = ( (RowHandle) handle ).visibilityRulesIterator( );
		}
		else if ( handle instanceof ColumnHandle )
		{
			visibilities = ( (ColumnHandle) handle ).visibilityRulesIterator( );
		}
		return visibilities;
	}

	public boolean shareSameVisibility( )
	{
		return DEUtil.getGroupElementHandle( DEUtil.getInputElements( input ) )
				.shareSameValue( IReportItemModel.VISIBILITY_PROP );
	}

	private String[] typeInfo;

	public String[] getTypeInfo( )
	{
		if ( typeInfo == null )
		{
			ReportEngine engine = new ReportEngine( new EngineConfig( ) );
			typeInfo = engine.getSupportedFormats( );
		}
		return typeInfo;
	}

	public DesignElementHandle getFirstElementHandle( )
	{
		Object obj = DEUtil.getInputFirstElement( input );
		if ( obj instanceof ReportItemHandle )
		{
			return (ReportItemHandle) obj;
		}
		else if ( obj instanceof RowHandle )
		{
			return (RowHandle) obj;
		}
		else if ( obj instanceof ColumnHandle )
		{
			return (ColumnHandle) obj;
		}
		else
			return null;
	}

	public void saveAllOutput( String value ) throws Exception
	{
		CommandStack stack = getActionStack( );
		stack.startTrans( Messages.getString( "VisibilityPage.menu.SaveHides" ) ); //$NON-NLS-1$

		for ( Iterator iter = DEUtil.getInputElements( input ).iterator( ); iter.hasNext( ); )
		{
			DesignElementHandle element = (DesignElementHandle) iter.next( );

			Iterator visibilities = visibilityRulesIterator( element );
			if ( visibilities != null && visibilities.hasNext( ) )
			{
				if ( getHideRuleHandle( element,
						DesignChoiceConstants.FORMAT_TYPE_ALL ) == null )
				{
					boolean flag = false;
					try
					{
						flag = clearProperty( element );
					}
					catch ( Exception e )
					{
						throw e;
					}
					if ( !flag )
						stack.rollback( );
				}
			}

			updateHideRule( element,
					DesignChoiceConstants.FORMAT_TYPE_ALL,
					true,
					value );

		}
		stack.commit( );
		
	}

	public void saveSpecialOutput( boolean[] selections, String[] expressions )
			throws Exception
	{
		CommandStack stack = getActionStack( );
		stack.startTrans( Messages.getString( "VisibilityPage.menu.SaveHides" ) ); //$NON-NLS-1$

		for ( Iterator iter = DEUtil.getInputElements( input ).iterator( ); iter.hasNext( ); )
		{
			DesignElementHandle element = (DesignElementHandle) iter.next( );

			boolean hideForAll = false;
			for ( int i = 0; i < getTypeInfo( ).length; i++ )
			{
				if ( selections[i] )
				{
					hideForAll = true;
				}
				if ( !updateHideRule( element,
						getTypeInfo( )[i],
						selections[i],
						expressions[i] ) )
				{
					stack.rollback( );
				}
			}

			if ( hideForAll )
			{
				if ( !updateHideRule( element,
						DesignChoiceConstants.FORMAT_TYPE_ALL,
						false,
						null ) )
				{
					stack.rollback( );
				}
			}
		}
		stack.commit( );
		
	}

	public String getDisplayName( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Object load( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void save( Object value ) throws SemanticException
	{
		// TODO Auto-generated method stub

	}

	private Object input;

	public void setInput( Object input )
	{
		this.input = input;
	}

	public ExpressionProvider getExpressionProvider( )
	{
		if ( shareSameVisibility( ) )
			return new ExpressionProvider( getFirstElementHandle( ) );
		else
			return new ExpressionProvider( );
	}

	public boolean isEnableHide( )
	{
		Iterator visibilities = getVisibilityRulesIterator( );
		if ( ( visibilities != null ) && visibilities.hasNext( ) )
			return true;
		else
			return false;
	}

	public String getFormat( Object obj )
	{
		HideRuleHandle ruleHandle = (HideRuleHandle) obj;
		return ruleHandle.getFormat( );
	}

	public String getExpression( Object obj )
	{
		HideRuleHandle ruleHandle = (HideRuleHandle) obj;
		return ruleHandle.getExpression( );
	}

	public boolean isFormatTypeAll( String format )
	{
		if ( DesignChoiceConstants.FORMAT_TYPE_ALL.equalsIgnoreCase( format ) )
			return true;
		else
			return false;
	}

	public Iterator getVisibilityRulesIterator( )
	{
		return visibilityRulesIterator( getFirstElementHandle( ) );
	}
	
}
