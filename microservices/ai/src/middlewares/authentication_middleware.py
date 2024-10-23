from kink import inject
from starlette.requests import Request

from protoc.common_pb2 import AuthoritiesDetailsGrpc
from src.grpc.clients.authentication_client import AuthenticationClient
from src.utils.cookies_utils import get_access_token_from_cookie


@inject
class AuthenticationMiddleware:
    def __init__(
            self,
            authentication_client: AuthenticationClient
    ):
        self.__authentication_client = authentication_client

    async def __call__(self, request: Request):
        access_token = await get_access_token_from_cookie(request)
        authorities_details: AuthoritiesDetailsGrpc = await (
            self.__authentication_client.verify_authentication(access_token))

        request.state.authorities = authorities_details
