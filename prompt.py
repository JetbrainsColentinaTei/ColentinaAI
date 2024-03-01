class BasePrompt:
    def __init__(self, task: str):
        self.task = task
        self.prompt = None

    def __str__(self):
        return self.prompt


class ErrorAnalysisPrompt(BasePrompt):
    def __init__(self, code: str, task: str, error: str):
        super().__init__(task)
        self.prompt = (f"The task is: {task}\nThe student's code is:\n{code}\nThe error message is: {error}\n"
                       f"Provide a hint that guides the student towards understanding the error and fixing it, "
                       f"without directly giving the solution. Consider potential causes of the error and offer "
                       f"debugging strategies.")


class CodeAnalysisPrompt(BasePrompt):
    def __init__(self, code: str, task: str = None):
        super().__init__(task)
        self.prompt = f"The student's code is:\n{code}\n"
        if task is not None:
            self.prompt += f"The task is: {task}\n"
        self.prompt += ("Analyze the code for readability and adherence to common style conventions. "
                        "Provide suggestions for improvement, such as better variable naming, formatting, "
                        "or commenting. Explain the benefits of good code style. ")


class ConceptAnalysisPrompt(BasePrompt):
    def __init__(self, concept: str):
        super().__init__(concept)
        self.prompt = f"The following concept is relevant: {concept}\n"
        self.prompt += ("Explain the concept clearly and concisely. "
                        "Provide examples to illustrate how it's used in programming. "
                        "If possible, relate it to the student's current task. ")
