import sys
import json
import requests

filename = sys.argv[1]
url = 'https://194e8462.ngrok.io/'


def api_post():
    imgurl = url + 'post_img/' + filename + '.jpg'
    payload = {
    'api_key':'Qc5frB4ioF8sih7NSwBW43HvFLQ3rVan',
    'api_secret':'PKpVZDZR0lu1JWn4o5wZ5Xc7KZDFc5ke',
    'image_url':imgurl,
    'return_attributes':'smiling'
    }
    det_url = 'https://api-cn.faceplusplus.com/facepp/v3/detect'
    res = requests.post(det_url,data = payload)
    jd = json.loads(res.text)

    payload1 = {
    'api_key':'Qc5frB4ioF8sih7NSwBW43HvFLQ3rVan',
    'api_secret':'PKpVZDZR0lu1JWn4o5wZ5Xc7KZDFc5ke',
    'image_url1':imgurl,
    'image_url2':url + 'post_img/' + 'me.jpg'
    }
    com_url = 'https://api-cn.faceplusplus.com/facepp/v3/compare'
    res1 = requests.post(com_url,data = payload1)
    jd1 = json.loads(res1.text)
    rp = {
        'smile':jd['faces'][0]['attributes']['smile']['value'],
        'compare':jd1['confidence']
    }
    with open('api_output.txt','w') as f:
        f.write(str(rp).replace('\'','\"'))

if __name__ == '__main__':
    api_post()