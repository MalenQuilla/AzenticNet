from contextlib import asynccontextmanager

from fastapi import FastAPI
from loguru import logger
from py_eureka_client import eureka_client

from src.configs.grpc.grpc_clients_config import init_grpc_clients_dependencies
from src.configs.middleware_config import init_middlewares_dependencies
from src.middlewares import exception_handlers
from src.routers import private_routers


@asynccontextmanager
async def pre_boot_and_pre_shutdown(app_):
    logger.info("Fastapi app booting...")
    await eureka_client.init_async(
        eureka_server="http://localhost:8761/eureka",
        app_name="ai",
        instance_port=7002
    )

    await init_middlewares_dependencies()
    await init_grpc_clients_dependencies()
    yield

    logger.info("Fastapi app shutting down...")


app = FastAPI(
    openapi_url="/api/v1/ai/openapi.json",
    docs_url="/api/v1/ai/docs",
    redoc_url=None,
    lifespan=pre_boot_and_pre_shutdown,
    exception_handlers=exception_handlers
)

app.include_router(private_routers)
