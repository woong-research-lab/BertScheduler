#-*- coding:utf-8 -*-

# urllib3 라이브러리 설치 필요
# pip install urllib3
import urllib3
import json

if __name__ == '__main__' :
    TBAI_API_URL = "http://122.32.43.13:6666/mrc"
    ACCESS_KEY = "kuvBCweSsRkFu6EfcgPxG6dMNxh7nL78APgMARBymUtVN4ywdWq2xzL3TrGcQ7Xh"

    context = ["읽기가 어렵다. 난독(難讀)이다. 읽어도 읽히지 않으니 잘못 읽는다. 오독(誤讀)이다. 대한민국 인터넷판은 난독자들의 오독으로 시끌벅적하다. 부산에 사는 함모(29)씨는 최근 가상 화폐에 2000만원을 투자했다. 그는 인터넷 난독자들 때문에 하루에도 몇 번씩 가슴이 철렁한다. '비트코인 커뮤니티에 하루에도 수십 번씩 '거래소가 완전히 폐쇄된다'나 '중국 정부가 암호화 화폐 생산자들을 처벌한다더라'는 식의 글이 올라와요. 놀라서 글을 읽어보면 기사를 잘못 이해한 경우가 많아요. 안 그래도 뉴스 하나에 코인 가격이 뒤흔들리는데, 인터넷 난독자들이 나르는 가짜 뉴스 때문에 울화통 터진 적이 한두 번이 아닙니다.' 같은 글을 읽어도 하는 말이 다르다. 글 읽기가 익숙하지 않은 탓에 글을 제대로 이해하지 못하거나, 글을 제대로 읽지도 않고 스스로 이해한 것으로 착각한다. 덕분에 인터넷에서는 '세 줄 요약'이라는 독특한 예절 문화까지 생겨났다. 긴 글을 올리면 이해하지 못할 것이 뻔하니, 인터넷 커뮤니티나 페이스북에 장문을 올릴 땐 세 줄 이하로 글 전체를 요약해 앞에 달아주는 게 일종의 매너가 됐다."]
    questions = ["읽기가 어려운 것은?", "장문을 올리는 곳은?", "독특한 예절 문화는?"]

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
