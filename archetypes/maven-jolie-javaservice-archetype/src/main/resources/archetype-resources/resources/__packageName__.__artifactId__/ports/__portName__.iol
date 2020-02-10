/* do not alter this file, it contains the outputport declaration which imports the interface of the javaservice */

include "../interfaces/${interfaceName}.iol"


outputPort ${artifactId}Port {
    Interfaces: ${interfaceName}
}

embedded {
    Java: 
        "${packageName}.${artifactId}" in ${artifactId}Port 
}

