from typing import AsyncIterator

from kink import inject
from langchain_core.messages import BaseMessage
from loguru import logger
from mem0 import MemoryClient

from src.utils import configs_data


@inject
class Mem0Repository:
    def __init__(self):
        self.__client = MemoryClient(api_key=configs_data.get("mem0.api.key"))
        logger.info("Initialized Mem0Repository")

    def get_mem_by_user_id(self, key: str, user_id: int):
        """
        Retrieves a memory by user ID from the Mem0 service.

        :param user_id: The user ID to retrieve the memory for.
        :param key: The key to retrieve the memory.
        :return: The memory associated with the given key.
        """
        memories = self.__client.search(key, user_id=user_id)

        serialized_memories = ' '.join([mem["memory"] for mem in memories])

        return [
            {
                "role": "assistant",
                "content": f"Relevant information: {serialized_memories}"
            },
            {
                "role": "user",
                "content": key
            }
        ]

    async def save_mem_by_user_id(self, user_mem: str, assistant_mem: AsyncIterator[BaseMessage], user_id: int):
        """
        Saves a memory for a user in the Mem0 service.

        :param user_mem: The user's memory to save.
        :param assistant_mem: The assistant's memory to save.
        :param user_id: The user ID to associate with the memory.
        :return: The response from the Mem0 service.
        """

        memory: str = ""
        async for message in assistant_mem:
            memory += message.content

        messages = [
            {"role": "user", "content": user_mem},
            {"role": "assistant", "content": memory}
        ]
        return self.__client.add(messages, user_id=user_id)
