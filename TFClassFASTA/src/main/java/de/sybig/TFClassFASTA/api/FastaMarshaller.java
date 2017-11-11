package de.sybig.TFClassFASTA.api;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import de.sybig.TFClassFASTA.core.Fasta;

@Provider
@Produces("application/fasta")
public class FastaMarshaller implements MessageBodyWriter<Fasta>{

	@Override
	public long getSize(Fasta arg0, Class<?> arg1, Type arg2, Annotation[] arg3, MediaType arg4) {
		return -1;
	}

	@Override
	public boolean isWriteable(Class<?> arg0, Type arg1, Annotation[] arg2, MediaType arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void writeTo(Fasta arg0, Class<?> arg1, Type arg2, Annotation[] arg3, MediaType arg4,
			MultivaluedMap<String, Object> arg5, OutputStream arg6) throws IOException, WebApplicationException {
		// TODO Auto-generated method stub
		
	}

}
