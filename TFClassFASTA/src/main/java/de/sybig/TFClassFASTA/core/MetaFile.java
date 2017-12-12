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
    @NamedQuery(name = "MetaFile.getByTFCLASS", query = "Select file FROM "
    		+ "MetaFile file where file.alignment = :ALIGNMENT "
    		+ "And file.tfclassID = :TFCLASSID "
    		+ "AND file.type = :TYPE "
    		+ "AND file.version = :VERSION"),
    @NamedQuery(name = "MetaFile.getNewestByTFCLASS", query = "Select file FROM "
    		+ "MetaFile file where file.alignment = :ALIGNMENT "
    		+ "AND file.tfclassID = :TFCLASSID "
    		+ "AND file.type = :TYPE "
    		+ "ORDER BY file.version DESC")
})
public class MetaFile {

	@Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)	
	private Long UID;

	@Enumerated(EnumType.STRING)
    private Type type;
    @Enumerated(EnumType.STRING)
    private Alignment alignment;
    private Long version;
    private String tfclassID;
    private String source;
    
    public MetaFile() {
    	this.alignment = Alignment.Not_Aligned;
    	this.type = Type.Protein;
    	this.version = -1l;
    	this.tfclassID = "Default";
    	this.source = "Default";
    }
    public MetaFile(String align, String type, String tfclassID, String source, Long version) {
    	this.alignment = Alignment.getEnum(align);
    	this.type = Type.getEnum(type);
    	this.version = version;
    	this.tfclassID = tfclassID;
    	this.source = source;  	
    }
    public Long getUID() {
		return UID;
	}

	public void setUID(Long uID) {
		UID = uID;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Alignment getAlignment() {
		return alignment;
	}

	public void setAlignment(Alignment alignment) {
		this.alignment = alignment;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public String getTfclassID() {
		return tfclassID;
	}

	public void setTfclassID(String tfclassID) {
		this.tfclassID = tfclassID;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	public enum Alignment{
    	Phyml, Prank, Not_Aligned;
    	@Override
    	public String toString(){
    	    switch (this) {
    	    case Phyml:
    		return "Phyml";
    	    case Prank:
    	    return "Prank";
    	    case Not_Aligned:
    	    return "not aligned";
    	    }
    	    return "";
    	}
    	public static Alignment getEnum(String value) {
    		if(value == null) {return null;}
    		try {
    			return Alignment.valueOf(value);
    		}
    		catch(IllegalArgumentException e) {
    			return Not_Aligned;
    		}
    	}
    }
	
	public enum Type{
    	Protein, DBD, Modules;
    	@Override
    	public String toString(){
    	    switch (this) {
    	    case Protein:
    		return "Protein";
    	    case DBD:
    	    return "DBD";
    	    case Modules:
    	    return "Modules";
    	    }
    	    return "";
    	}
    	public static Type getEnum(String value) {
    		if(value == null) {return null;}
    		try {
    			return Type.valueOf(value);
    		}
    		catch(IllegalArgumentException e) {
    			return null;
    		}
    	}
    }
	
	public String toString() {
		return this.source;
	}	
}
