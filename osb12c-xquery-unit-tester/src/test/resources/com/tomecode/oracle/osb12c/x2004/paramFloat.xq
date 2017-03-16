

declare variable $input as xs:float external;

declare function local:paramFloat($input as xs:float) as element()  {
    element{'value'}{$input}
};



local:paramFloat($input)
