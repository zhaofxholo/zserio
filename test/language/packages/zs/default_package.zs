// This is an example of default package without any package definition.

import default_package_import.top.*;

struct DefaultPackageStructure(uint8 numBits)
{
    bit<numBits>    value;
    TopStructure    topStructure;
};
