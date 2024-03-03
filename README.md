# ColentinaAI
This is our attempt at creating an AI companion that help students in solving programming questions with tests


## How to run

1. Clone the repository
2. Install the needed python server dependencies:
    ```bash
    pip install --extra-index-url https://packages.jetbrains.team/pypi/p/grazi/jetbrains-ai-platform-public/simple grazie-api-gateway-client==0.0.19
    pip install flask
    ```
3. Run the server:

    ```bash
    GRAZIE_JWT_TOKEN="<your grazie jwt token>" python

    >>> from src import *
    >>> app.run()
    ```

4. Install the plugin in your IntelliJ IDE of choice. You can download it from [here](https://github.com/JetbrainsColentinaTei/ColentinaAI/releases).
5. Done! Now you can use the plugin in your IDE.