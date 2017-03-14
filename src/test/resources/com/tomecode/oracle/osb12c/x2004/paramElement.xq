

declare variable $input as element() external;

declare function local:paramElement($input as element()) as element()  {
    element{'value'}{$input/*}
};

local:paramElement($input)
