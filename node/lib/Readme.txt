Copy and paster following script into nginx-x-x/conf/nginx.conf file.

location /lifestream/api {
    proxy_pass http://127.0.0.1:3009/lifestream/api;
}



==> Start nginx
C:\Dev\nginx-1.8.0>start nginx

==> Stop nginx
C:\Dev\nginx-1.8.0>nginx -s stop

