from fastapi import APIRouter, Depends
from kink import di

from src.middlewares.authentication_middleware import WebsocketAuthenticationMiddleware, AuthenticationMiddleware
from src.routers.ai_router import ai_router
from src.utils import configs_data

private_routers = APIRouter(
    dependencies=[Depends(di[AuthenticationMiddleware])],
    prefix=configs_data.get("common.api.prefix")
)

public_routers = APIRouter(
    prefix=configs_data.get("common.api.prefix")
)

private_websocket_routers = APIRouter(
    dependencies=[Depends(di[WebsocketAuthenticationMiddleware])],
    prefix=configs_data.get("common.api.prefix")
)

private_websocket_routers.include_router(ai_router)


@public_routers.post("/logout")
async def logout():
    return
