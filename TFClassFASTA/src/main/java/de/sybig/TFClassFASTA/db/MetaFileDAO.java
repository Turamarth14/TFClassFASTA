package de.sybig.TFClassFASTA.db;

import java.util.List;

import org.hibernate.SessionFactory;

import de.sybig.TFClassFASTA.core.Fasta;
import de.sybig.TFClassFASTA.core.MetaFile;
import io.dropwizard.hibernate.AbstractDAO;

public class MetaFileDAO extends AbstractDAO<MetaFile>{

	public MetaFileDAO(SessionFactory Factory) {
		super(Factory);
	}

	public MetaFile getByUID(Long uid){
		return get(uid);
	}

	public List<MetaFile> getByTFClassID(String tfclassID, String align, String type){
		return list(namedQuery("Fasta.getByTFCLASS").setParameter("TFCLASSID", tfclassID).setParameter("ALIGNMENT", align).setParameter("TYPE",type));
	}
	
	public MetaFile create(MetaFile file) {
		return persist(file);
	}
}
