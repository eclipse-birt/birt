/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
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

package org.eclipse.birt.chart.ui.swt;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.data.DataElement;
import org.eclipse.birt.chart.model.data.DateTimeDataElement;
import org.eclipse.birt.chart.ui.integrate.ChartUIHelperBase;
import org.eclipse.birt.chart.ui.swt.composites.DateTimeDataElementComposite;
import org.eclipse.birt.chart.ui.swt.composites.FontDefinitionDialog;
import org.eclipse.birt.chart.ui.swt.composites.HeadStyleChooserComposite;
import org.eclipse.birt.chart.ui.swt.composites.ImageDialog;
import org.eclipse.birt.chart.ui.swt.composites.InsetsComposite;
import org.eclipse.birt.chart.ui.swt.composites.IntegerSpinControl;
import org.eclipse.birt.chart.ui.swt.composites.LineStyleChooserComposite;
import org.eclipse.birt.chart.ui.swt.composites.LineWidthChooserComposite;
import org.eclipse.birt.chart.ui.swt.composites.LocalizedNumberEditorComposite;
import org.eclipse.birt.chart.ui.swt.composites.NumberDataElementComposite;
import org.eclipse.birt.chart.ui.swt.composites.TextEditorComposite;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartUIFactory;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartUIHelper;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataElementComposite;
import org.eclipse.birt.chart.ui.swt.interfaces.IFontDefinitionDialog;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.util.TriggerSupportMatrix;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

/**
 * ChartUIFactory
 */

public class ChartUIFactory implements IChartUIFactory {

	@Override
	public IChartUIHelper createUIHelper() {
		return new ChartUIHelperBase();
	}

	@Override
	public TriggerSupportMatrix createSupportMatrix(String outputFormat, int iType) {
		return new TriggerSupportMatrix(outputFormat, iType);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.ui.swt.interfaces.IChartUIFactory#createChartCheckbox(
	 * org.eclipse.swt.widgets.Composite, int, boolean)
	 */
	@Override
	public ChartCheckbox createChartCheckbox(Composite parent, int styles, boolean defaultSelection) {
		return new ChartCheckbox(parent, styles, defaultSelection);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.ui.swt.interfaces.IChartUIFactory#createChartSpinner(
	 * org.eclipse.swt.widgets.Composite, int, org.eclipse.emf.ecore.EObject,
	 * java.lang.String, boolean)
	 */
	@Override
	public ChartSpinner createChartSpinner(Composite parent, int styles, EObject obj, String property,
			boolean enabled) {
		return new ChartSpinner(parent, styles, obj, property, enabled);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.ui.swt.interfaces.IChartUIFactory#createChartSpinner(
	 * org.eclipse.swt.widgets.Composite, int, org.eclipse.emf.ecore.EObject,
	 * java.lang.String, boolean, java.lang.String, java.lang.String)
	 */
	@Override
	public ChartSpinner createChartSpinner(Composite parent, int styles, EObject obj, String property, boolean enabled,
			String label, String endLabel) {
		return new ChartSpinner(parent, styles, obj, property, enabled, label, endLabel);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartUIFactory#
	 * createChartInsetsComposite(org.eclipse.swt.widgets.Composite, int, int,
	 * org.eclipse.birt.chart.model.attribute.Insets, java.lang.String,
	 * org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider,
	 * org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext,
	 * org.eclipse.birt.chart.model.attribute.Insets)
	 */
	@Override
	public AbstractChartInsets createChartInsetsComposite(Composite parent, int style, int numberRows, Insets insets,
			String sUnits, IUIServiceProvider serviceprovider, ChartWizardContext context, Insets defInsets) {
		InsetsComposite insetsComp = new InsetsComposite(parent, style, numberRows, insets, sUnits, serviceprovider,
				context);
		insetsComp.setDefaultInsets(defInsets);
		return insetsComp;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartUIFactory#
	 * createChartIntSpinner(org.eclipse.swt.widgets.Composite, int, int,
	 * org.eclipse.emf.ecore.EObject, java.lang.String, boolean)
	 */
	@Override
	public AbstractChartIntSpinner createChartIntSpinner(Composite parent, int style, int iCurrentValue, EObject obj,
			String property, boolean enabled) {
		AbstractChartIntSpinner cis = new IntegerSpinControl(parent, style, iCurrentValue);
		cis.setEnabled(enabled);
		return cis;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartUIFactory#
	 * createNumberDataElementComposite(org.eclipse.swt.widgets.Composite,
	 * org.eclipse.birt.chart.model.data.DataElement, org.eclipse.emf.ecore.EObject,
	 * java.lang.String)
	 */
	@Override
	public IDataElementComposite createNumberDataElementComposite(Composite parent, DataElement data, EObject eParent,
			String sProperty) {
		return new NumberDataElementComposite(parent, data);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartUIFactory#
	 * createDateTimeDataElementComposite(org.eclipse.swt.widgets.Composite, int,
	 * org.eclipse.birt.chart.model.data.DateTimeDataElement, boolean,
	 * org.eclipse.emf.ecore.EObject, java.lang.String)
	 */
	@Override
	public IDataElementComposite createDateTimeDataElementComposite(Composite parent, int style,
			DateTimeDataElement data, boolean isNullAllowed, EObject eParent, String sProperty) {
		return new DateTimeDataElementComposite(parent, style, data, isNullAllowed);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.ui.swt.interfaces.IChartUIFactory#createChartCombo(org
	 * .eclipse.swt.widgets.Composite, int, org.eclipse.emf.ecore.EObject,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public ChartCombo createChartCombo(Composite parent, int style, EObject eParent, String sProperty,
			String defaultItem) {
		return new ChartCombo(parent, style, eParent, sProperty, defaultItem);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartUIFactory#
	 * createLineStyleChooserComposite(org.eclipse.swt.widgets.Composite, int, int,
	 * java.lang.Integer[], org.eclipse.emf.ecore.EObject, java.lang.String)
	 */
	@Override
	public AbstractLineStyleChooserComposite createLineStyleChooserComposite(Composite parent, int style,
			int iLineStyle, Integer[] lineStyleItems, EObject eParent, String sProperty) {
		return new LineStyleChooserComposite(parent, style, iLineStyle, lineStyleItems);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartUIFactory#
	 * createLineWidthChooserComposite(org.eclipse.swt.widgets.Composite, int, int,
	 * java.lang.Integer[], org.eclipse.emf.ecore.EObject, java.lang.String)
	 */
	@Override
	public AbstractLineWidthChooserComposite createLineWidthChooserComposite(Composite parent, int style, int iWidth,
			Integer[] lineWidths, EObject eParent, String sProperty) {
		return new LineWidthChooserComposite(parent, style, iWidth, lineWidths);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.ui.swt.interfaces.IChartUIFactory#canEnableUI(org.
	 * eclipse.birt.chart.ui.swt.ChartCheckbox)
	 */
	@Override
	public boolean canEnableUI(ChartCheckbox button) {
		return button.getSelectionState() == ChartCheckbox.STATE_SELECTED;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartUIFactory#
	 * createHeadStyleChooserComposite(org.eclipse.swt.widgets.Composite, int, int,
	 * org.eclipse.emf.ecore.EObject, java.lang.String)
	 */
	@Override
	public AbstractHeadStyleChooserComposite createHeadStyleChooserComposite(Composite parent, int style,
			int iLineDecorator, EObject eParent, String sProperty) {
		return new HeadStyleChooserComposite(parent, style, iLineDecorator);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartUIFactory#
	 * createFontDefinitionDialog(org.eclipse.swt.widgets.Shell,
	 * org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext,
	 * org.eclipse.birt.chart.model.attribute.FontDefinition,
	 * org.eclipse.birt.chart.model.attribute.ColorDefinition, int)
	 */
	@Override
	public IFontDefinitionDialog createFontDefinitionDialog(Shell shellParent, ChartWizardContext wizardContext,
			FontDefinition fdCurrent, ColorDefinition cdCurrent, int optionalStyle) {
		return new FontDefinitionDialog(shellParent, wizardContext, fdCurrent, cdCurrent, optionalStyle);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartUIFactory#
	 * createChartTextEditor(org.eclipse.swt.widgets.Composite, int,
	 * org.eclipse.emf.ecore.EObject, java.lang.String)
	 */
	@Override
	public AbstractChartTextEditor createChartTextEditor(Composite parent, int style, EObject eParent,
			String sProperty) {
		return new TextEditorComposite(parent, style);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartUIFactory#
	 * createChartNumberEditor(org.eclipse.swt.widgets.Composite, int,
	 * java.lang.String, org.eclipse.emf.ecore.EObject, java.lang.String)
	 */
	@Override
	public AbstractChartNumberEditor createChartNumberEditor(Composite parent, int style, String sUnit, EObject eParent,
			String sProperty) {
		return new LocalizedNumberEditorComposite(parent, style, sUnit);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.ui.swt.interfaces.IChartUIFactory#createChartSlider(
	 * org.eclipse.swt.widgets.Composite, int, org.eclipse.emf.ecore.EObject,
	 * java.lang.String)
	 */
	@Override
	public ChartSlider createChartSlider(Composite parent, int style, EObject eParent, String sProperty) {
		return new ChartSlider(parent, style);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartUIFactory#supportAutoUI()
	 */
	@Override
	public boolean supportAutoUI() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.ui.swt.interfaces.IChartUIFactory#isSetInvisible(org.
	 * eclipse.emf.ecore.EObject)
	 */
	@Override
	public boolean isSetInvisible(EObject obj) {
		boolean isSetInvisible = false;
		try {
			Method m = obj.getClass().getMethod("isVisible"); //$NON-NLS-1$
			isSetInvisible = !((Boolean) m.invoke(obj));
		} catch (SecurityException | NoSuchMethodException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			// Do nothing.
		}
		return isSetInvisible;
	}

	@Override
	public TrayDialog createChartMarkerIconDialog(Shell parent, Fill fill, ChartWizardContext context) {
		return new ImageDialog(parent, fill, context, true, false, true);
	}

	@Override
	public TrayDialog createChartImageDialog(Shell parentShell, Fill fCurrent, ChartWizardContext context,
			boolean bEmbeddedImageEnabled, boolean bResourceImageEnabled) {
		return new ImageDialog(parentShell, fCurrent, context, bEmbeddedImageEnabled, bResourceImageEnabled);
	}
}
