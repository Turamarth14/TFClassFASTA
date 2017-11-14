package de.sybig.TFClassFASTA.resources;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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

	@GET
	@Produces("application/fasta")
	@Path("/taxon/{TAXON}")
	@UnitOfWork
	public List<Fasta> getFastaByTaxon(@PathParam(value = "TAXON") String Taxons,
									   @QueryParam(value = "ALIGN") String Align,
			                           @QueryParam(value = "TYPE") String Type
	) {
		List<String> listTaxons = Arrays.asList(Taxons.split(","));
		return listTaxons.stream().flatMap(tax -> fastaDAO.getByTaxon(tax, Type, Align).stream()).collect(Collectors.toList());
	}
	
	@GET
	@Produces("application/fasta")
	@Path("/alignment/{ALIGN}")
	@UnitOfWork
	public List<Fasta> getFastaByAlignment(@PathParam(value = "ALIGN") String Aligns,
									       @QueryParam(value = "TAXON") String Taxon,
									       @QueryParam(value = "TYPE") String Type
	) {
		List<String> listAligns = Arrays.asList(Aligns.split(","));
		return listAligns.stream().flatMap(align -> fastaDAO.getByAlign(align, Taxon, Type).stream()).collect(Collectors.toList());
	}	
	
	@GET
	@Produces("application/fasta")
	@Path("/type/{TYPE}")
	@UnitOfWork
	public List<Fasta> getFastaByType(@PathParam(value = "TYPE") String Types,
									  @QueryParam(value = "TAXON") String Taxon,
									  @QueryParam(value = "ALIGN") String Align
	) {
		List<String> listTypes = Arrays.asList(Types.split(","));
		return listTypes.stream().flatMap(typ -> fastaDAO.getByType(typ, Taxon, Align).stream()).collect(Collectors.toList());
	}	
	
	@GET
	@Produces("application/fasta")
	@Path("/UID/{UID}")
	@UnitOfWork
	public List<Fasta> getFastas(@PathParam(value = "UID") String UIDs) {
		List<Long> listUID = Arrays.asList(UIDs.split(",")).stream().map(Long::parseLong).collect(Collectors.toList());
		List<Fasta> listFasta = listUID.stream().map(uid -> fastaDAO.getByUID(uid)).collect(Collectors.toList());
		return listFasta;
	}
}
