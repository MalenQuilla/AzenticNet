from fastapi import APIRouter
from starlette.requests import Request

ai_router = APIRouter()


@ai_router.get("/hello")
async def hello(request: Request):
    return "it works!"
