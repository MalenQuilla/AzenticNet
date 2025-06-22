from typing import Dict, List

from kink.inject import inject
from langchain_core.messages import SystemMessage, HumanMessage
from langchain_core.prompts import ChatPromptTemplate, MessagesPlaceholder
from langchain_ollama import ChatOllama

from src.repositories.mem0_repository import Mem0Repository


@inject
class ChainsService:
    def __init__(self, mem0_repo: Mem0Repository):
        self.__llm = ChatOllama(model="llama3.2", temperature=0)
        self.__mem0_repo = mem0_repo

    @staticmethod
    def generate_prompts(prompts: str):
        return ChatPromptTemplate.from_messages([
            SystemMessage(content="""You are a helpful travel agent AI. Use the provided context to personalize your responses and remember user preferences and past interactions. 
                                    Provide travel recommendations, itinerary suggestions, and answer questions about destinations. 
                                    If you don't have specific information, you can make general suggestions based on common travel knowledge."""),
            MessagesPlaceholder(variable_name="context"),
            HumanMessage(content=prompts)
        ])

    def generate_response(self, input_text: str, context: List[Dict]):
        chain = ChainsService.generate_prompts(input_text) | self.__llm

        response = chain.astream({
            "context": context,
            "input": input_text,
        })

        return response

    def chat(self, input_text: str, user_id: int):
        context = self.__mem0_repo.get_mem_by_user_id(input_text, user_id)

        response = self.generate_response(input_text, context)

        self.__mem0_repo.save_mem_by_user_id(input_text, response, user_id)

        return response
