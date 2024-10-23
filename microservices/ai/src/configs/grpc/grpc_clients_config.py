from kink import di
from loguru import logger

from src.grpc.clients.authentication_client import AuthenticationClient


async def init_grpc_clients_dependencies():
    logger.info("Initialized all grpc clients dependencies")
    di[AuthenticationClient] = AuthenticationClient()
