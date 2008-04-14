
package org.eclipse.birt.report.designer.internal.ui.views.attributes.page;

import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;


public class ColumnSectionPage extends SectionPage
{

	public void applyCustomSections( )
	{
		removeSection( PageSectionId.SECION_PAGE_BREAK_INSIDE );
	}

	protected void setVisible( )
	{
		if ( DEUtil.getInputSize( input ) == 1
				&& DEUtil.getInputFirstElement( input ) instanceof DesignElementHandle
				&& isElementInMasterPage( (DesignElementHandle) DEUtil.getInputFirstElement( input ) ) )
		{
			masterSection.setVisible( false );
			sepSection.setVisible( false );
			beforeSection.getLabelControl( ).setEnabled( false );
			beforeSection.getComboControl( ).getControl( ).setEnabled( false );
			afterSection.getLabelControl( ).setEnabled( false );
			afterSection.getComboControl( ).getControl( ).setEnabled( false );
		}
		else
		{
			masterSection.setVisible( true );
			sepSection.setVisible( true );
			beforeSection.getLabelControl( ).setEnabled( true );
			beforeSection.getComboControl( ).getControl( ).setEnabled( true );
			afterSection.getLabelControl( ).setEnabled( true );
			afterSection.getComboControl( ).getControl( ).setEnabled( true );
		}
	}
}
