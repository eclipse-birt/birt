package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.swt.graphics.Image;

public class MarginsPropertyDescriptorProvider extends PropertyDescriptorProvider {

	private IChoiceSet choiceSet;

	public MarginsPropertyDescriptorProvider(String property, String element) {
		super(property, element);
		choiceSet = ChoiceSetFactory.getDimensionChoiceSet(element, property);
	}

	public String getUnit(String unit) {
		IChoice choice = choiceSet.findChoiceByDisplayName(unit);
		if (choice != null)
			return choice.getName();
		else
			return null;
	}

	public String getUnitDisplayName(String unit) {
		IChoice choice = choiceSet.findChoice(unit);
		if (choice != null)
			return choice.getDisplayName();
		else
			return null;
	}

	public String[] getUnits() {
		return ChoiceSetFactory.getDisplayNamefromChoiceSet(choiceSet);
	}

	public String getDefaultUnit() {
		String value = load().toString();

		if (value == null || value.equals("")) //$NON-NLS-1$
			return value;
		try {
			DimensionValue dimensionValue = DimensionValue.parse(value);
			return dimensionValue.getUnits();
		} catch (PropertyValueException e) {
			ExceptionUtil.handle(e);
		}
		return ""; //$NON-NLS-1$
	}

	public String getMeasureValue() {
		String value = load().toString();
		if (value == null || value.equals("")) //$NON-NLS-1$
			return value;
		try {
			DimensionValue dimensionValue = DimensionValue.parse(value);
			return StringUtil.doubleToString(dimensionValue.getMeasure(), 3);
		} catch (PropertyValueException e) {
			ExceptionUtil.handle(e);
		}
		return ""; //$NON-NLS-1$
	}

	public Image getImage() {
		return ReportPlatformUIImages.getImage(getProperty());
	}

}
