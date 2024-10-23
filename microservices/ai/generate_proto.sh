#!/bin/bash

PROTO_SRC=../../common/src/main/proto
PROJECT_ROOT=./
PROTO_PATH=proto
PROTO_OUT_PATH=protoc

rm -rf $PROTO_OUT_PATH
cp -r $PROTO_SRC $PROJECT_ROOT

[ ! -d "$PROTO_OUT_PATH"  ] && mkdir "$PROTO_OUT_PATH"

find $PROTO_PATH -name "*.proto" | while read -r PROTO_FILE; do
    python3.13 -m grpc_tools.protoc \
    --proto_path=./$PROTO_PATH \
    --python_out=./$PROTO_OUT_PATH \
    --pyi_out=./$PROTO_OUT_PATH \
    --grpc_python_out=./$PROTO_OUT_PATH \
    ./"$PROTO_FILE"

    PROTO_DIR="$PROTO_OUT_PATH"/$(dirname "${PROTO_FILE#"$PROTO_PATH"}")
    if [[ ! -e /"$PROTO_DIR"/__init__.py ]]; then
      touch "$PROTO_DIR"/__init__.py
      echo "import os, sys

sys.path.append(os.path.dirname(os.path.realpath(__file__)))" > "$PROTO_DIR"/__init__.py
    fi

done

rm -rf $PROTO_PATH

echo "Generated gRPC protocol python files completed!"