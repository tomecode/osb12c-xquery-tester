declare function local:customFunction() as element()  {
    element{'uuid'}{fn-bea:hello('xquery world')}
};

local:customFunction()
