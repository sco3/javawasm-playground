const { chromium } = require('playwright');
const fs = require('fs');

(async () => {
  console.log('Starting UI test...');

  const browser = await chromium.launch({
    headless: true,
    args: ['--no-sandbox']
  });

  const page = await browser.newPage();

  // Set up network request monitoring
  const requests = [];
  page.on('response', (response) => {
    if (response.url().includes('/api/') || response.url().includes('/data')) {
      requests.push({
        url: response.url(),
        status: response.status(),
        ok: response.ok()
      });
    }
  });

  try {
    console.log('Opening page...');
    await page.goto('http://localhost:8080', {
      waitUntil: 'networkidle',
      timeout: 5000
    });

    console.log('Waiting for button...');
    await page.waitForSelector('#loadBtn', { timeout: 5000 });

    await page.screenshot({ path: 'screenshot-before.png' });
    console.log('Screenshot before click saved');

    console.log('Clicking button...');
    
    // Click and wait for the network response

    await page.click('#loadBtn');
    await page.waitForTimeout(2000);
    const outputText = await page.textContent('#output');
    if (outputText.startsWith("Hello from Java Backend!")) {
      console.log("✅ SUCCESS!");
    }

    const outputText = await page.textContent('#output');
    console.log(`Final response received: "${outputText}"`);

    await page.screenshot({ path: 'screenshot-after.png' });
    console.log('Screenshot after click saved');

    const expectedText = "Hello from Java Backend!";

    if (outputText.includes(expectedText)) {
      console.log(`SUCCESS: Response contains "${expectedText}"`);
    } else {
      console.error(`FAIL: Expected "${expectedText}", got "${outputText}"`);
      process.exit(1);
    }

    // Additional network request check
    if (requests.length > 0) {
      console.log('All network requests captured:', JSON.stringify(requests, null, 2));
      const allOk = requests.every(r => r.ok);
      if (!allOk) {
        console.error('Some network requests failed');
        process.exit(1);
      }
    }

    console.log('All tests passed!');

  } catch (error) {
    console.error('Test failed:', error.message);

    try {
      await page.screenshot({ path: 'screenshot-error.png' });
      console.log('Error screenshot saved');
      
      // Get the current content for debugging
      const currentContent = await page.textContent('#output').catch(() => 'Element not found');
      console.log(`Current output content: "${currentContent}"`);
      
    } catch (e) {}

    process.exit(1);
  } finally {
    await browser.close();
  }
})();