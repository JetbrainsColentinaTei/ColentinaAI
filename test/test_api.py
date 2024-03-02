import unittest
from src.prompt import ErrorAnalysisPrompt, CodeAnalysisPrompt, ConceptAnalysisPrompt
from src.api import GrazieApiGateWayClient


class TestApi(unittest.TestCase):
    def test_api(self):
        prompt = ErrorAnalysisPrompt("def f(x)\n    return x + 1", "Write a function that adds 1 to the input",
                                     "SyntaxError: invalid syntax")
        api = GrazieApiGateWayClient()
        response = api.chat(prompt)
        print("\nThe response given by GPT is:\n\n", response, "\n")
        self.assertIsNotNone(response)

    def test_prompt_error(self):
        prompt = ErrorAnalysisPrompt("code", "error", "task")
        self.assertEqual(str(prompt), ('The task is: task\n'
                                       "The student's code is:\n"
                                       'code\n'
                                       '\n'
                                       'The error message is: error\n'
                                       'Provide a hint that guides the student towards understanding the error and '
                                       'fixing it, without directly giving the solution. Consider potential causes '
                                       'of the error and offer debugging strategies.'))

    def test_prompt_code(self):
        prompt = CodeAnalysisPrompt("code", "task")
        self.assertEqual(str(prompt), ('The task is: task\n'
                                       "The student's code is:\n"
                                       'code\n'
                                       'Analyze the code for readability and adherence to common style conventions. '
                                       'Provide suggestions for improvement, such as better variable naming, '
                                       'formatting, or commenting. While not providing a direct code solution.'))

    def test_prompt_concept(self):
        prompt = ConceptAnalysisPrompt("concept", "code", "task")
        self.assertEqual(str(prompt), ('The task is: task\n'
                                       "The student's code is:\n"
                                       'code\n'
                                       'The following concept is relevant: concept\n'
                                       'Explain the concept clearly and concisely. Provide examples to illustrate '
                                       "how it's used in programming. If possible, relate it to the student's "
                                       'current task. '))

    def test_prompt_concept2(self):
        prompt = ConceptAnalysisPrompt("concept", task="task")
        self.assertEqual(str(prompt), ('The task is: task\n'
                                       'The following concept is relevant: concept\n'
                                       'Explain the concept clearly and concisely. Provide examples to illustrate '
                                       "how it's used in programming. If possible, relate it to the student's "
                                       'current task. '))
