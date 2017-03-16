
declare variable $input as xs:integer external;

declare function local:paramInt($input as xs:integer) as element()  {
    element{'value'}{$input}
};



local:paramInt($input)
