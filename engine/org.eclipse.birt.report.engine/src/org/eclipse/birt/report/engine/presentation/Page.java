package org.eclipse.birt.report.engine.presentation;

import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;


public class Page
{
	IPageContent pageContent;
	IContentEmitter emitter;
	PageRegion rootRegion;

	public Page(IContentEmitter emitter, IPageContent pageContent)
	{
		this.emitter = emitter;
		this.pageContent = pageContent;
		this.rootRegion = new PageRegion(this,null);
	}
	
	public PageRegion getRootRegion()
	{
		return rootRegion;
	}
	
	public IContentEmitter getEmitter()
	{
		return this.emitter;
	}
	
	public void open()
	{
		emitter.startPage(pageContent);
	}
	
	public void close()
	{
		emitter.endPage(pageContent);
	}
	
	public PageRegion createRegion(PageFlow pageFlow)
	{
		return getRootRegion();
	}
}
