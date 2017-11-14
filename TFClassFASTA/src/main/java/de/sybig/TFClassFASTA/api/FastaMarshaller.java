package de.sybig.TFClassFASTA.api;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
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
public class FastaMarshaller implements MessageBodyWriter<List<Fasta>>{

	@Override
	public long getSize(List<Fasta> obj, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return -1;
	}
	
	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		if(Collection.class.isAssignableFrom(type)) {
			ParameterizedType t = (ParameterizedType) genericType;
			if(t.getActualTypeArguments()[0].equals(Fasta.class)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void writeTo(List<Fasta> obj, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders, OutputStream outputStream) throws IOException, WebApplicationException {
		httpHeaders.add("Content-Type", "text/plain; charset=UTF-8");
		StringBuffer str = new StringBuffer();
		for(Fasta fst : obj) {
			str.append(fst.getHeader());
			str.append("\n");
			str.append(fst.getSequence());
			str.append("\n");
		}
		if(str.length() > 0) {
			str.setLength(str.length()-1);
		}
		outputStream.write(str.toString().getBytes());	
		
	}

}
