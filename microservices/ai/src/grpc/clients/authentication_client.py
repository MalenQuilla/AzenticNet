from kink import inject

from protoc.authentication.authentication_pb2 import VerifyAuthenticationRequest
from protoc.authentication.authentication_pb2_grpc import AuthenticationControllerStub
from protoc.common_pb2 import AuthoritiesDetailsGrpc
from src.grpc.clients.abstract_client import AbstractClient
from src.utils import configs_data


@inject
class AuthenticationClient(AbstractClient):
    def __init__(self):
        host = configs_data.get("cds.grpc.authentication.host")
        port = configs_data.get("cds.grpc.authentication.port")
        super().__init__(f"{host}:{port}")
        self.__stub = AuthenticationControllerStub(self.get_channel())

    async def verify_authentication(self, access_token: str) -> AuthoritiesDetailsGrpc:
        request = VerifyAuthenticationRequest(accessToken=access_token)
        response: AuthoritiesDetailsGrpc = self.__stub.verifyAuthentication(request)

        return response
