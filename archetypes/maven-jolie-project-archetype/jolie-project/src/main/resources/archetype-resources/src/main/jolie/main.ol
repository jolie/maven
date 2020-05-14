include "interfaces/MyInterface.iol"

execution{ concurrent }

inputPort MyInputPort {
    Location: "auto:ini:/Locations/MyInputPort:file:locations.ini"
    Protocol: sodep
    Interfaces: MyInterface
}

main {
    nullProcess
}