FROM node:20-alpine AS build
WORKDIR /app

COPY package.json pnpm-lock.yaml ./
RUN corepack enable && pnpm install --frozen-lockfile --prefer-offline

COPY . .
RUN pnpm run build          # Vite кладёт result в /app/dist

FROM nginx:1.27-alpine
WORKDIR /usr/share/nginx/html

COPY --from=build /app/dist/ .

RUN printf '%s\n' \
  'server {' \
  '  listen 80;' \
  '  root /usr/share/nginx/html;' \
  '  index index.html;' \
  '' \
  '  location /api/ {' \
  '    proxy_pass http://backend:8080;' \
  '    proxy_set_header Host $host;' \
  '    proxy_set_header X-Real-IP $remote_addr;' \
  '    proxy_connect_timeout 300s;' \
  '    proxy_send_timeout    300s;' \
  '    proxy_read_timeout    300s;' \
  '  }' \
  '' \
  '  location / {' \
  '    try_files $uri /index.html;' \
  '  }' \
  '}' \
  > /etc/nginx/conf.d/default.conf

EXPOSE 80