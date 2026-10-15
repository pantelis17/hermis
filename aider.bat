@REM  @echo off
@REM  set OLLAMA_API_BASE=http://127.0.0.1:11434
@REM  python -m uv tool run --python 3.12 --from aider-chat aider --model ollama_chat/qwen2.5-coder:7b %*

@echo off
set OLLAMA_API_BASE=http://127.0.0.1:11434
set DEFAULT_MODEL=ollama_chat/qwen3.6:35b-a3b-q4_K_M

python -m uv tool run --python 3.12 --from aider-chat aider --model %DEFAULT_MODEL% %*