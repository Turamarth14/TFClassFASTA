package de.sybig.TFClassFASTA.core;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
    @NamedQuery(name = "Fasta.getByUID", query = "SELECT fst FROM "
            + "Fasta fst WHERE fst.UID = :UID"),
    @NamedQuery(name = "Fasta.getByTAXON", query = "SELECT fst FROM "
            + "Fasta fst WHERE fst.taxon = :TAXON "
    		+ "AND (:TYPE is null OR fst.type = :TYPE) "
            + "AND (:ALIGN is null OR fst.alignment = :ALIGN) "),
    @NamedQuery(name = "Fasta.getByTYPE", query = "SELECT fst FROM "
            + "Fasta fst WHERE fst.type = :TYPE "
            + "AND (:TAXON is null OR fst.taxon = :TAXON) "
            + "AND (:ALIGN is null OR fst.alignment = :ALIGN) "),
    @NamedQuery(name = "Fasta.getByALIGNMENT", query = "SELECT fst FROM "
            + "Fasta fst WHERE fst.alignment = :ALIGNMENT "
            + "AND (:TAXON is null OR fst.taxon = :TAXON) "
            + "AND (:TYPE is null OR fst.type = :TYPE) ")
})
public class Fasta {
	
    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)	
	private Long UID;
    
    private String taxon;
    private String type;
    @Enumerated(EnumType.STRING)
    private Alignment alignment;
    private String sequence;
    private String version;
    private String header;
    private String tfclassID;
    
    public Fasta() {
    	
    }
    public Fasta(String header, String seq) {
    	this.header = header;
    	this.sequence = seq;
    	this.alignment = Alignment.TYP1;
    	this.taxon = "Default";
    	this.type = "Default";
    	this.version = "Default";
    	this.tfclassID = "Default";
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Alignment getAlignment() {
		return alignment;
	}

	public void setAlignment(Alignment alignment) {
		this.alignment = alignment;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getTfclassID() {
		return tfclassID;
	}

	public void setTfclassID(String tfclassID) {
		this.tfclassID = tfclassID;
	}

	public enum Alignment{
    	TYP1, TYP2, TYP3, TYPINVALID;
    	@Override
    	public String toString(){
    	    switch (this) {
    	    case TYP1:
    		return "typ1";
    	    case TYP2:
    		return "typ2";
    	    case TYP3:
    	    return "typ3";
    	    case TYPINVALID:
    	    return "invalid";
    	    }
    	    return "";
    	}
    	public static Alignment getEnum(String value) {
    		if(value == null) {return null;}
    		try {
    			return Alignment.valueOf(value);
    		}
    		catch(IllegalArgumentException e) {
    			return TYPINVALID;
    		}
    	}
    }
	
	public String toString() {
		return this.header + "\n" + this.sequence;
	}
}
