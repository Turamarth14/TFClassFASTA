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
		String line, type, align, desc, tfclassID,source;
		line = type = align = desc = tfclassID = source = null;
		while((line = in.readLine()) != null) {
			System.out.println(line);
			if(!line.startsWith("Content-Disposition")) {
				continue;
			}
			switch(line.substring(line.lastIndexOf("name=") + 6, line.lastIndexOf('"'))) {
			case "type":
				in.readLine();
				type = in.readLine();
				break;
			case "align":
				in.readLine();
				align = in.readLine();
				break;
			case "desc":
				in.readLine();
				desc = in.readLine();
				break;	
			case "tfclassid":
				in.readLine();
				tfclassID = in.readLine();
				break;	
			case "fasta":
				String temp = line;
				System.out.println(temp);
				int firstIndex = line.indexOf('"')+1;
				System.out.println("First index = " + firstIndex);
				int secondIndex = line.substring(firstIndex+1).indexOf('"')+1;
				source = line.substring(firstIndex,firstIndex+secondIndex);
				System.out.println("Second index = " + secondIndex);
				System.out.println("Substring = " + temp.substring(firstIndex, firstIndex+secondIndex));
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
		System.out.println("Type = " + type);
		System.out.println("Align = " + align);
		System.out.println("Desc = " + desc);
		System.out.println("TFClassID = " + tfclassID);
		System.out.println("Source = " + source);
		for(int i = 0; i < fastaFile.size(); i += 2) {
			String header = fastaFile.get(i);
			String seq = fastaFile.get(i+1);
			System.out.println(i);
			System.out.println(header);
			System.out.println(seq);
			listFasta.add(new Fasta(header,seq));			
		}
		return listFasta;
	}

}
