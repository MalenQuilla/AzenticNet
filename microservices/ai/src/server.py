from contextlib import asynccontextmanager

from fastapi import FastAPI
from loguru import logger
from py_eureka_client import eureka_client

from src.configs.grpc.grpc_clients_config import init_grpc_clients_dependencies
from src.configs.middleware_config import init_middlewares_dependencies
from src.middlewares import exception_handlers
from src.routers import private_routers
from src.utils import configs_data


@asynccontextmanager
async def pre_boot_and_pre_shutdown(app_):
    logger.info("Fastapi app booting...")

    await eureka_client.init_async(
        eureka_server=configs_data.get("eureka.url"),
        app_name=configs_data.get("server.name"),
        instance_port=int(configs_data.get("server.port"))
    )

    await init_middlewares_dependencies()
    await init_grpc_clients_dependencies()

    yield

    logger.info("Fastapi app shutting down...")


app = FastAPI(
    openapi_url=configs_data.get("openapi.url"),
    docs_url=configs_data.get("docs.url"),
    redoc_url=None,
    lifespan=pre_boot_and_pre_shutdown,
    exception_handlers=exception_handlers
)

app.include_router(private_routers)
