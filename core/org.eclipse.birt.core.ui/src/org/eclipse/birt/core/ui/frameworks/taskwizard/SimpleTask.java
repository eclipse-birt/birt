
package org.eclipse.birt.core.ui.frameworks.taskwizard;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.ITask;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.IWizardContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class SimpleTask implements ITask
{

	private transient Composite cmpTask = null;
	protected transient IWizardContext context = null;
	protected transient WizardBase container = null;
	private transient String sLabel = ""; //$NON-NLS-1$
	private static int iCount = 1;
	private transient List errorList = new ArrayList( );

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.frameworks.taskwizard.interfaces.ITask#getUI(org.eclipse.swt.widgets.Composite)
	 */
	public Composite getUI( Composite parent )
	{
		if ( cmpTask == null || cmpTask.isDisposed( ) )
		{
			cmpTask = new Composite( parent, SWT.NONE );
			cmpTask.setLayout( new FillLayout( ) );
			placeComponents( );
		}
		return cmpTask;
	}

	private void placeComponents( )
	{
		Label lbl = new Label( cmpTask, SWT.SHADOW_IN | SWT.CENTER );
		lbl.setText( "This is a placeholder for the task : " //$NON-NLS-1$
				+ getDisplayLabel( Locale.getDefault( ) ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.frameworks.taskwizard.interfaces.ITask#getDisplayLabel(java.util.Locale)
	 */
	public String getDisplayLabel( Locale locale )
	{
		return sLabel;
	}

	public SimpleTask( )
	{
		sLabel = "Task - " + String.valueOf( iCount ); //$NON-NLS-1$
		iCount++;
	}

	public SimpleTask( String sLabel )
	{
		this.sLabel = sLabel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.frameworks.taskwizard.interfaces.ITask#setContext(org.eclipse.birt.frameworks.taskwizard.interfaces.IWizardContext)
	 */
	public void setContext( IWizardContext context )
	{
		this.context = context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.frameworks.taskwizard.interfaces.ITask#getContext()
	 */
	public IWizardContext getContext( )
	{
		return this.context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.frameworks.taskwizard.interfaces.ITask#setUIProvider(org.eclipse.birt.frameworks.taskwizard.WizardBase)
	 */
	public void setUIProvider( WizardBase wizard )
	{
		this.container = wizard;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.ITask#getErrors()
	 */
	public String[] getErrors( )
	{
		return (String[]) errorList.toArray( new String[errorList.size( )] );
	}

	protected void addError( String errorInfo )
	{
		if ( !errorList.contains( errorInfo ) )
			errorList.add( errorInfo );
	}

	protected void removeError( String errorInfo )
	{
		errorList.remove( errorInfo );
	}

	protected void displayError( String strError, String strFix, String taskId,
			String hints )
	{
		container.displayError( new String[]{
			strError
		}, new String[]{
			strFix
		}, new String[]{
			taskId
		}, context, new String[]{
			hints
		} );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.ITask#setErrorHints(java.lang.Object[])
	 */
	public void setErrorHints( Object[] errorHints )
	{
		// TODO Auto-generated method stub

	}
}