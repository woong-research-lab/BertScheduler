



from flask import Flask
from flask_restful import Resource, Api
from flask_restful import reqparse
import werkzeug
from mrc_api import BertMRC

app = Flask(__name__)
api = Api(app)

class TrainData(Resource):
    def post(self):
        parser = reqparse.RequestParser()
        print('parser = ', parser)
        return {'status': 'success'}


class QueryData(Resource):
    def post(self):
        parser = reqparse.RequestParser()
        print('parser = ', parser)

        parser.add_argument('context', type=str)
        parser.add_argument('question', type=str)
        args = parser.parse_args()

        context = args['context']
        question = args['question']
        print('context = ', context)
        print('question = ', question)
        bert = BertMRC()
        response = bert.predict(context, question)

        return {'status': 'success', 'response': response}


class UploadData(Resource):
    def post(self):
        parser = reqparse.RequestParser()
        parser.add_argument('file', type=werkzeug.datastructures.FileStorage, location='files')
        args = parser.parse_args()
        upload_file = args['file']
        print('upload_file = ', upload_file.filename)
        upload_file.save(upload_file.filename)
        return {'status': 'success'}


api.add_resource(TrainData, '/train_data')
api.add_resource(QueryData, '/query_data')
api.add_resource(UploadData, '/upload_data')


if __name__ == '__main__':
    #app.run(debug=True)
    app.run(debug=True, host='0.0.0.0')