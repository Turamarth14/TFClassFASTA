package de.sybig.TFClassFASTA.api;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import de.sybig.TFClassFASTA.core.Fasta;

@Provider
@Consumes(MediaType.MULTIPART_FORM_DATA)
public class FastaUnmarshaller implements MessageBodyReader<List<Fasta>>{

	@Override
	public boolean isReadable(Class<?> arg0, Type genericType, Annotation[] annotations, MediaType mediaType) {
		// TODO Auto-generated method stub
		System.out.println("Type " + mediaType.getType());
		System.out.println("Subtype " + mediaType.getSubtype());
		mediaType.getParameters().forEach((key,value) -> System.out.println(key + "\t" + value));
		return true;
	}

	@Override
	public List<Fasta> readFrom(Class<List<Fasta>> arg0, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream inputStream) throws IOException, WebApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

}
