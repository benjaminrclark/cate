@org.hibernate.annotations.GenericGenerators({
  @org.hibernate.annotations.GenericGenerator(name="table-hilo", strategy="org.hibernate.id.MultipleHiLoPerTableGenerator", parameters={
		  @org.hibernate.annotations.Parameter(value="128", name="max_low")
  })
})
@org.hibernate.annotations.TypeDefs({
	  @org.hibernate.annotations.TypeDef(name = "dateTimeUserType", typeClass = org.jadira.usertype.dateandtime.joda.PersistentDateTime.class),
	  @org.hibernate.annotations.TypeDef(name = "languageUserType", typeClass = org.cateproject.domain.convert.jpa.LanguageUserType.class)
})
package org.cateproject.domain;