package de.sybig.TFClassFASTA.api;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import de.sybig.TFClassFASTA.core.Fasta;

@Provider
@Consumes("application/fasta")
public class FastaUnmarshaller implements MessageBodyReader<Fasta>{

	@Override
	public boolean isReadable(Class<?> arg0, Type arg1, Annotation[] arg2, MediaType arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Fasta readFrom(Class<Fasta> arg0, Type arg1, Annotation[] arg2, MediaType arg3,
			MultivaluedMap<String, String> arg4, InputStream arg5) throws IOException, WebApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

}
