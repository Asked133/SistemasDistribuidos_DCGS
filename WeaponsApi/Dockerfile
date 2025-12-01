FROM python:3.11-slim

WORKDIR /app

# Install build dependencies
RUN apt-get update && apt-get install -y --no-install-recommends \
    gcc \
    && rm -rf /var/lib/apt/lists/*

# Copy requirements and install Python dependencies
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Copy proto files and generate Python code
COPY protos/ protos/
RUN python -m grpc_tools.protoc -I protos --python_out=protos --pyi_out=protos --grpc_python_out=protos protos/diablo_inventory.proto

# Copy the server code
COPY server.py .

EXPOSE 50051

CMD ["python", "server.py"]
