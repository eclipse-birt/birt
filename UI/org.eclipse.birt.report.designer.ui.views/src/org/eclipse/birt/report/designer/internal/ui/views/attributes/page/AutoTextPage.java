package org.eclipse.birt.report.designer.internal.ui.views.attributes.page;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.ComboPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ComboSection;
import org.eclipse.birt.report.model.api.AutoTextHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;


public class AutoTextPage extends LabelPage
{
	public void applyCustomSections( ){
		getSection( PageSectionId.LABEL_STYLE ).setLayoutNum( 2 );
		getSection( PageSectionId.LABEL_STYLE ).setGridPlaceholder( 0, false );
		
		ComboPropertyDescriptorProvider styleProvider = new ComboPropertyDescriptorProvider( AutoTextHandle.AUTOTEXT_TYPE_PROP,
				ReportDesignConstants.AUTOTEXT_ITEM );
		ComboSection styleSection = new ComboSection( styleProvider.getDisplayName( ),
				container,
				true );
		styleSection.setProvider( styleProvider );
		styleSection.setLayoutNum( 4 );
		styleSection.setGridPlaceholder( 2, true );
		styleSection.setWidth( 200 );
		addSection( PageSectionId.AUTOTEXT_STYLE, styleSection );
	}
}
