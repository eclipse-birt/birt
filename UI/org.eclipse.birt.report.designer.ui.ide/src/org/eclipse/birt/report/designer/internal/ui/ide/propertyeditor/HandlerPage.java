package org.eclipse.birt.report.designer.internal.ui.ide.propertyeditor;

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.ide.util.ClassFinder;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.LibraryAttributePage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.PageSectionId;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.TextPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.TextAndButtonSection;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.GroupPropertyHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;

public class HandlerPage extends LibraryAttributePage {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * AttributePage#buildUI()
	 */
	public void buildUI(Composite parent) {
		super.buildUI(parent);
		container.setLayout(WidgetUtil.createGridLayout(5, 15));

		needCheckLibraryReadOnly(true);

		TextPropertyDescriptorProvider eventProvider = new TextPropertyDescriptorProvider(
				ReportDesignHandle.EVENT_HANDLER_CLASS_PROP, ReportDesignConstants.REPORT_DESIGN_ELEMENT) {

			public boolean isEditable() {
				if (HandlerPage.this.isLibraryReadOnly())
					return false;
				else
					return super.isEditable();
			}
		};
		TextAndButtonSection eventSection = new TextAndButtonSection(eventProvider.getDisplayName(), container, true);
		eventSection.setProvider(eventProvider);
		eventSection.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				ClassFinder finder = new ClassFinder();
				String className = null;
				if (input != null && ((List) input).size() > 0) {
					// if ( input.get( 0 ) instanceof ExtendedItemHandle
					// {
					// className = (String) ( EventHandlerWrapper.get(
					// AttributeConstant.EVENT_HANDLER_CLASS_PROPERTY_KEY ) );
					// }
					// else
					if (((List) input).get(0) instanceof DesignElementHandle) {
						className = EventHandlerWrapper
								.getEventHandlerClassName((DesignElementHandle) ((List) input).get(0));

					}
				}
				if (className != null) {
					finder.setParentClassName(className);
					GroupPropertyHandle handle = DEUtil.getMultiSelectionHandle((List) input)
							.getPropertyHandle(ReportDesignHandle.EVENT_HANDLER_CLASS_PROP);
					try {
						String finderClassName = finder.getFinderClassName();
						if (finderClassName != null && finderClassName.trim().length() > 0) {
							handle.setStringValue(finderClassName.trim());
						}
					} catch (SemanticException e1) {
						ExceptionUtil.handle(e1);
					}
				}
			}

		});
		eventSection.setWidth(400);
		eventSection.setGridPlaceholder(1, true);
		eventSection.setButtonText(Messages.getString("EventHandlerPage.Browse")); //$NON-NLS-1$
		addSection(PageSectionId.HANDLER_EVENT, eventSection);

		createSections();
		layoutSections();
	}

}