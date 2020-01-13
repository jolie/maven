include "interfaces/MyInterface.iol"

inputPort MyInputPort {
    Location: "socket://localhost:8000"
    Protocol: sodep
    Interfaces: MyInterface
}

main {
    nullProcess
}