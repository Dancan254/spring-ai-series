# Testing Multi-Model Demo

A multi-model AI assistant that intelligently routes questions to specialized LLM endpoints based on question type.

## What It Does

The app classifies incoming questions and routes them to the appropriate model:
- **FAST** (Groq): Factual, concise answers using `llama-3.1-8b-instant`
- **CODE** (OpenAI): Expert code advice using `gpt-4o`
- **REASONING** (OpenAI): Analytical, step-by-step responses using `gpt-4o`

## Prerequisites

1. **API Keys**
   - OpenAI API key (for code & reasoning models)
   - Groq API key (for fast model)

2. **Environment Setup**
   ```bash
   export OPENAI_API_KEY=sk-...
   export GROQ_API_KEY=gsk-...
   ```

## Running the App

```bash
cd multi-model-demo
./mvnw spring-boot:run
```

Server starts on `http://localhost:8080`

## Testing the Endpoint

**Endpoint:** `POST /api/v1/assistant/ask`  
**Parameter:** `question` (required)

### Test Cases

#### 1. Fast Response (Factual Question)
```bash
curl -X POST "http://localhost:8080/api/v1/assistant/ask?question=What+is+Java"
```
**Expected:** Routes to FAST model (Groq), concise answer

#### 2. Code Response (Programming Question)
```bash
curl -X POST "http://localhost:8080/api/v1/assistant/ask?question=How+do+I+implement+a+singleton+in+Java"
```
**Expected:** Routes to CODE model (OpenAI), detailed code example

#### 3. Code with Error (Debugging)
```bash
curl -X POST "http://localhost:8080/api/v1/assistant/ask?question=What+causes+NullPointerException+in+Spring"
```
**Expected:** Routes to CODE model, debugging advice

#### 4. Reasoning Response (Analytical Question)
```bash
curl -X POST "http://localhost:8080/api/v1/assistant/ask?question=Compare+SQL+vs+NoSQL+databases+and+trade-offs"
```
**Expected:** Routes to REASONING model (OpenAI), step-by-step analysis

#### 5. Should/Why Questions
```bash
curl -X POST "http://localhost:8080/api/v1/assistant/ask?question=Should+I+use+microservices+for+my+startup"
```
**Expected:** Routes to REASONING model, strategic thinking

## Response Format

```json
{
  "role": "CODE",
  "response": "...",
  "latencyMs": 1234
}
```

- **role**: Which model handled the question (FAST, CODE, or REASONING)
- **response**: The model's answer
- **latencyMs**: Execution time in milliseconds

## Classification Keywords

The router uses keyword matching to classify questions:

| Role | Keywords |
|---|---|
| CODE | code, function, class, method, bug, error, exception, java, spring, python, sql, algorithm, refactor, stacktrace |
| REASONING | why, compare, trade-off, pros and cons, analyze, strategy, decide, which is better |
| FAST | (default, if no keywords match) |

## Logs

View routing decisions in application logs:
```
INFO com.javaguy.multimodeldemo.service.SmartAssistantService : 
  routing question to role CODE | question = How do I fix this NullPointerException?
```

## Troubleshooting

- **401 Invalid API Key**: Check `OPENAI_API_KEY` and `GROQ_API_KEY` environment variables
- **Model not found**: Ensure OpenAI account has access to `gpt-4o` model
- **Slow responses**: Groq is faster; OpenAI models may have higher latency
