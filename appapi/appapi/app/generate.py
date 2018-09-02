import json
import datetime
import requests
import subprocess


url = 'https://194e8462.ngrok.io/'




imgres = []
def generate(request):
    pdata = request.POST
    if pdata['intent'] == 'activity':
        data = {'index': [1, 2, 3, 4, 5, 6, 7, 8, 9],
        'columns': [0, 1, 2, 3, 4, 5, 6],
        'data': [['項次', '活動名稱', '日期', '時間', '地點', '講師', '費用'],
        ['1',
        '花草樂趣  好生活',
        '7/31至9/11  計八堂課  (星期二)',
        '上午10時  至12時',
        '台灣大道二段659號  4樓4-2教室',
        '王麗卿  講師',
        '課程費用  1200元整'],
        ['2',
        '烏克麗麗  進階班',
        '8/07至11/20  計十六堂課  (星期二)',
        '下午2時  至4時',
        '台灣大道二段659號  12樓12-1教室',
        '林欣慧  講師',
        '報名費  1000元整'],
        ['3',
        '樂齡體適能',
        '9/12至11/21  計十堂課  (星期三)',
        '上午10時  至  12時',
        '台灣大道二段659號  12樓12-1教室',
        '岳一萍  講師',
        '報名費  1000元整'],
        ['4',
        '腦力激盪  智慧無限',
        '9/05至11/14  計十堂課  (星期三)',
        '下午2時  至4時',
        '台灣大道二段659號  4樓4-3教室',
        '黃淑芬  講師',
        '保證金500整'],
        ['5',
        '烏克麗麗',
        '9/28至11/30  計十堂課  (星期五)',
        '上午10時  至12時',
        '台灣大道二段659號  4樓4-2教室',
        '鄭美秀  講師',
        '報名費  1000元整'],
        ['6',
        '香氛手作  保養',
        '7/20至9/07  計十堂課  (星期五)',
        '上午10時  至12時',
        '台灣大道二段659號  4樓4-2教室',
        '簡湘瓈  講師',
        '課程費用  1600元整'],
        ['7',
        '素描輕鬆學',
        '9/08至11/10  計十堂課  (星期六)',
        '下午2時  至4時',
        '台灣大道二段659號  4樓4-2教室',
        '朱意萍  講師',
        '報名費  1000元整'],
        ['8',
        '日文讀書會',
        '9/12、9/26  (星期三)',
        '下午2時  至4時',
        '台灣大道二段659號  4樓4-2教室',
        '讀書會  小老師',
        '限讀書會  成員參加']]}
    elif pdata['intent'] == 'photo':
        filename = datetime.datetime.now().strftime('%Y%m%d%H%M%S')
        with open('./post_img/' + filename + '.jpg','wb') as f:
            for chunk in request.FILES['img'].chunks():
                f.write(chunk)
        data = {'s':'ok'}
        subprocess.Popen('python /Users/steven/Desktop/appapi/appapi/app/faceapi.py {}'.format(filename), shell=True)
    elif pdata['intent'] == 'photo_upload_test':
        filename = datetime.datetime.now().strftime('%Y%m%d%H%M%S')
        with open('./post_img/' + filename + '.jpg','wb') as f:
            for chunk in request.FILES['img'].chunks():
                f.write(chunk)
        data = {'s':'success'}
    elif pdata['intent'] == 'check':
        res = ''
        with open('api_output.txt','r+') as f:
            res = f.read()
        data = json.loads(res)
    return data