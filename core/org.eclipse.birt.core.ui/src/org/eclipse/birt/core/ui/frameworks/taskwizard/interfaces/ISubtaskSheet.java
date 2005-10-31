
package org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces;

import org.eclipse.swt.widgets.Composite;

public interface ISubtaskSheet extends Cloneable
{

	public void getComponent( Composite parent );

	/**
	 * Called just before the UI for this Subtask is shown. Intended for the UI
	 * to prepare itself by processing the context passed in. The context will
	 * depend on the wizard.
	 * 
	 * @param context
	 *            The context in which the Subtask UI will be shown. Its content
	 *            depend on individual wizards but it WILL be an instance 
     *            of IWizardContext. The Object type has been used to avoid adding 
     *            a dependency on the new UI plug-ins.
     * @param wizard
     *            The wizard container instance. It WILL be an instance of 
     *            WizardBase. The Object type has been used to avoid adding 
     *            a dependency on the new UI plug-ins.
	 */
	public void onShow( Object context, Object wizard );

	/**
	 * Called just before the UI for the subtask is disposed, it will be used by
	 * the wizard. The context returned should contain the complete updated
	 * context for the wizard. This context should be usable as is and should
	 * not require any additional processing before use.
	 * 
	 * @return complete context for the wizard with all updates that result from
	 *         operations performed as part of this task. This MUST be an instanceof 
     *         IWizardContext! The Object type has been used to avoid adding 
     *         a dependency on the new UI plug-ins.
	 */
	public Object onHide( );
	
	public void setIndex(int index);
}