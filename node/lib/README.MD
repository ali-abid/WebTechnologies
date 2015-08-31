
Note for nginx: 
Copy and paste following code into C:\nginx-1.8.0\conf>nginx.conf file.

location /lifestream/api {
    proxy_pass http://127.0.0.1:3009/lifestream/api;
}


