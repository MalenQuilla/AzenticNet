from fastapi import HTTPException
from grpc import RpcError

from src.middlewares.exception_handler import handle_http_exception, handle_grpc_exception

exception_handlers = {
    HTTPException: handle_http_exception,
    RpcError: handle_grpc_exception,
}
