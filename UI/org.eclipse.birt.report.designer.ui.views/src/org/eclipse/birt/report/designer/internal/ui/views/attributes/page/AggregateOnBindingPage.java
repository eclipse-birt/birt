
package org.eclipse.birt.report.designer.internal.ui.views.attributes.page;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.AggregateOnBindingsFormHandleProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.DataSetDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ComboAndButtonSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.FormSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.AggregateOnBindingsFormDescriptor;

public class AggregateOnBindingPage extends BindingPage
{

	protected void applyCustomSections( )
	{
		AggregateOnBindingsFormHandleProvider dataSetFormProvider = new AggregateOnBindingsFormHandleProvider( );
		( (FormSection) getSection( PageSectionId.BINDING_DATASET_FORM ) ).setCustomForm( new AggregateOnBindingsFormDescriptor( true ) );
		( (FormSection) getSection( PageSectionId.BINDING_DATASET_FORM ) ).setProvider( dataSetFormProvider );
		if ( ( (ComboAndButtonSection) getSection( PageSectionId.BINDING_DATASET ) ).getProvider( ) != null )
		{
			IDescriptorProvider dataSetProvider = ( (ComboAndButtonSection) getSection( PageSectionId.BINDING_DATASET ) ).getProvider( );
			if ( dataSetProvider instanceof DataSetDescriptorProvider )
				( (DataSetDescriptorProvider) dataSetProvider ).setDependedProvider( dataSetFormProvider );
		}
	}
}
