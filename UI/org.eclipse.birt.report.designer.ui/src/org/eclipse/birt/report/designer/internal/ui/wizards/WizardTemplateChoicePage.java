/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.wizards;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.internal.util.EclipseUtil;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.ReportGraphicsViewPainter;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.util.graphics.ImageCanvas;
import org.eclipse.birt.report.designer.internal.ui.wizards.ExtensionTemplateListProvider.TemplateUICallback;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.designer.util.ImageManager;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISharedImages;
import org.osgi.framework.Bundle;

/**
 * Supplies template selection page of new report wizard
 * 
 */
public class WizardTemplateChoicePage extends WizardPage implements TemplateUICallback {

	protected static final Logger logger = Logger.getLogger(WizardTemplateChoicePage.class.getName());

	private static final String[] IMAGE_TYPES = new String[] { ".bmp", //$NON-NLS-1$
			".jpg", //$NON-NLS-1$
			".jpeg", //$NON-NLS-1$
			".jpe", //$NON-NLS-1$
			".jfif", //$NON-NLS-1$
			".gif", //$NON-NLS-1$
			".png", //$NON-NLS-1$
			".tif", //$NON-NLS-1$
			".tiff", //$NON-NLS-1$
			".ico", //$NON-NLS-1$
			".svg" //$NON-NLS-1$
	};

	private static final String MESSAGE_DESCRIPTION = Messages.getString("WizardTemplateChoicePage.label.Description"); //$NON-NLS-1$

	private static final String MESSAGE_PREVIEW = Messages.getString("WizardTemplateChoicePage.label.Preview"); //$NON-NLS-1$

	private static final String MESSAGE_REPORT_TEMPLATES = Messages
			.getString("WizardTemplateChoicePage.label.ReportTemplates"); //$NON-NLS-1$

	private static final String MESSAGE_SHOW_CHEATSHEET = Messages
			.getString("WizardTemplateChoicePage.label.ShowCheatSheets"); //$NON-NLS-1$ )

	private static final String ENABLED = "true"; //$NON-NLS-1$

	private ImageCanvas previewCanvas;

	private Button chkBox;

	private Button useDefaultLibraryBox;

	private LibraryHandle defaultLibraryHandle;

	// bidi_hcg start
	private static final String MESSAGE_RTL_BIDI = Messages
			.getString("WizardTemplateChoicePage.label.rtlBiDiOrientation"); //$NON-NLS-1$ )
	private static final String MESSAGE_LTR_BIDI = Messages
			.getString("WizardTemplateChoicePage.label.ltrBiDiOrientation"); //$NON-NLS-1$ )
	private static final String MESSAGE_CHOOSE_BIDI_DIR = Messages
			.getString("WizardTemplateChoicePage.label.chooseBiDiDirection"); //$NON-NLS-1$ )
	public final int LTR_DIRECTION_INDX = 0;
	public final int RTL_DIRECTION_INDX = 1;
	private Label directionLabel;
	private Combo directionCombo;
	boolean isLTRDirection = ReportPlugin.getDefault().getLTRReportDirection();
	private int predefinedCount;
	private ExtensionTemplateListProvider provider;

	// bidi_hcg end

	private boolean isModified = false;

	public void setLTRDirection(boolean isLTRDirection) {
		if (!isModified) {
			this.isLTRDirection = isLTRDirection;
			reSelectDirectionCombo();
		}
	}

	private Label description;

	Image thumbnailImage;

	protected java.util.List<ReportDesignHandle> templates = new ArrayList<ReportDesignHandle>();

	// protected int selectedIndex;

	protected Map<String, Image> imageMap;

	private Composite previewPane;

	private TreeViewer templateList;

	private static final Object TreeRoot = new Object();
	private static final String RootDisplayName = Messages.getString("WizardTemplateChoicePage.RootDisplayName"); //$NON-NLS-1$

	/**
	 * @param pageName
	 */
	public WizardTemplateChoicePage(String pageName) {
		super(pageName);

		provider = new ExtensionTemplateListProvider(this);

		imageMap = new HashMap<String, Image>();

		if (UIUtil.getFragmentDirectory() == null) {
			return;
		}
		ReportDesignHandle[] predefinedTemplateArray = getAllTemplates(UIUtil.getFragmentDirectory(), "/templates/"); //$NON-NLS-1$
		SortPredefinedTemplates(predefinedTemplateArray);
		if (predefinedTemplateArray != null && predefinedTemplateArray.length > 0) {
			templates.addAll(Arrays.asList(predefinedTemplateArray));
		}

	}

	protected ReportDesignHandle[] getAllTemplates(String root) {
		return getAllTemplates(root, null);
	}

	protected ReportDesignHandle[] getAllTemplates(String root, String path) {

		if (root == null || root.trim().length() <= 0) {
			return null;
		}
		ReportDesignHandle[] templateArray = null;

		File templateDirectory = null;
		if (path == null) {
			templateDirectory = new File(root, File.separator);
		} else {
			templateDirectory = new File(root, path + File.separator);
		}

		if (templateDirectory.isDirectory()) {
			if (!templateDirectory.exists()) {
				boolean createSucc = templateDirectory.mkdirs();
				if (!createSucc) {
					return new ReportDesignHandle[0];
				}
			}
			File[] filesArray = templateDirectory.listFiles(new FilenameFilter() {

				public boolean accept(File dir, String name) {
					return name.endsWith(".rpttemplate");//$NON-NLS-1$
				}
			});

			java.util.List<ReportDesignHandle> reportDesingHandleList = new ArrayList<ReportDesignHandle>();
			for (int i = 0; i < filesArray.length; i++) {
				try {
					ModuleHandle moduleHandle = SessionHandleAdapter.getInstance().getSessionHandle()
							.openModule(filesArray[i].getAbsolutePath());
					// templateArray[i] = reportDesignHandle;
					if (moduleHandle != null && moduleHandle instanceof ReportDesignHandle) {
						reportDesingHandleList.add((ReportDesignHandle) moduleHandle);
					}
				} catch (Exception e) {
					// ignore
				}
			}

			int count = reportDesingHandleList.size();
			templateArray = new ReportDesignHandle[count];
			for (int i = 0; i < count; i++) {
				templateArray[i] = reportDesingHandleList.get(i);
			}

		}

		return templateArray;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		UIUtil.bindHelp(composite, IHelpContextIds.NEW_REPORT_COPY_WIZARD_ID);

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.marginHeight = 10;
		gridLayout.marginWidth = 10;
		gridLayout.horizontalSpacing = 10;
		gridLayout.verticalSpacing = 10;
		composite.setLayout(gridLayout);

		Label label0 = new Label(composite, SWT.NONE);
		label0.setText(MESSAGE_REPORT_TEMPLATES);

		Label previewLabel = new Label(composite, SWT.NONE);
		previewLabel.setText(MESSAGE_PREVIEW);
		GridData data = new GridData(GridData.BEGINNING);
		previewLabel.setLayoutData(data);

		templateList = new TreeViewer(composite, SWT.BORDER | SWT.SINGLE);

		predefinedCount = templates.size();

		createCustomTemplateList();

		data = new GridData(GridData.BEGINNING | GridData.FILL_VERTICAL);
		data.widthHint = 250;
		data.heightHint = 300;
		templateList.getTree().setLayoutData(data);

		previewPane = new Composite(composite, 0);
		data = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
		previewPane.setLayoutData(data);
		gridLayout = new GridLayout();
		gridLayout.verticalSpacing = 10;
		previewPane.setLayout(gridLayout);

		Composite previewComposite = new Composite(previewPane, SWT.BORDER);
		data = new GridData(GridData.BEGINNING);
		data.heightHint = 229;
		data.widthHint = 184;
		previewComposite.setLayoutData(data);
		previewComposite.setLayout(new FormLayout());

		previewCanvas = new ImageCanvas(previewComposite);
		FormData formData = new FormData(184, 229);
		formData.left = new FormAttachment(previewComposite);
		formData.top = new FormAttachment(previewComposite);
		previewCanvas.setLayoutData(formData);

		Label descriptionTitle = new Label(previewPane, SWT.NONE);
		descriptionTitle.setText(MESSAGE_DESCRIPTION);
		data = new GridData(GridData.FILL_HORIZONTAL);
		descriptionTitle.setLayoutData(data);

		description = new Label(previewPane, SWT.WRAP);

		data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = 184;
		data.horizontalIndent = 20;
		description.setLayoutData(data);

		new Label(previewPane, SWT.NONE);

		chkBox = new Button(composite, SWT.CHECK);
		chkBox.setText(MESSAGE_SHOW_CHEATSHEET);
		chkBox.setSelection(ReportPlugin.readCheatSheetPreference());
		chkBox.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				ReportPlugin.writeCheatSheetPreference(chkBox.getSelection());
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				ReportPlugin.writeCheatSheetPreference(chkBox.getSelection());
			}
		});
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		chkBox.setLayoutData(data);

		defaultLibraryHandle = getDefaultLibraryHandleFromResource();

		String value = ReportPlugin.getDefault().getPreferenceStore()
				.getString(ReportPlugin.LIBRARY_DEFAULT_THEME_ENABLE);

		if (defaultLibraryHandle != null && ENABLED.equals(value)) {
			String state = ReportPlugin.getDefault().getPreferenceStore()
					.getString(ReportPlugin.LIBRARY_DEFAULT_THEME_INCLUDE);

			useDefaultLibraryBox = new Button(composite, SWT.CHECK);
			useDefaultLibraryBox.setText(Messages.getString("WizardTemplateChoicePage.label.EnableDefaultThemes")); //$NON-NLS-1$
			useDefaultLibraryBox.setSelection(ENABLED.equals(state));
		}

		// bidi_hcg start
		/*
		 * if BiDi support is enabled - a combobox with BiDi Orientation choices will be
		 * added to wizard page
		 */

		// addBidiPart( composite );

		// bidi_hcg end
		hookListeners();

		initTreeViewer();

		if (templates.size() > 0) {
			templateList.setSelection(new StructuredSelection(templates.get(0)));
			setPageComplete(true);
		} else {
			setPageComplete(false);
		}
		// templateListener.selectionChanged( null );

		setControl(composite);

	}

	private void addBidiPart(Composite composite) {
		new Label(composite, SWT.NONE);

		Composite bidiComposite = new Composite(composite, SWT.NONE);
		GridData bidiGridData = new GridData(GridData.FILL_HORIZONTAL);
		bidiGridData.horizontalSpan = 2;
		bidiComposite.setLayoutData(bidiGridData);

		GridLayout bidiGridLayout = new GridLayout();
		bidiGridLayout.numColumns = 3;
		bidiGridLayout.marginHeight = 10;
		bidiGridLayout.marginWidth = 5;
		bidiGridLayout.horizontalSpacing = 5;
		bidiGridLayout.verticalSpacing = 10;
		bidiGridLayout.makeColumnsEqualWidth = false;
		bidiComposite.setLayout(bidiGridLayout);

		bidiGridData = new GridData();
		directionLabel = new Label(bidiComposite, SWT.NONE);
		directionLabel.setText(MESSAGE_CHOOSE_BIDI_DIR);
		directionLabel.setLayoutData(bidiGridData);

		directionCombo = new Combo(bidiComposite, SWT.DROP_DOWN | SWT.READ_ONLY);
		directionCombo.add(MESSAGE_LTR_BIDI, LTR_DIRECTION_INDX);
		directionCombo.add(MESSAGE_RTL_BIDI, RTL_DIRECTION_INDX);
		bidiGridData = new GridData();
		bidiGridData.grabExcessHorizontalSpace = true;
		bidiGridData.widthHint = 200;
		bidiGridData.horizontalIndent = 20;
		directionCombo.setLayoutData(bidiGridData);
		directionCombo.setVisibleItemCount(30);
		reSelectDirectionCombo();
		directionCombo.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				if (directionCombo.getSelectionIndex() == LTR_DIRECTION_INDX)
					isLTRDirection = true;
				else
					isLTRDirection = false;

				isModified = true;
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

	}

	private void reSelectDirectionCombo() {
		if (directionCombo != null)
			directionCombo.select(isLTRDirection ? LTR_DIRECTION_INDX : RTL_DIRECTION_INDX);
	}

	private void createCustomTemplateList() {

		String templateRoot = ReportPlugin.getDefault().getTemplatePreference();

		if (templateRoot == null || templateRoot.trim().length() == 0) {
			return;
		}

		// If the custom template folder is the same with predefined folder,
		// then return
		File preTemplateDirectory = new File(UIUtil.getFragmentDirectory(), "/templates/"); //$NON-NLS-1$
		File cusTemplateDirectory = new File(templateRoot.trim());
		if (preTemplateDirectory != null && cusTemplateDirectory != null
				&& preTemplateDirectory.equals(cusTemplateDirectory)) {
			return;
		}

		ReportDesignHandle[] customTmplateArray = getAllTemplates(templateRoot.trim());
		if (customTmplateArray != null) {
			templates.addAll(Arrays.asList(customTmplateArray));
		}

	}

	private void hookListeners() {

		// templateList.addListener( SWT.Selection, templateListener );
	}

	private ISelectionChangedListener templateListener = new ISelectionChangedListener() {

		public void selectionChanged(SelectionChangedEvent event) {
			ReportDesignHandle handle = getSelectionHandle();
			processSelectionReportDesignHandle(handle);

		}
	};

	private void processSelectionReportDesignHandle(ReportDesignHandle handle) {
		if (handle == null) {
			previewCanvas.clear();
			description.setText(""); //$NON-NLS-1$
			chkBox.setSelection(false);
			chkBox.setEnabled(false);
			setPageComplete(false);
			if (directionCombo != null) {
				directionCombo.setEnabled(false);
			}
			return;
		}
		if (directionCombo != null) {
			directionCombo.setEnabled(true);
		}
		String ReprotDescription = handle.getDescription();
		if (ReprotDescription != null && ReprotDescription.trim().length() != 0) {
			if (isPredifinedTemplate(handle.getFileName())) {
				description.setText(Messages.getString(ReprotDescription));
			} else {
				description.setText(Messages.getString(ReprotDescription));
			}
		} else {
			description.setText(""); //$NON-NLS-1$
		}

		// we need to relayout if the new text has different number of lines
		previewPane.layout();

		String key = handle.getIconFile();
		if (key != null && key.trim().length() != 0 && checkExtensions(key) == false) {
			key = null;
		}

		Image img = null;

		if (handle.getThumbnail() != null && handle.getThumbnail().length != 0) {
			byte[] thumbnailData = handle.getThumbnail();
			ByteArrayInputStream inputStream = new ByteArrayInputStream(thumbnailData);
			if (thumbnailImage != null) {
				thumbnailImage.dispose();
				thumbnailImage = null;
			}
			thumbnailImage = new Image(null, inputStream);

			previewCanvas.clear();
			previewCanvas.loadImage(thumbnailImage);
		} else if ((key != null) && (!"".equals(key.trim()))) //$NON-NLS-1$
		{
			URL url = getPreviewImageURL(handle.getFileName(), key);

			if (url != null) {
				try {
					key = FileLocator.resolve(url).getPath();
				} catch (IOException e) {
					logger.log(Level.SEVERE, e.getMessage(), e);
				}
				img = imageMap.get(key);

				if (img == null) {
					try {
						url = new URL("file://" + key); //$NON-NLS-1$
						img = ImageManager.getInstance().loadImage(url);
					} catch (IOException e) {
						logger.log(Level.SEVERE, e.getMessage(), e);
					}
					if (img != null) {
						imageMap.put(key, img);
					}

				}
				if (img != null) {
					previewCanvas.clear();
					previewCanvas.loadImage(img);
					// previewCanvas.showOriginal( );
				}

			} else {
				key = null;
			}

		}

		if ((handle.getThumbnail() == null || handle.getThumbnail().length == 0) && key == null) {
			if (thumbnailImage != null) {
				thumbnailImage.dispose();
				thumbnailImage = null;
			}

			Rectangle rect = previewCanvas.getBounds();

			thumbnailImage = new Image(null, rect.width, rect.height);

			ReportGraphicsViewPainter painter = new ReportGraphicsViewPainter(handle);

			painter.paint(thumbnailImage, previewCanvas.getDisplay(), rect);

			painter.dispose();

			previewCanvas.clear();
			previewCanvas.loadImage(thumbnailImage);
		}

		if (handle.getCheatSheet() != null && handle.getCheatSheet().trim().length() != 0) {
			chkBox.setEnabled(!(handle.getCheatSheet().equals("") || handle.getCheatSheet() //$NON-NLS-1$
					.equals("org.eclipse.birt.report.designer.ui.cheatsheet.firstreport"))); //$NON-NLS-1$
			// if ( handle.getCheatSheet( )
			// .equals(
			// "org.eclipse.birt.report.designer.ui.cheatsheet.firstreport"
			// ) )
			// {
			// chkBox.setSelection( true );
			// }
			chkBox.setSelection(true);
		} else {
			chkBox.setSelection(false);
			chkBox.setEnabled(false);
		}
		setPageComplete(true);
	}

	/**
	 * @return Returns the templates of selected item.
	 */
	public ReportDesignHandle getTemplate() {
		return getSelectionHandle();
	}

	// /**
	// * @return Returns the blank report template.
	// */
	// public ReportDesignHandle getBlankTemplate( )
	// {
	// if ( templates.size( ) == 0 )
	// {
	// return null;
	// }
	// return (ReportDesignHandle) templates.get( 0 );
	// }

	/**
	 * @return true if show CheatSheets is checked.
	 */
	public boolean getShowCheatSheet() {
		ReportDesignHandle handle = getSelectionHandle();
		if (handle != null) {
			if (handle.getCheatSheet() != null
					&& handle.getCheatSheet().equals("org.eclipse.birt.report.designer.ui.cheatsheet.firstreport")) //$NON-NLS-1$
			{
				return true;
			}
		}
		return chkBox.getSelection();
	}

	public void dispose() {
		super.dispose();
		for (Iterator<ReportDesignHandle> it = templates.iterator(); it.hasNext();) {
			Object item = it.next();
			if (item instanceof ReportDesignHandle) {
				((ReportDesignHandle) item).close();
			}

		}
		if (thumbnailImage != null) {
			thumbnailImage.dispose();
			thumbnailImage = null;
		}

		if (defaultLibraryHandle != null) {
			defaultLibraryHandle.close();
			defaultLibraryHandle = null;
		}
	}

	/*
	 * @see DialogPage.setVisible(boolean)
	 */
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			getControl().setFocus();
		}
	}

	private boolean isPredifinedTemplate(String sourceFileName) {
		String predifinedDir = UIUtil.getFragmentDirectory();
		if (predifinedDir == null || predifinedDir.length() <= 0) {
			return false;
		}
		File predifinedFile = new File(predifinedDir);
		File sourceFile = new File(sourceFileName);
		if (sourceFile.getAbsolutePath().startsWith(predifinedFile.getAbsolutePath())) {
			return true;
		}
		return false;
	}

	private void SortPredefinedTemplates(ReportDesignHandle[] predefinedTemplateArray) {

		if (predefinedTemplateArray == null || predefinedTemplateArray.length <= 1) {
			return;
		}

		final String[] predefinedTemplateFileName = { "blank_report.rpttemplate", //$NON-NLS-1$
				"my_first_report.rpttemplate", //$NON-NLS-1$
				"simple_listing.rpttemplate", //$NON-NLS-1$
				"grouped_listing.rpttemplate", //$NON-NLS-1$
				"grouped_listing_column_heading.rpttemplate", //$NON-NLS-1$
				"dual_column_listing.rpttemplate", //$NON-NLS-1$
				"chart_listing.rpttemplate", //$NON-NLS-1$
				"dual_column_chart_listing.rpttemplate", //$NON-NLS-1$
				"sidebyside_chart_listing.rpttemplate", //$NON-NLS-1$
		};

		int predefinedTemplateCount = predefinedTemplateFileName.length;
		ReportDesignHandle swapHandle = null;
		String templateName = null;
		int index = 0;
		for (int i = 0; i < predefinedTemplateCount; i++) {
			templateName = predefinedTemplateFileName[i];
			for (int j = index; j < predefinedTemplateArray.length; j++) {
				if (predefinedTemplateArray[j].getFileName().endsWith(templateName)) {
					if (index != j) {
						swapHandle = predefinedTemplateArray[j];
						predefinedTemplateArray[j] = predefinedTemplateArray[index];
						predefinedTemplateArray[index] = swapHandle;
					}
					index++;
					break;
				}
			}
		}

	}

	private URL getPreviewImageURL(String reportFileName, String key) {
		URL url = null;

		Bundle bundle = EclipseUtil.getBundle(IResourceLocator.FRAGMENT_RESOURCE_HOST);
		if (bundle == null) {
			return null;
		}
		url = bundle.getResource(key);

		if (url == null) {
			String path = ReportPlugin.getDefault().getResourceFolder();

			url = resolveURL(new File(path, key));

			if (url == null) {
				url = resolveURL(new File(key));
			}
		}

		return url;
	}

	private URL resolveURL(File file) {
		if (file.exists() && file.isFile()) {
			try {
				return file.toURL();
			} catch (MalformedURLException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
		}

		return null;
	}

	private boolean checkExtensions(String fileName) {
		for (int i = 0; i < IMAGE_TYPES.length; i++) {
			if (fileName.toLowerCase().endsWith(IMAGE_TYPES[i])) {
				return true;
			}
		}
		return false;
	}

	// bidi_hcg start
	public boolean isLTRDirection() {
		return isLTRDirection;
	}

	// bidi_hcg end

	private void initTreeViewer() {
		TemplateListProvider treeProvider = new TemplateListProvider();
		templateList.setLabelProvider(treeProvider);
		templateList.setContentProvider(treeProvider);
		templateList.addSelectionChangedListener(templateListener);

		Object[] objs = provider.getRootElements();
		Object[] roots = new Object[1 + objs.length];
		roots[0] = TreeRoot;

		System.arraycopy(objs, 0, roots, 1, objs.length);
		templateList.setInput(roots);

		templateList.expandAll();
	}

	public void contentChanged() {
		if (templateList != null && templateList.getTree() != null && !templateList.getTree().isDisposed()) {
			ISelection oldSel = templateList.getSelection();

			// reconstruct the input
			Object[] objs = provider.getRootElements();
			Object[] roots = new Object[1 + objs.length];
			roots[0] = TreeRoot;

			System.arraycopy(objs, 0, roots, 1, objs.length);
			templateList.setInput(roots);

			// TODO better state recovering

			templateList.expandToLevel(2);
			templateList.setSelection(oldSel);
		}
	}

	private ReportDesignHandle getSelectionHandle() {
		IStructuredSelection selection = (IStructuredSelection) templateList.getSelection();
		List<?> list = selection.toList();
		if (list.size() != 1) {
			return null;
		}

		Object data = list.get(0);
		if (data instanceof ReportDesignHandle) {
			return (ReportDesignHandle) data;
		}
		ReportDesignHandle handle = provider.getReportDesignHandle(data);

		return handle;
	}

	private class TemplateListProvider implements ILabelProvider, ITreeContentProvider {

		public Image getImage(Object element) {
			if (element instanceof ReportDesignHandle) {
				return ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_TEMPLATE_FILE);
			} else if (element == TreeRoot) {
				return ReportPlatformUIImages.getImage(ISharedImages.IMG_OBJ_FOLDER);
			}
			Image image = provider.getImage(element);
			return image;
		}

		public String getText(Object element) {
			String displayName = null;
			if (element instanceof ReportDesignHandle) {
				if (templates.contains(element)) {
					int index = templates.indexOf(element);

					if (index + 1 <= predefinedCount) {
						displayName = ((ReportDesignHandle) element).getDisplayName();
						if (displayName != null) {
							displayName = Messages.getString(displayName);
						} else
						// == null
						{
							displayName = Messages.getString(((ReportDesignHandle) element).getFileName());
						}
					} else {
						displayName = (((ReportDesignHandle) element).getDisplayName());
					}
				} else {
					displayName = (((ReportDesignHandle) element).getDisplayName());
				}
				return displayName;
			} else if (element == TreeRoot) {
				return RootDisplayName;
			}

			displayName = provider.getText(element);
			if (displayName == null) {
				displayName = element.toString();
			}
			return displayName;
		}

		public void addListener(ILabelProviderListener listener) {
			// do nothing

		}

		public void dispose() {
			provider.dispose();
		}

		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		public void removeListener(ILabelProviderListener listener) {

		}

		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof Object[]) {
				return (Object[]) parentElement;
			} else if (parentElement == TreeRoot) {
				return templates.toArray();
			}
			Object[] objs = provider.getChildren(parentElement);
			if (objs == null) {
				objs = new Object[0];
			}
			return objs;
		}

		public Object getParent(Object element) {
			return null;
		}

		public boolean hasChildren(Object element) {
			return getChildren(element).length != 0;
		}

		public Object[] getElements(Object inputElement) {
			return getChildren(inputElement);
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// do nothing

		}

	}

	private LibraryHandle getDefaultLibraryHandleFromResource() {
		return DEUtil.getDefaultLibraryHandle();
	}

	public boolean isUseDefaultLibrary() {
		if (useDefaultLibraryBox == null) {
			return false;
		}
		return useDefaultLibraryBox.getSelection();
	}

	public LibraryHandle getDefaultLibraryHandle() {
		return defaultLibraryHandle;
	}

}
