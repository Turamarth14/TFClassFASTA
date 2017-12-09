package de.sybig.TFClassFASTA.core;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
    @NamedQuery(name = "Fasta.getByFile", query = "Select fst FROM "
    		+ "Fasta fst where fst.file = :FILE "
    		+ "AND (:TAXON is null OR fst.taxon = :TAXON)")
})
public class Fasta {
	
    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)	
	private Long UID;
    
    private MetaFile file;
    private String taxon;
    private String sequence;
    private String header;
    
    public Fasta() {
    	
    }
    public Fasta(String header, String seq, MetaFile file) {
    	this.header = header;
    	this.sequence = seq;
    	this.file = file;
    	this.taxon = "Default";
    }
    public Fasta(String header, String seq, MetaFile file, String taxon) {
    	this.header = header;
    	this.sequence = seq;
    	this.file = file;
    	this.taxon = taxon; 	
    }
    public Long getUID() {
		return UID;
	}

	public void setUID(Long uID) {
		UID = uID;
	}

	public String getTaxon() {
		return taxon;
	}

	public void setTaxon(String taxon) {
		this.taxon = taxon;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}
	
	public MetaFile getFile() {
		return file;
	}
	
	public void setFile(MetaFile file) {
		this.file = file;
	}
	
	public String toString() {
		return this.header + "\n" + this.sequence;
	}
}
