import os
import socket
from grazie.api.client.gateway import AuthType, GrazieApiGatewayClient, GrazieHeaders
from grazie.api.client.chat.prompt import ChatPrompt
from grazie.api.client.endpoints import GrazieApiGatewayUrls
from grazie.api.client.profiles import Profile

token = os.getenv('GRAZIE_JWT_TOKEN')

if token is None:
    raise ValueError("GRAZIE_JWT_TOKEN environment variable is not set. Please set it to your JWT token.")

print(token)

client_ip = socket.gethostbyname(socket.gethostname())
client = GrazieApiGatewayClient(
    url=GrazieApiGatewayUrls.PRODUCTION,
    grazie_jwt_token=token,
    auth_type=AuthType.APPLICATION,
)
response = client.chat(
    chat=(
        ChatPrompt()
        .add_system("You are a helpful assistant.")
        .add_user("Who won the world series in 2020?")
    ),
    profile=Profile.OPENAI_GPT_4,
    headers={
        GrazieHeaders.ORIGINAL_USER_IP: client_ip,
    }
)
print(response.content)
