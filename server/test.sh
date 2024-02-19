#!/bin/bash
curl -X POST -H "Content-Type: multipart/form-data" -F "field1=<h1>value1</h1>" -F "field2=<h1>value1</h1>" -F "file1=@/home/andrey/images/cat2.jpg" -F "file2=@/home/andrey/images/cat2.jpg" http://example.com/api/v1/uploads
