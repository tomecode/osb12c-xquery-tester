
declare namespace ns1="http://ns.erstegroupit.sk/osb/symbols/v1";
declare namespace tome="http://tome/custom/v1/xpath";

declare function local:funcTransformationstandingordersearchinputvariable() as element()  {
    element{'response'}{tome:helloCustom('input')}
};

local:funcTransformationstandingordersearchinputvariable()
