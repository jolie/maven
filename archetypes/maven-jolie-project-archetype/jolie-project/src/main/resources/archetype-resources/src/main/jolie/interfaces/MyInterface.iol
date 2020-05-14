include "../types/MyType.iol"

interface MyInterface {
    RequestResponse:
        myOperation( MyType )( MyType )
}