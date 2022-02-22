/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
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

package org.eclipse.birt.chart.ui.swt.interfaces;

import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.data.DataElement;
import org.eclipse.birt.chart.model.data.DateTimeDataElement;
import org.eclipse.birt.chart.ui.swt.AbstractChartInsets;
import org.eclipse.birt.chart.ui.swt.AbstractChartIntSpinner;
import org.eclipse.birt.chart.ui.swt.AbstractChartNumberEditor;
import org.eclipse.birt.chart.ui.swt.AbstractChartTextEditor;
import org.eclipse.birt.chart.ui.swt.AbstractHeadStyleChooserComposite;
import org.eclipse.birt.chart.ui.swt.AbstractLineStyleChooserComposite;
import org.eclipse.birt.chart.ui.swt.AbstractLineWidthChooserComposite;
import org.eclipse.birt.chart.ui.swt.ChartCheckbox;
import org.eclipse.birt.chart.ui.swt.ChartCombo;
import org.eclipse.birt.chart.ui.swt.ChartSlider;
import org.eclipse.birt.chart.ui.swt.ChartSpinner;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.util.TriggerSupportMatrix;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

/**
 * UI factory used to create all kinds of UI classes.
 */

public interface IChartUIFactory {

	/**
	 * Returns the current UI helper
	 *
	 * @return UI helper
	 */
	IChartUIHelper createUIHelper();

	/**
	 * Creates instance of <code>TriggerSupportMatrix</code>.
	 *
	 * @param outputFormat output format
	 * @param iType        interactivity type
	 * @return instance
	 * @since 3.7
	 */
	TriggerSupportMatrix createSupportMatrix(String outputFormat, int iType);

	/**
	 * Creates instance of <code>AbstractChartCheckbox</code>.
	 *
	 * @param parent
	 * @param styles
	 * @param defaultSelection
	 * @return instance of <code>AbstractChartCheckbox</code>.
	 */
	ChartCheckbox createChartCheckbox(Composite parent, int styles, boolean defaultSelection);

	/**
	 * Creates instance of <code>ChartSpinner</code>.
	 *
	 * @param parent
	 * @param styles
	 * @param obj
	 * @param property
	 * @param enabled
	 * @return instance of <code>ChartSpinner</code>
	 */
	ChartSpinner createChartSpinner(Composite parent, int styles, EObject obj, String property, boolean enabled);

	/**
	 * Creates instance of <code>ChartSpinner</code>.
	 *
	 * @param parent
	 * @param styles
	 * @param obj
	 * @param property
	 * @param enabled
	 * @param label
	 * @param endLabel
	 * @return instance of <code>ChartSpinner</code>
	 */
	ChartSpinner createChartSpinner(Composite parent, int styles, EObject obj, String property, boolean enabled,
			String label, String endLabel);

	/**
	 * Create instance of insets composite.
	 *
	 * @param parent
	 * @param style
	 * @param numberRows
	 * @param insets
	 * @param sUnits
	 * @param serviceprovider
	 * @param context
	 * @param defInsets
	 * @return instance of chart insets.
	 */
	AbstractChartInsets createChartInsetsComposite(Composite parent, int style, int numberRows, Insets insets,
			String sUnits, IUIServiceProvider serviceprovider, ChartWizardContext context, Insets defInsets);

	/**
	 * Create instance of spinner composite.
	 *
	 * @param parent
	 * @param style
	 * @param iCurrentValue
	 * @param obj
	 * @param property
	 * @param enabled
	 * @return instance of spinner composite.
	 */
	AbstractChartIntSpinner createChartIntSpinner(Composite parent, int style, int iCurrentValue, EObject obj,
			String property, boolean enabled);

	/**
	 * Create instance of <code>IDataElementComposite</code> for editing number.
	 *
	 * @param parent
	 * @param data
	 * @param eParent
	 * @param sProperty
	 * @return instance of <code>IDataElementComposite</code>.
	 */
	IDataElementComposite createNumberDataElementComposite(Composite parent, DataElement data, EObject eParent,
			String sProperty);

	/**
	 * Create instance of <code>IDataElementComposite</code> for editing date time.
	 *
	 * @param parent
	 * @param style
	 * @param data
	 * @param isNullAllowed
	 * @param eParent
	 * @param sProperty
	 * @return instance of <code>IDataElementComposite</code>.
	 */
	IDataElementComposite createDateTimeDataElementComposite(Composite parent, int style, DateTimeDataElement data,
			boolean isNullAllowed, EObject eParent, String sProperty);

	/**
	 * Create instance of combo composite.
	 *
	 * @param parent
	 * @param style
	 * @param eParent
	 * @param sProperty
	 * @param defaultItem
	 * @return instance of combo composite.
	 */
	ChartCombo createChartCombo(Composite parent, int style, EObject eParent, String sProperty, String defaultItem);

	/**
	 * Create instance of combo list for editing list style.
	 *
	 * @param parent
	 * @param style
	 * @param iLineStyle
	 * @param lineStyleItems
	 * @param eParent
	 * @param sProperty
	 * @return instance of combo list for editing list style.
	 */
	AbstractLineStyleChooserComposite createLineStyleChooserComposite(Composite parent, int style, int iLineStyle,
			Integer[] lineStyleItems, EObject eParent, String sProperty);

	/**
	 * Create instance of combo list for editing line width.
	 *
	 * @param parent
	 * @param style
	 * @param iWidth
	 * @param lineWidths
	 * @param eParent
	 * @param sProperty
	 * @return instance of combo list for editing line width.
	 */
	AbstractLineWidthChooserComposite createLineWidthChooserComposite(Composite parent, int style, int iWidth,
			Integer[] lineWidths, EObject eParent, String sProperty);

	/**
	 * Create instance of combo list for editing meter head style.
	 *
	 * @param parent
	 * @param style
	 * @param iLineDecorator
	 * @param eParent
	 * @param sProperty
	 * @return instance of combo list for editing meter head style.
	 */
	AbstractHeadStyleChooserComposite createHeadStyleChooserComposite(Composite parent, int style, int iLineDecorator,
			EObject eParent, String sProperty);

	/**
	 * Create font definition dialog.
	 *
	 * @param shellParent
	 * @param wizardContext
	 * @param fdCurrent
	 * @param cdCurrent
	 * @param optionalStyle
	 * @return font definition dialog.
	 */
	IFontDefinitionDialog createFontDefinitionDialog(Shell shellParent, ChartWizardContext wizardContext,
			FontDefinition fdCurrent, ColorDefinition cdCurrent, int optionalStyle);

	/**
	 * Create text editor composite.
	 *
	 * @param parent
	 * @param style
	 * @param eParent
	 * @param sProperty
	 * @return text editor composite.
	 */
	AbstractChartTextEditor createChartTextEditor(Composite parent, int style, EObject eParent, String sProperty);

	/**
	 * Create number editor composite.
	 *
	 * @param parent
	 * @param style
	 * @param unit
	 * @param eParent
	 * @param sProperty
	 * @return number editor composite.
	 */
	AbstractChartNumberEditor createChartNumberEditor(Composite parent, int style, String unit, EObject eParent,
			String sProperty);

	/**
	 * Create slider composite.
	 *
	 * @param parent
	 * @param style
	 * @param eParent
	 * @param sProperty
	 * @return slider composite.
	 */
	ChartSlider createChartSlider(Composite parent, int style, EObject eParent, String sProperty);

	/**
	 * Create marker icon dialog.
	 *
	 * @param parent
	 * @param fill
	 * @param context
	 * @return marker icon dialog
	 */
	TrayDialog createChartMarkerIconDialog(Shell parent, Fill fill, ChartWizardContext context);

	/**
	 * Create image dialog
	 *
	 * @return image dialog
	 */
	TrayDialog createChartImageDialog(Shell parentShell, Fill fCurrent, ChartWizardContext context,
			boolean bEmbeddedImageEnabled, boolean bResourceImageEnabled);

	/**
	 * Check if the state of specified button allows to enable UI component.
	 *
	 * @param button
	 * @return true if the state of specified button allows to enable UI component.
	 */
	boolean canEnableUI(ChartCheckbox button);

	/**
	 * Check if current context is supporting 'auto' UI.
	 *
	 * @return true if current context is supporting 'auto' UI.
	 */
	boolean supportAutoUI();

	/**
	 * Check if specified EObject is set invisible, the EObject must have 'visible'
	 * property, the return result is used for updating chart UI.
	 *
	 * @param obj
	 * @return true if EObject is set invisible.
	 */
	boolean isSetInvisible(EObject obj);
}
