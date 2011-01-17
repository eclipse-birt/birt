package org.eclipse.birt.data.engine.impl.index;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

import org.eclipse.birt.core.btree.BTreeSerializer;
import org.eclipse.birt.core.i18n.CoreMessages;
import org.eclipse.birt.core.i18n.ResourceConstants;

public class BTreeSerializerUtil
{
	public static BTreeSerializer createSerializer( Class dataType )
	{
		if( dataType == String.class )
		{
			return new StringSerializer( );
		}
		else if ( dataType == BigDecimal.class )
		{
			return new BigDecimalSerializer( );
		}
		else if ( dataType == Integer.class )
		{
			return new IntegerSerializer( );
		}
		else if ( dataType == Double.class )
		{
			return new DoubleSerializer( );
		}
		else if ( dataType == java.util.Date.class )
		{
			return new DateTimeSerializer( );
		}
		else if ( dataType == java.sql.Date.class )
		{
			return new DateSerializer( );
		}
		else if ( dataType == Time.class )
		{
			return new TimeSerializer( );
		}
		else if ( dataType == Timestamp.class )
		{
			return new TimeStampSerializer( );
		}
		else if ( dataType == Boolean.class )
		{
			return new BooleanSerializer( );
		}
		return new JavaSerializer( );
	}	
}

class JavaSerializer implements BTreeSerializer<Object>
{
	ClassLoader classLoader = null;
	
	public void setClassLoader( ClassLoader classLoader )
	{
		this.classLoader = classLoader;
	}
	
	public byte[] getBytes( Object object ) throws IOException
	{
		if( ! ( object instanceof Serializable ) )
		{
			throw new NotSerializableException(
					CoreMessages.getString( ResourceConstants.NOT_SERIALIZABLE ) );
		}
		ByteArrayOutputStream buff = new ByteArrayOutputStream( );
		ObjectOutputStream oo = new ObjectOutputStream( buff );
		oo.writeObject( object );
		oo.close( );
		return buff.toByteArray( );
	}

	public Object getObject( byte[] bytes ) throws IOException,
			ClassNotFoundException
	{
		final ClassLoader loader = classLoader;
		ObjectInputStream oo = new ObjectInputStream(
					new ByteArrayInputStream( bytes ) ) {

				protected Class resolveClass( ObjectStreamClass desc )
						throws IOException, ClassNotFoundException
				{
					return Class.forName( desc.getName( ), false,
							loader );
				}
			};
		return oo.readObject( );
	}
}

class BooleanSerializer implements BTreeSerializer<Boolean>
{

	public byte[] getBytes( Boolean object ) throws IOException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream( 1024 );
		DataOutput oo = new DataOutputStream( out );
		oo.writeBoolean( object );
		return out.toByteArray( );
	}

	public Boolean getObject( byte[] bytes ) throws IOException,
			ClassNotFoundException
	{
		DataInput input = new DataInputStream( new ByteArrayInputStream(
				bytes ) );
		return input.readBoolean( );
	}
}

class IntegerSerializer implements BTreeSerializer<Integer>
{

	public byte[] getBytes( Integer object ) throws IOException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream( 1024 );
		DataOutput oo = new DataOutputStream( out );
		oo.writeInt( object );
		return out.toByteArray( );
	}

	public Integer getObject( byte[] bytes ) throws IOException,
			ClassNotFoundException
	{
		DataInput input = new DataInputStream( new ByteArrayInputStream(
				bytes ) );
		return input.readInt( );
	}
}

class DoubleSerializer implements BTreeSerializer<Double>
{

	public byte[] getBytes( Double object ) throws IOException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream( 1024 );
		DataOutput oo = new DataOutputStream( out );
		oo.writeDouble( object );
		return out.toByteArray( );
	}

	public Double getObject( byte[] bytes ) throws IOException,
			ClassNotFoundException
	{
		DataInput input = new DataInputStream( new ByteArrayInputStream(
				bytes ) );
		return input.readDouble( );
	}
}

class StringSerializer implements BTreeSerializer<String>
{

	public byte[] getBytes( String object ) throws IOException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream( 1024 );
		DataOutput oo = new DataOutputStream( out );
		oo.writeUTF( object );
		return out.toByteArray( );
	}

	public String getObject( byte[] bytes ) throws IOException,
			ClassNotFoundException
	{
		DataInput input = new DataInputStream( new ByteArrayInputStream(
				bytes ) );
		return input.readUTF( );
	}
}

class DateTimeSerializer<K> implements BTreeSerializer<java.util.Date>
{

	public byte[] getBytes( java.util.Date object ) throws IOException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream( 1024 );
		DataOutput oo = new DataOutputStream( out );
		oo.writeLong( object.getTime( ) );
		return out.toByteArray( );
	}

	public java.util.Date getObject( byte[] bytes ) throws IOException,
			ClassNotFoundException
	{
		DataInput input = new DataInputStream( new ByteArrayInputStream(
				bytes ) );
		return new Date( input.readLong( ) );
	}
}

class TimeStampSerializer<K> implements BTreeSerializer<Timestamp>
{

	public byte[] getBytes( Timestamp object ) throws IOException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream( 1024 );
		DataOutput oo = new DataOutputStream( out );
		oo.writeLong( object.getTime( ) );
		return out.toByteArray( );
	}

	public Timestamp getObject( byte[] bytes ) throws IOException,
			ClassNotFoundException
	{
		DataInput input = new DataInputStream( new ByteArrayInputStream(
				bytes ) );
		return new Timestamp( input.readLong( ) );
	}
}

class DateSerializer<K> implements BTreeSerializer<java.sql.Date>
{

	public byte[] getBytes( java.sql.Date object ) throws IOException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream( 1024 );
		DataOutput oo = new DataOutputStream( out );
		oo.writeLong( object.getTime( ) );
		return out.toByteArray( );
	}

	public java.sql.Date getObject( byte[] bytes ) throws IOException,
			ClassNotFoundException
	{
		DataInput input = new DataInputStream( new ByteArrayInputStream(
				bytes ) );
		return new java.sql.Date( input.readLong( ) );
	}
}

class TimeSerializer<K> implements BTreeSerializer<java.sql.Time>
{

	public byte[] getBytes( java.sql.Time object ) throws IOException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream( 1024 );
		DataOutput oo = new DataOutputStream( out );
		oo.writeLong( object.getTime( ) );
		return out.toByteArray( );
	}

	public java.sql.Time getObject( byte[] bytes ) throws IOException,
			ClassNotFoundException
	{
		DataInput input = new DataInputStream( new ByteArrayInputStream(
				bytes ) );
		return new java.sql.Time( input.readLong( ) );
	}
}

class BigDecimalSerializer<K> implements BTreeSerializer<BigDecimal>
{

	public byte[] getBytes( BigDecimal object ) throws IOException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream( 1024 );
		DataOutput oo = new DataOutputStream( out );
		oo.writeUTF( object.toString( ) );
		return out.toByteArray( );
	}

	public BigDecimal getObject( byte[] bytes ) throws IOException,
			ClassNotFoundException
	{
		DataInput input = new DataInputStream( new ByteArrayInputStream(
				bytes ) );
		return new BigDecimal( input.readUTF( ) );
	}
}