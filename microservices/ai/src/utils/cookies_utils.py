from starlette.requests import Request

from src.consts.cookies_consts import ACCESS_TOKEN_COOKIE


async def get_access_token_from_cookie(request: Request) -> str:
    return request.cookies.get(ACCESS_TOKEN_COOKIE, None)
