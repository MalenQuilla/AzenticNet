from kink import inject
from starlette.requests import Request
from starlette.websockets import WebSocket

from protoc.common_pb2 import AuthoritiesDetailsGrpc
from src.grpc.clients.authentication_client import AuthenticationClient
from src.utils.cookies_utils import get_access_token_from_cookie


@inject
class WebsocketAuthenticationMiddleware:
    def __init__(
            self,
            authentication_client: AuthenticationClient
    ):
        self.__authentication_client = authentication_client

    async def __call__(self, connection: WebSocket):
        access_token = await get_access_token_from_cookie(connection)
        authorities_details: AuthoritiesDetailsGrpc = await (
            self.__authentication_client.verify_authentication(access_token))

        connection.state.authorities = authorities_details


@inject
class AuthenticationMiddleware:
    def __init__(
            self,
            authentication_client: AuthenticationClient
    ):
        self.__authentication_client = authentication_client

    async def __call__(self, connection: Request):
        access_token = await get_access_token_from_cookie(connection)
        authorities_details: AuthoritiesDetailsGrpc = await (
            self.__authentication_client.verify_authentication(access_token))

        connection.state.authorities = authorities_details
