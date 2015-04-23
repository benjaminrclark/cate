package org.cateproject.domain.constants;

public enum DescriptionType {

	gneral("general"),
	morphology("morphology"),
	habit("habit"),
	cytology("cytology"),
	physiology("physiology"),
	sze("size"),
	weight("weight"),
	lifespan("lifespan"),
	lifetime("lifetime"),
	biology("biology"),
	ecology("ecology"),
	habitat("habitat"),
	distribution("distribution"),
	reproduction("reproduction"),
	conservation("conservation"),
	use("use"),
	dispersal("dispersal"),
	cyclicity("cyclicity"),
	lifecycle("lifecycle"),
	migration("migration"),
	growth("growth"),
	genetics("genetics"),
	chemistry("chemistry"),
	diseases("diseases"),
	associations("assiociations"),
	behaviour("behaviour"),
	population("population"),
	management("management"),
	legislation("legislation"),
	threats("threats"),
	typematerial("typematerial"),
	typelocality("typelocality"),
	phylogeny("phylogeny"),
	hybrids("hybrids"),
	literature("literature"),
	culture("culture"),
	vernacular("vernacular"),
	diagnostic("diagnostic");
	
	private String label;
	
	private DescriptionType(String label) {
		this.label = label;
	}

	public static DescriptionType fromString(String string) {
		for(DescriptionType d : DescriptionType.values()) {
			if(d.label.equals(string)) {
				return d;
			}
		}
		throw new IllegalArgumentException("No enum const class org.cateproject.domain.DescriptionType." + string);
	}
}
