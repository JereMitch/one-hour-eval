# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Repository layout

Two sibling projects, each with its own toolchain — there is no top-level package manager or build system.

- `backend/` — Spring Boot 3.5.14 / Java 17 / Maven (use the wrapper: `./mvnw.cmd` on Windows, `./mvnw` elsewhere).
- `frontend/` — Vite 8 / React 19 / TypeScript. Jest 29 (with ts-jest) for tests.

The frontend dev server proxies `/api/**` to `http://localhost:8080` (configured in `frontend/vite.config.ts`). Both processes run independently.

## Commands

Backend (run from `backend/`):
- `./mvnw.cmd test` — run all backend tests
- `./mvnw.cmd test -Dtest=ProductSelectorTest` — single test class
- `./mvnw.cmd test -Dtest=ProductSelectorTest#picksOneBeginnerOneExpertOneExtra` — single method
- `./mvnw.cmd spring-boot:run` — start API on :8080 (triggers Shopify ingest at startup)
- `./mvnw.cmd -DskipTests package` — build the jar without running tests

Frontend (run from `frontend/`):
- `npm run dev` — Vite dev server on :5173 with `/api` proxy to :8080
- `npm test` — run all Jest tests
- `npm test -- SearchBar` — single test file by name match
- `npm run build` — type-check (`tsc -b`) then production Vite build
- `npm run lint` — ESLint

## Architecture

### Data flow

```
Shopify /products.json → ShopifyIngestService (on startup) → H2 in-memory DB
                                                                   ↓
              Browser → Vite proxy → SearchController / RecommendController
                                                ↓
                                       ProductRepository
                                                ↓ (RecommendController only)
                                       ProductSelector  →  RecommendationService  →  response
                                       (1 BEGINNER + 1   (Mock OR Claude, behind
                                        EXPERT + 1 best)  @ConditionalOnProperty)
```

### Backend non-obvious pieces

- **Pluggable recommendation service** (`backend/src/main/java/com/example/widget/recommend/`): `RecommendationService` has two implementations gated by `@ConditionalOnProperty(name = "app.llm.provider", ...)`. Default is `mock` (templated string); `claude` swaps in `ClaudeRecommendationService` (raw HTTP to `https://api.anthropic.com/v1/messages` via `RestTemplate`, no SDK dependency). Switching providers means changing one property — no code change.
- **Random level assignment**: Shopify's `/products.json` has no Beginner/Expert field, so `ShopifyIngestService.toProduct(...)` randomly assigns one at ingest time. The 1-Beginner+1-Expert+1-best selection (`ProductSelector.selectThree`) only works because of this.
- **Ingest happens once on startup** via `ApplicationRunner`. No re-ingest endpoint. Restart the backend to pull fresh data. Failures are logged and swallowed so the app still boots with an empty DB.
- **Test-scoped properties** (`backend/src/test/resources/application.properties`) override the production Shopify URL with a `REPLACE_ME` placeholder so `@SpringBootTest` doesn't make real network calls during `WidgetApplicationTests`. The ingest service guards on this string.
- **Single `RestTemplate` bean** lives in `WidgetApplication` and is injected into both the ingest service and the Claude client.

### Frontend non-obvious pieces

- **Single API call** for the main flow: `SearchContainer` calls `/api/recommend?q=...` which returns both the chosen products and the recommendation in one response. The `/api/products/search` endpoint exists for the spec but isn't used by the UI.
- **Jest config quirk** (`frontend/jest.config.cjs`): the project is ESM (`"type": "module"`) so Jest config is `.cjs`. ts-jest gets an inline tsconfig (CommonJS module, jsx: react-jsx, types: jest+node) — separate from `tsconfig.app.json` which is bundler-mode for Vite. Test files are excluded from `tsconfig.app.json` so `npm run build` doesn't trip on Jest globals.
- **`@testing-library/jest-dom` is imported per-test-file** rather than via a `setupFilesAfter*` hook — chosen for simplicity after the standard config key was rejected.

## Configuration that matters

`backend/src/main/resources/application.properties`:
- `shopify.store-url` — base URL of a public Shopify storefront. The ingest appends `/products.json?limit=250`. Currently `https://kith.com`.
- `app.llm.provider` — `mock` (default) or `claude`.
- `anthropic.api-key` — read from `ANTHROPIC_API_KEY` env var; required only when `app.llm.provider=claude`.
- `anthropic.model` — defaults to `claude-haiku-4-5`.
