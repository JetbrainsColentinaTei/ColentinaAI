import os
import socket
from src.prompt import BasePrompt
from grazie.api.client.gateway import AuthType, GrazieApiGatewayClient, GrazieHeaders
from grazie.api.client.chat.prompt import ChatPrompt
from grazie.api.client.endpoints import GrazieApiGatewayUrls
from grazie.api.client.profiles import Profile


class GrazieApiGateWayClient:
    def __init__(self):
        self.token = os.getenv('GRAZIE_JWT_TOKEN')
        self.ip = socket.gethostbyname(socket.gethostname())
        if self.token is None:
            raise ValueError("GRAZIE_JWT_TOKEN environment variable is not set. Please set it to your JWT token.")

    def chat(self, prompt: BasePrompt):
        return self._chat(str(prompt))

    def _chat(self, text: str):
        client = GrazieApiGatewayClient(
            url=GrazieApiGatewayUrls.STAGING,
            grazie_jwt_token=self.token,
            auth_type=AuthType.APPLICATION,
        )
        response = client.chat(
            chat=(
                ChatPrompt()
                .add_system("You are an assistant that helps students solve educational programming tasks without "
                            "providing the answer.")
                .add_user(text)
            ),
            profile=Profile.OPENAI_GPT_4,
            headers={
                GrazieHeaders.ORIGINAL_USER_IP: self.ip,
            }
        )
        return response.content
