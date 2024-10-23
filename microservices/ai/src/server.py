from contextlib import asynccontextmanager

from fastapi import FastAPI
from loguru import logger

from src.configs.grpc.grpc_clients_config import init_grpc_clients_dependencies
from src.configs.middleware_config import init_middlewares_dependencies
from src.middlewares import exception_handlers
from src.routers import private_routers


@asynccontextmanager
async def pre_boot_and_pre_shutdown(app_):
    logger.info("Fastapi app booting...")
    await init_middlewares_dependencies()
    await init_grpc_clients_dependencies()
    yield

    logger.info("Fastapi app shutting down...")


app = FastAPI(
    lifespan=pre_boot_and_pre_shutdown,
    exception_handlers=exception_handlers,
)

app.include_router(private_routers)
