package de.sybig.TFClassFASTA.resources;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.sybig.TFClassFASTA.core.Fasta;
import de.sybig.TFClassFASTA.db.FastaDAO;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;
import io.dropwizard.setup.Environment;

@Path("/FASTA")
@Produces(MediaType.APPLICATION_JSON)
public class FastaResource {

	private final FastaDAO fastaDAO;
	private final Environment environment;
	
	public FastaResource(FastaDAO fastaDAO, Environment environment) {
		this.fastaDAO = fastaDAO;
		this.environment = environment;
	}
	
	/*@GET
	@Produces("application/fasta")
	@Path("/{UID}")
	@UnitOfWork
	public Fasta getFasta(@PathParam(value = "UID") LongParam UID) {
		Fasta fst = fastaDAO.getByUID(UID.get());
		return fst;
		//return fastaDAO.getByUID(UID.get());
	}*/

	@GET
	@Produces("application/fasta")
	@Path("/{UID}")
	@UnitOfWork
	public Response getFastas(@PathParam(value = "UID") String UIDs) {
		List<Long> listUID = Arrays.asList(UIDs.split(",")).stream().map(Long::parseLong).collect(Collectors.toList());
		List<Fasta> listFasta = listUID.stream().map(uid -> fastaDAO.getByUID(uid)).collect(Collectors.toList());
		for(Fasta fst : listFasta) {
			System.out.println(fst.getHeader());
		}
		GenericEntity<List<Fasta>> result = new GenericEntity<List<Fasta>>(listFasta) {};
		return Response.ok(result).build();
	}
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("File/{UID}")
	@UnitOfWork
	public Response getFile(@PathParam(value = "UID") LongParam UID) {
		Fasta fasta = fastaDAO.getByUID(UID.get());
		String data = fasta.toFile();
		return Response.ok(data, MediaType.TEXT_PLAIN)
				.header("Content-Disposition", "attachment; filename=\"" + fasta.getUID() + "\"")
				.build();
	}
}
