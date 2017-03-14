
declare variable $input as xs:boolean external;

declare function local:paramBoolean($input as xs:boolean) as element()  {
    element{'value'}{$input}
};



local:paramBoolean($input)
