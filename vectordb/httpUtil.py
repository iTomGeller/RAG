from flask import Flask, request, jsonify
from vectordb import add
from vectordb import query
app = Flask(__name__)

@app.route('/save', methods=['POST'])
def save_doc():
    data = request.get_json()  # 获取 JSON 格式的请求体
    if not data:
        return jsonify({"error": "No data provided"}), 400
    try:
        add(data)
        return jsonify({
            "message": "store document successfully"
        }), 200
    except Exception as e:
        print(e)
        return jsonify({
            "message": e
        }), 500

@app.route('/query', methods=['POST'])
def query_doc():
    data = request.get_json()
    print(data)
    if not data:
        return jsonify({"error": "No data provided"}), 400
    try:
        query_text = data.get("query")
        docs = query(query_text)
        title_list = []
        url_list = []
        for group in docs.get("metadatas", []):
            for item in group:
               title_list.append(item["title"])
               url_list.append(item["url"])
        print(docs)
        docs = {
            "documents" : docs["documents"],
            "title": title_list,
            "url": url_list,
        }
        print(docs)
        return jsonify({"message": docs}),200
    except Exception as e:
        print(e)
        return jsonify({"message": e}),500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)