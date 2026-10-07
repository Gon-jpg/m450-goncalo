import { defineConfig } from '@playwright/test';

export default defineConfig({
  testDir: './e2e',
  reporter: [['html', { open: 'never' }], ['list']],
  use: {
    baseURL: 'http://localhost:4200',
    screenshot: 'on',
    trace: 'retain-on-failure',
  },
});
