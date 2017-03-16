declare function local:buildInUUID() as element()  {
    element{'uuid'}{fn-bea:uuid()}
};

local:buildInUUID()
