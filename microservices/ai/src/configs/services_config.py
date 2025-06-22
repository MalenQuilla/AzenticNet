from kink import di
from loguru import logger

from src.repositories.mem0_repository import Mem0Repository
from src.services.chains_service import ChainsService


async def init_services_dependencies():
    di[ChainsService] = ChainsService(Mem0Repository())

    logger.info("Initialized all services dependencies")
