package de.sybig.TFClassFASTA.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import de.sybig.TFClassFASTA.core.Fasta;
import de.sybig.TFClassFASTA.core.MetaFile;
import de.sybig.TFClassFASTA.db.MetaFileDAO;

@Provider
@Consumes(MediaType.MULTIPART_FORM_DATA)
public class FastaUnmarshaller implements MessageBodyReader<List<Fasta>>{
	private static final Map<String,String> TFactortoID;
	static {
		Map<String, String> tempMap = new HashMap<>();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(FastaUnmarshaller.class.getClassLoader().getResourceAsStream("IDs_all.csv")));
			String line = null;
			while((line = in.readLine()) != null) {
				String[] strarray = line.split(";");
				if(strarray.length >= 2) {
					tempMap.put(strarray[1], strarray[0]);
				}
			}
			in.close();
		}
		catch(IOException e) {
			System.out.println(e.getMessage());
		}
		TFactortoID = Collections.unmodifiableMap(tempMap);
	}
	private final MetaFileDAO metafileDAO;
	
	public FastaUnmarshaller(MetaFileDAO metafileDAO) {
		this.metafileDAO = metafileDAO;
	}
	
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
		String line, type, align, tfclassID,source;
		Long version;
		line = type = align = tfclassID = source = null;
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
			case "tfclassid":
				in.readLine();
				tfclassID = in.readLine();
				break;
			case "source":
				in.readLine();
				source = in.readLine();
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
		System.out.println("Type = " + type);
		System.out.println("Align = " + align);
		System.out.println("TFClassID = " + tfclassID);
		System.out.println("Source = " + source);
		List<MetaFile> metafiles = metafileDAO.getNewestByTFClassID(tfclassID, align, type);
		if(metafiles.isEmpty()) {
			version = 1l;
		}
		else {
			version = metafiles.get(0).getVersion()+1;
		}
		MetaFile sourcefile = new MetaFile(align, type, tfclassID, source, version);
		if(fastaFile.get(0).startsWith(">")) { //Normal fasta format
			for(int i = 0; i < fastaFile.size();) {
				String header = fastaFile.get(i);
				String seq = fastaFile.get(i+1);
				i += 2;
				System.out.println(i);
				System.out.println(header);
				//System.out.println(seq);
				while(i < fastaFile.size() && !fastaFile.get(i).startsWith(">")) {
					seq += fastaFile.get(i);
					i++;
				}
				String taxon, tfactor; 
				if(header.startsWith(">Gorilla_gorilla_gorilla")) {
					taxon = "Gorilla_gorilla_gorilla";
				}
				else if(header.startsWith(">Gorilla_gorilla")) {
					taxon = "Gorilla_gorilla";
				}
				else if(header.startsWith(">Canis_lupus_familiaris")) {
					taxon = "Canis_lupus_familiaris";
				}
				else if(header.startsWith(">Mustela_putorius_furo")) {
					taxon = "Mustela_putorius_furo";
				}
				else {
					String[] strarray = header.split("_");
					taxon = strarray[0].substring(1) + "_" + strarray[1];
				}
				tfactor = header.substring(taxon.length()+2, header.length()-3);//Starting after Taxon and removing _ma from end	
				if(tfactor.endsWith("-DBD")) {//Removing -DBD
					tfactor = tfactor.substring(0,tfactor.length() - 4);
				}
				if(tfactor.contains("-annot")) {
					tfactor = tfactor.replace("-annot","");
				}
				if(TFactortoID.containsKey(tfactor)) { //Mapping tfactor to id
					tfactor = TFactortoID.get(tfactor);
				}
				else { //Removing last .X until it matches an entry in the map. Otherwise use original tfactor
					String temp = tfactor;
					while(temp.lastIndexOf(".") != -1) {
						temp = temp.substring(0, temp.lastIndexOf("."));
						System.out.println(temp);
						if(TFactortoID.containsKey(temp)) {
							tfactor = TFactortoID.get(temp);
							break;
						}
					}
				}
				listFasta.add(new Fasta(header, seq, sourcefile, taxon, tfactor));			
			}
		}
		else { //other format
			String[] strarray = fastaFile.get(0).trim().split(" ");
			int countofSeqs = Integer.parseInt(strarray[0]);
			System.out.println("Count of Seqs = " + countofSeqs);
			for(int i = 1; i <= countofSeqs; i++) {
				String header = fastaFile.get(i).substring(0, fastaFile.get(i).indexOf(" "));
				String seq = fastaFile.get(i).substring(header.length()).replace(" ", "");
				System.out.println(header);
				for(int j = i + countofSeqs; j < fastaFile.size(); j += countofSeqs) {
					seq += fastaFile.get(j).replace(" ", "");
				}
				System.out.println(seq);
				String taxon, tfactor; 
				if(header.startsWith("Gorilla_gorilla_gorilla")) {
					taxon = "Gorilla_gorilla_gorilla";
				}
				else if(header.startsWith("Gorilla_gorilla")) {
					taxon = "Gorilla_gorilla";
				}
				else if(header.startsWith("Canis_lupus_familiaris")) {
					taxon = "Canis_lupus_familiaris";
				}
				else if(header.startsWith("Mustela_putorius_furo")) {
					taxon = "Mustela_putorius_furo";
				}
				else {
					String[] strarray2 = header.split("_");
					taxon = strarray2[0] + "_" + strarray2[1];
				}
				tfactor = header.substring(taxon.length()+1, header.length()-3);//Starting after Taxon and removing _ma from end	
				if(tfactor.endsWith("-DBD")) {//Removing -DBD
					tfactor = tfactor.substring(0,tfactor.length() - 4);
				}
				if(tfactor.contains("-annot")) {
					tfactor = tfactor.replace("-annot","");
				}
				if(TFactortoID.containsKey(tfactor)) { //Mapping tfactor to id
					tfactor = TFactortoID.get(tfactor);
				}
				else { //Removing last .X until it matches an entry in the map. Otherwise use original tfactor
					String temp = tfactor;
					while(temp.lastIndexOf(".") != -1) {
						temp = temp.substring(0, temp.lastIndexOf("."));
						System.out.println(temp);
						if(TFactortoID.containsKey(temp)) {
							tfactor = TFactortoID.get(temp);
							break;
						}
					}
				}
				listFasta.add(new Fasta(header, seq, sourcefile, taxon, tfactor));						
			}
		}
		return listFasta;
	}

}
