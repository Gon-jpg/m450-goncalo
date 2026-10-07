import http from 'k6/http';
import { check, sleep } from 'k6';

const BASE = __ENV.BASE_URL || 'http://localhost:8081';
const VUS = parseInt(__ENV.VUS || '20');
const DURATION = __ENV.DURATION || '30s';

export const options = {
  stages: [
    { duration: '10s', target: VUS },
    { duration: DURATION, target: VUS },
    { duration: '5s', target: 0 },
  ],
  thresholds: {
    http_req_failed: ['rate<0.01'],
    http_req_duration: ['p(95)<500'],
  },
};

export default function () {
  const get = http.get(`${BASE}/students`);
  check(get, {
    'GET status 200': (r) => r.status === 200,
    'GET returns a list': (r) => Array.isArray(r.json()),
  });

  if (Math.random() < 0.1) {
    const post = http.post(
      `${BASE}/students`,
      JSON.stringify({ name: `load-${__VU}-${__ITER}`, email: `load-${__VU}-${__ITER}@example.com` }),
      { headers: { 'Content-Type': 'application/json' } },
    );
    check(post, { 'POST status 200': (r) => r.status === 200 });
  }

  sleep(1);
}
