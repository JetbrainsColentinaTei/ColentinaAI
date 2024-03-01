from api import GrazieApiGateWayClient
from prompt import ErrorAnalysisPrompt, CodeAnalysisPrompt, ConceptAnalysisPrompt

api = GrazieApiGateWayClient()

prompt = CodeAnalysisPrompt("def foo(x):\n    return x + 1", "Write a function that adds 1 to the input.")
print(api.chat(prompt))
