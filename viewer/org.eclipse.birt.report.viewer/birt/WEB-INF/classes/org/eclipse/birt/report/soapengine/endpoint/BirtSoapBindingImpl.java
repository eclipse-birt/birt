/**
 * BirtSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.endpoint;

import javax.xml.namespace.QName;

import org.apache.axis.AxisFault;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjects;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;
import org.eclipse.birt.report.soapengine.processor.ComponentProcessorFactory;
import org.eclipse.birt.report.soapengine.processor.IComponentProcessor;
import org.eclipse.birt.report.soapengine.BirtContext;

public class BirtSoapBindingImpl implements BirtSoapPort
{
    public GetUpdatedObjectsResponse getUpdatedObjects( GetUpdatedObjects request ) throws java.rmi.RemoteException
	{
    	GetUpdatedObjectsResponse response = new GetUpdatedObjectsResponse( );
    	Operation[] ops = request.getOperation( );
    	
    	BirtContext context =  BirtContext.get( );
    	if ( context.getBean( ).getException( ) != null )
    	{
			AxisFault fault = new AxisFault( );
			fault.setFaultCode( new QName( "Birt Context" ) ); //$NON-NLS-1$
			fault.setFaultString( context.getBean( ).getException( ).getMessage( ) ); //$NON-NLS-1$
			throw fault;
    	}
    	
    	IReportRunnable runnable = context.getBean( ).getReportRunnable( );
    	assert runnable != null;
		
		for( int i = 0; i < ops.length; i ++ )
    	{
    		Operation  op = ops[i];
    		IComponentProcessor processor = ComponentProcessorFactory.createProcessor( runnable, op.getTarget( ) );
    		
    		if ( processor == null )
    		{
    			AxisFault fault = new AxisFault( );
    			fault.setFaultCode( new QName( "Birt Processor" ) ); //$NON-NLS-1$
    			fault.setFaultString( "No handler can be found for this component!!! Target: " + op.getTarget( ) ); //$NON-NLS-1$
    			throw fault;
    		}
    		
   			processor.process( context, op, response );
    	}
    	
    	return response;    	    
    }
}