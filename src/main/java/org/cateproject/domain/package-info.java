@org.hibernate.annotations.GenericGenerators({
  @org.hibernate.annotations.GenericGenerator(name="table-hilo", strategy="org.hibernate.id.MultipleHiLoPerTableGenerator", parameters={
		  @org.hibernate.annotations.Parameter(value="128", name="max_low")
  })
})
package org.cateproject.domain;