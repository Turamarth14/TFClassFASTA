package de.sybig.TFClassFASTA.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
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
		if(mediaType.getType().equals("multipart") && mediaType.getSubtype().equals("form-data")) {
			return true;
		}
		return false;
	}

	@Override
	public List<Fasta> readFrom(Class<List<Fasta>> arg0, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream inputStream) throws IOException, WebApplicationException {
		List<Fasta> listFasta = new ArrayList<>();
		List<String> fastaFile = new ArrayList<>();
		BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
		String line, type, align, source;
		while((line = in.readLine()) != null) {
			if(!line.startsWith("Content-Disposition")) {
				continue;
			}
			switch(line.substring(line.lastIndexOf("name=") + 6, line.lastIndexOf('"'))) {
			case "type":
				in.readLine();
				System.out.println("Type = " + in.readLine());
				break;
			case "taxon":
				in.readLine();
				System.out.println("Taxon = " + in.readLine());
				break;
			case "fasta":
				in.readLine();
				line = in.readLine();
				while(!line.startsWith("--Boundary") && !line.isEmpty()) {
					fastaFile.add(line);
					fastaFile.add(in.readLine());
					line = in.readLine();
				}
				break;
			default:	
			}
		}
		in.close();
		for(int i = 0; i < fastaFile.size(); i += 2) {
			String header = fastaFile.get(i);
			String seq = fastaFile.get(i+1);
			listFasta.add(new Fasta(header,seq));			
		}
		return listFasta;
	}

}
