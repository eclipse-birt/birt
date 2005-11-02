package org.eclipse.birt.core.ui.frameworks.taskwizard;

import java.util.Locale;
import java.util.Vector;

import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.ITask;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.IWizardContext;
import org.eclipse.swt.widgets.Composite;

class SequencedTask implements ITask
{
    private transient Vector subtasks = new Vector();
    protected transient IWizardContext context = null;
    protected transient WizardBase container = null;
    private transient int iCurrentSubtaskIndex = 0;

    /* (non-Javadoc)
     * @see org.eclipse.birt.frameworks.taskwizard.interfaces.ITask#getUI(org.eclipse.swt.widgets.Composite)
     */
    public Composite getUI(Composite parent)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.birt.frameworks.taskwizard.interfaces.ITask#getDisplayLabel(java.util.Locale)
     */
    public String getDisplayLabel(Locale locale)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void addSubtask(int iSubtaskIndex, ITask task)
    {
        if(subtasks.size() <= iSubtaskIndex)
        {
            subtasks.add(task);
        }
        else
        {
            subtasks.setElementAt(task, iSubtaskIndex);
        }
    }

    public void next()
    {
        // TODO: Switch to next subtask
        this.iCurrentSubtaskIndex++;
    }

    public void previous()
    {
        // TODO: Switch to previous subtask
        this.iCurrentSubtaskIndex--;
    }


    /* (non-Javadoc)
     * @see org.eclipse.birt.frameworks.taskwizard.interfaces.ITask#setContext(org.eclipse.birt.frameworks.taskwizard.interfaces.IWizardContext)
     */
    public void setContext(IWizardContext context)
    {
        this.context = context;
    }

    /* (non-Javadoc)
     * @see org.eclipse.birt.frameworks.taskwizard.interfaces.ITask#getContext()
     */
    public IWizardContext getContext()
    {
        return this.context;
    }

    /* (non-Javadoc)
     * @see org.eclipse.birt.frameworks.taskwizard.interfaces.ITask#setUIProvider(org.eclipse.birt.frameworks.taskwizard.WizardBase)
     */
    public void setUIProvider(WizardBase wizard)
    {
        this.container = wizard;
    }

    /* (non-Javadoc)
     * @see org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.ITask#getErrors()
     */
    public String[] getErrors()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.ITask#setErrorHints(java.lang.Object[])
     */
    public void setErrorHints(Object[] errorHints)
    {
        // TODO Auto-generated method stub
        
    }
}