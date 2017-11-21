package de.sybig.TFClassFASTA.client;

import java.io.File;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

public class InputFasta {


	public static void main(String[] args) {
		if(args.length != 1) {
			System.out.println("Usage : java InputFasta fastaFilename");
			return;
		}
		try{
			Client client = ClientBuilder.newBuilder()
					.register(MultiPartFeature.class)
					.build();
			WebTarget target = client.target("http://localhost:8080/FASTA/upload");
			FileDataBodyPart filePart = new FileDataBodyPart("fasta", new File(args[0]));
			FormDataMultiPart multiPart = new FormDataMultiPart();
			multiPart.field("type", "TYP1");
			multiPart.field("taxon", "human");
			multiPart.bodyPart(filePart);
			System.out.println(multiPart.getMediaType());
			target.request(MediaType.MULTIPART_FORM_DATA).post(Entity.entity(multiPart, multiPart.getMediaType()));
			client.close();
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
