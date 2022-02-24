/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.chart.ui.swt.wizard.format.popup.series;

import java.text.ParseException;

import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.component.Dial;
import org.eclipse.birt.chart.model.component.DialRegion;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.DialRegionImpl;
import org.eclipse.birt.chart.model.data.DataElement;
import org.eclipse.birt.chart.model.data.NumberDataElement;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.model.type.DialSeries;
import org.eclipse.birt.chart.model.util.ChartDefaultValueUtil;
import org.eclipse.birt.chart.model.util.ChartElementUtil;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.AbstractChartNumberEditor;
import org.eclipse.birt.chart.ui.swt.AbstractChartTextEditor;
import org.eclipse.birt.chart.ui.swt.composites.FillChooserComposite;
import org.eclipse.birt.chart.ui.swt.composites.LineAttributesComposite;
import org.eclipse.birt.chart.ui.swt.composites.TextEditorComposite;
import org.eclipse.birt.chart.ui.swt.fieldassist.TextNumberEditorAssistField;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.AbstractPopupSheet;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIExtensionUtil;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;

import com.ibm.icu.text.NumberFormat;

/**
 * 
 */

public class SeriesRegionSheet extends AbstractPopupSheet implements SelectionListener, ModifyListener, Listener {

	private transient Composite cmpContent;

	private transient Composite cmpList = null;

	private transient Button btnAddRange = null;

	private transient Button btnRemove = null;

	private transient List lstMarkers = null;

	private transient Group grpGeneral = null;

	private transient Composite cmpRange = null;

	private transient Group grpMarkerRange = null;

	private transient Label lblStartValue = null;

	private transient AbstractChartTextEditor txtStartValue = null;

	private transient Label lblEndValue = null;

	private transient AbstractChartTextEditor txtEndValue = null;

	private transient Label lblInnerRadius = null;

	private transient AbstractChartNumberEditor txtInnerRadius = null;

	private transient Label lblOuterRadius = null;

	private transient AbstractChartNumberEditor txtOuterRadius = null;

	private transient Label lblRangeFill = null;

	private transient FillChooserComposite fccRange = null;

	private transient LineAttributesComposite liacMarkerRange = null;

	// private transient LabelAttributesComposite lacLabel = null;

	private transient int iRangeCount = 0;

	private transient SeriesDefinition seriesDefn;

	private Series series;

	private Series defSeries = null;

	/**
	 * @param title
	 * @param context
	 * @param seriesDefn
	 * 
	 * @deprecated since 3.7
	 */
	public SeriesRegionSheet(String title, ChartWizardContext context, SeriesDefinition seriesDefn) {
		super(title, context, false);
		this.seriesDefn = seriesDefn;
	}

	public SeriesRegionSheet(String title, ChartWizardContext context, Series series) {
		super(title, context, false);
		this.series = series;
		this.defSeries = ChartDefaultValueUtil.getDefaultSeries(series);
	}

	protected Composite getComponent(Composite parent) {
		ChartUIUtil.bindHelp(parent, ChartHelpContextIds.POPUP_SERIES_METER_REGION);

		// Layout for the main composite
		GridLayout glContent = new GridLayout();
		glContent.numColumns = 2;
		glContent.horizontalSpacing = 5;
		glContent.verticalSpacing = 5;
		glContent.marginHeight = 7;
		glContent.marginWidth = 7;

		cmpContent = new Composite(parent, SWT.NONE);
		cmpContent.setLayout(glContent);

		// Layout for the List composite
		GridLayout glList = new GridLayout();
		glList.numColumns = 3;
		glList.horizontalSpacing = 5;
		glList.verticalSpacing = 5;
		glList.marginHeight = 0;
		glList.marginWidth = 0;

		cmpList = new Composite(cmpContent, SWT.NONE);
		GridData gdCMPList = new GridData(GridData.FILL_BOTH);
		gdCMPList.horizontalSpan = 2;
		cmpList.setLayoutData(gdCMPList);
		cmpList.setLayout(glList);

		// Layout for the buttons composite
		GridLayout glButtons = new GridLayout();
		glButtons.numColumns = 3;
		glButtons.horizontalSpacing = 5;
		glButtons.verticalSpacing = 5;
		glButtons.marginHeight = 5;
		glButtons.marginWidth = 0;

		Composite cmpButtons = new Composite(cmpList, SWT.NONE);
		GridData gdCMPButtons = new GridData(GridData.FILL_HORIZONTAL);
		gdCMPButtons.horizontalSpan = 3;
		cmpButtons.setLayoutData(gdCMPButtons);
		cmpButtons.setLayout(glButtons);

		btnAddRange = new Button(cmpButtons, SWT.PUSH);
		GridData gdBTNAddRange = new GridData(GridData.FILL_HORIZONTAL);
		btnAddRange.setLayoutData(gdBTNAddRange);
		btnAddRange.setText(Messages.getString("BaseAxisMarkerAttributeSheetImpl.Lbl.AddRegion")); //$NON-NLS-1$
		btnAddRange.addSelectionListener(this);

		btnRemove = new Button(cmpButtons, SWT.PUSH);
		GridData gdBTNRemove = new GridData(GridData.FILL_HORIZONTAL);
		btnRemove.setLayoutData(gdBTNRemove);
		btnRemove.setText(Messages.getString("BaseAxisMarkerAttributeSheetImpl.Lbl.RemoveEntry")); //$NON-NLS-1$
		btnRemove.addSelectionListener(this);

		lstMarkers = new List(cmpList, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
		GridData gdLSTMarkers = new GridData(GridData.FILL_HORIZONTAL);
		gdLSTMarkers.horizontalSpan = 3;
		gdLSTMarkers.heightHint = 100;
		lstMarkers.setLayoutData(gdLSTMarkers);
		lstMarkers.addSelectionListener(this);

		grpGeneral = new Group(cmpContent, SWT.NONE);
		GridData gdCMPGeneral = new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL);
		gdCMPGeneral.widthHint = 200;
		grpGeneral.setLayoutData(gdCMPGeneral);
		grpGeneral.setLayout(new GridLayout());
		grpGeneral.setText(Messages.getString("BaseAxisMarkerAttributeSheetImpl.Lbl.DialProperties")); //$NON-NLS-1$

		// Layout for the Marker Range composite
		GridLayout glMarkerRange = new GridLayout();
		glMarkerRange.numColumns = 3;
		glMarkerRange.horizontalSpacing = 5;
		glMarkerRange.verticalSpacing = 5;
		glMarkerRange.marginHeight = 7;
		glMarkerRange.marginWidth = 7;

		cmpRange = new Composite(grpGeneral, SWT.NONE);
		GridData gdGRPRange = new GridData(GridData.FILL_HORIZONTAL);
		cmpRange.setLayoutData(gdGRPRange);
		cmpRange.setLayout(glMarkerRange);

		// Layout for Value composite
		GridLayout glRangeValue = new GridLayout();
		glRangeValue.numColumns = 3;
		glRangeValue.horizontalSpacing = 2;
		glRangeValue.verticalSpacing = 5;
		glRangeValue.marginHeight = 0;
		glRangeValue.marginWidth = 0;

		Composite cmpRangeValue = new Composite(cmpRange, SWT.NONE);
		GridData gdCMPRangeValue = new GridData(GridData.FILL_HORIZONTAL);
		gdCMPRangeValue.horizontalSpan = 3;
		cmpRangeValue.setLayoutData(gdCMPRangeValue);
		cmpRangeValue.setLayout(glRangeValue);

		lblStartValue = new Label(cmpRangeValue, SWT.NONE);
		GridData gdLBLStartValue = new GridData();
		gdLBLStartValue.horizontalIndent = 5;
		lblStartValue.setLayoutData(gdLBLStartValue);
		lblStartValue.setText(Messages.getString("BaseAxisMarkerAttributeSheetImpl.Lbl.StartValue")); //$NON-NLS-1$

		txtStartValue = getContext().getUIFactory().createChartTextEditor(cmpRangeValue, SWT.BORDER | SWT.SINGLE, null,
				"startValue");//$NON-NLS-1$
		new TextNumberEditorAssistField(txtStartValue.getTextControl(), null);
		GridData gdTXTStartValue = new GridData(GridData.FILL_HORIZONTAL);
		gdTXTStartValue.horizontalSpan = 2;
		txtStartValue.setLayoutData(gdTXTStartValue);
		txtStartValue.addListener(this);

		lblEndValue = new Label(cmpRangeValue, SWT.NONE);
		GridData gdLBLEndValue = new GridData();
		gdLBLEndValue.horizontalIndent = 5;
		lblEndValue.setLayoutData(gdLBLEndValue);
		lblEndValue.setText(Messages.getString("BaseAxisMarkerAttributeSheetImpl.Lbl.EndValue")); //$NON-NLS-1$

		txtEndValue = getContext().getUIFactory().createChartTextEditor(cmpRangeValue, SWT.BORDER | SWT.SINGLE, null,
				"endValue");//$NON-NLS-1$
		new TextNumberEditorAssistField(txtEndValue.getTextControl(), null);
		GridData gdTXTEndValue = new GridData(GridData.FILL_HORIZONTAL);
		gdTXTEndValue.horizontalSpan = 2;
		txtEndValue.setLayoutData(gdTXTEndValue);
		txtEndValue.addListener(this);

		// Radius
		if (supportRadius()) {
			lblInnerRadius = new Label(cmpRangeValue, SWT.NONE);
			GridData gdLBLInnerRadius = new GridData();
			gdLBLInnerRadius.horizontalIndent = 5;
			lblInnerRadius.setLayoutData(gdLBLInnerRadius);
			lblInnerRadius.setText(Messages.getString("BaseAxisMarkerAttributeSheetImpl.Lbl.InnerRadius")); //$NON-NLS-1$

			txtInnerRadius = getContext().getUIFactory().createChartNumberEditor(cmpRangeValue, SWT.BORDER | SWT.SINGLE,
					null, null, "innerRadius");//$NON-NLS-1$
			new TextNumberEditorAssistField(txtInnerRadius.getTextControl(), null);
			GridData gdTXTInnerRadius = new GridData(GridData.FILL_HORIZONTAL);
			gdTXTInnerRadius.horizontalSpan = 2;
			txtInnerRadius.setLayoutData(gdTXTInnerRadius);
			txtInnerRadius.addModifyListener(this);

			lblOuterRadius = new Label(cmpRangeValue, SWT.NONE);
			GridData gdLBLOuterRadius = new GridData();
			gdLBLOuterRadius.horizontalIndent = 5;
			lblOuterRadius.setLayoutData(gdLBLOuterRadius);
			lblOuterRadius.setText(Messages.getString("BaseAxisMarkerAttributeSheetImpl.Lbl.OuterRadius")); //$NON-NLS-1$

			txtOuterRadius = getContext().getUIFactory().createChartNumberEditor(cmpRangeValue, SWT.BORDER | SWT.SINGLE,
					null, null, "outerRadius");//$NON-NLS-1$
			new TextNumberEditorAssistField(txtOuterRadius.getTextControl(), null);
			GridData gdTXTOuterRadius = new GridData(GridData.FILL_HORIZONTAL);
			gdTXTOuterRadius.horizontalSpan = 2;
			txtOuterRadius.setLayoutData(gdTXTOuterRadius);
			txtOuterRadius.addModifyListener(this);
		}

		// Fill
		lblRangeFill = new Label(cmpRange, SWT.NONE);
		GridData gdLBLRangeFill = new GridData();
		gdLBLRangeFill.horizontalIndent = 5;
		lblRangeFill.setLayoutData(gdLBLRangeFill);
		lblRangeFill.setText(Messages.getString("BaseAxisMarkerAttributeSheetImpl.Lbl.Fill")); //$NON-NLS-1$

		int fillStyles = FillChooserComposite.ENABLE_GRADIENT | FillChooserComposite.ENABLE_TRANSPARENT
				| FillChooserComposite.ENABLE_TRANSPARENT_SLIDER;
		fillStyles |= getContext().getUIFactory().supportAutoUI() ? FillChooserComposite.ENABLE_AUTO : fillStyles;
		if (supportPatternAndImageFill()) {
			fillStyles |= FillChooserComposite.ENABLE_IMAGE;
		} else {
			fillStyles |= FillChooserComposite.DISABLE_PATTERN_FILL;
		}

		fccRange = new FillChooserComposite(cmpRange, SWT.NONE, fillStyles, getContext(), null);
		GridData gdFCCRange = new GridData(GridData.FILL_HORIZONTAL);
		gdFCCRange.horizontalSpan = 2;
		fccRange.setLayoutData(gdFCCRange);
		fccRange.addListener(this);

		// Range Outline
		if (supportRangeOutline()) {
			grpMarkerRange = new Group(cmpRange, SWT.NONE);
			GridData gdGRPMarkerRange = new GridData(GridData.FILL_HORIZONTAL);
			gdGRPMarkerRange.horizontalSpan = 3;
			grpMarkerRange.setLayoutData(gdGRPMarkerRange);
			grpMarkerRange.setLayout(new FillLayout());
			grpMarkerRange.setText(Messages.getString("BaseAxisMarkerAttributeSheetImpl.Lbl.RangeOutline")); //$NON-NLS-1$

			int lineStyles = LineAttributesComposite.ENABLE_COLOR | LineAttributesComposite.ENABLE_STYLES
					| LineAttributesComposite.ENABLE_VISIBILITY | LineAttributesComposite.ENABLE_WIDTH;
			lineStyles |= getContext().getUIFactory().supportAutoUI() ? LineAttributesComposite.ENABLE_AUTO_COLOR
					: lineStyles;
			liacMarkerRange = new LineAttributesComposite(grpMarkerRange, SWT.NONE, lineStyles, getContext(), null,
					((DialSeries) defSeries).getDial().getDialRegions().get(0).getOutline());
			liacMarkerRange.addListener(this);
		}
		// Label Properties
		// lacLabel = new LabelAttributesComposite( cmpContent,
		// SWT.NONE,
		// Messages.getString(
		// "BaseAxisMarkerAttributeSheetImpl.Lbl.DialLabelProperties" ),
		// //$NON-NLS-1$
		// LabelImpl.create( ),
		// chart.getUnits( ),
		// false,
		// false,
		// serviceprovider,
		// false,
		// false );
		// GridData gdLACLabel = new GridData( GridData.FILL_BOTH );
		// lacLabel.setLayoutData( gdLACLabel );
		// lacLabel.addListener( this );
		// lacLabel.setEnabled( false );

		populateLists();

		refreshButtons();

		return cmpContent;
	}

	protected boolean supportPatternAndImageFill() {
		return true;
	}

	protected boolean supportRangeOutline() {
		return true;
	}

	protected boolean supportRadius() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent(Event event) {
		// if ( event.widget.equals( lacLabel ) )
		// {
		// if ( this.lstMarkers.getSelection( ).length != 0 )
		// {
		// switch ( event.type )
		// {
		// case LabelAttributesComposite.FONT_CHANGED_EVENT :
		// getSelectedMarkerLabel( ).getCaption( )
		// .setFont( (FontDefinition) ( (Object[]) event.data )[0] );
		// getSelectedMarkerLabel( ).getCaption( )
		// .setColor( (ColorDefinition) ( (Object[]) event.data )[1] );
		// break;
		// case LabelAttributesComposite.BACKGROUND_CHANGED_EVENT :
		// getSelectedMarkerLabel( ).setBackground( (Fill) event.data );
		// break;
		// case LabelAttributesComposite.OUTLINE_STYLE_CHANGED_EVENT :
		// getSelectedMarkerLabel( ).getOutline( )
		// .setStyle( (LineStyle) event.data );
		// break;
		// case LabelAttributesComposite.OUTLINE_WIDTH_CHANGED_EVENT :
		// getSelectedMarkerLabel( ).getOutline( )
		// .setThickness( ( (Integer) event.data ).intValue( ) );
		// break;
		// case LabelAttributesComposite.OUTLINE_COLOR_CHANGED_EVENT :
		// getSelectedMarkerLabel( ).getOutline( )
		// .setColor( (ColorDefinition) event.data );
		// break;
		// case LabelAttributesComposite.OUTLINE_VISIBILITY_CHANGED_EVENT :
		// getSelectedMarkerLabel( ).getOutline( )
		// .setVisible( ( (Boolean) event.data ).booleanValue( ) );
		// break;
		// }
		// }
		// }
		if (event.widget.equals(fccRange)) {
			(getDialForProcessing().getDialRegions()).get(getMarkerIndex()).setFill((Fill) event.data);
		} else if (event.widget.equals(txtStartValue)) {
			if (!TextEditorComposite.TEXT_RESET_MODEL.equals(event.data)) {
				int iMarkerIndex = getMarkerIndex();
				(getDialForProcessing().getDialRegions()).get(iMarkerIndex)
						.setStartValue(this.getTypedDataElement(txtStartValue.getText()));
			}
		} else if (event.widget.equals(txtEndValue)) {
			if (!TextEditorComposite.TEXT_RESET_MODEL.equals(event.data)) {
				int iMarkerIndex = getMarkerIndex();
				(getDialForProcessing().getDialRegions()).get(iMarkerIndex)
						.setEndValue(this.getTypedDataElement(txtEndValue.getText()));
			}
		} else if (event.widget.equals(liacMarkerRange)) {
			boolean isUnset = (event.detail == ChartUIExtensionUtil.PROPERTY_UNSET);
			if (event.type == LineAttributesComposite.STYLE_CHANGED_EVENT) {
				ChartElementUtil.setEObjectAttribute(
						getDialForProcessing().getDialRegions().get(getMarkerIndex()).getOutline(), "style", //$NON-NLS-1$
						event.data, isUnset);
			} else if (event.type == LineAttributesComposite.WIDTH_CHANGED_EVENT) {
				ChartElementUtil.setEObjectAttribute(
						getDialForProcessing().getDialRegions().get(getMarkerIndex()).getOutline(), "thickness", //$NON-NLS-1$
						((Integer) event.data).intValue(), isUnset);
			} else if (event.type == LineAttributesComposite.COLOR_CHANGED_EVENT) {
				getDialForProcessing().getDialRegions().get(getMarkerIndex()).getOutline()
						.setColor((ColorDefinition) event.data);
			} else {
				ChartElementUtil.setEObjectAttribute(
						getDialForProcessing().getDialRegions().get(getMarkerIndex()).getOutline(), "visible", //$NON-NLS-1$
						((Boolean) event.data).booleanValue(), isUnset);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.
	 * ModifyEvent)
	 */
	public void modifyText(ModifyEvent e) {
		int markerIndex = getMarkerIndex();
		if (markerIndex >= 0) {
			if (e.widget.equals(txtInnerRadius)) {
				if (!TextEditorComposite.TEXT_RESET_MODEL.equals(e.data)) {
					if (txtInnerRadius.isSetValue()) {
						(getDialForProcessing().getDialRegions()).get(getMarkerIndex())
								.setInnerRadius(txtInnerRadius.getValue());
					} else {
						(getDialForProcessing().getDialRegions()).get(getMarkerIndex()).unsetInnerRadius();
					}
				}
			} else if (e.widget.equals(txtOuterRadius)) {
				if (!TextEditorComposite.TEXT_RESET_MODEL.equals(e.data)) {
					if (txtOuterRadius.isSetValue()) {
						(getDialForProcessing().getDialRegions()).get(getMarkerIndex())
								.setOuterRadius(txtOuterRadius.getValue());
					} else {
						(getDialForProcessing().getDialRegions()).get(getMarkerIndex()).unsetOuterRadius();
					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.
	 * events.SelectionEvent)
	 */
	public void widgetSelected(SelectionEvent e) {
		if (e.getSource().equals(btnAddRange)) {
			DialRegion range = null;
			if (getContext().getUIFactory().supportAutoUI()) {
				range = DialRegionImpl.createDefault();
			} else {
				range = DialRegionImpl.create();
			}

			range.setStartValue(getTypedDataElement(null));
			range.setEndValue(getTypedDataElement(null));
			getDialForProcessing().getDialRegions().add(range);
			range.eAdapters().addAll(getDialForProcessing().eAdapters());
			iRangeCount++;
			buildList();
			lstMarkers.select(lstMarkers.getItemCount() - 1);
			updateUIForSelection();
			if (lstMarkers.getItemCount() == 1) {
				// Enable UI elements
				setState(true);
			}

			refreshButtons();
		} else if (e.getSource().equals(btnRemove)) {
			if (lstMarkers.getSelection().length == 0) {
				return;
			}
			int iMarkerIndex = getMarkerIndex();

			getDialForProcessing().getDialRegions().remove(iMarkerIndex);
			iRangeCount--;

			buildList();
			if (lstMarkers.getItemCount() > 0) {
				lstMarkers.select(0);
				updateUIForSelection();
			} else {
				resetUI();
				setState(false);
			}

			refreshButtons();
		} else if (e.getSource().equals(lstMarkers)) {
			updateUIForSelection();
			refreshButtons();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.
	 * swt.events.SelectionEvent)
	 */
	public void widgetDefaultSelected(SelectionEvent e) {
	}

	private Dial getDialForProcessing() {
		if (series != null) {
			return ((DialSeries) series).getDial();
		}
		return ((DialSeries) seriesDefn.getDesignTimeSeries()).getDial();
	}

	private String getValueAsString(DataElement de) {
		if (de != null) {
			return ChartUIUtil.getDefaultNumberFormatInstance().format(((NumberDataElement) de).getValue());
		}
		return "";//$NON-NLS-1$
	}

	private int getMarkerIndex() {
		int iSelectionIndex = lstMarkers.getSelectionIndex();
		return iSelectionIndex;
	}

	private void buildList() {
		// Clear any existing contents
		lstMarkers.removeAll();

		iRangeCount = getDialForProcessing().getDialRegions().size();
		for (int iRanges = 0; iRanges < iRangeCount; iRanges++) {
			lstMarkers.add(Messages.getString("SeriesRegionSheet.message.dialRegion") + (iRanges + 1)); //$NON-NLS-1$
		}
	}

	private void refreshButtons() {
		btnRemove.setEnabled(lstMarkers.getSelectionIndex() != -1);
	}

	private void updateUIForSelection() {
		grpGeneral.layout();
		int iRangeIndex = getMarkerIndex();
		DialRegion range = getDialForProcessing().getDialRegions().get(iRangeIndex);

		// Update the value fields
		txtStartValue.setEObjectParent(range);
		txtStartValue.setText(getValueAsString(range.getStartValue()));
		txtEndValue.setEObjectParent(range);
		txtEndValue.setText(getValueAsString(range.getEndValue()));

		// Update the radius fields
		if (supportRadius()) {
			txtInnerRadius.setEObjectParent(range);
			if (range.isSetInnerRadius()) {
				txtInnerRadius.setValue(range.getInnerRadius());
			} else {
				txtInnerRadius.unsetValue();
			}

			txtOuterRadius.setEObjectParent(range);
			if (range.isSetOuterRadius()) {
				txtOuterRadius.setValue(range.getOuterRadius());
			} else {
				txtOuterRadius.unsetValue();
			}
		}

		// Update the fill
		fccRange.setFill(range.getFill());

		if (supportRangeOutline()) {
			// Update the Line attribute fields
			liacMarkerRange.setLineAttributes(range.getOutline());
		}

		// // Update the Label attribute fields
		// lacLabel.setLabel( range.getLabel( ), chart.getUnits( ) );
	}

	private void populateLists() {
		buildList();

		if (lstMarkers.getItemCount() > 0) {
			lstMarkers.select(0);
			updateUIForSelection();
		} else {
			setState(false);
		}
	}

	private void setState(boolean bState) {
		lblStartValue.setEnabled(bState);
		txtStartValue.setEnabled(bState);
		lblEndValue.setEnabled(bState);
		txtEndValue.setEnabled(bState);

		if (supportRadius()) {
			lblInnerRadius.setEnabled(bState);
			txtInnerRadius.setEnabled(bState);
			lblOuterRadius.setEnabled(bState);
			txtOuterRadius.setEnabled(bState);
		}

		if (supportRangeOutline()) {
			liacMarkerRange.setAttributesEnabled(bState);
			// lacLabel.setEnabled( bState );
			this.grpMarkerRange.setEnabled(bState);
		}

		lblRangeFill.setEnabled(bState);
		fccRange.setEnabled(bState);

		this.grpGeneral.setEnabled(bState);

	}

	private void resetUI() {
		txtStartValue.setText(""); //$NON-NLS-1$
		txtEndValue.setText(""); //$NON-NLS-1$
		if (supportRadius()) {
			txtInnerRadius.unsetValue();
			txtOuterRadius.unsetValue();
		}
		fccRange.setFill(null);

		if (supportRangeOutline()) {
			liacMarkerRange.setLineAttributes(null);
			liacMarkerRange.layout();
		}
		// lacLabel.setLabel( LabelImpl.create( ), chart.getUnits( ) );
		// lacLabel.layout( );
	}

	// private org.eclipse.birt.chart.model.component.Label
	// getSelectedMarkerLabel( )
	// {
	// int iMarkerIndex = getMarkerIndex( );
	// return ( (DialRegion) ( getDialForProcessing( ).getDialRegions( ).get(
	// iMarkerIndex ) ) ).getLabel( );
	// }

	private DataElement getTypedDataElement(String strDataElement) {
		if (strDataElement == null) {
			return null;
		}
		if (strDataElement.trim().length() == 0) {
			return NumberDataElementImpl.create(0.0);
		}
		NumberFormat nf = ChartUIUtil.getDefaultNumberFormatInstance();

		try {
			Number numberElement = nf.parse(strDataElement);
			return NumberDataElementImpl.create(numberElement.doubleValue());
		} catch (ParseException e1) {
			return NumberDataElementImpl.create(0.0);
		}
	}
}
