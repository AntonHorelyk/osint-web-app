FROM gradle:8.14.0-jdk21-alpine AS build
WORKDIR /usr/src/app
COPY . .
RUN gradle --no-daemon clean shadowJar && ls -R /usr/src/app

FROM openjdk:21-jdk-slim

RUN apt-get update && apt-get install -y --no-install-recommends \
    python3 python3-venv python3-pip git curl unzip ca-certificates \
    libnss3 libatk1.0-0 libatk-bridge2.0-0 libcups2 libxcomposite1 \
    libxdamage1 libxrandr2 libgbm1 libasound2 libpangocairo-1.0-0 \
    libxshmfence1 libxkbcommon0 libwayland-client0 libwoff1 \
    libharfbuzz0b libharfbuzz-icu0 && rm -rf /var/lib/apt/lists/*

RUN set -eux; \
    ARCH=$(uname -m); \
    case "$ARCH" in x86_64|amd64) PKG="amass_Linux_amd64.zip" ;; \
           aarch64|arm64) PKG="amass_Linux_arm64.zip" ;; \
           *) exit 1 ;; \
    esac; \
    VER=$(curl -sSL https://api.github.com/repos/owasp-amass/amass/releases/latest \
         | grep '"tag_name":' | head -n1 | cut -d'"' -f4); \
    curl -sSL -o /tmp/amass.zip \
         "https://github.com/owasp-amass/amass/releases/download/${VER}/${PKG}"; \
    unzip -q /tmp/amass.zip -d /tmp; \
    mv /tmp/*/amass /usr/local/bin/; \
    chmod +x /usr/local/bin/amass; \
    rm -rf /tmp/*

RUN python3 -m venv /opt/venv && . /opt/venv/bin/activate && \
    pip install --upgrade pip && \
    pip install git+https://github.com/laramies/theHarvester.git@4.7.1 && \
    ln -s /opt/venv/bin/theHarvester /usr/local/bin/theHarvester

    RUN mkdir -p /root/.theHarvester && printf '{"http":[],"https":[]}' > /root/.theHarvester/proxies.yaml

WORKDIR /app

COPY --from=build /usr/src/app/build/libs/*.jar /app/backend.jar

ENTRYPOINT ["java","-jar","/app/backend.jar"]