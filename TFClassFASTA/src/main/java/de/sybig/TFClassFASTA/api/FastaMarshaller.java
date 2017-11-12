package de.sybig.TFClassFASTA.api;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

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
	public long getSize(Fasta obj, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return type == Fasta.class;
	}

	@Override
	public void writeTo(Fasta obj, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders, OutputStream outputStream) throws IOException, WebApplicationException {
		httpHeaders.add("Content-Type", "text/plain; charset=UTF-8");
		StringBuffer str = new StringBuffer();
		str.append(obj.getHeader());
		str.append("\n");
		str.append(obj.getSequence());
		outputStream.write(str.toString().getBytes());	
	}

}
