package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.HyperlinkBuilder;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.PlatformUI;

public class HyperLinkDescriptorProvider implements ITextDescriptorProvider
{

	private static final String LABEL_LINK_TO = Messages.getString( "HyperLinkPage.Label.LnikTo" ); //$NON-NLS-1$

	private static final String LABEL_NONE = Messages.getString( "HyperLinkPage.Label.None" ); //$NON-NLS-1$

	public String getDisplayName( )
	{
		// TODO Auto-generated method stub
		return LABEL_LINK_TO;
	}

	private Object oldValue;

	public Object load( )
	{
		if ( needRefresh )
		{
			Collection previewStrings = new HashSet( );
			Iterator iterator;

			oldValue = ""; //$NON-NLS-1$

			try
			{
				iterator = getActionHandles( ).iterator( );
			}
			catch ( SemanticException e )
			{
				ExceptionHandler.handle( e );
				return oldValue;
			}

			while ( iterator.hasNext( ) )
			{
				Object action = iterator.next( );

				if ( action instanceof ActionHandle )
				{
					String str = getPreviewString( (ActionHandle) action );

					if ( str == null )
					{
						str = LABEL_NONE;
					}
					previewStrings.add( str );
				}
			}

			if ( previewStrings.isEmpty( ) )
			{
				oldValue = LABEL_NONE;
			}
			else if ( previewStrings.size( ) == 1 )
			{
				String previewString = previewStrings.iterator( )
						.next( )
						.toString( );

				oldValue = previewString;
			}
		}
		return oldValue;
	}

	/**
	 * Returns preview string with the specified action handle.
	 * 
	 * @param action
	 *            the specified action handle
	 * @return the preview string
	 */
	private String getPreviewString( ActionHandle action )
	{
		String previewString = null;

		if ( DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK.equals( action.getLinkType( ) ) )
		{
			previewString = action.getURI( );
		}
		else if ( DesignChoiceConstants.ACTION_LINK_TYPE_BOOKMARK_LINK.equals( action.getLinkType( ) ) )
		{
			previewString = action.getTargetBookmark( );
		}
		else if ( DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH.equals( action.getLinkType( ) ) )
		{
			previewString = action.getReportName( );
			if ( action.getTargetBookmark( ) != null )
			{
				previewString += ":" + action.getTargetBookmark( ); //$NON-NLS-1$
			}
		}
		return previewString;
	}

	public void save( Object value ) throws SemanticException
	{
		// TODO Auto-generated method stub

	}

	protected Object input;

	public void setInput( Object input )
	{
		this.input = input;
	}

	public boolean hyperLinkSelected( )
	{
		boolean flag = true;
		HyperlinkBuilder dialog = new HyperlinkBuilder( PlatformUI.getWorkbench( )
				.getDisplay( )
				.getActiveShell( ) );
		getActionStack( ).startTrans( Messages.getString( "HyperLinkPage.Menu.Save" ) ); //$NON-NLS-1$

		try
		{
			dialog.setInput( getActionHandles( ) );
		}
		catch ( SemanticException e )
		{
			getActionStack( ).rollback( );
			ExceptionHandler.handle( e );
			return false;
		}

		needRefresh = false;
		boolean isOK = dialog.open( ) == Dialog.OK;
		needRefresh = true;
		if ( isOK )
		{
			getActionStack( ).commit( );
			flag = true;
		}
		else
		{
			getActionStack( ).rollback( );
			flag = false;
		}
		return flag;
	}

	private boolean needRefresh = true;

	private CommandStack getActionStack( )
	{
		return SessionHandleAdapter.getInstance( ).getCommandStack( );
	}

	/**
	 * Returns the handle of the action of the given element.
	 * 
	 * @param element
	 *            the element handle
	 * @return the handle of the action, or null if the element is not a proper
	 *         type
	 */
	private ActionHandle getActionHandle( ReportItemHandle element )
	{
		return DEUtil.getActionHandle( element );
	}

	/**
	 * Returns the handles of the action of the inputs.
	 * 
	 * @return the handles of the action
	 * @throws SemanticException
	 *             if member of an action is not valid.
	 */
	private Collection getActionHandles( ) throws SemanticException
	{
		HashSet handles = new HashSet( );
		Collection elements = DEUtil.getInputElements( input );
		Iterator iterator = elements == null ? null : elements.iterator( );

		while ( iterator != null && iterator.hasNext( ) )
		{
			Object object = iterator.next( );

			if ( object instanceof ReportItemHandle )
			{
				ReportItemHandle element = (ReportItemHandle) object;
				ActionHandle handle = getActionHandle( element );

				if ( handle == null )
				{
					handle = DEUtil.setAction( element,
							StructureFactory.createAction( ) );
				}
				handles.add( handle );
			}
		}
		return handles;
	}

	public boolean isReadOnly( )
	{
		return true;
	}


}
