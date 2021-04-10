package org.eclipse.birt.chart.reportitem.ui.views.attributes.provider;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.PropertyDescriptorProvider;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.GroupElementHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.metadata.IPredefinedStyle;

public class ChoicePropertyDescriptorProvider extends PropertyDescriptorProvider {

	public ChoicePropertyDescriptorProvider(String property, String element) {
		super(property, element);
	}

	String[] values = null;

	public String[] getItems() {
		if (ReportItemHandle.DATA_SET_PROP.equals(getProperty()))
			return ChoiceSetFactory.getDataSets();
		else if (StyleHandle.MASTER_PAGE_PROP.equals(getProperty()))
			return ChoiceSetFactory.getMasterPages();
		else if (ReportItemHandle.STYLE_PROP.equals(getProperty())) {
			String[] itemsArray = ChoiceSetFactory.getStyles();

			// Filter predefined styles to make its logic same with report design side.
			itemsArray = filterPreStyles(itemsArray);
			return itemsArray;
		} else if (ReportDesignHandle.THEME_PROP.equals(getProperty()))
			return ChoiceSetFactory.getThemes();
		else {
			IChoiceSet cset = getChoiceSet();
			values = ChoiceSetFactory.getNamefromChoiceSet(cset);
			return ChoiceSetFactory.getDisplayNamefromChoiceSet(cset);

		}
	}

	/**
	 * Filter predefined styles.
	 * 
	 * @param items all available styles
	 * @return filtered styles.
	 */
	private String[] filterPreStyles(String items[]) {
		String[] newItems = items;
		if (items == null) {
			newItems = new String[] {};
		}

		List<IPredefinedStyle> preStyles = new DesignEngine(new DesignConfig()).getMetaData().getPredefinedStyles();
		List<String> preStyleNames = new ArrayList<String>();

		for (int i = 0; i < preStyles.size(); i++) {
			preStyleNames.add(preStyles.get(i).getName());
		}

		List<String> sytleNames = new ArrayList<String>();
		for (int i = 0; i < newItems.length; i++) {
			if (preStyleNames.indexOf(newItems[i]) == -1) {
				sytleNames.add(newItems[i]);
			}
		}

		return sytleNames.toArray(new String[] {});
	}

	public String[] getValues() {
		return values;
	}

	private IChoiceSet getChoiceSet() {
		GroupElementHandle multiSelectionHandle = DEUtil.getMultiSelectionHandle(DEUtil.getInputElements(input));
		return multiSelectionHandle.getPropertyHandle(getProperty()).getPropertyDefn().getChoices();
	}

	public boolean assertProperty() {
		return ReportItemHandle.STYLE_PROP.equals(getProperty()) || ReportDesignHandle.THEME_PROP.equals(getProperty());
	}

}
