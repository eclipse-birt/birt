
package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import org.eclipse.birt.report.designer.internal.ui.swt.custom.BorderInfomation;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.util.ColorUtil;
import org.eclipse.swt.graphics.RGB;

public class BorderToggleDescriptorProvider extends BorderDescriptorProvider implements IToggleDescriptorProvider {

	private String property;

	public String getPosition() {
		if (property.equals(StyleHandle.BORDER_LEFT_STYLE_PROP))
			return BorderInfomation.BORDER_LEFT;
		if (property.equals(StyleHandle.BORDER_RIGHT_STYLE_PROP))
			return BorderInfomation.BORDER_RIGHT;
		if (property.equals(StyleHandle.BORDER_TOP_STYLE_PROP))
			return BorderInfomation.BORDER_TOP;
		if (property.equals(StyleHandle.BORDER_BOTTOM_STYLE_PROP))
			return BorderInfomation.BORDER_BOTTOM;
		return null;
	}

	public BorderToggleDescriptorProvider(String property) {
		this.property = property;
	}

	public String getImageName() {
		if (property.equals(StyleHandle.BORDER_LEFT_STYLE_PROP))
			return IReportGraphicConstants.ICON_ATTRIBUTE_BORDER_LEFT;
		if (property.equals(StyleHandle.BORDER_RIGHT_STYLE_PROP))
			return IReportGraphicConstants.ICON_ATTRIBUTE_BORDER_RIGHT;
		if (property.equals(StyleHandle.BORDER_TOP_STYLE_PROP))
			return IReportGraphicConstants.ICON_ATTRIBUTE_BORDER_TOP;
		if (property.equals(StyleHandle.BORDER_BOTTOM_STYLE_PROP))
			return IReportGraphicConstants.ICON_ATTRIBUTE_BORDER_BOTTOM;
		return ""; //$NON-NLS-1$
	}

	public String getToogleValue() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTooltipText() {
		if (property.equals(StyleHandle.BORDER_LEFT_STYLE_PROP))
			return Messages.getString("BordersPage.Tooltip.Left"); //$NON-NLS-1$
		if (property.equals(StyleHandle.BORDER_RIGHT_STYLE_PROP))
			return Messages.getString("BordersPage.Tooltip.Right"); //$NON-NLS-1$
		if (property.equals(StyleHandle.BORDER_TOP_STYLE_PROP))
			return Messages.getString("BordersPage.Tooltip.Top"); //$NON-NLS-1$
		if (property.equals(StyleHandle.BORDER_BOTTOM_STYLE_PROP))
			return Messages.getString("BordersPage.Tooltip.Bottom"); //$NON-NLS-1$
		return ""; //$NON-NLS-1$
	}

	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	private RGB convertToRGB(String color) {
		int[] rgbValues = ColorUtil.getRGBs(color);
		if (rgbValues == null) {
			return null;
		} else {
			return new RGB(rgbValues[0], rgbValues[1], rgbValues[2]);
		}
	}

	public Object load() {
		BorderInfomation info = new BorderInfomation();
		if (property.equals(StyleHandle.BORDER_LEFT_STYLE_PROP)) {
			info.setPosition(BorderInfomation.BORDER_LEFT);
			info.setStyle(getLocalStringValue(StyleHandle.BORDER_LEFT_STYLE_PROP));
			info.setWidth(getLocalStringValue(StyleHandle.BORDER_LEFT_WIDTH_PROP));
			info.setColor(convertToRGB(getLocalStringValue(StyleHandle.BORDER_LEFT_COLOR_PROP)));
			info.setDefaultStyle(getDefaultStringValue(StyleHandle.BORDER_LEFT_STYLE_PROP));
			info.setDefaultWidth(getDefaultStringValue(StyleHandle.BORDER_LEFT_WIDTH_PROP));
			info.setDefaultColor(convertToRGB(getDefaultStringValue(StyleHandle.BORDER_LEFT_COLOR_PROP)));
			info.setInheritedStyle(getDisplayValue(StyleHandle.BORDER_LEFT_STYLE_PROP));
			info.setInheritedWidth(getDisplayValue(StyleHandle.BORDER_LEFT_WIDTH_PROP));
			info.setInheritedColor(convertToRGB(getDisplayValue(StyleHandle.BORDER_LEFT_COLOR_PROP)));
		} else if (property.equals(StyleHandle.BORDER_RIGHT_STYLE_PROP)) {
			info.setPosition(BorderInfomation.BORDER_RIGHT);
			info.setStyle(getLocalStringValue(StyleHandle.BORDER_RIGHT_STYLE_PROP));
			info.setWidth(getLocalStringValue(StyleHandle.BORDER_RIGHT_WIDTH_PROP));
			info.setColor(convertToRGB(getLocalStringValue(StyleHandle.BORDER_RIGHT_COLOR_PROP)));
			info.setDefaultStyle(getDefaultStringValue(StyleHandle.BORDER_RIGHT_STYLE_PROP));
			info.setDefaultWidth(getDefaultStringValue(StyleHandle.BORDER_RIGHT_WIDTH_PROP));
			info.setDefaultColor(convertToRGB(getDefaultStringValue(StyleHandle.BORDER_RIGHT_COLOR_PROP)));
			info.setInheritedStyle(getDisplayValue(StyleHandle.BORDER_RIGHT_STYLE_PROP));
			info.setInheritedWidth(getDisplayValue(StyleHandle.BORDER_RIGHT_WIDTH_PROP));
			info.setInheritedColor(convertToRGB(getDisplayValue(StyleHandle.BORDER_RIGHT_COLOR_PROP)));

		} else if (property.equals(StyleHandle.BORDER_TOP_STYLE_PROP)) {
			info.setPosition(BorderInfomation.BORDER_TOP);
			info.setStyle(getLocalStringValue(StyleHandle.BORDER_TOP_STYLE_PROP));
			info.setWidth(getLocalStringValue(StyleHandle.BORDER_TOP_WIDTH_PROP));
			info.setColor(convertToRGB(getLocalStringValue(StyleHandle.BORDER_TOP_COLOR_PROP)));
			info.setDefaultStyle(getDefaultStringValue(StyleHandle.BORDER_TOP_STYLE_PROP));
			info.setDefaultWidth(getDefaultStringValue(StyleHandle.BORDER_TOP_WIDTH_PROP));
			info.setDefaultColor(convertToRGB(getDefaultStringValue(StyleHandle.BORDER_TOP_COLOR_PROP)));
			info.setInheritedStyle(getDisplayValue(StyleHandle.BORDER_TOP_STYLE_PROP));
			info.setInheritedWidth(getDisplayValue(StyleHandle.BORDER_TOP_WIDTH_PROP));
			info.setInheritedColor(convertToRGB(getDisplayValue(StyleHandle.BORDER_TOP_COLOR_PROP)));
		} else if (property.equals(StyleHandle.BORDER_BOTTOM_STYLE_PROP)) {
			info.setPosition(BorderInfomation.BORDER_BOTTOM);
			info.setStyle(getLocalStringValue(StyleHandle.BORDER_BOTTOM_STYLE_PROP));
			info.setWidth(getLocalStringValue(StyleHandle.BORDER_BOTTOM_WIDTH_PROP));
			info.setColor(convertToRGB(getLocalStringValue(StyleHandle.BORDER_BOTTOM_COLOR_PROP)));
			info.setDefaultStyle(getDefaultStringValue(StyleHandle.BORDER_BOTTOM_STYLE_PROP));
			info.setDefaultWidth(getDefaultStringValue(StyleHandle.BORDER_BOTTOM_WIDTH_PROP));
			info.setDefaultColor(convertToRGB(getDefaultStringValue(StyleHandle.BORDER_BOTTOM_COLOR_PROP)));
			info.setInheritedStyle(getDisplayValue(StyleHandle.BORDER_BOTTOM_STYLE_PROP));
			info.setInheritedWidth(getDisplayValue(StyleHandle.BORDER_BOTTOM_WIDTH_PROP));
			info.setInheritedColor(convertToRGB(getDisplayValue(StyleHandle.BORDER_BOTTOM_COLOR_PROP)));
		}
		return info;
	}

	public void save(Object value) throws SemanticException {
		BorderInfomation info = (BorderInfomation) value;

		RGB rgb = info.getOriginColor();
		int colorValue = -1;
		Object color;
		if (rgb != null) {
			colorValue = ColorUtil.formRGB(rgb.red, rgb.green, rgb.blue);
			color = ColorUtil.format(colorValue, ColorUtil.INT_FORMAT);
		} else
			color = null;

		if (info.getPosition().equals(BorderInfomation.BORDER_TOP)) {
			save(StyleHandle.BORDER_TOP_STYLE_PROP, info.getOriginStyle());
			save(StyleHandle.BORDER_TOP_COLOR_PROP, color);
			save(StyleHandle.BORDER_TOP_WIDTH_PROP, info.getOriginWidth());
		} else if (info.getPosition().equals(BorderInfomation.BORDER_BOTTOM)) {
			save(StyleHandle.BORDER_BOTTOM_STYLE_PROP, info.getOriginStyle());
			save(StyleHandle.BORDER_BOTTOM_COLOR_PROP, color);
			save(StyleHandle.BORDER_BOTTOM_WIDTH_PROP, info.getOriginWidth());
		} else if (info.getPosition().equals(BorderInfomation.BORDER_LEFT)) {
			save(StyleHandle.BORDER_LEFT_STYLE_PROP, info.getOriginStyle());
			save(StyleHandle.BORDER_LEFT_COLOR_PROP, color);
			save(StyleHandle.BORDER_LEFT_WIDTH_PROP, info.getOriginWidth());
		} else if (info.getPosition().equals(BorderInfomation.BORDER_RIGHT)) {
			save(StyleHandle.BORDER_RIGHT_STYLE_PROP, info.getOriginStyle());
			save(StyleHandle.BORDER_RIGHT_COLOR_PROP, color);
			save(StyleHandle.BORDER_RIGHT_WIDTH_PROP, info.getOriginWidth());
		}

	}

	void handleModifyEvent() {
		// TODO Auto-generated method stub

	}

	public void reset() throws SemanticException {
		if (getPosition().equals(BorderInfomation.BORDER_TOP)) {
			save(StyleHandle.BORDER_TOP_STYLE_PROP, null);
			save(StyleHandle.BORDER_TOP_COLOR_PROP, null);
			save(StyleHandle.BORDER_TOP_WIDTH_PROP, null);
		} else if (getPosition().equals(BorderInfomation.BORDER_BOTTOM)) {
			save(StyleHandle.BORDER_BOTTOM_STYLE_PROP, null);
			save(StyleHandle.BORDER_BOTTOM_COLOR_PROP, null);
			save(StyleHandle.BORDER_BOTTOM_WIDTH_PROP, null);
		} else if (getPosition().equals(BorderInfomation.BORDER_LEFT)) {
			save(StyleHandle.BORDER_LEFT_STYLE_PROP, null);
			save(StyleHandle.BORDER_LEFT_COLOR_PROP, null);
			save(StyleHandle.BORDER_LEFT_WIDTH_PROP, null);
		} else if (getPosition().equals(BorderInfomation.BORDER_RIGHT)) {
			save(StyleHandle.BORDER_RIGHT_STYLE_PROP, null);
			save(StyleHandle.BORDER_RIGHT_COLOR_PROP, null);
			save(StyleHandle.BORDER_RIGHT_WIDTH_PROP, null);
		}

	}

	public String getProperty() {
		return property;
	}
}
