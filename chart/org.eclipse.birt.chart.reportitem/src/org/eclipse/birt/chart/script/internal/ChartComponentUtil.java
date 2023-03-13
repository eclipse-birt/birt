/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.script.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.Text;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.FontDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.TextImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.impl.LabelImpl;
import org.eclipse.birt.chart.model.data.DataElement;
import org.eclipse.birt.chart.model.data.DateTimeDataElement;
import org.eclipse.birt.chart.model.data.NumberDataElement;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.DateTimeDataElementImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.script.api.attribute.ILabel;
import org.eclipse.birt.chart.script.api.attribute.IText;
import org.eclipse.birt.chart.script.api.data.IDataElement;
import org.eclipse.birt.chart.script.api.data.IDateTimeDataElement;
import org.eclipse.birt.chart.script.api.data.INumberDataElement;
import org.eclipse.birt.chart.script.internal.attribute.ColorImpl;
import org.eclipse.birt.chart.script.internal.attribute.FontImpl;
import org.eclipse.birt.chart.script.internal.data.DateTimeElementImpl;
import org.eclipse.birt.chart.script.internal.data.NumberElementImpl;
import org.eclipse.birt.report.model.api.extension.IColor;
import org.eclipse.birt.report.model.api.extension.IFont;
import org.eclipse.emf.common.util.EList;

/**
 * Utility class for internal use.
 */

public class ChartComponentUtil {

	public static ColorDefinition createEMFColor() {
		ColorDefinition cd = ColorDefinitionImpl.BLACK();
		return cd;
	}

	public static FontDefinition createEMFFont() {
		FontDefinition fd = FontDefinitionImpl.createEmpty();
		return fd;
	}

	public static Text createEMFText() {
		Text desc = TextImpl.create(""); //$NON-NLS-1$
		desc.setColor(createEMFColor());
		desc.setFont(createEMFFont());
		return desc;
	}

	public static Label createEMFLabel() {
		Label label = LabelImpl.create();
		label.setCaption(createEMFText());
		label.setVisible(true);
		return label;
	}

	/**
	 * Converts DataElement from chart model to simple api
	 *
	 * @param data DataElement in chart model
	 * @return DataElement in simple api
	 */
	public static IDataElement convertDataElement(DataElement data) {
		if (data instanceof NumberDataElement) {
			return new NumberElementImpl((NumberDataElement) data);
		}
		if (data instanceof DateTimeDataElement) {
			return new DateTimeElementImpl((DateTimeDataElement) data);
		}
		return null;
	}

	/**
	 * Converts DataElement from simple api to chart model
	 *
	 * @param data DataElement in simple api
	 * @return DataElement in chart model
	 */
	public static DataElement convertIDataElement(IDataElement data) {
		if (data instanceof INumberDataElement) {
			return NumberDataElementImpl.create(((INumberDataElement) data).getValue());
		}
		if (data instanceof IDateTimeDataElement) {
			return DateTimeDataElementImpl.create(((IDateTimeDataElement) data).getValue());
		}
		return null;
	}

	/**
	 * Converts Text from chart model to simple api
	 *
	 * @param text Text in chart model. If it's null, create a new Text with blank
	 *             value.
	 * @return Text in simple api
	 */
	public static IText convertText(Text text) {
		if (text == null) {
			text = createEMFText();
		}
		return new org.eclipse.birt.chart.script.internal.attribute.TextImpl(text);
	}

	/**
	 * Converts Text from simple api to chart model
	 *
	 * @param itext Text in simple api
	 * @return Text in chart model
	 */
	public static Text convertIText(IText itext) {
		return org.eclipse.birt.chart.model.attribute.impl.TextImpl.create(itext.getValue());
	}

	/**
	 * Converts Label from chart model to simple api
	 *
	 * @param label Label in chart model. If it's null, create a blank Label.
	 * @return Label in simple api
	 */
	public static ILabel convertLabel(Label label) {
		if (label == null) {
			label = createEMFLabel();
		}
		return new org.eclipse.birt.chart.script.internal.attribute.LabelImpl(label);
	}

	/**
	 * Converts Label from simple api to chart model
	 *
	 * @param ilabel Label in simple api
	 * @return Label in chart model
	 */
	public static Label convertILabel(ILabel ilabel) {
		Label label = org.eclipse.birt.chart.model.component.impl.LabelImpl.create();
		label.setVisible(ilabel.isVisible());
		label.setCaption(convertIText(ilabel.getCaption()));
		return label;
	}

	/**
	 * Converts Font from chart model to simple api
	 *
	 * @param fd Font in chart model. If it's null, create a empty font instance.
	 * @return Font in simple api
	 */
	public static IFont convertFont(FontDefinition fd) {
		if (fd == null) {
			fd = createEMFFont();
		}
		return new FontImpl(fd);
	}

	/**
	 * Converts Font from simple api to chart model
	 *
	 * @param font Font in simple api
	 * @return Font in chart model
	 */
	public static FontDefinition convertIFont(IFont font) {
		return FontDefinitionImpl.create(font.getName(), font.getSize(), font.isBold(), font.isItalic(),
				font.isUnderline(), font.isStrikeThrough(), false, 0, null);
	}

	/**
	 * Converts Color from chart model to simple api
	 *
	 * @param cd Color in chart model. If it's blank, create a black color by
	 *           default.
	 * @return Color in simple api
	 */
	public static IColor convertColor(ColorDefinition cd) {
		if (cd == null) {
			cd = createEMFColor();
		}
		return new ColorImpl(cd);
	}

	/**
	 * Converts Color from simple api to chart model
	 *
	 * @param color Color in simple api
	 * @return Color in chart model
	 */
	public static ColorDefinition convertIColor(IColor color) {
		ColorDefinition cd = ColorDefinitionImpl.create(color.getRed(), color.getGreen(), color.getBlue());
		cd.setTransparency(color.getTransparency());
		return cd;
	}

	/**
	 * Return series definitions of specified axis index.
	 *
	 * @param chart     chart
	 * @param axisIndex If chart is without axis type, it always return all
	 *                  orthogonal series definition. -1 to return all
	 * @return specified axis definitions or all series definitions
	 */
	public static List getOrthogonalSeriesDefinitions(Chart chart, int axisIndex) {
		List seriesList = new ArrayList();
		if (chart instanceof ChartWithAxes) {
			EList axisList = ((Axis) ((ChartWithAxes) chart).getAxes().get(0)).getAssociatedAxes();
			for (int i = 0; i < axisList.size(); i++) {
				if (axisIndex < 0 || axisIndex == i) {
					seriesList.addAll(((Axis) axisList.get(i)).getSeriesDefinitions());
				}
			}
		} else if (chart instanceof ChartWithoutAxes) {
			seriesList.addAll(((SeriesDefinition) ((ChartWithoutAxes) chart).getSeriesDefinitions().get(0))
					.getSeriesDefinitions());
		}
		return seriesList;
	}
}
