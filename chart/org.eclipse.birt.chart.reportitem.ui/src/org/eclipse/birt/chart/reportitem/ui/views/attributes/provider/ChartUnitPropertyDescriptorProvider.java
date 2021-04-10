/**
 * 
 */

package org.eclipse.birt.chart.reportitem.ui.views.attributes.provider;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.UnitPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;

/**
 * ChartUnitPropertyDescriptorProvider
 */
public class ChartUnitPropertyDescriptorProvider extends UnitPropertyDescriptorProvider {

	public ChartUnitPropertyDescriptorProvider(String property, String element) {
		super(property, element);
	}

	public String[] getUnitItems() {
		IChoiceSet choiceSet;
		// use "Chart" instead of "ExtendedItem", following the suggestion of
		// model, bug 170740
		IElementPropertyDefn propertyDefn = DEUtil.getMetaDataDictionary().getElement("Chart") //$NON-NLS-1$
				.getProperty(getProperty());
		choiceSet = propertyDefn.getAllowedUnits();
		if (choiceSet != null) {
			return ChoiceSetFactory.getDisplayNamefromChoiceSet(choiceSet);
		} else {
			return super.getUnitItems();
		}

	}

}
