package org.eclipse.birt.report.designer.internal.ui.ide.propertyeditor;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.HyperLinkPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.PageSectionId;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.HyperLinkDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.TextAndButtonSection;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;


public class IDEHyperLinkPage extends HyperLinkPage
{
	private TextAndButtonSection section;
	private HyperLinkDescriptorProvider hyperLinkProvider;

	public void applyCustomSections(){
		hyperLinkProvider = new IDEHyperLinkDescriptorProvider();
		section = (TextAndButtonSection)getSection( PageSectionId.HYPERLINK_HYPERLINK );
		section.setProvider( hyperLinkProvider );
		section.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				if ( hyperLinkProvider.hyperLinkSelected( ) )
					section.load( );
			}

		} );
	}
	
}
