
package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.CSSUtil;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;

public class MarignPropertyDescriptorProvider extends PropertyDescriptorProvider {

	public MarignPropertyDescriptorProvider(String property, String element) {
		super(property, element);
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

	public String[] getValueItems() {
		return ChoiceSetFactory
				.getDisplayNamefromChoiceSet(ChoiceSetFactory.getElementChoiceSet(getElement(), getProperty()));
	}

	public String[] getUnitItems() {
		return ChoiceSetFactory
				.getDisplayNamefromChoiceSet(ChoiceSetFactory.getDimensionChoiceSet(getElement(), getProperty()));
	}

	public String getUnitName(String key) {
		IChoice choice = ChoiceSetFactory.getDimensionChoiceSet(getElement(), getProperty()).findChoice(key);
		if (choice == null) {
			return null;
		}
		return choice.getDisplayName();
	}

	public String getValueName(String key) {
		IChoice choice = ChoiceSetFactory.getElementChoiceSet(getElement(), getProperty()).findChoice(key);
		if (choice == null) {
			return null;
		}
		return choice.getDisplayName();
	}

	public String getUnitDisplayName(String key) {
		IChoice choice = ChoiceSetFactory.getDimensionChoiceSet(getElement(), getProperty())
				.findChoiceByDisplayName(key);
		if (choice == null) {
			return null;
		}
		return choice.getName();
	}

	public String getValueDisplayName(String key) {
		IChoice choice = ChoiceSetFactory.getElementChoiceSet(getElement(), getProperty()).findChoiceByDisplayName(key);
		if (choice == null) {
			return null;
		}
		return choice.getName();
	}

	public String getUnit() throws PropertyValueException {
		String value = load().toString();

		if (value == null || value.equals("")) //$NON-NLS-1$
			return value;

		DimensionValue dimensionValue = DimensionValue.parse(value);
		return dimensionValue.getUnits();

	}

	public boolean validateDimensionValue(String value, String unit) {
		String unitName = ChoiceSetFactory.getDimensionChoiceSet(getElement(), getProperty())
				.findChoiceByDisplayName(unit).getName();
		return validateDimensionValue(value + unitName);
	}

	public boolean validateDimensionValue(String text) {
		String unit;
		IChoice unitChoice = ChoiceSetFactory.getElementChoiceSet(getElement(), getProperty())
				.findChoiceByDisplayName(text);

		if (unitChoice == null)
			return true;
		else
			unit = unitChoice.getName();

		boolean val = true;
		try {
			DimensionValue dimensionValue = DimensionValue.parse(text + unit);

			if (dimensionValue == null) {
				return true;
			}

			int size = DEUtil.getFontSizeIntValue((DesignElementHandle) DEUtil.getInputFirstElement(input));

			double pointValue = CSSUtil.convertToPoint(dimensionValue, size);
			return pointValue < 1000000;

		} catch (PropertyValueException e1) {
			// do nothing
		}
		return val;
	}

	public String getDefaultUnit() {
		if (DEUtil.getInputElements(input) == null || DEUtil.getInputSize(input) == 0) {
			return null;
		}
		String unit = null;
		if (!DEUtil.getGroupElementHandle(DEUtil.getInputElements(input)).isSameType()) {
			return null;
		}
		DesignElementHandle handle = (DesignElementHandle) DEUtil.getInputFirstElement(input);
		unit = handle.getPropertyHandle(getProperty()).getDefaultUnit();

		if (unit != null) {
			if (!StringUtil.isBlank(unit)) {
				unit = ChoiceSetFactory.getDimensionChoiceSet(getElement(), getProperty()).findChoice(unit)
						.getDisplayName();
			}
			return unit;
		}
		return null;
	}
}
