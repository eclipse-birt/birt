/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import org.eclipse.birt.report.designer.internal.ui.swt.custom.BorderInfomation;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.util.ColorUtil;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.swt.graphics.RGB;

/**
 * Border toggle descriptor provider
 *
 * @since 3.3
 *
 */
public class BorderToggleDescriptorProvider extends BorderDescriptorProvider implements IToggleDescriptorProvider {

	private String property;

	/**
	 * Get the border position
	 *
	 * @return Return the border position
	 */
	public String getPosition() {
		if (property.equals(IStyleModel.BORDER_LEFT_STYLE_PROP)) {
			return BorderInfomation.BORDER_LEFT;
		}
		if (property.equals(IStyleModel.BORDER_RIGHT_STYLE_PROP)) {
			return BorderInfomation.BORDER_RIGHT;
		}
		if (property.equals(IStyleModel.BORDER_TOP_STYLE_PROP)) {
			return BorderInfomation.BORDER_TOP;
		}
		if (property.equals(IStyleModel.BORDER_BOTTOM_STYLE_PROP)) {
			return BorderInfomation.BORDER_BOTTOM;
		}
		if (property.equals(IStyleModel.BORDER_DIAGONAL_STYLE_PROP)) {
			return BorderInfomation.BORDER_DIAGONAL;
		}
		if (property.equals(IStyleModel.BORDER_ANTIDIAGONAL_STYLE_PROP)) {
			return BorderInfomation.BORDER_ANTIDIAGONAL;
		}
		return null;
	}

	/**
	 * Constructor
	 *
	 * @param property property name
	 */
	public BorderToggleDescriptorProvider(String property) {
		this.property = property;
	}

	@Override
	public String getImageName() {
		if (property.equals(IStyleModel.BORDER_LEFT_STYLE_PROP)) {
			return IReportGraphicConstants.ICON_ATTRIBUTE_BORDER_LEFT;
		}
		if (property.equals(IStyleModel.BORDER_RIGHT_STYLE_PROP)) {
			return IReportGraphicConstants.ICON_ATTRIBUTE_BORDER_RIGHT;
		}
		if (property.equals(IStyleModel.BORDER_TOP_STYLE_PROP)) {
			return IReportGraphicConstants.ICON_ATTRIBUTE_BORDER_TOP;
		}
		if (property.equals(IStyleModel.BORDER_BOTTOM_STYLE_PROP)) {
			return IReportGraphicConstants.ICON_ATTRIBUTE_BORDER_BOTTOM;
		}
		if (property.equals(IStyleModel.BORDER_DIAGONAL_STYLE_PROP)) {
			return IReportGraphicConstants.ICON_ATTRIBUTE_BORDER_DIAGONAL;
		}
		if (property.equals(IStyleModel.BORDER_ANTIDIAGONAL_STYLE_PROP)) {
			return IReportGraphicConstants.ICON_ATTRIBUTE_BORDER_ANTIDIAGONAL;
		}
		return ""; //$NON-NLS-1$
	}

	/**
	 * Get the toggle value
	 *
	 * @return Return the toggle value
	 */
	public String getToogleValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTooltipText() {
		if (property.equals(IStyleModel.BORDER_LEFT_STYLE_PROP)) {
			return Messages.getString("BordersPage.Tooltip.Left"); //$NON-NLS-1$
		}
		if (property.equals(IStyleModel.BORDER_RIGHT_STYLE_PROP)) {
			return Messages.getString("BordersPage.Tooltip.Right"); //$NON-NLS-1$
		}
		if (property.equals(IStyleModel.BORDER_TOP_STYLE_PROP)) {
			return Messages.getString("BordersPage.Tooltip.Top"); //$NON-NLS-1$
		}
		if (property.equals(IStyleModel.BORDER_BOTTOM_STYLE_PROP)) {
			return Messages.getString("BordersPage.Tooltip.Bottom"); //$NON-NLS-1$
		}
		if (property.equals(IStyleModel.BORDER_DIAGONAL_STYLE_PROP)) {
			return Messages.getString("BordersPage.Tooltip.Diagonal"); //$NON-NLS-1$
		}
		if (property.equals(IStyleModel.BORDER_ANTIDIAGONAL_STYLE_PROP)) {
			return Messages.getString("BordersPage.Tooltip.Antidiagonal"); //$NON-NLS-1$
		}
		return ""; //$NON-NLS-1$
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	private RGB convertToRGB(String color) {
		int[] rgbValues = ColorUtil.getRGBs(color);
		if (rgbValues == null) {
			return null;
		}
		return new RGB(rgbValues[0], rgbValues[1], rgbValues[2]);
	}

	@Override
	public Object load() {
		BorderInfomation info = new BorderInfomation();
		if (property.equals(IStyleModel.BORDER_LEFT_STYLE_PROP)) {
			info.setPosition(BorderInfomation.BORDER_LEFT);
			info.setStyle(getLocalStringValue(IStyleModel.BORDER_LEFT_STYLE_PROP));
			info.setWidth(getLocalStringValue(IStyleModel.BORDER_LEFT_WIDTH_PROP));
			info.setColor(convertToRGB(getLocalStringValue(IStyleModel.BORDER_LEFT_COLOR_PROP)));
			info.setDefaultStyle(getDefaultStringValue(IStyleModel.BORDER_LEFT_STYLE_PROP));
			info.setDefaultWidth(getDefaultStringValue(IStyleModel.BORDER_LEFT_WIDTH_PROP));
			info.setDefaultColor(convertToRGB(getDefaultStringValue(IStyleModel.BORDER_LEFT_COLOR_PROP)));
			info.setInheritedStyle(getDisplayValue(IStyleModel.BORDER_LEFT_STYLE_PROP));
			info.setInheritedWidth(getDisplayValue(IStyleModel.BORDER_LEFT_WIDTH_PROP));
			info.setInheritedColor(convertToRGB(getDisplayValue(IStyleModel.BORDER_LEFT_COLOR_PROP)));

		} else if (property.equals(IStyleModel.BORDER_RIGHT_STYLE_PROP)) {
			info.setPosition(BorderInfomation.BORDER_RIGHT);
			info.setStyle(getLocalStringValue(IStyleModel.BORDER_RIGHT_STYLE_PROP));
			info.setWidth(getLocalStringValue(IStyleModel.BORDER_RIGHT_WIDTH_PROP));
			info.setColor(convertToRGB(getLocalStringValue(IStyleModel.BORDER_RIGHT_COLOR_PROP)));
			info.setDefaultStyle(getDefaultStringValue(IStyleModel.BORDER_RIGHT_STYLE_PROP));
			info.setDefaultWidth(getDefaultStringValue(IStyleModel.BORDER_RIGHT_WIDTH_PROP));
			info.setDefaultColor(convertToRGB(getDefaultStringValue(IStyleModel.BORDER_RIGHT_COLOR_PROP)));
			info.setInheritedStyle(getDisplayValue(IStyleModel.BORDER_RIGHT_STYLE_PROP));
			info.setInheritedWidth(getDisplayValue(IStyleModel.BORDER_RIGHT_WIDTH_PROP));
			info.setInheritedColor(convertToRGB(getDisplayValue(IStyleModel.BORDER_RIGHT_COLOR_PROP)));

		} else if (property.equals(IStyleModel.BORDER_TOP_STYLE_PROP)) {
			info.setPosition(BorderInfomation.BORDER_TOP);
			info.setStyle(getLocalStringValue(IStyleModel.BORDER_TOP_STYLE_PROP));
			info.setWidth(getLocalStringValue(IStyleModel.BORDER_TOP_WIDTH_PROP));
			info.setColor(convertToRGB(getLocalStringValue(IStyleModel.BORDER_TOP_COLOR_PROP)));
			info.setDefaultStyle(getDefaultStringValue(IStyleModel.BORDER_TOP_STYLE_PROP));
			info.setDefaultWidth(getDefaultStringValue(IStyleModel.BORDER_TOP_WIDTH_PROP));
			info.setDefaultColor(convertToRGB(getDefaultStringValue(IStyleModel.BORDER_TOP_COLOR_PROP)));
			info.setInheritedStyle(getDisplayValue(IStyleModel.BORDER_TOP_STYLE_PROP));
			info.setInheritedWidth(getDisplayValue(IStyleModel.BORDER_TOP_WIDTH_PROP));
			info.setInheritedColor(convertToRGB(getDisplayValue(IStyleModel.BORDER_TOP_COLOR_PROP)));

		} else if (property.equals(IStyleModel.BORDER_BOTTOM_STYLE_PROP)) {
			info.setPosition(BorderInfomation.BORDER_BOTTOM);
			info.setStyle(getLocalStringValue(IStyleModel.BORDER_BOTTOM_STYLE_PROP));
			info.setWidth(getLocalStringValue(IStyleModel.BORDER_BOTTOM_WIDTH_PROP));
			info.setColor(convertToRGB(getLocalStringValue(IStyleModel.BORDER_BOTTOM_COLOR_PROP)));
			info.setDefaultStyle(getDefaultStringValue(IStyleModel.BORDER_BOTTOM_STYLE_PROP));
			info.setDefaultWidth(getDefaultStringValue(IStyleModel.BORDER_BOTTOM_WIDTH_PROP));
			info.setDefaultColor(convertToRGB(getDefaultStringValue(IStyleModel.BORDER_BOTTOM_COLOR_PROP)));
			info.setInheritedStyle(getDisplayValue(IStyleModel.BORDER_BOTTOM_STYLE_PROP));
			info.setInheritedWidth(getDisplayValue(IStyleModel.BORDER_BOTTOM_WIDTH_PROP));
			info.setInheritedColor(convertToRGB(getDisplayValue(IStyleModel.BORDER_BOTTOM_COLOR_PROP)));

		} else if (property.equals(IStyleModel.BORDER_DIAGONAL_STYLE_PROP)) {
			info.setPosition(BorderInfomation.BORDER_DIAGONAL);
			info.setStyle(getLocalStringValue(IStyleModel.BORDER_DIAGONAL_STYLE_PROP));
			info.setWidth(getLocalStringValue(IStyleModel.BORDER_DIAGONAL_WIDTH_PROP));
			info.setColor(convertToRGB(getLocalStringValue(IStyleModel.BORDER_DIAGONAL_COLOR_PROP)));
			info.setDefaultStyle(getDefaultStringValue(IStyleModel.BORDER_DIAGONAL_STYLE_PROP));
			info.setDefaultWidth(getDefaultStringValue(IStyleModel.BORDER_DIAGONAL_WIDTH_PROP));
			info.setDefaultColor(convertToRGB(getDefaultStringValue(IStyleModel.BORDER_DIAGONAL_COLOR_PROP)));
			info.setInheritedStyle(getDisplayValue(IStyleModel.BORDER_DIAGONAL_STYLE_PROP));
			info.setInheritedWidth(getDisplayValue(IStyleModel.BORDER_DIAGONAL_WIDTH_PROP));
			info.setInheritedColor(convertToRGB(getDisplayValue(IStyleModel.BORDER_DIAGONAL_COLOR_PROP)));

		} else if (property.equals(IStyleModel.BORDER_ANTIDIAGONAL_STYLE_PROP)) {
			info.setPosition(BorderInfomation.BORDER_ANTIDIAGONAL);
			info.setStyle(getLocalStringValue(IStyleModel.BORDER_ANTIDIAGONAL_STYLE_PROP));
			info.setWidth(getLocalStringValue(IStyleModel.BORDER_ANTIDIAGONAL_WIDTH_PROP));
			info.setColor(convertToRGB(getLocalStringValue(IStyleModel.BORDER_ANTIDIAGONAL_COLOR_PROP)));
			info.setDefaultStyle(getDefaultStringValue(IStyleModel.BORDER_ANTIDIAGONAL_STYLE_PROP));
			info.setDefaultWidth(getDefaultStringValue(IStyleModel.BORDER_ANTIDIAGONAL_WIDTH_PROP));
			info.setDefaultColor(convertToRGB(getDefaultStringValue(IStyleModel.BORDER_ANTIDIAGONAL_COLOR_PROP)));
			info.setInheritedStyle(getDisplayValue(IStyleModel.BORDER_ANTIDIAGONAL_STYLE_PROP));
			info.setInheritedWidth(getDisplayValue(IStyleModel.BORDER_ANTIDIAGONAL_WIDTH_PROP));
			info.setInheritedColor(convertToRGB(getDisplayValue(IStyleModel.BORDER_ANTIDIAGONAL_COLOR_PROP)));
		}
		return info;
	}

	@Override
	public void save(Object value) throws SemanticException {
		BorderInfomation info = (BorderInfomation) value;

		RGB rgb = info.getOriginColor();
		int colorValue = -1;
		Object color;
		if (rgb != null) {
			colorValue = ColorUtil.formRGB(rgb.red, rgb.green, rgb.blue);
			color = ColorUtil.format(colorValue, ColorUtil.INT_FORMAT);
		} else {
			color = null;
		}

		if (info.getPosition().equals(BorderInfomation.BORDER_TOP)) {
			save(IStyleModel.BORDER_TOP_STYLE_PROP, info.getOriginStyle());
			save(IStyleModel.BORDER_TOP_COLOR_PROP, color);
			save(IStyleModel.BORDER_TOP_WIDTH_PROP, info.getOriginWidth());
		} else if (info.getPosition().equals(BorderInfomation.BORDER_BOTTOM)) {
			save(IStyleModel.BORDER_BOTTOM_STYLE_PROP, info.getOriginStyle());
			save(IStyleModel.BORDER_BOTTOM_COLOR_PROP, color);
			save(IStyleModel.BORDER_BOTTOM_WIDTH_PROP, info.getOriginWidth());
		} else if (info.getPosition().equals(BorderInfomation.BORDER_LEFT)) {
			save(IStyleModel.BORDER_LEFT_STYLE_PROP, info.getOriginStyle());
			save(IStyleModel.BORDER_LEFT_COLOR_PROP, color);
			save(IStyleModel.BORDER_LEFT_WIDTH_PROP, info.getOriginWidth());
		} else if (info.getPosition().equals(BorderInfomation.BORDER_RIGHT)) {
			save(IStyleModel.BORDER_RIGHT_STYLE_PROP, info.getOriginStyle());
			save(IStyleModel.BORDER_RIGHT_COLOR_PROP, color);
			save(IStyleModel.BORDER_RIGHT_WIDTH_PROP, info.getOriginWidth());
		} else if (info.getPosition().equals(BorderInfomation.BORDER_DIAGONAL)) {
			save(IStyleModel.BORDER_DIAGONAL_STYLE_PROP, info.getOriginStyle());
			save(IStyleModel.BORDER_DIAGONAL_COLOR_PROP, color);
			save(IStyleModel.BORDER_DIAGONAL_WIDTH_PROP, info.getOriginWidth());
		} else if (info.getPosition().equals(BorderInfomation.BORDER_ANTIDIAGONAL)) {
			save(IStyleModel.BORDER_ANTIDIAGONAL_STYLE_PROP, info.getOriginStyle());
			save(IStyleModel.BORDER_ANTIDIAGONAL_COLOR_PROP, color);
			save(IStyleModel.BORDER_ANTIDIAGONAL_WIDTH_PROP, info.getOriginWidth());
		}

	}

	@Override
	void handleModifyEvent() {
		// TODO Auto-generated method stub

	}

	@Override
	public void reset() throws SemanticException {
		if (getPosition().equals(BorderInfomation.BORDER_TOP)) {
			save(IStyleModel.BORDER_TOP_STYLE_PROP, null);
			save(IStyleModel.BORDER_TOP_COLOR_PROP, null);
			save(IStyleModel.BORDER_TOP_WIDTH_PROP, null);
		} else if (getPosition().equals(BorderInfomation.BORDER_BOTTOM)) {
			save(IStyleModel.BORDER_BOTTOM_STYLE_PROP, null);
			save(IStyleModel.BORDER_BOTTOM_COLOR_PROP, null);
			save(IStyleModel.BORDER_BOTTOM_WIDTH_PROP, null);
		} else if (getPosition().equals(BorderInfomation.BORDER_LEFT)) {
			save(IStyleModel.BORDER_LEFT_STYLE_PROP, null);
			save(IStyleModel.BORDER_LEFT_COLOR_PROP, null);
			save(IStyleModel.BORDER_LEFT_WIDTH_PROP, null);
		} else if (getPosition().equals(BorderInfomation.BORDER_RIGHT)) {
			save(IStyleModel.BORDER_RIGHT_STYLE_PROP, null);
			save(IStyleModel.BORDER_RIGHT_COLOR_PROP, null);
			save(IStyleModel.BORDER_RIGHT_WIDTH_PROP, null);
		} else if (getPosition().equals(BorderInfomation.BORDER_DIAGONAL)) {
			save(IStyleModel.BORDER_DIAGONAL_STYLE_PROP, null);
			save(IStyleModel.BORDER_DIAGONAL_COLOR_PROP, null);
			save(IStyleModel.BORDER_DIAGONAL_WIDTH_PROP, null);
		} else if (getPosition().equals(BorderInfomation.BORDER_ANTIDIAGONAL)) {
			save(IStyleModel.BORDER_ANTIDIAGONAL_STYLE_PROP, null);
			save(IStyleModel.BORDER_ANTIDIAGONAL_COLOR_PROP, null);
			save(IStyleModel.BORDER_ANTIDIAGONAL_WIDTH_PROP, null);
		}

	}

	/**
	 * Get property
	 *
	 * @return Return the property
	 */
	public String getProperty() {
		return property;
	}
}
