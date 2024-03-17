# ColentinaAI
### 2nd Place in the JetBrains Hackathon
This is our attempt at creating an AI companion that helps students solve programming questions with tests.


## How to run

1. Clone the repository
2. Install the needed Python server dependencies:
    ```bash
    pip install --extra-index-url https://packages.jetbrains.team/pypi/p/grazi/jetbrains-ai-platform-public/simple grazie-api-gateway-client==0.0.19
    pip install flask
    ```
3. Run the server:

    ```bash
    GRAZIE_JWT_TOKEN="<your grazie jwt token>" python src/__init__.py
    ```

4. Install the plugin in your IntelliJ IDE of choice. You can download it from [here](https://github.com/JetbrainsColentinaTei/ColentinaAI/releases).
5. Done! Now you can use the plugin in your IDE.

## Functionality
We allow 3 different types of prompts that are sent from the front-end plugin:
1. **Error Analysis**: Our plugin allows the students to enter in a prompt the task they are doing and the error they encounter, and the model will automatically get the student's code and give back hints on how to fix the errors without directly giving the solution.
2. **Style Check**: Checks the student's code for both weak and strong points from the code style perspective returning appropriate feedback.
3. **Concept Analysis**: It explains the concept that the student inputs and relates it to the current task and code.
