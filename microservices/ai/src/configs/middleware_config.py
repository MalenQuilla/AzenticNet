from kink import di
from loguru import logger
from src.middlewares.authentication_middleware import AuthenticationMiddleware

async def init_middlewares_dependencies():
    logger.info("Initialized all middlewares dependencies")
    di[AuthenticationMiddleware] = AuthenticationMiddleware()