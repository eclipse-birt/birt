/*******************************************************************************
 * Copyright (c) 2012, 2014 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.ide.preferences;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.preference.IPreferences;
import org.eclipse.birt.report.designer.internal.ui.script.JSDocumentProvider;
import org.eclipse.birt.report.designer.internal.ui.script.JSEditorInput;
import org.eclipse.birt.report.designer.internal.ui.script.JSPartitionScanner;
import org.eclipse.birt.report.designer.internal.ui.script.JSSourceViewerConfiguration;
import org.eclipse.birt.report.designer.internal.ui.util.ColorHelper;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.preferences.OverlayPreferenceStore;
import org.eclipse.birt.report.designer.ui.preferences.OverlayPreferenceStore.OverlayKey;
import org.eclipse.birt.report.designer.ui.preferences.PreferenceFactory;
import org.eclipse.birt.report.designer.util.ColorManager;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.TextUtilities;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.dialogs.PreferencesUtil;

import com.ibm.icu.text.Collator;

/**
 * Class to represents the syntax coloring page
 *
 * @since 3.3
 *
 */
public final class ExpressionSyntaxColoringPage extends AbstractSyntaxColoringPage implements IWorkbenchPreferencePage {

	private Button fBold;
	private Label fForegroundLabel;
	private Label fBackgroundLabel;
	private Button fClearStyle;
	private Map<String, String> fContextToStyleMap;
	private Color fDefaultForeground = null;
	private Color fDefaultBackground = null;
	private IDocument fDocument;
	private ColorSelector fForegroundColorEditor;
	private ColorSelector fBackgroundColorEditor;
	private Button fItalic;
	private OverlayPreferenceStore fOverlayStore;
	private Button fStrike;
	private Collection<String> fStylePreferenceKeys;
	private StructuredViewer fStylesViewer = null;
	private Map<String, String> fStyleToDescriptionMap;
	private StyledText fText;
	private Button fUnderline;

	private ISourceViewer fPreviewViewer;
	private IPreferences preference;

	// activate controls based on the given local color type
	private void activate(String namedStyle) {
		Color foreground = fDefaultForeground;
		Color background = fDefaultBackground;
		if (namedStyle == null) {
			fClearStyle.setEnabled(false);
			fBold.setEnabled(false);
			fItalic.setEnabled(false);
			fStrike.setEnabled(false);
			fUnderline.setEnabled(false);
			fForegroundLabel.setEnabled(false);
			fBackgroundLabel.setEnabled(false);
			fForegroundColorEditor.setEnabled(false);
			fBackgroundColorEditor.setEnabled(false);
			fBold.setSelection(false);
			fItalic.setSelection(false);
			fStrike.setSelection(false);
			fUnderline.setSelection(false);
		} else {
			TextAttribute attribute = getAttributeFor(namedStyle);
			fClearStyle.setEnabled(true);
			fBold.setEnabled(true);
			fItalic.setEnabled(true);
			fStrike.setEnabled(true);
			fUnderline.setEnabled(true);
			fForegroundLabel.setEnabled(true);
			fBackgroundLabel.setEnabled(true);
			fForegroundColorEditor.setEnabled(true);
			fBackgroundColorEditor.setEnabled(true);
			fBold.setSelection((attribute.getStyle() & SWT.BOLD) != 0);
			fItalic.setSelection((attribute.getStyle() & SWT.ITALIC) != 0);
			fStrike.setSelection((attribute.getStyle() & TextAttribute.STRIKETHROUGH) != 0);
			fUnderline.setSelection((attribute.getStyle() & TextAttribute.UNDERLINE) != 0);
			if (attribute.getForeground() != null) {
				foreground = attribute.getForeground();
			}
			if (attribute.getBackground() != null) {
				background = attribute.getBackground();
			}
		}

		fForegroundColorEditor.setColorValue(foreground.getRGB());
		fBackgroundColorEditor.setColorValue(background.getRGB());
	}

	/**
	 * Color the text in the sample area according to the current preferences
	 */
	void applyStyles() {
		if (fText == null || fText.isDisposed()) {
			return;
		}

		try {
			ITypedRegion[] regions = TextUtilities.computePartitioning(fDocument,
					IDocumentExtension3.DEFAULT_PARTITIONING, 0, fDocument.getLength(), true);
			if (regions.length > 0) {
				for (int i = 0; i < regions.length; i++) {
					ITypedRegion region = regions[i];
					String namedStyle = fContextToStyleMap.get(region.getType());
					if (namedStyle == null) {
						continue;
					}
					TextAttribute attribute = getAttributeFor(namedStyle);
					if (attribute == null) {
						continue;
					}
					int fontStyle = attribute.getStyle() & (SWT.ITALIC | SWT.BOLD | SWT.NORMAL);
					StyleRange style = new StyleRange(region.getOffset(), region.getLength(), attribute.getForeground(),
							attribute.getBackground(), fontStyle);
					style.strikeout = (attribute.getStyle() & TextAttribute.STRIKETHROUGH) != 0;
					style.underline = (attribute.getStyle() & TextAttribute.UNDERLINE) != 0;
					style.font = attribute.getFont();
					fText.setStyleRange(style);
				}
			}
		} catch (BadLocationException e) {
			ExceptionHandler.handle(e);
		}
	}

	Button createCheckbox(Composite parent, String label) {
		Button button = new Button(parent, SWT.CHECK);
		button.setText(label);
		button.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		return button;
	}

	/**
	 * Creates composite control and sets the default layout data.
	 */
	private Composite createComposite(Composite parent, int numColumns) {
		Composite composite = new Composite(parent, SWT.NULL);

		// GridLayout
		GridLayout layout = new GridLayout();
		layout.numColumns = numColumns;
		layout.makeColumnsEqualWidth = false;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		composite.setLayout(layout);

		// GridData
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, false);
		composite.setLayoutData(data);
		return composite;
	}

	@Override
	protected Control createContents(final Composite parent) {
		initializeDialogUnits(parent);

		fDefaultForeground = UIUtil.getEclipseEditorForeground();
		fDefaultBackground = UIUtil.getEclipseEditorBackground();
		Composite pageComponent = createComposite(parent, 2);
		UIUtil.bindHelp(getControl(), IHelpContextIds.PREFERENCE_BIRT_EXPRESSION_SYNTAX_COLOR_ID);

		Link link = new Link(pageComponent, SWT.WRAP);
		link.setText(Messages.getString("ExpressionSyntaxColoringPage.Link.ColorAndFont")); //$NON-NLS-1$
		link.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				PreferencesUtil.createPreferenceDialogOn(parent.getShell(), e.text, null, null);
			}
		});

		GridData linkData = new GridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1);
		linkData.widthHint = 150; // only expand further if anyone else requires
									// it
		link.setLayoutData(linkData);

		new Label(pageComponent, SWT.NONE).setLayoutData(new GridData());
		new Label(pageComponent, SWT.NONE).setLayoutData(new GridData());

		SashForm editor = new SashForm(pageComponent, SWT.VERTICAL);
		GridData gridData2 = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData2.horizontalSpan = 2;
		editor.setLayoutData(gridData2);
		SashForm top = new SashForm(editor, SWT.HORIZONTAL);
		Composite styleEditor = createComposite(top, 1);
		((GridLayout) styleEditor.getLayout()).marginRight = 5;
		((GridLayout) styleEditor.getLayout()).marginLeft = 0;
		createLabel(styleEditor, Messages.getString("ExpressionSyntaxColoringPage.Label.SyntaxElement")); //$NON-NLS-1$
		fStylesViewer = createStylesViewer(styleEditor);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.horizontalIndent = 0;
		Iterator<String> iterator = fStyleToDescriptionMap.values().iterator();
		while (iterator.hasNext()) {
			gridData.widthHint = Math.max(gridData.widthHint,
					convertWidthInCharsToPixels(iterator.next().toString().length()));
		}
		gridData.heightHint = convertHeightInCharsToPixels(5);
		fStylesViewer.getControl().setLayoutData(gridData);

		Composite editingComposite = createComposite(top, 1);
		((GridLayout) styleEditor.getLayout()).marginLeft = 5;
		createLabel(editingComposite, ""); //$NON-NLS-1$
		Button enabler = createCheckbox(editingComposite,
				Messages.getString("ExpressionSyntaxColoringPage.Button.Enable")); //$NON-NLS-1$
		enabler.setEnabled(false);
		enabler.setSelection(true);
		Composite editControls = createComposite(editingComposite, 2);
		((GridLayout) editControls.getLayout()).marginLeft = 20;

		fForegroundLabel = createLabel(editControls,
				Messages.getString("ExpressionSyntaxColoringPage.Label.Foreground")); //$NON-NLS-1$
		((GridData) fForegroundLabel.getLayoutData()).verticalAlignment = SWT.CENTER;
		fForegroundLabel.setEnabled(false);

		fForegroundColorEditor = new ColorSelector(editControls);
		Button fForegroundColor = fForegroundColorEditor.getButton();
		GridData gd = new GridData(SWT.BEGINNING, SWT.FILL, false, false);
		fForegroundColor.setLayoutData(gd);
		fForegroundColorEditor.setEnabled(false);
		fForegroundColorEditor.getButton().getAccessible().addAccessibleListener(new AccessibleAdapter() {

			@Override
			public void getName(final AccessibleEvent e) {
				e.result = Messages.getString("ExpressionSyntaxColoringPage.Accessible.Name.Foreground"); //$NON-NLS-1$
			}
		});

		fBackgroundLabel = createLabel(editControls,
				Messages.getString("ExpressionSyntaxColoringPage.Label.Background")); //$NON-NLS-1$
		((GridData) fBackgroundLabel.getLayoutData()).verticalAlignment = SWT.CENTER;
		fBackgroundLabel.setEnabled(false);

		fBackgroundColorEditor = new ColorSelector(editControls);
		Button fBackgroundColor = fBackgroundColorEditor.getButton();
		gd = new GridData(SWT.BEGINNING, SWT.FILL, false, false);
		fBackgroundColor.setLayoutData(gd);
		fBackgroundColorEditor.setEnabled(false);
		fBackgroundColorEditor.getButton().getAccessible().addAccessibleListener(new AccessibleAdapter() {

			@Override
			public void getName(final AccessibleEvent e) {
				e.result = Messages.getString("ExpressionSyntaxColoringPage.Accessible.Name.Background"); //$NON-NLS-1$
			}
		});

		fBold = createCheckbox(editControls, Messages.getString("ExpressionSyntaxColoringPage.Button.Bold")); //$NON-NLS-1$
		fBold.setEnabled(false);
		((GridData) fBold.getLayoutData()).horizontalSpan = 2;
		fItalic = createCheckbox(editControls, Messages.getString("ExpressionSyntaxColoringPage.Button.Italic")); //$NON-NLS-1$
		fItalic.setEnabled(false);
		((GridData) fItalic.getLayoutData()).horizontalSpan = 2;
		fStrike = createCheckbox(editControls, Messages.getString("ExpressionSyntaxColoringPage.Button.Strikethrough")); //$NON-NLS-1$
		fStrike.setEnabled(false);
		((GridData) fStrike.getLayoutData()).horizontalSpan = 2;
		fUnderline = createCheckbox(editControls, Messages.getString("ExpressionSyntaxColoringPage.Button.Underline")); //$NON-NLS-1$
		fUnderline.setEnabled(false);
		((GridData) fUnderline.getLayoutData()).horizontalSpan = 2;
		fClearStyle = new Button(editingComposite, SWT.PUSH);
		fClearStyle.setText(Messages.getString("ExpressionSyntaxColoringPage.Button.Restore")); //$NON-NLS-1$
		fClearStyle.setLayoutData(new GridData(SWT.BEGINNING));
		((GridData) fClearStyle.getLayoutData()).horizontalIndent = 20;
		fClearStyle.setEnabled(false);

		Composite sampleArea = createComposite(editor, 1);

		((GridLayout) sampleArea.getLayout()).marginLeft = 5;
		((GridLayout) sampleArea.getLayout()).marginTop = 5;
		createLabel(sampleArea, Messages.getString("ExpressionSyntaxColoringPage.Label.SampleText")); //$NON-NLS-1$
		fPreviewViewer = new SourceViewer(sampleArea, null,
				SWT.BORDER | SWT.LEFT_TO_RIGHT | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | SWT.READ_ONLY);
		fPreviewViewer.configure(new JSSourceViewerConfiguration());
		fText = fPreviewViewer.getTextWidget();
		GridData gridData3 = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData3.widthHint = convertWidthInCharsToPixels(20);
		gridData3.heightHint = convertHeightInCharsToPixels(5);
		gridData3.horizontalSpan = 2;
		fText.setLayoutData(gridData3);
		fText.setEditable(false);
		fText.setFont(JFaceResources.getFont(JFaceResources.TEXT_FONT));
		fText.addKeyListener(getTextKeyListener());
		fText.addSelectionListener(getTextSelectionListener());
		fText.addMouseListener(getTextMouseListener());
		fText.addTraverseListener(getTraverseListener());
		setAccessible(fText, Messages.getString("ExpressionSyntaxColoringPage.Label.SampleText")); //$NON-NLS-1$

		JSEditorInput editorInput = new JSEditorInput(loadPreviewContentFromFile());
		JSDocumentProvider documentProvider = new JSDocumentProvider();

		try {
			documentProvider.connect(editorInput);
		} catch (CoreException e) {
			ExceptionHandler.handle(e);
		}

		fDocument = documentProvider.getDocument(editorInput);

		initializeSourcePreviewColors(fPreviewViewer);

		fPreviewViewer.setDocument(fDocument);

		top.setWeights(new int[] { 1, 1 });
		editor.setWeights(new int[] { 1, 1 });

		fStylesViewer.setInput(getStylePreferenceKeys());

		applyStyles();

		fStylesViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				if (!event.getSelection().isEmpty()) {
					Object o = ((IStructuredSelection) event.getSelection()).getFirstElement();
					String namedStyle = o.toString();
					activate(namedStyle);
					if (namedStyle == null) {
					}
				}
			}
		});

		fForegroundColorEditor.addListener(new IPropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent event) {
				if (event.getProperty().equals(ColorSelector.PROP_COLORCHANGE)) {
					Object o = ((IStructuredSelection) fStylesViewer.getSelection()).getFirstElement();
					String namedStyle = o.toString();
					String prefString = getOverlayStore().getString(namedStyle);
					String[] stylePrefs = ColorHelper.unpackStylePreferences(prefString);
					if (stylePrefs != null) {
						String oldValue = stylePrefs[0];
						// open color dialog to get new color
						String newValue = ColorHelper.toRGBString(fForegroundColorEditor.getColorValue());

						if (!newValue.equals(oldValue)) {
							stylePrefs[0] = newValue;
							String newPrefString = ColorHelper.packStylePreferences(stylePrefs);
							getOverlayStore().setValue(namedStyle, newPrefString);
							applyStyles();
							fText.redraw();
						}
					}
				}
			}
		});

		fBackgroundColorEditor.addListener(new IPropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent event) {
				if (event.getProperty().equals(ColorSelector.PROP_COLORCHANGE)) {
					Object o = ((IStructuredSelection) fStylesViewer.getSelection()).getFirstElement();
					String namedStyle = o.toString();
					String prefString = getOverlayStore().getString(namedStyle);
					String[] stylePrefs = ColorHelper.unpackStylePreferences(prefString);
					if (stylePrefs != null) {
						String oldValue = stylePrefs[1];
						// open color dialog to get new color
						String newValue = ColorHelper.toRGBString(fBackgroundColorEditor.getColorValue());

						if (!newValue.equals(oldValue)) {
							stylePrefs[1] = newValue;
							String newPrefString = ColorHelper.packStylePreferences(stylePrefs);
							getOverlayStore().setValue(namedStyle, newPrefString);
							applyStyles();
							fText.redraw();
							activate(namedStyle);
						}
					}
				}
			}
		});

		fBold.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				// get current (newly old) style
				Object o = ((IStructuredSelection) fStylesViewer.getSelection()).getFirstElement();
				String namedStyle = o.toString();
				String prefString = getOverlayStore().getString(namedStyle);
				String[] stylePrefs = ColorHelper.unpackStylePreferences(prefString);
				if (stylePrefs != null) {
					String oldValue = stylePrefs[2];
					String newValue = String.valueOf(fBold.getSelection());
					if (!newValue.equals(oldValue)) {
						stylePrefs[2] = newValue;
						String newPrefString = ColorHelper.packStylePreferences(stylePrefs);
						getOverlayStore().setValue(namedStyle, newPrefString);
						applyStyles();
						fText.redraw();
					}
				}
			}
		});

		fItalic.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				// get current (newly old) style
				Object o = ((IStructuredSelection) fStylesViewer.getSelection()).getFirstElement();
				String namedStyle = o.toString();
				String prefString = getOverlayStore().getString(namedStyle);
				String[] stylePrefs = ColorHelper.unpackStylePreferences(prefString);
				if (stylePrefs != null) {
					String oldValue = stylePrefs[3];
					String newValue = String.valueOf(fItalic.getSelection());
					if (!newValue.equals(oldValue)) {
						stylePrefs[3] = newValue;
						String newPrefString = ColorHelper.packStylePreferences(stylePrefs);
						getOverlayStore().setValue(namedStyle, newPrefString);
						applyStyles();
						fText.redraw();
					}
				}
			}
		});

		fStrike.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				// get current (newly old) style
				Object o = ((IStructuredSelection) fStylesViewer.getSelection()).getFirstElement();
				String namedStyle = o.toString();
				String prefString = getOverlayStore().getString(namedStyle);
				String[] stylePrefs = ColorHelper.unpackStylePreferences(prefString);
				if (stylePrefs != null) {
					String oldValue = stylePrefs[4];
					String newValue = String.valueOf(fStrike.getSelection());
					if (!newValue.equals(oldValue)) {
						stylePrefs[4] = newValue;
						String newPrefString = ColorHelper.packStylePreferences(stylePrefs);
						getOverlayStore().setValue(namedStyle, newPrefString);
						applyStyles();
						fText.redraw();
					}
				}
			}
		});

		fUnderline.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				// get current (newly old) style
				Object o = ((IStructuredSelection) fStylesViewer.getSelection()).getFirstElement();
				String namedStyle = o.toString();
				String prefString = getOverlayStore().getString(namedStyle);
				String[] stylePrefs = ColorHelper.unpackStylePreferences(prefString);
				if (stylePrefs != null) {
					String oldValue = stylePrefs[5];
					String newValue = String.valueOf(fUnderline.getSelection());
					if (!newValue.equals(oldValue)) {
						stylePrefs[5] = newValue;
						String newPrefString = ColorHelper.packStylePreferences(stylePrefs);
						getOverlayStore().setValue(namedStyle, newPrefString);
						applyStyles();
						fText.redraw();
					}
				}
			}
		});

		fClearStyle.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (fStylesViewer.getSelection().isEmpty()) {
					return;
				}
				String namedStyle = ((IStructuredSelection) fStylesViewer.getSelection()).getFirstElement().toString();
				getOverlayStore().setToDefault(namedStyle);
				applyStyles();
				fText.redraw();
				activate(namedStyle);
			}
		});

		return pageComponent;
	}

	private String loadPreviewContentFromFile() {
		String line;
		String separator = System.lineSeparator();
		StringBuilder buffer = new StringBuilder(512);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(
					new InputStreamReader(this.getClass().getResourceAsStream("ColorSettingPreviewCode.txt"))); //$NON-NLS-1$
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
				buffer.append(separator);
			}
		} catch (IOException io) {

		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
		}
		return buffer.toString();
	}

	private Label createLabel(Composite parent, String text) {
		Label label = new Label(parent, SWT.WRAP);
		label.setText(text);
		GridData data = new GridData(SWT.FILL, SWT.FILL, false, false);
		label.setLayoutData(data);
		label.setBackground(parent.getBackground());
		return label;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.wst.sse.ui.internal.preferences.ui.AbstractSyntaxColoringPage
	 * #getSourcePreview()
	 */
	@Override
	protected ISourceViewer getSourcePreviewViewer() {
		return fPreviewViewer;
	}

	// protected Label createDescriptionLabel(Composite parent) {
	// return null;
	// }

	/**
	 * Set up all the style preference keys in the overlay store
	 */
	private OverlayKey[] createOverlayStoreKeys() {
		List<OverlayKey> overlayKeys = new ArrayList<OverlayKey>();

		Iterator<String> i = getStylePreferenceKeys().iterator();
		while (i.hasNext()) {
			overlayKeys.add(new OverlayPreferenceStore.OverlayKey(OverlayPreferenceStore.STRING, i.next()));
		}

		OverlayPreferenceStore.OverlayKey[] keys = new OverlayPreferenceStore.OverlayKey[overlayKeys.size()];
		overlayKeys.toArray(keys);
		return keys;
	}

	/**
	 * Creates the List viewer where we see the various syntax element display
	 * names--would it ever be a Tree like JDT's?
	 *
	 * @param parent
	 * @return
	 */
	private StructuredViewer createStylesViewer(Composite parent) {
		StructuredViewer stylesViewer = new ListViewer(parent, SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		stylesViewer.setComparator(new ViewerComparator(Collator.getInstance()));
		stylesViewer.setLabelProvider(new LabelProvider() {

			@Override
			public String getText(Object element) {
				Object description = fStyleToDescriptionMap.get(element);
				if (description != null) {
					return description.toString();
				}
				return super.getText(element);
			}
		});
		stylesViewer.setContentProvider(new ITreeContentProvider() {

			@Override
			public void dispose() {
			}

			@Override
			public Object[] getChildren(Object parentElement) {
				return getStylePreferenceKeys().toArray();
			}

			@Override
			public Object[] getElements(Object inputElement) {
				return getChildren(inputElement);
			}

			@Override
			public Object getParent(Object element) {
				return getStylePreferenceKeys();
			}

			@Override
			public boolean hasChildren(Object element) {
				return false;
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			}
		});
		return stylesViewer;
	}

	@Override
	public void dispose() {
		if (fOverlayStore != null) {
			fOverlayStore.stop();
		}
		super.dispose();
	}

	private TextAttribute getAttributeFor(String namedStyle) {
		TextAttribute ta = new TextAttribute(fDefaultForeground, fDefaultBackground, SWT.NORMAL);

		if (namedStyle != null && fOverlayStore != null) {
			// note: "namedStyle" *is* the preference key
			String prefString = getOverlayStore().getString(namedStyle);
			String[] stylePrefs = ColorHelper.unpackStylePreferences(prefString);
			if (stylePrefs != null) {
				RGB foreground = ColorHelper.toRGB(stylePrefs[0]);
				RGB background = ColorHelper.toRGB(stylePrefs[1]);

				int fontModifier = SWT.NORMAL;

				if (stylePrefs.length > 2) {
					boolean on = Boolean.parseBoolean(stylePrefs[2]);
					if (on) {
						fontModifier = fontModifier | SWT.BOLD;
					}
				}
				if (stylePrefs.length > 3) {
					boolean on = Boolean.parseBoolean(stylePrefs[3]);
					if (on) {
						fontModifier = fontModifier | SWT.ITALIC;
					}
				}
				if (stylePrefs.length > 4) {
					boolean on = Boolean.parseBoolean(stylePrefs[4]);
					if (on) {
						fontModifier = fontModifier | TextAttribute.STRIKETHROUGH;
					}
				}
				if (stylePrefs.length > 5) {
					boolean on = Boolean.parseBoolean(stylePrefs[5]);
					if (on) {
						fontModifier = fontModifier | TextAttribute.UNDERLINE;
					}
				}

				ta = new TextAttribute((foreground != null) ? ColorManager.getColor(foreground) : null,
						(background != null) ? ColorManager.getColor(background) : null, fontModifier);
			}
		}
		return ta;
	}

	private String getNamedStyleAtOffset(int offset) {
		// ensure the offset is clean
		if (offset >= fDocument.getLength()) {
			return getNamedStyleAtOffset(fDocument.getLength() - 1);
		} else if (offset < 0) {
			return getNamedStyleAtOffset(0);
		}
		try {
			String regionContext = fDocument.getPartition(offset).getType();

			String namedStyle = fContextToStyleMap.get(regionContext);
			if (namedStyle != null) {
				return namedStyle;
			}
		} catch (BadLocationException e) {
		}
		return null;
	}

	private OverlayPreferenceStore getOverlayStore() {
		return fOverlayStore;
	}

	private Collection<String> getStylePreferenceKeys() {
		if (fStylePreferenceKeys == null) {
			List<String> styles = new ArrayList<String>();
			styles.add(ReportPlugin.EXPRESSION_CONTENT_COLOR_PREFERENCE);
			styles.add(ReportPlugin.EXPRESSION_COMMENT_COLOR_PREFERENCE);
			styles.add(ReportPlugin.EXPRESSION_KEYWORD_COLOR_PREFERENCE);
			styles.add(ReportPlugin.EXPRESSION_STRING_COLOR_PREFERENCE);
			styles.add(ReportPlugin.EXPRESSION_METHOD_COLOR_PREFERENCE);
			styles.add(ReportPlugin.EXPRESSION_OBJECT_COLOR_PREFERENCE);
			fStylePreferenceKeys = styles;
		}
		return fStylePreferenceKeys;
	}

	private KeyListener getTextKeyListener() {
		return new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.widget instanceof StyledText) {
					int x = ((StyledText) e.widget).getCaretOffset();
					selectColorAtOffset(x);
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.widget instanceof StyledText) {
					int x = ((StyledText) e.widget).getCaretOffset();
					selectColorAtOffset(x);
				}
			}
		};
	}

	private MouseListener getTextMouseListener() {
		return new MouseListener() {

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}

			@Override
			public void mouseUp(MouseEvent e) {
				if (e.widget instanceof StyledText) {
					int x = ((StyledText) e.widget).getCaretOffset();
					selectColorAtOffset(x);
				}
			}
		};
	}

	private SelectionListener getTextSelectionListener() {
		return new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				selectColorAtOffset(e.x);
				if (e.widget instanceof StyledText) {
					((StyledText) e.widget).setSelection(e.x);
				}
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				selectColorAtOffset(e.x);
				if (e.widget instanceof StyledText) {
					((StyledText) e.widget).setSelection(e.x);
				}
			}
		};
	}

	private TraverseListener getTraverseListener() {
		return new TraverseListener() {

			/**
			 * @see org.eclipse.swt.events.TraverseListener#keyTraversed(TraverseEvent)
			 */
			@Override
			public void keyTraversed(TraverseEvent e) {
				if (e.widget instanceof StyledText) {
					if ((e.detail == SWT.TRAVERSE_TAB_NEXT) || (e.detail == SWT.TRAVERSE_TAB_PREVIOUS)) {
						e.doit = true;
					}
				}
			}
		};
	}

	@Override
	public void init(IWorkbench workbench) {
		setDescription(Messages.getString("ExpressionSyntaxColoringPage.Decscription")); //$NON-NLS-1$

		fStyleToDescriptionMap = new HashMap<String, String>();
		fContextToStyleMap = new HashMap<String, String>();

		initStyleToDescriptionMap();
		initRegionContextToStyleMap();

		preference = PreferenceFactory.getInstance().getPreferences(ReportPlugin.getDefault());
		fOverlayStore = new OverlayPreferenceStore(preference, createOverlayStoreKeys());
		fOverlayStore.load();
		fOverlayStore.start();
	}

	private void initRegionContextToStyleMap() {
		fContextToStyleMap.put(IDocument.DEFAULT_CONTENT_TYPE, ReportPlugin.EXPRESSION_CONTENT_COLOR_PREFERENCE);
		fContextToStyleMap.put(JSPartitionScanner.JS_COMMENT, ReportPlugin.EXPRESSION_COMMENT_COLOR_PREFERENCE);
		fContextToStyleMap.put(JSPartitionScanner.JS_KEYWORD, ReportPlugin.EXPRESSION_KEYWORD_COLOR_PREFERENCE);
		fContextToStyleMap.put(JSPartitionScanner.JS_STRING, ReportPlugin.EXPRESSION_STRING_COLOR_PREFERENCE);
		fContextToStyleMap.put(JSPartitionScanner.JS_METHOD, ReportPlugin.EXPRESSION_METHOD_COLOR_PREFERENCE);
		fContextToStyleMap.put(JSPartitionScanner.JS_OBJECT, ReportPlugin.EXPRESSION_OBJECT_COLOR_PREFERENCE);
	}

	private void initStyleToDescriptionMap() {
		fStyleToDescriptionMap.put(ReportPlugin.EXPRESSION_CONTENT_COLOR_PREFERENCE,
				Messages.getString("ExpressionSyntaxColoringPage.Element.Default")); //$NON-NLS-1$
		fStyleToDescriptionMap.put(ReportPlugin.EXPRESSION_COMMENT_COLOR_PREFERENCE,
				Messages.getString("ExpressionSyntaxColoringPage.Element.Comment")); //$NON-NLS-1$
		fStyleToDescriptionMap.put(ReportPlugin.EXPRESSION_KEYWORD_COLOR_PREFERENCE,
				Messages.getString("ExpressionSyntaxColoringPage.Element.Keyword")); //$NON-NLS-1$
		fStyleToDescriptionMap.put(ReportPlugin.EXPRESSION_STRING_COLOR_PREFERENCE,
				Messages.getString("ExpressionSyntaxColoringPage.Element.String")); //$NON-NLS-1$
		fStyleToDescriptionMap.put(ReportPlugin.EXPRESSION_METHOD_COLOR_PREFERENCE,
				Messages.getString("ExpressionSyntaxColoringPage.Element.Method")); //$NON-NLS-1$
		fStyleToDescriptionMap.put(ReportPlugin.EXPRESSION_OBJECT_COLOR_PREFERENCE,
				Messages.getString("ExpressionSyntaxColoringPage.Element.Object")); //$NON-NLS-1$
	}

	@Override
	protected void performDefaults() {
		super.performDefaults();
		getOverlayStore().loadDefaults();
		applyStyles();
		fStylesViewer.setSelection(StructuredSelection.EMPTY);
		activate(null);
		fText.redraw();
	}

	@Override
	public boolean performOk() {
		getOverlayStore().propagate();
		if (preference != null) {
			try {
				preference.save();
			} catch (IOException e) {
				ExceptionHandler.handle(e);
			}
		}
		return true;
	}

	private void selectColorAtOffset(int offset) {
		String namedStyle = getNamedStyleAtOffset(offset);
		if (namedStyle != null) {
			fStylesViewer.setSelection(new StructuredSelection(namedStyle));
			fStylesViewer.reveal(namedStyle);
		} else {
			fStylesViewer.setSelection(StructuredSelection.EMPTY);
		}
		activate(namedStyle);
	}

	/**
	 * Specifically set the reporting name of a control for accessibility
	 */
	private void setAccessible(Control control, String name) {
		if (control == null) {
			return;
		}
		final String n = name;
		control.getAccessible().addAccessibleListener(new AccessibleAdapter() {

			@Override
			public void getName(AccessibleEvent e) {
				if (e.childID == ACC.CHILDID_SELF) {
					e.result = n;
				}
			}
		});
	}
}
