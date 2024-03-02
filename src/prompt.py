class BasePrompt:
    def __init__(self, code: str = None, task: str = None):
        self.task = task
        self.code = code
        self.prompt = ""
        if task:
            self.prompt = f"The task is: {task}\n"
        if code:
            self.prompt += f"The student's code is:\n{code}\n"

    def __str__(self):
        return self.prompt


class ErrorAnalysisPrompt(BasePrompt):
    def __init__(self, code: str, error: str, task: str = None):
        super().__init__(code, task)
        self.prompt = (f"{super().__str__()}\nThe error message is: {error}\n"
                       f"Provide a hint that guides the student towards understanding the error and fixing it, "
                       f"without directly giving the solution. Consider potential causes of the error and offer "
                       f"debugging strategies.")


class CodeAnalysisPrompt(BasePrompt):
    def __init__(self, code: str, task: str = None):
        super().__init__(code, task)
        self.prompt = f"{super().__str__()}"
        self.prompt += ("Analyze the code for readability and adherence to common style conventions. "
                        "Provide suggestions for improvement, such as better variable naming, formatting, "
                        "or commenting. While not providing a direct code solution.")


class ConceptAnalysisPrompt(BasePrompt):
    def __init__(self, concept: str, code: str = None, task: str = None):
        super().__init__(code, task)
        self.prompt = f"{super().__str__()}"
        self.prompt += f"The following concept is relevant: {concept}\n"
        self.prompt += ("Explain the concept clearly and concisely. "
                        "Provide examples to illustrate how it's used in programming. "
                        "If possible, relate it to the student's current task. ")
