declare namespace ns1="http://hello";

declare variable $index as xs:unsignedInt external;
declare variable $input2.payload as element()  external;

declare function local:funcTransformationstandingordersearchinputvariable($index as xs:unsignedInt, 
                                                                          $input2.payload as element()) 
                                                                          as element()  {
    let $rbTranAcctQry :=$input2.payload/helloCollection[$index]
    return(
      element{'ns1:StandingOrderSearchRequest'}{
        element{'hello'}{
        }
      }
    )
};

local:funcTransformationstandingordersearchinputvariable($index, $input2.payload)
