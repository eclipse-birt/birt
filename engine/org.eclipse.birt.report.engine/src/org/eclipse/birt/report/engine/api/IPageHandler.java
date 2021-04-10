package org.eclipse.birt.report.engine.api;

/**
 * An interface implemented by app developer to provide handler after each page
 * is generated in factoery. Can be used to support checkpointing, and therefore
 * progressive viewing.
 */
public interface IPageHandler {
	/**
	 * @param pageNumber page indexed by pageNumber has finished generation
	 * @param checkpoint whether the page indexed by pageNumber is ready for viewing
	 */
	public void onPage(int pageNumber, boolean checkpoint, IReportDocumentInfo doc);
}
