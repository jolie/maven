/* do not alter this file */

include "resources/${packageName}.${artifactId}/interfaces/${interfaceName}.iol"

outputPort ${artifactId}Port {
    Interfaces: ${interfaceName}
}

main {
    nullProcess
}