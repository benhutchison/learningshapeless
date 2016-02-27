package learnshapeless


import Data._

import shapeless._

class HListsTest extends org.specs2.mutable.Specification with Assert {

  import HLists._

  eg {mustEqual(ex_newton, "Newton" :: 1642 :: England :: HNil)}

  eg {mustEqual(ex_prepend, "Isaac" :: "Newton" :: 1642 :: England :: HNil)}

  eg {mustEqual(ex_tuple, ("Newton", 1642, England))}

  eg {mustEqual(ex_tuple_append, ("Newton", 1642, England, Calculus))}

  eg {mustEqual(ex_from_tuple, "Newton" :: 1642 :: England :: Calculus :: HNil)}

  eg {mustEqual(ex_poly,  "NEWTON" :: 1642 :: England :: HNil)}

  eg {mustEqual(ex_poly_country,  "Newton" :: 1642 :: England :: HNil)}

  eg {mustEqual(ex_poly_country_element_3rd,  England)}

  eg {mustEqual(ex_to_firstname_by_index,  ("Newton", "Isaac" :: 1642 :: England :: HNil))}
}
