from fastapi import APIRouter, Depends
from kink import di
from starlette.websockets import WebSocket

from protoc.common_pb2 import AuthoritiesDetailsGrpc
from src.services.chains_service import ChainsService

ai_router = APIRouter(
    prefix="/ai"
)


@ai_router.websocket("/chat")
async def chat(websocket: WebSocket,
               chains_service: ChainsService = Depends(lambda: di[ChainsService])):
    authorities: AuthoritiesDetailsGrpc = websocket.state.authorities
    await websocket.accept()

    while True:
        message: str = await websocket.receive_text()

        response = chains_service.chat(message, authorities.userId)

        async for chunk in response:
            await websocket.send_text(chunk.content)
