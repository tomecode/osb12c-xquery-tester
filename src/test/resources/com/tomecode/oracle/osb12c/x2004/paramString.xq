
declare variable $input as xs:string external;

declare function local:paramString($input as xs:string) as element()  {
    element{'value'}{$input}
};



local:paramString($input)
