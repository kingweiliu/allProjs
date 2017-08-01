from flask import Flask
from flask import request

app = Flask(__name__)

@app.route('/')
def hello_world():
    if request.method == 'POST':
        print(request.form)
    return 'Hello World!'

if __name__ == '__main__':
    app.run()