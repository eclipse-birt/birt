
package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.dialogs.provider.GroupHandleProvider;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.AttributeView;
import org.eclipse.birt.report.designer.ui.views.attributes.AttributeViewPage;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IViewPart;

public abstract class AbstractFormHandleProvider implements IFormProvider
{

	protected Object input;

	public void setInput( Object input )
	{
		this.input = input;

	}

	public boolean isEnable( )
	{
		if ( DEUtil.getInputSize( input ) != 1 )
			return false;
		else
			return true;
	}

	public boolean edit( int pos )
	{
		CommandStack stack = getActionStack( );
		stack.startTrans( Messages.getString( "FormPage.Menu.ModifyProperty" ) ); //$NON-NLS-1$
		if ( !doEditItem( pos ) )
		{
			stack.rollback( );
			return false;
		}
		stack.commit( );
		refreshRestoreProperty();
		return true;
	}

	public void add( int pos ) throws Exception
	{
		boolean sucess = false;
		CommandStack stack = getActionStack( );
		stack.startTrans( Messages.getString( "FormPage.Menu.ModifyProperty" ) ); //$NON-NLS-1$
		try
		{
			sucess = doAddItem( pos );
		}
		catch ( Exception e )
		{
			stack.rollback( );
			throw new Exception( e );
		}
		if ( sucess )
		{
			stack.commit( );
		}
		else
		{
			stack.rollback( );
		}
		refreshRestoreProperty();
	}

	public void transModify( Object data, String property, Object value )
			throws Exception
	{

		CommandStack stack = getActionStack( );
		stack.startTrans( Messages.getString( "FormPage.Menu.ModifyProperty" ) ); //$NON-NLS-1$
		try
		{
			modify( data, property, value );
			stack.commit( );
		}
		catch ( Exception e )
		{
			stack.rollback( );
			throw new Exception( e );
		}
		refreshRestoreProperty();
	}

	private CommandStack getActionStack( )
	{
		return SessionHandleAdapter.getInstance( ).getCommandStack( );
	}

	public FormContentProvider getFormContentProvider( Listener listener,
			IDescriptorProvider provider )
	{
		return new FormContentProvider( listener, provider );
	}

	public class FormContentProvider implements IStructuredContentProvider
	{

		private Listener listener;
		private IDescriptorProvider provider;

		public FormContentProvider( Listener listener,
				IDescriptorProvider provider )
		{
			this.listener = listener;
			this.provider = provider;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
		 */
		public Object[] getElements( Object inputElement )
		{
			assert provider instanceof AbstractFormHandleProvider;
			Object[] elements = ( (AbstractFormHandleProvider) provider ).getElements( inputElement );
			for ( int i = 0; i < elements.length; i++ )
			{
				if ( elements[i] instanceof DesignElementHandle )
				{
					DesignElementHandle element = (DesignElementHandle) elements[i];
					element.removeListener( listener );
					element.addListener( listener );
				}
			}
			return elements;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
		 */
		public void dispose( )
		{
			if ( !( ( (IFormProvider) provider ) instanceof GroupHandleProvider ) )
				return;

			Object[] elements = ( (IFormProvider) provider ).getElements( input );

			if ( elements == null )
			{
				return;
			}
			for ( int i = 0; i < elements.length; i++ )
			{
				if ( elements[i] instanceof DesignElementHandle )
				{
					DesignElementHandle element = (DesignElementHandle) elements[i];
					element.removeListener( listener );
				}
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer,
		 *      java.lang.Object, java.lang.Object)
		 */
		public void inputChanged( Viewer viewer, Object oldInput,
				Object newInput )
		{
		}
	}

	public Object load( )
	{
		return null;
	}

	public void save( Object value ) throws SemanticException
	{

	}
	
	protected void refreshRestoreProperty( )
	{
		IViewPart view = UIUtil.getView( "org.eclipse.birt.report.designer.ui.attributes.AttributeView" );
		if ( view != null
				&& view instanceof AttributeView
				&& ( (AttributeView) view ).getCurrentPage( ) instanceof AttributeViewPage )
		{

			( (AttributeViewPage) ( (AttributeView) view ).getCurrentPage( ) ).resetRestorePropertiesAction( DEUtil.getInputElements( input ) );

		}
	}

}
