const CACHE_NAME = `temperature-converter-v1`;

// Use the install event to pre-cache all initial resources.
self.addEventListener('install', event => {
    event.waitUntil((async () => {
        const cache = await caches.open(CACHE_NAME);
        cache.addAll([
            '/',
            '/js/cashexpenses.js',
            '/js/default.js',
            '/js/recycle.js',
            '/js/tablelist.js',
            '/js/timeworks.js',
            '/css/item/button.css',
            '/css/item/color.css',
            '/css/item/default.css',
            '/css/item/dialog.css',
            '/css/item/input.css',
            '/css/item/list.css',
            '/css/item/tab.css',
            '/css/item/table.css',
            '/css/index.css',
            '/css/tablelist.css'
        ]);
    })());
});

self.addEventListener('push', function (event) {
    console.log("✅ [Service Worker] Push event received!"); // ← ここで確認
    
    const data = event.data ? event.data.text() : '通知内容なし';
    console.log(data)

    event.waitUntil(
        self.registration.showNotification('お知らせ', {
            body: data,
            icon: '/icons/info.png' // 任意のアイコン
        })
    );
});

// プッシュ通知クリック時にGoogleに遷移させる
self.addEventListener('notificationclick', () => {
    clients.openWindow('https://www.kyouseipro.com/')
});
