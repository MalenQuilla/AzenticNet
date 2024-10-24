from fastapi import HTTPException
from fastapi.responses import ORJSONResponse
from grpc import RpcError
from starlette.requests import Request

from src.consts.grpc_exceptions_dict import grpc_exceptions_dict


async def handle_http_exception(req: Request, ex: HTTPException) -> ORJSONResponse:
    status_code = ex.status_code
    message = ex.detail

    return ORJSONResponse(
        status_code=status_code,
        content={"statusCode": status_code, "message": message}
    )


async def handle_grpc_exception(req: Request, ex: RpcError) -> ORJSONResponse:
    status_code: int = grpc_exceptions_dict.get(ex.code())
    message = ex.details()
    
    return ORJSONResponse(
        status_code=status_code,
        content={"statusCode": status_code, "message": message if message else None}
    )
