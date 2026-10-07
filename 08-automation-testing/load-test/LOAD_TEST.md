# Load test summary (Übung 3)

## Tool: k6
Chosen because tests are plain JavaScript files that can live in the repo, it runs from the CLI with one command, and thresholds make the result a pass/fail check.

Features used: ramp-up/ramp-down via `stages`, assertions via `check`, pass/fail criteria via `thresholds`, and a built-in end-of-run report (requests/s, avg, p95, error rate).

## Scenario
`load-test/students.js`: each virtual user calls `GET /students`, then sleeps 1 s. About 10% of iterations also `POST /students`, so the database grows during the test. Ramp-up 10 s, steady load, ramp-down 5 s.
Thresholds: error rate < 1%, p95 < 500 ms.

## Results
k6 v2.3.0, backend locally on macOS (Apple Silicon), H2 in-memory, `k6 run -e VUS=<n> load-test/students.js`:

| VUs | Duration | Requests | Requests/s | Avg (ms) | p95 (ms) | Max (ms) | Error rate | Thresholds |
|-----|----------|----------|-----------|----------|----------|----------|------------|------------|
| 10  | 30s      | 424      | 9.3       | 4.96     | 7.93     | 20.75    | 0.00%      | passed     |
| 50  | 30s      | 2090     | 45.8      | 2.35     | 5.69     | 43.69    | 0.00%      | passed     |
| 200 | 30s      | 8261     | 180.3     | 6.12     | 19.96    | 165.61   | 0.00%      | passed     |

All checks succeeded (804, 3985 and 15791). After the three runs the `students` table contained 976 rows.

## Findings
1. **Requests/s ≈ number of VUs.** Each VU sleeps 1 s per iteration, so this test measures latency at a fixed load, not the maximum capacity. To find the real limit, remove the sleep or use many more VUs.
2. **First sign of load at 200 VUs.** p95 rises from about 6 ms to about 20 ms and the maximum reaches 165 ms. That is still far below the 500 ms threshold.
3. **10 VUs was slower than 50 VUs** (avg 4.96 ms vs. 2.35 ms). It was the first run, so the JVM was still warming up (JIT). Run a warm-up before measuring.
4. **The table keeps growing.** After the tests there were 976 rows. `GET /students` returns all rows with no paging, so the response keeps getting bigger. This is a scaling risk for long-running load.

## Conclusion
At up to 200 constant users the backend is fast and stable, so no real limit was reached. The numbers say more about the test design (sleep, cold JVM) than about capacity. The bigger risk is not user count but the unpaged `GET /students` on a table that only grows. Next steps: add paging, run a warm-up first, and test without sleep to find the actual limit.
