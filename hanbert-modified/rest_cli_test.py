

import urllib3
import requests
import json
import time

TBAI_API_URL = "http://122.32.43.13:6666/mrc"
ACCESS_KEY = "kuvBCweSsRkFu6EfcgPxG6dMNxh7nL78APgMARBymUtVN4ywdWq2xzL3TrGcQ7Xh"



def test1():
    context = '운학터널 세종 Sta.83+715의 검측결과 통보서는 @@SJ83715202001310@@이다'
    question = '운학터널 세종 Sta.83+683의 막장관찰도는?'

    questions = [question]
    requestJson = {
        "access_key": ACCESS_KEY,
        "argument": {
            "context": context,
            "questions": questions
        }
    }

    http = urllib3.PoolManager()
    response = http.request(
        "POST",
        TBAI_API_URL,
        headers={"Content-Type": "application/json; charset=UTF-8"},
        body=json.dumps(requestJson)
    )

    results = json.loads(response.data)

    print("[responseCode] " + str(response.status))
    print("[responBody]")
    print(json.dumps(results, indent=4, ensure_ascii=False))


def test2():
    #f = open('/data/data/bert/hiway/hi.csv', 'r',  encoding='utf-8')
    #f = open('/data/data/bert/hiway/실무작업데이터.csv', 'r', encoding='utf-8')
    #f = open('/data/data/bert/hiway/막장관찰도.csv', 'r', encoding='utf-8')
    f = open('/data/data/bert/hiway/막장관찰도-small.csv', 'r', encoding='utf-8')

    print(f)
    context = f.read()

    context = context.replace("\n", "")
    context = context.replace("\r", "")
    context = context.replace("\"", " ")
    context = context.replace(",", ", ")

    print('context = ', context)
    print('context size = ', len(context))

    #question = '운학터널 세종 Sta.83+715의 검측결과 통보서는 무엇입니까?'
    #question = '운학터널 세종 Sta.83+715의 검측결과 통보서는 무엇입니까?'
    #question = '운학터널 포천 Sta.83+735의 검측결과 통보서는 무엇입니까?'
    #question = '운학터널 포천 Sta.83+715의 검측결과 통보서는 무엇입니까?'
    #question = '운학터널 포천 Sta.83+676.9의 검측결과는'

    #question = '운학터널 세종 Sta.83+667의 막장관찰도는?'
    #question = '운학터널 세종 Sta.83+693의 막장관찰도는?'
    question = '운학터널 세종 Sta.83+683의 막장관찰도는?'

    #question = '운학터널 세종 Sta.83+669의 막장관찰도의 특이사항은 무엇인가?'
    #question = '운학터널 세종 Sta.83+682의 막장관찰도의 특이사항은 무엇인가?'

    json_str = '{"context": "' + context + '", "question": "' + question + '" }'
    #data = json.dumps(json_str)
    data = json.loads(json_str)

    questions = [question]
    requestJson = {
        "access_key": ACCESS_KEY,
        "argument": {
            "context": context,
            "questions": questions
        }
    }

    http = urllib3.PoolManager()
    response = http.request(
        "POST",
        TBAI_API_URL,
        headers={"Content-Type": "application/json; charset=UTF-8"},
        body=json.dumps(requestJson)
    )

    results = json.loads(response.data)

    print("[responseCode] " + str(response.status))
    print("[responBody]")
    print(json.dumps(results, indent=4, ensure_ascii=False))

if __name__ == '__main__' :
    # test1()
    test2()
