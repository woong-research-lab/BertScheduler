

import requests
import json
import time


url = 'http://192.168.10.2:5000/query_data'


def test1():
    data = {'context': '이것은 쿼리입니다', 'question': '이것은 무었입니까?'}

    resp = requests.post(url, data=data)
    #print(resp['text'])
    print(resp.json())

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

    print(json_str)
    resp = requests.post(url, data=data)
    print(resp.json())


def test3():
    data = {'context': '운학터널 세종 Sta.83+715의 검측결과 통보서는 @@SJ83715202001310@@이다.,운학터널 세종 Sta.83+715의 검측일자는 2020년 01월 31일이다.,운학터널 세종 Sta.83+715의 검측결과는 지보 타입 L-3로 결정한다.', 'question': '운학터널 포천 Sta.83+715의 검축결과는?'}

    print(data)
    resp = requests.post(url, data=data)
    print(resp.json())



def test4():

    #context = '운학터널 세종 Sta.83+715의 검측결과 통보서는 @@SJ83715202001310@@이다.,운학터널 세종 Sta.83+715의 검측일자는 2020년 01월 31일이다.,운학터널 세종 Sta.83+715의 검측결과는 지보 타입 L-3로 결정한다.'
    context = '운학터널 세종 Sta.83+715의 검측결과 통보서는 @@SJ83715202001310@@이다'
    #question = '운학터널 포천 Sta.83+715의 검측결과 통보서는?'
    #question = '운학터널 포천 Sta.83+715의 검축일자는?'
    question = '운학터널 포천 Sta.83+715의 검축결과는?'

    json_str = '{"context": "' + context + '", "question": "' + question + '" }'
    #data = json.dumps(json_str, ensure_ascii=False)
    #data = json.dumps(json_str)
    data = json.loads(json_str)

    print(data)
    resp = requests.post(url, data=data)
    print(resp.status_code)
    print(resp.json())


# EUC-KR을 UTF-8로 변환
def euc2utf(input):
    return str(input, 'utf-8')


if __name__ == '__main__':

    start = time.time()
    #test1()
    test2()
    #test3()
    #test4()
    end = time.time()
    print('dur time ', (end-start))