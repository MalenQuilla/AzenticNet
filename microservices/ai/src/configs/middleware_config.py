from kink import di
from loguru import logger

from src.middlewares.authentication_middleware import WebsocketAuthenticationMiddleware, AuthenticationMiddleware


async def init_middlewares_dependencies():
    di[WebsocketAuthenticationMiddleware] = WebsocketAuthenticationMiddleware()
    di[AuthenticationMiddleware] = AuthenticationMiddleware()

    logger.info("Initialized all middlewares dependencies")
