#!/bin/bash
set -e

echo "Building project..."
./gradlew generateJavaScript generateWasmGC

echo "Starting server..."
./gradlew run >server.log 2>&1 &
SERVER_PID=$!
GITHUB_ENV=${GITHUB_ENV:-/tmp/env.txt}
echo "SERVER_PID=$SERVER_PID" >>$GITHUB_ENV

echo "Waiting for server (max 60 seconds)..."
MAX_ATTEMPTS=30
for i in $(seq 1 $MAX_ATTEMPTS); do
	sleep 2

	# Проверяем, жив ли процесс
	if ! kill -0 $SERVER_PID 2>/dev/null; then
		echo "Server process died!"
		echo "=== Server Log ==="
		cat server.log
		exit 1
	fi

	# Проверяем HTTP
	HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8080 2>/dev/null || echo "000")
	if [[ "$HTTP_CODE" =~ ^(200|304|404|403)$ ]]; then
		echo "Server is running! (HTTP $HTTP_CODE)"
		exit 0
	fi

	echo "Attempt $i/$MAX_ATTEMPTS - HTTP $HTTP_CODE"
done

echo "Server failed to start"
echo "=== Server Log ==="
cat server.log
exit 1
