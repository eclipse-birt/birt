
package org.eclipse.birt.report.designer.internal.ui.views.attributes.page;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IFormProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.FormSection;
import org.eclipse.birt.report.designer.internal.ui.views.dialogs.provider.GroupHandleProvider;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class FormPage extends AttributePage
{

	private int style;
	private IFormProvider provider;
	private boolean withDialog = false;
	private boolean isTabbed = false;

	public FormPage( int style, IFormProvider provider )
	{
		this.style = style;
		this.provider = provider;
	}

	public FormPage(int style, IFormProvider provider,
			boolean withDialog )
	{
		this.style = style;
		this.provider = provider;
		this.withDialog = withDialog;
	}

	public FormPage( int style, IFormProvider provider,
			boolean withDialog, boolean isTabbed )
	{
		this.style = style;
		this.provider = provider;
		this.withDialog = withDialog;
		this.isTabbed = isTabbed;
	}

	public void buildUI( Composite parent  )
	{
		super.buildUI( parent );
		container.setLayout( WidgetUtil.createGridLayout( 1 ) );
		FormSection formSection = new FormSection( provider.getDisplayName( ),
				container,
				true,
				isTabbed );
		formSection.setProvider( provider );
		formSection.setButtonWithDialog( withDialog );
		formSection.setStyle( style );
		formSection.setFillForm( true );
		addSection( PageSectionId.FORM_FORM, formSection );

		createSections( );
		layoutSections( );
	}

	public void dispose( )
	{
		if ( !( provider instanceof GroupHandleProvider ) )
			return;

		Object[] elements = provider.getElements( input );

		if ( elements == null )
		{
			return;
		}
		for ( int i = 0; i < elements.length; i++ )
		{
			if ( elements[i] instanceof DesignElementHandle )
			{
				DesignElementHandle element = (DesignElementHandle) elements[i];
				element.removeListener( FormPage.this );
			}
		}
	}

}
