
package org.eclipse.birt.report.designer.internal.ui.views.property.widgets;

import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.widgets.Composite;

abstract public class CDialogCellEditor extends DialogCellEditor
{

	/**
	 * @param parent
	 */
	public CDialogCellEditor( Composite parent )
	{
		super( parent );
	}

	/**
	 * @param parent
	 * @param style
	 */
	public CDialogCellEditor( Composite parent, int style )
	{
		super( parent, style );
	}

	/**
	 * Returns whether the given value is valid for this cell editor. This cell
	 * editor's validator (if any) makes the actual determination.
	 * 
	 * @return <code>true</code> if the value is valid, and <code>false</code>
	 *         if invalid
	 */
	protected boolean isCorrect( Object value )
	{
		if(value==null || doGetValue() == null)
		{
			return true;
		}
		if ( doGetValue( ).equals( value ))
		{
			setErrorMessage("");//$NON-NLS-1$
			return false;
		}
		return super.isCorrect( value );
	}

}