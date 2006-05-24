/**
 * BirtSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.endpoint;

import java.rmi.RemoteException;

import javax.xml.namespace.QName;

import org.apache.axis.AxisFault;
import org.eclipse.birt.report.context.BirtContext;
import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;
import org.eclipse.birt.report.soapengine.api.Update;
import org.eclipse.birt.report.soapengine.processor.BaseProcessorFactory;
import org.eclipse.birt.report.soapengine.processor.IComponentProcessor;
import org.eclipse.birt.report.soapengine.processor.IProcessorFactory;

public class BirtSoapBindingImpl implements BirtSoapPort
{    
	public Update[] getUpdatedObjects( Operation[] request ) throws RemoteException
	{
		IProcessorFactory processorFactory = BaseProcessorFactory.getInstance( );
		
    	GetUpdatedObjectsResponse response = new GetUpdatedObjectsResponse( );
    	   	
    	IContext context =  BirtContext.getInstance( );
    	if ( context.getBean( ).getException( ) != null )
    	{
			AxisFault fault = new AxisFault( );
			fault.setFaultCode( new QName( "BirtSoapBindingImpl.getUpdatedObjects( )" ) ); //$NON-NLS-1$
			fault.setFaultString( context.getBean( ).getException( ).getMessage( ) );
			throw fault;
    	}
    	
		for( int i = 0; i < request.length; i ++ )
    	{
    		Operation  op = request[i];
    		IComponentProcessor processor = processorFactory
    			.createProcessor( context.getBean( ).getCategory( ), op.getTarget( ).getType( ) );
    		
    		if ( processor == null )
    		{
    			AxisFault fault = new AxisFault( );
    			fault.setFaultCode( new QName( "BirtSoapBindingImpl.getUpdatedObjects( )" ) ); //$NON-NLS-1$
    			fault.setFaultString( "No handler can be found for this target. Target: " + op.getTarget( ) ); //$NON-NLS-1$
    			throw fault;
    		}
    		
   			processor.process( context, op, response );
    	}
    	
    	return response.getUpdate( );    	    
	}
}