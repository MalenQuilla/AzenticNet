import grpc
from grpc import Channel


class AbstractClient:
    def __init__(self, url):
        self.__channel: Channel = grpc.insecure_channel(url)

    def __del__(self):
        self.__channel.close()

    def get_channel(self):
        return self.__channel
