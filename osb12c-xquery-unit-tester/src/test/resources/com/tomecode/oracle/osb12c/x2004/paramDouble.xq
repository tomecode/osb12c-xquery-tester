
declare variable $input as xs:double external;

declare function local:paramDouble($input as xs:double) as element()  {
    element{'value'}{$input}
};



local:paramDouble($input)
