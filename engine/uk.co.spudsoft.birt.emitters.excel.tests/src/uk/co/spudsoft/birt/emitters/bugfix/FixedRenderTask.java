/*************************************************************************************
 * Copyright (c) 2011, 2012, 2013 James Talbut.
 *  jim-emitters@spudsoft.co.uk
 *
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     James Talbut - Initial implementation.
 ************************************************************************************/

package uk.co.spudsoft.birt.emitters.bugfix;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.engine.api.IHTMLRenderOption;
import org.eclipse.birt.report.engine.api.IPDFRenderOption;
import org.eclipse.birt.report.engine.api.IProgressMonitor;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.ITOCTree;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.api.impl.EngineTask;
import org.eclipse.birt.report.engine.api.impl.IInternalReportDocument;
import org.eclipse.birt.report.engine.api.impl.LogicalPageSequence;
import org.eclipse.birt.report.engine.api.impl.PageSequenceParse;
import org.eclipse.birt.report.engine.api.impl.ReportEngine;
import org.eclipse.birt.report.engine.api.impl.VisiblePageFilter;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.EngineExtensionManager;
import org.eclipse.birt.report.engine.executor.IReportExecutor;
import org.eclipse.birt.report.engine.executor.PageVariable;
import org.eclipse.birt.report.engine.executor.ReportExtensionExecutor;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.extension.engine.IContentProcessor;
import org.eclipse.birt.report.engine.extension.engine.IRenderExtension;
import org.eclipse.birt.report.engine.extension.internal.ExtensionManager;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.internal.document.PageHintReader;
import org.eclipse.birt.report.engine.internal.document.ReportPageExecutor;
import org.eclipse.birt.report.engine.internal.document.ReportletExecutor;
import org.eclipse.birt.report.engine.internal.document.v4.PageRangeIterator;
import org.eclipse.birt.report.engine.internal.executor.dup.SuppressDuplciateReportExecutor;
import org.eclipse.birt.report.engine.internal.executor.l18n.LocalizedReportExecutor;
import org.eclipse.birt.report.engine.ir.MasterPageDesign;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.layout.IReportLayoutEngine;
import org.eclipse.birt.report.engine.layout.html.HTMLReportLayoutEngine;
import org.eclipse.birt.report.engine.nLayout.LayoutEngine;
import org.eclipse.birt.report.engine.parser.ReportParser;
import org.eclipse.birt.report.engine.presentation.IPageHint;
import org.eclipse.birt.report.engine.toc.ITOCReader;
import org.eclipse.birt.report.engine.toc.ITreeNode;
import org.eclipse.birt.report.engine.toc.TOCView;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

@SuppressWarnings("restriction")
public class FixedRenderTask extends EngineTask implements IRenderTask {

	protected IReportDocument reportDocument;
	protected IReportRunnable reportRunnable;
	protected InnerRender innerRender;
	protected long outputPageCount;

	protected ITOCReader tocReader;
	protected boolean designLoaded = false;
	protected boolean variablesLoaded = false;

	// the flag of render page by page
	protected boolean PDFRenderPageByPage = true;

	/**
	 * @param engine    the report engine
	 * @param reportDoc the report document instance
	 */
	public FixedRenderTask(ReportEngine engine, IReportDocument reportDocument) {
		this(engine, null, reportDocument);
	}

	/**
	 * @param engine    the report engine
	 * @param runnable  the report runnable object
	 * @param reportDoc the report document instance
	 */
	public FixedRenderTask(ReportEngine engine, IReportRunnable runnable, IReportDocument reportDoc) {
		super(engine, IEngineTask.TASK_RENDER);
		this.reportDocument = reportDoc;
		this.reportRunnable = runnable;
		initRenderTask();
	}

	protected void initRenderTask() {
		executionContext.setFactoryMode(false);
		executionContext.setPresentationMode(true);

		executionContext.setReportDocument(reportDocument);

		assert (reportDocument instanceof IInternalReportDocument);
		IInternalReportDocument internalReportDoc = (IInternalReportDocument) reportDocument;
		if (reportDocument != null && reportRunnable == null) {
			// load the report runnable from the document
			IReportRunnable documentRunnable = getOnPreparedRunnable(reportDocument);
			setReportRunnable(documentRunnable);
			Report reportIR = internalReportDoc.getReportIR((ReportDesignHandle) documentRunnable.getDesignHandle());
			executionContext.setReport(reportIR);
		} else {
			// the report runnable is set by the user
			setReportRunnable(reportRunnable);
			Report reportIR = new ReportParser().parse((ReportDesignHandle) reportRunnable.getDesignHandle());
			executionContext.setReport(reportIR);
		}

	}

	/**
	 * Loads parameters and global variables from report document. Since the
	 * application context is not available and application class loader can't be
	 * created when render task is initialized, loading parameters and global
	 * variables from document must be deferred until application context is
	 * available.
	 */
	protected void loadDocument() {
		if (!variablesLoaded) {
			IInternalReportDocument documentReader = (IInternalReportDocument) reportDocument;
			try {
				// load the information from the report document
				ClassLoader classLoader = executionContext.getApplicationClassLoader();
				setParameters(documentReader.loadParameters(classLoader));
				usingParameterValues();
				executionContext.registerGlobalBeans(documentReader.loadVariables(classLoader));
				tocReader = documentReader.getTOCReader(classLoader);
			} catch (EngineException e) {
				log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
			variablesLoaded = true;
		}
	}

	protected void loadReportVariable() throws IOException {
		PageHintReader hintsReader = new PageHintReader(reportDocument);
		try {
			// load the report variables
			Collection<PageVariable> vars = hintsReader.getPageVariables();
			if (vars != null) {
				executionContext.addPageVariables(vars);
			}
		} finally {
			hintsReader.close();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.IRenderTask#render(long)
	 */
	@Override
	public void render(long pageNumber) throws EngineException {
		setPageNumber(pageNumber);
		render();
	}

	@Override
	public void close() {
		designLoaded = false;
		unloadRenderExtensions();
		unloadVisiblePages();
		if (tocReader != null) {
			try {
				tocReader.close();
			} catch (IOException ignored) {
			}
		}
		super.close();
	}

	@Override
	public void render(String pageRange) throws EngineException {
		setPageRange(pageRange);
		render();
	}

	@Override
	public void render(InstanceID iid) throws EngineException {
		setInstanceID(iid);
		render();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.IRenderTask#render()
	 */
	@Override
	public void render() throws EngineException {
		if (progressMonitor != null) {
			progressMonitor.onProgress(IProgressMonitor.START_TASK, TASK_RENDER);
		}
		try {
			switchToOsgiClassLoader();
			changeStatusToRunning();
			if (renderOptions == null) {
				throw new EngineException(MessageConstants.RENDER_OPTION_ERROR); // $NON-NLS-1$
			}
			loadDocument();
			loadReportVariable();
			IReportRunnable runnable = executionContext.getRunnable();
			if (runnable == null) {
				throw new EngineException(MessageConstants.REPORT_DESIGN_NOT_FOUND_ERROR,
						new Object[] { reportDocument.getName() });
			}

			if (!designLoaded) {
				// load report design
				loadDesign();
				// synchronize the design ir's version with the document
				String version = reportDocument.getVersion();
				Report report = executionContext.getReport();
				report.updateVersion(version);

				designLoaded = true;
			}

			updateRtLFlag();

			ReportDesignHandle design = executionContext.getReportDesign();
			if (DesignChoiceConstants.REPORT_LAYOUT_PREFERENCE_FIXED_LAYOUT.equals(design.getLayoutPreference())) {
				executionContext.setFixedLayout(true);
			}

			if (innerRender == null) {
				innerRender = new PageRangeRender(new long[] { 1, getTotalPage() });
			}

			innerRender.render();
		} catch (EngineException e) {
			log.log(Level.SEVERE, "An error happened while running the report. Cause:", e); //$NON-NLS-1$
			throw e;
		} catch (Exception ex) {
			log.log(Level.SEVERE, "An error happened while running the report. Cause:", ex); //$NON-NLS-1$
			throw new EngineException(MessageConstants.REPORT_RUN_ERROR, ex); // $NON-NLS-1$
		} catch (OutOfMemoryError err) {
			log.log(Level.SEVERE, "There is insufficient memory to execute this report."); //$NON-NLS-1$
			throw err;
		} catch (Throwable t) {
			log.log(Level.SEVERE, "Error happened while running the report.", t); //$NON-NLS-1$
			throw new EngineException(MessageConstants.REPORT_RUN_ERROR, t); // $NON-NLS-1$
		} finally {
			changeStatusToStopped();
			switchClassLoaderBack();
			if (progressMonitor != null) {
				progressMonitor.onProgress(IProgressMonitor.END_TASK, TASK_RENDER);
			}
		}
	}

	@Override
	public long getPageCount() throws EngineException {
		if (runningStatus != STATUS_SUCCEEDED) {
			throw new EngineException(MessageConstants.RENDERTASK_NOT_FINISHED_ERROR);
		}
		return outputPageCount;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.IRenderTask#render()
	 */
	@Override
	public void setPageNumber(long pageNumber) throws EngineException {
		long totalVisiblePageCount = getTotalPage();
		if (pageNumber <= 0 || pageNumber > totalVisiblePageCount) {
			throw new EngineException(MessageConstants.PAGE_NOT_FOUND_ERROR, Long.valueOf(pageNumber));
		}
		innerRender = new PageRangeRender(new long[] { pageNumber, pageNumber });
	}

	@Override
	public void setInstanceID(String iid) throws EngineException {
		setInstanceID(InstanceID.parse(iid));
	}

	@Override
	public void setInstanceID(InstanceID iid) throws EngineException {
		long offset = reportDocument.getInstanceOffset(iid);
		if (offset == -1) {
			throw new EngineException(MessageConstants.INVALID_INSTANCE_ID_ERROR, iid);
		}

		innerRender = new ReportletRender(offset);
	}

	@Override
	public void setReportlet(String bookmark) throws EngineException {
		long offset = reportDocument.getBookmarkOffset(bookmark);
		if (offset == -1) {
			throw new EngineException(MessageConstants.INVALID_BOOKMARK_ERROR, bookmark);
		}

		innerRender = new ReportletRender(offset);
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setPageRange(String pageRange) throws EngineException {
		long totalVisiblePageCount = FixedRenderTask.this.getTotalPage();
		List list = PageSequenceParse.parsePageSequence(pageRange, totalVisiblePageCount);
		innerRender = new PageRangeRender(list);
	}

	@Override
	public void setBookmark(String bookmark) throws EngineException {
		long pageNumber = getPageNumber(bookmark);
		if (pageNumber <= 0) {
			throw new EngineException(MessageConstants.BOOKMARK_NOT_FOUND_ERROR, bookmark); // $NON-NLS-1$
		}
		innerRender = new PageRangeRender(new long[] { pageNumber, pageNumber });
	}

	private interface InnerRender {

		void render() throws Exception;
	}

	/**
	 * Renders a range of pages.
	 */
	protected class PageRangeRender implements InnerRender {

		protected ArrayList<long[]> pageSequences;

		public PageRangeRender(long[] arrayRange) {
			this.pageSequences = new ArrayList<>();
			pageSequences.add(arrayRange);
		}

		public PageRangeRender(List<long[]> pageRange) {
			this.pageSequences = new ArrayList<>(pageRange);
		}

		/**
		 * @return true if BIRT render the report page by page. false if BIRT render the
		 *         report as a whole page.
		 */
		protected boolean needPagedExecutor(List<long[]> pageSequences) {
			if (ExtensionManager.PAPER_SIZE_PAGINATION.equals(pagination)) {
				/*
				 * if fixed-layout, need render page by page, or pagination may different with
				 * html in the following case: 1. element is set visibility to false in pdf
				 * format 2. element is set display to none
				 */
				if (FixedRenderTask.this.PDFRenderPageByPage && executionContext.isFixedLayout()) {
					return true;
				}

				// the output pages is sequential or there is no page sequence,
				// in this case, we can output the report content as a whole and
				// the HTML layout engine may re-paginate the content into
				// pages.
				if (pageSequences == null) {
					return false;
				}
				if (pageSequences != null && pageSequences.size() == 1) {
					long[] pages = pageSequences.get(0);
					if (pages[0] == 1 && pages[1] == reportDocument.getPageCount()) {
						return false;
					}
				}
				// the page sequence is defined by several segment, we can't
				// display the report as a whole as in this case the HTML layout
				// engine can't regenerate the pagination.
				return true;
			}
			int pageCount = getPageCount();
			if (pageCount == 1) {
				return true;
			}
			IRenderOption renderOption = executionContext.getRenderOption();
			HTMLRenderOption htmlRenderOption = new HTMLRenderOption(renderOption);
			boolean htmlPagination = htmlRenderOption.getHtmlPagination();
			if (!htmlPagination) {
				return false;
			}
			return true;
		}

		protected int getPageCount() {
			int pageCount = 0;
			for (long[] pageSeg : pageSequences) {
				long start = pageSeg[0];
				long end = pageSeg[1];
				pageCount += (end - start) + 1;
			}
			return pageCount;
		}

		protected IPageHint getPageHint(ReportPageExecutor executor, long pageNumber) {
			try {
				return executor.getLayoutPageHint(pageNumber);
			} catch (IOException ex) {
				executionContext.addException(new EngineException(MessageConstants.PAGE_HINT_LOADING_ERROR, ex));
				return null;
			}
		}

		@SuppressWarnings("deprecation")
		protected void supportHtmlPagination() {
			if (ExtensionManager.PAPER_SIZE_PAGINATION.equals(pagination)) {
				Object htmlPaginationObj = renderOptions.getOption(IHTMLRenderOption.HTML_PAGINATION);
				if (htmlPaginationObj instanceof Boolean) {
					boolean htmlPagination = ((Boolean) htmlPaginationObj).booleanValue();
					if (htmlPagination) {
						if (renderOptions.getOption(IPDFRenderOption.FIT_TO_PAGE) == null) {
							renderOptions.setOption(IPDFRenderOption.FIT_TO_PAGE, Boolean.TRUE);
						}
						renderOptions.setOption(IPDFRenderOption.PAGEBREAK_PAGINATION_ONLY, Boolean.TRUE);
					}
				}
			}
		}

		@Override
		public void render() throws Exception {
			// start the render
			setupRenderOption();
			IContentEmitter emitter = createContentEmitter();
			supportHtmlPagination();

			// setup the page sequences
			List<long[]> physicalPageSequences = getPhysicalPageSequence(pageSequences);
			long filteredTotalPage = getTotalPage();
			long totalPage = reportDocument.getPageCount();
			if (filteredTotalPage != totalPage) {
				executionContext.setFilteredTotalPage(filteredTotalPage);
			}
			PageRangeIterator iter = new PageRangeIterator(physicalPageSequences);

			initializeContentEmitter(emitter);
			boolean paged = needPagedExecutor(physicalPageSequences);

			// prepare the executor and emitter
			ReportPageExecutor pagesExecutor = new ReportPageExecutor(executionContext, physicalPageSequences, paged);

			IReportExecutor executor = createRenderExtensionExecutor(pagesExecutor);
			executor = new SuppressDuplciateReportExecutor(executor);
			executor = new LocalizedReportExecutor(executionContext, executor);
			executionContext.setExecutor(executor);

			// prepare the layout engine
			IReportLayoutEngine layoutEngine = createReportLayoutEngine(pagination, renderOptions);

			layoutEngine.setLocale(executionContext.getLocale());

			ReportDesignHandle design = executionContext.getReportDesign();
			if (DesignChoiceConstants.REPORT_LAYOUT_PREFERENCE_FIXED_LAYOUT.equals(design.getLayoutPreference())) {
				((HTMLReportLayoutEngine) layoutEngine).getContext().setFixedLayout(true);
			}
			if (ExtensionManager.PAPER_SIZE_PAGINATION.equals(pagination)) {
				LayoutEngine pdfEmitter = new LayoutEngine(((HTMLReportLayoutEngine) layoutEngine).getContext(),
						emitter, renderOptions, executionContext, getDocumentTotalPage());

				emitter = pdfEmitter;
				initializeContentEmitter(emitter);
			} else {
			}

			startRender();
			IReportContent report = executor.execute();
			emitter.start(report);
			layoutEngine.setTotalPageCount(getTotalPage());

			if (ExtensionManager.PAPER_SIZE_PAGINATION.equals(pagination)) {
				if (!paged) {
					long pageNumber = iter.next();
					if (pageNumber != 1) {
						layoutEngine.setLayoutPageHint(getPageHint(pagesExecutor, pageNumber));
					}
					setFilteredPageNumber(filteredTotalPage, totalPage, pageNumber);
					layoutEngine.layout(executor, report, emitter, true);
				} else {
					while (iter.hasNext()) {
						long pageNumber = iter.next();
						IPageHint pageHint = getPageHint(pagesExecutor, pageNumber);
						layoutEngine.setLayoutPageHint(pageHint);
						// here the pageExecutor will returns a report.root.
						IReportItemExecutor pageExecutor = executor.getNextChild();
						if (pageExecutor != null) {
							setFilteredPageNumber(filteredTotalPage, totalPage, pageNumber);
							IReportExecutor pExecutor = new ReportExecutorWrapper(pageExecutor, executor);
							layoutEngine.layout(pExecutor, report, emitter, false);
						}
					}
				}
			} else if (ExtensionManager.PAGE_BREAK_PAGINATION.equals(pagination)) {
				if (!paged) {
					long pageNumber = iter.next();
					if (pageNumber != 1) {
						layoutEngine.setLayoutPageHint(getPageHint(pagesExecutor, pageNumber));
					}
					setFilteredPageNumber(filteredTotalPage, totalPage, pageNumber);
					layoutEngine.layout(executor, report, emitter, true);
				} else {
					while (iter.hasNext()) {
						long pageNumber = iter.next();
						IPageHint pageHint = getPageHint(pagesExecutor, pageNumber);
						layoutEngine.setLayoutPageHint(pageHint);
						// here the pageExecutor will returns a report.root.
						IReportItemExecutor pageExecutor = executor.getNextChild();
						if (pageExecutor != null) {
							setFilteredPageNumber(filteredTotalPage, totalPage, pageNumber);
							IReportExecutor pExecutor = new ReportExecutorWrapper(pageExecutor, executor);
							layoutEngine.layout(pExecutor, report, emitter, false);
						}
					}
				}
			} else if (ExtensionManager.NO_PAGINATION.equals(pagination)) {
				layoutEngine.layout(executor, report, emitter, false);
			}
			outputPageCount = layoutEngine.getPageCount();

			layoutEngine.close();
			emitter.end(report);
			closeRender();
			executor.close();

		}

		private void setFilteredPageNumber(long filteredTotalPage, long totalPage, long pageNumber)
				throws EngineException {
			if (filteredTotalPage != totalPage) {
				long filteredPageNumber = getLogicalPageNumber(pageNumber);
				executionContext.setFilteredPageNumber(filteredPageNumber);
			}
		}
	}

	protected class ReportletRender implements InnerRender {

		private long offset;

		ReportletRender(long offset) {
			this.offset = offset;
		}

		@Override
		public void render() throws Exception {
			// start the render
			setupRenderOption();
			IContentEmitter emitter = createContentEmitter();
			IReportExecutor executor = new ReportletExecutor(executionContext, offset);
			executor = createRenderExtensionExecutor(executor);
			executor = new SuppressDuplciateReportExecutor(executor);
			executor = new LocalizedReportExecutor(executionContext, executor);
			executionContext.setExecutor(executor);
			initializeContentEmitter(emitter);
			IReportLayoutEngine layoutEngine = createReportLayoutEngine(pagination, renderOptions);

			layoutEngine.setLocale(executionContext.getLocale());

			// paper size output need re-paginate
			if (ExtensionManager.PAPER_SIZE_PAGINATION.equals(pagination)) {
				emitter = new LayoutEngine(((HTMLReportLayoutEngine) layoutEngine).getContext(), emitter, renderOptions,
						executionContext, getDocumentTotalPage());
			}

			startRender();
			IReportContent report = executor.execute();
			emitter.start(report);
			// output the reportlet without pagination
			layoutEngine.layout(executor, report, emitter, false);
			layoutEngine.close();
			emitter.end(report);
			closeRender();
			executor.close();
			outputPageCount = layoutEngine.getPageCount();
		}
	}

	private static class ReportExecutorWrapper implements IReportExecutor {
		IReportItemExecutor executor;
		IReportExecutor reportExecutor;

		ReportExecutorWrapper(IReportItemExecutor itemExecutor, IReportExecutor reportExecutor) {
			executor = itemExecutor;
			this.reportExecutor = reportExecutor;
		}

		@Override
		public void close() throws BirtException {
			executor.close();

		}

		@Override
		public IReportItemExecutor createPageExecutor(long pageNumber, MasterPageDesign pageDesign)
				throws BirtException {
			return reportExecutor.createPageExecutor(pageNumber, pageDesign);
		}

		@Override
		public IReportContent execute() throws BirtException {
			// FIXME: create the report content only once.
			return reportExecutor.execute();
		}

		@Override
		public IReportItemExecutor getNextChild() throws BirtException {
			return executor.getNextChild();
		}

		@Override
		public boolean hasNextChild() throws BirtException {
			return executor.hasNextChild();
		}

	}

	@Override
	public long getPageNumber(String bookmark) throws EngineException {
		int physicalPageNumber = (int) executionContext.getReportDocument().getPageNumber(bookmark);
		return getLogicalPageNumber(physicalPageNumber);
	}

	@Override
	public ITOCTree getTOCTree() throws EngineException {
		loadDocument();
		IReportDocument document = executionContext.getReportDocument();
		String format = IRenderOption.OUTPUT_FORMAT_HTML;
		if (renderOptions != null) {
			String renderFormat = renderOptions.getOutputFormat();
			if (renderFormat != null) {
				format = renderFormat;
			}
		}

		ULocale ulocale = getULocale();
		TimeZone timeZone = getTimeZone();
		ReportDesignHandle design = executionContext.getReportDesign();

		if (document instanceof IInternalReportDocument) {
			ITreeNode tocTree = null;
			if (tocReader != null) {
				try {
					tocTree = tocReader.readTree();
				} catch (IOException e) {
					throw new EngineException(MessageConstants.FAILED_TO_LOAD_TOC_TREE_EXCEPTION, e);
				}
			}
			if (tocTree != null) {
				LogicalPageSequence visiblePages = loadVisiblePages();
				if (visiblePages != null) {
					return new TOCView(tocTree, design, ulocale, timeZone, format,
							new VisiblePageFilter(document, visiblePages));
				} else {
					return new TOCView(tocTree, design, ulocale, timeZone, format);
				}
			}
		}
		return TOCView.EMPTY_TOC_VIEW;
	}

	public ITreeNode getRawTOCTree() {
		loadDocument();
		ITreeNode tocTree = null;
		if (tocReader != null) {
			try {
				tocTree = tocReader.readTree();
			} catch (IOException e) {
				log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
		return tocTree;
	}

	@Override
	public long getTotalPage() throws EngineException {
		LogicalPageSequence visiblePages = loadVisiblePages();
		if (visiblePages != null) {
			return visiblePages.getTotalVisiblePageCount();
		}
		return reportDocument.getPageCount();
	}

	@Override
	@SuppressWarnings("rawtypes")
	public HashMap getParameterValues() {
		loadDocument();
		return (HashMap) executionContext.getParameterValues();
	}

	@Override
	public Object getParameterDisplayText(String name) {
		loadDocument();
		return executionContext.getParameterDisplayText(name);
	}

	private long getDocumentTotalPage() {
		return reportDocument.getPageCount();
	}

	private long getLogicalPageNumber(long physicalPageNumber) throws EngineException {
		LogicalPageSequence visiblePages = loadVisiblePages();
		if (visiblePages != null) {
			return visiblePages.getLogicalPageNumber(physicalPageNumber);
		}
		return physicalPageNumber;
	}

	private ArrayList<long[]> getPhysicalPageSequence(ArrayList<long[]> logicalPages) throws EngineException {
		LogicalPageSequence visiblePages = loadVisiblePages();
		if (visiblePages != null) {
			long[][] pages = visiblePages.getPhysicalPageNumbers(logicalPages.toArray(new long[logicalPages.size()][]));
			ArrayList<long[]> physicalPages = new ArrayList<>(pages.length);
			for (int i = 0; i < pages.length; i++) {
				physicalPages.add(pages[i]);
			}
			return physicalPages;
		}
		return logicalPages;
	}

	boolean renderExtensionLoaded;
	ArrayList<IRenderExtension> renderExtensions;
	boolean visiblePageLoaded;
	LogicalPageSequence logicalPageSequence;

	private ArrayList<IRenderExtension> loadRenderExtensions() throws EngineException {
		if (!renderExtensionLoaded) {
			String[] extensions = executionContext.getEngineExtensions();
			if (extensions != null) {
				renderExtensions = new ArrayList<>();
				EngineExtensionManager manager = executionContext.getEngineExtensionManager();

				for (String extName : extensions) {
					IRenderExtension renderExtension = manager.getRenderExtension(extName);
					if (renderExtension != null) {
						renderExtensions.add(renderExtension);
					}
				}
			}
		}
		return renderExtensions;
	}

	private void unloadRenderExtensions() {
		if (renderExtensions != null) {
			for (IRenderExtension renderExtension : renderExtensions) {
				renderExtension.close();
			}
			renderExtensions = null;
		}
		renderExtensionLoaded = false;
	}

	private LogicalPageSequence loadVisiblePages() throws EngineException {
		if (!visiblePageLoaded) {
			ArrayList<IRenderExtension> renderExtensions = loadRenderExtensions();
			if (renderExtensions != null) {
				ArrayList<long[][]> pages = new ArrayList<>();
				for (IRenderExtension renderExtension : renderExtensions) {
					long[][] visiblePages = renderExtension.getVisiblePages();
					if (visiblePages != null) {
						pages.add(visiblePages);
					}
				}
				if (!pages.isEmpty()) {
					long physicalTotalPage = reportDocument.getPageCount();
					logicalPageSequence = new LogicalPageSequence(pages, physicalTotalPage);
				}
			}
			visiblePageLoaded = true;
		}
		return logicalPageSequence;
	}

	private void unloadVisiblePages() {
		visiblePageLoaded = false;
		logicalPageSequence = null;
	}

	protected IReportExecutor createRenderExtensionExecutor(IReportExecutor executor) throws EngineException {
		// prepare the extension executor
		ArrayList<IRenderExtension> renderExtensions = loadRenderExtensions();
		if (renderExtensions != null) {
			ArrayList<IContentProcessor> processors = new ArrayList<>();
			for (IRenderExtension extension : renderExtensions) {
				IContentProcessor processor = extension.getRenderProcessor();
				if (processor != null) {
					processors.add(processor);
				}
			}
			if (!processors.isEmpty()) {
				return new ReportExtensionExecutor(executionContext, executor,
						processors.toArray(new IContentProcessor[processors.size()]));
			}
		}
		return executor;
	}

}
