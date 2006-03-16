
package org.eclipse.birt.core.ui.frameworks.taskwizard;

import java.util.LinkedHashMap;

import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.ISubtaskSheet;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.ITask;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.IWizardContext;
import org.eclipse.swt.widgets.Composite;

import com.ibm.icu.util.ULocale;

public class CompoundTask implements ITask
{

	private transient LinkedHashMap subtasks = new LinkedHashMap( );
	protected transient IWizardContext context = null;
	protected transient WizardBase container = null;
	protected transient ISubtaskSheet sCurrentTaskSheet = null;
	private transient String sCurrentSubtask = ""; //$NON-NLS-1$
	private transient String sLabel = ""; //$NON-NLS-1$	

	public CompoundTask( String sLabel )
	{
		this.sLabel = sLabel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.frameworks.taskwizard.interfaces.ITask#getUI(org.eclipse.swt.widgets.Composite)
	 */
	public Composite getUI( Composite parent )
	{
		return null;
	}

	public String getDisplayLabel( ULocale locale )
	{
		return sLabel;
	}

	public void addSubtask( String sSubtaskPath, ISubtaskSheet subtask )
	{
		subtasks.put( sSubtaskPath, subtask );
	}

	public void removeSubtask( String sSubtaskPath )
	{
		// If the current subtask is being removed...first switch to the first
		// available subtask and THEN remove the subtask
		if ( subtasks.containsKey( sSubtaskPath )
				&& sCurrentSubtask.equals( sSubtaskPath ) )
		{
			switchTo( subtasks.keySet( ).toArray( )[0].toString( ) );
		}
		subtasks.remove( sSubtaskPath );
	}

	public void switchTo( String sSubtaskPath )
	{
		if ( getCurrentSubtask( ) != null )
		{
			getCurrentSubtask( ).onHide( );
		}
		if ( containSubtask( sSubtaskPath ) )
		{
			sCurrentTaskSheet = getSubtask( sSubtaskPath );
			this.sCurrentSubtask = sSubtaskPath;
		}
		getCurrentSubtask( ).onShow( context, container );
	}

	protected boolean containSubtask( String sSubtaskPath )
	{
		return subtasks.containsKey( sSubtaskPath );
	}

	protected ISubtaskSheet getSubtask( String sSubtaskPath )
	{
		if ( !subtasks.containsKey( sSubtaskPath ) )
		{
			return null;
		}
		return (ISubtaskSheet) subtasks.get( sSubtaskPath );
	}

	protected ISubtaskSheet getCurrentSubtask( )
	{
		return sCurrentTaskSheet;
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
		// TODO Auto-generated method stub
		return null;
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

	public void dispose( )
	{
		// Dispose current subtask
		if ( sCurrentTaskSheet != null )
		{
			sCurrentTaskSheet.onHide( );
		}
		sCurrentTaskSheet = null;
		subtasks.clear( );
	}

	public String getDescription( ULocale locale )
	{
		return sLabel;
	}
}