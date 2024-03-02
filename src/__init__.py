from flask import Flask, request
from src.api import GrazieApiGateWayClient
from src.prompt import ErrorAnalysisPrompt, CodeAnalysisPrompt, ConceptAnalysisPrompt

api = GrazieApiGateWayClient()
app = Flask(__name__)


@app.route('/chat/error', methods=['POST'])
def chat_err():
    data = request.get_json()

    if data is None:
        return 'Invalid json data', 400

    code = data.get('code')
    error = data.get('error')
    task = data.get('task')

    if code is None or error is None:
        return 'Missing required fields', 400

    text = ErrorAnalysisPrompt(code, error, task)

    return api.chat(text)


@app.route('/chat/style', methods=['POST'])
def chat_style():
    data = request.get_json()

    if data is None:
        return 'Invalid json data', 400

    code = data.get('code')
    task = data.get('task')

    if code is None:
        return 'Missing required fields', 400

    text = CodeAnalysisPrompt(code, task)

    return api.chat(text)


@app.route('/chat/concept', methods=['POST'])
def chat_concept():
    data = request.get_json()

    if data is None:
        return 'Invalid json data', 400
    concept = data.get('concept')
    code = data.get('code')
    task = data.get('task')

    if concept is None:
        return 'Missing required fields', 400

    text = ConceptAnalysisPrompt(concept, code, task)
    print(text)
    return api.chat(text)


if __name__ == '__main__':
    app.run()
