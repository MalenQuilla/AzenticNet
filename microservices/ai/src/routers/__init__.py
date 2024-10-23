from fastapi import APIRouter, Depends
from kink import di

from src.middlewares.authentication_middleware import AuthenticationMiddleware
from src.routers.ai_router import ai_router

private_routers = APIRouter(
    dependencies=[Depends(di[AuthenticationMiddleware])]
)

private_routers.include_router(ai_router)