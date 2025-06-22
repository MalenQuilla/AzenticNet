from kink import di
from loguru import logger

from src.repositories.mem0_repository import Mem0Repository


async def init_repositories_dependencies():
    di[Mem0Repository] = Mem0Repository()

    logger.info("Initialized all repositories dependencies")
