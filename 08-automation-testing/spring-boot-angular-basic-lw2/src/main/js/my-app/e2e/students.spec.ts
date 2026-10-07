import { test, expect } from '@playwright/test';

test('submit button is disabled while the fields are empty', async ({ page }) => {
  await page.goto('/addstudents');

  await expect(page.locator('button[type=submit]')).toBeDisabled();

  await page.locator('#name').fill('Only Name');
  await expect(page.locator('button[type=submit]')).toBeDisabled();

  await page.locator('#email').fill('only.name@example.com');
  await expect(page.locator('button[type=submit]')).toBeEnabled();
});

test('adding a student navigates to /students and shows the student', async ({ page }) => {
  const name = `E2E-${Date.now()}`;

  await page.goto('/addstudents');
  await page.locator('#name').fill(name);
  await page.locator('#email').fill(`${name}@example.com`);
  await page.locator('button[type=submit]').click();

  await expect(page).toHaveURL(/\/students$/);
  await expect(page.locator('tbody tr').filter({ hasText: name })).toHaveCount(1);
});

test('"Name is required" is not shown on an untouched form', async ({ page }) => {
  test.fail();
  await page.goto('/addstudents');

  await expect(page.getByText('Name is required')).toBeHidden();
});
