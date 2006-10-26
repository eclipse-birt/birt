/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.layout.pdf;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.engine.EngineCase;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.impl.RunAndRenderTask;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.impl.PageContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.layout.area.IArea;
import org.eclipse.birt.report.engine.layout.area.IContainerArea;
import org.eclipse.birt.report.engine.layout.area.impl.ContainerArea;
import org.eclipse.birt.report.engine.layout.area.impl.InlineContainerArea;
import org.eclipse.birt.report.engine.layout.area.impl.LineArea;
import org.eclipse.birt.report.engine.layout.area.impl.PageArea;
import org.eclipse.birt.report.engine.layout.area.impl.TextArea;

public class PDFLineAreaLMTest extends EngineCase
{

	/**
	 * Test case for bugzilla bug <a
	 * href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=157189">157189</a> :
	 * HTML BR tags cease to work for text element after page break in PDF
	 * 
	 * @throws EngineException
	 */
	public void testForeignContent( ) throws EngineException
	{
		String designFile = "org/eclipse/birt/report/engine/layout/pdf/LineAreaLMTest-157189.xml";
		IReportRunnable report = openReportDesign( designFile );
		List pageAreas = getPageAreas( report );
		
 		assertEquals( 2, pageAreas.size( ) );
		PageArea pageArea = (PageArea)pageAreas.get( 1 );
		Iterator logicContainers = pageArea.getBody( ).getChildren( );
		IContainerArea blockContains = (IContainerArea) logicContainers
					.next( );
		Iterator lineAreas = blockContains.getChildren( );
		lineAreas.next( );
		LineArea emptyLine = (LineArea)lineAreas.next( );
		assertTrue( "Second line is not an empty line.", isEmpty( emptyLine ) );
		LineArea lineArea = (LineArea)lineAreas.next( );
		assertEquals( " ", getText( lineArea, 2 ) );
		assertEquals( "paragraph 22.", getText( lineArea, 5 ) );
	}

	protected boolean isEmpty( ContainerArea container )
	{
		int childrenCount = container.getChildrenCount( );
		if ( childrenCount == 0 )
		{
			return true;
		}
		else if ( childrenCount == 1 )
		{
			Object children = container.getChildren( ).next( );
			if ( children instanceof ContainerArea )
			{
				return isEmpty( (ContainerArea )children);
			}
		}
		return false;
	}
	
	protected IArea getChildren( ContainerArea container, int index )
	{
		int current = 0;
		Iterator children = container.getChildren( );
		while( children.hasNext( ) )
		{
			Object child = children.next( );
			if ( current == index )
			{
				return (IArea)child;
			}
			++current;
		}
		return null;
	}

	protected String getText( LineArea line, int index )
	{
		InlineContainerArea inlineArea = (InlineContainerArea) getChildren(
				line, index );
		IArea area = getChildren( inlineArea, 0 );
		if ( !(area instanceof TextArea) )
		{
			fail( "Child " + index + " of line doesn't contains text Area");
		}
		return ((TextArea)area).getText( );
	}
	
	protected IReportRunnable openReportDesign( String designFile ) throws EngineException
	{
		useDesignFile( designFile );
		IReportRunnable report = engine.openReportDesign( REPORT_DESIGN );
		return report;
	}
	
	protected List getPageAreas(IReportRunnable runnable ) throws EngineException
	{
		final List pageAreas = new ArrayList();
		IEmitterMonitor monitor = new IEmitterMonitor(){
			public void onMethod( Method method, Object[] args )
			{
				if ( "startPage".equals( method.getName( ) ))
				{
					PageContent pageContent = (PageContent) args[0];
					pageAreas.add( pageContent
							.getExtension( IContent.LAYOUT_EXTENSION ) );
				}
			}
		};
		IRunAndRenderTask runAndRenderTask = new TestRunAndRenderTask( engine,
				runnable, monitor );
		HTMLRenderOption options = new HTMLRenderOption( );
		options.setOutputFormat( "pdf" );
		options.setMasterPageContent( false );
		runAndRenderTask.setRenderOption( options );
		runAndRenderTask.run( );
		runAndRenderTask.close( );
		return pageAreas;
	}
}

interface IEmitterMonitor
{
	void onMethod( Method method, Object[] args );
}

class TestRunAndRenderTask extends RunAndRenderTask
{
	IEmitterMonitor monitor;
	
	public TestRunAndRenderTask( IReportEngine engine,
			IReportRunnable runnable, IEmitterMonitor monitor )
	{
		super( engine, runnable );
		this.monitor = monitor;
	}

	protected IContentEmitter createContentEmitter( ) throws EngineException
	{
		final IContentEmitter emitter = super.createContentEmitter( );
		return (IContentEmitter) Proxy.newProxyInstance(
				emitter.getClass( ).getClassLoader( ), new Class[]{IContentEmitter.class},
				new InvocationHandler( ) {

					public Object invoke( Object proxy, Method method,
							Object[] args ) throws Throwable
					{
						monitor.onMethod( method, args );
						return method.invoke( emitter, args );
					}
				} );
	}
}