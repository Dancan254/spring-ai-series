# Getting Started with Spring AI

Introduces the core `ChatClient` API with four fundamental endpoint patterns covering the most common ways to interact with a language model in Spring AI.

## Endpoints

| Endpoint | Description |
|---|---|
| `GET /chat` | Returns a plain string response. Accepts a `?message=` param (default: `"Tell me a joke"`). |
| `GET /chat/system` | Same as above but includes a system prompt to shape model behavior. |
| `GET /chat/stream` | Streams the response token-by-token as Server-Sent Events (`text/event-stream`). |
| `GET /chat/entity` | Returns a structured `Answer` record with `answer`, `language`, and `funFact` fields using Spring AI's entity mapping. |

## Stack

- Spring Boot 4
- Spring AI 2.0
- Ollama (`llama3.2`)

## Running locally

1. Install and start [Ollama](https://ollama.com), then pull the model:
   ```bash
   ollama pull llama3.2
   ```
2. Start the application:
   ```bash
   ./mvnw spring-boot:run
   ```
3. Try an endpoint:
   ```bash
   curl "http://localhost:8080/chat?message=What+is+Java"
   curl "http://localhost:8080/chat/stream?message=Tell+me+a+story"
   curl "http://localhost:8080/chat/entity?message=What+is+the+capital+of+France"
   ```
