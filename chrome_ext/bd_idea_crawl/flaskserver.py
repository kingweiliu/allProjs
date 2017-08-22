from flask import Flask, jsonify, render_template
from flask import request
import json

app = Flask(__name__)


@app.route('/abc', methods=['GET', 'POST'])
def hello_world():
    if request.method == 'POST':
        data = json.loads(request.form["text"])
        print(data["cate"])
        file = open("abc", "a")
        file.write(json.dumps(data)+"\n")
        file.close()
        return jsonify({'success':True});

@app.route("/")
def root():
	return render_template("index.html")


if __name__ == '__main__':
    app.run()
