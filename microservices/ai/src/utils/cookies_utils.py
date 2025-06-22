from typing import Union

import loguru
from starlette.requests import Request
from starlette.websockets import WebSocket

from src.consts.cookies_consts import ACCESS_TOKEN_COOKIE


async def get_access_token_from_cookie(connection: Union[WebSocket, Request]) -> str:
    return connection.cookies.get(ACCESS_TOKEN_COOKIE, None)
