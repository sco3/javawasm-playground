const { chromium } = require('playwright');
const fs = require('fs');

(async () => {
  console.log('Starting UI test...');

  // Запускаем браузер
  const browser = await chromium.launch({
    headless: true // В GitHub Actions обязательно headless режим
  });

  const page = await browser.newPage();

  try {
    // 1. Открываем страницу
    console.log('Opening page...');
    await page.goto('http://localhost:8080', {
      waitUntil: 'networkidle',
      timeout: 5000
    });

    // 2. Ждём загрузки кнопки
    console.log('Waiting for button...');
    await page.waitForSelector('#loadBtn', { timeout: 5000 });

    // 3. Делаем скриншот ДО нажатия
    await page.screenshot({ path: 'screenshot-before.png' });
    console.log('Screenshot before click saved');

    // 4. Нажимаем на кнопку
    console.log('Clicking button...');
    await page.click('#loadBtn');

    // 5. Ждём появления результата в div#output
    console.log('Waiting for response...');
    await page.waitForSelector('#output:not(:empty)', { 
      timeout: 10000 
    });

    // 6. Получаем текст ответа
    const outputText = await page.textContent('#output');
    console.log(`Response received: "${outputText}"`);

    // 7. Делаем скриншот ПОСЛЕ нажатия
    await page.screenshot({ path: 'screenshot-after.png' });
    console.log('Screenshot after click saved');

    const expectedText = "Hello from Java Backend!"; 

    if (outputText.includes(expectedText)) {
      console.log(`SUCCESS: Response contains "${expectedText}"`);
    } else {
      console.error(`FAIL: Expected "${expectedText}", got "${outputText}"`);
      process.exit(1);
    }
    
    // 9. Дополнительная проверка - статус ответа через network
    console.log('Checking network request...');
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
    
    // Повторно нажимаем для захвата network
    await page.click('#loadBtn');
    await page.waitForTimeout(2000);
    
    if (requests.length > 0) {
      console.log('Network requests captured:', JSON.stringify(requests, null, 2));
      const allOk = requests.every(r => r.ok);
      if (!allOk) {
        console.error('Some network requests failed');
        process.exit(1);
      }
    }
    
    console.log('All tests passed!');
    
  } catch (error) {
    console.error('Test failed:', error.message);

    // Делаем скриншот ошибки
    try {
      await page.screenshot({ path: 'screenshot-error.png' });
      console.log('Error screenshot saved');
    } catch (e) {}
    
    process.exit(1);
  } finally {
    await browser.close();
  }
})();