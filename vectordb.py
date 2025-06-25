import chromadb
import hashlib
chroma_client = chromadb.PersistentClient(path="vector.db")
collection = chroma_client.get_or_create_collection(name="qwen-store")

def get_str_hash(s: str) -> str:
    return hashlib.md5(s.encode('utf-8')).hexdigest()

def add(data) :
    document = data.get("document")
    metadata = data.get("metadata")
    if document and metadata:
        collection.add(documents=[document], metadatas=[metadata],ids=[get_str_hash(document)])


def query(query, topk = 10) :
    results = collection.query(
        query_texts=query,
        n_results=topk
    )
    return results

#测试用
add({"document":"123","metadata":{"title":"say hello","url":"www.baidu.com"}})
print(query("what about the food?"))