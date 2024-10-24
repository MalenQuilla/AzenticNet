from fastapi import APIRouter
from starlette.requests import Request

from src.utils import configs_data

ai_router = APIRouter(
    prefix=configs_data.get("api.prefix")
)


@ai_router.get("/hello")
async def hello(request: Request):
    return "it works!"
