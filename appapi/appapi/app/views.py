from django.shortcuts import render
from django.http import HttpResponse,JsonResponse
from django.http import request
from app.generate import generate
import json
# Create your views here.

def receive_request(request):
    if request.method == 'GET':
        data = {
            'name':'steven',
            'age':'87',
            'status':'alive',
        }
        return JsonResponse(data)
    elif request.method == 'POST':
        
        data = generate(request)
        
        #data = request.POST
        #jd = json.loads(request.body)
        #print(request.POST)
        return JsonResponse(data)
        
    else:
        data = {
            'status':'method error!'
        }
        return JsonResponse(data)
def root_dict(requests):
    return HttpResponse("Hello World!")